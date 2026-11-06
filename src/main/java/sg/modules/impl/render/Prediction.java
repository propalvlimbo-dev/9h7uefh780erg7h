package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import settings.render.ClientResourceIds;
import sg.events.EventListener;
import sg.hud.HudScaleCoordinates;
import sg.ec.ClientFonts;
import sg.events.impl.WorldEffectsEvent;
import sg.render.WorldProjection;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.render.pipeline.UiBatchDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.HudRenderEvent;

public class Prediction extends ClientModule {
   private static final float ХС = (float) (Math.PI / 180.0);
   private static final int ХЧ = 44;
   private static final double сЙ = 0.1;
   private static final int ХЛ = 12;
   private static final int Х诶 = 400;
   private static final double ск = 128.0;
   private static final float Хз = 10.0F;
   private static final float[] Ъ_06b4556e = new float[]{Prediction.Яи衣, 0.0F, Prediction.Яиш};
   private static final int Х1 = ColorMath.packArgb(255, 0, 0, Prediction.ЯиИ);
   private static final Prediction.ProjectileSpec В = new Prediction.ProjectileSpec(
      Prediction.Яиь, Prediction.PhysicsKind.THROWN, Prediction.Яиж, Prediction.Яи弟, Prediction.Яиб, 0.0F, false
   );
   private static final Prediction.ProjectileSpec Я_f02fbd40_at_d02e9b55 = new Prediction.ProjectileSpec(
      Prediction.ЯиЫ, Prediction.PhysicsKind.THROWN, Prediction.Яиl, Prediction.Яи8, Prediction.Яи5, 0.0F, false
   );
   private static final Prediction.ProjectileSpec Х_at_d02e9b55 = new Prediction.ProjectileSpec(
      Prediction.Яив, Prediction.PhysicsKind.THROWN, Prediction.Яиъ, Prediction.ЯиМ, Prediction.ЯиН, Prediction.Яин, false
   );
   private static final Prediction.ProjectileSpec е_at_d02e9b55 = new Prediction.ProjectileSpec(
      Prediction.ЯиЦ, Prediction.PhysicsKind.THROWN, Prediction.Яич, Prediction.ЯиЭ, Prediction.Яи必, Prediction.Яищ, false
   );
   private static final Prediction.ProjectileSpec А_at_d02e9b55 = new Prediction.ProjectileSpec(
      Prediction.ЯиГ, Prediction.PhysicsKind.STRAIGHT, 0.0, 1.0, Prediction.Яил, 0.0F, false
   );
   private static final Prediction.ProjectileSpec field_4 = new Prediction.ProjectileSpec(
      Prediction.Яиэ, Prediction.PhysicsKind.ARROW, Prediction.Яию, Prediction.ЯиЩ, Prediction.Яиу, 0.0F, false
   );
   private static final Prediction.ProjectileSpec п_at_d02e9b55 = new Prediction.ProjectileSpec(
      Prediction.Яи2, Prediction.PhysicsKind.ARROW, Prediction.Яиф, Prediction.Яии, Prediction.Яи9, 0.0F, false
   );
   private static final Prediction.ProjectileSpec Д = new Prediction.ProjectileSpec(
      Prediction.Яио, Prediction.PhysicsKind.ARROW, Prediction.ЯиO, Prediction.Яи7, 0.0F, 0.0F, false
   );
   private static final Prediction.ProjectileSpec Ъ_f02fbd40 = new Prediction.ProjectileSpec(
      Prediction.ЯиР, Prediction.PhysicsKind.BOBBER, Prediction.Яи3, Prediction.Яир, 0.0F, 0.0F, true
   );
   private static final float Хц = 0.5F;
   private final RenderPipeline с1;
   private final RenderPipeline сЬ;
   private final RenderPipeline с衣;
   private final EventListener<WorldEffectsEvent> сO;
   private final EventListener<HudRenderEvent> с7;
   @SerializedName("х")
   private final List<Prediction.Trajectory> х_2571477d;
   private final List<Prediction.Trajectory> У;
   private long сз;
   private ClientLevel Ч;
   @SerializedName("х")
   private final Map<Integer, Prediction.IndicatorState> х_ea411899;
   private static final Identifier у = Identifier.fromNamespaceAndPath(Prediction.Яи6, Prediction.Яи_);
   private static final String ср = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAAIVBMVEUAAACM9OIszbE0mYglhHQbe2sQXlELTUIMNzAGOTEDJiC5a2thAAAAAXRSTlMAQObYZgAAAFtJREFUeNpjwAo4OqB0swmE1WyS5rIAJGAiVJ7mBWIYas4szwLJOM1aNb0MKLc4HchwAzKWgBhmQAaXe+XMIisGIMOtvFwRpJhhabozUAYktCxtFZACsVYh2wwA2BYa2KT1S0MAAAAASUVORK5CYII=";
   @SerializedName("Я")
   private DynamicTexture Я_be9bcd64_at_d02e9b55;
   private static final String Я2Ц = "Prediction";
   private static final String Я2ч = "Траектория метательных предметов";
   private static final String Я2Э = "rose/prediction_trail";
   private static final String Я2必 = "rose/prediction_box";
   private static final String Я2щ = "rose/prediction_potions";
   private static final long Я2Г = Long.MIN_VALUE;
   private static final String Я2л = "prediction_trail";
   private static final String Я2э = "prediction_trail";
   private static final String Я2ю = "prediction_trail";
   private static final String Я2Щ = "prediction_box";
   private static final long Я2у = Long.MIN_VALUE;
   private static final float Я22 = (float) (Math.PI / 180.0);
   private static final float Я2ф = (float) (Math.PI / 180.0);
   private static final float Я2и = (float) (Math.PI / 180.0);
   private static final float Я29 = (float) (Math.PI / 180.0);
   private static final float Я2о = (float) (Math.PI / 180.0);
   private static final double Я2O = 0.1;
   private static final float Я27 = (float) (Math.PI / 180.0);
   private static final float Я2Р = (float) Math.PI;
   private static final float Я23 = (float) (Math.PI / 180.0);
   private static final float Я2р = (float) Math.PI;
   private static final float Я26 = (float) (Math.PI / 180.0);
   private static final float Я2_ = (float) (Math.PI / 180.0);
   private static final float Я2Ш = -5.0F;
   private static final float Я2ы = 5.0F;
   private static final double Я2й = 1.0E-6;
   private static final double Я2Ж = 0.3;
   private static final double Я2м = 0.3;
   private static final double Я2К = 0.6;
   private static final double Я2I = 0.5;
   private static final float Я2Т = 0.1F;
   private static final double Я2О = 0.05;
   private static final double Я2Ф = 0.99;
   private static final float Я2Й = 3.0F;
   private static final double Я2к = 1.0E-4;
   private static final double Я2г = 16384.0;
   private static final double Я20 = 0.8;
   private static final double Я2х = Double.MAX_VALUE;
   private static final double Я2У = 0.3;
   private static final double Я2З = Double.MAX_VALUE;
   private static final double Я2Е = Double.MAX_VALUE;
   private static final double Я2П = Double.MAX_VALUE;
   private static final double Я2西 = -Double.MAX_VALUE;
   private static final double Я2д = -Double.MAX_VALUE;
   private static final double Я2Б = -Double.MAX_VALUE;
   private static final double Я2С = 2.0;
   private static final long Я2з = 600000L;
   private static final float Я2ц = 1000.0F;
   private static final double Я2я = 1.0E-8;
   private static final float Яфс = 0.25F;
   private static final double ЯфВ = 0.025;
   private static final float ЯфЯ = 0.5F;
   private static final float ЯфХ = 0.5F;
   private static final double Яфе = 0.035;
   private static final double ЯфА = 0.035;
   private static final float Яф4 = 3.0F;
   private static final float Яфп = 4.0F;
   private static final String ЯфД = "instant_damage";
   private static final String ЯфЪ = "poison";
   private static final String Яфт = "slowness";
   private static final String Яфа = "weakness";
   private static final String ЯфЮ = "invisibility";
   private static final String ЯфЧ = "instant_health";
   private static final String ЯфЛ = "regeneration";
   private static final String Яф诶 = "fire_resistance";
   private static final String Яф1 = "strength";
   private static final String ЯфЬ = "water_breathing";
   private static final String Яф衣 = "night_vision";
   private static final String Яфш = "jump_boost";
   private static final String ЯфИ = "speed";
   private static final int Яфь = -44976;
   private static final int Яфж = -11468976;
   private static final int Яф弟 = -11513601;
   private static final int Яфб = -6908266;
   private static final int ЯфЫ = -16726016;
   private static final int Яфl = -38476;
   private static final int Яф8 = -16738616;
   private static final int Яф5 = -10223416;
   private static final int Яфв = -16711786;
   private static final int Яфъ = -10158081;
   private static final int ЯфМ = -16777216;
   private static final String ЯфН = ", ";
   private static final double Яфн = 0.92;
   private static final double ЯфЦ = 1.0E-8;
   private static final double Яфч = 0.02;
   private static final double ЯфЭ = 0.55F;
   private static final float Яф必 = 0.93F;
   private static final float Яфщ = 0.07F;
   private static final float ЯфГ = 4.0F;
   private static final double Яфл = 0.78;
   private static final double Яфэ = 0.004;
   private static final double Яфю = 0.58;
   private static final float ЯфЩ = 0.5F;
   private static final float Яфу = 0.9F;
   private static final double Яф2 = 0.65;
   private static final double Яфф = 1.15;
   private static final double Яфи = 0.93;
   private static final float Яф9 = 200.0F;
   private static final float Яфо = (float) (Math.PI * 2);
   private static final float ЯфO = 44.0F;
   private static final float Яф7 = (float) (Math.PI * 2);
   private static final float ЯфР = 44.0F;
   private static final float Яф3 = 44.0F;
   private static final float Яфр = 44.0F;
   private static final double Яф6 = 0.06;
   private static final double Яф_ = 1.0E-8;
   private static final double ЯфШ = 1.0E-6;
   private static final double Яфы = 0.012;
   private static final double Яфй = 0.007;
   private static final float ЯфЖ = 0.05F;
   private static final float Яфм = 0.45F;
   private static final float ЯфК = 1.1F;
   private static final float ЯфI = 0.58F;
   private static final float ЯфТ = 0.42F;
   private static final float ЯфО = 13.0F;
   private static final float ЯфФ = 6.0F;
   private static final float ЯфЙ = 255.0F;
   private static final String Яфк = "Неизвестно";
   private static final float Яфг = 0.01F;
   private static final float Яф0 = 0.01F;
   private static final float Яфх = 20.0F;
   private static final float ЯфУ = 255.0F;
   private static final String ЯфЗ = "%.1fс";
   private static final String ЯфЕ = "%.1f блоков";
   private static final String ЯфП = "%.1fс";
   private static final float Яф西 = 20.0F;
   private static final String Яфд = " · ";
   private static final float ЯфБ = 34.0F;
   private static final float ЯфС = 8.0F;
   private static final float Яфз = 26.0F;
   private static final float Яфц = 14.0F;
   private static final float Яфя = 8.0F;
   private static final float Яис = 26.0F;
   private static final float ЯиВ = 8.0F;
   private static final float ЯиЯ = 5.0F;
   private static final float ЯиХ = 16.0F;
   private static final float Яие = 16.0F;
   private static final float ЯиА = 8.0F;
   private static final float Яи4 = 5.0F;
   private static final float Яип = 16.0F;
   private static final float ЯиД = 16.0F;
   private static final float ЯиЪ = 6.0F;
   private static final float Яит = 8.0F;
   private static final float Яиа = 6.0F;
   private static final String ЯиЮ = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAAIVBMVEUAAACM9OIszbE0mYglhHQbe2sQXlELTUIMNzAGOTEDJiC5a2thAAAAAXRSTlMAQObYZgAAAFtJREFUeNpjwAo4OqB0swmE1WyS5rIAJGAiVJ7mBWIYas4szwLJOM1aNb0MKLc4HchwAzKWgBhmQAaXe+XMIisGIMOtvFwRpJhhabozUAYktCxtFZACsVYh2wwA2BYa2KT1S0MAAAAASUVORK5CYII=";
   private static final String ЯиЧ = "Cannot decode bundled vanilla pearl";
   private static final String ЯиЛ = "Prediction vanilla pearl";
   private static final String Яи诶 = "prediction";
   private static final String Яи1 = "predictionVertices";
   private static final double ЯиЬ = 0.3;
   private static final float Яи衣 = -10.0F;
   private static final float Яиш = 10.0F;
   private static final double ЯиИ = 255.0;
   private static final String Яиь = "Эндер жемчуг";
   private static final double Яиж = 0.03;
   private static final double Яи弟 = 0.99;
   private static final float Яиб = 1.5F;
   private static final String ЯиЫ = "Снежки и яйца";
   private static final double Яиl = 0.03;
   private static final double Яи8 = 0.99;
   private static final float Яи5 = 1.5F;
   private static final String Яив = "Зелья";
   private static final double Яиъ = 0.05;
   private static final double ЯиМ = 0.99;
   private static final float ЯиН = 0.5F;
   private static final float Яин = -20.0F;
   private static final String ЯиЦ = "Бутыльки опыта";
   private static final double Яич = 0.07;
   private static final double ЯиЭ = 0.99;
   private static final float Яи必 = 0.7F;
   private static final float Яищ = -20.0F;
   private static final String ЯиГ = "Ветрозаряд";
   private static final float Яил = 1.5F;
   private static final String Яиэ = "Трезубец";
   private static final double Яию = 0.05;
   private static final double ЯиЩ = 0.99;
   private static final float Яиу = 2.5F;
   private static final String Яи2 = "Арбалет";
   private static final double Яиф = 0.05;
   private static final double Яии = 0.99;
   private static final float Яи9 = 3.15F;
   private static final String Яио = "Лук";
   private static final double ЯиO = 0.05;
   private static final double Яи7 = 0.99;
   private static final String ЯиР = "Удочка";
   private static final double Яи3 = 0.03;
   private static final double Яир = 0.92;
   private static final String Яи6 = "rose";
   private static final String Яи_ = "dynamic/prediction_vanilla_pearl";

