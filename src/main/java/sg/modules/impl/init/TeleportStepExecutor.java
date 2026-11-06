package sg.modules.impl.init;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

final class TeleportStepExecutor {
   private static final long field_2 = 200000000L;
   private static final long ф = 10L;
   private static final double ь = 0.001;
   private final Minecraft Я;
   private boolean ч;
   private boolean Э;
   private long и;
   private int щ;
   private Vec3 Х;
   private Runnable А;
   private static final double сОю = 0.5;
   private static final double сОЩ = 0.5;
   private static final long сОу = 10L;
   private static final long сО2 = 200000000L;
   private static final double сОф = 0.001;
   private static final double сОи = 0.001;
   private static final double сО9 = 0.001;

   boolean 弟() {
      return !this.Э || System.nanoTime() - this.и >= сО2;
   }

   private void з(int var1) {
      if (this.ч && var1 == this.щ) {
         this.ч = false;
         Runnable var2 = this.А;
         this.А = null;
         if (this.Щ() && var2 != null) {
            var2.run();
         }
      }
   }

   boolean З(double var1, double var3, double var5, Runnable var7) {
      if (this.Я.player != null && !this.ч && this.弟()) {
         double var8 = Math.floor(var1) + сОю;
         double var10 = Math.floor(var5) + сОЩ;
         this.Х = new Vec3(var8, var3, var10);
         this.А = var7;
         this.ч = true;
         int var12 = ++this.щ;
         this.Я.player.setPos(var8, var3, var10);
         this.Э = true;
         this.и = System.nanoTime();
         CompletableFuture.delayedExecutor(сОу, TimeUnit.MILLISECONDS).execute(() -> this.Я.execute(() -> this.з(var12)));
         return true;
      } else {
         return false;
      }
   }

   boolean Щ() {
      return this.Я.player != null && this.Х != null
         ? Math.abs(this.Я.player.getX() - this.Х.x) <= сОф
            && Math.abs(this.Я.player.getY() - this.Х.y) <= сОи
            && Math.abs(this.Я.player.getZ() - this.Х.z) <= сО9
         : false;
   }

   void ДO() {
      this.Д衣();
      this.Х = null;
      this.Э = false;
      this.и = 0L;
   }

   boolean ф() {
      return this.ч;
   }

   TeleportStepExecutor(Minecraft var1) {
      this.Я = var1;
   }

   void Д衣() {
      this.ч = false;
      this.А = null;
      this.щ++;
   }
}
