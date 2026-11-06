package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
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
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.hud.HudScaleCoordinates;
import sg.ec.ClientFonts;
import sg.events.impl.WorldEffectsEvent;
import sg.handler.Marker;
import sg.handler.PartyClient;
import sg.handler.PartySession;
import sg.modules.impl.render.Rain;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

public final class PartyMarker implements MinecraftAccess {
   @SerializedName("А")
   private final LinkedHashMap<UUID, PartyMarker.MarkerRenderState> А_d5b8fa95_at_bf703e30 = new LinkedHashMap<>();
   @SerializedName("5")
   private final Matrix4f field_5 = new Matrix4f();
   private final Matrix4f в = new Matrix4f();
   private final Vector4f Ч = new Vector4f();
   private final Vector4f Л = new Vector4f();
   @SerializedName("А")
   private final Vector3f А_f5cce25e_at_bf703e30 = new Vector3f();
   private final RenderPipeline ск;
   private final RenderPipeline сг;
   private GpuBuffer Щ;
   private GpuBuffer у;
   @SerializedName("4П")
   private int field_4П;
   @SerializedName("4西")
   private int field_4西;
   private ClientLevel б;
   private long ХХ;
   private static final String ЯмМ = "rose/party_marker";
   private static final String ЯмН = "party_marker";
   private static final String Ямн = "party_marker";
   private static final String ЯмЦ = "rose/party_marker_label";
   private static final String Ямч = "party_marker_label";
   private static final String ЯмЭ = "party_marker_label";
   private static final float Ям必 = 0.016666668F;
   private static final float Ямщ = 1.0E9F;
   private static final float ЯмГ = 0.1F;
   private static final float Ямл = 16.0F;
   private static final float Ямэ = 1.8F;
   private static final float Ямю = 0.5F;
   private static final float ЯмЩ = 7.5E8F;
   private static final float Яму = 0.65F;
   private static final float Ям2 = 0.58F;
   private static final float Ямф = 0.42F;
   private static final float Ями = 300.0F;
   private static final float Ям9 = 0.003F;
   private static final float Ямо = 1.5F;
   private static final float ЯмO = 0.025F;
   private static final float Ям7 = 1.5F;
   private static final float ЯмР = 12.0F;
   private static final float Ям3 = 0.7F;
   private static final float Ямр = 0.004F;
   private static final float Ям6 = 0.7F;
   private static final float Ям_ = 4.0F;
   private static final float ЯмШ = 0.008F;
   private static final float Ямы = 0.1F;
   private static final float Ямй = 0.12F;
   private static final float ЯмЖ = 3.0F;
   private static final float Ямм = 0.575F;
   private static final float ЯмК = 1.65F;
   private static final float ЯмI = 1.09F;
   private static final float ЯмТ = 0.575F;
   private static final float ЯмО = 0.5F;
   private static final float ЯмФ = 0.001F;
   private static final double ЯмЙ = 1.0E9;
   private static final double Ямк = Math.PI;
   private static final double Ямг = 2.0;
   private static final double Ям0 = 3.2;
   private static final float Ямх = 0.01F;
   private static final float ЯмУ = -1.0F;
   private static final float ЯмЗ = 0.5F;
   private static final float ЯмЕ = 0.5F;
   private static final float ЯмП = 0.5F;
   private static final float Ям西 = 32.0F;
   private static final float Ямд = 0.5F;
   private static final float ЯмБ = 32.0F;
   private static final float ЯмС = 0.5F;
   private static final float Ямз = 0.001F;
   private static final float Ямц = 0.5F;
   private static final float Ямя = 0.001F;
   private static final float ЯКс = (float) (Math.PI / 2);
   private static final float ЯКВ = 0.5F;
   private static final float ЯКЯ = 0.5F;
   private static final float ЯКХ = 0.9F;
   private static final float ЯКе = 0.16F;
   private static final float ЯКА = 80.0F;
   private static final long ЯК4 = 2147483647L;
   private static final float ЯКп = 0.003F;
   private static final double ЯКД = 1.0E9;
   private static final double ЯКЪ = Math.PI;
   private static final double ЯКт = 2.0;
   private static final double ЯКа = 3.2;
   private static final float ЯКЮ = 0.055F;
   private static final float ЯКЧ = 0.035F;
   private static final float ЯКЛ = 0.06F;
   private static final float ЯК诶 = 0.09F;
   private static final double ЯК1 = 0.55;
   private static final double ЯКЬ = 0.55;
   private static final float ЯК衣 = 0.65F;
   private static final float ЯКш = 0.006F;
   private static final double ЯКИ = Math.PI;
   private static final float ЯКь = 0.32F;
   private static final float ЯКж = 0.5F;
   private static final float ЯК弟 = 0.5F;
   private static final float ЯКб = 0.68F;
   private static final float ЯКЫ = 0.1F;
   private static final long ЯКl = 200000000L;
   private static final float ЯК8 = 0.003F;
   private static final float ЯК5 = 30.0F;
   private static final String ЯКв = "minecraft:player";
   private static final float ЯКъ = 32.0F;
   private static final float ЯКМ = 0.5F;
   private static final float ЯКН = 12.0F;
   private static final float ЯКн = 12.0F;
   private static final float ЯКЦ = 12.0F;
   private static final float ЯКч = 6.0F;
   private static final float ЯКЭ = 16.0F;
   private static final float ЯК必 = 16.0F;
   private static final float ЯКщ = 12.0F;
   private static final float ЯКГ = 16.0F;
   private static final float ЯКл = 12.0F;
   private static final float ЯКэ = 16.0F;
   private static final float ЯКю = 16.0F;
   private static final float ЯКЩ = 0.5F;
   private static final float ЯКу = 16.0F;
   private static final float ЯК2 = -1.0F;
   private static final float ЯКф = 0.5F;
   private static final float ЯКи = 32.0F;
   private static final float ЯК9 = 12.0F;
   private static final float ЯКо = 5.0F;
   private static final float ЯКO = 4.0F;
   private static final float ЯК7 = 9.0F;
   private static final float ЯКР = 10.0F;
   private static final float ЯК3 = 9.5F;
   private static final float ЯКр = -1.0F;
   private static final float ЯК6 = 10.0F;
   private static final float ЯК_ = 10.0F;
   private static final float ЯКШ = 20.0F;
   private static final float ЯКы = 20.0F;
   private static final float ЯКй = 0.88F;
   private static final float ЯКЖ = 0.12F;
   private static final float ЯКм = 24.0F;
   private static final float ЯКК = 24.0F;
   private static final float ЯКI = 5.0F;
   private static final float ЯКТ = 0.125F;
   private static final float ЯКО = 24.0F;
   private static final float ЯКФ = 24.0F;
   private static final float ЯКЙ = 5.0F;
   private static final float ЯКк = 0.625F;
   private static final float ЯКг = 0.125F;
   private static final float ЯК0 = 0.125F;
   private static final float ЯКх = 0.125F;
   private static final float ЯКУ = -1.0F;
   private static final float ЯКЗ = 0.5F;
   private static final float ЯКЕ = 0.5F;
   private static final float ЯКП = 1.0E-4F;
   private static final float ЯК西 = 1.0E-4F;
   private static final float ЯКд = 0.9999F;
   private static final float ЯКБ = 0.9999F;
   private static final int ЯКС = 16777215;
   private static final float ЯКз = 3.0F;
   private static final float ЯКц = 0.05F;
   private static final String ЯКя = "party-markers";
   private static final String ЯIс = "party-labels";
   private static final String ЯIВ = "party-markers";

