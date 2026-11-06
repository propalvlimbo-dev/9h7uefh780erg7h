package sg.manager;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import sg.annotation.LuaExport;
import sg.mixinbridge.HmiSwingStateAccess;

public class LuaPlayerAccess {
   @LuaExport("isCrawling")
   public boolean isCrawling(AbstractClientPlayer player) {
      return player.isVisuallyCrawling();
   }

   @LuaExport("isItemCoolingDown")
   public boolean isItemCoolingDown(ItemStack itemStack, AbstractClientPlayer player) {
      return player.getCooldowns().isOnCooldown(itemStack);
   }

   @LuaExport("isUsingRiptide")
   public boolean isUsingRiptide(AbstractClientPlayer player) {
      return player.isAutoSpinAttack();
   }

   @LuaExport("Ъ")
   public double Ъ_3f39d5c3(AbstractClientPlayer var1) {
      return var1.getX();
   }

   @LuaExport("getPitch")
   public double getPitch(AbstractClientPlayer player) {
      return player.getXRot();
   }

   @LuaExport("И")
   public double И_3f39d5c3(AbstractClientPlayer var1) {
      return var1.getDeltaMovement().x();
   }

   @LuaExport("getSpeed")
   public double getSpeed(AbstractClientPlayer player) {
      return player.getDeltaMovement().length();
   }

   @LuaExport("э")
   public double э_3f39d5c3(AbstractClientPlayer var1) {
      return var1.getDeltaMovement().z();
   }

   @LuaExport("getMainItem")
   public ItemStack getMainItem(AbstractClientPlayer player) {
      return player.getMainHandItem();
   }

   @LuaExport("getOffhandItem")
   public ItemStack getOffhandItem(AbstractClientPlayer player) {
      return player.getOffhandItem();
   }

   @LuaExport("getYaw")
   public double getYaw(AbstractClientPlayer player) {
      return player.getYRot();
   }

   @LuaExport("isOnGround")
   public boolean isOnGround(AbstractClientPlayer player) {
      return player.onGround();
   }

   @LuaExport("У")
   public boolean У_bbeebd87(AbstractClientPlayer var1) {
      return var1.isScoping();
   }

   @LuaExport("Г")
   public boolean Г_bbeebd87(AbstractClientPlayer var1, int var2) {
      return var1.isPassenger();
   }

   @LuaExport("Г")
   public double Г_3f39d5c3(AbstractClientPlayer var1) {
      return var1.getHealth();
   }

   @LuaExport("Я")
   public boolean Я_bbeebd87(AbstractClientPlayer var1) {
      return var1.isUnderWater();
   }

   @LuaExport("getSwingCount")
   public double getSwingCount(AbstractClientPlayer player) {
      return player instanceof HmiSwingStateAccess var2 ? var2.hMI5_0$getSwingCount() : 0.0;
   }

   @LuaExport("8")
   public double method_8_3f39d5c3(AbstractClientPlayer var1) {
      return var1.getY();
   }

   @LuaExport("isUsingItem")
   public boolean isUsingItem(AbstractClientPlayer player) {
      return player.isUsingItem();
   }

   @LuaExport("isSneaking")
   public boolean isSneaking(AbstractClientPlayer player) {
      return player.isShiftKeyDown();
   }

   @LuaExport("getActiveHand")
   public InteractionHand getActiveHand(AbstractClientPlayer player) {
      return player.getUsedItemHand();
   }

   @LuaExport("isSwimming")
   public boolean isSwimming(AbstractClientPlayer player) {
      return player.isSwimming();
   }

   @LuaExport("Г")
   public String Г_388008bb(AbstractClientPlayer var1, int var2) {
      return var1.level().getBlockState(var1.blockPosition().below().below(var2)).getBlock().builtInRegistryHolder().getRegisteredName();
   }

   @LuaExport("Ч")
   public double Ч_3f39d5c3(AbstractClientPlayer var1) {
      return var1.getZ();
   }

   @LuaExport("getAge")
   public int getAge(AbstractClientPlayer player) {
      return player.tickCount;
   }

   @LuaExport("isClimbing")
   public boolean isClimbing(AbstractClientPlayer player) {
      return player.onClimbable();
   }

   @LuaExport
   public String Ъ(AbstractClientPlayer var1, int var2) {
      return var1.level().getBlockState(var1.blockPosition().above().above().above(var2)).getBlock().builtInRegistryHolder().getRegisteredName();
   }

   @LuaExport("getYSpeed")
   public double getYSpeed(AbstractClientPlayer player) {
      return player.getDeltaMovement().y();
   }

   @LuaExport("isTouchingWater")
   public boolean isTouchingWater(AbstractClientPlayer player) {
      return player.isInWater();
   }

   @LuaExport("Г")
   public String Г_388008bb(AbstractClientPlayer var1) {
      return var1.level().getBlockState(var1.blockPosition().below()).getBlock().builtInRegistryHolder().getRegisteredName();
   }
}
