package sg.modules.impl.render;

import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import settings.render.ClientResourceIds;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.settings.BooleanSetting;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

/**
 * Cherry blossom vortex around the current target: tumbling petals and whole blossoms orbit the
 * entity and, when the target is lost, keep their momentum and rain down to the ground.
 */
public final class Sakura extends ClientModule {
   private static final Identifier PETAL_TEXTURE = Identifier.fromNamespaceAndPath("rose", "textures/targetesp/sakura_petal.png");
   private static final Identifier FLOWER_TEXTURE = Identifier.fromNamespaceAndPath("rose", "textures/targetesp/sakura_flower.png");
   private static final Identifier GLOW_TEXTURE = Identifier.fromNamespaceAndPath("rose", "images/fireflies/bloom.png");
   private static final float TWO_PI = (float) (Math.PI * 2.0);
   private static final int ORBIT_PETALS = 42;
   private static final int ORBIT_FLOWERS = 5;
   private static final float GRAVITY = 2.6F;
   private static final float DRAG = 1.4F;
   private static final float TERMINAL_FALL = 1.15F;
   private static final float GROUND_LIFE = 2.6F;
   private static final float FREE_LIFE = 7.0F;
   private static final int PETAL_TINT = 0xFFFFD2E1;
   private static final int GLOW_COLOR = 0xFFFF9EC4;

   private final FloatSetting radiusSetting;
   private final FloatSetting sizeSetting;
   private final FloatSetting opacitySetting;
   private final FloatSetting rangeSetting;
   private final BooleanSetting throughWallsSetting;
   private final BooleanSetting themeColorSetting;
   private final ColorSetting colorSetting;

   private final RenderPipeline petalPipeline;
   private final RenderPipeline petalThroughPipeline;
   private final RenderPipeline glowPipeline;
   private final RenderPipeline glowThroughPipeline;

   private final List<Particle> particles = new ArrayList<>();
   private final Matrix4f matrixScratch = new Matrix4f();
   private final EventListener<WorldEffectsEvent> worldListener;

   private LivingEntity attached;
   private long lastFrame;
   private float time;

   public Sakura() {
      super("Sakura", "Сакура вокруг цели: лепестки и цветы", ModuleCategory.RENDER);
      this.radiusSetting = new FloatSetting("Радиус", 1.0F, 0.4F, 2.2F).withStep(0.05F);
      this.sizeSetting = new FloatSetting("Размер", 1.0F, 0.4F, 2.0F).withStep(0.05F);
      this.opacitySetting = new FloatSetting("Прозрачность", 0.85F, 0.15F, 1.0F).withStep(0.05F);
      this.rangeSetting = new FloatSetting("Дальность", 12.0F, 4.0F, 32.0F).withStep(1.0F);
      this.throughWallsSetting = new BooleanSetting("Сквозь стены", false);
      this.themeColorSetting = new BooleanSetting("Цвет темы", true);
      this.colorSetting = new ColorSetting("Цвет", false, GLOW_COLOR).visibleWhen(() -> !this.themeColorSetting.getValue());
      this.petalPipeline = this.buildPipeline(CompareOp.GREATER_THAN_OR_EQUAL, BlendFunction.TRANSLUCENT, "sakura_petals");
      this.petalThroughPipeline = this.buildPipeline(CompareOp.ALWAYS_PASS, BlendFunction.TRANSLUCENT, "sakura_petals_through");
      this.glowPipeline = this.buildPipeline(CompareOp.GREATER_THAN_OR_EQUAL, BlendFunction.ADDITIVE, "sakura_glow");
      this.glowThroughPipeline = this.buildPipeline(CompareOp.ALWAYS_PASS, BlendFunction.ADDITIVE, "sakura_glow_through");
      this.worldListener = this::onWorldEffects;
      this.addSettings(
         this.radiusSetting,
         this.sizeSetting,
         this.opacitySetting,
         this.rangeSetting,
         this.throughWallsSetting,
         this.themeColorSetting,
         this.colorSetting
      );
   }

