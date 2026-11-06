#moj_import <rose:color.glsl>

// Заливка: цвет или градиент до 4 точек.
// type: 0 — цвет, 1 — линейный, 2 — радиальный, 3 — угловой (по кругу).
// lab — цвета точек в OKLab + прозрачность (по 4 числа на точку), stops — позиции точек 0..1.
// direction — cos/sin угла, center — центр (0..1 от размера), radius — радиус (доля полудиагонали).
vec4 rose_paint(vec2 local, vec2 size, int type, int count, float lab[16], vec4 stops, vec2 direction, vec2 center, float radius) {
    float t = 0.0;
    if (type == 1) {
        float extent = abs(size.x * direction.x) + abs(size.y * direction.y);
        t = dot(local - size * 0.5, direction) / max(extent, 0.0001) + 0.5;
    } else if (type == 2) {
        t = length(local - center * size) / max(radius * length(size) * 0.5, 0.0001);
    } else if (type == 3) {
        vec2 c = local - center * size;
        t = fract((atan(c.y, c.x) - atan(direction.y, direction.x)) / 6.2831853);
    }
    t = clamp(t, 0.0, 1.0);

    vec3 color = vec3(lab[0], lab[1], lab[2]);
    float alpha = lab[3];
    for (int i = 1; i < count; i++) {
        int o = i * 4;
        float k = clamp((t - stops[i - 1]) / max(stops[i] - stops[i - 1], 0.0001), 0.0, 1.0);
        vec3 next = vec3(lab[o], lab[o + 1], lab[o + 2]);
        // Смешиваем с учётом прозрачности, чтобы прозрачная точка не «пачкала» цвет
        float a0 = alpha * (1.0 - k);
        float a1 = lab[o + 3] * k;
        float sum = a0 + a1;
        color = sum > 0.0001 ? (color * a0 + next * a1) / sum : mix(color, next, k);
        alpha = sum;
    }
    return vec4(rose_linearToSrgb(rose_oklabToLinear(color)), alpha);
}
