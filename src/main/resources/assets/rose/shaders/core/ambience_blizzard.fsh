#version 330

layout(std140) uniform Blizzard {
    mat4 InverseProjection;
    mat4 InverseView;
    vec4 CameraTime;
    vec4 Params;
    vec4 Wind;
    vec4 Tint;
};

uniform sampler2D DepthSampler;

in vec2 TexCoord;
out vec4 fragColor;

float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

float noise(vec2 p) {
    vec2 cell = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    float a = hash(cell);
    float b = hash(cell + vec2(1.0, 0.0));
    float c = hash(cell + vec2(0.0, 1.0));
    float d = hash(cell + vec2(1.0, 1.0));
    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
}

float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.55;
    for (int i = 0; i < 3; i++) {
        value += noise(p) * amplitude;
        p = p * 2.13 + vec2(17.3, 9.1);
        amplitude *= 0.5;
    }
    return value;
}

vec3 viewFromDepth(vec2 uv, float depth) {
    vec4 homogeneous = InverseProjection * vec4(uv * 2.0 - 1.0, ROSE_DEPTH_TO_CLIP(depth), 1.0);
    return homogeneous.xyz / homogeneous.w;
}

void main() {
    float rawDepth = texture(DepthSampler, TexCoord).r;
    vec3 directionPoint = viewFromDepth(TexCoord, 0.9);
    vec3 rayDirection = normalize((InverseView * vec4(directionPoint, 0.0)).xyz);

    float maxDistance = max(Params.z, 8.0);
    float sceneDistance = maxDistance;
    if (rawDepth > 0.000001) {
        vec3 viewPosition = viewFromDepth(TexCoord, rawDepth);
        sceneDistance = length(viewPosition);
    }
    float marchEnd = min(sceneDistance, maxDistance);

    float time = CameraTime.w;
    float density = 0.0;
    for (int i = 0; i < 6; i++) {
        float t = marchEnd * (float(i) + 0.5) / 6.0;
        vec3 p = CameraTime.xyz + rayDirection * t;
        vec2 noiseCoord = (p.xz - Wind.xy) * 0.11
            + vec2(p.y * 0.07, -p.y * 0.05)
            + vec2(time * 0.12, time * 0.09);
        float sample1 = fbm(noiseCoord);
        density += smoothstep(0.34, 0.86, sample1);
    }
    density /= 6.0;

    float depthFactor = 1.0 - exp(-marchEnd * 0.05);
    float gustWave = 0.65 + 0.35 * sin(time * 0.85 + dot(rayDirection.xz, Wind.zw) * 2.4 + density * 3.0);
    float alpha = clamp(
        density * depthFactor * Params.x * Params.y * gustWave * 1.1
            + 0.05 * Params.x * Params.y * depthFactor,
        0.0,
        0.9
    );
    if (alpha <= 0.003) discard;

    vec3 mistColor = mix(Tint.rgb * 0.8, Tint.rgb, density);
    fragColor = vec4(mistColor, alpha);
}
