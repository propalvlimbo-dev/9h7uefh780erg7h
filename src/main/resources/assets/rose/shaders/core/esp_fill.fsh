#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;
uniform vec4 color;
uniform int autoColor;
uniform float saturation;

in vec2 TexCoord;

out vec4 fragColor;

float cover(vec2 uv) {
    float ed = texture(Sampler0, uv).r;
    float wd = texture(Sampler1, uv).r;
    return step(0.0001, ed) * step(wd, ed + 0.0008);
}

void main() {
    float cov = cover(TexCoord);
    if (cov < 0.5) discard;

    vec3 outCol = color.rgb;
    if (autoColor == 1) {
        vec4 b = texture(Sampler2, TexCoord);
        vec3 c = b.rgb / max(b.a, 0.08);
        float mx = max(c.r, max(c.g, c.b));
        if (mx > 0.001) c /= mx;
        float l = dot(c, vec3(0.299, 0.587, 0.114));
        outCol = clamp(mix(vec3(l), c, saturation), 0.0, 1.0);
    }

    fragColor = vec4(outCol, color.a * cov);
}
