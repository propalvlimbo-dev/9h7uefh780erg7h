package sg.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class VoicechatPushToTalk {
   private static boolean ХЕ;
   private static boolean ХП;
   private static Method Я;
   private static Method Х_bc9d9778;
   private static Method е_bc9d9778;
   private static Method А;
   private static Method field_4;
   private static Method п;
   private static Method Д;
   private static Field е_fc11c969;
   private static Boolean Х_6c785b53;
   private static Boolean е_6c785b53;
   private static boolean Х西;
   private static final String field_9у = "de.maxhenkel.voicechat.voice.client.ClientManager";
   private static final String field_92 = "getPlayerStateManager";
   private static final String field_9ф = "isMuted";
   private static final String field_9и = "setMuted";
   private static final String field_99 = "isDisabled";
   private static final String field_9о = "setDisabled";
   private static final String field_9O = "canEnable";
   private static final String field_97 = "getPttKeyHandler";
   private static final String field_9Р = "pttKeyDown";

   public static boolean Р7() {
      return Р9() && ХП;
   }

   private VoicechatPushToTalk() {
   }

   private static boolean Р9() {
      if (ХЕ) {
         return ХП;
      }

      ХЕ = true;

      try {
         Class var0 = Class.forName(field_9у);
         Я = var0.getMethod(field_92);
         Class var1 = Я.getReturnType();
         Х_bc9d9778 = var1.getMethod(field_9ф);
         е_bc9d9778 = var1.getMethod(field_9и, boolean.class);
         А = var1.getMethod(field_99);
         field_4 = var1.getMethod(field_9о, boolean.class);

         try {
            п = var1.getMethod(field_9O);
         } catch (NoSuchMethodException var3) {
            п = null;
         }

         Д = var0.getMethod(field_97);
         Class var2 = Д.getReturnType();
         е_fc11c969 = var2.getDeclaredField(field_9Р);
         е_fc11c969.setAccessible(true);
         ХП = true;
         return true;
      } catch (ReflectiveOperationException | LinkageError var4) {
         ХП = false;
         return false;
      }
   }

   private static void ж诶(boolean var0) {
      if (е_fc11c969 != null && Д != null) {
         try {
            Object var1 = Д.invoke(null);
            if (var1 != null) {
               е_fc11c969.setBoolean(var1, var0);
            }
         } catch (ReflectiveOperationException | LinkageError var2) {
         }
      }
   }

   public static synchronized void рл() {
      if (Х西) {
         ж诶(true);
      }
   }

   public static synchronized void ре() {
      Х西 = false;
      ж诶(false);
      if (ХП && Х_6c785b53 != null) {
         try {
            Object var0 = Я.invoke(null);
            if (var0 != null) {
               е_bc9d9778.invoke(var0, Х_6c785b53);
               if (е_6c785b53 != null) {
                  field_4.invoke(var0, е_6c785b53);
               }
            }
         } catch (ReflectiveOperationException | LinkageError var1) {
         }

         Х_6c785b53 = null;
         е_6c785b53 = null;
      } else {
         Х_6c785b53 = null;
         е_6c785b53 = null;
      }
   }

   public static synchronized void р弟() {
      if (Р9() && ХП) {
         try {
            Object var0 = Я.invoke(null);
            if (var0 != null) {
               if (Х_6c785b53 == null) {
                  Х_6c785b53 = (Boolean)Х_bc9d9778.invoke(var0);
                  е_6c785b53 = (Boolean)А.invoke(var0);
               }

               Boolean var1 = (Boolean)А.invoke(var0);
               if (Boolean.TRUE.equals(var1)) {
                  Boolean var2 = п != null ? (Boolean)п.invoke(var0) : Boolean.TRUE;
                  if (Boolean.TRUE.equals(var2)) {
                     field_4.invoke(var0, false);
                  }
               }

               е_bc9d9778.invoke(var0, false);
            }
         } catch (ReflectiveOperationException | LinkageError var3) {
         }

         Х西 = true;
         ж诶(true);
      }
   }

   public static boolean РВ() {
      return Х西;
   }
}
