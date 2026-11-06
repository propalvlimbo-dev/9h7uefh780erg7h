package sg.modules.impl.misc;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.settings.BooleanSetting;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.SingleChoiceSetting;

public final class Optimization extends ClientModule {
   private static final int[] Н = new int[]{16, 12, 8};
   private static final double[] т = new double[]{1.0, Optimization.ВСф, Optimization.ВСи};
   private static final double[] а = new double[]{Optimization.ВС9, Optimization.ВСо, Optimization.ВСO};
   private static final double[] Ю = new double[]{Optimization.ВС7, Optimization.ВСР, Optimization.ВС3};
   private static Optimization instance;
   private final @com.google.gson.annotations.SerializedName("с弟")  SingleChoiceSetting modeSetting;
   private final @com.google.gson.annotations.SerializedName("Ят")  BooleanSetting limitRenderDistanceSetting;
   private final @com.google.gson.annotations.SerializedName("ХЩ")  EventListener<PlayerTickEvent> playerTickListener;
   private @com.google.gson.annotations.SerializedName("Дх")  int lastEffectiveRenderDistance;
   private @com.google.gson.annotations.SerializedName("ДУ")  int lastModeIndex;
   private @com.google.gson.annotations.SerializedName("ез")  boolean lastRenderDistanceLimitEnabled;
   private static final String ВС必 = "Optimization";
   private static final String ВСщ = "Повышает FPS за счёт оптимизации частиц, тайлов, дистанции, облаков и теней";
   private static final String ВСГ = "Режим";
   private static final String ВСл = "Низкий";
   private static final String ВСэ = "Средний";
   private static final String ВСю = "Ультра";
   private static final String ВСЩ = "Ограничение прорисовки";
   private static final String ВСу = "Ультра";
   private static final String ВС2 = "Средний";
   private static final double ВСф = 0.8;
   private static final double ВСи = 0.6;
   private static final double ВС9 = 2304.0;
   private static final double ВСо = 1024.0;
   private static final double ВСO = 484.0;
   private static final double ВС7 = 2304.0;
   private static final double ВСР = 1296.0;
   private static final double ВС3 = 576.0;

   public static boolean method_76() {
      return method_7诶() && instance.гШ() == 2;
   }

   public static boolean method_7Д() {
      return method_7诶() && instance.гШ() >= 1;
   }

   public static int method_2(int var0) {
      if (var0 <= 0) {
         return 0;
      }

      if (!method_7诶()) {
         return var0;
      }

      return Math.max(1, switch (instance.гШ()) {
         case 0 -> var0 * 3 / 4;
         case 1 -> var0 / 2;
         default -> var0 / 4;
      });
   }

   private int гШ() {
      if (this.modeSetting.isSelected(ВСу)) {
         return 2;
      } else {
         return this.modeSetting.isSelected(ВС2) ? 1 : 0;
      }
   }

   public static int Ж(int var0) {
      if (!method_7诶()) {
         return var0;
      }

      if (!instance.limitRenderDistanceSetting.getValue()) {
         return var0;
      }

      int var1 = Н[instance.гШ()];
      return Math.min(var0, var1);
   }

   public static boolean method_7ж() {
      return false;
   }

   public static boolean ш(double var0) {
      return !method_7诶() || var0 <= Ю[instance.гШ()];
   }

   private static boolean method_7诶() {
      return instance != null && instance.isEnabled();
   }

   public static boolean method_7(double var0, double var2, double var4) {
      if (!method_7诶()) {
         return true;
      }

      Minecraft var6 = Minecraft.getInstance();
      if (var6.gameRenderer == null) {
         return true;
      }

      Camera var7 = var6.gameRenderer.mainCamera();
      if (var7 == null) {
         return true;
      }

      Vec3 var8 = var7.position();
      double var9 = var0 - var8.x;
      double var11 = var2 - var8.y;
      double var13 = var4 - var8.z;
      return var9 * var9 + var11 * var11 + var13 * var13 <= а[instance.гШ()];
   }

   public static boolean method_7ю() {
      return false;
   }

   public static double мн(double var0) {
      return method_7诶() ? Math.min(var0, а[instance.гШ()]) : var0;
   }

   public static boolean method_7М() {
      return false;
   }

   public Optimization() {
      super(ВС必, ВСщ, ModuleCategory.MISC);
      this.modeSetting = new SingleChoiceSetting(ВСГ, 1, new ChoiceOption(ВСл), new ChoiceOption(ВСэ), new ChoiceOption(ВСю));
      this.limitRenderDistanceSetting = new BooleanSetting(ВСЩ, true);
      this.playerTickListener = this::мц;
      this.lastEffectiveRenderDistance = -1;
      this.lastModeIndex = -1;
      instance = this;
      this.addSettings(this.modeSetting, this.limitRenderDistanceSetting);
   }

   private void мц(PlayerTickEvent var1) {
      this.зн();
   }

   public static boolean г(boolean var0) {
      return method_7诶() && instance.гШ() >= 1 ? false : var0;
   }

   private void зн() {
      if (MINECRAFT_CLIENT.level != null) {
         int var1 = this.гШ();
         boolean var2 = this.limitRenderDistanceSetting.getValue();
         int var3 = MINECRAFT_CLIENT.options.renderDistance().get();
         int var4 = var2 ? Math.min(var3, Н[var1]) : var3;
         if (var4 != this.lastEffectiveRenderDistance || var1 != this.lastModeIndex || var2 != this.lastRenderDistanceLimitEnabled) {
            this.lastEffectiveRenderDistance = var4;
            this.lastModeIndex = var1;
            this.lastRenderDistanceLimitEnabled = var2;
            MINECRAFT_CLIENT.levelExtractor.allChanged();
         }
      }
   }

   @Override
   public void onEnable() {
      this.lastEffectiveRenderDistance = -1;
      this.lastModeIndex = -1;
      this.lastRenderDistanceLimitEnabled = !this.limitRenderDistanceSetting.getValue();
      this.зн();
   }

   public static boolean method_7В() {
      return false;
   }

   public static double мш(double var0) {
      return method_7诶() ? var0 * т[instance.гШ()] : var0;
   }

   @Override
   public void onDisable() {
      this.lastEffectiveRenderDistance = -1;
      this.lastModeIndex = -1;
      if (MINECRAFT_CLIENT.level != null) {
         MINECRAFT_CLIENT.levelExtractor.allChanged();
      }
   }
}
