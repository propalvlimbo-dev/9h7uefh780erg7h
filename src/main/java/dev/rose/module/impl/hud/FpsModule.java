package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import net.minecraft.client.Minecraft;


public final class FpsModule extends InfoElement {
   public FpsModule() {
      super("fps", Anchor.TOP_LEFT, 6.0F, 28.0F, true);
   }

   @Override
   public int icon() {
      return 57400;
   }

   @Override
   protected int valueIcon() {
      return 57400;
   }

   @Override
   public void onTick() {
      this.sample(Minecraft.getInstance().getFps());
   }

   @Override
   protected String value(HudContext hud) {
      return Integer.toString(hud.mc.getFps());
   }

   @Override
   protected String unit(HudContext hud) {
      return "fps";
   }
}
