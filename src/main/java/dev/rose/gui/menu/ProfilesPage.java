package dev.rose.gui.menu;

import dev.rose.config.ConfigManager;
import dev.rose.notify.Notifications;
import dev.rose.server.KnownServers;
import dev.rose.server.ServerProfiles;
import dev.rose.util.ColorUtil;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;


public final class ProfilesPage {
   private static final float ROW = 32.0F;
   private static final float GAP = 10.0F;
   private static final long CONFIRM_MS = 3000L;
   private final TextBuffer name = new TextBuffer(32);
   private final TextBuffer server = new TextBuffer(64);
   private List<String> profiles;
   private int profileChoice;
   private String pendingDelete;
   private long pendingSince;

   public void refresh() {
      this.profiles = ConfigManager.profiles();
      this.pendingDelete = null;
   }

   public float draw(MenuUi ui, float x, float y, float w) {
      if (this.profiles == null) {
         this.refresh();
      }

      float cursor = y;
      cursor += MenuWidgets.sectionHeader(ui, 57677, I18n.get("rose.profiles.section.new", new Object[0]), x, cursor);
      cursor = this.newProfile(ui, x, cursor, w) + 10.0F;
      cursor += MenuWidgets.sectionHeader(ui, 58665, I18n.get("rose.profiles.section.saved", new Object[0]), x, cursor);
      cursor = this.saved(ui, x, cursor, w) + 10.0F;
      cursor += MenuWidgets.sectionHeader(ui, 57683, I18n.get("rose.profiles.section.servers", new Object[0]), x, cursor);
      cursor = this.servers(ui, x, cursor, w) + 10.0F;
      cursor += MenuWidgets.sectionHeader(ui, 57502, I18n.get("rose.profiles.section.share", new Object[0]), x, cursor);
      cursor = this.share(ui, x, cursor, w) + 10.0F;
      return cursor - y;
   }

   private float newProfile(MenuUi ui, float x, float y, float w) {
      MenuPalette p = ui.palette;
      float h = 36.0F;
      MenuDraw.rect(ui, x, y, w, h, 14.0F, p.group());
      String save = I18n.get("rose.profiles.save", new Object[0]);
      float buttonW = MenuWidgets.buttonWidth(ui, 57677, save);
      float fieldY = y + (h - 18.0F) / 2.0F;
      MenuWidgets.textField(
         ui,
         "profiles:name",
         x + 10.0F,
         fieldY,
         w - 28.0F - buttonW,
         this.name,
         I18n.get("rose.profiles.name_hint", new Object[0]),
         null,
         this::saveNew
      );
      MenuWidgets.button(
         ui,
         "profiles:save",
         x + w - 10.0F - buttonW,
         fieldY,
         buttonW,
         18.0F,
         57677,
         save,
         this.name.isEmpty() ? MenuWidgets.Style.NORMAL : MenuWidgets.Style.ACCENT,
         this::saveNew
      );
      return y + h;
   }

   private void saveNew() {
      String value = this.name.text().trim();
      if (!value.isEmpty()) {
         ConfigManager.saveProfile(value);
         this.name.clear();
         this.refresh();
         Notifications.show(Notifications.Kind.SUCCESS, I18n.get("rose.profiles.saved", new Object[0]), value);
      }
   }

