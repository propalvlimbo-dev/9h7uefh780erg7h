#version 330

#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    // The atlas stores the signed distance to the glyph outline: 0.5 is exactly on the contour.
    float dist = textureLod(Sampler0, texCoord0, 0.0).r;

    // Antialiasing width in atlas space, taken from the screen-space derivative of the distance.
    // That keeps thin stems of small text from fading away, and gives very large text a soft edge.
    float width = clamp(fwidth(dist) * 0.72, 0.004, 0.4);

    vec4 color = vertexColor * ColorModulator;
    color.a *= smoothstep(0.5 - width, 0.5 + width, dist);

    if (color.a <= 0.002) {
        discard;
    }

    fragColor = color;
}