   private RenderPipeline buildPipeline(CompareOp depth, BlendFunction blend, String name) {
      return RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation("rose/pipeline/" + name)
         .withVertexShader(ClientResourceIds.getCoreId("target_esp_orbit_glow"))
         .withFragmentShader(ClientResourceIds.getCoreId("target_esp_orbit_glow"))
         .withSampler("Sampler0")
         .withDepthTest(depth)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(blend)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
   }

   @Override
   public void onEnable() {
      this.reset();
   }

   @Override
   public void onDisable() {
      this.reset();
      super.onDisable();
   }

   private void reset() {
      this.particles.clear();
      this.attached = null;
      this.lastFrame = 0L;
      this.time = 0.0F;
   }

   /** Detaches the current target so the petals are released instead of snapping to a new one. */
   public void clearTarget() {
      this.attached = null;
   }

   private void onWorldEffects(WorldEffectsEvent event) {
      if (MINECRAFT_CLIENT.level == null || MINECRAFT_CLIENT.player == null) {
         this.reset();
         return;
      }

      if (this.isDrivenByAura()) {
         return;
      }

      this.renderTarget(event, this.findTarget(), this.currentColor(), this.throughWallsSetting.getValue());
   }

   private boolean isDrivenByAura() {
      return false;
   }

   private int currentColor() {
      return this.themeColorSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.colorSetting.getValue();
   }

   /**
    * Renders the vortex around an externally supplied target.
    */
   public void renderTarget(WorldEffectsEvent event, LivingEntity target, int color, boolean throughWalls) {
      if (MINECRAFT_CLIENT.level == null || MINECRAFT_CLIENT.player == null) {
         this.reset();
         return;
      }

      float partialTick = event.getDeltaTracker().getGameTimeDeltaPartialTick(true);
      this.update(partialTick, target);
      if (this.particles.isEmpty()) return;

      Camera camera = MINECRAFT_CLIENT.gameRenderer == null ? null : MINECRAFT_CLIENT.gameRenderer.mainCamera();
      if (camera == null) return;

      Vec3 cam = camera.position();
      Matrix4f base = event.getPoses().last().pose();
      float yaw = (float) Math.toRadians(-camera.yRot());
      float pitch = (float) Math.toRadians(camera.xRot());

      Matrix4f glowMatrix = new Matrix4f();
      BufferBuilder glow = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int glowVertices = 0;
      for (Particle p : this.particles) {
         float alpha = p.alpha * p.fade * (p.flower ? 0.22F : 0.08F);
         if (alpha <= 0.01F || p.grounded) continue;
         glowMatrix.set(base)
            .translate((float) (p.x - cam.x), (float) (p.y - cam.y), (float) (p.z - cam.z))
            .rotateY(yaw)
            .rotateX(pitch);
         this.quad(glow, glowMatrix, p.size * 2.6F, p.size * 2.6F, GLOW_COLOR, alpha);
         glowVertices += 4;
      }
      this.flush(glow, glowVertices, throughWalls ? this.glowThroughPipeline : this.glowPipeline, GLOW_TEXTURE, "sakuraGlow");

      float scale = this.sizeSetting.getValue();
      float opacity = this.opacitySetting.getValue();
      this.drawLayer(base, cam, false, PETAL_TEXTURE, scale, opacity, color, throughWalls);
      this.drawLayer(base, cam, true, FLOWER_TEXTURE, scale, opacity, color, throughWalls);
   }

   private void drawLayer(Matrix4f base, Vec3 cam, boolean flowers, Identifier texture, float scale, float opacity, int color, boolean throughWalls) {
      BufferBuilder buffer = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int vertices = 0;
      for (Particle p : this.particles) {
         if (p.flower != flowers) continue;
         float alpha = p.alpha * p.fade * opacity;
         if (alpha <= 0.01F) continue;
         this.matrixScratch.set(base).translate((float) (p.x - cam.x), (float) (p.y - cam.y), (float) (p.z - cam.z));
         if (p.grounded) {
            this.matrixScratch.rotateX(Mth.HALF_PI).rotateZ((float) Math.toRadians(p.spinA));
         } else {
            this.matrixScratch
               .rotateY((float) Math.toRadians(p.spinA))
               .rotateX((float) Math.toRadians(p.spinB))
               .rotateZ(Mth.sin(this.time * 2.0F + p.phase) * 0.6F);
         }
         int tint = flowers ? 0xFFFFFFFF : mixColor(0xFFFFFFFF, color & 0x00FFFFFF | 0xFF000000, p.tint);
         tint = mixColor(tint, PETAL_TINT, 0.35F);
         float width = p.size * scale;
         float height = width * (flowers ? 1.0F : 1.1F);
         this.quad(buffer, this.matrixScratch, width, height, tint, alpha);
         vertices += 4;
      }
      this.flush(buffer, vertices, throughWalls ? this.petalThroughPipeline : this.petalPipeline, texture, "sakuraPetals");
   }

