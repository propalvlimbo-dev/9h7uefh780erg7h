#version 330

// Сложение свечения с кадром: яркость и насыщенность ореола задаются снаружи, смешивание — «сложение» (ONE, ONE).
uniform sampler2D DiffuseSampler;
// Силуэты предметов в руке (альфа 1 внутри предмета): там свечение приглушается, и ореол остаётся только вокруг контура
uniform sampler2D MaskSampler;
layout(std140) uniform RoseParams {
    float Cut;
    float Strength;
    float Saturation;
};

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec3 color = texture(DiffuseSampler, texCoord).rgb;
    float luma = dot(color, vec3(0.299, 0.587, 0.114));
    color = max(mix(vec3(luma), color, Saturation), vec3(0.0));
    float inside = texture(MaskSampler, texCoord).a;
    fragColor = vec4(color * Strength * (1.0 - Cut * inside), 1.0);
}
