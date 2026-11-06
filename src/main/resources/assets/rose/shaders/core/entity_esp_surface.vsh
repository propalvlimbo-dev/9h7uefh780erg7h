#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

layout(std140) uniform EspSurfaceAnchors {
    vec4 AnchorInfo;
    vec4 WorldUp;
    vec4 Centers[64];
};

in vec3 Position;
in vec2 UV0;
out vec2 texCoord0;
flat out vec4 silhouetteAnchor;
out vec3 surfaceViewPosition;
flat out vec3 planeRight;
flat out vec3 planeNormal;

void main() {
    vec4 viewPosition = ModelViewMat * vec4(Position, 1.0);
    gl_Position = ProjMat * viewPosition;
    surfaceViewPosition = viewPosition.xyz;
    texCoord0 = UV0;
    vec4 center = vec4(0.0, 0.0, -1.0, 2.0);
    float nearest = 1e30;
    for (int i = 0; i < min(int(AnchorInfo.x), 64); i++) {
        vec3 delta = viewPosition.xyz - Centers[i].xyz;
        float distanceSquared = dot(delta, delta);
        if (distanceSquared < nearest) {
            nearest = distanceSquared;
            center = Centers[i];
        }
    }
    vec3 up = normalize(WorldUp.xyz);
    vec3 right = cross(center.xyz, up);
    planeRight = length(right) > 0.0001 ? normalize(right) : vec3(1.0, 0.0, 0.0);
    planeNormal = normalize(cross(up, planeRight));
    silhouetteAnchor = center;
}
