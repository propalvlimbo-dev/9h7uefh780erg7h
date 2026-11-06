package dev.rose.render.gpu;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import org.jetbrains.annotations.Nullable;


public enum Blend {
   NORMAL(BlendFunction.TRANSLUCENT),
   PREMULTIPLIED(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA),
   ADDITIVE(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE, SourceFactor.ZERO, DestFactor.ONE)),
   ADDITIVE_PREMULTIPLIED(new BlendFunction(SourceFactor.ONE, DestFactor.ONE, SourceFactor.ZERO, DestFactor.ONE)),
   LIGHTNING(BlendFunction.LIGHTNING),
   OVERLAY(BlendFunction.OVERLAY),
   REPLACE(null);

   @Nullable
   private final BlendFunction function;

   Blend(@Nullable BlendFunction function) {
      this.function = function;
   }

   @Nullable
   public BlendFunction function() {
      return this.function;
   }
}
