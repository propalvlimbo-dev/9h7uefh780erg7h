#version 330

#moj_import <rose:defaults.glsl>

in vec2 FragCoord;
in vec2 TexCoord;

uniform sampler2D Sampler0;

in vec2 Size;
in vec4 Radius;
in float Smoothness;
in float BlurRadius;

in vec4 Color1;
in vec4 Color2;
in vec4 Color3;
in vec4 Color4;

in vec4 Scissor;
in float UseScissor;

out vec4 fragColor;

const float DPI = 6.28318530718;
const float STEP = DPI / 16.0;

#define NOISE 0.5 / 255.0

void main() {
    if (UseScissor == 1.0 && !insideScissor(FragCoord, Scissor)) {
        discard;
    }

    vec2 uv = gl_FragCoord.xy / vec2(textureSize(Sampler0, 0));
    vec2 multiplier = BlurRadius / textureSize(Sampler0, 0);

    vec3 average = texture(Sampler0, uv).rgb;
    for (float d = 0.0; d < DPI; d += STEP) {
        for (float i = 0.2; i <= 1.0; i += 0.2) {
            average += texture(Sampler0, uv + vec2(cos(d), sin(d)) * multiplier * i).rgb;
        }
    }
    average /= 80.0;

    vec4 color = mix(mix(Color1, Color2, FragCoord.x), mix(Color3, Color4, FragCoord.x), FragCoord.y);

    color += mix(NOISE, -NOISE, fract(sin(dot(FragCoord.xy, vec2(12.9898, 78.233))) * 43758.5453));

    vec4 colors = vec4(average, 1.0) * color;

    colors.a *= ralpha(Size, FragCoord, Radius, Smoothness);

    if (colors.a == 0.0) {
        discard;
    }

    fragColor = colors;
}
