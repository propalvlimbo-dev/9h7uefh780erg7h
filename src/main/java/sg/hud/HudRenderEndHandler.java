package sg.hud;

import sg.ec.DeferredUiTextureQueue;
import sg.events.EventListener;
import sg.hud.HudElement;
import sg.modules.impl.init.PanelBackgroundRenderer;
import sg.events.impl.HudRenderEvent;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;

public class HudRenderEndHandler extends HudElement {
   private final @com.google.gson.annotations.SerializedName("Хц")  EventListener<HudRenderEvent> endListener = new EventListener<HudRenderEvent>() {
      @Override
      public int getPriority() {
         return -50;
      }

      public void onEvent(HudRenderEvent var1) {
         HudRenderEndHandler.this.endHudRender(var1);
      }
   };
   private static final String ELEMENT_NAME = "hud_without_config";

   public void endHudRender(HudRenderEvent event) {
      UiBatchDrawing.finishBatch();
      PanelBackgroundRenderer.Ен();
      DeferredUiTextureQueue.жф();
      UiDrawing.resetGlobalAlpha();
      UiDrawing.resetTransforms();
   }

   public HudRenderEndHandler() {
      super(ELEMENT_NAME, 0.0F, 0.0F, 0.0F, 0.0F);
   }
}
