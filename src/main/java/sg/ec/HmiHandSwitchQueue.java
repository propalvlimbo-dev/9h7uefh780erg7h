package sg.ec;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import sg.events.impl.HmiHandRenderContext;
import sg.mixinbridge.HmiClientInitializer;

public class HmiHandSwitchQueue {
   private static final List<HmiHandRenderContext> Т = new ArrayList<>();

   public static HmiHandRenderContext 弟() {
      if (!Т.isEmpty()) {
         HmiHandRenderContext var0 = Т.getFirst();
         Т.remove(var0);
         return var0;
      } else {
         return new HmiHandRenderContext(
            false,
            0.0F,
            Minecraft.getInstance().player,
            InteractionHand.MAIN_HAND,
            false,
            HmiClientInitializer.Юр,
            0.0F,
            0.0F,
            0.0F,
            false,
            false,
            false,
            false,
            false,
            false,
            Items.AIR.getDefaultInstance()
         );
      }
   }

   public static void 弟(HmiHandRenderContext var0, ItemStack var1) {
      if (!var1.isEmpty() && var1.getUseAnimation() != ItemUseAnimation.BLOCK && var1.getUseAnimation() != ItemUseAnimation.TRIDENT) {
         Т.add(var0);
      }
   }

   public static void фс() {
      Т.clear();
   }
}
