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

in vec2 ParticleUv;
in float ParticleSeed;
in float ParticleAlpha;
in float ParticleSphericalDistance;
in float ParticleCylindricalDistance;
flat in float ParticleType;
#ifdef ROSE_RAIN
in vec3 ParticleWorldPosition;
#endif

out vec4 fragColor;

void main() {
#ifdef ROSE_RAIN
    if (rainRoofDistance(ParticleWorldPosition) < -0.02) discard;
#endif
    float alpha;
    bool streakShape = ParticleType < 1.5 || (ParticleType > 2.5 && ParticleSeed > 0.55);
    if (streakShape) {
#ifdef ROSE_RAIN
        float taper = mix(0.35, 1.0, smoothstep(-1.0, 0.6, ParticleUv.y));
        float edge = 1.0 - smoothstep(0.0, 1.0, abs(ParticleUv.x) / taper);
        float ends = smoothstep(-1.0, -0.55, ParticleUv.y) * (1.0 - smoothstep(0.65, 1.0, ParticleUv.y));
        alpha = edge * edge * ends * mix(0.45, 0.9, ParticleUv.y * 0.5 + 0.5);
#else
        float edge = 1.0 - smoothstep(0.08, 0.92, abs(ParticleUv.x));
        float ends = 1.0 - smoothstep(0.62, 1.0, abs(ParticleUv.y));
        float highlight = mix(0.28, 0.85, ParticleUv.y * 0.5 + 0.5);
        alpha = edge * edge * ends * highlight;
#endif
    } else if (ParticleType > 2.5) {
        float radius = length(ParticleUv);
        float antialias = max(fwidth(radius) * 1.5, 0.01);
        float outer = 1.0 - smoothstep(0.55 - antialias, 1.0, radius);
        alpha = outer * outer * (0.85 + 0.15 * sin(ParticleSeed * 41.0));
    } else {
        float radius = length(ParticleUv);
        float angle = atan(ParticleUv.y, ParticleUv.x);
        float antialias = max(fwidth(radius) * 1.5, 0.01);
        float outer = 1.0 - smoothstep(0.78 - antialias, 1.0, radius);
        float armDistance = abs(sin(angle * 3.0)) * radius;
        float arms = 1.0 - smoothstep(0.035 + antialias, 0.12 + antialias, armDistance);
        float center = 1.0 - smoothstep(0.12, 0.3, radius);
        float sparkle = 0.82 + 0.18 * sin(ParticleSeed * 41.0);
        alpha = max(center, arms * outer) * outer * sparkle;
    }

    alpha *= ParticleAlpha;
    if (alpha <= 0.002) discard;

    vec4 color = vec4(WeatherColor.rgb, alpha);
    float sphericalDistance = ParticleSphericalDistance;
    if (Motion.w < 0.5) {
        color = apply_fog(
            color,
            sphericalDistance,
            ParticleCylindricalDistance,
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
