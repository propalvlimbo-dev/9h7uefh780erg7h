package sg.modules.impl.misc;

import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.settings.BooleanSetting;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;

public class AutoFish extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("сг")  BooleanSetting autoSelectRodSetting;
   private @com.google.gson.annotations.SerializedName("Ян")  boolean reelPending;
   private @com.google.gson.annotations.SerializedName("ЯЦ")  boolean recastPending;
   private @com.google.gson.annotations.SerializedName("АИ")  int rodHotbarSlot;
   private final @com.google.gson.annotations.SerializedName("诶")  Stopwatch actionTimer;
   private final @com.google.gson.annotations.SerializedName("ВШ")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("Вы")  EventListener<PacketReceiveEvent> packetReceiveListener;
   private static final String MODULE_NAME = "AutoFish";
   private static final String MODULE_DESCRIPTION = "Автоматизирует процесс рыбалки";
   private static final String AUTO_SELECT_ROD_LABEL = "Автоматически брать удочку";
   private static final double REEL_DELAY_MILLIS = 600.0;
   private static final double RECAST_DELAY_MILLIS = 300.0;

   private void 必х(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.autoSelectRodSetting.getValue() && this.rodHotbarSlot == -1) {
            this.ь6();
         }

         if (this.rodHotbarSlot != -1 && MINECRAFT_CLIENT.player.getInventory().selected != this.rodHotbarSlot) {
            MINECRAFT_CLIENT.player.connection.send(new ServerboundSetCarriedItemPacket(this.rodHotbarSlot));
            MINECRAFT_CLIENT.player.getInventory().selected = this.rodHotbarSlot;
         }

         if (this.reelPending && this.actionTimer.Т(REEL_DELAY_MILLIS)) {
            this.ь衣();
            this.reelPending = false;
            this.recastPending = true;
            this.actionTimer.ВД();
         }

         if (this.recastPending && this.actionTimer.Т(RECAST_DELAY_MILLIS)) {
            this.ь衣();
            this.recastPending = false;
            this.actionTimer.ВД();
         }
      }
   }

   private void ь衣() {
      if (this.rodHotbarSlot != -1) {
         if (MINECRAFT_CLIENT.player.getInventory().getItem(this.rodHotbarSlot).is(Items.FISHING_ROD)) {
            if (MINECRAFT_CLIENT.player.getInventory().selected != this.rodHotbarSlot) {
               MINECRAFT_CLIENT.player.connection.send(new ServerboundSetCarriedItemPacket(this.rodHotbarSlot));
               MINECRAFT_CLIENT.player.getInventory().selected = this.rodHotbarSlot;
            }

            MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         }
      }
   }

   private void ь6() {
      int var1 = -1;
      int var2 = -1;

      for (int var3 = 0; var3 < 9; var3++) {
         ItemStack var4 = MINECRAFT_CLIENT.player.getInventory().getItem(var3);
         if (var4.is(Items.FISHING_ROD)) {
            int var5 = EnchantmentHelper.getEnchantmentsForCrafting(var4).entrySet().size();
            if (var5 > var2) {
               var2 = var5;
               var1 = var3;
            }
         }
      }

      if (var1 != -1) {
         this.rodHotbarSlot = var1;
      }
   }

   public AutoFish() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
      this.autoSelectRodSetting = new BooleanSetting(AUTO_SELECT_ROD_LABEL, true);
      this.rodHotbarSlot = -1;
      this.actionTimer = new Stopwatch();
      this.playerTickListener = this::必х;
      this.packetReceiveListener = this::щ;
      this.addSettings(this.autoSelectRodSetting);
   }

   @Override
   public void onDisable() {
      this.reelPending = false;
      this.recastPending = false;
      this.rodHotbarSlot = -1;
      this.actionTimer.ВД();
   }

   private void щ(PacketReceiveEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (var1.getPacket() instanceof ClientboundSoundPacket var2) {
            if (var2.getSound().value() == SoundEvents.FISHING_BOBBER_SPLASH) {
               this.reelPending = true;
               this.actionTimer.ВД();
            }
         }
      }
   }
}
