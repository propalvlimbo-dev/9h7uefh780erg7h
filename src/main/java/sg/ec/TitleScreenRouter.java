package sg.ec;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import sg.gui.manager.now.MainMenuScreen;
import sg.gui.manager.now.WelcomeScreen;

public final class TitleScreenRouter {
   private static boolean welcomed;
   private TitleScreenRouter() {
   }

   public static Screen В(Screen var0, boolean var1) {
      if (UnhookState.ые()) {
         return var0;
      } else {
         if (!(var0 instanceof TitleScreen) && (var0 != null || var1)) return var0;
         if (!welcomed) {
            welcomed = true;
            return new WelcomeScreen();
         }
         return new MainMenuScreen();
      }
   }
}
