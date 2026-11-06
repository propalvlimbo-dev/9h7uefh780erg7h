#version 330

in vec2 coatCoord;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    float edge = clamp(coatCoord.x, 0.0, 1.0);
    float density = pow(1.0 - edge, 2.4);
    float alpha = vertexColor.a * density;

    if (alpha <= 0.004) discard;

    fragColor = vec4(vertexColor.rgb, alpha);
}
