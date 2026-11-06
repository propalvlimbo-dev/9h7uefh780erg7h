package sg.ec;

import lombok.Generated;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import sg.client.RoseClient;
import sg.gui.click.ClickGuiScreen;

public class SelfcodeService {
   private final ClickGuiScreen В = new ClickGuiScreen();
   private Screen returnScreen;

   public Screen returnScreen() {
      return this.returnScreen;
   }

   public void rememberParent() {
      Screen current = Minecraft.getInstance().gui.screen();
      if (!(current instanceof sg.gui.third.ThirdGuiScreen)
         && !(current instanceof sg.gui.third.ThirdGuiPanelScreen)
         && !(current instanceof ClickGuiScreen)
         && !(current instanceof com.deluxe.gui.DeluxeClickGUI)
         && !(current instanceof com.deluxe.gui.DeluxePanelGui)) {
         this.returnScreen = current;
      }
   }

   @Generated
   public ClickGuiScreen У() {
      return this.В;
   }

   /** The dispatcher routes the GUI key exactly once, including outside a world. */
   public Screen Уг() {
      return this.resolveGuiScreen();
   }

   private Screen resolveGuiScreen() {
      sg.modules.impl.render.ClickGui clickGui = RoseClient.getInstance().getServices().getModules().getClickGui();
      if (clickGui != null && clickGui.isThirdGuiType()) {
         Screen current = Minecraft.getInstance().gui.screen();
          if (current instanceof sg.gui.third.ThirdGuiScreen) {
             return this.returnScreen;
          }
          if (current instanceof sg.gui.third.ThirdGuiPanelScreen) return this.returnScreen;

         return sg.gui.third.ThirdGuiScreen.instance();
      } else if (clickGui != null && clickGui.isSecondGuiType()) {
         Screen current = Minecraft.getInstance().gui.screen();
         if (current instanceof com.deluxe.gui.DeluxeClickGUI || current instanceof com.deluxe.gui.DeluxePanelGui) {
             return this.returnScreen;
         }

         return com.deluxe.gui.DeluxeClickGUI.instance();
      } else {
         return Minecraft.getInstance().gui.screen() == this.В ? this.returnScreen : this.В;
      }
   }
}
