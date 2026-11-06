package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import sg.ec.MinecraftAccess;
import sg.events.impl.PlayerJumpEvent;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.ClientTickEvent;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.NightixCubeGlow;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public final class CubeField extends ClientModule {
   private final SingleChoiceSetting сН;
   @SerializedName("6")
   private final ColorSetting field_6;
   private final FloatSetting еЧ;
   private final FloatSetting еЛ;
   private static final int Ъ3 = 16;
   private static final long еД_6da43b94 = 2000L;
   private static final double Я西 = 0.6;
   private static final double Яд = 2.6;
   private static final double ЯБ = 6.0;
   private static final long еЪ = 400L;
   private static final int[] Г = new int[]{0, 90, 180, 270};
   private final Random Л;
   private final List<CubeField.Cube> ВЛ;
   private final List<CubeField.Cube> В诶;
   private ClientLevel н;
   private final EventListener<ClientTickEvent> еА;
   private final EventListener<PlayerTickEvent> е4;
   private final EventListener<WorldEffectsEvent> еп;
   @SerializedName("еД")
   private final EventListener<PlayerJumpEvent> еД_f8db0d09;
   private static final String Х诶п = "CubeField";
   private static final String Х诶Д = "Кубики вокруг игрока — прыгают и крутятся";
   private static final String Х诶Ъ = "Режим цвета";
   private static final String Х诶т = "Тема";
   private static final String Х诶а = "Свой";
   private static final String Х诶Ю = "Цвет";
   private static final int Х诶Ч = -16711681;
   private static final String Х诶Л = "Кубиков";
   private static final float Х诶诶 = 3.0F;
   private static final float Х诶1 = 10.0F;
   private static final String Х诶Ь = "Размер";
   private static final float Х诶衣 = 0.18F;
   private static final float Х诶ш = 0.05F;
   private static final float Х诶И = 0.4F;
   private static final float Х诶ь = 0.01F;
   private static final double Х诶ж = 0.6;
   private static final double Х诶弟 = 2.6;
   private static final String Х诶б = "Тема";
   private static final int Х诶Ы = 16777215;
   private static final float Х诶l = 0.01F;
   private static final float Х诶8 = 90.0F;
   private static final float Х诶5 = 0.2F;
   private static final float Х诶в = 255.0F;
   private static final float Х诶ъ = 255.0F;
   private static final float Х诶М = 5.5F;
   private static final float Х诶Н = 35.0F;
   private static final float Х诶н = 1.8F;
   private static final float Х诶Ц = 40.0F;
   private static final double Х诶ч = 0.0625;
   private static final double Х诶Э = 2.0;
   private static final double Х诶必 = 0.6;
   private static final double Х诶щ = 2.6;
   private static final double Х诶Г = 16.0;
   private static final double Х诶л = 16.0;
   private static final double Х诶э = 16.0;
   private static final double Х诶ю = 16.0;
   private static final String Х诶Щ = "Свой";

   private List<BlockPos> Е(Vec3 var1, double var2, double var4, int var6) {
      ArrayList var7 = new ArrayList();
      if (MINECRAFT_CLIENT.level == null) {
         return var7;
      }

      double var8 = var1.x;
      double var10 = var1.y + 1.0;
      double var12 = var1.z;

      for (double var14 = var8 - var4; var14 < var8 + var4; var14++) {
         for (double var16 = var12 - var4; var16 < var12 + var4; var16++) {
            for (double var18 = var10 - var6; var18 < var10; var18++) {
               BlockPos var20 = BlockPos.containing(var14, var18, var16);
               if (this.Ю(var20) && !var7.contains(var20)) {
                  var7.add(var20);
               }
            }
         }
      }

      return var7;
   }

   private void 衣Е(WorldEffectsEvent var1) {
      if (this.н != MINECRAFT_CLIENT.level) {
         this.лц();
      } else if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !this.ВЛ.isEmpty()) {
         float var2 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         Camera var3 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         Vec3 var4 = var3.position();
         PoseStack var5 = var1.getPoses();
         float var6 = this.еЛ.getValue();
         int var7 = (this.сН.isSelected(Х诶б) ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.field_6.getValue()) & Х诶Ы;
         this.В诶.clear();

         for (CubeField.Cube var9 : this.ВЛ) {
            float var10 = var9.ЛЪ();
            if (!var9.М0() && !(var10 < Х诶l)) {
               double var11 = var9.И.x - var4.x;
               double var13 = var9.И.y + var9.п(var2) - var4.y;
               double var15 = var9.И.z - var4.z;
               var5.pushPose();
               var5.translate(var11, var13, var15);
               if (var9.Вф > 0) {
                  float var17 = 1.0F - Mth.lerp(var2, var9.В2, var9.Ву) / var9.Вф;
                  var5.mulPose(Axis.YP.rotationDegrees(var9.Ви));
                  var5.mulPose(Axis.XP.rotationDegrees(Х诶8 * var17));
               }

               var5.scale(var10, var10, var10);
               var9.т.set(var5.last().pose());
               var5.popPose();
               var5.pushPose();
               var5.translate(var11, var13, var15);
               var5.mulPose(var3.rotation());
               var5.scale(var10, var10, var10);
               var9.а.set(var5.last().pose());
               var5.popPose();
               var9.ЯН = var10;
               this.В诶.add(var9);
            }
         }

         if (!this.В诶.isEmpty()) {
            BufferBuilder var18 = NightixCubeGlow.method_9(false, false);

            for (CubeField.Cube var26 : this.В诶) {
               NightixCubeGlow.Ц(var18, var26.т, var6 / 2.0F, var7 | (int)(var26.ЯН * Х诶5 * Х诶в) << 24);
            }

            NightixCubeGlow.method_9(var18, false, null, false);
            var18 = NightixCubeGlow.method_9(true, false);

            for (CubeField.Cube var27 : this.В诶) {
               NightixCubeGlow.ц(var18, var27.т, var6 / 2.0F, var7 | (int)(var27.ЯН * Х诶ъ) << 24);
            }

            NightixCubeGlow.method_9(var18, true, null, false);
            var18 = NightixCubeGlow.method_9(false, true);

            for (CubeField.Cube var28 : this.В诶) {
               NightixCubeGlow.method_9(var18, var28.а, var6 * Х诶М, var7 | (int)(Х诶Н * var28.ЯН) << 24);
            }

            NightixCubeGlow.method_9(var18, false, NightixCubeGlow.field_4, false);
            var18 = NightixCubeGlow.method_9(false, true);

            for (CubeField.Cube var29 : this.В诶) {
               NightixCubeGlow.method_9(var18, var29.а, var6 * Х诶н, var7 | (int)(Х诶Ц * var29.ЯН) << 24);
            }

            NightixCubeGlow.method_9(var18, false, NightixCubeGlow.п, false);
         }
      }
   }

   public CubeField() {
      super(Х诶п, Х诶Д, ModuleCategory.RENDER);
      this.сН = new SingleChoiceSetting(Х诶Ъ, new ChoiceOption(Х诶т), new ChoiceOption(Х诶а));
      this.field_6 = new ColorSetting(Х诶Ю, false, Х诶Ч).visibleWhen(() -> this.сН.isSelected(Х诶Щ));
      this.еЧ = new FloatSetting(Х诶Л, Х诶诶, 1.0F, Х诶1).withStep(1.0F);
      this.еЛ = new FloatSetting(Х诶Ь, Х诶衣, Х诶ш, Х诶И).withStep(Х诶ь);
      this.Л = new Random();
      this.ВЛ = new ArrayList<>();
      this.В诶 = new ArrayList<>();
      this.еА = var1 -> {
         if (this.н != MINECRAFT_CLIENT.level) {
            this.лц();
            this.н = MINECRAFT_CLIENT.level;
         }
      };
      this.е4 = this::衣Ц;
      this.еп = this::衣Е;
      this.еД_f8db0d09 = var1 -> this.ВЛ.forEach(CubeField.Cube::бО);
      this.addSettings(this.сН, this.field_6, this.еЧ, this.еЛ);
   }

   @Override
   public void onDisable() {
      this.лц();
   }

   private Vec3 Е(BlockPos var1, Vec3 var2) {
      double var3 = Х诶ч;
      double var5 = var3 / Х诶Э;

      for (int var7 = 64; var7 > 0; var7--) {
         Vec3 var8 = new Vec3(var1.getX() + this.Л.nextDouble(), var1.getY() + this.Л.nextDouble(), var1.getZ() + this.Л.nextDouble());
         double var9 = var8.distanceTo(var2);
         if (var9 >= Х诶必 && var9 <= Х诶щ) {
            double var11 = Math.floor(var8.x * Х诶Г) / Х诶л + var5;
            double var13 = Math.floor(var8.y) + var5 - var3;
            double var15 = Math.floor(var8.z * Х诶э) / Х诶ю + var5;
            return new Vec3(var11, var13, var15);
         }
      }

      return null;
   }

   private boolean Ю(BlockPos var1) {
      if (MINECRAFT_CLIENT.level == null) {
         return false;
      } else if (!MINECRAFT_CLIENT.level.isEmptyBlock(var1)) {
         return false;
      } else {
         return MINECRAFT_CLIENT.level.isEmptyBlock(var1.below()) ? false : MINECRAFT_CLIENT.level.getEntities(null, new AABB(var1)).isEmpty();
      }
   }

   private void 衣Ц(PlayerTickEvent var1) {
      if (this.н != MINECRAFT_CLIENT.level) {
         this.лц();
         this.н = MINECRAFT_CLIENT.level;
      }

      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         Vec3 var2 = MINECRAFT_CLIENT.player.position();
         List var3 = this.Е(var2, Х诶ж, Х诶弟, 3);

         for (int var4 = 0; var4 < this.еЧ.getValue().intValue() && !var3.isEmpty(); var4++) {
            Vec3 var5 = this.Е((BlockPos)var3.get(this.Л.nextInt(var3.size())), var2);
            if (var5 != null) {
               this.ВЛ.add(new CubeField.Cube(var5));
            }
         }

         this.ВЛ.forEach(CubeField.Cube::бР);
         this.ВЛ.removeIf(CubeField.Cube::М0);
      }
   }

   public void лц() {
      this.ВЛ.clear();
      this.В诶.clear();
      this.н = null;
   }

   private class Cube {
      final Vec3 И;
      final long сл = System.currentTimeMillis();
      float ЯН;
      int Ву = 0;
      int В2 = 0;
      int Вф = 0;
      int Ви = 0;
      double с4 = 0.0;
      boolean с9 = false;
      long сэ = 0L;
      final Matrix4f т = new Matrix4f();
      final Matrix4f а = new Matrix4f();
      private static final double ВбУ = 6.0;
      private static final float ВбЗ = 14.0F;
      private static final float ВбЕ = 0.5F;
      private static final float ВбП = 0.5F;
      private static final double Вб西 = 16.0;
      private static final float Вбд = 2000.0F;
      private static final float ВбБ = 400.0F;
      private static final float ВбС = 0.1F;
      private static final float Вбз = 0.1F;
      private static final float Вбц = 0.8F;
      private static final float Вбя = 0.8F;
      private static final float ВЫс = 0.2F;
      private static final float ВЫВ = 0.5F;
      private static final double ВЫЯ = 2.0;
      private static final float ВЫХ = 0.01F;

      Cube(Vec3 var2) {
         this.И = var2;
      }

      void бО() {
         if (this.Ву <= 0) {
            this.Вф = this.Ву = (int)(ВбЗ * (ВбЕ + ВбП * CubeField.this.Л.nextFloat()));
            this.с4 = (2 + CubeField.this.Л.nextInt(11)) / Вб西;
            this.Ви = CubeField.Г[CubeField.this.Л.nextInt(CubeField.Г.length)];
         }
      }

      float ЛЪ() {
         float var1 = this.ЛД();
         float var2 = var1 < ВбС ? var1 / Вбз : (var1 > Вбц ? 1.0F - (var1 - Вбя) / ВЫс : 1.0F);
         return Mth.clamp(var2, 0.0F, 1.0F) * this.ЛЕ();
      }

      boolean М0() {
         return this.ЛД() >= 1.0F || this.с9 && this.ЛЕ() <= ВЫХ;
      }

      double п(float var1) {
         if (this.Вф <= 0) {
            return 0.0;
         }

         float var2 = Mth.lerp(var1, this.В2, this.Ву) / this.Вф;
         if (var2 > ВЫВ) {
            var2 = 1.0F - var2;
         }

         return var2 * ВЫЯ * this.с4;
      }

      float ЛЕ() {
         return !this.с9 ? 1.0F : Mth.clamp(1.0F - (float)(System.currentTimeMillis() - this.сэ) / ВбБ, 0.0F, 1.0F);
      }

      void бР() {
         this.В2 = this.Ву;
         if (this.Ву > 0) {
            this.Ву--;
         }

         if (MinecraftAccess.MINECRAFT_CLIENT.player != null && MinecraftAccess.MINECRAFT_CLIENT.player.position().distanceTo(this.И) > ВбУ && !this.с9) {
            this.с9 = true;
            this.сэ = System.currentTimeMillis();
         }
      }

      float ЛД() {
         return Mth.clamp((float)(System.currentTimeMillis() - this.сл) / Вбд, 0.0F, 1.0F);
      }
   }
}
