package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;

public final class FireworkESP extends ClientModule {
   private final BooleanSetting ЯЫ;
   private final BooleanSetting Яl;
   private final ColorSetting Р;
   private final FloatSetting еВ;
   private final FloatSetting еЯ;
   @SerializedName("п")
   private final Queue<Vec3> п_at_9dd21b8b;
   private final List<FireworkESP.LaunchMarker> Вп;
   private ClientLevel М;
   private final EventListener<PacketReceiveEvent> ХФ;
   private final EventListener<PlayerTickEvent> ХЙ;
   private final EventListener<HudRenderEvent> Хк;
   private static final String сЮЭ = "FireworkESP";
   private static final String сЮ必 = "Показывает места запуска фейерверков";
   private static final String сЮщ = "Показывать время";
   private static final String сЮГ = "Цвет темы";
   private static final String сЮл = "Цвет";
   private static final int сЮэ = -9905665;
   private static final String сЮю = "Время метки";
   private static final float сЮЩ = 8.0F;
   private static final float сЮу = 3.0F;
   private static final float сЮ2 = 20.0F;
   private static final float сЮф = 0.5F;
   private static final String сЮи = "Радиус объединения";
   private static final float сЮ9 = 5.0F;
   private static final float сЮо = 12.0F;
   private static final float сЮO = 0.5F;
   private static final float сЮ7 = 1000.0F;
   private static final float сЮР = 1000.0F;
   private static final double сЮ3 = 0.5;
   private static final float сЮр = 0.002F;
   private static final String сЮ6 = "%.1fс";
   private static final double сЮ_ = 1000.0;
   private static final float сЮШ = 0.92F;
   private static final float сЮы = 0.08F;
   private static final float сЮй = 16.0F;
   private static final float сЮЖ = 28.0F;
   private static final float сЮм = 6.0F;
   private static final float сЮК = 5.0F;
   private static final float сЮI = 12.0F;
   private static final float сЮТ = 25.0F;
   private static final float сЮО = 10.0F;
   private static final float сЮФ = 0.5F;
   private static final float сЮЙ = 0.5F;
   private static final float сЮк = 4.0F;
   private static final float сЮг = 4.0F;
   private static final float сЮ0 = 14.0F;
   private static final float сЮх = 145.0F;
   private static final float сЮУ = 10.0F;
   private static final float сЮЗ = 180.0F;
   private static final float сЮЕ = 7.0F;
   private static final float сЮП = 205.0F;
   private static final float сЮ西 = 0.5F;
   private static final float сЮд = 6.0F;
   private static final float сЮБ = 0.5F;
   private static final float сЮС = 255.0F;
   private static final float сЮз = 5.0F;
   private static final float сЮц = 0.5F;
   private static final float сЮя = 255.0F;
   private static final float сЧс = 0.0025F;
   private static final float сЧВ = 0.5F;
   private static final long сЧЯ = 420L;

   public FireworkESP() {
      super(сЮЭ, сЮ必, ModuleCategory.RENDER);
      this.ЯЫ = new BooleanSetting(сЮщ, true);
      this.Яl = new BooleanSetting(сЮГ, true);
      this.Р = new ColorSetting(сЮл, false, сЮэ).visibleWhen(() -> !this.Яl.getValue());
      this.еВ = new FloatSetting(сЮю, сЮЩ, сЮу, сЮ2).withStep(сЮф);
      this.еЯ = new FloatSetting(сЮи, сЮ9, 1.0F, сЮо).withStep(сЮO);
      this.п_at_9dd21b8b = new ConcurrentLinkedQueue<>();
      this.Вп = new ArrayList<>();
      this.ХФ = this::У;
      this.ХЙ = this::Ып;
      this.Хк = this::Ыы;
      this.addSettings(this.ЯЫ, this.Яl, this.Р, this.еВ, this.еЯ);
   }

   private void щ(Vec3 var1) {
      double var2 = this.еЯ.getValue() * this.еЯ.getValue();
      long var4 = System.currentTimeMillis();

      for (FireworkESP.LaunchMarker var7 : this.Вп) {
         if (var4 - var7.Х0 < var7.Хх && var7.д.distanceToSqr(var1) <= var2) {
            var7.о((long)(this.еВ.getValue() * сЮ7));
            return;
         }
      }

      this.Вп.add(new FireworkESP.LaunchMarker(var1, (long)(this.еВ.getValue() * сЮР)));
   }

   private int Б(int var1, int var2) {
      return ColorMath.packArgb(ColorMath.getRed(var1), ColorMath.getGreen(var1), ColorMath.getBlue(var1), Mth.clamp(var2, 0, 255));
   }

