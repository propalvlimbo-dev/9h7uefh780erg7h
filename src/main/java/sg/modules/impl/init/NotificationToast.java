package sg.modules.impl.init;

import lombok.Generated;
import net.minecraft.network.chat.Component;
import sg.util.time.Stopwatch;
import sg.animation.FloatAnimation;

public abstract class NotificationToast {
   protected final Component е;
   protected final long Хя;
   protected final Stopwatch у = new Stopwatch();
   protected final FloatAnimation сй;
   protected final FloatAnimation сЖ;
   protected boolean Аб;
   protected boolean АЫ;
   protected NotificationCategory Я;
   private static final long ХЧл = 1500L;
   private static final float ХЧэ = 10.0F;
   private static final float ХЧю = 7.0F;

   @Generated
   public Component Ж_f022d65c() {
      return this.е;
   }

   @Generated
   public FloatAnimation х() {
      return this.сй;
   }

   @Generated
   public Stopwatch Ж_fed7dec4() {
      return this.у;
   }

   public abstract float Щр();

   public void ИК() {
      this.Аб = true;
   }

   protected void 衣(float var1) {
      if (!this.АЫ) {
         this.сй.setImmediately(var1);
         this.АЫ = true;
      }
   }

   public NotificationToast(Component var1) {
      this(var1, ХЧл);
   }

   public abstract void с(float var1, float var2, float var3);

   @Generated
   public NotificationCategory Ж_791d2e21() {
      return this.Я;
   }

   @Generated
   public FloatAnimation method__() {
      return this.сЖ;
   }

   public boolean ЖН() {
      return this.Аб || this.у.method_0(this.Хя);
   }

   @Generated
   public boolean Жг() {
      return this.АЫ;
   }

   public abstract float Щъ();

   @Generated
   public long ъ() {
      return this.Хя;
   }

   public void Й(float var1, float var2) {
      this.с(var1, var2, 0.0F);
   }

   @Generated
   public void Ж(NotificationCategory var1) {
      this.Я = var1;
   }

   public NotificationToast(Component var1, long var2) {
      this.сй = new FloatAnimation(0.0F, ХЧэ);
      this.сЖ = new FloatAnimation(0.0F, ХЧю);
      this.Аб = false;
      this.АЫ = false;
      this.Я = NotificationCategory.OTHER;
      this.е = var1;
      this.Хя = Math.max(1L, var2);
   }

   @Generated
   public boolean ЖО() {
      return this.Аб;
   }
}
