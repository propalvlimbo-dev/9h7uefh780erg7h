#version 330

#moj_import <rose:defaults.glsl>

in vec2 FragCoord;

in vec2 Size;
in vec4 Radius;
in float Smoothness;
in float Thickness;

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

    vec2 center = Size * 0.5;
    float dist = rdist(center - (FragCoord.xy * Size), center - 1.0, Radius);
    float alpha = smoothstep(1.0 - Thickness - Smoothness - Smoothness,
                             1.0 - Thickness - Smoothness, dist);
    alpha *= 1.0 - smoothstep(1.0 - Smoothness, 1.0, dist);

    vec4 color = mix(mix(Color1, Color2, FragCoord.x), mix(Color3, Color4, FragCoord.x), FragCoord.y);

    color += mix(NOISE, -NOISE, fract(sin(dot(FragCoord.xy, vec2(12.9898, 78.233))) * 43758.5453));

    vec4 colors = vec4(color.rgb, color.a * alpha);

    if (colors.a == 0.0) {
        discard;
    }

    fragColor = colors;
}