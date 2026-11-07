#version 330

#moj_import <minecraft:globals.glsl>

in vec3 vViewPos;
in vec3 vNormal;
in vec2 vUv;
in vec4 vColor;
in vec2 vLight;

out vec4 fragColor;

float rose_hash21(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * vec3(443.8975, 397.2973, 491.1871));
    p3 += dot(p3, p3.yzx + 19.1919);
    return fract((p3.x + p3.y) * p3.z);
}

float rose_noise2(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    float a = rose_hash21(i);
    float b = rose_hash21(i + vec2(1.0, 0.0));
    float c = rose_hash21(i + vec2(0.0, 1.0));
    float d = rose_hash21(i + vec2(1.0, 1.0));
    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
}

float rose_fbm(vec2 p) {
    return rose_noise2(p) * 0.5 + rose_noise2(p * 2.07 + vec2(4.17, 7.91)) * 0.25
         + rose_noise2(p * 4.11 + vec2(9.83, 2.41)) * 0.125 + rose_noise2(p * 8.21 + vec2(1.37, 6.73)) * 0.0625;
}

void main() {
    vec2 block = floor(vUv * vec2(4.0, 8.0));
    vec2 local = fract(vUv * vec2(4.0, 8.0));
    float material = block.x + (block.y >= 1.5 ? 4.0 : 0.0);

    float time = GameTime;
    vec3 n = normalize(vNormal);
    vec3 eye = normalize(-vViewPos);
    float ndv = clamp(abs(dot(n, eye)), 0.0, 1.0);
    float fresnel = pow(1.0 - ndv, 3.2);
    float softFresnel = pow(1.0 - ndv, 1.3);

    vec3 woodDark = vec3(0.105, 0.070, 0.052);
    vec3 woodMid = vec3(0.290, 0.185, 0.098);
    vec3 roseCore = vec3(0.560, 0.055, 0.150);
    vec3 rosePetal = vec3(0.895, 0.165, 0.295);
    vec3 leafDark = vec3(0.100, 0.290, 0.150);
    vec3 leafLight = vec3(0.200, 0.480, 0.240);
    vec3 petalLight = vec3(1.000, 0.430, 0.540);
    vec3 thorn = vec3(0.640, 0.520, 0.310);
    vec3 accent = vec3(1.000, 0.330, 0.450);

    vec3 base = woodDark;
    float emissive = 0.0;
    vec3 emissiveColor = rosePetal;

    if (material < 0.5) {
        float grain = rose_fbm(vec2(local.x * 9.0, local.y * 2.0 + vUv.y * 40.0));
        base = woodDark * (0.78 + grain * 0.55);
    } else if (material < 1.5) {
        float grain = rose_fbm(vec2(local.x * 7.0, local.y * 2.4 + vUv.y * 32.0));
        base = woodMid * (0.80 + grain * 0.52);
    } else if (material < 2.5) {
        float swirl = rose_fbm(local * 6.0 + vec2(time * 0.06, -time * 0.04));
        base = mix(roseCore, rosePetal, 0.25 + swirl * 0.45);
        emissive = 0.95;
        emissiveColor = rosePetal;
    } else if (material < 3.5) {
        float swirl = rose_fbm(local * 5.0 + vec2(-time * 0.05, time * 0.03));
        base = mix(rosePetal, petalLight, 0.20 + swirl * 0.55);
        emissive = 0.62;
        emissiveColor = petalLight;
    } else if (material < 4.5) {
        float veins = rose_fbm(vec2(local.x * 4.0, local.y * 11.0));
        base = leafDark * (0.82 + veins * 0.48);
    } else if (material < 5.5) {
        float veins = rose_fbm(vec2(local.x * 4.5, local.y * 12.0));
        base = leafLight * (0.84 + veins * 0.46);
        emissive = 0.16;
        emissiveColor = leafLight;
    } else if (material < 6.5) {
        base = petalLight * 0.92;
        emissive = 0.78;
        emissiveColor = petalLight;
    } else {
        base = thorn * (0.86 + rose_fbm(local * 8.0) * 0.30);
        emissive = 0.20;
        emissiveColor = petalLight;
    }

    float light = mix(0.62, 1.10, clamp(vLight.y, 0.0, 1.0));
    vec3 color = base * light;

    float flow = sin(vUv.y * 42.0 - time * 3.4 + vUv.x * 9.0);
    float band = pow(max(flow, 0.0), 6.0);
    color += accent * band * 0.22;

    float sparkleSeed = rose_hash21(floor(vUv * 190.0));
    float twinkle = step(0.988, sparkleSeed) * (0.55 + 0.45 * sin(time * 9.0 + sparkleSeed * 90.0));
    color += vec3(1.0, 0.86, 0.90) * twinkle * 1.35;

    float pulse = 0.62 + 0.38 * sin(time * 2.1 + vUv.y * 7.0);
    color += emissiveColor * (emissive * pulse * (0.30 + fresnel * 1.25 + softFresnel * 0.35));
    color += accent * fresnel * 0.55;

    color = clamp(color, vec3(0.0), vec3(1.6));
    float alpha = vColor.a * clamp(0.90 + fresnel * 0.10 + emissive * 0.05, 0.0, 1.0);
    if (alpha <= 0.004) {
        discard;
    }
    fragColor = vec4(color, alpha);
}
