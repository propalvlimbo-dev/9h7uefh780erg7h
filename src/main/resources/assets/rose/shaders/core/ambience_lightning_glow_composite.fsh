#version 330

layout(std140) uniform LightningGlowComposite {
    vec4 Exposure;
    vec4 WideColor;
    vec4 TightColor;
};

uniform sampler2D BloomSampler;

in vec2 TexCoord;
out vec4 fragColor;

void evaluateGlow(float bloomMask, float exposure, vec3 glowColor,
                  out vec3 color, out float alpha) {
    float outerGlow = pow(clamp(bloomMask, 0.0, 1.0), 0.72);
    float intensity = outerGlow * exposure;
    float hot = smoothstep(0.65, 1.55, intensity);
    float core = pow(clamp(outerGlow, 0.0, 1.0), 0.55);
    vec3 base = mix(glowColor * 0.5, glowColor, core);
    color = mix(base, vec3(1.0, 0.97, 0.88), hot * 0.85) * (1.0 + hot * 0.6);
    alpha = clamp(intensity, 0.0, 1.0);
}

void main() {
    vec2 bloom = texture(BloomSampler, TexCoord).rg;

    vec3 wideRgb;
    vec3 tightRgb;
    float wideAlpha;
    float tightAlpha;
    evaluateGlow(bloom.r, Exposure.x, WideColor.rgb, wideRgb, wideAlpha);
    evaluateGlow(bloom.g, Exposure.y, TightColor.rgb, tightRgb, tightAlpha);

    float combinedAlpha = clamp(sqrt(wideAlpha * wideAlpha + tightAlpha * tightAlpha), 0.0, 1.0);
    if (combinedAlpha < 0.002) discard;

    vec3 additiveContribution = wideRgb * wideAlpha + tightRgb * tightAlpha;
    fragColor = vec4(additiveContribution / max(combinedAlpha, 0.00001), combinedAlpha);
}
