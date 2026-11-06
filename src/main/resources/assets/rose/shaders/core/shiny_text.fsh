#version 330

layout(std140) uniform Shine {
    vec4 ShineBounds;
    vec4 ShineMotion;
    vec4 ShineColor;
};

in vec2 TexCoord;
in vec2 FragCoord;
in vec4 InputColor;
in float Range;
in float Thickness;
in float Smoothness;
in vec4 Scissor;
in float UseScissor;

uniform sampler2D Sampler0;

out vec4 fragColor;

float median(vec3 color) {
    return max(min(color.r, color.g), min(max(color.r, color.g), color.b));
}

void main() {
    if (UseScissor == 1.0 && (FragCoord.x < Scissor.x || FragCoord.y < Scissor.y
            || FragCoord.x > Scissor.z || FragCoord.y > Scissor.w)) discard;
    float dist = median(texture(Sampler0, TexCoord).rgb) - 0.5 + Thickness;
    vec2 h = vec2(dFdx(TexCoord.x), dFdy(TexCoord.y)) * textureSize(Sampler0, 0);
    float pixels = max(Range * inversesqrt(max(dot(h, h), 0.000001)), 1.0);
    float alpha = smoothstep(-Smoothness, Smoothness, dist * pixels) * InputColor.a;
    if (alpha <= 0.001) discard;
    vec2 local = (FragCoord - ShineBounds.xy) / ShineBounds.zw;
    float backgroundPosition = 1.5 - 2.0 * ShineMotion.x;
    vec2 imageSize = vec2(2.0, ShineMotion.y);
    vec2 imagePoint = vec2(local.x + backgroundPosition, local.y * ShineMotion.y);
    vec2 direction = ShineMotion.zw;
    float gradientPosition = 0.5 + dot(imagePoint - imageSize * 0.5, direction)
            / dot(imageSize, abs(direction));
    float shine = clamp(1.0 - abs(gradientPosition - 0.5) / 0.15, 0.0, 1.0);
    vec3 color = mix(InputColor.rgb, ShineColor.rgb, shine);
    fragColor = vec4(color, alpha);
}
