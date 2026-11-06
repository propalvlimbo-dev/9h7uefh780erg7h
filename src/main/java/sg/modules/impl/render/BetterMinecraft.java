package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import sg.events.impl.UiAnimationEvents;
import sg.events.EventListener;
import sg.events.impl.ChatMouseReleaseEvent;
import sg.events.impl.ChatClosedEvent;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiBatchDrawing;
import sg.settings.BooleanSetting;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.ChatMouseButtonEvent;
import sg.settings.ChoiceOption;
import sg.modules.impl.init.ChunkRevealAnimation;
import sg.settings.FloatSetting;
import sg.hud.HudElementRegistry;
import sg.events.impl.HudRenderEvent;
import sg.settings.MultiChoiceSetting;
import sg.hud.Scoreboard;
import sg.settings.SectionHeaderSetting;
import sg.settings.SingleChoiceSetting;

public class BetterMinecraft extends ClientModule {
   @SerializedName("А")
   private final SectionHeaderSetting animationsSection;
   @SerializedName("8")
   public final MultiChoiceSetting animatedElementsSetting;
   private final @com.google.gson.annotations.SerializedName("ЯС")  FloatSetting animationSpeedSetting;
   @SerializedName("4")
   private final SectionHeaderSetting renderElementsSection;
   public final @com.google.gson.annotations.SerializedName("Вн")  BooleanSetting chunkAnimationSetting;
   public final @com.google.gson.annotations.SerializedName("Яз")  FloatSetting chunkAnimationDurationSetting;
   public final @com.google.gson.annotations.SerializedName("сВ")  SingleChoiceSetting chunkDirectionSetting;
   public final @com.google.gson.annotations.SerializedName("сЯ")  SingleChoiceSetting chunkEasingSetting;
   public final @com.google.gson.annotations.SerializedName("В")  ChunkRevealAnimation chunkRevealAnimation;
   @SerializedName("п")
   private final SectionHeaderSetting cameraSection;
   public final @com.google.gson.annotations.SerializedName("ВЦ")  BooleanSetting customCameraSetting;
   private final @com.google.gson.annotations.SerializedName("Яц")  FloatSetting cameraDistanceSetting;
   public final @com.google.gson.annotations.SerializedName("Яя")  FloatSetting cameraOffsetXSetting;
   public final @com.google.gson.annotations.SerializedName("Вч")  BooleanSetting cameraThroughWallsSetting;
   private final @com.google.gson.annotations.SerializedName("ВЭ")  BooleanSetting smoothCameraSetting;
   private final @com.google.gson.annotations.SerializedName("Хс")  FloatSetting cameraSmoothingSpeedSetting;
   @SerializedName("п")
   private Entity cameraTargetEntity;
   private @com.google.gson.annotations.SerializedName("Хь")  long lastCameraUpdateNanos;
   private @com.google.gson.annotations.SerializedName("ДЧ")  float cameraSmoothingFactor;
   @SerializedName("Я")
   private Vec2 smoothedCameraRotation;
   @SerializedName("к")
   private Vec3 smoothedCameraPosition;
   private @com.google.gson.annotations.SerializedName("Х3")  boolean customCameraActive;
   @SerializedName("5")
   public final MultiChoiceSetting hudElementsSetting;
   private final @com.google.gson.annotations.SerializedName("Д")  SectionHeaderSetting hudSection;
   private final @com.google.gson.annotations.SerializedName("В必")  BooleanSetting hudDisplacementSetting;
   private final @com.google.gson.annotations.SerializedName("Вщ")  BooleanSetting customScoreboardPlacementSetting;
   private final @com.google.gson.annotations.SerializedName("Яю")  EventListener<HudRenderEvent.HotbarRender> hotbarRenderListener;
   private final @com.google.gson.annotations.SerializedName("ЯЩ")  EventListener<HudRenderEvent> scoreboardHudRenderListener;
   private final @com.google.gson.annotations.SerializedName("Яу")  EventListener<HudRenderEvent.GuiRender> guiRenderListener;
   private final @com.google.gson.annotations.SerializedName("Я2")  EventListener<ChatMouseButtonEvent> chatMouseButtonListener;
   private final @com.google.gson.annotations.SerializedName("Яф")  EventListener<ChatMouseReleaseEvent> chatMouseReleaseListener;
   private final @com.google.gson.annotations.SerializedName("Яи")  EventListener<ChatClosedEvent> chatClosedListener;
   private final @com.google.gson.annotations.SerializedName("Я9")  EventListener<UiAnimationEvents.InventoryAnimationEvent> inventoryAnimationListener;
   private final @com.google.gson.annotations.SerializedName("Яо")  EventListener<UiAnimationEvents.PlayerListAnimationEvent> playerListAnimationListener;
   private final @com.google.gson.annotations.SerializedName("ЯO")  EventListener<HudRenderEvent> playerListHudRenderListener;
   private final @com.google.gson.annotations.SerializedName("Я7")  EventListener<UiAnimationEvents.PerspectiveAnimationEvent> perspectiveAnimationListener;
   private final @com.google.gson.annotations.SerializedName("ЯР")  EventListener<UiAnimationEvents.ScoreboardPlacementQuery> scoreboardPlacementListener;
   private final @com.google.gson.annotations.SerializedName("Я3")  EventListener<UiAnimationEvents.HudDisplacementQuery> hudDisplacementListener;
   private static final float ДЛ = -0.1F;
   private static final float Д诶 = 1.1F;
   private final @com.google.gson.annotations.SerializedName("с衣")  FloatAnimation playerListAnimation;
   private final @com.google.gson.annotations.SerializedName("сш")  FloatAnimation inventoryAnimation;
   private @com.google.gson.annotations.SerializedName("Хр")  boolean playerListStillRendering;
   private @com.google.gson.annotations.SerializedName("Х6")  boolean lastPlayerListRequestedVisible;
   private static final String ЯЛш = "Better Minecraft";
   private static final String ЯЛИ = "Улучшает майнкрафт";
   private static final String ЯЛь = "Анимации";
   private static final String ЯЛж = "Что анимировать";
   private static final String ЯЛ弟 = "Список игроков";
   private static final String ЯЛб = "Инвентарь";
   private static final String ЯЛЫ = "Изменение перспективы";
   private static final String ЯЛl = "Скорость анимации";
   private static final float ЯЛ8 = 0.2F;
   private static final float ЯЛ5 = 0.08F;
   private static final float ЯЛв = 0.45F;
   private static final float ЯЛъ = 0.01F;
   private static final String ЯЛМ = "Элементы рендера";
   private static final String ЯЛН = "Анимация чанков";
   private static final String ЯЛн = "Длительность анимации чанков";
   private static final float ЯЛЦ = 1000.0F;
   private static final float ЯЛч = 100.0F;
   private static final float ЯЛЭ = 5000.0F;
   private static final float ЯЛ必 = 100.0F;
   private static final String ЯЛщ = "Направление чанков";
   private static final String ЯЛГ = "Снизу";
   private static final String ЯЛл = "Сверху";
   private static final String ЯЛэ = "От горизонта";
   private static final String ЯЛю = "От игрока";
   private static final String ЯЛЩ = "По направлению";
   private static final String ЯЛу = "Интерполяция чанков";
   private static final String ЯЛ2 = "Linear";
   private static final String ЯЛф = "Quad";
   private static final String ЯЛи = "Cubic";
   private static final String ЯЛ9 = "Quart";
   private static final String ЯЛо = "Quint";
   private static final String ЯЛO = "Expo";
   private static final String ЯЛ7 = "Sine";
   private static final String ЯЛР = "Circ";
   private static final String ЯЛ3 = "Back";
   private static final String ЯЛр = "Bounce";
   private static final String ЯЛ6 = "Elastic";
   private static final String ЯЛ_ = "Камера";
   private static final String ЯЛШ = "Своя камера";
   private static final String ЯЛы = "Дистанция камеры";
   private static final float ЯЛй = 3.0F;
   private static final float ЯЛЖ = 10.0F;
   private static final float ЯЛм = 0.5F;
   private static final String ЯЛК = "Смещение камеры по X";
   private static final float ЯЛI = -2.0F;
   private static final float ЯЛТ = 0.05F;
   private static final String ЯЛО = "Камера сквозь стены";
   private static final String ЯЛФ = "Плавная камера";
   private static final String ЯЛЙ = "Скорость плавности камеры";
   private static final float ЯЛк = 8.0F;
   private static final float ЯЛг = 20.0F;
   private static final String ЯЛ0 = "Что изменять";
   private static final String ЯЛх = "Хотбар";
   private static final String ЯЛУ = "HUD";
   private static final String ЯЛЗ = "Сдвиг от таба и боссбара";
   private static final String ЯЛЕ = "Менять Положение ScoreBoard";
   private static final float ЯЛП = 5.0F;
   private static final float ЯЛ西 = 12.0F;
   private static final double ЯЛд = 1.0E9;
   private static final double ЯЛБ = 0.1;
   private static final double ЯЛС = 256.0;
   private static final float ЯЛз = 4.0F;
   private static final String ЯЛц = "Инвентарь";
   private static final String ЯЛя = "Список игроков";
   private static final float Я诶с = 0.001F;
   private static final float Я诶В = 0.001F;
   private static final float Я诶Я = 1.1F;
   private static final String Я诶Х = "Список игроков";
   private static final float Я诶е = 0.001F;
   private static final String Я诶А = "Изменение перспективы";
   private static final String Я诶4 = "Хотбар";
   private static final float Я诶п = 182.0F;
   private static final float Я诶Д = 364.0F;
   private static final float Я诶Ъ = 44.0F;
   private static final float Я诶т = 12.0F;
   private static final float Я诶а = 364.0F;
   private static final float Я诶Ю = 44.0F;
   private static final float Я诶Ч = 24.0F;
   private static final float Я诶Л = 6.0F;
   private static final float Я诶诶 = 182.0F;
   private static final float Я诶1 = 3.0F;
   private static final float Я诶Ь = 3.0F;
   private static final float Я诶衣 = 38.0F;
   private static final float Я诶ш = 38.0F;
   private static final float Я诶И = 5.0F;
   private static final double Я诶ь = 150.0;
   private static final float Я诶ж = 180.0F;
   private static final float Я诶弟 = 4.0F;
   private static final float Я诶б = 6.0F;
   private static final float Я诶Ы = 32.0F;
   private static final float Я诶l = 32.0F;
   private static final float Я诶8 = 182.0F;
   private static final float Я诶5 = 52.0F;
   private static final float Я诶в = 6.0F;
   private static final float Я诶ъ = 32.0F;
   private static final float Я诶М = 32.0F;
   private static final float Я诶Н = 182.0F;
   private static final float Я诶н = 20.0F;
   private static final float Я诶Ц = 6.0F;
   private static final float Я诶ч = 32.0F;
   private static final float Я诶Э = 32.0F;
   private static final float Я诶必 = 0.5F;
   private static final float Я诶щ = -0.1F;
   private static final float Я诶Г = 1.1F;
   private static final float Я诶л = 1.0E-4F;
   private static final float Я诶э = 1.1F;
   private static final float Я诶ю = 1.0E-4F;
   private static final float Я诶Щ = 1.0E9F;
   private static final float Я诶у = 3.0F;
   private static final String Я诶2 = "Изменение перспективы";

