package dev.rose.module.impl.world;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.NumberSetting;
import java.util.concurrent.ThreadLocalRandom;


public final class ParticlesModule extends Module {
   private final NumberSetting density = this.add(new NumberSetting("density", 60.0, 5.0, 100.0, 5.0, "rose.unit.percent"));
   private final BoolSetting blockBreak = this.add(new BoolSetting("block_break", true));
   private final BoolSetting blockCrack = this.add(new BoolSetting("block_crack", true));

   public ParticlesModule() {
      super("particles", Category.WORLD, false);
   }

   @Override
   public int icon() {
      return 58494;
   }

   public boolean skip() {
      return this.isEnabled() && ThreadLocalRandom.current().nextFloat() * 100.0F >= this.density.floatValue();
   }

   public boolean hideBlockBreak() {
      return this.isEnabled() && !this.blockBreak.isOn();
   }

   public boolean hideBlockCrack() {
      return this.isEnabled() && !this.blockCrack.isOn();
   }
}
