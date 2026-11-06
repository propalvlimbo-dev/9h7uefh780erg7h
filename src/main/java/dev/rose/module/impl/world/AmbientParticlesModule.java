package dev.rose.module.impl.world;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.module.Category;
import dev.rose.module.impl.cosmetic.CosmeticModule;
import dev.rose.render.RoseRenderTypes;
import dev.rose.render.world.CosmeticContext;
import dev.rose.render.world.CosmeticTextures;
import dev.rose.render.world.Geometry;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.ColorsSetting;
import dev.rose.setting.NumberSetting;
import java.util.Random;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;


public final class AmbientParticlesModule extends CosmeticModule {
   private static final int MAX = 600;
   private final ChoiceSetting<AmbientParticlesModule.Style> style = this.add(new ChoiceSetting<>("style", AmbientParticlesModule.Style.SAKURA));
   private final NumberSetting density = this.add(new NumberSetting("density", 160.0, 10.0, 600.0, 10.0, ""));
   private final NumberSetting size = this.add(new NumberSetting("size", 100.0, 30.0, 300.0, 5.0, "rose.unit.percent"));
   private final NumberSetting speed = this.add(new NumberSetting("speed", 100.0, 10.0, 300.0, 5.0, "rose.unit.percent"));
   private final NumberSetting wind = this.add(new NumberSetting("wind", 30.0, -100.0, 100.0, 5.0, ""));
   private final NumberSetting range = this.add(new NumberSetting("range", 14.0, 6.0, 32.0, 1.0, "rose.unit.blocks"));
   private final float[] x = new float[600];
   private final float[] y = new float[600];
   private final float[] z = new float[600];
   private final float[] spin = new float[600];
   private final float[] phase = new float[600];
   private final float[] scale = new float[600];
   private final Random random = new Random();
   private boolean seeded;
   private AmbientParticlesModule.Style seededStyle;

   public AmbientParticlesModule() {
      super("ambient_particles", Category.WORLD);
   }

   @Override
   public int icon() {
      return 57486;
   }

   @Override
   protected ColorsSetting.Mode defaultMode() {
      return ColorsSetting.Mode.GRADIENT;
   }

   @Override
   protected int defaultColor() {
      return -18487;
   }

   @Override
   protected int defaultSecondColor() {
      return -6932;
   }

   @Override
   protected void onDisable() {
      this.seeded = false;
   }

   private void seed(Vec3 camera, float box) {
      for (int i = 0; i < 600; i++) {
         this.respawn(i, camera, box, true);
      }

      this.seeded = true;
      this.seededStyle = this.style.get();
   }

   private void respawn(int i, Vec3 camera, float box, boolean anywhere) {
      boolean rising = this.style.get() == AmbientParticlesModule.Style.EMBERS || this.style.get() == AmbientParticlesModule.Style.BUBBLES;
      this.x[i] = (float)camera.x + (this.random.nextFloat() * 2.0F - 1.0F) * box;
      this.z[i] = (float)camera.z + (this.random.nextFloat() * 2.0F - 1.0F) * box;
      float top = (float)camera.y + box * 0.6F;
      float bottom = (float)camera.y - box * 0.4F;
      this.y[i] = anywhere ? Mth.lerp(this.random.nextFloat(), bottom, top) : (rising ? bottom : top);
      this.spin[i] = this.random.nextFloat() * (float) (Math.PI * 2);
      this.phase[i] = this.random.nextFloat() * 100.0F;
      this.scale[i] = 0.6F + this.random.nextFloat() * 0.8F;
   }

