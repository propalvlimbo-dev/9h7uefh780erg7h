#version 330

#moj_import <rose:defaults.glsl>

layout(std140) uniform TimeDial {
    vec2 InSize;
    float InPhase;
    float InHover;
    float InAlpha;
};

layout(std140) uniform Scissors {
    vec4 InScissor;
    float InUseScissor;
};

in vec3 Position;

out vec2 FragCoord;
out vec2 Size;
out float Phase;
out float Hover;
out float Alpha;
out vec4 Scissor;
out float UseScissor;

void main() {
    gl_Position = vec4(Position, 1.0);
    FragCoord = rvertexcoord(gl_VertexID);
    Size = InSize;
    Phase = InPhase;
    Hover = InHover;
    Alpha = InAlpha;
    Scissor = InScissor;
    UseScissor = InUseScissor;
}
