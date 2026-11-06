#version 330

layout(std140) uniform Lightning {
    mat4 Projection;
    mat4 View;
    vec4 Color;
};

in vec3 Position;
in vec2 UV0;

out vec2 BoltUv;
out vec4 BoltColor;

void main() {
    BoltUv = UV0;
    BoltColor = Color;
    gl_Position = Projection * View * vec4(Position, 1.0);
}
