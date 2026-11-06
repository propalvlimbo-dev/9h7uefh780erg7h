#version 330

uniform sampler2D Sampler0;
in vec2 texCoord0;
in vec3 localPosition;
out vec4 fragColor;

void main() {
    if (texture(Sampler0, texCoord0).a == 0.0) discard;
    fragColor = vec4(clamp(localPosition * 0.25 + 0.5, 0.0, 1.0), 1.0);
}
