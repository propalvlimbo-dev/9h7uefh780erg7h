package sg.hud;

import sg.ec.DeferredUiTextureQueue;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.hud.HudElement;
import sg.modules.impl.init.PanelBackgroundRenderer;
import sg.events.impl.HudRenderEvent;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;

public class HudRenderBeginHandler extends HudElement {
   private final @com.google.gson.annotations.SerializedName("еу")  EventListener<HudRenderEvent> beginListener = new EventListener<HudRenderEvent>() {
      @Override
      public int getPriority() {
         return 200;
      }

      public void onEvent(HudRenderEvent var1) {
         HudRenderBeginHandler.this.beginHudRender(var1);
      }
   };
   private static final String ELEMENT_NAME = "hud_without_config";

   public HudRenderBeginHandler() {
      super(ELEMENT_NAME, 0.0F, 0.0F, 0.0F, 0.0F);
   }

   public void beginHudRender(HudRenderEvent event) {
      HudDisplacementManager.updateDisplacements();
      UiBatchDrawing.discardBatch();
      PanelBackgroundRenderer.ЕЩ();
      DeferredUiTextureQueue.жI();
      UiDrawing.resetGlobalAlpha();
      UiDrawing.resetTransforms();
      UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
   }
}
