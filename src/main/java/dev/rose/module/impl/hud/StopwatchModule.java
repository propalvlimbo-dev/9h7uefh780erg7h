package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.setting.ActionSetting;
import dev.rose.util.ColorUtil;
import java.util.Locale;


public final class StopwatchModule extends InfoElement {
   private long startedAt = -1L;
   private long accumulated;

   public StopwatchModule() {
      super("stopwatch", Anchor.BOTTOM_RIGHT, -6.0F, -92.0F, false);
      this.add(new ActionSetting("reset", 57910, this::reset));
   }

   @Override
   public int icon() {
      return 57824;
   }

   @Override
   protected int valueIcon() {
      return this.running() ? 57660 : 57646;
   }

   private boolean running() {
      return this.startedAt >= 0L;
   }

   public boolean isRunning() {
      return this.isEnabled() && this.running();
   }

   public String elapsed() {
      long millis = this.accumulated + (this.running() ? System.currentTimeMillis() - this.startedAt : 0L);
      return String.format(Locale.ROOT, "%02d:%02d.%d", millis / 60000L, millis / 1000L % 60L, millis / 100L % 10L);
   }

   @Override
   public void onKeyBind() {
      if (!this.isEnabled()) {
         this.setEnabled(true);
      }

      if (this.running()) {
         this.accumulated = this.accumulated + (System.currentTimeMillis() - this.startedAt);
         this.startedAt = -1L;
      } else {
         this.startedAt = System.currentTimeMillis();
      }
   }

   private void reset() {
      this.accumulated = 0L;
      this.startedAt = this.running() ? System.currentTimeMillis() : -1L;
   }

   @Override
   protected String value(HudContext hud) {
      return this.elapsed();
   }

   @Override
   protected int tint(HudContext hud) {
      return this.running() ? hud.accent() : ColorUtil.fade(hud.label, 0.6F);
   }
}
