#version 330

#moj_import <rose:defaults.glsl>

layout(std140) uniform ClickGuiBackground {
    vec4 ScreenData;
    vec4 CursorData;
    vec4 PrimaryColor;
    vec4 SecondaryColor;
};

in vec3 Position;

out vec2 FragCoord;
out vec2 Size;
out vec2 Cursor;
out float Clock;
out float Fade;
out vec3 Primary;
out vec3 Secondary;

void main() {
    gl_Position = vec4(Position, 1.0);
    FragCoord = rvertexcoord(gl_VertexID).yx;
    Size = ScreenData.xy;
    Clock = ScreenData.z;
    Fade = ScreenData.w;
    Cursor = CursorData.xy;
    Primary = PrimaryColor.rgb;
    Secondary = SecondaryColor.rgb;
}
