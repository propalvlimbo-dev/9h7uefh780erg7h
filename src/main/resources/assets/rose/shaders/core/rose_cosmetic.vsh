#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in ivec2 UV1;
in ivec2 UV2;
in vec3 Normal;

out vec3 vViewPos;
out vec3 vNormal;
out vec2 vUv;
out vec4 vColor;
out vec2 vLight;

void main() {
    vec4 viewPosition = ModelViewMat * vec4(Position, 1.0);
    gl_Position = ProjMat * viewPosition;
    vViewPos = viewPosition.xyz;
    vNormal = normalize(mat3(ModelViewMat) * Normal);
    vUv = UV0;
    vColor = Color;
    vLight = vec2(UV2) / 256.0;
}
