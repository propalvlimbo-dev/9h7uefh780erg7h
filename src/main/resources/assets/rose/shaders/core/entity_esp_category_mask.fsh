#version 330

uniform sampler2D InSampler;

layout(std140) uniform CategoryMask {
    vec4 Color;
    vec4 Category;
};

in vec2 TexCoord;
out vec4 fragColor;

void main() {
    vec4 source = texture(InSampler, TexCoord);
    ivec3 tag = ivec3(round(source.rgb * 255.0));
    bool selected = tag.r == int(Category.x + 0.5) && tag.g == 254;
    fragColor = selected ? vec4(Color.rgb, source.a) : vec4(0.0);
}
