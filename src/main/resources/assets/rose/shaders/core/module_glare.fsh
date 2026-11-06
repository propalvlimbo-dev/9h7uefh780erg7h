#version 330

#moj_import <rose:defaults.glsl>

in vec2 FragCoord;
in vec2 Size;
in vec4 Radius;
in float Progress;
in float Opacity;
in float GlareSize;
in vec4 Scissor;
in float UseScissor;
out vec4 fragColor;

void main() {
    if (UseScissor == 1.0 && !insideScissor(FragCoord, Scissor)) discard;
    float rounded = ralpha(Size, FragCoord, Radius, 1.5);
    if (rounded <= 0.001) discard;
    float diagonal = (FragCoord.x + 1.0 - FragCoord.y) * 0.5;
    float size = max(GlareSize, 0.1);
    float center = mix(1.0, 0.0, clamp(Progress, 0.0, 1.0));
    float gradientPosition = (diagonal - center) / size + 0.70;
    float leadingEdge = smoothstep(0.60, 0.70, gradientPosition);
    float trailingEdge = 1.0 - smoothstep(0.70, 0.85, gradientPosition);
    float glare = leadingEdge * trailingEdge;
    float core = 1.0 - smoothstep(0.0, 0.018, abs(gradientPosition - 0.70));
 float alpha = clamp(glare + core * 0.35, 0.0, 1.0) * 0.45 * Opacity * rounded;
    if (alpha <= 0.002) discard;
    vec3 color = mix(vec3(0.76, 0.84, 1.0), vec3(1.0), clamp(core * 0.88 + glare * 0.42, 0.0, 1.0));
    fragColor = vec4(color, alpha);
}
