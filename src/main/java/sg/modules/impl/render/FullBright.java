package sg.modules.impl.render;

import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public class FullBright extends ClientModule {
   private final SingleChoiceSetting Ж;
   private final FloatSetting ВЦ;
   private float А2;
   private long Ви;
   private volatile Vec3 ф;
   private volatile int еO;
   private Vec3 и;
   private int е7;
   private long В9;
   private static final String Ыу = "FullBright";
   private static final String Ы2 = "Плавно осветляет тёмные места и сохраняет атмосферу мира";
   private static final String Ыф = "Режим";
   private static final String Ыи = "Обычный";
   private static final String Ы9 = "Адаптивный";
   private static final String Ыо = "Яркость";
   private static final float ЫO = 0.05F;
   private static final String Ы7 = "Адаптивный";
   private static final float ЫР = 0.96F;
   private static final String Ы3 = "Адаптивный";
   private static final float Ыр = 15.0F;
   private static final double Ы6 = 0.01;
   private static final long Ы_ = 50000000L;
   private static final double ЫШ = 0.5;
   private static final double Ыы = 0.5;
   private static final double Ый = 0.5;
   private static final double ЫЖ = 0.5;
   private static final float Ым = 15.0F;
   private static final float ЫК = 15.0F;
   private static final float ЫI = 15.0F;
   private static final float ЫТ = 0.08F;
   private static final float ЫО = 0.76F;
   private static final float ЫФ = 0.68F;
   private static final float ЫЙ = 0.016666668F;
   private static final float Ык = 1.0E9F;
   private static final float Ыг = 0.1F;
   private static final float Ы0 = 3.4F;
   private static final float Ых = 1.55F;
   private static final float ЫУ = 0.001F;
   private static final float ЫЗ = 3.0F;
   private static final String ЫЕ = "Обычный";

   private float еэ() {
      BlockPos var1 = BlockPos.containing(MINECRAFT_CLIENT.player.getEyePosition());
      float var2 = MINECRAFT_CLIENT.level.getBrightness(LightLayer.BLOCK, var1) / Ым;
      float var3 = MINECRAFT_CLIENT.level.getBrightness(LightLayer.SKY, var1) / ЫК;
      float var4 = 1.0F - Math.clamp(MINECRAFT_CLIENT.level.getSkyDarken() / ЫI, 0.0F, 1.0F);
      float var5 = var3 * var4;
      float var6 = Math.max(var2, var5);
      float var7 = 1.0F - к(ЫТ, ЫО, var6);
      boolean var8 = !MINECRAFT_CLIENT.level.canSeeSky(var1);
      float var9 = var7 * (var8 ? 1.0F : ЫФ);
      return Math.clamp(var9, 0.0F, 1.0F);
   }

   public float б(float var1) {
      if (!this.isEnabled() || MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null) {
         this.яг();
         return var1;
      }

      if (this.Ж.isSelected(Ы7)) {
         return var1;
      }

      float var2 = this.ВЦ.getValue();
      this._ч(var2);
      float var3 = this.А2 * ЫР;
      return Math.max(var1, var3);
   }

   @Override
   public void onDisable() {
      this.я诶();
      this.яг();
   }

   public FullBright() {
      super(Ыу, Ы2, ModuleCategory.RENDER);
      this.Ж = new SingleChoiceSetting(Ыф, new ChoiceOption(Ыи), new ChoiceOption(Ы9));
      this.ВЦ = new FloatSetting(Ыо, 1.0F, 0.0F, 1.0F).withStep(ЫO).visibleWhen(() -> this.Ж.isSelected(ЫЕ));
      this.addSettings(this.Ж, this.ВЦ);
   }

   private void _ч(float var1) {
      long var2 = System.nanoTime();
      float var4 = this.Ви == 0L ? ЫЙ : Math.clamp((float)(var2 - this.Ви) / Ык, 0.0F, Ыг);
      this.Ви = var2;
      float var5 = var1 > this.А2 ? Ы0 : Ых;
      float var6 = 1.0F - (float)Math.exp(-var5 * var4);
      this.А2 = this.А2 + (var1 - this.А2) * var6;
      if (Math.abs(var1 - this.А2) < ЫУ) {
         this.А2 = var1;
      }
   }

   @Override
   public void onEnable() {
      this.яг();
   }

   private void яг() {
      this.А2 = 0.0F;
      this.Ви = 0L;
   }

   public void _3(float var1) {
      if (!this.isEnabled() || MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null) {
         this.я诶();
      } else if (!this.Ж.isSelected(Ы3)) {
         this.я诶();
      } else {
         this._ч(this.еэ());
         Vec3 var2 = MINECRAFT_CLIENT.player.getEyePosition(var1);
         int var3 = Math.clamp(Math.round(this.А2 * Ыр), 0, 15);
         this.ф = var2;
         this.еO = var3;
         long var4 = System.nanoTime();
         boolean var6 = this.и == null || var2.distanceToSqr(this.и) >= Ы6;
         boolean var7 = var3 != this.е7;
         if (this.и == null || (var6 || var7) && var4 - this.В9 >= Ы_) {
            this.е(this.и, var2, Math.max(this.е7, var3));
            this.и = var2;
            this.е7 = var3;
            this.В9 = var4;
         }
      }
   }

   public int е(int var1, BlockPos var2) {
      return this.е(var1, new Vec3(var2.getX() + ЫШ, var2.getY() + Ыы, var2.getZ() + Ый));
   }

   private void е(Vec3 var1, Vec3 var2, int var3) {
      if (var2 != null && var3 > 0 && MINECRAFT_CLIENT.levelRenderer != null) {
         int var4 = var3 + 2;
         Vec3 var5 = var1 == null ? var2 : var1;
         MINECRAFT_CLIENT.levelExtractor
            .setBlocksDirty(
               (int)Math.floor(Math.min(var5.x, var2.x)) - var4,
               (int)Math.floor(Math.min(var5.y, var2.y)) - var4,
               (int)Math.floor(Math.min(var5.z, var2.z)) - var4,
               (int)Math.floor(Math.max(var5.x, var2.x)) + var4,
               (int)Math.floor(Math.max(var5.y, var2.y)) + var4,
               (int)Math.floor(Math.max(var5.z, var2.z)) + var4
            );
      }
   }

   private void я诶() {
      Vec3 var1 = this.и;
      int var2 = this.е7;
      this.ф = null;
      this.еO = 0;
      this.и = null;
      this.е7 = 0;
      this.В9 = 0L;
      if (var1 != null && var2 > 0) {
         this.е(var1, var1, var2);
      }
   }

   private static float к(float var0, float var1, float var2) {
      float var3 = Math.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (ЫЗ - 2.0F * var3);
   }

   public int е(int var1, Vec3 var2) {
      Vec3 var3 = this.ф;
      int var4 = this.еO;
      if (var3 != null && var4 > 0) {
         double var5 = var2.distanceTo(var3);
         int var7 = Math.clamp((int)Math.floor(var4 - var5 + ЫЖ), 0, 15);
         int var8 = LightCoordsUtil.block(var1);
         if (var7 <= var8) {
            return var1;
         }

         int var9 = LightCoordsUtil.sky(var1);
         return LightCoordsUtil.pack(var7, var9);
      } else {
         return var1;
      }
   }
}
