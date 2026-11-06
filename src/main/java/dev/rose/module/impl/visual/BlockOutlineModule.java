package dev.rose.module.impl.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.module.Category;
import dev.rose.module.impl.cosmetic.CosmeticModule;
import dev.rose.render.RoseRenderTypes;
import dev.rose.render.Theme;
import dev.rose.render.world.CosmeticTextures;
import dev.rose.render.world.Geometry;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.NumberSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;


public final class BlockOutlineModule extends CosmeticModule {
   private final ChoiceSetting<BlockOutlineModule.Style> style = this.add(new ChoiceSetting<>("style", BlockOutlineModule.Style.GLASS));
   private final NumberSetting fill = this.add(new NumberSetting("fill", 34.0, 0.0, 90.0, 2.0, "rose.unit.percent"));
   private final NumberSetting width = this.add(new NumberSetting("width", 2.5, 1.0, 6.0, 0.5, "rose.unit.pixels"));
   private final NumberSetting glide = this.add(new NumberSetting("glide", 100.0, 0.0, 300.0, 10.0, "rose.unit.percent"));
   private final BoolSetting pulse = this.add(new BoolSetting("pulse", true));
   private final NumberSetting speed = this.add(new NumberSetting("speed", 100.0, 20.0, 300.0, 10.0, "rose.unit.percent"));
   private AABB shown;
   private float lastTime = -1.0F;
   private float appear;

   public BlockOutlineModule() {
      super("block_outline", Category.VISUALS);
      this.fill
         .visibleWhen(
            () -> this.style.get() == BlockOutlineModule.Style.FILL
               || this.style.get() == BlockOutlineModule.Style.GLASS
               || this.style.get() == BlockOutlineModule.Style.AURA
         );
   }

   @Override
   public int icon() {
      return 58660;
   }

   private boolean filled() {
      return (
            this.style.get() == BlockOutlineModule.Style.FILL
               || this.style.get() == BlockOutlineModule.Style.GLASS
               || this.style.get() == BlockOutlineModule.Style.AURA
         )
         && this.fill.get() > 0.0;
   }

