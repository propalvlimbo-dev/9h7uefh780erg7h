package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.setting.BoolSetting;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;


public final class SpeedModule extends InfoElement {
   private final BoolSetting vertical = this.add(new BoolSetting("vertical", false));
   private double lastX = Double.NaN;
   private double lastY;
   private double lastZ;
   private float speed;

   public SpeedModule() {
      super("speed", Anchor.TOP_LEFT, 6.0F, 68.0F, true);
   }

   @Override
   public int icon() {
      return 58297;
   }

   @Override
   protected int valueIcon() {
      return 58297;
   }

   @Override
   public void onTick() {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) {
         this.lastX = Double.NaN;
         this.speed = 0.0F;
      } else {
         if (!Double.isNaN(this.lastX)) {
            double dx = player.getX() - this.lastX;
            double dy = this.vertical.isOn() ? player.getY() - this.lastY : 0.0;
            double dz = player.getZ() - this.lastZ;
            double perTick = Math.sqrt(dx * dx + dy * dy + dz * dz);
            this.speed = perTick > 10.0 ? 0.0F : (float)(perTick * 20.0);
         }

         this.lastX = player.getX();
         this.lastY = player.getY();
         this.lastZ = player.getZ();
         this.sample(this.speed);
      }
   }

   @Override
   protected String value(HudContext hud) {
      return String.format(Locale.ROOT, "%.1f", hud.preview && this.speed == 0.0F ? 5.6F : this.speed);
   }

   @Override
   protected String unit(HudContext hud) {
      return I18n.get("rose.hud.speed.unit", new Object[0]);
   }
}
