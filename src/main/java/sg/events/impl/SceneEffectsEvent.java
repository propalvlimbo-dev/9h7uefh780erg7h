package sg.events.impl;

import sg.events.impl.WorldEffectsEvent;

public final class SceneEffectsEvent extends WorldEffectsEvent {
   public SceneEffectsEvent(WorldEffectsEvent worldEffects) {
      super(worldEffects.getDeltaTracker(), worldEffects.getPoses(), worldEffects.getGeometryBuffer(), worldEffects.getCameraPosition(), worldEffects.getTickDelta(), worldEffects.getProjectionMatrix(), worldEffects.getViewMatrix());
   }
}
