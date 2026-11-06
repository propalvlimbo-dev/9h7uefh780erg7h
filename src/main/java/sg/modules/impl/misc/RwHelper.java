package sg.modules.impl.misc;

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
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import settings.render.ClientResourceIds;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.network.ClientChat;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.InventoryActions;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ReallyWorldItemCooldowns;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.AttackEntityEvent;
import sg.modules.impl.init.BoundItemIndicator;
import sg.settings.FloatSetting;
import sg.events.impl.InputEvent;
import sg.modules.impl.init.InventoryActionCoordinator;
import sg.settings.KeybindSetting;

public class RwHelper extends ClientModule {
   private static final float ДН = 8.0F;
   private static final long Хб = 6000L;
   private static final long ХЫ = 420L;
   private static final long Хl = 380L;
   private static final long Х8 = 60000L;
   private static final float Дн = 0.012F;
   private static final List<BlockPos> сШ = м_2571477d();
   private static final Set<Long> Щ_2f23a69a = Б(сШ);
   private static final List<RwHelper.FaceOffset> сы = н_2571477d();
   private static final List<RwHelper.LineSegment> сй = Г_2571477d();
   private static final float ДЦ;
   private static final float Дч;
   @SerializedName("4")
   private final InventoryActionCoordinator field_4 = new InventoryActionCoordinator();
   private final KeybindSetting ю;
   @SerializedName("Щ")
   private final KeybindSetting Щ_bf56bd03;
   private final KeybindSetting у;
   @SerializedName("2")
   private final KeybindSetting field_2;
   private final KeybindSetting ф;
   private final KeybindSetting и;
   private final FloatSetting ХИ;
   private final BooleanSetting Вй;
   private final BooleanSetting ВЖ;
   private final BooleanSetting Вм;
   @SerializedName("9")
   private final KeybindSetting field_9;
   private final RenderPipeline сЗ;
   @SerializedName("В")
   private RwHelper.TrapUseStage В_c5409741;
   @SerializedName("В")
   private ReallyWorldItemCooldowns.TrackedItem В_2c346f44;
   private int пю;
   private int пЩ;
   private int пу;
   private int п2;
   private long Х5;
   private boolean Х0;
   @SerializedName("В")
   private ChunkPos В_d18fc59c;
   private double Яъ;
   private boolean Хх;
   private boolean ХУ;
   private long Хв;
   private long Хъ;
   @SerializedName("Я")
   private ReallyWorldItemCooldowns.TrackedItem Я_at_dcedea96;
   private final List<RwHelper.TrapMarker> сЖ;
   private final EventListener<InputEvent> ЯЗ;
   private final EventListener<PlayerTickEvent> ЯЕ;
   private final EventListener<AttackEntityEvent> ЯП;
   private final EventListener<WorldEffectsEvent> Я西;
   private static final String В97 = "RwHelper";
   private static final String В9Р = "Помощник донат-предметов ReallyWorld";
   private static final String В93 = "Ловушка";
   private static final String В9р = "Эндер Ловушка";
   private static final String В96 = "Каменная ловушка (BravoHvH)";
   private static final String В9_ = "Адская ловушка (BravoHvH)";
   private static final String В9Ш = "АнтиПолёт";
   private static final String В9ы = "Тёмный Пульс";
   private static final String В9й = "Дистанция цели";
   private static final float В9Ж = 6.0F;
   private static final float В9м = 12.0F;
   private static final float В9К = 0.5F;
   private static final String В9I = "Превью ловушки";
   private static final String В9Т = "Рендер АнтиПолёта";
   private static final String В9О = "CordDropper";
   private static final String В9Ф = "Бинд CordDropper";
   private static final String В9Й = "rose/rw_helper";
   private static final String В9к = "target_esp_crystal";
   private static final String В9г = "target_esp_crystal";
   private static final long В90 = -1L;
   private static final String В9х = "hud/ender_trap";
   private static final float В9У = 1.5F;
   private static final long В9З = -1L;
   private static final long В9Е = 60000L;
   private static final long В9П = 380L;
   private static final long В9西 = -1L;
   private static final float В9д = 8.0F;
   private static final long В9Б = 6000L;
   private static final long В9С = 350L;
   private static final long В9з = 120L;
   private static final long В9ц = 60L;
   private static final float В9я = 8.0F;
   private static final float Вос = 420.0F;
   private static final float ВоВ = 380.0F;
   private static final float ВоЯ = 0.01F;
   private static final float ВоХ = 0.18F;
   private static final float Вое = 0.82F;
   private static final float ВоА = 0.85F;
   private static final float Во4 = (float) Math.PI;
   private static final float Воп = 0.04F;
   private static final float ВоД = 0.18F;
   private static final float ВоЪ = 0.35F;
   private static final float Вот = 0.65F;
   private static final float Воа = 0.08F;
   private static final int ВоЮ = -10696449;
   private static final float ВоЧ = 0.35F;
   private static final float ВоЛ = 0.02F;
   private static final float Во诶 = 0.02F;
   private static final float Во1 = 0.02F;
   private static final float ВоЬ = 0.02F;
   private static final float Во衣 = 0.1F;
   private static final float Вош = 0.03F;
   private static final float ВоИ = 0.03F;
   private static final float Воь = 0.03F;
   private static final float Вож = 0.03F;
   private static final float Во弟 = 0.55F;
   private static final float Воб = 0.03F;
   private static final float ВоЫ = 0.03F;
   private static final float Воl = 0.03F;
   private static final float Во8 = 0.03F;
   private static final float Во5 = 0.55F;
   private static final float Вов = 0.03F;
   private static final float Воъ = 0.03F;
   private static final float ВоМ = 0.03F;
   private static final float ВоН = 0.03F;
   private static final float Вон = 0.55F;
   private static final float ВоЦ = 0.03F;
   private static final float Воч = 0.03F;
   private static final float ВоЭ = 0.03F;
   private static final float Во必 = 0.03F;
   private static final float Вощ = 0.55F;
   private static final float ВоГ = 0.5F;
   private static final float Вол = 0.5F;
   private static final float Воэ = 2.2F;
   private static final float Вою = 0.28F;
   private static final float ВоЩ = 0.08F;
   private static final float Воу = 0.02F;
   private static final float Во2 = 0.12F;
   private static final float Воф = 0.09F;
   private static final int Вои = -4162305;
   private static final int Во9 = -10696449;
   private static final float Воо = 0.62F;
   private static final int ВоO = -1521409;
   private static final int Во7 = -4657921;
   private static final float ВоР = 0.5F;
   private static final float Во3 = 0.001F;
   private static final float Вор = 0.01F;
   private static final float Во6 = 0.01F;
   private static final float Во_ = 0.5F;
   private static final float ВоШ = 0.5F;
   private static final float Воы = 0.5F;
   private static final float Вой = 0.62F;
   private static final float ВоЖ = 0.38F;
   private static final float Вом = 2.15F;
   private static final float ВоК = 0.31F;
   private static final float ВоI = 0.5F;
   private static final float ВоТ = 0.01F;
   private static final float ВоО = 0.55F;
   private static final float ВоФ = 0.45F;
   private static final float ВоЙ = 2.8F;
   private static final float Вок = 0.4F;
   private static final float Вог = 0.25F;
   private static final float Во0 = 0.35F;
   private static final float Вох = 0.055F;
   private static final float ВоУ = 0.04F;
   private static final float ВоЗ = 0.5F;
   private static final float ВоЕ = 0.01F;
   private static final float ВоП = 0.012F;
   private static final float Во西 = 0.7F;
   private static final float Вод = 0.3F;
   private static final float ВоБ = 0.22F;
   private static final float ВоС = 0.18F;
   private static final float Воз = 0.72F;
   private static final float Воц = 0.22F;
   private static final float Воя = 0.18F;
   private static final float ВOс = 0.04F;
   private static final float ВOВ = 1.0E-4F;
   private static final float ВOЯ = 0.9F;
   private static final float ВOХ = 0.9F;
   private static final float ВOе = 1.0E-4F;
   private static final float ВOА = 1.0E-4F;
   private static final float ВO4 = 0.38F;
   private static final float ВOп = 6.0F;
   private static final float ВOД = 15.0F;
   private static final float ВOЪ = 10.0F;
   private static final float ВOт = 255.0F;
   private static final int ВOа = 16777215;
   private static final long ВOЮ = 4294967295L;
   private static final long ВOЧ = 4294967295L;
   private static final long ВOЛ = 1023L;
   private static final long ВO诶 = 1023L;
   private static final long ВO1 = 1023L;
   private static final double ВOЬ = 3.5;
   private static final String ВO衣 = "rwHelper";
   private static final String ВOш = "rwHelperVertices";

