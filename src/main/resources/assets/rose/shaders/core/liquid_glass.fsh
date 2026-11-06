#version 330
#moj_import <minecraft:dynamictransforms.glsl>

#moj_import <rose:shape.glsl>
#moj_import <rose:paint.glsl>

// Жидкое стекло Rose: преломление у края, линза, «живая» жидкость, блики,
// радужная кромка, цветокоррекция подложки и слияние фигур в одну каплю.

uniform sampler2D Sampler0;     // чёткий снимок экрана
uniform sampler2D Sampler1;     // лёгкое размытие (линейный свет)
uniform sampler2D Sampler2;     // среднее размытие
uniform sampler2D Sampler3;     // сильное размытие

layout(std140) uniform RoseParams {
    vec2 OutSize;           // размер экрана, px
    float PixelScale;       // экранных пикселей в одном GUI-пикселе
    float Time;             // секунды — для анимаций
    float Shapes[32];       // до 8 фигур: центр x, центр y, полуширина, полувысота
    float Radii[32];        // по 4 радиуса углов на фигуру
    float Corners[32];      // по 4 формы углов на фигуру
    int ShapeCount;
    float Merge;            // радиус слияния фигур, px
    vec4 Bounds;            // x, y, ширина, высота всей стеклянной области — для оттенка
    float Blur;             // 0 — чётко, 1 — сильно размыто
    float Refraction;       // сила преломления у края, px (минус — внутрь)
    float Bevel;            // ширина линзы у края, px
    float Curve;            // профиль линзы: больше — эффект ближе к самому краю
    float Magnify;          // увеличение в центре (1 — без увеличения)
    float Dispersion;       // радужная кромка
    float Specular;         // яркость бликов
    float Sharpness;        // резкость бликов
    vec3 SpecularColor;
    vec2 LightDir;          // откуда падает свет (x вправо, y вниз)
    float Rim;              // яркость тонкой кромки
    float RimWidth;         // ширина кромки, экранные пиксели
    float Fresnel;          // свечение к краям
    float Saturation;
    float Brightness;
    float Contrast;
    float Grain;            // зерно
    float Wobble;           // «живая» жидкость: амплитуда, px
    float WobbleScale;      // размер волн
    float WobbleSpeed;      // скорость волн
    float Depth;            // затемнение нижнего края — ощущение толщины
    int TintType;
    int TintCount;
    float TintLab[16];
    vec4 TintPos;
    vec2 TintDir;
    vec2 TintCenter;
    float TintRadius;
    float Ripples[16];      // до 4 кругов от нажатий: центр x, центр y, возраст (с), сила (px)
    int RippleCount;
};





in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

float shapeDistance(vec2 p, int i) {
    int o = i * 4;
    vec2 center = vec2(Shapes[o], Shapes[o + 1]);
    vec2 halfSize = vec2(Shapes[o + 2], Shapes[o + 3]);
    vec4 radius = vec4(Radii[o], Radii[o + 1], Radii[o + 2], Radii[o + 3]);
    vec4 corner = vec4(Corners[o], Corners[o + 1], Corners[o + 2], Corners[o + 3]);
    return rose_roundedBox(p - center, halfSize, radius, corner);
}

float scene(vec2 p) {
    float d = 100000.0;
    for (int i = 0; i < ShapeCount; i++) {
        d = rose_smoothMin(d, shapeDistance(p, i), Merge);
    }
    return d;
}

vec2 nearestCenter(vec2 p) {
    float best = 100000.0;
    vec2 center = p;
    for (int i = 0; i < ShapeCount; i++) {
        float d = shapeDistance(p, i);
        if (d < best) {
            best = d;
            center = vec2(Shapes[i * 4], Shapes[i * 4 + 1]);
        }
    }
    return center;
}

// Подложка нужной размытости: плавно между чётким снимком и тремя уровнями размытия
vec3 backdrop(vec2 uv) {
    float level = clamp(Blur, 0.0, 1.0) * 3.0;
    if (level < 1.0) {
        return mix(texture(Sampler0, uv).rgb, rose_toGamma(texture(Sampler1, uv).rgb), level);
    }
    if (level < 2.0) {
        return mix(rose_toGamma(texture(Sampler1, uv).rgb), rose_toGamma(texture(Sampler2, uv).rgb), level - 1.0);
    }
    return mix(rose_toGamma(texture(Sampler2, uv).rgb), rose_toGamma(texture(Sampler3, uv).rgb), level - 2.0);
}

