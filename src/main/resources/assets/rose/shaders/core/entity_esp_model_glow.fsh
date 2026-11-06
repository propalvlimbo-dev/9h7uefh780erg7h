#version 330

uniform sampler2D BloomSampler;
uniform sampler2D MaskSampler;

layout(std140) uniform EspOuterGlow {
    vec4 Params;
    vec4 GlowColor1;
    vec4 GlowColor2;
    vec4 OutlineColor;
    vec4 Screen;
    vec4 EffectParams;
    vec4 SmokeParams;
    vec4 EnergyParams;
    vec4 DistanceParams;
};

in vec2 TexCoord;
out vec4 fragColor;

void outputGlow(vec4 color) {
    fragColor = EnergyParams.w > 0.5
            ? vec4(color.rgb * color.a, color.a > 0.0 ? 1.0 / 255.0 : 0.0)
            : color;
}

vec4 sampleSmoothBloom(vec2 uv) {
    vec2 size = vec2(textureSize(BloomSampler, 0));
    vec2 pixel = uv * size - 0.5;
    vec2 cell = floor(pixel);
    vec2 f = fract(pixel);
    vec2 f2 = f * f;
    vec2 f3 = f2 * f;
    vec2 w0 = (1.0 - 3.0 * f + 3.0 * f2 - f3) / 6.0;
    vec2 w1 = (4.0 - 6.0 * f2 + 3.0 * f3) / 6.0;
    vec2 w2 = (1.0 + 3.0 * f + 3.0 * f2 - 3.0 * f3) / 6.0;
    vec2 w3 = f3 / 6.0;
    vec2 left = w0 + w1;
    vec2 right = w2 + w3;
    vec2 low = (cell - 0.5 + w1 / left) / size;
    vec2 high = (cell + 1.5 + w3 / right) / size;
    vec4 bottom = texture(BloomSampler, vec2(low.x, low.y)) * left.x
            + texture(BloomSampler, vec2(high.x, low.y)) * right.x;
    vec4 top = texture(BloomSampler, vec2(low.x, high.y)) * left.x
            + texture(BloomSampler, vec2(high.x, high.y)) * right.x;
    return bottom * left.y + top * right.y;
}

