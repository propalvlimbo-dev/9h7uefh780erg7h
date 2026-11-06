#version 330

layout(std140) uniform RainEffectData {
    mat4 InverseProjection;
    mat4 InverseWorldView;
    mat4 ReflectionVP[3];
    vec4 PlaneData;
    vec4 EffectData;
    vec4 MotionData;
    vec4 SplashPosition[32];
    vec4 SplashData[32];
    vec4 SplashMeta;
};

uniform sampler2D SceneSampler;
uniform sampler2D MainDepthSampler;
uniform sampler2D ReflectionSampler0;
uniform sampler2D ReflectionSampler1;
uniform sampler2D ReflectionSampler2;
in vec2 TexCoord;
out vec4 fragColor;

vec3 reconstructView(vec2 uv, float depth) {
    vec4 clip = vec4(uv * 2.0 - 1.0, ROSE_DEPTH_TO_CLIP(depth), 1.0);
    vec4 view = InverseProjection * clip;
    return view.xyz / max(abs(view.w), 0.000001);
}

vec3 reconstructWorld(vec2 uv, float depth) {
    return (InverseWorldView * vec4(reconstructView(uv, depth), 1.0)).xyz;
}

float medianHeight(float first, float second, float third) {
    return first + second + third
            - min(first, min(second, third))
            - max(first, max(second, third));
}

bool upwardSurface(ivec2 pixel, vec3 center, vec3 viewOrigin,
                   out float surfaceHeight, out vec3 surfaceNormal) {
    ivec2 size = textureSize(MainDepthSampler, 0);
    ivec2 minimumPixel = ivec2(0);
    ivec2 maximumPixel = size - ivec2(1);
    ivec2 leftPixel = clamp(pixel + ivec2(-1, 0), minimumPixel, maximumPixel);
    ivec2 rightPixel = clamp(pixel + ivec2(1, 0), minimumPixel, maximumPixel);
    ivec2 downPixel = clamp(pixel + ivec2(0, -1), minimumPixel, maximumPixel);
    ivec2 upPixel = clamp(pixel + ivec2(0, 1), minimumPixel, maximumPixel);

    float leftDepth = texelFetch(MainDepthSampler, leftPixel, 0).r;
    float rightDepth = texelFetch(MainDepthSampler, rightPixel, 0).r;
    float downDepth = texelFetch(MainDepthSampler, downPixel, 0).r;
    float upDepth = texelFetch(MainDepthSampler, upPixel, 0).r;
    bool leftValid = leftDepth > 0.0;
    bool rightValid = rightDepth > 0.0;
    bool downValid = downDepth > 0.0;
    bool upValid = upDepth > 0.0;
    if ((!leftValid && !rightValid) || (!downValid && !upValid)) return false;

    vec2 inverseSize = 1.0 / vec2(size);
    vec3 left = leftValid ? reconstructWorld((vec2(leftPixel) + 0.5) * inverseSize, leftDepth) : center;
    vec3 right = rightValid ? reconstructWorld((vec2(rightPixel) + 0.5) * inverseSize, rightDepth) : center;
    vec3 down = downValid ? reconstructWorld((vec2(downPixel) + 0.5) * inverseSize, downDepth) : center;
    vec3 up = upValid ? reconstructWorld((vec2(upPixel) + 0.5) * inverseSize, upDepth) : center;

    bool useLeft = leftValid && (!rightValid || distance(center, left) <= distance(center, right));
    bool useDown = downValid && (!upValid || distance(center, down) <= distance(center, up));
    vec3 horizontalEndpoint = useLeft ? left : right;
    vec3 verticalEndpoint = useDown ? down : up;
    vec3 horizontal = useLeft ? center - left : right - center;
    vec3 vertical = useDown ? center - down : up - center;
    surfaceHeight = medianHeight(center.y, horizontalEndpoint.y, verticalEndpoint.y);
    vec3 normal = cross(horizontal, vertical);
    float normalLength = length(normal);
    if (normalLength <= 0.000001) return false;

    normal /= normalLength;
    if (dot(normal, viewOrigin - center) < 0.0) normal = -normal;
    surfaceNormal = normal;
    vec3 magnitude = abs(normal);
    return magnitude.y > magnitude.x && magnitude.y > magnitude.z && normal.y > 0.0;
}

