#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;

in vec2 TexCoord;

out vec4 fragColor;

void main() {
    float ed = texture(Sampler1, TexCoord).r;
    float wd = texture(Sampler2, TexCoord).r;
    float cov = step(0.0001, ed) * step(wd, ed + 0.0008);
    vec3 c = texture(Sampler0, TexCoord).rgb;
    fragColor = vec4(c * cov, cov);
}
