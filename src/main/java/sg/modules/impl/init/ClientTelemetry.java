package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import net.minecraft.world.entity.Entity;
import sg.ec.MinecraftAccess;
import sg.ec.ClientService;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.events.impl.PacketSendEvent;
import sg.util.time.ClientMetrics;
import sg.events.impl.HudRenderEvent;

public final class ClientTelemetry extends ClientService implements MinecraftAccess {
   private static final int с5 = 120;
   private static final ClientTelemetry Я_4109462f = new ClientTelemetry();
   @SerializedName("Я")
   private final float[] Я_06b4556e = new float[120];
   private int св;
   private int съ;
   private long З;
   private long Е;
   private float сЗ;
   private int сМ;
   private int сН;
   private int сн;
   private int сЦ;
   private long П;
   @SerializedName("2")
   private final EventListener<HudRenderEvent> field_2 = new EventListener<HudRenderEvent>() {
      @Override
      public int getPriority() {
         return 10000;
      }

      public void onEvent(HudRenderEvent var1) {
         ClientTelemetry.this.Е = System.nanoTime();
         ClientTelemetry.this.х3();
         ClientTelemetry.this.их();
      }
   };
   private final EventListener<HudRenderEvent> ф = new EventListener<HudRenderEvent>() {
      private static final float Яая = 1000000.0F;

      @Override
      public int getPriority() {
         return -10000;
      }

      public void onEvent(HudRenderEvent var1) {
         if (ClientTelemetry.this.Е != 0L) {
            ClientTelemetry.this.сЗ = (float)(System.nanoTime() - ClientTelemetry.this.Е) / Яая;
         }
      }
   };
   private final EventListener<PacketReceiveEvent> и = var1 -> this.сМ++;
   @SerializedName("9")
   private final EventListener<PacketSendEvent> field_9 = var1 -> this.сН++;
   private static final float сР6 = 250.0F;
   private static final float сР_ = 1000000.0F;
   private static final long сРШ = 1000L;
   private static final float сРы = 0.5F;
   private static final float сРй = 1000.0F;
   private static final long сРЖ = 1048576L;
   private static final long сРм = 1048576L;

   public int Ж_a83dd0cc() {
      return MINECRAFT_CLIENT == null ? 0 : MINECRAFT_CLIENT.getFps();
   }

   public static ClientTelemetry и_4109462f() {
      return Я_4109462f;
   }

   private void х3() {
      long var1 = System.nanoTime();
      if (this.З != 0L) {
         float var3 = Math.min(сР6, (float)(var1 - this.З) / сР_);
         this.Я_06b4556e[this.св] = var3;
         this.св = (this.св + 1) % 120;
         this.съ = Math.min(120, this.съ + 1);
      }

      this.З = var1;
   }

   public float[] и_06b4556e() {
      float[] var1 = new float[this.съ];
      int var2 = (this.св - this.съ + 120) % 120;

      for (int var3 = 0; var3 < this.съ; var3++) {
         var1[var3] = this.Я_06b4556e[(var2 + var3) % 120];
      }

      return var1;
   }

   public int З_a83dd0cc() {
      return MINECRAFT_CLIENT != null && MINECRAFT_CLIENT.player != null ? ClientMetrics.getPlayerLatencyMillis(MINECRAFT_CLIENT.player) : 0;
   }

   public float З_f67ab10a() {
      if (this.съ == 0) {
         return 0.0F;
      }

      float[] var1 = this.и_06b4556e();
      Arrays.sort(var1);
      int var2 = Math.max(0, var1.length - 1 - Math.max(1, var1.length / 100));
      float var3 = Math.max(сРы, var1[var2]);
      return сРй / var3;
   }

   public long щ() {
      return Runtime.getRuntime().maxMemory() / сРм;
   }

   public long method_6_6da43b94() {
      Runtime var1 = Runtime.getRuntime();
      return (var1.totalMemory() - var1.freeMemory()) / сРЖ;
   }

   public int з_a83dd0cc() {
      if (MINECRAFT_CLIENT != null && MINECRAFT_CLIENT.level != null) {
         int var1 = 0;

         for (Entity var3 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
            var1++;
         }

         return var1;
      } else {
         return 0;
      }
   }

   public int р() {
      return this.сЦ;
   }

   private ClientTelemetry() {
   }

   private void их() {
      long var1 = System.currentTimeMillis();
      if (this.П == 0L) {
         this.П = var1;
      }

      if (var1 - this.П >= сРШ) {
         this.сн = this.сМ;
         this.сЦ = this.сН;
         this.сМ = 0;
         this.сН = 0;
         this.П = var1;
      }
   }

   public float Ж_f67ab10a() {
      if (this.съ == 0) {
         return 0.0F;
      }

      float var1 = 0.0F;

      for (int var2 = 0; var2 < this.съ; var2++) {
         var1 += this.Я_06b4556e[var2];
      }

      return var1 / this.съ;
   }

   public float э_f67ab10a() {
      return this.сЗ;
   }

   public int э_a83dd0cc() {
      return this.сн;
   }
}
