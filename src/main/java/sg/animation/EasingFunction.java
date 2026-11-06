package sg.animation;

@FunctionalInterface
public interface EasingFunction {
   double apply(double progress);
}
