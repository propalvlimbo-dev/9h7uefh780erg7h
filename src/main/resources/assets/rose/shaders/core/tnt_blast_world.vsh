#version 330
layout(std140) uniform BlastWorld {
    mat4 Projection;
    mat4 View;
};
in vec3 Position;
in vec4 Color;
out vec4 vertexColor;
void main() {
    vertexColor = Color;
    gl_Position = Projection * View * vec4(Position, 1.0);
}
