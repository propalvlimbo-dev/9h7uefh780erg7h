package sg.mixin;

import sg.mixinbridge.HmiClientInitializer;
import sg.mixinbridge.WorldEffectsRendererAccess;
import sg.mixinbridge.RenderSuppressionQueries;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settings.render.WorldEffectsResources;
import settings.render.HmiIntegration;
import sg.ec.UnhookState;
import sg.ec.MinecraftAccess;
import sg.events.impl.SceneEffectsEvent;
import sg.events.impl.WorldGeometryBuffer;
import sg.events.impl.WorldEffectsEvent;
import sg.modules.ModuleRegistry;
import sg.modules.impl.combat.Aura;
import sg.modules.impl.init.RoseXrayLines;
import sg.modules.impl.render.FullBright;
import sg.modules.impl.render.Hands;
import sg.modules.impl.render.Invoker;
import sg.client.RoseClient;
import sg.modules.impl.render.MotionBlur;
import sg.modules.impl.render.Rain;
import sg.events.impl.RenderSuppressionQuery;
import sg.modules.impl.render.ShaderMist;
import sg.modules.impl.render.ViewModel;
import sg.render.WorldProjection;
import sg.modules.impl.render.Ambience;
import sg.modules.impl.render.BlockOverlay;
import sg.modules.impl.render.FogAtmosphere;
import sg.render.pipeline.PrismaticChams;
import sg.render.pipeline.TargetEspMask;

@Mixin(GameRenderer.class)
public class GameRendererWorldEffectsMixin implements WorldEffectsRendererAccess {
   @Shadow
   @Final
   private FeatureRenderDispatcher featureRenderDispatcher;
   @Shadow
   @Final
   private SubmitNodeStorage handAndScreenSubmitNodeStorage;
   @Shadow
   @Final
   private Minecraft minecraft;
   @Unique
   private final Matrix4f fogProjection = new Matrix4f();
   @Unique
   private DeltaTracker worldEffectsTickCounter;
   @Unique
   private WorldEffectsEvent worldEffectsEvent;
   private static final double MAX_HMI_DELTA_SECONDS = 0.05;

   @Inject(method = "render", at = @At("HEAD"))
   private void hmiDeltaTime(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo callback) {
      HmiIntegration.Т_();
      if (HmiIntegration.п弟()) {
         float currentTimeSeconds = (float)GLFW.glfwGetTime();
         HmiClientInitializer.Юр = currentTimeSeconds - HmiClientInitializer.Ю3;
         HmiClientInitializer.Ю3 = currentTimeSeconds;
         if (Minecraft.getInstance().isPaused()) {
            HmiClientInitializer.Юр = 0.0F;
         } else {
            HmiClientInitializer.Юр = (float)Math.min(MAX_HMI_DELTA_SECONDS, HmiClientInitializer.Юр);
         }
      }
   }

