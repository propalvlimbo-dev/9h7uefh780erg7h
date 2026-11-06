package sg.render;

import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import sg.ec.MinecraftAccess;
import sg.render.pipeline.UiDrawing;

public final class WorldProjection implements MinecraftAccess {
   private static final Matrix4f viewMatrix = new Matrix4f();
   private static final Matrix4f projectionMatrix = new Matrix4f();
   private static boolean matricesReady;
   private static final float NDC_TO_SCREEN_X_FACTOR = 0.5F;
   private static final float NDC_TO_SCREEN_Y_FACTOR = 0.5F;
   private static final float SCALE_NDC_X_OFFSET = -1.0F;
   private static final float UNSCALE_NDC_X_OFFSET = -1.0F;
   private static final float FALLBACK_VIEW_Z_LIMIT = -0.15F;
   private static final float FALLBACK_VIEW_Z_CLAMP = -0.15F;
   private static final String UTILITY_CONSTRUCTOR_MESSAGE = "This is a utility class and cannot be instantiated";

   public static Vector2f projectToScreen(Vec3 worldPosition, boolean clipToViewport) {
      try {
         if (matricesReady && MINECRAFT_CLIENT.gameRenderer != null) {
            Vec3 cameraRelativePosition = worldPosition.subtract(MINECRAFT_CLIENT.gameRenderer.mainCamera().position());
            Vector4f clipPosition = new Vector4f((float)cameraRelativePosition.x, (float)cameraRelativePosition.y, (float)cameraRelativePosition.z, 1.0F);
            clipPosition.mul(viewMatrix);
            clipPosition.mul(projectionMatrix);
            if (clipPosition.w <= 0.0F) {
               if (clipToViewport) {
                  return null;
               }

               Vector4f fallbackProjectionPosition = new Vector4f((float)cameraRelativePosition.x, (float)cameraRelativePosition.y, (float)cameraRelativePosition.z, 1.0F);
               fallbackProjectionPosition.mul(viewMatrix);
               if (fallbackProjectionPosition.z > FALLBACK_VIEW_Z_LIMIT) {
                  fallbackProjectionPosition.z = FALLBACK_VIEW_Z_CLAMP;
               }

               fallbackProjectionPosition.w = 1.0F;
               fallbackProjectionPosition.mul(projectionMatrix);
               if (fallbackProjectionPosition.w <= 0.0F) {
                  return null;
               }

               clipPosition = fallbackProjectionPosition;
            }

            float ndcX = clipPosition.x / clipPosition.w;
            float ndcY = clipPosition.y / clipPosition.w;
            if (!clipToViewport || !(Math.abs(ndcX) > 1.0F) && !(Math.abs(ndcY) > 1.0F)) {
               int screenWidth = MINECRAFT_CLIENT.getWindow().getScreenWidth();
               int screenHeight = MINECRAFT_CLIENT.getWindow().getScreenHeight();
               return new Vector2f((ndcX + 1.0F) / 2.0F * screenWidth, (1.0F - ndcY) / 2.0F * screenHeight);
            } else {
               return null;
            }
         } else {
            return null;
         }
      } catch (Exception exception) {
         return null;
      }
   }

   @Generated
   private WorldProjection() {
      throw new UnsupportedOperationException(UTILITY_CONSTRUCTOR_MESSAGE);
   }

   public static Vector2f projectToScreen(Vec3 worldPosition) {
      return projectToScreen(worldPosition, true);
   }

   public static Matrix4f getViewMatrix() {
      return matricesReady ? new Matrix4f(viewMatrix) : null;
   }

