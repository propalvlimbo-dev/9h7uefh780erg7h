#version 150

uniform sampler2D Sampler0;
uniform vec2 texelSize;
uniform vec2 direction;
uniform float radius;

in vec2 TexCoord;

out vec4 fragColor;

const int MAXR = 32;

void main() {
    vec2 off = direction * texelSize;
    float sigma = max(1.0, radius * 0.5);
    float wsum = 1.0;
    vec4 sum = texture(Sampler0, TexCoord);
    for (int i = 1; i <= MAXR; i++) {
        if (float(i) > radius) break;
        float w = exp(-float(i * i) / (2.0 * sigma * sigma));
        sum += texture(Sampler0, TexCoord + off * float(i)) * w;
        sum += texture(Sampler0, TexCoord - off * float(i)) * w;
        wsum += 2.0 * w;
    }
    fragColor = sum / wsum;
}
