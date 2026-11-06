package sg.events.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import sg.events.ClientEvent;

public record HeldItemPoseEvent(PoseStack matrices, ItemStack stack, InteractionHand hand) implements ClientEvent {
}
