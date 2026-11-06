package sg.mixin;

import sg.mixinbridge.ModelBending;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPart.class)
public class ModelPartEmoteMixin {
   @Inject(method = "translateAndRotate", at = @At("HEAD"))
   private void applyEmoteUpperBodyBend(PoseStack var1, CallbackInfo var2) {
      ModelBending.в((ModelPart)(Object)this, var1);
   }
}