   private int I(BufferBuilder var1, Matrix4f var2, Vec3 var3, Entity var4) {
      AABB var5 = var4.getBoundingBox().inflate(Яф6);
      int var6 = Х1;
      Vec3[] var7 = new Vec3[]{
         new Vec3(var5.minX, var5.minY, var5.minZ),
         new Vec3(var5.maxX, var5.minY, var5.minZ),
         new Vec3(var5.maxX, var5.minY, var5.maxZ),
         new Vec3(var5.minX, var5.minY, var5.maxZ)
      };
      Vec3[] var8 = new Vec3[]{
         new Vec3(var5.minX, var5.maxY, var5.minZ),
         new Vec3(var5.maxX, var5.maxY, var5.minZ),
         new Vec3(var5.maxX, var5.maxY, var5.maxZ),
         new Vec3(var5.minX, var5.maxY, var5.maxZ)
      };
      int var9 = 0;

      for (int var10 = 0; var10 < 4; var10++) {
         int var11 = (var10 + 1) % 4;
         var9 += this.I(var1, var2, var3, var7[var10], var7[var11], var6);
         var9 += this.I(var1, var2, var3, var8[var10], var8[var11], var6);
         var9 += this.I(var1, var2, var3, var7[var10], var8[var10], var6);
      }

      return var9;
   }

