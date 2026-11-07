package sg.gui.manager.now;

import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.resources.Identifier;
import sg.render.ClientLogo;
import sg.render.pipeline.UiDrawing;

/**
 * Эндер-кристаллы на обсидиановых столбах «питают» логотип Rose:
 * от каждого кристалла к знаку идёт луч, по нему бегут сгустки энергии,
 * логотип подсвечивается и вспыхивает, когда энергия доходит.
 * Всё рисуется кодом, поэтому анимация живая и не требует видео.
 */
public final class EndShrine {
   private static final String DIR = "assets/deluxe/textures/gui/mainmenu/background/";
   private static final Identifier GLOW = Render2D.toIdentifier(DIR + "glow.png");
   private static final Identifier CRYSTAL = Render2D.toIdentifier(DIR + "crystal.png");

   private static final float LOGO_CX = 0.655F;
   private static final float LOGO_CY = 0.345F;
   private static final float LOGO_H = 0.215F;

   private static final int RUBY = 0xE4294B;
   private static final int BRIGHT = 0xFF6B85;
   private static final int PINK = 0xFF74D9;
   private static final int WHITE = 0xFFF2FB;

   /** x кристалла, y кристалла, y основания, полуширина столба — в долях экрана. */
   private static final float[][] PILLARS = {
      {0.455F, 0.600F, 0.930F, 0.0125F},
      {0.875F, 0.600F, 0.930F, 0.0125F},
      {0.545F, 0.665F, 0.955F, 0.0145F},
      {0.780F, 0.665F, 0.955F, 0.0145F},
      {0.660F, 0.705F, 0.975F, 0.0170F}
   };

   private EndShrine() {
   }

   public static void render(float screenW, float screenH, float scale) {
      long now = System.currentTimeMillis();
      float time = (float) (now % 1_000_000L);
      float logoCx = LOGO_CX * screenW;
      float logoCy = LOGO_CY * screenH;
      float logoH = LOGO_H * screenH;

      // столбы и кристаллы (дальние — раньше)
      for (int i = 0; i < PILLARS.length; i++) {
         float[] p = PILLARS[i];
         pillar(screenW, screenH, p[0] * screenW, p[1] * screenH, p[2] * screenH, p[3] * screenW, i, time);
      }

      // лучи от кристаллов к знаку
      float charge = 0.0F;
      for (int i = 0; i < PILLARS.length; i++) {
         float[] p = PILLARS[i];
         float crystalX = p[0] * screenW;
         float crystalY = p[1] * screenH - screenH * 0.024F;
         float halfW = p[3] * screenW;
         charge += beam(crystalX, crystalY, logoCx, logoCy, halfW, i, time);
      }

      charge = Math.clamp(charge, 0.0F, 1.0F);
      aura(logoCx, logoCy, logoH, time, charge);
      logo(logoCx, logoCy, logoH, time, charge);
   }

   private static void pillar(float screenW, float screenH, float x, float crystalY, float baseY, float halfW, int index, float time) {
      float sway = 0.55F + 0.45F * (float) Math.sin(time * 0.0018F + index * 1.7F);
      // подсветка у основания и парящая тень
      glow(x, baseY - screenH * 0.008F, halfW * 3.2F, PINK, 0.16F * sway + 0.06F);
      // тело столба: тень, тело, светлая грань, кромка
      UiDrawing.drawRoundedRect(px(x - halfW * 1.16F), px(crystalY), px(halfW * 2.32F), px(baseY - crystalY), px(halfW * 0.5F), argb(0x08030C, 0.85F));
      UiDrawing.drawRoundedRect(px(x - halfW), px(crystalY), px(halfW * 2.0F), px(baseY - crystalY), px(halfW * 0.45F), argb(0x190B22, 1.0F));
      UiDrawing.drawRoundedRect(px(x - halfW), px(crystalY), px(halfW * 0.46F), px(baseY - crystalY), px(halfW * 0.22F), argb(0x2C1338, 1.0F));
      UiDrawing.drawRoundedRect(
         px(x - halfW), px(crystalY - screenH * 0.0035F), px(halfW * 2.0F), px(screenH * 0.007F), px(halfW * 0.3F), argb(0x8A46AD, 0.95F)
      );

      float crystalSize = halfW * 4.0F * (0.94F + 0.06F * (float) Math.sin(time * 0.0026F + index));
      float crystalCenterY = crystalY - screenH * 0.024F;
      glow(x, crystalCenterY, crystalSize * 1.25F, PINK, 0.35F * sway + 0.12F);
      UiDrawing.drawTexture(
         px(x - crystalSize * 0.5F), px(crystalCenterY - crystalSize * 0.5F), px(crystalSize), px(crystalSize),
         0.0F, argb(WHITE, 1.0F), CRYSTAL, FilterMode.LINEAR
      );
   }

