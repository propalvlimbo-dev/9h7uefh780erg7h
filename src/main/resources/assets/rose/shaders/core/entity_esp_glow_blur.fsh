#version 330

uniform sampler2D InSampler;

layout(std140) uniform GlowBlur {
    vec4 Step;
    vec4 Pairs;
    vec4 Tail;
};

in vec2 TexCoord;
out vec4 fragColor;

void main() {
    vec4 result = texture(InSampler, TexCoord) * Step.z;
    result += (texture(InSampler, TexCoord + Step.xy * Pairs.x)
            + texture(InSampler, TexCoord - Step.xy * Pairs.x)) * Pairs.y;
    result += (texture(InSampler, TexCoord + Step.xy * Pairs.z)
            + texture(InSampler, TexCoord - Step.xy * Pairs.z)) * Pairs.w;
    result += (texture(InSampler, TexCoord + Step.xy * Tail.x)
            + texture(InSampler, TexCoord - Step.xy * Tail.x)) * Tail.y;
    result += (texture(InSampler, TexCoord + Step.xy * Tail.z)
            + texture(InSampler, TexCoord - Step.xy * Tail.z)) * Tail.w;
    fragColor = result;
}
