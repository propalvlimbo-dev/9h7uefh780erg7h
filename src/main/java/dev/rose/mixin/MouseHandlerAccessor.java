package dev.rose.mixin;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(MouseHandler.class)
public interface MouseHandlerAccessor {
   @Accessor("xpos")
   void rose$setX(double var1);

   @Accessor("ypos")
   void rose$setY(double var1);
}
