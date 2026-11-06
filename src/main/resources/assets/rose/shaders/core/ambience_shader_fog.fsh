#version 330

uniform sampler2D DepthSampler;

layout(std140) uniform ShaderFog {
    mat4 InvProjection;
    mat4 InvView;
    vec4 CameraTime;
    vec4 ScreenModeIntensity;
    vec4 PurpleColor;
    vec4 Params;
    vec4 AuroraColor;
    vec4 NebulaColor;
    vec4 ExtraParams;
};

in vec2 TexCoord;
out vec4 fragColor;

const mat3 NOISE_BASIS = mat3(
     0.580613, -0.688201,  0.435941,
     0.572688,  0.722526,  0.381346,
    -0.578789,  0.066232,  0.812905
);

mat3 rotX(float a) {
    float c = cos(a), s = sin(a);
    return mat3(
        1.0, 0.0, 0.0,
        0.0, c, s,
        0.0, -s, c
    );
}

mat3 rotY(float a) {
    float c = cos(a), s = sin(a);
    return mat3(
        c, 0.0, s,
        0.0, 1.0, 0.0,
        -s, 0.0, c
    );
}

float hash31(vec3 p) {
    p = fract(p * 0.1031);
    p += dot(p, p.yzx + 33.33);
    return fract((p.x + p.y) * p.z);
}

float ambienceValueNoise(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);

    float n000 = hash31(i + vec3(0.0, 0.0, 0.0));
    float n100 = hash31(i + vec3(1.0, 0.0, 0.0));
    float n010 = hash31(i + vec3(0.0, 1.0, 0.0));
    float n110 = hash31(i + vec3(1.0, 1.0, 0.0));
    float n001 = hash31(i + vec3(0.0, 0.0, 1.0));
    float n101 = hash31(i + vec3(1.0, 0.0, 1.0));
    float n011 = hash31(i + vec3(0.0, 1.0, 1.0));
    float n111 = hash31(i + vec3(1.0, 1.0, 1.0));

    float nx00 = mix(n000, n100, f.x);
    float nx10 = mix(n010, n110, f.x);
    float nx01 = mix(n001, n101, f.x);
    float nx11 = mix(n011, n111, f.x);
    return mix(mix(nx00, nx10, f.y), mix(nx01, nx11, f.y), f.z);
}

float fbm(vec3 p) {
    float value = 0.0;
    float amplitude = 0.55;
    vec3 q = NOISE_BASIS * p;
    for (int i = 0; i < 3; i++) {
        value += ambienceValueNoise(q) * amplitude;
        q = q * 2.03 + vec3(13.1, 7.7, 5.3);
        amplitude *= 0.5;
    }
    return value;
}

vec3 worldRay(vec2 uv) {
    vec2 sp = uv * 2.0 - 1.0;
    float aspect = ScreenModeIntensity.x / max(ScreenModeIntensity.y, 1.0);
    float tanV = tan(radians(max(CameraTime.z, 1.0)) * 0.5);
    vec3 rayView = normalize(vec3(sp.x * tanV * aspect, sp.y * tanV, 1.0));
    return normalize(rotY(CameraTime.x) * rotX(CameraTime.y) * rayView);
}

vec3 starField(vec3 ray, float time, float threshold, float brightness) {
    vec3 oriented = NOISE_BASIS * ray;
    vec3 gridPos = floor(oriented * 140.0);
    float starHash = hash31(gridPos);
    if (starHash < threshold) {
        return vec3(0.0);
    }

    vec3 starOffset = vec3(
        hash31(gridPos + vec3(1.1, 0.0, 0.0)),
        hash31(gridPos + vec3(0.0, 2.2, 0.0)),
        hash31(gridPos + vec3(0.0, 0.0, 3.3))
    ) - 0.5;
    vec3 localPos = (gridPos + 0.5 + starOffset * 0.75) / 140.0;
    vec3 starDir = normalize(transpose(NOISE_BASIS) * localPos);
    float d = length(ray - starDir);
    float twinkle = 0.7 + 0.3 * sin(time * (0.55 + starHash * 2.2) + starHash * 37.0);
    float intensity = exp(-d * 720.0) * twinkle * brightness
        * (0.35 + 0.65 * hash31(gridPos + vec3(5.5)));
    return vec3(intensity);
}

