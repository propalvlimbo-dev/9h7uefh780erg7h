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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import settings.render.ClientResourceIds;
import sg.events.impl.PlayerTickEvent;
import sg.ec.EntityHealth;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.network.InventoryInteractions;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.util.entity.CombatCalculations;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;

public final class HoleESP extends ClientModule {
   private static final Direction[] В = Direction.values();
   private static final int ет = 5;
   private static final int еа = 48;
   private static final int еЮ = 24;
   private final FloatSetting ВЛ;
   private final FloatSetting В诶;
   @SerializedName("5")
   private final ColorSetting field_5;
   @SerializedName("в")
   private final ColorSetting в_at_3f34e5f4;
   private final FloatSetting В1;
   private final List<HoleESP.RenderCandidate> сД;
   private int еЧ;
   private final RenderPipeline сб;
   private final EventListener<PlayerTickEvent> ВИ;
   private final EventListener<WorldEffectsEvent> Вь;
   private static final String ВвР = "HoleESP";
   private static final String Вв3 = "Позиции под кристалл и обсидиан";
   private static final String Ввр = "Кол-во лунок";
   private static final float Вв6 = 20.0F;
   private static final float Вв_ = 64.0F;
   private static final String ВвШ = "Радиус";
   private static final float Ввы = 12.0F;
   private static final float Ввй = 4.0F;
   private static final float ВвЖ = 24.0F;
   private static final String Ввм = "Благоприятный";
   private static final int ВвК = -14090476;
   private static final String ВвI = "Отрицательный";
   private static final int ВвТ = -55256;
   private static final String ВвО = "Кол-во Для обсидиана";
   private static final float ВвФ = 3.0F;
   private static final String ВвЙ = "rose/hole_esp";
   private static final String Ввк = "target_esp_crystal";
   private static final String Ввг = "target_esp_crystal";
   private static final double Вв0 = 0.5;
   private static final double Ввх = 0.5;
   private static final float ВвУ = 0.55F;
   private static final float ВвЗ = 0.5F;
   private static final float ВвЕ = 0.75F;
   private static final float ВвП = 0.05F;
   private static final float Вв西 = 0.05F;
   private static final float Ввд = 0.95F;
   private static final float ВвБ = 0.95F;
   private static final float ВвС = 0.95F;
   private static final float Ввз = 0.08F;
   private static final float Ввц = 0.92F;
   private static final float Ввя = 0.08F;
   private static final float Въс = 0.92F;
   private static final float ВъВ = 1.12F;
   private static final float ВъЯ = 0.92F;
   private static final float ВъХ = 0.5F;
   private static final float Въе = 0.5F;
   private static final float ВъА = 0.5F;
   private static final float Въ4 = 0.4F;
   private static final float Въп = 0.32F;
   private static final float ВъД = 0.08F;
   private static final float ВъЪ = 0.35F;
   private static final float Вът = 0.88F;
   private static final float Въа = 0.85F;
   private static final float ВъЮ = 0.5F;
   private static final float ВъЧ = 0.5F;
   private static final float ВъЛ = 0.5F;
   private static final float Въ诶 = 0.03F;
   private static final float Въ1 = 0.003F;
   private static final float ВъЬ = 0.01F;
   private static final float Въ衣 = 0.5F;
   private static final float Въш = 0.5F;
   private static final float ВъИ = 0.5F;
   private static final float Въь = 255.0F;
   private static final String Въж = "holeEsp";
   private static final String Въ弟 = "holeEspVertices";

