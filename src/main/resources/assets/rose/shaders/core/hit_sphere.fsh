#version 330

layout(std140) uniform HitSphere {
    vec4 Data;
    vec4 Tint;
};

in vec2 texCoord;
in vec4 vertexColor;

out vec4 fragColor;

float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    float a = hash(i);
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
}

float crackField(vec2 p, float time) {
    float n = noise(p * 3.4 + vec2(time * 0.31, -time * 0.17));
    float n2 = noise(p.yx * 5.1 + vec2(-time * 0.22, time * 0.19));
    float ridges = abs(n - 0.5) + abs(n2 - 0.5) * 0.62;
    return 1.0 - smoothstep(0.02, 0.11, ridges);
}

void main() {
    float progress = clamp(Data.x, 0.0, 1.0);
    float alpha = vertexColor.a;
    float mode = Data.z;
    float time = Data.w;
    float shatterDt = Tint.w;

    vec2 p = (texCoord - 0.5) * 2.0;
    float radiusSquared = dot(p, p);
    if (radiusSquared > 1.0 || alpha <= 0.0) discard;
    float fadeT = progress < 0.15 ? 0.0 : clamp((progress - 0.15) / 0.85, 0.0, 1.0);

    if (mode > 2.5) {
        float ang = atan(p.y, p.x) + fadeT * fadeT * 6.2831853 * 1.35;
        float rad = length(p);
        p = vec2(cos(ang), sin(ang)) * rad;
    }

    float r = length(p);
    if (r > 1.0) discard;
    float cracks = 0.0;
    float dissolve = fadeT;
    if (mode > 0.5 && mode < 1.5) {
        dissolve = mix(smoothstep(0.42, 0.72, progress), 1.0, shatterDt);
    } else if (mode > 1.5 && mode < 2.5) {
        dissolve = fadeT * fadeT;
    }

    if (dissolve > 0.0 || shatterDt > 0.0) {
        cracks = crackField(p, time);
        float cells = noise(p * 7.5 + 4.0 + time * 0.05);
        float breakMask = dissolve * 1.08 + cracks * dissolve * 0.55;
        if (cells < breakMask - 0.08) discard;
        if (cracks > 0.55 && dissolve > 0.18 && hash(p * 18.0) < dissolve * 0.85) discard;
    }

    float z = sqrt(max(0.0, 1.0 - r * r));
    vec3 normal = vec3(p, z);
    float fres = pow(1.0 - z, 2.2);
    float outline = smoothstep(0.88, 1.0, r);

    vec3 lightDir = normalize(vec3(-0.45, 0.55, 0.70));
    float spec = pow(max(dot(normal, lightDir), 0.0), 30.0);
    vec3 lightDir2 = normalize(vec3(0.40, -0.30, 0.60));
    float spec2 = pow(max(dot(normal, lightDir2), 0.0), 60.0) * 0.6;

    float crackGlow = cracks * smoothstep(0.08, 0.7, dissolve + shatterDt);
    vec3 base = mix(Tint.rgb, vertexColor.rgb, 0.22);
    vec3 col = mix(base, vec3(1.0), spec * 0.55 + spec2 * 0.35 + outline * 0.18 + crackGlow * 0.65);
    float a = clamp(0.18 + fres * 0.58 + outline * 0.72 + spec + spec2 + crackGlow, 0.0, 1.0) * alpha;
    a *= 1.0 - dissolve * 0.35;
    fragColor = vec4(col, a);
}
