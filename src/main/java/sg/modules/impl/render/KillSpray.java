package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
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
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import settings.render.ClientResourceIds;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.combat.Aura;
import sg.events.impl.AttackEntityEvent;
import sg.settings.FloatSetting;

public class KillSpray extends ClientModule {
   private static final long сС = 2500L;
   private final BooleanSetting сЯ;
   private final FloatSetting сШ;
   private final FloatSetting сы;
   @SerializedName("г")
   private final List<KillSpray.Spray> г_at_9653152b;
   @SerializedName("0")
   private final List<KillSpray.TrackedVictim> field_0_2571477d_at_9653152b;
   @SerializedName("0")
   private final Map<Integer, Long> field_0_ea411899_at_9653152b;
   private final RenderPipeline с诶;
   private final EventListener<AttackEntityEvent> сф;
   private final EventListener<PacketReceiveEvent> си;
   private final EventListener<PlayerTickEvent> с9;
   private final EventListener<WorldEffectsEvent> со;
   private static final String Янф = "KillSpray";
   private static final String Яни = "Спрей с логотипом Rose на месте килла";
   private static final String Ян9 = "Только игроки";
   private static final String Яно = "Размер";
   private static final float ЯнO = 1.2F;
   private static final float Ян7 = 0.5F;
   private static final float ЯнР = 2.5F;
   private static final float Ян3 = 0.05F;
   private static final String Янр = "Длительность";
   private static final float Ян6 = 5.0F;
   private static final float Ян_ = 12.0F;
   private static final float ЯнШ = 0.5F;
   private static final String Яны = "rose/killspray";
   private static final String Янй = "dead_chams";
    private static final String ЯнЖ = "client_logo_world";
   private static final String Янм = "Sampler0";
   private static final float ЯнК = 180.0F;
   private static final float ЯнI = 3.0F;
   private static final float ЯнТ = 0.65F;
   private static final float ЯнО = 0.65F;
   private static final float ЯнФ = 0.35F;
   private static final float ЯнЙ = 0.01F;
   private static final float Янк = 0.55F;
   private static final float Янг = 0.45F;
   private static final float Ян0 = 0.15F;
   private static final long Янх = 2500L;
   private static final long ЯнУ = 2500L;
   private static final double ЯнЗ = 0.55;
   private static final double ЯнЕ = 0.9;
   private static final double ЯнП = 4.0;
   private static final float Ян西 = 1000.0F;
   private static final long Янд = 750L;
   private static final String ЯнБ = "Sampler0";
   private static final String ЯнС = "killspray";
   private static final String Янз = "killSprayVertices";

   public KillSpray() {
      super(Янф, Яни, ModuleCategory.RENDER);
      this.сЯ = new BooleanSetting(Ян9, true);
      this.сШ = new FloatSetting(Яно, ЯнO, Ян7, ЯнР).withStep(Ян3);
      this.сы = new FloatSetting(Янр, Ян6, 1.0F, Ян_).withStep(ЯнШ);
      this.г_at_9653152b = new ArrayList<>();
      this.field_0_2571477d_at_9653152b = new ArrayList<>();
      this.field_0_ea411899_at_9653152b = new HashMap<>();
      this.с诶 = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(Яны)
         .withVertexShader(ClientResourceIds.getCoreId(Янй))
         .withFragmentShader(ClientResourceIds.getCoreId(ЯнЖ))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(Янм)
         .build();
      this.сф = this::衣;
      this.си = this::т;
      this.с9 = this::а;
      this.со = this::ъ;
      this.addSettings(this.сЯ, this.сШ, this.сы);
   }

   private synchronized void л(LivingEntity var1) {
      if (this.衣(var1)) {
         long var2 = System.currentTimeMillis();
         Long var4 = this.field_0_ea411899_at_9653152b.get(var1.getId());
         if (var4 == null || var4 <= var2) {
            Vec3 var5 = var1.position().add(0.0, Math.max(var1.getBbHeight() * ЯнЗ, ЯнЕ), 0.0);

            for (KillSpray.Spray var7 : this.г_at_9653152b) {
               if (var7.Н.distanceToSqr(var5) < ЯнП) {
                  return;
               }
            }

            long var8 = (long)(this.сы.getValue() * Ян西);
            this.field_0_ea411899_at_9653152b.put(var1.getId(), var2 + var8 + Янд);
            this.г_at_9653152b.clear();
            this.г_at_9653152b.add(new KillSpray.Spray(var5, this.сШ.getValue(), var8));
         }
      }
   }

