package dev.rose.module.impl;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.notify.Notifications;
import dev.rose.server.KnownServers;
import net.minecraft.client.resources.language.I18n;


public final class ServerWarningModule extends Module {
   public ServerWarningModule() {
      super("server_warning", Category.INTERFACE, true);
   }

   @Override
   public int icon() {
      return 57854;
   }

   public void onJoin() {
      if (this.isEnabled()) {
         KnownServers.whitelisted(KnownServers.currentHost())
            .ifPresent(
               server -> Notifications.show(
                  Notifications.Kind.WARNING,
                  I18n.get("rose.warning.whitelist.title", new Object[]{server.name()}),
                  I18n.get("rose.warning.whitelist.subtitle", new Object[0])
               )
            );
      }
   }
}
