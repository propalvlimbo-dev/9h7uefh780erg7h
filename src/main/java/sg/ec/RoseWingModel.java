package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;
import sg.render.AvianWingAnimator;
import sg.render.WingAnimator;
import sg.render.WingModelPart;

/**
 * Крылья «Роза». Анатомия как у дефолтных птичьих крыльев: цепочка из четырёх
 * сегментов-«костей», на каждом висит пластина-«перо». Кости — колючие побеги
 * (дерево + шипы), нижние пластины — листва, верхние — лепестки розы, в узлах
 * сидят маленькие розы. Материал задаётся полосой UV (дерево / листва / лепестки),
 * цвет, прожилки, блик и искры считает шейдер rose_cosmetic.
 */
public final class RoseWingModel extends WingModel {
   private final WingModelPart root = createPart(0, 0);
   private final WingModelPart[] leftBones = new WingModelPart[4];
   private final WingModelPart[] rightBones = new WingModelPart[4];
   private final WingModelPart[][] leftPlates = new WingModelPart[4][];
   private final WingModelPart[][] rightPlates = new WingModelPart[4][];
   private final WingModelPart leftThorns = createPart(0, 28);
   private final WingModelPart rightThorns = createPart(0, 28);
   private final WingModelPart leftTwigs = createPart(8, 28);
   private final WingModelPart rightTwigs = createPart(8, 28);
   private final WingModelPart leftRoses = createPart(46, 50);
   private final WingModelPart rightRoses = createPart(46, 50);
   private static final int[] BONE_TEXTURE_V = new int[]{0, 8, 16, 24};
   private static final float PLATE_Z = -0.5F;

   public RoseWingModel() {
      for (int side = 0; side < 2; side++) {
         int sign = side == 0 ? 1 : -1;
         WingModelPart[] bones = side == 0 ? this.leftBones : this.rightBones;
         WingModelPart[][] plates = side == 0 ? this.leftPlates : this.rightPlates;

         bones[0] = bone(0.0F, -1.5F, -1.5F, 5, 3, 3, 1.5F, 5.5F, 2.5F, 0, sign);
         bones[1] = bone(-0.1F, -1.1F, -2.0F, 7, 3, 4, 4.7F, -0.6F, 0.1F, 1, sign);
         bones[2] = bone(0.0F, -1.5F, -1.5F, 9, 3, 3, 6.5F, 0.2F, 0.1F, 2, sign);
         bones[3] = bone(0.0F, -1.0F, -1.0F, 5, 2, 2, 8.5F, 0.0F, 0.0F, 3, sign);
         bones[0].addChild(bones[1]);
         bones[1].addChild(bones[2]);
         bones[2].addChild(bones[3]);

         plates[0] = plate(26, 20, 0.0F, 0.0F, 6, 8, 0.4F, 0.0F, 1.0F, sign);
         plates[1] = plate(26, 34, 0.0F, 0.0F, 10, 14, 0.0F, 1.5F, 1.0F, sign);
         plates[2] = plate(46, 20, -2.0F, 0.0F, 11, 12, 0.0F, 1.0F, 0.0F, sign);
         plates[3] = plate(46, 36, 0.0F, -2.1F, 11, 11, 0.0F, 0.0F, 0.0F, sign);
         addAll(bones[0], plates[0]);
         addAll(bones[1], plates[1]);
         addAll(bones[2], plates[2]);
         addAll(bones[3], plates[3]);

         WingModelPart thorns = side == 0 ? this.leftThorns : this.rightThorns;
         WingModelPart twigs = side == 0 ? this.leftTwigs : this.rightTwigs;
         WingModelPart roses = side == 0 ? this.leftRoses : this.rightRoses;
         spike(thorns, 1, 4, 2, sign);
         spike(thorns, 3, 2, 2, sign);
         spike(thorns, 5, 0, 2, sign);
         spike(thorns, 7, -1, 1, sign);
         spike(thorns, 9, -1, 1, sign);
         twig(twigs, 6, 2, 0, sign);
         twig(twigs, 10, -2, 0, sign);
         rose(roses, 3, 5, 1, sign);
         rose(roses, 7, 1, 1, sign);
         rose(roses, 11, -2, 1, sign);
         rose(roses, 14, -1, 0, sign);
         bones[2].addChild(thorns);
         bones[2].addChild(roses);
         bones[1].addChild(twigs);

         this.root.addChild(bones[0]);
      }
   }

