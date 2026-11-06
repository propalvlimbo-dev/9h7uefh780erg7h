package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;
import sg.events.impl.FogRenderEvent;
import sg.events.impl.HudRenderEvent;

public class HpHelper extends ClientModule {
   private static HpHelper instance;
   private static final String л = "У вас малое количество здоровья";
   @SerializedName("ч")
   private final FloatSetting healthThresholdSetting;
   private final @com.google.gson.annotations.SerializedName("ш")  FloatAnimation warningAnimation;
   private final @com.google.gson.annotations.SerializedName("н")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("Ц")  EventListener<FogRenderEvent> fogRenderListener;
   @SerializedName("ч")
   private final EventListener<HudRenderEvent> hudRenderListener;
   private static final String Выб = "HpHelper";
   private static final String ВыЫ = "Красит мир в красный, когда мало здоровья";
   private static final String Выl = "Здоровье";
   private static final float Вы8 = 6.0F;
   private static final float Вы5 = 20.0F;
   private static final float Выв = 12.0F;
   private static final float Выъ = 0.45F;
   private static final float ВыМ = 0.55F;
   private static final float ВыН = 0.45F;
   private static final float Вын = 6.0F;
   private static final float ВыЦ = 0.75F;
   private static final float Выч = 0.04F;
   private static final int ВыЭ = -16777216;
   private static final float Вы必 = 0.01F;
   private static final float Выщ = 0.82F;
   private static final float ВыГ = 0.18F;
   private static final float Выл = 280.0F;
   private static final String Выэ = "У вас малое количество здоровья";
   private static final float Выю = 18.0F;
   private static final float ВыЩ = 11.0F;
   private static final float Выу = 0.18F;
   private static final String Вы2 = "У вас малое количество здоровья";
   private static final float Выф = 200.0F;
   private static final String Выи = "У вас малое количество здоровья";
   private static final float Вы9 = 255.0F;

   @Override
   public void onDisable() {
      this.warningAnimation.setImmediately(0.0F);
   }

   public static boolean method_2西() {
      return instance != null && instance.isEnabled() && instance.method_2Д();
   }

   public static float ь_f67ab10a() {
      if (method_2西() && MINECRAFT_CLIENT.player != null) {
         float var0 = instance.healthThresholdSetting.getValue();
         float var1 = MINECRAFT_CLIENT.player.getHealth();
         float var2 = 1.0F - Mth.clamp(var1 / Math.max(1.0F, var0), 0.0F, 1.0F);
         return Mth.clamp(Выъ + var2 * ВыМ, ВыН, 1.0F);
      } else {
         return 0.0F;
      }
   }

   public static int х_a83dd0cc() {
      float var0 = ь_f67ab10a();
      int var1 = Mth.lerpInt(var0, 110, 210);
      int var2 = Mth.lerpInt(var0, 16, 4);
      int var3 = Mth.lerpInt(var0, 16, 4);
      return var1 << 16 | var2 << 8 | var3;
   }

   public HpHelper() {
      super(Выб, ВыЫ, ModuleCategory.MISC);
      this.healthThresholdSetting = new FloatSetting(Выl, Вы8, 1.0F, Вы5).withStep(1.0F);
      this.warningAnimation = new FloatAnimation(0.0F, Выв, Easings.CUBIC_OUT);
      this.playerTickListener = this::Ш;
      this.fogRenderListener = this::Й;
      this.hudRenderListener = this::method_0;
      instance = this;
      this.addSettings(this.healthThresholdSetting);
   }

   private void Ш(PlayerTickEvent var1) {
      this.warningAnimation.updateTarget(this.method_2Д() ? 1.0F : 0.0F);
   }

   private void method_0(HudRenderEvent var1) {
      this.warningAnimation.updateTarget(this.method_2Д() ? 1.0F : 0.0F);
      float var2 = this.warningAnimation.getValue();
      if (!(var2 <= Вы必)) {
         float var3 = Выщ + ВыГ * Mth.sin((float)System.currentTimeMillis() / Выл);
         float var4 = var2 * var3;
         float var5 = MINECRAFT_CLIENT.getWindow().getScreenWidth();
         float var6 = MINECRAFT_CLIENT.getWindow().getScreenHeight();
         byte var7 = 16;
         float var8 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(Выэ, var7, 0.0F);
         float var9 = UiDrawing.getCapHeight(ClientFonts.getMediumFont(), var7);
         float var10 = Выю;
         float var11 = ВыЩ;
         float var12 = var8 + var10 * 2.0F;
         float var13 = var9 + var11 * 2.0F;
         float var14 = (var5 - var12) / 2.0F;
         float var15 = var6 * Выу;
         float var16 = var14 + var10;
         float var17 = UiDrawing.alignTextCenterY(ClientFonts.getMediumFont(), Вы2, var15 + var13 / 2.0F, var7);
         UiDrawing.setTransform(new Matrix4f());
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());

         try {
            UiBatchDrawing.drawRoundedRect(var14, var15, var12, var13, var13 / 2.0F, ColorMath.packArgb(12, 4, 4, (int)(Выф * var2)));
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), Выи, var16, var17, var7, ColorMath.packArgb(255, 92, 92, (int)(Вы9 * var4)));
         } finally {
            UiBatchDrawing.endBatch(ClientFonts.getMediumFont());
         }
      }
   }

   private boolean method_2Д() {
      if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null) {
         return false;
      } else {
         return !MINECRAFT_CLIENT.player.isCreative() && !MINECRAFT_CLIENT.player.isSpectator() ? MINECRAFT_CLIENT.player.getHealth() <= this.healthThresholdSetting.getValue() : false;
      }
   }

   private void Й(FogRenderEvent var1) {
      if (this.method_2Д()) {
         float var2 = ь_f67ab10a();
         float var3 = Math.max(Вын, var1.getBaseEndDistance() * (1.0F - var2 * ВыЦ));
         var1.setDistanceOverride(var3 * Выч, var3);
         int var4 = Mth.lerpInt(var2, 130, 210);
         int var5 = Mth.lerpInt(var2, 8, 4);
         int var6 = Mth.lerpInt(var2, 8, 4);
         var1.setColorOverride(ВыЭ | var4 << 16 | var5 << 8 | var6);
      }
   }
}