   public void render(PoseStack poseStack, VoxelShape shape, BlockPos pos, double camX, double camY, double camZ) {
      if (!shape.isEmpty()) {
         float time = Theme.time();
         boolean fresh = this.lastTime < 0.0F || time - this.lastTime > 0.2F;
         float dt = fresh ? 0.0F : Math.clamp(time - this.lastTime, 0.0F, 0.1F);
         this.lastTime = time;
         AABB target = shape.bounds().move(pos);
         if (fresh || this.shown == null) {
            this.appear = 0.0F;
         }

         float rate = this.glide.floatValue() <= 0.0F ? 0.0F : 2200.0F / this.glide.floatValue();
         if (!fresh && this.shown != null && !(rate <= 0.0F) && !(this.shown.getCenter().distanceToSqr(target.getCenter()) > 36.0)) {
            double k = 1.0 - Math.exp(-rate * dt);
            this.shown = new AABB(
               Mth.lerp(k, this.shown.minX, target.minX),
               Mth.lerp(k, this.shown.minY, target.minY),
               Mth.lerp(k, this.shown.minZ, target.minZ),
               Mth.lerp(k, this.shown.maxX, target.maxX),
               Mth.lerp(k, this.shown.maxY, target.maxY),
               Mth.lerp(k, this.shown.maxZ, target.maxZ)
            );
         } else {
            this.shown = target;
         }

         this.appear = this.appear + (1.0F - this.appear) * (1.0F - (float)Math.exp(-14.0F * dt));
         if (dt == 0.0F && fresh) {
            this.appear = 0.35F;
         }

         float pace = this.speed.floatValue() / 100.0F;
         float breath = this.pulse.isOn() ? 0.8F + 0.2F * Mth.sin(time * 3.2F * pace) : 1.0F;
         float visible = Math.clamp(this.appear, 0.0F, 1.0F) * breath;
         double pop = 0.05 * (1.0F - this.appear);
         AABB frame = this.shown.inflate(0.002 + pop);
         List<AABB> boxes = new ArrayList<>();
         shape.forAllBoxes((x0, y0, z0, x1, y1, z1) -> boxes.add(map(new AABB(x0, y0, z0, x1, y1, z1).move(pos), target, frame)));
         List<BlockOutlineModule.Edge> edges = new ArrayList<>();
         shape.forAllEdges(
            (x0, y0, z0, x1, y1, z1) -> {
               AABB a = map(new AABB(x0, y0, z0, x0, y0, z0).move(pos), target, frame);
               AABB b = map(new AABB(x1, y1, z1, x1, y1, z1).move(pos), target, frame);
               edges.add(
                  new BlockOutlineModule.Edge(
                     a.minX - camX, a.minY - camY, a.minZ - camZ, b.minX - camX, b.minY - camY, b.minZ - camZ
                  )
               );
            }
         );
         MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
         PoseStack.Pose pose = poseStack.last();
         double height = Math.max(1.0E-4, frame.getYsize());
         if (this.filled()) {
            float strength = this.fill.floatValue() / 100.0F * visible;
            VertexConsumer faces = buffers.getBuffer(RoseRenderTypes.FACES);
            boolean glass = this.style.get() == BlockOutlineModule.Style.GLASS;

            for (AABB box : boxes) {
               float low = (float)((box.minY - frame.minY) / height);
               float high = (float)((box.maxY - frame.minY) / height);
               int bottom = this.color(low * 0.5F, time, strength * (glass ? 0.85F : 1.0F));
               int top = this.color(high * 0.5F, time, strength * (glass ? 0.55F : 1.0F));
               int lid = this.color(high * 0.5F, time, Math.min(1.0F, strength * (glass ? 1.5F : 1.0F)));
               box(faces, pose, box, camX, camY, camZ, bottom, top, lid);
            }

            buffers.endBatch(RoseRenderTypes.FACES);
         }

         RenderType lineType = RoseRenderTypes.lines();
         VertexConsumer lines = Geometry.lines(buffers.getBuffer(lineType), this.width.floatValue());
         float lineStrength = Math.min(1.0F, visible * (this.style.get() == BlockOutlineModule.Style.FILL ? 0.7F : 1.0F));

         for (BlockOutlineModule.Edge edge : edges) {
            switch ((BlockOutlineModule.Style)this.style.get()) {
               case CORNERS:
                  this.segment(lines, pose, edge, 0.0, 0.24, time, lineStrength, frame, camY);
                  this.segment(lines, pose, edge, 0.76, 1.0, time, lineStrength, frame, camY);
                  break;
               case DASH:
                  double length = Math.sqrt(sq(edge.x1 - edge.x0) + sq(edge.y1 - edge.y0) + sq(edge.z1 - edge.z0));
                  if (!(length < 1.0E-4)) {
                     double dash = 0.13 / length;
                     double period = 0.23 / length;
                     double shift = time * 0.55 * pace % 0.23 / length;

                     for (double start = shift - period; start < 1.0; start += period) {
                        double from = Math.max(0.0, start);
                        double to = Math.min(1.0, start + dash);
                        if (to > from) {
                           this.segment(lines, pose, edge, from, to, time, lineStrength, frame, camY);
                        }
                     }
                  }
                  break;
               default:
                  this.segment(lines, pose, edge, 0.0, 1.0, time, lineStrength, frame, camY);
            }
         }

         buffers.endBatch(lineType);
         if (this.style.get() == BlockOutlineModule.Style.GLASS) {
            this.sheen(buffers, pose, frame, camX, camY, camZ, time, pace, visible);
         }

         if (this.style.get() == BlockOutlineModule.Style.AURA) {
            this.aura(buffers, pose, frame, camX, camY, camZ, time, pace, visible);
         }
      }
   }

   private static double sq(double value) {
      return value * value;
   }

   private static AABB map(AABB part, AABB from, AABB to) {
      return new AABB(
         axis(part.minX, from.minX, from.maxX, to.minX, to.maxX),
         axis(part.minY, from.minY, from.maxY, to.minY, to.maxY),
         axis(part.minZ, from.minZ, from.maxZ, to.minZ, to.maxZ),
         axis(part.maxX, from.minX, from.maxX, to.minX, to.maxX),
         axis(part.maxY, from.minY, from.maxY, to.minY, to.maxY),
         axis(part.maxZ, from.minZ, from.maxZ, to.minZ, to.maxZ)
      );
   }

   private static double axis(double value, double fromMin, double fromMax, double toMin, double toMax) {
      double size = fromMax - fromMin;
      return size < 1.0E-6 ? toMin + (value - fromMin) : toMin + (value - fromMin) / size * (toMax - toMin);
   }

