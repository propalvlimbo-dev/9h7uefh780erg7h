#version 330

in vec2 BoltUv;
in vec4 BoltColor;

out vec4 fragColor;

void main() {
    float distanceFromCore = abs(BoltUv.x);
    float solidCore = 1.0 - smoothstep(0.08, 0.34, distanceFromCore);
    float innerGlow = exp(-distanceFromCore * distanceFromCore * 12.0) * 0.9;
    float outerGlow = exp(-distanceFromCore * distanceFromCore * 3.2) * 0.42;
    float alpha = max(solidCore, max(innerGlow, outerGlow)) * BoltColor.a;
    if (alpha <= 0.00001) discard;

    fragColor = vec4(BoltColor.rgb, alpha);
}
