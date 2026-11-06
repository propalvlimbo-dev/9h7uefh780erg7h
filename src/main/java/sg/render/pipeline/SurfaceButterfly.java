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
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.events.impl.WorldEffectsEvent;
import sg.modules.impl.render.Rain;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;

public final class SurfaceButterfly implements MinecraftAccess {
   private static final String Ви = "surface_butterfly";
   private static final int field_4е = 72;
   private static final int field_4А = 180;
   private static final int field_44 = 6;
   private static final Direction[] Х_14381db8 = Direction.values();
   private final RenderPipeline сй;
   private final List<SurfaceButterfly.SurfacePatch> сЦ = new ArrayList<>();
   private final Random п = new Random();
   private final MutableBlockPos Я = new MutableBlockPos();
   private final FrustumIntersection В = new FrustumIntersection();
   private final Matrix4f Ы = new Matrix4f();
   @SerializedName("Х")
   private final Vector3f Х_f5cce25e = new Vector3f();
   private final Vector3f е = new Vector3f();
   private ClientLevel 弟;
   private Vec3 К;
   private GpuBuffer Г;
   @SerializedName("4п")
   private int field_4п;
   @SerializedName("4Д")
   private int field_4Д;
   @SerializedName("4Ъ")
   private int field_4Ъ;
   private static final String Х衣б = "rose/surface_butterfly";
   private static final String Х衣Ы = "surface_particle";
   private static final String Х衣l = "surface_butterfly";
   private static final float Х衣8 = 4.0F;
   private static final float Х衣5 = 72.0F;
   private static final float Х衣в = 64.0F;
   private static final double Х衣ъ = 2.0;
   private static final double Х衣М = 2.0;
   private static final double Х衣Н = 2.0;
   private static final double Х衣н = 0.12;
   private static final double Х衣Ц = 0.76;
   private static final double Х衣ч = 0.12;
   private static final double Х衣Э = 0.76;
   private static final double Х衣必 = 0.12;
   private static final double Х衣щ = 0.76;
   private static final double Х衣Г = 0.025;
   private static final double Х衣л = 0.35;
   private static final double Х衣э = Math.PI;
   private static final double Х衣ю = 2.0;
   private static final float Х衣Щ = 0.13F;
   private static final float Х衣у = 0.1F;
   private static final float Х衣2 = 180.0F;
   private static final float Х衣ф = 0.75F;
   private static final float Х衣и = 0.5F;
   private static final float Х衣9 = (float) (Math.PI * 2);
   private static final double Х衣о = 0.17;
   private static final double Х衣O = 0.5;
   private static final double Х衣7 = Math.PI;
   private static final double Х衣Р = 0.12;
   private static final double Х衣3 = 0.075;
   private static final double Х衣р = 0.55;
   private static final double Х衣6 = 0.06;
   private static final float Х衣_ = 14.0F;
   private static final float Х衣Ш = 22.0F;
   private static final float Х衣ы = 12.0F;
   private static final double Х衣й = 2.0;
   private static final double Х衣Ж = 0.2;
   private static final double Х衣м = 0.6;
   private static final float Х衣К = 0.005F;
   private static final float Х衣I = 3.0F;
   private static final float Х衣Т = 0.5F;
   private static final float Х衣О = 0.5F;
   private static final float Х衣Ф = 255.0F;
   private static final float Х衣Й = 255.0F;
   private static final float Х衣к = 255.0F;
   private static final float Х衣г = 0.25F;
   private static final float Х衣0 = 0.7F;
   private static final float Х衣х = 0.5F;
   private static final float Х衣У = 0.5F;
   private static final float Х衣З = 0.6F;
   private static final double Х衣Е = 0.82;
   private static final float Х衣П = 0.5F;
   private static final float Х衣西 = 0.5F;
   private static final float Х衣д = 0.22F;
   private static final float Х衣Б = 0.28F;
   private static final float Х衣С = 0.72F;
   private static final double Х衣з = 1.9;
   private static final double Х衣ц = 1.9;
   private static final float Х衣я = 4.0F;
   private static final float Хшс = 5.0F;
   private static final float ХшВ = 0.2F;
   private static final float ХшЯ = 0.18F;
   private static final double ХшХ = 0.004;
   private static final double Хше = 0.085;
   private static final double ХшА = 0.65;
   private static final float Хш4 = 3.0F;
   private static final float Хшп = 0.01F;
   private static final float ХшД = 0.18F;
   private static final float ХшЪ = 1.7F;
   private static final float Хшт = 6.0F;
   private static final float Хша = -1.25F;
   private static final float ХшЮ = 2.5F;
   private static final float ХшЧ = (float) Math.PI;
   private static final float ХшЛ = 2.17F;
   private static final float Хш诶 = 1.43F;
   private static final float Хш1 = 0.7F;
   private static final double ХшЬ = 0.3;
   private static final double Хш衣 = 0.18;
   private static final double Хшш = 1.12;
   private static final double ХшИ = 0.12;
   private static final double Хшь = 0.2;
   private static final double Хшж = 0.16;
   private static final float Хш弟 = 1.9F;
   private static final float Хшб = 0.12F;
   private static final float ХшЫ = 0.88F;
   private static final double Хшl = 0.12;
   private static final double Хш8 = 0.45;
   private static final double Хш5 = 0.24;
   private static final double Хшв = 0.45;
   private static final double Хшъ = 0.24;
   private static final double ХшМ = 0.45;
   private static final double ХшН = 0.24;
   private static final double Хшн = 0.22;
   private static final double ХшЦ = 0.4;
   private static final double Хшч = 0.22;
   private static final double ХшЭ = 0.4;
   private static final double Хш必 = 0.22;
   private static final double Хшщ = 0.4;
   private static final double ХшГ = 0.09;
   private static final float Хшл = 0.7F;
   private static final float Хшэ = 0.4F;
   private static final double Хшю = 0.07;
   private static final float ХшЩ = 0.4F;
   private static final double Хшу = 1.0E-12;
   private static final float Хш2 = 6.0F;
   private static final float Хшф = 6.0F;
   private static final float Хши = 7.0F;
   private static final float Хш9 = 7.0F;
   private static final String Хшо = "surface_butterfly";
   private static final String ХшO = "surface_butterfly";