vec3 galaxy(vec3 ray, vec3 primary, vec3 secondary, float time) {
    vec3 p = ray * 3.4;
    float cloud = fbm(p + vec3(time * 0.018, -time * 0.011, time * 0.014));
    float ridge = 1.0 - abs(fbm(p * 1.65 - vec3(time * 0.012)) * 2.0 - 1.0);
    ridge = pow(max(ridge, 0.0), 3.0);

    vec3 deep = mix(vec3(0.002), primary, 0.055);
    vec3 color = deep + primary * cloud * 0.52 + secondary * ridge * 0.62;
    color += starField(ray, time, 0.9945, 3.8);
    return color;
}

vec3 milkyStars(vec3 ray, vec3 primary, vec3 secondary, float density, float scale, float brightness, float sharpness) {
    vec3 oriented = NOISE_BASIS * ray;
    vec3 gridPos = floor(oriented * scale);
    float starHash = hash31(gridPos + vec3(density * 17.0));
    if (starHash < density) {
        return vec3(0.0);
    }

    vec3 starOffset = vec3(
        hash31(gridPos + vec3(1.7, 0.0, 0.0)),
        hash31(gridPos + vec3(0.0, 2.9, 0.0)),
        hash31(gridPos + vec3(0.0, 0.0, 4.1))
    ) - 0.5;
    vec3 localPos = (gridPos + 0.5 + starOffset * 0.45) / scale;
    if (dot(localPos, localPos) < 1e-10) {
        return vec3(0.0);
    }
    vec3 starDir = normalize(transpose(NOISE_BASIS) * localPos);
    float d = length(ray - starDir);
    if (d > 0.02) {
        return vec3(0.0);
    }

    float mag = 0.3 + 0.7 * hash31(gridPos + vec3(8.2));
    float core = exp(-d * d * sharpness);
    float glow = exp(-d * d * sharpness * 0.06) * 0.28;
    float intensity = min((core + glow) * brightness * mag, 1.5);

    float tintPick = hash31(gridPos + vec3(13.7));
    vec3 tint = mix(primary, mix(primary, vec3(1.0), 0.45), step(0.4, tintPick));
    tint = mix(tint, secondary, step(0.78, tintPick));
    tint = mix(tint, mix(primary, secondary, 0.5), step(0.94, tintPick));
    return tint * intensity;
}

