package sg.hud;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import sg.ec.UnhookState;
import sg.hud.GpsHud;
import sg.events.EventListener;
import sg.events.impl.ChatMouseReleaseEvent;
import sg.events.impl.ChatClosedEvent;
import sg.hud.CoordinatesHud;
import sg.ec.ClientFonts;
import sg.hud.HudRenderBeginHandler;
import sg.hud.HudRenderEndHandler;
import sg.hud.TargetHud;
import sg.render.WorldProjection;
import sg.hud.MusicPlayerHud;
import sg.render.pipeline.GravityGrid;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.settings.BooleanSetting;
import sg.hud.InventoryHudElement;
import sg.hud.Keybinds;
import sg.util.time.ColorMath;
import sg.hud.StaffList;
import sg.hud.TimerHudElement;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.events.impl.ChatMouseButtonEvent;
import sg.events.impl.HudRenderEvent;
import sg.settings.FloatSetting;
import sg.settings.MultiChoiceSetting;
import sg.settings.SingleChoiceSetting;

public class HudElementRegistry extends ArrayList<HudElement> {
   private static final float тщ = 26.0F;
   private static final float тГ = 40.0F;
   private static final float[] SNAP_LINE_FRACTIONS = new float[]{
      HudElementRegistry.SNAP_ONE_SIXTH, HudElementRegistry.SNAP_ONE_THIRD, HudElementRegistry.SNAP_ONE_HALF, HudElementRegistry.SNAP_TWO_THIRDS, HudElementRegistry.SNAP_FIVE_SIXTHS
   };
   private static final String Яы = "Отпустите ЛКМ для подтверждения позиции.";
   private @com.google.gson.annotations.SerializedName("тл")  float dragOffsetX;
   private @com.google.gson.annotations.SerializedName("тэ")  float dragOffsetY;
   private @com.google.gson.annotations.SerializedName("тю")  float dragStartX;
   private @com.google.gson.annotations.SerializedName("тЩ")  float dragStartBaseY;
   @SerializedName("В")
   private HudElement draggedElement = null;
   private @com.google.gson.annotations.SerializedName("Я")  HudElement animatingElement = null;
   private @com.google.gson.annotations.SerializedName("ту")  float targetX;
   private @com.google.gson.annotations.SerializedName("т2")  float targetBaseY;
   private @com.google.gson.annotations.SerializedName("тф")  float velocityX;
   private @com.google.gson.annotations.SerializedName("ти")  float velocityY;
   private @com.google.gson.annotations.SerializedName("т9")  float snappedCenterX = Float.NaN;
   private @com.google.gson.annotations.SerializedName("то")  float snappedCenterY = Float.NaN;
   private @com.google.gson.annotations.SerializedName("ХЙ")  long lastSpringUpdateNanos;
   @SerializedName("В")
   private final WaterMark watermark;
   @SerializedName("В")
   private final TargetHud targetHud;
   @SerializedName("В")
   private final Potions potions;
   @SerializedName("В")
   private final LootTrackerHudElement lootTracker;
   @SerializedName("В")
   private final KillCardsHudElement killCards;
   @SerializedName("В")
   private final Scoreboard scoreboard;
   @SerializedName("В")
   private final Notifications notifications;
   @SerializedName("В")
   private final ItemIndicator itemIndicator;
   private final @com.google.gson.annotations.SerializedName("Яп")  BooleanSetting dragPreviewSetting;
   private final @com.google.gson.annotations.SerializedName("сШ")  FloatAnimation gridVisibilityAnimation;
   private @com.google.gson.annotations.SerializedName("е西")  boolean enabled;
   private final @com.google.gson.annotations.SerializedName("Хн")  EventListener<ChatMouseReleaseEvent> mouseReleaseListener;
   private final @com.google.gson.annotations.SerializedName("ХЦ")  EventListener<ChatClosedEvent> chatClosedListener;
   private final @com.google.gson.annotations.SerializedName("Хч")  EventListener<HudRenderEvent> dragUpdateListener;
   private final @com.google.gson.annotations.SerializedName("ХЭ")  EventListener<HudRenderEvent> editorOverlayListener;
   private final @com.google.gson.annotations.SerializedName("Х必")  EventListener<ChatMouseButtonEvent> mouseButtonListener;
   private static final float GRID_VISIBILITY_ANIMATION_SPEED = 12.0F;
   private static final float MUSIC_DRAG_MIN_VELOCITY_IMPULSE_X = -180.0F;
   private static final float MUSIC_DRAG_MAX_VELOCITY_IMPULSE_X = 180.0F;
   private static final float MUSIC_DRAG_VELOCITY_GAIN_X = 7.0F;
   private static final float MUSIC_DRAG_BASE_Y = 10.0F;
   private static final float DRAG_MIN_VELOCITY_IMPULSE_X = -180.0F;
   private static final float DRAG_MAX_VELOCITY_IMPULSE_X = 180.0F;
   private static final float DRAG_VELOCITY_GAIN_X = 7.0F;
   private static final float DRAG_MIN_VELOCITY_IMPULSE_Y = -180.0F;
   private static final float DRAG_MAX_VELOCITY_IMPULSE_Y = 180.0F;
   private static final float DRAG_VELOCITY_GAIN_Y = 7.0F;
   private static final float GRID_VISIBILITY_THRESHOLD = 0.01F;
   private static final String GRID_HINT_TEXT = "Зажми Ctrl + Shift чтобы показалась сетка";
   private static final float GRID_HINT_CENTER_FACTOR = 0.5F;
   private static final float GRID_HINT_Y = 17.0F;
   private static final double GRID_HINT_ALPHA = 170.0;
   private static final double PREVIEW_TRAJECTORY_ALPHA = 210.0;
   private static final double PREVIEW_OUTLINE_ALPHA = 230.0;
   private static final double PREVIEW_FILL_ALPHA = 22.0;
   private static final double Впщ = 210.0;
   private static final float PREVIEW_TOP_LEFT_DASH_THICKNESS = 1.5F;
   private static final float PREVIEW_TOP_LEFT_DASH_LENGTH = 8.0F;
   private static final float PREVIEW_TOP_LEFT_DASH_GAP = 5.0F;
   private static final float PREVIEW_TOP_RIGHT_DASH_THICKNESS = 1.5F;
   private static final float PREVIEW_TOP_RIGHT_DASH_LENGTH = 8.0F;
   private static final float PREVIEW_TOP_RIGHT_DASH_GAP = 5.0F;
   private static final float PREVIEW_BOTTOM_LEFT_DASH_THICKNESS = 1.5F;
   private static final float PREVIEW_BOTTOM_LEFT_DASH_LENGTH = 8.0F;
   private static final float PREVIEW_BOTTOM_LEFT_DASH_GAP = 5.0F;
   private static final float PREVIEW_BOTTOM_RIGHT_DASH_THICKNESS = 1.5F;
   private static final float PREVIEW_BOTTOM_RIGHT_DASH_LENGTH = 8.0F;
   private static final float PREVIEW_BOTTOM_RIGHT_DASH_GAP = 5.0F;
   private static final float PREVIEW_FILL_CORNER_RADIUS = 8.0F;
   private static final float PREVIEW_OUTLINE_THICKNESS = 1.25F;
   private static final float PREVIEW_OUTLINE_CORNER_RADIUS = 8.0F;
   private static final String PREVIEW_CAPTION_MEASURE_TEXT = "Отпустите ЛКМ для подтверждения позиции.";
   private static final float PREVIEW_CAPTION_PADDING_X = 9.0F;
   private static final float PREVIEW_CAPTION_PADDING_Y = 6.0F;
   private static final float PREVIEW_CAPTION_CENTER_FACTOR = 0.5F;
   private static final float PREVIEW_CAPTION_MIN_X = 6.0F;
   private static final float PREVIEW_CAPTION_MIN_MAX_X = 6.0F;
   private static final float PREVIEW_CAPTION_RIGHT_MARGIN = 6.0F;
   private static final float PREVIEW_CAPTION_BELOW_GAP = 8.0F;
   private static final float PREVIEW_CAPTION_BOTTOM_MARGIN = 6.0F;
   private static final float PREVIEW_CAPTION_ABOVE_GAP = 8.0F;
   private static final String PREVIEW_CAPTION_TEXT = "Отпустите ЛКМ для подтверждения позиции.";
   private static final float MIN_DASHED_LINE_LENGTH = 0.5F;
   private static final float MIN_DASH_SAMPLE_STEP = 0.75F;
   private static final float DASH_SAMPLE_THICKNESS_FACTOR = 0.65F;
   private static final float DASH_SAMPLE_CENTER_FACTOR_X = 0.5F;
   private static final float DASH_SAMPLE_CENTER_FACTOR_Y = 0.5F;
   private static final float MUSIC_DRAG_START_BASE_Y = 10.0F;
   private static final float INITIAL_DRAG_VELOCITY_Y = -22.0F;
   private static final float SNAP_ELEMENT_CENTER_FACTOR = 0.5F;
   private static final float SNAP_RELEASE_DISTANCE = 40.0F;
   private static final float SNAP_RETAINED_CENTER_FACTOR = 0.5F;
   private static final float INITIAL_SNAP_SEARCH_DISTANCE = Float.MAX_VALUE;
   private static final float SNAP_CAPTURE_DISTANCE = 26.0F;
   private static final float SNAP_CAPTURED_CENTER_FACTOR = 0.5F;
   private static final float MAX_SPRING_FRAME_SECONDS = 0.1F;
   private static final float NANOS_PER_SECOND = 1.0E9F;
   private static final float MAX_SPRING_STEP_SECONDS = 0.016F;
   private static final float SPRING_STIFFNESS_X = 520.0F;
   private static final float SPRING_DAMPING_X = 30.0F;
   private static final float SPRING_STIFFNESS_Y = 520.0F;
   private static final float SPRING_DAMPING_Y = 30.0F;
   private static final float SPRING_FINISH_DISTANCE_X = 0.05F;
   private static final float SPRING_FINISH_DISTANCE_Y = 0.05F;
   private static final float SPRING_FINISH_VELOCITY_X = 0.5F;
   private static final float SPRING_FINISH_VELOCITY_Y = 0.5F;
   private static final float SNAP_ONE_SIXTH = 0.16666667F;
   private static final float SNAP_ONE_THIRD = 0.33333334F;
   private static final float SNAP_ONE_HALF = 0.5F;
   private static final float SNAP_TWO_THIRDS = 0.6666667F;
   private static final float SNAP_FIVE_SIXTHS = 0.8333333F;

