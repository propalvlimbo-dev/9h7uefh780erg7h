#version 330
layout(std140) uniform AuroraView {
    mat4 inverseProjection;
    mat4 inverseView;
};
out vec3 skyDir;
void main() {
    vec2 pos = vec2((gl_VertexID << 1) & 2, gl_VertexID & 2) * 2.0 - 1.0;
    vec4 nearPoint = inverseProjection * vec4(pos, 0.5, 1.0);
    vec4 farPoint = inverseProjection * vec4(pos, 0.25, 1.0);
    vec3 ray = farPoint.xyz * nearPoint.w - nearPoint.xyz * farPoint.w;
    skyDir = mat3(inverseView) * ray;
    gl_Position = vec4(pos, 0.0, 1.0);
}
