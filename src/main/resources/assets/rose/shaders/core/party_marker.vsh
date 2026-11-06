#version 330
#moj_import <minecraft:projection.glsl>
in vec3 Position;
in vec2 UV0;
in vec4 Color;
out vec2 texCoord;
out vec4 vertexColor;
void main() {
    gl_Position = ProjMat * vec4(Position, 1.0);
    gl_Position.z = min(gl_Position.z, gl_Position.w * 0.9999);
    texCoord = UV0;
    vertexColor = Color;
}
