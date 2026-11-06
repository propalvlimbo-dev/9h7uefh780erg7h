#version 330
in vec2 texCoord;
in vec4 vertexColor;
out vec4 fragColor;
void main() {
    vec2 uv = 1.4 * (2.0 * texCoord - vec2(1.0));
    float radius = length(uv);
    float distanceToBoundary = abs(radius - 1.0);
    if (distanceToBoundary >= 0.30) discard;
    float bloom = exp(-(distanceToBoundary * distanceToBoundary) / 0.0105);
    float side = radius >= 1.0 ? 1.0 : 0.70;
    float cutoff = 1.0 - smoothstep(0.23, 0.30, distanceToBoundary);
    float intensity = bloom * side * cutoff * vertexColor.a * 0.62;
    if (intensity <= 0.002) discard;
    fragColor = vec4(vertexColor.rgb * intensity, intensity);
}
