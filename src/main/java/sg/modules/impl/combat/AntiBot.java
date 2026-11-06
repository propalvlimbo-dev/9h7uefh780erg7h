package sg.modules.impl.combat;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.player.Player;
import sg.ec.PlayerTeamPrefix;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.SingleChoiceSetting;

public class AntiBot extends ClientModule {
   @SerializedName("3")
   private final SingleChoiceSetting detectionModeSetting;
   private final @com.google.gson.annotations.SerializedName("с5")  BooleanSetting removeDetectedBotsSetting;
   public final @com.google.gson.annotations.SerializedName("с4")  List<Player> detectedBots;
   private final @com.google.gson.annotations.SerializedName("В1")  EventListener<PlayerTickEvent> playerTickListener;
   private static final String MODULE_NAME = "AntiBot";
   private static final String MODULE_DESCRIPTION = "Игнорирует ботов от античита";
   private static final String MODE_LABEL = "Мод";
   private static final String MATRIX_OPTION_LABEL = "Matrix";
   private static final String SLOTH_AC_OPTION_LABEL = "Sloth AC";
   private static final String REMOVE_BOT_LABEL = "Удалять бота";
   private static final String MATRIX_MODE_NAME = "Matrix";
   private static final String SLOTH_AC_MODE_NAME = "Sloth AC";

   private void дУ() {
      for (Player var2 : MINECRAFT_CLIENT.level.players()) {
         if (this.Я(var2)) {
            this.detectedBots.remove(var2);
         } else if (!this.detectedBots.contains(var2)) {
            String var3 = this.method_0(var2);
            if (var3 != null) {
               boolean var4 = !var2.getUUID().equals(UUIDUtil.createOfflinePlayerUUID(var3));
               if (var4) {
                  this.method__(var2);
               }
            }
         }
      }
   }

   private void method__(Player var1) {
      this.detectedBots.add(var1);
      if (this.removeDetectedBotsSetting.getValue()) {
         var1.remove(RemovalReason.DISCARDED);
      }
   }

   private void дв() {
      for (Player var2 : MINECRAFT_CLIENT.level.players()) {
         if (var2 != MINECRAFT_CLIENT.player && var2.isInvisible() && !this.detectedBots.contains(var2) && PlayerTeamPrefix.getPrefix(var2).getString().isEmpty()) {
            this.method__(var2);
         }
      }
   }

   public void чЮ(PlayerTickEvent var1) {
      if (this.detectionModeSetting.isSelected(MATRIX_MODE_NAME)) {
         this.дУ();
      } else if (this.detectionModeSetting.isSelected(SLOTH_AC_MODE_NAME)) {
         this.дв();
      }
   }

   private boolean Я(Player var1) {
      if (var1 != null && var1.getGameProfile() != null) {
         RoseClient var2 = RoseClient.getInstance();
         if (var2 != null && var2.getServices() != null && var2.getServices().getFriends() != null) {
            String var3 = var1.getGameProfile().name();
            return var3 != null && var2.getServices().getFriends().О(var3);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private String method_0(Player var1) {
      return var1 != null && var1.getGameProfile() != null ? var1.getGameProfile().name() : null;
   }

   public AntiBot() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.COMBAT);
      this.detectionModeSetting = new SingleChoiceSetting(MODE_LABEL, new ChoiceOption(MATRIX_OPTION_LABEL), new ChoiceOption(SLOTH_AC_OPTION_LABEL));
      this.removeDetectedBotsSetting = new BooleanSetting(REMOVE_BOT_LABEL, false);
      this.detectedBots = new ArrayList<>();
      this.playerTickListener = new EventListener<PlayerTickEvent>() {
         @Override
         public int getPriority() {
            return -1000;
         }

         public void onEvent(PlayerTickEvent var1) {
            AntiBot.this.чЮ(var1);
         }
      };
      this.addSettings(this.detectionModeSetting, this.removeDetectedBotsSetting);
   }

   @Override
   public void onEnable() {
      super.onEnable();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      if (!this.detectedBots.isEmpty()) {
         this.detectedBots.clear();
      }
   }
}
