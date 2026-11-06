package sg.render.pipeline;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemUseAnimation;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.modules.impl.render.Hands;

public final class HandsItemBloom {
   public static final HandsItemBloom INSTANCE = new HandsItemBloom();
   private static final int BLUR_UNIFORM_SIZE_BYTES = 48;
   private static final int GLOW_UNIFORM_SIZE_BYTES = 80;
   private static final float сЙ = 0.001F;
   private final @com.google.gson.annotations.SerializedName("Ы")  RenderPipeline baseCompositePipeline = createCompositePipeline(-1);
   private final @com.google.gson.annotations.SerializedName("е")  RenderPipeline[] shaderStylePipelines = new RenderPipeline[6];
   private final @com.google.gson.annotations.SerializedName("l")  RenderPipeline noneCompositePipeline;
   @SerializedName("8")
   private final RenderPipeline handMaskPipeline;
   @SerializedName("5")
   private final RenderPipeline sceneDownsamplePipeline;
   private final @com.google.gson.annotations.SerializedName("в")  RenderPipeline maskDownsamplePipeline;
   private final @com.google.gson.annotations.SerializedName("ъ")  RenderPipeline maskUpsamplePipeline;
   private final @com.google.gson.annotations.SerializedName("М")  RenderPipeline sceneGaussianPipeline;
   private final @com.google.gson.annotations.SerializedName("Н")  RenderPipeline maskGaussianPipeline;
   private final @com.google.gson.annotations.SerializedName("н")  RenderPipeline maskSupportPipeline;
   private final @com.google.gson.annotations.SerializedName("Ц")  RenderPipeline itemDownsamplePipeline;
   private final @com.google.gson.annotations.SerializedName("ч")  RenderPipeline itemUpsamplePipeline;
   private final @com.google.gson.annotations.SerializedName("Э")  RenderPipeline itemGaussianPipeline;
   private final @com.google.gson.annotations.SerializedName("必")  RenderPipeline itemColorPipeline;
   private final @com.google.gson.annotations.SerializedName("щ")  RenderPipeline outerGlowPipeline;
   private final @com.google.gson.annotations.SerializedName("Г")  RenderPipeline outlinePipeline;
   private final @com.google.gson.annotations.SerializedName("л")  RenderPipeline glowOutlinePipeline;
   private final @com.google.gson.annotations.SerializedName("э")  RenderPipeline flameTrailDisplacePipeline;
   private final @com.google.gson.annotations.SerializedName("ю")  RenderPipeline energyTrailDisplacePipeline;
   private final @com.google.gson.annotations.SerializedName("Щ")  RenderPipeline flameTrailPipeline;
   private final @com.google.gson.annotations.SerializedName("у")  RenderPipeline energyTrailPipeline;
   @SerializedName("2")
   private final RenderPipeline ribbonTrailDisplacePipeline;
   private final @com.google.gson.annotations.SerializedName("ф")  RenderPipeline ribbonTrailPipeline;
   private final @com.google.gson.annotations.SerializedName("и")  RenderPipeline trailBlitPipeline;
   @SerializedName("В")
   private RenderTarget handMaskTarget;
   @SerializedName("Я")
   private RenderTarget handSupportTarget;
   @SerializedName("В")
   private final HandsItemBloom.BlurChain maskBloomChain;
   @SerializedName("Я")
   private final HandsItemBloom.BlurChain itemBloomChain;
   private final @com.google.gson.annotations.SerializedName("Х")  HandsItemBloom.BlurChain mirrorSceneBlurChain;
   private @com.google.gson.annotations.SerializedName("Д")  TextureTarget trailReadTarget;
   private @com.google.gson.annotations.SerializedName("Ъ")  TextureTarget trailWriteTarget;
   private @com.google.gson.annotations.SerializedName("а")  GpuBuffer handsUniformBuffer;
   private @com.google.gson.annotations.SerializedName("Ю")  GpuBuffer glowUniformBuffer;
   private @com.google.gson.annotations.SerializedName("Ч")  GpuBuffer outlineUniformBuffer;
   private @com.google.gson.annotations.SerializedName("Л")  GpuBuffer trailDisplacementUniformBuffer;
   private @com.google.gson.annotations.SerializedName("诶")  GpuBuffer flameTrailUniformBuffer;
   @SerializedName("1")
   private GpuBuffer energyTrailUniformBuffer;
   private @com.google.gson.annotations.SerializedName("Ь")  GpuBuffer ribbonTrailUniformBuffer;
   private @com.google.gson.annotations.SerializedName("У")  long previousTrailTimeNanos;
   private @com.google.gson.annotations.SerializedName("о")  double ribbonAnimationTime;
   private @com.google.gson.annotations.SerializedName("сЫ")  int ribbonFrameIndex;
   private @com.google.gson.annotations.SerializedName("ск")  float smoothedRibbonLength;
   private @com.google.gson.annotations.SerializedName("сг")  float smoothedRibbonStrength;
   private @com.google.gson.annotations.SerializedName("Ш")  boolean ribbonStateInitialized;
   private @com.google.gson.annotations.SerializedName("с0")  float swingDecayImpulse;
   private @com.google.gson.annotations.SerializedName("ы")  boolean previousSwinging;
   private @com.google.gson.annotations.SerializedName("сх")  float previousCameraYaw;
   private @com.google.gson.annotations.SerializedName("сУ")  float previousCameraPitch;
   private @com.google.gson.annotations.SerializedName("й")  boolean cameraAnglesReady;
   private @com.google.gson.annotations.SerializedName("сl")  int previousTrailStyleId;
   private @com.google.gson.annotations.SerializedName("Ж")  boolean previousItemColorsEnabled;
   @SerializedName("В")
   private final HandsItemBloom.UniformStateCache glowUniformStateCache;
   @SerializedName("Я")
   private final HandsItemBloom.UniformStateCache outlineUniformStateCache;
   private static final String BASE_COMPOSITE_STYLE_LABEL = "base";
   private static final String CREATE_COMPOSITE_PIPELINE_VERTEX_SHADER = "hands_composite";
   private static final String CREATE_COMPOSITE_PIPELINE_FRAGMENT_SHADER = "hands_composite";
   private static final String CREATE_COMPOSITE_PIPELINE_UNIFORM_NAME = "HandsData";
   private static final String CREATE_COMPOSITE_PIPELINE_HAND_SAMPLER_NAME = "HandSampler";
   private static final String CREATE_COMPOSITE_PIPELINE_HAND_DEPTH_SAMPLER_NAME = "HandDepthSampler";
   private static final String CREATE_COMPOSITE_PIPELINE_HAND_MASK_SAMPLER_NAME = "HandMaskSampler";
   private static final String CREATE_COMPOSITE_PIPELINE_HAND_SUPPORT_SAMPLER_NAME = "HandSupportSampler";
   private static final String CREATE_COMPOSITE_PIPELINE_BLURRED_SCENE_SAMPLER_NAME = "BlurredSceneSampler";
   private static final String CREATE_COMPOSITE_PIPELINE_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String CREATE_COMPOSITE_PIPELINE_SHADER_DEFINE = "HANDS_STYLE";
   private static final String MASK_PIPELINE_LOCATION_SUFFIX = "_mask";
   private static final String ITEM_COLOR_PIPELINE_LOCATION_SUFFIX = "_item";
   private static final String SCENE_PIPELINE_LOCATION_SUFFIX = "_scene";
   private static final String CREATE_BLUR_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String BLUR_PIPELINE_SOURCE_SAMPLER_NAME = "Sampler0";
   private static final String CREATE_BLUR_PIPELINE_SHADER_DEFINE = "HANDS_MASK";
   private static final String CREATE_BLUR_PIPELINE_UNIFORM_NAME = "HandsBlur";
   private static final String NONE_COMPOSITE_PIPELINE_LOCATION = "rose/hands_nothing_composite";
   private static final String NONE_COMPOSITE_PIPELINE_VERTEX_SHADER = "hands_composite";
   private static final String NONE_COMPOSITE_PIPELINE_FRAGMENT_SHADER = "hands_nothing_composite";
   private static final String NONE_COMPOSITE_PIPELINE_UNIFORM_NAME = "HandsData";
   private static final String NONE_COMPOSITE_PIPELINE_HAND_SAMPLER_NAME = "HandSampler";
   private static final String NONE_COMPOSITE_PIPELINE_HAND_MASK_SAMPLER_NAME = "HandMaskSampler";
   private static final String NONE_COMPOSITE_PIPELINE_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String HAND_MASK_PIPELINE_LOCATION = "rose/hands_mask";
   private static final String HAND_MASK_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String HAND_MASK_PIPELINE_FRAGMENT_SHADER = "hands_mask";
   private static final String HAND_MASK_PIPELINE_HAND_SAMPLER_NAME = "HandSampler";
   private static final String HAND_MASK_PIPELINE_HAND_DEPTH_SAMPLER_NAME = "HandDepthSampler";
   private static final String SCENE_DOWNSAMPLE_PIPELINE_FRAGMENT_SHADER = "hands_downsample";
   private static final String MASK_DOWNSAMPLE_PIPELINE_FRAGMENT_SHADER = "hands_downsample";
   private static final String MASK_UPSAMPLE_PIPELINE_FRAGMENT_SHADER = "hands_upsample";
   private static final String SCENE_GAUSSIAN_PIPELINE_FRAGMENT_SHADER = "hands_gaussian";
   private static final String MASK_GAUSSIAN_PIPELINE_FRAGMENT_SHADER = "hands_gaussian";
   private static final String MASK_SUPPORT_PIPELINE_FRAGMENT_SHADER = "hands_support";
   private static final String ITEM_DOWNSAMPLE_PIPELINE_FRAGMENT_SHADER = "hands_downsample";
   private static final String ITEM_UPSAMPLE_PIPELINE_FRAGMENT_SHADER = "hands_upsample";
   private static final String ITEM_GAUSSIAN_PIPELINE_FRAGMENT_SHADER = "hands_gaussian";
   private static final String ITEM_COLOR_PIPELINE_LOCATION = "rose/hands_item_color";
   private static final String ITEM_COLOR_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String ITEM_COLOR_PIPELINE_FRAGMENT_SHADER = "hands_item_color";
   private static final String ITEM_COLOR_PIPELINE_HAND_SAMPLER_NAME = "HandSampler";
   private static final String ITEM_COLOR_PIPELINE_HAND_MASK_SAMPLER_NAME = "HandMaskSampler";
   private static final String OUTER_GLOW_PIPELINE_LOCATION = "rose/hands_outer_glow";
   private static final String OUTER_GLOW_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String OUTER_GLOW_PIPELINE_FRAGMENT_SHADER = "hands_outer_glow";
   private static final String OUTER_GLOW_PIPELINE_UNIFORM_NAME = "HandsGlow";
   private static final String OUTER_GLOW_PIPELINE_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String OUTER_GLOW_PIPELINE_HAND_MASK_SAMPLER_NAME = "HandMaskSampler";
   private static final String OUTLINE_PIPELINE_LOCATION = "rose/hands_outline";
   private static final String OUTLINE_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String OUTLINE_PIPELINE_FRAGMENT_SHADER = "hands_outline";
   private static final String OUTLINE_PIPELINE_HAND_SAMPLER_NAME = "HandSampler";
   private static final String OUTLINE_PIPELINE_UNIFORM_NAME = "HandsGlow";
   private static final String OUTLINE_PIPELINE_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String OUTLINE_PIPELINE_HAND_MASK_SAMPLER_NAME = "HandMaskSampler";
   private static final String GLOW_OUTLINE_PIPELINE_LOCATION = "rose/hands_glow_outline";
   private static final String GLOW_OUTLINE_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String GLOW_OUTLINE_PIPELINE_FRAGMENT_SHADER = "hands_glow_outline";
   private static final String GLOW_OUTLINE_PIPELINE_HAND_SAMPLER_NAME = "HandSampler";
   private static final String GLOW_OUTLINE_PIPELINE_UNIFORM_NAME = "HandsGlow";
   private static final String GLOW_OUTLINE_PIPELINE_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String GLOW_OUTLINE_PIPELINE_HAND_MASK_SAMPLER_NAME = "HandMaskSampler";
   private static final String FLAME_TRAIL_DISPLACE_PIPELINE_LOCATION = "rose/hands_trail_displace";
   private static final String FLAME_TRAIL_DISPLACE_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String FLAME_TRAIL_DISPLACE_PIPELINE_FRAGMENT_SHADER = "hands_trail_displace";
   private static final String FLAME_TRAIL_DISPLACE_PIPELINE_UNIFORM_NAME = "TrailDisplace";
   private static final String FLAME_TRAIL_DISPLACE_PIPELINE_TRAIL_SAMPLER_NAME = "TrailSampler";
   private static final String ENERGY_TRAIL_DISPLACE_PIPELINE_LOCATION = "rose/hands_trail_energy_displace";
   private static final String ENERGY_TRAIL_DISPLACE_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String ENERGY_TRAIL_DISPLACE_PIPELINE_FRAGMENT_SHADER = "hands_trail_energy_displace";
   private static final String ENERGY_TRAIL_DISPLACE_PIPELINE_UNIFORM_NAME = "TrailDisplace";
   private static final String ENERGY_TRAIL_DISPLACE_PIPELINE_TRAIL_SAMPLER_NAME = "TrailSampler";
   private static final String FLAME_TRAIL_PIPELINE_LOCATION = "rose/hands_trail_flame";
   private static final String FLAME_TRAIL_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String FLAME_TRAIL_PIPELINE_FRAGMENT_SHADER = "hands_trail_flame";
   private static final String FLAME_TRAIL_PIPELINE_UNIFORM_NAME = "TrailFlame";
   private static final String FLAME_TRAIL_PIPELINE_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String ENERGY_TRAIL_PIPELINE_LOCATION = "rose/hands_trail_energy";
   private static final String ENERGY_TRAIL_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String ENERGY_TRAIL_PIPELINE_FRAGMENT_SHADER = "hands_trail_energy";
   private static final String ENERGY_TRAIL_PIPELINE_UNIFORM_NAME = "TrailEnergy";
   private static final String ENERGY_TRAIL_PIPELINE_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String RIBBON_TRAIL_DISPLACE_PIPELINE_LOCATION = "rose/hands_trail_ribbon_displace";
   private static final String RIBBON_TRAIL_DISPLACE_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String RIBBON_TRAIL_DISPLACE_PIPELINE_FRAGMENT_SHADER = "hands_trail_ribbon_displace";
   private static final String RIBBON_TRAIL_DISPLACE_PIPELINE_UNIFORM_NAME = "TrailDisplace";
   private static final String RIBBON_TRAIL_DISPLACE_PIPELINE_TRAIL_SAMPLER_NAME = "TrailSampler";
   private static final String RIBBON_TRAIL_PIPELINE_LOCATION = "rose/hands_trail_ribbon";
   private static final String RIBBON_TRAIL_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String RIBBON_TRAIL_PIPELINE_FRAGMENT_SHADER = "hands_trail_ribbon";
   private static final String RIBBON_TRAIL_PIPELINE_UNIFORM_NAME = "TrailRibbon";
   private static final String RIBBON_TRAIL_PIPELINE_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String TRAIL_BLIT_PIPELINE_LOCATION = "rose/hands_trail_blit";
   private static final String TRAIL_BLIT_PIPELINE_VERTEX_SHADER = "hands_fullscreen";
   private static final String TRAIL_BLIT_PIPELINE_FRAGMENT_SHADER = "hands_trail_blit";
   private static final String TRAIL_BLIT_PIPELINE_TRAIL_SAMPLER_NAME = "TrailSampler";
   private static final String MASK_BLOOM_TARGET_LABEL_PREFIX = "hands-bloom";
   private static final String ITEM_BLOOM_TARGET_LABEL_PREFIX = "hands-item-bloom";
   private static final String MIRROR_BLUR_TARGET_LABEL_PREFIX = "hands-mirror";
   private static final float OUTER_GLOW_BRIGHTNESS_EPSILON = 0.001F;
   private static final float INNER_GLOW_STRENGTH_EPSILON = 0.001F;
   private static final float OUTLINE_WIDTH_EPSILON = 0.001F;
   private static final float MIRROR_BLEND_EPSILON = 0.001F;
   private static final float MIRROR_BLUR_BASE_SIGMA = 0.6F;
   private static final float MIRROR_BLUR_STRENGTH_FACTOR = 3.8F;
   private static final float MIRROR_BLUR_SIGMA_SCALE = 0.25F;
   private static final String HAND_SUPPORT_TARGET_LABEL = "hands-support";
   private static final float MAX_GLOW_BLUR_STRENGTH = 5.0F;
   private static final float MIN_ENERGY_BLUR_STRENGTH = 3.0F;
   private static final float GLOW_BLUR_BASE_SIGMA = 1.5F;
   private static final float GLOW_BLUR_SIGMA_FACTOR = 2.5F;
   private static final float OUTLINE_BLUR_SIGMA = 0.75F;
   private static final double HALF_FOV_FACTOR = 0.5;
   private static final String HANDS_UNIFORM_BUFFER_LABEL = "handsData";
   private static final float SHADER_OPACITY_EPSILON = 0.001F;
   private static final String COMPOSITE_HANDS_HAND_SAMPLER_NAME = "HandSampler";
   private static final String NONE_COMPOSITE_HAND_MASK_SAMPLER_NAME = "HandMaskSampler";
   private static final String COMPOSITE_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String COMPOSITE_HANDS_HAND_SUPPORT_SAMPLER_NAME = "HandSupportSampler";
   private static final String COMPOSITE_HANDS_HAND_DEPTH_SAMPLER_NAME = "HandDepthSampler";
   private static final String EFFECT_COMPOSITE_HAND_MASK_SAMPLER_NAME = "HandMaskSampler";
   private static final String COMPOSITE_HANDS_BLURRED_SCENE_SAMPLER_NAME = "BlurredSceneSampler";
   private static final String COMPOSITE_HANDS_UNIFORM_NAME = "HandsData";
   private static final String RENDER_HAND_MASK_HAND_SAMPLER_NAME = "HandSampler";
   private static final String RENDER_HAND_MASK_HAND_DEPTH_SAMPLER_NAME = "HandDepthSampler";
   private static final float SINGLE_LEVEL_BLUR_SKIP_SIGMA_LIMIT = 0.75F;
   private static final String RENDER_ITEM_COLOR_HAND_SAMPLER_NAME = "HandSampler";
   private static final String RENDER_ITEM_COLOR_HAND_MASK_SAMPLER_NAME = "HandMaskSampler";
   private static final String BLUR_PASS_SOURCE_SAMPLER_NAME = "Sampler0";
   private static final String RENDER_BLUR_PASS_UNIFORM_NAME = "HandsBlur";
   private static final String OUTLINE_UNIFORM_BUFFER_LABEL = "handsOutlineData";
   private static final String GLOW_UNIFORM_BUFFER_LABEL = "handsGlowData";
   private static final String RENDER_GLOW_OR_OUTLINE_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String RENDER_GLOW_OR_OUTLINE_HAND_MASK_SAMPLER_NAME = "HandMaskSampler";
   private static final String RENDER_GLOW_OR_OUTLINE_HAND_SAMPLER_NAME = "HandSampler";
   private static final String RENDER_GLOW_OR_OUTLINE_UNIFORM_NAME = "HandsGlow";
   private static final float MAX_TRAIL_DELTA_SECONDS = 0.1F;
   private static final float TRAIL_DELTA_NANOSECONDS_PER_SECOND = 1.0E9F;
   private static final float INITIAL_TRAIL_DELTA_SECONDS = 0.016666668F;
   private static final long TRAIL_CLOCK_PERIOD_NANOS = 100000000000L;
   private static final float TRAIL_CLOCK_NANOSECONDS_PER_SECOND = 1.0E9F;
   private static final float TRAIL_SWAY_ANGULAR_FREQUENCY = (float) (Math.PI * 8.0 / 5.0);
   private static final float ENERGY_RISE_SPEED_FACTOR = 0.035F;
   private static final float ENERGY_SWAY_AMPLITUDE_FACTOR = 0.018F;
   private static final float MAX_YAW_DELTA_DEGREES = 180.0F;
   private static final float YAW_WRAP_SUBTRACT_DEGREES = 360.0F;
   private static final float MIN_YAW_DELTA_DEGREES = -180.0F;
   private static final float YAW_WRAP_ADD_DEGREES = 360.0F;
   private static final float MIN_CAMERA_UV_SHIFT_X = -0.25F;
   private static final float MAX_CAMERA_UV_SHIFT_X = 0.25F;
   private static final float MIN_CAMERA_UV_SHIFT_Y = -0.25F;
   private static final float MAX_CAMERA_UV_SHIFT_Y = 0.25F;
   private static final float RIBBON_DECAY_LENGTH_FACTOR = 0.045F;
   private static final float MIN_RIBBON_DECAY_LENGTH = 0.15F;
   private static final float ENERGY_DECAY_LENGTH_FACTOR = 0.048F;
   private static final float MIN_ENERGY_DECAY_LENGTH = 0.4F;
   private static final float SWING_DECAY_BOOST_FACTOR = 0.012F;
   private static final float SWING_IMPULSE_DURATION_SECONDS = 0.45F;
   private static final float ENERGY_TURBULENCE_FACTOR = 0.32F;
   private static final String RENDER_TRAIL_BUFFER_LABEL = "handsTrailDisplaceData";
   private static final String TRAIL_DISPLACE_TRAIL_SAMPLER_NAME = "TrailSampler";
   private static final String RENDER_TRAIL_UNIFORM_NAME = "TrailDisplace";
   private static final String TRAIL_BLIT_TRAIL_SAMPLER_NAME = "TrailSampler";
   private static final String RENDER_FLAME_TRAIL_BUFFER_LABEL = "handsTrailFlameData";
   private static final String RENDER_FLAME_TRAIL_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String RENDER_FLAME_TRAIL_UNIFORM_NAME = "TrailFlame";
   private static final String RENDER_ENERGY_TRAIL_BUFFER_LABEL = "handsTrailEnergyData";
   private static final String RENDER_ENERGY_TRAIL_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String RENDER_ENERGY_TRAIL_UNIFORM_NAME = "TrailEnergy";
   private static final float RIBBON_BRIGHTNESS_EPSILON = 0.001F;
   private static final String RENDER_RIBBON_TRAIL_BUFFER_LABEL = "handsTrailRibbonData";
   private static final String RENDER_RIBBON_TRAIL_BLOOM_SAMPLER_NAME = "BloomSampler";
   private static final String RENDER_RIBBON_TRAIL_UNIFORM_NAME = "TrailRibbon";
   private static final float FALLBACK_EATING_RIBBON_LENGTH = 0.15F;
   private static final float FALLBACK_SWING_RIBBON_LENGTH = 0.15F;
   private static final float RIBBON_SMOOTHING_RATE = 16.0F;
   private static final String TRAIL_READ_TARGET_LABEL = "hands-trail-a";
   private static final String TRAIL_WRITE_TARGET_LABEL = "hands-trail-b";
   private static final String HAND_MASK_TARGET_LABEL = "hands-mask";
   private static final float RED_CHANNEL_NORMALIZATION_DIVISOR = 255.0F;
   private static final float GREEN_CHANNEL_NORMALIZATION_DIVISOR = 255.0F;
   private static final float BLUE_CHANNEL_NORMALIZATION_DIVISOR = 255.0F;
   private static final float ALPHA_CHANNEL_NORMALIZATION_DIVISOR = 255.0F;
   private static final String RIBBON_TRAIL_PASS_LABEL = "handsTrailRibbon";
   private static final String ENERGY_TRAIL_PASS_LABEL = "handsTrailEnergy";
   private static final String FLAME_TRAIL_PASS_LABEL = "handsTrailFlame";
   private static final String TRAIL_BLIT_PASS_LABEL = "handsTrailBlit";
   private static final String TRAIL_DISPLACE_PASS_LABEL = "handsTrailDisplace";
   private static final String GLOW_OUTLINE_PASS_LABEL = "handsGlowOutline";
   private static final String OUTLINE_PASS_LABEL = "handsOutline";
   private static final String OUTER_GLOW_PASS_LABEL = "handsOuterGlow";
   private static final String BLUR_PASS_LABEL = "handsBlur";
   private static final String ITEM_COLOR_PASS_LABEL = "handsItemColor";
   private static final String HAND_MASK_PASS_LABEL = "handsMask";
   private static final String HANDS_COMPOSITE_PASS_LABEL = "handsComposite";

