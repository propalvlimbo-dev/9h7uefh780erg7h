#version 330

in vec2 texCoord;
in vec4 vertexColor;
uniform sampler2D Sampler0;
out vec4 fragColor;

// The shared client_logo font atlas is a scalar SDF, with distanceRange = 64 texels.
void main() {
    float distance = texture(Sampler0, texCoord).r - 0.5;
    vec2 derivative = vec2(dFdx(texCoord.x), dFdy(texCoord.y)) * textureSize(Sampler0, 0);
    float pixelRange = max(64.0 * inversesqrt(max(dot(derivative, derivative), 0.000001)), 1.0);
    float coverage = smoothstep(-0.5, 0.5, distance * pixelRange);
    float alpha = vertexColor.a * coverage;
    if (alpha <= 0.001) discard;
    fragColor = vec4(vertexColor.rgb, alpha);
}
