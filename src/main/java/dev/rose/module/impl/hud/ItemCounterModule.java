package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public final class ItemCounterModule extends HudElement {
   private static final float ITEM = 14.0F;
   private static final float PAD = 5.0F;
   private final ChoiceSetting<ItemCounterModule.Layout> layout = this.add(new ChoiceSetting<>("layout", ItemCounterModule.Layout.HORIZONTAL));
   private final BoolSetting hideZero = this.add(new BoolSetting("hide_zero", true));
   private final List<ItemCounterModule.Entry> entries = List.of(
      new ItemCounterModule.Entry(this.add(new BoolSetting("totems", true)), new Item[]{Items.TOTEM_OF_UNDYING}),
      new ItemCounterModule.Entry(this.add(new BoolSetting("golden_apples", true)), new Item[]{Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE}),
      new ItemCounterModule.Entry(this.add(new BoolSetting("pearls", true)), new Item[]{Items.ENDER_PEARL}),
      new ItemCounterModule.Entry(
         this.add(new BoolSetting("arrows", false)), new Item[]{Items.ARROW, Items.SPECTRAL_ARROW, Items.TIPPED_ARROW}
      ),
      new ItemCounterModule.Entry(this.add(new BoolSetting("experience", false)), new Item[]{Items.EXPERIENCE_BOTTLE}),
      new ItemCounterModule.Entry(this.add(new BoolSetting("fireworks", false)), new Item[]{Items.FIREWORK_ROCKET}),
      new ItemCounterModule.Entry(this.add(new BoolSetting("obsidian", false)), new Item[]{Items.OBSIDIAN}),
      new ItemCounterModule.Entry(this.add(new BoolSetting("crystals", false)), new Item[]{Items.END_CRYSTAL})
   );

   public ItemCounterModule() {
      super("item_counter", false, Anchor.BOTTOM_RIGHT, -6.0F, -60.0F);
   }

   @Override
   public int icon() {
      return 57641;
   }

   private List<ItemCounterModule.Count> counts(HudContext hud) {
      List<ItemCounterModule.Count> list = new ArrayList<>();
      LocalPlayer player = hud.mc.player;

      for (ItemCounterModule.Entry entry : this.entries) {
         if (entry.setting().isOn()) {
            int amount = 0;
            if (player != null) {
               for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                  ItemStack stack = player.getInventory().getItem(slot);

                  for (Item item : entry.items()) {
                     if (stack.is(item)) {
                        amount += stack.getCount();
                     }
                  }
               }
            }

            if (hud.preview && amount == 0) {
               amount = 3;
            }

            if (amount != 0 || !this.hideZero.isOn()) {
               list.add(new ItemCounterModule.Count(new ItemStack(entry.items()[0]), amount));
            }
         }
      }

      return list;
   }

   @Override
   protected boolean hasContent(HudContext hud) {
      return !this.counts(hud).isEmpty();
   }

   @Override
   protected Size measure(HudContext hud) {
      List<ItemCounterModule.Count> counts = this.counts(hud);
      float cell = this.cellWidth(hud, counts);
      return this.layout.get() == ItemCounterModule.Layout.VERTICAL
         ? new Size(10.0F + cell, 10.0F + counts.size() * 17.0F - 3.0F)
         : new Size(10.0F + counts.size() * (cell + 6.0F) - 6.0F, 24.0F);
   }

   private float cellWidth(HudContext hud, List<ItemCounterModule.Count> counts) {
      float widest = 0.0F;

      for (ItemCounterModule.Count count : counts) {
         widest = Math.max(widest, hud.semibold.width(Integer.toString(count.amount()), 7.2F));
      }

      return 17.0F + widest;
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      List<ItemCounterModule.Count> counts = this.counts(hud);
      float cell = this.cellWidth(hud, counts);

      for (int i = 0; i < counts.size(); i++) {
         ItemCounterModule.Count count = counts.get(i);
         float x = this.layout.get() == ItemCounterModule.Layout.VERTICAL ? 5.0F : 5.0F + i * (cell + 6.0F);
         float y = this.layout.get() == ItemCounterModule.Layout.VERTICAL ? 5.0F + i * 17.0F : 5.0F;
         hud.item(count.icon(), x, y, 14.0F);
         int color = count.amount() == 0 ? hud.secondary : hud.label;
         hud.text(hud.semibold, Integer.toString(count.amount()), x + 14.0F + 3.0F, HudContext.centered(y, 14.0F, 7.2F), 7.2F, color);
      }
   }

   private record Count(ItemStack icon, int amount) {
   }

   private record Entry(BoolSetting setting, Item[] items) {
   }

   public enum Layout {
      HORIZONTAL,
      VERTICAL;
   }
}
