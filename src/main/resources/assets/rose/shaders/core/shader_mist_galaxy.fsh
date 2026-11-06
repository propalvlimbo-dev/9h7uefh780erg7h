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

#define iterations 17
#define formuparam 0.53
#define volsteps 20
#define stepsize 0.1
#define zoom 0.600
#define tile 0.850
#define speed 0.010
#define brightness 0.0015
#define darkmatter 0.300
#define distfading 0.730
#define saturation 1.0

#define iTime (uTime * uSpeed)
#define iResolution vec3(uResolution, 0.0)

float hash(float n) {
    return fract(sin(n) * 43758.5453);
}

float perlin(vec3 p) {
    vec3 pi = floor(p);
    vec3 pf = p - pi;
    pf = smoothstep(0.0, 1.0, pf);

    float n00 = hash(pi.x + pi.y * 73.0 + pi.z * 317.0);
    float n10 = hash(pi.x + 1.0 + pi.y * 73.0 + pi.z * 317.0);
    float n01 = hash(pi.x + (pi.y + 1.0) * 73.0 + pi.z * 317.0);
    float n11 = hash(pi.x + 1.0 + (pi.y + 1.0) * 73.0 + pi.z * 317.0);

    float nx0 = mix(n00, n10, pf.x);
    float nx1 = mix(n01, n11, pf.x);
    return mix(nx0, nx1, pf.y);
}

mat3 rotX(float a) {
    float c = cos(a), s = sin(a);
    return mat3(1.0, 0.0, 0.0, 0.0, c, s, 0.0, -s, c);
}

mat3 rotY(float a) {
    float c = cos(a), s = sin(a);
    return mat3(c, 0.0, s, 0.0, 1.0, 0.0, -s, 0.0, c);
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

    vec3 dir = rayW;
    float time = iTime * speed + 0.25;

    float a1 = 0.5 + time * 0.1;
    float a2 = 0.8 + time * 0.15;
    mat2 rot1 = mat2(cos(a1), sin(a1), -sin(a1), cos(a1));
    mat2 rot2 = mat2(cos(a2), sin(a2), -sin(a2), cos(a2));
    dir.xz *= rot1;
    dir.xy *= rot2;

    vec3 from = vec3(1.0, 0.5, 0.5);
    from += vec3(time * 2.0, time, -2.0);
    from.xz *= rot1;
    from.xy *= rot2;

    float s = 0.1, fade = 1.0;
    vec3 v = vec3(0.0);
    for (int r = 0; r < volsteps; r++) {
        vec3 p = from + s * dir * 0.5;
        p = abs(vec3(tile) - mod(p, vec3(tile * 2.0)));
        float pa, a = pa = 0.0;
        for (int i = 0; i < iterations; i++) {
            p = abs(p) / dot(p, p) - formuparam;
            a += abs(length(p) - pa);
            pa = length(p);
        }
        float dm = max(0.0, darkmatter - a * a * 0.001);
        a *= a * a;
        if (r > 6) fade *= 1.0 - dm;
        v += fade;
        v += vec3(s, s * s, s * s * s * s) * a * brightness * fade;
        fade *= distfading;
        s += stepsize;
    }
    v = mix(vec3(length(v)), v, saturation);

    vec3 cosmosColor = v * 0.01;
    cosmosColor = clamp(cosmosColor * 1.5, 0.0, 1.0);

    fragColor = vec4(cosmosColor * max(uAlpha, 0.0), skyMask);
}