vec2 flow(vec2 p) {
    float t = Time * WobbleSpeed;
    vec2 q = p * (0.04 / max(WobbleScale, 0.05));
    return vec2(rose_valueNoise(q + vec2(t * 0.7, t * 0.3)),
                rose_valueNoise(q + vec2(-t * 0.4, t * 0.6) + 19.3)) * 2.0 - 1.0;
}

void main() {
    vec2 p = texCoord0;
    float d = scene(p);
    float pixel = max(fwidth(d), 0.0001);
    float mask = clamp(0.5 - d / pixel, 0.0, 1.0);
    if (mask <= 0.0) {
        discard;
    }

    // Нормаль края: направление, в котором расстояние растёт быстрее всего
    vec2 gradient = vec2(scene(p + vec2(0.5, 0.0)) - scene(p - vec2(0.5, 0.0)),
                         scene(p + vec2(0.0, 0.5)) - scene(p - vec2(0.0, 0.5)));
    float gradientLength = length(gradient);
    vec2 normal = gradientLength > 0.00001 ? gradient / gradientLength : vec2(0.0);

    // Профиль линзы: 1 у самого края, 0 в плоской середине
    float edge = 1.0 - clamp(-d / max(Bevel, 0.001), 0.0, 1.0);
    float lens = pow(edge, max(Curve, 0.1));

    vec2 shiftGui = normal * Refraction * lens;
    if (abs(Magnify - 1.0) > 0.001) {
        shiftGui += (nearestCenter(p) - p) * (1.0 - 1.0 / max(Magnify, 0.05));
    }
    if (Wobble > 0.0) {
        shiftGui += flow(p) * Wobble;
    }

    // Круги от нажатий: кольцевая волна бежит по стеклу, искажает подложку и ловит свет
    float rippleLight = 0.0;
    for (int i = 0; i < RippleCount; i++) {
        int o = i * 4;
        vec2 delta = p - vec2(Ripples[o], Ripples[o + 1]);
        float dist = length(delta);
        float age = Ripples[o + 2];
        float front = dist - age * 190.0;
        float spread = 9.0 + age * 26.0;
        float envelope = exp(-front * front / (2.0 * spread * spread)) * exp(-age * 2.6);
        float wave = sin(front * 0.42) * envelope * Ripples[o + 3];
        if (dist > 0.001) {
            shiftGui += delta / dist * wave;
        }
        rippleLight += max(wave, 0.0);
    }

    vec2 uv = gl_FragCoord.xy / OutSize;
    vec2 shift = vec2(shiftGui.x, -shiftGui.y) * PixelScale / OutSize;

    vec3 color;
    float spread = Dispersion * lens;
    if (spread > 0.0) {
        color = vec3(backdrop(uv + shift * (1.0 + spread)).r,
                     backdrop(uv + shift).g,
                     backdrop(uv + shift * (1.0 - spread)).b);
    } else {
        color = backdrop(uv + shift);
    }

    // Цветокоррекция подложки
    float luma = dot(color, vec3(0.2126, 0.7152, 0.0722));
    color = mix(vec3(luma), color, Saturation);
    color = (color - 0.5) * Contrast + 0.5 + Brightness;

    // Оттенок стекла — цвет или градиент
    vec4 tint = rose_paint(p - Bounds.xy, Bounds.zw, TintType, TintCount, TintLab, TintPos, TintDir, TintCenter, TintRadius);
    color = mix(color, tint.rgb, tint.a);

    // Свет: блики со стороны света и слабее — с противоположной
    float facing = dot(normal, normalize(LightDir));
    float highlight = pow(max(facing, 0.0), 2.0) + 0.4 * pow(max(-facing, 0.0), 2.0);
    color += SpecularColor * highlight * pow(edge, max(Sharpness, 0.1)) * Specular;
    color += SpecularColor * Fresnel * edge * edge * 0.5;
    color += SpecularColor * rippleLight * 0.03;

    // Тонкая кромка по контуру
    float rim = smoothstep(-(max(RimWidth, 0.0) + 1.0) * pixel, -0.5 * pixel, d);
    color += SpecularColor * rim * Rim * (0.35 + 0.65 * max(facing, 0.0));

    // Толщина: нижний край чуть темнее
    color -= Depth * edge * edge * max(normal.y, 0.0);

    // Зерно и дизеринг
    color += (rose_noise(gl_FragCoord.xy) - 0.5) * (Grain + 1.0 / 255.0);

    vec4 modulate = vertexColor * ColorModulator;
    fragColor = vec4(clamp(color, 0.0, 1.0) * modulate.rgb, mask * modulate.a);
}
