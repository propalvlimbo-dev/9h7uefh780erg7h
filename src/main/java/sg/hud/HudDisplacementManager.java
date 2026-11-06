package sg.hud;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import sg.events.impl.UiAnimationEvents;
import sg.mixin.BossHealthOverlayAccessor;
import sg.hud.HudElementRegistry;
import sg.client.RoseClient;
import sg.hud.HudElement;
import sg.hud.Scoreboard;
import sg.animation.Easings;
import sg.animation.FloatAnimation;

public final class HudDisplacementManager {
   private static final float Ап = 8.0F;
   private static final float АД = 0.58F;
   private static final int еш = 12;
   private static final int еИ = 5;
   private static final FloatAnimation playerListVisibilityAnimation = new FloatAnimation(0.0F, HudDisplacementManager.PLAYER_LIST_VISIBILITY_ANIMATION_SPEED, Easings.CUBIC_OUT);
   private static final FloatAnimation obstructionBottomAnimation = new FloatAnimation(0.0F, HudDisplacementManager.OBSTRUCTION_BOTTOM_ANIMATION_SPEED, Easings.CUBIC_OUT);
   private static final Map<HudElement, Float> elementDisplacements = new IdentityHashMap<>();
   private static float playerListLeft;
   private static float playerListTop;
   private static float playerListRight;
   private static float playerListBottom;
   private static boolean hasPlayerListBounds;
   private static float animatedObstructionBottom;
   private static float bossBarLeft;
   private static float bossBarTop;
   private static float bossBarRight;
   private static float bossBarBottom;
   private static boolean hasBossBarBounds;
   private static boolean displacementEnabled;
   private static boolean collectingPlayerListBounds;
   private static float collectedPlayerListLeft = Float.POSITIVE_INFINITY;
   private static float collectedPlayerListTop = Float.POSITIVE_INFINITY;
   private static float collectedPlayerListRight = Float.NEGATIVE_INFINITY;
   private static float collectedPlayerListBottom = Float.NEGATIVE_INFINITY;
   private static final float PLAYER_LIST_HIDE_THRESHOLD = 0.001F;
   private static final float PLAYER_LIST_REMAINING_VISIBILITY_THRESHOLD = 0.001F;
   private static final float PLAYER_LIST_OBSTRUCTION_VISIBILITY_THRESHOLD = 0.001F;
   private static final float PLAYER_LIST_VISIBLE_QUERY_THRESHOLD = 0.001F;
   private static final float PLAYER_LIST_DISPLACEMENT_VISIBILITY_THRESHOLD = 0.001F;
   private static final float PLAYER_LIST_DISPLACEMENT_PADDING = 8.0F;
   private static final float PLAYER_LIST_DISPLACEMENT_BOTTOM_MARGIN = 8.0F;
   private static final float MIN_CAPTURED_PLAYER_LIST_WIDTH = 8.0F;
   private static final float MIN_CAPTURED_PLAYER_LIST_HEIGHT = 4.0F;
   private static final float BOSS_BAR_PLAYER_LIST_DISPLACEMENT_START_Y = 3.0F;
   private static final float BOSS_BAR_ROW_HEIGHT = 19.0F;
   private static final float BOSS_BAR_HALF_WIDTH = 91.0F;
   private static final float BOSS_BAR_SCREEN_CENTER_FACTOR = 0.5F;
   private static final float BOSS_BAR_TOP_OFFSET = 12.0F;
   private static final String EXCLUDED_HUD_ELEMENT_NAME = "hud_without_config";
   private static final float ELEMENT_CENTER_Y_FACTOR = 0.5F;
   private static final float PLAYER_LIST_GROUP_SPLIT_HEIGHT_FACTOR = 0.58F;
   private static final float GROUP_DISPLACEMENT_PADDING = 8.0F;
   private static final float GROUP_BOTTOM_MARGIN = 8.0F;
   private static final float MIN_GROUP_DISPLACEMENT = 0.01F;
   private static final float RECTANGLE_DISPLACEMENT_PADDING = 8.0F;
   private static final float RECTANGLE_BOTTOM_MARGIN = 8.0F;
   private static final float PLAYER_LIST_INTERSECTION_VISIBILITY_THRESHOLD = 0.001F;
   private static final float INTERSECTION_BOTTOM_PADDING = 8.0F;
   private static final float PLAYER_LIST_VISIBILITY_ANIMATION_SPEED = 14.0F;
   private static final float OBSTRUCTION_BOTTOM_ANIMATION_SPEED = 14.0F;

