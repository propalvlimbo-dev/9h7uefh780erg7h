package sg.ec;

import com.mojang.blaze3d.textures.GpuTextureView;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material.Baked;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class ClientTextureLookup {
   private static final ItemStackRenderState В_1c9f7a98 = new ItemStackRenderState();
   private static final RandomSource В_f9c21f3a = RandomSource.create();
   private static final String сЧХ = "This is a utility class and cannot be instantiated";

   public static Identifier Н_c635a640(LivingEntity var0) {
      if (var0 instanceof AbstractClientPlayer var6) {
         return var6.getSkin().body().texturePath();
      } else {
         Minecraft var1 = Minecraft.getInstance();
         EntityRenderer var2 = var1.getEntityRenderDispatcher().getRenderer(var0);
         if (var2 instanceof LivingEntityRenderer var3) {
            float var4 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
            LivingEntityRenderState var5 = (LivingEntityRenderState)var2.createRenderState(var0, var4);
            return var3.getTextureLocation(var5);
         } else {
            return null;
         }
      }
   }

   @Generated
   private ClientTextureLookup() {
      throw new UnsupportedOperationException(сЧХ);
   }

   public static GpuTextureView Н_dd184401(LivingEntity var0) {
      Identifier var1 = Н_c635a640(var0);
      return var1 == null ? null : Minecraft.getInstance().getTextureManager().getTexture(var1).getTextureView();
   }

   public static Identifier Н(ItemStack var0) {
      Minecraft var1 = Minecraft.getInstance();
      var1.getItemModelResolver().updateForTopItem(В_1c9f7a98, var0, ItemDisplayContext.GUI, var1.level, null, 0);
      Baked var2 = В_1c9f7a98.pickParticleMaterial(В_f9c21f3a);
      TextureAtlasSprite var3 = var2 == null ? null : var2.sprite();
      if (var3 == null) {
         return null;
      }

      Identifier var4 = var3.contents().name();
      return Identifier.fromNamespaceAndPath(var4.getNamespace(), "textures/" + var4.getPath() + ".png");
   }
}