   private void Ф(BufferBuilder var1, boolean var2) {
      MeshData var3 = var1.build();
      if (var3 != null) {
         try (var3) {
            int var5 = var3.vertexBuffer().remaining();
            GpuBuffer var6 = var2 ? this.у : this.Щ;
            if (var6 == null || var6.isClosed() || (var2 ? this.field_4西 : this.field_4П) < var5) {
               if (var6 != null && !var6.isClosed()) {
                  var6.close();
               }

               int var7 = Math.max(4096, Integer.highestOneBit(var5 - 1) << 1);
               var6 = RenderSystem.getDevice().createBuffer(() -> var2 ? ЯIс : ЯIВ, 40, var7);
               if (var2) {
                  this.у = var6;
                  this.field_4西 = var7;
               } else {
                  this.Щ = var6;
                  this.field_4П = var7;
               }
            }

            CommandEncoder var19 = RenderSystem.getDevice().createCommandEncoder();
            var19.writeToBuffer(var6.slice(0L, var5), var3.vertexBuffer());
            AutoStorageIndexBuffer var8 = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
            int var9 = var3.drawState().indexCount();
            GpuBuffer var10 = var8.getBuffer(var9);
            RenderTarget var11 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();

            try (RenderPass var12 = var19.createRenderPass(() -> ЯКя, var11.getColorTextureView(), Optional.empty())) {
               RenderSystem.bindDefaultUniforms(var12);
               var12.setPipeline(var2 ? this.сг : this.ск);
               var12.setVertexBuffer(0, var6.slice());
               var12.setIndexBuffer(var10, var8.type());
               var12.drawIndexed(var9, 1, 0, 0, 0);
            }
         }
      }
   }

