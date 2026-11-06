#version 330

uniform sampler2D InSampler;

in vec2 TexCoord;
out vec4 fragColor;

void main() {
    float mask = texture(InSampler, TexCoord).a;
    fragColor = vec4(mask, mask, 0.0, mask);
}
