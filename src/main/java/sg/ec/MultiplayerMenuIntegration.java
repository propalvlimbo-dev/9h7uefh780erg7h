package sg.ec;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import sg.client.RoseClient;

public final class MultiplayerMenuIntegration {
   private static final String т_388008bb = "ru.fiw.proxyserver.ProxyServer";
   private static final String а = "ru.fiw.proxyserver.GuiProxy";
   private static final String Ю = "com.viaversion.viafabricplus.screen.impl.ProtocolSelectionScreen";
   private static Field В_fc11c969;
   private static Field Я;
   private static Method В_bc9d9778;
   private static boolean т_bbeebd87;
   private static final String ч7 = "ru.fiw.proxyserver.ProxyServer";
   private static final String чР = "proxyMenuButton";
   private static final String ч3 = "proxyEnabled";
   private static final String чр = "getLastUsedProxyIp";
   private static final String ч6 = "Proxy: none";
   private static final String ч_ = "Via";
   private static final String чШ = "Прокси";
   private static final String чы = "none";
   private static final String чй = "null";
   private static final String чЖ = "Прокси: выкл";
   private static final String чм = "Прокси";
   private static final String чК = "com.viaversion.viafabricplus.screen.impl.ProtocolSelectionScreen";
   private static final String чI = "INSTANCE";
   private static final String чТ = "open";
   private static final String чО = "prevScreen";
   private static final String чФ = "ru.fiw.proxyserver.GuiProxy";

   public static boolean т() {
      return т_bbeebd87;
   }

   public static void ъ() {
      try {
         if (В_fc11c969 == null) {
            Class var0 = Class.forName(ч7);
            В_fc11c969 = var0.getField(чР);
            Я = var0.getField(ч3);
            В_bc9d9778 = var0.getMethod(чр);
            т_bbeebd87 = true;
         }

         if (В_fc11c969.get(null) == null) {
            В_fc11c969.set(null, Button.builder(Component.literal(ч6), var0x -> {}).bounds(0, 0, 120, 20).build());
         }
      } catch (Throwable var1) {
         т_bbeebd87 = false;
      }
   }

   public static String м() {
      ъ();
      if (!т_bbeebd87) {
         return чШ;
      }

      try {
         boolean var0 = Я.getBoolean(null);
         String var1 = String.valueOf(В_bc9d9778.invoke(null));
         return var0 && !var1.isBlank() && !чы.equalsIgnoreCase(var1) && !чй.equalsIgnoreCase(var1) ? "Прокси: " + var1 : чЖ;
      } catch (Throwable var2) {
         return чм;
      }
   }

   private MultiplayerMenuIntegration() {
   }

   public static String ц() {
      try {
         ProtocolVersion var0 = ViaProtocolVersion.Г();
         if (var0 != null && var0.getName() != null && !var0.getName().isBlank()) {
            return var0.getName();
         }
      } catch (Throwable var1) {
      }

      return ч_;
   }

   public static boolean Ю(Screen var0) {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getViaIntegration().Як()) {
         try {
            Class var1 = Class.forName(чК);
            Object var2 = var1.getField(чI).get(null);
            Method var3 = Ю(var1, чТ, Screen.class);
            if (var3 != null) {
               var3.setAccessible(true);
               var3.invoke(var2, var0);
               return true;
            }

            Field var4 = Ю(var1, чО);
            if (var4 != null) {
               var4.setAccessible(true);
               var4.set(var2, var0);
            }

            Minecraft.getInstance().gui.setScreen((Screen)var2);
            return true;
         } catch (Throwable var5) {
            return false;
         }
      } else {
         return false;
      }
   }

   private static Screen Ю(Class<?> var0, Screen var1) {
      try {
         Constructor var5 = var0.getConstructor(Screen.class);
         return (Screen)var5.newInstance(var1);
      } catch (Throwable var4) {
         try {
            Constructor var2 = var0.getConstructor(JoinMultiplayerScreen.class);
            return (Screen)var2.newInstance(new JoinMultiplayerScreen(var1));
         } catch (Throwable var3) {
            return null;
         }
      }
   }

   private static Method Ю(Class<?> var0, String var1, Class<?>... var2) {
      for (Class var3 = var0; var3 != null; var3 = var3.getSuperclass()) {
         try {
            return var3.getDeclaredMethod(var1, var2);
         } catch (NoSuchMethodException var5) {
         }
      }

      return null;
   }

   public static void Б() {
      ъ();
   }

   public static boolean К(Screen var0) {
      ъ();

      try {
         Class var1 = Class.forName(чФ);
         Screen var2 = Ю(var1, var0);
         if (var2 == null) {
            return false;
         }

         Minecraft.getInstance().gui.setScreen(var2);
         return true;
      } catch (Throwable var3) {
         return false;
      }
   }

   private static Field Ю(Class<?> var0, String var1) {
      for (Class var2 = var0; var2 != null; var2 = var2.getSuperclass()) {
         try {
            return var2.getDeclaredField(var1);
         } catch (NoSuchFieldException var4) {
         }
      }

      return null;
   }
}
