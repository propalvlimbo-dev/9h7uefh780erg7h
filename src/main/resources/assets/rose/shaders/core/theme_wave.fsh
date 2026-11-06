#version 330

uniform sampler2D Sampler0;
uniform sampler2D BaseSampler;

layout(std140) uniform ThemeWave {
    vec4 CenterProgress;
    vec4 WaveParams;
    vec4 OldPalette[25];
    vec4 NewPalette[25];
    vec4 UiRects[192];
};

in vec2 TexCoord;
out vec4 fragColor;

float easeOutCubic(float t) {
    t = clamp(t, 0.0, 1.0);
    return 1.0 - pow(1.0 - t, 3.0);
}

float hash21(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);
}

float noise21(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash21(i), hash21(i + vec2(1.0, 0.0)), f.x),
               mix(hash21(i + vec2(0.0, 1.0)), hash21(i + vec2(1.0, 1.0)), f.x), f.y);
}

void main() {
    vec4 source = texture(Sampler0, TexCoord);
    vec4 background = texture(BaseSampler, TexCoord);
    float aspect = WaveParams.x / max(WaveParams.y, 1.0);
    vec2 center = CenterProgress.xy;
    float progress = clamp(CenterProgress.z, 0.0, 1.0);
    float t = easeOutCubic(progress);

    vec2 delta = (TexCoord - center) * vec2(aspect, 1.0);
    float dist = length(delta);
    vec2 farCorner = max(center, vec2(1.0) - center) * vec2(aspect, 1.0);
    float maxRadius = length(farCorner) + 0.14;

    vec2 sweepDir = normalize(vec2(0.86, 0.42));
    if (dot(delta, sweepDir) < 0.0) {
        sweepDir = -sweepDir;
    }
    vec2 acrossDir = vec2(-sweepDir.y, sweepDir.x);
    float along = dot(delta, sweepDir);
    float across = dot(delta, acrossDir);

    float silk = 0.048 * sin(across * 15.0 + t * 7.2)
            + 0.022 * sin(across * 33.0 - t * 4.6)
            + 0.016 * (noise21(vec2(across * 9.0, t * 3.0)) - 0.5);
    float cover = mix(-0.04, maxRadius + 0.16, t);
    float feather = mix(0.085, 0.038, t);
    float fold = abs(along) - cover - silk * mix(0.35, 1.0, t);
    float reveal = 1.0 - smoothstep(-feather, feather, fold);
    float originBloom = (1.0 - smoothstep(0.0, 0.42, t))
            * (1.0 - smoothstep(0.0, mix(0.08, 0.28, t), dist));
    reveal = max(reveal, originBloom);

    float uiRegion = 0.0;
    int uiRectCount = int(WaveParams.w + 0.5);
    for (int i = 0; i < 192; i++) {
        if (i >= uiRectCount) break;
        vec4 rect = UiRects[i];
        if (TexCoord.x >= rect.x && TexCoord.y >= rect.y && TexCoord.x <= rect.z && TexCoord.y <= rect.w) {
            uiRegion = 1.0;
            break;
        }
    }

    float bestResidual = 1000.0;
    float bestOpacity = 0.0;
    vec3 previous = source.rgb;
    for (int i = 0; i < 25; i++) {
        vec3 direction = NewPalette[i].rgb - background.rgb;
        float lengthSquared = dot(direction, direction);
        if (lengthSquared > 0.0001) {
            float opacity = clamp(dot(source.rgb - background.rgb, direction) / lengthSquared, 0.0, 1.0);
            vec3 reconstructed = mix(background.rgb, NewPalette[i].rgb, opacity);
            float residual = distance(source.rgb, reconstructed);
            if (residual < bestResidual) {
                bestResidual = residual;
                bestOpacity = opacity;
                previous = clamp(source.rgb + (OldPalette[i].rgb - NewPalette[i].rgb) * opacity, 0.0, 1.0);
            }
        }
    }

    float paletteMask = 1.0 - smoothstep(0.01, 0.07, bestResidual);
    float overlayMask = smoothstep(0.012, 0.10, bestOpacity);
    float guiDifference = max(max(abs(source.r - background.r), abs(source.g - background.g)),
            max(abs(source.b - background.b), abs(source.a - background.a)));
    float guiMask = smoothstep(0.0015, 0.016, guiDifference);
    float surface = uiRegion * guiMask;
    float themedGuiMask = max(paletteMask * overlayMask * surface, surface * 0.72);
    float waveMask = max(themedGuiMask, 0.55);
    vec3 outsideColor = mix(source.rgb, previous, themedGuiMask);
    vec3 result = mix(outsideColor, source.rgb, reveal);

    float edge = 1.0 - smoothstep(feather * 0.10, feather * 2.6, abs(fold));
    float edgeFade = 1.0 - smoothstep(0.70, 1.0, progress);
    vec2 uvDir = normalize(vec2(sweepDir.x / max(aspect, 0.001), sweepDir.y));
    float aberr = edge * edgeFade * 0.0075;
    vec3 chroma = vec3(
            texture(Sampler0, TexCoord + uvDir * aberr).r,
            source.g,
            texture(Sampler0, TexCoord - uvDir * aberr).b
    );
    result = mix(result, chroma, edge * waveMask * 0.62 * edgeFade);

    vec3 accentA = NewPalette[0].rgb;
    vec3 accentB = NewPalette[1].rgb;
    vec3 prism = mix(accentA, accentB, 0.5 + 0.5 * sin(across * 8.5 + t * 5.0));
    float ribbon = pow(0.5 + 0.5 * sin((abs(along) - cover) * 26.0 + across * 5.5 - t * 8.0), 10.0);
    ribbon *= edge * edgeFade;
    result = mix(result, prism, edge * waveMask * 0.38 * edgeFade);
    result += prism * ribbon * waveMask * 0.48;
    result = mix(result, accentA, originBloom * waveMask * 0.28);

    fragColor = vec4(result, source.a);
}
