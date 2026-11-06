#version 330

layout(std140) uniform VolumeFog {
    mat4 InverseProjection;
    mat4 InverseView;
    vec4 CameraTime;
    vec4 Tint;
    vec4 Range;
};

uniform sampler2D DepthSampler;
#ifndef FOG_DENSITY_PASS
uniform sampler2D DensitySampler;
#endif
in vec2 TexCoord;
out vec4 fragColor;

vec4 hashCells(vec4 x, vec4 y, float z) {
    x = fract(x * 0.1031);
    y = fract(y * 0.1031);
    z = fract(z * 0.1031);
    vec4 d = x * (y + 33.33) + y * (z + 33.33) + z * (x + 33.33);
    return fract((x + y + 2.0 * d) * (z + d));
}

float densityNoise(vec3 p) {
    vec3 cell = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    vec3 next = mod(cell + 1.0, 256.0);
    cell = mod(cell, 256.0);
    vec4 x = vec4(cell.x, next.x, cell.x, next.x);
    vec4 y = vec4(cell.y, cell.y, next.y, next.y);
    vec4 values = mix(hashCells(x, y, cell.z), hashCells(x, y, next.z), f.z);
    return mix(mix(values.x, values.y, f.x), mix(values.z, values.w, f.x), f.y);
}

void main() {
    float depth = texture(DepthSampler, TexCoord).r;
    vec4 viewPoint = InverseProjection * vec4(TexCoord * 2.0 - 1.0, ROSE_DEPTH_TO_CLIP(depth), 1.0);
    vec3 viewPosition = viewPoint.xyz / max(viewPoint.w, 1e-7);
    float surfaceDistance = length(viewPosition);
    float end = min(surfaceDistance, Range.y);
    float start = Range.x;
#ifdef FOG_DENSITY_PASS
    float packedDistance = end / Range.y * 255.0;
    if (end <= start) {
        fragColor = vec4(0.5, floor(packedDistance) / 255.0, fract(packedDistance), 1.0);
        return;
    }
#else
    if (end <= start) discard;
    vec3 cached = texture(DensitySampler, TexCoord).rgb;
    float cachedDistance = (cached.g + cached.b / 255.0) * Range.y;
    if (abs(cachedDistance - end) <= max(0.5, end * 0.025)) {
        float coverage = 1.0 - exp(-(end - start) * (0.65 + 0.7 * cached.r) * Range.z);
        fragColor = vec4(Tint.rgb, coverage);
        return;
    }
#endif

    vec3 ray = mat3(InverseView) * (viewPosition / surfaceDistance);
    vec3 drift = vec3(CameraTime.w * 0.003, 0.0, CameraTime.w * 0.0017);
    vec3 origin = CameraTime.xyz * 0.03125 + drift;
    float pathLength = end - start;
    vec3 delta = ray * (pathLength * 0.03125);
    vec3 first = origin + ray * (start * 0.03125);
    float averageNoise = 0.0;
    for (int i = 0; i < 6; i++) {
        averageNoise += densityNoise(first + delta * ((float(i) + 0.5) / 6.0));
    }
    averageNoise /= 6.0;
#ifdef FOG_DENSITY_PASS
    fragColor = vec4(averageNoise, floor(packedDistance) / 255.0, fract(packedDistance), 1.0);
#else
    float opticalDepth = pathLength * (0.65 + 0.7 * averageNoise);
    float coverage = 1.0 - exp(-opticalDepth * Range.z);
    fragColor = vec4(Tint.rgb, coverage);
#endif
}
