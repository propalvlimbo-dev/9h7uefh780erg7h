package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import net.minecraft.client.CameraType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.events.impl.CameraPositionEvent;
import sg.events.impl.CameraRotationEvent;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.settings.FloatSetting;
import sg.events.impl.PlayerTurnEvent;

public class FreeCam extends ClientModule {
   private static final long сН = 380000000L;
   private final FloatSetting О;
   private final FloatSetting Ф;
   private Vec3 诶;
   @SerializedName("1")
   private Vec3 field_1;
   @SerializedName("В")
   private Vec2 В_d1fff921;
   @SerializedName("В")
   private CameraType В_c1e99c6a;
   private Vec3 Ь;
   private float ЯЧ;
   private float ЯЛ;
   private long сн;
   private boolean сэ;
   private static float Я诶;
   private static float Я1;
   private final EventListener<CameraPositionEvent> сЯ;
   private final EventListener<PlayerTickEvent> сХ;
   private final EventListener<PlayerMovementSyncEvent> се;
   private final EventListener<CameraRotationEvent> сА;
   private final EventListener<PlayerTurnEvent> с4;
   private static final String сOЯ = "FreeCam";
   private static final String сOХ = "Свободная камера";
   private static final String сOе = "Скорость XZ";
   private static final float сOА = 0.5F;
   private static final float сO4 = 0.1F;
   private static final float сOп = 1.5F;
   private static final float сOД = 0.05F;
   private static final String сOЪ = "Скорость Y";
   private static final float сOт = 0.5F;
   private static final float сOа = 0.1F;
   private static final float сOЮ = 1.5F;
   private static final float сOЧ = 0.05F;
   private static final float сOЛ = 0.15F;
   private static final float сO诶 = 0.15F;
   private static final float сO1 = -90.0F;
   private static final float сOЬ = 90.0F;
   private static final float сO衣 = 3.8E8F;
   private static final float сOш = -45.0F;
   private static final float сOИ = 45.0F;
   private static final float сOь = 45.0F;
   private static final float сOж = -45.0F;
   private static final double сO弟 = -1.0;
   private static final float сOб = 90.0F;
   private static final float сOЫ = 90.0F;
   private static final float сOl = 90.0F;
   private static final float сO8 = 90.0F;

   @Generated
   public static float ИИ() {
      return Я诶;
   }

   private float Ы() {
      return Mth.clamp((float)(System.nanoTime() - this.сн) / сO衣, 0.0F, 1.0F);
   }

   public void б(CameraRotationEvent var1) {
      var1.setYaw(Я诶);
      var1.setPitch(Я1);
   }

   private void б(PlayerTurnEvent var1) {
      Я诶 = Я诶 + var1.getYawDelta() * сOЛ;
      Я1 = Math.clamp(Я1 + var1.getPitchDelta() * сO诶, сO1, сOЬ);
      var1.setCancelled(true);
   }

   private void э(CameraPositionEvent var1) {
      if (this.诶 != null && this.field_1 != null) {
         Vec3 var2 = this.field_1.lerp(this.诶, var1.getTickDelta());
         var1.setPosition(var2);
      }
   }

   @Generated
   public static void Ч(float var0) {
      Я1 = var0;
   }

   @Generated
   public static float Иэ() {
      return Я1;
   }

   private float Б(float var1) {
      float var2 = 1.0F - var1;
      return 1.0F - var2 * var2 * var2;
   }

   public Vec2 э(float var1, float var2) {
      if (!this.сэ) {
         return new Vec2(var1, var2);
      }

      float var3 = this.Б(this.Ы());
      float var4 = Mth.wrapDegrees(var1 - this.ЯЧ);
      return new Vec2(this.ЯЧ + var4 * var3, Mth.lerp(var3, this.ЯЛ, var2));
   }

   @Generated
   public static void Я(float var0) {
      Я诶 = var0;
   }

   private void и(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.诶 == null) {
            Vec3 var2 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
            this.诶 = var2;
            this.field_1 = var2;
         }

         if (MINECRAFT_CLIENT.options.getCameraType() != CameraType.FIRST_PERSON) {
            MINECRAFT_CLIENT.options.setCameraType(CameraType.FIRST_PERSON);
         }

         this.field_1 = this.诶;
         float var13 = this.О.getValue();
         float var3 = this.Ф.getValue();
         float var4 = Я诶;
         double var5 = 0.0;
         double var7 = 0.0;
         if (MINECRAFT_CLIENT.options.keyUp.isDown()) {
            var5++;
         }

