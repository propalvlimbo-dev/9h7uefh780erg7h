#version 330

uniform sampler2D InSampler;

layout(std140) uniform PreviewFade {
    vec4 FadeData;
};

in vec2 TexCoord;
out vec4 fragColor;

void main() {
    fragColor = texture(InSampler, TexCoord) * clamp(FadeData.x, 0.0, 1.0);
}
