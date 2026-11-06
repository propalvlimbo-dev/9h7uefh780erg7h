#version 330

uniform sampler2D Sampler0;

layout(std140) uniform ColorPlusGrade {
    vec2 OutSize;
    vec4 GradeA;
    vec4 GradeB;
    vec4 Lift;
    vec4 GammaRgb;
    vec4 Gain;
    vec4 Effects;
};

in vec2 TexCoord;
out vec4 fragColor;

const vec3 LUMA = vec3(0.2126, 0.7152, 0.0722);

vec3 toLinear(vec3 color) {
    return pow(max(color, vec3(0.0)), vec3(2.2));
}

vec3 toGamma(vec3 color) {
    return pow(max(color, vec3(0.0)), vec3(1.0 / 2.2));
}

vec3 acesFilm(vec3 color) {
    const float a = 2.51;
    const float b = 0.03;
    const float c = 2.43;
    const float d = 0.59;
    const float e = 0.14;
    return clamp((color * (a * color + b)) / (color * (c * color + d) + e), 0.0, 1.0);
}

vec3 applyTemperature(vec3 color, float value) {
    vec3 warm = vec3(1.06, 0.98, 0.86);
    vec3 cool = vec3(0.86, 0.96, 1.08);
    return color * mix(vec3(1.0), mix(cool, warm, step(0.0, value)), abs(value));
}

vec3 applyTint(vec3 color, float value) {
    vec3 magenta = vec3(1.05, 0.94, 1.05);
    vec3 green = vec3(0.94, 1.06, 0.94);
    return color * mix(vec3(1.0), mix(green, magenta, step(0.0, value)), abs(value));
}

vec3 applySaturation(vec3 color, float value) {
    float luma = dot(color, LUMA);
    return mix(vec3(luma), color, 1.0 + value);
}

vec3 applyVibrance(vec3 color, float value) {
    float maximum = max(color.r, max(color.g, color.b));
    float minimum = min(color.r, min(color.g, color.b));
    float saturation = maximum - minimum;
    float luma = dot(color, LUMA);
    float weight = 1.0 - clamp(saturation, 0.0, 1.0);
    return mix(vec3(luma), color, 1.0 + value * weight * 1.4);
}

vec3 applyContrast(vec3 color, float value) {
    return clamp((color - 0.5) * (1.0 + value * 0.85) + 0.5, 0.0, 16.0);
}

vec3 casSharpen(vec2 uv, vec3 center, float amount) {
    vec2 texel = 1.0 / max(OutSize, vec2(1.0));
    vec3 top = texture(Sampler0, uv + vec2(0.0, -texel.y)).rgb;
    vec3 left = texture(Sampler0, uv + vec2(-texel.x, 0.0)).rgb;
    vec3 right = texture(Sampler0, uv + vec2(texel.x, 0.0)).rgb;
    vec3 bottom = texture(Sampler0, uv + vec2(0.0, texel.y)).rgb;
    vec3 minimum = min(min(top, left), min(right, bottom));
    vec3 maximum = max(max(top, left), max(right, bottom));
    vec3 weight = sqrt(clamp(min(minimum, vec3(1.0) - maximum) / max(maximum, vec3(0.0001)), 0.0, 1.0));
    vec3 average = (top + left + right + bottom) * 0.25;
    float maximumChannel = max(center.r, max(center.g, center.b));
    float minimumChannel = min(center.r, min(center.g, center.b));
    float neutralHighlight = smoothstep(0.70, 0.98, dot(center, LUMA))
            * (1.0 - smoothstep(0.045, 0.20, maximumChannel - minimumChannel));
    return center + (center - average) * weight * amount * (1.0 - neutralHighlight * 0.94) * 1.6;
}

void main() {
    vec3 original = texture(Sampler0, TexCoord).rgb;
    vec3 color = original;

    if (Effects.x > 0.001) {
        color = casSharpen(TexCoord, color, Effects.x);
    }

    vec3 linearColor = toLinear(color);
    linearColor *= pow(2.0, GradeA.y);
    linearColor *= vec3(1.0) + Gain.rgb * 0.6;
    linearColor += Lift.rgb * 0.18 * (1.0 - linearColor);
    linearColor = pow(max(linearColor, vec3(0.0)), vec3(1.0) / max(vec3(1.0) + GammaRgb.rgb * 0.6, vec3(0.05)));
    linearColor = applyTemperature(linearColor, GradeB.z * 0.8);
    linearColor = applyTint(linearColor, GradeB.w * 0.6);
    color = toGamma(acesFilm(linearColor));

    color = pow(max(color, vec3(0.0)), vec3(1.0 / max(0.05, 1.0 + GradeB.y * 0.55)));
    color = applySaturation(color, GradeA.w);
    color = applyVibrance(color, GradeB.x);
    color = applyContrast(color, GradeA.z);

    if (Effects.y > 0.001) {
        vec2 centered = TexCoord - 0.5;
        float vignette = 1.0 - dot(centered, centered) * Effects.y * 1.4;
        color *= clamp(vignette, 0.55, 1.0);
    }

    color = clamp(color, 0.0, 1.0);
    fragColor = vec4(mix(original, color, clamp(GradeA.x, 0.0, 1.0)), 1.0);
}