   private void К(BufferBuilder var1) {
      MeshData var2 = var1.build();
      if (var2 != null) {
         GpuBuffer var3 = null;

         try {
            DrawState var4 = var2.drawState();
            int var5 = var4.indexCount();
            AutoStorageIndexBuffer var6 = RenderSystem.getSequentialBuffer(var4.primitiveTopology());
            GpuBuffer var7 = var6.getBuffer(var5);
            IndexType var8 = var6.type();
            var3 = RenderSystem.getDevice().createBuffer(() -> Янз, 32, var2.vertexBuffer());
            RenderTarget var9 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
             AbstractTexture var10 = sg.render.ClientLogo.font().getFontFace().getTexture();
            CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();

            try (RenderPass var12 = var11.createRenderPass(
                  () -> ЯнС, var9.getColorTextureView(), Optional.empty(), var9.getDepthTextureView(), OptionalDouble.empty()
               )) {
               RenderSystem.bindDefaultUniforms(var12);
               var12.setPipeline(this.с诶);
                var12.bindTexture(ЯнБ, var10.getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               var12.setVertexBuffer(0, var3.slice());
               var12.setIndexBuffer(var7, var8);
               var12.drawIndexed(var5, 1, 0, 0, 0);
            }
         } finally {
            if (var3 != null) {
               var3.close();
            }

            var2.close();
         }
      }
   }

   private void т(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundEntityEventPacket var2 && MINECRAFT_CLIENT.level != null) {
         if (var2.getEntity(MINECRAFT_CLIENT.level) instanceof LivingEntity var4 && this.衣(var4)) {
            if (var2.getEventId() == 3) {
               if (this.э(var4)) {
                  MINECRAFT_CLIENT.execute(() -> this.л(var4));
               }
            }
         }
      }
   }

   private void ы(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      var1.addVertex(var2, var3, var4, var5).setUv(var6, var7).setColor(1.0F, 1.0F, 1.0F, var8);
   }

