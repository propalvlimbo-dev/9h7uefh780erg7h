package sg.gui.animation;

public final class GuiLayoutTransition {
   private @com.google.gson.annotations.SerializedName("А7")  boolean initialized;
   private @com.google.gson.annotations.SerializedName("ет")  long transitionStartNanos;
   private @com.google.gson.annotations.SerializedName("ая")  float contentWidth;
   private @com.google.gson.annotations.SerializedName("Юс")  float guiHeight;
   private @com.google.gson.annotations.SerializedName("ЮВ")  float sidebarWidth;
   private @com.google.gson.annotations.SerializedName("ЮЯ")  float sidebarLabelAlpha;
   private @com.google.gson.annotations.SerializedName("ЮХ")  float startContentWidth;
   private @com.google.gson.annotations.SerializedName("Юе")  float startGuiHeight;
   private @com.google.gson.annotations.SerializedName("ЮА")  float startSidebarWidth;
   private @com.google.gson.annotations.SerializedName("Ю4")  float startSidebarLabelAlpha;
   private @com.google.gson.annotations.SerializedName("Юп")  float targetContentWidth;
   private @com.google.gson.annotations.SerializedName("ЮД")  float targetGuiHeight;
   private @com.google.gson.annotations.SerializedName("ЮЪ")  float targetSidebarWidth;
   private @com.google.gson.annotations.SerializedName("Ют")  float transitionProgress = 1.0F;
   private static final float INITIAL_COMPACT_SIDEBAR_THRESHOLD = 100.0F;
   private static final float TRANSITION_DURATION_NANOS = 7.2E8F;
   private static final float DIMENSIONS_SPRING_PHASE_END = 0.82F;
   private static final float SIDEBAR_SPRING_PHASE_END = 0.68F;
   private static final float MIN_SIDEBAR_WIDTH = 62.0F;
   private static final float TARGET_COMPACT_SIDEBAR_THRESHOLD = 100.0F;
   private static final float SIDEBAR_LABEL_HIDE_PHASE_DURATION = 0.3F;
   private static final float SIDEBAR_LABEL_SHOW_PHASE_DELAY = 0.2F;
   private static final float SIDEBAR_LABEL_SHOW_PHASE_DURATION = 0.6F;
   private static final float SIDEBAR_LABEL_SMOOTHSTEP_LINEAR_COEFFICIENT = 3.0F;
   private static final float SPRING_PHASE_FACTOR = 9.0F;
   private static final float SECTION_REVEAL_PROGRESS_FACTOR = 0.72F;
   private static final float SECTION_REVEAL_STAGGER_DELAY = 0.028F;
   private static final float SECTION_REVEAL_PHASE_DURATION = 0.42F;

   public float getTransitionProgress() {
      return this.transitionProgress;
   }

   public float getSidebarWidth() {
      return this.sidebarWidth;
   }

   public void updateTargets(float targetContentWidth, float targetGuiHeight, float targetSidebarWidth) {
      long nowNanos = System.nanoTime();
      if (!this.initialized) {
         this.initialized = true;
         this.contentWidth = this.targetContentWidth = targetContentWidth;
         this.guiHeight = this.targetGuiHeight = targetGuiHeight;
         this.sidebarWidth = this.targetSidebarWidth = targetSidebarWidth;
         this.sidebarLabelAlpha = targetSidebarWidth < INITIAL_COMPACT_SIDEBAR_THRESHOLD ? 0.0F : 1.0F;
      } else {
         this.advanceTransition(nowNanos);
         if (targetContentWidth != this.targetContentWidth || targetGuiHeight != this.targetGuiHeight || targetSidebarWidth != this.targetSidebarWidth) {
            this.startContentWidth = this.contentWidth;
            this.startGuiHeight = this.guiHeight;
            this.startSidebarWidth = this.sidebarWidth;
            this.startSidebarLabelAlpha = this.sidebarLabelAlpha;
            this.targetContentWidth = targetContentWidth;
            this.targetGuiHeight = targetGuiHeight;
            this.targetSidebarWidth = targetSidebarWidth;
            this.transitionStartNanos = nowNanos;
            this.transitionProgress = 0.0F;
         }
      }
   }

   public float getGuiHeight() {
      return this.guiHeight;
   }

   public float getContentWidth() {
      return this.contentWidth;
   }

   public float getSidebarLabelAlpha() {
      return this.sidebarLabelAlpha;
   }

   private float easeDampedSpring(float progress) {
      if (progress >= 1.0F) {
         return 1.0F;
      }

      double springPhase = progress * SPRING_PHASE_FACTOR;
      return (float)(1.0 - Math.exp(-springPhase) * (Math.cos(springPhase) + Math.sin(springPhase)));
   }

   private void advanceTransition(long nowNanos) {
      if (!(this.transitionProgress >= 1.0F)) {
         this.transitionProgress = Math.clamp((float)(nowNanos - this.transitionStartNanos) / TRANSITION_DURATION_NANOS, 0.0F, 1.0F);
         float dimensionsSpringProgress = this.easeDampedSpring(Math.clamp(this.transitionProgress / DIMENSIONS_SPRING_PHASE_END, 0.0F, 1.0F));
         float sidebarSpringProgress = this.easeDampedSpring(Math.clamp(this.transitionProgress / SIDEBAR_SPRING_PHASE_END, 0.0F, 1.0F));
         this.contentWidth = this.startContentWidth + (this.targetContentWidth - this.startContentWidth) * dimensionsSpringProgress;
         this.guiHeight = this.startGuiHeight + (this.targetGuiHeight - this.startGuiHeight) * dimensionsSpringProgress;
         this.sidebarWidth = Math.max(MIN_SIDEBAR_WIDTH, this.startSidebarWidth + (this.targetSidebarWidth - this.startSidebarWidth) * sidebarSpringProgress);
         float targetSidebarLabelAlpha = this.targetSidebarWidth < TARGET_COMPACT_SIDEBAR_THRESHOLD ? 0.0F : 1.0F;
         float sidebarLabelProgress = targetSidebarLabelAlpha == 0.0F ? Math.clamp(this.transitionProgress / SIDEBAR_LABEL_HIDE_PHASE_DURATION, 0.0F, 1.0F) : Math.clamp((this.transitionProgress - SIDEBAR_LABEL_SHOW_PHASE_DELAY) / SIDEBAR_LABEL_SHOW_PHASE_DURATION, 0.0F, 1.0F);
         sidebarLabelProgress = sidebarLabelProgress * sidebarLabelProgress * (SIDEBAR_LABEL_SMOOTHSTEP_LINEAR_COEFFICIENT - 2.0F * sidebarLabelProgress);
         this.sidebarLabelAlpha = this.startSidebarLabelAlpha + (targetSidebarLabelAlpha - this.startSidebarLabelAlpha) * sidebarLabelProgress;
      }
   }

   public float getSectionRevealProgress(int sectionIndex) {
      return this.transitionProgress >= 1.0F ? 1.0F : Math.clamp((this.transitionProgress * SECTION_REVEAL_PROGRESS_FACTOR - Math.min(sectionIndex, 8) * SECTION_REVEAL_STAGGER_DELAY) / SECTION_REVEAL_PHASE_DURATION, 0.0F, 1.0F);
   }
}
