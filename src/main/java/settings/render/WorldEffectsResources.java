package settings.render;

import sg.events.impl.WorldGeometryBuffer;
import sg.modules.impl.render.Beautiful;
import sg.modules.impl.render.CubeField;
import sg.client.RoseClient;
import sg.modules.impl.render.MotionBlur;
import sg.modules.impl.misc.Party;
import sg.render.pipeline.LightningTargetEspRenderer;
import sg.modules.impl.render.Ambience;
import sg.render.pipeline.TargetLighting;

public final class WorldEffectsResources {
   public static void resetWorldEffects() {
      TargetLighting.closeResources();
      LightningTargetEspRenderer.н2();
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         Beautiful beautiful = RoseClient.getInstance().getServices().getModules().getBeautiful();
         if (beautiful != null) {
            beautiful.releaseResources();
         }

         MotionBlur motionBlur = RoseClient.getInstance().getServices().getModules().getMotionBlur();
         if (motionBlur != null) {
            motionBlur.Ои();
         }

         CubeField cubeField = RoseClient.getInstance().getServices().getModules().getCubeField();
         if (cubeField != null) {
            cubeField.лц();
         }

         Party party = RoseClient.getInstance().getServices().getModules().getParty();
         if (party != null) {
            party.Дю();
         }

         Ambience ambience = RoseClient.getInstance().getServices().getModules().getAmbience();
         if (ambience != null) {
            ambience.resetWorldEffects();
         }
      }
   }

   public static void finishFrame() {
      TargetLighting.resetFrame();
      LightningTargetEspRenderer.нш();
      WorldGeometryBuffer.getShared().finishFrame();
   }

   private WorldEffectsResources() {
   }

   public static void close() {
      resetWorldEffects();
      WorldGeometryBuffer.getShared().close();
   }
}