   private static boolean overlapsBossBarBounds(float x, float y, float width, float height) {
      return hasBossBarBounds && !(bossBarBottom <= 1.0F) ? intersectsPaddedBounds(x, y, width, height, bossBarLeft, bossBarTop, bossBarRight, bossBarBottom) : false;
   }

   private static float calculateVerticalDisplacement(float x, float y, float width, float height, float screenHeight) {
      if (!overlapsObstructionBounds(x, y, width, height)) {
         return 0.0F;
      }

      float intersectingObstructionBottom = 0.0F;
      if (overlapsPlayerListBounds(x, y, width, height)) {
         intersectingObstructionBottom = Math.max(intersectingObstructionBottom, playerListBottom);
      }

      if (overlapsBossBarBounds(x, y, width, height)) {
         intersectingObstructionBottom = Math.max(intersectingObstructionBottom, bossBarBottom);
      }

      float requiredDisplacement = Math.max(0.0F, Math.min(animatedObstructionBottom, intersectingObstructionBottom) + RECTANGLE_DISPLACEMENT_PADDING - y);
      float availableDisplacement = Math.max(0.0F, screenHeight - RECTANGLE_BOTTOM_MARGIN - (y + height));
      return Math.min(requiredDisplacement, availableDisplacement);
   }

   public static float calculateDisplacementWithWindowScale(float x, float y, float width, float height) {
      Minecraft client = Minecraft.getInstance();
      if (client.getWindow() == null) {
         return 0.0F;
      }

      float windowScale = Math.max(1.0F, client.getWindow().getGuiScale());
      return calculateVerticalDisplacement(x * windowScale, y * windowScale, width * windowScale, height * windowScale) / windowScale;
   }

   private static boolean overlapsObstructionBounds(float x, float y, float width, float height) {
      return overlapsPlayerListBounds(x, y, width, height) || overlapsBossBarBounds(x, y, width, height);
   }

   public static void finishPlayerListBoundsCapture() {
      collectingPlayerListBounds = false;
      if (collectedPlayerListRight > collectedPlayerListLeft && collectedPlayerListBottom > collectedPlayerListTop) {
         Minecraft client = Minecraft.getInstance();
         if (client.getWindow() != null) {
            float windowScale = client.getWindow().getGuiScale();
            playerListLeft = collectedPlayerListLeft * windowScale;
            playerListTop = collectedPlayerListTop * windowScale;
            playerListRight = collectedPlayerListRight * windowScale;
            playerListBottom = collectedPlayerListBottom * windowScale;
            hasPlayerListBounds = true;
         }
      }
   }

   public static void beginPlayerListBoundsCapture() {
      if (!displacementEnabled) {
         collectingPlayerListBounds = false;
      } else {
         collectingPlayerListBounds = true;
         collectedPlayerListLeft = Float.POSITIVE_INFINITY;
         collectedPlayerListTop = Float.POSITIVE_INFINITY;
         collectedPlayerListRight = Float.NEGATIVE_INFINITY;
         collectedPlayerListBottom = Float.NEGATIVE_INFINITY;
      }
   }

   public static float calculateVerticalDisplacement(float x, float y, float width, float height) {
      if (isDisplacementEnabled() && !(animatedObstructionBottom <= 1.0F) && !(width <= 1.0F) && !(height <= 1.0F)) {
         Minecraft client = Minecraft.getInstance();
         return client.getWindow() == null ? 0.0F : calculateVerticalDisplacement(x, y, width, height, client.getWindow().getScreenHeight());
      } else {
         return 0.0F;
      }
   }

   public static float getVerticalDisplacement(HudElement element) {
      if (isDisplacementEnabled() && element != null) {
         Float displacement = elementDisplacements.get(element);
         return displacement == null ? 0.0F : displacement;
      } else {
         return 0.0F;
      }
   }

