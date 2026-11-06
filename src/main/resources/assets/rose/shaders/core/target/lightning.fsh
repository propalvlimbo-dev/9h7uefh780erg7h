#version 150

in vec4 vertexColor;
in float edgePx;
in float halfPx;

out vec4 fragColor;

const float CORE_SIGMA = 1.7;

void main() {
    fragColor = vec4(0.0);

    float d = abs(edgePx);
    float coverage = clamp(halfPx - d + 0.5, 0.0, 1.0);
    if (coverage <= 0.0) {
        discard;
    }

    float coreWeight = smoothstep(10.0, 5.0, halfPx);
    float core = exp(-(d * d) / (2.0 * CORE_SIGMA * CORE_SIGMA)) * coreWeight;
    float halo = pow(clamp(1.0 - d / max(halfPx, 0.001), 0.0, 1.0), 2.2);

    vec3 hotCore = mix(vertexColor.rgb * 1.7, vec3(1.0, 0.55, 0.42), 0.16);
    vec3 color = mix(vertexColor.rgb, clamp(hotCore, 0.0, 1.0), core);
    float alpha = clamp((core * 1.4 + halo * 0.55) * coverage * vertexColor.a, 0.0, 1.0);
    if (alpha <= 0.003) {
        discard;
    }

    fragColor = vec4(color, alpha);
}
