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
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import settings.render.ClientResourceIds;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.network.ClientChat;
import sg.render.avatar.CustomPictureCatalog;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.render.pipeline.UiRenderPassSupport;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ActionSetting;
import sg.settings.ChoiceOption;
import sg.settings.SingleChoiceSetting;

public class Picture extends ClientModule implements UiRenderPassSupport {
   private static final float ЮO = 0.7F;
   private static final float Ю7 = 0.45F;
   private static final CustomPictureCatalog.PictureTexture В = new CustomPictureCatalog.PictureTexture(Identifier.fromNamespaceAndPath(Picture.ВВи, Picture.ВВ9), 1.0F);
   private static final CustomPictureCatalog.PictureTexture Я_at_d2fe211c = new CustomPictureCatalog.PictureTexture(Identifier.fromNamespaceAndPath(Picture.ВВо, Picture.ВВO), Picture.ВВ7);
   @SerializedName("А")
   private final ChoiceOption А_at_d2fe211c;
   @SerializedName("4")
   private final ChoiceOption field_4;
   @SerializedName("В弟")
   private final Map<String, ChoiceOption> В弟_ea411899;
   private final SingleChoiceSetting сЦ;
   private final SingleChoiceSetting сч;
   private final ActionSetting Д;
   @SerializedName("В弟")
   private final RenderPipeline В弟_bfbf25ea;
   private final EventListener<WorldEffectsEvent> еб;
   private static final String ВВь = "Picture";
   private static final String ВВж = "Рисует картинку над головой";
   private static final String ВВ弟 = "Сюся";
   private static final String ВВб = "Могучий Орёл";
   private static final String ВВЫ = "Картинка";
   private static final String ВВl = "Цель";
   private static final String ВВ8 = "На себя";
   private static final String ВВ5 = "На всех";
   private static final String ВВв = "На друзей";
   private static final String ВВъ = "Загрузить свои";
   private static final String ВВМ = "rose/picture";
   private static final String ВВН = "dead_chams";
   private static final String ВВн = "dead_chams";
   private static final String ВВЦ = "Sampler0";
   private static final String ВВч = "На себя";
   private static final String ВВЭ = "На друзей";
   private static final float ВВ必 = 0.7F;
   private static final float ВВщ = 90.0F;
   private static final float ВВГ = 90.0F;
   private static final float ВВл = 0.45F;
   private static final String ВВэ = "explorer.exe";
   private static final String ВВю = "Сюся";
   private static final String ВВЩ = "Могучий Орёл";
   private static final String ВВу = "Sampler0";
   private static final String ВВ2 = "picture";
   private static final String ВВф = "pictureVertices";
   private static final String ВВи = "rose";
   private static final String ВВ9 = "textures/test/soca.png";
   private static final String ВВо = "rose";
   private static final String ВВO = "textures/picture/mighty_eagle.png";
   private static final float ВВ7 = 1.9543568F;

   private CustomPictureCatalog.PictureTexture р() {
      if (this.сЦ.isSelected(ВВю)) {
         return В;
      }

      if (this.сЦ.isSelected(ВВЩ)) {
         return Я_at_d2fe211c;
      }

      ChoiceOption var1 = this.сЦ.getSelectedOption();
      CustomPictureCatalog.PictureTexture var2 = var1 == null ? null : CustomPictureCatalog.method_4(var1.getName());
      return var2 != null ? var2 : В;
   }

