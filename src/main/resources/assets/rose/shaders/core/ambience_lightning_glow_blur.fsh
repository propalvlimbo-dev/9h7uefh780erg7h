#version 330

layout(std140) uniform LightningGlowBlur {
    vec2 TexelSize;
    vec2 Direction;
    vec2 Radii;
};

uniform sampler2D InSampler;

in vec2 TexCoord;
out vec4 fragColor;

const float WEIGHTS[5] = float[5](
    0.9231163464,
    0.7261490371,
    0.4867522569,
    0.2780373005,
    0.1353352832
);
const float WEIGHT_SUM = 6.0987804482;

void main() {
    vec2 center = texture(InSampler, TexCoord).rg;
    float wide = center.r;
    float tight = center.g;

    for (int index = 1; index <= 5; index++) {
        float stepValue = float(index) * 0.2;
        float weight = WEIGHTS[index - 1];
        vec2 wideOffset = Direction * TexelSize * Radii.x * stepValue;
        vec2 tightOffset = Direction * TexelSize * Radii.y * stepValue;
        wide += (texture(InSampler, TexCoord + wideOffset).r
               + texture(InSampler, TexCoord - wideOffset).r) * weight;
        tight += (texture(InSampler, TexCoord + tightOffset).g
                + texture(InSampler, TexCoord - tightOffset).g) * weight;
    }

    fragColor = vec4(wide / WEIGHT_SUM, tight / WEIGHT_SUM, 0.0, 1.0);
}
