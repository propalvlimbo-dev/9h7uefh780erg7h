package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.music.NowPlaying;
import dev.rose.render.Theme;
import dev.rose.render.font.SdfFont;
import dev.rose.setting.ActionSetting;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.KeySetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;


public final class MusicModule extends HudElement {
   private final ChoiceSetting<MusicModule.Style> style = this.add(new ChoiceSetting<>("style", MusicModule.Style.CARD));
   private final ChoiceSetting<NowPlaying.Source> source = this.add(new ChoiceSetting<>("source", NowPlaying.Source.ANY));
   private final NumberSetting maxWidth = this.add(new NumberSetting("max_width", 150.0, 80.0, 260.0, 5.0, "rose.unit.pixels"));
   private final BoolSetting showPaused = this.add(new BoolSetting("show_paused", true));
   private final BoolSetting equalizer = this.add(new BoolSetting("equalizer", true));
   private final BoolSetting scroll = this.add(new BoolSetting("scroll", true));
   private final KeySetting playPauseKey;
   private final KeySetting nextKey;
   private final KeySetting previousKey;
   private final ActionSetting playPause;
   private final ActionSetting next;
   private final ActionSetting previous;
   private static final NowPlaying.Track SAMPLE = new NowPlaying.Track("Rose", "Liquid Glass", true, "Spotify");

   public MusicModule() {
      super("music", false, Anchor.TOP_LEFT, 6.0F, 40.0F);
      this.section("controls", 57660);
      this.playPauseKey = this.add(new KeySetting("play_pause_key", -1));
      this.nextKey = this.add(new KeySetting("next_key", -1));
      this.previousKey = this.add(new KeySetting("previous_key", -1));
      this.playPause = this.add(new ActionSetting("play_pause", 57646, NowPlaying::playPause));
      this.next = this.add(new ActionSetting("next", 58016, NowPlaying::next));
      this.previous = this.add(new ActionSetting("previous", 58017, NowPlaying::previous));
   }

   @Override
   public int icon() {
      return 57634;
   }

   @Override
   public void onRawKey(int key) {
      if (key != -1) {
         if (key == this.playPauseKey.get()) {
            NowPlaying.playPause();
         } else if (key == this.nextKey.get()) {
            NowPlaying.next();
         } else if (key == this.previousKey.get()) {
            NowPlaying.previous();
         }
      }
   }

   public NowPlaying.Track track() {
      return this.isEnabled() ? NowPlaying.current(this.source.get()) : NowPlaying.Track.NONE;
   }

   private NowPlaying.Track track(HudContext hud) {
      if (hud.preview) {
         NowPlaying.Track live = NowPlaying.current(this.source.get());
         return live.present() ? live : SAMPLE;
      } else {
         return NowPlaying.current(this.source.get());
      }
   }

   @Override
   protected boolean hasContent(HudContext hud) {
      NowPlaying.Track track = this.track(hud);
      return track.present() && (track.playing() || this.showPaused.isOn());
   }

   @Override
   protected float defaultRoundness() {
      return this.style.get() == MusicModule.Style.COMPACT ? 1.0F : 0.45F;
   }

   @Override
   protected Size measure(HudContext hud) {
      NowPlaying.Track track = this.track(hud);
      float limit = this.maxWidth.floatValue();
      if (this.style.get() == MusicModule.Style.COMPACT) {
         float text = hud.semibold.width(line(track), 7.2F);
         return new Size(Math.min(limit, 19.0F + text + (this.equalizer.isOn() ? 16 : 0) + 7.0F), 16.0F);
      } else {
         float text = Math.max(hud.semibold.width(title(track), 7.6F), hud.regular.width(artist(track), 6.4F));
         return new Size(Math.min(limit, 41.0F + text + (this.equalizer.isOn() ? 18 : 0) + 8.0F), 40.0F);
      }
   }

   private static String title(NowPlaying.Track track) {
      return track.title();
   }

   private static String artist(NowPlaying.Track track) {
      if (!track.playing()) {
         return I18n.get("rose.hud.music.paused", new Object[0]);
      } else {
         return track.artist().isEmpty() ? track.app() : track.artist();
      }
   }