vec3 milkyWay(vec3 ray, vec3 primary, vec3 secondary, float time) {
    vec3 deep = vec3(0.0);

    float live = Params.w;
    float breath = 0.5 + 0.5 * sin(live * 1.15);
    float breath2 = 0.5 + 0.5 * sin(live * 0.73 + 1.7);
    float sizePulse = 1.0 + (breath * 0.08 - 0.04);
    float glowPulse = 0.88 + breath * 0.22;
    float armPulse = 0.92 + breath2 * 0.16;

    vec3 center = normalize(vec3(0.06, 0.5, 0.86));
    vec3 major = normalize(vec3(0.9, 0.22, -0.38));
    vec3 minor = normalize(cross(center, major));
    major = normalize(cross(minor, center));

    float z = dot(ray, center);
    float x = dot(ray, major);
    float y = dot(ray, minor);
    float facing = smoothstep(0.1, 0.42, z);

    float persp = 1.0 / max(z * 0.5 + 0.62, 0.42);
    float size = 1.85 * sizePulse;
    float ex = x * persp * 0.62 * size;
    float ey = y * persp * 2.45 * size;
    float er2 = ex * ex + ey * ey;
    float er = sqrt(max(er2, 1e-6));
    float ang = atan(ey, ex);

    float n1 = fbm(vec3(ex, ey, live * 0.02) * 2.2);
    float n2 = fbm(vec3(ex, ey, 1.3) * 4.2);
    float n3 = fbm(vec3(ex, ey, 2.7) * 8.0);

    float spiral = ang * 2.0 + er * 5.2;
    float arm1 = pow(0.5 + 0.5 * sin(spiral + n1 * 2.2 + breath * 0.35), 2.6);
    float arm2 = pow(0.5 + 0.5 * sin(spiral + 3.14159 + n2 * 1.8), 2.4);
    float arm3 = pow(0.5 + 0.5 * sin(ang + er * 2.0 + 0.7 + n2), 1.8) * 0.55;
    float arms = max(max(arm1, arm2), arm3) * armPulse;
    arms *= 0.5 + 0.5 * n2;

    float diskCore = exp(-er2 * 1.2) * facing;
    float disk = exp(-er2 * 0.52) * facing;
    float diskSoft = exp(-er2 * 0.22) * facing;
    float diskWide = exp(-er2 * 0.085) * facing;
    float diskFar = exp(-er2 * 0.038) * facing;

    float coreMask = exp(-er2 * 16.0) * facing;
    float coreMid = exp(-er2 * 6.5) * facing;
    float bulge = exp(-er2 * 2.8) * facing;
    float coreStruct = 0.55 + 0.45 * n2;
    coreMask *= mix(1.0, coreStruct, 0.55) * glowPulse;
    coreMid *= mix(1.0, 0.6 + 0.4 * n1, 0.4) * glowPulse;

    float dustA = 0.5 + 0.5 * sin(spiral + 0.6 + n2 * 3.0);
    float dustB = 0.5 + 0.5 * sin(ang * 3.0 + er * 4.0 - 1.2 + n3 * 2.0);
    float dust = max(
        smoothstep(0.2, 0.65, dustA) * smoothstep(0.3, 0.75, n2),
        smoothstep(0.35, 0.7, dustB) * smoothstep(0.4, 0.8, n3) * 0.7
    );
    dust *= smoothstep(0.06, 0.22, er) * smoothstep(1.2, 0.4, er);
    dust *= (0.25 + 0.75 * arms) * facing;

    float milk = disk * (0.2 + 0.45 * n3) * (0.35 + 0.65 * arms);
    milk += diskSoft * n2 * 0.15 * arms;

    vec3 colCore = mix(vec3(1.0), primary, 0.18);
    vec3 colWarm = mix(primary, vec3(1.0), 0.28);
    vec3 colOrange = mix(primary, secondary, 0.24);
    vec3 colBrown = primary * 0.34;
    vec3 colPink = mix(primary, secondary, 0.36);
    vec3 colPurple = mix(primary, secondary, 0.58);
    vec3 colViolet = mix(primary, secondary, 0.76);
    vec3 colBlue = secondary;
    vec3 colDust = mix(primary, secondary, 0.5) * 0.12;

    float t = clamp(er * 0.78, 0.0, 1.0);
    vec3 diskCol = mix(colWarm, colOrange, smoothstep(0.04, 0.2, t));
    diskCol = mix(diskCol, colBrown, smoothstep(0.12, 0.32, t));
    diskCol = mix(diskCol, colPink, smoothstep(0.25, 0.48, t));
    diskCol = mix(diskCol, colPurple, smoothstep(0.4, 0.68, t));
    diskCol = mix(diskCol, mix(colViolet, colBlue, 0.5), smoothstep(0.58, 1.0, t));

    vec3 color = deep;
    color += diskFar * colViolet * (0.07 + 0.08 * n1) * glowPulse;
    color += diskWide * mix(colPurple, colBlue, 0.4) * (0.12 + 0.14 * n1) * (0.4 + 0.6 * arms) * glowPulse;
    color += diskSoft * diskCol * (0.2 + 0.65 * arms) * glowPulse;
    color += disk * diskCol * (0.16 + milk * 1.05) * glowPulse;
    color += diskCore * mix(colWarm, colPink, 0.3) * arms * 0.4 * glowPulse;
    color += bulge * colWarm * 0.38 * coreStruct * glowPulse;
    color += coreMid * mix(colWarm, colCore, 0.55) * 0.7;
    color += coreMask * colCore * 0.85;
    color += coreMask * coreMask * mix(vec3(1.0), primary, 0.16) * 0.22;

    color = mix(color, color * colDust, dust * 0.88);
    color *= 1.0 - dust * 0.52;

    vec2 c1 = vec2(ex - 0.38, ey - 0.1);
    vec2 c2 = vec2(ex + 0.52, ey + 0.26);
    color += exp(-dot(c1, c1) * 60.0) * facing * mix(primary, vec3(1.0), 0.3) * 0.5 * glowPulse;
    color += exp(-dot(c2, c2) * 26.0) * facing * mix(primary, secondary, 0.6) * 0.26 * glowPulse;

    color = max(color, deep);

    color += milkyStars(ray, primary, secondary, 0.97, 160.0, 1.4, 120000.0);
    color += milkyStars(ray, primary, secondary, 0.994, 85.0, 2.8, 70000.0);
    color += milkyStars(ray, primary, secondary, 0.9985, 48.0, 4.5, 42000.0);

    return color;
}

