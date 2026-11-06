package dev.rose.module.impl.visual;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.ChoiceSetting;
import dev.rose.setting.NumberSetting;
import org.joml.Matrix3x2fStack;


public final class ContainerAnimationModule extends Module {
   private final ChoiceSetting<ContainerAnimationModule.Style> style = this.add(new ChoiceSetting<>("style", ContainerAnimationModule.Style.POP));
   private final NumberSetting duration = this.add(new NumberSetting("duration", 260.0, 100.0, 600.0, 20.0, "rose.unit.ms"));

   public ContainerAnimationModule() {
      super("container_animation", Category.VISUALS, false);
   }

   @Override
   public int icon() {
      return 58060;
   }

   public float progress(long openedAt) {
      return !this.isEnabled() ? 1.0F : Math.clamp((float)(System.currentTimeMillis() - openedAt) / this.duration.floatValue(), 0.0F, 1.0F);
   }

   public boolean apply(Matrix3x2fStack pose, float width, float height, float progress) {
      if (this.isEnabled() && !(progress >= 1.0F)) {
         switch ((ContainerAnimationModule.Style)this.style.get()) {
            case POP:
               float t = progress;
               float s = 1.0F + (float)(Math.pow(2.0, -9.0F * t) * Math.sin((t * 8.0F - 0.75) * (Math.PI * 2.0 / 3.0))) * -0.12F;
               scaleAround(pose, width, height, Math.max(0.8F, s));
               break;
            case SLIDE: {
               float eased = 1.0F - (float)Math.pow(1.0F - progress, 3.0);
               pose.translate(0.0F, (1.0F - eased) * 24.0F);
               break;
            }
            case ZOOM: {
               float eased = 1.0F - (float)Math.pow(1.0F - progress, 3.0);
               scaleAround(pose, width, height, 0.85F + 0.15F * eased);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static void scaleAround(Matrix3x2fStack pose, float width, float height, float scale) {
      pose.translate(width / 2.0F, height / 2.0F);
      pose.scale(scale, scale);
      pose.translate(-width / 2.0F, -height / 2.0F);
   }

   public enum Style {
      POP,
      SLIDE,
      ZOOM;
   }
}
