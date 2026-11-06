package sg.modules.impl.render;

import com.mojang.blaze3d.textures.FilterMode;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import settings.render.ClientResourceIds;
import sg.events.EventListener;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.HudRenderEvent;

public final class ShulkerView extends ClientModule {
   private static final Identifier ш = ClientResourceIds.getImageId(ShulkerView.ВьМ);
   private static final Vector4f Х_at_bb660d92 = new Vector4f(0.0F, 0.0F, ShulkerView.ВьН, ShulkerView.Вьн);
   private static final int ВМ = 175;
   private static final int ВН = 70;
   private static final int Вн = 27;
   private static ShulkerView В;
   private final EventListener<HudRenderEvent> сп = this::З;
   private static final String ВьХ = "ShulkerView";
   private static final String Вье = "Показывает содержимое шалкеров в инвентаре и на земле";
   private static final float ВьА = 8.0F;
   private static final float Вь4 = 16.0F;
   private static final float Вьп = 175.0F;
   private static final float ВьД = 8.0F;
   private static final float ВьЪ = 175.0F;
   private static final float Вьт = 70.0F;
   private static final float Вьа = 0.5F;
   private static final double ВьЮ = 64.0;
   private static final double ВьЧ = 0.5;
   private static final float ВьЛ = 175.0F;
   private static final float Вь诶 = 0.5F;
   private static final float Вь1 = 70.0F;
   private static final float ВьЬ = 175.0F;
   private static final float Вь衣 = 70.0F;
   private static final float Вьш = 10.0F;
   private static final float ВьИ = 18.0F;
   private static final float Вьь = 10.0F;
   private static final float Вьж = 18.0F;
   private static final float Вь弟 = 0.75F;
   private static final float Вьб = 10.0F;
   private static final float ВьЫ = 18.0F;
   private static final float Вьl = 10.0F;
   private static final float Вь8 = 18.0F;
   private static final float Вь5 = 0.75F;
   private static final float Вьв = 0.75F;
   private static final double Вьъ = 255.0;
   private static final String ВьМ = "shulker/3x9";
   private static final float ВьН = 0.68359375F;
   private static final float Вьн = 0.2734375F;

   public ShulkerView() {
      super(ВьХ, Вье, ModuleCategory.RENDER);
      В = this;
   }

   private void З(HudRenderEvent var1) {
      if (var1.getGraphics() != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gui.screen() == null) {
         float var2 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         float var3 = Вьа * MINECRAFT_CLIENT.getWindow().getGuiScale();

         for (ItemEntity var5 : MINECRAFT_CLIENT.level.getEntitiesOfClass(ItemEntity.class, MINECRAFT_CLIENT.player.getBoundingBox().inflate(ВьЮ), var0 -> true)) {
            ItemStack var6 = var5.getItem();
            if (ь(var6)) {
               ItemContainerContents var7 = var6.get(DataComponents.CONTAINER);
               if (var7 != null) {
                  List var8 = var7.allItemsCopyStream().toList();
                  if (!var8.isEmpty()) {
                     Vec3 var9 = new Vec3(var5.xo, var5.yo, var5.zo).lerp(var5.position(), var2).add(0.0, ВьЧ, 0.0);
                     Vector2f var10 = WorldProjection.projectToScreen(var9);
                     if (var10 != null) {
                        float var11 = var10.x - ВьЛ * var3 * Вь诶;
                        float var12 = var10.y - Вь1 * var3;
                        this.method_1(var1.getGraphics(), var6, var8, var11, var12, var3, false);
                     }
                  }
               }
            }
         }
      }
   }

   private ItemStack method_1(Slot var1) {
      if (var1 != null && var1.hasItem()) {
         return var1.getItem();
      }

      if (MINECRAFT_CLIENT.player == null) {
         return ItemStack.EMPTY;
      }

      ItemStack var2 = MINECRAFT_CLIENT.player.containerMenu.getCarried();
      return var2.isEmpty() ? ItemStack.EMPTY : var2;
   }

   private static int I(ItemStack var0) {
      if (var0.getItem() instanceof BlockItem var1) {
         int var3 = var1.getBlock().defaultMapColor().col;
         return ColorMath.packArgb(var3 >> 16 & 0xFF, var3 >> 8 & 0xFF, var3 & 0xFF, Вьъ);
      } else {
         return ColorMath.packRgb(152, 106, 155);
      }
   }

   private static boolean ь(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && var0.getItem() instanceof BlockItem var1 && var1.getBlock() instanceof ShulkerBoxBlock;
   }

   private void е(GuiGraphicsExtractor var1, Slot var2, int var3, int var4) {
      ItemStack var5 = this.method_1(var2);
      if (ь(var5)) {
         ItemContainerContents var6 = var5.get(DataComponents.CONTAINER);
         if (var6 != null) {
            float var7 = var3 + ВьА;
            float var8 = var4 - 70 - Вь4;
            float var9 = MINECRAFT_CLIENT.getWindow().getGuiScaledWidth();
            float var10 = MINECRAFT_CLIENT.getWindow().getGuiScaledHeight();
            if (var7 + Вьп > var9) {
               var7 = var3 - 175 - ВьД;
            }

            var7 = Math.clamp(var7, 0.0F, Math.max(0.0F, var9 - ВьЪ));
            var8 = Math.clamp(var8, 0.0F, Math.max(0.0F, var10 - Вьт));
            this.method_1(var1, var5, var6.allItemsCopyStream().toList(), var7, var8);
         }
      }
   }

   private void method_1(GuiGraphicsExtractor var1, ItemStack var2, List<ItemStack> var3, float var4, float var5) {
      var1.nextStratum();
      var1.pose().pushMatrix();
      var1.pose().translate(var4, var5);
      var1.blit(RenderPipelines.GUI_TEXTURED, ш, 0, 0, 0.0F, 0.0F, 175, 70, 256, 256, I(var2));

      for (int var6 = 0; var6 < Math.min(var3.size(), 27); var6++) {
         ItemStack var7 = (ItemStack)var3.get(var6);
         if (var7 != null && !var7.isEmpty()) {
            float var8 = Вьб + var6 % 9 * ВьЫ;
            float var9 = Вьl + var6 / 9 * Вь8;
            var1.pose().pushMatrix();
            var1.pose().translate(var8, var9);
            var1.pose().scale(Вь5, Вьв);
            var1.item(var7, 0, 0);
            var1.itemDecorations(MINECRAFT_CLIENT.font, var7, 0, 0);
            var1.pose().popMatrix();
         }
      }

      var1.pose().popMatrix();
   }

   public static void method_1(GuiGraphicsExtractor var0, Slot var1, int var2, int var3) {
      if (В != null && В.isEnabled()) {
         В.е(var0, var1, var2, var3);
      }
   }

   private void method_1(GuiGraphicsExtractor var1, ItemStack var2, List<ItemStack> var3, float var4, float var5, float var6, boolean var7) {
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.drawTexture(var4, var5, ВьЬ * var6, Вь衣 * var6, 0.0F, Х_at_bb660d92, I(var2), ш, FilterMode.NEAREST);

      for (int var8 = 0; var8 < Math.min(var3.size(), 27); var8++) {
         ItemStack var9 = (ItemStack)var3.get(var8);
         if (var9 != null && !var9.isEmpty()) {
            float var10 = var4 + (Вьш + var8 % 9 * ВьИ) * var6;
            float var11 = var5 + (Вьь + var8 / 9 * Вьж) * var6;
            UiDrawing.drawItem(var1, var9, var10, var11, Вь弟 * var6, var7);
         }
      }
   }
}
