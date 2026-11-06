package sg.ec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import net.minecraft.world.entity.player.Player;
import sg.events.impl.WorldEffectsEvent;
import sg.modules.impl.init.PetMotionAnimator;
import sg.render.CosmeticCategory;
import sg.render.CosmeticDefinition;
import sg.render.ClientCosmeticSelections;
import sg.events.EventListener;
import sg.events.impl.PlayerTickEvent;

public final class WorldPetCosmetics extends ClientService {
   private final Map<UUID, PetMotionAnimator> сД = new HashMap<>();
   private final Map<UUID, CosmeticDefinition> сЪ = new HashMap<>();
   private final EventListener<PlayerTickEvent> В8 = this::method_5Х;
   private final EventListener<WorldEffectsEvent> В5 = this::М;
   private static final float Вдя = 0.01F;

   private CosmeticDefinition О(Player var1) {
      CosmeticDefinition var2 = ClientCosmeticSelections.getCatalogModelForPlayer(var1, CosmeticCategory.PETS);
      return var2 != null && var2.attach().isWorldAttachment() ? var2 : null;
   }

   private void М(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.level != null) {
         for (Player var3 : MINECRAFT_CLIENT.level.players()) {
            if (var3 != null && !var3.isRemoved() && this.к(var3)) {
               PetMotionAnimator var4 = this.сД.get(var3.getUUID());
               CosmeticDefinition var5 = this.сЪ.get(var3.getUUID());
               if (var4 != null && var5 != null) {
                  float var6 = MINECRAFT_CLIENT.player != null && var3.getUUID().equals(MINECRAFT_CLIENT.player.getUUID()) ? ClientCosmeticSelections.getPetsVisibility() : 1.0F;
                  if (!(var6 < Вдя)) {
                     var4.ж(var1.getPoses(), var1.getGeometryBuffer(), var1.getCameraPosition(), var1.getTickDelta(), var5, var6);
                  }
               }
            }
         }
      }
   }

   private void method_5Х(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.level == null) {
         this.сД.values().forEach(PetMotionAnimator::Гс);
         this.сД.clear();
         this.сЪ.clear();
      } else {
         HashSet var2 = new HashSet();

         for (Player var4 : MINECRAFT_CLIENT.level.players()) {
            if (var4 != null && !var4.isRemoved() && this.к(var4)) {
               CosmeticDefinition var5 = this.О(var4);
               if (var5 != null) {
                  var2.add(var4.getUUID());
                  this.сЪ.put(var4.getUUID(), var5);
                  this.сД.computeIfAbsent(var4.getUUID(), var0 -> new PetMotionAnimator()).Г(var4);
               }
            }
         }

         this.сД.entrySet().removeIf(var2x -> {
            if (var2.contains(var2x.getKey())) {
               return false;
            }

            var2x.getValue().Гс();
            this.сЪ.remove(var2x.getKey());
            return true;
         });
      }
   }

   private boolean к(Player var1) {
      return ClientCosmeticSelections.shouldShowCosmeticsForPlayer(var1, MINECRAFT_CLIENT);
   }
}
