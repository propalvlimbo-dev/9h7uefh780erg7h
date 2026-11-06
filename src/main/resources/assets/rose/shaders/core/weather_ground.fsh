#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <rose:rain_roof.glsl>

layout(std140) uniform Weather {
    mat4 Projection;
    mat4 View;
    mat4 InverseProjection;
    mat4 InverseView;
    vec4 CameraTime;
    vec4 FrameMode;
    vec4 Motion;
    vec4 WindGround;
    vec4 WeatherColor;
    vec4 WeatherPhase;
    vec4 WeatherGrid;
    vec4 ParticleMotion;
    vec4 ParticleGrid;
};

uniform sampler2D DepthSampler;

in vec2 TexCoord;

out vec4 fragColor;

float hash12(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

vec2 hash22(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * vec3(0.1031, 0.1030, 0.0973));
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.xx + p3.yz) * p3.zy);
}

void main() {
    float rawDepth = texture(DepthSampler, TexCoord).r;
    if (rawDepth <= 0.000001) discard;

    vec4 viewHomogeneous = InverseProjection * vec4(TexCoord * 2.0 - 1.0, ROSE_DEPTH_TO_CLIP(rawDepth), 1.0);
    if (abs(viewHomogeneous.w) < 0.000001) discard;

    vec3 viewPosition = viewHomogeneous.xyz / viewHomogeneous.w;
    float cameraDistanceSquared = dot(viewPosition, viewPosition);
    float weatherRange = Motion.z;
    if (cameraDistanceSquared > weatherRange * weatherRange) discard;
    vec3 relativeWorld = mat3(InverseView) * viewPosition;

    bool rain = FrameMode.z < 1.5;
    vec2 worldPosition = CameraTime.xz + relativeWorld.xz;
#ifdef ROSE_RAIN
    float surfacePixel = max(length(dFdx(worldPosition)), length(dFdy(worldPosition))) * 0.65;
#endif

    vec3 normal = normalize(cross(dFdx(relativeWorld), dFdy(relativeWorld)));
    vec3 towardCamera = normalize(-relativeWorld);
    if (dot(normal, towardCamera) < 0.0) normal = -normal;
    float upwardSurface = smoothstep(0.45, 0.85, normal.y);
    if (upwardSurface <= 0.001) discard;
#ifdef ROSE_RAIN
    if (rainRoofDistance(relativeWorld) < -1.02) discard;
#endif

    float cellSize = rain ? max(0.85, WindGround.w * 2.6) : 1.25;
    vec2 cell = floor(worldPosition / cellSize);
    vec2 hashCell = mod(cell, 4096.0);
    float seed = hash12(hashCell + vec2(9.17, 31.73));
    float intensity = clamp(FrameMode.w, 0.0, 1.0);
    float impactsPerSecond = mix(rain ? 0.8 : 0.22, rain ? 3.0 : 0.9, intensity);
    float lifetime = max(WindGround.z, 0.01);
    float cycle = max(1.0 / max(impactsPerSecond, 0.01), lifetime * 1.12);
    float localTime = mod(WeatherPhase.w + seed * cycle, cycle);
    if (localTime > lifetime) discard;
    float age = clamp(localTime / lifetime, 0.0, 1.0);

    vec2 impactCenter = (cell + 0.12 + hash22(hashCell + vec2(17.1, 4.7)) * 0.76) * cellSize;
#ifdef ROSE_RAIN
    float cycleIndex = floor((WeatherPhase.w + seed * cycle) / cycle);
    float padding = (WindGround.w + 0.015) / cellSize;
    impactCenter = (cell + padding + hash22(hashCell + vec2(17.1, 4.7) + mod(cycleIndex, 4096.0))
        * (1.0 - 2.0 * padding)) * cellSize;
#endif
    float distanceToImpact = length(worldPosition - impactCenter);

    float groundRadius = max(WindGround.w, 0.01);
    float antialias = max(fwidth(distanceToImpact) * 0.75, 0.004);
    float effectAlpha;
    if (rain) {
#ifdef ROSE_RAIN
        antialias = max(surfacePixel, 0.002);
#endif
        float ringRadius = groundRadius * mix(0.12, 1.0, age) * mix(0.7, 1.0, seed);
        float width = max(0.005, groundRadius * 0.018);
        float ring = 1.0 - smoothstep(max(0.0, width - antialias), width + antialias,
            abs(distanceToImpact - ringRadius));
        ring *= width / max(width, antialias);
        float dropRadius = max(groundRadius * 0.07, antialias);
        float splash = (1.0 - smoothstep(0.0, dropRadius, distanceToImpact)) * exp(-age * 14.0);
        effectAlpha = (ring * 0.32 * smoothstep(0.0, 0.08, age) + splash * 0.38)
            * (1.0 - age) * (1.0 - age);
    } else {
        float puffRadius = groundRadius * (0.22 + age * 0.78);
        float puff = 1.0 - smoothstep(puffRadius * 0.42, puffRadius, distanceToImpact);
        float rim = 1.0 - smoothstep(
            antialias,
            antialias * 3.0,
            abs(distanceToImpact - puffRadius * 0.72)
        );
        effectAlpha = (puff * 0.34 + rim * 0.48) * sin(age * 3.14159265);
    }

    float cameraDistance = sqrt(cameraDistanceSquared);
    float distanceFade = 1.0 - smoothstep(Motion.z * 0.55, Motion.z, cameraDistance);
    float alpha = effectAlpha * upwardSurface * distanceFade * intensity * WeatherColor.a;
    if (alpha <= 0.002) discard;

    vec4 color = vec4(WeatherColor.rgb, alpha);
    float sphericalDistance = cameraDistance;
    if (Motion.w < 0.5) {
        color = apply_fog(
            color,
            sphericalDistance,
            fog_cylindrical_distance(viewPosition),
            FogEnvironmentalStart,
            FogEnvironmentalEnd,
            FogRenderDistanceStart,
            FogRenderDistanceEnd,
            FogColor
        );
    } else {
        float fogStart = min(FogRenderDistanceStart, FogEnvironmentalStart);
        float fogEnd = max(FogRenderDistanceEnd, FogEnvironmentalEnd);
        color.a *= 1.0 - smoothstep(fogStart, max(fogEnd, fogStart + 0.001), sphericalDistance);
    }
    fragColor = color;
}