   private static WingModelPart bone(float x, float y, float z, int width, int height, int depth, float pivotX, float pivotY, float pivotZ, int index, int sign) {
      WingModelPart part = createPart(0, BONE_TEXTURE_V[index]);
      part.addCuboid(sign > 0 ? x : -(x + width), y, z, width, height, depth);
      part.setPivot(sign > 0 ? pivotX : -pivotX, pivotY, pivotZ);
      return part;
   }

   /** «Перо»: полоса из узких лепестков разной длины — кромка получается рваной, как у птичьих крыльев. */
   private static WingModelPart[] plate(int textureU, int textureV, float x, float y, int width, int height, float pivotX, float pivotY, float pivotZ, int sign) {
      int strips = Math.max(2, width / 2);
      WingModelPart[] parts = new WingModelPart[strips];
      float step = (float)width / strips;

      for (int strip = 0; strip < strips; strip++) {
         float offset = strip * step;
         float wave = (float)Math.sin((strip + 1) * 1.9F + width * 0.7F);
         float taper = 1.0F - 0.32F * Math.abs((float)strip / Math.max(1, strips - 1) - 0.42F) * 2.0F;
         int stripHeight = Math.max(3, Math.round(height * taper * (0.86F + 0.16F * wave)));
         float minX = sign > 0 ? x + offset : -(x + offset + step);
         ExtrudedWingModelPart part = ExtrudedWingModelPart.create(minX, y, PLATE_Z, Math.max(1, (int)step), stripHeight, textureU, textureV, 64, 64);
         part.setPivot(sign > 0 ? pivotX : -pivotX, pivotY, pivotZ);
         parts[strip] = part;
      }

      return parts;
   }

   private static void addAll(WingModelPart parent, WingModelPart[] children) {
      for (WingModelPart child : children) {
         parent.addChild(child);
      }
   }

   private static void spike(WingModelPart part, int x, int y, int z, int sign) {
      part.addCuboid(sign > 0 ? x : -(x + 1), y, z, 1, 1, 1);
   }

   private static void twig(WingModelPart part, int x, int y, int z, int sign) {
      part.addCuboid(sign > 0 ? x : -(x + 3), y, z, 3, 1, 1);
   }

   private static void rose(WingModelPart part, int x, int y, int z, int sign) {
      part.addCuboid(sign > 0 ? x : -(x + 2), y, z, 2, 2, 2);
      part.addCuboid(sign > 0 ? x : -(x + 2), y - 1, z, 2, 1, 2);
      part.addCuboid(sign > 0 ? x - 1 : -(x + 1), y, z, 1, 2, 2);
   }

   @Override
   public void render(WingAnimator animator, float partialTick, PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      AvianWingAnimator avianAnimator = (AvianWingAnimator)animator;

      Vec3 secondary;

      for (int index = 0; index < this.leftBones.length; index++) {
         applyMirroredRotationDegrees(this.leftBones[index], this.rightBones[index], avianAnimator.getPrimaryRotationDegrees(index, partialTick));
         secondary = avianAnimator.getSecondaryRotationDegrees(index, partialTick);

         for (int strip = 0; strip < this.leftPlates[index].length; strip++) {
            applyMirroredRotationDegrees(this.leftPlates[index][strip], this.rightPlates[index][strip], secondary);
         }
      }

      this.root.render(matrices, vertices, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
