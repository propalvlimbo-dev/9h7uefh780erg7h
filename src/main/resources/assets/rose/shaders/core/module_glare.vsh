#version 330

#moj_import <rose:defaults.glsl>

layout(std140) uniform GlareData {
    vec4 InRadius;
    vec4 InGeometry;
    vec4 InEffect;
};

layout(std140) uniform Scissors {
    vec4 InScissor;
    float InUseScissor;
};

in vec3 Position;
out vec2 FragCoord;
out vec2 Size;
out vec4 Radius;
out float Progress;
out float Opacity;
out float GlareSize;
out vec4 Scissor;
out float UseScissor;

void main() {
    FragCoord = rvertexcoord(gl_VertexID);
    gl_Position = vec4(Position, 1.0);
    Radius = InRadius;
    Size = InGeometry.xy;
    Progress = InGeometry.z;
    Opacity = InGeometry.w;
    GlareSize = InEffect.x;
    Scissor = InScissor;
    UseScissor = InUseScissor;
}