   private void шД() {
      if (this.В_2c346f44 == null) {
         this.шШ();
      } else {
         if (this.пЩ == -1) {
            int var1 = this.Б(this.В_2c346f44);
            if (var1 == -1) {
               this.шШ();
               return;
            }

            if (var1 == 40) {
               MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.OFF_HAND);
               MINECRAFT_CLIENT.player.swing(InteractionHand.OFF_HAND);
               this.шШ();
               return;
            }

            this.пЩ = MINECRAFT_CLIENT.player.getInventory().selected;
            if (var1 < 9) {
               this.пу = var1;
            } else {
               this.пю = this.西(var1);
            }
         }

         InventoryActionCoordinator.Result var2 = this.field_4.л(() -> {
            if (this.пу != -1) {
               MINECRAFT_CLIENT.player.getInventory().selected = this.пу;
            } else {
               InventoryActions.method_7(this.пЩ, this.пю);
            }

            MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
            MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
         }, true);
         if (var2 == InventoryActionCoordinator.Result.SUCCESS) {
            this.В_c5409741 = RwHelper.TrapUseStage.RESTORE_TRAP;
         }
      }
   }

   private static List<BlockPos> м_2571477d() {
      LinkedHashSet var0 = new LinkedHashSet();
      double var1 = ВOЬ;
      double var3 = 1.0 / var1;
      int var5 = (int)Math.ceil(var1);

      label49:
      for (int var6 = 0; var6 <= var5; var6++) {
         double var7 = var6 * var3;
         double var9 = (var6 + 1) * var3;

         for (int var11 = 0; var11 <= var5; var11++) {
            double var12 = var11 * var3;
            double var14 = (var11 + 1) * var3;

            for (int var16 = 0; var16 <= var5; var16++) {
               double var17 = var16 * var3;
               double var19 = (var16 + 1) * var3;
               if (衣(var7, var12, var17) > 1.0) {
                  if (var16 == 0) {
                     if (var11 == 0) {
                        return List.copyOf(var0);
                     }
                     continue label49;
                  }
                  break;
               }

               if (!(衣(var9, var12, var17) <= 1.0) || !(衣(var7, var14, var17) <= 1.0) || !(衣(var7, var12, var19) <= 1.0)) {
                  var0.add(new BlockPos(var6, var11, var16));
                  var0.add(new BlockPos(-var6, var11, var16));
                  var0.add(new BlockPos(var6, -var11, var16));
                  var0.add(new BlockPos(var6, var11, -var16));
                  var0.add(new BlockPos(-var6, -var11, var16));
                  var0.add(new BlockPos(var6, -var11, -var16));
                  var0.add(new BlockPos(-var6, var11, -var16));
                  var0.add(new BlockPos(-var6, -var11, -var16));
               }
            }
         }
      }

      return List.copyOf(var0);
   }

   private Player Б(UUID var1) {
      if (MINECRAFT_CLIENT.level != null && var1 != null) {
         for (Player var3 : MINECRAFT_CLIENT.level.players()) {
            if (var3.getUUID().equals(var1) && var3 != MINECRAFT_CLIENT.player) {
               return var3;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private void ш(BufferBuilder var1, int var2) {
      MeshData var3 = var1.build();
      if (var3 != null) {
         if (var2 <= 0) {
            var3.close();
         } else {
            GpuBuffer var4 = null;

            try {
               DrawState var5 = var3.drawState();
               int var6 = var5.indexCount();
               if (var6 > 0) {
                  AutoStorageIndexBuffer var7 = RenderSystem.getSequentialBuffer(var5.primitiveTopology());
                  GpuBuffer var8 = var7.getBuffer(var6);
                  IndexType var9 = var7.type();
                  var4 = RenderSystem.getDevice().createBuffer(() -> ВOш, 32, var3.vertexBuffer());
                  CommandEncoder var10 = RenderSystem.getDevice().createCommandEncoder();
                  RenderTarget var11 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();

                  try (RenderPass var12 = var10.createRenderPass(
                        () -> ВO衣, var11.getColorTextureView(), Optional.empty(), var11.getDepthTextureView(), OptionalDouble.empty()
                     )) {
                     RenderSystem.bindDefaultUniforms(var12);
                     var12.setPipeline(this.сЗ);
                     var12.setVertexBuffer(0, var4.slice());
                     var12.setIndexBuffer(var8, var9);
                     var12.drawIndexed(var6, 1, 0, 0, 0);
                     return;
                  }
               }
            } finally {
               if (var4 != null) {
                  var4.close();
               }

               var3.close();
            }
         }
      }
   }

   private static long Б(int var0, int var1, int var2) {
      return (var0 + 512 & ВOЛ) << 20 | (var1 + 512 & ВO诶) << 10 | var2 + 512 & ВO1;
   }

   private static List<RwHelper.LineSegment> Г_2571477d() {
      LinkedHashSet var0 = new LinkedHashSet();
      ArrayList var1 = new ArrayList();

      for (RwHelper.FaceOffset var3 : сы) {
         float var4 = var3.ox();
         float var5 = var3.oy();
         float var6 = var3.oz();
         float[][] var7 = Б(var3);

         for (int var8 = 0; var8 < 4; var8++) {
            float[] var9 = var7[var8];
            float[] var10 = var7[var8 + 1 & 3];
            long var11 = Б(var4 + var9[0], var5 + var9[1], var6 + var9[2], var4 + var10[0], var5 + var10[1], var6 + var10[2]);
            if (var0.add(var11)) {
               var1.add(new RwHelper.LineSegment(var4 + var9[0], var5 + var9[1], var6 + var9[2], var4 + var10[0], var5 + var10[1], var6 + var10[2]));
            }
         }
      }

      return List.copyOf(var1);
   }

   private float Ь(float var1, float var2) {
      float var3 = Воя;
      return Mth.clamp((var2 + var3 - var1) / var3, 0.0F, 1.0F);
   }

   private int Б(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, int var11) {
      float var12 = var6 - var3;
      float var13 = var7 - var4;
      float var14 = var8 - var5;
      float var15 = Mth.sqrt(var12 * var12 + var13 * var13 + var14 * var14);
      if (var15 < ВOВ) {
         return 0;
      }

      float var16 = Math.abs(var12) < ВOЯ ? 1.0F : 0.0F;
      float var17 = Math.abs(var12) < ВOХ ? 0.0F : 1.0F;
      float var18 = var17 * var14;
      float var19 = -var16 * var14;
      float var20 = var16 * var13 - var17 * var12;
      float var21 = Mth.sqrt(var18 * var18 + var19 * var19 + var20 * var20);
      if (var21 < ВOе) {
         return 0;
      }

      var18 = var18 / var21 * var9;
      var19 = var19 / var21 * var9;
      var20 = var20 / var21 * var9;
      float var22 = var13 * var20 - var14 * var19;
      float var23 = var14 * var18 - var12 * var20;
      float var24 = var12 * var19 - var13 * var18;
      float var25 = Mth.sqrt(var22 * var22 + var23 * var23 + var24 * var24);
      if (var25 < ВOА) {
         return 0;
      }

      var22 = var22 / var25 * var9;
      var23 = var23 / var25 * var9;
      var24 = var24 / var25 * var9;
      Vector4f var26 = ColorMath.toRgbaVector(var10);
      Vector4f var27 = ColorMath.toRgbaVector(var11);
      this.у(
         var1,
         var2,
         var3 + var18,
         var4 + var19,
         var5 + var20,
         var3 - var18,
         var4 - var19,
         var5 - var20,
         var6 - var18,
         var7 - var19,
         var8 - var20,
         var6 + var18,
         var7 + var19,
         var8 + var20,
         var26
      );
      this.у(
         var1,
         var2,
         var3 + var22,
         var4 + var23,
         var5 + var24,
         var3 - var22,
         var4 - var23,
         var5 - var24,
         var6 - var22,
         var7 - var23,
         var8 - var24,
         var6 + var22,
         var7 + var23,
         var8 + var24,
         var26
      );
      float var28 = ВO4;
      this.у(
         var1,
         var2,
         var3 + var18 * var28,
         var4 + var19 * var28,
         var5 + var20 * var28,
         var3 - var18 * var28,
         var4 - var19 * var28,
         var5 - var20 * var28,
         var6 - var18 * var28,
         var7 - var19 * var28,
         var8 - var20 * var28,
         var6 + var18 * var28,
         var7 + var19 * var28,
         var8 + var20 * var28,
         var27
      );
      return 12;
   }

   private int Б(
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
      int var15
   ) {
      Vector4f var16 = ColorMath.toRgbaVector(var15);
      this.ж(var1, var2, var3, var4, var5, var16);
      this.ж(var1, var2, var6, var7, var8, var16);
      this.ж(var1, var2, var9, var10, var11, var16);
      this.ж(var1, var2, var12, var13, var14, var16);
      return 4;
   }

   private void ц(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameRenderer != null) {
         float var2 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         Vec3 var3 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         Matrix4f var4 = var1.getPoses().last().pose();
         BufferBuilder var5 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         int var6 = 0;
         if (this.ВЖ.getValue() && this.тн()) {
            var6 += this.Б(var5, var4, var3, var2);
         }

         if (this.Вй.getValue() && !this.сЖ.isEmpty()) {
            ReallyWorldItemCooldowns.TrackedItem var7 = this.Б_2c346f44();
            if (var7 != null) {
               this.Я_at_dcedea96 = var7;
            }

            ReallyWorldItemCooldowns.TrackedItem var8 = this.Я_at_dcedea96 != null ? this.Я_at_dcedea96 : ReallyWorldItemCooldowns.TrackedItem.TRAP;
            var6 += this.Б(var5, var4, var3, var2, var8);
         }

         this.ш(var5, var6);
      }
   }

   private void Ф(InputEvent var1) {
      if (var1.getAction() == 1 && var1.getInputCode() >= 0 && MINECRAFT_CLIENT.gui.screen() == null) {
         if (this.Вм.getValue() && this.у(this.field_9, var1.getInputCode())) {
            this.шТ();
         } else if (this.В_c5409741 == RwHelper.TrapUseStage.IDLE) {
            ReallyWorldItemCooldowns.TrackedItem var2 = this.Б(var1.getInputCode());
            if (var2 != null) {
               int var3 = this.Б(var2);
               if (var3 == -1) {
                  ClientChat.З(var2.бЩ() + " не найдена");
               } else {
                  this.В_2c346f44 = var2;
                  if (var2.С_ecbefc53() == ReallyWorldItemCooldowns.ActivationMethod.USE) {
                     this.В_c5409741 = RwHelper.TrapUseStage.USE_TRAP;
                  } else {
                     this.В_c5409741 = RwHelper.TrapUseStage.SWAP_OFFHAND;
                  }

                  this.Х5 = System.currentTimeMillis();
               }
            }
         }
      }
   }

   private ReallyWorldItemCooldowns.TrackedItem Б(int var1) {
      if (this.у(this.ю, var1)) {
         return ReallyWorldItemCooldowns.TrackedItem.TRAP;
      } else if (this.у(this.Щ_bf56bd03, var1)) {
         return ReallyWorldItemCooldowns.TrackedItem.ENDER_TRAP;
      } else if (this.у(this.у, var1)) {
         return ReallyWorldItemCooldowns.TrackedItem.STONE_TRAP;
      } else if (this.у(this.field_2, var1)) {
         return ReallyWorldItemCooldowns.TrackedItem.HELL_TRAP;
      } else if (this.у(this.ф, var1)) {
         return ReallyWorldItemCooldowns.TrackedItem.ANTI_FLIGHT;
      } else {
         return this.у(this.и, var1) ? ReallyWorldItemCooldowns.TrackedItem.DARK_PULSE : null;
      }
   }

   private void у(
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
      this.ж(var1, var2, var3, var4, var5, var15);
      this.ж(var1, var2, var6, var7, var8, var15);
      this.ж(var1, var2, var9, var10, var11, var15);
      this.ж(var1, var2, var12, var13, var14, var15);
   }

   private void шЧ() {
      if (this.Х0) {
         if (MINECRAFT_CLIENT.options != null) {
            MINECRAFT_CLIENT.options.keyShift.setDown(false);
         }

         this.Х0 = false;
      }
   }

   private static int ъ(int var0, float var1) {
      return Math.round(ВOт * Mth.clamp(var1, 0.0F, 1.0F)) << 24 | var0 & ВOа;
   }

   private int Б(BufferBuilder var1, Matrix4f var2, Vec3 var3, float var4, ReallyWorldItemCooldowns.TrackedItem var5) {
      float var6 = ((float)MINECRAFT_CLIENT.level.getGameTime() + var4) * Воф;
      boolean var7 = var5 == ReallyWorldItemCooldowns.TrackedItem.ENDER_TRAP;
      int var8 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      int var9 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO);
      int var10 = ColorMath.blendArgb(var8, var7 ? Вои : Во9, Воо);
      int var11 = ColorMath.blendArgb(var9, var7 ? ВоO : Во7, ВоР);
      float var12 = Math.max(Во3, Дч - ДЦ);
      int var13 = 0;

      for (RwHelper.TrapMarker var15 : this.сЖ) {
         if (!var15.Шл()) {
            float var16 = var15.method_2();
            if (!(var16 <= Вор)) {
               float var17 = var15.method__();
               float var18 = var15.ч() * (1.0F - this.弟б(var15.Я()));
               if (!(var18 <= Во6)) {
                  Player var19 = this.Б(var15.Х);
                  double var20;
                  double var22;
                  double var24;
                  if (var19 != null) {
                     var20 = Mth.lerp(var4, var19.xo, var19.getX());
                     var22 = Mth.lerp(var4, var19.yo, var19.getY());
                     var24 = Mth.lerp(var4, var19.zo, var19.getZ());
                     var15.я(var20, var22, var24, var19.getId());
                  } else {
                     var20 = var15.西;
                     var22 = var15.д;
                     var24 = var15.Б;
                  }

                  BlockPos var26 = BlockPos.containing(var20, var22, var24).above();
                  float var27 = var26.getX() + Во_;
                  float var28 = var26.getY() + (ДЦ + Дч) * ВоШ;
                  float var29 = var26.getZ() + Воы;
                  float var30 = Вой + ВоЖ * Mth.sin(var6 * Вом + var15.со * ВоК);

                  for (RwHelper.FaceOffset var32 : сы) {
                     float var33 = (var32.oy() + ВоI - ДЦ) / var12;
                     float var34 = this.Ь(var33, var18);
                     if (!(var34 <= ВоТ)) {
                        BlockPos var35 = var26.offset(var32.ox(), var32.oy(), var32.oz());
                        float var36 = ВоО + ВоФ * Mth.sin(var6 * ВоЙ + var32.ox() * Вок + var32.oy() * Вог + var32.oz() * Во0);
                        var13 += this.Б(
                           var1,
                           var2,
                           var3,
                           var35,
                           var32.axis(),
                           var32.positive(),
                           var27,
                           var28,
                           var29,
                           var17,
                           ъ(var10, (Вох + ВоУ * var36 * var30) * var16 * var34)
                        );
                     }
                  }

                  for (RwHelper.LineSegment var43 : сй) {
                     float var44 = (var43.y1() + var43.y2()) * ВоЗ;
                     float var45 = (var44 - ДЦ) / var12;
                     float var46 = this.Ь(var45, var18);
                     if (!(var46 <= ВоЕ)) {
                        float var47 = this.Я(var26.getX() + var43.x1(), var27, var17) - (float)var3.x;
                        float var37 = this.Я(var26.getY() + var43.y1(), var28, var17) - (float)var3.y;
                        float var38 = this.Я(var26.getZ() + var43.z1(), var29, var17) - (float)var3.z;
                        float var39 = this.Я(var26.getX() + var43.x2(), var27, var17) - (float)var3.x;
                        float var40 = this.Я(var26.getY() + var43.y2(), var28, var17) - (float)var3.y;
                        float var41 = this.Я(var26.getZ() + var43.z2(), var29, var17) - (float)var3.z;
                        var13 += this.Б(
                           var1,
                           var2,
                           var47,
                           var37,
                           var38,
                           var39,
                           var40,
                           var41,
                           ВоП * (Во西 + Вод * var17),
                           ъ(var11, (ВоБ + ВоС * var30) * var16 * var46),
                           ъ(var10, (Воз + Воц * var30) * var16 * var46)
                        );
                     }
                  }
               }
            }
         }
      }

      return var13;
   }

   private static boolean у(ReallyWorldItemCooldowns.TrackedItem var0) {
      return var0 == ReallyWorldItemCooldowns.TrackedItem.TRAP
         || var0 == ReallyWorldItemCooldowns.TrackedItem.ENDER_TRAP
         || var0 == ReallyWorldItemCooldowns.TrackedItem.STONE_TRAP
         || var0 == ReallyWorldItemCooldowns.TrackedItem.HELL_TRAP;
   }

   private int Б(
      BufferBuilder var1, Matrix4f var2, Vec3 var3, BlockPos var4, int var5, boolean var6, float var7, float var8, float var9, float var10, int var11
   ) {
      float var12 = ВOс;
      float var13 = this.Я(var4.getX() + var12, var7, var10) - (float)var3.x;
      float var14 = this.Я(var4.getX() + 1.0F - var12, var7, var10) - (float)var3.x;
      float var15 = this.Я(var4.getY() + var12, var8, var10) - (float)var3.y;
      float var16 = this.Я(var4.getY() + 1.0F - var12, var8, var10) - (float)var3.y;
      float var17 = this.Я(var4.getZ() + var12, var9, var10) - (float)var3.z;
      float var18 = this.Я(var4.getZ() + 1.0F - var12, var9, var10) - (float)var3.z;
      Vector4f var19 = ColorMath.toRgbaVector(var11);
      switch (var5) {
         case 0:
            float var22 = var6 ? var14 : var13;
            this.у(var1, var2, var22, var15, var17, var22, var16, var17, var22, var16, var18, var22, var15, var18, var19);
            break;
         case 1:
            float var21 = var6 ? var16 : var15;
            this.у(var1, var2, var13, var21, var17, var14, var21, var17, var14, var21, var18, var13, var21, var18, var19);
            break;
         default:
            float var20 = var6 ? var18 : var17;
            this.у(var1, var2, var13, var15, var20, var14, var15, var20, var14, var16, var20, var13, var16, var20, var19);
      }

      return 4;
   }

   public RwHelper() {
      super(В97, В9Р, ModuleCategory.MISC);
      this.ю = new KeybindSetting(В93);
      this.Щ_bf56bd03 = new KeybindSetting(В9р);
      this.у = new KeybindSetting(В96);
      this.field_2 = new KeybindSetting(В9_);
      this.ф = new KeybindSetting(В9Ш);
      this.и = new KeybindSetting(В9ы);
      this.ХИ = new FloatSetting(В9й, В9Ж, 2.0F, В9м).withStep(В9К);
      this.Вй = new BooleanSetting(В9I, true);
      this.ВЖ = new BooleanSetting(В9Т, true);
      this.Вм = new BooleanSetting(В9О, true);
      this.field_9 = new KeybindSetting(В9Ф, -1, this.Вм::getValue);
      this.сЗ = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(В9Й)
         .withVertexShader(ClientResourceIds.getCoreId(В9к))
         .withFragmentShader(ClientResourceIds.getCoreId(В9г))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
      this.В_c5409741 = RwHelper.TrapUseStage.IDLE;
      this.пю = -1;
      this.пЩ = -1;
      this.пу = -1;
      this.п2 = -1;
      this.Хъ = В90;
      this.сЖ = new ArrayList<>(2);
      this.ЯЗ = this::Ф;
      this.ЯЕ = this::弟Ш;
      this.ЯП = this::Я;
      this.Я西 = this::ц;
      this.addSettings(this.ю, this.Щ_bf56bd03, this.у, this.field_2, this.ф, this.и, this.ХИ, this.Вй, this.ВЖ, this.Вм, this.field_9);
   }

   private void ж(UUID var1) {
      for (RwHelper.TrapMarker var3 : this.сЖ) {
         if (var3.Х.equals(var1)) {
            var3.ын();
            return;
         }
      }

      this.сЖ.add(new RwHelper.TrapMarker(var1));
      int var5 = 0;

      for (RwHelper.TrapMarker var4 : this.сЖ) {
         if (!var4.ШГ()) {
            var5++;
         }
      }

      if (var5 > 2) {
         for (RwHelper.TrapMarker var8 : this.сЖ) {
            if (!var8.ШГ()) {
               var8.ыь();
               break;
            }
         }
      }
   }

   private float Я(float var1, float var2, float var3) {
      return var2 + (var1 - var2) * var3;
   }

   private boolean у(KeybindSetting var1, int var2) {
      return var1.getValue() >= 0 && var1.getValue() == var2;
   }

   private int 西(int var1) {
      return var1 < 9 ? var1 + 36 : var1;
   }

   private void шШ() {
      if (this.Х0) {
         this.шЧ();
      }

      this.В_c5409741 = RwHelper.TrapUseStage.IDLE;
      this.В_2c346f44 = null;
      this.пю = -1;
      this.пЩ = -1;
      this.пу = -1;
      this.п2 = -1;
      this.Х5 = 0L;
   }

   private static double 衣(double var0, double var2, double var4) {
      return var0 * var0 + var2 * var2 + var4 * var4;
   }

   private void ш5() {
      if (this.В_2c346f44 == null) {
         this.шШ();
      } else {
         ReallyWorldItemCooldowns.TrackedItem var1 = ReallyWorldItemCooldowns.Ы(MINECRAFT_CLIENT.player.getOffhandItem());
         if (var1 != this.В_2c346f44) {
            if (System.currentTimeMillis() - this.Х5 > В9С) {
               this.шШ();
            }
         } else {
            this.шР();
            if (System.currentTimeMillis() - this.Х5 >= В9з) {
               this.шЧ();
               if (this.п2 != -1) {
                  this.В_c5409741 = RwHelper.TrapUseStage.RESTORE_OFFHAND;
                  this.Х5 = System.currentTimeMillis();
               } else {
                  this.шШ();
               }
            }
         }
      }
   }

   private float 弟4(float var1) {
      var1 = Mth.clamp(var1, 0.0F, 1.0F);
      return var1 * var1 * var1 * (var1 * (var1 * ВOп - ВOД) + ВOЪ);
   }

   static {
      float var0 = Float.POSITIVE_INFINITY;
      float var1 = Float.NEGATIVE_INFINITY;

      for (BlockPos var3 : сШ) {
         var0 = Math.min(var0, var3.getY());
         var1 = Math.max(var1, var3.getY() + 1.0F);
      }

      ДЦ = var0;
      Дч = var1;
   }

   private int Б(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9, int var10) {
      Vector4f var11 = ColorMath.toRgbaVector(var9);
      Vector4f var12 = ColorMath.toRgbaVector(var10);
      this.ж(var1, var2, var3, var4, var5, var11);
      this.ж(var1, var2, var6, var4, var8, var11);
      this.ж(var1, var2, var6, var7, var8, var12);
      this.ж(var1, var2, var3, var7, var5, var12);
      return 4;
   }

   private void шз() {
      if (this.пЩ != -1 && (this.пю != -1 || this.пу != -1)) {
         InventoryActionCoordinator.Result var1 = this.field_4.л(() -> {
            if (this.пу != -1) {
               MINECRAFT_CLIENT.player.getInventory().selected = this.пЩ;
            } else {
               InventoryActions.method_7(this.пЩ, this.пю);
            }
         }, true);
         if (var1 == InventoryActionCoordinator.Result.SUCCESS) {
            this.шШ();
         }
      } else {
         this.шШ();
      }
   }

   private static List<RwHelper.FaceOffset> н_2571477d() {
      ArrayList var0 = new ArrayList();
      int[][] var1 = new int[][]{{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};

      for (BlockPos var3 : сШ) {
         for (int var4 = 0; var4 < var1.length; var4++) {
            int var5 = var3.getX() + var1[var4][0];
            int var6 = var3.getY() + var1[var4][1];
            int var7 = var3.getZ() + var1[var4][2];
            if (!Щ_2f23a69a.contains(BlockPos.asLong(var5, var6, var7))) {
               int var8 = var4 / 2;
               boolean var9 = var4 % 2 == 0;
               var0.add(new RwHelper.FaceOffset(var3.getX(), var3.getY(), var3.getZ(), var8, var9));
            }
         }
      }

      return List.copyOf(var0);
   }

   private void Я(AttackEntityEvent var1) {
      if (var1.getPhase() == AttackEntityEvent.Phase.POST) {
         if (var1.getAttacker() == MINECRAFT_CLIENT.player) {
            if (var1.getTarget() instanceof Player var2) {
               if (var2.isAlive() && !var2.isSpectator()) {
                  if (this.Б_2c346f44() != null) {
                     if (!(MINECRAFT_CLIENT.player.distanceTo(var2) > this.ХИ.getValue() + В9У)) {
                        this.ж(var2.getUUID());
                     }
                  }
               }
            }
         }
      }
   }

   private void 弟Ш(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null) {
         this.ш8();
         this.шх();
         if (this.Х0 && this.В_c5409741 == RwHelper.TrapUseStage.IDLE) {
            this.шЧ();
         }

         switch (this.В_c5409741) {
            case USE_TRAP:
               this.шД();
               break;
            case RESTORE_TRAP:
               this.шз();
               break;
            case SWAP_OFFHAND:
               this.шЦ();
               break;
            case SNEAK:
               this.ш5();
               break;
            case RESTORE_OFFHAND:
               this.шЛ();
         }
      } else {
         this.шШ();
         this.шю();
         this.ХУ = false;
         this.сЖ.clear();
      }
   }

   private int Б(BufferBuilder var1, Matrix4f var2, Vec3 var3, float var4) {
      ChunkPos var5 = this.В_d18fc59c;
      double var6 = this.Яъ;
      float var8 = (var5.x() << 4) - (float)var3.x;
      float var9 = (var5.x() << 4) + 16 - (float)var3.x;
      float var10 = (var5.z() << 4) - (float)var3.z;
      float var11 = (var5.z() << 4) + 16 - (float)var3.z;
      float var12 = (float)(var6 - var3.y);
      float var13 = var12 - 1.0F;
      float var14 = var12 + В9я;
      float var15 = Mth.clamp((float)(System.currentTimeMillis() - this.Хв) / Вос, 0.0F, 1.0F);
      float var16 = this.弟4(var15);
      float var17 = this.弟б(var15);
      float var18 = this.Хъ < 0L ? 0.0F : Mth.clamp((float)(System.currentTimeMillis() - this.Хъ) / ВоВ, 0.0F, 1.0F);
      float var19 = var16 * (1.0F - this.弟4(var18));
      if (var19 <= ВоЯ) {
         return 0;
      }

      float var20 = (ВоХ + Вое * var17) * (1.0F - var18 * ВоА);
      float var21 = var13 + (var14 - var13) * var20;
      float var22 = Mth.sin(var15 * Во4) * Воп;
      float var23 = ВоД * (ВоЪ + Вот * var17);
      float var24 = ((float)MINECRAFT_CLIENT.level.getGameTime() + var4) * Воа;
      int var25 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      int var26 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO);
      int var27 = ColorMath.blendArgb(var25, ВоЮ, ВоЧ);
      int var28 = 0;
      var28 += this.Б(var1, var2, var8, var13 + ВоЛ, var10, var9, var13 + Во诶, var10, var9, var13 + Во1, var11, var8, var13 + ВоЬ, var11, ъ(var27, Во衣 * var19));
      var28 += this.Б(
         var1,
         var2,
         var8,
         var13 + Вош,
         var10,
         var8 + var23,
         var13 + ВоИ,
         var10,
         var8 + var23,
         var13 + Воь,
         var11,
         var8,
         var13 + Вож,
         var11,
         ъ(var25, Во弟 * var19)
      );
      var28 += this.Б(
         var1,
         var2,
         var9 - var23,
         var13 + Воб,
         var10,
         var9,
         var13 + ВоЫ,
         var10,
         var9,
         var13 + Воl,
         var11,
         var9 - var23,
         var13 + Во8,
         var11,
         ъ(var25, Во5 * var19)
      );
      var28 += this.Б(
         var1,
         var2,
         var8,
         var13 + Вов,
         var10,
         var9,
         var13 + Воъ,
         var10,
         var9,
         var13 + ВоМ,
         var10 + var23,
         var8,
         var13 + ВоН,
         var10 + var23,
         ъ(var25, Вон * var19)
      );
      var28 += this.Б(
         var1,
         var2,
         var8,
         var13 + ВоЦ,
         var11 - var23,
         var9,
         var13 + Воч,
         var11 - var23,
         var9,
         var13 + ВоЭ,
         var11,
         var8,
         var13 + Во必,
         var11,
         ъ(var25, Вощ * var19)
      );
      float var29 = ВоГ + Вол * Mth.sin(var24 * Воэ);
      int var30 = ъ(var27, (Вою + ВоЩ * var29) * var19);
      int var31 = ъ(var26, Воу * var19);
      var28 += this.Б(var1, var2, var8, var13, var10, var9, var21, var10, var30, var31);
      var28 += this.Б(var1, var2, var9, var13, var10, var9, var21, var11, var30, var31);
      var28 += this.Б(var1, var2, var9, var13, var11, var8, var21, var11, var30, var31);
      var28 += this.Б(var1, var2, var8, var13, var11, var8, var21, var10, var30, var31);
      return var28
         + this.Б(
            var1, var2, var8, var21 + var22, var10, var9, var21 + var22, var10, var9, var21 + var22, var11, var8, var21 + var22, var11, ъ(var25, Во2 * var19)
         );
   }

   private void шЦ() {
      if (this.В_2c346f44 == null) {
         this.шШ();
      } else {
         ReallyWorldItemCooldowns.TrackedItem var1 = ReallyWorldItemCooldowns.Ы(MINECRAFT_CLIENT.player.getOffhandItem());
         if (var1 == this.В_2c346f44) {
            this.В_c5409741 = RwHelper.TrapUseStage.SNEAK;
            this.Х5 = System.currentTimeMillis();
         } else {
            int var2 = this.Б(this.В_2c346f44);
            if (var2 == -1) {
               ClientChat.З(this.В_2c346f44.бЩ() + " не найдена");
               this.шШ();
            } else if (var2 == 40) {
               this.В_c5409741 = RwHelper.TrapUseStage.SNEAK;
               this.Х5 = System.currentTimeMillis();
            } else {
               this.п2 = this.西(var2);
               MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.containerMenu.containerId, this.п2, 40, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
               if (MINECRAFT_CLIENT.gui.screen() == null) {
                  MINECRAFT_CLIENT.player.connection.send(new ServerboundContainerClosePacket(MINECRAFT_CLIENT.player.containerMenu.containerId));
               }

               this.В_c5409741 = RwHelper.TrapUseStage.SNEAK;
               this.Х5 = System.currentTimeMillis();
            }
         }
      }
   }

   private void ш8() {
      boolean var1 = ReallyWorldItemCooldowns.Ы().Ы_bbeebd87(ReallyWorldItemCooldowns.TrackedItem.ANTI_FLIGHT);
      long var2 = System.currentTimeMillis();
      if (var1 && !this.ХУ && MINECRAFT_CLIENT.player != null) {
         this.Хх = true;
         this.Хв = var2;
         this.Хъ = В9З;
         this.В_d18fc59c = MINECRAFT_CLIENT.player.chunkPosition();
         this.Яъ = MINECRAFT_CLIENT.player.getY();
      }

      this.ХУ = var1;
      if (this.Хх || this.Хъ >= 0L) {
         boolean var4 = this.Хх && var2 - this.Хв < В9Е;
         if (var4 && MINECRAFT_CLIENT.player != null) {
            this.В_d18fc59c = MINECRAFT_CLIENT.player.chunkPosition();
            this.Яъ = MINECRAFT_CLIENT.player.getY();
         } else {
            if (this.Хх) {
               this.Хх = false;
               this.Хъ = var2;
            }

            if (this.Хъ >= 0L && var2 - this.Хъ >= В9П) {
               this.шю();
            }
         }
      }
   }

   private void шю() {
      this.В_d18fc59c = null;
      this.Хх = false;
      this.Хв = 0L;
      this.Хъ = В9西;
   }

   private static float[][] Б(RwHelper.FaceOffset var0) {
      return switch (var0.axis()) {
         case 0 -> {
            float var3 = var0.positive() ? 1.0F : 0.0F;
            yield new float[][]{{var3, 0.0F, 0.0F}, {var3, 1.0F, 0.0F}, {var3, 1.0F, 1.0F}, {var3, 0.0F, 1.0F}};
         }
         case 1 -> {
            float var2 = var0.positive() ? 1.0F : 0.0F;
            yield new float[][]{{0.0F, var2, 0.0F}, {1.0F, var2, 0.0F}, {1.0F, var2, 1.0F}, {0.0F, var2, 1.0F}};
         }
         default -> {
            float var1 = var0.positive() ? 1.0F : 0.0F;
            yield new float[][]{{0.0F, 0.0F, var1}, {1.0F, 0.0F, var1}, {1.0F, 1.0F, var1}, {0.0F, 1.0F, var1}};
         }
      };
   }

   private void шЛ() {
      if (this.п2 != -1 && MINECRAFT_CLIENT.player != null) {
         if (System.currentTimeMillis() - this.Х5 >= В9ц) {
            MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.containerMenu.containerId, this.п2, 40, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
            if (MINECRAFT_CLIENT.gui.screen() == null) {
               MINECRAFT_CLIENT.player.connection.send(new ServerboundContainerClosePacket(MINECRAFT_CLIENT.player.containerMenu.containerId));
            }

            this.шШ();
         }
      } else {
         this.шШ();
      }
   }

   public float уу() {
      return this.ХИ.getValue();
   }

   private void шх() {
      long var1 = System.currentTimeMillis();
      boolean var3 = this.Б_2c346f44() != null;
      float var4 = this.ХИ.getValue() + В9д;
      Iterator var5 = this.сЖ.iterator();

      while (var5.hasNext()) {
         RwHelper.TrapMarker var6 = (RwHelper.TrapMarker)var5.next();
         if (var6.Шл()) {
            var5.remove();
         } else {
            Player var7 = this.Б(var6.Х);
            boolean var8 = !var3 || var7 == null || !var7.isAlive() || var7.isSpectator() || MINECRAFT_CLIENT.player.distanceTo(var7) > var4 || var1 - var6.я > В9Б;
            if (var8) {
               if (!var6.ШГ()) {
                  var6.ыь();
               }

               if (var6.Шл()) {
                  var5.remove();
               }
            }
         }
      }
   }

   private ReallyWorldItemCooldowns.TrackedItem Б_2c346f44() {
      if (MINECRAFT_CLIENT.player == null) {
         return null;
      }

      ReallyWorldItemCooldowns.TrackedItem var1 = ReallyWorldItemCooldowns.Ы(MINECRAFT_CLIENT.player.getMainHandItem());
      if (у(var1)) {
         return var1;
      }

      ReallyWorldItemCooldowns.TrackedItem var2 = ReallyWorldItemCooldowns.Ы(MINECRAFT_CLIENT.player.getOffhandItem());
      return у(var2) ? var2 : null;
   }

   private boolean тн() {
      return this.ВЖ.getValue() && this.В_d18fc59c != null && (this.Хх || this.Хъ >= 0L);
   }

   private int Б(ReallyWorldItemCooldowns.TrackedItem var1) {
      if (MINECRAFT_CLIENT.player != null && var1 != null) {
         if (ReallyWorldItemCooldowns.Ы(MINECRAFT_CLIENT.player.getOffhandItem()) == var1) {
            return 40;
         }

         for (int var2 = 0; var2 < 36; var2++) {
            if (ReallyWorldItemCooldowns.Ы(MINECRAFT_CLIENT.player.getInventory().getItem(var2)) == var1) {
               return var2;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private float 弟б(float var1) {
      float var2 = 1.0F - Mth.clamp(var1, 0.0F, 1.0F);
      return 1.0F - var2 * var2 * var2;
   }

   private static Set<Long> Б(List<BlockPos> var0) {
      LinkedHashSet var1 = new LinkedHashSet();

      for (BlockPos var3 : var0) {
         var1.add(BlockPos.asLong(var3.getX(), var3.getY(), var3.getZ()));
      }

      return Set.copyOf(var1);
   }

   @Override
   public void onDisable() {
      this.шШ();
      this.шю();
      this.ХУ = false;
      this.сЖ.clear();
   }

   private void шТ() {
      if (MINECRAFT_CLIENT.player != null) {
         BlockPos var1 = MINECRAFT_CLIENT.player.blockPosition();
         ClientChat.т("! " + var1.getX() + " " + var1.getY() + " " + var1.getZ());
      }
   }

   public List<BoundItemIndicator> М() {
      return List.of(
         new BoundItemIndicator(ReallyWorldItemCooldowns.TrackedItem.TRAP.С_50cb85c5(), this.ю.getValue()),
         new BoundItemIndicator(ReallyWorldItemCooldowns.TrackedItem.ENDER_TRAP.С_50cb85c5(), this.Щ_bf56bd03.getValue(), ClientResourceIds.getImageId(В9х)),
         new BoundItemIndicator(ReallyWorldItemCooldowns.TrackedItem.STONE_TRAP.С_50cb85c5(), this.у.getValue()),
         new BoundItemIndicator(ReallyWorldItemCooldowns.TrackedItem.HELL_TRAP.С_50cb85c5(), this.field_2.getValue()),
         new BoundItemIndicator(ReallyWorldItemCooldowns.TrackedItem.ANTI_FLIGHT.С_50cb85c5(), this.ф.getValue()),
         new BoundItemIndicator(ReallyWorldItemCooldowns.TrackedItem.DARK_PULSE.С_50cb85c5(), this.и.getValue())
      );
   }

   private void ж(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, Vector4f var6) {
      var1.addVertex(var2, var3, var4, var5).setUv(0.0F, 0.0F).setColor(var6.x, var6.y, var6.z, var6.w);
   }

   private static long Б(float var0, float var1, float var2, float var3, float var4, float var5) {
      int var6 = Math.round(var0 * 2.0F);
      int var7 = Math.round(var1 * 2.0F);
      int var8 = Math.round(var2 * 2.0F);
      int var9 = Math.round(var3 * 2.0F);
      int var10 = Math.round(var4 * 2.0F);
      int var11 = Math.round(var5 * 2.0F);
      long var12 = Б(var6, var7, var8);
      long var14 = Б(var9, var10, var11);
      return var12 <= var14 ? var12 << 32 | var14 & ВOЮ : var14 << 32 | var12 & ВOЧ;
   }

   private void шР() {
      if (MINECRAFT_CLIENT.options != null) {
         this.Х0 = true;
         MINECRAFT_CLIENT.options.keyShift.setDown(true);
      }
   }

   private enum TrapUseStage {
      IDLE,
      USE_TRAP,
      RESTORE_TRAP,
      SWAP_OFFHAND,
      SNEAK,
      RESTORE_OFFHAND;
   }

   private static final class TrapMarker {
      private final UUID Х;
      private final long ц;
      private long я;
      private long сс;
      private double 西;
      private double д;
      private double Б;
      private int со;
      private static final long Выз = -1L;
      private static final long Выц = -1L;
      private static final long Выя = 380L;
      private static final float Вйс = 420.0F;
      private static final float ВйВ = 380.0F;
      private static final float ВйЯ = 6.0F;
      private static final float ВйХ = 15.0F;
      private static final float Вйе = 10.0F;
      private static final float ВйА = 6.0F;
      private static final float Вй4 = 15.0F;
      private static final float Вйп = 10.0F;
      private static final float ВйД = (float) Math.PI;
      private static final float ВйЪ = 0.035F;
      private static final float Вйт = 0.72F;
      private static final float Вйа = 0.28F;
      private static final float ВйЮ = 0.08F;

      private float Я() {
         return this.сс < 0L ? 0.0F : Mth.clamp((float)(System.currentTimeMillis() - this.сс) / ВйВ, 0.0F, 1.0F);
      }

      private boolean Шл() {
         return this.сс >= 0L && System.currentTimeMillis() - this.сс >= Выя;
      }

      private float method__() {
         float var1 = this.Ю();
         float var2 = 1.0F - var1;
         float var3 = 1.0F - var2 * var2 * var2;
         float var4 = Mth.sin(var1 * ВйД) * ВйЪ;
         return Вйт + var3 * Вйа + var4 + this.Я() * ВйЮ;
      }

      private TrapMarker(UUID var1) {
         this.сс = Выз;
         this.Х = var1;
         long var2 = System.currentTimeMillis();
         this.ц = var2;
         this.я = var2;
      }

      private boolean ШГ() {
         return this.сс >= 0L;
      }

      private void ыь() {
         if (this.сс < 0L) {
            this.сс = System.currentTimeMillis();
         }
      }

      private void ын() {
         this.я = System.currentTimeMillis();
         this.сс = Выц;
      }

      private float ч() {
         float var1 = this.Ю();
         float var2 = 1.0F - var1;
         return 1.0F - var2 * var2 * var2;
      }

      private float Ю() {
         return Mth.clamp((float)(System.currentTimeMillis() - this.ц) / Вйс, 0.0F, 1.0F);
      }

      private void я(double var1, double var3, double var5, int var7) {
         this.西 = var1;
         this.д = var3;
         this.Б = var5;
         this.со = var7;
      }

      private float method_2() {
         float var1 = this.Ю();
         var1 = var1 * var1 * var1 * (var1 * (var1 * ВйЯ - ВйХ) + Вйе);
         float var2 = this.Я();
         var2 = var2 * var2 * var2 * (var2 * (var2 * ВйА - Вй4) + Вйп);
         return var1 * (1.0F - var2);
      }
   }

   private record FaceOffset(int ox, int oy, int oz, int axis, boolean positive) {
   }

   private record LineSegment(float x1, float y1, float z1, float x2, float y2, float z2) {
   }
}
