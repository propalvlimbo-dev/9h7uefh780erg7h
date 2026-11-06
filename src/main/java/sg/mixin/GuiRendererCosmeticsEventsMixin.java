package sg.mixin;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.ec.UnhookState;
import sg.ec.WingPreviewRenderState;
import sg.ec.HatPreviewRenderState;
import sg.gui.BackpackPreviewRenderState;
import sg.client.RoseClient;
import sg.gui.autobuy.AutoBuyPanel;
import sg.events.impl.HudRenderEvent;
import sg.render.BackpackPreview;
import sg.render.HatPreview;
import sg.render.WingPreview;
import sg.render.avatar.MainMenuLoadingRenderer;
import sg.render.pipeline.UiDrawing;

@Mixin(GuiRenderer.class)
public class GuiRendererCosmeticsEventsMixin {
   @Shadow
   @Final
   @Mutable
   private Map<Class<? extends PictureInPictureRenderState>, PictureInPictureRenderer<?>> pictureInPictureRenderers;

   @Inject(method = "<init>", at = @At("TAIL"))
   private void addWingRenderer(CallbackInfo var1) {
      HashMap var2 = new HashMap<>(this.pictureInPictureRenderers);
      var2.put(WingPreviewRenderState.class, new WingPreview());
      var2.put(HatPreviewRenderState.class, new HatPreview());
      var2.put(BackpackPreviewRenderState.class, new BackpackPreview());
      this.pictureInPictureRenderers = Map.copyOf(var2);
   }

   @Inject(method = "render()V", at = @At("TAIL"))
   private void renders(CallbackInfo var1) {
      if (!UnhookState.ые()) {
         HudRenderEvent.GuiRender var2 = new HudRenderEvent.GuiRender(
            (float)Minecraft.getInstance().mouseHandler.xpos(), (float)Minecraft.getInstance().mouseHandler.ypos()
         );
         RoseClient.getInstance().getEventBus().post(var2);
         AutoBuyPanel.Вй();
         MainMenuLoadingRenderer.рЕ();
      }

      UiDrawing.advanceItemFrame();
   }
}