float planeHeight(int index) {
    if (index == 0) return PlaneData.x;
    if (index == 1) return PlaneData.y;
    return PlaneData.z;
}

int closestPlane(float surfaceHeight) {
    int count = max(1, int(PlaneData.w + 0.5));
    int selected = 0;
    float distanceToPlane = abs(surfaceHeight - PlaneData.x);
    for (int index = 1; index < count; index++) {
        float candidate = abs(surfaceHeight - planeHeight(index));
        if (candidate < distanceToPlane) {
            selected = index;
            distanceToPlane = candidate;
        }
    }
    return selected;
}

vec4 reflectionSample(vec2 uv, int index) {
    if (index == 0) return texture(ReflectionSampler0, uv);
    if (index == 1) return texture(ReflectionSampler1, uv);
    return texture(ReflectionSampler2, uv);
}

vec2 hash22(vec2 position) {
    vec3 value = fract(vec3(position.xyx) * vec3(0.1031, 0.1030, 0.0973));
    value += dot(value, value.yzx + 33.33);
    return fract((value.xx + value.yz) * value.zy);
}

float valueNoise(vec2 position) {
    vec2 cell = floor(position);
    vec2 local = fract(position);
    local = local * local * (3.0 - 2.0 * local);
    float bottomLeft = hash22(cell).x;
    float bottomRight = hash22(cell + vec2(1.0, 0.0)).x;
    float topLeft = hash22(cell + vec2(0.0, 1.0)).x;
    float topRight = hash22(cell + vec2(1.0, 1.0)).x;
    return mix(mix(bottomLeft, bottomRight, local.x), mix(topLeft, topRight, local.x), local.y);
}

float ringBand(float distanceToCenter, float radius, float width) {
    return 1.0 - smoothstep(width * 0.35, width, abs(distanceToCenter - radius));
}

float splashRandom(float value) {
    return fract(sin(value * 91.713 + 17.127) * 43758.5453);
}

