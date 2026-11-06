package sg.modules.impl.misc;

import java.util.ArrayList;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.joml.Matrix4f;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.events.impl.HudRenderEvent;
import sg.settings.MultiChoiceSetting;

public class KitHelper extends ClientModule {
   private static final int пО = 10;
   private static final int пФ = 3;
   private static final int пЙ = 2;
   private static final int пк = 2;
   private static final EquipmentSlot[] п_at_2476dbde = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
   private final @com.google.gson.annotations.SerializedName("Н")  MultiChoiceSetting trackedItemsSetting;
   private final @com.google.gson.annotations.SerializedName("с5")  FloatAnimation warningAnimation;
   private final @com.google.gson.annotations.SerializedName("Яд")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("ЯБ")  EventListener<HudRenderEvent> hudRenderListener;
   private @com.google.gson.annotations.SerializedName("Яш")  String warningText;
   private static final String Вчн = "KitHelper";
   private static final String ВчЦ = "Предупреждает, если нет тотема или мало гепла, перлов и бафов";
   private static final String Вчч = "Проверять";
   private static final String ВчЭ = "Тотем";
   private static final String Вч必 = "Гепл";
   private static final String Вчщ = "Перлы";
   private static final String ВчГ = "Зелья";
   private static final float Вчл = 12.0F;
   private static final float Вчэ = 0.01F;
   private static final float Вчю = 0.82F;
   private static final float ВчЩ = 0.18F;
   private static final float Вчу = 280.0F;
   private static final float Вч2 = 18.0F;
   private static final float Вчф = 11.0F;
   private static final float Вчи = 0.18F;
   private static final float Вч9 = 200.0F;
   private static final float Вчо = 255.0F;
   private static final String ВчO = "Тотем";
   private static final String Вч7 = "тотем";
   private static final String ВчР = "Гепл";
   private static final String Вч3 = "гепл";
   private static final String Вчр = "Перлы";
   private static final String Вч6 = "перлы";
   private static final String Вч_ = "Зелья";
   private static final String ВчШ = "бафы";
   private static final String Вчы = ", ";

   private String ЧЗ() {
      if (MINECRAFT_CLIENT.player == null) {
         return "";
      }

      ArrayList var1 = new ArrayList();
      if (this.trackedItemsSetting.isSelected(ВчO) && this.и(Items.TOTEM_OF_UNDYING) <= 0) {
         var1.add(Вч7);
      }

      if (this.trackedItemsSetting.isSelected(ВчР) && this.и(Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE) < 10) {
         var1.add(Вч3);
      }

      if (this.trackedItemsSetting.isSelected(Вчр) && this.и(Items.ENDER_PEARL) < 3) {
         var1.add(Вч6);
      }

      if (this.trackedItemsSetting.isSelected(Вч_) && this.и(Items.SPLASH_POTION, Items.LINGERING_POTION, Items.POTION) < 2) {
         var1.add(ВчШ);
      }

      return var1.isEmpty() ? "" : "Проверьте кит: " + String.join(Вчы, var1);
   }

   public KitHelper() {
      super(Вчн, ВчЦ, ModuleCategory.MISC);
      this.trackedItemsSetting = new MultiChoiceSetting(
         Вчч, new ChoiceOption(ВчЭ, true), new ChoiceOption(Вч必, true), new ChoiceOption(Вчщ, true), new ChoiceOption(ВчГ, true)
      );
      this.warningAnimation = new FloatAnimation(0.0F, Вчл, Easings.CUBIC_OUT);
      this.playerTickListener = this::method_2щ;
      this.hudRenderListener = this::у;
      this.warningText = "";
      this.addSettings(this.trackedItemsSetting);
   }

