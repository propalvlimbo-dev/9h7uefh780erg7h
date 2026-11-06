#version 330

in vec2 coatCoord;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    if (vertexColor.a <= 0.004) discard;

    float sheen = pow(clamp(dot(vertexColor.rgb, vec3(0.299, 0.587, 0.114)), 0.0, 1.0), 3.0);
    vec3 polished = vertexColor.rgb + vec3(sheen * 0.10);

    fragColor = vec4(polished, vertexColor.a);
}