   private int I(BufferBuilder var1, Matrix4f var2, Vec3 var3, Prediction.Trajectory var4, float var5) {
      List var6 = var4.path();
      int var7 = var6.size();
      Vec3[] var8 = new Vec3[var7];
      int[] var9 = new int[var7];

      for (int var10 = 0; var10 < var7; var10++) {
         Vec3 var11 = (Vec3)var6.get(var10);
         Vec3 var12 = ((Vec3)var6.get(Math.min(var7 - 1, var10 + 1))).subtract((Vec3)var6.get(Math.max(0, var10 - 1)));
         Vec3 var13 = var11.subtract(var3);
         Vec3 var14 = var12.cross(var13);
         double var15 = var13.length();
         var8[var10] = (var14.lengthSqr() < Я2я ? new Vec3(1.0, 0.0, 0.0) : var14.normalize())
            .scale(this.I(var15) * (var4.spec() == Х_at_d02e9b55 ? Яфс : 1.0F));
         var9[var10] = var4.spec() == Х_at_d02e9b55
            ? this.I_a83dd0cc((float)var10 / (var7 - 1))
            : this.I((float)var10 / (var7 - 1), var5, var15, var4.handheld());
      }

      int var17 = 0;

      for (int var18 = 0; var18 < var7 - 1; var18++) {
         if (ColorMath.getAlpha(var9[var18]) > 1 || ColorMath.getAlpha(var9[var18 + 1]) > 1) {
            float var19 = (float)var18 / (var7 - 1);
            float var20 = (float)(var18 + 1) / (var7 - 1);
            Vec3 var21 = ((Vec3)var6.get(var18)).subtract(var3);
            Vec3 var22 = ((Vec3)var6.get(var18 + 1)).subtract(var3);
            this.I(var1, var2, var21.add(var8[var18]), var19, 0.0F, var9[var18]);
            this.I(var1, var2, var21.subtract(var8[var18]), var19, 1.0F, var9[var18]);
            this.I(var1, var2, var22.subtract(var8[var18 + 1]), var20, 1.0F, var9[var18 + 1]);
            this.I(var1, var2, var22.add(var8[var18 + 1]), var20, 0.0F, var9[var18 + 1]);
            var17 += 4;
         }
      }

      return var17;
   }

   private void бИ() {
      for (Prediction.IndicatorState var2 : this.х_ea411899.values()) {
         var2.Аз = false;
      }

      for (Prediction.Trajectory var11 : this.х_2571477d) {
         if (var11.spec() != Х_at_d02e9b55 && !var11.handheld() && var11.source() != null && !var11.source().isRemoved() && var11.impact() != null) {
            Prediction.IndicatorState var3 = this.х_ea411899
               .computeIfAbsent(var11.source().getId(), var2x -> new Prediction.IndicatorState(this.I_a4db5d8e(var11.source())));
            var3.В = var11;
            var3.Аз = true;
            Entity var4 = ((Projectile)var11.source()).getOwner();
            var3.Хэ = var4 == null ? Яфк : var4.getName().getString();
         }
      }

      Iterator var10 = this.х_ea411899.values().iterator();

      while (var10.hasNext()) {
         Prediction.IndicatorState var12 = (Prediction.IndicatorState)var10.next();
         var12.сх.updateTarget(var12.Аз ? 1.0F : 0.0F);
         if (!var12.Аз && var12.сх.getValue() <= Яфг) {
            var10.remove();
         } else {
            Vector2f var13 = WorldProjection.projectToScreen(var12.В.impact());
            if (var13 != null && !(var12.сх.getValue() <= Яф0)) {
               float var14 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(false);
               float var5 = var12.Аз ? Math.max(0.0F, var12.В.ticks() - var14) / Яфх : 0.0F;
               UiDrawing.setGlobalAlpha(ЯфУ * var12.сх.getValue());

               try {
                  this.I(var13, var12.Хэ, String.format(Locale.ROOT, ЯфЗ, var5), var12.ж);
               } finally {
                  UiDrawing.resetGlobalAlpha();
               }
            }
         }
      }

      this.бо();
   }