   public Picture() {
      super(ВВь, ВВж, ModuleCategory.RENDER);
      this.А_at_d2fe211c = new ChoiceOption(ВВ弟);
      this.field_4 = new ChoiceOption(ВВб);
      this.В弟_ea411899 = new LinkedHashMap<>();
      this.сЦ = new SingleChoiceSetting(ВВЫ, this.А_at_d2fe211c, this.field_4);
      this.сч = new SingleChoiceSetting(ВВl, new ChoiceOption(ВВ8), new ChoiceOption(ВВ5), new ChoiceOption(ВВв));
      this.Д = new ActionSetting(ВВъ).onClick(this::method_8т).showOtherSettings();
      this.В弟_bfbf25ea = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ВВМ)
         .withVertexShader(ClientResourceIds.getCoreId(ВВН))
         .withFragmentShader(ClientResourceIds.getCoreId(ВВн))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(ВВЦ)
         .build();
      this.еб = this::п必;
      this.addSettings(this.сЦ, this.сч, this.Д);
      CustomPictureCatalog.西у();
      CustomPictureCatalog.ж(false, this::method_8ъ);
   }

   private void method_8ъ() {
      ArrayList var1 = new ArrayList<>(List.of(this.А_at_d2fe211c, this.field_4));
      HashSet var2 = new HashSet<>(CustomPictureCatalog.шl());
      this.В弟_ea411899.keySet().removeIf(var1x -> !var2.contains(var1x));

      for (String var4 : CustomPictureCatalog.шl()) {
         var1.add(this.В弟_ea411899.computeIfAbsent(var4, ChoiceOption::new));
      }

      this.сЦ.setOptions(var1);
   }

   private void р(WorldEffectsEvent var1, CustomPictureCatalog.PictureTexture var2, Player var3, float var4) {
      Vec3 var5 = new Vec3(Mth.lerp(var4, var3.xo, var3.getX()), Mth.lerp(var4, var3.yo, var3.getY()), Mth.lerp(var4, var3.zo, var3.getZ()));
      float var6 = Mth.rotLerp(var4, var3.yBodyRotO, var3.yBodyRot);
      float var7 = Mth.lerp(var4, var3.xRotO, var3.getXRot());
      boolean var8 = var3.isFallFlying() || var3.isVisuallySwimming() || var3.isVisuallyCrawling();
      float var9 = ВВ必;
      float var10 = var9 / var2.aspectRatio();
      float var11 = var8 ? var3.getDimensions(Pose.STANDING).height() : var3.getBbHeight();
      PoseStack var12 = var1.getPoses();
      var12.pushPose();
      var12.translate(var5.x - var1.getCameraPosition().x, var5.y - var1.getCameraPosition().y, var5.z - var1.getCameraPosition().z);
      var12.mulPose(Axis.YP.rotationDegrees(-var6));
      if (var8) {
         var12.mulPose(Axis.XP.rotationDegrees(var7 + ВВщ));
      }

      var12.translate(0.0F, var11, 0.0F);
      if (var8) {
         var12.mulPose(Axis.XP.rotationDegrees(-var7 - ВВГ));
      }

      var12.translate(0.0F, ВВл + var10 / 2.0F, 0.0F);
      Matrix4f var13 = var12.last().pose();
      BufferBuilder var14 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      float var15 = var9 / 2.0F;
      float var16 = var10 / 2.0F;
      this.р(var14, var13, -var15, -var16, 0.0F, 0.0F, 1.0F);
      this.р(var14, var13, -var15, var16, 0.0F, 0.0F, 0.0F);
      this.р(var14, var13, var15, var16, 0.0F, 1.0F, 0.0F);
      this.р(var14, var13, var15, -var16, 0.0F, 1.0F, 1.0F);
      var12.popPose();
      this.method_0(var14, var2.texture());
   }

   private boolean В(Player var1) {
      if (this.сч.isSelected(ВВч)) {
         return var1 == MINECRAFT_CLIENT.player;
      } else {
         return this.сч.isSelected(ВВЭ) ? RoseClient.getInstance().getServices().getFriends().О(var1.getGameProfile().name()) : true;
      }
   }

   private void п必(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         CustomPictureCatalog.PictureTexture var2 = this.р();
         float var3 = var1.getTickDelta();

         for (Player var5 : MINECRAFT_CLIENT.level.players()) {
            if (this.В(var5)) {
               this.р(var1, var2, var5, var3);
            }
         }
      }
   }

   @Override
   public void onEnable() {
      CustomPictureCatalog.ж(false, this::method_8ъ);
   }

   private void method_0(BufferBuilder var1, Identifier var2) {
      MeshData var3 = var1.build();
      if (var3 != null) {
         GpuBuffer var4 = null;

         try {
            DrawState var5 = var3.drawState();
            int var6 = var5.indexCount();
            AutoStorageIndexBuffer var7 = RenderSystem.getSequentialBuffer(var5.primitiveTopology());
            GpuBuffer var8 = var7.getBuffer(var6);
            IndexType var9 = var7.type();
            var4 = RenderSystem.getDevice().createBuffer(() -> ВВф, 32, var3.vertexBuffer());
            RenderTarget var10 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
            AbstractTexture var11 = MINECRAFT_CLIENT.getTextureManager().getTexture(var2);
            CommandEncoder var12 = RenderSystem.getDevice().createCommandEncoder();

            try (RenderPass var13 = var12.createRenderPass(
                  () -> ВВ2, var10.getColorTextureView(), Optional.empty(), var10.getDepthTextureView(), OptionalDouble.empty()
               )) {
               RenderSystem.bindDefaultUniforms(var13);
               var13.setPipeline(this.В弟_bfbf25ea);
               var13.bindTexture(ВВу, var11.getTextureView(), var11.getSampler());
               var13.setVertexBuffer(0, var4.slice());
               var13.setIndexBuffer(var8, var9);
               var13.drawIndexed(var6, 1, 0, 0, 0);
            }
         } finally {
            if (var4 != null) {
               var4.close();
            }

            var3.close();
         }
      }
   }

   private void method_8т() {
      try {
         CustomPictureCatalog.西у();
         new ProcessBuilder(ВВэ, CustomPictureCatalog.field_4.getAbsolutePath()).start();
         CustomPictureCatalog.ж(true, this::method_8ъ);
      } catch (Exception var3) {
         String var2 = var3.getMessage() != null ? var3.getMessage() : var3.getClass().getSimpleName();
         if (MINECRAFT_CLIENT.player != null) {
            ClientChat.З("Не удалось открыть папку: " + var2);
            ClientChat.З("Путь: " + CustomPictureCatalog.field_4.getAbsolutePath());
         }
      }
   }

   private void р(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6, float var7) {
      var1.addVertex(var2, var3, var4, var5).setUv(var6, var7).setColor(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