   private void Аь() {
      Scoreboard var1 = Ь();
      if (var1 != null) {
         var1.ШК();
         var1.Шъ();
      }
   }

   private void Ь(UiAnimationEvents.ScoreboardPlacementQuery var1) {
      var1.setCustomPlacementEnabled(this.customScoreboardPlacementSetting.getValue());
   }

   private void Ь(UiAnimationEvents.PerspectiveAnimationEvent var1) {
      if (this.animatedElementsSetting.isSelected(Я诶А)) {
         var1.setAnimationEnabled(true);
         var1.setDurationNanos(this.衣_6da43b94());
         var1.setEasedProgress(this.н5(var1.getProgress()));
      }
   }

   private boolean Пl() {
      return this.customScoreboardPlacementSetting.getValue();
   }

   private static Scoreboard Ь() {
      try {
         HudElementRegistry var0 = RoseClient.getInstance().getServices().getHudElements();
         return var0 == null ? null : var0.getScoreboard();
      } catch (Throwable var1) {
         return Scoreboard.必_791f1767();
      }
   }

   private void Ь(UiAnimationEvents.PlayerListAnimationEvent var1) {
      if (this.animatedElementsSetting.isSelected(ЯЛя)) {
         if (var1.isRequestedVisible() && !this.playerListStillRendering) {
            this.playerListStillRendering = true;
         }

         float var2 = this.Ь(var1.isRequestedVisible());
         if (!var1.isRequestedVisible() && var2 <= Я诶с && this.playerListAnimation.isFinished()) {
            this.playerListStillRendering = false;
         }

         var1.setAnimationEnabled(true);
         var1.setScale(Mth.clamp(var2, Я诶В, Я诶Я));
      }
   }

