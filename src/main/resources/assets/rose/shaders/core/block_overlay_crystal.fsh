#version 330

uniform sampler2D MaskSampler;

layout(std140) uniform BlockOverlayCrystal {
    vec4 Accent;
    vec4 Params;
    vec4 Outline;
    vec2 Size;
};

in vec2 TexCoord;
out vec4 fragColor;

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
    if (Params.y > 0.5) {
        outline = outlineFactor(TexCoord, Outline.x) * Outline.y;
    }

    if (mask <= 0.001 && outline <= 0.001) discard;

    float pulse = 0.5 + 0.5 * sin(Params.x * 2.4);
    vec3 fill = Accent.rgb * (0.75 + pulse * 0.35);
    fill += Accent.rgb * pow(pulse, 2.0) * 0.25;

    vec4 color = vec4(0.0);
    if (mask > 0.001) {
        color = vec4(fill, Accent.a * mask * (0.85 + pulse * 0.15));
    }
    if (outline > 0.001) {
        vec3 glow = Accent.rgb * (1.15 + Outline.z);
        color.rgb = mix(color.rgb, glow, clamp(outline * 1.5, 0.0, 1.0));
        color.a = max(color.a, outline);
    }

    fragColor = color;
}
