package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import sg.render.CosmeticModel;
import sg.render.CosmeticModelRenderer;

public final class MaskCubeModel {
   private static final CosmeticModel В = К();
   private static final float ЯЮс = -5.0F;
   private static final float ЯЮВ = -1.0F;
   private static final float ЯЮЯ = -5.0F;
   private static final float ЯЮХ = 10.0F;
   private static final float ЯЮе = 10.0F;
   private static final float ЯЮА = 10.0F;
   private static final float ЯЮ4 = -0.4F;
   private static final float ЯЮп = -0.4F;
   private static final float ЯЮД = -0.4F;

   private MaskCubeModel() {
   }

   public static void К(PoseStack var0, VertexConsumer var1, int var2, int var3, float var4) {
      CosmeticModelRenderer.render(var0, var1, В, var2, var3, 1.0F, 1.0F, 1.0F, var4);
   }

   private static CosmeticModel К() {
      CosmeticModel var0 = new CosmeticModel();
      CosmeticModel.ModelBone var1 = new CosmeticModel.ModelBone();
      CosmeticModel.ModelCube var2 = new CosmeticModel.ModelCube();
      var2.originX = ЯЮс;
      var2.originY = ЯЮВ;
      var2.originZ = ЯЮЯ;
      var2.width = ЯЮХ;
      var2.height = ЯЮе;
      var2.depth = ЯЮА;
      var2.inflateX = ЯЮ4;
      var2.inflateY = ЯЮп;
      var2.inflateZ = ЯЮД;
      var1.cubes.add(var2);
      var0.rootBones.add(var1);
      return var0;
   }
}
