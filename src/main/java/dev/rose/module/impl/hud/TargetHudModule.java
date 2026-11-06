package dev.rose.module.impl.hud;

import dev.rose.anim.Spring;
import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.hud.TargetTracker;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Paint;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;


public final class TargetHudModule extends HudElement {
   private static final float WIDTH = 142.0F;
   private static final float PORTRAIT = 32.0F;
   private static final float PAD = 6.0F;
   private static final EquipmentSlot[] ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
   private final BoolSetting crosshair = this.add(new BoolSetting("crosshair", true));
   private final NumberSetting keep = this.add(new NumberSetting("keep", 4.0, 1.0, 10.0, 0.5, "rose.unit.seconds"));
   private final BoolSetting model = this.add(new BoolSetting("model", true));
   private final BoolSetting items = this.add(new BoolSetting("items", true));
   private final BoolSetting distance = this.add(new BoolSetting("distance", true));
   private LivingEntity shown;
   private int shownId = Integer.MIN_VALUE;
   private final Spring health = new Spring(1.0, 0.22, 1.0);
   private final Spring trail = new Spring(1.0, 0.9, 1.0);
   private final Spring absorption = new Spring(0.0, 0.3, 1.0);

   public TargetHudModule() {
      super("target_hud", false, Anchor.CENTER, 0.0F, 52.0F);
   }

   @Override
   public int icon() {
      return 57728;
   }

   @Override
   protected float defaultRoundness() {
      return 0.42F;
   }

   private LivingEntity current() {
      return TargetTracker.target(Math.round(this.keep.get() * 1000.0), this.crosshair.isOn());
   }

   @Override
   protected boolean hasContent(HudContext hud) {
      return this.current() != null;
   }

   @Override
   protected Size measure(HudContext hud) {
      return new Size(142.0F, this.items.isOn() ? 48.0F : 44.0F);
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      LivingEntity target = this.current();
      if (target != null) {
         this.shown = target;
      }

      boolean sample = false;
      if (target == null) {
         target = (LivingEntity)(hud.preview ? (hud.mc.player != null ? hud.mc.player : null) : this.shown);
         sample = hud.preview && target == null;
      }

      if (target != null || sample) {
         float maxHealth = sample ? 20.0F : Math.max(1.0F, target.getMaxHealth());
         float value = sample ? 15.0F : target.getHealth();
         float extra = sample ? 4.0F : target.getAbsorptionAmount();
         int id = sample ? -1 : target.getId();
         if (id != this.shownId) {
            this.shownId = id;
            this.health.snap(value / maxHealth);
            this.trail.snap(value / maxHealth);
            this.absorption.snap(extra / maxHealth);
         }

         float ratio = Math.clamp(value / maxHealth, 0.0F, 1.0F);
         this.health.setTarget(ratio);
         if (ratio > this.trail.getFloat()) {
            this.trail.snap(ratio);
         }

         this.trail.setTarget(ratio);
         this.absorption.setTarget(Math.clamp(extra / maxHealth, 0.0F, 1.0F));
         float flash = sample ? 0.0F : Math.clamp((target.hurtTime - hud.partialTick) / 10.0F, 0.0F, 1.0F);
         this.drawPortrait(hud, target, sample, flash);
         float textX = 45.0F;
         float right = w - 6.0F - 1.0F;
         String healthText = String.format(Locale.ROOT, "%.1f", value + extra);
         float healthW = hud.textRight(hud.semibold, healthText, right, 7.5F, 7.2F, hud.label);
         hud.icon(57586, right - healthW - 5.5F, 11.9F, 7.5F, extra > 0.0F ? -15043 : Theme.danger);
         String name = sample ? "Player" : target.getName().getString();
         float nameMax = right - healthW - 14.0F - textX;
         hud.text(hud.semibold, ellipsize(hud, name, 7.6F, nameMax), textX, 7.0F, 7.6F, hud.label);
         float barX = textX;
         float barY = 19.0F;
         float barW = right - textX;
         float barH = 5.0F;
         int track = ColorUtil.fade(hud.label, 0.13F);
         hud.fill(barX, barY, barW, barH, barH / 2.0F, track);
         float trailW = barW * Math.clamp(this.trail.getFloat(), 0.0F, 1.0F);
         if (trailW > 0.5F) {
            hud.fill(barX, barY, trailW, barH, barH / 2.0F, ColorUtil.withAlpha(16777215, 112));
         }

         float healthW2 = barW * Math.clamp(this.health.getFloat(), 0.0F, 1.0F);
         if (healthW2 > 0.5F) {
            int from = ColorUtil.lerp(Theme.danger, hud.accent(), Math.clamp(ratio * 1.6F, 0.0F, 1.0F));
            Surfaces.fill(hud.g, barX, barY, Math.max(healthW2, barH), barH, Corners.round(barH / 2.0F), Paint.linear(0.0F, from, Theme.accent2()), hud.alpha);
         }

         float gold = barW * Math.clamp(this.absorption.getFloat(), 0.0F, 1.0F);
         if (gold > 0.5F) {
            hud.fill(barX, barY - 1.2F, Math.max(gold, 2.0F), 1.6F, 0.8F, -15043);
         }

         if (this.items.isOn()) {
            float itemY = 28.0F;
            float size = 10.0F;
            float cursor = textX;
            if (!sample) {
               for (EquipmentSlot slot : ARMOR) {
                  ItemStack stack = target.getItemBySlot(slot);
                  if (!stack.isEmpty()) {
                     hud.item(stack, cursor, itemY, size);
                     cursor += size + 2.0F;
                  }
               }

               ItemStack hand = target.getMainHandItem();
               if (!hand.isEmpty()) {
                  if (cursor > textX) {
                     cursor += 3.0F;
                  }

                  hud.item(hand, cursor, itemY, size);
               }
            }

            if (this.distance.isOn() && hud.mc.player != null && !sample && target != hud.mc.player) {
               String text = String.format(
                  Locale.ROOT, "%.1f %s", hud.mc.player.distanceTo(target), I18n.get("rose.hud.target_hud.meters", new Object[0])
               );
               hud.textRight(hud.regular, text, right, itemY + 1.8F, 6.2F, hud.secondary);
            }
         }
      }
   }

