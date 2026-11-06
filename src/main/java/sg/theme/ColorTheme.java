package sg.theme;

import java.util.Arrays;
import lombok.Generated;

public class ColorTheme {
   private final @com.google.gson.annotations.SerializedName("ХА")  String name;
   private final @com.google.gson.annotations.SerializedName("Х4")  String author;
   private final @com.google.gson.annotations.SerializedName("Ао")  boolean userCreated;
   private @com.google.gson.annotations.SerializedName("щ")  int[] colors;
   private static final String UNKNOWN_AUTHOR = "Unknown";

   public void setColors(int[] colors) {
      this.colors = Arrays.copyOf(colors, colors.length);
   }

   public ColorTheme(String name, String author, boolean userCreated, int[] colors) {
      this.name = name;
      this.author = author == null ? UNKNOWN_AUTHOR : author;
      this.userCreated = userCreated;
      this.colors = Arrays.copyOf(colors, colors.length);
   }

   public int[] copyColors() {
      return Arrays.copyOf(this.colors, this.colors.length);
   }

   @Generated
   public boolean isUserCreated() {
      return this.userCreated;
   }

   @Generated
   public int[] getColors() {
      return this.colors;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public String getAuthor() {
      return this.author;
   }
}
