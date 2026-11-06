#version 330

uniform sampler2D DiffuseSampler;
layout(std140) uniform RoseParams {
    vec2 HalfPixel;
    float Offset;
    float Linearize;
};

in vec2 texCoord;

out vec4 fragColor;

vec4 fetch(vec2 uv) {
    vec4 c = texture(DiffuseSampler, uv);
    return Linearize > 0.5 ? vec4(pow(c.rgb, vec3(2.2)), c.a) : c;
}

void main() {
    vec2 o = HalfPixel * Offset;
    vec4 sum = fetch(texCoord) * 4.0;
    sum += fetch(texCoord - o);
    sum += fetch(texCoord + o);
    sum += fetch(texCoord + vec2(o.x, -o.y));
    sum += fetch(texCoord - vec2(o.x, -o.y));
    fragColor = vec4(sum.rgb / 8.0, 1.0);
}
