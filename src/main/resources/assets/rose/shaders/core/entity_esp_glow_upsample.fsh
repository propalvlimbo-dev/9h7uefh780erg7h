#version 330

uniform sampler2D InSampler;
in vec2 TexCoord;
out vec4 fragColor;

void main() {
    vec2 offset = 1.0 / vec2(textureSize(InSampler, 0));
    vec4 result = texture(InSampler, TexCoord) * 4.0;
    result += texture(InSampler, TexCoord + offset);
    result += texture(InSampler, TexCoord - offset);
    result += texture(InSampler, TexCoord + vec2(offset.x, -offset.y));
    result += texture(InSampler, TexCoord + vec2(-offset.x, offset.y));
    fragColor = result * 0.125;
}
