#ifdef ROSE_RAIN
layout(std140) uniform RainRoof {
    vec4 RoofCamera;
};

uniform sampler2D RoofSampler;

float rainRoofDistance(vec3 relativeWorld) {
    vec2 cell = floor(relativeWorld.xz + RoofCamera.xy);
    vec2 uv = fract((cell + 0.5) * RoofCamera.w);
    vec2 packed = floor(texture(RoofSampler, uv).rg * 255.0 + 0.5);
    float height = packed.x + packed.y * 256.0 - 32768.0;
    return relativeWorld.y + RoofCamera.z - height;
}
#endif