   @Generated
   public LootTrackerHudElement getLootTracker() {
      return this.lootTracker;
   }

   private void advanceDragSpring() {
      if (this.animatingElement != null) {
         if (this.animatingElement instanceof Notifications) {
            this.targetX = this.animatingElement.getX();
            this.animatingElement.setBaseY(this.targetBaseY);
            this.velocityX = this.velocityY = 0.0F;
            if (this.draggedElement == null) {
               this.clearDragSpring();
            }
         } else {
            long nowNanos = System.nanoTime();
            float remainingSeconds = this.lastSpringUpdateNanos == 0L ? 0.0F : Math.min(MAX_SPRING_FRAME_SECONDS, (float)(nowNanos - this.lastSpringUpdateNanos) / NANOS_PER_SECOND);
            this.lastSpringUpdateNanos = nowNanos;
            if (!(remainingSeconds <= 0.0F)) {
               while (remainingSeconds > 0.0F) {
                  float stepSeconds = Math.min(MAX_SPRING_STEP_SECONDS, remainingSeconds);
                  float deltaX = this.targetX - this.animatingElement.getX();
                  float deltaY = this.targetBaseY - this.animatingElement.getBaseY();
                  this.velocityX = this.velocityX + (deltaX * SPRING_STIFFNESS_X - this.velocityX * SPRING_DAMPING_X) * stepSeconds;
                  this.velocityY = this.velocityY + (deltaY * SPRING_STIFFNESS_Y - this.velocityY * SPRING_DAMPING_Y) * stepSeconds;
                  this.animatingElement.setX(this.animatingElement.getX() + this.velocityX * stepSeconds);
                  this.animatingElement.setBaseY(this.animatingElement.getBaseY() + this.velocityY * stepSeconds);
                  remainingSeconds -= stepSeconds;
               }

               if (this.draggedElement == null
                  && Math.abs(this.targetX - this.animatingElement.getX()) < SPRING_FINISH_DISTANCE_X
                  && Math.abs(this.targetBaseY - this.animatingElement.getBaseY()) < SPRING_FINISH_DISTANCE_Y
                  && Math.abs(this.velocityX) < SPRING_FINISH_VELOCITY_X
                  && Math.abs(this.velocityY) < SPRING_FINISH_VELOCITY_Y) {
                  this.animatingElement.setX(this.targetX);
                  this.animatingElement.setBaseY(this.targetBaseY);
                  this.clearDragSpring();
               }
            }
         }
      }
   }

