package sg.mixin;

import net.minecraft.client.resources.model.cuboid.ItemModelGenerator;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import sg.ec.ItemModelGapFix;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorGapFixMixin {
   @ModifyArgs(
      method = "bakeSideFaces",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/resources/model/cuboid/FaceBakery;bakeQuad(Lnet/minecraft/client/resources/model/ModelBaker$Interner;Lorg/joml/Vector3fc;Lorg/joml/Vector3fc;Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;Lcom/mojang/math/Quadrant;Lnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;Lnet/minecraft/core/Direction;Lnet/minecraft/client/renderer/block/dispatch/ModelState;Lnet/minecraft/client/resources/model/cuboid/CuboidRotation;)Lnet/minecraft/client/resources/model/geometry/BakedQuad;"
      )
   )
   private static void modelGapFix$fixLayerElements(Args var0) {
      Vector3f var1 = new Vector3f((org.joml.Vector3fc)var0.get(1));
      Vector3f var2 = new Vector3f((org.joml.Vector3fc)var0.get(2));
      ItemModelGapFix.К(var1, var2, var0.get(6));
      var0.set(1, var1);
      var0.set(2, var2);
   }
}
