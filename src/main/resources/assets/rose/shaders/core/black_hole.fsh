#version 330

// Чёрная дыра Шварцшильда. Кольца и диск здесь не нарисованы геометрией — вместо этого для каждой точки
// экрана честно прослеживается луч света, который загибается тяготением. Всё, что видно (тень, кольцо
// фотонов, диск, загнутый над дырой, второе и третье изображение диска у самого края тени), получается
// само собой как следствие этого загиба.
//
// Физика: уравнение Бине для света в метрике Шварцшильда, d²u/dφ² + u = 3/2·rs·u². В декартовом виде,
// который удобно считать по шагам, оно превращается в ускорение a = −3/2·rs·h²·r̂/r⁵, где h — сохраняющийся
// момент луча. Тот же приём используют открытые реализации, по которым сверялись, — в том числе
// oseiskar/black-hole (MIT). Код здесь свой, заимствован только способ считать.
//
// Радиус горизонта принят за единицу; расстояние до наблюдателя и размеры диска — в тех же единицах.
// Готовая картинка потом просто масштабируется до нужного углового размера на небе.

in vec2 texCoord0;
in vec4 vertexColor;

layout(std140) uniform RoseParams {
    float Extent;       // половина кадра в радиусах тени
    float Inclination;  // наклон диска к лучу зрения, радианы (0 — точно с ребра)
    float Time;
    float Strength;     // общая видимость: ночь, дождь, ползунок
    float Steps;        // шагов трассировки; на слабых компьютерах меньше
    float StarPower;    // яркость своего звёздного поля вокруг дыры
    vec4 HotColor;      // цвет раскалённой внутренней части диска
    vec4 CoolColor;     // цвет остывшего края
};

out vec4 fragColor;

const float RS = 1.0;                // радиус горизонта
const float BCRIT = 2.59807621;      // 3·√3/2 — с таким прицельным параметром луч уже не вырывается
const float DIST = 28.0;             // где стоит наблюдатель
const float DISC_IN = 2.7;           // внутренний край диска: ближе вещество уже не держится на орбите
const float DISC_OUT = 13.0;
const int MAX_STEPS = 220;

float hash13(vec3 p) {
    p = fract(p * vec3(0.1031, 0.1030, 0.0973));
    p += dot(p, p.zyx + 31.32);
    return fract((p.x + p.y) * p.z);
}

/** Своё звёздное поле: по одной звезде на ячейку сетки направлений, редко и разного цвета. */
vec3 stars(vec3 d) {
    vec3 total = vec3(0.0);
    for (int layer = 0; layer < 2; layer++) {
        float scale = layer == 0 ? 150.0 : 310.0;
        vec3 s = d * scale + float(layer) * 19.0;
        vec3 cell = floor(s);
        vec3 f = fract(s) - 0.5;
        float pick = hash13(cell);
        if (pick < 0.986) continue;
        float shine = hash13(cell + 7.7);
        // Мерцание: у каждой звезды своя частота, поэтому поле не «дышит» целиком
        float twinkle = 0.75 + 0.25 * sin(Time * (1.4 + shine * 2.6) + shine * 31.0);
        float spot = exp(-dot(f, f) * 62.0) * (0.35 + 0.65 * shine) * twinkle;
        vec3 tint = mix(vec3(0.72, 0.82, 1.0), vec3(1.0, 0.88, 0.7), hash13(cell + 13.3));
        total += tint * spot * (layer == 0 ? 1.0 : 0.55);
    }
    return total;
}