   private void а(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         try {
            Aura var2 = RoseClient.getInstance().getServices().getModules().getAura();
            if (var2 != null && var2.isEnabled() && var2.Ж_95bedd6e() != null && var2.Ж_95bedd6e().isAlive()) {
               this.т(var2.Ж_95bedd6e());
            }
         } catch (Throwable var8) {
         }

         long var9 = System.currentTimeMillis();
         Iterator var4 = this.field_0_2571477d_at_9653152b.iterator();

         while (var4.hasNext()) {
            KillSpray.TrackedVictim var5 = (KillSpray.TrackedVictim)var4.next();
            if (!var5.и(var9)) {
               var4.remove();
            } else {
               LivingEntity var6 = var5.Ю;
               boolean var7 = MINECRAFT_CLIENT.level.getEntity(var6.getId()) == null;
               if (var6.getHealth() <= 0.0F || !var6.isAlive() || var7) {
                  this.л(var6);
               }
            }
         }

         this.г_at_9653152b.removeIf(var2x -> var9 - var2x.Ва > var2x.Вт);
         this.field_0_ea411899_at_9653152b.entrySet().removeIf(var2x -> var2x.getValue() < var9);
      } else {
         this.г_at_9653152b.clear();
         this.field_0_2571477d_at_9653152b.clear();
         this.field_0_ea411899_at_9653152b.clear();
      }
   }

   private boolean э(LivingEntity var1) {
      long var2 = System.currentTimeMillis();

      for (KillSpray.TrackedVictim var5 : this.field_0_2571477d_at_9653152b) {
         if (var5.Ю.getId() == var1.getId() && var5.и(var2)) {
            return true;
         }
      }

      return false;
   }

   private void 衣(AttackEntityEvent var1) {
      if (var1.getPhase() == AttackEntityEvent.Phase.POST && var1.getAttacker() == MINECRAFT_CLIENT.player) {
         if (var1.getTarget() instanceof LivingEntity var2 && var2.isAlive()) {
            this.т(var2);
         }
      }
   }

   private boolean 衣(LivingEntity var1) {
      if (var1 == null || var1 == MINECRAFT_CLIENT.player) {
         return false;
      } else {
         return this.сЯ.getValue() ? var1 instanceof Player : true;
      }
   }

   private void ъ(WorldEffectsEvent var1) {
      if (!this.г_at_9653152b.isEmpty() && MINECRAFT_CLIENT.gameRenderer != null) {
         Camera var2 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         long var3 = System.currentTimeMillis();

         for (KillSpray.Spray var6 : this.г_at_9653152b) {
            float var7 = (float)(var3 - var6.Ва);
            if (!(var7 < 0.0F) && !(var7 > (float)var6.Вт)) {
               float var8 = var7 / (float)var6.Вт;
               float var9 = Mth.clamp(var7 / ЯнК, 0.0F, 1.0F);
               var9 = var9 * var9 * (ЯнI - 2.0F * var9);
               float var10 = var8 > ЯнТ ? 1.0F - (var8 - ЯнО) / ЯнФ : 1.0F;
               float var11 = Mth.clamp(var9 * var10, 0.0F, 1.0F);
               if (!(var11 <= ЯнЙ)) {
                  float var12 = var6.еъ * (Янк + Янг * var9) * sg.render.ClientLogo.DISPLAY_SCALE;
                  Vec3 var13 = var6.Н;
                  PoseStack var14 = var1.getPoses();
                  var14.pushPose();
                  var14.translate(var13.x - var1.getCameraPosition().x, var13.y - var1.getCameraPosition().y, var13.z - var1.getCameraPosition().z);
                  var14.mulPose(Axis.YP.rotationDegrees(-var2.yRot()));
                  var14.translate(0.0F, Ян0, 0.0F);
                  Matrix4f var15 = var14.last().pose();
                  BufferBuilder var16 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                  float var17 = var12 / 2.0F;
                  this.ы(var16, var15, -var17, -var17, 0.0F, 0.0F, 1.0F, var11);
                  this.ы(var16, var15, -var17, var17, 0.0F, 0.0F, 0.0F, var11);
                  this.ы(var16, var15, var17, var17, 0.0F, 1.0F, 0.0F, var11);
                  this.ы(var16, var15, var17, -var17, 0.0F, 1.0F, 1.0F, var11);
                  var14.popPose();
                  this.К(var16);
               }
            }
         }
      }
   }

   private void т(LivingEntity var1) {
      if (this.衣(var1)) {
         long var2 = System.currentTimeMillis();

         for (KillSpray.TrackedVictim var5 : this.field_0_2571477d_at_9653152b) {
            if (var5.Ю.getId() == var1.getId()) {
               var5.ВЩ = var2 + Янх;
               return;
            }
         }

         this.field_0_2571477d_at_9653152b.add(new KillSpray.TrackedVictim(var1, var2 + ЯнУ));
      }
   }

   @Override
   public void onDisable() {
      this.г_at_9653152b.clear();
      this.field_0_2571477d_at_9653152b.clear();
      this.field_0_ea411899_at_9653152b.clear();
   }

   private static final class Spray {
      private final Vec3 Н;
      private final float еъ;
      private final long Вт;
      private final long Ва = System.currentTimeMillis();

      private Spray(Vec3 var1, float var2, long var3) {
         this.Н = var1;
         this.еъ = var2;
         this.Вт = var3;
      }
   }

   private static final class TrackedVictim {
      private final LivingEntity Ю;
      private long ВЩ;

      private boolean и(long var1) {
         return var1 <= this.ВЩ && this.Ю != null;
      }

      private TrackedVictim(LivingEntity var1, long var2) {
         this.Ю = var1;
         this.ВЩ = var2;
      }
   }
}
