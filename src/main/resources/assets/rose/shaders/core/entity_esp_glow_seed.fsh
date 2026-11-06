#version 330

uniform sampler2D MaskSampler;
uniform sampler2D SurfaceSampler;
uniform sampler2D RadiusSampler;

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

float radiusWeight(ivec2 texel) {
    ivec2 size = textureSize(RadiusSampler, 0);
    vec4 sampleValue = texelFetch(RadiusSampler, clamp(texel, ivec2(0), size - 1), 0);
    ivec3 tag = ivec3(round(sampleValue.rgb * 255.0));
    if (tag.r != int(DistanceParams.w + 0.5) || tag.g != 254) return 0.0;
    float level = clamp((253.0 - float(tag.b)) / 50.0, 0.0, 5.0);
    return max(0.0, 1.0 - abs(level - DistanceParams.x)) * sampleValue.a;
}

float distanceWeight(float mask) {
    if (DistanceParams.y < 0.5) return 1.0;
    vec2 pixel = TexCoord * vec2(textureSize(RadiusSampler, 0)) - 0.5;
    ivec2 base = ivec2(floor(pixel));
    vec2 blend = fract(pixel);
    float bottom = mix(radiusWeight(base), radiusWeight(base + ivec2(1, 0)), blend.x);
    float top = mix(radiusWeight(base + ivec2(0, 1)), radiusWeight(base + ivec2(1, 1)), blend.x);
    return clamp(mix(bottom, top, blend.y) / max(mask, 0.0001), 0.0, 1.0) * DistanceParams.z;
}

float glowNoise(vec3 p) {
    vec3 cell = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    vec3 coefficients = vec3(127.1, 311.7, 74.7);
    float n = dot(cell, coefficients);
    vec4 offsets = vec4(0.0, coefficients.x, coefficients.y, coefficients.x + coefficients.y);
    vec4 front = fract(sin(n + offsets) * 43758.5453);
    vec4 back = fract(sin(n + offsets + coefficients.z) * 43758.5453);
    vec4 values = mix(front, back, f.z);
    return mix(mix(values.x, values.y, f.x), mix(values.z, values.w, f.x), f.y);
}

void main() {
    vec4 mask = texture(MaskSampler, TexCoord);
    if (mask.a < 0.001) {
        fragColor = vec4(0.0);
        return;
    }
    float distanceFade = distanceWeight(mask.a);
    if (distanceFade <= 0.0) {
        fragColor = vec4(0.0);
        return;
    }
    vec3 local = vec3(0.0);
    if (Screen.w > 0.5 && (EffectParams.x > 1.5 || SmokeParams.z > 0.5)) {
        vec4 surface = texture(SurfaceSampler, TexCoord);
        if (surface.a > 0.001) local = (surface.rgb / surface.a - 0.5) * 4.0;
    }
    vec2 weights = vec2(mask.a, 0.0);
    float white = 0.0;
    if (SmokeParams.z > 0.5) {
        float phase = local.y * 6.2831853 + sin(local.x * 4.0 + local.z * 3.0) * 0.35
                - EffectParams.z * SmokeParams.w * 6.2831853;
        float blend = smoothstep(0.15, 0.85, 0.5 + 0.5 * sin(phase));
        weights = mask.a * vec2((1.0 - blend) * GlowColor1.a, blend * GlowColor2.a);
    }
    if (EffectParams.x > 2.5) {
        float twist = sin(local.x * 4.0 + local.z * 3.0) * EnergyParams.z * 0.25;
        float phase = local.y * 2.5 + twist - EffectParams.z * EnergyParams.x * 0.6;
        float distanceToWave = abs(fract(phase + 0.5) - 0.5);
        float width = max(EnergyParams.y, 0.01);
        float wave = 1.0 - smoothstep(width * 0.25, width, distanceToWave);
        weights *= 0.06 + wave * 0.94;
        white = (weights.x + weights.y) * wave * 0.16;
        weights *= 1.0 - wave * 0.16;
    } else if (EffectParams.x > 1.5) {
        vec3 p = local * (3.0 / max(SmokeParams.x, 0.1));
        float t = EffectParams.z * EffectParams.w;
        vec3 drift = vec3(t * 0.18, -t * 0.42, t * 0.11);
        float broad = glowNoise(p + drift);
        float detail = glowNoise(p * 1.65 - drift * 0.7 + broad * 1.4);
        float smoke = smoothstep(0.32, 0.66, broad * 0.7 + detail * 0.3);
        weights *= 0.06 + smoke * 0.94;
    }
    fragColor = vec4(weights, white, mask.a) * distanceFade;
}