   @Inject(method = "displayItemActivation", at = @At("HEAD"), cancellable = true)
   private void showFloatingItem(ItemStack itemStack, CallbackInfo callback) {
      if (itemStack.is(Items.TOTEM_OF_UNDYING) && RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.TOTEM)) {
         callback.cancel();
      }
   }

   @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
   private void tiltViewWhenHurt(CameraRenderState cameraRenderState, PoseStack poseStack, CallbackInfo callback) {
      if (RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.HURT_TIME)) {
         callback.cancel();
      }
   }

   @Inject(method = "extractOptions", at = @At("TAIL"))
   private void rose$removeNauseaDistortion(CallbackInfo callback) {
      if (RenderSuppressionQueries.С(RenderSuppressionQuery.Effect.BAD_EFFECTS)) {
         this.minecraft.gameRenderer.gameRenderState().optionsRenderState.screenEffectScale = 0.0F;
      }
   }

   @Inject(method = "shouldRenderBlockOutline", at = @At("HEAD"), cancellable = true)
   private void hideVanillaBlockOutline(CallbackInfoReturnable<Boolean> callback) {
      if (BlockOverlay.УО()) {
         callback.setReturnValue(false);
      }
   }

   @Inject(method = "renderLevel", at = @At("HEAD"))
   private void resetChamsFrameState(DeltaTracker deltaTracker, CallbackInfo callback) {
      PrismaticChams.beginFrame();
   }

   @Inject(
      method = "renderLevel",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/CommandEncoder;clearDepthTexture(Lcom/mojang/blaze3d/textures/GpuTexture;D)V")
   )
   private void applyMotionBlurBeforeHands(DeltaTracker deltaTracker, CallbackInfo callback, @Local(ordinal = 0) Matrix4f projectionMatrix) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         MotionBlur motionBlur = RoseClient.getInstance().getServices().getModules().getMotionBlur();
         if (motionBlur != null) {
            motionBlur.к(projectionMatrix, this.minecraft.gameRenderer.gameRenderState().levelRenderState.cameraRenderState.viewRotationMatrix);
         }
      }
   }

   @Inject(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lnet/minecraft/client/renderer/state/level/CameraRenderState;FLorg/joml/Matrix4fc;)V",
         shift = Shift.BEFORE
      )
   )
   private void flushBeforeHandsCapture(DeltaTracker deltaTracker, CallbackInfo callback) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         Hands hands = RoseClient.getInstance().getServices().getModules().getHands();
         if (hands != null && hands.shouldCapture()) {
            this.featureRenderDispatcher.renderAllFeatures(this.handAndScreenSubmitNodeStorage);
            WorldGeometryBuffer.getShared().flush();
         }
      }
   }

   @Inject(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lnet/minecraft/client/renderer/state/level/CameraRenderState;FLorg/joml/Matrix4fc;)V",
         shift = Shift.AFTER
      )
   )
   private void flushAndCompositeHands(DeltaTracker deltaTracker, CallbackInfo callback) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         Hands hands = RoseClient.getInstance().getServices().getModules().getHands();
         ViewModel viewModel = RoseClient.getInstance().getServices().getModules().getViewModel();
         boolean viewModelCaptureActive = viewModel != null && ViewModel.АI;
         boolean handsCaptureActive = hands != null && Hands.captureActive;
         if (viewModelCaptureActive || handsCaptureActive) {
            this.featureRenderDispatcher.renderAllFeatures(this.handAndScreenSubmitNodeStorage);
            WorldGeometryBuffer.getShared().flush();
            if (viewModelCaptureActive) {
               viewModel.к诶();
            } else {
               hands.endCaptureAndComposite();
            }
         }
      }
   }

   @Inject(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lnet/minecraft/client/renderer/state/level/CameraRenderState;FLorg/joml/Matrix4fc;)V"
      )
   )
   private void compositeTargetEspCube(DeltaTracker deltaTracker, CallbackInfo callback) {
      if (TargetEspMask.isCubeCompositePending()) {
         this.featureRenderDispatcher.renderAllFeatures(this.handAndScreenSubmitNodeStorage);
         WorldGeometryBuffer.getShared().flush();
         TargetEspMask.compositeCubeMask();
      }
   }

   @Inject(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/LevelRenderer;render(Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/renderer/state/level/CameraRenderState;Lorg/joml/Matrix4fc;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V"
      )
   )
   private void captureWorldMatrices(DeltaTracker deltaTracker, CallbackInfo callback, @Local(ordinal = 0) Matrix4f projectionMatrix) {
      Matrix4f viewRotationMatrix = new Matrix4f(this.minecraft.gameRenderer.gameRenderState().levelRenderState.cameraRenderState.viewRotationMatrix);
      this.fogProjection.set(projectionMatrix);
      this.worldEffectsTickCounter = deltaTracker;
      this.worldEffectsEvent = null;
      WorldProjection.setMatrices(viewRotationMatrix, projectionMatrix);
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
         FullBright fullBright = RoseClient.getInstance().getServices().getModules().getFullBright();
         if (fullBright != null && fullBright.isEnabled()) {
            fullBright._3(deltaTracker.getGameTimeDeltaPartialTick(false));
         }
      }
   }

   @Inject(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/LevelRenderer;render(Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/renderer/state/level/CameraRenderState;Lorg/joml/Matrix4fc;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V",
         shift = Shift.AFTER
      )
   )
   private void renderWorldEffectsFallback(DeltaTracker deltaTracker, CallbackInfo callback) {
      this.rose$renderWorldEffects();
      this.renderSceneEffects();
   }

   @Unique
   private void renderSceneEffects() {
      WorldEffectsEvent worldEvent = this.worldEffectsEvent;
      this.worldEffectsEvent = null;
      if (worldEvent != null) {
         if (RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
            ModuleRegistry modules = RoseClient.getInstance().getServices().getModules();
            FogAtmosphere fogAtmosphere = modules.getFogAtmosphere();
            if (fogAtmosphere != null) {
               fogAtmosphere.Й(worldEvent.getProjectionMatrix(), worldEvent.getViewMatrix(), worldEvent.getDeltaTracker());
            }

            Ambience ambience = modules.getAmbience();
            if (ambience != null) {
               ambience.renderWorldPostEffects(worldEvent.getProjectionMatrix(), worldEvent.getViewMatrix(), worldEvent.getDeltaTracker());
            }
         }

         RoseClient.getInstance().getEventBus().post(new SceneEffectsEvent(worldEvent));
         WorldGeometryBuffer.getShared().flush();
      }
   }

   @Override
   public void rose$renderWorldEffects() {
      if (this.worldEffectsTickCounter != null && !Rain.Пз()) {
         DeltaTracker deltaTracker = this.worldEffectsTickCounter;
         this.worldEffectsTickCounter = null;
         Matrix4f projectionMatrix = new Matrix4f(this.fogProjection);
         if (!UnhookState.ые()) {
            Matrix4f viewRotationMatrix = new Matrix4f(this.minecraft.gameRenderer.gameRenderState().levelRenderState.cameraRenderState.viewRotationMatrix);
            if (RoseClient.getInstance() != null) {
               PrismaticChams.captureSceneIfNeeded();
               if (RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
                  Ambience ambience = RoseClient.getInstance().getServices().getModules().getAmbience();
                  if (ambience != null) {
                     ambience.renderShaderSky(projectionMatrix, viewRotationMatrix, deltaTracker);
                  }

                  ShaderMist shaderMist = RoseClient.getInstance().getServices().getModules().getShaderMist();
                  if (shaderMist != null) {
                     shaderMist.renderPulsarSky(projectionMatrix, viewRotationMatrix, deltaTracker);
                  }

                  if (ambience != null) {
                     ambience.renderWeatherEffects(projectionMatrix, viewRotationMatrix, RenderSystem.getShaderFog());
                     ambience.renderLightning(projectionMatrix, viewRotationMatrix);
                  }
               }

               TargetEspMask.beginDeferredRender();
               Aura aura = null;
               if (RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null) {
                  aura = RoseClient.getInstance().getServices().getModules().getAura();
               }

               PoseStack poseStack = new PoseStack();
               poseStack.mulPose(new Matrix4f(viewRotationMatrix));
               WorldGeometryBuffer.StagedBuffer geometryBuffer = WorldGeometryBuffer.getShared();
               Vec3 cameraPosition = MinecraftAccess.MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
               float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
               PrismaticChams.renderPendingModels();
               WorldEffectsEvent worldEvent = new WorldEffectsEvent(deltaTracker, poseStack, geometryBuffer, cameraPosition, partialTick, new Matrix4f(projectionMatrix), new Matrix4f(viewRotationMatrix));
               this.worldEffectsEvent = worldEvent;
               Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
               modelViewStack.pushMatrix();

               try {
                  // Event vertices already include the camera rotation in their PoseStack.
                  modelViewStack.identity();
                  RoseClient.getInstance().getEventBus().post(worldEvent);
                  geometryBuffer.flush(RenderTypes.lines());
                  geometryBuffer.flush(RoseXrayLines.LINES);
                  geometryBuffer.flush(RoseXrayLines.QUADS);
                  geometryBuffer.flush(Invoker.Я_at_5266f6df);
                  geometryBuffer.flush(Invoker.Х_at_5266f6df);
                  if (aura != null && !aura.isEnabled()) {
                     TargetEspMask.renderFadingTarget(worldEvent);
                  }
               } finally {
                  modelViewStack.popMatrix();
               }
            }
         }
      }
   }

   @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = Shift.AFTER))
   private void renderTargetEspAboveEntityEsp(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo callback) {
      TargetEspMask.flushDeferredRender();
   }

   @Inject(method = "render", at = @At("RETURN"))
   private void endRenderResources(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo callback) {
      WorldEffectsResources.finishFrame();
   }

   @Inject(method = "mainRenderTarget", at = @At("RETURN"), cancellable = true)
   private void routeHandsFramebuffer(CallbackInfoReturnable<RenderTarget> callback) {
      if (Rain.Пз() && Rain.method_8_68bfbb94() != null) {
         callback.setReturnValue(Rain.method_8_68bfbb94());
      } else if (ViewModel.АI && ViewModel.Ъ != null) {
         callback.setReturnValue(ViewModel.Ъ);
      } else if (Hands.captureActive && Hands.activeCaptureTarget != null) {
         callback.setReturnValue(Hands.activeCaptureTarget);
      }
   }

   @WrapOperation(
      method = "extract",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;extractRenderState(Lnet/minecraft/client/DeltaTracker;ZZ)V")
   )
   private void rose$deferGui(Gui gui, DeltaTracker deltaTracker, boolean renderLevel, boolean gameLoadFinished, Operation<Void> original) {
      this.minecraft.gameRenderer.gameRenderState().guiRenderState.isHudHidden = gui.hud.isHidden();
   }

   @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;render()V"))
   private void rose$extractGuiBeforeDrawing(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo callback) {
      boolean gameLoadFinished = this.minecraft.isGameLoadFinished();
      this.minecraft.gui.extractRenderState(deltaTracker, gameLoadFinished && renderLevel && this.minecraft.level != null, gameLoadFinished);
   }
}