   private void method_1(WorldEffectsEvent var1) {
      if (!this.сД.isEmpty() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameRenderer != null) {
         PoseStack var2 = var1.getPoses();
         Vec3 var3 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         BufferBuilder var4 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

         for (HoleESP.RenderCandidate var6 : this.сД) {
            float var7;
            float var8;
            float var9;
            float var10;
            float var11;
            float var12;
            if (var6.type == HoleESP.PlacementKind.OBSIDIAN) {
               var7 = var6.pos.getX() + ВвП;
               var8 = var6.pos.getY();
               var9 = var6.pos.getZ() + Вв西;
               var10 = var6.pos.getX() + Ввд;
               var11 = var6.pos.getY() + ВвБ;
               var12 = var6.pos.getZ() + ВвС;
            } else {
               var7 = var6.pos.getX() + Ввз;
               var8 = var6.pos.getY() + Ввц;
               var9 = var6.pos.getZ() + Ввя;
               var10 = var6.pos.getX() + Въс;
               var11 = var6.pos.getY() + ВъВ;
               var12 = var6.pos.getZ() + ВъЯ;
            }

            float var13 = var10 - var7;
            float var14 = var11 - var8;
            float var15 = var12 - var9;
            float var16 = (var7 + var10) * ВъХ - (float)var3.x;
            float var17 = (var8 + var11) * Въе - (float)var3.y;
            float var18 = (var9 + var12) * ВъА - (float)var3.z;
            int var19 = var6.favorable ? this.field_5.getValue() : this.в_at_3f34e5f4.getValue();
            float var20 = var6.type == HoleESP.PlacementKind.OBSIDIAN ? Въ4 : Въп;
            int var21 = this.С(this.method_5(var19, ВъД), var20);
            int var22 = this.С(this.method_5(var19, ВъЪ), Вът);
            var2.pushPose();
            var2.translate(var16, var17, var18);
            Matrix4f var23 = var2.last().pose();
            this.и(var4, var23, var13, var14, var15, var21);
            this.Ц(var4, var23, var13, var14, var15, var22);
            var2.popPose();
         }

         this.и(var4);
      }
   }

   private void Ц(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, Vector4f var6) {
      var1.addVertex(var2, var3, var4, var5).setUv(0.0F, 0.0F).setColor(var6.x, var6.y, var6.z, var6.w);
   }

   private boolean Ц(MutableBlockPos var1) {
      int var2 = var1.getX();
      int var3 = var1.getY();
      int var4 = var1.getZ();
      boolean var5 = this.щ(MINECRAFT_CLIENT.level.getBlockState(var1.set(var2 + 1, var3, var4)))
         || this.щ(MINECRAFT_CLIENT.level.getBlockState(var1.set(var2 - 1, var3, var4)))
         || this.щ(MINECRAFT_CLIENT.level.getBlockState(var1.set(var2, var3, var4 + 1)))
         || this.щ(MINECRAFT_CLIENT.level.getBlockState(var1.set(var2, var3, var4 - 1)))
         || this.щ(MINECRAFT_CLIENT.level.getBlockState(var1.set(var2, var3 - 1, var4)));
      var1.set(var2, var3, var4);
      return var5;
   }

   private void Ц(List<HoleESP.ScoredCandidate> var1, float var2) {
      if (!var1.isEmpty()) {
         float var3 = 0.0F;
         float var4 = Float.NEGATIVE_INFINITY;

         for (HoleESP.ScoredCandidate var6 : var1) {
            if (!(var6.score.selfDamage() >= var2 - 1.0F)) {
               var3 = Math.max(var3, var6.score.enemyDamage());
               var4 = Math.max(var4, var6.score.value());
            }
         }

         for (int var13 = 0; var13 < var1.size(); var13++) {
            HoleESP.ScoredCandidate var14 = (HoleESP.ScoredCandidate)var1.get(var13);
            HoleESP.DamageScore var7 = var14.score;
            boolean var8 = var7.selfDamage() >= var2 - 1.0F;
            boolean var9 = var7.enemyDamage() < 1.0F;
            boolean var10;
            if (!var8 && !var9 && !(var3 <= 0.0F)) {
               boolean var11 = var7.enemyDamage() >= var3 * ВвУ;
               boolean var12 = var7.value() >= var4 * ВвЗ || var7.enemyDamage() >= var7.selfDamage() * ВвЕ;
               var10 = var11 && var12;
            } else {
               var10 = false;
            }

            var1.set(
               var13,
               new HoleESP.ScoredCandidate(var14.pos, var14.type, new HoleESP.DamageScore(var10, var7.value(), var7.enemyDamage(), var7.selfDamage()))
            );
         }
      }
   }

