#version 330
#moj_import <rose:defaults.glsl>

layout(std140) uniform Glass {
    vec4 InRect;
    vec4 InOptions;
    vec4 InTint;
};
layout(std140) uniform Scissors {
    vec4 InScissor;
    float InUseScissor;
};
in vec3 Position;
out vec2 FragCoord;
flat out vec4 PanelRect;
flat out vec4 Options;
flat out vec4 Tint;
flat out vec4 Scissor;
flat out float UseScissor;
void main() {
    gl_Position = vec4(Position, 1.0);
    FragCoord = rvertexcoord(gl_VertexID);
    PanelRect = InRect;
    Options = InOptions;
    Tint = InTint;
    Scissor = InScissor;
    UseScissor = InUseScissor;
}
