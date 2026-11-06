#version 330
#moj_import <minecraft:dynamictransforms.glsl>

// Силуэт предмета: форма берётся из прозрачности текстуры, цвет — один на весь предмет.
// Цвет сразу умножен на силу: буфер свечения складывается с кадром как свет.
uniform sampler2D Sampler0;


in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    if (texture(Sampler0, texCoord0).a < 0.1) {
        discard;
    }
    fragColor = vec4(vertexColor.rgb * vertexColor.a, 1.0) * ColorModulator;
}
