package sg.gui.manager.now;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMenuScreen extends Screen {
    /** Отношение ширины к высоте спрайта Херобрина (708x1240). */
    private static final float HERO_RATIO = 0.5710F;
    private static final String COMMUNITY_URL = "https://t.me/Rose";
    private final AccountManagerPanel accountManagerPanel = new AccountManagerPanel();
    private boolean flag = false;
    private final AnimatedValue animation = new AnimatedValue(260L);
    private final Map<String, AnimatedValue> map = new HashMap<>();
    private final List<DataRecord> list = new ArrayList<>();
    private boolean flag3;
    private long timeMillis = System.currentTimeMillis();

    public MainMenuScreen(boolean local7240_1) {
        super(Component.literal("Rose"));
        this.flag3 = local7240_1;
    }

    public MainMenuScreen() {
        this(false);
    }

    private String welcomeName() {
        Minecraft mc = Minecraft.getInstance();
        return (mc != null && mc.getUser() != null && !mc.getUser().getName().isBlank())
                ? mc.getUser().getName()
                : "Player";
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float delta) {
    }

    @Override
    protected void init() {
        super.init();
        if (System.currentTimeMillis() - this.timeMillis > 600L) {
            this.timeMillis = System.currentTimeMillis();
            this.flag3 = false;
            this.flag = false;
            this.animation.getAnimatedValue(0.0);
            this.accountManagerPanel.updateState();
        }
    }

    private static float spaceFactor() {
        return 1.0f;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x(), mouseY = event.y();
        int button = event.button();
        if (button == 0) {
            float sf = spaceFactor();
            float mx = (float) mouseX * sf;
            float my = (float) mouseY * sf;
            if (this.flag && this.animation.computeDouble() > 0.6 && this.accountManagerPanel.matchesCriteria(mx, my)) {
                return true;
            }

            for (int index = this.list.size() - 1; index >= 0; index--) {
                if (UiTheme.matchesCriteria(
                        mx,
                        my,
                        this.list.get(index).x(),
                        this.list.get(index).y(),
                        this.list.get(index).w(),
                        this.list.get(index).h()
                )) {
                    this.list.get(index).action().run();
                    return true;
                }
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        return super.mouseDragged(event, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        float sf = spaceFactor();
        if (this.flag && this.accountManagerPanel.matchesCriteria2((float) mouseX * sf, (float) mouseY * sf, (float) verticalAmount)) {
            return true;
        } else {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int keyCode = event.key();
        if (this.flag) {
            if (this.accountManagerPanel.matchesCriteria3(keyCode)) {
                return true;
            }

            if (keyCode == 256) {
                this.toggleAlt();
                return true;
            }
        }

        return super.keyPressed(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (this.flag && this.accountManagerPanel.matchesCriteria4(event.codepoint())) {
            return true;
        }
        return super.charTyped(event);
    }

    private void drawMenuColumn(float local7244_1, float local7244_2, float local7244_3, float local7244_4, float local7244_5, float local7244_6) {
        Minecraft minecraftClient = Minecraft.getInstance();
        float local7244_8 = local7244_1 + 51.5F * local7244_2;
        boolean local7244_9 = local7244_6 < 0.5F;
        float local7244_10 = this.slideInPC(0);
        Render2D.processData46(local7244_10);
        float local7244_11 = local7244_8 - (1.0F - local7244_10) * 16.0F * local7244_2;
        FontText.processData3(
                FontFace.SEMI,
                "Rose  /  Главная",
                local7244_11,
                local7244_3 + 127.5F * local7244_2,
                6.5F * local7244_2,
                UiTheme.intVal29
        );
        FontText.processData3(
                FontFace.SEMI, "Рады видеть тебя,", local7244_11, local7244_3 + 142.0F * local7244_2, 20.0F * local7244_2, UiTheme.intVal
        );
        FontText.processData3(
                FontFace.SEMI,
                this.welcomeName() + ".",
                local7244_11,
                local7244_3 + 166.2F * local7244_2,
                20.0F * local7244_2,
                UiTheme.intVal
        );
        FontText.processData3(
                FontFace.SEMI,
                "Всё готово - выбирай режим и начинай играть.",
                local7244_11,
                local7244_3 + 201.0F * local7244_2,
                7.5F * local7244_2,
                UiTheme.intVal29
        );
        Render2D.updateState();

        this.drawOnlineButton(
                local7244_8,
                local7244_3 + 227.0F * local7244_2,
                local7244_2,
                local7244_4,
                local7244_5,
                local7244_9,
                () -> minecraftClient.gui.setScreen(new JoinMultiplayerScreen(this))
        );

        this.drawRow(
                local7244_8,
                local7244_3 + 270.0F * local7244_2,
                local7244_2,
                2,
                "assets/deluxe/textures/gui/mainmenu/icons/singleplayer.png",
                6.5F,
                "Одиночная игра",
                null,
                0,
                local7244_4,
                local7244_5,
                local7244_9,
                () -> minecraftClient.gui.setScreen(new SelectWorldScreen(this))
        );

        this.drawRow(
                local7244_8,
                local7244_3 + 295.5F * local7244_2,
                local7244_2,
                3,
                "assets/deluxe/textures/gui/mainmenu/icons/accounts.png",
                6.5F,
                "Ваши аккаунты",
                "assets/deluxe/textures/gui/mainmenu/icons/bell.png",
                UiTheme.intVal35,
                local7244_4,
                local7244_5,
                local7244_9,
                this::toggleAlt
        );

        this.drawRow(
                local7244_8,
                local7244_3 + 321.0F * local7244_2,
                local7244_2,
                4,
                "assets/deluxe/textures/gui/mainmenu/icons/settings.png",
                7.0F,
                "Настройки игры",
                null,
                0,
                local7244_4,
                local7244_5,
                local7244_9,
                () -> minecraftClient.gui.setScreen(new OptionsScreen(this, minecraftClient.options, false))
        );

        this.drawRow(
                local7244_8,
                local7244_3 + 351.0F * local7244_2,
                local7244_2,
                5,
                "assets/deluxe/textures/gui/mainmenu/icons/exit.png",
                7.0F,
                "Выйти из игры",
                null,
                0,
                local7244_4,
                local7244_5,
                local7244_9,
                minecraftClient::stop
        );
    }

    private void drawRow(
            float local7245_1,
            float local7245_2,
            float local7245_3,
            int local7245_4,
            String local7245_5,
            float local7245_6,
            String local7245_7,
            String local7245_8,
            int local7245_9,
            float local7245_10,
            float local7245_11,
            boolean local7245_12,
            Runnable runnable
    ) {
        float local7245_14 = this.slideInPC(local7245_4);
        Render2D.processData46(local7245_14);
        float local7245_15 = local7245_1 - (1.0F - local7245_14) * 16.0F * local7245_3;
        float local7245_16 = this.hoverPC(
                "row" + local7245_7,
                local7245_12 && UiTheme.matchesCriteria(local7245_10, local7245_11, local7245_15, local7245_2, 226.5F * local7245_3, 19.5F * local7245_3)
        );
        local7245_15 += local7245_16 * 2.0F * local7245_3;

        float btnH = 19.5F * local7245_3;
        Render2D.processData51(
                local7245_15,
                local7245_2,
                226.5F * local7245_3,
                btnH,
                6.0F * local7245_3,
                UiTheme.computeInt(UiTheme.intVal, 0.031F + 0.05F * local7245_16)
        );
        Render2D.processData86(
                local7245_15,
                local7245_2,
                226.5F * local7245_3,
                btnH,
                6.0F * local7245_3,
                0.5F,
                UiTheme.computeInt(UiTheme.intVal, 0.078F + 0.09F * local7245_16)
        );

        float iconSize = local7245_6 * local7245_3;
        float iconY = local7245_2 + (btnH - iconSize) / 2.0F;
        float iconX = local7245_15 + 10.0F * local7245_3;

        UiTheme.processData3(
                local7245_5,
                iconX,
                iconY,
                iconSize,
                iconSize,
                UiTheme.computeInt2(UiTheme.intVal28, UiTheme.intVal, local7245_16)
        );

        FontText.processData3(
                FontFace.SEMI,
                local7245_7,
                local7245_15 + 21.0F * local7245_3,
                local7245_2 + (btnH - 7.0F * local7245_3) / 2.0F,
                7.0F * local7245_3,
                UiTheme.computeInt2(UiTheme.intVal28, UiTheme.intVal, local7245_16)
        );

        if (local7245_8 != null) {
            float rightIconSize = 7.0F * local7245_3;
            float rightIconY = local7245_2 + (btnH - rightIconSize) / 2.0F;
            UiTheme.processData3(
                    local7245_8,
                    local7245_15 + 212.0F * local7245_3 + local7245_16 * 1.5F * local7245_3,
                    rightIconY,
                    rightIconSize,
                    rightIconSize,
                    local7245_9
            );
        }

        Render2D.updateState();
        if (local7245_12 && local7245_14 > 0.7F) {
            this.list.add(new DataRecord(local7245_15, local7245_2, 226.5F * local7245_3, btnH, runnable));
        }
    }

    private void drawOnlineButton(
            float local7248_1, float local7248_2, float local7248_3, float local7248_4, float local7248_5, boolean local7248_6, Runnable runnable
    ) {
        float local7248_8 = this.slideInPC(1);
        Render2D.processData46(local7248_8);
        float local7248_9 = local7248_1 - (1.0F - local7248_8) * 16.0F * local7248_3;
        float local7248_10 = this.hoverPC(
                "onlineGame",
                local7248_6 && UiTheme.matchesCriteria(local7248_4, local7248_5, local7248_9, local7248_2, 226.5F * local7248_3, 37.0F * local7248_3)
        );
        local7248_9 += local7248_10 * 2.0F * local7248_3;
        Render2D.processData51(
                local7248_9,
                local7248_2,
                226.5F * local7248_3,
                37.0F * local7248_3,
                6.0F * local7248_3,
                UiTheme.computeInt2(UiTheme.intVal7, UiTheme.intVal8, local7248_10)
        );
        UiTheme.flag = true;
        float patW = 87.0F * local7248_3;
        float patH = 35.0F * local7248_3;
        float patX = local7248_9 + 225.5F * local7248_3 - patW;
        float patY = local7248_2 + 1.0F * local7248_3;
        Render2D.processData26(
                "assets/deluxe/textures/gui/mainmenu/icons/pattern.png",
                patX,
                patY,
                patW,
                patH,
                5.0F * local7248_3,
                1.0F,
                0.0F,
                0.0F,
                1.0F,
                0.81F,
                UiTheme.computeInt(UiTheme.intVal, 0.25F)
        );
        Render2D.processData86(local7248_9, local7248_2, 226.5F * local7248_3, 37.0F * local7248_3, 6.0F * local7248_3, 0.5F, UiTheme.intVal34);
        FontText.processData3(
                FontFace.SEMI,
                "Онлайн игра",
                local7248_9 + 8.5F * local7248_3,
                local7248_2 + 6.5F * local7248_3,
                7.0F * local7248_3,
                UiTheme.intVal10
        );
        float local7248_11 = local7248_2 + 16.5F * local7248_3;

        for (String local7248_13 : UiTheme.getList(
                FontFace.SEMI, "Подключайтесь к серверам и играйте вместе с другими.", 5.5F * local7248_3, 118.0F * local7248_3
        )) {
            FontText.processData3(
                    FontFace.SEMI,
                    local7248_13,
                    local7248_9 + 8.5F * local7248_3,
                    local7248_11,
                    5.5F * local7248_3,
                    UiTheme.computeInt(UiTheme.intVal10, 0.5F)
            );
            local7248_11 += 6.65F * local7248_3;
        }

        Render2D.updateState();
        if (local7248_6 && local7248_8 > 0.7F) {
            this.list.add(new DataRecord(local7248_9, local7248_2, 226.5F * local7248_3, 37.0F * local7248_3, runnable));
        }
    }

    private void drawChrome(float local7251_1, float local7251_2, float local7251_3, float local7251_4, float local7251_5) {
        float local7251_6 = UiTheme.computeFloat2(UiTheme.computeFloat3(this.timeMillis, this.flag3 ? 260L : 60L, 420L));
        Render2D.processData46(local7251_6);
        sg.render.ClientLogo.drawSpin3DByTime(
                Render2D.px(local7251_1 + 60.5F * local7251_3),
                Render2D.px(40.0F * local7251_3 - (1.0F - local7251_6) * 8.0F * local7251_3 + 10.0F * local7251_3),
                Render2D.px(20.0F * local7251_3),
                UiTheme.computeInt(UiTheme.intVal7, local7251_6),
                4000L
        );
        Render2D.updateState();

        float local7251_7 = UiTheme.computeFloat2(UiTheme.computeFloat3(this.timeMillis, this.flag3 ? 700L : 300L, 420L));
        Render2D.processData46(local7251_7);
        float local7251_8 = this.hoverPC(
                "socialTelegram",
                UiTheme.matchesCriteria(
                        local7251_4, local7251_5, local7251_1 + 47.5F * local7251_3, local7251_2 - 59.0F * local7251_3, 16.5F * local7251_3, 16.5F * local7251_3
                )
        );
        UiTheme.processData3(
                "assets/deluxe/textures/gui/mainmenu/icons/telegram.png",
                local7251_1 + 51.5F * local7251_3,
                local7251_2 - 50.75F * local7251_3 - local7251_8 * 1.5F * local7251_3,
                8.5F * local7251_3,
                8.5F * local7251_3,
                UiTheme.computeInt2(UiTheme.intVal28, UiTheme.intVal, local7251_8)
        );

        float local7251_9 = this.hoverPC(
                "socialDiscord",
                UiTheme.matchesCriteria(
                        local7251_4, local7251_5, local7251_1 + 65.0F * local7251_3, local7251_2 - 59.0F * local7251_3, 16.5F * local7251_3, 16.5F * local7251_3
                )
        );
        UiTheme.processData3(
                "assets/deluxe/textures/gui/mainmenu/icons/discord.png",
                local7251_1 + 69.0F * local7251_3,
                local7251_2 - 50.75F * local7251_3 - local7251_9 * 1.5F * local7251_3,
                8.5F * local7251_3,
                8.5F * local7251_3,
                UiTheme.computeInt2(UiTheme.intVal28, UiTheme.intVal, local7251_9)
        );
        Render2D.updateState();

        if (local7251_7 > 0.7F) {
            this.list.add(
                    new DataRecord(
                            local7251_1 + 47.5F * local7251_3,
                            local7251_2 - 59.0F * local7251_3,
                            16.5F * local7251_3,
                            16.5F * local7251_3,
                             () -> Util.getPlatform().openUri(COMMUNITY_URL)
                    )
            );
            this.list.add(
                    new DataRecord(
                            local7251_1 + 65.0F * local7251_3,
                            local7251_2 - 59.0F * local7251_3,
                            16.5F * local7251_3,
                            16.5F * local7251_3,
                             () -> Util.getPlatform().openUri(COMMUNITY_URL)
                    )
            );
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float delta) {
        float screenW = this.width;
        float screenH = this.height;
        float scale = Math.min(screenW / 960.0F, screenH / 540.0F);
        float centerY = (screenH - 540.0F * scale) / 2.0F;
        float mx = (float) mouseX;
        float my = (float) mouseY;

        this.list.clear();
        UiTheme.flag = true;
        Render2D.renderElement5(drawContext);

        this.animation.processData(this.flag ? 1.0 : 0.0);
        float animAlt = (float) this.animation.computeDouble();

        // Fullscreen main menu background: ад, живой зум по замкнутому циклу 60 секунд.
        double phase = (System.currentTimeMillis() % 60000L) / 60000.0 * Math.PI * 2.0;
        float photoZoom = 1.030F + 0.020F * (float) Math.sin(phase);
        float photoShiftX = (float) Math.sin(phase + 1.1) * 7.0F * scale;
        float photoShiftY = (float) Math.cos(phase * 0.7) * 5.0F * scale;
        Render2D.drawFullscreenTextureZoom("assets/deluxe/textures/gui/mainmenu/background/hell_bg.png",
                screenW, screenH, 1.0F, photoZoom, photoShiftX, photoShiftY);

        // Херобрин левитирует: плавно покачивается вверх-вниз и чуть плывёт в стороны.
        double levitation = (System.currentTimeMillis() % 8400L) / 8400.0 * Math.PI * 2.0;
        float figureHeight = screenH * 0.52F;
        float figureWidth = figureHeight * HERO_RATIO;
        float floatY = (float) Math.sin(levitation) * screenH * 0.010F;
        float floatX = (float) Math.cos(levitation * 0.5) * screenH * 0.003F;
        float breathe = 1.0F + (float) Math.sin(levitation) * 0.006F;
        float drawnHeight = figureHeight * breathe;
        float drawnWidth = figureWidth * breathe;
        Render2D.drawTextureSmooth("assets/deluxe/textures/gui/mainmenu/background/herobrine.png",
                screenW * 0.60F - drawnWidth * 0.5F + floatX,
                screenH * 0.90F - drawnHeight + floatY,
                drawnWidth, drawnHeight, -1);

        if (this.flag3) {
            float fadeWelcome = UiTheme.computeFloat2(UiTheme.computeFloat3(this.timeMillis, 100L, 600L));
            if (fadeWelcome < 0.999F) {
                UiTheme.processData6("assets/deluxe/textures/gui/mainmenu/background/welcome.png", screenW, screenH, 1.0F - fadeWelcome);
            }
        }

        // Left sidebar slide-in
        float slideProgress = UiTheme.computeFloat2(
                UiTheme.computeFloat3(this.timeMillis, this.flag3 ? 150L : 0L, this.flag3 ? 500L : 350L)
        );
        float sidebarX = -330.0F * scale * (1.0F - slideProgress);
        float sidebarW = 329.5F * scale + Math.min(0.0F, sidebarX);
        if (sidebarW > 1.0F) {
            Render2D.renderElement2(drawContext, Math.max(0.0F, sidebarX), 0.0F, sidebarW, screenH);
            UiTheme.processData6("assets/deluxe/textures/gui/mainmenu/background/hell_bg_blur.png", screenW, screenH, 1.0F);
            Render2D.renderElement(drawContext);
        }

        Render2D.processData51(
                sidebarX, 0.0F, 329.5F * scale, screenH, 0.0F, UiTheme.computeInt(UiTheme.intVal, 0.012F)
        );
        Render2D.processData51(
                sidebarX + 329.5F * scale - 0.5F * scale,
                0.0F,
                0.5F * scale,
                screenH,
                0.0F,
                UiTheme.computeInt(UiTheme.intVal, 0.03F)
        );

        Render2D.processData46(slideProgress);
        this.drawChrome(sidebarX, screenH, scale, mx, my);

        if (animAlt < 0.999F) {
            Render2D.processData46(1.0F - animAlt);
            this.drawMenuColumn(sidebarX - 36.0F * scale * animAlt, scale, centerY, mx, my, animAlt);
            Render2D.updateState();
        }

        if (animAlt > 0.001F) {
            Render2D.processData46(animAlt);
            this.accountManagerPanel.renderElement(drawContext, sidebarX + 36.0F * scale * (1.0F - animAlt), scale, centerY, mx, my);
            Render2D.updateState();
        }

        Render2D.updateState();
        if (animAlt > 0.95F) {
            this.accountManagerPanel.renderElement3(drawContext, sidebarX + 36.0F * scale * (1.0F - animAlt), scale, centerY);
        }
        Render2D.updateState6();
    }

    private float hoverPC(String local7260_1, boolean local7260_2) {
        AnimatedValue animatedValue = this.map.computeIfAbsent(local7260_1, k -> new AnimatedValue(150L));
        animatedValue.processData(local7260_2 ? 1.0 : 0.0);
        return (float) animatedValue.computeDouble();
    }

    private float slideInPC(int local7261_1) {
        return UiTheme.computeFloat2(UiTheme.computeFloat3(this.timeMillis, (this.flag3 ? 300 : 80) + local7261_1 * 70L, 450L));
    }

    private void toggleAlt() {
        this.flag = !this.flag;
        if (this.flag) {
            this.accountManagerPanel.processData2(this::toggleAlt);
        } else {
            this.accountManagerPanel.updateState();
        }
    }

    private record DataRecord(float x, float y, float w, float h, Runnable action) {
    }
}