   public static void scaleAroundRect(float x, float y, float width, float height, float scale) {
      float ndcWidth = 2.0F / Minecraft.getInstance().getWindow().getScreenWidth() * width;
      float ndcHeight = 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() * height;
      float ndcLeft = SCALE_NDC_X_OFFSET + x * 2.0F / Minecraft.getInstance().getWindow().getScreenWidth();
      float ndcBottom = 1.0F - y * 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() - ndcHeight;
      float ndcCenterX = ndcLeft + ndcWidth / 2.0F;
      float ndcCenterY = ndcBottom + ndcHeight / 2.0F;
      UiDrawing.getTransform().translate(ndcCenterX, ndcCenterY, 0.0F);
      UiDrawing.getTransform().scale(scale, scale, 1.0F);
      UiDrawing.getTransform().translate(-ndcCenterX, -ndcCenterY, 0.0F);
   }

   public static Vector2f guiToScreen(double guiX, double guiY) {
      return new Vector2f((float)(guiX * MINECRAFT_CLIENT.getWindow().getGuiScale()), (float)(guiY * MINECRAFT_CLIENT.getWindow().getGuiScale()));
   }

   public static void unscaleAroundRect(float x, float y, float width, float height, float scale) {
      float ndcWidth = 2.0F / Minecraft.getInstance().getWindow().getScreenWidth() * width;
      float ndcHeight = 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() * height;
      float ndcLeft = UNSCALE_NDC_X_OFFSET + x * 2.0F / Minecraft.getInstance().getWindow().getScreenWidth();
      float ndcBottom = 1.0F - y * 2.0F / Minecraft.getInstance().getWindow().getScreenHeight() - ndcHeight;
      float ndcCenterX = ndcLeft + ndcWidth / 2.0F;
      float ndcCenterY = ndcBottom + ndcHeight / 2.0F;
      UiDrawing.getTransform().translate(ndcCenterX, ndcCenterY, 0.0F);
      UiDrawing.getTransform().scale(1.0F / scale, 1.0F / scale, 1.0F);
      UiDrawing.getTransform().translate(-ndcCenterX, -ndcCenterY, 0.0F);
   }

   public static void setMatrices(Matrix4f sourceViewMatrix, Matrix4f sourceProjectionMatrix) {
      if (sourceViewMatrix != null && sourceProjectionMatrix != null) {
         viewMatrix.set(sourceViewMatrix);
         projectionMatrix.set(sourceProjectionMatrix);
         matricesReady = true;
      } else {
         matricesReady = false;
      }
   }

   public static Vector2f projectToNormalizedScreen(Vec3 worldPosition, boolean clipToViewport) {
      Vector2f screenPosition = projectToScreen(worldPosition, clipToViewport);
      if (screenPosition == null) {
         return null;
      }

      int screenWidth = Math.max(MINECRAFT_CLIENT.getWindow().getScreenWidth(), 1);
      int screenHeight = Math.max(MINECRAFT_CLIENT.getWindow().getScreenHeight(), 1);
      return new Vector2f(screenPosition.x / screenWidth, 1.0F - screenPosition.y / screenHeight);
   }

   public static boolean containsPoint(double pointX, double pointY, double x, double y, double width, double height) {
      return pointX >= x && pointX <= x + width && pointY >= y && pointY <= y + height;
   }

   public static Matrix4f getProjectionMatrix() {
      return matricesReady ? new Matrix4f(projectionMatrix) : null;
   }

   public static Vector2f transformScreenPoint(float screenX, float screenY) {
      float screenWidth = MINECRAFT_CLIENT.getWindow().getScreenWidth();
      float screenHeight = MINECRAFT_CLIENT.getWindow().getScreenHeight();
      Vector4f transformedNdcPosition = new Vector4f(screenX * 2.0F / screenWidth - 1.0F, 1.0F - screenY * 2.0F / screenHeight, 0.0F, 1.0F);
      UiDrawing.getTransform().transform(transformedNdcPosition);
      return new Vector2f((transformedNdcPosition.x + 1.0F) * screenWidth * NDC_TO_SCREEN_X_FACTOR, (1.0F - transformedNdcPosition.y) * screenHeight * NDC_TO_SCREEN_Y_FACTOR);
   }
}