   private void У(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundSoundPacket var2) {
         if (var2.getSound().value() == SoundEvents.FIREWORK_ROCKET_LAUNCH) {
            this.п_at_9dd21b8b.add(new Vec3(var2.getX(), var2.getY(), var2.getZ()));
         }
      }
   }

   private void Ыы(HudRenderEvent var1) {
      if (var1.getGraphics() != null && !this.Вп.isEmpty() && MINECRAFT_CLIENT.player != null) {
         UiDrawing.setTransform(new Matrix4f());
         long var2 = System.currentTimeMillis();

         for (FireworkESP.LaunchMarker var5 : this.Вп) {
            Vector2f var6 = WorldProjection.projectToScreen(var5.д.add(0.0, сЮ3, 0.0));
            if (var6 != null) {
               float var7 = var5.В(var2);
               if (!(var7 <= сЮр)) {
                  float var8 = var5.З(var2);
                  int var9 = this.о(var5, var2);
                  String var10 = String.format(Locale.ROOT, сЮ6, Math.max(0.0, (var5.Хх - (var2 - var5.Х0)) / сЮ_));
                  String var11 = this.ЯЫ.getValue() ? var10 : "";
                  byte var12 = 11;
                  float var13 = сЮШ + сЮы * var8;
                  float var14 = сЮй * var13;
                  float var15 = var11.isEmpty() ? 0.0F : UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var11, var12);
                  float var16 = var11.isEmpty() ? сЮЖ : сЮм + var14 + сЮК + var15 + сЮI;
                  float var17 = сЮТ;
                  float var18 = (1.0F - var8) * сЮО;
                  float var19 = var6.x - var16 * сЮФ;
                  float var20 = var6.y - var17 * сЮЙ + var18;
                  UiDrawing.drawShadow(var19 - 2.0F, var20 - 2.0F, var16 + сЮк, var17 + сЮг, сЮ0, this.Б(var9, Math.round(сЮх * var7)));
                  UiDrawing.drawShadow(var19, var20, var16, var17, сЮУ, ColorMath.packArgb(0, 0, 0, Math.round(сЮЗ * var7)));
                  UiDrawing.drawRoundedRect(var19, var20, var16, var17, сЮЕ, ColorMath.packArgb(8, 13, 22, Math.round(сЮП * var7)));
                  float var21 = var11.isEmpty() ? var19 + (var16 - var14) * сЮ西 : var19 + сЮд;
                  float var22 = var20 + (var17 - var14) * сЮБ;
                  UiDrawing.setGlobalAlpha(var7 * сЮС);

                  try {
                     UiDrawing.drawItem(Items.FIREWORK_ROCKET.getDefaultInstance(), var21, var22, var13, false);
                  } finally {
                     UiDrawing.resetGlobalAlpha();
                  }

                  if (!var11.isEmpty()) {
                     float var23 = var21 + var14 + сЮз;
                     float var24 = UiDrawing.alignCapHeightCenterY(ClientFonts.getMediumFont(), var20 + var17 * сЮц, var12);
                     UiDrawing.drawText(ClientFonts.getMediumFont(), var11, var23, var24, var12, ColorMath.packArgb(238, 247, 255, Math.round(сЮя * var7)));
                  }
               }
            }
         }
      }
   }

   private void Ып(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         if (this.М != MINECRAFT_CLIENT.level) {
            this.МШ();
            this.М = MINECRAFT_CLIENT.level;
         }

         Vec3 var2;
         while ((var2 = this.п_at_9dd21b8b.poll()) != null) {
            this.щ(var2);
         }

         long var3 = System.currentTimeMillis();
         this.Вп.removeIf(var2x -> var3 - var2x.Х0 > var2x.Хх + сЧЯ);
      } else {
         this.МШ();
      }
   }

   private int о(FireworkESP.LaunchMarker var1, long var2) {
      if (!this.Яl.getValue()) {
         return this.Р.getValue();
      }

      float var4 = (Mth.sin((float)(var2 - var1.Х0) * сЧс) + 1.0F) * сЧВ;
      return ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO), var4);
   }

   @Override
   public void onDisable() {
      this.МШ();
   }

   private void МШ() {
      this.п_at_9dd21b8b.clear();
      this.Вп.clear();
      this.М = null;
   }

   private static final class LaunchMarker {
      private final Vec3 д;
      private long Х0 = System.currentTimeMillis();
      private long Хх;
      private static final float ЫЪ = 260.0F;
      private static final double Ыт = 3.0;
      private static final float Ыа = 420.0F;

      private float В(long var1) {
         long var3 = var1 - this.Х0;
         if (var3 <= this.Хх) {
            return this.З(var1);
         }

         float var5 = 1.0F - Mth.clamp((float)(var3 - this.Хх) / Ыа, 0.0F, 1.0F);
         return var5 * var5;
      }

      private LaunchMarker(Vec3 var1, long var2) {
         this.д = var1;
         this.Хх = var2;
      }

      private float З(long var1) {
         float var3 = Mth.clamp((float)(var1 - this.Х0) / ЫЪ, 0.0F, 1.0F);
         return 1.0F - (float)Math.pow(1.0F - var3, Ыт);
      }

      private void о(long var1) {
         this.Х0 = System.currentTimeMillis();
         this.Хх = var1;
      }
   }
}
