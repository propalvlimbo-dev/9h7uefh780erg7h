#version 330

layout(std140) uniform TNTSphere {
    mat4 Projection;
    mat4 View;
    vec4 Screen;
};

uniform sampler2D SceneSampler;

in vec3 ViewPosition;
in vec3 ViewNormal;
in vec2 SphereUv;
in vec2 SphereData;
in vec4 SphereColor;

out vec4 fragColor;

void main() {
    vec3 normal = normalize(ViewNormal);
    vec3 eye = normalize(-ViewPosition);
    float facing = dot(normal, eye);
    bool cameraInside = SphereData.y < 0.0;
    if ((!cameraInside && facing <= 0.0) || (cameraInside && facing >= 0.0)) discard;

    float fresnel = 1.0 - clamp(abs(facing), 0.0, 1.0);
    float front = smoothstep(0.54, 0.98, fresnel);
    front *= front;
    float latitudeRipple = 0.88 + 0.12 * sin(SphereUv.y * 43.0 + SphereUv.x * 17.0);
    float longitudeRipple = 0.90 + 0.10 * sin(SphereUv.x * 71.0 - SphereUv.y * 13.0);
    front *= latitudeRipple * longitudeRipple;
    if (front <= 0.008) discard;

    vec2 screenSize = max(Screen.xy, vec2(1.0));
    vec2 screenUv = gl_FragCoord.xy / screenSize;
    float distanceToCamera = max(length(ViewPosition), 0.2);
    float sphereRadius = abs(SphereData.y);
    float perspectiveScale = clamp(sphereRadius / distanceToCamera, 0.025, 1.2);
    vec2 projectedNormal = vec2(normal.x * Screen.z, normal.y * Screen.w);
    float projectedLength = length(projectedNormal);
    vec2 direction = projectedLength > 0.000001
            ? projectedNormal / projectedLength
            : vec2(0.0);

    float signedStrength = SphereData.x;
    float pressure = signedStrength * front;
    float compression = pressure * perspectiveScale * (0.018 + fresnel * 0.034);
    vec2 refractOffset = direction * compression;
    vec2 sampleUv = clamp(screenUv - refractOffset, vec2(0.001), vec2(0.999));

    float blurRadius = abs(pressure) * perspectiveScale * (0.0015 + fresnel * 0.0055);
    vec2 radialStep = direction * blurRadius;
    vec2 tangentStep = vec2(-direction.y, direction.x) * blurRadius * 0.62;
    vec3 scene = texture(SceneSampler, sampleUv).rgb * 0.38;
    scene += texture(SceneSampler, clamp(sampleUv + radialStep, vec2(0.001), vec2(0.999))).rgb * 0.17;
    scene += texture(SceneSampler, clamp(sampleUv - radialStep, vec2(0.001), vec2(0.999))).rgb * 0.17;
    scene += texture(SceneSampler, clamp(sampleUv + tangentStep, vec2(0.001), vec2(0.999))).rgb * 0.12;
    scene += texture(SceneSampler, clamp(sampleUv - tangentStep, vec2(0.001), vec2(0.999))).rgb * 0.12;
    scene += texture(SceneSampler, clamp(sampleUv + radialStep * 2.0, vec2(0.001), vec2(0.999))).rgb * 0.02;
    scene += texture(SceneSampler, clamp(sampleUv - radialStep * 2.0, vec2(0.001), vec2(0.999))).rgb * 0.02;

    vec3 accent = clamp(SphereColor.rgb, 0.0, 1.0);
    float accentAmount = clamp(front * SphereColor.a * 0.016, 0.0, 0.018);
    scene += scene * accent * accentAmount;
    fragColor = vec4(scene, 1.0);
}
