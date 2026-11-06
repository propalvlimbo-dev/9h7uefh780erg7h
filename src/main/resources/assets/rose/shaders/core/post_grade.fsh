#version 330

// Цветокоррекция мира и размытие в движении.
// Grade = 1 — применять насыщенность/контраст/сочность/экспозицию/теплоту; Blur > 0 — смешивать с прошлым кадром.

uniform sampler2D DiffuseSampler;
uniform sampler2D HistorySampler;
layout(std140) uniform RoseParams {
    float Saturation;
    float Contrast;
    float Vibrance;
    float Exposure;
    float Warmth;
    float Grade;
    float Blur;
};

in vec2 texCoord;

out vec4 fragColor;

const vec3 LUMA = vec3(0.2126, 0.7152, 0.0722);

void main() {
    vec3 color = texture(DiffuseSampler, texCoord).rgb;

    if (Grade > 0.5) {
        color *= Exposure;
        // Контраст вокруг средне-серого
        color = (color - 0.5) * Contrast + 0.5;
        float luma = dot(color, LUMA);
        color = mix(vec3(luma), color, Saturation);
        // Сочность: блёклые цвета усиливаются сильнее, чем и так яркие
        float spread = max(color.r, max(color.g, color.b)) - min(color.r, min(color.g, color.b));
        color = mix(vec3(luma), color, 1.0 + Vibrance * (1.0 - clamp(spread, 0.0, 1.0)));
        color += vec3(Warmth * 0.05, Warmth * 0.012, -Warmth * 0.05);
        color = clamp(color, 0.0, 1.0);
    }

    if (Blur > 0.0) {
        vec3 history = texture(HistorySampler, texCoord).rgb;
        color = mix(color, history, Blur);
    }

    fragColor = vec4(color, 1.0);
}
