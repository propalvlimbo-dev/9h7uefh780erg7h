#version 330

#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV1;
in ivec2 UV2;
in vec3 Normal;

layout(std140) uniform PrismaticChams {
    vec4 u_AccentTop;
    vec4 u_AccentBottom;
    vec4 u_CameraAndTime;
    vec4 u_Params;
    vec4 u_State;
    vec4 u_ResolutionInfo;
    vec4 u_EntityScreen;
};

#define u_Time u_CameraAndTime.w
#define u_Animation u_State.x
#define u_Resolution u_ResolutionInfo.xy

out vec3 v_Normal;
out vec3 v_ViewDir;
out vec3 v_ViewPos;
out vec3 v_Position;
out vec4 v_Color;
out vec2 v_Uv;
out vec2 v_ScreenPos;
out vec2 v_EntityScreenPos;

vec2 screenFromClip(vec4 clip) {
    float w = abs(clip.w) < 0.00001 ? (clip.w < 0.0 ? -0.00001 : 0.00001) : clip.w;
    vec2 ndc = clip.xy / w;
    return (ndc * 0.5 + 0.5) * u_Resolution;
}

void main() {
    float entry = smoothstep(0.0, 1.0, clamp(u_Animation, 0.0, 1.0));
    float pull = 1.0 - entry;
    vec3 animatedPosition = Position + Normal * pull * (0.038 + 0.014 * sin(u_Time * 6.1 + Position.y * 9.0));
    vec4 clipPosition = ProjMat * vec4(animatedPosition, 1.0);
    gl_Position = clipPosition;
    v_Normal = normalize(Normal);
    v_ViewDir = -animatedPosition;
    v_ViewPos = animatedPosition;
    v_Position = animatedPosition;
    v_Color = Color;
    v_Uv = UV0;
    v_ScreenPos = screenFromClip(clipPosition);
    v_EntityScreenPos = u_EntityScreen.xy;
}