   private void ъ(
      BufferBuilder var1,
      Matrix4f var2,
      double var3,
      double var5,
      double var7,
      double var9,
      double var11,
      double var13,
      double var15,
      float var17,
      float var18,
      float var19,
      float var20,
      float var21
   ) {
      double var22 = var9 - var3;
      double var24 = var11 - var5;
      double var26 = var13 - var7;
      double var28 = var24 * var7 - var26 * var5;
      double var30 = var26 * var3 - var22 * var7;
      double var32 = var22 * var5 - var24 * var3;
      double var34 = var28 * var28 + var30 * var30 + var32 * var32;
      if (!(var34 < Хшу)) {
         double var36 = var15 / Math.sqrt(var34);
         var28 *= var36;
         var30 *= var36;
         var32 *= var36;
         this.ъ(var1, var2, var3 - var28, var5 - var30, var7 - var32, Хш2, 0.0F, var17, var18, var19, var20);
         this.ъ(var1, var2, var3 + var28, var5 + var30, var7 + var32, Хшф, 1.0F, var17, var18, var19, var20);
         this.ъ(var1, var2, var9 + var28, var11 + var30, var13 + var32, Хши, 1.0F, var17, var18, var19, var21);
         this.ъ(var1, var2, var9 - var28, var11 - var30, var13 - var32, Хш9, 0.0F, var17, var18, var19, var21);
      }
   }

   private void ъ(BufferBuilder var1, Matrix4f var2, Vec3 var3, Vec3 var4, Vec3 var5, float var6, float var7, float var8, float var9, float var10, float var11) {
      this.ъ(var1, var2, var3.subtract(var4).subtract(var5), var6, 0.0F, var8, var9, var10, var11);
      this.ъ(var1, var2, var3.subtract(var4).add(var5), var6, 1.0F, var8, var9, var10, var11);
      this.ъ(var1, var2, var3.add(var4).add(var5), var7, 1.0F, var8, var9, var10, var11);
      this.ъ(var1, var2, var3.add(var4).subtract(var5), var7, 0.0F, var8, var9, var10, var11);
   }

