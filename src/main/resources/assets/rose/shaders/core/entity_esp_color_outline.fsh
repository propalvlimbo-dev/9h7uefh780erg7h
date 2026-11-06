#version 330

#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 sampled = texture(Sampler0, texCoord0);
    if (sampled.a == 0.0) discard;

    bool preserveTexture = vertexColor.r < 0.01
        && vertexColor.g > 0.98
        && vertexColor.b > 0.97;
    if (preserveTexture) {
        fragColor = vec4(sampled.rgb, sampled.a * ColorModulator.a);
    } else {
        fragColor = vec4(ColorModulator.rgb * vertexColor.rgb, ColorModulator.a);
    }
}
