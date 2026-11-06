package sg.modules.impl.init;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import sg.ec.UnhookState;
import sg.ec.MinecraftAccess;
import sg.ec.ClientService;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.hud.HudScaleCoordinates;
import sg.ec.ClientFonts;
import sg.network.PlayerWarningTracker;
import sg.network.StatusNotificationToast;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.util.time.ColorMath;
import sg.client.ClientServices;
import sg.client.RoseClient;
import sg.modules.impl.render.Hud;
import sg.events.impl.HudRenderEvent;
import sg.hud.Notifications;

public class NotificationManager extends ClientService implements MinecraftAccess {
   private static final float ДК = 16.0F;
   private static final List<NotificationToast> сI = new LinkedList<>();
   private final ArrayList<NotificationToast> Ч = new ArrayList<>(10);
   private boolean ес;
   private final PlayerWarningTracker В = new PlayerWarningTracker();
   private final EventListener<PlayerTickEvent> ЯС = this::Эи;
   private final EventListener<HudRenderEvent> Яз = new EventListener<HudRenderEvent>() {
      @Override
      public int getPriority() {
         return -90;
      }

      public void onEvent(HudRenderEvent var1) {
         NotificationManager.this.method_8(var1);
      }
   };
   private static final String ЯЮ0 = "Уведомления";
   private static final long ЯЮх = 3500L;
   private static final String ЯЮУ = "Уведомления";
   private static final double ЯЮЗ = Double.MAX_VALUE;
   private static final double ЯЮЕ = 16.0;
   private static final double ЯЮП = 16.0;
   private static final String ЯЮ西 = "TNT рядом — расстояние %.1f блоков, взорвётся через %.1f сек.";
   private static final float ЯЮд = 20.0F;
   private static final String ЯЮБ = "Уведомления";
   private static final float ЯЮС = 0.001F;
   private static final float ЯЮз = 36.0F;
   private static final float ЯЮц = 4.0F;
   private static final float ЯЮя = 0.001F;
   private static final float ЯЧс = 36.0F;
   private static final float ЯЧВ = 36.0F;
   private static final float ЯЧЯ = 0.5F;

   private static void р(NotificationToast var0) {
      р(var0, NotificationCategory.OTHER);
   }

   public static boolean р(NotificationCategory var0) {
      if (!UnhookState.ые() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         ClientServices var1 = RoseClient.getInstance().getServices();
         if (var1 == null) {
            return false;
         }

         Hud var2 = var1.getModules().getHud();
         return var2.isEnabled() && var2.я().isSelected(ЯЮ0) && var0.ъ(var2.ъ_3bdb855e());
      } else {
         return false;
      }
   }

   public static void р(String var0, Component var1, int var2, NotificationCategory var3) {
      if (р(var3)) {
         р(new StatusNotificationToast(var0, var1, var2), var3);
      }
   }

   public static void р(String var0, String var1, String var2, int var3) {
      р(var0, var1, var3);
   }

   public static void р(String var0, Component var1, int var2) {
      р(new StatusNotificationToast(var0, var1, var2));
   }

   public static void р(Component var0, ItemStack var1, int var2) {
      р(var0, var1, var2, NotificationCategory.OTHER);
   }