         if (MINECRAFT_CLIENT.options.keyDown.isDown()) {
            var5--;
         }

         if (MINECRAFT_CLIENT.options.keyLeft.isDown()) {
            var7++;
         }

         if (MINECRAFT_CLIENT.options.keyRight.isDown()) {
            var7--;
         }

         double var9 = 0.0;
         double var11 = 0.0;
         if (var5 != 0.0 || var7 != 0.0) {
            if (var5 != 0.0) {
               if (var7 > 0.0) {
                  var4 += var5 > 0.0 ? сOш : сOИ;
               } else if (var7 < 0.0) {
                  var4 += var5 > 0.0 ? сOь : сOж;
               }

               var7 = 0.0;
               if (var5 > 0.0) {
                  var5 = 1.0;
               } else {
                  var5 = сO弟;
               }
            }

            var9 = var5 * var13 * Mth.cos((float)Math.toRadians(var4 + сOб)) + var7 * var13 * Mth.sin(Math.toRadians(var4 + сOЫ));
            var11 = var5 * var13 * Mth.sin((float)Math.toRadians(var4 + сOl)) - var7 * var13 * Mth.cos(Math.toRadians(var4 + сO8));
         }

         this.诶 = this.诶.add(var9, 0.0, var11);
         if (MINECRAFT_CLIENT.options.keyJump.isDown()) {
            this.诶 = this.诶.add(0.0, var3, 0.0);
         }

         if (MINECRAFT_CLIENT.options.keyShift.isDown()) {
            this.诶 = this.诶.add(0.0, -var3, 0.0);
         }
      }
   }

   private void э诶() {
      this.сэ = false;
      this.Ь = null;
      this.ЯЧ = 0.0F;
      this.ЯЛ = 0.0F;
      this.сн = 0L;
      this.诶 = null;
      this.field_1 = null;
      if (this.В_c1e99c6a != null) {
         MINECRAFT_CLIENT.options.setCameraType(this.В_c1e99c6a);
         this.В_c1e99c6a = null;
      }
   }

   public Vec3 э(Vec3 var1) {
      if (this.сэ && this.Ь != null) {
         float var2 = this.Ы();
         float var3 = this.Б(var2);
         Vec3 var4 = this.Ь.lerp(var1, var3);
         if (var2 >= 1.0F) {
            this.э诶();
            return var1;
         } else {
            return var4;
         }
      } else {
         return var1;
      }
   }

   @Override
   public void onEnable() {
      this.сэ = false;
      this.Ь = null;
      this.сн = 0L;
      this.В_c1e99c6a = MINECRAFT_CLIENT.options.getCameraType();
      if (MINECRAFT_CLIENT.player != null) {
         this.В_d1fff921 = new Vec2(MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot());
         Я诶 = MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot();
         Я1 = MINECRAFT_CLIENT.gameRenderer.mainCamera().xRot();
      }
   }

   @Override
   public void onDisable() {
      if (this.В_d1fff921 != null) {
         MINECRAFT_CLIENT.player.setYRot(this.В_d1fff921.x);
         MINECRAFT_CLIENT.player.setXRot(this.В_d1fff921.y);
      }

      if (this.诶 != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         this.сэ = true;
         this.Ь = this.诶;
         this.ЯЧ = Я诶;
         this.ЯЛ = Я1;
         this.сн = System.nanoTime();
      } else {
         this.э诶();
      }

      this.В_d1fff921 = null;
   }

   public FreeCam() {
      super(сOЯ, сOХ, ModuleCategory.MISC);
      this.О = new FloatSetting(сOе, сOА, сO4, сOп).withStep(сOД);
      this.Ф = new FloatSetting(сOЪ, сOт, сOа, сOЮ).withStep(сOЧ);
      this.сЯ = this::э;
      this.сХ = this::и;
      this.се = this::弟;
      this.сА = this::б;
      this.с4 = this::б;
      this.addSettings(this.О, this.Ф);
   }

   private void 弟(PlayerMovementSyncEvent var1) {
      if (this.В_d1fff921 == null) {
         this.В_d1fff921 = new Vec2(var1.getYaw(), var1.getPitch());
      }

      MINECRAFT_CLIENT.player.setYRot(this.В_d1fff921.x);
      MINECRAFT_CLIENT.player.setXRot(this.В_d1fff921.y);
      var1.setCancelled(true);
   }
}
