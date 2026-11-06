#version 330

#moj_import <rose:defaults.glsl>

layout(std140) uniform Wheel {
    vec2 InSize;
    float InInner;
    float InOuter;
    float InStart;
    float InSpan;
    float InGap;
    float InFeather;
};

layout(std140) uniform Colors {
    vec4 InColor1;
    vec4 InColor2;
    vec4 InColor3;
    vec4 InColor4;
};

in vec3 Position;

out vec2 FragCoord;
out vec2 TexCoord;
out vec2 Size;
out float Inner;
out float Outer;
out float Start;
out float Span;
out float Gap;
out float Feather;
out vec4 Color;

void main() {
    FragCoord = rvertexcoord(gl_VertexID);
    gl_Position = vec4(Position, 1.0);
    TexCoord = gl_Position.xy * 0.5 + 0.5;
    Size = InSize;
    Inner = InInner;
    Outer = InOuter;
    Start = InStart;
    Span = InSpan;
    Gap = InGap;
    Feather = InFeather;
    Color = InColor1;
}