   private float saved(MenuUi ui, float x, float y, float w) {
      MenuPalette p = ui.palette;
      if (this.profiles.isEmpty()) {
         MenuDraw.rect(ui, x, y, w, 32.0F, 14.0F, p.group());
         ui.textCentered(ui.regular, I18n.get("rose.profiles.empty", new Object[0]), x + 10.0F, y + 16.0F, 7.4F, p.textFaint());
         return y + 32.0F;
      }

      float h = this.profiles.size() * 32.0F;
      MenuDraw.rect(ui, x, y, w, h, 14.0F, p.group());
      if (this.pendingDelete != null && System.currentTimeMillis() - this.pendingSince > 3000L) {
         this.pendingDelete = null;
      }

      for (int i = 0; i < this.profiles.size(); i++) {
         String profile = this.profiles.get(i);
         float rowY = y + i * 32.0F;
         float buttonY = rowY + 7.0F;
         float right = x + w - 8.0F;
         if (i > 0) {
            MenuDraw.hline(ui, x + 10.0F, rowY, w - 20.0F, p.divider());
         }

         boolean confirming = profile.equals(this.pendingDelete);
         String deleteLabel = confirming ? I18n.get("rose.profiles.confirm", new Object[0]) : "";
         float deleteW = MenuWidgets.buttonWidth(ui, 57742, deleteLabel);
         right -= deleteW;
         MenuWidgets.button(
            ui,
            "profiles:delete:" + profile,
            right,
            buttonY,
            deleteW,
            18.0F,
            57742,
            deleteLabel,
            confirming ? MenuWidgets.Style.DANGER : MenuWidgets.Style.GHOST,
            () -> this.delete(profile)
         );
         float overwriteW = MenuWidgets.buttonWidth(ui, 57677, "");
         right -= overwriteW + 4.0F;
         MenuWidgets.button(ui, "profiles:overwrite:" + profile, right, buttonY, overwriteW, 18.0F, 57677, "", MenuWidgets.Style.GHOST, () -> {
            ConfigManager.saveProfile(profile);
            Notifications.show(Notifications.Kind.SUCCESS, I18n.get("rose.profiles.saved", new Object[0]), profile);
         });
         ui.tooltip(
            "profiles:overwrite:" + profile + ":tip", right, buttonY, overwriteW, 18.0F, I18n.get("rose.profiles.overwrite", new Object[0])
         );
         String load = I18n.get("rose.profiles.load", new Object[0]);
         float loadW = MenuWidgets.buttonWidth(ui, 57452, load);
         right -= loadW + 4.0F;
         MenuWidgets.button(
            ui,
            "profiles:load:" + profile,
            right,
            buttonY,
            loadW,
            18.0F,
            57452,
            load,
            MenuWidgets.Style.NORMAL,
            () -> {
               boolean loaded = ConfigManager.loadProfile(profile);
               Notifications.show(
                  loaded ? Notifications.Kind.SUCCESS : Notifications.Kind.ERROR,
                  I18n.get(loaded ? "rose.profiles.loaded" : "rose.profiles.load_failed", new Object[0]),
                  profile
               );
            }
         );
         ui.textCentered(ui.semibold, MenuUi.ellipsize(ui.semibold, profile, 8.0F, right - x - 20.0F), x + 10.0F, rowY + 16.0F, 8.0F, p.text());
      }

      return y + h;
   }

   private void delete(String profile) {
      if (!profile.equals(this.pendingDelete)) {
         this.pendingDelete = profile;
         this.pendingSince = System.currentTimeMillis();
      } else {
         ConfigManager.deleteProfile(profile);
         this.refresh();
         Notifications.show(Notifications.Kind.NEUTRAL, I18n.get("rose.profiles.deleted", new Object[0]), profile);
      }
   }

   private float servers(MenuUi ui, float x, float y, float w) {
      MenuPalette p = ui.palette;
      List<ServerProfiles.Binding> bindings = ServerProfiles.bindings();
      float h = (bindings.size() + 1) * 32.0F;
      MenuDraw.rect(ui, x, y, w, h, 14.0F, p.group());
      String current = KnownServers.currentHost();

      for (int i = 0; i < bindings.size(); i++) {
         ServerProfiles.Binding binding = bindings.get(i);
         float rowY = y + i * 32.0F;
         float center = rowY + 16.0F;
         if (i > 0) {
            MenuDraw.hline(ui, x + 10.0F, rowY, w - 20.0F, p.divider());
         }

         ui.icon(57683, x + 14.0F, center, 8.0F, p.accent());
         float hostW = ui.textCentered(ui.semibold, binding.server(), x + 24.0F, center, 7.6F, p.text());
         ui.icon(57417, x + 30.0F + hostW, center, 7.0F, p.textFaint());
         ui.textCentered(ui.medium, binding.profile(), x + 37.0F + hostW, center, 7.6F, p.textDim());
         float deleteW = MenuWidgets.buttonWidth(ui, 57742, "");
         MenuWidgets.button(
            ui,
            "profiles:unbind:" + binding.server(),
            x + w - 8.0F - deleteW,
            rowY + 7.0F,
            deleteW,
            18.0F,
            57742,
            "",
            MenuWidgets.Style.GHOST,
            () -> ServerProfiles.unbind(binding.server())
         );
         boolean active = binding.profile().equals(ServerProfiles.activeProfile())
            && current != null
            && (current.equals(binding.server()) || current.endsWith("." + binding.server()));
         if (active) {
            String badge = I18n.get("rose.server_profiles.active", new Object[0]);
            float badgeW = ui.medium.width(badge, 6.2F) + 10.0F;
            float badgeX = x + w - 14.0F - deleteW - badgeW;
            MenuDraw.pill(ui, badgeX, center - 6.0F, badgeW, 12.0F, ColorUtil.withAlpha(p.positive(), 64));
            ui.textCentered(ui.medium, badge, badgeX + 5.0F, center, 6.2F, p.text());
         }
      }

      float rowY = y + bindings.size() * 32.0F;
      float fieldY = rowY + 7.0F;
      if (!bindings.isEmpty()) {
         MenuDraw.hline(ui, x + 10.0F, rowY, w - 20.0F, p.divider());
      }

      if (this.profileChoice >= this.profiles.size()) {
         this.profileChoice = 0;
      }

      String add = I18n.get("rose.server_profiles.add", new Object[0]);
      float addW = MenuWidgets.buttonWidth(ui, 57661, add);
      float chooserW = Math.min(96.0F, w * 0.3F);
      float fieldW = w - 20.0F - addW - chooserW - 12.0F;
      String hint = current != null ? current : I18n.get("rose.server_profiles.host_hint", new Object[0]);
      MenuWidgets.textField(ui, "profiles:host", x + 10.0F, fieldY, fieldW, this.server, hint, null, this::bindServer);
      float chooserX = x + 10.0F + fieldW + 6.0F;
      if (this.profiles.isEmpty()) {
         MenuWidgets.dropdown(
            ui,
            "profiles:choice",
            chooserX,
            fieldY,
            chooserW,
            new String[]{I18n.get("rose.server_profiles.no_profiles", new Object[0])},
            0,
            i -> {}
         );
      } else {
         MenuWidgets.dropdown(
            ui, "profiles:choice", chooserX, fieldY, chooserW, this.profiles.toArray(String[]::new), this.profileChoice, i -> this.profileChoice = i
         );
      }

      ui.tooltip("profiles:choice:tip", chooserX, fieldY, chooserW, 18.0F, I18n.get("rose.server_profiles.choose", new Object[0]));
      boolean ready = !this.profiles.isEmpty() && (!this.server.isEmpty() || current != null);
      MenuWidgets.button(
         ui,
         "profiles:bind",
         x + w - 10.0F - addW,
         fieldY,
         addW,
         18.0F,
         57661,
         add,
         ready ? MenuWidgets.Style.ACCENT : MenuWidgets.Style.NORMAL,
         this::bindServer
      );
      return y + h;
   }

