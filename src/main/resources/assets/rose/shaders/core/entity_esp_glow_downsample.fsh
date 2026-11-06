#version 330

uniform sampler2D InSampler;
in vec2 TexCoord;
out vec4 fragColor;

void main() {
    vec2 offset = 0.5 / vec2(textureSize(InSampler, 0));
    fragColor = (texture(InSampler, TexCoord + offset)
            + texture(InSampler, TexCoord - offset)
            + texture(InSampler, TexCoord + vec2(offset.x, -offset.y))
            + texture(InSampler, TexCoord + vec2(-offset.x, offset.y))) * 0.25;
}