   public void Ъ(Entity var1) {
      boolean var2 = this.isEnabled()
         && this.customCameraSetting.getValue()
         && MINECRAFT_CLIENT.level != null
         && var1 != null
         && !MINECRAFT_CLIENT.options.getCameraType().isFirstPerson()
         && !RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled();
      if (!var2) {
         this.А1();
      } else {
         long var3 = System.nanoTime();
         if (!this.customCameraActive || this.cameraTargetEntity != var1) {
            this.А1();
            this.cameraTargetEntity = var1;
         }

         this.customCameraActive = true;
         double var5 = this.lastCameraUpdateNanos == 0L ? 0.0 : Math.min((var3 - this.lastCameraUpdateNanos) / ЯЛд, ЯЛБ);
         this.cameraSmoothingFactor = (float)(1.0 - Math.exp(-this.cameraSmoothingSpeedSetting.getValue() * var5));
         this.lastCameraUpdateNanos = var3;
      }
   }

   private void А1() {
      this.customCameraActive = false;
      this.cameraTargetEntity = null;
      this.smoothedCameraRotation = null;
      this.smoothedCameraPosition = null;
      this.lastCameraUpdateNanos = 0L;
   }

   private static boolean ПР() {
      try {
         HudElementRegistry var0 = RoseClient.getInstance().getServices().getHudElements();
         return var0 != null && var0.isEnabled();
      } catch (Throwable var1) {
         return false;
      }
   }

