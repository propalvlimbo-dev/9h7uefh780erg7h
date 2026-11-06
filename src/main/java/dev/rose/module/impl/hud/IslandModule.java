package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.module.Modules;
import dev.rose.music.NowPlaying;
import dev.rose.notify.Notifications;
import dev.rose.render.Theme;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.util.ColorUtil;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;


public final class IslandModule extends HudElement {
   private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");
   private static final float PAD = 9.0F;
   private static final float SMALL = 7.2F;
   private static final float TITLE = 7.5F;
   private static final float SUBTITLE = 6.5F;
   private static final float MAX_TEXT = 190.0F;
   private static final float MAX_TRACK = 118.0F;
   private static final NowPlaying.Track SAMPLE = new NowPlaying.Track("Rose", "Liquid Glass", true, "");
   private final ChoiceSetting<IslandModule.Idle> idle = this.add(new ChoiceSetting<>("idle", IslandModule.Idle.CLOCK_FPS));
   private final BoolSetting notices = this.add(new BoolSetting("notices", true));
   private final BoolSetting music = this.add(new BoolSetting("music", true));
   private final BoolSetting timer = this.add(new BoolSetting("timer", true));
   private final BoolSetting victories = this.add(new BoolSetting("victories", true));
   private String momentTitle = "";
   private String momentSubtitle = "";
   private int momentIcon;
   private int momentColor;
   private float momentUntil = -1.0F;
   private IslandModule.Scene drawn = IslandModule.Scene.IDLE;
   private String drawnKey = "";
   private String title = "";
   private String subtitle = "";
   private String leading = "";
   private String trailing = "";
   private int icon;
   private int color;
   private float remaining = -1.0F;
   private float fade = 1.0F;
   private float lastFrame;
   private String idleLeft = "";
   private String idleRight = "";

   public IslandModule() {
      super("island", true, Anchor.TOP, 0.0F, 5.0F);
      this.sizeSpring(0.4, 0.7);
   }

   @Override
   public int icon() {
      return 58181;
   }

   @Override
   protected float defaultRoundness() {
      return 1.0F;
   }

   public boolean showsNotices() {
      return this.isEnabled() && this.notices.isOn();
   }

   public void victory(Entity entity) {
      if (this.isEnabled() && this.victories.isOn() && entity != null) {
         this.moment(58036, Theme.positive, I18n.get("rose.island.victory", new Object[0]), entity.getName().getString(), 3.2F);
      }
   }

   public void moment(int icon, int color, String title, String subtitle, float seconds) {
      this.momentIcon = icon;
      this.momentColor = color;
      this.momentTitle = title;
      this.momentSubtitle = subtitle == null ? "" : subtitle;
      this.momentUntil = Theme.time() + seconds;
   }

   public float reservedTop(float screenWidth) {
      if (!this.isEnabled()) {
         return 0.0F;
      } else {
         float[] b = this.bounds();
         if (!(b[2] <= 0.0F) && !(b[1] > 40.0F)) {
            float center = b[0] + b[2] / 2.0F;
            return Math.abs(center - screenWidth / 2.0F) < 110.0F ? b[1] + b[3] : 0.0F;
         } else {
            return 0.0F;
         }
      }
   }

   @Override
   public void onTick() {
      Minecraft mc = Minecraft.getInstance();
      String clock = LocalTime.now().format(TIME);
      String fps = mc.getFps() + " fps";
      switch ((IslandModule.Idle)this.idle.get()) {
         case CLOCK_FPS:
            this.idleLeft = clock;
            this.idleRight = fps;
            break;
         case CLOCK:
            this.idleLeft = clock;
            this.idleRight = "";
            break;
         case FPS_PING:
            this.idleLeft = fps;
            this.idleRight = "";
            if (mc.getConnection() != null && mc.player != null && !mc.hasSingleplayerServer()) {
               PlayerInfo info = mc.getConnection().getPlayerInfo(mc.player.getUUID());
               if (info != null) {
                  this.idleRight = info.getLatency() + " " + I18n.get("rose.unit.ms", new Object[0]);
               }
            }
            break;
         case HIDDEN:
            this.idleLeft = this.idleRight = "";
      }
   }

