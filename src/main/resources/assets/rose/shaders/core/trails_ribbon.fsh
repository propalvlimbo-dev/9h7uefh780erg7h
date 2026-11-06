#version 330

layout(std140) uniform TrailsStyle {
    vec4 Color1;
    vec4 Color2;
    vec4 Color3;
    vec4 TimeSpeedAmplitudeWaviness;
    vec4 ThicknessGlowTaperSpread;
    vec4 HueIntensityOpacityScale;
    vec4 SaturationCountPadding;
};

in vec2 texCoord;
in vec4 vertexColor;

out vec4 fragColor;

vec3 samplePalette(float t) {
    t = fract(t);
    float scaled = t * 3.0;
    int index = int(floor(scaled));
    float blend = fract(scaled);
    if (index == 0) return mix(Color1.rgb, Color2.rgb, blend);
    if (index == 1) return mix(Color2.rgb, Color3.rgb, blend);
    return mix(Color3.rgb, Color1.rgb, blend);
}

void main() {
    float time = TimeSpeedAmplitudeWaviness.x;
    float speed = TimeSpeedAmplitudeWaviness.y;
    float amplitude = TimeSpeedAmplitudeWaviness.z;
    float waviness = TimeSpeedAmplitudeWaviness.w;
    float thickness = ThicknessGlowTaperSpread.x;
    float glow = ThicknessGlowTaperSpread.y;
    float taper = ThicknessGlowTaperSpread.z;
    float spread = ThicknessGlowTaperSpread.w;
    float hueShift = HueIntensityOpacityScale.x;
    float intensity = HueIntensityOpacityScale.y;
    float opacity = HueIntensityOpacityScale.z;
    float scale = HueIntensityOpacityScale.w;
    float saturation = SaturationCountPadding.x;
    int strandCount = int(SaturationCountPadding.y);

    float u = texCoord.x;
    float paletteOffset = texCoord.y;
    float envelope = pow(max(1.0 - smoothstep(0.72, 1.02, u), 0.0), taper);
    vec3 color = samplePalette(paletteOffset + u * 0.34 + time * 0.04 + hueShift);
    float energy = 0.06 + intensity * 0.94;
    color *= envelope * (0.45 + 0.7 * energy);
    color = 1.0 - exp(-color * glow);

    float gray = dot(color, vec3(0.2126, 0.7152, 0.0722));
    color = clamp(mix(vec3(gray), color, saturation), 0.0, 1.0);

    float luminance = max(max(color.r, color.g), color.b);
    float alpha = clamp(luminance, 0.0, 1.0) * opacity * vertexColor.a;
    if (alpha <= 0.002) discard;
    vec3 safeColor = min(color * opacity * vertexColor.a, vec3(0.98));
    fragColor = vec4(safeColor, alpha);
}
