#version 330

uniform sampler2D InSampler;

layout(std140) uniform PreviewMask {
    vec4 MaskColor;
};

in vec2 TexCoord;
out vec4 fragColor;

void main() {
    float alpha = texture(InSampler, TexCoord).a * MaskColor.a;
    fragColor = vec4(MaskColor.rgb * alpha, alpha);
}
