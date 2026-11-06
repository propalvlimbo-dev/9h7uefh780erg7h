package sg.modules.impl.init;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Generated;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import sg.ec.MinecraftAccess;
import sg.ec.ForcedPlayerTarget;
import sg.ec.EntityHealth;
import sg.client.RoseClient;
import sg.settings.SingleChoiceSetting;

public final class CombatTargetSelector implements MinecraftAccess {
   private static final String ВМТ = "Умная";
   private static final String ВМО = "This is a utility class and cannot be instantiated";
   private static final String ВМФ = "Дистанция";
   private static final String ВМЙ = "Броня";

   private static boolean method_4(LivingEntity var0, float var1) {
      if (var0 instanceof Player var2 && RoseClient.getInstance().getServices().getModules().getAntiBot().detectedBots.contains(var2)) {
         return false;
      } else {
         return !var0.isAlive() ? false : var0.distanceTo(MINECRAFT_CLIENT.player) <= var1;
      }
   }

   @Generated
   private CombatTargetSelector() {
      throw new UnsupportedOperationException(ВМО);
   }

   public static List<LivingEntity> method_4(float var0, boolean var1, boolean var2, boolean var3, boolean var4) {
      return new ArrayList<>(Л(var0, var1, var2, var3, var4));
   }

   private static List<LivingEntity> Л(float var0, boolean var1, boolean var2, boolean var3, boolean var4) {
      ArrayList var5 = new ArrayList();

      for (Entity var7 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
         if (var7 != MINECRAFT_CLIENT.player && !(var7 instanceof LivingEntity var8 && !method_4(var8, var0))) {
            if (ForcedPlayerTarget.method_3и()) {
               if (var7 instanceof Player var10 && ForcedPlayerTarget.ю(var10)) {
                  var5.add(var10);
               }
            } else if (var7 instanceof Player var11) {
               if (RoseClient.getInstance().getServices().getFriends().О(var11.getGameProfile().name())) {
                  if (var3) {
                     var5.add(var11);
                  }
               } else if (var11.getArmorValue() == 0) {
                  if (var2) {
                     var5.add(var11);
                  }
               } else if (var1) {
                  var5.add(var11);
               }
            } else if (var7 instanceof Mob var9 && var4) {
               var5.add(var9);
            }
         }
      }

      return var5;
   }

   public static LivingEntity method_4(LivingEntity var0, float var1, boolean var2, boolean var3, boolean var4, boolean var5, SingleChoiceSetting var6) {
      return var6.isSelected(ВМТ)
         ? Л(var1, var2, var3, var4, var5)
            .stream()
            .min(
               Comparator.<LivingEntity, Boolean>comparing(var1x -> var1x != var0)
                  .thenComparingDouble(LivingEntity::getArmorValue)
                  .thenComparingDouble(EntityHealth::getHealth)
                  .thenComparingDouble(var0x -> var0x.distanceTo(MINECRAFT_CLIENT.player))
                  .thenComparingInt(Entity::getId)
            )
            .orElse(null)
         : Л(var1, var2, var3, var4, var5)
            .stream()
            .min(
               Comparator.<LivingEntity, Boolean>comparing(var1x -> var1x != var0)
                  .thenComparingDouble(var1x -> var6.isSelected(ВМФ) ? var1x.distanceTo(MINECRAFT_CLIENT.player) : (var6.isSelected(ВМЙ) ? var1x.getArmorValue() : EntityHealth.getHealth(var1x)))
                  .thenComparingInt(Entity::getId)
            )
            .orElse(null);
   }
}