/** Свечение диска в точке пересечения. rayDir — куда летит луч, то есть от наблюдателя. */
vec3 disc(vec3 hit, float rd, vec3 rayDir) {
    float t = clamp((rd - DISC_IN) / (DISC_OUT - DISC_IN), 0.0, 1.0);
    // Температура падает к краю: внутри добела, снаружи в красноту
    vec3 tint = mix(HotColor.rgb, CoolColor.rgb, pow(t, 0.55));
    // У самого горизонта вещество раскалено так, что цвет уходит в белый — иначе диск читается
    // просто оранжевым кольцом, без ощущения жара
    tint = mix(tint, vec3(1.0), pow(1.0 - t, 3.0) * 0.6);
    float bright = (pow(1.0 - t, 2.1) + 0.05) * 1.3;

    // Кеплерово вращение: чем ближе к дыре, тем быстрее. Отсюда и закрутка рисунка, и доплер
    vec3 radial = normalize(vec3(hit.x, 0.0, hit.z));
    vec3 orbit = cross(vec3(0.0, 1.0, 0.0), radial);
    float beta = sqrt(0.5 * RS / rd);
    // Приближающийся край ярче в разы — это и делает картинку несимметричной, как на настоящих снимках
    float doppler = 1.0 / max(0.18, 1.0 - beta * dot(orbit, -rayDir));
    bright *= pow(doppler, 3.0);
    // Гравитационное красное смещение: у самого горизонта свет ослабевает
    bright *= sqrt(max(0.0, 1.0 - RS / rd));

    // Вещество падает не ровным кольцом, а клочьями. Крутим рисунок как единое целое: если дать
    // внутренним слоям честно обгонять внешние, за минуты игры набегут тысячи витков и диск
    // рассыплется в муар из концентрических колец. Ощущение вращения даёт и рисунок сам по себе
    float ang = atan(hit.z, hit.x);
    float phase = ang + Time * 0.45;
    float turbulence = sin(phase * 3.0 + rd * 1.3) * sin(phase * 5.0 - rd * 0.7 + Time * 0.3);
    bright *= 0.74 + 0.26 * turbulence;

    // Пологий луч идёт сквозь вещество дольше и забирает больше света — от этого диск
    // читается не бритвой, а слоем
    bright *= min(1.0 / max(0.22, abs(rayDir.y)), 3.2) * 0.45;

    // Мягкие края, чтобы не было видно, где диск обрезан
    bright *= smoothstep(0.0, 0.10, t) * smoothstep(1.0, 0.78, t);
    return tint * bright;
}

void main() {
    vec2 p = (texCoord0 * 2.0 - 1.0) * Extent;
    float rp = length(p);
    // Края кадра гасим, иначе на небе был бы виден квадрат
    float edge = 1.0 - smoothstep(Extent * 0.72, Extent * 0.99, rp);
    if (edge <= 0.001) discard;

    float ci = cos(Inclination), si = sin(Inclination);
    vec3 camPos = DIST * vec3(0.0, si, -ci);
    vec3 fwd = normalize(-camPos);
    vec3 right = vec3(1.0, 0.0, 0.0);
    vec3 up = cross(fwd, right);
    vec3 dir = normalize(fwd + (p.x * right + p.y * up) * (BCRIT / DIST));

    vec3 light = vec3(0.0);
    float occlusion = 0.0;

    // Далёкие лучи загибаются на доли градуса и до диска не достают — их не трассируем совсем.
    // Это отсекает большую часть кадра и делает эффект дешёвым
    if (rp < DISC_OUT / BCRIT + 0.45) {
        vec3 pos = camPos;
        vec3 h = cross(pos, dir);
        float h2 = dot(h, h);
        float previousY = pos.y;
        int steps = int(Steps);
        for (int i = 0; i < MAX_STEPS; i++) {
            if (i >= steps) break;
            float r2 = dot(pos, pos);
            float r = sqrt(r2);
            if (r < RS) {
                occlusion = 1.0;
                break;
            }
            if (r > DIST * 2.0 && dot(pos, dir) > 0.0) break;
            // Шаг по мере удаления растёт: у дыры считаем точно, вдали — грубо и быстро
            float dt = clamp(0.055 * r, 0.02, 1.5);
            vec3 accel = -1.5 * RS * h2 * pos / (r2 * r2 * r);
            vec3 next = pos + dir * dt + 0.5 * accel * dt * dt;
            dir = normalize(dir + accel * dt);
            // Луч пересёк плоскость диска — значит, что-то оттуда светит
            if (previousY * next.y < 0.0) {
                float k = previousY / (previousY - next.y);
                vec3 hit = mix(pos, next, k);
                float rd = length(hit.xz);
                if (rd > DISC_IN && rd < DISC_OUT) light += disc(hit, rd, dir);
            }
            previousY = next.y;
            pos = next;
        }
    }

    if (occlusion < 0.5) light += stars(dir) * StarPower;

    float fade = edge * Strength * vertexColor.a;
    // Предумноженная альфа: свет складываем с кадром, а тень его закрывает
    fragColor = vec4(light * vertexColor.rgb * fade, occlusion * fade);
}
