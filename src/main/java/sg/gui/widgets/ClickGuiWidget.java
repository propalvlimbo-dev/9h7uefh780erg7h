package sg.gui.widgets;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import sg.gui.click.ClickGuiScreen;

public abstract class ClickGuiWidget {
   @SerializedName("4")
   protected final ClickGuiScreen screen;
   protected @com.google.gson.annotations.SerializedName("ЪИ")  float x;
   protected @com.google.gson.annotations.SerializedName("Ъь")  float y;
   protected @com.google.gson.annotations.SerializedName("Ъж")  float width;
   protected @com.google.gson.annotations.SerializedName("Ъ弟")  float height;

   @Generated
   public void setX(float x) {
      this.x = x;
   }

   public void mouseReleased(float mouseX, float mouseY, int button) {
   }

   public void keyReleased(int keyCode) {
   }

   public abstract void tick();

   @Generated
   public void setY(float y) {
      this.y = y;
   }

   public boolean handlePriorityMouseButton(float mouseX, float mouseY, int button) {
      return false;
   }

   public abstract void mouseClicked(float mouseX, float mouseY, int button);

   @Generated
   public float getX() {
      return this.x;
   }

   protected ClickGuiWidget(ClickGuiScreen screen) {
      this.screen = screen;
   }

   public abstract void render(float mouseX, float mouseY, float tickDelta);

   @Generated
   public float getY() {
      return this.y;
   }

   @Generated
   public float getHeight() {
      return this.height;
   }

   public void keyPressed(int keyCode) {
   }

   @Generated
   public void setWidth(float width) {
      this.width = width;
   }

   public void renderOverlay(float mouseX, float mouseY, float tickDelta) {
   }

   @Generated
   public float getWidth() {
      return this.width;
   }

   public void charTyped(String text, int codePoint, int modifiers) {
   }

   @Generated
   public void setHeight(float height) {
      this.height = height;
   }
}
