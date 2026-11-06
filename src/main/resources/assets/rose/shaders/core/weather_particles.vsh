#version 330

#moj_import <minecraft:fog.glsl>

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

in vec3 Position;

out vec2 ParticleUv;
out float ParticleSeed;
out float ParticleAlpha;
out float ParticleSphericalDistance;
out float ParticleCylindricalDistance;
flat out float ParticleType;
#ifdef ROSE_RAIN
out vec3 ParticleWorldPosition;
#endif

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
    const int GRID_SIZE = 64;
    const int GRID_AREA = GRID_SIZE * GRID_SIZE;
    const int TOTAL_INSTANCES = GRID_AREA * 2;

    int sourceId = (gl_InstanceID * 4051 + 7919) % TOTAL_INSTANCES;
    int localId = sourceId % GRID_AREA;
    int layer = sourceId / GRID_AREA;
    ivec2 originResidue = ivec2(mod(WeatherGrid.zw, float(GRID_SIZE)));
    int gridX = (localId % GRID_SIZE - originResidue.x + GRID_SIZE) % GRID_SIZE;
    int gridZ = (localId / GRID_SIZE - originResidue.y + GRID_SIZE) % GRID_SIZE;

    float range = max(Motion.z, 1.0);
    float cellSize = ParticleGrid.y;
    vec2 gridPosition = vec2(gridX, gridZ);
    vec2 cell = mod(WeatherGrid.zw + gridPosition, 4096.0);

    float seed = hash12(cell + vec2(float(layer) * 19.17, float(layer) * 7.31));
    vec2 jitter = hash22(cell + vec2(float(layer) * 13.7, float(layer) * 29.3));

    bool rain = FrameMode.z < 1.5;
    bool blizzard = FrameMode.z > 2.5;
    float fallSpan = ParticleGrid.x;
    float cameraTopPhase = ParticleGrid.z;
    float fallDistance = mod(
        cameraTopPhase + WeatherPhase.x + seed * fallSpan + float(layer) * fallSpan * 0.5,
        fallSpan
    );

    vec2 weatherOffset = vec2(0.0);
    if (!rain) {
        float flutterPhase = CameraTime.w * (1.1 + seed * 0.9) + seed * 31.0;
        float flutterAmplitude = 0.25 + seed * 0.2;
        if (blizzard) {
            flutterPhase *= 2.3;
            flutterAmplitude *= 3.0;
            weatherOffset += vec2(
                sin(CameraTime.w * 3.7 + seed * 47.0),
                cos(CameraTime.w * 3.1 + seed * 23.0)
            ) * 0.35;
        }
        weatherOffset += vec2(sin(flutterPhase), cos(flutterPhase * 0.73)) * flutterAmplitude;
    }

    vec3 center = vec3(
        WeatherGrid.x + (gridPosition.x + jitter.x) * cellSize + weatherOffset.x,
        fallSpan * 0.5 - fallDistance,
        WeatherGrid.y + (gridPosition.y + jitter.y) * cellSize + weatherOffset.y
    );

    float horizontalDistance = length(center.xz);
    float distanceFade = 1.0 - smoothstep(range * 0.72, range, horizontalDistance);
    float verticalFade = 1.0 - smoothstep(fallSpan * 0.42, fallSpan * 0.5, abs(center.y));
    bool particleActive = distanceFade * verticalFade > 0.001;

    ParticleUv = Position.xy;
    ParticleSeed = seed;
    ParticleType = FrameMode.z;
    ParticleAlpha = distanceFade * verticalFade * ParticleGrid.w;
#ifdef ROSE_RAIN
    ParticleWorldPosition = center;
    ParticleAlpha *= smoothstep(0.5, 1.6, length(center)) * mix(0.65, 1.0, seed);
#endif

    if (!particleActive || ParticleAlpha <= 0.001) {
        ParticleSphericalDistance = 0.0;
        ParticleCylindricalDistance = 0.0;
        ParticleAlpha = 0.0;
        gl_Position = vec4(2.0, 2.0, 2.0, 1.0);
        return;
    }

    vec3 vertexPosition;
    bool streak = blizzard && seed > 0.55;
    if (rain || streak) {
        vec3 fallDirection = ParticleMotion.xyz;
        vec3 viewDirection = normalize(-center);
        vec3 right = cross(fallDirection, viewDirection);
        if (dot(right, right) < 0.0001) {
            right = InverseView[0].xyz;
        } else {
            right = normalize(right);
        }

        float windSpeed = ParticleMotion.w;
        float halfWidth = (rain ? 0.014 : 0.032) * Motion.y * (0.75 + seed * 0.5);
        float halfLength = (rain ? 0.42 : 0.09 + windSpeed * 0.022) * Motion.y * (0.75 + seed * 0.5);
        float pixelWorld = 2.0 * length(center) / max(abs(Projection[1][1]) * FrameMode.y, 1.0);
#ifdef ROSE_RAIN
        float physicalWidth = 0.007 * Motion.y * (0.75 + seed * 0.5);
        halfWidth = max(physicalWidth, pixelWorld * 0.4);
        halfLength = mix(0.16, 0.34, seed) * Motion.y * sqrt(max(Motion.x, 0.01));
        vec3 along = cross(viewDirection, right);
        float alignment = dot(fallDirection, viewDirection);
        float projectedLength = sqrt(max(0.0, 1.0 - alignment * alignment));
        halfLength = max(halfWidth, halfLength * projectedLength);
        ParticleAlpha *= min(1.0, physicalWidth / halfWidth);
        vertexPosition = center + right * Position.x * halfWidth + along * Position.y * halfLength;
#else
        halfWidth = max(halfWidth, pixelWorld * 0.45);
        vertexPosition = center
            + right * Position.x * halfWidth
            + fallDirection * Position.y * halfLength;
#endif
    } else {
        vec3 cameraRight = InverseView[0].xyz;
        vec3 cameraUp = InverseView[1].xyz;
        float spin = CameraTime.w * (0.7 + seed * 1.5) + seed * 18.0;
        float radius = 0.09 * Motion.y * (0.7 + seed * 0.65);
        if (blizzard) {
            spin *= 2.6;
            radius *= 0.8;
        }
        float c = cos(spin);
        float s = sin(spin);
        vec2 rotated = mat2(c, -s, s, c) * Position.xy;
        float pixelWorld = 2.0 * length(center) / max(abs(Projection[1][1]) * FrameMode.y, 1.0);
        radius = max(radius, pixelWorld * 0.55);
        vertexPosition = center + (cameraRight * rotated.x + cameraUp * rotated.y) * radius;
    }

#ifdef ROSE_RAIN
    ParticleWorldPosition = vertexPosition;
#endif
    vec4 viewPosition = View * vec4(vertexPosition, 1.0);
    ParticleSphericalDistance = fog_spherical_distance(viewPosition.xyz);
    ParticleCylindricalDistance = fog_cylindrical_distance(viewPosition.xyz);
    gl_Position = Projection * viewPosition;
}
