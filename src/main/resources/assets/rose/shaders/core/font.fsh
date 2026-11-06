#version 330

#moj_import <rose:defaults.glsl>

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

bool insideFontScissor(vec2 p, vec4 r) {
    return p.x >= r.x &&
           p.y >= r.y &&
           p.x <= r.z &&
           p.y <= r.w;
}

void main() {
    if (UseScissor == 1.0 && !insideFontScissor(FragCoord, Scissor)) {
        discard;
    }

    float dist = median(texture(Sampler0, TexCoord).rgb) - 0.5 + Thickness;
    vec2 h = vec2(dFdx(TexCoord.x), dFdy(TexCoord.y)) * textureSize(Sampler0, 0);
    float pixels = max(Range * inversesqrt(h.x * h.x + h.y * h.y), 1.0);
    float alpha = smoothstep(-Smoothness, Smoothness, dist * pixels);
    vec4 color = vec4(InputColor.rgb, InputColor.a * alpha);

    fragColor = color;
}