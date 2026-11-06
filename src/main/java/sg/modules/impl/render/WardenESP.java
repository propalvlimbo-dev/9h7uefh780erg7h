package sg.modules.impl.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import sg.events.impl.PlayerTickEvent;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.events.impl.WorldEffectsEvent;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.WardenFarmArea;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.RoseXrayLines;
import sg.events.impl.HudRenderEvent;
import sg.modules.impl.init.TimedChestTracker;

public final class WardenESP extends ClientModule {
   private final TimedChestTracker В = new TimedChestTracker();
   private final EventListener<PlayerTickEvent> К = this::西;
   private final EventListener<WorldEffectsEvent> I = this::а;
   private final EventListener<HudRenderEvent> Т = this::я;
   private static final String ВЦя = "Warden ESP";
   private static final String Вчс = "Отображает сундуки в городе варденов с таймером возрождения";
   private static final double ВчВ = 255.0;
   private static final double ВчЯ = 0.6;
   private static final long ВчХ = 1000L;
   private static final String Вче = "%02d:%02d";
   private static final float ВчА = 28.0F;
   private static final float Вч4 = 22.0F;
   private static final float Вчп = 0.5F;
   private static final float ВчД = 0.5F;
   private static final float ВчЪ = 6.0F;
   private static final double Вчт = 210.0;
   private static final float Вча = 4.0F;
   private static final float ВчЮ = 3.0F;
   private static final float ВчЧ = 0.9F;
   private static final float ВчЛ = 22.0F;
   private static final float Вч诶 = 0.5F;
   private static final float Вч1 = 11.0F;
   private static final double ВчЬ = 255.0;

   private void я(HudRenderEvent var1) {
      if (var1.getGraphics() != null && MINECRAFT_CLIENT.level != null && WardenFarmArea.Л(MINECRAFT_CLIENT.level)) {
         UiDrawing.setTransform(new Matrix4f());

         for (BlockPos var3 : this.В.о_2571477d()) {
            TimedChestTracker.TimedChest var4 = this.В.诶_80db4183(var3);
            if (var4 != null) {
               Vector2f var5 = WorldProjection.projectToScreen(Vec3.atCenterOf(var3).add(0.0, ВчЯ, 0.0));
               if (var5 != null) {
                  int var6 = (int)(var4.Ф() / ВчХ);
                  String var7 = String.format(Locale.US, Вче, var6 / 60, var6 % 60);
                  float var8 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var7, 11);
                  float var9 = ВчА + var8;
                  float var10 = Вч4;
                  float var11 = var5.x - var9 * Вчп;
                  float var12 = var5.y - var10 * ВчД;
                  UiDrawing.drawRoundedRect(var11, var12, var9, var10, ВчЪ, ColorMath.packArgb(11, 11, 13, Вчт));
                  UiDrawing.drawItem(Items.CHEST.getDefaultInstance(), var11 + Вча, var12 + ВчЮ, ВчЧ, false);
                  UiDrawing.drawText(
                     ClientFonts.getMediumFont(),
                     var7,
                     var11 + ВчЛ,
                     UiDrawing.alignCapHeightCenterY(ClientFonts.getMediumFont(), var12 + var10 * Вч诶, Вч1),
                     11,
                     ColorMath.packArgb(238, 247, 255, ВчЬ)
                  );
               }
            }
         }
      }
   }

   public WardenESP() {
      super(ВЦя, Вчс, ModuleCategory.RENDER);
   }

   private void 西(PlayerTickEvent var1) {
      this.В.щ();
   }

   private void а(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.level != null && WardenFarmArea.Л(MINECRAFT_CLIENT.level)) {
         VertexConsumer var2 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
         Vec3 var3 = var1.getCameraPosition();
         int var4 = ColorMath.packArgb(255, 100, 100, ВчВ);

         for (BlockPos var6 : this.В.о_2571477d()) {
            if (this.В.诶_80db4183(var6) == null) {
               WorldBoxGeometry.appendBox(
                  var1.getPoses(),
                  var2,
                  var3,
                  var6.getX(),
                  var6.getY(),
                  var6.getZ(),
                  var6.getX() + 1.0,
                  var6.getY() + 1.0,
                  var6.getZ() + 1.0,
                  var4,
                  false
               );
            }
         }
      }
   }

   public TimedChestTracker е_a46d1ceb() {
      return this.В;
   }
}
