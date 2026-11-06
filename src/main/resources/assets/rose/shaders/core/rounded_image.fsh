#version 330
#moj_import <minecraft:dynamictransforms.glsl>

#moj_import <rose:shape.glsl>

// Часть текстуры (например, лицо скина) в фигуре с настраиваемыми углами.

uniform sampler2D Sampler0;
layout(std140) uniform RoseParams {
    vec2 Size;
    vec4 Radius;
    vec4 Corner;
    vec4 UvRect;         // u0, v0, u1, v1
};

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec2 halfSize = Size * 0.5;
    float d = rose_roundedBox(texCoord0 - halfSize, halfSize, Radius, Corner);
    float mask = clamp(0.5 - d / max(fwidth(d), 0.0001), 0.0, 1.0);
    if (mask <= 0.0) {
        discard;
    }
    vec2 uv = mix(UvRect.xy, UvRect.zw, clamp(texCoord0 / Size, 0.0, 0.9999));
    vec4 color = texture(Sampler0, uv) * vertexColor * ColorModulator;
    color.a *= mask;
    if (color.a <= 0.0) {
        discard;
    }
    fragColor = color;
}
