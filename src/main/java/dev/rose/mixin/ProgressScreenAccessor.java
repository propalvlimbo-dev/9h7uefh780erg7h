package dev.rose.mixin;

import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(ProgressScreen.class)
public interface ProgressScreenAccessor {
   @Accessor("header")
   Component rose$header();

   @Accessor("stage")
   Component rose$stage();

   @Accessor("progress")
   int rose$progress();
}
