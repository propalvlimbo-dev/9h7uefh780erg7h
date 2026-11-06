package sg.hud;

import com.mojang.blaze3d.textures.FilterMode;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector4f;
import sg.ec.FontRenderer;
import sg.gui.third.ThirdGuiAnim;
import sg.gui.third.ThirdGuiColors;
import sg.gui.third.ThirdGuiIcons;
import sg.render.pipeline.UiDrawing;

/** Catlavanskid's 220x88 TargetHud geometry on the base renderer and loaded skin textures. */
final class CatlavanTargetHudRenderer {
   static final float WIDTH = 220.0F;
   static final float HEIGHT = 88.0F;
   private static final long KEEP_MS = 1500L;
   private LivingEntity target;
   private long lastSeen;
   private final ThirdGuiAnim bar = new ThirdGuiAnim(1.0F, 9.0F);

   LivingEntity update(LivingEntity auraTarget, float dt) {
      Minecraft mc = Minecraft.getInstance();
      long now = System.currentTimeMillis();
      LivingEntity next = auraTarget;
      if (next == null && mc.crosshairPickEntity instanceof LivingEntity living && living != mc.player) next = living;
      if (mc.gui.screen() instanceof ChatScreen && next == null) next = mc.player;
      if (next != null) {
         this.target = next;
         this.lastSeen = now;
      }
      if (this.target != null && this.target.level() == mc.level && now - this.lastSeen < KEEP_MS) {
         this.bar.set(ThirdGuiAnim.clamp01(this.target.getHealth() / Math.max(1.0F, this.target.getMaxHealth())));
         this.bar.update(dt);
         return this.target;
      }
      this.target = null;
      return null;
   }

   void reset() { this.target = null; this.lastSeen = 0L; this.bar.snap(1.0F); }

   void draw(float x, float y) {
      LivingEntity entity = this.target;
      if (entity == null) return;
      UiDrawing.drawShadow(x, y + 4.0F, WIDTH, HEIGHT, 14.0F, 0x66000000);
      HudBackground.draw(x, y, WIDTH, HEIGHT, 14.0F);
      UiDrawing.drawOutline(x, y, WIDTH, HEIGHT, 1.0F, 14.0F, 0x16FFFFFF);
      this.avatar(entity, x + 10.0F, y + 10.0F);
      float iconX = x + 82.0F;
      if (!entity.getMainHandItem().isEmpty()) { ThirdGuiIcons.draw(ThirdGuiIcons.sword(), iconX, y + 22.0F, 15.0F, 0xFFB9BCC8); iconX += 20.0F; }
      if (!entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) { ThirdGuiIcons.draw(ThirdGuiIcons.crown(), iconX, y + 22.0F, 15.0F, 0xFFB9BCC8); iconX += 20.0F; }
      if (!entity.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) { ThirdGuiIcons.draw(ThirdGuiIcons.shirt(), iconX, y + 22.0F, 15.0F, 0xFFB9BCC8); iconX += 20.0F; }
      if (!entity.getItemBySlot(EquipmentSlot.FEET).isEmpty()) ThirdGuiIcons.draw(ThirdGuiIcons.sneaker(), iconX, y + 22.0F, 15.0F, 0xFFB9BCC8);
      FontRenderer font = Fonts.SEMIBOLD;
      UiDrawing.pushScissor(x + 74.0F, y + 34.0F, WIDTH - 130.0F, 30.0F);
      this.text(entity.getName().getString(), x + 74.0F, y + 49.0F, 17.0F, ThirdGuiColors.TEXT);
      UiDrawing.popScissor();
      String health = String.format(Locale.ROOT, "%d", Math.round(entity.getHealth()));
      this.text(health, x + WIDTH - 12.0F - UiDrawing.getTextWidth(font, health, 15.0F, 1.0F), y + 49.0F, 15.0F, ThirdGuiColors.ACCENT);
      UiDrawing.drawRoundedRect(x + 10.0F, y + 72.0F, WIDTH - 20.0F, 5.0F, 2.5F, ThirdGuiColors.TRACK);
      float fill = (WIDTH - 20.0F) * this.bar.value;
      if (fill > 1.0F) {
         UiDrawing.drawRoundedRect(x + 10.0F, y + 72.0F, fill, 5.0F, 2.5F, ThirdGuiColors.ACCENT);
         UiDrawing.drawRoundedRect(x + 10.0F + Math.max(0.0F, fill - 22.0F), y + 72.0F, Math.min(22.0F, fill), 5.0F, 2.5F, 0x59FFFFFF);
      }
   }

   private void text(String text, float x, float centerY, float size, int color) {
      FontRenderer font = Fonts.SEMIBOLD;
      UiDrawing.drawText(font, text, x, UiDrawing.alignCapHeightCenterY(font, centerY, size), size, color, 0.0F, 0.5F, 1.0F);
   }

   private void avatar(LivingEntity entity, float x, float y) {
      if (entity instanceof AbstractClientPlayer player) {
         var skin = player.getSkin().body().texturePath();
         UiDrawing.drawTexture(x, y, 52.0F, 52.0F, 11.0F, new Vector4f(0.125F, 0.125F, 0.125F, 0.125F), -1, skin, FilterMode.NEAREST);
         UiDrawing.drawTexture(x, y, 52.0F, 52.0F, 11.0F, new Vector4f(0.625F, 0.125F, 0.125F, 0.125F), -1, skin, FilterMode.NEAREST);
      } else {
         UiDrawing.drawRoundedRect(x, y, 52.0F, 52.0F, 11.0F, ThirdGuiColors.CARD);
         String name = entity.getName().getString();
         String initial = name.isEmpty() ? "?" : name.substring(0, name.offsetByCodePoints(0, 1)).toUpperCase(Locale.ROOT);
         float width = UiDrawing.getTextWidth(Fonts.SEMIBOLD, initial, 24.0F, 1.0F);
         this.text(initial, x + 26.0F - width / 2.0F, y + 26.0F, 24.0F, ThirdGuiColors.ACCENT);
      }
   }

   private static final class Fonts {
      private static final FontRenderer SEMIBOLD = new FontRenderer("thirdgui_semibold", true);
   }
}