   private void update(float tickDelta, LivingEntity target) {
      long now = System.nanoTime();
      float dt = this.lastFrame == 0L ? 0.016F : Math.clamp((now - this.lastFrame) / 1.0E9F, 0.001F, 0.05F);
      this.lastFrame = now;
      this.time += dt;

      if (target != this.attached) {
         if (this.attached != null) this.release();
         this.attached = target;
         if (target != null) this.spawnOrbit();
      }

      Vec3 center = null;
      float height = 1.8F;
      float width = 0.6F;
      if (this.attached != null) {
         center = new Vec3(
            Mth.lerp(tickDelta, this.attached.xo, this.attached.getX()),
            Mth.lerp(tickDelta, this.attached.yo, this.attached.getY()),
            Mth.lerp(tickDelta, this.attached.zo, this.attached.getZ())
         );
         height = this.attached.getBbHeight();
         width = this.attached.getBbWidth();
      }

      for (int i = this.particles.size() - 1; i >= 0; i--) {
         Particle p = this.particles.get(i);
         if (p.orbiting && center != null) {
            this.orbit(p, center, height, width, dt);
         } else {
            p.orbiting = false;
            this.fall(p, dt);
         }
         if (p.dead) this.particles.remove(i);
      }
   }

   private LivingEntity findTarget() {
      double range = this.rangeSetting.getValue();
      double bestDistance = range * range;
      LivingEntity best = null;
      for (Player player : MINECRAFT_CLIENT.level.players()) {
         if (player == MINECRAFT_CLIENT.player || player == this.attached || !player.isAlive()) continue;
         double distance = player.distanceToSqr(MINECRAFT_CLIENT.player);
         if (distance > bestDistance) continue;
         bestDistance = distance;
         best = player;
      }
      return best;
   }

   private void spawnOrbit() {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      this.particles.removeIf(p -> p.orbiting);
      float radius = this.radiusSetting.getValue();
      int total = ORBIT_PETALS + ORBIT_FLOWERS;
      for (int i = 0; i < total; i++) {
         Particle p = new Particle();
         p.flower = i >= ORBIT_PETALS;
         p.orbiting = true;
         p.angle = random.nextFloat() * TWO_PI;
         p.level = i / (float) total + random.nextFloat() * 0.05F;
         p.angularSpeed = (1.0F + random.nextFloat() * 0.9F) * (p.flower ? 0.6F : 1.0F);
         p.sink = 0.07F + random.nextFloat() * 0.06F;
         p.radiusScale = radius * (p.flower ? 1.25F + random.nextFloat() * 0.15F : 0.95F + random.nextFloat() * 0.3F);
         p.phase = random.nextFloat() * TWO_PI;
         p.size = p.flower ? 0.24F + random.nextFloat() * 0.06F : 0.11F + random.nextFloat() * 0.06F;
         p.spinA = random.nextFloat() * 360.0F;
         p.spinB = random.nextFloat() * 360.0F;
         p.spinSpeedA = (random.nextFloat() - 0.5F) * (p.flower ? 90.0F : 420.0F);
         p.spinSpeedB = (random.nextFloat() - 0.5F) * (p.flower ? 60.0F : 360.0F);
         p.tint = random.nextFloat();
         p.alpha = 0.0F;
         this.particles.add(p);
      }
   }

