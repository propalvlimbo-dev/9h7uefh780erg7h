#version 330

// Туманность через всё небо: широкая полоса вроде Млечного Пути, но клочковатая и цветная.
//
// Рисунок строится искажением области (domain warping): шум сначала считают в точке, потом этим же
// шумом сдвигают саму точку и считают заново — и так дважды. Из-за этого ровные разводы превращаются
// в волокна и завихрения, которых обычным сложением октав не получить.
//
// Координаты берутся не на плоскости, а на окружности: полоса замкнута вокруг неба, и если считать
// шум по «развёртке», на стыке будет видно шов.

in vec2 texCoord0;   // x — вдоль полосы (0..1 вокруг неба), y — поперёк (0.5 — середина)
in vec4 vertexColor;

layout(std140) uniform RoseParams {
    float Time;
    float Strength;     // общая видимость: ночь, дождь, ползунок
    float Density;      // насколько плотные клочья
    float Scale;        // крупность рисунка
    vec4 DeepColor;     // цвет холодных, разреженных мест
    vec4 WarmColor;     // цвет плотных, подсвеченных изнутри мест
};

out vec4 fragColor;

const float TAU = 6.28318531;

float hash13(vec3 p) {
    p = fract(p * vec3(0.1031, 0.1030, 0.0973));
    p += dot(p, p.zyx + 31.32);
    return fract((p.x + p.y) * p.z);
}

float noise3(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    float n000 = hash13(i);
    float n100 = hash13(i + vec3(1.0, 0.0, 0.0));
    float n010 = hash13(i + vec3(0.0, 1.0, 0.0));
    float n110 = hash13(i + vec3(1.0, 1.0, 0.0));
    float n001 = hash13(i + vec3(0.0, 0.0, 1.0));
    float n101 = hash13(i + vec3(1.0, 0.0, 1.0));
    float n011 = hash13(i + vec3(0.0, 1.0, 1.0));
    float n111 = hash13(i + vec3(1.0, 1.0, 1.0));
    return mix(mix(mix(n000, n100, f.x), mix(n010, n110, f.x), f.y),
               mix(mix(n001, n101, f.x), mix(n011, n111, f.x), f.y), f.z);
}

float fbm(vec3 p) {
    float sum = 0.0, amp = 0.5;
    for (int i = 0; i < 5; i++) {
        sum += amp * noise3(p);
        p *= 2.02;
        amp *= 0.5;
    }
    return sum;
}

void main() {
    // Полоса замкнута: идём по окружности, поэтому шва на стыке нет
    float along = texCoord0.x * TAU;
    float across = texCoord0.y;
    vec3 p = vec3(cos(along), sin(along), across * 1.6) * Scale;
    // Очень медленный дрейф: за игровой вечер рисунок заметно переливается, но никуда не «едет»
    p += vec3(0.0, 0.0, Time * 0.006);

    vec2 warp1 = vec2(fbm(p), fbm(p + vec3(4.7, 1.3, 2.9)));
    vec3 p2 = p + vec3(warp1 * 2.4, warp1.x * 1.1);
    vec2 warp2 = vec2(fbm(p2 + vec3(1.7, 9.2, 0.4)), fbm(p2 + vec3(8.3, 2.8, 5.1)));
    vec3 p3 = p + vec3(warp2 * 3.1, warp2.y * 1.4);
    float density = fbm(p3);

    // Поперёк полоса плавно сходит на нет, иначе будет видно её край
    float band = smoothstep(0.0, 0.42, across) * smoothstep(1.0, 0.58, across);
    // Клочья: ниже порога пусто, выше — быстро набирает плотность
    float cloud = smoothstep(0.42, 0.78, density * (0.65 + 0.7 * Density)) * band;
    if (cloud <= 0.002) discard;

    // Плотные места подсвечены изнутри, разреженные — холодные
    float hot = smoothstep(0.5, 0.92, density);
    vec3 tint = mix(DeepColor.rgb, WarmColor.rgb, hot);
    // Редкие яркие сгустки — там, где «рождаются звёзды»
    float core = pow(smoothstep(0.72, 0.98, density), 3.0);
    tint += WarmColor.rgb * core * 1.6;

    float alpha = cloud * Strength * vertexColor.a;
    fragColor = vec4(tint * vertexColor.rgb * alpha, alpha);
}