   public void render(CosmeticContext ctx) {
      Vec3 camera = ctx.cameraPos();
      float box = this.range.floatValue();
      if (!this.seeded || this.seededStyle != this.style.get()) {
         this.seed(camera, box);
      }

      float dt = ctx.delta();
      float time = ctx.time();
      float motion = this.speed.floatValue() / 100.0F;
      float drift = this.wind.floatValue() / 100.0F;
      int count = this.density.intValue();
      AmbientParticlesModule.Style current = this.style.get();

      Identifier texture = switch (current) {
         case SAKURA -> CosmeticTextures.petal();
         default -> CosmeticTextures.dot();
         case LEAVES -> CosmeticTextures.leaf();
         case BUBBLES -> CosmeticTextures.ring();
      };
      boolean glow = current == AmbientParticlesModule.Style.EMBERS
         || current == AmbientParticlesModule.Style.SNOW
         || current == AmbientParticlesModule.Style.BUBBLES;
      VertexConsumer consumer = ctx.buffers().getBuffer(glow ? RoseRenderTypes.glowSprite(texture) : RoseRenderTypes.softSprite(texture));
      PoseStack.Pose pose = ctx.pose().last();
      float baseSize = this.size.floatValue() / 100.0F;

      for (int i = 0; i < count; i++) {
         float fall = switch (current) {
            case SAKURA -> -0.55F;
            case SNOW -> -0.9F;
            case EMBERS -> 0.7F;
            case LEAVES -> -0.8F;
            case BUBBLES -> 0.45F;
         } * motion;
         float sway = Mth.sin(time * 1.3F + this.phase[i]) * 0.35F * motion;
         this.x[i] = this.x[i] + (drift * 0.9F + sway) * dt;
         this.z[i] = this.z[i] + Mth.cos(time * 0.9F + this.phase[i]) * 0.2F * motion * dt;
         this.y[i] = this.y[i] + fall * dt * (0.7F + 0.6F * this.scale[i] / 1.4F);
         this.spin[i] = this.spin[i] + dt * (1.2F + this.phase[i] % 1.5F) * motion;
         float dx = this.x[i] - (float)camera.x;
         float dz = this.z[i] - (float)camera.z;
         float dy = this.y[i] - (float)camera.y;
         if (!(dy < -box * 0.4F) && !(dy > box * 0.6F)) {
            if (Math.abs(dx) > box) {
               this.x[i] = this.x[i] - Math.signum(dx) * box * 2.0F;
            } else if (Math.abs(dz) > box) {
               this.z[i] = this.z[i] - Math.signum(dz) * box * 2.0F;
            }
         } else {
            this.respawn(i, camera, box, false);
         }

         Vector3f local = ctx.local(this.x[i], this.y[i], this.z[i]);
         float distance = local.length();
         float fade = Math.clamp((distance - 0.6F) / 1.2F, 0.0F, 1.0F) * Math.clamp((box - distance) / (box * 0.3F), 0.0F, 1.0F);
         if (!(fade <= 0.01F)) {
            float s = baseSize * this.scale[i] * switch (current) {
               case SAKURA, LEAVES -> 0.09F;
               case SNOW -> 0.05F;
               case EMBERS -> 0.035F;
               case BUBBLES -> 0.07F;
            };
            float twinkle = current == AmbientParticlesModule.Style.EMBERS ? 0.55F + 0.45F * Mth.sin(time * 7.0F + this.phase[i] * 3.0F) : 1.0F;
            int c = this.color(this.phase[i] % 1.0F, time, fade * twinkle * (current == AmbientParticlesModule.Style.SNOW ? 0.85F : 1.0F));
            float halfH = current != AmbientParticlesModule.Style.SAKURA && current != AmbientParticlesModule.Style.LEAVES
               ? s
               : s * (0.35F + 0.65F * Math.abs(Mth.sin(this.spin[i] * 0.7F)));
            Geometry.sprite(consumer, pose, local, ctx.right(), ctx.up(), s, halfH, this.spin[i], c);
         }
      }

      ctx.buffers().endBatch();
   }

   public enum Style {
      SAKURA,
      SNOW,
      EMBERS,
      LEAVES,
      BUBBLES;
   }
}