   private Prediction.LaunchState п(Prediction.ProjectileSpec var1, Vec3 var2, double var3, float var5, float var6) {
      float var7 = Mth.cos(-var6 * Я27 - Я2Р);
      float var8 = Mth.sin(-var6 * Я23 - Я2р);
      float var9 = -Mth.cos(-var5 * Я26);
      float var10 = Mth.sin(-var5 * Я2_);
      Vec3 var11 = new Vec3(-var8, Mth.clamp(-(var10 / var9), Я2Ш, Я2ы), -var7);
      double var12 = var11.length();
      if (var12 < Я2й) {
         return null;
      }

      Vec3 var14 = new Vec3(var2.x - var8 * Я2Ж, var2.y + var3, var2.z - var7 * Я2м);
      return new Prediction.LaunchState(var1, var14, var11.scale(Я2К / var12 + Я2I), true, false);
   }

   public Prediction() {
      super(Я2Ц, Я2ч, ModuleCategory.RENDER);
      this.с1 = I(Я2Э, CompareOp.GREATER_THAN_OR_EQUAL);
      this.сЬ = п(Я2必, CompareOp.GREATER_THAN_OR_EQUAL);
      this.с衣 = п(Я2щ, CompareOp.ALWAYS_PASS);
      this.сO = this::Ы;
      this.с7 = this::ж;
      this.х_2571477d = new ArrayList<>();
      this.У = new ArrayList<>();
      this.сз = Я2Г;
      this.х_ea411899 = new LinkedHashMap<>();
   }

   private void I(Matrix4f var1, Vec3 var2) {
      BufferBuilder var3 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int var4 = 0;

      for (Prediction.Trajectory var6 : this.х_2571477d) {
         if (var6.spec() != Х_at_d02e9b55 && var6.victim() != null) {
            var4 += this.I(var3, var1, var2, var6.victim());
         }
      }

      this.I(var3, var4, this.сЬ);
   }

   private void х(float var1) {
      for (Prediction.LaunchState var3 : this.I_2571477d(var1)) {
         this.I_de5a6f78(this.х_2571477d, var3);
      }

      long var6 = MINECRAFT_CLIENT.level.getGameTime();
      if (var6 != this.сз) {
         this.сз = var6;
         this.б6();
      }

      for (Prediction.Trajectory var5 : this.У) {
         if (var5.source() == null || !var5.source().isRemoved()) {
            this.х_2571477d.add(var5);
         }
      }
   }

