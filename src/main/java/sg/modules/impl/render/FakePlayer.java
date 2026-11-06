package sg.modules.impl.render;

import com.mojang.authlib.GameProfile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.item.ItemStack;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;

public final class FakePlayer extends ClientModule {
   private static final EquipmentSlot[] Х_at_1afd5105 = new EquipmentSlot[]{
      EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND
   };
   private final Map<String, RemotePlayer> с8 = new LinkedHashMap<>();
   private final EventListener<PlayerTickEvent> Вй = this::МГ;
   private ClientLevel 衣;
   private boolean Яч;
   private int А弟 = 1;
   private static int Аб = -1;
   private static final String ВшХ = "FakePlayer";
   private static final String Вше = "Создаёт и настраивает несколько фейковых игроков";
   private static final String ВшА = "[^A-Za-z0-9_]";

   private String хс() {
      String var1;
      do {
         var1 = "FakePlayer" + this.А弟++;
      } while (this.К(var1) != null);

      return var1;
   }

   public boolean х(String var1, ItemStack var2) {
      RemotePlayer var3 = this.К(var1);
      return var3 != null && var2 != null && !var2.isEmpty() ? var3.addItem(var2.copy()) : false;
   }

   @Override
   public void onEnable() {
      if (!this.Яч && this.с8.isEmpty() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         this.Б(this.хс());
      }

      this.Яч = false;
      super.onEnable();
   }

   public RemotePlayer К(String var1) {
      String var2 = this.method_9_388008bb(var1);
      return var2 == null ? null : this.с8.get(var2);
   }

   public RemotePlayer х(String var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (!this.isEnabled()) {
            this.Яч = true;
            this.enable();
         }

         String var2 = this.Х(var1);
         if (var2 == null) {
            var2 = this.хс();
         }

         return this.К(var2) != null ? null : this.Б(var2);
      } else {
         return null;
      }
   }

   public boolean С(String var1) {
      RemotePlayer var2 = this.К(var1);
      if (var2 != null && MINECRAFT_CLIENT.player != null) {
         this.х(var2);
         return true;
      } else {
         return false;
      }
   }

   private String Х(String var1) {
      if (var1 != null && !var1.isBlank()) {
         String var2 = var1.trim().replaceAll(ВшА, "");
         return var2.isEmpty() ? null : var2.substring(0, Math.min(16, var2.length()));
      } else {
         return null;
      }
   }

   public boolean е(String var1) {
      String var2 = this.method_9_388008bb(var1);
      if (var2 == null) {
         return false;
      }

      RemotePlayer var3 = this.с8.remove(var2);
      if (var3 == null) {
         return false;
      }

      var3.remove(RemovalReason.DISCARDED);
      return true;
   }

   public boolean х(String var1, EquipmentSlot var2, ItemStack var3) {
      RemotePlayer var4 = this.К(var1);
      if (var4 != null && var2 != null && var3 != null) {
         var4.setItemSlot(var2, var3.copy());
         return true;
      } else {
         return false;
      }
   }

   public FakePlayer() {
      super(ВшХ, Вше, ModuleCategory.RENDER);
   }

   private void МГ(PlayerTickEvent var1) {
      if (!this.с8.isEmpty()) {
         if (MINECRAFT_CLIENT.level != null && this.衣 == MINECRAFT_CLIENT.level) {
            this.с8.entrySet().removeIf(var0 -> var0.getValue() == null || var0.getValue().isRemoved());
         } else {
            this.МЕ();
         }
      }
   }

   public List<String> С() {
      ArrayList var1 = new ArrayList();

      for (RemotePlayer var3 : this.с8.values()) {
         var1.add(var3.getGameProfile().name());
      }

      return List.copyOf(var1);
   }

   public int МЕ() {
      int var1 = this.с8.size();

      for (RemotePlayer var3 : this.с8.values()) {
         if (var3 != null && !var3.isRemoved()) {
            var3.remove(RemovalReason.DISCARDED);
         }
      }

      this.с8.clear();
      this.衣 = null;
      return var1;
   }

   private void х(RemotePlayer var1) {
      var1.getInventory().replaceWith(MINECRAFT_CLIENT.player.getInventory());

      for (EquipmentSlot var5 : Х_at_1afd5105) {
         var1.setItemSlot(var5, MINECRAFT_CLIENT.player.getItemBySlot(var5).copy());
      }
   }

   private String method_9_388008bb(String var1) {
      String var2 = this.Х(var1);
      return var2 == null ? null : var2.toLowerCase(Locale.ROOT);
   }

   @Override
   public void onDisable() {
      this.МЕ();
      super.onDisable();
   }

   private RemotePlayer Б(String var1) {
      UUID var2 = UUID.nameUUIDFromBytes(("rose-fake:" + var1).getBytes(StandardCharsets.UTF_8));
      RemotePlayer var3 = new RemotePlayer(MINECRAFT_CLIENT.level, new GameProfile(var2, var1));

      while (MINECRAFT_CLIENT.level.getEntity(Аб) != null) {
         Аб--;
      }

      var3.setId(Аб--);
      var3.snapTo(MINECRAFT_CLIENT.player.getX(), MINECRAFT_CLIENT.player.getY(), MINECRAFT_CLIENT.player.getZ(), MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot());
      var3.yHeadRot = MINECRAFT_CLIENT.player.yHeadRot;
      var3.yBodyRot = MINECRAFT_CLIENT.player.yBodyRot;
      var3.setHealth(MINECRAFT_CLIENT.player.getHealth());
      var3.setAbsorptionAmount(MINECRAFT_CLIENT.player.getAbsorptionAmount());
      this.х(var3);
      MINECRAFT_CLIENT.level.addEntity(var3);
      this.с8.put(this.method_9_388008bb(var1), var3);
      this.衣 = MINECRAFT_CLIENT.level;
      return var3;
   }
}
