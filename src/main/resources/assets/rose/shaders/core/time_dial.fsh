#version 330

#moj_import <rose:defaults.glsl>

in vec2 FragCoord;
in vec2 Size;
in float Phase;
in float Hover;
in float Alpha;
in vec4 Scissor;
in float UseScissor;

out vec4 fragColor;

float roundedBox(vec2 p, vec2 halfSize, float radius) {
    vec2 q = abs(p) - halfSize + radius;
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - radius;
}

float circleMask(vec2 p, float radius, float softness) {
    return 1.0 - smoothstep(radius - softness, radius + softness, length(p));
}

mat2 rotation(float angle) {
    float sine = sin(angle);
    float cosine = cos(angle);
    return mat2(cosine, -sine, sine, cosine);
}

void main() {
    if (UseScissor == 1.0 && !insideScissor(FragCoord, Scissor)) discard;

    vec2 uv = FragCoord.yx;
    float aspect = Size.x / max(Size.y, 1.0);
    vec2 p = (uv - 0.5) * vec2(aspect, 1.0);
    float edge = 0.45 / max(Size.y, 1.0);
    float capsuleDistance = roundedBox(p, vec2(aspect * 0.5 - edge, 0.5 - edge), 0.48);
    float capsule = 1.0 - smoothstep(-edge, edge, capsuleDistance);
    if (capsule <= 0.001) discard;

    float capsuleBorder = 1.0 - smoothstep(edge * 0.25, edge * 1.75, abs(capsuleDistance));
    vec3 color = vec3(1.0);
    float outputAlpha = capsuleBorder * (15.0 / 255.0);

    vec2 globeCenter = vec2(0.69, 0.04);
    vec2 globe = (uv - globeCenter) * vec2(aspect, 1.0);
    float globeRadius = 0.73 + Hover * 0.025;
    float globeMask = circleMask(globe, globeRadius, edge) * capsule;

    float angle = Phase * 6.28318530718;
    vec2 rotatedGlobe = rotation(-angle) * globe;
    float nightSide = smoothstep(-0.012, 0.012, rotatedGlobe.x);
    vec3 daylight = vec3(0.28, 0.73, 0.83);
    vec3 night = vec3(0.055, 0.08, 0.26);
    vec3 globeColor = mix(daylight, night, nightSide);

    vec2 sunPosition = rotation(angle) * vec2(-0.27, 0.0);
    vec2 moonPosition = -sunPosition;
    float sunCore = circleMask(globe - sunPosition, 0.235, edge * 0.8);
    float sunHalo = circleMask(globe - sunPosition, 0.34, edge * 1.2) * (1.0 - sunCore);
    globeColor = mix(globeColor, vec3(1.0, 0.69, 0.10), sunHalo * 0.58);
    globeColor = mix(globeColor, vec3(1.0, 0.82, 0.20), sunCore);

    float moon = circleMask(globe - moonPosition, 0.235, edge * 0.8);
    float moonCut = circleMask(globe - moonPosition - vec2(-0.085, -0.055), 0.205, edge * 0.8);
    float crescent = moon * (1.0 - moonCut);
    globeColor = mix(globeColor, vec3(0.91, 0.95, 1.0), crescent);

    vec2 starOnePosition = rotation(angle) * vec2(0.15, -0.42);
    vec2 starTwoPosition = rotation(angle) * vec2(0.42, 0.22);
    float stars = circleMask(globe - starOnePosition, 0.045, edge)
            + circleMask(globe - starTwoPosition, 0.032, edge);
    globeColor = mix(globeColor, vec3(1.0, 0.84, 0.22), clamp(stars * nightSide, 0.0, 1.0));

    float globeBorder = circleMask(globe, globeRadius + 0.025, edge)
            - circleMask(globe, globeRadius - 0.025, edge);
    float globeGlow = (1.0 - smoothstep(globeRadius, globeRadius + 0.18, length(globe)))
            * (1.0 - globeMask) * capsule;
    float glowAlpha = globeGlow * (0.34 + Hover * 0.28);
    color = mix(color, vec3(0.18, 0.62, 1.0), glowAlpha);
    outputAlpha = max(outputAlpha, glowAlpha);
    color = mix(color, globeColor, globeMask);
    outputAlpha = max(outputAlpha, globeMask);
    color = mix(color, vec3(0.48, 0.78, 1.0), clamp(globeBorder, 0.0, 1.0) * 0.68);

    vec2 leftIcon = (uv - vec2(0.155, 0.5)) * vec2(aspect, 1.0);
    float leftMoon = circleMask(leftIcon, 0.17, edge);
    float leftCut = circleMask(leftIcon - vec2(0.075, -0.055), 0.15, edge);
    float leftCrescent = leftMoon * (1.0 - leftCut) * capsule;
    color = mix(color, vec3(0.78, 0.90, 1.0), leftCrescent);
    outputAlpha = max(outputAlpha, leftCrescent);
    float smallStar = circleMask(leftIcon - vec2(0.19, -0.18), 0.033, edge);
    smallStar *= capsule;
    color = mix(color, vec3(0.88, 0.95, 1.0), smallStar);
    outputAlpha = max(outputAlpha, smallStar);

    fragColor = vec4(clamp(color, 0.0, 1.0), outputAlpha * Alpha);
}