   private static void updateElementDisplacements(Minecraft client) {
      HudElementRegistry hudElements;
      try {
         hudElements = RoseClient.getInstance().getServices().getHudElements();
      } catch (Throwable ignored) {
         return;
      }

      if (hudElements != null) {
         float screenHeight = client.getWindow().getScreenHeight();
         ArrayList upperPlayerListElements = new ArrayList();
         ArrayList lowerPlayerListElements = new ArrayList();
         ArrayList bossBarOnlyElements = new ArrayList();

         for (HudElement element : hudElements) {
            if (!EXCLUDED_HUD_ELEMENT_NAME.equals(element.getName()) && !(element instanceof Scoreboard)) {
               float scaledWidth = element.getScaledWidth();
               float scaledHeight = element.getScaledHeight();
               if (!(scaledWidth <= 1.0F) && !(scaledHeight <= 1.0F)) {
                  float baseY = element.getBaseY();
                  float x = element.getX();
                  boolean overlapsPlayerList = overlapsPlayerListBounds(x, baseY, scaledWidth, scaledHeight);
                  boolean overlapsBossBars = overlapsBossBarBounds(x, baseY, scaledWidth, scaledHeight);
                  if (overlapsPlayerList || overlapsBossBars) {
                     if (overlapsPlayerList) {
                        if (baseY + scaledHeight * ELEMENT_CENTER_Y_FACTOR > screenHeight * PLAYER_LIST_GROUP_SPLIT_HEIGHT_FACTOR) {
                           lowerPlayerListElements.add(element);
                        } else {
                           upperPlayerListElements.add(element);
                        }
                     } else {
                        bossBarOnlyElements.add(element);
                     }
                  }
               }
            }
         }

         assignGroupDisplacement(upperPlayerListElements, screenHeight);
         assignGroupDisplacement(lowerPlayerListElements, screenHeight);
         assignGroupDisplacement(bossBarOnlyElements, screenHeight);
      }
   }

   public static boolean isDisplacementEnabled() {
      return displacementEnabled;
   }

   private static float calculateBossBarBottom(Minecraft client) {
      if (client.gui.hud != null && client.getWindow() != null) {
         int bossBarCount;
         try {
            bossBarCount = ((BossHealthOverlayAccessor)client.gui.hud.getBossOverlay()).getBossBars().size();
         } catch (Throwable ignored) {
            return 0.0F;
         }

         if (bossBarCount <= 0) {
            return 0.0F;
         }

         float windowScale = client.getWindow().getGuiScale();
         int rowHeight = 10 + Math.max(1, 9);
         int rowLimitY = Math.max(rowHeight, client.getWindow().getGuiScaledHeight() / 3);
         int visibleRowCount = 0;
         int nextRowY = 12;

         for (int rowIndex = 0; rowIndex < bossBarCount; rowIndex++) {
            visibleRowCount++;
            nextRowY += rowHeight;
            if (nextRowY >= rowLimitY) {
               break;
            }
         }

         return (12 + (visibleRowCount - 1) * rowHeight + 5) * windowScale;
      } else {
         return 0.0F;
      }
   }

   public static boolean isPlayerListObstructionVisible() {
      return displacementEnabled && playerListVisibilityAnimation.getValue() > PLAYER_LIST_VISIBLE_QUERY_THRESHOLD && hasPlayerListBounds;
   }

