#version 330
layout(std140) uniform ShaderSkyData {
    vec4 screenTimeOpacity;
    vec4 primaryColor;
    vec4 secondaryColor;
    vec4 accentColor;
    vec4 params;
    vec4 extra;
};
in vec3 skyDir;
out vec4 fragColor;
float hash(vec3 p) {
    p = fract(p * vec3(123.34, 456.21, 321.79));
    p += dot(p, p.yzx + 45.32);
    return fract((p.x + p.y) * p.z);
}
float noise(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    vec3 u = f * f * (3.0 - 2.0 * f);
    float n000 = hash(i + vec3(0.0, 0.0, 0.0));
    float n100 = hash(i + vec3(1.0, 0.0, 0.0));
    float n010 = hash(i + vec3(0.0, 1.0, 0.0));
    float n110 = hash(i + vec3(1.0, 1.0, 0.0));
    float n001 = hash(i + vec3(0.0, 0.0, 1.0));
    float n101 = hash(i + vec3(1.0, 0.0, 1.0));
    float n011 = hash(i + vec3(0.0, 1.0, 1.0));
    float n111 = hash(i + vec3(1.0, 1.0, 1.0));
    float nx00 = mix(n000, n100, u.x);
    float nx10 = mix(n010, n110, u.x);
    float nx01 = mix(n001, n101, u.x);
    float nx11 = mix(n011, n111, u.x);
    float nxy0 = mix(nx00, nx10, u.y);
    float nxy1 = mix(nx01, nx11, u.y);
    return mix(nxy0, nxy1, u.z);
}
float fbm(vec3 p) {
    float value = 0.0;
    float amp = 0.5;
    for (int i = 0; i < 5; i++) {
        value += noise(p) * amp;
        p = mat3(
            1.53, 0.72, 0.21,
           -0.64, 1.39, 0.57,
            0.31,-0.48, 1.71
        ) * p;
        amp *= 0.5;
    }
    return value;
}
float stars(vec3 dir, float amount, float time) {
    vec3 grid = dir * 130.0;
    vec3 cell = floor(grid);
    vec3 local = fract(grid) - 0.5;
    float h = hash(cell);
    float sparkle = smoothstep(0.997 - amount * 0.045, 1.0, h);
    float shape = smoothstep(0.18, 0.0, length(local));
    return sparkle * shape * (0.65 + 0.35 * sin(time * 2.0 + h * 12.0));
}
vec3 aurora(vec3 dir, float time, float scale, float intensity) {
    vec3 p = dir * scale * 2.6;
    float flow = fbm(p + vec3(time * 0.08, -time * 0.03, time * 0.04));
    float bands = 0.0;
    for (int i = 0; i < 4; i++) {
        float fi = float(i);
        float center = -0.15 + fi * 0.18 + sin((dir.x + dir.z) * (2.2 + fi) + time * (0.35 + fi * 0.08)) * 0.10;
        bands += smoothstep(0.18, 0.0, abs(dir.y - center - flow * 0.16));
    }
    vec3 base = mix(primaryColor.rgb * 0.16, secondaryColor.rgb * 0.32, clamp(dir.y * 0.5 + 0.5, 0.0, 1.0));
    vec3 glow = mix(primaryColor.rgb, secondaryColor.rgb, flow);
    return base + glow * bands * 0.55 * intensity + accentColor.rgb * pow(bands, 3.0) * 0.24;
}
void main() {
    float time = screenTimeOpacity.z * params.y;
    float opacity = screenTimeOpacity.w;
    float mode = params.x;
    float scale = max(params.z, 0.01);
    float intensity = params.w;
    float starAmount = extra.x;
    float vanillaBlend = extra.y;
    vec3 dir = normalize(skyDir);
    vec3 color = aurora(dir, time, scale, intensity);
    float starValue = stars(dir, starAmount, time) * smoothstep(-0.15, 0.65, dir.y);
    color += accentColor.rgb * starValue;
    color *= 0.72 + smoothstep(-0.65, 0.85, dir.y) * 0.34;
    float alpha = opacity * mix(1.0, 0.72, clamp(vanillaBlend, 0.0, 1.0));
    fragColor = vec4(color, alpha);
}