   private static void box(VertexConsumer consumer, PoseStack.Pose pose, AABB box, double camX, double camY, double camZ, int bottom, int top, int lid) {
      float x0 = (float)(box.minX - camX);
      float y0 = (float)(box.minY - camY);
      float z0 = (float)(box.minZ - camZ);
      float x1 = (float)(box.maxX - camX);
      float y1 = (float)(box.maxY - camY);
      float z1 = (float)(box.maxZ - camZ);
      Vector3f a = new Vector3f(x0, y0, z0);
      Vector3f b = new Vector3f(x1, y0, z0);
      Vector3f c = new Vector3f(x1, y0, z1);
      Vector3f d = new Vector3f(x0, y0, z1);
      Vector3f e = new Vector3f(x0, y1, z0);
      Vector3f f = new Vector3f(x1, y1, z0);
      Vector3f g = new Vector3f(x1, y1, z1);
      Vector3f h = new Vector3f(x0, y1, z1);
      Geometry.glowQuad(consumer, pose, a, b, f, e, bottom, bottom, top, top);
      Geometry.glowQuad(consumer, pose, b, c, g, f, bottom, bottom, top, top);
      Geometry.glowQuad(consumer, pose, c, d, h, g, bottom, bottom, top, top);
      Geometry.glowQuad(consumer, pose, d, a, e, h, bottom, bottom, top, top);
      Geometry.glowQuad(consumer, pose, e, f, g, h, lid, lid, lid, lid);
      Geometry.glowQuad(consumer, pose, a, b, c, d, bottom, bottom, bottom, bottom);
   }

   private void segment(
      VertexConsumer lines, PoseStack.Pose pose, BlockOutlineModule.Edge edge, double from, double to, float time, float strength, AABB frame, double camY
   ) {
      float ax = (float)Mth.lerp(from, edge.x0, edge.x1);
      float ay = (float)Mth.lerp(from, edge.y0, edge.y1);
      float az = (float)Mth.lerp(from, edge.z0, edge.z1);
      float bx = (float)Mth.lerp(to, edge.x0, edge.x1);
      float by = (float)Mth.lerp(to, edge.y0, edge.y1);
      float bz = (float)Mth.lerp(to, edge.z0, edge.z1);
      float dx = bx - ax;
      float dy = by - ay;
      float dz = bz - az;
      float length = Mth.sqrt(dx * dx + dy * dy + dz * dz);
      if (!(length < 1.0E-5F)) {
         dx /= length;
         dy /= length;
         dz /= length;
         double height = Math.max(1.0E-4, frame.getYsize());
         int colorA = this.color((float)((ay + camY - frame.minY) / height) * 0.5F, time, strength);
         int colorB = this.color((float)((by + camY - frame.minY) / height) * 0.5F, time, strength);
         lines.addVertex(pose, ax, ay, az).setColor(colorA).setNormal(pose, dx, dy, dz);
         lines.addVertex(pose, bx, by, bz).setColor(colorB).setNormal(pose, dx, dy, dz);
      }
   }

   private void sheen(MultiBufferSource.BufferSource buffers, PoseStack.Pose pose, AABB frame, double camX, double camY, double camZ, float time, float pace, float visible) {
      float cycle = time * 0.32F * pace % 1.7F;
      if (!(cycle > 1.25F)) {
         float p = cycle - 0.25F;
         float band = 0.16F;
         float slant = 0.3F;
         VertexConsumer glow = buffers.getBuffer(RoseRenderTypes.GLOW);
         int bright = this.color(0.25F, time, visible * 0.34F);
         int clear = bright & 16777215;
         double grow = 0.004;
         float x0 = (float)(frame.minX - grow - camX);
         float y0 = (float)(frame.minY - camY);
         float z0 = (float)(frame.minZ - grow - camZ);
         float x1 = (float)(frame.maxX + grow - camX);
         float y1 = (float)(frame.maxY - camY);
         float z1 = (float)(frame.maxZ + grow - camZ);
         float[][] sides = new float[][]{{x0, z0, x1, z0}, {x1, z0, x1, z1}, {x1, z1, x0, z1}, {x0, z1, x0, z0}};

         for (float[] side : sides) {
            for (int half = 0; half < 2; half++) {
               float startLow = Math.clamp(half == 0 ? p - band : p, 0.0F, 1.0F);
               float endLow = Math.clamp(half == 0 ? p : p + band, 0.0F, 1.0F);
               float startHigh = Math.clamp(startLow + slant, 0.0F, 1.0F);
               float endHigh = Math.clamp(endLow + slant, 0.0F, 1.0F);
               if (!(endLow <= startLow) || !(endHigh <= startHigh)) {
                  Vector3f a = new Vector3f(Mth.lerp(startLow, side[0], side[2]), y0, Mth.lerp(startLow, side[1], side[3]));
                  Vector3f b = new Vector3f(Mth.lerp(endLow, side[0], side[2]), y0, Mth.lerp(endLow, side[1], side[3]));
                  Vector3f c = new Vector3f(Mth.lerp(endHigh, side[0], side[2]), y1, Mth.lerp(endHigh, side[1], side[3]));
                  Vector3f d = new Vector3f(Mth.lerp(startHigh, side[0], side[2]), y1, Mth.lerp(startHigh, side[1], side[3]));
                  if (half == 0) {
                     Geometry.glowQuad(glow, pose, a, b, c, d, clear, bright, bright, clear);
                  } else {
                     Geometry.glowQuad(glow, pose, a, b, c, d, bright, clear, clear, bright);
                  }
               }
            }
         }

         buffers.endBatch(RoseRenderTypes.GLOW);
      }
   }

