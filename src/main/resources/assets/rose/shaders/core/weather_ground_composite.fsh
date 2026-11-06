#version 330

uniform sampler2D Sampler0;

in vec2 TexCoord;
out vec4 fragColor;

void main() {
    vec4 effect = texture(Sampler0, TexCoord);
    if (effect.a <= 0.002) discard;
    fragColor = effect;
}
