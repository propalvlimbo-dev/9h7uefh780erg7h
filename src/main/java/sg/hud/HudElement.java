package sg.hud;

import lombok.Generated;
import sg.ec.MinecraftAccess;
import sg.hud.HudScaleCoordinates;
import sg.hud.HudDisplacementManager;
import sg.render.pipeline.UiDrawing;

public class HudElement implements MinecraftAccess {
   private final @com.google.gson.annotations.SerializedName("Вц")  String name;
   protected @com.google.gson.annotations.SerializedName("пЗ")  float x;
   protected @com.google.gson.annotations.SerializedName("пЕ")  float baseY;
   protected @com.google.gson.annotations.SerializedName("пП")  float width;
   protected @com.google.gson.annotations.SerializedName("п西")  float height;

   public void popGuiScaleTransform() {
      UiDrawing.popGuiScaleTransform();
   }

   public float getBaseY() {
      return this.baseY;
   }

   public float getScaledWidth() {
      return this.width * HudScaleCoordinates.getHudScale();
   }

   public float getScaledHeight() {
      return this.height * HudScaleCoordinates.getHudScale();
   }

   public float getY() {
      return this.baseY + HudDisplacementManager.getVerticalDisplacement(this);
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public void setHeight(float height) {
      this.height = height;
   }

   @Generated
   public HudElement(String name, float x, float baseY, float width, float height) {
      this.name = name;
      this.x = x;
      this.baseY = baseY;
      this.width = width;
      this.height = height;
   }

   @Generated
   public float getX() {
      return this.x;
   }

   @Generated
   public void setBaseY(float baseY) {
      this.baseY = baseY;
   }

   public float getWidth() {
      return this.width;
   }

   public float getHeight() {
      return this.height;
   }

   public boolean hasRenderableSize() {
      return this.getScaledWidth() > 1.0F && this.getScaledHeight() > 1.0F;
   }

   @Generated
   public void setWidth(float width) {
      this.width = width;
   }

   @Generated
   public void setX(float x) {
      this.x = x;
   }

   public void pushGuiScaleTransform() {
      UiDrawing.pushGuiScaleTransform(this.getX(), this.getY());
   }
}
