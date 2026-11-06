package dev.rose.gui.menu;

import dev.rose.RoseClient;
import dev.rose.anim.Spring;
import dev.rose.gui.HudEditorScreen;
import dev.rose.gui.UiSounds;
import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.module.impl.ClickGuiModule;
import dev.rose.module.impl.KeysModule;
import dev.rose.module.impl.ProfilesModule;
import dev.rose.module.impl.ThemeModule;
import dev.rose.module.impl.visual.LooksModule;
import dev.rose.notify.Notifications;
import dev.rose.render.Images;
import dev.rose.render.MenuBackground;
import dev.rose.render.Surfaces;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.render.style.Shadow;
import dev.rose.server.KnownServers;
import dev.rose.setting.Setting;
import dev.rose.util.ColorUtil;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.IntConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.PlayerSkin;


public final class MenuScreen extends Screen {
   private static final float RADIUS = 24.0F;
   private static final float PAD = 8.0F;
   private static final float SIDEBAR_W = 136.0F;
   private static final float LIST_W = 188.0F;
   private static final float LIST_TOP = 46.0F;
   private static final float ROW_H = 30.0F;
   private static final float ROW_GAP = 2.0F;
   private static final float HINT_H = 20.0F;
   private static final float PAGE_TOP = 76.0F;
   private static final float ITEM_H = 24.0F;
   private static final float MIN_W = 520.0F;
   private static final float MIN_H = 300.0F;
   private static final float DROP = 28.0F;
   private static final String VERSION = RoseClient.version();
   private static final Category[] ORDER = new Category[]{
      Category.VISUALS, Category.HUD, Category.WORLD, Category.COSMETICS, Category.SOUNDS, Category.INTERFACE
   };
   private static Category lastCategory = Category.VISUALS;
   private static boolean lastFavorites;
   private static final Map<Category, String> lastModules = new EnumMap<>(Category.class);
   private static String lastFavoriteModule;
   private final Screen parent;
   private final MenuUi ui = new MenuUi();
   private final SettingsPanel settings = new SettingsPanel();
   private final ProfilesPage profiles = new ProfilesPage();
   private final ThemesPage themes = new ThemesPage();
   private final LooksPage looks = new LooksPage();
   private final KeysPage keys = new KeysPage();
   private final MenuCharacter character = new MenuCharacter();
   private final TextBuffer search = new TextBuffer(40);
   private final Scroll listScroll = new Scroll();
   private final Scroll pageScroll = new Scroll();
   private final Spring open = new Spring(0.0, 0.56, 0.64);
   private final Spring navHead = new Spring(0.0, 0.3, 0.72);
   private final Spring navTail = new Spring(0.0, 0.5, 0.95);
   private final Spring rowHead = new Spring(0.0, 0.28, 0.74);
   private final Spring rowTail = new Spring(0.0, 0.46, 0.95);
   private final long openedAt = System.currentTimeMillis();
   private final MenuTest test;
   private Category category;
   private boolean favorites;
   private Module selected;
   private List<MenuScreen.Entry> results = List.of();
   private String resultsFor = "";
   private boolean scrollToHighlight;
   private long highlightRequested;
   private boolean closing;
   private float scale = 1.0F;
   private float winX;
   private float winY;
   private float winW;
   private float winH;
   private MenuScreen.Drag drag = MenuScreen.Drag.NONE;
   private Module resetPending;
   private long resetSince;
   private boolean navSnapped;
   private boolean rowSnapped;
   private double[] testMouse;

   public MenuScreen(Screen parent) {
      super(Component.literal("Rose"));
      this.parent = parent;
      this.category = lastCategory;
      this.favorites = lastFavorites;
      this.selected = this.remembered();
      this.open.setTarget(1.0);
      if (Modules.clickGui().openAnimation() != ClickGuiModule.OpenAnimation.LIQUID) {
         this.open.snap(1.0);
      }

      if (Modules.clickGui().openAnimation() == ClickGuiModule.OpenAnimation.FADE) {
         this.ui.snap("menu:fade", 0.0F);
      }

      UiSounds.play(UiSounds.Sound.OPEN);
      this.test = "true".equals(RoseClient.uiTest()) ? new MenuTest(this) : null;
   }

   public static void remember(Module module) {
      lastFavorites = false;
      lastCategory = module.category();
      lastModules.put(module.category(), module.id());
   }

   public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
      long started = System.nanoTime();
      if (this.test != null) {
         this.test.tick();
         if (this.testMouse != null) {
            mouseX = (int)Math.round(this.testMouse[0]);
            mouseY = (int)Math.round(this.testMouse[1]);
         }
      }

