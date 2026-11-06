package sg.modules.impl.misc;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.network.ClientChat;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.MultiChoiceSetting;
import sg.modules.impl.init.NotificationCategory;
import sg.modules.impl.init.NotificationManager;

public final class UseTracker extends ClientModule {
   private static final String Вж = "Пить";
   private static final String В弟 = "Выкидывать";
   private static final String Вб = "Использовать";
   private static final String ВЫ = "Уведомления";
   private static final String Вl = "Чат";
   private static final long ВЕ = 2000000000L;
   private final MultiChoiceSetting И;
   private final MultiChoiceSetting ь;
   private final IntSet В;
   private final Map<Integer, UseTracker.ConsumedItem> съ;
   private final Map<Integer, UseTracker.ItemUseState> сМ;
   private ClientLevel ш;
   private final EventListener<PlayerTickEvent> ВТ;
   private final EventListener<PacketReceiveEvent> ВО;
   private static final String ЯЫ4 = "UseTracker";
   private static final String ЯЫп = "Уведомления об использовании предметов игроками";
   private static final String ЯЫД = "Types";
   private static final String ЯЫЪ = "Использовать";
   private static final String ЯЫт = "Пить";
   private static final String ЯЫа = "Выкидывать";
   private static final String ЯЫЮ = "Вывод";
   private static final String ЯЫЧ = "Уведомления";
   private static final String ЯЫЛ = "Чат";
   private static final String ЯЫ诶 = "Выкидывать";
   private static final String ЯЫ1 = "Выкидывать";
   private static final String ЯЫЬ = "Выкидывать";
   private static final double ЯЫ衣 = 0.5;
   private static final double ЯЫш = 0.5;
   private static final double ЯЫИ = 0.5;
   private static final String ЯЫь = "Вы использовали ";
   private static final String ЯЫж = " использовал ";
   private static final double ЯЫ弟 = 0.7;
   private static final double ЯЫб = 0.3;
   private static final double ЯЫЫ = 0.35;
   private static final double ЯЫl = 0.35;
   private static final long ЯЫ8 = 2000000000L;
   private static final String ЯЫ5 = "Пить";
   private static final String ЯЫв = "Использовать";
   private static final String ЯЫъ = "Вы выпили ";
   private static final String ЯЫМ = "Вы съели ";
   private static final String ЯЫН = " выпил ";
   private static final String ЯЫн = " съел ";
   private static final String ЯЫЦ = "Уведомления";
   private static final String ЯЫч = "Чат";
   private static final int ЯЫЭ = 16777215;
   private static final long ЯЫ必 = 2000000000L;

   private void 必(Packet<?> var1) {
      if (this.isEnabled() && this.И.isSelected(ЯЫЬ) && MINECRAFT_CLIENT.level != null) {
         if (var1 instanceof ClientboundBundlePacket var2) {
            for (Packet var5 : var2.subPackets()) {
               this.必(var5);
            }
         } else if (var1 instanceof ClientboundLevelEventPacket var3) {
            this.у(var3);
         }
      }
   }

   @Override
   public void onDisable() {
      this.ТЖ();
      super.onDisable();
   }

   public UseTracker() {
      super(ЯЫ4, ЯЫп, ModuleCategory.MISC);
      this.И = new MultiChoiceSetting(ЯЫД, new ChoiceOption(ЯЫЪ, true), new ChoiceOption(ЯЫт, true), new ChoiceOption(ЯЫа, true));
      this.ь = new MultiChoiceSetting(ЯЫЮ, new ChoiceOption(ЯЫЧ, true), new ChoiceOption(ЯЫЛ, false));
      this.В = new IntOpenHashSet();
      this.съ = new HashMap<>();
      this.сМ = new HashMap<>();
      this.ВТ = var1 -> this.ТЙ();
      this.ВО = this::Ф;
      this.addSettings(this.И, this.ь);
   }

   private void у(ClientboundLevelEventPacket var1) {
      int var2 = var1.getType();
      if (var2 == 2002 || var2 == 2007) {
         BlockPos var3 = var1.getPos();
         Player var4 = this.у(var3.getX() + ЯЫ衣, var3.getY() + ЯЫш, var3.getZ() + ЯЫИ);
         if (var4 != null) {
            ItemStack var5 = this.у(var4);
            if (!var5.isEmpty()) {
               String var6 = к(var5);
               if (var6 == null) {
                  var6 = var5.getHoverName().getString();
               }

               MutableComponent var7 = Component.empty();
               if (var4 == MINECRAFT_CLIENT.player) {
                  var7.append(Component.literal(ЯЫь));
               } else {
                  var7.append(у(var4.getName().getString())).append(Component.literal(ЯЫж));
               }

               var7.append(у(var6));
               this.у(var7, var5, var4 == MINECRAFT_CLIENT.player);
            }
         }
      }
   }

   private ItemStack у(Player var1) {
      ItemStack var2 = var1.getMainHandItem();
      if (Ю(var2)) {
         return var2;
      }

      UseTracker.ConsumedItem var3 = this.съ.get(var1.getId());
      return var3 != null && System.nanoTime() - var3.timeNs <= ЯЫ8 ? var3.stack : ItemStack.EMPTY;
   }

   private void у(Component var1, ItemStack var2, boolean var3) {
      if (this.ь.isSelected(ЯЫЦ)) {
         NotificationManager.р(var1, var2, ColorMath.getAnimatedThemeColor(0), var3 ? NotificationCategory.SELF_USE : NotificationCategory.PLAYER_USE);
      }

      if (this.ь.isSelected(ЯЫч)) {
         ClientChat.З(var1);
      }
   }

