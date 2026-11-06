package sg.manager;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.RegistryOps;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.world.item.component.ItemAttributeModifiers.Entry;
import net.minecraft.world.item.component.KineticWeapon.Condition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import sg.annotation.LuaExport;
import sg.mixinbridge.HmiScriptState;
import sg.mixinbridge.HmiItemStackAccess;

public class LuaItemAccess {
   private static <T> void е(ItemStack var0, ItemStack var1, DataComponentType<T> var2) {
      if (var0.has(var2)) {
         var1.set(var2, var0.get(var2));
      }
   }

   @LuaExport("getSpearData")
   public SpearImpactState getSpearData(ItemStack itemStack) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      KineticWeapon var3 = itemStack.get(DataComponents.KINETIC_WEAPON);
      int var4 = itemStack.getUseDuration(var2) - (var2.getUseItemRemainingTicks() + 1);
      int var5 = var3.delayTicks();
      boolean var6 = var4 < var3.dismountConditions().map(Condition::maxDurationTicks).orElse(0) + var5;
      boolean var7 = var4 < var3.knockbackConditions().map(Condition::maxDurationTicks).orElse(0) + var5;
      boolean var8 = var4 < var3.damageConditions().map(Condition::maxDurationTicks).orElse(0) + var5;
      boolean var9 = var2.getTicksSinceLastKineticHitFeedback(0.0F) == 1.0F;
      return new SpearImpactState(var8, var7, var6, var9);
   }

   @LuaExport
   public ItemStack е(Item var1) {
      return var1.getDefaultInstance();
   }

   @LuaExport("isThrowable")
   public boolean isThrowable(ItemStack itemStack) {
      return itemStack.getItem() instanceof SplashPotionItem || itemStack.getItem() instanceof ProjectileItem;
   }

   @LuaExport("isBlock")
   public boolean isBlock(ItemStack itemStack) {
      return Block.byItem(itemStack.getItem()) != Blocks.AIR;
   }

   @LuaExport("isLantern")
   public boolean isLantern(ItemStack itemStack) {
      return Block.byItem(itemStack.getItem()) instanceof LanternBlock;
   }

   @LuaExport
   public void е(ItemStack var1, boolean var2) {
      ((HmiItemStackAccess)(Object)var1).hMI5_0$setTransform(var2);
   }

   @LuaExport
   public boolean У(ItemStack var1) {
      if (Block.byItem(var1.getItem()) == Blocks.AIR) {
         return false;
      }

      int var2 = ((HmiItemStackAccess)(Object)var1).hMI5_0$getRenderAsBlock();
      return var2 == 1 || var2 == -1;
   }

   @LuaExport
   public boolean ю(ItemStack var1) {
      return var1.isEnchanted();
   }

   @LuaExport("е")
   public static LuaJsonObject е_a1727c08(ItemStack var0) {
      RegistryOps<JsonElement> var1 = RegistryOps.create(JsonOps.INSTANCE, Minecraft.getInstance().level.registryAccess());
      DataComponentMap var2 = var0.getComponents();
      JsonElement var3 = DataComponentMap.CODEC.encodeStart(var1, var2).getOrThrow();
      return new LuaJsonObject(var3.getAsJsonObject());
   }

   @LuaExport("isEmpty")
   public boolean isEmpty(ItemStack itemStack) {
      return itemStack.isEmpty();
   }

   @LuaExport
   public void 衣(Item var1) {
      HmiScriptState.Я_a4db5d8e = var1.getDefaultInstance();
   }

   @LuaExport
   public void В(Item var1) {
      HmiScriptState.В_a4db5d8e = var1.getDefaultInstance();
   }

   @LuaExport("isChargedCrossbow")
   public boolean isChargedCrossbow(ItemStack itemStack) {
      return CrossbowItem.isCharged(itemStack);
   }

   @LuaExport
   public void В(ItemStack var1) {
      ItemStack var2 = var1.getItem().getDefaultInstance();
      е(var1, var2, DataComponents.ENCHANTMENTS);
      е(var1, var2, DataComponents.ITEM_MODEL);
      е(var1, var2, DataComponents.CUSTOM_MODEL_DATA);
      е(var1, var2, DataComponents.CUSTOM_DATA);
   }

   @LuaExport
   public String я(ItemStack var1) {
      return var1.getCustomName() != null ? var1.getCustomName().getString() : var1.getHoverName().getString();
   }

   @LuaExport("isCustomTranslate")
   public boolean isCustomTranslate(ItemStack itemStack) {
      return HmiScriptState.Х_e12d7b95.getOrDefault(this.getName(itemStack), false);
   }

   @LuaExport("shouldTranslateItem")
   public boolean shouldTranslateItem(ItemStack itemStack) {
      int var2 = ((HmiItemStackAccess)(Object)itemStack).hMI5_0$getTransform();
      return var2 == 0
         ? false
         : !itemStack.is(ConventionalItemTags.RODS)
               && !itemStack.is(ConventionalItemTags.TOOLS)
               && !itemStack.is(ItemTags.SWORDS)
               && !itemStack.is(ConventionalItemTags.MACE_TOOLS)
               && itemStack.getUseAnimation() != ItemUseAnimation.BLOCK
               && !(this.е_f67ab10a(itemStack) > 0.0F)
            || itemStack.getUseAnimation() == ItemUseAnimation.EAT
            || itemStack.getUseAnimation() == ItemUseAnimation.DRINK
            || itemStack.getUseAnimation() == ItemUseAnimation.SPYGLASS;
   }

   @LuaExport("getUseAction")
   public String getUseAction(ItemStack itemStack) {
      return itemStack.getUseAnimation().getSerializedName();
   }

   @LuaExport("isIn")
   public boolean isIn(ItemStack itemStack, TagKey<Item> tag) {
      return itemStack.is(tag);
   }

   @LuaExport
   public void е(ItemStack var1, double var2) {
      ((HmiItemStackAccess)(Object)var1).hMI5_0$setSwingSpeed((int)var2);
   }

   @LuaExport("е")
   public float е_f67ab10a(ItemStack var1) {
      ItemAttributeModifiers var2 = var1.getComponents().get(DataComponents.ATTRIBUTE_MODIFIERS);
      if (var2 == null) {
         return 0.0F;
      }

      float var3 = 0.0F;

      for (Entry var5 : var2.modifiers()) {
         if (var5.attribute().value() == Attributes.ATTACK_DAMAGE.value()) {
            var3 += (float)var5.modifier().amount();
         }
      }

      return var3;
   }

   @LuaExport("isOf")
   public boolean isOf(ItemStack itemStack, Item item) {
      return itemStack.is(item);
   }

   @LuaExport
   public void М(ItemStack var1, boolean var2) {
      if (Block.byItem(var1.getItem()) != Blocks.AIR) {
         ((HmiItemStackAccess)(Object)var1).hMI5_0$setRenderAsBlock(var2);
      }
   }

   @LuaExport("getName")
   public String getName(ItemStack itemStack) {
      return itemStack.getItem().toString();
   }
}