   /** Spiral that slowly sinks around the target and wraps back to the top. */
   private void orbit(Particle p, Vec3 center, float height, float width, float dt) {
      p.angle += p.angularSpeed * dt;
      p.level -= p.sink * dt;
      if (p.level < 0.0F) {
         p.level += 1.0F;
         p.alpha = 0.0F;
      }
      p.alpha = Math.min(1.0F, p.alpha + dt * 1.8F);
      float edge = Math.min(1.0F, Math.min(p.level / 0.08F, (1.0F - p.level) / 0.08F));
      p.fade = Mth.clamp(edge, 0.0F, 1.0F);
      double radius = width * 0.62F * p.radiusScale * (1.0F + 0.1F * Mth.sin(this.time * 1.7F + p.phase));
      double prevX = p.x;
      double prevY = p.y;
      double prevZ = p.z;
      p.x = center.x + Math.cos(p.angle) * radius;
      p.z = center.z + Math.sin(p.angle) * radius;
      p.y = center.y + 0.1 + p.level * height * 1.05 + Mth.sin(this.time * 2.3F + p.phase) * 0.05;
      if (p.placed) {
         p.vx = (p.x - prevX) / dt;
         p.vy = (p.y - prevY) / dt;
         p.vz = (p.z - prevZ) / dt;
      }
      p.placed = true;
      p.spinA += p.spinSpeedA * dt;
      p.spinB += p.spinSpeedB * dt;
   }

   /** Free petals: air drag, slow gravity and a sideways flutter, then lying on the ground until they fade. */
   private void fall(Particle p, float dt) {
      p.age += dt;
      if (p.grounded) {
         p.groundTime += dt;
         p.fade = 1.0F - Mth.clamp((p.groundTime - GROUND_LIFE) / 0.8F, 0.0F, 1.0F);
         if (p.fade <= 0.0F) p.dead = true;
         return;
      }
      float drag = (float) Math.exp(-DRAG * dt);
      p.vx *= drag;
      p.vz *= drag;
      p.vy = Math.max(-TERMINAL_FALL, p.vy * drag - GRAVITY * dt);
      double sway = Mth.sin(this.time * 3.1F + p.phase) * 0.55;
      p.x += (p.vx + Math.cos(p.phase) * sway) * dt;
      p.y += p.vy * dt;
      p.z += (p.vz + Math.sin(p.phase) * sway) * dt;
      p.spinA += p.spinSpeedA * dt;
      p.spinB += p.spinSpeedB * dt;
      p.alpha = Math.min(1.0F, p.alpha + dt * 3.0F);
      if (p.y <= p.groundY) {
         p.y = p.groundY;
         p.grounded = true;
         p.spinB = 0.0F;
      }
      p.fade = 1.0F - Mth.clamp((p.age - FREE_LIFE) / 1.0F, 0.0F, 1.0F);
      if (p.fade <= 0.0F) p.dead = true;
   }

   /** The vortex lets go: every orbiting petal keeps its momentum and drifts down to the ground. */
   private void release() {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      double sumX = 0.0;
      double sumZ = 0.0;
      double minY = Double.MAX_VALUE;
      int count = 0;
      for (Particle p : this.particles) {
         if (!p.orbiting) continue;
         sumX += p.x;
         sumZ += p.z;
         minY = Math.min(minY, p.y);
         count++;
      }
      if (count == 0) return;
      double ground = this.findGround(sumX / count, minY + 0.5, sumZ / count);
      for (Particle p : this.particles) {
         if (!p.orbiting) continue;
         p.orbiting = false;
         p.vx = p.vx * 0.55 + (random.nextDouble() - 0.5) * 0.6;
         p.vz = p.vz * 0.55 + (random.nextDouble() - 0.5) * 0.6;
         p.vy = 0.3 + random.nextDouble() * 0.6;
         p.groundY = ground;
         p.age = 0.0F;
         p.alpha = Math.max(p.alpha * p.fade, 0.4F);
         p.fade = 1.0F;
      }
   }

