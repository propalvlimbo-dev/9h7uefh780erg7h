#version 330
in vec2 texCoord;
in vec4 vertexColor;
out vec4 fragColor;
void main() {
    float core;
    float halo;
    if (texCoord.x > 3.5) {
        float x = abs((texCoord.x - 4.0) * 2.0 - 1.0);
        float endFade = smoothstep(0.0, 0.12, texCoord.y) * (1.0 - smoothstep(0.88, 1.0, texCoord.y));
        core = (1.0 - smoothstep(0.03, 0.03 + max(fwidth(x), 0.025), x)) * endFade;
        halo = exp2(-x * x * 8.0) * endFade * (1.0 - smoothstep(0.7, 1.0, x));
    } else {
        vec2 p = vec2(fract(texCoord.x) * 2.0 - 1.0, texCoord.y * 2.0 - 1.0);
        float d = abs(length(p) - 0.64);
        float aa = max(fwidth(d), 0.009);
        core = 1.0 - smoothstep(0.018, 0.018 + aa, d);
        halo = exp2(-d * d * 90.0);
        halo *= 1.0 - smoothstep(0.82, 1.0, max(abs(p.x), abs(p.y)));
    }
    float alpha = (core * 0.8 + halo * 0.48) * vertexColor.a;
    if (alpha < 0.002) discard;
    fragColor = vec4(mix(vertexColor.rgb * 1.35, vec3(1.0), core * 0.72), min(alpha, 1.0));
}