   private static int Т(int var0, float var1) {
      return var0 & ЯКС | Math.round((var0 >>> 24) * Math.clamp(var1, 0.0F, 1.0F)) << 24;
   }

   public void эЛ() {
      this.А_d5b8fa95_at_bf703e30.clear();
      this.б = null;
      this.ХХ = 0L;
      if (this.Щ != null && !this.Щ.isClosed()) {
         this.Щ.close();
      }

      this.Щ = null;
      this.field_4П = 0;
      if (this.у != null && !this.у.isClosed()) {
         this.у.close();
      }

      this.у = null;
      this.field_4西 = 0;
   }

   private static void Ф(BufferBuilder var0, float var1, float var2, float var3, float var4, int var5, int var6) {
      float var7 = Math.max(1, MINECRAFT_CLIENT.getWindow().getScreenWidth());
      float var8 = Math.max(1, MINECRAFT_CLIENT.getWindow().getScreenHeight());
      Ф(
         var0,
         UiDrawing.getTransform(),
         ЯКУ + (var1 + var3 * ЯКЗ) * 2.0F / var7,
         1.0F - (var2 + var4 * ЯКЕ) * 2.0F / var8,
         0.0F,
         var3 / var7,
         0.0F,
         0.0F,
         0.0F,
         var4 / var8,
         0.0F,
         var5,
         var6
      );
   }

   private static boolean в(Vector4f var0) {
      return var0.w > ЯКц && Math.abs(var0.x) <= var0.w && Math.abs(var0.y) <= var0.w && var0.z <= var0.w;
   }

   private void Ф(PartyMarker.MarkerRenderState var1, float var2, float var3) {
      Marker var4 = var1.В.Ц();
      if (var1.ж instanceof AbstractClientPlayer var5) {
         var1.сп = var5.getSkin().body().texturePath();
      } else if (MINECRAFT_CLIENT.getConnection() != null) {
         PlayerInfo var8 = MINECRAFT_CLIENT.getConnection().getPlayerInfo(var4.entityUuid());
         if (var8 != null) {
            var1.сп = var8.getSkin().body().texturePath();
         }
      }

      Identifier var7 = var1.сп != null ? var1.сп : DefaultPlayerSkin.get(var4.entityUuid()).body().texturePath();
      GpuTextureView var9 = MINECRAFT_CLIENT.getTextureManager().getTexture(var7).getTextureView();
      UiBatchDrawing.drawTextureNearestFlipped(var2, var3, ЯКм, ЯКК, new Vector4f(ЯКI), new Vector4f(ЯКТ), new Vector4i(-1), var9);
      UiBatchDrawing.drawTextureNearestFlipped(var2, var3, ЯКО, ЯКФ, new Vector4f(ЯКЙ), new Vector4f(ЯКк, ЯКг, ЯК0, ЯКх), new Vector4i(-1), var9);
   }

