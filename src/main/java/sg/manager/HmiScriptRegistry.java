package sg.manager;

import java.io.IOException;
import java.util.ArrayList;

public class HmiScriptRegistry {
   public static ArrayList<HeldItemAnimationScript> Ъ = new ArrayList<>();
   public static ArrayList<HeldItemAnimationScript> т = new ArrayList<>();
   public static ArrayList<HeldItemAnimationScript> а = new ArrayList<>();
   public static ArrayList<ItemQuadAnimationScript> Ю = new ArrayList<>();
   public static HeldItemAnimationScript В_464ec6e0;
   public static HeldItemAnimationScript Я;
   public static HeldItemAnimationScript Х;
   public static ItemQuadAnimationScript В_673b7c17;
   private static final String Х衣衣 = "return";
   private static final String Х衣ш = "return";
   private static final String Х衣И = "return";
   private static final String Х衣ь = "return";

   static {
      try {
         В_464ec6e0 = new HeldItemAnimationScript(Х衣衣);
         Я = new HeldItemAnimationScript(Х衣ш);
         Х = new HeldItemAnimationScript(Х衣И);
         В_673b7c17 = new ItemQuadAnimationScript(Х衣ь);
      } catch (IOException var1) {
         throw new RuntimeException(var1);
      }
   }
}