   private void aura(MultiBufferSource.BufferSource buffers, PoseStack.Pose pose, AABB frame, double camX, double camY, double camZ, float time, float pace, float visible) {
      VertexConsumer glow = buffers.getBuffer(RoseRenderTypes.GLOW);

      for (int shell = 1; shell <= 3; shell++) {
         float wave = 0.5F + 0.5F * Mth.sin(time * 2.4F * pace - shell * 0.9F);
         double grow = 0.012 + 0.045 * shell * (0.55 + 0.45 * wave);
         int bottom = this.color(0.1F * shell, time, visible * 0.26F / shell);
         int top = this.color(0.1F * shell + 0.4F, time, visible * 0.1F / shell);
         box(glow, pose, frame.inflate(grow), camX, camY, camZ, bottom, top, top);
      }

      buffers.endBatch(RoseRenderTypes.GLOW);
      Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
      Vector3f right = new Vector3f(camera.leftVector()).negate();
      Vector3f up = new Vector3f(camera.upVector());
      VertexConsumer dots = buffers.getBuffer(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));
      double grow = 0.03;
      double x0 = frame.minX - grow;
      double x1 = frame.maxX + grow;
      double z0 = frame.minZ - grow;
      double z1 = frame.maxZ + grow;
      double sizeX = x1 - x0;
      double sizeZ = z1 - z0;
      double perimeter = 2.0 * (sizeX + sizeZ);

      for (int ring = 0; ring < 2; ring++) {
         double y = ring == 0 ? frame.maxY + 0.02 : frame.minY + 0.02;

         for (int i = 0; i < 3; i++) {
            for (int tail = 0; tail < 7; tail++) {
               double u = (time * 0.22 * pace * (ring == 0 ? 1 : -1) + i / 3.0 - tail * 0.012 * (ring == 0 ? 1 : -1)) % 1.0;
               if (u < 0.0) {
                  u++;
               }

               double along = u * perimeter;
               double x;
               double z;
               if (along < sizeX) {
                  x = x0 + along;
                  z = z0;
               } else if (along < sizeX + sizeZ) {
                  x = x1;
                  z = z0 + (along - sizeX);
               } else if (along < 2.0 * sizeX + sizeZ) {
                  x = x1 - (along - sizeX - sizeZ);
                  z = z1;
               } else {
                  x = x0;
                  z = z1 - (along - 2.0 * sizeX - sizeZ);
               }

               float fade = 1.0F - tail / 7.0F;
               Vector3f at = new Vector3f((float)(x - camX), (float)(y - camY), (float)(z - camZ));
               Geometry.dotSprite(
                  dots, pose, at, right, up, 0.05F * (0.4F + 0.6F * fade), this.color(i / 3.0F + ring * 0.5F, time, visible * fade * fade * 0.9F)
               );
               if (tail == 0) {
                  Geometry.dotSprite(dots, pose, at, right, up, 0.022F, 16777215 | Math.round(230.0F * visible * this.opacity()) << 24);
               }
            }
         }
      }

      buffers.endBatch(RoseRenderTypes.glowSprite(CosmeticTextures.dot()));
   }

   private record Edge(double x0, double y0, double z0, double x1, double y1, double z1) {
   }

   public enum Style {
      OUTLINE,
      FILL,
      GLASS,
      AURA,
      CORNERS,
      DASH;
   }
}
