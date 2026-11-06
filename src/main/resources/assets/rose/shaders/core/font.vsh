#version 330

#moj_import <rose:defaults.glsl>

layout(std140) uniform Params {
    float InRange;
    float InThickness;
    float InSmoothness;
};

layout(std140) uniform Scissors {
    vec4 InScissor;
    vec2 ScreenSize;
    float InUseScissor;
};

in vec3 Position;
in vec2 UV0;
in vec4 Color;

out vec2 TexCoord;
out vec2 FragCoord;
out vec4 InputColor;

out float Range;
out float Thickness;
out float Smoothness;

out vec4 Scissor;
out float UseScissor;

void main() {
    TexCoord = UV0;
    InputColor = Color;

    gl_Position = vec4(Position, 1.0);
    FragCoord = gl_Position.xy;

    Range = InRange;
    Thickness = InThickness;
    Smoothness = InSmoothness;

    Scissor = InScissor;
    UseScissor = InUseScissor;
}