   private boolean 衣(ItemStack var1) {
      if (MINECRAFT_CLIENT.level != null && var1 != null && !var1.isEmpty()) {
         Reference var2 = MINECRAFT_CLIENT.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.MULTISHOT);
         return EnchantmentHelper.getItemEnchantmentLevel(var2, var1) > 0;
      } else {
         return false;
      }
   }

   private int I(BufferBuilder var1, Matrix4f var2, Vec3 var3, Prediction.Trajectory var4) {
      Prediction.AreaEffect var5 = this.I(var4.item());
      Vec3 var6 = var4.impact().subtract(var3).add(0.0, ЯфВ, 0.0);
      Vec3 var7 = new Vec3(1.0, 0.0, 0.0);
      Vec3 var8 = new Vec3(0.0, 0.0, 1.0);
      double var9 = var5.radius();
      int var11 = this.I(var1, var2, var6, var7, var8, 0.0, var9, ЯфЯ, ЯфХ, this.б(var5.color(), 24));
      return var11 + this.I(var1, var2, var6, var7, var8, var9 - Яфе, var9 + ЯфА, 0.0F, 1.0F, this.б(var5.color(), 200));
   }

   private void I(Matrix4f var1, Vec3 var2, float var3) {
      BufferBuilder var4 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int var5 = 0;

      for (Prediction.Trajectory var7 : this.х_2571477d) {
         if (var7.spec() == Х_at_d02e9b55) {
            var5 += this.I(var4, var1, var2, var7, var3);
            if (var7.impact() != null && this.I(var7)) {
               var5 += this.I(var4, var1, var2, var7);
            }
         }
      }

      this.I(var4, var5, this.с衣);
   }

   private static RenderPipeline I(String var0, CompareOp var1) {
      return RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(var0)
         .withVertexShader(ClientResourceIds.getCoreId(Я2л))
         .withFragmentShader(ClientResourceIds.getCoreId(Я2э))
         .withDepthTest(var1)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
   }

   private List<Prediction.LaunchState> I_2571477d(float var1) {
      LocalPlayer var2 = MINECRAFT_CLIENT.player;
      if (var2.isUsingItem()) {
         List var3 = this.I(var2.getItemInHand(var2.getUsedItemHand()), true, var1);
         if (!var3.isEmpty()) {
            return var3;
         }
      }

      List var4 = this.I(var2.getMainHandItem(), false, var1);
      return var4.isEmpty() ? this.I(var2.getOffhandItem(), false, var1) : var4;
   }

   private int I_a83dd0cc(float var1) {
      int var2 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      int var3 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO);
      return ColorMath.lerpArgb(var2, var3, Mth.clamp(var1, 0.0F, 1.0F));
   }

   private int п(BufferBuilder var1, Matrix4f var2, Vec3 var3, Prediction.Trajectory var4, float var5) {
      Vec3 var6 = var4.normal() == null ? this.п(var4.impact(), var3) : var4.normal();
      Vec3 var7 = var6.cross(Math.abs(var6.y) > Яфн ? new Vec3(1.0, 0.0, 0.0) : new Vec3(0.0, 1.0, 0.0));
      if (var7.lengthSqr() < ЯфЦ) {
         return 0;
      }

      var7 = var7.normalize();
      Vec3 var8 = var6.cross(var7).normalize();
      Vec3 var9 = var4.impact().add(var6.scale(Яфч)).subtract(var3);
      double var10 = ЯфЭ;
      int var12 = this.I_a83dd0cc(1.0F);
      int var13 = 0;
      float var14 = Яф必 + Яфщ * Mth.sin(var5 * ЯфГ);
      var13 += this.I(var1, var2, var9, var7, var8, var10 * Яфл * var14, var10 * var14, 0.0F, 1.0F, this.б(var12, 235));
      var13 += this.I(var1, var2, var9, var7, var8, Яфэ, var10 * Яфю, ЯфЩ, 0.0F, this.б(var12, 110));
      float var15 = var5 * Яфу % 1.0F;
      float var16 = 1.0F - var15;
      double var17 = var10 * (Яф2 + var15 * Яфф);
      return var13 + this.I(var1, var2, var9, var7, var8, var17 * Яфи, var17, 0.0F, 1.0F, this.б(var12, Math.round(Яф9 * var16 * var16)));
   }

   private void I(BufferBuilder var1, Matrix4f var2, Vec3 var3, float var4, float var5, int var6) {
      Vector4f var7 = ColorMath.toRgbaVector(var6);
      var1.addVertex(var2, (float)var3.x, (float)var3.y, (float)var3.z).setUv(var4, var5).setColor(var7.x, var7.y, var7.z, var7.w);
   }

   private ItemStack I_a4db5d8e(Entity var1) {
      if (var1 instanceof ThrownEnderpearl) {
         return new ItemStack(Items.ENDER_PEARL);
      } else if (var1 instanceof ThrowableItemProjectile var3) {
         return var3.getItem().copyWithCount(1);
      } else if (var1 instanceof AbstractArrow var2) {
         return var2.getPickupItemStackOrigin().copyWithCount(1);
      } else if (var1 instanceof AbstractWindCharge) {
         return new ItemStack(Items.WIND_CHARGE);
      } else {
         return var1 instanceof FishingHook ? new ItemStack(Items.FISHING_ROD) : ItemStack.EMPTY;
      }
   }

   private int I(BufferBuilder var1, Matrix4f var2, Vec3 var3, Vec3 var4, Vec3 var5, double var6, double var8, float var10, float var11, int var12) {
      if (ColorMath.getAlpha(var12) <= 1) {
         return 0;
      }

      int var13 = 0;

      for (int var14 = 0; var14 < 44; var14++) {
         float var15 = Яфо * var14 / ЯфO;
         float var16 = Яф7 * (var14 + 1) / ЯфР;
         Vec3 var17 = var4.scale(Mth.cos(var15)).add(var5.scale(Mth.sin(var15)));
         Vec3 var18 = var4.scale(Mth.cos(var16)).add(var5.scale(Mth.sin(var16)));
         float var19 = var14 / Яф3;
         float var20 = (var14 + 1) / Яфр;
         this.I(var1, var2, var3.add(var17.scale(var6)), var19, var10, var12);
         this.I(var1, var2, var3.add(var17.scale(var8)), var19, var11, var12);
         this.I(var1, var2, var3.add(var18.scale(var8)), var20, var11, var12);
         this.I(var1, var2, var3.add(var18.scale(var6)), var20, var10, var12);
         var13 += 4;
      }

      return var13;
   }

   private void бы() {
      this.х_2571477d.clear();
      this.х_ea411899.clear();
      this.Ч = MINECRAFT_CLIENT.level;
      this.У.clear();
      this.сз = Я2у;
   }

   @Override
   public void onDisable() {
      this.бы();
   }

   private void I(BufferBuilder var1, int var2, RenderPipeline var3) {
      MeshData var4 = var1.build();
      if (var4 != null) {
         if (var2 == 0) {
            var4.close();
         } else {
            GpuBuffer var5 = null;

            try {
               DrawState var6 = var4.drawState();
               int var7 = var6.indexCount();
               AutoStorageIndexBuffer var8 = RenderSystem.getSequentialBuffer(var6.primitiveTopology());
               GpuBuffer var9 = var8.getBuffer(var7);
               IndexType var10 = var8.type();
               var5 = RenderSystem.getDevice().createBuffer(() -> Яи1, 32, var4.vertexBuffer());
               RenderTarget var11 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder var12 = RenderSystem.getDevice().createCommandEncoder();

               try (RenderPass var13 = var12.createRenderPass(
                     () -> Яи诶, var11.getColorTextureView(), Optional.empty(), var11.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var13);
                  var13.setPipeline(var3);
                  var13.setVertexBuffer(0, var5.slice());
                  var13.setIndexBuffer(var9, var10);
                  var13.drawIndexed(var7, 1, 0, 0, 0);
               }
            } finally {
               if (var5 != null) {
                  var5.close();
               }

               var4.close();
            }
         }
      }
   }

   private Prediction.LaunchState I(Prediction.ProjectileSpec var1, Vec3 var2, double var3, float var5, float var6) {
      LocalPlayer var7 = MINECRAFT_CLIENT.player;
      Vec3 var8 = var7.getDeltaMovement();
      Vec3 var9 = new Vec3(-Mth.sin(var6 * Я22) * Mth.cos(var5 * Я2ф), -Mth.sin((var5 + var1.roll()) * Я2и), Mth.cos(var6 * Я29) * Mth.cos(var5 * Я2о));
      Vec3 var10 = var9.normalize().scale(var1.speed()).add(var8.x, var7.onGround() ? 0.0 : var8.y, var8.z);
      return new Prediction.LaunchState(var1, var2.add(0.0, var3 - Я2O, 0.0), var10, true, false);
   }

   private int I(float var1, float var2, double var3, boolean var5) {
      float var6 = var5 ? Mth.clamp(var1 / ЯфЖ, 0.0F, 1.0F) : 1.0F;
      float var7 = Mth.clamp(((float)var3 - Яфм) / ЯфК, 0.0F, 1.0F);
      float var8 = ЯфI + ЯфТ * Mth.sin(var1 * ЯфО - var2 * ЯфФ);
      return this.б(this.I_a83dd0cc(var1), Math.round(ЯфЙ * var6 * var7 * var8));
   }

   private void ж(HudRenderEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && this.Ч == MINECRAFT_CLIENT.level) {
         UiDrawing.setTransform(new Matrix4f());
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());

         try {
            this.бИ();
         } finally {
            UiBatchDrawing.endBatch(ClientFonts.getMediumFont());
         }
      } else {
         this.бы();
      }
   }

   private void method_0(WorldEffectsEvent var1) {
      Vec3 var2 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
      Matrix4f var3 = var1.getPoses().last().pose();
      float var4 = (float)(System.currentTimeMillis() % Я2з) / Я2ц;
      BufferBuilder var5 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int var6 = 0;

      for (Prediction.Trajectory var8 : this.х_2571477d) {
         if (var8.spec() != Х_at_d02e9b55) {
            var6 += this.I(var5, var3, var2, var8, var4);
            if (var8.impact() != null) {
               var6 += this.п(var5, var3, var2, var8, var4);
            }
         }
      }

      this.I(var5, var6, this.с1);
      this.I(var3, var2);
      this.I(var3, var2, var4);
   }

   private Prediction.EntityHit I_9d6a057c(List<Vec3> var1, Prediction.LaunchState var2) {
      Entity var3 = var2.source();
      Entity var4 = var3 instanceof Projectile var5 ? var5.getOwner() : MINECRAFT_CLIENT.player;
      List<Entity> var18 = MINECRAFT_CLIENT.level
         .getEntities(
            var3 == null ? MINECRAFT_CLIENT.player : var3,
            this.I(var1),
            var3x -> this.О(var3x) && (var3x != var4 || !var3x.getBoundingBox().inflate(ЯиЬ).contains(var2.origin()))
         );
      if (var18.isEmpty()) {
         return null;
      }

      for (int var6 = 0; var6 < var1.size() - 1; var6++) {
         Vec3 var7 = (Vec3)var1.get(var6);
         Vec3 var8 = (Vec3)var1.get(var6 + 1);
         Entity var9 = null;
         Vec3 var10 = null;
         double var11 = Я2х;

         for (Entity var14 : var18) {
            Optional var15 = var14.getBoundingBox().inflate(Я2У).clip(var7, var8);
            if (!var15.isEmpty()) {
               double var16 = var7.distanceToSqr((Vec3)var15.get());
               if (!(var16 >= var11)) {
                  var9 = var14;
                  var10 = (Vec3)var15.get();
                  var11 = var16;
               }
            }
         }

         if (var9 != null) {
            return new Prediction.EntityHit(var9, var10, var6);
         }
      }

      return null;
   }

   private AABB I(List<Vec3> var1) {
      double var2 = Я2З;
      double var4 = Я2Е;
      double var6 = Я2П;
      double var8 = Я2西;
      double var10 = Я2д;
      double var12 = Я2Б;

      for (Vec3 var15 : var1) {
         var2 = Math.min(var2, var15.x);
         var4 = Math.min(var4, var15.y);
         var6 = Math.min(var6, var15.z);
         var8 = Math.max(var8, var15.x);
         var10 = Math.max(var10, var15.y);
         var12 = Math.max(var12, var15.z);
      }

      return new AABB(var2, var4, var6, var8, var10, var12).inflate(Я2С);
   }

   private boolean I(Prediction.Trajectory var1) {
      return !var1.handheld() && var1.source() instanceof AbstractThrownPotion && !var1.source().isRemoved();
   }

   private Vec3 п(Vec3 var1, Vec3 var2) {
      Vec3 var3 = var2.subtract(var1);
      return var3.lengthSqr() < ЯфШ ? new Vec3(0.0, 1.0, 0.0) : var3.normalize();
   }

   private Prediction.AreaEffect I(ItemStack var1) {
      PotionContents var2 = var1.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
      ArrayList var3 = new ArrayList();
      String var4 = "";

      for (MobEffectInstance var6 : var2.getAllEffects()) {
         if (var3.isEmpty()) {
            var4 = BuiltInRegistries.MOB_EFFECT.getKey(var6.getEffect().value()).getPath();
         }

         var3.add(var6.getEffect().value().getDisplayName().getString() + (var6.getAmplifier() > 0 ? " " + (var6.getAmplifier() + 1) : ""));
      }

      float var9 = var1.is(Items.LINGERING_POTION) ? Яф4 : Яфп;

      int var10 = switch (var4) {
         case ЯфД -> Яфь;
         case ЯфЪ -> Яфж;
         case Яфт -> Яф弟;
         case Яфа, ЯфЮ -> Яфб;
         case ЯфЧ -> ЯфЫ;
         case ЯфЛ -> Яфl;
         case Яф诶, Яф1 -> -29696;
         case ЯфЬ -> Яф8;
         case Яф衣 -> Яф5;
         case Яфш -> Яфв;
         case ЯфИ -> Яфъ;
         default -> ЯфМ | var2.getColor();
      };
      return new Prediction.AreaEffect(var3.isEmpty() ? var1.getHoverName().getString() : String.join(ЯфН, var3), var9, var10);
   }

   private void I(Vector2f var1, String var2, String var3, ItemStack var4) {
      byte var5 = 12;
      byte var6 = 12;
      float var7 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var2, var5);
      float var8 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var3, var6);
      float var9 = ЯфБ + var7 + ЯфС + var8;
      float var10 = Яфз;
      float var11 = HudScaleCoordinates.getHudScale();
      float var12 = var1.x - var9 * var11 / 2.0F;
      float var13 = var1.y - var10 * var11 - Яфц;
      UiDrawing.pushGuiScaleTransform(var12, var13);
      UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());

      try {
         UiBatchDrawing.drawHudWindowBackground(var12, var13, var9, var10, Яфя);
         float var14 = var12 + Яис;
         if (var4.is(Items.ENDER_PEARL)) {
            UiBatchDrawing.drawTextureFlipped(
               var12 + ЯиВ, var13 + ЯиЯ, ЯиХ, Яие, new Vector4f(0.0F), new Vector4f(0.0F, 0.0F, 1.0F, 1.0F), new Vector4i(-1), this.п().getTextureView()
            );
         } else {
            UiBatchDrawing.drawItem(var12 + ЯиА, var13 + Яи4, Яип, ЯиД, new Vector4f(0.0F), new Vector4i(-1), var4);
         }

         UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var2, var14 + 2.0F, var13 + ЯиЪ, var5, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
         UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var3, var14 + var7 + Яит, var13 + Яиа, var6, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT));
      } finally {
         UiBatchDrawing.endBatch(ClientFonts.getMediumFont());
         UiDrawing.popGuiScaleTransform();
      }
   }

   private int I(BufferBuilder var1, Matrix4f var2, Vec3 var3, Vec3 var4, Vec3 var5, int var6) {
      Vec3 var7 = var4.subtract(var3);
      Vec3 var8 = var5.subtract(var3);
      Vec3 var9 = var8.subtract(var7).cross(var7);
      if (var9.lengthSqr() < Яф_) {
         return 0;
      }

      var9 = var9.normalize();
      Vec3 var10 = var9.scale(this.I(var7.length()));
      Vec3 var11 = var9.scale(this.I(var8.length()));
      this.I(var1, var2, var7.add(var10), 0.0F, 0.0F, var6);
      this.I(var1, var2, var7.subtract(var10), 0.0F, 1.0F, var6);
      this.I(var1, var2, var8.subtract(var11), 1.0F, 1.0F, var6);
      this.I(var1, var2, var8.add(var11), 1.0F, 0.0F, var6);
      return 4;
   }

   private void бо() {
      for (Prediction.Trajectory var2 : this.х_2571477d) {
         if (var2.spec() == Х_at_d02e9b55 && var2.impact() != null && !var2.item().isEmpty() && (var2.source() == null || !var2.source().isRemoved())) {
            Vector2f var3 = WorldProjection.projectToScreen(var2.impact());
            if (var3 != null) {
               Prediction.AreaEffect var4 = this.I(var2.item());
               ArrayList var5 = new ArrayList();
               if (this.I(var2)) {
                  var5.add(String.format(Locale.ROOT, ЯфЕ, var4.radius()));
               }

               float var6 = var2.handheld() ? 0.0F : MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(false);
               var5.add(String.format(Locale.ROOT, ЯфП, Math.max(0.0F, var2.ticks() - var6) / Яф西));
               this.I(var3, var4.name(), String.join(Яфд, var5), var2.item());
            }
         }
      }
   }

   private double I(double var1) {
      return Math.max(Яфы, Яфй * var1);
   }

   private void I_de5a6f78(List<Prediction.Trajectory> var1, Prediction.LaunchState var2) {
      if (var2 != null) {
         Prediction.Trajectory var3 = this.I(var2);
         if (var3.path().size() >= 2) {
            var1.add(var3);
         }
      }
   }

   private Prediction.Trajectory I(Prediction.LaunchState var1) {
      Prediction.ProjectileSpec var2 = var1.spec();
      Prediction.PhysicsKind var3 = var2.physics();
      ArrayList var4 = new ArrayList();
      var4.add(var1.origin());
      Vec3 var5 = var1.origin();
      Vec3 var6 = var1.velocity();
      Vec3 var7 = null;
      Fluid var8 = var2.stopsInWater() ? Fluid.ANY : Fluid.NONE;

      for (int var9 = 0; var9 < 400; var9++) {
         double var10 = var1.source() != null && var1.source().isNoGravity() ? 0.0 : var2.gravity();
         if (var3.Вщ) {
            var6 = var6.add(0.0, -var10, 0.0);
         }

         double var12 = (var2 == В || var2 == Х_at_d02e9b55) && MINECRAFT_CLIENT.level.getFluidState(BlockPos.containing(var5)).is(FluidTags.WATER) ? Я20 : var2.drag();
         if (var3.ВГ) {
            var6 = var6.scale(var12);
         }

         Vec3 var14 = var5.add(var6);
         BlockHitResult var15 = MINECRAFT_CLIENT.level.clip(new ClipContext(var5, var14, Block.COLLIDER, var8, MINECRAFT_CLIENT.player));
         boolean var16 = var15.getType() != Type.MISS;
         var5 = var16 ? var15.getLocation() : var14;
         var4.add(var5);
         if (var16) {
            var7 = var15.getDirection().getUnitVec3();
            break;
         }

         if (!var3.ВГ) {
            var6 = var6.scale(var12);
         }

         if (!var3.Вщ) {
            var6 = var6.add(0.0, -var10, 0.0);
         }

         if (var5.y < MINECRAFT_CLIENT.level.getMinY() - 16) {
            break;
         }
      }

      Prediction.EntityHit var17 = this.I_9d6a057c(var4, var1);
      if (var17 != null) {
         ArrayList var18 = new ArrayList(var4.subList(0, var17.step() + 1));
         var18.add(var17.position());
         return new Prediction.Trajectory(
            var2, var18, var17.position(), null, var17.entity(), var17.step() + 1, var1.handheld(), var1.info(), var1.source(), var1.item()
         );
      } else {
         return new Prediction.Trajectory(
            var2, var4, var7 == null ? null : var5, var7, null, var4.size() - 1, var1.handheld(), var1.info(), var1.source(), var1.item()
         );
      }
   }

   private Prediction.LaunchState I_3b9d34a0(Entity var1) {
      Prediction.ProjectileSpec var2 = this.I_f02fbd40(var1);
      if (var2 == null) {
         return null;
      } else {
         Vec3 var3 = var1.getDeltaMovement();
         if (var3.lengthSqr() < Я2к) {
            return null;
         } else if (!var2.stopsInWater() || !var1.isInWater() && !var1.onGround()) {
            return var1.distanceToSqr(MINECRAFT_CLIENT.player) > Я2г
               ? null
               : new Prediction.LaunchState(
                  var2, var1.position(), var3, false, true, var1, var1 instanceof ThrowableItemProjectile var4 ? var4.getItem().copy() : ItemStack.EMPTY
               );
         } else {
            return null;
         }
      }
   }

   private void б6() {
      this.У.clear();

      for (Entity var2 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
         if (this.У.size() >= 12) {
            break;
         }

         Prediction.LaunchState var3 = this.I_3b9d34a0(var2);
         if (var3 != null) {
            this.I_de5a6f78(this.У, var3.withInfo(true));
         }
      }
   }

   private Prediction.ProjectileSpec I_f02fbd40(Entity var1) {
      if (var1 instanceof ThrownEnderpearl) {
         return В;
      } else if (var1 instanceof Snowball || var1 instanceof ThrownEgg) {
         return Я_f02fbd40_at_d02e9b55;
      } else if (var1 instanceof AbstractThrownPotion) {
         return Х_at_d02e9b55;
      } else if (var1 instanceof ThrownExperienceBottle) {
         return е_at_d02e9b55;
      } else if (var1 instanceof AbstractWindCharge) {
         return А_at_d02e9b55;
      } else if (var1 instanceof ThrownTrident) {
         return field_4;
      } else if (var1 instanceof AbstractArrow) {
         return Д;
      } else {
         return var1 instanceof FishingHook ? Ъ_f02fbd40 : null;
      }
   }

   private static RenderPipeline п(String var0, CompareOp var1) {
      return RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(var0)
         .withVertexShader(ClientResourceIds.getCoreId(Я2ю))
         .withFragmentShader(ClientResourceIds.getCoreId(Я2Щ))
         .withDepthTest(var1)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
   }

   private Prediction.ProjectileSpec I(ItemStack var1, boolean var2) {
      Item var3 = var1.getItem();
      if (var3 == Items.ENDER_PEARL) {
         return В;
      }

      if (var3 == Items.SNOWBALL || var3 == Items.EGG) {
         return Я_f02fbd40_at_d02e9b55;
      }

      if (var3 == Items.SPLASH_POTION || var3 == Items.LINGERING_POTION) {
         return Х_at_d02e9b55;
      }

      if (var3 == Items.EXPERIENCE_BOTTLE) {
         return е_at_d02e9b55;
      }

      if (var3 == Items.WIND_CHARGE) {
         return А_at_d02e9b55;
      }

      if (var3 == Items.TRIDENT) {
         return field_4;
      }

      if (var3 == Items.FISHING_ROD) {
         return Ъ_f02fbd40;
      }

      if (var3 instanceof BowItem) {
         if (MINECRAFT_CLIENT.player.getProjectile(var1).isEmpty() && !MINECRAFT_CLIENT.player.hasInfiniteMaterials()) {
            return null;
         }

         float var5 = var2 ? BowItem.getPowerForTime(MINECRAFT_CLIENT.player.getTicksUsingItem()) : 1.0F;
         return var5 < Я2Т ? null : new Prediction.ProjectileSpec(Д.group(), Prediction.PhysicsKind.ARROW, Я2О, Я2Ф, var5 * Я2Й, 0.0F, false);
      } else {
         if (!(var3 instanceof CrossbowItem)) {
            return null;
         }

         if (!CrossbowItem.isCharged(var1)) {
            return null;
         }

         ChargedProjectiles var4 = var1.get(DataComponents.CHARGED_PROJECTILES);
         return var4 != null && var4.contains(Items.FIREWORK_ROCKET) ? null : п_at_d02e9b55;
      }
   }

   @Override
   public void onEnable() {
      this.бы();
   }

   private void Ы(WorldEffectsEvent var1) {
      if (this.Ч != MINECRAFT_CLIENT.level || MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null) {
         this.бы();
      }

      this.х_2571477d.clear();
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         this.х(var1.getDeltaTracker().getGameTimeDeltaPartialTick(true));
         if (!this.х_2571477d.isEmpty()) {
            this.method_0(var1);
         }
      }
   }

   private List<Prediction.LaunchState> I(ItemStack var1, boolean var2, float var3) {
      Prediction.ProjectileSpec var4 = this.I(var1, var2);
      if (var4 == null) {
         return List.of();
      }

      LocalPlayer var5 = MINECRAFT_CLIENT.player;
      float var6 = var5.getXRot();
      float var7 = var5.getYRot();
      Vec3 var8 = new Vec3(Mth.lerp(var3, var5.xo, var5.getX()), Mth.lerp(var3, var5.yo, var5.getY()), Mth.lerp(var3, var5.zo, var5.getZ()));
      double var9 = var5.getEyeY() - var5.getY();
      if (var4.physics() == Prediction.PhysicsKind.BOBBER) {
         Prediction.LaunchState var19 = this.п(var4, var8, var9, var6, var7);
         return var19 == null ? List.of() : List.of(var19.withInfo(true));
      }

      boolean var11 = var4 == п_at_d02e9b55 && this.衣(var1);
      float[] var12 = var11 ? Ъ_06b4556e : new float[]{0.0F};
      ArrayList var13 = new ArrayList(var12.length);

      for (float var17 : var12) {
         Prediction.LaunchState var18 = this.I(var4, var8, var9, var6, var7 + var17);
         if (var18 != null) {
            var13.add(var18.withInfo(var17 == 0.0F).withItem(var1));
         }
      }

      return var13;
   }

   private boolean О(Entity var1) {
      return var1.isAlive() && !var1.isSpectator() && var1.canBeHitByProjectile();
   }

   private DynamicTexture п() {
      if (this.Я_be9bcd64_at_d02e9b55 == null) {
         try (ByteArrayInputStream var1 = new ByteArrayInputStream(Base64.getDecoder().decode(ЯиЮ))) {
            NativeImage var2 = NativeImage.read(var1);
            this.Я_be9bcd64_at_d02e9b55 = new DynamicTexture(() -> ЯиЛ, var2);
            MINECRAFT_CLIENT.getTextureManager().register(у, this.Я_be9bcd64_at_d02e9b55);
         } catch (IOException var6) {
            throw new IllegalStateException(ЯиЧ, var6);
         }
      }

      return this.Я_be9bcd64_at_d02e9b55;
   }

   private int б(int var1, int var2) {
      return ColorMath.packArgb(ColorMath.getRed(var1), ColorMath.getGreen(var1), ColorMath.getBlue(var1), Mth.clamp(var2, 0, 255));
   }

   private record ProjectileSpec(
      String group, Prediction.PhysicsKind physics, double gravity, double drag, float speed, float roll, boolean stopsInWater
   ) {
   }

   private static final class IndicatorState {
      private final FloatAnimation сх;
      private final ItemStack ж;
      private Prediction.Trajectory В;
      private String Хэ;
      private boolean Аз;
      private static final float стШ = 10.0F;

      private IndicatorState(ItemStack var1) {
         this.сх = new FloatAnimation(0.0F, стШ);
         this.ж = var1;
      }
   }

   private record LaunchState(Prediction.ProjectileSpec spec, Vec3 origin, Vec3 velocity, boolean handheld, boolean info, Entity source, ItemStack item) {
      private Prediction.LaunchState withItem(ItemStack var1) {
         return new Prediction.LaunchState(this.spec, this.origin, this.velocity, this.handheld, this.info, this.source, var1.copy());
      }

      private Prediction.LaunchState withInfo(boolean var1) {
         return new Prediction.LaunchState(this.spec, this.origin, this.velocity, this.handheld, var1, this.source, this.item);
      }

      private LaunchState(Prediction.ProjectileSpec var1, Vec3 var2, Vec3 var3, boolean var4, boolean var5) {
         this(var1, var2, var3, var4, var5, null, ItemStack.EMPTY);
      }
   }

   private record EntityHit(Entity entity, Vec3 position, int step) {
   }

   private record Trajectory(
      Prediction.ProjectileSpec spec,
      List<Vec3> path,
      Vec3 impact,
      Vec3 normal,
      Entity victim,
      int ticks,
      boolean handheld,
      boolean info,
      Entity source,
      ItemStack item
   ) {
   }

   private record AreaEffect(String name, float radius, int color) {
   }

   private enum PhysicsKind {
      THROWN(true, true),
      ARROW(false, false),
      STRAIGHT(false, true),
      BOBBER(true, false);

      private final boolean Вщ;
      private final boolean ВГ;

      PhysicsKind(boolean var3, boolean var4) {
         this.Вщ = var3;
         this.ВГ = var4;
      }
   }
}
