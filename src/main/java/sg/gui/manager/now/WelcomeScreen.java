package sg.gui.manager.now;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public class WelcomeScreen extends Screen {
    private final long timeMillis = System.currentTimeMillis();
    private long timeMillis2;
    private long timeMillis3;
    private final AnimatedValue animatedValue = new AnimatedValue(160L);
    private float floatVal;
    private float floatVal2;
    private float floatVal3;
    private float floatVal4;

    public WelcomeScreen() {
        super(Component.literal("Rose"));
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x(), mouseY = event.y();
        int button = event.button();
        if (button == 0) {
            long now = System.currentTimeMillis();
            if (this.timeMillis2 == 0L) {
                if (now - this.timeMillis > 400L) {
                    this.timeMillis2 = now;
                }
                return true;
            }

            float sf = spaceFactor();
            if (this.timeMillis3 == 0L
                    && UiTheme.matchesCriteria(
                    (float) mouseX * sf, (float) mouseY * sf, this.floatVal, this.floatVal2, this.floatVal3, this.floatVal4
            )) {
                this.timeMillis3 = now;
                return true;
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().gui.setScreen(new MainMenuScreen(true));
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int keyCode = event.key();
        if (keyCode == 256 || keyCode == 257 || keyCode == 32) { // ESC, ENTER, SPACE
            long now = System.currentTimeMillis();
            if (this.timeMillis2 == 0L) {
                this.timeMillis2 = now;
            }
            if (this.timeMillis3 == 0L) {
                this.timeMillis3 = now;
            }
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float delta) {
    }

    private static float spaceFactor() {
        return 1.0f;
    }

    private void drawSessionChip(float local5206_1, float local5206_2, float local5206_3) {
        Minecraft mc = Minecraft.getInstance();
        String username = mc.getUser().getName();
        GuiGraphicsExtractor context = Render2D.getCurrentDrawContext();
        if (context != null) {
            AccountPreviewRenderer.renderElement(
                    context,
                    new AccountEntry(username, AccountEntry.getUUID(username)),
                    local5206_3,
                    local5206_1,
                    local5206_2,
                    15.0F * local5206_3
            );
        }

        UiTheme.processData3(
                "assets/deluxe/textures/gui/mainmenu/icons/person.png",
                local5206_1 + 20.0F * local5206_3,
                local5206_2 + 0.8F * local5206_3,
                3.5F * local5206_3,
                4.5F * local5206_3,
                UiTheme.intVal29
        );
        FontText.processData3(
                FontFace.SEMI,
                "Активная сессия",
                local5206_1 + 25.5F * local5206_3,
                local5206_2 + 0.5F * local5206_3,
                5.0F * local5206_3,
                UiTheme.intVal29
        );
        FontText.processData3(
                FontFace.SEMI_BOLD,
                username,
                local5206_1 + 20.0F * local5206_3,
                local5206_2 + 7.5F * local5206_3,
                6.5F * local5206_3,
                UiTheme.intVal
        );
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float delta) {
        float screenW = this.width;
        float screenH = this.height;
        float scale = Math.min(screenW / 960.0F, screenH / 540.0F);
        float centerY = (screenH - 540.0F * scale) / 2.0F;
        long now = System.currentTimeMillis();

        UiTheme.flag = true;
        Render2D.renderElement5(drawContext);

        if (this.timeMillis2 == 0L && now - this.timeMillis >= 2700L) {
            this.timeMillis2 = now;
        }

        float welcomeFade = this.timeMillis2 == 0L ? 0.0F : UiTheme.computeFloat2(UiTheme.computeFloat3(this.timeMillis2, 0L, 450L));
        UiTheme.processData6("assets/deluxe/textures/gui/mainmenu/background/loading.png", screenW, screenH, 1.0F);

        if (welcomeFade > 0.001F) {
            UiTheme.processData6("assets/deluxe/textures/gui/mainmenu/background/welcome.png", screenW, screenH, welcomeFade);
        }

        if (welcomeFade < 0.999F) {
            Render2D.processData46(1.0F - welcomeFade);
            UiTheme.processData4(this.timeMillis, screenW, screenH);
            Render2D.updateState();
        }

        if (welcomeFade > 0.001F) {
            float transitionExit = this.timeMillis3 == 0L ? 0.0F : UiTheme.computeFloat2(UiTheme.computeFloat3(this.timeMillis3, 0L, 320L));
            Render2D.processData46(welcomeFade * (1.0F - transitionExit));
            this.drawWelcomePhase(
                    screenH, scale, centerY, (float) mouseX, (float) mouseY, transitionExit * 26.0F * scale
            );
            Render2D.updateState();
        }

        Render2D.updateState6();
    }

    @Override
    public void tick() {
        long now = System.currentTimeMillis();
        if (this.timeMillis3 != 0L && now - this.timeMillis3 >= 340L) {
            Minecraft.getInstance().gui.setScreen(new MainMenuScreen(true));
        } else if (this.timeMillis3 == 0L && now - this.timeMillis >= 5000L) {
            Minecraft.getInstance().gui.setScreen(new MainMenuScreen(true));
        }
    }

    private void drawWelcomePhase(float screenH, float scale, float centerY, float mouseX, float mouseY, float shiftX) {
        long phaseStart = this.timeMillis2;
        float baseLeft = 51.5F * scale - shiftX;
        float logoAnim = UiTheme.computeFloat2(UiTheme.computeFloat3(phaseStart, 0L, 420L));
        Render2D.processData46(logoAnim);
        Render2D.processData89(
                "assets/deluxe/textures/gui/mainmenu/icons/logo.png",
                baseLeft - (1.0F - logoAnim) * 14.0F * scale,
                40.0F * scale,
                13.5F * scale,
                10.5F * scale,
                0.0F,
                UiTheme.intVal7
        );
        Render2D.updateState();

        float titleAnim = UiTheme.computeFloat2(UiTheme.computeFloat3(phaseStart, 90L, 460L));
        Render2D.processData46(titleAnim);
        float titleX = baseLeft - (1.0F - titleAnim) * 18.0F * scale;
        FontText.processData3(
                FontFace.SEMI, "Добро пожаловать в", titleX, centerY + 209.0F * scale, 20.0F * scale, UiTheme.intVal
        );
        FontText.processData3(FontFace.SEMI_BOLD, "Rose", titleX,
                centerY + 236.0F * scale, 22.0F * scale, UiTheme.intVal7);
        Render2D.updateState();

        float descAnim = UiTheme.computeFloat2(UiTheme.computeFloat3(phaseStart, 170L, 460L));
        Render2D.processData46(descAnim);
        float descY = centerY + 268.5F * scale;

        for (String line : UiTheme.getList(
                FontFace.SEMI,
                "Всё готово для комфортной игры. Настройте клиент под себя, используйте любимые функции и откройте больше возможностей вместе с Rose.",
                7.5F * scale,
                246.0F * scale
        )) {
            FontText.processData3(
                    FontFace.SEMI,
                    line,
                    baseLeft - (1.0F - descAnim) * 18.0F * scale,
                    descY,
                    7.5F * scale,
                    UiTheme.intVal29
            );
            descY += 9.0F * scale;
        }
        Render2D.updateState();

        float btnAnim = UiTheme.computeFloat2(UiTheme.computeFloat3(phaseStart, 250L, 460L));
        Render2D.processData46(btnAnim);
        this.floatVal = baseLeft - (1.0F - btnAnim) * 18.0F * scale;
        this.floatVal2 = centerY + 311.0F * scale;
        this.floatVal3 = 231.0F * scale;
        this.floatVal4 = 19.5F * scale;
        this.animatedValue.processData(
                this.timeMillis3 == 0L
                        && UiTheme.matchesCriteria(mouseX, mouseY, this.floatVal, this.floatVal2, this.floatVal3, this.floatVal4)
                        ? 1.0
                        : 0.0
        );
        float btnHover = (float) this.animatedValue.computeDouble();
        Render2D.processData51(
                this.floatVal,
                this.floatVal2,
                this.floatVal3,
                this.floatVal4,
                6.0F * scale,
                UiTheme.computeInt2(UiTheme.intVal7, UiTheme.intVal8, btnHover * 0.65F)
        );
        Render2D.processData86(this.floatVal, this.floatVal2, this.floatVal3, this.floatVal4, 6.0F * scale, 0.5F, UiTheme.intVal34);
        UiTheme.processData3(
                "assets/deluxe/textures/gui/mainmenu/icons/arrow.png",
                this.floatVal + 9.5F * scale + btnHover * 1.5F * scale,
                this.floatVal2 + this.floatVal4 / 2.0F - 3.0F * scale,
                6.0F * scale,
                6.0F * scale,
                UiTheme.intVal10
        );
        FontText.processData3(
                FontFace.SEMI,
                "Перейти в главное меню",
                this.floatVal + 19.5F * scale,
                this.floatVal2 + this.floatVal4 / 2.0F - 3.5F * scale,
                7.0F * scale,
                UiTheme.intVal10
        );
        Render2D.updateState();

        float sessionAnim = UiTheme.computeFloat2(UiTheme.computeFloat3(phaseStart, 330L, 460L));
        Render2D.processData46(sessionAnim);
        this.drawSessionChip(baseLeft, screenH - 51.0F * scale + (1.0F - sessionAnim) * 10.0F * scale, scale);
        Render2D.updateState();
    }
}
