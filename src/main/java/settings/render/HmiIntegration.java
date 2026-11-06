package settings.render;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.PackRepository;
import sg.mixinbridge.HmiClientInitializer;
import sg.client.RoseClient;
import sg.modules.impl.render.SwingAnimations;

public final class HmiIntegration {
   private static final String ВO = "holdmyitems:pack_test";
   private static boolean ХД;
   private static Boolean Я;
   private static final String ЯйО = "HMI";
   private static final String ЯйФ = "holdmyitems:pack_test";
   private static final String ЯйЙ = "holdmyitems:pack_test";
   private static final String Яйк = "holdmyitems:pack_test";
   private static final String Яйг = "holdmyitems:pack_test";

   public static boolean п弟() {
      RoseClient var0 = RoseClient.getInstance();
      if (var0 != null && var0.getServices() != null && var0.getServices().getModules() != null) {
         SwingAnimations var1 = var0.getServices().getModules().getSwingAnimations();
         return var1 != null && var1.isEnabled() && var1.сГ.isSelected(ЯйО);
      } else {
         return false;
      }
   }

   public static void Т_() {
      if (ХД) {
         Minecraft var0 = Minecraft.getInstance();
         PackRepository var1 = var0.getResourcePackRepository();
         boolean var2 = п弟();
         if (Я == null || Я != var2) {
            Я = var2;
            boolean var3 = var1.getSelectedIds().contains(ЯйФ);
            if (var3 != var2) {
               ArrayList var4 = new ArrayList<>(var1.getSelectedIds());
               if (var2) {
                  if (!var4.contains(ЯйЙ)) {
                     var4.add(Яйк);
                  }
               } else {
                  var4.remove(Яйг);
               }

               var1.setSelected(var4);
               var0.reloadResourcePacks();
            }
         }
      }
   }

   private HmiIntegration() {
   }

   public static void Тх() {
      if (!ХД) {
         ХД = true;
         new HmiClientInitializer().onInitialize();
      }
   }
}
