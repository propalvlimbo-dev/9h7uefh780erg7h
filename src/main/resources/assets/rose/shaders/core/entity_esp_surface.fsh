#version 330

uniform sampler2D Sampler0;
layout(std140) uniform EspSurfaceAnchors {
    vec4 AnchorInfo;
    vec4 WorldUp;
    vec4 Centers[64];
};
in vec2 texCoord0;
flat in vec4 silhouetteAnchor;
in vec3 surfaceViewPosition;
flat in vec3 planeRight;
flat in vec3 planeNormal;
out vec4 fragColor;

void main() {
    if (texture(Sampler0, texCoord0).a == 0.0) discard;
    float denominator = dot(surfaceViewPosition, planeNormal);
    denominator = (denominator < 0.0 ? -1.0 : 1.0) * max(abs(denominator), 0.00001);
    vec3 hit = surfaceViewPosition * (dot(silhouetteAnchor.xyz, planeNormal) / denominator);
    vec3 relative = hit - silhouetteAnchor.xyz;
    vec2 local = vec2(dot(relative, planeRight), dot(relative, normalize(WorldUp.xyz)))
            / max(silhouetteAnchor.w, 0.00001);
    vec2 encodedUv = floor(clamp(local / 8.0 + 0.5, 0.0, 1.0) * 65535.0 + 0.5);
    fragColor = vec4(floor(encodedUv.x / 256.0), mod(encodedUv.x, 256.0),
                    floor(encodedUv.y / 256.0), mod(encodedUv.y, 256.0)) / 255.0;
}