   private float Ь(boolean var1) {
      if (this.lastPlayerListRequestedVisible != var1) {
         float var2 = this.playerListAnimation.getValue();
         float var3 = Mth.clamp(var2, Я诶щ, Я诶Г);
         if (Math.abs(var2 - var3) > Я诶л) {
            this.playerListAnimation.setImmediately(var3);
         }

         this.lastPlayerListRequestedVisible = var1;
      }

      this.playerListAnimation.updateTarget(var1 ? 1.0F : 0.0F);
      return Mth.clamp(this.playerListAnimation.getValue(), 0.0F, Я诶э);
   }

   public float нэ(float var1) {
      return !this.customCameraActive ? var1 : var1 * this.cameraDistanceSetting.getValue() / ЯЛз;
   }

   private void Ь(UiAnimationEvents.HudDisplacementQuery var1) {
      var1.setDisplacementEnabled(this.hudDisplacementSetting.getValue());
   }

   public boolean ПЗ() {
      return this.customCameraActive;
   }

   @Override
   public void onDisable() {
      this.А1();
      this.chunkRevealAnimation.Б6();
      this.playerListAnimation.setImmediately(0.0F);
      this.inventoryAnimation.setImmediately(1.0F);
      this.playerListStillRendering = false;
      this.lastPlayerListRequestedVisible = false;
      this.АА();
      Scoreboard var1 = Ь();
      if (var1 != null) {
         var1.ШР();
      }
   }

