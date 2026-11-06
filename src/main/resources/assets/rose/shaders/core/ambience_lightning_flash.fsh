#version 330

layout(std140) uniform LightningFlash {
    vec4 Bolt;
    vec4 Params;
    vec4 Accent;
};

uniform sampler2D DepthSampler;

in vec2 TexCoord;
out vec4 fragColor;

float segmentDistance(vec2 point, vec2 start, vec2 endPoint) {
    vec2 direction = endPoint - start;
    float lengthSquared = max(dot(direction, direction), 0.000001);
    float position = clamp(dot(point - start, direction) / lengthSquared, 0.0, 1.0);
    return length(point - start - direction * position);
}

void main() {
    vec2 point = vec2(TexCoord.x * Params.y, TexCoord.y);
    vec2 start = vec2(Bolt.x * Params.y, Bolt.y);
    vec2 endPoint = vec2(Bolt.z * Params.y, Bolt.w);
    float distanceFromBolt = segmentDistance(point, start, endPoint);
    float radius = max(Params.z, 0.001);
    float innerLight = exp(-distanceFromBolt * distanceFromBolt / (radius * radius) * 3.0);
    float stormGlow = exp(-distanceFromBolt * distanceFromBolt / (radius * radius) * 0.72);
    float darkHalo = smoothstep(0.16, 0.55, distanceFromBolt / radius)
            * (1.0 - smoothstep(0.62, 1.75, distanceFromBolt / radius));
    float depth = texture(DepthSampler, TexCoord).r;
    float sky = (1.0 - step(0.000001, depth));
    float localLight = Params.x * innerLight * mix(0.28, 0.62, sky);
    float skyFlash = Params.x * sky * (0.055 + stormGlow * 0.15);
    float darkness = Params.x * darkHalo * mix(0.17, 0.27, sky);
    vec3 accent = clamp(Accent.rgb, 0.0, 1.0);
    vec3 brightAccent = mix(accent, vec3(1.0), 0.68);
    vec3 shadowAccent = accent * 0.025;
    float lightAlpha = clamp(localLight + skyFlash, 0.0, 0.58);
    float shadowWeight = darkness * (1.0 - smoothstep(0.0, 0.35, innerLight));
    vec3 lightColor = mix(accent, brightAccent, innerLight);
    vec3 color = mix(lightColor, shadowAccent, clamp(shadowWeight * 2.5, 0.0, 1.0));
    float alpha = clamp(lightAlpha + shadowWeight, 0.0, 0.62);
    if (alpha <= 0.00001) discard;
    fragColor = vec4(color, alpha);
}
