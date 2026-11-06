#version 330

#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;

in vec4 vertexColor;
in vec2 texCoord;

out vec4 fragColor;

vec3 permute(vec3 x) {
    return mod(((x * 34.0) + 1.0) * x, 289.0);
}

float snoise(vec2 value) {
    const vec4 coefficients = vec4(0.211324865405187, 0.366025403784439, -0.577350269189626, 0.024390243902439);
    vec2 cell = floor(value + dot(value, coefficients.yy));
    vec2 local = value - cell + dot(cell, coefficients.xx);
    vec2 corner = local.x > local.y ? vec2(1.0, 0.0) : vec2(0.0, 1.0);
    vec4 offsets = local.xyxy + coefficients.xxzz;
    offsets.xy -= corner;
    cell = mod(cell, 289.0);
    vec3 permutation = permute(permute(cell.y + vec3(0.0, corner.y, 1.0)) + cell.x + vec3(0.0, corner.x, 1.0));
    vec3 mask = max(0.5 - vec3(dot(local, local), dot(offsets.xy, offsets.xy), dot(offsets.zw, offsets.zw)), 0.0);
    mask *= mask;
    mask *= mask;
    vec3 gradient = 2.0 * fract(permutation * coefficients.www) - 1.0;
    vec3 height = abs(gradient) - 0.5;
    vec3 offset = floor(gradient + 0.5);
    vec3 slope = gradient - offset;
    mask *= 1.79284291400159 - 0.85373472095314 * (slope * slope + height * height);
    vec3 direction;
    direction.x = slope.x * local.x + height.x * local.y;
    direction.yz = slope.yz * offsets.xz + height.yz * offsets.yw;
    return 130.0 * dot(mask, direction);
}

void main() {
    vec2 sceneUv = vec2(texCoord.x, 1.0 - texCoord.y);
    float time = GameTime * 1200.0;
    vec2 field = texCoord * 14.0;
    float firstNoise = snoise(field + vec2(time * 0.35, time * 0.20));
    float secondNoise = snoise(field * 1.7 - vec2(time * 0.25, time * 0.40) + 37.0);
    vec2 offset = vec2(firstNoise, secondNoise) * 0.010 * vertexColor.a;
    vec3 scene = texture(Sampler0, clamp(sceneUv + offset, vec2(0.001), vec2(0.999))).rgb;
    float crest = clamp(0.5 + 0.5 * (firstNoise * 0.7 + secondNoise * 0.3), 0.0, 1.0);
    scene += crest * crest * crest * 0.12 * vertexColor.a;
    fragColor = vec4(scene, vertexColor.a);
}
