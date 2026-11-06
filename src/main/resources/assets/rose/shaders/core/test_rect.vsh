#version 330

#moj_import <rose:defaults.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;

out vec2 FragCoord;
out vec4 Colors;

void main() {
    FragCoord = rvertexcoord(gl_VertexID);
    gl_Position = vec4(Position, 1.0);

    Colors = Color;
}