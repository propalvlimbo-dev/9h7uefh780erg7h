#version 330

#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec4 Color;

out vec4 vColor;
out vec2 vWorld;
out float vDist;

void main() {
    vec4 viewPos = ModelViewMat * vec4(Position, 1.0);

    // distance from the camera in the horizontal plane (view space has the camera at origin)
    vDist = length(viewPos.xz);
    // world XZ so the cloud pattern is anchored to the world, not to the player
    vWorld = Position.xz;
    vColor = Color;

    gl_Position = ProjMat * viewPos;
}