   private void drawDragPreview() {
      if (this.isDragPreviewEnabled() && this.draggedElement != null) {
         Minecraft client = Minecraft.getInstance();
         float screenWidth = client.getWindow().getScreenWidth();
         float screenHeight = client.getWindow().getScreenHeight();
         float elementWidth = this.draggedElement.getScaledWidth();
         float elementHeight = this.draggedElement.getScaledHeight();
         float startX = this.dragStartX;
         float startY = this.dragStartBaseY;
         float previewX = this.targetX;
         float previewY = this.targetBaseY;
         int trajectoryColor = ColorMath.packArgb(255, 255, 255, PREVIEW_TRAJECTORY_ALPHA);
         int outlineColor = ColorMath.packArgb(255, 255, 255, PREVIEW_OUTLINE_ALPHA);
         int fillColor = ColorMath.packArgb(255, 255, 255, PREVIEW_FILL_ALPHA);
         int var13 = ColorMath.packArgb(12, 14, 18, Впщ);
         UiDrawing.setTransform(new Matrix4f());
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());

         try {
            this.drawDashedLine(startX, startY, previewX, previewY, PREVIEW_TOP_LEFT_DASH_THICKNESS, PREVIEW_TOP_LEFT_DASH_LENGTH, PREVIEW_TOP_LEFT_DASH_GAP, trajectoryColor);
            this.drawDashedLine(startX + elementWidth, startY, previewX + elementWidth, previewY, PREVIEW_TOP_RIGHT_DASH_THICKNESS, PREVIEW_TOP_RIGHT_DASH_LENGTH, PREVIEW_TOP_RIGHT_DASH_GAP, trajectoryColor);
            this.drawDashedLine(startX, startY + elementHeight, previewX, previewY + elementHeight, PREVIEW_BOTTOM_LEFT_DASH_THICKNESS, PREVIEW_BOTTOM_LEFT_DASH_LENGTH, PREVIEW_BOTTOM_LEFT_DASH_GAP, trajectoryColor);
            this.drawDashedLine(startX + elementWidth, startY + elementHeight, previewX + elementWidth, previewY + elementHeight, PREVIEW_BOTTOM_RIGHT_DASH_THICKNESS, PREVIEW_BOTTOM_RIGHT_DASH_LENGTH, PREVIEW_BOTTOM_RIGHT_DASH_GAP, trajectoryColor);
            UiBatchDrawing.drawRoundedRect(previewX, previewY, elementWidth, elementHeight, PREVIEW_FILL_CORNER_RADIUS, fillColor);
            UiBatchDrawing.drawOutline(previewX, previewY, elementWidth, elementHeight, PREVIEW_OUTLINE_THICKNESS, PREVIEW_OUTLINE_CORNER_RADIUS, outlineColor);
            byte captionFontSize = 11;
            float captionTextWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), PREVIEW_CAPTION_MEASURE_TEXT, captionFontSize);
            float captionPaddingX = PREVIEW_CAPTION_PADDING_X;
            float captionPaddingY = PREVIEW_CAPTION_PADDING_Y;
            float captionWidth = captionTextWidth + captionPaddingX * 2.0F;
            float captionHeight = captionFontSize + captionPaddingY * 2.0F;
            float captionX = Math.clamp(previewX + (elementWidth - captionWidth) * PREVIEW_CAPTION_CENTER_FACTOR, PREVIEW_CAPTION_MIN_X, Math.max(PREVIEW_CAPTION_MIN_MAX_X, screenWidth - captionWidth - PREVIEW_CAPTION_RIGHT_MARGIN));
            float captionY = previewY + elementHeight + PREVIEW_CAPTION_BELOW_GAP;
            if (captionY + captionHeight > screenHeight - PREVIEW_CAPTION_BOTTOM_MARGIN) {
               captionY = previewY - captionHeight - PREVIEW_CAPTION_ABOVE_GAP;
            }

            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), PREVIEW_CAPTION_TEXT, captionX + captionPaddingX, captionY + captionPaddingY, captionFontSize, -1);
         } finally {
            UiBatchDrawing.endBatch();
         }
      }
   }

   @Generated
   public Scoreboard getScoreboard() {
      return this.scoreboard;
   }

   public boolean dispatchMouseButton(float mouseX, float mouseY, int button) {
      if (!this.enabled) {
         return false;
      }

      for (HudElement element : this.reversed()) {
         if (element instanceof InteractiveHudElement interactiveElement && interactiveElement.ш(mouseX, mouseY, button)) {
            return true;
         }
      }

      return false;
   }

   private void drawDashedLine(float startX, float startY, float endX, float endY, float thickness, float dashLength, float dashGap, int color) {
      float deltaX = endX - startX;
      float deltaY = endY - startY;
      float lineLength = (float)Math.hypot(deltaX, deltaY);
      if (!(lineLength < MIN_DASHED_LINE_LENGTH)) {
         float directionX = deltaX / lineLength;
         float directionY = deltaY / lineLength;
         float sampleStep = Math.max(MIN_DASH_SAMPLE_STEP, thickness * DASH_SAMPLE_THICKNESS_FACTOR);
         float dashStart = 0.0F;

         while (dashStart < lineLength) {
            float dashEnd = Math.min(dashStart + dashLength, lineLength);

            for (float sampleDistance = dashStart; sampleDistance < dashEnd; sampleDistance += sampleStep) {
               float sampleX = startX + directionX * sampleDistance;
               float sampleY = startY + directionY * sampleDistance;
               UiBatchDrawing.drawRoundedRect(sampleX - thickness * DASH_SAMPLE_CENTER_FACTOR_X, sampleY - thickness * DASH_SAMPLE_CENTER_FACTOR_Y, thickness, thickness, 0.0F, color);
            }

            dashStart += dashLength + dashGap;
         }
      }
   }

   private boolean isCtrlShiftDown(Minecraft client) {
      long windowHandle = client.getWindow().handle();
      boolean shiftDown = GLFW.glfwGetKey(windowHandle, 340) == 1 || GLFW.glfwGetKey(windowHandle, 344) == 1;
      boolean ctrlDown = GLFW.glfwGetKey(windowHandle, 341) == 1 || GLFW.glfwGetKey(windowHandle, 345) == 1;
      return shiftDown && ctrlDown;
   }

   public void onMouseButton(ChatMouseButtonEvent event) {
      if (!event.isCancelled()) {
         if (this.dispatchMouseButton(event.getMouseX(), event.getMouseY(), event.getButton())) {
            event.setCancelled(true);
         } else {
            this.beginDrag(event.getMouseX(), event.getMouseY(), event.getButton());
         }
      }
   }

   private boolean isDragPreviewEnabled() {
      return !(this.draggedElement instanceof Notifications) && this.dragPreviewSetting != null && this.dragPreviewSetting.getValue();
   }

   public boolean beginDrag(float mouseX, float mouseY, int button) {
      if (this.enabled && button == 0) {
         for (HudElement element : this.reversed()) {
            if (element.hasRenderableSize() && WorldProjection.containsPoint(mouseX, mouseY, element.getX(), element.getY(), element.getScaledWidth(), element.getScaledHeight())) {
               this.finishPreviousDragSpring(element);
               this.dragOffsetX = element.getX() - mouseX;
               this.dragOffsetY = element.getBaseY() - mouseY;
               this.dragStartX = element.getX();
               this.dragStartBaseY = element instanceof MusicPlayerHud ? MUSIC_DRAG_START_BASE_Y : element.getBaseY();
               this.draggedElement = element;
               this.animatingElement = element;
               if (element instanceof Scoreboard scoreboardElement) {
                  scoreboardElement.ШЪ();
               }

               this.targetX = element.getX();
               this.targetBaseY = this.dragStartBaseY;
               this.velocityX = 0.0F;
               this.velocityY = !this.isDragPreviewEnabled() && !(element instanceof MusicPlayerHud) ? INITIAL_DRAG_VELOCITY_Y : 0.0F;
               this.lastSpringUpdateNanos = System.nanoTime();
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private float snapCoordinate(float coordinate, float elementSize, float screenSize, float maxCoordinate, boolean horizontal) {
      float center = coordinate + elementSize * SNAP_ELEMENT_CENTER_FACTOR;
      float retainedSnapCenter = horizontal ? this.snappedCenterX : this.snappedCenterY;
      if (!Float.isNaN(retainedSnapCenter)) {
         if (Math.abs(center - retainedSnapCenter) <= SNAP_RELEASE_DISTANCE) {
            return Math.clamp(retainedSnapCenter - elementSize * SNAP_RETAINED_CENTER_FACTOR, 0.0F, maxCoordinate);
         }

         if (horizontal) {
            this.snappedCenterX = Float.NaN;
         } else {
            this.snappedCenterY = Float.NaN;
         }
      }

      float nearestSnapCenter = Float.NaN;
      float nearestSnapDistance = INITIAL_SNAP_SEARCH_DISTANCE;

      for (float lineFraction : SNAP_LINE_FRACTIONS) {
         float lineCoordinate = screenSize * lineFraction;
         float lineDistance = Math.abs(center - lineCoordinate);
         if (lineDistance < nearestSnapDistance) {
            nearestSnapDistance = lineDistance;
            nearestSnapCenter = lineCoordinate;
         }
      }

      if (!Float.isNaN(nearestSnapCenter) && nearestSnapDistance <= SNAP_CAPTURE_DISTANCE) {
         if (horizontal) {
            this.snappedCenterX = nearestSnapCenter;
         } else {
            this.snappedCenterY = nearestSnapCenter;
         }

         return Math.clamp(nearestSnapCenter - elementSize * SNAP_CAPTURED_CENTER_FACTOR, 0.0F, maxCoordinate);
      } else {
         return Math.clamp(coordinate, 0.0F, maxCoordinate);
      }
   }

   public void onChatClosed(ChatClosedEvent event) {
      for (HudElement element : this) {
         if (element instanceof InteractiveHudElement interactiveElement) {
            interactiveElement.Б5();
         }
      }

      this.finishPreviousDragSpring(null);
      this.draggedElement = null;
      this.dragOffsetX = 0.0F;
      this.dragOffsetY = 0.0F;
      this.snappedCenterX = Float.NaN;
      this.snappedCenterY = Float.NaN;
   }

   @Generated
   public KillCardsHudElement getKillCards() {
      return this.killCards;
   }

   public void enable() {
      if (!this.enabled) {
         this.enabled = true;
         this.forEach(element -> RoseClient.getInstance().getEventBus().register(element));
         RoseClient.getInstance().getEventBus().register(this.targetHud.method_6_10d41d08());
         RoseClient.getInstance().getEventBus().register(this.potions.ш_10d41d08());
         RoseClient.getInstance().getEventBus().register(this.lootTracker.и_10d41d08());
         RoseClient.getInstance().getEventBus().register(this.itemIndicator.method_1());
         RoseClient.getInstance().getEventBus().register(this.notifications.method_9());
      }
   }

   public void finishDrag() {
      for (HudElement element : this) {
         if (element instanceof InteractiveHudElement interactiveElement) {
            interactiveElement.иВ();
         }
      }

      if (!this.isDragPreviewEnabled() || !this.commitDragPosition()) {
         this.draggedElement = null;
         this.dragOffsetX = 0.0F;
         this.dragOffsetY = 0.0F;
         this.snappedCenterX = Float.NaN;
         this.snappedCenterY = Float.NaN;
      }
   }

   private void clearDragSpring() {
      this.animatingElement = null;
      this.velocityX = this.velocityY = 0.0F;
      this.lastSpringUpdateNanos = 0L;
   }

   private void drawEditorOverlay() {
      if (UnhookState.ые()) {
         this.gridVisibilityAnimation.setImmediately(0.0F);
      } else {
         Minecraft client = Minecraft.getInstance();
         boolean chatOpen = client.gui.screen() instanceof ChatScreen;
         float screenWidth = client.getWindow().getScreenWidth();
         float screenHeight = client.getWindow().getScreenHeight();
         this.gridVisibilityAnimation.updateTarget(chatOpen && this.isCtrlShiftDown(client) ? 1.0F : 0.0F);
         if (this.gridVisibilityAnimation.getValue() > GRID_VISIBILITY_THRESHOLD) {
            GravityGrid.INSTANCE.drawGrid(screenWidth, screenHeight, this.gridVisibilityAnimation.getValue());
         }

         if (chatOpen) {
            String hintText = GRID_HINT_TEXT;
            float hintWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), hintText, 11);
            float hintX = (screenWidth - hintWidth) * GRID_HINT_CENTER_FACTOR;
            UiDrawing.setTransform(new Matrix4f());
            UiDrawing.drawText(ClientFonts.getMediumFont(), hintText, hintX, GRID_HINT_Y, 11, ColorMath.packArgb(255, 255, 255, GRID_HINT_ALPHA));
         }

         this.drawDragPreview();
      }
   }

   public void disable() {
      if (this.enabled) {
         this.enabled = false;
         this.targetHud.method_6_10d41d08().сС();
         this.potions.ш_10d41d08().сС();
         this.lootTracker.и_10d41d08().сС();
         this.itemIndicator.method_1().сС();
         this.notifications.Б5();
         this.notifications.setWidth(0.0F);
         this.notifications.setHeight(0.0F);
         this.forEach(element -> RoseClient.getInstance().getEventBus().unregister(element));
         RoseClient.getInstance().getEventBus().unregister(this.targetHud.method_6_10d41d08());
         RoseClient.getInstance().getEventBus().unregister(this.potions.ш_10d41d08());
         RoseClient.getInstance().getEventBus().unregister(this.lootTracker.и_10d41d08());
         RoseClient.getInstance().getEventBus().unregister(this.itemIndicator.method_1());
         RoseClient.getInstance().getEventBus().unregister(this.notifications.method_9());
      }
   }

   private boolean commitDragPosition() {
      if (this.draggedElement == null) {
         return false;
      }

      this.draggedElement.setX(this.targetX);
      this.draggedElement.setBaseY(this.targetBaseY);
      this.draggedElement = null;
      this.dragOffsetX = 0.0F;
      this.dragOffsetY = 0.0F;
      this.snappedCenterX = Float.NaN;
      this.snappedCenterY = Float.NaN;
      this.clearDragSpring();
      return true;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void onMouseRelease(ChatMouseReleaseEvent event) {
      this.finishDrag();
   }

   public void updateDrag(HudRenderEvent event) {
      if (this.draggedElement != null) {
         Minecraft client = Minecraft.getInstance();
         float screenWidth = client.getWindow().getScreenWidth();
         float screenHeight = client.getWindow().getScreenHeight();
         float maxX = Math.max(0.0F, screenWidth - this.draggedElement.getScaledWidth());
         float dragX = Math.clamp(event.getMouseX() + this.dragOffsetX, 0.0F, maxX);
         boolean snapToGrid = this.isCtrlShiftDown(client);
         if (snapToGrid) {
            dragX = this.snapCoordinate(dragX, this.draggedElement.getScaledWidth(), screenWidth, maxX, true);
         } else {
            this.snappedCenterX = Float.NaN;
         }

         if (this.draggedElement instanceof Notifications) {
            dragX = this.draggedElement.getX();
            this.dragStartX = dragX;
            this.targetX = dragX;
            this.velocityX = 0.0F;
            this.snappedCenterX = Float.NaN;
         }

         if (this.draggedElement instanceof MusicPlayerHud) {
            this.velocityX = this.velocityX + Math.max(MUSIC_DRAG_MIN_VELOCITY_IMPULSE_X, Math.min(MUSIC_DRAG_MAX_VELOCITY_IMPULSE_X, (dragX - this.targetX) * MUSIC_DRAG_VELOCITY_GAIN_X));
            this.velocityY = 0.0F;
            this.targetX = dragX;
            this.targetBaseY = MUSIC_DRAG_BASE_Y;
            this.snappedCenterY = Float.NaN;
         } else {
            float maxY = Math.max(0.0F, screenHeight - this.draggedElement.getScaledHeight());
            float dragY = Math.clamp(event.getMouseY() + this.dragOffsetY, 0.0F, maxY);
            if (snapToGrid) {
               dragY = this.snapCoordinate(dragY, this.draggedElement.getScaledHeight(), screenHeight, maxY, false);
            } else {
               this.snappedCenterY = Float.NaN;
            }

            this.velocityX = this.velocityX + Math.max(DRAG_MIN_VELOCITY_IMPULSE_X, Math.min(DRAG_MAX_VELOCITY_IMPULSE_X, (dragX - this.targetX) * DRAG_VELOCITY_GAIN_X));
            this.velocityY = this.velocityY + Math.max(DRAG_MIN_VELOCITY_IMPULSE_Y, Math.min(DRAG_MAX_VELOCITY_IMPULSE_Y, (dragY - this.targetBaseY) * DRAG_VELOCITY_GAIN_Y));
            this.targetX = dragX;
            this.targetBaseY = dragY;
         }

         if (this.isDragPreviewEnabled()) {
            this.draggedElement.setX(this.dragStartX);
            this.draggedElement.setBaseY(this.dragStartBaseY);
            this.velocityX = 0.0F;
            this.velocityY = 0.0F;
            this.lastSpringUpdateNanos = System.nanoTime();
         }
      }

      if (!this.isDragPreviewEnabled() || this.draggedElement == null) {
         this.advanceDragSpring();
      }
   }

   public HudElementRegistry(
      SingleChoiceSetting targetStyle,
      SingleChoiceSetting potionsStyle,
      BooleanSetting particlesFromHead,
      BooleanSetting particlesFromHealthBar,
      FloatSetting particleCount,
      SingleChoiceSetting particleStyle,
      BooleanSetting replaceTargetSkin,
      FloatSetting pvpAssistScale,
      FloatSetting lootTrackerScale,
      FloatSetting itemIndicatorScale,
      BooleanSetting pinnedHotbarSetting,
      BooleanSetting dragPreviewSetting,
      MultiChoiceSetting notificationCategories
   ) {
      this.gridVisibilityAnimation = new FloatAnimation(0.0F, GRID_VISIBILITY_ANIMATION_SPEED);
      this.mouseReleaseListener = this::onMouseRelease;
      this.chatClosedListener = this::onChatClosed;
      this.dragUpdateListener = this::updateDrag;
      this.editorOverlayListener = new EventListener<HudRenderEvent>() {
         public void onEvent(HudRenderEvent var1) {
            HudElementRegistry.this.drawEditorOverlay();
         }

         @Override
         public int getPriority() {
            return -150;
         }
      };
      this.mouseButtonListener = this::onMouseButton;
      this.add(new HudRenderBeginHandler());
      this.add(new Keybinds());
      this.watermark = new WaterMark();
      this.add(this.watermark);
      this.targetHud = new TargetHud(targetStyle, particlesFromHead, particlesFromHealthBar, particleCount, particleStyle, replaceTargetSkin);
      this.add(this.targetHud);
      this.potions = new Potions(potionsStyle);
      this.add(this.potions);
      this.add(new Cooldowns());
      this.add(new StaffList());
      this.add(new CoordinatesHud());
      this.add(new SpeedHudElement());
      this.add(new Profiler());
      this.add(new TimerHudElement());
      this.itemIndicator = new ItemIndicator(itemIndicatorScale);
      this.add(this.itemIndicator);
      this.add(new InventoryHudElement());
      this.lootTracker = new LootTrackerHudElement(lootTrackerScale);
      this.add(this.lootTracker);
      this.killCards = new KillCardsHudElement();
      this.add(this.killCards);
      this.add(new MusicPlayerHud());
      this.add(new GpsHud());
      this.scoreboard = new Scoreboard();
      this.add(this.scoreboard);
      this.notifications = new Notifications(notificationCategories);
      this.add(this.notifications);
      this.add(new HudRenderEndHandler());
      this.dragPreviewSetting = dragPreviewSetting;
      RoseClient.getInstance().getEventBus().register(this);
   }

   private void finishPreviousDragSpring(HudElement nextElement) {
      if (this.animatingElement != null && this.animatingElement != nextElement) {
         this.animatingElement.setX(this.targetX);
         this.animatingElement.setBaseY(this.targetBaseY);
         this.clearDragSpring();
      }
   }

   @Generated
   public Notifications getNotifications() {
      return this.notifications;
   }
}
