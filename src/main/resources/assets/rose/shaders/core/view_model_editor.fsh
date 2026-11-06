#version 330

uniform sampler2D HandSampler;
uniform sampler2D ItemSampler;
uniform sampler2D HandDepthSampler;
uniform sampler2D ItemDepthSampler;
layout(std140) uniform ViewModelEditor {
    vec4 Hover;
    vec4 Centers;
    vec4 OutlineColor;
};

in vec2 TexCoord;
out vec4 fragColor;

float maskAlpha(sampler2D mask, vec2 uv) {
    if (any(lessThan(uv, vec2(0.0))) || any(greaterThan(uv, vec2(1.0)))) {
        return 0.0;
    }
    return texture(mask, uv).a;
}

float sampleRing(sampler2D mask, vec2 uv, vec2 texel, float radius) {
    float coverage = 0.0;
    const float TAU = 6.28318530718;
    for (int i = 0; i < 16; i++) {
        float angle = TAU * (float(i) / 16.0);
        vec2 direction = vec2(cos(angle), sin(angle));
        coverage = max(coverage, maskAlpha(mask, uv + direction * texel * radius));
    }
    return coverage;
}

void main() {
    vec4 hand = texture(HandSampler, TexCoord);
    vec4 item = texture(ItemSampler, TexCoord);
    float handDepth = texture(HandDepthSampler, TexCoord).r;
    float itemDepth = texture(ItemDepthSampler, TexCoord).r;
    vec2 texel = 1.0 / vec2(textureSize(HandSampler, 0));

    float mainDistance = distance(TexCoord, Centers.xy);
    float offDistance = distance(TexCoord, Centers.zw);
    bool main = mainDistance <= offDistance;
    float handHover = main ? Hover.x : Hover.z;
    float itemHover = main ? Hover.y : Hover.w;

    float handCoreCoverage = sampleRing(HandSampler, TexCoord, texel, 1.35);
    float itemCoreCoverage = sampleRing(ItemSampler, TexCoord, texel, 1.35);
    float outside = 1.0 - smoothstep(0.01, 0.20, max(hand.a, item.a));
    float handCore = smoothstep(0.10, 0.82, handCoreCoverage) * outside * handHover;
    float itemCore = smoothstep(0.10, 0.82, itemCoreCoverage) * outside * itemHover;
    float core = max(handCore, itemCore);

    float nearGlow = max(sampleRing(HandSampler, TexCoord, texel, 2.6) * handHover,
            sampleRing(ItemSampler, TexCoord, texel, 2.6) * itemHover);
    float midGlow = max(sampleRing(HandSampler, TexCoord, texel, 4.4) * handHover,
            sampleRing(ItemSampler, TexCoord, texel, 4.4) * itemHover);
    float farGlow = max(sampleRing(HandSampler, TexCoord, texel, 6.8) * handHover,
            sampleRing(ItemSampler, TexCoord, texel, 6.8) * itemHover);
    float glow = max(nearGlow * 0.32, max(midGlow * 0.17, farGlow * 0.075));
    glow = smoothstep(0.015, 0.34, glow) * outside;

    float colorAlpha = OutlineColor.a;
    float coreAlpha = core * colorAlpha * 0.92;
    float glowAlpha = glow * colorAlpha * 0.38;
    float effectAlpha = max(coreAlpha, glowAlpha);

    if (hand.a > 0.001 || item.a > 0.001) {
        bool handVisible = hand.a > 0.001;
        bool itemVisible = item.a > 0.001;
        fragColor = handVisible && (!itemVisible || handDepth >= itemDepth) ? hand : item;
        return;
    }
    if (effectAlpha <= 0.001) discard;

    vec3 glowColor = mix(OutlineColor.rgb, vec3(1.0), coreAlpha * 0.42);
    fragColor = vec4(glowColor, effectAlpha);
}
