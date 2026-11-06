#version 330
#moj_import <minecraft:dynamictransforms.glsl>

// Персонаж: картинка на деформируемой сетке. Silhouette перекрашивает её в один цвет
// (для тени и контурного свечения), не трогая прозрачность.

uniform sampler2D Sampler0;
layout(std140) uniform RoseParams {
    vec4 Silhouette;       // rgb — цвет, a — насколько перекрасить (0 — картинка как есть)
};

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 image = texture(Sampler0, texCoord0);
    vec4 color = vec4(mix(image.rgb, Silhouette.rgb, Silhouette.a), image.a) * vertexColor * ColorModulator;
    if (color.a <= 0.002) {
        discard;
    }
    fragColor = color;
}