void walkingSplashEffect(vec3 relativeWorld,
                         out float rippleMask, out float impactMask, out vec2 rippleWarp) {
    rippleMask = 0.0;
    impactMask = 0.0;
    rippleWarp = vec2(0.0);
    int count = min(32, max(0, int(SplashMeta.x + 0.5)));
    for (int index = 0; index < 32; index++) {
        if (index >= count) break;
        vec4 splashPosition = SplashPosition[index];
        float age = clamp(splashPosition.w, 0.0, 1.0);
        if (age >= 1.0 || abs(relativeWorld.y - splashPosition.z) > 0.14) continue;

        vec4 splashData = SplashData[index];
        float power = max(0.2, splashData.w);
        vec2 forward = normalize(splashData.xy);
        vec2 right = vec2(forward.y, -forward.x);
        vec2 delta = relativeWorld.xz - splashPosition.xy;
        float maximumRadius = 0.34 + power * 1.38;
        if (dot(delta, delta) > maximumRadius * maximumRadius) continue;

        vec2 local = vec2(dot(delta, right), dot(delta, forward));
        vec2 unevenLocal = vec2(local.x * 1.06, local.y * 0.90);
        float radialDistance = length(unevenLocal);
        float angle = atan(unevenLocal.y, unevenLocal.x);
        float seed = splashData.z;
        float edgeWarp = sin(angle * 5.0 + seed * 31.0) * 0.030
                + sin(angle * 9.0 - seed * 47.0) * 0.014;
        float fade = 1.0 - smoothstep(0.48, 1.0, age);

        float firstRadius = mix(0.035, 1.08 * power, pow(age, 0.72));
        float firstWidth = mix(0.030, 0.072, age) * mix(0.85, 1.25, power);
        float firstRing = ringBand(radialDistance + edgeWarp * fade, firstRadius, firstWidth) * fade;

        float secondAge = clamp((age - 0.11) / 0.89, 0.0, 1.0);
        float secondRadius = mix(0.025, 0.78 * power, pow(secondAge, 0.74));
        float secondRing = secondAge > 0.0
                ? ringBand(radialDistance - edgeWarp * 0.7, secondRadius, firstWidth * 0.78)
                        * (1.0 - smoothstep(0.42, 1.0, secondAge)) * 0.72
                : 0.0;

        float wakeAge = clamp((age - 0.035) / 0.965, 0.0, 1.0);
        vec2 wakeCenter = vec2(0.0, -wakeAge * 0.31 * power);
        float wakeDistance = length((local - wakeCenter) * vec2(1.18, 0.77));
        float wakeRadius = mix(0.07, 0.58 * power, pow(wakeAge, 0.82));
        float wake = ringBand(wakeDistance, wakeRadius, firstWidth * 1.15)
                * smoothstep(-0.16, 0.10, local.y) * fade * 0.52;

        float satellite = 0.0;
        for (int droplet = 0; droplet < 4; droplet++) {
            float id = float(droplet);
            float delay = 0.045 + splashRandom(seed * 113.0 + id * 17.0) * 0.14;
            float dropletAge = clamp((age - delay) / max(1.0 - delay, 0.01), 0.0, 1.0);
            float dropletAngle = splashRandom(seed * 71.0 + id * 29.0) * 6.2831853;
            float dropletDistance = mix(0.16, 0.42, splashRandom(seed * 157.0 + id * 43.0)) * power;
            vec2 dropletCenter = vec2(cos(dropletAngle), sin(dropletAngle)) * dropletDistance;
            float localDistance = length(delta - dropletCenter);
            float radius = dropletAge * mix(0.09, 0.22, splashRandom(seed * 199.0 + id * 11.0)) * power;
            float ring = dropletAge > 0.0
                    ? ringBand(localDistance, radius, 0.024 + dropletAge * 0.018)
                            * (1.0 - smoothstep(0.35, 1.0, dropletAge))
                    : 0.0;
            satellite = max(satellite, ring * 0.62);
        }

        float impact = (1.0 - smoothstep(0.0, 0.19 * power, radialDistance))
                * (1.0 - smoothstep(0.0, 0.22, age));
        float combinedRing = max(max(firstRing, secondRing), max(wake, satellite));
        rippleMask = max(rippleMask, combinedRing);
        impactMask = max(impactMask, impact);

        vec2 radialDirection = radialDistance > 0.0001
                ? normalize(delta + forward * 0.001)
                : forward;
        float wavePhase = sin((radialDistance - firstRadius) * 31.0) * firstRing
                + sin((radialDistance - secondRadius) * 37.0) * secondRing * 0.65;
        rippleWarp += radialDirection * wavePhase * fade * power;
        rippleWarp += forward * wake * 0.34 * power;
    }
    rippleMask = clamp(rippleMask * SplashMeta.y, 0.0, 1.0);
    impactMask = clamp(impactMask * SplashMeta.y, 0.0, 1.0);
    rippleWarp *= SplashMeta.y;
}

float wetSpotPattern(vec2 worldPosition) {
    vec2 warp = vec2(
            sin(dot(worldPosition, vec2(0.13, 0.07)) + 1.4),
            sin(dot(worldPosition, vec2(-0.08, 0.16)) - 0.9)
    ) * 1.35;
    vec2 position = worldPosition + warp;
    float broad = valueNoise(position * 0.16 + vec2(11.7, -23.4));
    float detail = valueNoise(position * 0.39 + vec2(-37.1, 8.6));
    float scattered = smoothstep(0.56, 0.80, broad * 0.72 + detail * 0.28);
    return mix(0.90, 1.08, scattered);
}

