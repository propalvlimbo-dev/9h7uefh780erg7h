#version 330

#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in vec3 Normal;

out vec3 vViewPos;
out vec3 vNormal;
out vec2 vUv;
out vec4 vColor;

void main() {
    gl_Position = ProjMat * vec4(Position, 1.0);
    vViewPos = Position;
    vNormal = normalize(Normal);
    vUv = UV0;
    vColor = Color;
}
