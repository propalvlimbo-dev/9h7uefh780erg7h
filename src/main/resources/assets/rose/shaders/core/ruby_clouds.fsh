#version 330

#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

in vec4 vColor;
in vec2 vWorld;
in float vDist;

out vec4 fragColor;

// --- value noise -----------------------------------------------------------
float hash21(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}

float vnoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    float a = hash21(i);
    float b = hash21(i + vec2(1.0, 0.0));
    float c = hash21(i + vec2(0.0, 1.0));
    float d = hash21(i + vec2(1.0, 1.0));
    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
}

float fbm(vec2 p) {
    float sum = 0.0;
    float amp = 0.55;
    for (int i = 0; i < 5; i++) {
        sum += amp * vnoise(p);
        p = p * 2.07 + vec2(11.3, 7.7);
        amp *= 0.5;
    }
    return sum;
}

void main() {
    // slow, natural drift driven by the game clock (seconds)
    vec2 drift = vec2(GameTime * 5.5, GameTime * 1.9);
    vec2 p = (vWorld + drift) * 0.0125;

    float density = fbm(p);

    // vColor.a carries the coverage threshold (set from the GUI settings)
    float threshold = 1.0 - clamp(vColor.a, 0.05, 0.95);
    float mask = smoothstep(threshold, threshold + 0.17, density);
    if (mask <= 0.003) {
        discard;
    }

    // fake volumetric lighting from the density gradient
    float dx = fbm(p + vec2(0.085, 0.0)) - density;
    float dz = fbm(p + vec2(0.0, 0.085)) - density;
    vec3 nrm = normalize(vec3(dx * 9.0, 0.72, dz * 9.0));
    float light = clamp(dot(nrm, normalize(vec3(0.32, 1.0, 0.44))), 0.0, 1.0);
    light = mix(0.30, 1.05, light);

    vec3 deep = vColor.rgb * 0.26;
    vec3 mid = vColor.rgb * 0.85;
    vec3 col = mix(deep, mid, smoothstep(0.0, 0.55, density)) * light;

    // silver lining on the rim of the puffs
    col += vColor.rgb * pow(mask, 3.0) * 0.75;

    // horizon / distance fade
    float fade = 1.0 - smoothstep(175.0, 330.0, vDist);

    fragColor = vec4(col * ColorModulator.rgb, mask * fade * 0.9 * ColorModulator.a);
}