   private void у(Player var1, boolean var2, ItemStack var3, ItemUseAnimation var4) {
      if (var4 == ItemUseAnimation.EAT || var4 == ItemUseAnimation.DRINK) {
         boolean var5 = var4 == ItemUseAnimation.DRINK;
         if (this.И.isSelected(var5 ? ЯЫ5 : ЯЫв)) {
            MutableComponent var6 = Component.empty();
            if (var2) {
               var6.append(Component.literal(var5 ? ЯЫъ : ЯЫМ));
            } else {
               var6.append(у(var1.getName().getString())).append(Component.literal(var5 ? ЯЫН : ЯЫн));
            }

            var6.append(у(var3.getHoverName().getString()));
            this.у(var6, var3, var2);
         }
      }
   }

   private static MutableComponent у(String var0) {
      return Component.literal(var0).setStyle(Style.EMPTY.withColor(ColorMath.getAnimatedThemeColor(0) & ЯЫЭ));
   }

   private void Ф(PacketReceiveEvent var1) {
      if (this.И.isSelected(ЯЫ1)) {
         Packet var2 = var1.getPacket();
         if (!MINECRAFT_CLIENT.isSameThread()) {
            ClientLevel var3 = MINECRAFT_CLIENT.level;
            MINECRAFT_CLIENT.execute(() -> {
               if (MINECRAFT_CLIENT.level == var3) {
                  this.必(var2);
               }
            });
         } else {
            this.必(var2);
         }
      }
   }

   private Player у(double var1, double var3, double var5) {
      AbstractClientPlayer var7 = null;
      double var8 = ЯЫ弟;

      for (AbstractClientPlayer var11 : MINECRAFT_CLIENT.level.players()) {
         Vec3 var12 = var11.getViewVector(1.0F);
         Vec3 var13 = var11.getEyePosition().add(var12.scale(ЯЫб)).add(-var12.x * ЯЫЫ, 0.0, var12.z * ЯЫl);
         double var14 = var13.distanceToSqr(var1, var3, var5);
         if (!(var14 >= var8)) {
            var8 = var14;
            var7 = var11;
         }
      }

      return var7;
   }

   private void ТЖ() {
      this.В.clear();
      this.съ.clear();
      this.сМ.clear();
      this.ш = null;
   }

   private static String к(ItemStack var0) {
      PotionContents var1 = var0.get(DataComponents.POTION_CONTENTS);
      return var1 == null ? null : var0.getHoverName().getString();
   }

   private static boolean Ю(ItemStack var0) {
      return var0 != null && (var0.is(Items.SPLASH_POTION) || var0.is(Items.LINGERING_POTION));
   }

   private void ТЙ() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.ш != MINECRAFT_CLIENT.level) {
            this.ТЖ();
            this.ш = MINECRAFT_CLIENT.level;
         }

         this.Т弟();
         if (this.И.isSelected(ЯЫ诶)) {
            this.Т7();
         } else {
            this.съ.clear();
         }
      } else {
         this.ТЖ();
      }
   }

   private void Т7() {
      long var1 = System.nanoTime();
      this.В.clear();

      for (AbstractClientPlayer var4 : MINECRAFT_CLIENT.level.players()) {
         this.В.add(var4.getId());
         ItemStack var5 = var4.getMainHandItem();
         if (Ю(var5)) {
            this.съ.put(var4.getId(), new UseTracker.ConsumedItem(var5.copy(), var1));
         }
      }

      this.съ.entrySet().removeIf(var3 -> !this.В.contains(var3.getKey().intValue()) || var1 - var3.getValue().timeNs > ЯЫ必);
   }

   private void Т弟() {
      this.В.clear();

      for (AbstractClientPlayer var2 : MINECRAFT_CLIENT.level.players()) {
         if (var2.isAlive() && !var2.isRemoved()) {
            this.В.add(var2.getId());
            this.сМ.computeIfAbsent(var2.getId(), var1 -> new UseTracker.ItemUseState()).method_7(var2, var2 == MINECRAFT_CLIENT.player);
         }
      }

      this.сМ.keySet().removeIf(var1 -> !this.В.contains(var1.intValue()));
   }

   private final class ItemUseState {
      private int сO;
      private int с7;
      private ItemStack е = ItemStack.EMPTY;

      private void method_7(Player var1, boolean var2) {
         if (var1.isUsingItem()) {
            ItemStack var3 = var1.getUseItem();
            ItemUseAnimation var4 = var3.getUseAnimation();
            if (var4 != ItemUseAnimation.EAT && var4 != ItemUseAnimation.DRINK) {
               this.method_7Ч();
            } else {
               int var5 = var1.getTicksUsingItem();
               if (this.е.isEmpty() || var5 < this.сO || !ItemStack.isSameItemSameComponents(this.е, var3)) {
                  this.е = var3.copyWithCount(1);
               }

               this.сO = var5;
               this.с7 = this.е.getUseDuration(var1);
            }
         } else {
            if (this.сO > 0 && this.с7 > 0 && this.сO >= this.с7 - 1 && !this.е.isEmpty()) {
               UseTracker.this.у(var1, var2, this.е, this.е.getUseAnimation());
            }

            this.method_7Ч();
         }
      }

      private void method_7Ч() {
         this.сO = 0;
         this.с7 = 0;
         this.е = ItemStack.EMPTY;
      }
   }

   private record ConsumedItem(ItemStack stack, long timeNs) {
   }
}
