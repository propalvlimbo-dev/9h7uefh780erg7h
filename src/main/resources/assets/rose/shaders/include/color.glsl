// Цвет Rose: переходы между пространствами и шум.

vec3 rose_linearToSrgb(vec3 c) {
    c = max(c, 0.0);
    return mix(c * 12.92, 1.055 * pow(c, vec3(1.0 / 2.4)) - 0.055, step(0.0031308, c));
}

// OKLab — пространство, где смешивание цветов выглядит «как надо», без грязных середин.
vec3 rose_oklabToLinear(vec3 lab) {
    float l = lab.x + 0.3963377774 * lab.y + 0.2158037573 * lab.z;
    float m = lab.x - 0.1055613458 * lab.y - 0.0638541728 * lab.z;
    float s = lab.x - 0.0894841775 * lab.y - 1.2914855480 * lab.z;
    l = l * l * l;
    m = m * m * m;
    s = s * s * s;
    return vec3(
         4.0767416621 * l - 3.3077115913 * m + 0.2309699292 * s,
        -1.2684380046 * l + 2.6097574011 * m - 0.3413193965 * s,
        -0.0041960863 * l - 0.7034186147 * m + 1.7076147010 * s);
}

// Размытие считается в «линейном свете» (гамма 2.2) — яркие места не тускнеют.
vec3 rose_toGamma(vec3 c) {
    return pow(max(c, 0.0), vec3(1.0 / 2.2));
}

float rose_noise(vec2 fragCoord) {
    return fract(sin(dot(fragCoord, vec2(12.9898, 78.233))) * 43758.5453);
}

float rose_hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

// Плавный шум для «живой» жидкости.
float rose_valueNoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(rose_hash(i), rose_hash(i + vec2(1.0, 0.0)), u.x),
               mix(rose_hash(i + vec2(0.0, 1.0)), rose_hash(i + vec2(1.0, 1.0)), u.x), u.y);
}
