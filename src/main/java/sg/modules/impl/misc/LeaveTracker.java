package sg.modules.impl.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.PlayerTeam;
import sg.events.impl.EntityRemovalEvent;
import sg.events.EventListener;
import sg.network.ClientChat;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;

public final class LeaveTracker extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("Я5")  EventListener<EntityRemovalEvent> entityRemovalListener = this::衣;
   private static final String MODULE_NAME = "LeaveTracker";
   private static final String MODULE_DESCRIPTION = "Отслеживает координаты, на которых пропадают дальние игроки";
   private static final float MIN_REPORT_DISTANCE = 250.0F;
   private static final String EXCLUDED_NAME_FRAGMENT = "CIT-";
   private static final String COORDINATES_MESSAGE = " замечен на координатах ";

   public LeaveTracker() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
   }

   private void 衣(EntityRemovalEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (var1.getEntity() instanceof AbstractClientPlayer var2) {
            if (!(var2 instanceof LocalPlayer) && var2 != MINECRAFT_CLIENT.player) {
               if (!(MINECRAFT_CLIENT.player.distanceTo(var2) < MIN_REPORT_DISTANCE)) {
                  String var9 = var2.getGameProfile().name();
                  if (var9 != null && !var9.isBlank() && !var9.contains(EXCLUDED_NAME_FRAGMENT)) {
                     int var4 = (int)var2.getX();
                     int var5 = (int)var2.getY();
                     int var6 = (int)var2.getZ();
                     PlayerTeam var7 = var2.getTeam();
                     MutableComponent var8 = Component.empty();
                     if (var7 != null) {
                        var8.append(var7.getPlayerPrefix());
                     }

                     var8.append(Component.literal(var9));
                     if (var7 != null) {
                        var8.append(var7.getPlayerSuffix());
                     }

                     var8.append(Component.literal(COORDINATES_MESSAGE).withStyle(ChatFormatting.GRAY));
                     var8.append(Component.literal(var4 + " " + var5 + " " + var6).withStyle(ChatFormatting.WHITE));
                     ClientChat.З(var8);
                  }
               }
            }
         }
      }
   }
}