   public Vec3 А(Vec3 var1) {
      if (!this.customCameraActive) {
         return var1;
      }

      if (this.smoothedCameraPosition != null && this.smoothCameraSetting.getValue() && !(this.smoothedCameraPosition.distanceToSqr(var1) > ЯЛС)) {
         this.smoothedCameraPosition = this.smoothedCameraPosition.lerp(var1, this.cameraSmoothingFactor);
      } else {
         this.smoothedCameraPosition = var1;
      }

      return this.smoothedCameraPosition;
   }

   private void Ь(ChatMouseButtonEvent var1) {
      if (!var1.isCancelled() && this.Пl()) {
         Scoreboard var2 = Ь();
         if (var2 != null) {
            if (var2.ш(var1.getMouseX(), var1.getMouseY(), var1.getButton())) {
               var1.setCancelled(true);
            } else if (!ПР()) {
               if (var2.р(var1.getMouseX(), var1.getMouseY(), var1.getButton())) {
                  var1.setCancelled(true);
               }
            }
         }
      }
   }

   private float н5(float var1) {
      float var2 = Mth.clamp(var1, 0.0F, 1.0F);
      return var2 * var2 * (Я诶у - 2.0F * var2);
   }

   private void Щ(HudRenderEvent var1) {
      if (this.animatedElementsSetting.isSelected(Я诶Х) && this.playerListStillRendering && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.options != null && MINECRAFT_CLIENT.gui.hud != null) {
         if (!MINECRAFT_CLIENT.options.keyPlayerList.isDown()) {
            float var2 = this.Ь(false);
            if (var2 <= Я诶е && this.playerListAnimation.isFinished()) {
               this.playerListStillRendering = false;
            } else {
               net.minecraft.world.scores.Scoreboard var3 = MINECRAFT_CLIENT.level.getScoreboard();
               Objective var4 = var3.getDisplayObjective(DisplaySlot.LIST);
               MINECRAFT_CLIENT.gui.hud.getTabList().extractRenderState(var1.getGraphics(), MINECRAFT_CLIENT.getWindow().getGuiScaledWidth(), var3, var4);
            }
         }
      }
   }

