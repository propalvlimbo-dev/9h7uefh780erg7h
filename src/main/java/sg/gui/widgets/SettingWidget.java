package sg.gui.widgets;

import lombok.Generated;
import net.minecraft.client.Minecraft;
import sg.modules.impl.init.TickAnimation;
import sg.gui.click.ClickGuiScreen;
import sg.settings.ValueSetting;

public abstract class SettingWidget<T extends ValueSetting<?>> {
   private final @com.google.gson.annotations.SerializedName("А")  TickAnimation visibilityAnimation = new TickAnimation();
   protected @com.google.gson.annotations.SerializedName("Ан")  float x;
   protected @com.google.gson.annotations.SerializedName("АЦ")  float y;
   protected @com.google.gson.annotations.SerializedName("Ач")  float width;
   protected @com.google.gson.annotations.SerializedName("АЭ")  float height;
   protected @com.google.gson.annotations.SerializedName("В")  T setting;
   private static final float POPUP_HORIZONTAL_BASE_MARGIN = 8.0F;
   private static final float POPUP_HORIZONTAL_SIZE_MARGIN_FACTOR = 0.125F;
   private static final float POPUP_VERTICAL_BASE_MARGIN = 10.0F;
   private static final float POPUP_VERTICAL_SIZE_MARGIN_FACTOR = 0.125F;
   private static final float POPUP_ABOVE_WIDGET_GAP = 5.0F;

   public void renderOverlay(float mouseX, float mouseY, float tickDelta) {
   }

   public void charTyped(String text, int codePoint, int modifiers) {
   }

   @Generated
   public void setX(float x) {
      this.x = x;
   }

   public void resetTransientState() {
   }

   public boolean handlePriorityMouseButton(float mouseX, float mouseY, int button) {
      return false;
   }

   public void keyPressed(int keyCode) {
   }

   @Generated
   public float getHeight() {
      return this.height;
   }

   public void mouseReleased(float mouseX, float mouseY, int button) {
   }

   public void keyReleased(int keyCode) {
   }

   @Generated
   public void setY(float y) {
      this.y = y;
   }

   @Generated
   public float getX() {
      return this.x;
   }

   @Generated
   public void setHeight(float height) {
      this.height = height;
   }

   @Generated
   public void setWidth(float width) {
      this.width = width;
   }

   public abstract void mouseClicked(float mouseX, float mouseY, int button);

   @Generated
   public float getY() {
      return this.y;
   }

   public boolean isCapturingKeybind() {
      return false;
   }

   @Generated
   public boolean isMouseOver(float mouseX, float mouseY) {
      return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
   }

   public float getWidth() {
      return this.width;
   }

   public SettingWidget(T setting) {
      this.setting = (T)setting;
   }

   public boolean mouseScrolled(float mouseX, float mouseY, double scrollDelta) {
      return false;
   }

   public SettingWidget(T setting, float height) {
      this((T)setting);
      this.height = height;
   }

   protected float clampPopupY(float popupY, float popupHeight) {
      if (Minecraft.getInstance().gui.screen() instanceof ClickGuiScreen screen) {
         float margin = POPUP_VERTICAL_BASE_MARGIN + popupHeight * POPUP_VERTICAL_SIZE_MARGIN_FACTOR;
         float minY = screen.getModuleViewportY() + margin;
         float maxY = screen.getModuleViewportY() + screen.getModuleViewportHeight() - popupHeight - margin;
         if (popupY > maxY) {
            popupY = this.y - popupHeight - POPUP_ABOVE_WIDGET_GAP;
         }

         return Math.clamp(popupY, minY, Math.max(minY, maxY));
      } else {
         return popupY;
      }
   }

   @Generated
   public TickAnimation getVisibilityAnimation() {
      return this.visibilityAnimation;
   }

   public abstract void tick();

   protected float clampPopupX(float popupX, float popupWidth) {
      if (Minecraft.getInstance().gui.screen() instanceof ClickGuiScreen screen) {
         float margin = POPUP_HORIZONTAL_BASE_MARGIN + popupWidth * POPUP_HORIZONTAL_SIZE_MARGIN_FACTOR;
         float minX = screen.getModuleViewportX() + margin;
         float maxX = screen.getModuleViewportX() + screen.getModuleViewportWidth() - popupWidth - margin;
         return Math.clamp(popupX, minX, Math.max(minX, maxX));
      } else {
         return popupX;
      }
   }

   @Generated
   public T getSetting() {
      return this.setting;
   }

   public abstract void render(float mouseX, float mouseY, float tickDelta);
}
