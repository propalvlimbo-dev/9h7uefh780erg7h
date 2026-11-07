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
    float time = GameTime;
    vec3 n = normalize(vNormal);
    vec3 eye = normalize(-vViewPos);
    float ndv = clamp(abs(dot(n, eye)), 0.0, 1.0);
    float fresnel = pow(1.0 - ndv, 3.0);
    float softFresnel = pow(1.0 - ndv, 1.35);

    vec3 woodDark = vec3(0.115, 0.078, 0.058);
    vec3 woodMid = vec3(0.330, 0.215, 0.110);
    vec3 leafDark = vec3(0.085, 0.260, 0.130);
    vec3 leafLight = vec3(0.230, 0.520, 0.270);
    vec3 roseCore = vec3(0.520, 0.045, 0.130);
    vec3 rosePetal = vec3(0.895, 0.165, 0.295);
    vec3 petalLight = vec3(1.000, 0.500, 0.600);

    float v = vUv.y;
    float u = vUv.x;
    vec3 base;
    vec3 emissiveColor = rosePetal;
    float emissive = 0.0;

    if (u < 0.405) {
        float grain = rose_fbm(vec2(vUv.x * 26.0, v * 7.0));
        base = mix(woodDark, woodMid, clamp(v * 1.25, 0.0, 1.0)) * (0.80 + grain * 0.45);
    } else if (u < 0.690) {
        float veins = rose_fbm(vec2(vUv.x * 34.0, v * 9.0));
        float veinLines = pow(max(sin((vUv.x * 120.0 + veins * 6.0)), 0.0), 14.0);
        base = mix(leafDark, leafLight, clamp(v * 1.15, 0.0, 1.0)) * (0.84 + veins * 0.40 + veinLines * 0.30);
        emissive = 0.18;
        emissiveColor = leafLight;
        float autumn = smoothstep(0.86, 1.0, v);
        base = mix(base, rosePetal * 0.85, autumn * 0.75);
        emissive += autumn * 0.35;
    } else {
        float swirl = rose_fbm(vec2(vUv.y * 9.0 + time * 0.05, vUv.x * 40.0 - time * 0.04));
        float core = smoothstep(0.62, 0.0, v);
        base = mix(petalLight, rosePetal, smoothstep(0.0, 0.55, v));
        base = mix(base, roseCore, core * 0.85);
        base *= 0.86 + swirl * 0.42;
        emissive = 0.85 + core * 0.35;
        emissiveColor = mix(rosePetal, petalLight, 0.35 + 0.35 * sin(time * 1.4 + vUv.y * 5.0));
    }

    float light = mix(0.66, 1.08, clamp(vLight.y, 0.0, 1.0));
    vec3 color = base * light;

    float flow = sin(vUv.y * 26.0 - time * 2.6 + vUv.x * 7.0);
    color += rosePetal * pow(max(flow, 0.0), 8.0) * 0.18;

    float sparkleSeed = rose_hash21(floor(vUv * 220.0));
    float twinkle = step(0.9905, sparkleSeed) * (0.5 + 0.5 * sin(time * 8.0 + sparkleSeed * 80.0));
    color += vec3(1.0, 0.88, 0.92) * twinkle * (u > 0.69 ? 1.35 : 0.55);

    float pulse = 0.62 + 0.38 * sin(time * 1.9 + vUv.y * 6.0);
    color += emissiveColor * (emissive * pulse * (0.22 + fresnel * 0.95 + softFresnel * 0.28));
    color += rosePetal * fresnel * 0.35;

    color = clamp(color, vec3(0.0), vec3(1.55));
    float alpha = vColor.a * clamp(0.94 + fresnel * 0.06, 0.0, 1.0);
    if (alpha <= 0.004) {
        discard;
    }
    fragColor = vec4(color, alpha);
}
