#version 330
#moj_import <minecraft:dynamictransforms.glsl>

#moj_import <rose:shape.glsl>
#moj_import <rose:paint.glsl>

// Поверхность Rose: заливка, обводка, тень снаружи или внутри — для фигуры с любыми углами.

layout(std140) uniform RoseParams {
    vec2 Size;           // размер фигуры, px
    vec4 Radius;         // радиусы углов
    vec4 Corner;         // форма углов (см. shape.glsl)
    int Mode;            // 0 заливка, 1 обводка, 2 тень снаружи, 3 тень внутри, 4 всё вокруг фигуры
    float Thickness;     // толщина обводки, px
    float Blur;          // размытие тени (сигма), px
    float Spread;        // расширение тени, px
    vec2 Offset;         // смещение тени, px
    int PaintType;       // 0 цвет, 1 линейный, 2 радиальный, 3 угловой, 4 цвета вершин
    int PaintCount;
    float PaintLab[16];
    vec4 PaintPos;
    vec2 PaintDir;
    vec2 PaintCenter;
    float PaintRadius;
};


in vec2 texCoord0;           // координаты пикселя относительно левого верхнего угла фигуры
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec2 halfSize = Size * 0.5;
    vec2 p = texCoord0 - halfSize;
    float coverage;

    if (Mode == 2) {
        float d = rose_roundedBox(p - Offset, halfSize, Radius, Corner) - Spread;
        coverage = 0.5 - 0.5 * rose_erf(d / (max(Blur, 0.001) * 1.41421356));
    } else {
        float d = rose_roundedBox(p, halfSize, Radius, Corner);
        float pixel = max(fwidth(d), 0.0001);
        float inside = clamp(0.5 - d / pixel, 0.0, 1.0);
        if (Mode == 0) {
            coverage = inside;
        } else if (Mode == 1) {
            coverage = inside - clamp(0.5 - (d + Thickness) / pixel, 0.0, 1.0);
        } else if (Mode == 3) {
            float shifted = rose_roundedBox(p - Offset, halfSize, Radius, Corner) + Spread;
            coverage = inside * (0.5 + 0.5 * rose_erf(shifted / (max(Blur, 0.001) * 1.41421356)));
        } else {
            coverage = 1.0 - inside;
        }
    }

    vec4 color = PaintType == 4 ? vertexColor
            : rose_paint(texCoord0, Size, PaintType, PaintCount, PaintLab, PaintPos, PaintDir, PaintCenter, PaintRadius) * vertexColor;
    color *= ColorModulator;
    color.a *= coverage;
    if (color.a <= 0.0) {
        discard;
    }
    color.rgb += (rose_noise(gl_FragCoord.xy) - 0.5) / 255.0;
    fragColor = color;
}
