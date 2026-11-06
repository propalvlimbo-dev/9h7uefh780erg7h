package sg.gui;

import sg.gui.click.ClickGuiScreen;
import sg.render.pipeline.ThemeWave;

/** Layout contract shared by the existing editors and their Third GUI host. */
public interface ClickGuiPanelHost {
   float getGuiX();
   float getGuiY();
   float getGuiWidth();
   float getGuiHeight();
   float getSidebarWidth();
   float getModuleViewportHeight();

   default float getGuiAlpha() { return 1.0F; }
   default float getSidebarLabelAlpha() { return 1.0F; }
   default float getLayoutContentAlpha(int index) { return 1.0F; }
   default float getLayoutOffset(int index) { return 0.0F; }
   default float getGuiScroll() { return 0.0F; }
   default void resetGuiScroll() { }
   default float getSearchOverlayAlpha() { return 0.0F; }
   default boolean isUnderSearch(float x, float y, float width, float height) { return false; }
   default boolean usesCustomBackground() { return false; }
   default int getPanelBackgroundColor() { return sg.util.time.ThemeEditor.getColorArgb(sg.theme.ThemeColorSlot.BACKGROUND); }
   default int getPanelCardColor() { return getPanelBackgroundColor(); }
   default float getThemeColorPanelBottomInset() { return 36.0F; }
   default void startThemeTransition(float x, float y, int[] previousColors, int[] nextColors) {
      ThemeWave.INSTANCE.startTransition(x, y, previousColors, nextColors);
   }

   /** Preserve the First GUI's existing coordinates, animation and search behaviour. */
   static ClickGuiPanelHost of(ClickGuiScreen screen) {
      return new ClickGuiPanelHost() {
         public float getGuiX() { return screen.getGuiX(); }
         public float getGuiY() { return screen.getGuiY(); }
         public float getGuiWidth() { return screen.getGuiWidth(); }
         public float getGuiHeight() { return screen.getGuiHeight(); }
         public float getSidebarWidth() { return screen.getSidebarWidth(); }
         public float getModuleViewportHeight() { return screen.getModuleViewportHeight(); }
         public float getGuiAlpha() { return screen.getGuiAlpha(); }
         public float getSidebarLabelAlpha() { return screen.getSidebarLabelAlpha(); }
         public float getLayoutContentAlpha(int index) { return screen.getLayoutContentAlpha(index); }
         public float getLayoutOffset(int index) { return screen.getLayoutOffset(index); }
         public float getGuiScroll() { return screen.getGuiScroll(); }
         public void resetGuiScroll() { screen.resetGuiScroll(); }
         public float getSearchOverlayAlpha() { return screen.getSearchOverlayAlpha(); }
         public boolean isUnderSearch(float x, float y, float width, float height) {
            return screen.isUnderSearch(x, y, width, height);
         }
      };
   }
}