   private void Ь(HudRenderEvent.HotbarRender var1) {
      if (this.hudElementsSetting.isSelected(Я诶4)) {
         float var2 = MINECRAFT_CLIENT.getWindow().getGuiScale();
         float var3 = MINECRAFT_CLIENT.getWindow().getScreenWidth() / 2;
         float var4 = MINECRAFT_CLIENT.getWindow().getScreenHeight() - 44;
         float var5 = var3 - Я诶п;
         ItemStack var6 = MINECRAFT_CLIENT.player.getOffhandItem();
         HumanoidArm var7 = MINECRAFT_CLIENT.player.getMainArm().getOpposite();
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());
         UiBatchDrawing.drawShadow(var5, var4, Я诶Д, Я诶Ъ, Я诶т, ColorMath.packRgb(0, 0, 0));
         UiBatchDrawing.drawBlur(var5, var4, Я诶а, Я诶Ю, Я诶Ч, Я诶Л, -1);
         UiBatchDrawing.drawRoundedRect(
            var3 - Я诶诶 + MINECRAFT_CLIENT.player.getInventory().getSelectedSlot() * 40 + Я诶1, var4 + Я诶Ь, Я诶衣, Я诶ш, Я诶И, ColorMath.packArgb(20, 20, 20, Я诶ь)
         );

         for (int var8 = 0; var8 < 9; var8++) {
            float var9 = var3 - Я诶ж + var8 * 40 + Я诶弟;
            UiBatchDrawing.drawItemWithLegacyOverlay(var9, var4 + Я诶б, Я诶Ы, Я诶l, MINECRAFT_CLIENT.player.getInventory().getItem(var8), true);
         }

         if (!var6.isEmpty()) {
            if (var7 == HumanoidArm.LEFT) {
               UiBatchDrawing.drawItemWithLegacyOverlay(var3 - Я诶8 - Я诶5, var4 + Я诶в, Я诶ъ, Я诶М, var6, true);
            } else {
               UiBatchDrawing.drawItemWithLegacyOverlay(var3 + Я诶Н + Я诶н, var4 + Я诶Ц, Я诶ч, Я诶Э, var6, true);
            }
         }

         UiBatchDrawing.endBatch(ClientFonts.getMediumFont());
         var1.setCancelled(true);
      }
   }

   private void Аж() {
      this.inventoryAnimation.setImmediately(Я诶必);
      this.inventoryAnimation.updateTarget(1.0F);
   }

   public Vec2 м(float var1, float var2) {
      if (!this.customCameraActive) {
         return new Vec2(var1, var2);
      }

      if (this.smoothedCameraRotation != null && this.smoothCameraSetting.getValue()) {
         this.smoothedCameraRotation = new Vec2(
            this.smoothedCameraRotation.x + Mth.wrapDegrees(var1 - this.smoothedCameraRotation.x) * this.cameraSmoothingFactor, Mth.lerp(this.cameraSmoothingFactor, this.smoothedCameraRotation.y, var2)
         );
      } else {
         this.smoothedCameraRotation = new Vec2(var1, var2);
      }

      return this.smoothedCameraRotation;
   }

   private float Ь6() {
      return Ь(this.inventoryAnimation, 1.0F);
   }

   private static float Ь(FloatAnimation var0, float var1) {
      if (Math.abs(var0.getValue() - var1) > Я诶ю) {
         var0.updateTarget(var1);
      }

      return var0.getValue();
   }

   public BetterMinecraft() {
      super(ЯЛш, ЯЛИ, ModuleCategory.RENDER);
      this.animationsSection = new SectionHeaderSetting(ЯЛь);
      this.animatedElementsSetting = new MultiChoiceSetting(ЯЛж, new ChoiceOption(ЯЛ弟, false), new ChoiceOption(ЯЛб, false), new ChoiceOption(ЯЛЫ, false));
      this.animationSpeedSetting = new FloatSetting(ЯЛl, ЯЛ8, ЯЛ5, ЯЛв).withStep(ЯЛъ).visibleWhen(() -> this.animatedElementsSetting.isSelected(Я诶2));
      this.renderElementsSection = new SectionHeaderSetting(ЯЛМ);
      this.chunkAnimationSetting = new BooleanSetting(ЯЛН, false);
      this.chunkAnimationDurationSetting = new FloatSetting(ЯЛн, ЯЛЦ, ЯЛч, ЯЛЭ).withStep(ЯЛ必).visibleWhen(this.chunkAnimationSetting::getValue);
      this.chunkDirectionSetting = new SingleChoiceSetting(
            ЯЛщ, new ChoiceOption(ЯЛГ), new ChoiceOption(ЯЛл), new ChoiceOption(ЯЛэ), new ChoiceOption(ЯЛю), new ChoiceOption(ЯЛЩ)
         )
         .visibleWhen(this.chunkAnimationSetting::getValue);
      this.chunkEasingSetting = new SingleChoiceSetting(
            ЯЛу,
            6,
            new ChoiceOption(ЯЛ2),
            new ChoiceOption(ЯЛф),
            new ChoiceOption(ЯЛи),
            new ChoiceOption(ЯЛ9),
            new ChoiceOption(ЯЛо),
            new ChoiceOption(ЯЛO),
            new ChoiceOption(ЯЛ7),
            new ChoiceOption(ЯЛР),
            new ChoiceOption(ЯЛ3),
            new ChoiceOption(ЯЛр),
            new ChoiceOption(ЯЛ6)
         )
         .visibleWhen(this.chunkAnimationSetting::getValue);
      this.chunkRevealAnimation = new ChunkRevealAnimation();
      this.cameraSection = new SectionHeaderSetting(ЯЛ_);
      this.customCameraSetting = new BooleanSetting(ЯЛШ, false);
      this.cameraDistanceSetting = new FloatSetting(ЯЛы, ЯЛй, 1.0F, ЯЛЖ).withStep(ЯЛм).visibleWhen(this.customCameraSetting::getValue);
      this.cameraOffsetXSetting = new FloatSetting(ЯЛК, 0.0F, ЯЛI, 2.0F).withStep(ЯЛТ).visibleWhen(this.customCameraSetting::getValue);
      this.cameraThroughWallsSetting = new BooleanSetting(ЯЛО, true).visibleWhen(this.customCameraSetting::getValue);
      this.smoothCameraSetting = new BooleanSetting(ЯЛФ, true).visibleWhen(this.customCameraSetting::getValue);
      this.cameraSmoothingSpeedSetting = new FloatSetting(ЯЛЙ, ЯЛк, 1.0F, ЯЛг).withStep(1.0F).visibleWhen(() -> this.customCameraSetting.getValue() && this.smoothCameraSetting.getValue());
      this.cameraSmoothingFactor = 1.0F;
      this.hudElementsSetting = new MultiChoiceSetting(ЯЛ0, new ChoiceOption(ЯЛх, false));
      this.hudSection = new SectionHeaderSetting(ЯЛУ);
      this.hudDisplacementSetting = new BooleanSetting(ЯЛЗ, true);
      this.customScoreboardPlacementSetting = new BooleanSetting(ЯЛЕ, false);
      this.hotbarRenderListener = this::Ь;
      this.scoreboardHudRenderListener = this::ъ;
      this.guiRenderListener = var1 -> this.Аь();
      this.chatMouseButtonListener = this::Ь;
      this.chatMouseReleaseListener = var1 -> this.АА();
      this.chatClosedListener = var1 -> this.АА();
      this.inventoryAnimationListener = this::Ь;
      this.playerListAnimationListener = this::Ь;
      this.playerListHudRenderListener = this::Щ;
      this.perspectiveAnimationListener = this::Ь;
      this.scoreboardPlacementListener = this::Ь;
      this.hudDisplacementListener = this::Ь;
      this.playerListAnimation = new FloatAnimation(0.0F, ЯЛП, Easings.BACK_IN_OUT);
      this.inventoryAnimation = new FloatAnimation(1.0F, ЯЛ西, Easings.CUBIC_OUT);
      this.addSettings(
         this.animationsSection,
         this.animatedElementsSetting,
         this.animationSpeedSetting,
         this.chunkAnimationSetting,
         this.chunkAnimationDurationSetting,
         this.chunkDirectionSetting,
         this.chunkEasingSetting,
         this.cameraSection,
         this.customCameraSetting,
         this.cameraDistanceSetting,
         this.cameraOffsetXSetting,
         this.cameraThroughWallsSetting,
         this.smoothCameraSetting,
         this.cameraSmoothingSpeedSetting,
         this.renderElementsSection,
         this.hudElementsSetting,
         this.hudSection,
         this.hudDisplacementSetting,
         this.customScoreboardPlacementSetting
      );
   }

   private void АА() {
      Scoreboard var1 = Ь();
      if (var1 != null) {
         var1.У_();
      }
   }

   private long 衣_6da43b94() {
      return (long)(this.animationSpeedSetting.getValue() * Я诶Щ);
   }

   private void Ь(UiAnimationEvents.InventoryAnimationEvent var1) {
      if (this.animatedElementsSetting.isSelected(ЯЛц)) {
         var1.setAnimationEnabled(true);
         if (var1.getPhase() == UiAnimationEvents.InventoryAnimationEvent.Phase.START) {
            this.Аж();
            var1.setScale(this.inventoryAnimation.getValue());
         } else {
            var1.setScale(this.Ь6());
         }
      }
   }

   private void ъ(HudRenderEvent var1) {
      Scoreboard var2 = Ь();
      if (var2 != null) {
         var2.Шь();
         if (!ПР()) {
            var2.к(var1.getMouseX(), var1.getMouseY());
         }
      }
   }
}
