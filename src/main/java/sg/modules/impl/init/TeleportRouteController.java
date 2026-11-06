package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Pose;
import sg.network.ClientChat;
import sg.client.RoseClient;

public final class TeleportRouteController {
   private static final int Вз = 100;
   private static final Runnable п = () -> {};
   private final Minecraft е;
   @SerializedName("В")
   private final TeleportSurfacePlanner В_12ffa04e;
   @SerializedName("В")
   private final TeleportStepExecutor В_aedf66ed;
   @SerializedName("В")
   private TeleportRouteController.RouteContext В_a874a5f0;
   private TeleportRouteController.RouteStage Я;
   private static final String 诶н = "TP RW: сейчас маршрут недоступен";
   private static final String 诶Ц = "мир или состояние игрока изменились";
   private static final String 诶ч = "TP RW: маршрут остановлен";
   private static final String 诶Э = "TP RW: активного маршрута нет";
   private static final String 诶必 = "над следующей колонкой нет твёрдой поверхности";
   private static final String 诶щ = "следующий участок не загрузился или на нём нет поверхности";
   private static final String 诶Г = "целевой чанк не загрузился";
   private static final String 诶л = "над целевой колонкой нет твёрдой поверхности";
   private static final String 诶э = "TP RW: сухого выхода нет, выхожу под водой";

   public void ФТ() {
      boolean var1 = this.В_a874a5f0 != null || this.В_aedf66ed.ф();
      this.ФР();
      ClientChat.З(var1 ? 诶ч : 诶Э);
   }

   private void х(double var1, double var3, double var5, Runnable var7) {
      TeleportRouteController.RouteContext var8 = this.В_a874a5f0;
      TeleportRouteController.RouteStage var9 = var8.Х();
      boolean var10 = this.В_aedf66ed.З(var1, var3, var5, () -> {
         if (this.В_a874a5f0 == var8) {
            var7.run();
         }
      });
      if (var10) {
         this.Я = var9;
      }
   }

   private boolean хЬ() {
      return this.е.player != null
         && this.е.level != null
         && this.е.level == this.В_a874a5f0.я()
         && this.е.getConnection() != null
         && this.е.player.isAlive()
         && !this.е.player.isPassenger()
         && this.е.player.getPose() == Pose.STANDING
         && !this.х衣();
   }

   public void т(int var1, int var2) {
      if (!this.хO()) {
         ClientChat.З(诶н);
      } else {
         this.ФР();
         this.В_a874a5f0 = new TeleportRouteController.RouteContext(this.е.level, var1, var2);
         ClientChat.З("TP RW: маршрут к X=" + var1 + " Z=" + var2);
      }
   }

   private void у(String var1) {
      this.ФР();
      ClientChat.З("TP RW остановлен: " + var1);
   }

   public void ФР() {
      this.В_a874a5f0 = null;
      this.Я = null;
      this.В_aedf66ed.ДO();
   }

   private void Фд() {
      if (!this.В_12ffa04e.Ю_bbeebd87(this.В_a874a5f0.я(), this.В_a874a5f0.а(), this.В_a874a5f0.method_1())) {
         if (this.В_a874a5f0.method__() >= 100) {
            this.у(诶Г);
         }
      } else {
         this.В_a874a5f0.яЙ();
         TeleportSurfacePlanner.Surface var1 = this.В_12ffa04e.Ю_6a0a43d3(this.В_a874a5f0.я(), this.В_a874a5f0.а(), this.В_a874a5f0.method_1());
         if (var1 == null) {
            this.у(诶л);
         } else {
            this.В_a874a5f0.ш(var1.y());
            if (var1.underwater()) {
               ClientChat.З(诶э);
            }

            this.В_a874a5f0.Х(TeleportRouteController.RouteStage.ASCEND);
         }
      }
   }

   private void Ф4() {
      if (this.В_aedf66ed.弟()) {
         if (!this.В_aedf66ed.Щ()) {
            this.В_a874a5f0.Х(TeleportRouteController.RouteStage.ASCEND);
         } else {
            int var1 = this.е.player.getBlockX();
            int var2 = this.е.player.getBlockY();
            int var3 = this.е.player.getBlockZ();
            this.ФР();
            ClientChat.З("TP RW: готово, X=" + var1 + " Y=" + var2 + " Z=" + var3);
         }
      }
   }