      ClickGuiModule options = Modules.clickGui();
      this.scale = Math.max(0.25F, Math.min(this.width / 800.0F, this.height / 450.0F) * options.scale());
      if (!this.ui.begin(g, this.scale, mouseX / this.scale, mouseY / this.scale)) {
         this.drawBehind(g, partialTick, 1.0F);
      } else {
         this.ui.motion = options.reduceMotion() ? 0.15F : 1.0F;
         ClickGuiModule.OpenAnimation animation = options.openAnimation();
         float liquid = animation == ClickGuiModule.OpenAnimation.LIQUID ? Math.max(0.0F, this.open.getFloat()) : (this.closing ? 0 : 1);
         float fade = animation == ClickGuiModule.OpenAnimation.FADE
            ? this.ui.anim("menu:fade", this.closing ? 0.0F : 1.0F, this.closing ? 28.0F : 18.0F)
            : (animation == ClickGuiModule.OpenAnimation.NONE ? (this.closing ? 0 : 1) : 1.0F);
         float shown = Math.min(Math.clamp(liquid, 0.0F, 1.0F), fade);
         if (!this.closing || (animation == ClickGuiModule.OpenAnimation.LIQUID ? !(liquid < 0.03F) : !(fade < 0.03F))) {
            this.drawBehind(g, partialTick, shown);
            this.ui.screenWidth = this.width / this.scale;
            this.ui.screenHeight = this.height / this.scale;
            this.layoutWindow(options);
            g.pose().pushMatrix();
            g.pose().scale(this.scale, this.scale);
            this.ui.alpha = fade;
            this.character.draw(this.ui, shown);
            this.ui.alpha = 1.0F;
            this.ui.target("window:body", this.winX, this.winY, this.winW, this.winH, new MenuUi.Handler() {});
            this.drawWindow(options, liquid, fade);
            float content = animation == ClickGuiModule.OpenAnimation.LIQUID ? Math.clamp((liquid - 0.7F) / 0.3F, 0.0F, 1.0F) : 1.0F;
            this.ui.alpha = fade * content;
            if (this.ui.alpha > 0.003F) {
               this.ui.target("window:move", this.winX, this.winY, this.winW, 40.0F, this.moveHandler());
               this.drawSidebar(options);
               this.drawList();
               this.drawPage();
               this.drawResizeHandle();
               this.ui.drawOverlays();
               this.character.drawOverlay(this.ui, shown);
               this.ui.drawTooltip();
            }

            this.ui.alpha = 1.0F;
            g.pose().popMatrix();
            this.ui.end();
            if (this.test != null) {
               this.test.frameTime(System.nanoTime() - started);
            }
         } else {
            this.ui.end();
            this.minecraft.setScreen(this.parent);
         }
      }
   }

   public void tick() {
      if (this.test != null) {
         this.test.clientTick();
      }
   }

   public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
   }

   private void drawBehind(GuiGraphics g, float partialTick, float appear) {
      ClickGuiModule options = Modules.clickGui();
      boolean image = MenuBackground.active()
         && MenuBackground.draw(g, this.width, this.height, this.minecraft.level == null ? 1.0F : appear);
      if (!image && this.minecraft.level == null) {
         this.renderPanorama(g, partialTick);
      }

      if (options.blurWorld() && appear > 0.1F) {
         this.renderBlurredBackground(g);
      }

      float dim = options.dim() * appear;
      if (dim > 0.0F) {
         g.fill(0, 0, this.width, this.height, ColorUtil.withAlpha(0, Math.round(dim * 255.0F)));
      }
   }

   private void layoutWindow(ClickGuiModule options) {
      if (this.drag == MenuScreen.Drag.NONE) {
         float screenW = this.width / this.scale;
         float screenH = this.height / this.scale;
         this.winW = Math.min(Math.max(options.windowWidth(), 520.0F), Math.max(200.0F, screenW - 8.0F));
         this.winH = Math.min(Math.max(options.windowHeight(), 300.0F), Math.max(160.0F, screenH - 8.0F));
         this.winX = (screenW - this.winW) * options.windowX();
         this.winY = (screenH - this.winH) * options.windowY();
      }

      this.clampWindow();
   }

   private void clampWindow() {
      float screenW = this.width / this.scale;
      float screenH = this.height / this.scale;
      this.winW = Math.min(this.winW, Math.max(200.0F, screenW));
      this.winH = Math.min(this.winH, Math.max(160.0F, screenH));
      this.winX = Math.clamp(this.winX, 0.0F, Math.max(0.0F, screenW - this.winW));
      this.winY = Math.clamp(this.winY, 0.0F, Math.max(0.0F, screenH - this.winH));
   }

   private void saveWindow() {
      float freeX = this.width / this.scale - this.winW;
      float freeY = this.height / this.scale - this.winH;
      Modules.clickGui().saveWindow(freeX > 0.0F ? this.winX / freeX : 0.5F, freeY > 0.0F ? this.winY / freeY : 0.5F, this.winW, this.winH);
   }

   private void drawWindow(ClickGuiModule options, float liquid, float fade) {
      MenuPalette p = this.ui.palette;
      float m = Math.max(0.0F, liquid);
      float w = 28.0F + (this.winW - 28.0F) * (float)Math.pow(Math.min(m, 1.15F), 0.7);
      float h = 28.0F + (this.winH - 28.0F) * (float)Math.pow(Math.min(m, 1.15F), 1.4);
      float x = this.winX + (this.winW - w) / 2.0F;
      float y = this.winY + (this.winH - h) / 2.0F;
      float radius = Math.min(24.0F, Math.min(w, h) / 2.0F);
      float appear = Math.clamp(m * 4.0F, 0.0F, 1.0F) * fade;
      Corners corners = Corners.themed(radius);
      this.ui.alpha = appear;
      Surfaces.shadow(this.ui.g, x, y, w, h, corners, new Shadow(1845493760, 22.0F, 0.0F, 0.0F, 14.0F), appear);
      if (options.windowStyle() == ClickGuiModule.WindowStyle.GLASS) {
         Glass glass = p.glass().copy();
         glass.refraction = glass.refraction + Math.min(30.0F, (float)Math.abs(this.open.velocity()) * 8.0F) * this.ui.motion;
         float density = options.glassDensity();
         if (density != 1.0F && glass.tint.type() == Paint.Type.SOLID) {
            int tint = glass.tint.color(0);
            glass.tint = Paint.solid(ColorUtil.withAlpha(tint, Math.clamp(Math.round(ColorUtil.alpha(tint) * density), 0, 242)));
         }

         MenuDraw.glass(this.ui, x, y, w, h, corners, glass, false);
      } else {
         int fill = p.light() ? -855305 : -15658473;
         MenuDraw.rect(this.ui, x, y, w, h, corners, ColorUtil.withAlpha(fill, Math.round(255.0F * options.windowOpacity())));
         Surfaces.border(this.ui.g, x, y, w, h, corners, 0.8F, p.border(), appear);
      }

      this.ui.alpha = 1.0F;
   }

   private void drawSidebar(ClickGuiModule options) {
      MenuPalette p = this.ui.palette;
      float x = this.winX + 8.0F;
      float y = this.winY + 8.0F;
      float w = 136.0F;
      float h = this.winH - 16.0F;
      if (options.windowStyle() == ClickGuiModule.WindowStyle.GLASS) {
         Glass pane = p.glass().copy();
         pane.tint = Paint.solid(p.light() ? 956301311 : 268435455);
         pane.blur = 0.2F;
         pane.refraction = 6.0F;
         pane.bevel = 9.0F;
         pane.specular = 0.55F;
         pane.rim = 0.5F;
         pane.dispersion = 0.15F;
         pane.depth = 0.04F;
         MenuDraw.glass(this.ui, x, y, w, h, Corners.themed(18.0F), pane, true);
      } else {
         MenuDraw.rect(this.ui, x, y, w, h, 18.0F, p.group());
      }

      this.ui.icon(57525, x + 17.0F, y + 17.0F, 12.0F, p.accentText());
      this.ui.textCentered(this.ui.display, "Rose", x + 28.0F, y + 17.0F, 10.0F, p.text());
      float versionW = this.ui.regular.width(VERSION, 5.6F);
      this.ui.textCentered(this.ui.regular, VERSION, x + w - 10.0F - versionW, y + 17.5F, 5.6F, p.textFaint());
      this.searchField(x + 8.0F, y + 32.0F, w - 16.0F, 20.0F);
      boolean searching = !this.search.isEmpty();
      float itemX = x + 6.0F;
      float itemW = w - 12.0F;
      float favoritesY = y + 60.0F;
      float categoriesY = y + 104.0F;
      int active = searching ? -1 : (this.favorites ? 0 : 1 + indexOf(this.category));
      if (active >= 0) {
         if (!this.navSnapped) {
            this.navHead.snap(active);
            this.navTail.snap(active);
            this.navSnapped = true;
         }

         this.navHead.setTarget(active);
         this.navTail.setTarget(active);
      }

      float lens = this.ui.anim("nav:lens", active >= 0 ? 1.0F : 0.0F, 16.0F);
      if (lens > 0.01F) {
         float headY = navY(this.navHead.getFloat(), favoritesY, categoriesY);
         float tailY = navY(this.navTail.getFloat(), favoritesY, categoriesY);
         MenuDraw.lens(this.ui, itemX, headY, itemX, tailY, itemW, 24.0F, 12.0F, ColorUtil.withAlpha(p.accent(), p.light() ? 150 : 120), 19.2F, lens);
      }

      int favoriteCount = 0;

      for (Module module : Modules.all()) {
         if (module.isFavorite()) {
            favoriteCount++;
         }
      }

      this.sidebarItem(
         "nav:favorites",
         itemX,
         favoritesY,
         itemW,
         57718,
         I18n.get("rose.menu.favorites", new Object[0]),
         favoriteCount,
         active >= 0 ? 1.0F - Math.min(1.0F, Math.abs(this.navHead.getFloat())) : 0.0F,
         active == 0,
         this::openFavorites
      );
      this.ui
         .textCentered(
            this.ui.semibold,
            I18n.get("rose.menu.categories", new Object[0]).toUpperCase(Locale.ROOT),
            x + 14.0F,
            y + 96.0F,
            5.8F,
            0.8F,
            ColorUtil.fade(p.text(), 0.42F)
         );

      for (int i = 0; i < ORDER.length; i++) {
         Category next = ORDER[i];
         int enabled = 0;

         for (Module module : Modules.in(next)) {
            if (!module.isAlwaysOn() && module.isEnabled()) {
               enabled++;
            }
         }

         float focus = active >= 0 ? 1.0F - Math.min(1.0F, Math.abs(this.navHead.getFloat() - (i + 1))) : 0.0F;
         this.sidebarItem(
            "nav:" + next.id(),
            itemX,
            categoriesY + i * 26.0F,
            itemW,
            next.icon(),
            next.displayName(),
            enabled,
            focus,
            active == i + 1,
            () -> this.openCategory(next)
         );
      }

      float playerH = 30.0F;
      float playerY = y + h - playerH - 2.0F;
      float buttonH = 20.0F;
      float buttonY = playerY - 6.0F - buttonH;
      if (buttonY > categoriesY + ORDER.length * 26.0F + 4.0F) {
         MenuWidgets.button(
            this.ui,
            "nav:hud_editor",
            x + 8.0F,
            buttonY,
            w - 16.0F,
            buttonH,
            57633,
            I18n.get("rose.menu.hud_editor", new Object[0]),
            MenuWidgets.Style.NORMAL,
            this::openHudEditor
         );
         this.ui.tooltip("nav:hud_editor:tip", x + 8.0F, buttonY, w - 16.0F, buttonH, I18n.get("rose.menu.hud_editor.tip", new Object[0]));
      }

      MenuDraw.hline(this.ui, x + 12.0F, playerY, w - 24.0F, p.divider());
      float face = 17.0F;
      float faceX = x + 12.0F;
      float faceY = playerY + (playerH - face) / 2.0F;
      PlayerSkin skin = (PlayerSkin)this.minecraft.getSkinManager().createLookup(this.minecraft.getGameProfile(), false).get();
      Images.playerFace(this.ui.g, skin.body().texturePath(), faceX, faceY, face, Corners.themed(5.0F), this.ui.alpha);
      float textX = faceX + face + 7.0F;
      float textW = x + w - 10.0F - textX;
      this.ui
         .text(
            this.ui.semibold,
            MenuUi.ellipsize(this.ui.semibold, this.minecraft.getUser().getName(), 7.2F, textW),
            textX,
            faceY + 1.0F,
            7.2F,
            p.text()
         );
      String status = this.minecraft.level == null ? I18n.get("rose.gui.main_menu", new Object[0]) : KnownServers.currentName();
      MenuDraw.circle(this.ui, textX + 1.7F, faceY + 13.0F, 1.7F, p.positive());
      this.ui.text(this.ui.regular, MenuUi.ellipsize(this.ui.regular, status, 6.0F, textW - 6.0F), textX + 6.0F, faceY + 10.2F, 6.0F, p.textDim());
   }

   private static int indexOf(Category category) {
      for (int i = 0; i < ORDER.length; i++) {
         if (ORDER[i] == category) {
            return i;
         }
      }

      return 0;
   }

   private static float navY(float index, float favoritesY, float categoriesY) {
      return index < 1.0F ? favoritesY + index * (categoriesY - favoritesY) : categoriesY + (index - 1.0F) * 26.0F;
   }

   private void sidebarItem(String id, float x, float y, float w, int icon, String label, int count, float focus, boolean active, Runnable action) {
      MenuPalette p = this.ui.palette;
      float hover = this.ui.anim(id + ":hover", this.ui.hot(id) && !active ? 1.0F : 0.0F, 22.0F);
      if (hover > 0.01F) {
         MenuDraw.pill(this.ui, x, y, w, 24.0F, p.hover(hover));
      }

      int idle = ColorUtil.lerp(p.textDim(), p.text(), hover);
      int selectedColor = p.light() ? p.text() : -1;
      int color = ColorUtil.lerp(idle, selectedColor, Math.clamp(focus, 0.0F, 1.0F));
      this.ui.icon(icon, x + 13.0F, y + 12.0F, 9.5F, color);
      float right = x + w - 8.0F;
      if (count > 0) {
         String badge = Integer.toString(count);
         float bw = Math.max(13.0F, this.ui.semibold.width(badge, 6.0F) + 8.0F);
         MenuDraw.pill(this.ui, right - bw, y + 12.0F - 5.5F, bw, 11.0F, active ? 872415231 : p.track());
         this.ui.textCentered(this.ui.semibold, badge, right - bw + (bw - this.ui.semibold.width(badge, 6.0F)) / 2.0F, y + 12.0F, 6.0F, color);
         right -= bw + 4.0F;
      }

      String text = MenuUi.ellipsize(this.ui.medium, label, 7.8F, right - x - 26.0F);
      this.ui.textCentered(focus > 0.5F ? this.ui.semibold : this.ui.medium, text, x + 25.0F, y + 12.0F, 7.8F, color);
      this.ui.target(id, x, y, w, 24.0F, MenuUi.click(action));
   }

   private void searchField(float x, float y, float w, float h) {
      MenuPalette p = this.ui.palette;
      boolean focused = this.ui.focused == this.search;
      float hover = this.ui.anim("search:hover", this.ui.hot("search") && !focused ? 1.0F : 0.0F, 20.0F);
      float focus = this.ui.anim("search:focus", focused ? 1.0F : 0.0F, 20.0F);
      MenuDraw.pill(this.ui, x, y, w, h, ColorUtil.lerp(p.track(), p.groupHover(), hover * 0.6F));
      if (focus > 0.01F) {
         MenuDraw.pillBorder(this.ui, x, y, w, h, 1.0F, ColorUtil.withAlpha(p.accent(), Math.round(208.0F * focus)));
      }

      this.ui.icon(57681, x + 11.0F, y + h / 2.0F, 8.5F, ColorUtil.lerp(p.textDim(), p.accentText(), focus));
      float textX = x + 20.0F;
      float textW = w - 20.0F - (this.search.isEmpty() ? 8 : 18);
      float size = 7.2F;
      this.ui.target("search", x, y, w, h, MenuUi.click(this::focusSearch));
      this.ui.tooltip("search:tip", x, y, w, h, I18n.get("rose.menu.search.tip", new Object[0]));
      if (this.search.isEmpty() && !focused) {
         this.ui
            .textCentered(
               this.ui.regular,
               MenuUi.ellipsize(this.ui.regular, I18n.get("rose.menu.search", new Object[0]), size, textW),
               textX,
               y + h / 2.0F,
               size,
               p.textDim()
            );
      } else {
         this.ui.clip(textX, y, textW, h);
         float caret = this.ui.regular.width(this.search.text().substring(0, this.search.cursor()), size);
         float shift = Math.max(0.0F, caret - (textW - 4.0F));
         this.ui.textCentered(this.ui.regular, this.search.text(), textX - shift, y + h / 2.0F, size, p.text());
         if (focused && System.currentTimeMillis() / 530L % 2L == 0L) {
            MenuDraw.rect(this.ui, textX + caret - shift, y + 5.0F, 0.8F, h - 10.0F, 0.0F, p.accent());
         }

         this.ui.unclip();
         if (!this.search.isEmpty()) {
            MenuWidgets.iconButton(this.ui, "search:clear", x + w - 17.0F, y + 3.0F, 14.0F, 57778, p.textDim(), this::clearSearch);
         }
      }
   }

   private void drawList() {
      MenuPalette p = this.ui.palette;
      float x = this.winX + 8.0F + 136.0F + 8.0F;
      float w = 188.0F;
      float top = this.winY + 46.0F;
      float bottom = this.winY + this.winH - 8.0F;
      List<MenuScreen.Entry> entries = this.entries();
      String title;
      int icon;
      if (!this.search.isEmpty()) {
         title = I18n.get("rose.menu.search_results", new Object[0]);
         icon = 57681;
      } else if (this.favorites) {
         title = I18n.get("rose.menu.favorites", new Object[0]);
         icon = 57718;
      } else {
         title = this.category.displayName();
         icon = this.category.icon();
      }

      this.ui.icon(icon, x + 12.0F, this.winY + 20.0F, 11.0F, p.accentText());
      this.ui.textCentered(this.ui.display, MenuUi.ellipsize(this.ui.display, title, 12.0F, w - 30.0F), x + 24.0F, this.winY + 20.0F, 12.0F, p.text());
      int moduleCount = 0;
      int enabled = 0;

      for (MenuScreen.Entry entry : entries) {
         if (entry instanceof MenuScreen.ModuleEntry moduleEntry) {
            moduleCount++;
            if (!moduleEntry.module().isAlwaysOn() && moduleEntry.module().isEnabled()) {
               enabled++;
            }
         }
      }

      String subtitle = this.search.isEmpty()
         ? MenuUi.plural("rose.gui.modules", moduleCount) + "  ·  " + I18n.get("rose.gui.enabled_count", new Object[]{enabled})
         : MenuUi.plural("rose.menu.results", entries.size());
      this.ui.textCentered(this.ui.semibold, subtitle.toUpperCase(Locale.ROOT), x + 12.0F, this.winY + 35.0F, 5.6F, 0.8F, ColorUtil.fade(p.text(), 0.44F));
      float viewY = top;
      float viewH = bottom - 20.0F - top;
      float step = 32.0F;
      this.listScroll.update(this.ui, entries.size() * step + 8.0F, viewH);
      this.ui.target("list:view", x, viewY, w, viewH, this.listScroll.wheel());
      this.ui.clip(x - 2.0F, viewY, w + 4.0F, viewH);
      this.ui.viewport(viewY, viewY + viewH);
      float rowsTop = viewY + 2.0F - this.listScroll.value();
      if (entries.isEmpty()) {
         String key = !this.search.isEmpty() ? "rose.menu.nothing_found" : (this.favorites ? "rose.menu.no_favorites" : "rose.menu.empty");
         this.ui.icon(this.favorites && this.search.isEmpty() ? 57718 : 57681, x + w / 2.0F, viewY + 34.0F, 18.0F, p.textFaint());
         List<String> lines = MenuUi.wrap(this.ui.regular, I18n.get(key, new Object[0]), 7.2F, w - 30.0F);

         for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            this.ui.text(this.ui.regular, line, x + (w - this.ui.regular.width(line, 7.2F)) / 2.0F, viewY + 52.0F + i * 10, 7.2F, p.textDim());
         }
      }

      int selectedIndex = -1;

      for (int i = 0; i < entries.size(); i++) {
         if (entries.get(i) instanceof MenuScreen.ModuleEntry entry && entry.module() == this.selected) {
            selectedIndex = i;
         }
      }

      if (selectedIndex >= 0) {
         if (!this.rowSnapped) {
            this.rowHead.snap(selectedIndex);
            this.rowTail.snap(selectedIndex);
            this.rowSnapped = true;
         }

         this.rowHead.setTarget(selectedIndex);
         this.rowTail.setTarget(selectedIndex);
      }

      float blob = this.ui.anim("list:blob", selectedIndex >= 0 ? 1.0F : 0.0F, 16.0F);
      if (blob > 0.01F) {
         MenuDraw.lens(
            this.ui,
            x,
            rowsTop + this.rowHead.getFloat() * step,
            x,
            rowsTop + this.rowTail.getFloat() * step,
            w,
            30.0F,
            11.0F,
            p.light() ? -2130706433 : 486539263,
            14.0F,
            blob
         );
      }

      float fullAlpha = this.ui.alpha;

      for (int i = 0; i < entries.size(); i++) {
         float rowY = rowsTop + i * step;
         if (!(rowY + 30.0F < viewY) && !(rowY > viewY + viewH)) {
            this.ui.alpha = fullAlpha * this.ui.edgeFade(rowY, 30.0F);
            switch ((MenuScreen.Entry)entries.get(i)) {
               case MenuScreen.ModuleEntry entry:
                  this.moduleRow(entry.module(), x, rowY, w);
                  break;
               case MenuScreen.SettingEntry entry:
                  this.settingRow(entry.module(), entry.setting(), x, rowY, w);
                  break;
               default:
                  throw new MatchException(null, null);
            }
         }
      }

      this.ui.alpha = fullAlpha;
      this.ui.clearViewport();
      this.ui.unclip();
      this.listScroll.drawBar(this.ui, "list:bar", x + w + 3.0F, viewY + 4.0F, viewH - 8.0F);
      if (this.category == Category.HUD && !this.favorites && this.search.isEmpty()) {
         String label = I18n.get("rose.menu.hud_hint", new Object[0]);
         float buttonW = Math.min(w - 8.0F, MenuWidgets.buttonWidth(this.ui, 57633, label));
         float buttonH = 18.0F;
         MenuWidgets.button(
            this.ui,
            "list:hud_editor",
            x + (w - buttonW) / 2.0F,
            bottom - 10.0F - buttonH / 2.0F,
            buttonW,
            buttonH,
            57633,
            label,
            MenuWidgets.Style.ACCENT,
            this::openHudEditor
         );
      } else {
         String hint = MenuUi.ellipsize(this.ui.regular, I18n.get("rose.menu.mouse_hint", new Object[0]), 6.0F, w - 24.0F);
         float hintW = this.ui.regular.width(hint, 6.0F) + 12.0F;
         float hintX = x + (w - hintW) / 2.0F;
         float hintY = bottom - 10.0F;
         this.ui.icon(57998, hintX + 4.0F, hintY, 7.5F, p.textFaint());
         this.ui.textCentered(this.ui.regular, hint, hintX + 12.0F, hintY, 6.0F, p.textFaint());
         this.ui.tooltip("list:hint", x, bottom - 20.0F, w, 20.0F, I18n.get("rose.menu.mouse_hint.full", new Object[0]));
      }
   }

   private List<MenuScreen.Entry> entries() {
      if (!this.search.isEmpty()) {
         if (!this.search.text().equals(this.resultsFor)) {
            this.resultsFor = this.search.text();
            this.results = search(this.resultsFor);
         }

         return this.results;
      } else {
         List<MenuScreen.Entry> list = new ArrayList<>();
         if (this.favorites) {
            for (Module module : Modules.all()) {
               if (module.isFavorite()) {
                  list.add(new MenuScreen.ModuleEntry(module));
               }
            }
         } else {
            for (Module module : Modules.in(this.category)) {
               list.add(new MenuScreen.ModuleEntry(module));
            }
         }

         return list;
      }
   }

   private static List<MenuScreen.Entry> search(String query) {
      String needle = query.trim().toLowerCase(Locale.ROOT);
      List<MenuScreen.Entry> found = new ArrayList<>();
      if (needle.isEmpty()) {
         return found;
      }

      List<Module> starts = new ArrayList<>();
      List<Module> contains = new ArrayList<>();

      for (Module module : Modules.all()) {
         String name = module.name().toLowerCase(Locale.ROOT);
         if (name.startsWith(needle)) {
            starts.add(module);
         } else if (name.contains(needle) || module.description().toLowerCase(Locale.ROOT).contains(needle)) {
            contains.add(module);
         }
      }

      for (Module module : starts) {
         found.add(new MenuScreen.ModuleEntry(module));
      }

      for (Module module : contains) {
         found.add(new MenuScreen.ModuleEntry(module));
      }

      for (Module module : Modules.all()) {
         for (Setting<?> setting : module.settings()) {
            if (found.size() >= 80) {
               return found;
            }

            if (setting.isVisible() && setting.name().toLowerCase(Locale.ROOT).contains(needle)) {
               found.add(new MenuScreen.SettingEntry(module, setting));
            }
         }
      }

      return found;
   }

   private void moduleRow(Module module, float x, float y, float w) {
      MenuPalette p = this.ui.palette;
      String id = "module:" + module.id();
      boolean toggleable = !module.isAlwaysOn();
      boolean chosen = module == this.selected;
      float hover = this.ui.anim(id + ":hover", this.ui.hot(id) && !chosen ? 1.0F : 0.0F, 22.0F);
      float on = toggleable ? this.ui.anim(id + ":on", module.isEnabled() ? 1.0F : 0.0F, 14.0F) : 1.0F;
      float press = this.ui.anim(id + ":press", this.ui.pressed(id) ? 1.0F : 0.0F, 30.0F);
      if (hover > 0.01F) {
         MenuDraw.rect(this.ui, x, y, w, 30.0F, 11.0F, p.hover(hover));
      }

      float shift = press * 1.2F;
      float icon = 22.0F;
      float iconX = x + 5.0F + shift;
      float iconY = y + (30.0F - icon) / 2.0F;
      MenuDraw.rect(this.ui, iconX, iconY, icon, icon, 7.0F, ColorUtil.lerp(p.track(), p.accentSoft(), on));
      this.ui.icon(module.icon(), iconX + icon / 2.0F, iconY + icon / 2.0F, 11.0F, ColorUtil.lerp(p.textDim(), p.accentText(), on));
      float right = x + w - 8.0F;
      if (toggleable) {
         float tw = 26.0F;
         float th = 15.0F;
         MenuWidgets.toggleVisual(this.ui, id + ":toggle", right - tw, y + (30.0F - th) / 2.0F, tw, th, module.isEnabled(), this.ui.pressed(id));
         right -= tw + 7.0F;
      } else {
         this.ui.icon(57455, right - 4.0F, y + 15.0F, 9.0F, p.textFaint());
         right -= 14.0F;
      }

      boolean listening = ("key:" + module.id()).equals(this.ui.listeningId);
      if (listening || module.hasKeybind() && module.key().isBound()) {
         String key = MenuUi.ellipsize(this.ui.medium, listening ? "…" : module.key().keyName(), 6.0F, 40.0F);
         float keyW = this.ui.medium.width(key, 6.0F) + 9.0F;
         MenuDraw.pill(this.ui, right - keyW, y + 15.0F - 6.0F, keyW, 12.0F, listening ? ColorUtil.withAlpha(p.accent(), 85) : p.track());
         this.ui.textCentered(this.ui.medium, key, right - keyW + 4.5F, y + 15.0F, 6.0F, listening ? p.text() : p.textDim());
         right -= keyW + 4.0F;
      }

      if (module.isFavorite() && !this.favorites) {
         this.ui.icon(57718, right - 5.0F, y + 15.0F, 8.0F, ColorUtil.lerp(p.warning(), -1, 0.1F));
         right -= 13.0F;
      }

      float textX = x + 34.0F + shift;
      String name = MenuUi.ellipsize(this.ui.semibold, module.name(), 8.2F, right - textX - 2.0F);
      this.ui
         .textCentered(
            this.ui.semibold,
            name,
            textX,
            y + 15.0F,
            8.2F,
            toggleable ? ColorUtil.lerp(ColorUtil.fade(p.text(), 0.78F), p.text(), Math.max(on, Math.max(hover, chosen ? 1.0F : 0.0F))) : p.text()
         );
      this.ui.target(id, x, y, w, 30.0F, this.rowHandler(module));
      this.ui.tooltip(id + ":tip", x, y, w, 30.0F, rowTooltip(module));
   }

   private static String rowTooltip(Module module) {
      StringBuilder text = new StringBuilder(module.description());
      if (!text.isEmpty()) {
         text.append('\n');
      }

      text.append(I18n.get(module.isAlwaysOn() ? "rose.menu.row_tip.open" : "rose.menu.row_tip.toggle", new Object[0]));
      return text.toString();
   }

   private MenuUi.Handler rowHandler(Module module) {
      return new MenuUi.Handler() {
         private int button = -1;

         @Override
         public void press(int pressed, float localX, float localY) {
            this.button = pressed;
         }

         @Override
         public void release(float localX, float localY, boolean inside) {
            if (inside) {
               switch (this.button) {
                  case 0:
                     if (!module.isAlwaysOn()) {
                        MenuScreen.this.toggle(module);
                        UiSounds.play(module.isEnabled() ? UiSounds.Sound.TOGGLE_ON : UiSounds.Sound.TOGGLE_OFF);
                     } else {
                        UiSounds.play(UiSounds.Sound.CLICK);
                     }

                     MenuScreen.this.select(module);
                     break;
                  case 1:
                     MenuScreen.this.select(module);
                     UiSounds.play(UiSounds.Sound.CLICK);
                     break;
                  case 2:
                     if (!module.hasKeybind()) {
                        return;
                     }

                     MenuScreen.this.ui.closePopups();
                     MenuScreen.this.ui.listen("key:" + module.id(), key -> module.key().set(key));
                     UiSounds.play(UiSounds.Sound.CLICK);
               }
            }
         }
      };
   }

   private void settingRow(Module module, Setting<?> setting, float x, float y, float w) {
      MenuPalette p = this.ui.palette;
      String id = "result:" + module.id() + "." + setting.id();
      float hover = this.ui.anim(id + ":hover", this.ui.hot(id) ? 1.0F : 0.0F, 22.0F);
      if (hover > 0.01F) {
         MenuDraw.rect(this.ui, x, y, w, 30.0F, 11.0F, p.hover(hover));
      }

      float icon = 22.0F;
      float iconX = x + 5.0F;
      float iconY = y + (30.0F - icon) / 2.0F;
      MenuDraw.rect(this.ui, iconX, iconY, icon, icon, 7.0F, p.track());
      this.ui.icon(module.icon(), iconX + icon / 2.0F, iconY + icon / 2.0F, 10.0F, p.textDim());
      float textX = x + 34.0F;
      float textW = w - 34.0F - 20.0F;
      this.ui.textCentered(this.ui.semibold, MenuUi.ellipsize(this.ui.semibold, setting.name(), 7.6F, textW), textX, y + 10.5F, 7.6F, p.text());
      String where = module.name();
      if (!setting.section().isEmpty()) {
         where = where + " · " + I18n.get("rose.section." + setting.section(), new Object[0]);
      }

      this.ui.textCentered(this.ui.regular, MenuUi.ellipsize(this.ui.regular, where, 6.0F, textW), textX, y + 20.5F, 6.0F, p.textDim());
      this.ui.icon(57417, x + w - 12.0F, y + 15.0F, 8.0F, ColorUtil.lerp(p.textFaint(), p.accentText(), hover));
      this.ui.target(id, x, y, w, 30.0F, MenuUi.click(() -> {
         this.openSetting(module, setting);
         UiSounds.play(UiSounds.Sound.CLICK);
      }, () -> this.openSetting(module, setting)));
   }

   private void drawPage() {
      MenuPalette p = this.ui.palette;
      float x = this.winX + 8.0F + 136.0F + 8.0F + 188.0F + 10.0F;
      float right = this.winX + this.winW - 8.0F;
      float w = right - x;
      float top = this.winY;
      float bottom = this.winY + this.winH - 8.0F;
      MenuDraw.vline(this.ui, x - 5.0F, top + 14.0F, this.winH - 28.0F, p.divider());
      float closeSize = 22.0F;
      float closeX = right - closeSize - 2.0F;
      float closeY = top + 9.0F;
      MenuWidgets.iconButton(this.ui, "window:close", closeX, closeY, closeSize, 57778, p.textDim(), this::onClose);
      this.ui.tooltip("window:close:tip", closeX, closeY, closeSize, closeSize, I18n.get("rose.menu.close", new Object[0]));
      Module module = this.selected;
      if (module == null) {
         this.emptyPage(x, top + 76.0F, w, bottom - top - 76.0F);
      } else {
         boolean toggleable = !module.isAlwaysOn();
         float on = toggleable ? this.ui.anim("page:on:" + module.id(), module.isEnabled() ? 1.0F : 0.0F, 14.0F) : 1.0F;
         float icon = 30.0F;
         float iconX = x + 4.0F;
         float iconY = top + 12.0F;
         MenuDraw.rect(this.ui, iconX, iconY, icon, icon, 9.0F, ColorUtil.lerp(p.track(), p.accentSoft(), on));
         this.ui.icon(module.icon(), iconX + icon / 2.0F, iconY + icon / 2.0F, 15.0F, ColorUtil.lerp(p.textDim(), p.accentText(), on));
         float textX = iconX + icon + 9.0F;
         float textRight = closeX - 6.0F;
         if (toggleable) {
            float tw = 36.0F;
            float th = 21.0F;
            float tx = closeX - 10.0F - tw;
            float ty = iconY + (icon - th) / 2.0F;
            MenuWidgets.toggle(this.ui, "page:toggle", tx, ty, tw, th, module.isEnabled(), () -> this.toggle(module));
            textRight = tx - 8.0F;
         }

         this.ui
            .textCentered(this.ui.display, MenuUi.ellipsize(this.ui.display, module.name(), 12.0F, textRight - textX), textX, iconY + 9.5F, 12.0F, p.text());
         String description = module.description();
         if (!description.isEmpty()) {
            String shown = MenuUi.ellipsize(this.ui.regular, description, 6.8F, textRight - textX);
            this.ui.textCentered(this.ui.regular, shown, textX, iconY + 23.0F, 6.8F, p.textDim());
            if (!shown.equals(description)) {
               this.ui.tooltip("page:description", textX, iconY + 16.0F, textRight - textX, 12.0F, description);
            }
         }

         float chipY = top + 50.0F;
         float chipX = x + 4.0F;
         float limit = right - 4.0F;
         if (module.hasKeybind()) {
            boolean listening = "page:key".equals(this.ui.listeningId);
            float keyW = MenuWidgets.keyWidth(this.ui, module.key(), listening);
            MenuWidgets.keybind(this.ui, "page:key", chipX, chipY, module.key());
            this.ui.tooltip("page:key:tip", chipX, chipY, keyW, 18.0F, I18n.get("rose.menu.key_hint", new Object[0]));
            chipX += keyW + 6.0F;
         }

         boolean favorite = module.isFavorite();
         String favoriteLabel = I18n.get(favorite ? "rose.menu.favorite.remove" : "rose.menu.favorite.add", new Object[0]);
         float favoriteW = MenuWidgets.buttonWidth(this.ui, 57718, favoriteLabel);
         if (chipX + favoriteW <= limit) {
            MenuWidgets.button(
               this.ui,
               "page:favorite",
               chipX,
               chipY,
               favoriteW,
               18.0F,
               57718,
               favoriteLabel,
               favorite ? MenuWidgets.Style.ACCENT : MenuWidgets.Style.NORMAL,
               () -> module.setFavorite(!favorite)
            );
            chipX += favoriteW + 6.0F;
         }

         if (!module.settings().isEmpty()) {
            boolean confirming = module == this.resetPending && System.currentTimeMillis() - this.resetSince < 3000L;
            String resetLabel = I18n.get(confirming ? "rose.menu.reset.confirm" : "rose.menu.reset", new Object[0]);
            float resetW = MenuWidgets.buttonWidth(this.ui, 57672, resetLabel);
            if (chipX + resetW <= limit) {
               MenuWidgets.button(
                  this.ui,
                  "page:reset",
                  chipX,
                  chipY,
                  resetW,
                  18.0F,
                  57672,
                  resetLabel,
                  confirming ? MenuWidgets.Style.DANGER : MenuWidgets.Style.GHOST,
                  () -> this.resetModule(module)
               );
               if (!confirming) {
                  this.ui.tooltip("page:reset:tip", chipX, chipY, resetW, 18.0F, I18n.get("rose.menu.reset.tip", new Object[0]));
               }
            }
         }

         float viewY = top + 76.0F;
         float viewH = bottom - viewY;
         this.ui.target("page:view", x - 4.0F, viewY, w + 4.0F, viewH, this.pageScroll.wheel());
         this.ui.clip(x - 4.0F, viewY, w + 4.0F, viewH);
         this.ui.viewport(viewY, viewY + viewH);
         float contentTop = viewY + 4.0F - this.pageScroll.value();
         float contentX = x;
         float contentW = w - 6.0F;
         float contentH;
         if (module instanceof ProfilesModule) {
            contentH = this.profiles.draw(this.ui, contentX, contentTop, contentW);
         } else if (module instanceof LooksModule looksModule) {
            contentH = this.looks.draw(this.ui, looksModule, contentX, contentTop, contentW);
         } else if (module instanceof KeysModule) {
            contentH = this.keys.draw(this.ui, contentX, contentTop, contentW);
         } else if (module instanceof ThemeModule) {
            float gallery = this.themes.draw(this.ui, contentX, contentTop, contentW);
            contentH = gallery + this.settings.draw(this.ui, module, contentX, contentTop + gallery, contentW);
         } else {
            contentH = this.settings.draw(this.ui, module, contentX, contentTop, contentW);
         }

         this.ui.clearViewport();
         this.ui.unclip();
         this.pageScroll.update(this.ui, contentH + 16.0F, viewH);
         this.pageScroll.drawBar(this.ui, "page:bar", right + 3.0F, viewY + 6.0F, viewH - 12.0F);
         if (this.scrollToHighlight) {
            float offset = this.settings.highlightOffset();
            if (!Float.isNaN(offset)) {
               this.scrollToHighlight = false;
               this.pageScroll.scrollTo(offset - contentTop - viewH * 0.3F);
            } else if (System.currentTimeMillis() - this.highlightRequested > 1000L) {
               this.scrollToHighlight = false;
            }
         }
      }
   }

   private void emptyPage(float x, float y, float w, float h) {
      MenuPalette p = this.ui.palette;
      float cy = y + h / 2.0F - 50.0F;
      this.ui.icon(57632, x + w / 2.0F, cy, 24.0F, p.textFaint());
      String title = I18n.get("rose.menu.pick_module", new Object[0]);
      this.ui.text(this.ui.semibold, title, x + (w - this.ui.semibold.width(title, 9.0F)) / 2.0F, cy + 20.0F, 9.0F, p.text());
      String[] hints = new String[]{
         I18n.get("rose.menu.hint.left", new Object[0]),
         I18n.get("rose.menu.hint.right", new Object[0]),
         I18n.get("rose.menu.hint.middle", new Object[0])
      };

      for (int i = 0; i < hints.length; i++) {
         String hint = MenuUi.ellipsize(this.ui.regular, hints[i], 7.2F, w - 24.0F);
         this.ui.text(this.ui.regular, hint, x + (w - this.ui.regular.width(hint, 7.2F)) / 2.0F, cy + 38.0F + i * 11, 7.2F, p.textDim());
      }
   }

   private void drawResizeHandle() {
      MenuPalette p = this.ui.palette;
      float size = 16.0F;
      float hx = this.winX + this.winW - size - 3.0F;
      float hy = this.winY + this.winH - size - 3.0F;
      float active = this.ui.anim("window:resize:hover", !this.ui.hot("window:resize") && this.drag != MenuScreen.Drag.RESIZE ? 0.0F : 1.0F, 20.0F);
      int color = ColorUtil.lerp(ColorUtil.fade(p.text(), 0.25F), p.text(), active);

      for (int a = 0; a < 3; a++) {
         for (int b = 0; a + b < 3; b++) {
            MenuDraw.circle(this.ui, hx + size - 5.0F - a * 3.6F, hy + size - 5.0F - b * 3.6F, 0.85F, color);
         }
      }

      this.ui.target("window:resize", hx, hy, size, size, new MenuUi.Handler() {
         private float startW;
         private float startH;
         private float pressX;
         private float pressY;

         @Override
         public void press(int button, float localX, float localY) {
            MenuScreen.this.drag = MenuScreen.Drag.RESIZE;
            this.startW = MenuScreen.this.winW;
            this.startH = MenuScreen.this.winH;
            this.pressX = localX;
            this.pressY = localY;
            MenuScreen.this.ui.closePopups();
         }

         @Override
         public void drag(float localX, float localY) {
            if (MenuScreen.this.drag == MenuScreen.Drag.RESIZE) {
               float screenW = MenuScreen.this.width / MenuScreen.this.scale;
               float screenH = MenuScreen.this.height / MenuScreen.this.scale;
               MenuScreen.this.winW = Math.min(Math.max(520.0F, this.startW + localX - this.pressX), Math.max(520.0F, screenW - MenuScreen.this.winX));
               MenuScreen.this.winH = Math.min(Math.max(300.0F, this.startH + localY - this.pressY), Math.max(300.0F, screenH - MenuScreen.this.winY));
            }
         }

         @Override
         public void release(float localX, float localY, boolean inside) {
            MenuScreen.this.drag = MenuScreen.Drag.NONE;
            MenuScreen.this.saveWindow();
         }
      });
      this.ui.tooltip("window:resize:tip", hx, hy, size, size, I18n.get("rose.menu.resize", new Object[0]));
   }

   private MenuUi.Handler moveHandler() {
      return new MenuUi.Handler() {
         private float startX;
         private float startY;
         private float pressX;
         private float pressY;

         @Override
         public void press(int button, float localX, float localY) {
            if (button == 0) {
               MenuScreen.this.drag = MenuScreen.Drag.MOVE;
               this.startX = MenuScreen.this.winX;
               this.startY = MenuScreen.this.winY;
               this.pressX = localX;
               this.pressY = localY;
               MenuScreen.this.ui.closePopups();
            }
         }

         @Override
         public void drag(float localX, float localY) {
            if (MenuScreen.this.drag == MenuScreen.Drag.MOVE) {
               MenuScreen.this.winX = this.startX + localX - this.pressX;
               MenuScreen.this.winY = this.startY + localY - this.pressY;
               MenuScreen.this.clampWindow();
            }
         }

         @Override
         public void release(float localX, float localY, boolean inside) {
            if (MenuScreen.this.drag == MenuScreen.Drag.MOVE) {
               MenuScreen.this.drag = MenuScreen.Drag.NONE;
               MenuScreen.this.saveWindow();
            }
         }
      };
   }

   private void openCategory(Category next) {
      boolean changed = this.favorites || this.category != next || !this.search.isEmpty();
      this.favorites = false;
      this.category = next;
      lastCategory = next;
      lastFavorites = false;
      if (!this.search.isEmpty()) {
         this.clearSearch();
      }

      if (changed) {
         this.listScroll.reset();
         this.rowSnapped = false;
         this.select(this.remembered());
         UiSounds.play(UiSounds.Sound.TAB);
      }
   }

   private void openFavorites() {
      boolean changed = !this.favorites || !this.search.isEmpty();
      this.favorites = true;
      lastFavorites = true;
      if (!this.search.isEmpty()) {
         this.clearSearch();
      }

      if (changed) {
         this.listScroll.reset();
         this.rowSnapped = false;
         this.select(this.remembered());
         UiSounds.play(UiSounds.Sound.TAB);
      }
   }

   private Module remembered() {
      String id = this.favorites ? lastFavoriteModule : lastModules.get(this.category);
      Module module = id == null ? null : Modules.byId(id);
      if (this.favorites) {
         if (module != null && module.isFavorite()) {
            return module;
         }

         for (Module candidate : Modules.all()) {
            if (candidate.isFavorite()) {
               return candidate;
            }
         }

         return null;
      } else {
         if (module != null && module.category() == this.category) {
            return module;
         }

         List<Module> list = Modules.in(this.category);
         return list.isEmpty() ? null : list.getFirst();
      }
   }

   private void select(Module module) {
      if (module != this.selected) {
         this.selected = module;
         this.pageScroll.reset();
         this.ui.closePopups();
         if (this.ui.focused != this.search) {
            this.ui.unfocus();
         }

         this.resetPending = null;
         if (module != null) {
            this.settings.restartAppear(this.ui, module);
         }

         if (module instanceof ProfilesModule) {
            this.profiles.refresh();
         }

         if (module instanceof ThemeModule) {
            this.themes.refresh();
         }

         if (module instanceof KeysModule) {
            this.keys.refresh();
         }

         if (module instanceof LooksModule) {
            this.looks.restartAppear(this.ui);
         }
      }

      if (module != null) {
         if (this.search.isEmpty()) {
            if (this.favorites) {
               lastFavoriteModule = module.id();
            } else {
               lastModules.put(this.category, module.id());
            }
         }
      }
   }

   private void openSetting(Module module, Setting<?> setting) {
      this.select(module);
      this.settings.highlight(setting);
      this.scrollToHighlight = true;
      this.highlightRequested = System.currentTimeMillis();
   }

   private void toggle(Module module) {
      module.toggle();
      this.character.moduleToggled(module.name(), module.isEnabled());
   }

   private void resetModule(Module module) {
      long now = System.currentTimeMillis();
      if (this.resetPending == module && now - this.resetSince <= 3000L) {
         this.resetPending = null;
         module.resetSettings();
         Notifications.show(Notifications.Kind.NEUTRAL, module.name(), I18n.get("rose.gui.reset_done", new Object[0]));
      } else {
         this.resetPending = module;
         this.resetSince = now;
      }
   }

   private void focusSearch() {
      this.ui.closePopups();
      this.ui.focus("search", this.search, this::searchChanged, this::submitSearch);
   }

   private void searchChanged() {
      this.listScroll.reset();
      this.rowSnapped = false;
      if (this.search.isEmpty()) {
         this.select(this.remembered());
      }
   }

   private void submitSearch() {
      List<MenuScreen.Entry> entries = this.entries();
      if (!entries.isEmpty()) {
         switch ((MenuScreen.Entry)entries.getFirst()) {
            case MenuScreen.ModuleEntry entry:
               this.select(entry.module());
               break;
            case MenuScreen.SettingEntry entry:
               this.openSetting(entry.module(), entry.setting());
               break;
            default:
               throw new MatchException(null, null);
         }

         UiSounds.play(UiSounds.Sound.CLICK);
      }
   }

   private void clearSearch() {
      this.search.clear();
      this.resultsFor = "";
      this.results = List.of();
      if (this.ui.focused == this.search) {
         this.ui.unfocus();
      }

      this.listScroll.reset();
      this.rowSnapped = false;
   }

   private void moveSelection(int delta) {
      List<MenuScreen.Entry> entries = this.entries();
      List<Integer> modules = new ArrayList<>();

      for (int i = 0; i < entries.size(); i++) {
         if (entries.get(i) instanceof MenuScreen.ModuleEntry) {
            modules.add(i);
         }
      }

      if (!modules.isEmpty()) {
         int current = -1;

         for (int i = 0; i < modules.size(); i++) {
            if (((MenuScreen.ModuleEntry)entries.get(modules.get(i))).module() == this.selected) {
               current = i;
            }
         }

         int next = current < 0 ? 0 : Math.floorMod(current + delta, modules.size());
         int row = modules.get(next);
         this.select(((MenuScreen.ModuleEntry)entries.get(row)).module());
         float rowTop = row * 32.0F;
         float view = this.winH - 46.0F - 8.0F - 20.0F - 8.0F;
         if (rowTop < this.listScroll.value()) {
            this.listScroll.scrollTo(rowTop);
         } else if (rowTop + 30.0F > this.listScroll.value() + view) {
            this.listScroll.scrollTo(rowTop + 30.0F - view);
         }

         UiSounds.play(UiSounds.Sound.HOVER);
      }
   }

   public void openHudEditor() {
      Screen back = this.parent;
      this.closing = true;
      this.ui.unfocus();
      this.ui.closePopups();
      this.minecraft.setScreen(new HudEditorScreen(() -> new MenuScreen(back)));
   }

   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();
      if (this.closing) {
         return true;
      }

      float x = (float)mouseX / this.scale;
      float y = (float)mouseY / this.scale;
      this.ui.blurOutside(this.ui.targetAt(x, y));
      if (!this.ui.press(x, y, button)) {
         this.ui.closePopups();
         if (button == 0) {
            this.character.click(x, y);
         }
      }

      return true;
   }

   public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();
      this.ui.drag((float)mouseX / this.scale, (float)mouseY / this.scale);
      return true;
   }

   public boolean mouseReleased(MouseButtonEvent event) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();
      this.ui.release((float)mouseX / this.scale, (float)mouseY / this.scale);
      if (this.drag != MenuScreen.Drag.NONE) {
         this.drag = MenuScreen.Drag.NONE;
         this.saveWindow();
      }

      return true;
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
      this.ui.scroll((float)mouseX / this.scale, (float)mouseY / this.scale, scrollY);
      return true;
   }

   public boolean keyPressed(KeyEvent event) {
      int key = event.key();
      int scancode = event.scancode();
      int modifiers = event.modifiers();
      if (this.closing) {
         return true;
      }

      if (this.ui.listening != null) {
         IntConsumer consumer = this.ui.listening;
         this.ui.listening = null;
         this.ui.listeningId = null;
         boolean clear = key == 256 || key == 259 || key == 261;
         consumer.accept(clear ? -1 : key);
         UiSounds.play(UiSounds.Sound.CLICK);
         return true;
      }

      if (this.ui.focused == null) {
         boolean control = (modifiers & 2) != 0;
         if (control && key == 70) {
            this.focusSearch();
            return true;
         }

         switch (key) {
            case 32:
               if (this.selected != null && !this.selected.isAlwaysOn()) {
                  this.toggle(this.selected);
                  UiSounds.play(this.selected.isEnabled() ? UiSounds.Sound.TOGGLE_ON : UiSounds.Sound.TOGGLE_OFF);
               }

               return true;
            case 256:
               if (this.ui.popupOpen()) {
                  this.ui.closePopups();
               } else if (!this.search.isEmpty()) {
                  this.clearSearch();
                  this.select(this.remembered());
               } else {
                  this.onClose();
               }

               return true;
            case 264:
            case 265:
               this.moveSelection(key == 264 ? 1 : -1);
               return true;
            default:
               if (key == Modules.clickGui().key().get() && System.currentTimeMillis() - this.openedAt > 250L) {
                  this.onClose();
                  return true;
               } else {
                  return false;
               }
         }
      } else {
         switch (key) {
            case 256:
               if (this.ui.focused == this.search && !this.search.isEmpty()) {
                  this.clearSearch();
                  this.select(this.remembered());
               } else {
                  this.ui.unfocus();
               }
               break;
            case 257:
            case 335:
               Runnable submit = this.ui.focusedSubmit;
               if (submit != null) {
                  submit.run();
               } else {
                  this.ui.unfocus();
               }
               break;
            case 264:
            case 265:
               if (this.ui.focused == this.search) {
                  this.moveSelection(key == 264 ? 1 : -1);
               }
               break;
            default:
               Runnable change = this.ui.focusedChange;
               if (this.ui.focused.key(key, modifiers) && change != null) {
                  change.run();
               }
         }

         return true;
      }
   }

   public boolean charTyped(CharacterEvent event) {
      char character = (char)event.codepoint();
      int modifiers = event.modifiers();
      if (!this.closing && this.ui.listening == null) {
         if (this.ui.focused == null) {
            if (character <= ' ') {
               return true;
            }

            this.focusSearch();
         }

         Runnable change = this.ui.focusedChange;
         if (this.ui.focused.insert(String.valueOf(character)) && change != null) {
            change.run();
         }

         return true;
      } else {
         return true;
      }
   }

   public void onClose() {
      if (!this.closing) {
         this.closing = true;
         this.ui.unfocus();
         this.ui.closePopups();
         this.ui.listening = null;
         this.ui.listeningId = null;
         this.open.configure(0.36, 0.92);
         this.open.setTarget(0.0);
         UiSounds.play(UiSounds.Sound.CLOSE);
         if (Modules.clickGui().openAnimation() == ClickGuiModule.OpenAnimation.NONE) {
            this.minecraft.setScreen(this.parent);
         }
      }
   }

   public boolean isPauseScreen() {
      return Modules.clickGui().pausesGame();
   }

   private double[] center(String id, float fx, float fy) {
      MenuUi.Target target = this.ui.find(id);
      if (target == null) {
         RoseClient.LOGGER.warn("[Rose][menutest] target not found: {}", id);
         return null;
      } else {
         return new double[]{(target.x() + target.w() * fx) * this.scale, (target.y() + target.h() * fy) * this.scale};
      }
   }

   public void testClick(String id, int button) {
      double[] point = this.center(id, 0.5F, 0.5F);
      if (point != null) {
         this.mouseClicked(new MouseButtonEvent(point[0], point[1], new MouseButtonInfo(button, 0)), false);
         this.mouseReleased(new MouseButtonEvent(point[0], point[1], new MouseButtonInfo(button, 0)));
      }
   }

   public void testHover(String id) {
      double[] point = this.center(id, 0.5F, 0.5F);
      if (point != null) {
         this.testMouse = point;
      }
   }

   public void testDrag(String id, float fx, float fy, float dx, float dy) {
      double[] point = this.center(id, fx, fy);
      if (point != null) {
         this.mouseClicked(new MouseButtonEvent(point[0], point[1], new MouseButtonInfo(0, 0)), false);

         for (int i = 1; i <= 8; i++) {
            this.mouseDragged(new MouseButtonEvent(point[0] + dx * this.scale * i / 8.0F, point[1] + dy * this.scale * i / 8.0F, new MouseButtonInfo(0, 0)), 0.0, 0.0);
         }

         this.mouseReleased(new MouseButtonEvent(point[0] + dx * this.scale, point[1] + dy * this.scale, new MouseButtonInfo(0, 0)));
      }
   }

   public void testScroll(String id, double amount) {
      double[] point = this.center(id, 0.5F, 0.5F);
      if (point != null) {
         this.mouseScrolled(point[0], point[1], 0.0, amount);
      }
   }

   public boolean testPopupOpen() {
      return this.ui.popupOpen();
   }

   public void testKey(int key, int modifiers) {
      this.keyPressed(new KeyEvent(key, 0, modifiers));
   }

   public void testType(String text) {
      for (char c : text.toCharArray()) {
         this.charTyped(new CharacterEvent(c, 0));
      }
   }

   public void testMouseAway() {
      this.testMouse = new double[]{-10.0, -10.0};
   }

   private enum Drag {
      NONE,
      MOVE,
      RESIZE;
   }

   private sealed interface Entry permits MenuScreen.ModuleEntry, MenuScreen.SettingEntry {
   }

   private record ModuleEntry(Module module) implements MenuScreen.Entry {
   }

   private record SettingEntry(Module module, Setting<?> setting) implements MenuScreen.Entry {
   }
}