   private void ъ(BufferBuilder var1, Matrix4f var2, Vec3 var3, Vec3 var4, Vec3 var5, float var6, float var7, float var8, float var9) {
      this.ъ(var1, var2, var3.subtract(var5), 0.0F, 0.0F, var6, var7, var8, var9);
      this.ъ(var1, var2, var3.add(var5), 0.0F, 1.0F, var6, var7, var8, var9);
      this.ъ(var1, var2, var3.add(var4).add(var5), 1.0F, 1.0F, var6, var7, var8, var9);
      this.ъ(var1, var2, var3.add(var4).subtract(var5), 1.0F, 0.0F, var6, var7, var8, var9);
   }

   public void ыТ() {
      this.сЦ.clear();
      this.弟 = null;
      this.К = null;
      this.field_4Д = 0;
      if (this.Г != null) {
         this.Г.close();
         this.Г = null;
      }

      this.field_4п = 0;
   }

   public void С(float var1, float var2) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         Vec3 var3 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         if (this.弟 != MINECRAFT_CLIENT.level || this.К != null && this.К.distanceToSqr(var3) > var1 * var1 * Х衣8) {
            this.ыТ();
            this.弟 = MINECRAFT_CLIENT.level;
         }

         this.К = var3;
         this.field_4Д++;
         int var4 = Math.round(Х衣5 * var2);
         Iterator var5 = this.сЦ.iterator();

         while (var5.hasNext()) {
            SurfaceButterfly.SurfacePatch var6 = (SurfaceButterfly.SurfacePatch)var5.next();
            if (this.field_4Д - var6.ВЙ < var6.Вк && (var6.Вг < 0 || this.field_4Д - var6.Вг < 12)) {
               if (var6.Вг < 0
                  && (
                     var6.ь.distanceToSqr(var3) > (var1 + 2.0F) * (var1 + 2.0F)
                        || !this.弟.getBlockState(var6.е).equals(var6.Я)
                        || this.о(this.ъ(var6, this.field_4Д))
                  )) {
                  var6.Вг = this.field_4Д;
               }
            } else {
               var5.remove();
            }
         }