   private boolean хO() {
      return this.е.player != null
         && this.е.level != null
         && this.е.getConnection() != null
         && this.е.player.isAlive()
         && !this.е.player.isPassenger()
         && this.е.player.getPose() == Pose.STANDING
         && !this.х衣();
   }

   private void ФЙ() {
      if (this.В_12ffa04e.Ю(this.е.player.getX(), this.е.player.getZ(), this.В_a874a5f0.а(), this.В_a874a5f0.method_1())) {
         this.В_a874a5f0.Х(TeleportRouteController.RouteStage.WAIT_TARGET_CHUNK);
      } else {
         TeleportSurfacePlanner.Step var1 = this.В_12ffa04e
            .Ю(this.В_a874a5f0.я(), this.е.player, this.е.player.getX(), this.е.player.getZ(), this.В_a874a5f0.а(), this.В_a874a5f0.method_1());
         if (var1 == null) {
            if (this.В_a874a5f0.method__() >= 100) {
               this.у(诶щ);
            }
         } else {
            this.В_a874a5f0.яЙ();
            this.х(var1.x(), var1.surface().y(), var1.z(), п);
         }
      }
   }

   private void ФЛ() {
      this.х(
         this.В_12ffa04e.Ю(this.В_a874a5f0.а()),
         this.В_a874a5f0.А(),
         this.В_12ffa04e.Ю(this.В_a874a5f0.method_1()),
         () -> this.В_a874a5f0.Х(TeleportRouteController.RouteStage.FINAL_CONFIRM)
      );
   }

   public void Ф9() {
      if (this.В_a874a5f0 != null) {
         if (!this.хЬ()) {
            this.у(诶Ц);
         } else if (!this.В_aedf66ed.ф()) {
            switch (this.В_a874a5f0.Х()) {
               case ALIGN_SURFACE:
                  this.Фщ();
                  break;
               case MOVE_HORIZONTAL:
                  this.ФЙ();
                  break;
               case WAIT_TARGET_CHUNK:
                  this.Фд();
                  break;
               case ASCEND:
                  this.ФЛ();
                  break;
               case FINAL_CONFIRM:
                  this.Ф4();
            }
         }
      }
   }

   public TeleportRouteController(Minecraft var1) {
      this.е = var1;
      this.В_12ffa04e = new TeleportSurfacePlanner();
      this.В_aedf66ed = new TeleportStepExecutor(var1);
   }

   private void Фщ() {
      TeleportSurfacePlanner.Surface var1 = this.В_12ffa04e
         .Ю_6a0a43d3(this.В_a874a5f0.я(), (int)Math.floor(this.е.player.getX()), (int)Math.floor(this.е.player.getZ()));
      if (var1 == null) {
         this.у(诶必);
      } else {
         this.х(this.е.player.getX(), var1.y(), this.е.player.getZ(), () -> this.В_a874a5f0.Х(TeleportRouteController.RouteStage.MOVE_HORIZONTAL));
      }
   }

   private boolean х衣() {
      return RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled();
   }

   public void Фо() {
      if (this.В_a874a5f0 != null) {
         this.В_aedf66ed.Д衣();
         if (this.Я != null) {
            this.В_a874a5f0.Х(this.Я);
         }
      }
   }

   private enum RouteStage {
      ALIGN_SURFACE,
      MOVE_HORIZONTAL,
      WAIT_TARGET_CHUNK,
      ASCEND,
      FINAL_CONFIRM;
   }

   private static final class RouteContext {
      private final ClientLevel п;
      private final int ВI;
      private final int ВТ;
      private TeleportRouteController.RouteStage В = TeleportRouteController.RouteStage.ALIGN_SURFACE;
      private int ВО;
      private int ВФ;

      private int а() {
         return this.ВI;
      }

      private RouteContext(ClientLevel var1, int var2, int var3) {
         this.п = var1;
         this.ВI = var2;
         this.ВТ = var3;
      }

      private ClientLevel я() {
         return this.п;
      }

      private int method__() {
         return ++this.ВФ;
      }

      private TeleportRouteController.RouteStage Х() {
         return this.В;
      }

      private void яЙ() {
         this.ВФ = 0;
      }

      private int method_1() {
         return this.ВТ;
      }

      private int А() {
         return this.ВО;
      }

      private void ш(int var1) {
         this.ВО = var1;
      }

      private void Х(TeleportRouteController.RouteStage var1) {
         this.В = var1;
      }
   }
}
