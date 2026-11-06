#version 330
in vec2 texCoord;
in vec4 vertexColor;
out vec4 fragColor;
void main() {
    vec2 p = vec2(fract(texCoord.x) * 2.0 - 1.0, texCoord.y * 2.0 - 1.0);
    vec2 q = vec2(abs(p.x), p.y);
    vec2 a = vec2(0.0, 0.55);
    vec2 v = vec2(0.62, -0.9);
    float d = length(q - a - v * clamp(dot(q - a, v) / dot(v, v), 0.0, 1.0));
    float alpha = 1.0 - smoothstep(0.07, 0.07 + max(fwidth(d), 0.03), d);
    fragColor = vec4(vertexColor.rgb, vertexColor.a * alpha);
}