   private void bindServer() {
      String host = this.server.text().trim();
      if (host.isEmpty() && KnownServers.currentHost() != null) {
         host = KnownServers.currentHost();
      }

      if (!host.isEmpty()) {
         if (this.profiles.isEmpty()) {
            Notifications.show(
               Notifications.Kind.WARNING,
               I18n.get("rose.server_profiles.no_profiles", new Object[0]),
               I18n.get("rose.server_profiles.no_profiles_hint", new Object[0])
            );
         } else {
            String profile = this.profiles.get(Math.min(this.profileChoice, this.profiles.size() - 1));
            ServerProfiles.bind(host, profile);
            this.server.clear();
            Notifications.show(
               Notifications.Kind.SUCCESS, I18n.get("rose.server_profiles.bound", new Object[]{profile}), KnownServers.normalizeAddress(host)
            );
         }
      }
   }

   private float share(MenuUi ui, float x, float y, float w) {
      MenuPalette p = ui.palette;
      float h = 64.0F;
      MenuDraw.rect(ui, x, y, w, h, 14.0F, p.group());
      Minecraft mc = Minecraft.getInstance();
      this.shareRow(
         ui,
         x,
         y,
         w,
         "copy",
         57502,
         () -> {
            mc.keyboardHandler.setClipboard(ConfigManager.exportCode());
            Notifications.show(
               Notifications.Kind.SUCCESS,
               I18n.get("rose.profiles.copied", new Object[0]),
               I18n.get("rose.profiles.copied_hint", new Object[0])
            );
         }
      );
      MenuDraw.hline(ui, x + 10.0F, y + 32.0F, w - 20.0F, p.divider());
      this.shareRow(
         ui,
         x,
         y + 32.0F,
         w,
         "paste",
         58344,
         () -> {
            boolean imported = ConfigManager.importCode(mc.keyboardHandler.getClipboard());
            Notifications.show(
               imported ? Notifications.Kind.SUCCESS : Notifications.Kind.ERROR,
               I18n.get(imported ? "rose.profiles.imported" : "rose.profiles.import_failed", new Object[0]),
               ""
            );
         }
      );
      return y + h;
   }

   private void shareRow(MenuUi ui, float x, float y, float w, String id, int icon, Runnable action) {
      MenuPalette p = ui.palette;
      String button = I18n.get("rose.profiles." + id + ".button", new Object[0]);
      float buttonW = MenuWidgets.buttonWidth(ui, icon, button);
      ui.textCentered(
         ui.medium,
         MenuUi.ellipsize(ui.medium, I18n.get("rose.profiles." + id, new Object[0]), 8.0F, w - buttonW - 28.0F),
         x + 10.0F,
         y + 16.0F,
         8.0F,
         p.text()
      );
      MenuWidgets.button(ui, "profiles:" + id, x + w - 10.0F - buttonW, y + 7.0F, buttonW, 18.0F, icon, button, MenuWidgets.Style.NORMAL, action);
   }
}
