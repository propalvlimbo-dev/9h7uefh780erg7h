package dev.rose.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.rose.module.Modules;
import dev.rose.module.impl.cosmetic.CapeModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {
   @ModifyReturnValue(method = "getSkin", at = @At("RETURN"))
   private PlayerSkin rose$ownCape(PlayerSkin skin) {
      if ((Object)this != Minecraft.getInstance().player) {
         return skin;
      }

      CapeModule cape = Modules.cape();
      Identifier texture = cape != null ? cape.texture() : null;
      return texture == null ? skin : new PlayerSkin(skin.body(), new ClientAsset.ResourceTexture(texture, texture), skin.elytra(), skin.model(), skin.secure());
   }
}