vec2 rainImpactLayer(vec2 worldPosition, float time, float cellSize,
                     float speed, float phase, out float ringMask, out float coreMask) {
    vec2 cellPosition = worldPosition / cellSize;
    vec2 cell = floor(cellPosition);
    vec2 local = fract(cellPosition);
    vec2 random = hash22(cell + vec2(phase * 17.0, phase * -23.0));
    vec2 center = 0.16 + random * 0.68;
    vec2 delta = (local - center) * cellSize;
    float distanceToCenter = length(delta);
    float cycle = fract(time * speed + random.x + phase);
    float activeWindow = 1.0 - step(0.72, cycle);
    float age = clamp(cycle / 0.72, 0.0, 1.0);
    float radius = pow(age, 0.68) * mix(0.48, 0.92, random.y);
    float width = mix(0.025, 0.070, age);
    float angularWarp = sin(atan(delta.y, delta.x) * 6.0 + random.x * 29.0) * 0.020 * (1.0 - age);
    float ring = ringBand(distanceToCenter + angularWarp, radius, width);
    float boundary = min(min(local.x, local.y), min(1.0 - local.x, 1.0 - local.y));
    float boundaryFade = smoothstep(0.0, 0.16, boundary);
    float fade = (1.0 - smoothstep(0.42, 1.0, age)) * activeWindow * boundaryFade;
    ringMask = ring * fade;
    coreMask = (1.0 - smoothstep(0.0, 0.085 + random.y * 0.035, distanceToCenter))
            * (1.0 - smoothstep(0.0, 0.16, age)) * activeWindow * boundaryFade;
    vec2 direction = delta / max(distanceToCenter, 0.001);
    float wave = sin((distanceToCenter - radius) * 38.0) * ringMask;
    return direction * (wave + coreMask * 0.28);
}

vec2 reflectionMotion(vec2 worldPosition, float time,
                      out float wetPatch, out float impactMask, out float coreMask) {
    vec2 firstDirection = vec2(0.8192, 0.5735);
    vec2 secondDirection = vec2(-0.6258, 0.7799);
    vec2 thirdDirection = vec2(0.2588, 0.9659);
    float first = dot(worldPosition, firstDirection) * 1.05 + time * 0.62;
    float second = dot(worldPosition, secondDirection) * 1.68 - time * 0.91;
    float third = dot(worldPosition, thirdDirection) * 0.47 + time * 0.23;
    vec2 flow = firstDirection * cos(first) * 0.52
            + secondDirection * cos(second) * 0.34
            + thirdDirection * sin(third) * 0.26;
    float firstRing;
    float secondRing;
    float thirdRing;
    float firstCore;
    float secondCore;
    float thirdCore;
    vec2 firstImpact = rainImpactLayer(worldPosition, time, 2.15, 0.58, 0.07, firstRing, firstCore);
    vec2 secondImpact = rainImpactLayer(worldPosition + vec2(13.7, -8.4), time, 3.35, 0.41, 0.39, secondRing, secondCore);
    vec2 thirdImpact = rainImpactLayer(worldPosition + vec2(-21.1, 17.3), time, 5.40, 0.29, 0.71, thirdRing, thirdCore);
    float activity = clamp(MotionData.z, 0.0, 3.0);
    impactMask = clamp(max(firstRing, max(secondRing * 0.82, thirdRing * 0.68)) * activity, 0.0, 1.0);
    coreMask = clamp(max(firstCore, max(secondCore * 0.85, thirdCore * 0.72)) * activity, 0.0, 1.0);
    vec2 impact = (firstImpact + secondImpact * 0.78 + thirdImpact * 0.58) * activity;
    float firstPatch = sin(dot(worldPosition, vec2(0.31, 0.17)) + time * 0.075);
    float secondPatch = sin(dot(worldPosition, vec2(-0.19, 0.28)) - time * 0.052 + 1.7);
    float crossingWave = sin(dot(worldPosition, vec2(0.71, -0.48)) * 2.2 + time * 1.35)
            * sin(dot(worldPosition, vec2(-0.37, 0.93)) * 1.7 - time * 1.08);
    wetPatch = clamp(0.5 + firstPatch * 0.25 + secondPatch * 0.21 + crossingWave * 0.12 * activity, 0.0, 1.0);
    vec2 microWave = vec2(cos(first * 2.7 + second), sin(second * 2.2 - third)) * 0.13 * activity;
    return flow * (1.0 + activity * 0.10) + impact * 1.85 + microWave;
}

