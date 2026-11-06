#version 330

#moj_import <rose:defaults.glsl>

in vec4 Colors;
in vec2 FragCoord;

out vec4 fragColor;

void main() {
    fragColor = Colors;
}