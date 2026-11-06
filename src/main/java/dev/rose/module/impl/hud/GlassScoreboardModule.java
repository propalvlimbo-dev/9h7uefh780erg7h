package dev.rose.module.impl.hud;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.render.Backdrop;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.render.style.Shadow;
import dev.rose.setting.BoolSetting;
import dev.rose.util.ColorUtil;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.network.chat.numbers.StyledFormat;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;


public final class GlassScoreboardModule extends Module {
   private static final Comparator<PlayerScoreEntry> ORDER = Comparator.comparing(PlayerScoreEntry::value)
      .reversed()
      .thenComparing(PlayerScoreEntry::owner, String.CASE_INSENSITIVE_ORDER);
   private final BoolSetting numbers = this.add(new BoolSetting("numbers", true));

   public GlassScoreboardModule() {
      super("glass_scoreboard", Category.HUD, false);
   }

   @Override
   public int icon() {
      return 58463;
   }

   public void render(GuiGraphics g, Objective objective) {
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      Scoreboard scoreboard = objective.getScoreboard();
      NumberFormat format = objective.numberFormatOrDefault(StyledFormat.SIDEBAR_DEFAULT);
      List<GlassScoreboardModule.Line> lines = scoreboard.listPlayerScores(objective)
         .stream()
         .filter(entry -> !entry.isHidden())
         .sorted(ORDER)
         .limit(15L)
         .map(entry -> {
            PlayerTeam team = scoreboard.getPlayersTeam(entry.owner());
            Component name = PlayerTeam.formatNameForTeam(team, entry.ownerName());
            Component score = entry.formatValue(format);
            return new GlassScoreboardModule.Line(name, score, this.numbers.isOn() ? font.width(score) : 0);
         })
         .toList();
      Component title = objective.getDisplayName();
      int titleWidth = font.width(title);
      int colon = font.width(": ");
      int width = titleWidth;

      for (GlassScoreboardModule.Line line : lines) {
         width = Math.max(width, font.width(line.name()) + (line.scoreWidth() > 0 ? colon + line.scoreWidth() : 0));
      }

      int count = lines.size();
      int lineHeight = 9;
      int bottom = g.guiHeight() / 2 + count * lineHeight / 3;
      int right = g.guiWidth() - 6;
      int left = right - width;
      int top = bottom - count * lineHeight;
      float panelX = left - 6;
      float panelY = top - lineHeight - 8;
      float panelW = width + 12;
      float panelH = bottom - panelY + 5.0F;
      Backdrop.capture();
      Glass glass = Theme.glass().copy();
      glass.tint = Paint.solid(-1710223340);
      Corners corners = Corners.themed(11.0F);
      Surfaces.shadow(g, panelX, panelY + 2.0F, panelW, panelH, corners, new Shadow(1157627904, 9.0F, 0.0F, 0.0F, 3.0F), 1.0F);
      LiquidGlass.draw(g, panelX, panelY, panelW, panelH, corners, glass, 1.0F);
      int accent = Theme.accent();
      Surfaces.fill(
         g,
         panelX + 8.0F,
         top - 3.5F,
         panelW - 16.0F,
         0.8F,
         Corners.NONE,
         ColorUtil.withAlpha(accent, 0),
         ColorUtil.withAlpha(accent, 192),
         ColorUtil.withAlpha(accent, 192),
         ColorUtil.withAlpha(accent, 0),
         1.0F
      );
      g.drawString(font, title, left + width / 2 - titleWidth / 2, top - lineHeight - 3, -1, false);

      for (int i = 0; i < count; i++) {
         GlassScoreboardModule.Line line = lines.get(i);
         int y = bottom - (count - i) * lineHeight;
         g.drawString(font, line.name(), left, y, -1, false);
         if (line.scoreWidth() > 0) {
            g.drawString(font, line.score(), right - line.scoreWidth(), y, -1, false);
         }
      }
   }

   private record Line(Component name, Component score, int scoreWidth) {
   }
}