vec2 reflectionTexel(int index) {
    if (index == 0) return 1.0 / vec2(textureSize(ReflectionSampler0, 0));
    if (index == 1) return 1.0 / vec2(textureSize(ReflectionSampler1, 0));
    return 1.0 / vec2(textureSize(ReflectionSampler2, 0));
}

vec4 softenedReflection(vec2 uv, int index, float roughness) {
    vec2 texel = reflectionTexel(index);
    vec2 offset = texel * mix(0.65, 2.35, roughness);
    vec2 minimumUv = texel * 0.5;
    vec2 maximumUv = vec2(1.0) - minimumUv;
    vec4 center = reflectionSample(clamp(uv, minimumUv, maximumUv), index) * 0.44;
    vec4 horizontal = reflectionSample(clamp(uv + vec2(offset.x, 0.0), minimumUv, maximumUv), index)
            + reflectionSample(clamp(uv - vec2(offset.x, 0.0), minimumUv, maximumUv), index);
    vec4 vertical = reflectionSample(clamp(uv + vec2(0.0, offset.y), minimumUv, maximumUv), index)
            + reflectionSample(clamp(uv - vec2(0.0, offset.y), minimumUv, maximumUv), index);
    return center + (horizontal + vertical) * 0.14;
}

void main() {
    ivec2 pixel = ivec2(gl_FragCoord.xy);
    ivec2 screenSize = textureSize(MainDepthSampler, 0);
    vec2 pixelUv = (vec2(pixel) + 0.5) / vec2(screenSize);
    float depth = texelFetch(MainDepthSampler, pixel, 0).r;
    if (depth <= 0.0) discard;

    vec3 worldPosition = reconstructWorld(pixelUv, depth);
    vec3 viewOrigin = (InverseWorldView * vec4(0.0, 0.0, 0.0, 1.0)).xyz;
    vec3 cameraRelativeWorld = worldPosition - viewOrigin;
    float cameraDistanceSquared = dot(cameraRelativeWorld, cameraRelativeWorld);
    if (cameraDistanceSquared >= 20736.0) discard;
    float cameraDistance = sqrt(cameraDistanceSquared);
    float surfaceHeight;
    vec3 surfaceNormal;
    if (!upwardSurface(pixel, worldPosition, viewOrigin, surfaceHeight, surfaceNormal)) discard;
    float wetSpotVisibility = wetSpotPattern(worldPosition.xz);
    float walkingRipple;
    float walkingImpact;
    vec2 walkingWarp;
    walkingSplashEffect(cameraRelativeWorld, walkingRipple, walkingImpact, walkingWarp);

    int planeIndex = closestPlane(surfaceHeight);

    float captureHeight = planeHeight(planeIndex);
    vec3 reflectionPosition = worldPosition;
    float planeOffset = surfaceHeight - captureHeight;
    reflectionPosition.y = abs(planeOffset) <= 0.14
            ? captureHeight
            : captureHeight - planeOffset;
    vec3 scene = texelFetch(SceneSampler, pixel, 0).rgb;
    vec4 reflected = vec4(scene, 0.0);
    float edgeFade = 0.0;
    bool reflectionValid = false;
    vec4 reflectedClip = ReflectionVP[planeIndex] * vec4(reflectionPosition, 1.0);
    float wetPatch;
    float rainImpactMask;
    float rainCoreMask;
    vec2 waterMotion = reflectionMotion(worldPosition.xz, MotionData.y, wetPatch, rainImpactMask, rainCoreMask);
    if (reflectedClip.w > 0.00001) {
        vec2 reflectedUv = reflectedClip.xy / reflectedClip.w * 0.5 + 0.5;
        float motionScale = MotionData.x * 0.0025 + MotionData.x * MotionData.x * 0.0015
                + MotionData.z * 0.0009;
        reflectedUv += waterMotion * motionScale + walkingWarp * 0.0065;
        bool insideReflection = all(greaterThan(reflectedUv, vec2(0.0)))
                && all(lessThan(reflectedUv, vec2(1.0)));
        if (insideReflection) {
            float edgeDistance = min(min(reflectedUv.x, reflectedUv.y), min(1.0 - reflectedUv.x, 1.0 - reflectedUv.y));
            edgeFade = smoothstep(0.0, 0.045, edgeDistance);
            reflected = softenedReflection(reflectedUv, planeIndex, EffectData.y);
            reflectionValid = reflected.a > 0.001;
        }
    }

    vec3 viewDirection = -cameraRelativeWorld / max(cameraDistance, 0.000001);
    float grazing = 1.0 - clamp(dot(surfaceNormal, viewDirection), 0.0, 1.0);
    float fresnel = grazing * grazing * grazing;
    float distanceFade = 1.0 - smoothstep(72.0, 144.0, cameraDistance);
    float wetness = clamp(EffectData.w, 0.0, 1.0);
    float detailStrength = clamp(MotionData.x / 2.5, 0.0, 1.0);
    float animatedVisibility = mix(1.0, mix(0.72, 1.18, wetPatch) + rainImpactMask * 0.18, detailStrength);
    float puddleResponse = smoothstep(0.92, 1.065, wetSpotVisibility);
    float puddleWave = smoothstep(0.64, 0.94, wetPatch)
            * clamp(MotionData.z * 0.28, 0.0, 0.72) * wetness
            * mix(0.30, 1.0, puddleResponse);
    float ambientRainReaction = clamp(rainImpactMask * 0.62 + rainCoreMask * 0.82, 0.0, 1.0)
            * wetness * mix(0.58, 1.0, puddleResponse);
    float opacity = reflectionValid
            ? EffectData.x
                    * mix(0.28, 1.0, fresnel)
                    * edgeFade
                    * distanceFade
                    * wetness
                    * wetSpotVisibility
                    * animatedVisibility
                    * reflected.a
            : 0.0;
    opacity = clamp(opacity, 0.0, 0.82);
    float splashVisibility = clamp(walkingRipple * 0.54 + walkingImpact * 0.42, 0.0, 1.0);
    opacity = max(opacity, splashVisibility * distanceFade * 0.58);
    opacity = max(opacity, ambientRainReaction * distanceFade * 0.52);
    opacity = max(opacity, puddleWave * distanceFade * 0.18);
    if (opacity <= 0.001) discard;

    float darkening = EffectData.z * mix(0.75, 0.25, fresnel) * wetness
            + walkingImpact * 0.035 + walkingRipple * 0.012
            + rainCoreMask * wetness * 0.045 + puddleWave * 0.020;
    darkening = clamp(darkening, 0.0, 0.32);
    vec3 wetSurface = scene * (1.0 - darkening);
    float reflectionMix = reflectionValid
            ? mix(0.52, 0.78, fresnel) * mix(1.0, 0.82, EffectData.y)
                    + walkingRipple * 0.08
            : 0.0;
    reflectionMix = clamp(reflectionMix, 0.0, 0.9);
    vec3 composite = mix(wetSurface, reflected.rgb, reflectionMix);
    vec3 rippleSheen = reflectionValid
            ? mix(scene, reflected.rgb, 0.48 + fresnel * 0.18)
            : min(scene * 1.075, vec3(1.0));
    float sheenAmount = walkingRipple * 0.30 + walkingImpact * 0.18
            + rainImpactMask * wetness * 0.27 + rainCoreMask * wetness * 0.22 + puddleWave * 0.11;
    composite = mix(composite, rippleSheen, clamp(sheenAmount, 0.0, 0.58));
    fragColor = vec4(composite, opacity);
}
