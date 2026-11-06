#version 330

#moj_import <rose:defaults.glsl>

layout(std140) uniform Forms {
    vec4 InRadius;
    vec2 InSize;
    float InSmoothness;
    float InBlurRadius;
};

layout(std140) uniform Colors {
    vec4 InColor1;
    vec4 InColor2;
    vec4 InColor3;
    vec4 InColor4;
};

layout(std140) uniform Scissors {
    vec4 InScissor;
    float InUseScissor;
};

in vec3 Position;

out vec2 Size;
out vec4 Radius;
out float Smoothness;
out float BlurRadius;

out vec4 Color1;
out vec4 Color2;
out vec4 Color3;
out vec4 Color4;

out vec2 FragCoord;
out vec2 TexCoord;

out vec4 Scissor;
out float UseScissor;

void main() {
    gl_Position = vec4(Position, 1.0);

    FragCoord = rvertexcoord(gl_VertexID);
    TexCoord = gl_Position.xy * 0.5 + 0.5;

    Size = InSize;
    Radius = InRadius;
    Smoothness = InSmoothness;
    BlurRadius = InBlurRadius;

    Color1 = InColor1;
    Color2 = InColor2;
    Color3 = InColor3;
    Color4 = InColor4;

    Scissor = InScissor;
    UseScissor = InUseScissor;
}