   private double findGround(double gx, double fromY, double gz) {
      if (MINECRAFT_CLIENT.level == null) return fromY - 1.0;
      int startY = Mth.floor(fromY);
      for (int by = startY; by > startY - 10; by--) {
         BlockPos pos = BlockPos.containing(gx, by, gz);
         BlockState state = MINECRAFT_CLIENT.level.getBlockState(pos);
         VoxelShape shape = state.getCollisionShape(MINECRAFT_CLIENT.level, pos);
         if (!shape.isEmpty()) {
            double top = by + shape.max(Axis.Y);
            if (top <= fromY + 0.05) return top + 0.015;
         }
      }
      return fromY - 1.0;
   }

   private void quad(BufferBuilder buffer, Matrix4f matrix, float width, float height, int color, float alpha) {
      float r = (color >> 16 & 0xFF) / 255.0F;
      float g = (color >> 8 & 0xFF) / 255.0F;
      float b = (color & 0xFF) / 255.0F;
      float a = Mth.clamp(alpha, 0.0F, 1.0F);
      float hw = width * 0.5F;
      float hh = height * 0.5F;
      buffer.addVertex(matrix, -hw, -hh, 0.0F).setUv(0.0F, 1.0F).setColor(r, g, b, a);
      buffer.addVertex(matrix, hw, -hh, 0.0F).setUv(1.0F, 1.0F).setColor(r, g, b, a);
      buffer.addVertex(matrix, hw, hh, 0.0F).setUv(1.0F, 0.0F).setColor(r, g, b, a);
      buffer.addVertex(matrix, -hw, hh, 0.0F).setUv(0.0F, 0.0F).setColor(r, g, b, a);
   }

   private void flush(BufferBuilder buffer, int vertices, RenderPipeline pipeline, Identifier texture, String label) {
      MeshData mesh = buffer.build();
      if (mesh == null) return;
      if (vertices == 0) {
         mesh.close();
         return;
      }
      GpuBuffer vertexBuffer = null;
      try {
         DrawState state = mesh.drawState();
         int indexCount = state.indexCount();
         AutoStorageIndexBuffer sequential = RenderSystem.getSequentialBuffer(state.primitiveTopology());
         GpuBuffer indexBuffer = sequential.getBuffer(indexCount);
         IndexType indexType = sequential.type();
         vertexBuffer = RenderSystem.getDevice().createBuffer(() -> label + "Vertices", 32, mesh.vertexBuffer());
         RenderTarget target = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
         AbstractTexture textureObject = MINECRAFT_CLIENT.getTextureManager().getTexture(texture);
         try (RenderPass pass = encoder.createRenderPass(
            () -> label, target.getColorTextureView(), Optional.empty(), target.getDepthTextureView(), OptionalDouble.empty()
         )) {
            RenderSystem.bindDefaultUniforms(pass);
            pass.setPipeline(pipeline);
            pass.bindTexture("Sampler0", textureObject.getTextureView(), textureObject.getSampler());
            pass.setVertexBuffer(0, vertexBuffer.slice());
            pass.setIndexBuffer(indexBuffer, indexType);
            pass.drawIndexed(indexCount, 1, 0, 0, 0);
         }
      } finally {
         if (vertexBuffer != null) vertexBuffer.close();
         mesh.close();
      }
   }

   private static int mixColor(int from, int to, float t) {
      int r = Math.round((from >> 16 & 0xFF) + ((to >> 16 & 0xFF) - (from >> 16 & 0xFF)) * t);
      int g = Math.round((from >> 8 & 0xFF) + ((to >> 8 & 0xFF) - (from >> 8 & 0xFF)) * t);
      int b = Math.round((from & 0xFF) + ((to & 0xFF) - (from & 0xFF)) * t);
      return 0xFF000000 | r << 16 | g << 8 | b;
   }

   private static final class Particle {
      double x;
      double y;
      double z;
      double vx;
      double vy;
      double vz;
      double groundY;
      float angle;
      float level;
      float angularSpeed;
      float sink;
      float radiusScale;
      float phase;
      float size;
      float spinA;
      float spinB;
      float spinSpeedA;
      float spinSpeedB;
      float tint;
      float alpha;
      float fade = 1.0F;
      float age;
      float groundTime;
      boolean flower;
      boolean orbiting;
      boolean grounded;
      boolean dead;
      boolean placed;
   }
}