vec3 purpleFog(vec3 ray, vec3 tint, float time) {
    vec3 drift = vec3(time * 0.035, -time * 0.018, time * 0.026);
    float large = fbm(ray * 2.8 + drift);
    float detail = fbm(ray * 7.5 - drift * 1.7);
    float bands = 0.5 + 0.5 * sin(dot(NOISE_BASIS * ray, vec3(2.4, 9.5, -3.1)) + large * 7.0 + time * 0.12);
    float density = clamp(large * 0.75 + detail * 0.28 + bands * 0.18, 0.0, 1.0);
    vec3 darkTint = tint * 0.14;
    vec3 brightTint = min(vec3(1.0), mix(tint, vec3(1.0), 0.12) * 1.35);
    return mix(darkTint, brightTint, density);
}

vec3 aurora(vec3 ray, vec3 tint, vec3 secondary, float time) {
    vec3 p = ray * 2.6;
    float warp = fbm(p * 1.4 + vec3(time * 0.05, time * 0.07, -time * 0.03));
    float curtain = fbm(p * 1.6 + warp * 1.6 + vec3(time * 0.03, -time * 0.09, time * 0.02));
    float ribbon = pow(clamp(curtain * 1.45 - 0.28, 0.0, 1.0), 2.0);
    float band = 0.55 + 0.45 * sin(dot(NOISE_BASIS * ray, vec3(3.5, 1.2, 3.5)) + warp * 5.0 + time * 0.3);
    ribbon *= band;

    float elevSoft = 0.55 + 0.45 * smoothstep(-1.0, 0.85, ray.y);
    ribbon *= elevSoft;

    vec3 nightSky = mix(tint * 0.025, secondary * 0.065, elevSoft);
    vec3 glowLow = tint * 0.9;
    vec3 glowHigh = mix(tint, secondary, 0.55) * 1.2;
    vec3 auroraColor = mix(glowLow, glowHigh, clamp(ray.y * 0.9 + 0.5, 0.0, 1.0));
    vec3 color = nightSky + auroraColor * ribbon;
    color += starField(ray, time, 0.9955, 3.0) * (1.0 - ribbon * 0.5);
    return color;
}

vec3 nebula(vec3 ray, vec3 tint, vec3 secondary, float time) {
    vec3 drift = vec3(time * 0.02, -time * 0.014, time * 0.017);
    vec3 p = ray * 3.0 + drift;
    float warp = fbm(p * 1.1);
    vec3 warped = p + warp * 0.8;
    float cloud = fbm(warped * 1.6 - vec3(time * 0.01));
    float wisp = fbm(warped * 3.4 + vec3(time * 0.03, 0.0, -time * 0.02));
    float density = clamp(cloud * 0.65 + wisp * 0.4, 0.0, 1.0);

    vec3 deep = mix(tint, secondary, 0.5) * 0.035;
    vec3 warm = tint * 1.3;
    vec3 glow = mix(tint * 0.4, mix(secondary, vec3(1.0), 0.24), pow(density, 2.0) * 0.6);
    vec3 color = deep + warm * density * 0.5 + glow * pow(density, 3.0) * 0.6;
    color += starField(ray, time, 0.9965, 2.8) * 0.8;
    return color;
}

