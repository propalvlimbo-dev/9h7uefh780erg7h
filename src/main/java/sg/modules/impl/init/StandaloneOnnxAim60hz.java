package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sg.ec.MinecraftAccess;
import sg.ec.Aim60Schema;
import sg.render.pipeline.OnnxAimPredictor;
import sg.util.time.SampleRateGate;
import sg.client.RoseClient;

public final class StandaloneOnnxAim60hz implements MinecraftAccess {
   private static final Logger В_9c0cd60f = LoggerFactory.getLogger(StandaloneOnnxAim60hz.ХЫЖ);
   private static StandaloneOnnxAim60hz Я;
   @SerializedName("В")
   private final FixedRateScheduler В_91944f82 = new FixedRateScheduler();
   @SerializedName("В")
   private final SampleRateGate В_b8eb1c57 = new SampleRateGate();
   @SerializedName("В")
   private StandaloneAimConfig В_69ea131a;
   @SerializedName("В")
   private OnnxAimPredictor В_188c5bad;
   private ClientLevel т;
   private boolean Ва;
   private boolean ВЮ;
   private boolean ВЧ;
   private boolean ВЛ;
   private static final String ХЫы = "ONNX Aim initialized: rate={} Hz, sequence={}, horizon={}, features={}";
   private static final String ХЫй = "выключен";
   private static final String ХЫЖ = "standalone-onnx-aim-60hz";

   private void п(float var1) {
      if (this.Ва && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gui.screen() == null && MINECRAFT_CLIENT.isWindowActive() && MINECRAFT_CLIENT.mouseHandler.isMouseGrabbed()) {
         long var2 = System.nanoTime();
         if (RoseClient.getInstance().getServices().getModules().getAura().Ж_95bedd6e() instanceof AbstractClientPlayer var4) {
            this.В_188c5bad.С(var4, var1, var2);
         } else {
            this.В_188c5bad.Бь();
         }
      } else {
         this.Йъ();
      }
   }

   private void Йц() {
      boolean var1 = InputConstants.isKeyDown(MINECRAFT_CLIENT.getWindow(), 295);
      boolean var2 = InputConstants.isKeyDown(MINECRAFT_CLIENT.getWindow(), 298);
      if (var1 && !this.ВЮ) {
         this.ЙТ();
      }

      if (var2 && !this.ВЧ) {
         this.ЙА();
      }

      this.ВЮ = var1;
      this.ВЧ = var2;
   }

   private void Х(String var1) {
      if (MINECRAFT_CLIENT.player != null) {
         MINECRAFT_CLIENT.player.sendOverlayMessage(Component.literal("[ONNX Aim] " + var1));
      }
   }

   public static void ж(float var0) {
      StandaloneOnnxAim60hz var1 = Я;
      if (var1 != null) {
         var1.п(var0);
      }
   }

   public void Й诶() {
      this.Ва = false;
      if (this.В_188c5bad != null) {
         this.В_188c5bad.close();
      }

      if (Я == this) {
         Я = null;
      }
   }

   public void Йд() {
      Я = this;
      this.Е(false);
      В_9c0cd60f.info(ХЫы, 60, 60, 15, Aim60Schema.Хп);
   }

   private void ЙА() {
      boolean var1 = this.Ва;
      this.Ва = false;
      this.Е(var1);
      this.Х(this.В_188c5bad.诶п() ? "модель перезагружена; model=" + this.В_188c5bad.Ъ7() : "ошибка загрузки: " + this.В_188c5bad.Ъг());
   }

   public void ЙС() {
      this.Йц();
      if (MINECRAFT_CLIENT.level != this.т) {
         this.т = MINECRAFT_CLIENT.level;
         this.Йъ();
      }
   }

   private void Е(boolean var1) {
      if (this.В_188c5bad != null) {
         this.В_188c5bad.close();
      }

      this.В_69ea131a = StandaloneAimConfig.Ь(В_9c0cd60f);
      this.В_188c5bad = new OnnxAimPredictor(this.В_69ea131a, В_9c0cd60f);
      boolean var2 = this.В_188c5bad.诶к();
      this.Ва = var2 && (var1 || this.В_69ea131a.е诶);
      this.Йъ();
   }

   private void ЙТ() {
      if (this.Ва) {
         this.Ва = false;
         this.Йъ();
         this.Х(ХЫй);
      } else if (!this.В_188c5bad.诶п() && !this.В_188c5bad.诶к()) {
         this.Х("модель не загружена: " + this.В_188c5bad.Ъг());
      } else {
         this.Ва = true;
         this.Йъ();
         this.Х("включён; model=" + this.В_188c5bad.Ъ7() + "; rate=60 Hz");
      }
   }

   private void Йъ() {
      this.В_91944f82.ы5();
      this.В_b8eb1c57.Кр();
      this.ВЛ = false;
      if (this.В_188c5bad != null) {
         this.В_188c5bad.Бь();
      }
   }
}
