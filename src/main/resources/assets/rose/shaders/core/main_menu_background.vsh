#version 330

#moj_import <rose:defaults.glsl>

layout(std140) uniform MainMenu {
    vec2 Resolution;
    vec2 Mouse;
    float Time;
    float Entry;
};

in vec3 Position;

out vec2 FragCoord;
out vec2 Size;
out vec2 Cursor;
out float Clock;
out float Fade;

void main() {
    gl_Position = vec4(Position, 1.0);
    FragCoord = rvertexcoord(gl_VertexID).yx;
    Size = Resolution;
    Cursor = Mouse;
    Clock = Time;
    Fade = Entry;
}
