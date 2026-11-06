package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.render.Theme;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;


public final class WarningsModule extends HudElement {
   private static final EquipmentSlot[] ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
   private static final float PILL_H = 16.0F;
   private static final float GAP = 3.0F;
   private static final float TEXT = 6.8F;
   private final BoolSetting armor = this.add(new BoolSetting("armor", true));
   private final NumberSetting armorPercent = this.add(new NumberSetting("armor_percent", 15.0, 5.0, 50.0, 5.0, "rose.unit.percent"));
   private final BoolSetting tool = this.add(new BoolSetting("tool", true));
   private final NumberSetting toolPercent = this.add(new NumberSetting("tool_percent", 10.0, 5.0, 50.0, 5.0, "rose.unit.percent"));
   private final BoolSetting effects = this.add(new BoolSetting("effects", true));
   private final NumberSetting effectSeconds = this.add(new NumberSetting("effect_seconds", 5.0, 2.0, 30.0, 1.0, "rose.unit.seconds"));
   private List<String> warnings = List.of();

   public WarningsModule() {
      super("warnings", true, Anchor.CENTER, 0.0F, -46.0F);
   }

   @Override
   public int icon() {
      return 57747;
   }

   @Override
   protected boolean drawsBackground() {
      return false;
   }

   @Override
   protected float defaultRoundness() {
      return 1.0F;
   }

   @Override
   public void onTick() {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) {
         this.warnings = List.of();
      } else {
         List<String> next = new ArrayList<>();
         if (this.armor.isOn()) {
            for (EquipmentSlot slot : ARMOR) {
               ItemStack stack = player.getItemBySlot(slot);
               int left = percentLeft(stack);
               if (left >= 0 && left <= this.armorPercent.intValue()) {
                  next.add(I18n.get("rose.hud.warnings.armor", new Object[]{stack.getHoverName().getString(), left}));
               }
            }
         }

         if (this.tool.isOn()) {
            ItemStack hand = player.getMainHandItem();
            int left = percentLeft(hand);
            if (left >= 0 && left <= this.toolPercent.intValue()) {
               next.add(I18n.get("rose.hud.warnings.tool", new Object[]{hand.getHoverName().getString(), left}));
            }
         }

         if (this.effects.isOn()) {
            for (MobEffectInstance instance : player.getActiveEffects()) {
               if (!instance.isInfiniteDuration()
                  && instance.getDuration() > 0
                  && ((MobEffect)instance.getEffect().value()).getCategory() == MobEffectCategory.BENEFICIAL) {
                  int seconds = instance.getDuration() / 20;
                  if (seconds <= this.effectSeconds.intValue()) {
                     next.add(
                        I18n.get(
                           "rose.hud.warnings.effect",
                           new Object[]{
                              ((MobEffect)instance.getEffect().value()).getDisplayName().getString(), String.format(Locale.ROOT, "0:%02d", seconds)
                           }
                        )
                     );
                  }
               }
            }
         }

         this.warnings = next;
      }
   }

   private static int percentLeft(ItemStack stack) {
      return !stack.isEmpty() && stack.isDamageableItem() ? Math.round(100.0F * (1.0F - (float)stack.getDamageValue() / stack.getMaxDamage())) : -1;
   }

   private List<String> shown(HudContext hud) {
      return this.warnings.isEmpty() && hud.preview
         ? List.of(
            I18n.get("rose.hud.warnings.armor", new Object[]{I18n.get("item.minecraft.diamond_helmet", new Object[0]), 12}),
            I18n.get("rose.hud.warnings.effect", new Object[]{I18n.get("effect.minecraft.strength", new Object[0]), "0:04"})
         )
         : this.warnings;
   }

   @Override
   protected boolean hasContent(HudContext hud) {
      return !this.warnings.isEmpty();
   }

   @Override
   protected Size measure(HudContext hud) {
      List<String> list = this.shown(hud);
      float width = 0.0F;

      for (String text : list) {
         width = Math.max(width, pillWidth(hud, text));
      }

      return new Size(Math.max(width, 40.0F), Math.max(16.0F, list.size() * 19.0F - 3.0F));
   }

   private static float pillWidth(HudContext hud, String text) {
      return 22.0F + hud.medium.width(text, 6.8F) + 9.0F;
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      List<String> list = this.shown(hud);
      float pulse = 0.6F + 0.4F * (float)Math.sin(System.currentTimeMillis() / 180.0);
      float y = 0.0F;

      for (String text : list) {
         float pillW = pillWidth(hud, text);
         float x = hud.alignRight() ? w - pillW : (hud.alignCenter() ? (w - pillW) / 2.0F : 0.0F);
         hud.background(x, y, pillW, 16.0F);
         hud.fill(x + 1.0F, y + 1.0F, pillW - 2.0F, 14.0F, 7.0F, ColorUtil.withAlpha(Theme.warning, Math.round(34.0F * pulse)));
         hud.icon(57747, x + 8.0F + 5.0F, y + 8.0F, 8.5F, ColorUtil.lerp(Theme.warning, -1, 0.25F * (1.0F - pulse)));
         hud.text(hud.medium, text, x + 8.0F + 14.0F, HudContext.centered(y, 16.0F, 6.8F), 6.8F, hud.label);
         y += 19.0F;
      }
   }
}
