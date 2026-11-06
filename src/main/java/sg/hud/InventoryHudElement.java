package sg.hud;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.modules.impl.render.Hud;
import sg.client.RoseClient;
import sg.hud.HudElement;
import sg.events.impl.HudRenderEvent;
import sg.client.ClientServices;
import sg.render.RoseBatchedItemAtlas;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.animation.Easings;
import sg.animation.FloatAnimation;

public final class InventoryHudElement extends HudElement {
   private final @com.google.gson.annotations.SerializedName("э")  Matrix4f transform = new Matrix4f();
   @SerializedName("А")
   private final FloatAnimation[] presenceAnimations = new FloatAnimation[27];
   @SerializedName("4")
   private final FloatAnimation[] countPulseAnimations = new FloatAnimation[27];
   private final @com.google.gson.annotations.SerializedName("ъ")  int[] itemCounts = new int[27];
   private final @com.google.gson.annotations.SerializedName("Х")  RoseBatchedItemAtlas.ItemHandle[] itemHandles = new RoseBatchedItemAtlas.ItemHandle[27];
   private final @com.google.gson.annotations.SerializedName("Хш")  EventListener<HudRenderEvent> hudRenderListener = this::renderInventory;
   private static final String ELEMENT_NAME = "Inventory";
   private static final float INITIAL_X = 28.0F;
   private static final float INITIAL_Y = 190.0F;
   private static final float INITIAL_WIDTH = 290.0F;
   private static final float INITIAL_HEIGHT = 104.0F;
   private static final float PRESENCE_ANIMATION_SPEED = 12.0F;
   private static final float COUNT_PULSE_SPEED = 16.0F;
   private static final float WINDOW_WIDTH = 290.0F;
   private static final float WINDOW_HEIGHT = 104.0F;
   private static final float WINDOW_PADDING = 7.0F;
   private static final float SLOT_SIZE = 28.0F;
   private static final float SLOT_GAP = 3.0F;
   private static final float WINDOW_CORNER_RADIUS = 14.0F;
   private static final float MIN_PRESENCE_AMOUNT = 0.01F;
   private static final float ITEM_SCALE = 1.25F;
   private static final float BASE_SCALE_WEIGHT = 0.72F;
   private static final float PRESENCE_SCALE_WEIGHT = 0.28F;
   private static final float COUNT_PULSE_SCALE_WEIGHT = 0.1F;
   private static final float BASE_ITEM_SIZE = 16.0F;
   private static final float ITEM_CENTER_X_FACTOR = 0.5F;
   private static final float ITEM_CENTER_Y_FACTOR = 0.5F;
   private static final String HUD_OPTION_NAME = "Инвентарь";

   private void renderInventory(HudRenderEvent event) {
      if (!this.isVisible()) {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else if (MINECRAFT_CLIENT.gui.hud.isHidden() && !(MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen)) {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else {
         float x = Math.round(this.getX());
         float y = Math.round(this.getY());
         float width = WINDOW_WIDTH;
         float height = WINDOW_HEIGHT;
         float padding = WINDOW_PADDING;
         float slotSize = SLOT_SIZE;
         float slotGap = SLOT_GAP;
         UiDrawing.setTransform(this.transform.identity());
         this.pushGuiScaleTransform();
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());

         try {
            UiBatchDrawing.drawHudWindowBackground(x, y, width, height, WINDOW_CORNER_RADIUS);
            net.minecraft.world.entity.player.Inventory playerInventory = MINECRAFT_CLIENT.player.getInventory();

            for (int slotIndex = 0; slotIndex < this.presenceAnimations.length; slotIndex++) {
               int column = slotIndex % 9;
               int row = slotIndex / 9;
               float slotX = x + padding + column * (slotSize + slotGap);
               float slotY = y + padding + row * (slotSize + slotGap);
               ItemStack stack = playerInventory.getItem(9 + slotIndex);
               boolean occupied = stack != null && !stack.isEmpty();
               this.presenceAnimations[slotIndex].updateTarget(occupied ? 1.0F : 0.0F);
               float presenceAmount = Math.clamp(this.presenceAnimations[slotIndex].getValue(), 0.0F, 1.0F);
               int itemCount = occupied ? stack.getCount() : 0;
               if (this.itemCounts[slotIndex] != itemCount) {
                  this.itemCounts[slotIndex] = itemCount;
                  this.countPulseAnimations[slotIndex].setImmediately(1.0F);
               }

               this.countPulseAnimations[slotIndex].updateTarget(0.0F);
               float countPulse = Math.clamp(this.countPulseAnimations[slotIndex].getValue(), 0.0F, 1.0F);
               if (occupied && !(presenceAmount <= MIN_PRESENCE_AMOUNT)) {
                  float itemScale = ITEM_SCALE * (BASE_SCALE_WEIGHT + presenceAmount * PRESENCE_SCALE_WEIGHT + countPulse * COUNT_PULSE_SCALE_WEIGHT);
                  float itemSize = BASE_ITEM_SIZE * itemScale;
                  UiBatchDrawing.drawItemWithOverlay(slotX + (slotSize - itemSize) * ITEM_CENTER_X_FACTOR, slotY + (slotSize - itemSize) * ITEM_CENTER_Y_FACTOR, itemSize, itemSize, stack, false, this.itemHandles[slotIndex]);
               }
            }
         } finally {
            UiBatchDrawing.endBatch(ClientFonts.getMediumFont());
            this.popGuiScaleTransform();
         }

         this.setWidth(width);
         this.setHeight(height);
      }
   }

   public InventoryHudElement() {
      super(ELEMENT_NAME, INITIAL_X, INITIAL_Y, INITIAL_WIDTH, INITIAL_HEIGHT);
      Arrays.fill(this.itemCounts, -1);

      for (int slotIndex = 0; slotIndex < this.presenceAnimations.length; slotIndex++) {
         this.presenceAnimations[slotIndex] = new FloatAnimation(0.0F, PRESENCE_ANIMATION_SPEED, Easings.CUBIC_OUT);
         this.countPulseAnimations[slotIndex] = new FloatAnimation(0.0F, COUNT_PULSE_SPEED, Easings.CUBIC_OUT);
         this.itemHandles[slotIndex] = new RoseBatchedItemAtlas.ItemHandle();
      }
   }

   private boolean isVisible() {
      try {
         if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
            ClientServices services = RoseClient.getInstance().getServices();
            if (services != null && services.getHudElements().isEnabled()) {
               Hud hud = services.getModules().getHud();
               return hud != null && hud.isEnabled() && hud.я().isSelected(HUD_OPTION_NAME);
            } else {
               return false;
            }
         } else {
            return false;
         }
      } catch (Throwable failure) {
         return false;
      }
   }
}