   private void clearTrailTargets(CommandEncoder commandEncoder) {
      if (this.trailReadTarget != null && this.trailWriteTarget != null) {
         commandEncoder.clearColorTexture(this.trailReadTarget.getColorTexture(), new Vector4f(0.0F));
         commandEncoder.clearColorTexture(this.trailWriteTarget.getColorTexture(), new Vector4f(0.0F));
      }
   }

   private void ensureTrailTargets(int width, int height, CommandEncoder commandEncoder) {
      if (this.trailReadTarget == null) {
         this.trailReadTarget = new TextureTarget(TRAIL_READ_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
         this.trailWriteTarget = new TextureTarget(TRAIL_WRITE_TARGET_LABEL, width, height, false, GpuFormat.RGBA8_UNORM);
         commandEncoder.clearColorTexture(this.trailReadTarget.getColorTexture(), new Vector4f(0.0F));
         commandEncoder.clearColorTexture(this.trailWriteTarget.getColorTexture(), new Vector4f(0.0F));
      } else if (this.trailReadTarget.width != width || this.trailReadTarget.height != height) {
         this.trailReadTarget.resize(width, height);
         this.trailWriteTarget.resize(width, height);
         commandEncoder.clearColorTexture(this.trailReadTarget.getColorTexture(), new Vector4f(0.0F));
         commandEncoder.clearColorTexture(this.trailWriteTarget.getColorTexture(), new Vector4f(0.0F));
      }
   }

   private void renderBlurPass(RenderTarget source, RenderTarget destination, RenderPipeline pipeline, GpuBuffer blurUniformBuffer, CommandEncoder commandEncoder) {
      try (RenderPass renderPass = commandEncoder.createRenderPass(() -> BLUR_PASS_LABEL, destination.getColorTextureView(), Optional.empty())) {
         renderPass.setPipeline(pipeline);
         renderPass.bindTexture(BLUR_PASS_SOURCE_SAMPLER_NAME, source.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         if (blurUniformBuffer != null) {
            renderPass.setUniform(RENDER_BLUR_PASS_UNIFORM_NAME, blurUniformBuffer);
         }

         renderPass.draw(3, 1, 0, 0);
      }
   }

   private void renderEnergyTrail(CommandEncoder commandEncoder, RenderTarget bloomSource, Hands hands, float deltaSeconds, float timeSeconds) {
      try (MemoryStack stack = MemoryStack.stackPush()) {
         Std140Builder uniformBuilder = Std140Builder.onStack(stack, 64).putVec4(hands.getGlowBrightness(), hands.isItemColorsEnabled() ? 1.0F : 0.0F, 0.0F, deltaSeconds).putVec4(timeSeconds, hands.getEnergyTrailBrightness(), hands.getEnergyTrailPulse(), 0.0F);
         putArgbColor(uniformBuilder, hands.getPrimaryGlowColorArgb());
         putArgbColor(uniformBuilder, hands.getSecondaryGlowColorArgb());
         ByteBuffer uniformData = uniformBuilder.get();
         this.energyTrailUniformBuffer = this.ensureUniformBuffer(this.energyTrailUniformBuffer, 64, RENDER_ENERGY_TRAIL_BUFFER_LABEL);
         commandEncoder.writeToBuffer(this.energyTrailUniformBuffer.slice(), uniformData);

         try (RenderPass renderPass = commandEncoder.createRenderPass(() -> ENERGY_TRAIL_PASS_LABEL, this.trailWriteTarget.getColorTextureView(), Optional.empty())) {
            renderPass.setPipeline(this.energyTrailPipeline);
            renderPass.bindTexture(RENDER_ENERGY_TRAIL_BLOOM_SAMPLER_NAME, bloomSource.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            renderPass.setUniform(RENDER_ENERGY_TRAIL_UNIFORM_NAME, this.energyTrailUniformBuffer);
            renderPass.draw(3, 1, 0, 0);
         }
      }
   }

   private RenderTarget blurSource(RenderTarget source, HandsItemBloom.BlurChain chain, int downsampleLevels, float sigma, boolean upsampleToFirstLevel, RenderTarget itemColorSource, CommandEncoder commandEncoder) {
      RenderTarget currentSource = source;

      for (int levelIndex = 0; levelIndex < downsampleLevels; levelIndex++) {
         int levelWidth = Math.max(2, (currentSource.width + 1) / 2);
         int levelHeight = Math.max(2, (currentSource.height + 1) / 2);
         if (chain.downsampleTargets.size() <= levelIndex) {
            chain.downsampleTargets.add(null);
         }

         RenderTarget levelTarget = this.ensureTarget(chain.downsampleTargets.get(levelIndex), chain.labelPrefix + "-" + levelIndex, levelWidth, levelHeight, chain.colorFormat);
         chain.downsampleTargets.set(levelIndex, levelTarget);
         if (itemColorSource != null && levelIndex == 0) {
            this.renderItemColor(itemColorSource, levelTarget, commandEncoder);
         } else {
            this.renderBlurPass(currentSource, levelTarget, chain.maskFormat ? this.maskDownsamplePipeline : (chain == this.itemBloomChain ? this.itemDownsamplePipeline : this.sceneDownsamplePipeline), null, commandEncoder);
         }

         currentSource = levelTarget;
      }

      if (chain != this.mirrorSceneBlurChain && downsampleLevels == 1 && sigma <= SINGLE_LEVEL_BLUR_SKIP_SIGMA_LIMIT) {
         return currentSource;
      } else {
         chain.gaussianScratchTarget = this.ensureTarget(chain.gaussianScratchTarget, chain.labelPrefix + "-scratch", currentSource.width, currentSource.height, chain.colorFormat);
         this.updateGaussianUniforms(chain, currentSource.width, currentSource.height, sigma, commandEncoder);
         RenderPipeline gaussianPipeline = chain.maskFormat ? this.maskGaussianPipeline : (chain == this.itemBloomChain ? this.itemGaussianPipeline : this.sceneGaussianPipeline);
         this.renderBlurPass(currentSource, chain.gaussianScratchTarget, gaussianPipeline, chain.horizontalUniformBuffer, commandEncoder);
         this.renderBlurPass(chain.gaussianScratchTarget, currentSource, gaussianPipeline, chain.verticalUniformBuffer, commandEncoder);
         if (upsampleToFirstLevel && downsampleLevels > 1) {
            RenderTarget firstLevelTarget = chain.downsampleTargets.getFirst();
            this.renderBlurPass(currentSource, firstLevelTarget, chain == this.itemBloomChain ? this.itemUpsamplePipeline : this.maskUpsamplePipeline, null, commandEncoder);
            return firstLevelTarget;
         } else {
            return currentSource;
         }
      }
   }

   private static RenderPipeline createBlurPipeline(String shaderName, GpuFormat colorFormat, boolean usesBlurUniforms) {
      boolean maskFormat = colorFormat == GpuFormat.R8_UNORM;
      RenderPipelineBuilder pipelineBuilder = RenderPipelineBuilder.builder()
         .withLocation("rose/" + shaderName + (maskFormat ? MASK_PIPELINE_LOCATION_SUFFIX : (colorFormat == GpuFormat.RGBA16_FLOAT ? ITEM_COLOR_PIPELINE_LOCATION_SUFFIX : SCENE_PIPELINE_LOCATION_SUFFIX)))
         .withColorTargetFormat(colorFormat)
         .withVertexShader(ClientResourceIds.getCoreId(CREATE_BLUR_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(shaderName))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withSampler(BLUR_PIPELINE_SOURCE_SAMPLER_NAME);
      if (maskFormat) {
         pipelineBuilder.withShaderDefine(CREATE_BLUR_PIPELINE_SHADER_DEFINE);
      }

      if (usesBlurUniforms) {
         pipelineBuilder.withUniform(CREATE_BLUR_PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER);
      }

      return pipelineBuilder.build();
   }

   public void dispose() {
      if (this.handMaskTarget != null) {
         this.handMaskTarget.destroyBuffers();
      }

      if (this.handSupportTarget != null) {
         this.handSupportTarget.destroyBuffers();
      }

      this.handMaskTarget = null;
      this.handSupportTarget = null;
      this.destroyBlurChain(this.maskBloomChain);
      this.destroyBlurChain(this.itemBloomChain);
      this.destroyBlurChain(this.mirrorSceneBlurChain);
      if (this.trailReadTarget != null) {
         this.trailReadTarget.destroyBuffers();
      }

      if (this.trailWriteTarget != null) {
         this.trailWriteTarget.destroyBuffers();
      }

      this.trailReadTarget = null;
      this.trailWriteTarget = null;
      this.resetTrailAnimation();
      this.previousTrailStyleId = -1;
      this.previousItemColorsEnabled = false;
      this.glowUniformStateCache.invalidate();
      this.outlineUniformStateCache.invalidate();
      this.closeBuffer(this.handsUniformBuffer);
      this.closeBuffer(this.glowUniformBuffer);
      this.closeBuffer(this.outlineUniformBuffer);
      this.closeBuffer(this.trailDisplacementUniformBuffer);
      this.closeBuffer(this.flameTrailUniformBuffer);
      this.closeBuffer(this.energyTrailUniformBuffer);
      this.closeBuffer(this.ribbonTrailUniformBuffer);
      this.handsUniformBuffer = null;
      this.glowUniformBuffer = null;
      this.outlineUniformBuffer = null;
      this.trailDisplacementUniformBuffer = null;
      this.flameTrailUniformBuffer = null;
      this.energyTrailUniformBuffer = null;
      this.ribbonTrailUniformBuffer = null;
   }

   private static Std140Builder putArgbColor(Std140Builder builder, int argbColor) {
      return builder.putVec4((argbColor >>> 16 & 0xFF) / RED_CHANNEL_NORMALIZATION_DIVISOR, (argbColor >>> 8 & 0xFF) / GREEN_CHANNEL_NORMALIZATION_DIVISOR, (argbColor & 0xFF) / BLUE_CHANNEL_NORMALIZATION_DIVISOR, (argbColor >>> 24 & 0xFF) / ALPHA_CHANNEL_NORMALIZATION_DIVISOR);
   }

   private RenderTarget blurSource(RenderTarget source, HandsItemBloom.BlurChain chain, int downsampleLevels, float sigma, boolean upsampleToFirstLevel, CommandEncoder commandEncoder) {
      return this.blurSource(source, chain, downsampleLevels, sigma, upsampleToFirstLevel, null, commandEncoder);
   }

   private static RenderPipeline createBlurPipeline(String shaderName, boolean maskFormat, boolean usesBlurUniforms) {
      return createBlurPipeline(shaderName, maskFormat ? GpuFormat.R8_UNORM : GpuFormat.RGBA8_UNORM, usesBlurUniforms);
   }

   private void ensureHandMaskTarget(int width, int height) {
      if (this.handMaskTarget == null) {
         this.handMaskTarget = new HandsItemBloom.SingleChannelTarget(HAND_MASK_TARGET_LABEL, width, height);
      } else if (this.handMaskTarget.width != width || this.handMaskTarget.height != height) {
         this.handMaskTarget.resize(width, height);
         this.glowUniformStateCache.invalidate();
         this.outlineUniformStateCache.invalidate();
      }
   }

   private void updateRibbonAnimation(Hands hands, Minecraft minecraft, float deltaSeconds) {
      float desiredRibbonLength = hands.getRibbonTrailLength();
      float desiredRibbonStrength = 1.0F;
      boolean eatingOrDrinking = false;
      if (minecraft.player != null && minecraft.player.isUsingItem()) {
         ItemUseAnimation useAnimation = minecraft.player.getUseItem().getUseAnimation();
         eatingOrDrinking = useAnimation == ItemUseAnimation.EAT || useAnimation == ItemUseAnimation.DRINK;
      }

      if (eatingOrDrinking) {
         desiredRibbonLength = hands.isEatingRibbonsEnabled() ? hands.getEatingRibbonLength() : FALLBACK_EATING_RIBBON_LENGTH;
         desiredRibbonStrength = hands.isEatingRibbonsEnabled() ? hands.getEatingRibbonStrength() : 0.0F;
      } else if (minecraft.player != null && minecraft.player.swinging) {
         desiredRibbonLength = hands.isSwingRibbonsEnabled() ? hands.getSwingRibbonLength() : FALLBACK_SWING_RIBBON_LENGTH;
         desiredRibbonStrength = hands.isSwingRibbonsEnabled() ? hands.getSwingRibbonStrength() : 0.0F;
      }

      float smoothingFactor = this.ribbonStateInitialized ? (float)(-Math.expm1(-deltaSeconds * RIBBON_SMOOTHING_RATE)) : 1.0F;
      this.smoothedRibbonLength = this.smoothedRibbonLength + (desiredRibbonLength - this.smoothedRibbonLength) * smoothingFactor;
      this.smoothedRibbonStrength = this.smoothedRibbonStrength + (desiredRibbonStrength - this.smoothedRibbonStrength) * smoothingFactor;
      this.ribbonStateInitialized = true;
      this.ribbonAnimationTime = this.ribbonAnimationTime + deltaSeconds * hands.getRibbonWaveSpeed();
      this.ribbonFrameIndex = this.ribbonFrameIndex + 1 & 4095;
   }

   private void renderHandMask(RenderTarget handSource, CommandEncoder commandEncoder) {
      try (RenderPass renderPass = commandEncoder.createRenderPass(() -> HAND_MASK_PASS_LABEL, this.handMaskTarget.getColorTextureView(), Optional.empty())) {
         renderPass.setPipeline(this.handMaskPipeline);
         renderPass.bindTexture(RENDER_HAND_MASK_HAND_SAMPLER_NAME, handSource.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         renderPass.bindTexture(RENDER_HAND_MASK_HAND_DEPTH_SAMPLER_NAME, handSource.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         renderPass.draw(3, 1, 0, 0);
      }
   }

   private RenderTarget ensureTarget(RenderTarget existingTarget, String targetLabel, int width, int height, GpuFormat colorFormat) {
      if (existingTarget == null) {
         return colorFormat == GpuFormat.R8_UNORM ? new HandsItemBloom.SingleChannelTarget(targetLabel, width, height) : new TextureTarget(targetLabel, width, height, false, colorFormat);
      }

      if (existingTarget.width != width || existingTarget.height != height) {
         existingTarget.resize(width, height);
      }

      return existingTarget;
   }

   private void renderFlameTrail(CommandEncoder commandEncoder, RenderTarget bloomSource, Hands hands, float deltaSeconds) {
      try (MemoryStack stack = MemoryStack.stackPush()) {
         Std140Builder uniformBuilder = Std140Builder.onStack(stack, 48).putVec4(hands.getGlowBrightness(), hands.isItemColorsEnabled() ? 1.0F : 0.0F, 0.0F, deltaSeconds);
         putArgbColor(uniformBuilder, hands.getPrimaryGlowColorArgb());
         putArgbColor(uniformBuilder, hands.getSecondaryGlowColorArgb());
         ByteBuffer uniformData = uniformBuilder.get();
         this.flameTrailUniformBuffer = this.ensureUniformBuffer(this.flameTrailUniformBuffer, 48, RENDER_FLAME_TRAIL_BUFFER_LABEL);
         commandEncoder.writeToBuffer(this.flameTrailUniformBuffer.slice(), uniformData);

         try (RenderPass renderPass = commandEncoder.createRenderPass(() -> FLAME_TRAIL_PASS_LABEL, this.trailWriteTarget.getColorTextureView(), Optional.empty())) {
            renderPass.setPipeline(this.flameTrailPipeline);
            renderPass.bindTexture(RENDER_FLAME_TRAIL_BLOOM_SAMPLER_NAME, bloomSource.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            renderPass.setUniform(RENDER_FLAME_TRAIL_UNIFORM_NAME, this.flameTrailUniformBuffer);
            renderPass.draw(3, 1, 0, 0);
         }
      }
   }

   private void renderTrail(RenderTarget bloomSource, RenderTarget destination, Hands hands, CommandEncoder commandEncoder) {
      this.ensureTrailTargets(bloomSource.width, bloomSource.height, commandEncoder);
      boolean energyTrail = hands.isEnergyTrailEnabled();
      boolean ribbonTrail = hands.isRibbonTrailEnabled();
      if (this.previousTrailStyleId != hands.getTrailStyleId()) {
         this.clearTrailTargets(commandEncoder);
         this.previousTrailStyleId = hands.getTrailStyleId();
         this.resetTrailAnimation();
      }

      long nowNanos = System.nanoTime();
      float boundedDeltaSeconds = this.previousTrailTimeNanos > 0L ? Math.min(MAX_TRAIL_DELTA_SECONDS, (float)(nowNanos - this.previousTrailTimeNanos) / TRAIL_DELTA_NANOSECONDS_PER_SECOND) : INITIAL_TRAIL_DELTA_SECONDS;
      this.previousTrailTimeNanos = nowNanos;
      float deltaSeconds = boundedDeltaSeconds;
      float animationTimeSeconds = (float)(nowNanos % TRAIL_CLOCK_PERIOD_NANOS) / TRAIL_CLOCK_NANOSECONDS_PER_SECOND;
      Minecraft minecraft = Minecraft.getInstance();
      if (ribbonTrail) {
         this.updateRibbonAnimation(hands, minecraft, deltaSeconds);
         animationTimeSeconds = (float)this.ribbonAnimationTime;
      }

      float swayAngularFrequency = TRAIL_SWAY_ANGULAR_FREQUENCY;
      float energyMotion = hands.getEnergyTrailMotion();
      float riseSpeed = ribbonTrail ? hands.getRibbonRise() : (energyTrail ? ENERGY_RISE_SPEED_FACTOR * energyMotion : hands.getFlameTrailRise());
      float swayAmplitude = ribbonTrail ? 0.0F : (energyTrail ? ENERGY_SWAY_AMPLITUDE_FACTOR * energyMotion : hands.getFlameTrailSway());
      float cameraTrailStrength = ribbonTrail ? hands.getRibbonCameraTrailStrength() : (energyTrail ? hands.getEnergyCameraTrailStrength() : hands.getFlameCameraTrailStrength());
      float verticalUvShift = riseSpeed * deltaSeconds;
      float horizontalUvSway = ((float)Math.sin(animationTimeSeconds * swayAngularFrequency) - (float)Math.sin((animationTimeSeconds - deltaSeconds) * swayAngularFrequency)) * swayAmplitude;
      float cameraUvShiftX = 0.0F;
      float cameraUvShiftY = 0.0F;
      Camera camera = minecraft.gameRenderer != null ? minecraft.gameRenderer.mainCamera() : null;
      if (camera != null) {
         float cameraYaw = camera.yRot();
         float cameraPitch = camera.xRot();
         if (this.cameraAnglesReady) {
            float yawDelta = cameraYaw - this.previousCameraYaw;

            while (yawDelta > MAX_YAW_DELTA_DEGREES) {
               yawDelta -= YAW_WRAP_SUBTRACT_DEGREES;
            }

            while (yawDelta < MIN_YAW_DELTA_DEGREES) {
               yawDelta += YAW_WRAP_ADD_DEGREES;
            }

            float pitchDelta = cameraPitch - this.previousCameraPitch;
            float fovDegrees = Math.max(1.0F, minecraft.options.fov().get().intValue());
            cameraUvShiftX = Math.clamp(-(yawDelta / fovDegrees) * cameraTrailStrength, MIN_CAMERA_UV_SHIFT_X, MAX_CAMERA_UV_SHIFT_X);
            cameraUvShiftY = Math.clamp(pitchDelta / fovDegrees * cameraTrailStrength, MIN_CAMERA_UV_SHIFT_Y, MAX_CAMERA_UV_SHIFT_Y);
         }

         this.previousCameraYaw = cameraYaw;
         this.previousCameraPitch = cameraPitch;
         this.cameraAnglesReady = true;
      }

      boolean swinging = minecraft.player != null && minecraft.player.swinging;
      boolean swingImpulseTriggered = hands.isFlameTrailEnabled() && hands.isSwingDecayBoostEnabled() && swinging && !this.previousSwinging;
      this.previousSwinging = swinging;
      if (swingImpulseTriggered) {
         this.swingDecayImpulse = 1.0F;
      }

      float decayRate = (ribbonTrail ? RIBBON_DECAY_LENGTH_FACTOR / Math.max(MIN_RIBBON_DECAY_LENGTH, this.smoothedRibbonLength) : (energyTrail ? ENERGY_DECAY_LENGTH_FACTOR / Math.max(MIN_ENERGY_DECAY_LENGTH, hands.getEnergyTrailLength()) : hands.getFlameTrailDecay())) + this.swingDecayImpulse * hands.getSwingDecayBoostStrength() * SWING_DECAY_BOOST_FACTOR;
      this.swingDecayImpulse = Math.max(0.0F, this.swingDecayImpulse - deltaSeconds / SWING_IMPULSE_DURATION_SECONDS);
      float turbulence = ribbonTrail ? hands.getRibbonBend() : (energyTrail ? ENERGY_TURBULENCE_FACTOR * energyMotion : hands.getFlameTrailTurbulence());
      float flicker = !ribbonTrail && !energyTrail ? hands.getFlameTrailFlicker() : 0.0F;

      try (MemoryStack stack = MemoryStack.stackPush()) {
         ByteBuffer uniformData = Std140Builder.onStack(stack, 48)
            .putVec2(horizontalUvSway + cameraUvShiftX, verticalUvShift + cameraUvShiftY)
            .putVec2(this.trailReadTarget.width, this.trailReadTarget.height)
            .putVec4(decayRate, animationTimeSeconds, deltaSeconds, turbulence)
            .putVec4(ribbonTrail ? hands.getRibbonWaveSpeed() : flicker, ribbonTrail ? this.ribbonFrameIndex : 0.0F, 0.0F, 0.0F)
            .get();
         this.trailDisplacementUniformBuffer = this.ensureUniformBuffer(this.trailDisplacementUniformBuffer, 48, RENDER_TRAIL_BUFFER_LABEL);
         commandEncoder.writeToBuffer(this.trailDisplacementUniformBuffer.slice(), uniformData);

         try (RenderPass displacePass = commandEncoder.createRenderPass(() -> TRAIL_DISPLACE_PASS_LABEL, this.trailWriteTarget.getColorTextureView(), Optional.of(new Vector4f(0.0F)))) {
            displacePass.setPipeline(ribbonTrail ? this.ribbonTrailDisplacePipeline : (energyTrail ? this.energyTrailDisplacePipeline : this.flameTrailDisplacePipeline));
            displacePass.bindTexture(TRAIL_DISPLACE_TRAIL_SAMPLER_NAME, this.trailReadTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            displacePass.setUniform(RENDER_TRAIL_UNIFORM_NAME, this.trailDisplacementUniformBuffer);
            displacePass.draw(3, 1, 0, 0);
         }
      }

      if (ribbonTrail) {
         this.renderRibbonTrail(commandEncoder, bloomSource, hands, deltaSeconds, animationTimeSeconds);
      } else if (energyTrail) {
         this.renderEnergyTrail(commandEncoder, bloomSource, hands, deltaSeconds, animationTimeSeconds);
      } else {
         this.renderFlameTrail(commandEncoder, bloomSource, hands, deltaSeconds);
      }

      try (RenderPass blitPass = commandEncoder.createRenderPass(() -> TRAIL_BLIT_PASS_LABEL, destination.getColorTextureView(), Optional.empty())) {
         blitPass.setPipeline(this.trailBlitPipeline);
         blitPass.bindTexture(TRAIL_BLIT_TRAIL_SAMPLER_NAME, this.trailWriteTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         blitPass.draw(3, 1, 0, 0);
      }

      TextureTarget previousReadTarget = this.trailReadTarget;
      this.trailReadTarget = this.trailWriteTarget;
      this.trailWriteTarget = previousReadTarget;
   }

   private void destroyBlurChain(HandsItemBloom.BlurChain chain) {
      for (RenderTarget levelTarget : chain.downsampleTargets) {
         levelTarget.destroyBuffers();
      }

      chain.downsampleTargets.clear();
      if (chain.gaussianScratchTarget != null) {
         chain.gaussianScratchTarget.destroyBuffers();
      }

      chain.gaussianScratchTarget = null;
      this.closeBuffer(chain.horizontalUniformBuffer);
      this.closeBuffer(chain.verticalUniformBuffer);
      chain.horizontalUniformBuffer = null;
      chain.verticalUniformBuffer = null;
      chain.cachedSigma = Float.NaN;
   }

   private void resetTrailAnimation() {
      this.previousTrailTimeNanos = 0L;
      this.swingDecayImpulse = 0.0F;
      this.previousSwinging = false;
      this.cameraAnglesReady = false;
      this.ribbonAnimationTime = 0.0;
      this.ribbonFrameIndex = 0;
      this.ribbonStateInitialized = false;
   }

   private void closeBuffer(GpuBuffer buffer) {
      if (buffer != null && !buffer.isClosed()) {
         buffer.close();
      }
   }

   public void compositeHands(RenderTarget handSource, RenderTarget destination, Hands hands) {
      CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
      if (this.previousItemColorsEnabled != hands.isItemColorsEnabled()) {
         this.previousItemColorsEnabled = hands.isItemColorsEnabled();
         this.glowUniformStateCache.invalidate();
         this.outlineUniformStateCache.invalidate();
         this.clearTrailTargets(commandEncoder);
         this.destroyBlurChain(this.previousItemColorsEnabled ? this.maskBloomChain : this.itemBloomChain);
      }

      boolean mirrorEnabled = hands.isMirrorMode();
      boolean trailEnabled = hands.isTrailEnabled();
      boolean outerGlowEnabled = hands.isGlowEnabled() && hands.getGlowBrightness() > OUTER_GLOW_BRIGHTNESS_EPSILON;
      boolean bloomRequired = hands.isGlowEnabled() && (outerGlowEnabled || hands.getInnerGlowStrength() > INNER_GLOW_STRENGTH_EPSILON || trailEnabled);
      int outlineColorArgb = hands.getOutlineColorArgb();
      boolean outlineEnabled = hands.isOutlineEnabled() && hands.getOutlineWidth() > OUTLINE_WIDTH_EPSILON && outlineColorArgb >>> 24 != 0;
      boolean combinedGlowOutline = outerGlowEnabled && outlineEnabled && outlineColorArgb >>> 24 == 255;
      RenderTarget mirrorScene = handSource;
      if (mirrorEnabled && hands.getMirrorBlend() > MIRROR_BLEND_EPSILON) {
         mirrorScene = this.blurSource(destination, this.mirrorSceneBlurChain, 2, (MIRROR_BLUR_BASE_SIGMA + hands.getMirrorBlurStrength() * MIRROR_BLUR_STRENGTH_FACTOR) * MIRROR_BLUR_SIGMA_SCALE, false, commandEncoder);
      }

      this.ensureHandMaskTarget(handSource.width, handSource.height);
      this.renderHandMask(handSource, commandEncoder);
      boolean noneMode = hands.isNoneMode();
      if (!noneMode) {
         this.handSupportTarget = this.ensureTarget(this.handSupportTarget, HAND_SUPPORT_TARGET_LABEL, Math.max(2, (handSource.width + 1) / 2), Math.max(2, (handSource.height + 1) / 2), true);
         this.renderBlurPass(this.handMaskTarget, this.handSupportTarget, this.maskSupportPipeline, null, commandEncoder);
      }

      if (!trailEnabled && this.previousTrailStyleId != -1) {
         this.clearTrailTargets(commandEncoder);
         this.previousTrailStyleId = -1;
         this.resetTrailAnimation();
      }

      RenderTarget bloomSource = null;
      if (bloomRequired) {
         float blurStrength = Math.clamp(hands.getGlowBlurStrength(), 1.0F, MAX_GLOW_BLUR_STRENGTH);
         if (hands.isEnergyTrailEnabled()) {
            blurStrength = Math.max(MIN_ENERGY_BLUR_STRENGTH, blurStrength);
         }

         int downsampleLevels = blurStrength <= 1.0F ? 1 : 2;
         float blurSigma = (GLOW_BLUR_BASE_SIGMA + (blurStrength - 1.0F) * GLOW_BLUR_SIGMA_FACTOR) / (1 << downsampleLevels);
         bloomSource = this.blurHands(handSource, hands, downsampleLevels, blurSigma, trailEnabled, commandEncoder);
         if (trailEnabled) {
            this.renderTrail(bloomSource, destination, hands, commandEncoder);
         }

         if (outerGlowEnabled && !combinedGlowOutline) {
            this.renderGlowOrOutline(bloomSource, this.handMaskTarget, handSource, destination, hands, true, false, this.outerGlowPipeline, commandEncoder);
         }
      }

      if (outlineEnabled) {
         if (bloomSource == null) {
            bloomSource = this.blurHands(handSource, hands, 1, OUTLINE_BLUR_SIGMA, false, commandEncoder);
         }

         this.renderGlowOrOutline(bloomSource, this.handMaskTarget, handSource, destination, hands, combinedGlowOutline, true, combinedGlowOutline ? this.glowOutlinePipeline : this.outlinePipeline, commandEncoder);
      }

      try (MemoryStack stack = MemoryStack.stackPush()) {
         float fovDegrees = Minecraft.getInstance().options.fov().get().intValue();
         float tanHalfFov = (float)Math.tan(Math.toRadians(fovDegrees) * HALF_FOV_FACTOR);
         float aspectRatio = (float)destination.width / Math.max(1, destination.height);
         Std140Builder uniformBuilder = Std140Builder.onStack(stack, 240)
            .putVec4(hands.getEffectModeId(), hands.getShaderStyleId(), hands.getElapsedSeconds(), hands.getShaderSpeed())
            .putVec4(hands.getShaderScale(), hands.getShaderGlow(), hands.getShaderOpacity(), hands.getShaderShadingStrength())
            .putVec4(hands.isShaderStarsEnabled() ? 1.0F : 0.0F, hands.isShaderShadingPreserved() ? 1.0F : 0.0F, hands.isRainbowFillEnabled() ? 1.0F : 0.0F, hands.isFillShadingPreserved() ? 1.0F : 0.0F)
            .putVec4(hands.getFillOpacity(), hands.getFillShadingStrength(), hands.getRainbowSpeed(), hands.getRainbowScale())
            .putVec4(hands.getMirrorBlend(), hands.getMirrorDistortion(), bloomRequired ? 1.0F : 0.0F, hands.getGlowBlurStrength())
            .putVec4(hands.getGlowBrightness(), hands.isItemColorsEnabled() ? 1.0F : 0.0F, 0.0F, hands.getInnerGlowStrength())
            .putVec4(aspectRatio, tanHalfFov, hands.getShaderIterationBudget(), hands.getMirrorBlurStrength());
         putArgbColor(uniformBuilder, hands.getPrimaryShaderColorArgb());
         putArgbColor(uniformBuilder, hands.getSecondaryShaderColorArgb());
         putArgbColor(uniformBuilder, hands.getFillColorArgb());
         putArgbColor(uniformBuilder, hands.getMirrorColorArgb());
         putArgbColor(uniformBuilder, hands.getPrimaryGlowColorArgb());
         putArgbColor(uniformBuilder, hands.getSecondaryGlowColorArgb());
         ByteBuffer uniformData = uniformBuilder.putVec2(handSource.width, handSource.height).get();
         this.handsUniformBuffer = this.ensureUniformBuffer(this.handsUniformBuffer, 240, HANDS_UNIFORM_BUFFER_LABEL);
         commandEncoder.writeToBuffer(this.handsUniformBuffer.slice(), uniformData);
      }

      RenderPipeline compositePipeline = noneMode ? this.noneCompositePipeline : this.baseCompositePipeline;
      if (!noneMode && hands.isShaderMode() && hands.getShaderOpacity() > SHADER_OPACITY_EPSILON) {
         int shaderStyleId = Math.clamp(hands.getShaderStyleId(), 0, this.shaderStylePipelines.length - 1);
         if (this.shaderStylePipelines[shaderStyleId] == null) {
            this.shaderStylePipelines[shaderStyleId] = createCompositePipeline(shaderStyleId);
         }

         compositePipeline = this.shaderStylePipelines[shaderStyleId];
      }

      try (RenderPass renderPass = commandEncoder.createRenderPass(() -> HANDS_COMPOSITE_PASS_LABEL, destination.getColorTextureView(), Optional.empty())) {
         RenderSystem.bindDefaultUniforms(renderPass);
         renderPass.bindTexture(COMPOSITE_HANDS_HAND_SAMPLER_NAME, handSource.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         if (noneMode) {
            renderPass.bindTexture(NONE_COMPOSITE_HAND_MASK_SAMPLER_NAME, this.handMaskTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         }

         renderPass.bindTexture(
            COMPOSITE_BLOOM_SAMPLER_NAME, (bloomSource != null ? bloomSource : this.handMaskTarget).getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
         );
         if (!noneMode) {
            renderPass.bindTexture(COMPOSITE_HANDS_HAND_SUPPORT_SAMPLER_NAME, this.handSupportTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            renderPass.bindTexture(COMPOSITE_HANDS_HAND_DEPTH_SAMPLER_NAME, handSource.getDepthTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            renderPass.bindTexture(EFFECT_COMPOSITE_HAND_MASK_SAMPLER_NAME, this.handMaskTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
            renderPass.bindTexture(COMPOSITE_HANDS_BLURRED_SCENE_SAMPLER_NAME, mirrorScene.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         }

         renderPass.setUniform(COMPOSITE_HANDS_UNIFORM_NAME, this.handsUniformBuffer);
         renderPass.setPipeline(compositePipeline);
         renderPass.draw(3, 1, 0, 0);
      }
   }

   private RenderTarget ensureTarget(RenderTarget existingTarget, String targetLabel, int width, int height, boolean maskFormat) {
      return this.ensureTarget(existingTarget, targetLabel, width, height, maskFormat ? GpuFormat.R8_UNORM : GpuFormat.RGBA8_UNORM);
   }

   private static RenderPipeline createCompositePipeline(int shaderStyleId) {
      RenderPipelineBuilder pipelineBuilder = RenderPipelineBuilder.builder()
         .withLocation("rose/hands_composite_" + (shaderStyleId < 0 ? BASE_COMPOSITE_STYLE_LABEL : shaderStyleId))
         .withVertexShader(ClientResourceIds.getCoreId(CREATE_COMPOSITE_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(CREATE_COMPOSITE_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withColorWrite(true, true)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(CREATE_COMPOSITE_PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(CREATE_COMPOSITE_PIPELINE_HAND_SAMPLER_NAME)
         .withSampler(CREATE_COMPOSITE_PIPELINE_HAND_DEPTH_SAMPLER_NAME)
         .withSampler(CREATE_COMPOSITE_PIPELINE_HAND_MASK_SAMPLER_NAME)
         .withSampler(CREATE_COMPOSITE_PIPELINE_HAND_SUPPORT_SAMPLER_NAME)
         .withSampler(CREATE_COMPOSITE_PIPELINE_BLURRED_SCENE_SAMPLER_NAME)
         .withSampler(CREATE_COMPOSITE_PIPELINE_BLOOM_SAMPLER_NAME);
      pipelineBuilder.withShaderDefine(CREATE_COMPOSITE_PIPELINE_SHADER_DEFINE, shaderStyleId);
      return pipelineBuilder.build();
   }

   private void updateGaussianUniforms(HandsItemBloom.BlurChain chain, int width, int height, float sigma, CommandEncoder commandEncoder) {
      boolean buffersReady = chain.horizontalUniformBuffer != null && !chain.horizontalUniformBuffer.isClosed() && chain.verticalUniformBuffer != null && !chain.verticalUniformBuffer.isClosed();
      if (!buffersReady || chain.cachedWidth != width || chain.cachedHeight != height || Float.compare(chain.cachedSigma, sigma) != 0) {
         chain.horizontalUniformBuffer = this.ensureUniformBuffer(chain.horizontalUniformBuffer, 48, chain.labelPrefix + "-horizontal");
         chain.verticalUniformBuffer = this.ensureUniformBuffer(chain.verticalUniformBuffer, 48, chain.labelPrefix + "-vertical");
         float[] gaussianWeights = new float[7];
         float weightSum = 1.0F;
         gaussianWeights[0] = 1.0F;

         for (int sampleIndex = 1; sampleIndex < gaussianWeights.length; sampleIndex++) {
            gaussianWeights[sampleIndex] = (float)Math.exp(-(sampleIndex * sampleIndex) / (2.0F * sigma * sigma));
            weightSum += gaussianWeights[sampleIndex] * 2.0F;
         }

         float[] pairedOffsets = new float[3];
         float[] pairedWeights = new float[3];

         for (int pairIndex = 0; pairIndex < pairedWeights.length; pairIndex++) {
            int firstSampleIndex = pairIndex * 2 + 1;
            float pairWeight = gaussianWeights[firstSampleIndex] + gaussianWeights[firstSampleIndex + 1];
            pairedOffsets[pairIndex] = firstSampleIndex + (pairWeight > 0.0F ? gaussianWeights[firstSampleIndex + 1] / pairWeight : 0.0F);
            pairedWeights[pairIndex] = pairWeight / weightSum;
         }

         try (MemoryStack stack = MemoryStack.stackPush()) {
            for (int axisIndex = 0; axisIndex < 2; axisIndex++) {
               ByteBuffer uniformData = Std140Builder.onStack(stack, 48)
                  .putVec4(axisIndex == 0 ? 1.0F / width : 0.0F, axisIndex == 1 ? 1.0F / height : 0.0F, 0.0F, 0.0F)
                  .putVec4(1.0F / weightSum, pairedWeights[0], pairedWeights[1], pairedWeights[2])
                  .putVec4(pairedOffsets[0], pairedOffsets[1], pairedOffsets[2], 0.0F)
                  .get();
               commandEncoder.writeToBuffer((axisIndex == 0 ? chain.horizontalUniformBuffer : chain.verticalUniformBuffer).slice(), uniformData);
            }
         }

         chain.cachedWidth = width;
         chain.cachedHeight = height;
         chain.cachedSigma = sigma;
      }
   }

   private void renderGlowOrOutline(
      RenderTarget bloomSource,
      RenderTarget handMask,
      RenderTarget handSource,
      RenderTarget destination,
      Hands hands,
      boolean includeGlow,
      boolean includeOutline,
      RenderPipeline pipeline,
      CommandEncoder commandEncoder
   ) {
      float glowBrightness = includeGlow ? hands.getGlowBrightness() : 0.0F;
      int primaryGlowColorArgb = hands.getPrimaryGlowColorArgb();
      int secondaryGlowColorArgb = hands.getSecondaryGlowColorArgb();
      int outlineColorArgb = hands.getOutlineColorArgb();
      float outlineWidth = includeOutline ? hands.getOutlineWidth() : 0.0F;
      GpuBuffer previousUniformBuffer = includeOutline ? this.outlineUniformBuffer : this.glowUniformBuffer;
      GpuBuffer uniformBuffer = this.ensureUniformBuffer(previousUniformBuffer, 80, includeOutline ? OUTLINE_UNIFORM_BUFFER_LABEL : GLOW_UNIFORM_BUFFER_LABEL);
      HandsItemBloom.UniformStateCache uniformCache = includeOutline ? this.outlineUniformStateCache : this.glowUniformStateCache;
      if (uniformBuffer != previousUniformBuffer || !uniformCache.matches(glowBrightness, primaryGlowColorArgb, secondaryGlowColorArgb, outlineColorArgb, handMask.width, handMask.height, outlineWidth, includeOutline)) {
         try (MemoryStack stack = MemoryStack.stackPush()) {
            Std140Builder uniformBuilder = Std140Builder.onStack(stack, 80).putVec4(glowBrightness, hands.isItemColorsEnabled() ? 1.0F : 0.0F, 0.0F, 0.0F);
            putArgbColor(uniformBuilder, primaryGlowColorArgb);
            putArgbColor(uniformBuilder, secondaryGlowColorArgb);
            putArgbColor(uniformBuilder, outlineColorArgb);
            ByteBuffer uniformData = uniformBuilder.putVec4(handMask.width, handMask.height, outlineWidth, includeOutline ? 1.0F : 0.0F).get();
            commandEncoder.writeToBuffer(uniformBuffer.slice(), uniformData);
         }

         uniformCache.update(glowBrightness, primaryGlowColorArgb, secondaryGlowColorArgb, outlineColorArgb, handMask.width, handMask.height, outlineWidth, includeOutline);
      }

      if (includeOutline) {
         this.outlineUniformBuffer = uniformBuffer;
      } else {
         this.glowUniformBuffer = uniformBuffer;
      }

      try (RenderPass renderPass = commandEncoder.createRenderPass(() -> includeGlow && includeOutline ? GLOW_OUTLINE_PASS_LABEL : (includeOutline ? OUTLINE_PASS_LABEL : OUTER_GLOW_PASS_LABEL), destination.getColorTextureView(), Optional.empty())) {
         renderPass.setPipeline(pipeline);
         renderPass.bindTexture(RENDER_GLOW_OR_OUTLINE_BLOOM_SAMPLER_NAME, bloomSource.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
         renderPass.bindTexture(RENDER_GLOW_OR_OUTLINE_HAND_MASK_SAMPLER_NAME, handMask.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         if (includeOutline) {
            renderPass.bindTexture(RENDER_GLOW_OR_OUTLINE_HAND_SAMPLER_NAME, handSource.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         }

         renderPass.setUniform(RENDER_GLOW_OR_OUTLINE_UNIFORM_NAME, uniformBuffer);
         renderPass.draw(3, 1, 0, 0);
      }
   }

   private void renderItemColor(RenderTarget handSource, RenderTarget destination, CommandEncoder commandEncoder) {
      try (RenderPass renderPass = commandEncoder.createRenderPass(() -> ITEM_COLOR_PASS_LABEL, destination.getColorTextureView(), Optional.empty())) {
         renderPass.setPipeline(this.itemColorPipeline);
         renderPass.bindTexture(RENDER_ITEM_COLOR_HAND_SAMPLER_NAME, handSource.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         renderPass.bindTexture(RENDER_ITEM_COLOR_HAND_MASK_SAMPLER_NAME, this.handMaskTarget.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
         renderPass.draw(3, 1, 0, 0);
      }
   }

   private HandsItemBloom() {
      this.noneCompositePipeline = RenderPipelineBuilder.builder()
         .withLocation(NONE_COMPOSITE_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(NONE_COMPOSITE_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(NONE_COMPOSITE_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withColorWrite(true, true)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(NONE_COMPOSITE_PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(NONE_COMPOSITE_PIPELINE_HAND_SAMPLER_NAME)
         .withSampler(NONE_COMPOSITE_PIPELINE_HAND_MASK_SAMPLER_NAME)
         .withSampler(NONE_COMPOSITE_PIPELINE_BLOOM_SAMPLER_NAME)
         .build();
      this.handMaskPipeline = RenderPipelineBuilder.builder()
         .withLocation(HAND_MASK_PIPELINE_LOCATION)
         .withColorTargetFormat(GpuFormat.R8_UNORM)
         .withVertexShader(ClientResourceIds.getCoreId(HAND_MASK_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(HAND_MASK_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withSampler(HAND_MASK_PIPELINE_HAND_SAMPLER_NAME)
         .withSampler(HAND_MASK_PIPELINE_HAND_DEPTH_SAMPLER_NAME)
         .build();
      this.sceneDownsamplePipeline = createBlurPipeline(SCENE_DOWNSAMPLE_PIPELINE_FRAGMENT_SHADER, false, false);
      this.maskDownsamplePipeline = createBlurPipeline(MASK_DOWNSAMPLE_PIPELINE_FRAGMENT_SHADER, true, false);
      this.maskUpsamplePipeline = createBlurPipeline(MASK_UPSAMPLE_PIPELINE_FRAGMENT_SHADER, true, false);
      this.sceneGaussianPipeline = createBlurPipeline(SCENE_GAUSSIAN_PIPELINE_FRAGMENT_SHADER, false, true);
      this.maskGaussianPipeline = createBlurPipeline(MASK_GAUSSIAN_PIPELINE_FRAGMENT_SHADER, true, true);
      this.maskSupportPipeline = createBlurPipeline(MASK_SUPPORT_PIPELINE_FRAGMENT_SHADER, true, false);
      this.itemDownsamplePipeline = createBlurPipeline(ITEM_DOWNSAMPLE_PIPELINE_FRAGMENT_SHADER, GpuFormat.RGBA16_FLOAT, false);
      this.itemUpsamplePipeline = createBlurPipeline(ITEM_UPSAMPLE_PIPELINE_FRAGMENT_SHADER, GpuFormat.RGBA16_FLOAT, false);
      this.itemGaussianPipeline = createBlurPipeline(ITEM_GAUSSIAN_PIPELINE_FRAGMENT_SHADER, GpuFormat.RGBA16_FLOAT, true);
      this.itemColorPipeline = RenderPipelineBuilder.builder()
         .withLocation(ITEM_COLOR_PIPELINE_LOCATION)
         .withColorTargetFormat(GpuFormat.RGBA16_FLOAT)
         .withVertexShader(ClientResourceIds.getCoreId(ITEM_COLOR_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(ITEM_COLOR_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withSampler(ITEM_COLOR_PIPELINE_HAND_SAMPLER_NAME)
         .withSampler(ITEM_COLOR_PIPELINE_HAND_MASK_SAMPLER_NAME)
         .build();
      this.outerGlowPipeline = RenderPipelineBuilder.builder()
         .withLocation(OUTER_GLOW_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(OUTER_GLOW_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(OUTER_GLOW_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.LIGHTNING)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(OUTER_GLOW_PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(OUTER_GLOW_PIPELINE_BLOOM_SAMPLER_NAME)
         .withSampler(OUTER_GLOW_PIPELINE_HAND_MASK_SAMPLER_NAME)
         .build();
      this.outlinePipeline = RenderPipelineBuilder.builder()
         .withLocation(OUTLINE_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(OUTLINE_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(OUTLINE_PIPELINE_FRAGMENT_SHADER))
         .withSampler(OUTLINE_PIPELINE_HAND_SAMPLER_NAME)
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(OUTLINE_PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(OUTLINE_PIPELINE_BLOOM_SAMPLER_NAME)
         .withSampler(OUTLINE_PIPELINE_HAND_MASK_SAMPLER_NAME)
         .build();
      this.glowOutlinePipeline = RenderPipelineBuilder.builder()
         .withLocation(GLOW_OUTLINE_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(GLOW_OUTLINE_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(GLOW_OUTLINE_PIPELINE_FRAGMENT_SHADER))
         .withSampler(GLOW_OUTLINE_PIPELINE_HAND_SAMPLER_NAME)
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(GLOW_OUTLINE_PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(GLOW_OUTLINE_PIPELINE_BLOOM_SAMPLER_NAME)
         .withSampler(GLOW_OUTLINE_PIPELINE_HAND_MASK_SAMPLER_NAME)
         .build();
      this.flameTrailDisplacePipeline = RenderPipelineBuilder.builder()
         .withLocation(FLAME_TRAIL_DISPLACE_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(FLAME_TRAIL_DISPLACE_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FLAME_TRAIL_DISPLACE_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(FLAME_TRAIL_DISPLACE_PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(FLAME_TRAIL_DISPLACE_PIPELINE_TRAIL_SAMPLER_NAME)
         .build();
      this.energyTrailDisplacePipeline = RenderPipelineBuilder.builder()
         .withLocation(ENERGY_TRAIL_DISPLACE_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(ENERGY_TRAIL_DISPLACE_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(ENERGY_TRAIL_DISPLACE_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(ENERGY_TRAIL_DISPLACE_PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(ENERGY_TRAIL_DISPLACE_PIPELINE_TRAIL_SAMPLER_NAME)
         .build();
      this.flameTrailPipeline = RenderPipelineBuilder.builder()
         .withLocation(FLAME_TRAIL_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(FLAME_TRAIL_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(FLAME_TRAIL_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(FLAME_TRAIL_PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(FLAME_TRAIL_PIPELINE_BLOOM_SAMPLER_NAME)
         .build();
      this.energyTrailPipeline = RenderPipelineBuilder.builder()
         .withLocation(ENERGY_TRAIL_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(ENERGY_TRAIL_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(ENERGY_TRAIL_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(ENERGY_TRAIL_PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(ENERGY_TRAIL_PIPELINE_BLOOM_SAMPLER_NAME)
         .build();
      this.ribbonTrailDisplacePipeline = RenderPipelineBuilder.builder()
         .withLocation(RIBBON_TRAIL_DISPLACE_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(RIBBON_TRAIL_DISPLACE_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(RIBBON_TRAIL_DISPLACE_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend()
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(RIBBON_TRAIL_DISPLACE_PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(RIBBON_TRAIL_DISPLACE_PIPELINE_TRAIL_SAMPLER_NAME)
         .build();
      this.ribbonTrailPipeline = RenderPipelineBuilder.builder()
         .withLocation(RIBBON_TRAIL_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(RIBBON_TRAIL_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(RIBBON_TRAIL_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withUniform(RIBBON_TRAIL_PIPELINE_UNIFORM_NAME, UniformType.UNIFORM_BUFFER)
         .withSampler(RIBBON_TRAIL_PIPELINE_BLOOM_SAMPLER_NAME)
         .build();
      this.trailBlitPipeline = RenderPipelineBuilder.builder()
         .withLocation(TRAIL_BLIT_PIPELINE_LOCATION)
         .withVertexShader(ClientResourceIds.getCoreId(TRAIL_BLIT_PIPELINE_VERTEX_SHADER))
         .withFragmentShader(ClientResourceIds.getCoreId(TRAIL_BLIT_PIPELINE_FRAGMENT_SHADER))
         .withDepthTest(CompareOp.ALWAYS_PASS)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
         .withSampler(TRAIL_BLIT_PIPELINE_TRAIL_SAMPLER_NAME)
         .build();
      this.maskBloomChain = new HandsItemBloom.BlurChain(MASK_BLOOM_TARGET_LABEL_PREFIX, true);
      this.itemBloomChain = new HandsItemBloom.BlurChain(ITEM_BLOOM_TARGET_LABEL_PREFIX, GpuFormat.RGBA16_FLOAT);
      this.mirrorSceneBlurChain = new HandsItemBloom.BlurChain(MIRROR_BLUR_TARGET_LABEL_PREFIX, false);
      this.previousTrailStyleId = -1;
      this.glowUniformStateCache = new HandsItemBloom.UniformStateCache();
      this.outlineUniformStateCache = new HandsItemBloom.UniformStateCache();
   }

   private RenderTarget blurHands(RenderTarget handSource, Hands hands, int downsampleLevels, float sigma, boolean upsampleToFirstLevel, CommandEncoder commandEncoder) {
      return this.blurSource(this.handMaskTarget, hands.isItemColorsEnabled() ? this.itemBloomChain : this.maskBloomChain, downsampleLevels, sigma, upsampleToFirstLevel, hands.isItemColorsEnabled() ? handSource : null, commandEncoder);
   }

   private GpuBuffer ensureUniformBuffer(GpuBuffer existingBuffer, int sizeBytes, String bufferLabel) {
      return existingBuffer != null && !existingBuffer.isClosed() ? existingBuffer : RenderSystem.getDevice().createBuffer(() -> bufferLabel, 136, sizeBytes);
   }

   private void renderRibbonTrail(CommandEncoder commandEncoder, RenderTarget bloomSource, Hands hands, float deltaSeconds, float timeSeconds) {
      float ribbonBrightness = hands.getGlowBrightness() * hands.getRibbonTrailBrightness() * this.smoothedRibbonStrength;
      if (!(ribbonBrightness <= RIBBON_BRIGHTNESS_EPSILON)) {
         try (MemoryStack stack = MemoryStack.stackPush()) {
            Std140Builder uniformBuilder = Std140Builder.onStack(stack, 64)
               .putVec4(ribbonBrightness, timeSeconds, deltaSeconds, hands.getRibbonTrailWidth())
               .putVec4(hands.getRibbonBend(), (float)bloomSource.width / Math.max(1, bloomSource.height), hands.isItemColorsEnabled() ? 1.0F : 0.0F, 0.0F);
            putArgbColor(uniformBuilder, hands.getPrimaryGlowColorArgb());
            putArgbColor(uniformBuilder, hands.getSecondaryGlowColorArgb());
            this.ribbonTrailUniformBuffer = this.ensureUniformBuffer(this.ribbonTrailUniformBuffer, 64, RENDER_RIBBON_TRAIL_BUFFER_LABEL);
            commandEncoder.writeToBuffer(this.ribbonTrailUniformBuffer.slice(), uniformBuilder.get());

            try (RenderPass renderPass = commandEncoder.createRenderPass(() -> RIBBON_TRAIL_PASS_LABEL, this.trailWriteTarget.getColorTextureView(), Optional.empty())) {
               renderPass.setPipeline(this.ribbonTrailPipeline);
               renderPass.bindTexture(RENDER_RIBBON_TRAIL_BLOOM_SAMPLER_NAME, bloomSource.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
               renderPass.setUniform(RENDER_RIBBON_TRAIL_UNIFORM_NAME, this.ribbonTrailUniformBuffer);
               renderPass.draw(3, 1, 0, 0);
            }
         }
      }
   }

   private static final class UniformStateCache {
      private @com.google.gson.annotations.SerializedName("Хщ")  float cachedGlowBrightness = Float.NaN;
      private @com.google.gson.annotations.SerializedName("ЯБ")  int cachedPrimaryGlowColorArgb;
      private @com.google.gson.annotations.SerializedName("ЯС")  int cachedSecondaryGlowColorArgb;
      private @com.google.gson.annotations.SerializedName("Яз")  int cachedOutlineColorArgb;
      private @com.google.gson.annotations.SerializedName("Яц")  int cachedMaskWidth = -1;
      private @com.google.gson.annotations.SerializedName("Яя")  int cachedMaskHeight = -1;
      private @com.google.gson.annotations.SerializedName("ХГ")  float cachedOutlineWidth = Float.NaN;
      private @com.google.gson.annotations.SerializedName("В弟")  boolean cachedOutlinePass;

      private void update(float glowBrightness, int primaryGlowColorArgb, int secondaryGlowColorArgb, int outlineColorArgb, int maskWidth, int maskHeight, float outlineWidth, boolean outlinePass) {
         this.cachedGlowBrightness = glowBrightness;
         this.cachedPrimaryGlowColorArgb = primaryGlowColorArgb;
         this.cachedSecondaryGlowColorArgb = secondaryGlowColorArgb;
         this.cachedOutlineColorArgb = outlineColorArgb;
         this.cachedMaskWidth = maskWidth;
         this.cachedMaskHeight = maskHeight;
         this.cachedOutlineWidth = outlineWidth;
         this.cachedOutlinePass = outlinePass;
      }

      private void invalidate() {
         this.cachedGlowBrightness = Float.NaN;
         this.cachedMaskWidth = -1;
         this.cachedMaskHeight = -1;
         this.cachedOutlineWidth = Float.NaN;
      }

      private boolean matches(float glowBrightness, int primaryGlowColorArgb, int secondaryGlowColorArgb, int outlineColorArgb, int maskWidth, int maskHeight, float outlineWidth, boolean outlinePass) {
         return Float.compare(this.cachedGlowBrightness, glowBrightness) == 0
            && this.cachedPrimaryGlowColorArgb == primaryGlowColorArgb
            && this.cachedSecondaryGlowColorArgb == secondaryGlowColorArgb
            && this.cachedOutlineColorArgb == outlineColorArgb
            && this.cachedMaskWidth == maskWidth
            && this.cachedMaskHeight == maskHeight
            && Float.compare(this.cachedOutlineWidth, outlineWidth) == 0
            && this.cachedOutlinePass == outlinePass;
      }
   }

   private static final class BlurChain {
      private final @com.google.gson.annotations.SerializedName("Вм")  String labelPrefix;
      private final @com.google.gson.annotations.SerializedName("Хъ")  boolean maskFormat;
      private final @com.google.gson.annotations.SerializedName("Х")  GpuFormat colorFormat;
      private final @com.google.gson.annotations.SerializedName("сл")  List<RenderTarget> downsampleTargets = new ArrayList<>(2);
      private @com.google.gson.annotations.SerializedName("А")  RenderTarget gaussianScratchTarget;
      private @com.google.gson.annotations.SerializedName("э")  GpuBuffer horizontalUniformBuffer;
      private @com.google.gson.annotations.SerializedName("ю")  GpuBuffer verticalUniformBuffer;
      @SerializedName("43")
      private int cachedWidth;
      @SerializedName("4р")
      private int cachedHeight;
      private @com.google.gson.annotations.SerializedName("пэ")  float cachedSigma = Float.NaN;

      private BlurChain(String targetLabelPrefix, boolean maskFormat) {
         this(targetLabelPrefix, maskFormat ? GpuFormat.R8_UNORM : GpuFormat.RGBA8_UNORM);
      }

      private BlurChain(String targetLabelPrefix, GpuFormat targetFormat) {
         this.labelPrefix = targetLabelPrefix;
         this.colorFormat = targetFormat;
         this.maskFormat = targetFormat == GpuFormat.R8_UNORM;
      }
   }

   private static final class SingleChannelTarget extends RenderTarget {
      private SingleChannelTarget(String targetLabel, int requestedWidth, int requestedHeight) {
         super(targetLabel, false, GpuFormat.R8_UNORM);
         this.resize(requestedWidth, requestedHeight);
      }

      @Override
      public void createBuffers(int requestedWidth, int requestedHeight) {
         RenderSystem.assertOnRenderThread();
         GpuDevice gpuDevice = RenderSystem.getDevice();
         int maxTextureSize = gpuDevice.getDeviceInfo().limits().maxTextureSize();
         if (requestedWidth > 0 && requestedHeight > 0 && requestedWidth <= maxTextureSize && requestedHeight <= maxTextureSize) {
            this.width = requestedWidth;
            this.height = requestedHeight;
            this.colorTexture = gpuDevice.createTexture(() -> this.label + " color", 13, GpuFormat.R8_UNORM, requestedWidth, requestedHeight, 1, 1);
            this.colorTextureView = gpuDevice.createTextureView(this.colorTexture);
         } else {
            throw new IllegalArgumentException("Invalid single-channel framebuffer size " + requestedWidth + "x" + requestedHeight);
         }
      }
   }
}