   private void update(HudContext hud) {
      float now = Theme.time();
      float delta = Math.min(0.1F, now - this.lastFrame);
      if (!(delta < 4.0E-4F)) {
         this.lastFrame = now;
         Notifications.Shown notice = this.notices.isOn() && !hud.preview ? Notifications.shown() : null;
         NowPlaying.Track track = !this.music.isOn()
            ? NowPlaying.Track.NONE
            : (hud.preview ? SAMPLE : (Modules.music() != null ? Modules.music().track() : NowPlaying.Track.NONE));
         StopwatchModule stopwatch = Modules.stopwatch();
         IslandModule.Scene wanted;
         String key;
         if (notice != null) {
            wanted = IslandModule.Scene.NOTICE;
            key = "notice:" + notice.title() + "|" + notice.subtitle();
         } else if (now < this.momentUntil) {
            wanted = IslandModule.Scene.MOMENT;
            key = "moment:" + this.momentTitle + "|" + this.momentSubtitle;
         } else if (this.timer.isOn() && stopwatch != null && stopwatch.isRunning()) {
            wanted = IslandModule.Scene.TIMER;
            key = "timer";
         } else if (track.present() && track.playing()) {
            wanted = IslandModule.Scene.MUSIC;
            key = "music:" + track.title();
         } else {
            wanted = IslandModule.Scene.IDLE;
            key = "idle";
         }

         boolean same = key.equals(this.drawnKey);
         this.fade = Mth.clamp(this.fade + (same ? 1 : -1) * delta / (same ? 0.16F : 0.09F), 0.0F, 1.0F);
         if (!same && this.fade <= 0.001F) {
            this.drawn = wanted;
            this.drawnKey = key;
         }

         switch (this.drawn) {
            case IDLE:
               this.leading = this.idleLeft;
               this.trailing = this.idleRight;
               this.color = hud.accent();
               break;
            case MUSIC:
               if (track.present()) {
                  this.title = track.title();
                  this.subtitle = track.artist();
               }

               this.color = hud.accent();
               break;
            case TIMER:
               this.title = stopwatch == null ? "" : stopwatch.elapsed();
               this.subtitle = "";
               this.icon = 57824;
               this.color = hud.accent();
               break;
            case MOMENT:
               this.title = this.momentTitle;
               this.subtitle = this.momentSubtitle;
               this.color = this.momentColor;
               this.icon = this.momentIcon;
               this.remaining = -1.0F;
               break;
            case NOTICE:
               if (notice != null) {
                  this.title = notice.title();
                  this.subtitle = notice.subtitle();
                  this.color = notice.color();
                  this.remaining = notice.remaining();
               }

               this.icon = 0;
         }
      }
   }

   @Override
   protected boolean hasContent(HudContext hud) {
      this.update(hud);
      return this.drawn != IslandModule.Scene.IDLE || this.idle.get() != IslandModule.Idle.HIDDEN || hud.preview;
   }

   @Override
   protected Size measure(HudContext hud) {
      this.update(hud);

      return switch (this.drawn) {
         case IDLE -> {
            float left = hud.semibold.width(this.leading, 7.2F);
            float right = this.trailing.isEmpty() ? 0.0F : 9.0F + hud.regular.width(this.trailing, 7.2F);
            yield new Size(19.0F + left + right + 9.0F, 16.0F);
         }
         case MUSIC -> new Size(28.0F + Math.min(hud.medium.width(this.title, 7.2F), 118.0F) + 9.0F, 18.0F);
         case TIMER -> new Size(23.0F + hud.semibold.width(this.title, 7.5F) + 9.0F, 17.0F);
         case MOMENT, NOTICE -> {
            float text = Math.max(hud.semibold.width(this.title, 7.5F), this.subtitle.isEmpty() ? 0.0F : hud.regular.width(this.subtitle, 6.5F));
            yield new Size(25.0F + Math.min(text, 190.0F) + 9.0F + 3.0F, this.subtitle.isEmpty() ? 20.0F : 30.0F);
         }
      };
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      float outer = hud.alpha;
      hud.alpha = outer * this.fade * this.fade;
      hud.clip(0.0F, 0.0F, w, h);
      switch (this.drawn) {
         case IDLE:
            this.resting(hud, h);
            break;
         case MUSIC:
            this.track(hud, w, h);
            break;
         case TIMER:
            this.stopwatch(hud, h);
            break;
         case MOMENT:
         case NOTICE:
            this.message(hud, w, h);
      }

      hud.unclip();
      hud.alpha = outer;
   }

