package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.GameStats;
import dev.rose.hud.HudContext;
import dev.rose.setting.BoolSetting;


public final class CpsModule extends InfoElement {
   private final BoolSetting right = this.add(new BoolSetting("right", false));

   public CpsModule() {
      super("cps", Anchor.TOP_LEFT, 6.0F, 88.0F, true);
   }

   @Override
   public int icon() {
      return 57632;
   }

   @Override
   protected int valueIcon() {
      return 57632;
   }

   @Override
   public void onTick() {
      this.sample(GameStats.cps(true));
   }

   @Override
   protected String value(HudContext hud) {
      int left = GameStats.cps(true);
      if (hud.preview && left == 0) {
         left = 9;
      }

      return this.right.isOn() ? left + " | " + GameStats.cps(false) : Integer.toString(left);
   }

   @Override
   protected String unit(HudContext hud) {
      return "CPS";
   }
}
