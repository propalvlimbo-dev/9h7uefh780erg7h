#version 330

layout(std140) uniform WetWorld {
    mat4 ViewProj;
    mat4 InvViewProj;
    vec4 CamPosTime;
    vec4 SkyTintRefl;
    vec4 SunDirWet;
    vec4 RippleGloss;
    vec4 DistUp;
};

uniform sampler2D SceneSampler;
uniform sampler2D DepthSampler;

in vec2 TexCoord;
out vec4 fragColor;

const int MAX_STEPS = 48;
const int REFINE_STEPS = 4;
const float STEP_GROWTH = 0.12;
const float SURFACE_BIAS = 0.035;
const float EDGE_FADE = 0.12;
const float SILHOUETTE_LIMIT = 0.55;
const float SPEC_POWER = 190.0;

vec3 worldFromDepth(vec2 uv, float depth) {
    vec4 clip = vec4(uv * 2.0 - 1.0, ROSE_DEPTH_TO_CLIP(depth), 1.0);
    vec4 pos = InvViewProj * clip;
    return pos.xyz / pos.w;
}

vec3 faceNormal(vec2 uv, float depth, vec3 view, out float discontinuity) {
    vec2 texel = 1.0 / vec2(textureSize(DepthSampler, 0));

    vec2 uvL = uv - vec2(texel.x, 0.0);
    vec2 uvR = uv + vec2(texel.x, 0.0);
    vec2 uvD = uv - vec2(0.0, texel.y);
    vec2 uvU = uv + vec2(0.0, texel.y);

    float dL = texture(DepthSampler, uvL).r;
    float dR = texture(DepthSampler, uvR).r;
    float dD = texture(DepthSampler, uvD).r;
    float dU = texture(DepthSampler, uvU).r;

    bool leftCloser = abs(dL - depth) < abs(dR - depth);
    bool downCloser = abs(dD - depth) < abs(dU - depth);

    vec3 ddx = leftCloser ? view - worldFromDepth(uvL, dL) : worldFromDepth(uvR, dR) - view;
    vec3 ddy = downCloser ? view - worldFromDepth(uvD, dD) : worldFromDepth(uvU, dU) - view;

    float depthDistance = length(view);
    discontinuity = max(length(ddx), length(ddy)) / max(depthDistance, 1.0);

    vec3 n = cross(ddx, ddy);
    float len = length(n);
    if (len < 1e-8) return normalize(-view);

    n /= len;
    return dot(n, -view) < 0.0 ? -n : n;
}

float hash12(vec2 p) {
    vec3 q = fract(vec3(p.xyx) * 0.1031);
    q += dot(q, q.yzx + 33.33);
    return fract((q.x + q.y) * q.z);
}

float rippleNoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash12(i), hash12(i + vec2(1.0, 0.0)), f.x),
               mix(hash12(i + vec2(0.0, 1.0)), hash12(i + vec2(1.0, 1.0)), f.x), f.y);
}

vec2 rippleGradient(vec2 p) {
    const float e = 0.25;
    float base = rippleNoise(p);
    return vec2(rippleNoise(p + vec2(e, 0.0)) - base, rippleNoise(p + vec2(0.0, e)) - base) / e;
}

float interleavedGradient(vec2 pixel) {
    return fract(52.9829189 * fract(dot(pixel, vec2(0.06711056, 0.00583715))));
}

float edgeFade(vec2 uv) {
    vec2 fade = smoothstep(vec2(0.0), vec2(EDGE_FADE), uv)
              * smoothstep(vec2(0.0), vec2(EDGE_FADE), 1.0 - uv);
    return fade.x * fade.y;
}

