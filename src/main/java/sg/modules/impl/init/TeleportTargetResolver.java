package sg.modules.impl.init;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class TeleportTargetResolver {
   private static final int Аь = 8;
   private static final int Аж = 4;
   private static final double ВЛ = 0.29;
   private static final double В诶 = 1.0;
   private static final double В1 = 1.8;
   private final Minecraft п;
   private static final double В6弟 = Double.MAX_VALUE;
   private static final double В6б = 0.5;
   private static final double В6Ы = 0.5;
   private static final double В6l = 0.5;
   private static final double В68 = 0.5;
   private static final double В65 = 0.29;
   private static final double В6в = 0.29;
   private static final double В6ъ = 0.29;
   private static final double В6М = 1.8;
   private static final double В6Н = 0.29;
   private static final double В6н = 0.5;
   private static final double В6Ц = 0.5;

   private boolean Й(BlockPos var1) {
      if (this.п.level != null && this.п.level.hasChunk(var1.getX() >> 4, var1.getZ() >> 4)) {
         BlockState var2 = this.п.level.getBlockState(var1);
         if (var2.getCollisionShape(this.п.level, var1).isEmpty()) {
            return false;
         }

         double var3 = var1.getX() + В6l;
         double var5 = var1.getZ() + В68;
         AABB var7 = new AABB(var3 - В65, var1.getY() + 1.0, var5 - В6в, var3 + В6ъ, var1.getY() + В6М, var5 + В6Н);
         return this.п.level.noCollision(this.п.player, var7);
      } else {
         return false;
      }
   }

   public TeleportTargetResolver(Minecraft var1) {
      this.п = var1;
   }

   private Optional<AbstractClientPlayer> method_3(String var1) {
      return this.п.level == null
         ? Optional.empty()
         : this.п.level.players().stream().filter(var1x -> var1x.getName().getString().equalsIgnoreCase(var1)).findFirst();
   }

   public CompletableFuture<Suggestions> У(SuggestionsBuilder var1) {
      if (this.п.getConnection() == null) {
         return var1.buildFuture();
      }

      String var2 = var1.getRemaining().toLowerCase(Locale.ROOT);
      this.п
         .getConnection()
         .getOnlinePlayers()
         .stream()
         .map(PlayerInfo::getProfile)
         .map(GameProfile::name)
         .filter(var1x -> var1x != null && var1x.toLowerCase(Locale.ROOT).startsWith(var2))
         .distinct()
         .sorted(String.CASE_INSENSITIVE_ORDER)
         .forEach(var1::suggest);
      return var1.buildFuture();
   }

   public Optional<Vec3> щ(String var1) {
      return this.п.player == null
         ? Optional.empty()
         : this.method_3(var1).map(this::西).map(var0 -> new Vec3(var0.getX() + В6н, var0.getY(), var0.getZ() + В6Ц));
   }

   private BlockPos 西(AbstractClientPlayer var1) {
      BlockPos var2 = var1.blockPosition();
      BlockPos var3 = null;
      double var4 = В6弟;

      for (int var6 = -4; var6 <= 4; var6++) {
         for (int var7 = -8; var7 <= 8; var7++) {
            for (int var8 = -8; var8 <= 8; var8++) {
               BlockPos var9 = var2.offset(var7, var6, var8);
               if (this.Й(var9)) {
                  double var10 = var9.getX() + В6б;
                  double var12 = var9.getZ() + В6Ы;
                  double var14 = var10 - var1.getX();
                  double var16 = var9.getY() - var1.getY();
                  double var18 = var12 - var1.getZ();
                  double var20 = var14 * var14 + var16 * var16 + var18 * var18;
                  if (var20 < var4) {
                     var3 = var9;
                     var4 = var20;
                  }
               }
            }
         }
      }

      return var3;
   }

   public boolean Ф(Vec3 var1) {
      return this.п.player != null && var1 != null && Double.isFinite(var1.x) && Double.isFinite(var1.y) && Double.isFinite(var1.z);
   }

   public void Ъ(Vec3 var1) {
      this.п.player.setPos(var1.x, var1.y, var1.z);
   }

   public Optional<Vec3> т(String var1) {
      return this.method_3(var1).map(Entity::position);
   }
}
