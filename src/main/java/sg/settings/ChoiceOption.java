package sg.settings;

import com.google.gson.annotations.SerializedName;
import java.util.function.BooleanSupplier;
import lombok.Generated;
import sg.animation.FloatAnimation;
import sg.modules.impl.init.TickAnimation;

public class ChoiceOption {
   @SerializedName("5")
   private final FloatAnimation selectionAnimation;
   private final @com.google.gson.annotations.SerializedName("Х")  TickAnimation visibilityAnimation;
   @SerializedName("В")
   private final Keybind keybind;
   private final @com.google.gson.annotations.SerializedName("м")  String name;
   private @com.google.gson.annotations.SerializedName("О")  boolean selected;
   @SerializedName("В")
   private BooleanSupplier visibilityCondition;
   private static final float SELECTION_ANIMATION_SPEED = 10.0F;
   private static final float DEFAULT_SELECTION_ANIMATION_SPEED = 10.0F;

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public TickAnimation getVisibilityAnimation() {
      return this.visibilityAnimation;
   }

   @Generated
   public void setSelected(boolean selected) {
      this.selected = selected;
   }

   @Generated
   public void setVisibilityCondition(BooleanSupplier visibilityCondition) {
      this.visibilityCondition = visibilityCondition;
   }

   public ChoiceOption visibleWhen(BooleanSupplier visibilityCondition) {
      this.visibilityCondition = visibilityCondition;
      return this;
   }

   @Generated
   public FloatAnimation getSelectionAnimation() {
      return this.selectionAnimation;
   }

   @Generated
   public boolean isSelected() {
      return this.selected;
   }

   @Generated
   public ChoiceOption(String name) {
      this.selectionAnimation = new FloatAnimation(0.0F, DEFAULT_SELECTION_ANIMATION_SPEED);
      this.visibilityAnimation = new TickAnimation();
      this.keybind = new Keybind(KeybindMode.TOGGLE, -1);
      this.visibilityCondition = () -> true;
      this.name = name;
   }

   @Generated
   public BooleanSupplier getVisibilityCondition() {
      return this.visibilityCondition;
   }

   public ChoiceOption(String name, boolean selected) {
      this.selectionAnimation = new FloatAnimation(0.0F, SELECTION_ANIMATION_SPEED);
      this.visibilityAnimation = new TickAnimation();
      this.keybind = new Keybind(KeybindMode.TOGGLE, -1);
      this.visibilityCondition = () -> true;
      this.name = name;
      this.selected = selected;
   }

   @Generated
   public Keybind getKeybind() {
      return this.keybind;
   }
}
