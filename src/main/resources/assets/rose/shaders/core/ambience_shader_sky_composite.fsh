#version 330

uniform sampler2D SkySampler;
uniform sampler2D DepthSampler;

in vec2 TexCoord;
out vec4 fragColor;

void main() {
    float depth = texture(DepthSampler, TexCoord).r;
    if (depth > 0.000001) {
        discard;
    }

    vec4 sky = texture(SkySampler, TexCoord);
    if (sky.a <= 0.001) {
        discard;
    }

    fragColor = sky;
}
