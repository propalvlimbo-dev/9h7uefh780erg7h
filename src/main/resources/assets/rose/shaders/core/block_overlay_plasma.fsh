#version 330

uniform sampler2D MaskSampler;

layout(std140) uniform BlockOverlayPlasma {
    vec4 Accent;
    vec4 AccentHot;
    vec4 Params;
    vec4 Outline;
    vec2 Size;
};

in vec2 TexCoord;
out vec4 fragColor;

float hash21(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(
        mix(hash21(i), hash21(i + vec2(1.0, 0.0)), f.x),
        mix(hash21(i + vec2(0.0, 1.0)), hash21(i + 1.0), f.x),
        f.y
    );
}

float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.5;
    mat2 rotation = mat2(1.6, 1.2, -1.2, 1.6);
    for (int i = 0; i < 5; i++) {
        value += noise(p) * amplitude;
        p = rotation * p;
        amplitude *= 0.55;
    }
    return value;
}

float maskAt(vec2 uv) {
    return texture(MaskSampler, uv).a;
}

float outlineFactor(vec2 uv, float widthPx) {
    vec2 texel = 1.0 / max(Size, vec2(1.0));
    float center = maskAt(uv);
    float maxNeighbor = 0.0;
    float radius = max(widthPx, 0.5);
    for (float x = -2.0; x <= 2.0; x += 1.0) {
        for (float y = -2.0; y <= 2.0; y += 1.0) {
            if (abs(x) + abs(y) < 0.5) continue;
            vec2 offset = vec2(x, y) * texel * radius;
            maxNeighbor = max(maxNeighbor, maskAt(uv + offset));
        }
    }
    return clamp(maxNeighbor - center, 0.0, 1.0);
}

void main() {
    float mask = maskAt(TexCoord);
    float outline = 0.0;
    if (Params.w > 0.5) {
        outline = outlineFactor(TexCoord, Outline.x) * Outline.y;
    }

    if (mask <= 0.001 && outline <= 0.001) discard;

    float time = Params.x * Params.y;
    float scale = max(Params.z, 0.1);
    vec2 uv = TexCoord * scale * 3.0;
    float plasma = fbm(uv + vec2(time * 0.35, -time * 0.22));
    plasma = fbm(uv * 1.7 + plasma * 2.2 + vec2(-time * 0.18, time * 0.27));
    plasma = smoothstep(0.15, 0.95, plasma);

    vec3 fill = mix(Accent.rgb, AccentHot.rgb, plasma);
    fill += AccentHot.rgb * pow(plasma, 3.0) * 0.35;

    vec4 color = vec4(0.0);
    if (mask > 0.001) {
        color = vec4(fill, Accent.a * mask);
    }
    if (outline > 0.001) {
        vec3 glow = mix(Accent.rgb, AccentHot.rgb, 0.65) * (1.0 + Outline.z);
        color.rgb = mix(color.rgb, glow, clamp(outline * 1.4, 0.0, 1.0));
        color.a = max(color.a, outline);
    }

    fragColor = color;
}
