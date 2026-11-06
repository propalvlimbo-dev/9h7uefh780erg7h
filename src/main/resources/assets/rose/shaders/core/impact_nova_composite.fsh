#version 330

uniform sampler2D InSampler;

layout(std140) uniform ImpactNovaColor {
    vec4 Color;
};

in vec2 TexCoord;
out vec4 fragColor;

void main() {
    float alpha = texture(InSampler, TexCoord).a;
    if (alpha <= 0.001) discard;
    fragColor = vec4(Color.rgb * alpha, alpha);
}