   public static void updateDisplacements() {
      Minecraft client = Minecraft.getInstance();
      displacementEnabled = queryDisplacementEnabled();
      if (!displacementEnabled) {
         playerListVisibilityAnimation.setImmediately(0.0F);
         obstructionBottomAnimation.setImmediately(0.0F);
         elementDisplacements.clear();
         hasPlayerListBounds = false;
         hasBossBarBounds = false;
         animatedObstructionBottom = 0.0F;
         collectingPlayerListBounds = false;
      } else {
         boolean playerListVisible = isPlayerListVisible(client);
         playerListVisibilityAnimation.updateTarget(playerListVisible && hasPlayerListBounds ? 1.0F : 0.0F);
         if (!playerListVisible && playerListVisibilityAnimation.getValue() <= PLAYER_LIST_HIDE_THRESHOLD) {
            hasPlayerListBounds = false;
         }

         boolean playerListVisibleOrFading = playerListVisible || playerListVisibilityAnimation.getValue() > PLAYER_LIST_REMAINING_VISIBILITY_THRESHOLD;
         updateBossBarBounds(client, playerListVisibleOrFading);
         float obstructionBottom = 0.0F;
         if (hasPlayerListBounds && playerListVisibilityAnimation.getValue() > PLAYER_LIST_OBSTRUCTION_VISIBILITY_THRESHOLD) {
            obstructionBottom = Math.max(obstructionBottom, playerListBottom);
         }

         if (hasBossBarBounds) {
            obstructionBottom = Math.max(obstructionBottom, bossBarBottom);
         }

         obstructionBottomAnimation.updateTarget(obstructionBottom);
         animatedObstructionBottom = obstructionBottomAnimation.getValue();
         elementDisplacements.clear();
         if (displacementEnabled && !(animatedObstructionBottom <= 1.0F) && client.getWindow() != null) {
            updateElementDisplacements(client);
         }
      }
   }

   private HudDisplacementManager() {
   }

   private static boolean overlapsPlayerListBounds(float x, float y, float width, float height) {
      return hasPlayerListBounds && !(playerListVisibilityAnimation.getValue() <= PLAYER_LIST_INTERSECTION_VISIBILITY_THRESHOLD) ? intersectsPaddedBounds(x, y, width, height, playerListLeft, playerListTop, playerListRight, playerListBottom) : false;
   }

   private static boolean intersectsPaddedBounds(float x, float y, float width, float height, float left, float top, float right, float bottom) {
      return x < right && x + width > left && y < bottom + INTERSECTION_BOTTOM_PADDING && y + height > top;
   }

   public static float calculatePlayerListDisplacement(float y, float height) {
      if (!displacementEnabled) {
         return 0.0F;
      }

      float visibility = playerListVisibilityAnimation.getValue();
      if (!(visibility <= PLAYER_LIST_DISPLACEMENT_VISIBILITY_THRESHOLD) && hasPlayerListBounds) {
         Minecraft client = Minecraft.getInstance();
         if (client.getWindow() == null) {
            return 0.0F;
         }

         float windowScale = Math.max(1.0F, client.getWindow().getGuiScale());
         float paddedPlayerListBottom = playerListBottom / windowScale + PLAYER_LIST_DISPLACEMENT_PADDING / windowScale;
         float requiredDisplacement = paddedPlayerListBottom - y;
         if (requiredDisplacement <= 0.0F) {
            return 0.0F;
         }

         float availableDisplacement = client.getWindow().getGuiScaledHeight() - PLAYER_LIST_DISPLACEMENT_BOTTOM_MARGIN / windowScale - (y + height);
         return Math.min(requiredDisplacement, Math.max(0.0F, availableDisplacement)) * visibility;
      } else {
         return 0.0F;
      }
   }

   private static void assignGroupDisplacement(List<HudElement> elements, float screenHeight) {
      if (!elements.isEmpty()) {
         float groupTop = Float.POSITIVE_INFINITY;
         float groupBottom = Float.NEGATIVE_INFINITY;
         float intersectingObstructionBottom = 0.0F;

         for (HudElement element : elements) {
            float x = element.getX();
            float baseY = element.getBaseY();
            float scaledWidth = element.getScaledWidth();
            float scaledHeight = element.getScaledHeight();
            groupTop = Math.min(groupTop, baseY);
            groupBottom = Math.max(groupBottom, baseY + scaledHeight);
            if (overlapsPlayerListBounds(x, baseY, scaledWidth, scaledHeight)) {
               intersectingObstructionBottom = Math.max(intersectingObstructionBottom, playerListBottom);
            }

            if (overlapsBossBarBounds(x, baseY, scaledWidth, scaledHeight)) {
               intersectingObstructionBottom = Math.max(intersectingObstructionBottom, bossBarBottom);
            }
         }

         float displacement = Math.max(0.0F, Math.min(animatedObstructionBottom, intersectingObstructionBottom) + GROUP_DISPLACEMENT_PADDING - groupTop);
         float availableDisplacement = Math.max(0.0F, screenHeight - GROUP_BOTTOM_MARGIN - groupBottom);
         displacement = Math.min(displacement, availableDisplacement);
         if (!(displacement <= MIN_GROUP_DISPLACEMENT)) {
            for (HudElement elementToDisplace : elements) {
               elementDisplacements.put(elementToDisplace, displacement);
            }
         }
      }
   }

