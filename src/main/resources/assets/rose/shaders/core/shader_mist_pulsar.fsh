#version 330

uniform sampler2D DepthSampler;

layout(std140) uniform ShaderMist {
    vec4 ResolutionTime;
    vec4 ColorAlpha;
    vec4 Params;
    vec4 CameraThunder;
    vec4 Extra;
};

out vec4 fragColor;

mat3 rotX(float a) {
    float c = cos(a), s = sin(a);
    return mat3(1.0, 0.0, 0.0,
                0.0,   c,   s,
                0.0,  -s,   c);
}
mat3 rotY(float a) {
    float c = cos(a), s = sin(a);
    return mat3(  c, 0.0,   s,
                0.0, 1.0, 0.0,
                 -s, 0.0,   c);
}

float hash(vec3 p) {
    p = fract(p * 0.1031);
    p += dot(p, p.yzx + 33.33);
    return fract((p.x + p.y) * p.z);
}

float noise(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);

    float n000 = hash(i + vec3(0.0, 0.0, 0.0));
    float n100 = hash(i + vec3(1.0, 0.0, 0.0));
    float n010 = hash(i + vec3(0.0, 1.0, 0.0));
    float n110 = hash(i + vec3(1.0, 1.0, 0.0));
    float n001 = hash(i + vec3(0.0, 0.0, 1.0));
    float n101 = hash(i + vec3(1.0, 0.0, 1.0));
    float n011 = hash(i + vec3(0.0, 1.0, 1.0));
    float n111 = hash(i + vec3(1.0, 1.0, 1.0));

    vec4 a = mix(vec4(n000, n010, n001, n011),
                 vec4(n100, n110, n101, n111), f.x);
    vec2 b = mix(a.xz, a.yw, f.y);
    return mix(b.x, b.y, f.z);
}

float fbm(vec3 p) {
    float v = 0.0;
    float a = 0.5;
    vec3 shift = vec3(100.0);
    for (int i = 0; i < 3; ++i) {
        v += a * noise(p);
        p = p * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}

vec3 starField(vec3 rd, float uTime, vec3 tint) {
    vec3 gridPos = floor(rd * 130.0);
    float starHash = hash(gridPos);

    vec3 starColor = vec3(0.0);
    if (starHash > 0.992) {
        vec3 starOffset = vec3(hash(gridPos + 1.1), hash(gridPos + 2.2), hash(gridPos + 3.3)) - 0.5;
        vec3 starPos = (gridPos + 0.5 + starOffset * 0.85) / 130.0;

        float d = length(rd - normalize(starPos));
        float twinkle = 0.55 + 0.45 * sin(uTime * 3.5 + starHash * 120.0);
        float starIntensity = exp(-d * 600.0) * twinkle * (0.3 + 0.7 * hash(gridPos + 5.5));

        vec3 col = mix(vec3(1.0), tint, 0.18);
        float colorHash = hash(gridPos + 7.7);
        if (colorHash < 0.22) {
            col = mix(vec3(1.0), tint, 0.48);
        } else if (colorHash < 0.38) {
            col = mix(tint, vec3(1.0), 0.38);
        } else if (colorHash < 0.43) {
            col = tint;
        }

        starColor = col * starIntensity;
    }
    return starColor;
}

void main() {
    vec2 uResolution = ResolutionTime.xy;
    float uTime = ResolutionTime.z;
    float uFov = ResolutionTime.w;
    vec3 uColor = ColorAlpha.rgb;
    float uAlpha = ColorAlpha.a;
    float uSpeed = Params.x;
    float uScale = Params.y;
    float uIntensity = Params.z;
    vec2 uCameraDir = CameraThunder.xy;

    vec2 uv = gl_FragCoord.xy / uResolution.xy;
    float depth = texture(DepthSampler, uv).r;
    float skyMask = smoothstep(Extra.y, Extra.z, 1.0 - depth);
    if (skyMask <= 0.001) {
        discard;
    }

    vec2 sp = uv * 2.0 - 1.0;
    float aspect = uResolution.x / uResolution.y;

    float tanV = tan(radians(uFov) * 0.5);
    vec3 rayV = normalize(vec3(sp.x * tanV * aspect, sp.y * tanV, 1.0));
    vec3 rayW = rotY(uCameraDir.x) * rotX(uCameraDir.y) * rayV;

    float n1 = fbm(rayW * uScale * 0.4 + vec3(uTime * 0.003, 0.0, 0.0));
    float n2 = fbm(rayW * uScale * 0.9 - vec3(0.0, uTime * 0.002, 0.0));

    vec3 gasColor = mix(uColor * 0.008, uColor * 0.065, n1);
    gasColor = mix(gasColor, mix(uColor, vec3(1.0), 0.12) * 0.04, n2 * n1);

    float dust = smoothstep(0.3, 0.7, n1 * 0.58 + n2 * 0.42);
    vec3 skyBackground = mix(gasColor, uColor * 0.002, dust * 0.85);

    vec3 col = skyBackground + starField(rayW, uTime, uColor);

    vec3 pulsarCenter = normalize(vec3(0.55, 0.65, -0.45));
    float frontMask = smoothstep(0.0, 0.4, dot(rayW, pulsarCenter));

    if (frontMask > 0.0) {
        vec3 tangent1 = normalize(cross(pulsarCenter, vec3(0.0, 1.0, 0.0)));
        vec3 tangent2 = cross(tangent1, pulsarCenter);

        vec2 p2d = vec2(dot(rayW, tangent1), dot(rayW, tangent2));
        float dCore = length(p2d);

        float pulse = 0.95 + 0.05 * sin(uTime * uSpeed * 8.0);
        float coreSize = 0.058 * pulse;
        float core = smoothstep(coreSize, 0.0, dCore);

        float flare = fbm(rayW * 12.0 + vec3(0.0, uTime * 0.25, 0.0));
        float halo = exp(-dCore * 16.0) * 3.2 + exp(-dCore * 3.5) * 0.9 * (0.7 + 0.3 * flare);

        float wobbleAngle = sin(uTime * uSpeed * 6.0) * 0.22;
        vec2 jetDir2d = vec2(sin(wobbleAngle), cos(wobbleAngle));

        float distAlong = dot(p2d, jetDir2d);
        float wave = sin(abs(distAlong) * 75.0 - uTime * uSpeed * 32.0) * 0.0055 * abs(distAlong);
        float distToJet = length(p2d - distAlong * jetDir2d) - wave;

        float jetIntensity = (exp(-distToJet * 320.0) * 3.0 + exp(-distToJet * 38.0) * 0.8);
        jetIntensity *= exp(-abs(distAlong) * 0.85);

        float jetFlicker = 0.82 + 0.18 * sin(uTime * 115.0) * cos(uTime * 145.0);

        vec3 coreColor = mix(vec3(1.0), uColor, 0.16);
        vec3 haloColor = uColor;
        vec3 jetColor = mix(uColor, vec3(1.0), 0.22);

        vec3 pulsarCol = mix(vec3(0.0), coreColor, core);
        pulsarCol += haloColor * halo;
        pulsarCol += jetColor * jetIntensity * jetFlicker;

        col += pulsarCol * frontMask;
    }

    col *= (1.0 + uIntensity * 10.0);

    fragColor = vec4(col * max(uAlpha, 0.0), skyMask);
}
