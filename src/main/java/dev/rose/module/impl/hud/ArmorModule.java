package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.render.Theme;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public final class ArmorModule extends HudElement {
   private static final EquipmentSlot[] ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
   private static final float ITEM = 14.0F;
   private static final float PAD = 5.0F;
   private final ChoiceSetting<ArmorModule.Layout> layout = this.add(new ChoiceSetting<>("layout", ArmorModule.Layout.HORIZONTAL));
   private final BoolSetting percent = this.add(new BoolSetting("percent", true));
   private final BoolSetting hands = this.add(new BoolSetting("hands", false));
   private List<ItemStack> preview;

   public ArmorModule() {
      super("armor", false, Anchor.BOTTOM, 118.0F, -4.0F);
   }

   @Override
   public int icon() {
      return 58647;
   }

   private List<ItemStack> items(HudContext hud) {
      LocalPlayer player = hud.mc.player;
      List<ItemStack> items = new ArrayList<>();
      if (player != null) {
         for (EquipmentSlot slot : ARMOR) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!stack.isEmpty()) {
               items.add(stack);
            }
         }

         if (this.hands.isOn()) {
            if (!player.getMainHandItem().isEmpty()) {
               items.add(player.getMainHandItem());
            }

            if (!player.getOffhandItem().isEmpty()) {
               items.add(player.getOffhandItem());
            }
         }
      }

      return items.isEmpty() && hud.preview ? this.previewItems() : items;
   }

   private List<ItemStack> previewItems() {
      if (this.preview == null) {
         this.preview = new ArrayList<>();
         ItemStack[] stacks = new ItemStack[]{
            new ItemStack(Items.DIAMOND_HELMET),
            new ItemStack(Items.DIAMOND_CHESTPLATE),
            new ItemStack(Items.DIAMOND_LEGGINGS),
            new ItemStack(Items.DIAMOND_BOOTS)
         };
         float[] wear = new float[]{0.12F, 0.35F, 0.6F, 0.86F};

         for (int i = 0; i < stacks.length; i++) {
            stacks[i].set(DataComponents.DAMAGE, Math.round(stacks[i].getMaxDamage() * wear[i]));
            this.preview.add(stacks[i]);
         }
      }

      return this.preview;
   }

   @Override
   protected boolean hasContent(HudContext hud) {
      return !this.items(hud).isEmpty();
   }

   private String percentText(ItemStack stack) {
      if (!stack.isDamageableItem()) {
         return stack.getCount() > 1 ? Integer.toString(stack.getCount()) : "";
      } else {
         return Math.round(durability(stack) * 100.0F) + "%";
      }
   }

   private static float durability(ItemStack stack) {
      return !stack.isDamageableItem() ? 1.0F : 1.0F - (float)stack.getDamageValue() / stack.getMaxDamage();
   }

   private static int durabilityColor(float value) {
      return value > 0.5F ? ColorUtil.lerp(Theme.warning, Theme.positive, (value - 0.5F) * 2.0F) : ColorUtil.lerp(Theme.danger, Theme.warning, value * 2.0F);
   }

   @Override
   protected Size measure(HudContext hud) {
      List<ItemStack> items = this.items(hud);
      int count = Math.max(1, items.size());
      if (this.layout.get() == ArmorModule.Layout.HORIZONTAL) {
         float cell = this.percent.isOn() ? 22.0F : 18.0F;
         return new Size(10.0F + cell * count - (cell - 14.0F) + 2.0F, this.percent.isOn() ? 31.0F : 24.0F);
      }

      float textW = 0.0F;
      if (this.percent.isOn()) {
         for (ItemStack stack : items) {
            textW = Math.max(textW, hud.medium.width(this.percentText(stack), 6.4F));
         }
      }

      return new Size(24.0F + (this.percent.isOn() ? 5.0F + Math.max(textW, 18.0F) : 0.0F), 10.0F + count * 18.0F - 4.0F);
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      List<ItemStack> items = this.items(hud);
      int track = ColorUtil.fade(hud.label, 0.14F);
      if (this.layout.get() == ArmorModule.Layout.HORIZONTAL) {
         float cell = this.percent.isOn() ? 22.0F : 18.0F;
         float x = 5.0F;

         for (ItemStack stack : items) {
            float itemX = x + 1.0F;
            hud.item(stack, itemX, 4.0F, 14.0F);
            float value = durability(stack);
            if (stack.isDamageableItem()) {
               hud.bar(itemX + 1.0F, 19.5F, 12.0F, 1.6F, value, durabilityColor(value), track);
            }

            if (this.percent.isOn()) {
               String text = this.percentText(stack);
               float size = 5.4F;
               float textW = hud.medium.width(text, size);
               hud.text(hud.medium, text, itemX + 7.0F - textW / 2.0F, 22.2F, size, hud.secondary);
            }

            x += cell;
         }
      } else {
         float y = 5.0F;

         for (ItemStack stack : items) {
            hud.item(stack, 5.0F, y, 14.0F);
            float value = durability(stack);
            if (this.percent.isOn()) {
               float textX = 24.0F;
               hud.text(hud.medium, this.percentText(stack), textX, y + 1.0F, 6.4F, hud.label);
               if (stack.isDamageableItem()) {
                  hud.bar(textX, y + 10.0F, w - textX - 5.0F, 1.6F, value, durabilityColor(value), track);
               }
            } else if (stack.isDamageableItem()) {
               hud.bar(6.0F, y + 14.0F, 12.0F, 1.4F, value, durabilityColor(value), track);
            }

            y += 18.0F;
         }
      }
   }

   public enum Layout {
      HORIZONTAL,
      VERTICAL;
   }
}
