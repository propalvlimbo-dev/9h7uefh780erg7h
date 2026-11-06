#version 330

uniform sampler2D Sampler0;

layout(std140) uniform PrismaticChams {
    vec4 u_AccentTop;
    vec4 u_AccentBottom;
    vec4 u_CameraAndTime;
    vec4 u_Params;
    vec4 u_State;
    vec4 u_ResolutionInfo;
    vec4 u_EntityScreen;
};

#define u_CameraPos u_CameraAndTime.xyz
#define u_Time u_CameraAndTime.w
#define u_Intensity u_Params.x
#define u_Opacity u_Params.y
#define u_Refraction u_Params.z
#define u_Mode int(u_Params.w + 0.5)
#define u_Animation u_State.x
#define u_Pass u_State.y
#define u_Seed u_State.z
#define u_DepthMode u_State.w
#define u_Resolution u_ResolutionInfo.xy
#define u_Texel u_ResolutionInfo.zw

in vec3 v_Normal;
in vec3 v_ViewDir;
in vec3 v_ViewPos;
in vec3 v_Position;
in vec4 v_Color;
in vec2 v_Uv;
in vec2 v_ScreenPos;
in vec2 v_EntityScreenPos;

out vec4 fragColor;

const float PI2 = 6.28318530718;

float saturate(float value) {
    return clamp(value, 0.0, 1.0);
}

vec2 safeUv(vec2 uv) {
    return clamp(uv, vec2(0.0015), vec2(0.9985));
}

vec3 toneMap(vec3 color) {
    return color / (vec3(1.0) + color * 0.58);
}

vec3 palette(float t) {
    vec3 a = vec3(0.54, 0.51, 0.58);
    vec3 b = vec3(0.48, 0.43, 0.42);
    vec3 c = vec3(0.98, 1.03, 0.92);
    vec3 d = vec3(0.02, 0.22, 0.38);
    return clamp(a + b * cos(PI2 * (c * t + d)), 0.0, 1.0);
}

vec3 faceCamera(vec3 normal, vec3 viewDir) {
    float lengthSq = dot(normal, normal);
    if (lengthSq < 1.0e-8) {
        return viewDir;
    }
    vec3 unit = normal * inversesqrt(lengthSq);
    return dot(unit, viewDir) < 0.0 ? -unit : unit;
}

vec3 screenChromatic(vec2 uv, vec2 offset) {
    float r = texture(Sampler0, safeUv(uv + offset * 1.18)).r;
    float g = texture(Sampler0, safeUv(uv + offset * 0.82)).g;
    float b = texture(Sampler0, safeUv(uv + offset * 0.48)).b;
    return vec3(r, g, b);
}

vec4 prismaticCrystal(vec2 screenUv, vec3 normal, vec3 viewDir, vec3 reflectDir, float fresnel, float pulse) {
    vec2 distortion = normal.xy * 0.05 * clamp(u_Refraction, 0.35, 1.15) * mix(0.42, 1.0, fresnel);
    vec3 refracted = screenChromatic(screenUv, distortion);
    float facetA = floor((dot(normalize(v_Position + normal * 0.35), vec3(0.88, 0.31, 0.37)) * 0.5 + 0.5) * 8.0) / 8.0;
    float facetB = floor((dot(normalize(v_Position.yzx - normal * 0.18), vec3(-0.24, 0.81, 0.54)) * 0.5 + 0.5) * 10.0) / 10.0;
    float facet = saturate(abs(facetA - facetB) * 2.8);
    float sparkle = pow(saturate(sin(dot(reflectDir, vec3(15.7, 7.4, 11.2)) + u_Time * 2.1 + u_Seed * 9.0) * 0.5 + 0.5), 18.0);
    vec3 accent = mix(u_AccentBottom.rgb, u_AccentTop.rgb, saturate(normal.y * 0.5 + 0.5));
    vec3 film = palette(facetA * 0.67 + facetB * 0.41 + fresnel * 0.42 + u_Time * 0.035);
    float rim = pow(fresnel, 1.55);
    vec3 color = refracted * 0.78 + mix(accent, film, 0.66) * (0.22 + facet * 0.28);
    color += accent * rim * 1.15;
    color += film * sparkle * (0.55 + pulse * 0.35);
    float alpha = mix(0.28, 0.78, saturate(rim + facet * 0.22));
    return vec4(color, alpha);
}