   private int Б(ItemStack var1) {
      if (MINECRAFT_CLIENT.level == null) {
         return 0;
      }

      try {
         Reference var2 = MINECRAFT_CLIENT.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.PROTECTION);
         return EnchantmentHelper.getItemEnchantmentLevel(var2, var1);
      } catch (Throwable var3) {
         return 0;
      }
   }

   private boolean еЕ() {
      for (EquipmentSlot var4 : п_at_2476dbde) {
         if (!this.method_0(MINECRAFT_CLIENT.player.getItemBySlot(var4))) {
            return false;
         }
      }

      return true;
   }

   private boolean ем() {
      return MINECRAFT_CLIENT.gui.screen() instanceof InventoryScreen;
   }

   private static int и(ItemStack var0, Item... var1) {
      if (var0 != null && !var0.isEmpty()) {
         for (Item var5 : var1) {
            if (var0.is(var5)) {
               return var0.getCount();
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   private void method_2щ(PlayerTickEvent var1) {
      this.warningText = this.ЧЗ();
      this.warningAnimation.updateTarget(this.е6() ? 1.0F : 0.0F);
   }

   private void у(HudRenderEvent var1) {
      this.warningText = this.ЧЗ();
      this.warningAnimation.updateTarget(this.е6() ? 1.0F : 0.0F);
      float var2 = this.warningAnimation.getValue();
      if (!(var2 <= Вчэ) && !this.warningText.isBlank()) {
         float var3 = Вчю + ВчЩ * Mth.sin((float)System.currentTimeMillis() / Вчу);
         float var4 = var2 * var3;
         float var5 = MINECRAFT_CLIENT.getWindow().getScreenWidth();
         float var6 = MINECRAFT_CLIENT.getWindow().getScreenHeight();
         byte var7 = 16;
         float var8 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(this.warningText, var7, 0.0F);
         float var9 = UiDrawing.getCapHeight(ClientFonts.getMediumFont(), var7);
         float var10 = Вч2;
         float var11 = Вчф;
         float var12 = var8 + var10 * 2.0F;
         float var13 = var9 + var11 * 2.0F;
         float var14 = (var5 - var12) / 2.0F;
         float var15 = var6 * Вчи;
         float var16 = var14 + var10;
         float var17 = UiDrawing.alignTextCenterY(ClientFonts.getMediumFont(), this.warningText, var15 + var13 / 2.0F, var7);
         UiDrawing.setTransform(new Matrix4f());
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());

         try {
            UiBatchDrawing.drawRoundedRect(var14, var15, var12, var13, var13 / 2.0F, ColorMath.packArgb(28, 16, 4, (int)(Вч9 * var2)));
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), this.warningText, var16, var17, var7, ColorMath.packArgb(255, 196, 72, (int)(Вчо * var4)));
         } finally {
            UiBatchDrawing.endBatch(ClientFonts.getMediumFont());
         }
      }
   }

   @Override
   public void onDisable() {
      this.warningAnimation.setImmediately(0.0F);
      this.warningText = "";
   }

   private boolean е6() {
      if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null) {
         return false;
      } else if (MINECRAFT_CLIENT.player.isCreative() || MINECRAFT_CLIENT.player.isSpectator()) {
         return false;
      } else {
         return this.warningText.isBlank() ? false : this.еЕ() || this.ем();
      }
   }

   private static boolean Т(Item var0) {
      return var0 == Items.DIAMOND_HELMET
         || var0 == Items.DIAMOND_CHESTPLATE
         || var0 == Items.DIAMOND_LEGGINGS
         || var0 == Items.DIAMOND_BOOTS
         || var0 == Items.NETHERITE_HELMET
         || var0 == Items.NETHERITE_CHESTPLATE
         || var0 == Items.NETHERITE_LEGGINGS
         || var0 == Items.NETHERITE_BOOTS;
   }

   private int и(Item... var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < MINECRAFT_CLIENT.player.getInventory().getContainerSize(); var3++) {
         var2 += и(MINECRAFT_CLIENT.player.getInventory().getItem(var3), var1);
      }

      return var2;
   }

   private boolean method_0(ItemStack var1) {
      return var1 != null && !var1.isEmpty() && Т(var1.getItem()) ? this.Б(var1) >= 2 : false;
   }
}
