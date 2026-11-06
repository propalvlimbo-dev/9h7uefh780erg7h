#version 330

#moj_import <rose:defaults.glsl>

in vec2 FragCoord;
in vec2 TexCoord;

uniform sampler2D Sampler0;

in vec2 Size;
in vec4 Radius;
in float Smoothness;

in vec4 Color1;
in vec4 Color2;
in vec4 Color3;
in vec4 Color4;

in vec4 Scissor;
in float UseScissor;

out vec4 fragColor;

#define NOISE 0.5 / 255.0

void main() {
    if (UseScissor == 1.0 && !insideScissor(FragCoord, Scissor)) {
        discard;
    }

    float alpha = ralpha(Size, FragCoord, Radius, Smoothness);

    vec4 color = mix(mix(Color1, Color2, FragCoord.x), mix(Color3, Color4, FragCoord.x), FragCoord.y);

    color += mix(NOISE, -NOISE, fract(sin(dot(FragCoord.xy, vec2(12.9898, 78.233))) * 43758.5453));

    vec4 colors = vec4(1.0, 1.0, 1.0, alpha) * texture(Sampler0, TexCoord) * color;

    if (colors.a == 0.0) {
        discard;
    }

    fragColor = colors;
}