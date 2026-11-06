#version 330

in vec2 coatCoord;
in vec4 vertexColor;

out vec4 fragColor;

float hash(vec2 point) {
    return fract(sin(dot(point, vec2(127.1, 311.7))) * 43758.5453123);
}

float noise(vec2 point) {
    vec2 cell = floor(point);
    vec2 offset = fract(point);
    offset = offset * offset * (3.0 - 2.0 * offset);

    float corner = hash(cell);
    float right = hash(cell + vec2(1.0, 0.0));
    float top = hash(cell + vec2(0.0, 1.0));
    float diagonal = hash(cell + vec2(1.0, 1.0));

    return mix(mix(corner, right, offset.x), mix(top, diagonal, offset.x), offset.y);
}

float coat(vec2 point) {
    float clumps = noise(point * 4.5);
    float grain = noise(point * 15.0 + clumps * 1.2);
    float down = noise(vec2(point.x * 58.0 + grain * 2.4, point.y * 26.0));

    return clumps * 0.30 + grain * 0.34 + down * 0.36;
}

void main() {
    if (vertexColor.a <= 0.004) discard;

    float fibre = coat(coatCoord);
    float depth = smoothstep(0.20, 0.84, fibre);

    vec3 shaded = vertexColor.rgb * (0.90 + depth * 0.20);
    shaded += vertexColor.rgb * pow(depth, 5.0) * 0.16;

    fragColor = vec4(shaded, vertexColor.a);
}