vec3 plasma(vec3 ray, vec3 tint, vec3 secondary, float time) {
    vec3 p = NOISE_BASIS * ray * 3.1;
    vec3 drift = vec3(time * 0.045, -time * 0.028, time * 0.033);

    float waveA = sin(dot(p, vec3(1.7, 2.4, 0.9)) + time * 1.15
        + sin(dot(p, vec3(0.6, 1.9, -1.1)) - time * 0.55) * 1.8);
    float waveB = sin(dot(p, vec3(-2.1, 1.3, 1.8)) - time * 0.95
        + sin(dot(p, vec3(1.4, -0.8, 1.6)) + time * 0.7) * 1.4);
    float waveC = sin(dot(p, vec3(1.1, 1.1, 1.6)) + time * 0.65);
    float waveD = sin(length(p.xy + p.zz * 0.35) * 2.4 - time * 1.35);
    float field = (waveA + waveB + waveC + waveD) * 0.25;

    float cloud = fbm(ray * 2.4 + drift);
    float detail = fbm(ray * 6.2 - drift * 1.4);
    float density = clamp(field * 0.55 + 0.5 + cloud * 0.28 + detail * 0.16, 0.0, 1.0);
    density = smoothstep(0.12, 0.92, density);

    vec3 deep = mix(tint, secondary, 0.5) * 0.025;
    vec3 hot = min(vec3(1.0), mix(tint, vec3(1.0), 0.2) * 1.35);
    vec3 cool = mix(tint, secondary, 0.65);
    vec3 mid = mix(tint, cool, 0.45);

    vec3 color = mix(deep, mid, density);
    color = mix(color, hot, pow(density, 2.4));

    float filament = pow(max(0.0, 1.0 - abs(waveA + waveB * 0.7) * 0.72), 7.0);
    float sparks = pow(0.5 + 0.5 * sin(dot(p, vec3(4.0, 14.0, -3.0)) - time * 6.5 + waveC * 3.0), 14.0);
    color += hot * filament * 0.42;
    color += cool * sparks * 0.28;

    float elevSoft = 0.62 + 0.38 * smoothstep(-1.0, 0.9, ray.y);
    color *= elevSoft;

    color += starField(ray, time, 0.996, 2.9) * (1.0 - density * 0.55);
    return color;
}

const float STARDUST_PI = 3.14159265359;
const float STARDUST_TAU = 6.28318530718;

float stardustSaturate(float value) {
    return clamp(value, 0.0, 1.0);
}

vec3 stardustSaturate3(vec3 value) {
    return clamp(value, vec3(0.0), vec3(1.0));
}

float stardustHash21(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}

float stardustHash31(vec3 p) {
    p = fract(p * vec3(0.1031, 0.11369, 0.13787));
    p += dot(p, p.yzx + 19.19);
    return fract((p.x + p.y) * p.z);
}