void main() {
    float mask = texture(MaskSampler, TexCoord).a;
    float entityMask = smoothstep(0.01, 0.12, mask);
    bool innerPixel = entityMask >= 1.0;
    if (innerPixel && Params.w <= 0.0) discard;
    vec4 glowData = innerPixel ? texture(BloomSampler, TexCoord) : sampleSmoothBloom(TexCoord);
    if (glowData.a <= 0.0 && mask <= 0.0) discard;
    float glowCoverage = glowData.r + glowData.g + glowData.b;
    vec3 firstColor = SmokeParams.z > 0.5 ? GlowColor1.rgb : OutlineColor.rgb;
    vec3 glowColor = firstColor * glowData.r + GlowColor2.rgb * glowData.g + vec3(glowData.b);
    vec4 bloom = innerPixel ? vec4(OutlineColor.rgb * glowData.a, glowData.a) : vec4(glowColor, glowCoverage);
    float twoColors = innerPixel ? 0.0 : SmokeParams.z;
    float bloomMask = clamp(bloom.a, 0.0, 1.0);
    float outerGlow = pow(bloomMask, 0.72) * (1.0 - entityMask);
    float innerDistance = clamp((1.0 - bloomMask) * 2.15, 0.0, 1.0);
    float innerSupport = smoothstep(0.04, 0.32, bloomMask);
    float cornerSupport = smoothstep(0.015, 0.18, bloomMask) * 0.42;
    innerSupport = max(innerSupport, cornerSupport);
    float innerGlow = entityMask * pow(innerDistance, 0.62) * innerSupport * Params.w;
    float style = innerPixel ? 0.0 : EffectParams.x;
    if (style > 0.5) {
        float softness = clamp(EffectParams.y / 1.5, 0.0, 1.0);
        float halo = pow(bloomMask, mix(1.05, 0.42, softness));
        float rim = pow(bloomMask, 2.4) * 1.6;
        outerGlow = (halo * 1.2 + rim * 0.85) * (1.0 - entityMask);
        if (style > 2.5) {
            outerGlow *= 2.2;
        } else if (style > 1.5) {
            outerGlow *= max(SmokeParams.y, 0.0) * 2.0;
        }
    } else if (twoColors > 0.5) {
        outerGlow *= 1.35;
    }
    outerGlow *= smoothstep(0.0, 1.0 / 65536.0, bloomMask);
    float alphaMask = max(outerGlow, innerGlow * 0.82);
    vec2 outlineOffset = Screen.z / max(Screen.xy, vec2(1.0));
    float outlineMask = 0.0;
    if (!innerPixel && OutlineColor.a > 0.001 && Screen.z > 0.0) {
        outlineMask = max(outlineMask, texture(MaskSampler, TexCoord + vec2(outlineOffset.x, 0.0)).a);
        outlineMask = max(outlineMask, texture(MaskSampler, TexCoord - vec2(outlineOffset.x, 0.0)).a);
        outlineMask = max(outlineMask, texture(MaskSampler, TexCoord + vec2(0.0, outlineOffset.y)).a);
        outlineMask = max(outlineMask, texture(MaskSampler, TexCoord - vec2(0.0, outlineOffset.y)).a);
        outlineMask = max(outlineMask, texture(MaskSampler, TexCoord + outlineOffset * 0.7071).a);
        outlineMask = max(outlineMask, texture(MaskSampler, TexCoord + vec2(-outlineOffset.x, outlineOffset.y) * 0.7071).a);
        outlineMask = max(outlineMask, texture(MaskSampler, TexCoord + vec2(outlineOffset.x, -outlineOffset.y) * 0.7071).a);
        outlineMask = max(outlineMask, texture(MaskSampler, TexCoord - outlineOffset * 0.7071).a);
    }
    float outlineGlow = smoothstep(0.02, 0.8, outlineMask)
        * (1.0 - entityMask) * OutlineColor.a;
    if (max(alphaMask, outlineGlow) < 0.002) discard;

    float outerIntensity = outerGlow * Params.x;
    float innerIntensity = innerGlow * Params.x * 0.78;
    float intensity = outerIntensity + innerIntensity * (1.0 - clamp(outerIntensity, 0.0, 1.0));
    float hot = smoothstep(0.65, 1.55, intensity);
    vec3 base;
    if (Params.y > 1.5) {
        vec3 sampled = bloom.rgb / max(bloom.a, 0.0001);
        float core = pow(clamp(alphaMask, 0.0, 1.0), 0.55);
        base = mix(sampled * 0.5, sampled, core);
    } else if (Params.y > 0.5) {
        float confidence = smoothstep(0.03, 0.25, bloom.a);
        vec3 color = bloom.rgb / max(bloom.a, 0.08);
        float maximum = max(color.r, max(color.g, color.b));
        if (maximum > 0.001) color /= maximum;
        float luminance = dot(color, vec3(0.299, 0.587, 0.114));
        vec3 saturated = clamp(mix(vec3(luminance), color, Params.z), 0.0, 1.0);
        base = mix(vec3(1.0), saturated, confidence);
    } else {
        float core = pow(clamp(alphaMask, 0.0, 1.0), 0.55);
        base = mix(GlowColor2.rgb, GlowColor1.rgb, core);
    }
    float colorAlpha = twoColors > 0.5 ? smoothstep(0.0, 0.02, bloomMask) : 1.0;
    if (style > 0.5) {
        base = bloom.rgb / max(bloom.a, 0.0001);
        float rimLight = smoothstep(0.35, 0.85, bloomMask) * (1.0 - entityMask);
        vec3 neon = mix(base, vec3(1.0), rimLight * 0.16);
        float glowAlpha = clamp(intensity, 0.0, 1.0) * colorAlpha;
        float edgeAlpha = clamp(outlineGlow, 0.0, 1.0);
        vec3 edgeColor = OutlineColor.rgb;
        outputGlow(vec4(mix(neon, edgeColor, edgeAlpha), max(glowAlpha, edgeAlpha)));
        return;
    }
    vec3 color = mix(base, vec3(1.0, 0.97, 0.88), hot * 0.85);
    float innerCore = smoothstep(0.08, 0.72, innerGlow);
    color = mix(color, mix(base, vec3(1.0), 0.72), innerCore * 0.58);
    vec3 outlineColor = twoColors > 0.5 ? OutlineColor.rgb : Params.y > 0.5 ? base : OutlineColor.rgb;
    color = mix(color, outlineColor, clamp(outlineGlow * 1.35, 0.0, 1.0));
    float brightness = 1.0 + hot * 0.6 + innerCore * 0.34;
    outputGlow(vec4(color * brightness, max(clamp(intensity, 0.0, 1.0) * colorAlpha, clamp(outlineGlow, 0.0, 1.0))));
}
