#version 330

in vec2 FragCoord;
in vec2 Size;
in vec2 Cursor;
in float Clock;
in float Fade;
in vec3 Primary;
in vec3 Secondary;

out vec4 fragColor;

float hash21(vec2 value) {
    value = fract(value * vec2(123.34, 456.21));
    value += dot(value, value + 45.32);
    return fract(value.x * value.y);
}

float thinLine(float value, float spacing, float width) {
    float distanceToLine = abs(fract(value / spacing) - 0.5) * spacing;
    float antialiasing = max(fwidth(value) * 0.72, 0.7);
    return 1.0 - smoothstep(width * 0.5 - antialiasing, width * 0.5 + antialiasing, distanceToLine);
}

vec3 diamondLayer(vec2 pixel, vec2 cellSize, float seed, float speed) {
    vec2 cellId = floor(pixel / cellSize);
    float random = hash21(cellId + seed);
    float offsetX = (hash21(cellId + seed * 2.7) - 0.5) * cellSize.x * 0.62;
    vec2 local = vec2(
        mod(pixel.x + offsetX, cellSize.x) - cellSize.x * 0.5,
        mod(pixel.y + Clock * (speed + random * speed) + random * cellSize.y, cellSize.y) - cellSize.y * 0.5
    );
    float visibility = step(0.76, random);
    float diamondDistance = abs(local.x) + abs(local.y);
    float antialiasing = max(fwidth(diamondDistance), 0.7);
    float wrapFade = 1.0 - smoothstep(cellSize.y * 0.34, cellSize.y * 0.48, abs(local.y));
    float core = (1.0 - smoothstep(1.0 - antialiasing, 2.5 + antialiasing, diamondDistance)) * visibility * wrapFade;
    float glow = (1.0 - smoothstep(2.0 - antialiasing, 7.5 + antialiasing, diamondDistance)) * visibility * wrapFade;
    float tail = exp(-abs(local.x) * 0.82)
            * smoothstep(1.0, 3.0, local.y)
            * (1.0 - smoothstep(3.0, 15.0, local.y))
            * visibility
            * wrapFade;
    return vec3(core, glow, tail);
}

void main() {
    vec2 pixel = FragCoord * Size;
    float fade = clamp(Fade, 0.0, 1.0);

    vec2 dotCell = fract(pixel / 6.0) - 0.5;
    float dots = 1.0 - smoothstep(0.075, 0.17, length(dotCell));
    float scanLines = thinLine(pixel.y + Clock * 7.0, 4.0, 0.85);

    vec3 diamondsA = diamondLayer(pixel, vec2(116.0, 94.0), 3.4, 8.0);
    vec3 diamondsB = diamondLayer(pixel + vec2(41.0, 19.0), vec2(154.0, 128.0), 11.7, 5.0);
    vec3 diamonds = diamondsA + diamondsB * vec3(0.82, 0.66, 0.72);

    vec2 cursorVector = (pixel - Cursor) / max(Size.y, 1.0);
    float cursorGlow = exp(-dot(cursorVector, cursorVector) * 7.0);
    vec3 theme = mix(Primary, Secondary, clamp(FragCoord.x * 0.72 + (1.0 - FragCoord.y) * 0.18, 0.0, 1.0));
    vec3 particleColor = mix(Secondary, Primary, hash21(floor(pixel / vec2(116.0, 94.0))));

    float alpha = (dots * 0.10
            + scanLines * 0.075
            + diamonds.x * 0.62
            + diamonds.y * 0.20
            + diamonds.z * 0.12) * fade;
    vec3 color = mix(vec3(0.72, 0.76, 0.86), theme, 0.30 + cursorGlow * 0.16);
    color = mix(color, particleColor, clamp(diamonds.x + diamonds.y * 0.62, 0.0, 1.0));

    if (alpha <= 0.001) discard;
    fragColor = vec4(color, alpha);
}
