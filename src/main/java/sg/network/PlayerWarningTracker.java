package sg.network;

import com.google.gson.annotations.SerializedName;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import sg.modules.impl.init.NotificationCategory;
import sg.modules.impl.init.NotificationManager;
import sg.util.time.ColorMath;

public final class PlayerWarningTracker {
   private static final EquipmentSlot[] В_a366f762 = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
   @SerializedName("3")
   private final Map<EquipmentSlot, PlayerWarningTracker.ArmorWarningState> field_3 = new EnumMap<>(EquipmentSlot.class);
   private final Map<Holder<MobEffect>, PlayerWarningTracker.EffectWarningState> р = new HashMap<>();
   private final Set<Holder<MobEffect>> И = new HashSet<>();
   private Player Х;
   @SerializedName("В")
   private Level В_3d2cb785;
   private static final double с诶4 = 100.0;
   private static final double с诶п = 10.0;
   private static final double с诶Д = 5.0;
   private static final double с诶Ъ = 5.0;
   private static final String с诶т = "Скоро сломается: ";
   private static final String с诶а = " — осталось ";
   private static final double с诶Ю = 200.0;
   private static final double с诶Ч = 100.0;
   private static final String с诶Л = "Скоро закончится эффект: ";
   private static final String с诶诶 = " — осталось ";

   private void у(Player var1) {
      this.И.clear();

      for (MobEffectInstance var3 : var1.getActiveEffects()) {
         if (!var3.isInfiniteDuration() && var3.getDuration() > 0 && !var3.isAmbient() && var3.showIcon() && !var3.getEffect().value().isInstantaneous()) {
            Holder var4 = var3.getEffect();
            this.И.add(var4);
            PlayerWarningTracker.EffectWarningState var5 = this.р.get(var4);
            if (var5 == null || var5.ВЪ != var3.getAmplifier()) {
               var5 = new PlayerWarningTracker.EffectWarningState(var3.getAmplifier());
               this.р.put(var4, var5);
            }

            if (var5.В.I(var3.getDuration(), с诶Ю, с诶Ч)) {
               int var6 = (var3.getDuration() + 19) / 20;
               int var7 = var3.getDuration() <= 100 ? ColorMath.packRgb(255, 180, 65) : ColorMath.packRgb(115, 185, 255);
               MutableComponent var8 = Component.literal(с诶Л)
                  .append(PotionContents.getPotionDescription(var4, var3.getAmplifier()))
                  .append(с诶诶)
                  .append(Component.literal(var6 + " сек.").withColor(var7));
               ItemStack var9 = new ItemStack(Items.POTION);
               var9.set(
                  DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withEffectAdded(new MobEffectInstance(var4, var3.getDuration(), var3.getAmplifier()))
               );
               NotificationManager.р(var8, var9, var7, NotificationCategory.EFFECTS);
            }
         }
      }

      this.р.keySet().removeIf(var1x -> !this.И.contains(var1x));
   }

   public void 弟(Player var1, boolean var2, boolean var3) {
      if (!var1.isAlive()) {
         this.уО();
      } else {
         if (this.Х != var1 || this.В_3d2cb785 != var1.level()) {
            this.уО();
            this.Х = var1;
            this.В_3d2cb785 = var1.level();
         }

         if (var2) {
            this.弟(var1);
         } else {
            this.field_3.clear();
         }

         if (var3) {
            this.у(var1);
         } else {
            this.р.clear();
            this.И.clear();
         }
      }
   }

   private void 弟(Player var1) {
      for (EquipmentSlot var5 : В_a366f762) {
         ItemStack var6 = var1.getItemBySlot(var5);
         if (!var6.isEmpty() && var6.isDamageableItem() && var6.getMaxDamage() > 0) {
            PlayerWarningTracker.ArmorWarningState var7 = this.field_3.get(var5);
            if (var7 == null || !var7.Ъ(var6)) {
               var7 = new PlayerWarningTracker.ArmorWarningState(var6);
               this.field_3.put(var5, var7);
            }

            int var8 = Math.max(0, var6.getMaxDamage() - var6.getDamageValue());
            double var9 = var8 * с诶4 / var6.getMaxDamage();
            if (var7.Я.I(var9, с诶п, с诶Д)) {
               int var11 = var9 <= с诶Ъ ? ColorMath.packRgb(255, 70, 70) : ColorMath.packRgb(255, 180, 65);
               MutableComponent var12 = Component.literal(с诶т)
                  .append(var6.getHoverName().copy())
                  .append(с诶а)
                  .append(Component.literal((int)Math.ceil(var9) + "%").withColor(var11));
               NotificationManager.р(var12, var6, var11, NotificationCategory.ARMOR);
            }
         } else {
            this.field_3.remove(var5);
         }
      }
   }

   public void уО() {
      this.field_3.clear();
      this.р.clear();
      this.И.clear();
      this.Х = null;
      this.В_3d2cb785 = null;
   }

   private static final class ArmorWarningState {
      private final ItemStack Ю;
      private final PlayerWarningTracker.WarningThreshold Я = new PlayerWarningTracker.WarningThreshold();

      private ArmorWarningState(ItemStack var1) {
         this.Ю = var1.copyWithCount(1);
      }

      private boolean Ъ(ItemStack var1) {
         if (this.Ю.getItem() != var1.getItem()) {
            return false;
         }

         this.Ю.setDamageValue(var1.getDamageValue());
         return ItemStack.isSameItemSameComponents(this.Ю, var1);
      }
   }

   private static final class WarningThreshold {
      private int пэ;

      private boolean I(double var1, double var3, double var5) {
         int var7 = var1 <= var5 ? 2 : (var1 <= var3 ? 1 : 0);
         boolean var8 = var7 > this.пэ;
         this.пэ = var7;
         return var8;
      }
   }

   private static final class EffectWarningState {
      private final int ВЪ;
      private final PlayerWarningTracker.WarningThreshold В = new PlayerWarningTracker.WarningThreshold();

      private EffectWarningState(int var1) {
         this.ВЪ = var1;
      }
   }
}
