#version 330
uniform sampler2D SceneSampler;
layout(std140) uniform BlastScreen {
    vec4 ScreenOrigin;
    vec4 Wave;
};
out vec4 fragColor;
void main() {
    vec2 uv = gl_FragCoord.xy / ScreenOrigin.xy;
    vec2 delta = gl_FragCoord.xy - ScreenOrigin.zw;
    float dist = length(delta);
    vec2 dir = dist > 0.001 ? delta / dist : vec2(0.0);
    float ring = exp(-pow((dist - Wave.x) / max(Wave.y, 1.0), 2.0));
    vec2 offset = dir * ring * Wave.z / ScreenOrigin.xy;
    vec3 col = texture(SceneSampler, uv + offset).rgb;
    col += ring * 0.08 * vec3(1.0, 0.96, 0.88);
    fragColor = vec4(col, 1.0);
}
