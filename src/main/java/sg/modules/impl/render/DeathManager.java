package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;

public class DeathManager extends ClientModule {
   private static final float тр = 12.0F;
   private static final float т6 = 10.0F;
   private static final float т_ = 22.0F;
   private static final float тШ = 18.0F;
   private static final float ты = 1.4F;
   private final FloatSetting ХЙ;
   private final BooleanSetting ЯД;
   private final BooleanSetting ЯЪ;
   private final List<DeathManager.DeathMarker> сз;
   @SerializedName("2")
   private final Set<Integer> field_2;
   private final EventListener<PlayerTickEvent> Хэ;
   private final EventListener<HudRenderEvent> Хю;
   private static final String скш = "DeathManager";
   private static final String скИ = "Метка на месте смерти";
   private static final String скь = "Время жизни";
   private static final float скж = 15000.0F;
   private static final float ск弟 = 2000.0F;
   private static final float скб = 20000.0F;
   private static final float скЫ = 100.0F;
   private static final String скl = "Игроки";
   private static final String ск8 = "Мобы";
   private static final String ск5 = "%.1f, %.1f, %.1f";
   private static final double скв = 0.15;
   private static final float скъ = 0.12F;
   private static final String скМ = "DEAD";
   private static final String скН = "XYZ";
   private static final float скн = 13.0F;
   private static final float скЦ = 12.0F;
   private static final float скч = 14.0F;
   private static final String скЭ = "I";
   private static final float ск必 = 7.0F;
   private static final float скщ = 10.0F;
   private static final float скГ = 8.0F;
   private static final float скл = 116.0F;
   private static final float скэ = 20.0F;
   private static final float скю = 51.4F;
   private static final float скЩ = 4.0F;
   private static final float ску = 255.0F;
   private static final float ск2 = 12.0F;
   private static final double скф = 255.0;
   private static final float ски = 10.0F;
   private static final float ск9 = 5.0F;
   private static final String ско = "I";
   private static final float скO = 7.0F;
   private static final float ск7 = 10.0F;
   private static final float скР = 22.0F;
   private static final float ск3 = 0.5F;
   private static final float скр = 0.5F;
   private static final double ск6 = 700.0;
   private static final float ск_ = 0.55F;
   private static final float скШ = 10.0F;
   private static final float скы = 20.0F;
   private static final float скй = 1.4F;
   private static final float скЖ = 1.4F;
   private static final float скм = 5.0F;
   private static final float скК = 10.0F;
   private static final float скI = 10.0F;

   @Override
   public void onDisable() {
      this.сз.clear();
      this.field_2.clear();
   }