void main() {
    vec4 src = texture(SceneSampler, TexCoord);
    float depth = texture(DepthSampler, TexCoord).r;
    if (depth <= 0.0) {
        fragColor = src;
        return;
    }

    vec3 view = worldFromDepth(TexCoord, depth);
    float discontinuity;
    vec3 normal = faceNormal(TexCoord, depth, view, discontinuity);
    if (discontinuity > SILHOUETTE_LIMIT) {
        fragColor = src;
        return;
    }

    float mask = mix(1.0, smoothstep(0.5, 0.82, normal.y), DistUp.y);
    if (mask <= 0.002) {
        fragColor = src;
        return;
    }

    vec3 world = CamPosTime.xyz + view;
    float time = CamPosTime.w;
    float reflectivity = SkyTintRefl.w;
    float wetness = SunDirWet.w;
    float ripple = RippleGloss.x;
    float gloss = RippleGloss.y;
    float maxDistance = DistUp.x;
    float hitThickness = RippleGloss.w;

    if (ripple > 0.001 && normal.y > 0.1) {
        vec2 flow = world.xz * 0.85 + vec2(time * 0.31, time * 0.19);
        vec2 gradient = rippleGradient(flow);
        normal = normalize(normal + vec3(gradient.x, 0.0, gradient.y) * ripple * 0.22);
    }

    vec3 incident = normalize(view);
    float lum = dot(src.rgb, vec3(0.2126, 0.7152, 0.0722));
    vec3 soaked = mix(vec3(lum), src.rgb, 1.0 + 0.4 * wetness) * (1.0 - 0.32 * wetness);
    soaked = mix(src.rgb, soaked, mask);

    vec3 reflected = reflect(incident, normal);
    vec3 origin = view + normal * SURFACE_BIAS;

    vec4 rayOrigin = ViewProj * vec4(origin, 1.0);
    vec4 rayDirection = ViewProj * vec4(reflected, 0.0);

    int steps = clamp(int(RippleGloss.z), 8, MAX_STEPS);
    float stepLength = maxDistance / float(steps);
    float jitter = interleavedGradient(gl_FragCoord.xy);

    float travelled = stepLength * (0.35 + jitter * 0.65);
    float previous = travelled;
    vec2 hitUv = vec2(0.0);
    float hit = 0.0;

    for (int i = 0; i < MAX_STEPS; i++) {
        if (i >= steps || reflectivity <= 0.0) break;

        vec3 marchPos = origin + reflected * travelled;
        vec4 clip = rayOrigin + rayDirection * travelled;
        if (clip.w <= 0.001) break;

        vec2 uv = (clip.xy / clip.w) * 0.5 + 0.5;
        if (uv.x < 0.0 || uv.x > 1.0 || uv.y < 0.0 || uv.y > 1.0) break;

        float sceneDepth = texture(DepthSampler, uv).r;
        if (sceneDepth > 0.0) {
            float behind = length(marchPos) - length(worldFromDepth(uv, sceneDepth));
            if (behind > 0.0 && behind < hitThickness + travelled * 0.08) {
                float lo = previous;
                float hi = travelled;
                for (int r = 0; r < REFINE_STEPS; r++) {
                    float mid = (lo + hi) * 0.5;
                    vec3 probe = origin + reflected * mid;
                    vec4 probeClip = rayOrigin + rayDirection * mid;
                    vec2 probeUv = clamp((probeClip.xy / max(probeClip.w, 1e-4)) * 0.5 + 0.5, 0.0, 1.0);
                    float probeDepth = texture(DepthSampler, probeUv).r;
                    float probeBehind = length(probe) - length(worldFromDepth(probeUv, probeDepth));
                    if (probeBehind > 0.0) {
                        hi = mid;
                        uv = probeUv;
                    } else {
                        lo = mid;
                    }
                }
                hitUv = uv;
                hit = edgeFade(uv) * (1.0 - smoothstep(maxDistance * 0.6, maxDistance, hi));
                break;
            }
        }

        previous = travelled;
        travelled += stepLength * (1.0 + float(i) * STEP_GROWTH);
    }

    vec3 reflection = mix(SkyTintRefl.rgb, texture(SceneSampler, hitUv).rgb, hit);
    float fresnel = 0.02 + 0.98 * pow(1.0 - clamp(dot(normal, -incident), 0.0, 1.0), 5.0);
    float amount = clamp(reflectivity * (0.12 + 0.88 * fresnel), 0.0, 1.0) * mask;

    vec3 sunDir = SunDirWet.xyz;
    vec3 halfway = normalize(sunDir - incident);
    float specular = pow(max(dot(normal, halfway), 0.0), SPEC_POWER) * gloss * mask * max(sunDir.y, 0.0);

    vec3 lit = mix(soaked, reflection, amount) + specular;
    fragColor = vec4(clamp(lit, 0.0, 1.0), src.a);
}