float stardustNoise2(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * (3.0 - 2.0 * f);
    float a = stardustHash21(i);
    float b = stardustHash21(i + vec2(1.0, 0.0));
    float c = stardustHash21(i + vec2(0.0, 1.0));
    float d = stardustHash21(i + vec2(1.0, 1.0));
    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

float stardustNoise3(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    vec3 u = f * f * (3.0 - 2.0 * f);
    float n000 = stardustHash31(i + vec3(0.0, 0.0, 0.0));
    float n100 = stardustHash31(i + vec3(1.0, 0.0, 0.0));
    float n010 = stardustHash31(i + vec3(0.0, 1.0, 0.0));
    float n110 = stardustHash31(i + vec3(1.0, 1.0, 0.0));
    float n001 = stardustHash31(i + vec3(0.0, 0.0, 1.0));
    float n101 = stardustHash31(i + vec3(1.0, 0.0, 1.0));
    float n011 = stardustHash31(i + vec3(0.0, 1.0, 1.0));
    float n111 = stardustHash31(i + vec3(1.0, 1.0, 1.0));
    float nx00 = mix(n000, n100, u.x);
    float nx10 = mix(n010, n110, u.x);
    float nx01 = mix(n001, n101, u.x);
    float nx11 = mix(n011, n111, u.x);
    float nxy0 = mix(nx00, nx10, u.y);
    float nxy1 = mix(nx01, nx11, u.y);
    return mix(nxy0, nxy1, u.z);
}

float stardustFbm3(vec3 p) {
    float value = 0.0;
    float amp = 0.52;
    for (int i = 0; i < 4; i++) {
        value += stardustNoise3(p) * amp;
        p = p * 2.031 + vec3(17.17, -11.31, 23.83);
        amp *= 0.52;
    }
    return value;
}

float stardustFbm2(vec2 p) {
    float value = 0.0;
    float amp = 0.55;
    mat2 m = mat2(1.74, -1.08, 1.08, 1.74);
    for (int i = 0; i < 4; i++) {
        value += stardustNoise2(p) * amp;
        p = m * p + vec2(9.7, -13.1);
        amp *= 0.50;
    }
    return value;
}

vec3 stardustExposureTone(vec3 color, float exposure) {
    return vec3(1.0) - exp(-max(color, vec3(0.0)) * exposure);
}

float stardustTime() {
    return CameraTime.w * Params.x;
}

vec3 stardustCameraDir() {
    return normalize(rotY(CameraTime.x) * rotX(CameraTime.y) * vec3(0.0, 0.0, 1.0));
}

vec3 stardustProjectionDir() {
    vec2 ndc = TexCoord * 2.0 - 1.0;
    vec4 view = InvProjection * vec4(ndc, ROSE_DEPTH_TO_CLIP(0.000001), 1.0);
    vec3 viewDir = normalize(view.xyz / max(abs(view.w), 0.00001));
    return normalize((InvView * vec4(viewDir, 0.0)).xyz);
}

vec3 stardustSunDirection() {
    float a = ExtraParams.y * STARDUST_TAU - STARDUST_PI * 0.48;
    float low = sin(a) * 0.18 - 0.045;
    return normalize(vec3(cos(a) * 0.78, low, sin(a) * 0.62));
}

float stardustStarsOn() {
    return step(0.5, max(Params.w, ExtraParams.x));
}

float stardustStarLayer(vec3 d, float scale, float threshold, float brightness) {
    if (stardustStarsOn() < 0.5) {
        return 0.0;
    }
    vec3 g = d * scale;
    vec3 cell = floor(g);
    vec3 local = fract(g) - 0.5;
    float h = stardustHash31(cell);
    float alive = smoothstep(threshold, 1.0, h);
    vec2 p = local.xy + local.z * vec2(0.37, -0.29);
    float r = dot(p, p);
    float core = exp(-r * mix(380.0, 820.0, h));
    float halo = exp(-r * mix(18.0, 48.0, h));
    float rare = smoothstep(0.996, 1.0, h);
    float spike = (exp(-abs(p.x) * 120.0) * exp(-abs(p.y) * 15.0) + exp(-abs(p.y) * 120.0) * exp(-abs(p.x) * 15.0)) * rare;
    float twinkle = 0.72 + 0.28 * sin(stardustTime() * 9.0 + h * 73.0);
    return (core * 2.2 + halo * 0.34 + spike * 0.56) * alive * twinkle * brightness;
}

vec3 stardustStellarColor(vec3 d) {
    float h = stardustHash31(floor(d * 93.0));
    vec3 cold = max(AuroraColor.rgb, vec3(0.02));
    vec3 warm = max(PurpleColor.rgb, vec3(0.02));
    vec3 pearl = mix(vec3(1.0), mix(cold, warm, 0.5), 0.2);
    return mix(mix(cold, warm, smoothstep(0.16, 0.86, h)), pearl, 0.26);
}

vec3 stardustMoonDirection() {
    vec3 s = stardustSunDirection();
    return normalize(vec3(-s.x, max(-s.y, 0.12), -s.z));
}

vec4 stardustMoonLayer(vec3 d, vec3 primary, vec3 secondary, out float diskMask) {
    diskMask = 0.0;
    vec3 m = stardustMoonDirection();
    float cosA = clamp(dot(d, m), -1.0, 1.0);
    float rr = sqrt(max(2.0 * (1.0 - cosA), 0.0)) / 0.052;
    if (rr >= 5.0) {
        return vec4(0.0);
    }
    vec3 axisRight = normalize(cross(vec3(0.0, 1.0, 0.0), m));
    vec3 axisUp = cross(m, axisRight);
    vec2 mp = vec2(dot(d, axisRight), dot(d, axisUp)) / 0.052;
    float disk = 1.0 - smoothstep(0.90, 1.0, rr);
    diskMask = disk;
    float surf = stardustNoise2(mp * 3.1 + 4.7) * 0.6 + stardustNoise2(mp * 7.3 - 2.9) * 0.4;
    float shade = 0.74 + 0.26 * smoothstep(0.25, 0.78, surf);
    float limb = 1.0 - 0.34 * smoothstep(0.45, 1.0, rr);
    vec3 moonCol = mix(vec3(0.93, 0.96, 1.04), secondary, 0.16) * shade * limb;
    float halo = exp(-max(rr - 1.0, 0.0) * 1.7);
    vec3 haloCol = mix(vec3(0.72, 0.80, 1.00), primary, 0.30);
    return vec4(moonCol * disk * 1.25 + haloCol * halo * 0.22, disk * 0.92 + halo * 0.20);
}

vec3 originSky(vec3 d, float fade) {
    vec3 primary = max(AuroraColor.rgb, vec3(0.02));
    vec3 secondary = max(PurpleColor.rgb, vec3(0.02));
    float top = smoothstep(-0.10, 0.95, d.y);
    vec2 dome = d.xz * (2.15 + top * 0.72) + d.y * vec2(0.37, -0.29);
    float drift = stardustTime() * 0.08;
    float n1 = stardustFbm2(dome * vec2(2.4, 1.8) + vec2(drift, -drift * 0.37));
    float n2 = stardustFbm2(dome * vec2(5.2, 3.6) + vec2(-drift * 1.4, drift * 0.82));
    float wave = sin(d.x * 7.8 - d.z * 6.4 + n1 * 5.2 + stardustTime() * 0.55) * 0.5 + 0.5;
    float bandCenter = 0.16 + wave * 0.21 + (n2 - 0.5) * 0.10;
    float bandDist = abs(d.y - bandCenter);
    float curtain = pow(stardustSaturate(sin(d.x * 13.0 - d.z * 9.0 + n2 * 8.0 + stardustTime() * 0.72)), 2.0);
    float az = atan(d.z, d.x);
    float striae = stardustNoise2(vec2(az * 15.0 + n1 * 2.4, stardustTime() * 0.05));
    striae = 0.42 + 0.58 * smoothstep(0.28, 0.85, striae);
    float mask = smoothstep(-0.035, 0.30, d.y) * (1.0 - smoothstep(0.78, 1.0, d.y)) * fade;
    float body = (1.0 - smoothstep(0.0, 0.17, bandDist)) * (0.32 + curtain * 0.68) * striae * mask;
    float glow = (1.0 - smoothstep(0.10, 0.48, bandDist)) * mask;
    float veil = (1.0 - smoothstep(0.0, 0.46, abs(d.y - bandCenter - 0.20))) * curtain * striae * mask * 0.5;
    float nebulaWash = smoothstep(0.50, 0.86, n1 * 0.64 + n2 * 0.36) * fade;
    vec3 base = mix(primary * 0.10, mix(primary, secondary, 0.35) * 0.38, top);
    vec3 auroraCol = mix(primary, secondary, stardustSaturate((d.y - bandCenter) * 2.6 + 0.35));
    float moonMask;
    vec4 moon = stardustMoonLayer(d, primary, secondary, moonMask);
    float starVis = 1.0 - moonMask;
    float star = stardustStarLayer(d, 132.0, 0.972, smoothstep(0.02, 0.40, d.y)) * starVis;
    float deepStar = stardustStarLayer(normalize(d + vec3(0.017, -0.008, 0.023)), 78.0, 0.958, smoothstep(0.06, 0.62, d.y)) * 0.55 * starVis;
    vec3 color = base + primary * nebulaWash * 1.05 + auroraCol * body * 1.85 + (primary + secondary) * glow * 0.42;
    color += mix(auroraCol, secondary, 0.5) * veil * 1.15;
    color += stardustStellarColor(d) * star * 2.4 + mix(stardustStellarColor(d), secondary, 0.38) * deepStar;
    color += starField(d, stardustTime(), 0.9935, 4.4) * stardustStarsOn() * starVis;
    color += moon.rgb * fade;
    return stardustSaturate3(color);
}

vec3 quantumNebulaSky(vec3 d, float fade) {
    vec3 projectionDir = stardustProjectionDir();
    vec3 p = normalize(mix(d, normalize(projectionDir + d * 0.35), 0.28)) * 2.55 + stardustCameraDir() * 0.22;
    vec3 t = vec3(stardustTime() * 0.018, -stardustTime() * 0.012, stardustTime() * 0.015);
    vec3 advectA = vec3(stardustFbm3(p + t), stardustFbm3(p.yzx - t * 1.31), stardustFbm3(p.zxy + t * 0.83));
    vec3 advectB = advectA.yzx * 1.28 - advectA.zxy * 0.55 + vec3(advectA.x * advectA.y, advectA.y * advectA.z, advectA.z * advectA.x) * 1.15 - t * 0.74;
    float gas = stardustFbm3(p * 1.28 + advectA * 2.15 + advectB * 0.72);
    float filament = stardustFbm3(p * 3.30 + advectB * 2.40 - t * 1.6);
    float dust = stardustFbm3(p * 2.15 - advectA * 1.45 + vec3(3.7, -8.1, 5.4));
    float lane = smoothstep(0.50, 0.82, dust) * (1.0 - smoothstep(0.83, 1.0, gas));
    float ha = smoothstep(0.40, 0.88, gas * 0.72 + filament * 0.28);
    float oiii = smoothstep(0.48, 0.92, filament * 0.68 + gas * 0.32);
    float cluster = stardustStarLayer(d, 96.0, 0.960, smoothstep(-0.02, 0.96, d.y));
    float micro = stardustStarLayer(normalize(d + advectA * 0.018), 210.0, 0.982, smoothstep(0.02, 0.96, d.y));
    vec3 tint = max(NebulaColor.rgb, vec3(0.02));
    vec3 tint2 = max(PurpleColor.rgb, vec3(0.02));
    vec3 hAlpha = tint;
    vec3 oxygen = tint2;
    float elev = smoothstep(-0.10, 0.92, d.y);
    vec3 base = mix(tint * 0.08, mix(tint, tint2, 0.4) * 0.28, elev);
    vec3 emission = hAlpha * ha * 1.55 + oxygen * oiii * 1.25 + mix(tint, tint2, gas) * smoothstep(0.45, 0.96, gas) * 1.05;
    vec3 color = base + emission * (1.0 - lane * 0.55);
    color += stardustStellarColor(d) * cluster * (1.80 - lane * 0.70) + mix(oxygen, vec3(1.0), 0.52) * micro * (0.80 - lane * 0.28);
    color += starField(d, stardustTime(), 0.9935, 4.4) * stardustStarsOn();
    return stardustSaturate3(color);
}

void main() {
    vec2 uv = TexCoord;
    float depth = texture(DepthSampler, uv).r;
    float skyMask = smoothstep(Params.y, Params.z, 1.0 - depth);
    if (skyMask <= 0.001) {
        discard;
    }

    float intensity = max(ScreenModeIntensity.w, 0.0);
    float speed = Params.x;

    vec3 ray = worldRay(uv);
    float time = CameraTime.w * speed;
    int modeIndex = int(ScreenModeIntensity.z + 0.5);
    vec3 effect;
    if (modeIndex == 1) {
        effect = galaxy(ray, PurpleColor.rgb, AuroraColor.rgb, time);
    } else if (modeIndex == 2) {
        effect = purpleFog(ray, PurpleColor.rgb, time);
    } else if (modeIndex == 3) {
        effect = aurora(ray, PurpleColor.rgb, AuroraColor.rgb, time);
    } else if (modeIndex == 5) {
        effect = plasma(ray, PurpleColor.rgb, AuroraColor.rgb, time);
    } else if (modeIndex == 6) {
        effect = milkyWay(ray, PurpleColor.rgb, AuroraColor.rgb, time);
    } else if (modeIndex == 7 || modeIndex == 8) {
        float fade = smoothstep(-0.20, 0.08, ray.y) * (1.0 - smoothstep(0.992, 1.0, ray.y) * 0.08);
        vec3 sky = modeIndex == 7 ? originSky(ray, fade) : quantumNebulaSky(ray, fade);
        float rainDim = mix(1.0, 0.58, clamp(ExtraParams.z, 0.0, 1.0));
        effect = stardustSaturate3(sky * rainDim);
    } else {
        effect = nebula(ray, PurpleColor.rgb, AuroraColor.rgb, time);
    }

    float strength = max(intensity, 0.0);
    fragColor = vec4(effect * strength, skyMask);
}
