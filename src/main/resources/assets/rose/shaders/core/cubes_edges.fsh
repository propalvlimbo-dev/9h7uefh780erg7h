#version 330

in vec4 vertexColor;

out vec4 fragColor;

void main() {
    float alpha = vertexColor.a;
    if (alpha <= 0.001) discard;
    fragColor = vec4(vertexColor.rgb * alpha, alpha);
}
