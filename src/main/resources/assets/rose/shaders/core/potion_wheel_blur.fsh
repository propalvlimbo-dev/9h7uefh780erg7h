#version 330

in vec2 FragCoord;
in vec2 TexCoord;
in vec2 Size;
in float Inner;
in float Outer;
in float Start;
in float Span;
in float Gap;
in float Feather;
in vec4 Color;

uniform sampler2D Sampler0;

out vec4 fragColor;

const float TAU = 6.28318530718;

void main() {
    vec2 point = vec2(FragCoord.y - 0.5, 0.5 - FragCoord.x) * Size;
    float radius = length(point);
    float angle = atan(point.y, point.x);
    float local = mod(angle - Start + TAU, TAU);
    float middle = (Inner + Outer) * 0.5;
    float halfThickness = max((Outer - Inner) * 0.5, 0.0);
    float halfArc = max(Span * middle * 0.5 - Gap, 0.0);
    float arc = (local - Span * 0.5) * middle;
    float rounding = min(halfThickness * 0.38, 12.0);
    vec2 bounds = vec2(max(halfThickness - rounding, 0.0), max(halfArc - rounding, 0.0));
    vec2 offset = abs(vec2(radius - middle, arc)) - bounds;
    float distance = length(max(offset, 0.0)) + min(max(offset.x, offset.y), 0.0) - rounding;
    float mask = 1.0 - smoothstep(-Feather * 0.5, Feather * 0.5, distance);
    if (mask <= 0.001) discard;

    vec2 pixel = 1.0 / textureSize(Sampler0, 0);
    vec3 average = texture(Sampler0, TexCoord).rgb;
    float total = 1.0;
    for (int ring = 1; ring <= 3; ring++) {
        float radiusSample = float(ring) * 3.5;
        for (int i = 0; i < 12; i++) {
            float angleSample = float(i) * TAU / 12.0;
            vec2 direction = vec2(cos(angleSample), sin(angleSample));
            average += texture(Sampler0, TexCoord + direction * pixel * radiusSample).rgb;
            total += 1.0;
        }
    }
    average /= total;
    fragColor = vec4(average * Color.rgb, Color.a * mask);
}
