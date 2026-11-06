#version 330

in vec2 texCoord;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    float edge = clamp(abs(texCoord.y * 2.0 - 1.0), 0.0, 1.0);
    float halo = pow(1.0 - edge, 1.6);
    float core = pow(1.0 - edge, 7.0);

    float intensity = vertexColor.a * (halo * 0.55 + core * 0.85);
    if (intensity <= 0.002) discard;

    vec3 color = mix(vertexColor.rgb, vec3(1.0), core * 0.7);
    fragColor = vec4(color * intensity, intensity);
}