   private void resting(HudContext hud, float h) {
      float cursor = 9.0F;
      float y = HudContext.centered(0.0F, h, 7.2F);
      hud.fill(cursor, (h - 5.0F) / 2.0F, 5.0F, 5.0F, 2.5F, this.color);
      cursor += 10.0F;
      cursor += hud.text(hud.semibold, this.leading, cursor, y, 7.2F, hud.label);
      if (!this.trailing.isEmpty()) {
         hud.text(hud.regular, this.trailing, cursor + 9.0F, y, 7.2F, hud.secondary);
      }
   }

   private void track(HudContext hud, float w, float h) {
      float time = Theme.time();
      float base = h / 2.0F + 5.0F;

      for (int i = 0; i < 4; i++) {
         float level = 0.5F + 0.5F * Mth.sin(time * (5.2F + i * 1.3F) + i * 1.9F);
         float bar = 3.0F + 7.0F * level * level;
         hud.fill(9.0F + i * 3.4F, base - bar, 2.0F, bar, 1.0F, this.color);
      }

      float x = 28.0F;
      float room = w - x - 9.0F;
      float width = hud.medium.width(this.title, 7.2F);
      float shift = 0.0F;
      if (width > room) {
         float range = width - room;
         float period = range * 2.0F + 56.0F;
         float phase = time * 13.0F % period;
         shift = phase < 28.0F ? 0.0F : (phase < 28.0F + range ? phase - 28.0F : (phase < 56.0F + range ? range : period - phase));
      }

      hud.clip(x, 0.0F, room, h);
      hud.text(hud.medium, this.title, x - shift, HudContext.centered(0.0F, h, 7.2F), 7.2F, hud.label);
      hud.unclip();
   }

   private void stopwatch(HudContext hud, float h) {
      hud.icon(this.icon, 13.5F, h / 2.0F, 9.0F, this.color);
      hud.text(hud.semibold, this.title, 23.0F, HudContext.centered(0.0F, h, 7.5F), 7.5F, hud.label);
   }

   private void message(HudContext hud, float w, float h) {
      float center = h / 2.0F;
      float cursor = 9.0F;
      if (this.icon != 0) {
         hud.icon(this.icon, cursor + 4.5F, center, 10.0F, this.color);
      } else {
         hud.fill(cursor + 1.0F, center - 3.5F, 7.0F, 7.0F, 3.5F, this.color);
      }

      cursor += 16.0F;
      if (this.subtitle.isEmpty()) {
         hud.text(hud.semibold, this.title, cursor, HudContext.centered(0.0F, h, 7.5F), 7.5F, hud.label);
      } else {
         hud.text(hud.semibold, this.title, cursor, center - 7.5F - 0.5F, 7.5F, hud.label);
         hud.text(hud.regular, this.subtitle, cursor, center + 1.0F, 6.5F, hud.secondary);
      }

      if (this.remaining >= 0.0F) {
         hud.fill(h / 2.0F, h - 2.6F, (w - h) * this.remaining, 1.2F, 0.6F, ColorUtil.withAlpha(this.color, 153));
      }
   }

   public enum Idle {
      CLOCK_FPS,
      CLOCK,
      FPS_PING,
      HIDDEN;
   }

   private enum Scene {
      IDLE,
      MUSIC,
      TIMER,
      MOMENT,
      NOTICE;
   }
}
