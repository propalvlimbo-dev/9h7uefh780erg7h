#version 150

uniform sampler2D ColorTexture;
uniform sampler2D DepthTexture;

uniform vec2 resolution;
uniform float time;
uniform vec2 handMotion;
uniform float effectAlpha;

in vec2 texCoord;
out vec4 fragColor;

float hash21(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}

float noise21(vec2 p) {
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
    float value = 0.0;
    float amplitude = 0.5;

    for (int i = 0; i < 5; i++) {
        value += noise21(p) * amplitude;
        p = p * 2.03 + vec2(17.1, 9.2);
        amplitude *= 0.5;
    }

    return value;
}

vec3 homiePalette(float t) {
    vec3 darkColor = vec3(0.015, 0.010, 0.035);
    vec3 purple = vec3(0.40, 0.055, 0.72);
    vec3 pink = vec3(1.00, 0.08, 0.52);
    vec3 cyan = vec3(0.08, 0.72, 1.00);

    vec3 color = mix(darkColor, purple, smoothstep(0.10, 0.62, t));
    color = mix(color, pink, smoothstep(0.50, 0.88, t));
    color += cyan * pow(max(t - 0.66, 0.0), 2.2) * 1.8;
    return color;
}

void main() {
    vec4 hand = texture(ColorTexture, texCoord);
    float depth = texture(DepthTexture, texCoord).r;

    if (hand.a <= 0.001 || depth <= 0.000001) {
        discard;
    }

    vec2 uv = texCoord;
    vec2 centered = (uv * resolution - 0.5 * resolution) / max(resolution.y, 1.0);

    centered.x += (handMotion.x - 0.5) * 0.18;
    centered.y += (handMotion.y - 0.5) * 0.12;

    float t = time * 0.06;

    vec2 warped = centered * 3.1;
    warped += vec2(
        fbm(centered * 2.2 + vec2(t, -t * 0.7)),
        fbm(centered * 2.4 + vec2(-t * 0.55, t))
    ) * 1.35;

    float cloudA = fbm(warped + vec2(t * 0.7, -t * 0.4));
    float cloudB = fbm(warped * 1.75 - vec2(t * 0.4, t * 0.65));
    float bands = 0.5 + 0.5 * sin(
        warped.x * 2.8 +
        warped.y * 2.0 +
        cloudB * 5.5 -
        time * 0.22
    );

    float field = clamp(
        cloudA * 0.72 +
        cloudB * 0.38 +
        bands * 0.30,
        0.0,
        1.0
    );

    vec3 effect = homiePalette(field);

    float edgeGlow = pow(
        clamp(1.0 - abs(depth * 2.0 - 1.0), 0.0, 1.0),
        0.35
    );

    float spark = step(0.993, hash21(floor(uv * resolution * 0.38)));
    spark *= 0.55 + 0.45 * sin(time * 2.4 + hash21(uv * 900.0) * 6.2831);
    effect += vec3(1.0, 0.55, 0.95) * spark * 1.8;

    effect += vec3(0.35, 0.08, 0.70) * edgeGlow * 0.42;

    float originalLight = dot(hand.rgb, vec3(0.299, 0.587, 0.114));
    effect *= mix(0.72, 1.20, originalLight);

    float alpha = hand.a * clamp(effectAlpha, 0.0, 1.0);
    fragColor = vec4(effect, alpha);
}
