#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

layout(std140) uniform EspGlowSurface {
    vec4 AnchorInfo;
    vec4 WorldRight;
    vec4 WorldUp;
    vec4 WorldForward;
    vec4 Centers[64];
};

in vec3 Position;
in vec2 UV0;
out vec2 texCoord0;
out vec3 localPosition;

void main() {
    vec4 position = ModelViewMat * vec4(Position, 1.0);
    gl_Position = ProjMat * position;
    texCoord0 = UV0;
    vec4 center = vec4(position.xyz, 1.0);
    float nearest = 1e30;
    for (int i = 0; i < min(int(AnchorInfo.x), 64); i++) {
        vec3 delta = position.xyz - Centers[i].xyz;
        float distanceSquared = dot(delta, delta);
        if (distanceSquared < nearest) {
            nearest = distanceSquared;
            center = Centers[i];
        }
    }
    vec3 relative = (position.xyz - center.xyz) / max(center.w, 0.001);
    localPosition = vec3(dot(relative, WorldRight.xyz), dot(relative, WorldUp.xyz), dot(relative, WorldForward.xyz));
}
