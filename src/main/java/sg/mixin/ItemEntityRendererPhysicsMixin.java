package sg.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.item.ItemEntity;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.render.ItemPhysics;
import sg.settings.EntityEspSettingsPanel;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererPhysicsMixin {
   @Unique
   private static final Set<ItemEntityRenderState> rose$vanillaPreviewStates = Collections.newSetFromMap(new WeakHashMap<>());
   private static final float Х弟ъ = -90.0F;

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At("RETURN")
   )
   private void fixUniqueOffset(ItemEntity var1, ItemEntityRenderState var2, float var3, CallbackInfo var4) {
      boolean var5 = EntityEspSettingsPanel.В(var1);
      if (var5) {
         rose$vanillaPreviewStates.add(var2);
      } else {
         rose$vanillaPreviewStates.remove(var2);
      }

      if (ItemPhysics.пК() && !var5) {
         var2.bobOffset = 0.0F;
      }
   }

   @Redirect(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 0)
   )
   private void killBob(
      PoseStack var1, float var2, float var3, float var4, ItemEntityRenderState var5, PoseStack var6, SubmitNodeCollector var7, CameraRenderState var8
   ) {
      if (!rose$useItemPhysics(var5)) {
         var1.translate(var2, var3, var4);
      } else {
         var1.translate(var2, 0.0F, var4);
      }
   }

   @Redirect(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V")
   )
   private void replaceRotation(PoseStack var1, Quaternionfc var2, ItemEntityRenderState var3, PoseStack var4, SubmitNodeCollector var5, CameraRenderState var6) {
      if (!rose$useItemPhysics(var3)) {
         var1.mulPose(var2);
      } else {
         var1.mulPose(Axis.XP.rotationDegrees(Х弟ъ));
      }
   }

   @Unique
   private static boolean rose$useItemPhysics(ItemEntityRenderState var0) {
      return ItemPhysics.пК() && !rose$vanillaPreviewStates.contains(var0);
   }
}
