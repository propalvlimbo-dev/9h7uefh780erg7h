#version 330

layout(std140) uniform WorldColor {
    vec4 ColorMultiplier;
};

in vec2 TexCoord;
out vec4 fragColor;

void main() {
    fragColor = vec4(ColorMultiplier.rgb * 0.5, 1.0);
}