   private void drawPortrait(HudContext hud, LivingEntity target, boolean sample, float flash) {
      float shake = flash > 0.0F ? (float)Math.sin(flash * 38.0F) * 1.2F * flash : 0.0F;
      float x = 6.0F + shake;
      float y = 6.0F;
      float size = 32.0F;
      if (!sample && !(target instanceof AbstractClientPlayer) && this.model.isOn() && !target.isDeadOrDying()) {
         hud.fill(x, y, size, size, 9.0F, ColorUtil.fade(hud.label, 0.08F));
         this.drawModel(hud, target, x, y, size);
      } else {
         Identifier skin = target instanceof AbstractClientPlayer player ? player.getSkin().body().texturePath() : DefaultPlayerSkin.getDefaultTexture();
         if (!sample && !(target instanceof AbstractClientPlayer)) {
            hud.fill(x, y, size, size, 9.0F, ColorUtil.fade(hud.label, 0.1F));
            hud.icon(57889, x + size / 2.0F, y + size / 2.0F, 14.0F, hud.secondary);
         } else {
            hud.face(skin, x, y, size, 9.0F);
         }
      }

      if (flash > 0.01F) {
         int red = ColorUtil.withAlpha(Theme.danger, Math.round(150.0F * flash));
         Surfaces.fill(hud.g, x, y, size, size, Corners.themed(9.0F), red, red, red, red, hud.alpha);
      }
   }

   private void drawModel(HudContext hud, LivingEntity target, float x, float y, float size) {
      float height = Math.max(0.3F, target.getBbHeight());
      float width = Math.max(0.3F, target.getBbWidth());
      float scale = size * 1.15F / Math.max(height, width * 1.6F) / Math.max(0.1F, target.getScale());
      EntityRenderState state = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(target).createRenderState(target, 1.0F);
      state.lightCoords = 15728880;
      state.shadowPieces.clear();
      state.outlineColor = 0;
      if (state instanceof LivingEntityRenderState living) {
         living.bodyRot = 200.0F;
         living.yRot = 0.0F;
         living.xRot = 0.0F;
      }

      hud.clip(x, y, size, size);
      Quaternionf pose = new Quaternionf().rotateZ((float) Math.PI);
      Matrix4f local = GuiDraw.pose(hud.g);
      Vector3f corner = local.transformPosition(new Vector3f(x, y, 0.0F));
      Vector3f far = local.transformPosition(new Vector3f(x + size, y + size * 1.48F, 0.0F));
      hud.g
         .submitEntityRenderState(
            state,
            scale * (far.x - corner.x) / size,
            new Vector3f(0.0F, height / 2.0F * target.getScale(), 0.0F),
            pose,
            null,
            Mth.floor(corner.x),
            Mth.floor(corner.y),
            Mth.ceil(far.x),
            Mth.ceil(far.y)
         );
      hud.unclip();
   }

   private static String ellipsize(HudContext hud, String text, float size, float max) {
      if (hud.semibold.width(text, size) <= max) {
         return text;
      }

      int end = text.length();

      while (end > 0 && hud.semibold.width(text.substring(0, end) + "…", size) > max) {
         end--;
      }

      return text.substring(0, end) + "…";
   }
}