   private static String line(NowPlaying.Track track) {
      return !track.artist().isEmpty() && track.playing() ? track.artist() + " — " + track.title() : track.title();
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      NowPlaying.Track track = this.track(hud);
      float time = Theme.time();
      float playing = hud.animate("music:playing", track.playing() ? 1.0F : 0.0F, 0.3, 1.0);
      if (this.style.get() == MusicModule.Style.COMPACT) {
         hud.icon(57634, 12.0F, h / 2.0F, 8.5F, hud.accent());
         float textX = 19.0F;
         float textW = w - textX - 7.0F - (this.equalizer.isOn() ? 16 : 0);
         this.marquee(hud, line(track), textX, HudContext.centered(0.0F, h, 7.2F), textW, 7.2F, hud.semibold, hud.label, time);
         if (this.equalizer.isOn()) {
            this.bars(hud, w - 7.0F - 12.0F, h / 2.0F, 12.0F, 8.0F, playing, time);
         }
      } else {
         int seed = track.title().hashCode();
         int first = ColorUtil.lerp(ColorUtil.hsb((seed & 0xFF) / 255.0F, 0.55F, 0.85F, 255), hud.accent(), 0.35F);
         int second = ColorUtil.hsb((seed >> 8 & 0xFF) / 255.0F, 0.6F, 0.55F, 255);
         float cover = 28.0F;
         float coverX = 6.0F;
         float coverY = (h - cover) / 2.0F;
         hud.fill(coverX, coverY, cover, cover, 7.0F, second);
         hud.fill(coverX + 2.0F, coverY + 2.0F, cover - 4.0F, cover - 4.0F, 6.0F, ColorUtil.withAlpha(first, 176));
         float pulse = 1.0F + 0.08F * playing * Mth.sin(time * 6.0F);
         hud.icon(track.playing() ? 58186 : 57646, coverX + cover / 2.0F, coverY + cover / 2.0F, 11.0F * pulse, -1);
         float textX = coverX + cover + 7.0F;
         float textW = w - textX - 8.0F - (this.equalizer.isOn() ? 18 : 0);
         this.marquee(hud, title(track), textX, h / 2.0F - 10.0F, textW, 7.6F, hud.semibold, hud.label, time);
         this.marquee(hud, artist(track), textX, h / 2.0F + 1.5F, textW, 6.4F, hud.regular, hud.secondary, time + 1.3F);
         if (this.equalizer.isOn()) {
            this.bars(hud, w - 8.0F - 13.0F, h / 2.0F, 13.0F, 12.0F, playing, time);
         }
      }
   }

   private void marquee(HudContext hud, String text, float x, float y, float width, float size, SdfFont font, int color, float time) {
      float full = font.width(text, size);
      if (!(full <= width) && this.scroll.isOn()) {
         float travel = full - width + 12.0F;
         float cycle = 2.5F + travel / 18.0F;
         float t = time % (cycle * 2.0F) / cycle;
         float shift = t < 1.0F ? Mth.clamp((t - 0.25F) / 0.6F, 0.0F, 1.0F) : Mth.clamp(1.0F - (t - 1.25F) / 0.6F, 0.0F, 1.0F);
         hud.clip(x, y - 2.0F, width, size + 4.0F);
         hud.text(font, text, x - shift * travel, y, size, color);
         hud.unclip();
      } else {
         String shown = text;
         if (full > width) {
            while (shown.length() > 1 && font.width(shown + "…", size) > width) {
               shown = shown.substring(0, shown.length() - 1);
            }

            shown = shown + "…";
         }

         hud.text(font, shown, x, y, size, color);
      }
   }

   private void bars(HudContext hud, float x, float centerY, float w, float h, float playing, float time) {
      int count = 4;
      float gap = 1.6F;
      float barW = (w - gap * (count - 1)) / count;

      for (int i = 0; i < count; i++) {
         float wave = 0.3F
            + 0.7F
               * Math.abs(Mth.sin(time * (4.1F + i * 1.3F) + i * 1.7F))
               * (0.6F + 0.4F * Math.abs(Mth.sin(time * 1.7F + i)));
         float barH = Math.max(2.0F, h * Mth.lerp(playing, 0.18F, wave));
         hud.fill(x + i * (barW + gap), centerY + h / 2.0F - barH, barW, barH, barW / 2.0F, ColorUtil.lerp(hud.accent(), -1, i * 0.12F));
      }
   }

   public enum Style {
      CARD,
      COMPACT;
   }
}
