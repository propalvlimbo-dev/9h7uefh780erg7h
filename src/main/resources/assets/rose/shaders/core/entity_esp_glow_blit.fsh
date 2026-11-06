#version 330

uniform sampler2D InSampler;

layout(std140) uniform EspGlow {
    vec4 Params0;
    vec4 Params1;
    vec4 GlowColor;
    vec4 OutlineColor;
    vec4 FillColor;
    vec2 Size;
};

in vec2 texCoord;
out vec4 fragColor;

int proceduralCategory(vec4 color) {
    ivec3 bytes = ivec3(round(color.rgb / max(color.a, 0.01) * 255.0));
    if (bytes.r >= 1 && bytes.r <= 6 && bytes.g >= 253 && bytes.b >= 252) {
        return bytes.r;
    }
    return 0;
}

vec3 categoryColor(int category) {
    if (category == 1) return vec3(1.0);
    if (category == 2) return vec3(1.0, 0.31, 0.31);
    if (category == 3) return vec3(0.35, 0.90, 0.51);
    if (category == 4) return vec3(0.35, 0.82, 0.90);
    if (category == 5) return vec3(1.0, 0.80, 0.31);
    if (category == 6) return vec3(0.35, 0.67, 1.0);
    return vec3(1.0);
}

vec3 decodedColor(vec4 color) {
    int category = proceduralCategory(color);
    if (category > 0) {
        return categoryColor(category);
    }
    return color.rgb / max(color.a, 0.01);
}

vec3 saturated(vec3 color, float amount) {
    float maximum = max(color.r, max(color.g, color.b));
    if (maximum > 0.001) color /= maximum;
    float luminance = dot(color, vec3(0.299, 0.587, 0.114));
    return clamp(mix(vec3(luminance), color, amount), 0.0, 1.0);
}

void main() {
    float thickness = max(0.0, Params0.z);
    if (thickness <= 0.0) discard;

    vec2 texel = 1.0 / max(Size, vec2(1.0));
    vec4 center = texture(InSampler, texCoord);
    float outside = 1.0 - smoothstep(0.01, 0.12, center.a);
    if (outside <= 0.001) discard;

    vec2 offset = texel * thickness;
    vec4 samples[8] = vec4[](
        texture(InSampler, texCoord + vec2(offset.x, 0.0)),
        texture(InSampler, texCoord - vec2(offset.x, 0.0)),
        texture(InSampler, texCoord + vec2(0.0, offset.y)),
        texture(InSampler, texCoord - vec2(0.0, offset.y)),
        texture(InSampler, texCoord + offset * 0.7071),
        texture(InSampler, texCoord + vec2(-offset.x, offset.y) * 0.7071),
        texture(InSampler, texCoord + vec2(offset.x, -offset.y) * 0.7071),
        texture(InSampler, texCoord - offset * 0.7071)
    );

    float outlineMask = 0.0;
    float colorWeight = 0.001;
    vec3 sampledColor = vec3(0.0);
    for (int index = 0; index < 8; index++) {
        float sampleMask = samples[index].a;
        outlineMask = max(outlineMask, sampleMask);
        sampledColor += decodedColor(samples[index]) * sampleMask;
        colorWeight += sampleMask;
    }

    float alpha = smoothstep(0.02, 0.78, outlineMask) * outside * OutlineColor.a;
    if (alpha <= 0.002) discard;

    vec3 color = Params1.x > 0.5
        ? saturated(sampledColor / colorWeight, Params1.y)
        : OutlineColor.rgb;
    fragColor = vec4(color, alpha);
}