         if (this.сЦ.size() < var4) {
            int var8 = Math.round(Х衣в * var2);
            int var7 = 0;

            while (var8-- > 0 && var7 < 5 && this.сЦ.size() < var4) {
               if (this.ъ(var3, var1)) {
                  var7++;
               }
            }
         }
      } else {
         this.ыТ();
      }
   }

   private boolean о(Vec3 var1) {
      BlockPos var2 = BlockPos.containing(var1);
      if (!this.弟.hasChunkAt(var2)) {
         return true;
      }

      VoxelShape var3 = this.弟.getBlockState(var2).getShape(this.弟, var2);
      if (var3.isEmpty()) {
         return false;
      }

      double var4 = var1.x - var2.getX();
      double var6 = var1.y - var2.getY();
      double var8 = var1.z - var2.getZ();

      for (AABB var11 : var3.toAabbs()) {
         if (var4 > var11.minX && var4 < var11.maxX && var6 > var11.minY && var6 < var11.maxY && var8 > var11.minZ && var8 < var11.maxZ) {
            return true;
         }
      }

      return false;
   }

   public void ъ(WorldEffectsEvent var1, float var2) {
      if (MINECRAFT_CLIENT.level != null && this.弟 == MINECRAFT_CLIENT.level && MINECRAFT_CLIENT.player != null && !this.сЦ.isEmpty() && !Rain.Пз()) {
         float var3 = this.field_4Д + var1.getDeltaTracker().getGameTimeDeltaPartialTick(false);
         Vec3 var4 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         int var5 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
         int var6 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO);
         Matrix4f var7 = var1.getPoses().last().pose();
         this.В.set(this.Ы.set(var1.getProjectionMatrix()).mul(var7));
         this.Х_f5cce25e.set(1.0F, 0.0F, 0.0F).rotate(MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation());
         this.е.set(0.0F, 1.0F, 0.0F).rotate(MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation());
         Vec3 var8 = new Vec3(this.Х_f5cce25e.x, this.Х_f5cce25e.y, this.Х_f5cce25e.z);
         Vec3 var9 = new Vec3(this.е.x, this.е.y, this.е.z);
         BufferBuilder var10 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

         for (SurfaceButterfly.SurfacePatch var12 : this.сЦ) {
            float var13 = var3 - var12.ВЙ;
            float var14 = Math.clamp(var13 / Х衣_, 0.0F, 1.0F) * Math.clamp((var12.Вк - var13) / Х衣Ш, 0.0F, 1.0F);
            if (var12.Вг >= 0) {
               var14 *= Math.clamp(1.0F - (var3 - var12.Вг) / Х衣ы, 0.0F, 1.0F);
            }

            Vec3 var15 = this.ъ(var12, var3).subtract(var4);
            double var16 = var15.length();
            var14 *= Math.clamp((float)((var2 + 1.0F - var16) / Х衣й), 0.0F, 1.0F);
            var14 *= Math.clamp((float)((var16 - Х衣Ж) / Х衣м), 0.0F, 1.0F);
            if (!(var14 <= Х衣К) && this.В.testSphere((float)var15.x, (float)var15.y, (float)var15.z, var12.Ям * Х衣I)) {
               int var18 = ColorMath.lerpArgb(var5, var6, Х衣Т + Х衣О * Mth.sin(var12.ЯК));
               float var19 = ColorMath.getRed(var18) / Х衣Ф;
               float var20 = ColorMath.getGreen(var18) / Х衣Й;
               float var21 = ColorMath.getBlue(var18) / Х衣к;
               float var22 = Х衣г + Х衣0 * (Х衣х + Х衣У * Mth.sin(var13 * Х衣З + var12.ЯК));
               Vec3 var23 = var12.弟.scale(Math.cos(var22) * var12.Ям);
               Vec3 var24 = var12.ж.scale(Math.sin(var22) * var12.Ям);
               Vec3 var25 = var12.б.scale(var12.Ям * Х衣Е);
               float var26 = Х衣П + Х衣西 * Mth.sin(var13 * Х衣д + var12.ЯК * 2.0F);
               float var27 = var26 * var26 * var26;
               float var28 = var14 * (Х衣Б + Х衣С * var27);
               this.ъ(var10, var7, var15, var8.scale(var12.Ям * Х衣з), var9.scale(var12.Ям * Х衣ц), Х衣я, Хшс, var19, var20, var21, var14 * (ХшВ + ХшЯ * var27));
               this.ъ(var10, var7, var15, var23.add(var24), var25, var19, var20, var21, var14);
               this.ъ(var10, var7, var15, var23.reverse().add(var24), var25, var19, var20, var21, var14);
               this.ъ(
                  var10,
                  var7,
                  var15.add(var12.ж.scale(ХшХ)),
                  var12.弟.scale(var12.Ям * Хше),
                  var12.б.scale(var12.Ям * ХшА),
                  2.0F,
                  Хш4,
                  var19,
                  var20,
                  var21,
                  var14
               );
               if (var28 > Хшп) {
                  this.ъ(var10, var7, var15, var12, var13, 1, var19, var20, var21, var28);
                  this.ъ(var10, var7, var15, var12, var13, -1, var19, var20, var21, var28);
               }
            }
         }

         this.l(var10);
      }
   }

   private void ъ(
      BufferBuilder var1,
      Matrix4f var2,
      Vec3 var3,
      SurfaceButterfly.SurfacePatch var4,
      float var5,
      int var6,
      float var7,
      float var8,
      float var9,
      float var10
   ) {
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      float var17 = 0.0F;
      float var18 = var5 * ХшД + var4.ЯК + var6 * ХшЪ;

      for (int var19 = 0; var19 <= 6; var19++) {
         float var20 = var19 / Хшт;
         float var21 = Хша + var20 * ХшЮ;
         float var22 = Mth.sin(var20 * ХшЧ);
         float var23 = Mth.sin(var19 * ХшЛ + var18) * Mth.sin(var19 * Хш诶 - var18 * Хш1);
         double var24 = var6 * var4.Ям * (ХшЬ + Mth.cos(var21) + var23 * Хш衣 * var22);
         double var26 = var4.Ям * (Mth.sin(var21) * Хшш + var23 * ХшИ * var22);
         double var28 = var4.Ям * (Хшь + Хшж * Mth.sin(var18 + var19 * Хш弟));
         double var30 = var3.x + var4.弟.x * var24 + var4.б.x * var26 + var4.ж.x * var28;
         double var32 = var3.y + var4.弟.y * var24 + var4.б.y * var26 + var4.ж.y * var28;
         double var34 = var3.z + var4.弟.z * var24 + var4.б.z * var26 + var4.ж.z * var28;
         float var36 = var10 * (Хшб + ХшЫ * var22);
         if (var19 > 0) {
            this.ъ(var1, var2, var11, var13, var15, var30, var32, var34, var4.Ям * Хшl, var7, var8, var9, var17, var36);
         }

         if (var19 == 3) {
            double var37 = var30 + (var4.弟.x * var6 * Хш8 + var4.б.x * Хш5) * var4.Ям;
            double var39 = var32 + (var4.弟.y * var6 * Хшв + var4.б.y * Хшъ) * var4.Ям;
            double var41 = var34 + (var4.弟.z * var6 * ХшМ + var4.б.z * ХшН) * var4.Ям;
            double var43 = var37 + (var4.弟.x * var6 * Хшн + var4.б.x * ХшЦ) * var4.Ям;
            double var45 = var39 + (var4.弟.y * var6 * Хшч + var4.б.y * ХшЭ) * var4.Ям;
            double var47 = var41 + (var4.弟.z * var6 * Хш必 + var4.б.z * Хшщ) * var4.Ям;
            this.ъ(var1, var2, var30, var32, var34, var37, var39, var41, var4.Ям * ХшГ, var7, var8, var9, var10 * Хшл, var10 * Хшэ);
            this.ъ(var1, var2, var37, var39, var41, var43, var45, var47, var4.Ям * Хшю, var7, var8, var9, var10 * ХшЩ, 0.0F);
         }

         var11 = var30;
         var13 = var32;
         var15 = var34;
         var17 = var36;
      }
   }

   private boolean ъ(Vec3 var1, float var2) {
      int var3 = Mth.floor(var1.x + (this.п.nextDouble() * Х衣ъ - 1.0) * var2);
      int var4 = Mth.floor(var1.y + (this.п.nextDouble() * Х衣М - 1.0) * var2);
      int var5 = Mth.floor(var1.z + (this.п.nextDouble() * Х衣Н - 1.0) * var2);
      this.Я.set(var3, var4, var5);
      if (!this.弟.hasChunkAt(this.Я)) {
         return false;
      }

      BlockState var6 = this.弟.getBlockState(this.Я);
      if (var6.isAir()) {
         return false;
      }

      VoxelShape var7 = var6.getShape(this.弟, this.Я);
      if (var7.isEmpty()) {
         return false;
      }

      List var8 = var7.toAabbs();
      AABB var9 = (AABB)var8.get(this.п.nextInt(var8.size()));
      Direction var10 = Х_14381db8[this.field_4Ъ++ % Х_14381db8.length];
      Vec3 var11 = new Vec3(var10.getStepX(), var10.getStepY(), var10.getStepZ());
      double var12 = Mth.lerp(Х衣н + this.п.nextDouble() * Х衣Ц, var9.minX, var9.maxX);
      double var14 = Mth.lerp(Х衣ч + this.п.nextDouble() * Х衣Э, var9.minY, var9.maxY);
      double var16 = Mth.lerp(Х衣必 + this.п.nextDouble() * Х衣щ, var9.minZ, var9.maxZ);
      switch (var10) {
         case DOWN:
            var14 = var9.minY;
            break;
         case UP:
            var14 = var9.maxY;
            break;
         case NORTH:
            var16 = var9.minZ;
            break;
         case SOUTH:
            var16 = var9.maxZ;
            break;
         case WEST:
            var12 = var9.minX;
            break;
         case EAST:
            var12 = var9.maxX;
      }

      Vec3 var18 = new Vec3(var3 + var12, var4 + var14, var5 + var16);
      if (!(var18.distanceToSqr(var1) > var2 * var2) && !this.о(var18.add(var11.scale(Х衣Г))) && !this.о(var18.add(var11.scale(Х衣л)))) {
         Vec3 var19 = var10.getAxis() == Axis.Y ? new Vec3(1.0, 0.0, 0.0) : new Vec3(0.0, 1.0, 0.0);
         Vec3 var20 = var11.cross(var19);
         double var21 = this.п.nextDouble() * Х衣э * Х衣ю;
         Vec3 var23 = var19.scale(Math.cos(var21)).add(var20.scale(Math.sin(var21)));
         Vec3 var24 = var11.cross(var23);
         float var25 = Х衣Щ + this.п.nextFloat() * Х衣у;
         this.сЦ
            .add(
               new SurfaceButterfly.SurfacePatch(
                  this.Я.immutable(),
                  var6,
                  var18,
                  var11,
                  var23,
                  var24,
                  this.field_4Д,
                  Math.round(Х衣2 * (Х衣ф + this.п.nextFloat() * Х衣и)),
                  var25,
                  this.п.nextFloat() * Х衣9
               )
            );
         return true;
      } else {
         return false;
      }
   }

   private Vec3 ъ(SurfaceButterfly.SurfacePatch var1, float var2) {
      float var3 = Math.max(0.0F, var2 - var1.ВЙ);
      float var4 = var3 / var1.Вк;
      double var5 = Х衣о + Х衣O * Math.sin(Х衣7 * Math.min(1.0F, var4));
      double var7 = Х衣Р * Math.sin(var3 * Х衣3 + var1.ЯК);
      return var1.ь.add(var1.ж.scale(var5)).add(var1.弟.scale(var7)).add(var1.б.scale(var7 * Х衣р * Math.cos(var3 * Х衣6 + var1.ЯК)));
   }

   private void l(BufferBuilder var1) {
      MeshData var2 = var1.build();
      if (var2 != null) {
         try (var2) {
            int var4 = var2.vertexBuffer().remaining();
            if (this.Г == null || this.Г.isClosed() || this.field_4п < var4) {
               if (this.Г != null && !this.Г.isClosed()) {
                  this.Г.close();
               }

               this.field_4п = 4096;

               while (this.field_4п < var4) {
                  this.field_4п *= 2;
               }

               this.Г = RenderSystem.getDevice().createBuffer(() -> ХшO, 40, this.field_4п);
            }

            CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
            var5.writeToBuffer(this.Г.slice(0L, var4), var2.vertexBuffer());
            AutoStorageIndexBuffer var6 = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
            int var7 = var2.drawState().indexCount();
            GpuBuffer var8 = var6.getBuffer(var7);
            RenderTarget var9 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();

            try (RenderPass var10 = var5.createRenderPass(
                  () -> Хшо, var9.getColorTextureView(), Optional.empty(), var9.getDepthTextureView(), OptionalDouble.empty()
               )) {
               RenderSystem.bindDefaultUniforms(var10);
               var10.setPipeline(this.сй);
               var10.setVertexBuffer(0, this.Г.slice());
               var10.setIndexBuffer(var8, var6.type());
               var10.drawIndexed(var7, 1, 0, 0, 0);
            }
         }
      }
   }

   private void ъ(
      BufferBuilder var1, Matrix4f var2, double var3, double var5, double var7, float var9, float var10, float var11, float var12, float var13, float var14
   ) {
      var1.addVertex(var2, (float)var3, (float)var5, (float)var7).setUv(var9, var10).setColor(var11, var12, var13, var14);
   }

   public SurfaceButterfly() {
      this.сй = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(Х衣б)
         .withVertexShader(ClientResourceIds.getCoreId(Х衣Ы))
         .withFragmentShader(ClientResourceIds.getCoreId(Х衣l))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.LIGHTNING)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
   }

   private void ъ(BufferBuilder var1, Matrix4f var2, Vec3 var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      this.ъ(var1, var2, var3.x, var3.y, var3.z, var4, var5, var6, var7, var8, var9);
   }

   private static final class SurfacePatch {
      final BlockPos е;
      final BlockState Я;
      final Vec3 ь;
      final Vec3 ж;
      final Vec3 弟;
      final Vec3 б;
      final int ВЙ;
      final int Вк;
      final float Ям;
      final float ЯК;
      int Вг = -1;

      SurfacePatch(BlockPos var1, BlockState var2, Vec3 var3, Vec3 var4, Vec3 var5, Vec3 var6, int var7, int var8, float var9, float var10) {
         this.е = var1;
         this.Я = var2;
         this.ь = var3;
         this.ж = var4;
         this.弟 = var5;
         this.б = var6;
         this.ВЙ = var7;
         this.Вк = var8;
         this.Ям = var9;
         this.ЯК = var10;
      }
   }
}
