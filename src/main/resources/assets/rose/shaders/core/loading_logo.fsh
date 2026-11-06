#version 330

layout(std140) uniform LogoParams {
    vec4 UvBounds;
    vec4 Animation;
    vec4 AccentTwo;
};

in vec2 TexCoord;
in vec4 InputColor;

uniform sampler2D Sampler0;

out vec4 fragColor;

float median(vec3 color) {
    return max(min(color.r, color.g), min(max(color.r, color.g), color.b));
}

float ease(float value) {
    float t = clamp(value, 0.0, 1.0);
    return t * t * (3.0 - 2.0 * t);
}

float tracePhase(vec2 point) {
    if (point.x < 0.29 && point.y < 0.42) {
        float path = clamp((point.y + point.x * 0.18) / 0.46, 0.0, 1.0);
        return 0.035 + path * 0.19;
    }

    if (point.x < 0.53) {
        float path = clamp(point.y * 0.88 + (0.53 - point.x) * 0.32, 0.0, 1.0);
        return 0.16 + path * 0.37;
    }

    vec2 relative = point - vec2(0.57, 0.50);
    float angle = atan(relative.y, relative.x);
    float path = fract((angle + 1.5707963) / 6.2831853);
    return 0.46 + path * 0.54;
}

void main() {
    vec3 sampleColor = texture(Sampler0, TexCoord).rgb;
    float distance = median(sampleColor) - 0.5;
    vec2 atlasDerivative = vec2(dFdx(TexCoord.x), dFdy(TexCoord.y)) * textureSize(Sampler0, 0);
    float pixelRange = Animation.x * inversesqrt(dot(atlasDerivative, atlasDerivative));
    float signedPixels = distance * pixelRange;
    float coverage = smoothstep(-0.55, 0.55, signedPixels);
    float contour = 1.0 - smoothstep(0.25, 1.65, abs(signedPixels));

    vec2 local = clamp((TexCoord - UvBounds.xy) / (UvBounds.zw - UvBounds.xy), 0.0, 1.0);
    float phase = tracePhase(local);
    float drawn = smoothstep(phase - 0.035, phase + 0.005, Animation.y);
    float traceHead = 1.0 - smoothstep(0.0, 0.055, abs(Animation.y - phase));
    float outlineFade = 1.0 - smoothstep(0.48, 1.0, Animation.z);
    float outlineAlpha = contour * drawn * outlineFade;

    float fillPhase = 0.12 + (local.x * 0.42 + local.y * 0.58) * 0.64;
    float fillMask = smoothstep(fillPhase - 0.13, fillPhase + 0.07, Animation.z);
    float fillAlpha = coverage * fillMask;
    float alpha = max(outlineAlpha, fillAlpha);

    vec3 baseColor = mix(InputColor.rgb, AccentTwo.rgb, 0.12 + local.y * 0.08);
    baseColor = mix(baseColor, vec3(1.0), traceHead * outlineAlpha * 0.34);

    float sweepPosition = mix(-0.30, 1.48, ease(Animation.w));
    float sweepDistance = abs(local.x + local.y * 0.16 - sweepPosition);
    float sweepBand = exp(-sweepDistance * sweepDistance * 145.0);
    float sweepEnvelope = smoothstep(0.015, 0.12, Animation.w)
            * (1.0 - smoothstep(0.86, 0.985, Animation.w));
    float sweep = sweepBand * sweepEnvelope * fillAlpha;
    vec3 sweepColor = mix(AccentTwo.rgb, vec3(1.0), 0.58);
    baseColor = mix(baseColor, sweepColor, sweep * 0.38);

    if (alpha <= 0.001) {
        discard;
    }

    fragColor = vec4(baseColor, InputColor.a * alpha);
}
