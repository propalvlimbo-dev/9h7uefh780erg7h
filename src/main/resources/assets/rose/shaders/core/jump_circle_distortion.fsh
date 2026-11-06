#version 330
uniform sampler2D SceneSampler;
layout(std140) uniform JumpDistortion {
    vec2 OutSize;
    float Strength;
    float Time;
    vec2 Mouse;
};
in vec2 texCoord;
in vec4 vertexColor;
out vec4 fragColor;
void main() {
    vec2 uv = 1.4 * (2.0 * texCoord - vec2(1.0));
    float radius = length(uv);
    if (radius >= 1.4) discard;

    vec2 screenUv = gl_FragCoord.xy / max(OutSize, vec2(1.0));
    mat2 derivatives = mat2(dFdx(texCoord), dFdy(texCoord));
    float det = determinant(derivatives);
    if (abs(det) <= 0.00000001) discard;
    mat2 inverseDerivatives = inverse(derivatives);
    vec2 radial = radius > 0.0001 ? uv / radius : vec2(0.0);
    vec2 tangent = vec2(-radial.y, radial.x);
    float strength = clamp(Strength / 24.0, 0.08, 1.35);

    if (radius <= 1.0) {
        float height = sqrt(max(1.0 - radius * radius, 0.0));
        vec3 normal = normalize(vec3(uv, height));
        float eta = mix(1.0, 0.52, clamp(strength * 1.05, 0.0, 1.0));
        vec3 refractedDirection = refract(vec3(0.0, 0.0, -1.0), normal, eta);
        float innerBulge = (1.0 - radius * radius) * strength;
        float innerPinch = sin(radius * 3.14159265) * strength;
        vec2 innerWarp = -uv * innerBulge * 0.62;
        innerWarp -= radial * innerPinch * (0.070 + 0.025 * cos(atan(uv.y, uv.x) * 4.0));
        vec2 localDelta = (refractedDirection.xy * 0.54 + innerWarp) / 2.8;
        vec2 pixelOffset = inverseDerivatives * localDelta;
        vec2 refractOffset = pixelOffset / max(OutSize, vec2(1.0));
        float offsetLength = length(refractOffset);
        if (offsetLength > 0.26) refractOffset *= 0.26 / offsetLength;
        vec2 sampleUv = clamp(screenUv + refractOffset, vec2(0.001), vec2(0.999));
        vec2 chroma = refractOffset * (0.050 + radius * 0.050);
        float red = texture(SceneSampler, clamp(sampleUv + chroma, vec2(0.001), vec2(0.999))).r;
        float green = texture(SceneSampler, sampleUv).g;
        float blue = texture(SceneSampler, clamp(sampleUv - chroma, vec2(0.001), vec2(0.999))).b;
        float alpha = (0.92 + height * 0.07) * vertexColor.a;
        fragColor = vec4(red, green, blue, alpha);
    } else {
        float outer = (radius - 1.0) / 0.4;
        float phase = clamp(outer * 0.88 + 0.12, 0.0, 1.0);
        float band = pow(sin(phase * 3.14159265), 0.82);
        float angle = atan(uv.y, uv.x);
        float lobes = 0.70 + 0.30 * sin(angle * 5.0);
        float wave = (0.66 + 0.34 * sin(outer * 5.2 + angle * 3.0)) * lobes;
        vec2 localDelta = radial * band * wave * strength * 0.34;
        localDelta += tangent * band * sin(angle * 6.0 - outer * 3.0) * strength * 0.052;
        vec2 pixelOffset = inverseDerivatives * (localDelta / 2.8);
        vec2 refractOffset = pixelOffset / max(OutSize, vec2(1.0));
        float offsetLength = length(refractOffset);
        if (offsetLength > 0.20) refractOffset *= 0.20 / offsetLength;
        vec2 sampleUv = clamp(screenUv + refractOffset, vec2(0.001), vec2(0.999));
        vec2 chroma = refractOffset * 0.065;
        float red = texture(SceneSampler, clamp(sampleUv + chroma, vec2(0.001), vec2(0.999))).r;
        float green = texture(SceneSampler, sampleUv).g;
        float blue = texture(SceneSampler, clamp(sampleUv - chroma, vec2(0.001), vec2(0.999))).b;
        float alpha = band * (0.72 + lobes * 0.20) * vertexColor.a;
        fragColor = vec4(red, green, blue, alpha);
    }
}