   public static void capturePlayerListRectangle(int x1, int y1, int x2, int y2) {
      if (collectingPlayerListBounds) {
         float left = Math.min(x1, x2);
         float right = Math.max(x1, x2);
         float top = Math.min(y1, y2);
         float bottom = Math.max(y1, y2);
         if (!(right - left < MIN_CAPTURED_PLAYER_LIST_WIDTH) && !(bottom - top < MIN_CAPTURED_PLAYER_LIST_HEIGHT)) {
            collectedPlayerListLeft = Math.min(collectedPlayerListLeft, left);
            collectedPlayerListTop = Math.min(collectedPlayerListTop, top);
            collectedPlayerListRight = Math.max(collectedPlayerListRight, right);
            collectedPlayerListBottom = Math.max(collectedPlayerListBottom, bottom);
         }
      }
   }

   private static boolean queryDisplacementEnabled() {
      try {
         UiAnimationEvents.HudDisplacementQuery query = new UiAnimationEvents.HudDisplacementQuery();
         RoseClient.getInstance().getEventBus().post(query);
         return query.isDisplacementEnabled();
      } catch (Throwable ignored) {
         return false;
      }
   }

   private static void updateBossBarBounds(Minecraft client, boolean applyPlayerListDisplacement) {
      hasBossBarBounds = false;
      bossBarLeft = 0.0F;
      bossBarTop = 0.0F;
      bossBarRight = 0.0F;
      bossBarBottom = 0.0F;
      if (client.getWindow() != null) {
         int bossBarCount;
         try {
            if (client.gui.hud == null) {
               return;
            }

            bossBarCount = ((BossHealthOverlayAccessor)client.gui.hud.getBossOverlay()).getBossBars().size();
         } catch (Throwable ignored) {
            return;
         }

         if (bossBarCount > 0) {
            float bottom = calculateBossBarBottom(client);
            if (!(bottom <= 0.0F)) {
               float windowScale = client.getWindow().getGuiScale();
               float playerListDisplacement = 0.0F;
               if (applyPlayerListDisplacement && hasPlayerListBounds) {
                  playerListDisplacement = calculatePlayerListDisplacement(BOSS_BAR_PLAYER_LIST_DISPLACEMENT_START_Y, bossBarCount * BOSS_BAR_ROW_HEIGHT) * windowScale;
               }

               float halfWidth = BOSS_BAR_HALF_WIDTH * windowScale;
               float centerX = client.getWindow().getScreenWidth() * BOSS_BAR_SCREEN_CENTER_FACTOR;
               bossBarLeft = centerX - halfWidth;
               bossBarRight = centerX + halfWidth;
               bossBarTop = BOSS_BAR_TOP_OFFSET * windowScale + playerListDisplacement;
               bossBarBottom = bottom + playerListDisplacement;
               hasBossBarBounds = bossBarRight > bossBarLeft && bossBarBottom > bossBarTop;
            }
         }
      }
   }

   private static boolean isPlayerListVisible(Minecraft client) {
      if (client.player == null || client.options == null || client.getConnection() == null || client.level == null) {
         return false;
      }

      if (!client.options.keyPlayerList.isDown()) {
         return false;
      }

      if (!client.isLocalServer()) {
         return true;
      }

      int listedPlayerCount = client.getConnection().getListedOnlinePlayers().size();
      Objective playerListObjective = client.level.getScoreboard().getDisplayObjective(DisplaySlot.LIST);
      return listedPlayerCount > 1 || playerListObjective != null;
   }
}
