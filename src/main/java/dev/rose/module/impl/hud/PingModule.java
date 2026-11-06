package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.render.Theme;
import dev.rose.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.language.I18n;


public final class PingModule extends InfoElement {
   private int latency = -1;

   public PingModule() {
      super("ping", Anchor.TOP_LEFT, 6.0F, 48.0F, true);
   }

   @Override
   public int icon() {
      return 57774;
   }

   @Override
   protected int valueIcon() {
      return 57774;
   }

   @Override
   public void onTick() {
      Minecraft mc = Minecraft.getInstance();
      this.latency = -1;
      if (mc.getConnection() != null && mc.player != null && !mc.hasSingleplayerServer()) {
         PlayerInfo info = mc.getConnection().getPlayerInfo(mc.player.getUUID());
         if (info != null) {
            this.latency = info.getLatency();
         }
      }

      if (this.latency >= 0) {
         this.sample(this.latency);
      }
   }

   @Override
   protected boolean hasContent(HudContext hud) {
      return this.latency >= 0;
   }

   @Override
   protected String value(HudContext hud) {
      return this.latency >= 0 ? Integer.toString(this.latency) : (hud.preview ? "42" : "—");
   }

   @Override
   protected String unit(HudContext hud) {
      return I18n.get("rose.unit.ms", new Object[0]);
   }

   @Override
   protected int tint(HudContext hud) {
      int shown = this.latency >= 0 ? this.latency : 42;
      if (shown < 80) {
         return ColorUtil.lerp(Theme.positive, hud.accent(), 0.35F);
      } else {
         return shown < 160 ? Theme.warning : Theme.danger;
      }
   }
}
