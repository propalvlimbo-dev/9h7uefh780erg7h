#version 330

#moj_import <rose:defaults.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV1;
in ivec2 UV2;

out vec2 Size;
out vec4 Colors;
out vec4 Radius;
out vec2 FragCoord;
out float Thickness;

void main() {
    FragCoord = rvertexcoord(gl_VertexID);
    Colors = Color;
    Size = vec2(UV2);
    Thickness = UV0.x;

    uint xy = uint(UV1.x) & 0xFFFFu;
    uint zw = uint(UV1.y) & 0xFFFFu;

    Radius = vec4(
        float(xy & 0xFFu),
        float((xy >> 8u) & 0xFFu),
        float(zw & 0xFFu),
        float((zw >> 8u) & 0xFFu)
    );

    gl_Position = vec4(Position, 1.0);
}