package sg.modules.impl.render;

import java.util.Locale;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import sg.rotation.AimGeometryMath;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.HudRenderEvent;

public final class TNTTimer extends ClientModule {
   private static final float Аъ = 64.0F;
   private static final int еГ = 13;
   private final EventListener<HudRenderEvent> Вн = this::Ъ;
   private static final String съу = "TNTTimer";
   private static final String съ2 = "Время до взрыва динамита";
   private static final double съф = 64.0;
   private static final String съи = "%.1f сек";
   private static final float съ9 = 20.0F;
   private static final double съо = 0.5;
   private static final float съO = 6.0F;
   private static final float съ7 = 13.0F;
   private static final float съР = 6.0F;
   private static final float съ3 = 0.5F;
   private static final double сър = 125.0;
   private static final float съ6 = 3.0F;
   private static final float съ_ = 0.5F;
   private static final float съШ = 13.0F;
   private static final double съы = 255.0;

   public TNTTimer() {
      super(съу, съ2, ModuleCategory.RENDER);
   }

   private void Ъ(HudRenderEvent var1) {
      if (var1.getGraphics() != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         UiDrawing.setTransform(new Matrix4f());
         float var2 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);

         for (PrimedTnt var4 : MINECRAFT_CLIENT.level.getEntitiesOfClass(PrimedTnt.class, MINECRAFT_CLIENT.player.getBoundingBox().inflate(съф), var0 -> true)) {
            String var5 = String.format(Locale.ROOT, съи, var4.getFuse() / съ9);
            Vec3 var6 = AimGeometryMath.lerpVector(new Vec3(var4.xo, var4.yo, var4.zo), var4.position(), var2).add(0.0, var4.getBbHeight() + съо, 0.0);
            Vector2f var7 = WorldProjection.projectToScreen(var6);
            if (var7 != null) {
               float var8 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var5, 13) + съO;
               float var9 = UiDrawing.getCapHeight(ClientFonts.getMediumFont(), съ7) + съР;
               float var10 = var7.x - var8 * съ3;
               float var11 = var7.y;
               UiDrawing.drawRoundedRect(var10, var11, var8, var9, 0.0F, ColorMath.packArgb(15, 15, 16, сър));
               UiDrawing.drawText(
                  ClientFonts.getMediumFont(),
                  var5,
                  var10 + съ6,
                  UiDrawing.alignCapHeightCenterY(ClientFonts.getMediumFont(), var11 + var9 * съ_, съШ),
                  13,
                  ColorMath.packArgb(255, 55, 55, съы)
               );
            }
         }
      }
   }
}
