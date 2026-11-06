#version 330

uniform sampler2D DepthSampler;
uniform sampler2D MaskSampler;

layout(std140) uniform PreviewSurface {
    mat4 InverseTransform;
    vec4 Model;
};

in vec2 TexCoord;
out vec4 fragColor;

void main() {
    float coverage = texture(MaskSampler, TexCoord).a;
    float depth = texture(DepthSampler, TexCoord).r;
    if (coverage < 0.001 || depth <= 0.0) {
        fragColor = vec4(0.0);
        return;
    }
    vec4 position = InverseTransform * vec4(TexCoord * 2.0 - 1.0, ROSE_DEPTH_TO_CLIP(depth), 1.0);
    vec3 local = (position.xyz / position.w - vec3(0.0, Model.x * 0.5, 0.0)) / max(Model.x, 0.5);
    fragColor = vec4(clamp(local * 0.25 + 0.5, 0.0, 1.0), 1.0);
}
