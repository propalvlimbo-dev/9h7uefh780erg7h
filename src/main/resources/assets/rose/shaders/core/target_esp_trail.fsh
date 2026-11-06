#version 330

in vec2 texCoord;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    float intensity = vertexColor.a;
    if (intensity <= 0.001) discard;
    vec3 color = mix(vertexColor.rgb, vec3(1.0), intensity * 0.28);
    fragColor = vec4(color * intensity, intensity);
}
