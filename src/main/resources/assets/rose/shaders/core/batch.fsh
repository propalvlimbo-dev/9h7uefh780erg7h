#version 330

#moj_import <rose:defaults.glsl>

in float Instruction;
in float Thickness;
in float Range;
flat in int Type;
in vec4 Colors;
in vec4 Radius;
in vec2 FragCoord;
in vec2 TexCoord;
in vec2 Size;

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;
uniform sampler2D Sampler3;
uniform sampler2D Sampler4;
uniform sampler2D Sampler5;
uniform sampler2D Sampler6;
uniform sampler2D Sampler10;
uniform sampler2D Sampler11;
uniform sampler2D Sampler12;
uniform sampler2D Sampler13;

out vec4 fragColor;

const float DPI = 6.28318530718;
const float STEP = DPI / 16.0;

#define NOISE 0.5 / 255.0

float median(vec3 color) {
    return max(min(color.r, color.g), min(max(color.r, color.g), color.b));
}

vec4 getSample(int type, vec2 uv) {
    switch (type) {
        case 0: return texture(Sampler0, uv);
        case 1: return texture(Sampler1, uv);
        case 2: return texture(Sampler2, uv);
        case 3: return texture(Sampler3, uv);
        case 4: return texture(Sampler4, uv);
        case 5: return texture(Sampler5, uv);
        case 6: return texture(Sampler6, uv);
        default: return texture(Sampler0, uv);
    }
}

ivec2 getSampleSize(int type) {
    switch (type) {
        case 0: return textureSize(Sampler0, 0);
        case 1: return textureSize(Sampler1, 0);
        case 2: return textureSize(Sampler2, 0);
        case 3: return textureSize(Sampler3, 0);
        case 4: return textureSize(Sampler4, 0);
        case 5: return textureSize(Sampler5, 0);
        case 6: return textureSize(Sampler6, 0);
        default: return textureSize(Sampler0, 0);
    }
}

