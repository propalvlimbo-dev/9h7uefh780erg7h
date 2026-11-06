package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.render.Theme;
import dev.rose.setting.BoolSetting;
import dev.rose.util.ColorUtil;
import java.util.Arrays;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public final class InventoryModule extends HudElement {
   private static final float SLOT = 17.0F;
   private static final float ITEM = 14.0F;
   private static final float PAD = 5.0F;
   private final BoolSetting slots = this.add(new BoolSetting("slots", true));
   private final BoolSetting hideEmpty = this.add(new BoolSetting("hide_empty", false));
   private ItemStack[] preview;

   public InventoryModule() {
      super("inventory", false, Anchor.BOTTOM_RIGHT, -6.0F, -6.0F);
   }

   @Override
   public int icon() {
      return 58056;
   }

   @Override
   protected float defaultRoundness() {
      return 0.3F;
   }

   private ItemStack stack(HudContext hud, int index, boolean sample) {
      if (sample) {
         return this.previewStacks()[index];
      }

      LocalPlayer player = hud.mc.player;
      return player == null ? ItemStack.EMPTY : (ItemStack)player.getInventory().getNonEquipmentItems().get(9 + index);
   }

   private static boolean emptyInventory(LocalPlayer player) {
      for (int i = 9; i < 36; i++) {
         if (!((ItemStack)player.getInventory().getNonEquipmentItems().get(i)).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private ItemStack[] previewStacks() {
      if (this.preview == null) {
         this.preview = new ItemStack[27];
         Arrays.fill(this.preview, ItemStack.EMPTY);
         this.preview[0] = new ItemStack(Items.GOLDEN_APPLE, 12);
         this.preview[1] = new ItemStack(Items.ENDER_PEARL, 16);
         this.preview[4] = new ItemStack(Items.TOTEM_OF_UNDYING);
         this.preview[9] = new ItemStack(Items.COOKED_BEEF, 48);
         this.preview[12] = new ItemStack(Items.OBSIDIAN, 64);
         this.preview[17] = new ItemStack(Items.EXPERIENCE_BOTTLE, 32);
         this.preview[22] = new ItemStack(Items.DIAMOND, 7);
      }

      return this.preview;
   }

   @Override
   protected boolean hasContent(HudContext hud) {
      if (this.hideEmpty.isOn() && !hud.preview) {
         LocalPlayer player = hud.mc.player;
         return player != null && !emptyInventory(player);
      } else {
         return true;
      }
   }

   @Override
   protected Size measure(HudContext hud) {
      return new Size(163.0F, 61.0F);
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      int slotColor = ColorUtil.fade(hud.label, 0.07F);
      LocalPlayer player = hud.mc.player;
      boolean sample = hud.preview && (player == null || emptyInventory(player));

      for (int row = 0; row < 3; row++) {
         for (int column = 0; column < 9; column++) {
            float x = 5.0F + column * 17.0F;
            float y = 5.0F + row * 17.0F;
            if (this.slots.isOn()) {
               hud.fill(x + 1.0F, y + 1.0F, 15.0F, 15.0F, 4.0F, slotColor);
            }

            ItemStack stack = this.stack(hud, row * 9 + column, sample);
            if (!stack.isEmpty()) {
               hud.item(stack, x + 1.5F, y + 1.5F, 14.0F);
               if (stack.isDamageableItem() && stack.isDamaged()) {
                  float value = 1.0F - (float)stack.getDamageValue() / stack.getMaxDamage();
                  hud.bar(x + 3.0F, y + 17.0F - 3.0F, 11.0F, 1.2F, value, ColorUtil.lerp(Theme.danger, Theme.positive, value), ColorUtil.fade(hud.label, 0.15F));
               } else if (stack.getCount() > 1) {
                  String count = Integer.toString(stack.getCount());
                  float size = 5.4F;
                  hud.textRight(hud.semibold, count, x + 17.0F - 1.5F, y + 17.0F - size - 1.0F, size, hud.label);
               }
            }
         }
      }
   }
}
