package dev.rose.module.impl.cosmetic;

import dev.rose.render.GlowEngine;
import dev.rose.render.RoseRenderTypes;
import dev.rose.render.Theme;
import dev.rose.render.world.ItemProbe;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.Weapons;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;


public final class ItemGlowModule extends CosmeticModule {
   private final NumberSetting strength = this.add(new NumberSetting("strength", 50.0, 10.0, 100.0, 5.0, "rose.unit.percent"));
   private final BoolSetting pulse = this.add(new BoolSetting("pulse", true));
   private final BoolSetting offhand = this.add(new BoolSetting("offhand", true));
   private final BoolSetting onlyWeapons = this.add(new BoolSetting("only_weapons", false));
   private final ItemProbe probe = new ItemProbe();

   public ItemGlowModule() {
      super("item_glow");
   }

   @Override
   public int icon() {
      return 57922;
   }

   public boolean wants(ItemStack stack, boolean mainHand) {
      if (!this.isEnabled() || stack.isEmpty() || !GlowEngine.active()) {
         return false;
      } else {
         return !mainHand && !this.offhand.isOn() ? false : !this.onlyWeapons.isOn() || Weapons.is(stack);
      }
   }

   public SubmitNodeCollector silhouette(SubmitNodeCollector target, boolean mainHand) {
      float time = Theme.time();
      float wave = this.pulse.isOn() ? 0.82F + 0.18F * Mth.sin(time * 2.4F + (mainHand ? 0.0F : 1.7F)) : 1.0F;
      return this.probe
         .silhouette(target, RoseRenderTypes.ITEM_SILHOUETTE, this.color(mainHand ? 0.0F : 0.5F, time, this.strength.floatValue() / 100.0F * wave));
   }
}
