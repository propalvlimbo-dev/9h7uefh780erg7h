#version 330

layout(std140) uniform TNTSphere {
    mat4 Projection;
    mat4 View;
    vec4 Screen;
};

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in vec3 Normal;

out vec3 ViewPosition;
out vec3 ViewNormal;
out vec2 SphereUv;
out vec2 SphereData;
out vec4 SphereColor;

void main() {
    vec4 viewPosition = View * vec4(Position, 1.0);
    ViewPosition = viewPosition.xyz;
    ViewNormal = normalize(mat3(View) * Normal);
    SphereUv = vec2(atan(Normal.z, Normal.x) / 6.28318531 + 0.5, asin(clamp(Normal.y, -1.0, 1.0)) / 3.14159265 + 0.5);
    SphereData = UV0;
    SphereColor = Color;
    gl_Position = Projection * viewPosition;
}
