#version 150

layout(std140) uniform LightningComposite {
    vec4 Params;
};

uniform sampler2D Sampler0;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 blurred = texture(Sampler0, texCoord);
    float alpha = min(blurred.a * 1.25, 1.0);
    if (alpha <= 0.001) {
        discard;
    }
    fragColor = vec4(blurred.rgb * (1.0 + Params.x * 3.0), alpha);
}
