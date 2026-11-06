#version 330

#moj_import <rose:defaults.glsl>

in vec2 Size;
in vec4 Colors;
in vec4 Radius;
in vec2 FragCoord;
in float Thickness;

out vec4 fragColor;

void main() {
    vec2 center = Size * 0.5;
    float dist = rdist(center - (FragCoord.xy * Size), center - 1.0, Radius);
    float alpha = smoothstep(1.0 - Thickness - 3.0,
                             1.0 - Thickness - 1.5, dist);
    alpha *= 1.0 - smoothstep(1.0 - 1.5, 1.0, dist);

    vec4 color = vec4(Colors.rgb, Colors.a * alpha);

    fragColor = color;
}