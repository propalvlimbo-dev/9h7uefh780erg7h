#version 330
#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;
layout(std140) uniform RoseParams {
    float DistPerTexel;   // на сколько меняется значение атласа при сдвиге на один тексель
};

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

float coverage(vec2 uv, float width) {
    // В атласе хранится расстояние до края буквы: 0.5 — ровно контур
    float dist = textureLod(Sampler0, uv, 0.0).r;
    return smoothstep(0.5 - width, 0.5 + width, dist);
}

void main() {
    // Ширина сглаживания — из размера пикселя экрана в текселях атласа.
    // fwidth(dist) для этого не годится: на тонком штрихе мелкого текста соседние пиксели
    // по обе стороны штриха дают почти одинаковое расстояние, fwidth падает к нулю — и штрих пропадает.
    vec2 atlas = vec2(textureSize(Sampler0, 0));
    vec2 dx = dFdx(texCoord0);
    vec2 dy = dFdy(texCoord0);
    float texels = max(sqrt(0.5 * (dot(dx * atlas, dx * atlas) + dot(dy * atlas, dy * atlas))), 0.0001);
    float width = clamp(texels * DistPerTexel * 0.7, 0.004, 0.49);

    float alpha;
    if (texels > 1.25) {
        // Мелкий текст: четыре выборки внутри пикселя — штрихи ровные и не рвутся
        float subWidth = width * 0.5;
        vec2 a = 0.25 * (dx + dy);
        vec2 b = 0.25 * (dx - dy);
        alpha = 0.25 * (coverage(texCoord0 + a, subWidth) + coverage(texCoord0 - a, subWidth)
                + coverage(texCoord0 + b, subWidth) + coverage(texCoord0 - b, subWidth));
    } else {
        alpha = coverage(texCoord0, width);
    }

    vec4 color = vertexColor * ColorModulator;
    color.a *= alpha;
    if (color.a <= 0.002) {
        discard;
    }
    fragColor = color;
}
