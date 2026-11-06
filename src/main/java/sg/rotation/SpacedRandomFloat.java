package sg.rotation;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import lombok.Generated;

public class SpacedRandomFloat {
   private final @com.google.gson.annotations.SerializedName("衣")  Set<Float> occupiedValues;
   private final @com.google.gson.annotations.SerializedName("Яб")  float minimumSpacing;
   private final @com.google.gson.annotations.SerializedName("ЯЫ")  float minimumValue;
   private final @com.google.gson.annotations.SerializedName("Яl")  float maximumValue;
   private @com.google.gson.annotations.SerializedName("Я8")  float currentValue = 1.0F;
   private final @com.google.gson.annotations.SerializedName("ВЩ")  int resetThreshold;
   private static final float OCCUPIED_VALUE_STEP = 0.01F;

   public SpacedRandomFloat(float minimumValue, float maximumValue, float minimumSpacing) {
      this.occupiedValues = new HashSet<>();
      this.minimumSpacing = minimumSpacing;
      this.minimumValue = minimumValue;
      this.maximumValue = maximumValue;
      this.resetThreshold = (int)((maximumValue - minimumValue) / minimumSpacing);
   }

   @Generated
   public float getCurrentValue() {
      return this.currentValue;
   }

   public void chooseNext() {
      Random random = new Random();
      if (this.occupiedValues.size() >= this.resetThreshold) {
         this.occupiedValues.clear();
      }

      float candidate;
      do {
         candidate = this.minimumValue + random.nextFloat() * (this.maximumValue - this.minimumValue);
      } while (!this.isAvailable(candidate));

      for (float occupiedValue = candidate - this.minimumSpacing; occupiedValue <= candidate + this.minimumSpacing; occupiedValue += OCCUPIED_VALUE_STEP) {
         this.occupiedValues.add(occupiedValue);
      }

      this.currentValue = candidate;
   }

   private boolean isAvailable(float candidate) {
      return this.occupiedValues.stream().noneMatch(var2 -> Math.abs(var2 - candidate) < this.minimumSpacing);
   }
}
