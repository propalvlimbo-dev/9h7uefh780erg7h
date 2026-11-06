package sg.mixinbridge;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.block.state.BlockState;

public final class HmiBlockItemRenderer implements BlockItemRenderBridge {
   public static final HmiBlockItemRenderer В_ccb1b0c5 = new HmiBlockItemRenderer();
   private static final BlockDisplayContext В_55b028ad = BlockDisplayContext.create();
   @SerializedName("В")
   private final BlockModelRenderState В_fa97164e = new BlockModelRenderState();
   private static final long ЯщЯ = 42L;
   private static final long ЯщХ = 42L;
   private static final int Яще = 15728880;

   @Override
   public void Й(BlockState var1, PoseStack var2, SubmitNodeCollector var3, int var4, ClientLevel var5, AbstractClientPlayer var6) {
      this.В_fa97164e.clear();
      Minecraft.getInstance().getModelManager().getBlockModelSet().get(var1).update(this.В_fa97164e, var1, В_55b028ad, ЯщХ);
      this.В_fa97164e.blockLightCoords = var1.emissiveRendering() ? Яще : LightCoordsUtil.pack(var1.getLightEmission(), 0);
      this.В_fa97164e.submit(var2, var3, var4, OverlayTexture.NO_OVERLAY, 0);
   }

   public void З(SubmitNodeCollector var1, PoseStack var2, BlockState var3, int var4, int var5, int var6) {
      this.В_fa97164e.clear();
      Minecraft.getInstance().getModelManager().getBlockModelSet().get(var3).update(this.В_fa97164e, var3, В_55b028ad, ЯщЯ);
      this.В_fa97164e.submit(var2, var1, var4, var5, var6);
   }
}