   private boolean щ(BlockState var1) {
      return var1.is(Blocks.OBSIDIAN) || var1.is(Blocks.BEDROCK);
   }

   private void и(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, int var6) {
      Vector4f var7 = ColorMath.toRgbaVector(var6);
      float var8 = var3 * Въ衣;
      float var9 = var4 * Въш;
      float var10 = var5 * ВъИ;
      this.Ц(var1, var2, -var8, -var9, var10, -var8, var9, var10, var8, var9, var10, var8, -var9, var10, var7);
      this.Ц(var1, var2, var8, -var9, -var10, var8, var9, -var10, -var8, var9, -var10, -var8, -var9, -var10, var7);
      this.Ц(var1, var2, -var8, var9, var10, -var8, var9, -var10, var8, var9, -var10, var8, var9, var10, var7);
      this.Ц(var1, var2, -var8, -var9, -var10, -var8, -var9, var10, var8, -var9, var10, var8, -var9, -var10, var7);
      this.Ц(var1, var2, var8, -var9, var10, var8, var9, var10, var8, var9, -var10, var8, -var9, -var10, var7);
      this.Ц(var1, var2, -var8, -var9, -var10, -var8, var9, -var10, -var8, var9, var10, -var8, -var9, var10, var7);
   }

   private boolean ХБ() {
      return InventoryInteractions.Ъ_a83dd0cc(Items.OBSIDIAN) == -1 && InventoryInteractions.Ъ_a83dd0cc(Items.END_CRYSTAL) == -1
         ? MINECRAFT_CLIENT.player.getOffhandItem().is(Items.OBSIDIAN) || MINECRAFT_CLIENT.player.getOffhandItem().is(Items.END_CRYSTAL)
         : true;
   }

   private void Ц(
      BlockPos var1,
      int var2,
      int var3,
      Vec3 var4,
      double var5,
      Set<Long> var7,
      List<HoleESP.CrystalPosition> var8,
      List<HoleESP.CrystalPosition> var9,
      MutableBlockPos var10,
      MutableBlockPos var11
   ) {
      for (int var12 = -var2; var12 <= var2; var12++) {
         for (int var13 = -var3; var13 <= var3; var13++) {
            for (int var14 = -var2; var14 <= var2; var14++) {
               var10.set(var1.getX() + var12, var1.getY() + var13, var1.getZ() + var14);
               long var15 = BlockPos.asLong(var10.getX(), var10.getY(), var10.getZ());
               if (var7.add(var15)) {
                  double var17 = var10.getX() + Вв0;
                  double var19 = var10.getY() + 1.0;
                  double var21 = var10.getZ() + Ввх;
                  if (!(var4.distanceToSqr(var17, var19, var21) > var5)) {
                     BlockState var23 = MINECRAFT_CLIENT.level.getBlockState(var10);
                     var11.set(var10.getX(), var10.getY() + 1, var10.getZ());
                     if (this.щ(var23)) {
                        if (MINECRAFT_CLIENT.level.isEmptyBlock(var11)) {
                           var8.add(new HoleESP.CrystalPosition(var10.immutable(), new Vec3(var17, var19, var21)));
                        }
                     } else if (var23.canBeReplaced() && MINECRAFT_CLIENT.level.isEmptyBlock(var11) && this.Ц(var10) && this.и(var10)) {
                        var9.add(new HoleESP.CrystalPosition(var10.immutable(), new Vec3(var17, var19, var21)));
                     }
                  }
               }
            }
         }
      }
   }

