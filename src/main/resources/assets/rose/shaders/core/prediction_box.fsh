#version 330

in vec2 texCoord;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    float edge = clamp(abs(texCoord.y * 2.0 - 1.0), 0.0, 1.0);
    float mask = pow(1.0 - edge, 1.4);
    float alpha = vertexColor.a * mask;
    if (alpha <= 0.002) discard;

    fragColor = vec4(vertexColor.rgb, alpha);
}
