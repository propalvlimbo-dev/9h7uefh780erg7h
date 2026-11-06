// Формы Rose: расстояние до прямоугольника с настраиваемыми углами.

// p — точка относительно центра, halfSize — половина размера.
// radius — радиусы углов: верх-лево, верх-право, низ-право, низ-лево.
// shape — форма каждого угла:
//   1 — срез по диагонали, 2 — дуга окружности, 4 — плавный «iOS»-угол,
//   50 и больше — острый, меньше 0 — вогнутый (вырезан круг).
// Результат: меньше нуля — внутри фигуры, больше нуля — снаружи.
float rose_roundedBox(vec2 p, vec2 halfSize, vec4 radius, vec4 shape) {
    int i = p.x < 0.0 ? (p.y < 0.0 ? 0 : 3) : (p.y < 0.0 ? 1 : 2);
    float r = clamp(radius[i], 0.0, min(halfSize.x, halfSize.y));
    float n = shape[i];
    vec2 a = abs(p);

    if (r <= 0.0) {
        vec2 q = a - halfSize;
        return min(max(q.x, q.y), 0.0) + length(max(q, 0.0));
    }
    if (n < 0.0) {
        vec2 fromCorner = a - halfSize;
        return max(max(fromCorner.x, fromCorner.y), r - length(fromCorner));
    }
    if (abs(n - 1.0) < 0.001) {
        vec2 q = a - halfSize;
        float box = min(max(q.x, q.y), 0.0) + length(max(q, 0.0));
        return max(box, (a.x + a.y - (halfSize.x + halfSize.y - r)) * 0.70710678);
    }
    vec2 q = a - halfSize + r;
    vec2 m = max(q, 0.0);
    float corner = n >= 50.0 ? max(m.x, m.y) : pow(pow(m.x, n) + pow(m.y, n), 1.0 / n);
    return min(max(q.x, q.y), 0.0) + corner - r;
}

// Плавный минимум: фигуры сливаются, как капли. k — радиус слияния.
float rose_smoothMin(float a, float b, float k) {
    if (k <= 0.0) {
        return min(a, b);
    }
    float h = clamp(0.5 + 0.5 * (b - a) / k, 0.0, 1.0);
    return mix(b, a, h) - k * h * (1.0 - h);
}

// Функция ошибок — для мягких теней по Гауссу.
float rose_erf(float x) {
    float s = sign(x);
    float a = abs(x);
    float t = 1.0 / (1.0 + 0.3275911 * a);
    float y = 1.0 - (((((1.061405429 * t - 1.453152027) * t) + 1.421413741) * t - 0.284496736) * t + 0.254829592) * t * exp(-a * a);
    return s * y;
}