   public HoleESP() {
      super(ВвР, Вв3, ModuleCategory.RENDER);
      this.ВЛ = new FloatSetting(Ввр, Вв6, 1.0F, Вв_).withStep(1.0F);
      this.В诶 = new FloatSetting(ВвШ, Ввы, Ввй, ВвЖ).withStep(1.0F);
      this.field_5 = new ColorSetting(Ввм, false, ВвК);
      this.в_at_3f34e5f4 = new ColorSetting(ВвI, false, ВвТ);
      this.В1 = new FloatSetting(ВвО, 2.0F, 1.0F, ВвФ).withStep(1.0F);
      this.сД = new ArrayList<>();
      this.сб = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ВвЙ)
         .withVertexShader(ClientResourceIds.getCoreId(Ввк))
         .withFragmentShader(ClientResourceIds.getCoreId(Ввг))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
      this.ВИ = this::бХ;
      this.Вь = this::method_1;
      this.addSettings(this.ВЛ, this.В诶, this.field_5, this.в_at_3f34e5f4, this.В1);
   }

   private void и(BufferBuilder var1) {
      MeshData var2 = var1.build();
      if (var2 != null) {
         GpuBuffer var3 = null;

         try {
            DrawState var4 = var2.drawState();
            int var5 = var4.indexCount();
            if (var5 > 0) {
               AutoStorageIndexBuffer var6 = RenderSystem.getSequentialBuffer(var4.primitiveTopology());
               GpuBuffer var7 = var6.getBuffer(var5);
               IndexType var8 = var6.type();
               var3 = RenderSystem.getDevice().createBuffer(() -> Въ弟, 32, var2.vertexBuffer());
               CommandEncoder var9 = RenderSystem.getDevice().createCommandEncoder();
               RenderTarget var10 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();

               try (RenderPass var11 = var9.createRenderPass(
                     () -> Въж, var10.getColorTextureView(), Optional.empty(), var10.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var11);
                  var11.setPipeline(this.сб);
                  var11.setVertexBuffer(0, var3.slice());
                  var11.setIndexBuffer(var7, var8);
                  var11.drawIndexed(var5, 1, 0, 0, 0);
                  return;
               }
            }
         } finally {
            if (var3 != null) {
               var3.close();
            }

            var2.close();
         }
      }
   }

   private void и(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      this.и(var1, new Matrix4f(var2).translate(var3, var4, var5), var6, var7, var8, var9);
   }

   @Override
   public void onDisable() {
      this.сД.clear();
      this.еЧ = 0;
   }

   private int Ц(int var1, int var2, int var3, int var4) {
      return var4 << 24 | var1 << 16 | var2 << 8 | var3;
   }

   private int С(int var1, float var2) {
      return this.Ц(ColorMath.getRed(var1), ColorMath.getGreen(var1), ColorMath.getBlue(var1), Math.round(Въь * Mth.clamp(var2, 0.0F, 1.0F)));
   }

   private List<LivingEntity> I_2571477d() {
      ArrayList<LivingEntity> var1 = new ArrayList<>(4);
      ArrayList<LivingEntity> var2 = new ArrayList<>(2);
      float var3 = this.В诶.getValue() + 2.0F;

      for (Player var5 : MINECRAFT_CLIENT.level.players()) {
         if (var5 != MINECRAFT_CLIENT.player
            && var5.isAlive()
            && !var5.isSpectator()
            && !RoseClient.getInstance().getServices().getModules().getAntiBot().detectedBots.contains(var5)
            && !(var5.distanceTo(MINECRAFT_CLIENT.player) > var3)) {
            if (RoseClient.getInstance().getServices().getFriends().О(var5.getName().getString())) {
               var2.add(var5);
            } else {
               var1.add(var5);
            }
         }
      }

      ArrayList<LivingEntity> var6 = var1.isEmpty() ? var2 : var1;
      var6.sort(Comparator.comparingDouble(var0 -> var0.distanceToSqr(MINECRAFT_CLIENT.player)));
      return var6.size() > 2 ? var6.subList(0, 2) : var6;
   }

   private void Ц(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, int var6) {
      float var7 = var3 * ВъЮ;
      float var8 = var4 * ВъЧ;
      float var9 = var5 * ВъЛ;
      float var10 = Mth.clamp(Math.min(var3, var5) * Въ诶, Въ1, ВъЬ);

      for (int var11 = -1; var11 <= 1; var11 += 2) {
         for (int var12 = -1; var12 <= 1; var12 += 2) {
            this.и(var1, var2, 0.0F, var11 * var8, var12 * var9, var3 + var10, var10, var10, var6);
         }
      }

      for (int var13 = -1; var13 <= 1; var13 += 2) {
         for (int var15 = -1; var15 <= 1; var15 += 2) {
            this.и(var1, var2, var13 * var7, 0.0F, var15 * var9, var10, var4 + var10, var10, var6);
         }
      }

      for (int var14 = -1; var14 <= 1; var14 += 2) {
         for (int var16 = -1; var16 <= 1; var16 += 2) {
            this.и(var1, var2, var14 * var7, var16 * var8, 0.0F, var10, var10, var5 + var10, var6);
         }
      }
   }

   private void бХ(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null) {
         this.сД.clear();
      } else if (!this.ХБ()) {
         this.сД.clear();
      } else if (++this.еЧ % 5 == 0) {
         List var2 = this.I_2571477d();
         LivingEntity var3 = var2.isEmpty() ? null : (LivingEntity)var2.getFirst();
         BlockPos var4 = MINECRAFT_CLIENT.player.blockPosition();
         int var5 = Math.min(Math.round(this.В诶.getValue()), 16);
         double var6 = this.В诶.getValue() * this.В诶.getValue();
         Vec3 var8 = MINECRAFT_CLIENT.player.getEyePosition();
         float var9 = EntityHealth.getHealth(MINECRAFT_CLIENT.player);
         ArrayList<HoleESP.CrystalPosition> var10 = new ArrayList<>();
         ArrayList<HoleESP.CrystalPosition> var11 = new ArrayList<>();
         HashSet var12 = new HashSet();
         MutableBlockPos var13 = new MutableBlockPos();
         MutableBlockPos var14 = new MutableBlockPos();
         this.Ц(var4, var5, 3, var8, var6, var12, var10, var11, var13, var14);
         if (var3 != null) {
            this.Ц(var3.blockPosition(), 5, 2, var8, var6, var12, var10, var11, var13, var14);
         }

         Vec3 var15 = var3 != null ? var3.position() : MINECRAFT_CLIENT.player.position();
         var10.sort(Comparator.comparingDouble(var1x -> var15.distanceToSqr(var1x.crystalCenter)));
         var11.sort(Comparator.comparingDouble(var1x -> var15.distanceToSqr(var1x.crystalCenter)));
         List<HoleESP.ScoredCandidate> var16 = this.Ц(var10, HoleESP.PlacementKind.CRYSTAL, var2, 48);
         List<HoleESP.ScoredCandidate> var17 = this.Ц(var11, HoleESP.PlacementKind.OBSIDIAN, var2, 24);
         this.Ц(var16, var9);
         this.Ц(var17, var9);
         var16.sort(
            Comparator.<HoleESP.ScoredCandidate, Boolean>comparing(var0 -> !var0.score.favorable())
               .thenComparing(var0 -> -var0.score.enemyDamage())
               .thenComparing(var0 -> -var0.score.value())
         );
         var17.sort(Comparator.<HoleESP.ScoredCandidate, Float>comparing(var0 -> -var0.score.enemyDamage()).thenComparing(var0 -> -var0.score.value()));
         ArrayList var18 = new ArrayList();
         HashSet var19 = new HashSet();
         int var20 = Math.round(this.ВЛ.getValue());

         for (HoleESP.ScoredCandidate var22 : var16) {
            if (var18.size() >= var20) {
               break;
            }

            if (var19.add(var22.pos)) {
               var18.add(new HoleESP.RenderCandidate(var22.pos, HoleESP.PlacementKind.CRYSTAL, var22.score.favorable()));
            }
         }

         int var25 = Math.round(this.В1.getValue());
         int var26 = 0;

         for (HoleESP.ScoredCandidate var24 : var17) {
            if (var26 >= var25) {
               break;
            }

            if (var19.add(var24.pos)) {
               var18.add(new HoleESP.RenderCandidate(var24.pos, HoleESP.PlacementKind.OBSIDIAN, var24.score.favorable()));
               var26++;
            }
         }

         this.сД.clear();
         this.сД.addAll(var18);
      }
   }

   private void Ц(
      BufferBuilder var1,
      Matrix4f var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13,
      float var14,
      Vector4f var15
   ) {
      this.Ц(var1, var2, var3, var4, var5, var15);
      this.Ц(var1, var2, var6, var7, var8, var15);
      this.Ц(var1, var2, var9, var10, var11, var15);
      this.Ц(var1, var2, var12, var13, var14, var15);
   }

   private HoleESP.DamageScore Ц(Vec3 var1, List<LivingEntity> var2) {
      float var3 = CombatCalculations.calculateExplosionDamage(var1, MINECRAFT_CLIENT.player);
      float var4 = 0.0F;
      int var5 = Math.min(var2.size(), 2);

      for (int var6 = 0; var6 < var5; var6++) {
         var4 = Math.max(var4, CombatCalculations.calculateExplosionDamage(var1, (Entity)var2.get(var6)));
      }

      return new HoleESP.DamageScore(false, var4 - var3 * Въа, var4, var3);
   }

   private List<HoleESP.ScoredCandidate> Ц(List<HoleESP.CrystalPosition> var1, HoleESP.PlacementKind var2, List<LivingEntity> var3, int var4) {
      ArrayList var5 = new ArrayList(Math.min(var1.size(), var4));
      int var6 = Math.min(var1.size(), var4);

      for (int var7 = 0; var7 < var6; var7++) {
         HoleESP.CrystalPosition var8 = (HoleESP.CrystalPosition)var1.get(var7);
         var5.add(new HoleESP.ScoredCandidate(var8.pos, var2, this.Ц(var8.crystalCenter, var3)));
      }

      return var5;
   }

   private int method_5(int var1, float var2) {
      return this.Ц(
         Math.round(Mth.lerpInt(var2, ColorMath.getRed(var1), 255)),
         Math.round(Mth.lerpInt(var2, ColorMath.getGreen(var1), 255)),
         Math.round(Mth.lerpInt(var2, ColorMath.getBlue(var1), 255)),
         255
      );
   }

   private boolean и(MutableBlockPos var1) {
      int var2 = var1.getX();
      int var3 = var1.getY();
      int var4 = var1.getZ();

      for (Direction var8 : В) {
         BlockState var9 = MINECRAFT_CLIENT.level.getBlockState(var1.set(var2 + var8.getStepX(), var3 + var8.getStepY(), var4 + var8.getStepZ()));
         if (!var9.canBeReplaced() && !var9.getCollisionShape(MINECRAFT_CLIENT.level, var1).isEmpty()) {
            var1.set(var2, var3, var4);
            return true;
         }
      }

      var1.set(var2, var3, var4);
      return false;
   }

   private record RenderCandidate(BlockPos pos, HoleESP.PlacementKind type, boolean favorable) {
   }

   private record DamageScore(boolean favorable, float value, float enemyDamage, float selfDamage) {
   }

   private record CrystalPosition(BlockPos pos, Vec3 crystalCenter) {
   }

   private enum PlacementKind {
      CRYSTAL,
      OBSIDIAN;
   }

   private record ScoredCandidate(BlockPos pos, HoleESP.PlacementKind type, HoleESP.DamageScore score) {
   }
}
