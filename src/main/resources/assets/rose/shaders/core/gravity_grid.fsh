#version 330

layout(std140) uniform GravityGrid {
    vec4 ResolutionAlpha;
    vec4 AccentTop;
    vec4 AccentBottom;
};

in vec2 TexCoord;
out vec4 fragColor;

float sat(float v) {
    return clamp(v, 0.0, 1.0);
}

float lineGrid(vec2 p, float spacing, float width) {
    vec2 cell = abs(fract(p / spacing) - 0.5) * spacing;
    float d = min(cell.x, cell.y);
    return 1.0 - smoothstep(width, width + 1.35, d);
}

void main() {
    vec2 resolution = max(ResolutionAlpha.xy, vec2(1.0));
    vec2 pixel = vec2(TexCoord.x * resolution.x, (1.0 - TexCoord.y) * resolution.y);
    float reveal = 1.0 - exp(-pow(sat(ResolutionAlpha.z), 1.85) * 3.2);
    float minor = lineGrid(pixel, 50.0, 0.66);
    float major = lineGrid(pixel, 250.0, 0.96);
    float fine = lineGrid(pixel, 25.0, 0.26) * 0.10;
    float grid = max(minor * 0.62 + major * 0.74, fine);
    vec3 base = mix(AccentBottom.rgb, AccentTop.rgb, sat(0.43 + pixel.y / resolution.y * 0.18));
    vec3 cold = vec3(0.34, 0.78, 1.0);
    vec3 color = mix(cold, base, 0.62);
    fragColor = vec4(clamp(color, 0.0, 1.0), sat(grid * 0.12) * reveal);
}
