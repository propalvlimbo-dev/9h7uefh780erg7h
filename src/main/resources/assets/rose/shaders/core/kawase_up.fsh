#version 330

uniform sampler2D DiffuseSampler;
layout(std140) uniform RoseParams {
    vec2 HalfPixel;
    float Offset;
};

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec2 o = HalfPixel * Offset;
    vec4 sum = texture(DiffuseSampler, texCoord + vec2(-o.x * 2.0, 0.0));
    sum += texture(DiffuseSampler, texCoord + vec2(-o.x, o.y)) * 2.0;
    sum += texture(DiffuseSampler, texCoord + vec2(0.0, o.y * 2.0));
    sum += texture(DiffuseSampler, texCoord + vec2(o.x, o.y)) * 2.0;
    sum += texture(DiffuseSampler, texCoord + vec2(o.x * 2.0, 0.0));
    sum += texture(DiffuseSampler, texCoord + vec2(o.x, -o.y)) * 2.0;
    sum += texture(DiffuseSampler, texCoord + vec2(0.0, -o.y * 2.0));
    sum += texture(DiffuseSampler, texCoord + vec2(-o.x, -o.y)) * 2.0;
    fragColor = vec4(sum.rgb / 12.0, 1.0);
}