   public PartyMarker() {
      this.ск = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ЯмМ)
         .withVertexShader(ClientResourceIds.getCoreId(ЯмН))
         .withFragmentShader(ClientResourceIds.getCoreId(Ямн))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.LIGHTNING)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
      this.сг = RenderPipelineBuilder.builder()
         .withLocation(ЯмЦ)
         .withVertexShader(ClientResourceIds.getCoreId(Ямч))
         .withFragmentShader(ClientResourceIds.getCoreId(ЯмЭ))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
   }

   public void Ф(WorldEffectsEvent var1, int var2) {
      if (!Rain.Пз()) {
         if (this.б != MINECRAFT_CLIENT.level) {
            this.эЛ();
            this.б = MINECRAFT_CLIENT.level;
         }

         if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && var1.getProjectionMatrix() != null && var1.getViewMatrix() != null) {
            long var3 = System.nanoTime();
            float var5 = this.ХХ == 0L ? Ям必 : Math.clamp((float)(var3 - this.ХХ) / Ямщ, 0.0F, ЯмГ);
            this.ХХ = var3;

            for (PartyMarker.MarkerRenderState var7 : this.А_d5b8fa95_at_bf703e30.values()) {
               var7.Аы = false;
            }

            String var53 = MINECRAFT_CLIENT.level.dimension().identifier().toString();

            for (PartySession.TrackedMarker var8 : PartyClient.Х_4d6328fc().Ж().values()) {
               if (var53.equals(var8.Ц().dimension()) && PartyClient.Х_4d6328fc().Х_bbeebd87(var8.Ц()) && var3 < var8.Ы()) {
                  PartyMarker.MarkerRenderState var9 = this.А_d5b8fa95_at_bf703e30.get(var8.Ц().id());
                  if (var9 == null) {
                     if (this.А_d5b8fa95_at_bf703e30.size() >= 64) {
                        this.А_d5b8fa95_at_bf703e30.pollFirstEntry();
                     }

                     var9 = new PartyMarker.MarkerRenderState(var8);
                     this.А_d5b8fa95_at_bf703e30.put(var8.Ц().id(), var9);
                  }

                  var9.В = var8;
                  var9.Аы = true;
               }
            }

            if (!this.А_d5b8fa95_at_bf703e30.isEmpty()) {
               float var55 = 1.0F - (float)Math.exp(-var5 * Ямл);
               int var56 = Math.max(1, MINECRAFT_CLIENT.getWindow().getScreenWidth());
               int var57 = Math.max(1, MINECRAFT_CLIENT.getWindow().getScreenHeight());
               Matrix4f var10 = var1.getViewMatrix();
               this.field_5.set(var1.getProjectionMatrix()).mul(var10);
               Vec3 var11 = var1.getCameraPosition();
               this.А_f5cce25e_at_bf703e30.set(1.0F, 0.0F, 0.0F).rotate(MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation());
               BufferBuilder var12 = null;
               Iterator var13 = this.А_d5b8fa95_at_bf703e30.values().iterator();

               while (var13.hasNext()) {
                  PartyMarker.MarkerRenderState var14 = (PartyMarker.MarkerRenderState)var13.next();
                  Marker var15 = var14.В.Ц();
                  boolean var16 = var15.entityUuid() != null;
                  var14.ж = var16 ? PartyClient.Х_4d6328fc().Х_95bedd6e(var15) : null;
                  if (var14.ж != null) {
                     var14.弟 = var14.ж;
                  }

                  Vec3 var17 = var14.ж == null ? null : var14.ж.getPosition(var1.getTickDelta());
                  double var18 = var17 == null ? var15.x() : var17.x;
                  double var20 = var17 == null ? var15.y() : var17.y;
                  double var22 = var17 == null ? var15.z() : var17.z;
                  if (var16) {
                     if (var14.ж != null) {
                        Vec3 var24 = var14.ж.getBoundingBox().getCenter();
                        var18 += var24.x - var14.ж.getX();
                        var20 += var24.y - var14.ж.getY();
                        var22 += var24.z - var14.ж.getZ();
                     } else {
                        EntityType var58 = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse(var15.entityType()));
                        float var25 = var14.弟 != null ? var14.弟.getBbHeight() : (var58 != null ? var58.getDimensions().height() : Ямэ);
                        var20 += var25 * Ямю;
                        if (var14.АЖ) {
                           var18 = var14.Яз + (var18 - var14.Яз) * var55;
                           var20 = var14.Яц + (var20 - var14.Яц) * var55;
                           var22 = var14.Яя + (var22 - var14.Яя) * var55;
                        }
                     }

                     var14.Яз = var18;
                     var14.Яц = var20;
                     var14.Яя = var22;
                     var14.АЖ = true;
                  }

                  double var59 = var18 - MINECRAFT_CLIENT.player.getX();
                  double var26 = var20 - MINECRAFT_CLIENT.player.getY();
                  double var28 = var22 - MINECRAFT_CLIENT.player.getZ();
                  double var30 = Math.sqrt(var59 * var59 + var26 * var26 + var28 * var28);
                  float var32 = var14.Аы ? ц2((float)(var14.В.Ы() - var3) / ЯмЩ) * ц2((float)var30 / Яму) * (Ям2 + Ямф / (1.0F + (float)var30 / Ями)) : 0.0F;
                  var14.ЮЫ = var14.ЮЫ + (var32 - var14.ЮЫ) * var55;
                  if (!var14.Аы && var14.ЮЫ < Ям9) {
                     var13.remove();
                  } else {
                     float var33 = (float)(var18 - var11.x);
                     float var34 = (float)(var20 - var11.y);
                     float var35 = (float)(var22 - var11.z);
                     float var36 = Math.clamp(Ямо + (float)var30 * ЯмO, Ям7, ЯмР);
                     float var37 = Math.clamp(Ям3 + (float)var30 * Ямр, Ям6, Ям_);
                     if (var16) {
                        this.field_5.transform(this.Ч.set(var33, var34, var35, 1.0F));
                     } else {
                        this.field_5.transform(this.Ч.set(var33, var34 + var36, var35, 1.0F));
                     }

                     this.field_5.transform(this.Л.set(var33, var34, var35, 1.0F));
                     float var38 = Math.max(ЯмШ, Math.max(this.Ч.w, this.Л.w) * 2.0F / (var57 * Math.max(Ямы, var1.getProjectionMatrix().m11())));
                     float var39 = Math.max(Ямй, var38 * ЯмЖ);
                     var14.Ай = var16 ? в(this.Ч) : Ф(this.field_5, var33, var34 + var36 * Ямм, var35, Math.max(var37 * ЯмК, var39 * ЯмI), var36 * ЯмТ);
                     if (!var16 && !в(this.Ч)) {
                        if (var14.Ай && !в(this.Л)) {
                           this.Ч.add(this.Л).mul(ЯмО);
                        } else {
                           this.Ч.set(this.Л);
                        }
                     }

                     float var40 = Math.max(Math.abs(this.Ч.w), ЯмФ);
                     float var41 = this.Ч.x / var40;
                     float var42 = this.Ч.y / var40;
                     var14.Юъ = 0.0F;
                     if (!var16 && !var14.Ай) {
                        double var43 = (var3 - var14.В.И()) / ЯмЙ;
                        var14.Юъ = (float)Math.sin(var43 * Ямк * Ямг / Ям0);
                        if (Math.abs(var41) + Math.abs(var42) < Ямх) {
                           var42 = ЯмУ;
                        }

                        float var45 = var41 * var56 * ЯмЗ;
                        float var46 = -var42 * var57 * ЯмЕ;
                        float var47 = Math.max(Math.abs(var45) / Math.max(1.0F, var56 * ЯмП - Ям西), Math.abs(var46) / Math.max(1.0F, var57 * Ямд - ЯмБ));
                        var14.Юl = var56 * ЯмС + var45 / Math.max(var47, Ямз);
                        var14.Ю8 = var57 * Ямц + var46 / Math.max(var47, Ямя);
                        var14.Юв = (float)Math.atan2(var46, var45) + ЯКс;
                     } else {
                        var14.Юl = (var41 + 1.0F) * var56 * ЯКВ;
                        var14.Ю8 = (1.0F - var42) * var57 * ЯКЯ;
                     }

                     var14.Ю5 = ЯКХ + ЯКе / (1.0F + (float)var30 / ЯКА);
                     int var60 = (int)Math.min(ЯК4, Math.round(var30));
                     if (var60 != var14.ЪО) {
                        var14.ЪО = var60;
                        var14.Ха = var60 + " м";
                        var14.ЮМ = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var14.Ха, 13);
                     }

                     if (!var16 && !(var14.ЮЫ < ЯКп)) {
                        double var44 = (var3 - var14.В.И()) / ЯКД;
                        float var61 = (float)Math.sin(var44 * ЯКЪ * ЯКт / ЯКа);
                        float var62 = var36 * (ЯКЮ + ЯКЧ * var61);
                        float var48 = var36 * (1.0F + ЯКЛ * var61);
                        float var49 = var39 * (1.0F + ЯК诶 * var61);
                        float var50 = (float)(var44 * ЯК1 - Math.floor(var44 * ЯКЬ));
                        int var51 = Т(var2, var14.ЮЫ);
                        if (var12 == null) {
                           var12 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                        }

                        Ф(var12, var10, var33, var34, var35, var37, 0.0F, 0.0F, 0.0F, 0.0F, var37, 0, var51);
                        float var52 = var37 * (1.0F + var50 * ЯК衣);
                        Ф(
                           var12,
                           var10,
                           var33,
                           var34 + ЯКш,
                           var35,
                           var52,
                           0.0F,
                           0.0F,
                           0.0F,
                           0.0F,
                           var52,
                           0,
                           Т(var2, var14.ЮЫ * (float)Math.sin(var50 * ЯКИ) * ЯКь)
                        );
                        Ф(
                           var12,
                           var10,
                           var33,
                           var34 + var62 + var48 * ЯКж,
                           var35,
                           this.А_f5cce25e_at_bf703e30.x * var49,
                           this.А_f5cce25e_at_bf703e30.y * var49,
                           this.А_f5cce25e_at_bf703e30.z * var49,
                           0.0F,
                           var48 * ЯК弟,
                           0.0F,
                           4,
                           Т(var2, var14.ЮЫ * (ЯКб + ЯКЫ * var61))
                        );
                     }
                  }
               }

               if (var12 != null) {
                  this.Ф(var12, false);
               }
            }
         }
      }
   }

   private static boolean Ф(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      return var0 * var4 + var1 * var5 + var2 * var6 + var3 + (Math.abs(var0) + Math.abs(var2)) * var7 + Math.abs(var1) * var8 < 0.0F;
   }

   private static boolean Ф(Matrix4f var0, float var1, float var2, float var3, float var4, float var5) {
      return !Ф(var0.m03() + var0.m00(), var0.m13() + var0.m10(), var0.m23() + var0.m20(), var0.m33() + var0.m30(), var1, var2, var3, var4, var5)
         && !Ф(var0.m03() - var0.m00(), var0.m13() - var0.m10(), var0.m23() - var0.m20(), var0.m33() - var0.m30(), var1, var2, var3, var4, var5)
         && !Ф(var0.m03() + var0.m01(), var0.m13() + var0.m11(), var0.m23() + var0.m21(), var0.m33() + var0.m31(), var1, var2, var3, var4, var5)
         && !Ф(var0.m03() - var0.m01(), var0.m13() - var0.m11(), var0.m23() - var0.m21(), var0.m33() - var0.m31(), var1, var2, var3, var4, var5)
         && !Ф(var0.m03() - var0.m02(), var0.m13() - var0.m12(), var0.m23() - var0.m22(), var0.m33() - var0.m32(), var1, var2, var3, var4, var5);
   }

   private static void Ф(
      BufferBuilder var0,
      Matrix4f var1,
      float var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      int var11,
      int var12
   ) {
      var0.addVertex(var1, var2 - var5 - var8, var3 - var6 - var9, var4 - var7 - var10).setUv(var11 + ЯКП, 0.0F).setColor(var12);
      var0.addVertex(var1, var2 - var5 + var8, var3 - var6 + var9, var4 - var7 + var10).setUv(var11 + ЯК西, 1.0F).setColor(var12);
      var0.addVertex(var1, var2 + var5 + var8, var3 + var6 + var9, var4 + var7 + var10).setUv(var11 + ЯКд, 1.0F).setColor(var12);
      var0.addVertex(var1, var2 + var5 - var8, var3 + var6 - var9, var4 + var7 - var10).setUv(var11 + ЯКБ, 0.0F).setColor(var12);
   }

   private static float ц2(float var0) {
      float var1 = Math.clamp(var0, 0.0F, 1.0F);
      return var1 * var1 * (ЯКз - 2.0F * var1);
   }

   public void Ф(int var1, GuiGraphicsExtractor var2) {
      if (MINECRAFT_CLIENT.level != null
         && MINECRAFT_CLIENT.player != null
         && !this.А_d5b8fa95_at_bf703e30.isEmpty()
         && !Rain.Пз()
         && System.nanoTime() - this.ХХ <= ЯКl
         && !MINECRAFT_CLIENT.gui.hud.isHidden()) {
         int var3 = Math.max(1, MINECRAFT_CLIENT.getWindow().getScreenWidth());
         int var4 = Math.max(1, MINECRAFT_CLIENT.getWindow().getScreenHeight());
         Matrix4f var5 = UiDrawing.getTransform();
         this.в.set(var5);
         float var6 = UiDrawing.getGlobalAlpha();
         BufferBuilder var7 = null;
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());

         try {
            for (PartyMarker.MarkerRenderState var9 : this.А_d5b8fa95_at_bf703e30.values()) {
               if (!(var9.ЮЫ < ЯК8)) {
                  float var10 = var9.Юб + var9.ЮМ + ЯК5;
                  boolean var11 = var9.В.Ц().entityUuid() != null;
                  if (!var11 || var9.Ай) {
                     boolean var12 = var11 && ЯКв.equals(var9.В.Ц().entityType());
                     if (var12) {
                        var10 += ЯКъ;
                     }

                     float var13 = var9.Ю5 * HudScaleCoordinates.getHudScale();
                     float var14 = var10 * var13 * ЯКМ;
                     float var15 = var11 ? var9.Юl : Math.clamp(var9.Юl, ЯКН + var14, Math.max(ЯКн + var14, var3 - var14 - ЯКЦ));
                     float var16 = var9.Ай ? ЯКч * var13 : ЯКЭ;
                     float var17 = var11
                        ? var9.Ю8
                        : Math.clamp(var9.Ю8 - var16 - ЯК必 * var13, ЯКщ + ЯКГ * var13, Math.max(ЯКл + ЯКэ * var13, var4 - 12 - ЯКю * var13));
                     float var18 = var15 - var10 * ЯКЩ;
                     float var19 = var17 - ЯКу;
                     float var20 = ЯК2 + (var18 + var10 * ЯКф) * 2.0F / var3;
                     float var21 = 1.0F - var17 * 2.0F / var4;
                     var5.identity().translate(var20, var21, 0.0F).scale(var13, var13, 1.0F).translate(-var20, -var21, 0.0F);
                     UiDrawing.setGlobalAlpha(var6 * var9.ЮЫ);
                     UiBatchDrawing.drawHudWindowBackground(var18, var19, var10, ЯКи, ЯК9);
                     if (var12) {
                        this.Ф(var9, var18 + ЯКо, var19 + ЯКO);
                     }

                     UiBatchDrawing.drawText(
                        ClientFonts.getMediumFont(), var9.Хт, var18 + (var12 ? 42 : 10), var19 + ЯК7, 14, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT)
                     );
                     UiBatchDrawing.drawText(
                        ClientFonts.getMediumFont(), var9.Ха, var18 + var10 - var9.ЮМ - ЯКР, var19 + ЯК3, 13, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
                     );
                     if (!var11 && !var9.Ай) {
                        if (var7 == null) {
                           var7 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                        }

                        float var22 = ЯКр + var9.Юl * 2.0F / var3;
                        float var23 = 1.0F - var9.Ю8 * 2.0F / var4;
                        float var24 = (float)var3 / var4;
                        var5.identity()
                           .translate(var22, var23, 0.0F)
                           .scale(1.0F, var24, 1.0F)
                           .rotateZ(-var9.Юв)
                           .scale(1.0F, 1.0F / var24, 1.0F)
                           .translate(-var22, -var23, 0.0F);
                        Ф(var7, var9.Юl - ЯК6, var9.Ю8 - ЯК_ + var9.Юъ * 2.0F, ЯКШ, ЯКы, 8, Т(var1, var9.ЮЫ * (ЯКй + ЯКЖ * var9.Юъ)));
                     }
                  }
               }
            }
         } finally {
            try {
               UiBatchDrawing.endBatch(ClientFonts.getMediumFont());
            } finally {
               var5.set(this.в);
               UiDrawing.setGlobalAlpha(var6);
            }
         }

         if (var7 != null) {
            this.Ф(var7, true);
         }
      }
   }

   private static final class MarkerRenderState {
      private PartySession.TrackedMarker В;
      private final float Юб;
      private final String Хт;
      private boolean Аы;
      private boolean Ай;
      private float ЮЫ;
      private float Юl;
      private float Ю8;
      private float Ю5;
      private float Юв;
      private float Юъ;
      private LivingEntity ж;
      private LivingEntity 弟;
      private Identifier сп;
      private boolean АЖ;
      private double Яз;
      private double Яц;
      private double Яя;
      private int ЪО = -1;
      private String Ха = "";
      private float ЮМ;
      private static final String с1ш = "minecraft:player";

      private MarkerRenderState(PartySession.TrackedMarker var1) {
         this.В = var1;
         Marker var2 = var1.Ц();
         EntityType var3 = var2.entityUuid() != null && !с1ш.equals(var2.entityType())
            ? BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse(var2.entityType()))
            : null;
         this.Хт = var3 != null ? var3.getDescription().getString() : var2.name();
         this.Юб = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), this.Хт, 14);
      }
   }
}