   /** Луч из отдельных светящихся точек + бегущий сгусток. Возвращает «заряд», пришедший к знаку. */
   private static float beam(float x0, float y0, float x1, float y1, float halfW, int index, float time) {
      float dx = x1 - x0;
      float dy = y1 - y0;
      float distance = (float) Math.sqrt(dx * dx + dy * dy);
      int steps = Math.max(8, Math.round(distance / 7.0F));
      float thickness = halfW * 2.1F;

      for (int i = 0; i <= steps; i++) {
         float t = i / (float) steps;
         float ripple = 0.5F + 0.5F * (float) Math.sin(time * 0.0032F - t * 7.0F + index * 2.1F);
         float alpha = (0.22F + 0.16F * ripple) * (1.0F - 0.45F * t);
         float radius = thickness * (1.0F - 0.55F * t);
         glow(x0 + dx * t, y0 + dy * t, radius, PINK, alpha);
      }

      // сгусток энергии: летит от кристалла к знаку
      float phase = (time * 0.00042F + index * 0.23F) % 1.0F;
      float travel = phase * phase * (3.0F - 2.0F * phase);
      glow(x0 + dx * travel, y0 + dy * travel, thickness * 2.2F, WHITE, 0.60F);
      glow(x0 + dx * travel, y0 + dy * travel, thickness * 4.2F, PINK, 0.18F);
      return Math.max(0.0F, 1.0F - Math.abs(phase - 1.0F) * 6.0F);
   }

   private static void aura(float cx, float cy, float logoH, float time, float charge) {
      glow(cx, cy, logoH * 1.35F, RUBY, 0.26F + 0.16F * charge);
      glow(cx, cy, logoH * 0.80F, PINK, 0.12F + 0.22F * charge);

      embers(cx, cy, logoH, time);

      int dots = 26;
      for (int i = 0; i < dots; i++) {
         double angle = time * 0.00035 + i * (Math.PI * 2.0 / dots);
         float radius = logoH * (0.78F + 0.03F * (float) Math.sin(time * 0.0015F + i));
         float a = 0.10F + 0.18F * (float) Math.pow(Math.max(0.0, Math.sin(time * 0.0024F + i * 0.7)), 3.0);
         glow(cx + (float) Math.cos(angle) * radius * 1.15F, cy + (float) Math.sin(angle) * radius,
            logoH * 0.045F + logoH * 0.02F * charge, BRIGHT, a);
      }
   }

   /** Медленно всплывающие искры вокруг святилища. */
   private static void embers(float cx, float cy, float logoH, float time) {
      for (int i = 0; i < 18; i++) {
         float seed = i * 1.618F;
         float span = logoH * 2.6F;
         float rise = (time * (0.006F + (i % 5) * 0.0011F) + seed * 40.0F) % span;
         float x = cx + (float) Math.sin(seed * 2.4F + time * 0.0007F) * span * 0.55F;
         float y = cy + logoH * 1.1F - rise;
         float twinkle = 0.35F + 0.65F * (float) Math.pow(Math.max(0.0, Math.sin(time * 0.003F + seed * 3.0F)), 2.0);
         float fade = 1.0F - rise / span;
         glow(x, y, logoH * 0.026F, i % 3 == 0 ? WHITE : BRIGHT, 0.32F * twinkle * fade);
      }
   }

   private static void logo(float cx, float cy, float logoH, float time, float charge) {
      float pulse = 1.0F + 0.015F * (float) Math.sin(time * 0.0021F) + 0.03F * charge;
      float height = logoH * pulse;
      float width = height * ClientLogo.ASPECT;
      ClientLogo.drawInBox(px(cx - width * 0.5F), px(cy - height * 0.5F), px(width), px(height), argb(BRIGHT, 1.0F));
      if (charge > 0.02F) {
         ClientLogo.drawInBox(px(cx - width * 0.5F), px(cy - height * 0.5F), px(width), px(height), argb(WHITE, 0.45F * charge));
      }
   }

   private static void glow(float cx, float cy, float radius, int rgb, float alpha) {
      if (alpha <= 0.004F || radius <= 0.0F) {
         return;
      }

      UiDrawing.drawTexture(
         px(cx - radius), px(cy - radius), px(radius * 2.0F), px(radius * 2.0F),
         0.0F, argb(rgb, Math.min(1.0F, alpha)), GLOW, FilterMode.LINEAR
      );
   }

   private static int argb(int rgb, float alpha) {
      int a = Math.clamp(Math.round(alpha * 255.0F), 0, 255);
      return a << 24 | rgb & 0xFFFFFF;
   }

   private static float px(float value) {
      return Render2D.px(value);
   }
}