void main() {
    if (Instruction >= 0 && Instruction <= 1) {
        float dist = median(getSample(Type, TexCoord).rgb) - 0.5;
        vec2 h = vec2(dFdx(TexCoord.x), dFdy(TexCoord.y)) * getSampleSize(Type);
        float pixels = max(Range * inversesqrt(h.x * h.x + h.y * h.y), 1.0);
        float alpha = smoothstep(-0.5, 0.5, dist * pixels);
        vec4 color = vec4(Colors.rgb, Colors.a * alpha);

        fragColor = color;
    } else if (Instruction >= 2 && Instruction <= 3) {
        float alpha = ralpha(Size, FragCoord, Radius, 1.5);

        vec4 color = Colors;
        color += mix(NOISE, -NOISE, fract(sin(dot(FragCoord.xy, vec2(12.9898, 78.233))) * 43758.5453));

        vec4 colors = vec4(1.0, 1.0, 1.0, alpha) * texture(Sampler11, TexCoord) * color;

        if (colors.a == 0.0) {
            discard;
        }

        fragColor = colors;
    } else if (Instruction >= 4 && Instruction <= 5) {
        float alpha = ralpha(Size, FragCoord, Radius, 1.5);
        vec4 sampled;
        if (TexCoord.x >= 20.0) {
            int samplerIndex = int(floor((TexCoord.x - 20.0) * 0.5));
            float base = 20.0 + float(samplerIndex) * 2.0;
            sampled = getSample(samplerIndex, vec2(TexCoord.x - base, TexCoord.y - 4.0));
        } else {
            sampled = texture(Sampler12, TexCoord - vec2(4.0));
        }
        vec4 colors = vec4(1.0, 1.0, 1.0, alpha) * sampled * Colors;

        if (colors.a == 0.0) {
            discard;
        }

        fragColor = colors;
    } else if (Instruction >= 30.0 && Instruction <= 31.0) {
        float alpha = ralpha(Size, FragCoord, Radius, 1.5);

        vec2 uv = TexCoord - vec2(30.0);
        vec4 sampled = texture(Sampler13, uv);

        vec4 color = sampled * Colors;
        color.a *= alpha;

        fragColor = color;
    } else if (Instruction == 7) {
        float alpha = ralpha(Size, FragCoord, Radius, 1.5);

        vec4 color = Colors;
        color += mix(NOISE, -NOISE, fract(sin(dot(FragCoord.xy, vec2(12.9898, 78.233))) * 43758.5453));

        fragColor = vec4(color.rgb, color.a * alpha);
    } else if (Instruction == 8) {
        vec2 center = Size * 0.5;
        float dist = rdist(center - (FragCoord.xy * Size), center - 1.0, Radius);
        float alpha = smoothstep(1.0 - Thickness - 3.0,
                                 1.0 - Thickness - 1.5, dist);
        alpha *= 1.0 - smoothstep(1.0 - 1.5, 1.0, dist);

        vec4 color = vec4(Colors.rgb, Colors.a * alpha);

        fragColor = color;
    } else if (Instruction == 9) {
        vec2 multiplier = Thickness / textureSize(Sampler10, 0);

        vec3 average = texture(Sampler10, TexCoord).rgb;
        for (float d = 0.0; d < DPI; d += STEP) {
            for (float i = 0.2; i <= 1.0; i += 0.2) {
                average += texture(Sampler10, TexCoord + vec2(cos(d), sin(d)) * multiplier * i).rgb;
            }
        }
        average /= 80.0;

        vec4 color = vec4(average, 1.0) * Colors;

        color.a *= ralpha(Size, FragCoord, Radius, 1.5);

        fragColor = color;
    } else if (Instruction == 11) {
        float alpha = Colors.a * ralpha(Size, FragCoord, Radius, 1.5);
        if (alpha <= 0.0) discard;

        vec2 stepSize = vec2(Thickness * 0.25) / vec2(textureSize(Sampler10, 0));
        vec3 average = texture(Sampler10, TexCoord).rgb * 0.125;
        average += (texture(Sampler10, TexCoord + stepSize * vec2(-1.0, -1.0)).rgb
                  + texture(Sampler10, TexCoord + stepSize * vec2(1.0, -1.0)).rgb
                  + texture(Sampler10, TexCoord + stepSize * vec2(-1.0, 1.0)).rgb
                  + texture(Sampler10, TexCoord + stepSize * vec2(1.0, 1.0)).rgb) * 0.125;
        average += (texture(Sampler10, TexCoord + stepSize * vec2(-2.0, 0.0)).rgb
                  + texture(Sampler10, TexCoord + stepSize * vec2(2.0, 0.0)).rgb
                  + texture(Sampler10, TexCoord + stepSize * vec2(0.0, -2.0)).rgb
                  + texture(Sampler10, TexCoord + stepSize * vec2(0.0, 2.0)).rgb) * 0.0625;
        average += (texture(Sampler10, TexCoord + stepSize * vec2(-2.0, -2.0)).rgb
                  + texture(Sampler10, TexCoord + stepSize * vec2(2.0, -2.0)).rgb
                  + texture(Sampler10, TexCoord + stepSize * vec2(-2.0, 2.0)).rgb
                  + texture(Sampler10, TexCoord + stepSize * vec2(2.0, 2.0)).rgb) * 0.03125;

        fragColor = vec4(average * Colors.rgb, alpha);
    } else if (Instruction == 12) {
        vec2 center = Size * 0.5;
        float distance = rdist(center - FragCoord * Size, center - vec2(Thickness), Radius);
        float falloff = max(distance, 0.0) / max(Thickness, 0.001);
        float alpha = exp(-4.5 * falloff * falloff) * (1.0 - smoothstep(0.8, 1.0, falloff));
        fragColor = vec4(Colors.rgb, Colors.a * alpha * 0.35);
    } else if (Instruction == 10) {
        vec2 p = (FragCoord - 0.5) * Size;
        float stroke = max(Radius.x, 0.75);
        float radius = min(Size.x, Size.y) * 0.5 - stroke * 0.5 - 0.35;
        float radialDistance = abs(length(p) - radius);
        float radialAa = max(fwidth(radialDistance), 0.35);
        float ring = 1.0 - smoothstep(stroke * 0.5 - radialAa,
                                     stroke * 0.5 + radialAa, radialDistance);

        float progress = clamp(Thickness, 0.0, 1.0);
        float angle = atan(p.x, -p.y);
        float turn = mod(angle + DPI, DPI) / DPI;
        float angularAa = max(fwidth(turn), 0.0025);
        float body = progress >= 0.999
                ? ring
                : ring * (1.0 - smoothstep(progress - angularAa,
                                           progress + angularAa, turn));

        float alpha = body;
        if (progress > 0.001 && progress < 0.999) {
            float endAngle = progress * DPI;
            vec2 startPoint = vec2(0.0, -radius);
            vec2 endPoint = vec2(sin(endAngle), -cos(endAngle)) * radius;
            float capRadius = stroke * 0.5;
            float startCap = 1.0 - smoothstep(capRadius - radialAa,
                                             capRadius + radialAa, length(p - startPoint));
            float endCap = 1.0 - smoothstep(capRadius - radialAa,
                                           capRadius + radialAa, length(p - endPoint));
            alpha = max(alpha, max(startCap, endCap));
        }

        fragColor = vec4(Colors.rgb, Colors.a * alpha);
    }
}
