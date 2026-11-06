#version 330
#moj_import <minecraft:dynamictransforms.glsl>

// Мягкий спрайт: как ванильный position_tex_color, но без отсечения почти прозрачных точек —
// у свечений, сияния и звёзд края тают до нуля, а не обрываются на десяти процентах.
uniform sampler2D Sampler0;


in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor;
    if (color.a <= 0.0) {
        discard;
    }
    fragColor = color * ColorModulator;
}