   public void method_8(HudRenderEvent var1) {
      if (MINECRAFT_CLIENT.getWindow() != null) {
         ClientServices var2 = RoseClient.getInstance().getServices();
         if (var2 != null) {
            Notifications var3 = var2.getHudElements().getNotifications();
            Hud var4 = var2.getModules().getHud();
            if (!UnhookState.ые() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && var4.isEnabled() && var4.я().isSelected(ЯЮБ)) {
               synchronized (сI) {
                  сI.removeIf(var1x -> !var1x.Ж_791d2e21().ъ(var4.ъ_3bdb855e()) || var1x.ЖН() && var1x.method__().isFinished());
                  this.Ч.clear();
                  this.Ч.addAll(сI);
               }

               var3.ЖЫ();
               NotificationToast var22 = var3.т();
               NotificationToast var6 = var3.method_6_29299991();
               float var7 = var22 == null ? 0.0F : var22.Щъ();
               if (var6 != null && var6.method__().getValue() > ЯЮС) {
                  var7 = Math.max(var7, var6.Щъ());
               }

               for (NotificationToast var9 : this.Ч) {
                  var7 = Math.max(var7, var9.Щъ());
               }

               int var23 = this.Ч.size() + (var22 == null ? 0 : 1);
               var3.setWidth(var7);
               var3.setHeight(var23 == 0 ? 0.0F : var23 * ЯЮз - ЯЮц);
               if (var23 != 0) {
                  float var24 = HudScaleCoordinates.getHudScale();
                  float var10 = var3.getY();
                  float var11 = 0.0F;
                  UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());

                  try {
                     if (var22 != null) {
                        if (var6 != null && var6.method__().getValue() > ЯЮя) {
                           this.р(var6, var10, var24, var11);
                        }

                        this.р(var22, var10, var24, var11);
                        var11 += ЯЧс;
                     }

                     for (NotificationToast var13 : this.Ч) {
                        this.р(var13, var10, var24, var11);
                        var11 += ЯЧВ;
                     }
                  } finally {
                     UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
                     PanelBackgroundRenderer.Ен();
                  }
               }
            } else {
               synchronized (сI) {
                  сI.clear();
               }

               var3.setWidth(0.0F);
               var3.setHeight(0.0F);
               var3.Б5();
            }
         }
      }
   }

   private void р(NotificationToast var1, float var2, float var3, float var4) {
      float var5 = (MINECRAFT_CLIENT.getWindow().getScreenWidth() - var1.Щъ() * var3) * ЯЧЯ;
      UiDrawing.pushGuiScaleTransform(var5, var2);

      try {
         var1.с(var5, var2, var4);
      } finally {
         UiDrawing.popGuiScaleTransform();
      }
   }

   public static void р(Component var0, ItemStack var1, int var2, NotificationCategory var3) {
      if (р(var3)) {
         р(new StatusNotificationToast("", var0, var2, var1, ЯЮх), var3);
      }
   }

   public static void р(String var0, String var1, int var2) {
      р(new StatusNotificationToast(var0, var1, var2));
   }

   private static void р(NotificationToast var0, NotificationCategory var1) {
      if (р(var1)) {
         var0.Ж(var1);
         synchronized (сI) {
            сI.add(0, var0);

            for (int var3 = 10; var3 < сI.size(); var3++) {
               сI.get(var3).ИК();
            }
         }
      }
   }

   public static boolean А衣() {
      return !сI.isEmpty();
   }

   public static void р(String var0, Component var1, String var2, int var3, ItemStack var4) {
      р(new StatusNotificationToast(var0, var1, var3, var4));
   }

   private void Эи(PlayerTickEvent var1) {
      if (UnhookState.ые()) {
         this.ес = false;
         this.В.уО();
      } else if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         Hud var2 = RoseClient.getInstance().getServices().getModules().getHud();
         if (var2.isEnabled() && var2.я().isSelected(ЯЮУ)) {
            this.В.弟(MINECRAFT_CLIENT.player, р(NotificationCategory.ARMOR), р(NotificationCategory.EFFECTS));
            if (!р(NotificationCategory.TNT)) {
               this.ес = false;
            } else {
               PrimedTnt var3 = null;
               double var4 = ЯЮЗ;

               for (PrimedTnt var7 : MINECRAFT_CLIENT.level.getEntitiesOfClass(PrimedTnt.class, MINECRAFT_CLIENT.player.getBoundingBox().inflate(ЯЮЕ), var0 -> true)) {
                  double var8 = var7.distanceTo(MINECRAFT_CLIENT.player);
                  if (!(var8 > ЯЮП) && !(var8 >= var4)) {
                     var3 = var7;
                     var4 = var8;
                  }
               }

               if (var3 == null) {
                  this.ес = false;
               } else {
                  if (!this.ес) {
                     this.ес = true;
                     String var10 = String.format(Locale.ROOT, ЯЮ西, var4, Math.max(0, var3.getFuse()) / ЯЮд);
                     р(Component.literal(var10), new ItemStack(Items.TNT), ColorMath.packRgb(255, 55, 55), NotificationCategory.TNT);
                  }
               }
            }
         } else {
            this.ес = false;
            this.В.уО();
         }
      } else {
         this.ес = false;
         this.В.уО();
      }
   }

   public static void р(String var0, Component var1, String var2, int var3) {
      р(var0, var1, var3);
   }
}
