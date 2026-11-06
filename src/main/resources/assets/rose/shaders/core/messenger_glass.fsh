#version 330
#moj_import <rose:defaults.glsl>

// Telegram iOS 6ad963e5: LegacyGlassView + GenerateMesh analytical displacement.
uniform sampler2D Sampler0;
in vec2 FragCoord;
flat in vec4 PanelRect;
flat in vec4 Options;
flat in vec4 Tint;
flat in vec4 Scissor;
flat in float UseScissor;
out vec4 fragColor;

float bezier(float t, float a, float b) {
    return ((1.0 - 3.0*b + 3.0*a)*t*t + (3.0*b - 6.0*a)*t + 3.0*a)*t;
}
float displacementCurve(float x) {
    const float x1 = 0.816137566137566, x2 = 0.5806878306878306;
    const float y1 = 0.20502645502645533, y2 = 0.873015873015873;
    float t = x;
    for (int i = 0; i < 4; ++i) {
        float slope = 3.0*(1.0-3.0*x2+3.0*x1)*t*t + 2.0*(3.0*x2-6.0*x1)*t + 3.0*x1;
        if (abs(slope) > 0.00001) t -= (bezier(t,x1,x2)-x)/slope;
    }
    float value = bezier(t,y1,y2);
    return value >= 0.997 ? 1.0 : clamp(value,0.0,1.0);
}
vec3 sampleBackdrop(vec2 point, vec2 resolution) {
    return texture(Sampler0,vec2(point.x, resolution.y-point.y)/resolution).rgb;
}
void main() {
    if (UseScissor == 1.0 && !insideScissor(FragCoord,Scissor)) discard;
    vec2 resolution = vec2(textureSize(Sampler0,0));
    vec2 pixel = vec2(gl_FragCoord.x, resolution.y-gl_FragCoord.y);
    vec2 size = PanelRect.zw;
    vec2 local = pixel-PanelRect.xy;
    vec2 centered = local-size*.5;
    float radius = Options.x, scale = Options.y;
    vec2 q = abs(centered)-size*.5+radius;
    float sdf = length(max(q,0.0))+min(max(q.x,q.y),0.0)-radius;
    float coverage = 1.0-smoothstep(-.6,.6,sdf);
    if (coverage <= 0.0) discard;

    vec2 normal;
    if (q.x>0.0 && q.y>0.0) normal = normalize(q);
    else normal = q.x>q.y ? vec2(1.0,0.0) : vec2(0.0,1.0);
    normal *= sign(centered);
    float edgeDistance = max(scale,min(12.0*scale,radius));
    float depth = max(0.0,-sdf);
    float weight = clamp(1.0-depth/edgeDistance,0.0,1.0);
    float t = clamp((2.0*scale-depth)/(2.0*scale),0.0,1.0);
    float edgeBoost = 1.0+t*t*(3.0-2.0*t)*.5;
    vec2 displaced = local*(1.0+2.0*scale/size)-scale-normal*displacementCurve(weight)*20.0*scale*edgeBoost;
    vec2 source = PanelRect.xy+clamp(displaced,vec2(0.0),size);

    float b = 2.0*scale;
    vec3 color = sampleBackdrop(source,resolution)*.25;
    color += (sampleBackdrop(source+vec2(b,0),resolution)+sampleBackdrop(source-vec2(b,0),resolution)
            +sampleBackdrop(source+vec2(0,b),resolution)+sampleBackdrop(source-vec2(0,b),resolution))*.125;
    color += (sampleBackdrop(source+vec2(b,b),resolution)+sampleBackdrop(source-vec2(b,b),resolution)
            +sampleBackdrop(source+vec2(-b,b),resolution)+sampleBackdrop(source+vec2(b,-b),resolution))*.0625;
    if (Options.z < .5) color = clamp(vec3(
        dot(color,vec3(2.6705,-1.1087999,-.1117)),
        dot(color,vec3(-.3295,1.8914,-.111899994)),
        dot(color,vec3(-.3297,-1.1084,2.8881)))+.05,0.0,1.0);
    color = mix(color,Tint.rgb,Tint.a);
    float border = (1.0-smoothstep(.4*scale,.4*scale+.65,abs(sdf+.4*scale)))*.15;
    color = mix(color,vec3(1.0),border);
    fragColor = vec4(color,coverage*Options.w);
}
