package sg.modules.impl.init;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import settings.render.ClientLocalization;
import sg.ec.KeyDisplayNames;
import sg.settings.BooleanSetting;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.settings.ChoiceOption;
import sg.settings.KeybindSetting;
import sg.settings.MultiChoiceSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.ValueSetting;

public final class KeybindConflictResolver {
   private static final double ХМр = 220.0;
   private static final String ХМ6 = ", ";
   private static final String ХМ_ = "I";
   private static final String ХМШ = "Бинд ";
   private static final String ХМы = "Bind ";
   private static final String ХМй = " уже занят: ";
   private static final String ХМЖ = " is already used by: ";

   public static int в(int var0, boolean var1, int var2, int var3) {
      return в(var0, var1, var2, var3, (ValueSetting<?>)null);
   }

   public static boolean в(int var0, ValueSetting<?> var1) {
      ClientModule var2 = в_bddb06f1(var1);
      return в(var0, var2);
   }

   private static boolean в(ValueSetting<?> var0, int var1) {
      if (var0 instanceof KeybindSetting var7) {
         return var7.getValue() == var1;
      } else if (var0 instanceof BooleanSetting var6) {
         return var6.getKeybind().getKeyCode() == var1;
      } else if (var0 instanceof SingleChoiceSetting var5) {
         for (ChoiceOption var9 : var5.getValue()) {
            if (var9.getKeybind().getKeyCode() == var1) {
               return true;
            }
         }

         return false;
      } else {
         if (var0 instanceof MultiChoiceSetting var2) {
            for (ChoiceOption var4 : var2.getValue()) {
               if (var4.getKeybind().getKeyCode() == var1) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static void б(int var0) {
      if (Р(var0)) {
         List var1 = 衣(var0);
         if (!var1.isEmpty()) {
            String var2 = String.join(ХМ6, var1);
            NotificationManager.р(ХМ_, Component.literal(ClientLocalization.selectLocalizedText(ХМШ, ХМы) + KeyDisplayNames.О(var0) + ClientLocalization.selectLocalizedText(ХМй, ХМЖ) + var2), -1);
         }
      }
   }

   private static ClientModule в_bddb06f1(ValueSetting<?> var0) {
      if (var0 != null && RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null) {
         for (ClientModule var2 : RoseClient.getInstance().getServices().getModules()) {
            if (var2.getSettings().contains(var0)) {
               return var2;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private static boolean в(ClientModule var0, int var1) {
      if (var0.getKeybind().getKeyCode() == var1) {
         return true;
      }

      for (ValueSetting var3 : var0.getSettings()) {
         if (в(var3, var1)) {
            return true;
         }
      }

      return false;
   }

   public static boolean Р(int var0) {
      if (var0 >= 0 && RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null) {
         int var1 = 0;

         for (ClientModule var3 : RoseClient.getInstance().getServices().getModules()) {
            if (var3.isEnabled() && в(var3, var0)) {
               if (++var1 > 1) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private KeybindConflictResolver() {
   }

   private static boolean в_bbeebd87(ValueSetting<?> var0) {
      if (var0 instanceof KeybindSetting var6) {
         return Р(var6.getValue());
      } else if (var0 instanceof BooleanSetting var5) {
         return Р(var5.getKeybind().getKeyCode());
      } else if (var0 instanceof SingleChoiceSetting var4) {
         for (ChoiceOption var8 : var4.getValue()) {
            if (Р(var8.getKeybind().getKeyCode())) {
               return true;
            }
         }

         return false;
      } else {
         if (var0 instanceof MultiChoiceSetting var1) {
            for (ChoiceOption var3 : var1.getValue()) {
               if (Р(var3.getKeybind().getKeyCode())) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static boolean в(int var0, ClientModule var1) {
      return var1 != null && var1.isEnabled() && Р(var0);
   }

   public static int в(int var0, boolean var1, int var2, int var3, ValueSetting<?> var4) {
      if (var1) {
         return var3;
      }

      boolean var5 = var4 == null ? Р(var0) : в(var0, var4);
      return var5 ? ColorMath.packArgb(255, 92, 92, ХМр) : var2;
   }

   public static List<String> 衣(int var0) {
      ArrayList var1 = new ArrayList();
      if (var0 >= 0 && RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null) {
         for (ClientModule var3 : RoseClient.getInstance().getServices().getModules()) {
            if (var3.isEnabled()) {
               if (var3.getKeybind().getKeyCode() == var0) {
                  var1.add(var3.getName());
               }

               for (ValueSetting var5 : var3.getSettings()) {
                  if (var5 instanceof KeybindSetting var6 && var6.getValue() == var0) {
                     var1.add(var3.getName() + " · " + var5.getName());
                  } else if (var5 instanceof BooleanSetting var7 && var7.getKeybind().getKeyCode() == var0) {
                     var1.add(var3.getName() + " · " + var5.getName());
                  } else if (var5 instanceof SingleChoiceSetting var8) {
                     for (ChoiceOption var13 : var8.getValue()) {
                        if (var13.getKeybind().getKeyCode() == var0) {
                           var1.add(var3.getName() + " · " + var13.getName());
                        }
                     }
                  } else if (var5 instanceof MultiChoiceSetting var9) {
                     for (ChoiceOption var11 : var9.getValue()) {
                        if (var11.getKeybind().getKeyCode() == var0) {
                           var1.add(var3.getName() + " · " + var11.getName());
                        }
                     }
                  }
               }
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static boolean в(ClientModule var0) {
      if (var0 != null && var0.isEnabled()) {
         if (Р(var0.getKeybind().getKeyCode())) {
            return true;
         }

         for (ValueSetting var2 : var0.getSettings()) {
            if (в_bbeebd87(var2)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
