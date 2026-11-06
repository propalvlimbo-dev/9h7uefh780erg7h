#version 330
layout(std140) uniform JumpCircleStyle {
    vec4 GlowColor;
    vec4 StyleParams;
};
in vec2 texCoord;
in vec4 vertexColor;
out vec4 fragColor;
void main() {
    vec2 uv = 1.5 * (2.0 * texCoord - vec2(1.0));
    float radius = length(uv);
    if (radius < 0.94 || radius > 1.34) discard;
    float core = 1.0 - smoothstep(0.0, 0.035, abs(radius - 1.0));
    float outer = radius >= 1.0 ? exp(-(radius - 1.0) * 11.0) : 0.0;
    outer *= 1.0 - smoothstep(1.22, 1.34, radius);
    float life = vertexColor.a * GlowColor.a;
    float intensity = clamp(core + outer * 0.72, 0.0, 1.0) * life;
    if (intensity <= 0.002) discard;
    vec3 color = mix(GlowColor.rgb, vec3(1.0), core * 0.82);
    fragColor = vec4(color * intensity, intensity);
}
