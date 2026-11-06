#version 330

uniform sampler2D MaskSampler;
uniform sampler2D SceneSampler;
uniform sampler2D SurfaceSampler;

layout(std140) uniform EspChamsFill {
    vec4 Params;
    vec4 TintColor;
    vec4 Color2;
    vec4 Color3;
    vec4 View;
};

in vec2 TexCoord;
out vec4 fragColor;

float hash21(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(
        mix(hash21(i), hash21(i + vec2(1.0, 0.0)), f.x),
        mix(hash21(i + vec2(0.0, 1.0)), hash21(i + 1.0), f.x),
        f.y
    );
}

float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.5;
    mat2 rotation = mat2(1.6, 1.2, -1.2, 1.6);
    for (int octave = 0; octave < 5; octave++) {
        value += noise(p) * amplitude;
        p = rotation * p;
        amplitude *= 0.55;
    }
    return value;
}

vec3 cosmosFill(vec2 uv, vec3 tint, float time) {
    vec2 p = uv * vec2(0.75, 1.8);
    vec2 drift = vec2(time * 0.035, -time * 0.022);

    float cloudA = fbm(p * 5.2 + drift);
    float cloudB = fbm(p * 9.0 - drift * 1.7 + cloudA * 1.8);
    float nebula = smoothstep(0.28, 0.88, cloudA * 0.58 + cloudB * 0.62);

    vec2 starGrid = p * 42.0;
    vec2 starCell = floor(starGrid);
    vec2 starUv = fract(starGrid) - 0.5;
    float starSeed = hash21(starCell);
    float star = 1.0 - smoothstep(0.0, 0.095, length(starUv));
    star *= smoothstep(0.965, 1.0, starSeed);
    star *= 0.65 + 0.35 * sin(time * 3.0 + starSeed * 25.0);

    vec3 deep = mix(vec3(0.008, 0.012, 0.055), tint * 0.16, 0.7);
    vec3 nebulaColor = mix(tint * 0.55, vec3(0.38, 0.16, 0.82), 0.42);
    vec3 color = deep + nebulaColor * nebula * 1.35;
    color += mix(tint, vec3(0.72, 0.92, 1.0), 0.7) * star * 2.6;
    return color;
}

vec3 plasmaFill(vec2 uv, vec3 tint, float time) {
    vec2 p = uv * vec2(1.65, 3.95);
    float fieldA = fbm(p + vec2(time * 0.42, -time * 0.28));
    float fieldB = fbm(p * 1.75 + fieldA * 2.4 + vec2(-time * 0.24, time * 0.36));
    float plasma = smoothstep(0.16, 0.94, fieldB);
    float veins = pow(smoothstep(0.50, 0.92, fieldA + fieldB * 0.45), 2.2);
    vec3 hot = mix(tint, vec3(1.0), 0.72);
    return mix(tint * 0.28, tint, plasma) + hot * veins * 0.95;
}

vec3 glassFill(vec2 uv, vec3 tint) {
    vec4 blurredSample = texture(SceneSampler, uv);
    vec3 blurredScene = clamp(
        blurredSample.rgb / max(blurredSample.a, 0.08),
        vec3(0.0),
        vec3(1.0)
    );
    float luminance = dot(blurredScene, vec3(0.299, 0.587, 0.114));
    vec3 frosted = mix(blurredScene, vec3(luminance), 0.12);
    vec3 tinted = frosted * mix(vec3(1.0), tint, 0.40) + tint * 0.13;
    return mix(frosted, tinted, 0.68);
}

float resolveFillCoverage(float mask) {
    if (Params.y < 0.5 || Params.z < 0.5) {
        return clamp(mask * 2.0, 0.0, 1.0);
    }

    float marker = 1.0 - smoothstep(0.62, 0.86, mask);
    return clamp(mask * 2.0, 0.0, 1.0) * marker;
}

float friendFromMask(vec4 color) {
    if (Params.y < 0.5 || color.a < 0.01) return 0.0;
    vec3 rgb = color.rgb / max(color.a, 0.01);
    float green = rgb.g;
    float notRed = step(rgb.r + 0.28, green);
    float notBlue = step(rgb.b + 0.28, green);
    return step(0.72, green) * notRed * notBlue;
}

void main() {
    vec4 maskColor = texture(MaskSampler, TexCoord);
    float mask = maskColor.a;
    float coverage = resolveFillCoverage(mask);
    float alpha = coverage * clamp(Params.x, 0.0, 1.0);
    if (alpha <= 0.002) discard;
    int style = int(Params.w + 0.5);
    vec2 patternUv = TexCoord - 0.5;
    if (Params.y > 0.5 && (style == 2 || style == 3)) {
        patternUv = vec2(0.0);
        if (View.z > 0.5) {
            vec4 encodedUv = floor(texture(SurfaceSampler, TexCoord) * 255.0 + 0.5);
            patternUv = (vec2(dot(encodedUv.rg, vec2(256.0, 1.0)), dot(encodedUv.ba, vec2(256.0, 1.0))) / 65535.0 - 0.5) * 8.0;
        }
    }
    vec3 tint = TintColor.rgb;
    vec3 color = tint;

    if (style == 2) {
        color = cosmosFill(patternUv, tint, View.w);
    } else if (style == 3) {
        color = plasmaFill(patternUv, tint, View.w);
    } else if (style == 4) {
        color = glassFill(TexCoord, tint);
    }

    fragColor = vec4(max(color, vec3(0.0)), alpha);
}
