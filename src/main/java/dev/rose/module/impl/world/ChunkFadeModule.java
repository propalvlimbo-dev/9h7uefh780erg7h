package dev.rose.module.impl.world;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;


public final class ChunkFadeModule extends Module {
   private final float[] frame = new float[3];
   private final ChoiceSetting<ChunkFadeModule.Style> style = this.add(new ChoiceSetting<>("style", ChunkFadeModule.Style.FOG));
   private final NumberSetting duration = this.add(new NumberSetting("duration", 0.6, 0.1, 3.0, 0.05, "rose.unit.seconds"));
   private final NumberSetting spread = this.add(new NumberSetting("spread", 0.5, 0.0, 2.0, 0.05, "rose.unit.seconds"));
   private final BoolSetting nearby = this.add(new BoolSetting("nearby", false));

   public ChunkFadeModule() {
      super("chunk_fade", Category.WORLD, true);
   }

   @Override
   public int icon() {
      return 58618;
   }

   @Override
   public boolean hasKeybind() {
      return false;
   }

   public float progress(long shownAt, long now) {
      return shownAt == 0L ? 0.0F : Mth.clamp((float)(now - shownAt) / 1.0E9F / Math.max(0.05F, this.duration.floatValue()), 0.0F, 1.0F);
   }

   public float[] frame(float progress) {
      float t = progress * progress * (3.0F - 2.0F * progress);
      ChunkFadeModule.Style current = this.style.get();
      this.frame[0] = current == ChunkFadeModule.Style.GLASS ? 0.0F : 1.0F - Math.min(1.0F, t * 1.25F);

      this.frame[1] = switch (current) {
         case DUSK -> Mth.lerp(Math.min(1.0F, t * 1.4F), 0.45F, 1.0F);
         case SHINE -> Mth.lerp(Math.min(1.0F, t * 1.4F), 1.6F, 1.0F);
         default -> 1.0F;
      };
      this.frame[2] = current == ChunkFadeModule.Style.GLASS ? t : 1.0F;
      return this.frame;
   }

   public float delay(double distanceSqr) {
      double blocks = Math.sqrt(distanceSqr);
      float far = Math.max(64, Minecraft.getInstance().options.getEffectiveRenderDistance() * 16);
      return (float)Mth.clamp(blocks / far, 0.0, 1.0) * this.spread.floatValue();
   }

   public boolean fades(double distanceSqr) {
      return this.nearby.isOn() || distanceSqr > 576.0;
   }

   public boolean active() {
      Minecraft mc = Minecraft.getInstance();
      return this.isEnabled() && mc.level != null && !mc.isPaused();
   }

   public double totalSeconds() {
      return Math.max(0.05, this.duration.get()) + this.spread.get();
   }

   public float visibility(float vanilla, double distanceSqr) {
      float elapsed = vanilla * (float)this.totalSeconds();
      float progress = Mth.clamp((elapsed - this.delay(distanceSqr)) / Math.max(0.05F, this.duration.floatValue()), 0.0F, 1.0F);
      return progress * progress * (3.0F - 2.0F * progress);
   }

   public float encode(float t) {
      if (t >= 1.0F) {
         return 1.0F;
      }

      return switch ((ChunkFadeModule.Style)this.style.get()) {
         case FOG -> Math.min(1.0F, t * 1.25F);
         case DUSK -> 2.0F + t;
         case SHINE -> 4.0F + t;
         case GLASS -> 6.0F + t;
      };
   }

   public enum Style {
      FOG,
      DUSK,
      SHINE,
      GLASS;
   }
}
