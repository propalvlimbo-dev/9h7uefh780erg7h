#version 330

in vec2 FragCoord;
in vec2 Size;
in vec2 Cursor;
in float Clock;
in float Fade;

out vec4 fragColor;

float hash21(vec2 p) {
    p = fract(p * vec2(123.34, 345.45));
    p += dot(p, p + 34.345);
    return fract(p.x * p.y);
}

float valueNoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash21(i), hash21(i + vec2(1.0, 0.0)), f.x),
               mix(hash21(i + vec2(0.0, 1.0)), hash21(i + 1.0), f.x), f.y);
}

float fbm(vec2 p) {
    float sum = 0.0;
    float weight = 0.5;
    for (int i = 0; i < 4; i++) {
        sum += valueNoise(p) * weight;
        p = p * 2.03 + vec2(17.1, 11.7);
        weight *= 0.5;
    }
    return sum;
}

float sdCircle(vec2 p, vec2 center, float radius) {
    return length(p - center) - radius;
}

float sdRoundBox(vec2 p, vec2 center, vec2 halfSize, float radius) {
    vec2 q = abs(p - center) - halfSize + radius;
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - radius;
}

float sdSegment(vec2 p, vec2 a, vec2 b, float radius) {
    vec2 pa = p - a;
    vec2 ba = b - a;
    float h = clamp(dot(pa, ba) / dot(ba, ba), 0.0, 1.0);
    return length(pa - ba * h) - radius;
}

float ghostDistance(vec2 p) {
    float body = sdCircle(p, vec2(0.0, -11.0), 17.0);
    body = min(body, sdRoundBox(p, vec2(0.0, 5.0), vec2(17.0, 21.0), 10.0));
    body = min(body, sdSegment(p, vec2(-13.0, -1.0), vec2(-24.0, 8.0), 5.2));
    body = min(body, sdSegment(p, vec2(13.0, -1.0), vec2(24.0, 7.0), 5.2));
    body = min(body, sdCircle(p, vec2(-11.0, 21.0), 8.0));
    body = min(body, sdCircle(p, vec2(0.0, 17.0), 8.0));
    body = min(body, sdCircle(p, vec2(11.0, 21.0), 8.0));
    return body;
}

void main() {
    vec2 uv = FragCoord;
    vec2 centered = uv - 0.5;
    centered.x *= Size.x / max(Size.y, 1.0);
    vec2 mouse = Cursor / max(Size, vec2(1.0)) - 0.5;
    mouse.x *= Size.x / max(Size.y, 1.0);
    vec2 p = centered - mouse * 0.018;

    float slowNoise = fbm(p * 1.35 + vec2(Clock * 0.006, -Clock * 0.004));
    vec3 color = mix(vec3(0.016, 0.032, 0.054), vec3(0.030, 0.078, 0.124), uv.y);
    float topLight = exp(-p.x * p.x * 3.8 - (uv.y - 0.91) * (uv.y - 0.91) * 5.2);
    float centerLight = exp(-p.x * p.x * 1.65 - (uv.y - 0.58) * (uv.y - 0.58) * 3.0);
    color += vec3(0.15, 0.43, 0.69) * topLight * 0.48;
    color += vec3(0.07, 0.29, 0.48) * centerLight * 0.24;

    vec2 blobAOffset = vec2(sin(Clock * 0.09) * 0.16, cos(Clock * 0.07) * 0.08);
    vec2 blobBOffset = vec2(cos(Clock * 0.065) * 0.13, sin(Clock * 0.08) * 0.10);
    vec2 blobA = (p - vec2(-0.42, 0.18) - blobAOffset) * vec2(0.85, 1.4);
    vec2 blobB = (p - vec2(0.50, -0.02) - blobBOffset) * vec2(0.90, 1.25);
    vec2 blobC = (p - vec2(0.02, -0.36)) * vec2(0.72, 1.7);
    color += vec3(0.04, 0.23, 0.42) * exp(-dot(blobA, blobA) * 3.0) * 0.22;
    color += vec3(0.03, 0.37, 0.56) * exp(-dot(blobB, blobB) * 3.4) * 0.12;
    color += vec3(0.02, 0.17, 0.34) * exp(-dot(blobC, blobC) * 4.0) * 0.10;
    color += vec3(0.04, 0.16, 0.27) * (slowNoise - 0.5) * 0.055;

    float cursorLight = exp(-length(p - mouse) * 5.8);
    color += vec3(0.06, 0.43, 0.70) * cursorLight * 0.075;

    vec2 starGrid = uv * vec2(70.0, 40.0);
    vec2 starCell = floor(starGrid);
    vec2 starPoint = fract(starGrid) - 0.5;
    float starRandom = hash21(starCell);
    starPoint += vec2(sin(Clock * 0.16 + starRandom * 18.0), cos(Clock * 0.13 + starRandom * 12.0)) * 0.08;
    float stars = (1.0 - smoothstep(0.0, 0.045, length(starPoint))) * step(0.972, starRandom);
    float twinkle = 0.35 + 0.65 * (0.5 + 0.5 * sin(Clock * (0.7 + starRandom) + starRandom * 30.0));
    color += vec3(0.35, 0.82, 1.0) * stars * twinkle * 0.22;

    float vignette = 1.0 - smoothstep(0.33, 1.06, length(centered * vec2(0.82, 1.0)));
    color *= 0.54 + vignette * 0.46;

    float uiScale = Size.y / 720.0;
    float characterScale = uiScale * 1.32;
    vec2 screenPoint = vec2(uv.x * Size.x, (1.0 - uv.y) * Size.y);
    vec2 cursorFollow = clamp(Cursor / max(Size, vec2(1.0)) - 0.5, vec2(-0.5), vec2(0.5));
    vec2 characterCenter = vec2(
        Size.x * 0.5 + cursorFollow.x * 42.0 * uiScale,
        Size.y * 0.235 + cursorFollow.y * 20.0 * uiScale + sin(Clock * 1.15) * 2.5 * uiScale
    );
    vec2 ghostPoint = (screenPoint - characterCenter) / characterScale;
    float characterAngle = cursorFollow.x * 0.11;
    ghostPoint = mat2(cos(characterAngle), -sin(characterAngle), sin(characterAngle), cos(characterAngle)) * ghostPoint;
    float ghost = ghostDistance(ghostPoint);
    float solidGhost = 1.0 - smoothstep(-1.3, 1.3, ghost);
    float ghostMask = solidGhost;
    vec2 eyeFollow = cursorFollow * vec2(2.2, 1.4);
    float eyeLeft = 1.0 - smoothstep(1.6, 3.2, length(ghostPoint - vec2(-6.0, -12.0) - eyeFollow));
    float eyeRight = 1.0 - smoothstep(1.8, 3.6, length(ghostPoint - vec2(7.0, -12.0) - eyeFollow));
    float mouth = 1.0 - smoothstep(1.8, 3.5, length((ghostPoint - vec2(1.0, -3.0) - eyeFollow * 0.35) * vec2(0.75, 1.35)));
    ghostMask *= 1.0 - max(max(eyeLeft, eyeRight), mouth);
    float ghostGlow = exp(-max(ghost, 0.0) * 0.16) * (1.0 - solidGhost) * 0.32;
    vec3 ghostColor = vec3(0.33, 0.78, 1.0);
    color = mix(color, ghostColor, ghostGlow * Fade);
    color = mix(color, ghostColor, ghostMask * Fade);
    color *= mix(0.78, 1.0, smoothstep(0.0, 1.0, Fade));
    fragColor = vec4(color, 1.0);
}