vec4 outlineRim(vec3 normal, float fresnel, float pulse) {
    vec3 accent = mix(u_AccentBottom.rgb, u_AccentTop.rgb, saturate(normal.y * 0.5 + 0.5));
    float rimPower = mix(3.2, 1.05, saturate((u_Intensity - 0.35) / 2.65));
    float rim = pow(fresnel, rimPower);
    float inner = pow(fresnel, rimPower + 2.4);
    vec3 color = accent * (rim * 2.15 + inner * 0.7);
    color += vec3(1.0) * pow(fresnel, 7.5) * (0.22 + 0.12 * pulse);
    float alpha = saturate(rim * 1.45 + inner * 0.25);
    return vec4(color, alpha);
}

vec4 gravitationalVoid(vec2 screenUv, vec2 centerUv, vec3 normal, float fresnel, float pulse) {
    float inward = 1.0 - fresnel;
    vec2 warped = screenUv - normal.xy * 0.08 * inward * clamp(u_Refraction, 0.35, 1.15);
    warped = mix(warped, centerUv, 0.34 * inward);
    vec3 scene = texture(Sampler0, safeUv(warped)).rgb;
    float radius = length((screenUv - centerUv) * vec2(u_Resolution.x / max(u_Resolution.y, 1.0), 1.0));
    float disk = exp(-radius * 8.0) * (0.62 + 0.38 * pulse);
    float rim = pow(fresnel, 0.72);
    vec3 neon = mix(u_AccentBottom.rgb, u_AccentTop.rgb, 0.5 + 0.5 * sin(u_Time * 1.35 + u_Seed * PI2));
    vec3 diskColor = mix(neon, vec3(1.0, 0.92, 0.68), 0.28 + 0.24 * pulse);
    vec3 color = scene * 0.055 * inward;
    color += diskColor * disk * 1.65;
    color += neon * rim * 3.4;
    color = mix(vec3(0.0), color, saturate(rim * 1.65 + disk * 0.34));
    float alpha = saturate(0.38 + rim * 0.56 + disk * 0.22);
    return vec4(color, alpha);
}

void main() {
    vec3 viewDir = normalize(-v_ViewPos);
    vec3 geoNormal = faceCamera(cross(dFdx(v_ViewPos), dFdy(v_ViewPos)), viewDir);
    vec3 vertexNormal = faceCamera(v_Normal, viewDir);
    vec3 normal = length(v_Normal) > 0.04
        ? normalize(mix(geoNormal, vertexNormal, 0.35))
        : geoNormal;
    vec3 reflectDir = reflect(-viewDir, normal);
    float ndv = saturate(dot(normal, viewDir));
    float fresnel = pow(1.0 - ndv, 3.0);
    float pulse = 0.5 + 0.5 * sin(u_Time * 2.2 + u_Seed * PI2);
    vec2 screenUv = safeUv(gl_FragCoord.xy / max(u_Resolution, vec2(1.0)));
    vec2 centerUv = safeUv(v_EntityScreenPos / max(u_Resolution, vec2(1.0)));

    vec4 result = u_Mode == 2
        ? outlineRim(normal, fresnel, pulse)
        : (u_Mode == 1
            ? gravitationalVoid(screenUv, centerUv, normal, fresnel, pulse)
            : prismaticCrystal(screenUv, normal, viewDir, reflectDir, fresnel, pulse));

    float animation = smoothstep(0.0, 1.0, clamp(u_Animation, 0.0, 1.0));
    float overshoot = max(u_Animation - 1.0, 0.0);
    float passMask = step(0.5, u_Pass);
    vec3 passTint = mix(u_AccentBottom.rgb, u_AccentTop.rgb, pulse);
    result.rgb = mix(result.rgb, result.rgb * 0.56 + passTint * fresnel * 0.82, passMask);
    result.a *= mix(1.0, 0.46, passMask);
    result.rgb += v_Color.rgb * 0.025;
    result.rgb *= u_Intensity * (1.0 + overshoot * 1.2);
    result.a = saturate(result.a * u_Opacity * animation);
    fragColor = vec4(clamp(toneMap(result.rgb), 0.0, 1.0), result.a);
}