   private void зР(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         if (this.ЯД.getValue() || this.ЯЪ.getValue()) {
            HashSet var2 = new HashSet();

            for (Entity var4 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
               if (var4 instanceof LivingEntity var5) {
                  var2.add(var5.getId());
                  boolean var6 = var5 instanceof Player;
                  if ((!var6 || this.ЯД.getValue())
                     && (var6 || this.ЯЪ.getValue())
                     && (!(var5.getHealth() > 0.0F) || !var5.isAlive())
                     && this.field_2.add(var5.getId())
                     && this.сз.size() < 35) {
                     Vec3 var7 = var5.position();
                     float var8 = var5.getBbHeight();
                     String var9 = var5.getDisplayName().getString();
                     String var10 = String.format(ск5, var7.x, var7.y, var7.z);
                     this.сз.add(new DeathManager.DeathMarker(var5.getId(), var7.add(0.0, var8 + скв, 0.0), var9, var10, Math.round(this.ХЙ.getValue())));
                  }
               }
            }

            for (DeathManager.DeathMarker var13 : this.сз) {
               var2.add(var13.ч_a83dd0cc);
            }

            this.field_2.retainAll(var2);
            long var12 = System.currentTimeMillis();
            this.сз.removeIf(var2x -> var12 - var2x.э >= var2x.ю);
         }
      } else {
         this.сз.clear();
         this.field_2.clear();
      }
   }

   public DeathManager() {
      super(скш, скИ, ModuleCategory.RENDER);
      this.ХЙ = new FloatSetting(скь, скж, ск弟, скб).withStep(скЫ);
      this.ЯД = new BooleanSetting(скl, true);
      this.ЯЪ = new BooleanSetting(ск8, false);
      this.сз = new ArrayList<>();
      this.field_2 = new HashSet<>();
      this.Хэ = this::зР;
      this.Хю = this::зй;
      this.addSettings(this.ХЙ, this.ЯД, this.ЯЪ);
   }

   private void зй(HudRenderEvent var1) {
      if (!this.сз.isEmpty() && var1.getGraphics() != null) {
         long var2 = System.currentTimeMillis();

         for (DeathManager.DeathMarker var5 : this.сз) {
            float var6 = var5.т(var2);
            var6 *= var6;
            if (!(var6 <= скъ)) {
               Vector2f var7 = WorldProjection.projectToScreen(var5.Я);
               if (var7 != null) {
                  String var8 = скМ;
                  String var9 = var5.Ц;
                  String var10 = скН;
                  String var11 = var5.ч_388008bb;
                  float var12 = скн;
                  float var13 = скЦ;
                  float var14 = скч;
                  float var15 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var8, (int)var12);
                  float var16 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var9, (int)var12);
                  float var17 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var11, (int)var13);
                  float var18 = UiDrawing.getTextWidth(ClientFonts.getIconsFont(), скЭ, (int)var14);
                  float var19 = var18 + ск必 + var15 + скщ + var16;
                  float var20 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var10, (int)var13) + скГ + var17;
                  float var21 = Math.max(скл, Math.max(var19, var20) + скэ);
                  float var22 = скю;
                  float var23 = var7.x - var21 / 2.0F;
                  float var24 = var7.y - var22 - скЩ;
                  UiDrawing.setGlobalAlpha(ску * var6);
                  UiDrawing.drawWindowBackground(var23, var24, var21, var22, ск2);
                  int var25 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
                  int var26 = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT);
                  int var27 = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT);
                  int var28 = ColorMath.packArgb(255, 72, 88, скф);
                  float var29 = var23 + ски;
                  float var30 = var24 + ск9;
                  UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
                  UiBatchDrawing.drawText(ClientFonts.getIconsFont(), ско, var29, var30 + 1.0F, (int)var14, var25);
                  var29 += var18 + скO;
                  UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var8, var29, var30, (int)var12, var28);
                  var29 += var15 + ск7;
                  UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var9, var29, var30, (int)var12, var26);
                  float var31 = var24 + скР;
                  int var32 = ColorMath.withAlpha(
                     ColorMath.blendArgb(var25, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO), ск3 + скр * (float)Math.sin(System.currentTimeMillis() / ск6)), ск_
                  );
                  int var33 = ColorMath.withAlpha(var25, 0.0F);
                  UiBatchDrawing.drawRoundedRect(var23 + скШ, var31, var21 - скы, скй, new Vector4f(0.0F), new Vector4i(var33, var32, var32, var33));
                  float var34 = var31 + скЖ + скм;
                  UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var10, var23 + скК, var34, (int)var13, var25);
                  UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var11, var23 + var21 - скI - var17, var34, (int)var13, var27);
                  UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
                  UiDrawing.resetGlobalAlpha();
               }
            }
         }
      }
   }

   private static final class DeathMarker {
      private final Vec3 Я;
      private final String Ц;
      @SerializedName("ч")
      private final String ч_388008bb;
      private final long э = System.currentTimeMillis();
      private final long ю;
      @SerializedName("ч")
      private final int ч_a83dd0cc;
      private static final float Яну = 0.08F;
      private static final float Ян2 = 0.2F;

      private DeathMarker(int var1, Vec3 var2, String var3, String var4, long var5) {
         this.ч_a83dd0cc = var1;
         this.Я = var2;
         this.Ц = var3;
         this.ч_388008bb = var4;
         this.ю = var5;
      }

      private float т(long var1) {
         float var3 = Mth.clamp((float)(var1 - this.э) / (float)this.ю, 0.0F, 1.0F);
         float var4 = Mth.clamp(var3 / Яну, 0.0F, 1.0F);
         float var5 = Mth.clamp((1.0F - var3) / Ян2, 0.0F, 1.0F);
         return var4 * var5;
      }
   }
}
