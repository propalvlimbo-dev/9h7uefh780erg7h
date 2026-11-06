package sg.hud;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import sg.events.EventListener;
import sg.hud.HudScaleCoordinates;
import sg.ec.ClientFonts;
import sg.client.RoseClient;
import sg.modules.impl.misc.MusicPlayer;
import sg.hud.HudElement;
import sg.events.impl.HudRenderEvent;
import sg.hud.InteractiveHudElement;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.render.avatar.WindowsMediaSession;

public class MusicPlayerHud extends HudElement implements InteractiveHudElement {
   private static final float field_4Ж = 148.0F;
   private static final float field_4м = 36.0F;
   private static final float field_4К = 268.0F;
   private static final float field_4I = 148.0F;
   public static final float field_4Т = 10.0F;
   private static final float field_4О = 14.0F;
   private static final float field_4Ф = 52.0F;
   private static final float field_4Й = 22.0F;
   private static final float field_4к = 10.0F;
   private static final float field_4г = 3.0F;
   private static final float field_40 = 54.0F;
   private static final float field_4х = 18.0F;
   private static final String ВЦ = "4";
   private static final String Вч = "5";
   private static final String ВЭ = "6";
   private static final String В必 = "7";
   private static final String Вщ = "!";
   private static final String ВГ = "\"";
   private static final String Вл = "#";
   private static final float field_4У = 14.0F;
   private static final float field_4З = 11.0F;
   private static final float field_4Е = 0.82F;
   private static final long Яш = 260L;
   private final EventListener<HudRenderEvent> Яе = this::л;
   @SerializedName("4П")
   private float field_4П;
   @SerializedName("4西")
   private float field_4西;
   @SerializedName("4д")
   private float field_4д;
   private String Вэ = "";
   private long ЯИ;
   private long Яь;
   private boolean Ях;
   @SerializedName("4Б")
   private float field_4Б = Float.NaN;
   @SerializedName("4С")
   private float field_4С = Float.NaN;
   private MusicPlayerHud.DragMode В = MusicPlayerHud.DragMode.NONE;
   @SerializedName("4з")
   private float field_4з;
   private static final String ВВР = "MusicPlayer";
   private static final float ВВ3 = 148.0F;
   private static final float ВВр = 36.0F;
   private static final float ВВ6 = 0.5F;
   private static final float ВВ_ = 0.5F;
   private static final float ВВШ = 0.05F;
   private static final float ВВы = 0.5F;
   private static final float ВВй = 268.0F;
   private static final float ВВЖ = 36.0F;
   private static final float ВВм = 148.0F;
   private static final float ВВК = 0.5F;
   private static final float ВВI = 0.015F;
   private static final float ВВТ = 0.016F;
   private static final float ВВО = 1.0E9F;
   private static final float ВВФ = 0.001F;
   private static final float ВВЙ = 0.05F;
   private static final long ВВк = 260L;
   private static final float ВВг = 14.0F;
   private static final float ВВ0 = 11.0F;
   private static final float ВВх = 0.82F;
   private static final float ВВУ = 8.0E-4F;
   private static final float ВВЗ = 8.0E-4F;
   private static final float ВВЕ = 14.0F;
   private static final float ВВП = 22.0F;
   private static final String ВВ西 = "Ничего не играет";
   private static final float ВВд = 13.0F;
   private static final float ВВБ = 36.0F;
   private static final float ВВС = 54.0F;
   private static final float ВВз = 88.0F;
   private static final float ВВц = 148.0F;
   private static final float ВВя = 0.02F;
   private static final float ВЯс = 0.55F;
   private static final float ВЯВ = 0.5F;
   private static final float ВЯЯ = 6.0F;
   private static final String ВЯХ = "Ничего не играет";
   private static final float ВЯе = 42.0F;
   private static final float ВЯА = 28.0F;
   private static final float ВЯ4 = 8.0F;
   private static final float ВЯп = 8.0F;
   private static final float ВЯД = 10.0F;
   private static final float ВЯЪ = 7.0F;
   private static final float ВЯт = 8.0F;
   private static final float ВЯа = 20.0F;
   private static final float ВЯЮ = 5.0F;
   private static final float ВЯЧ = 14.0F;
   private static final float ВЯЛ = 4.0F;
   private static final float ВЯ诶 = 14.0F;
   private static final float ВЯ1 = 28.0F;
   private static final float ВЯЬ = 52.0F;
   private static final float ВЯ衣 = 12.0F;
   private static final float ВЯш = 12.0F;
   private static final float ВЯИ = 0.5F;
   private static final float ВЯь = 0.5F;
   private static final float ВЯж = 0.9F;
   private static final float ВЯ弟 = 13.0F;
   private static final float ВЯб = 19.0F;
   private static final float ВЯЫ = 38.0F;
   private static final float ВЯl = 42.0F;
   private static final float ВЯ8 = 0.12F;
   private static final float ВЯ5 = 0.85F;
   private static final float ВЯв = 0.01F;
   private static final float ВЯъ = 10.0F;
   private static final float ВЯМ = 255.0F;
   private static final float ВЯН = 14.0F;
   private static final float ВЯн = 240.0F;
   private static final float ВЯЦ = 268.0F;
   private static final float ВЯч = 14.0F;
   private static final float ВЯЭ = 54.0F;
   private static final float ВЯ必 = 54.0F;
   private static final float ВЯщ = 100.0F;
   private static final float ВЯГ = 3.0F;
   private static final float ВЯл = 6.5F;
   private static final float ВЯэ = 0.5F;
   private static final float ВЯю = 9.0F;
   private static final float ВЯЩ = 9.0F;
   private static final float ВЯу = 4.5F;
   private static final float ВЯ2 = 1.5F;
   private static final float ВЯф = 8.5F;
   private static final float ВЯи = 2.5F;
   private static final float ВЯ9 = 14.0F;
   private static final float ВЯо = 1.2F;
   private static final float ВЯO = 1.5F;
   private static final float ВЯ7 = 8.5F;
   private static final float ВЯР = 7.0F;
   private static final float ВЯ3 = 2.5F;
   private static final float ВЯр = 1.2F;
   private static final String ВЯ6 = "Ничего не играет";
   private static final float ВЯ_ = 14.0F;
   private static final float ВЯШ = 52.0F;
   private static final float ВЯы = 12.0F;
   private static final float ВЯй = 176.0F;
   private static final float ВЯЖ = 20.0F;
   private static final float ВЯм = 40.0F;
   private static final String ВЯК = "Включите музыку в браузере";
   private static final long ВЯI = 7000L;
   private static final float ВЯТ = 7000.0F;
   private static final double ВЯО = Math.PI;
   private static final double ВЯФ = 2.0;
   private static final float ВЯЙ = 0.5F;
   private static final float ВЯк = 14.0F;
   private static final float ВЯг = 72.0F;
   private static final float ВЯ0 = 240.0F;
   private static final float ВЯх = 12.0F;
   private static final float ВЯУ = 40.0F;
   private static final float ВЯЗ = 1.4F;
   private static final float ВЯЕ = 40.0F;
   private static final float ВЯП = 1.7F;
   private static final float ВЯ西 = 1.2F;
   private static final float ВЯд = 1.2F;
   private static final float ВЯБ = 0.16F;
   private static final double ВЯС = 140.0;
   private static final double ВЯз = 0.55;
   private static final double ВЯц = 0.5;
   private static final double ВЯя = 0.5;
   private static final double ВХс = 90.0;
   private static final double ВХВ = 0.31;
   private static final double ВХЯ = 0.5;
   private static final double ВХХ = 0.5;
   private static final double ВХе = 0.18;
   private static final double ВХА = 0.82;
   private static final double ВХ4 = 0.55;
   private static final double ВХп = 0.45;
   private static final double ВХД = 0.12;
   private static final float ВХЪ = 0.12F;
   private static final float ВХт = 2.4F;
   private static final float ВХа = 0.5F;
   private static final float ВХЮ = 14.0F;
   private static final float ВХЧ = 86.0F;
   private static final float ВХЛ = 240.0F;
   private static final float ВХ诶 = 0.14F;
   private static final float ВХ1 = 3.0F;
   private static final float ВХЬ = 1.5F;
   private static final float ВХ衣 = 3.0F;
   private static final float ВХш = 3.0F;
   private static final float ВХИ = 1.5F;
   private static final float ВХь = 4.0F;
   private static final float ВХж = 2.5F;
   private static final float ВХ弟 = 8.0F;
   private static final float ВХб = 8.0F;
   private static final float ВХЫ = 4.0F;
   private static final String ВХl = "live";
   private static final float ВХ8 = 10.0F;
   private static final float ВХ5 = 8.0F;
   private static final float ВХв = 8.0F;
   private static final long ВХъ = 1000L;
   private static final String ВХМ = "%d:%02d";
   private static final long ВХН = 60L;
   private static final long ВХн = 60L;
   private static final float ВХЦ = 114.0F;
   private static final double ВХч = 22.0;
   private static final double ВХЭ = 22.0;
   private static final float ВХ必 = 22.0F;
   private static final float ВХщ = 22.0F;
   private static final float ВХГ = 7.0F;
   private static final float ВХл = 0.12F;
   private static final float ВХэ = 11.0F;
   private static final float ВХю = 11.0F;
   private static final String ВХЩ = "4";
   private static final String ВХу = "6";
   private static final String ВХ2 = "7";
   private static final String ВХф = "5";
   private static final float ВХи = 268.0F;
   private static final float ВХ9 = 14.0F;
   private static final float ВХо = 54.0F;
   private static final float ВХO = 114.0F;
   private static final float ВХ7 = 9.5F;
   private static final float ВХР = 4.0F;
   private static final float ВХ3 = 7.0F;
   private static final double ВХр = 62.0;
   private static final double ВХ6 = 18.0;
   private static final float ВХ_ = 0.35F;
   private static final float ВХШ = 0.35F;
   private static final float ВХы = 3.0F;
   private static final float ВХй = 54.0F;
   private static final float ВХЖ = 100.0F;
   private static final float ВХм = 9.75F;
   private static final float ВХК = 1.5F;
   private static final float ВХI = 54.0F;
   private static final float ВХТ = 3.0F;
   private static final float ВХО = 1.5F;
   private static final float ВХФ = 0.14F;
   private static final float ВХЙ = 3.0F;
   private static final float ВХк = 1.5F;
   private static final float ВХг = 3.5F;
   private static final float ВХ0 = 7.0F;
   private static final float ВХх = 7.0F;
   private static final float ВХУ = 3.5F;
   private static final String ВХЗ = "!";
   private static final String ВХЕ = "\"";
   private static final String ВХП = "#";
   private static final float ВХ西 = 86.0F;
   private static final float ВХд = 268.0F;
   private static final float ВХБ = 0.5F;
   private static final float ВХС = 24.0F;
   private static final float ВХз = 32.0F;
   private static final float ВХц = 0.5F;
   private static final float ВХя = 0.55F;
   private static final float Вес = 114.0F;
   private static final double ВеВ = 22.0;
   private static final double ВеЯ = 22.0;
   private static final float ВеХ = 86.0F;
   private static final float Вее = 14.0F;
   private static final float ВеА = 3.0F;
   private static final float Ве4 = 7.0F;
   private static final double Веп = 246.0;
   private static final double ВеД = 18.0;
   private static final float ВеЪ = 14.0F;
   private static final float Вет = 240.0F;
   private static final float Веа = 268.0F;
   private static final float ВеЮ = 14.0F;
   private static final float ВеЧ = 54.0F;
   private static final float ВеЛ = 9.5F;
   private static final float Ве诶 = 4.0F;
   private static final float Ве1 = 7.0F;
   private static final double ВеЬ = 62.0;
   private static final double Ве衣 = 18.0;

   private String а(int var1) {
      if (var1 < 34) {
         return ВХЗ;
      } else {
         return var1 < 67 ? ВХЕ : ВХП;
      }
   }

   @Override
   public boolean ш(float var1, float var2, int var3) {
      if (this.уЙ() && var3 == 0) {
         if (!WorldProjection.containsPoint(var1, var2, this.x, this.getY(), this.getScaledWidth(), this.getScaledHeight())) {
            return false;
         }

         var1 = HudScaleCoordinates.unscaleX(var1, this.x);
         var2 = HudScaleCoordinates.unscaleY(var2, this.getY());
         if (!this.уС()) {
            return false;
         }

         MusicPlayer var4 = г_e2b88ac7();
         if (var4 == null) {
            return false;
         }

         float var5 = this.getY() + Вес;

         for (int var6 = 0; var6 < 3; var6++) {
            if (WorldProjection.containsPoint(var1, var2, this.I(var6), var5, ВеВ, ВеЯ)) {
               switch (var6) {
                  case 0:
                     var4.Вм();
                     break;
                  case 1:
                     var4.必_de5a6f78();
                     break;
                  default:
                     var4.ВВ();
               }

               return true;
            }
         }

         WindowsMediaSession var12 = WindowsMediaSession.П_1751811a();
         float var7 = this.getY() + ВеХ;
         if (var12.Цэ() && WorldProjection.containsPoint(var1, var2, this.x + Вее - ВеА, var7 - Ве4, Веп, ВеД)) {
            this.В = MusicPlayerHud.DragMode.SEEK;
            this.field_4з = а(var1, this.x + ВеЪ, Вет);
            return true;
         } else {
            float var8 = this.x + Веа - ВеЮ - ВеЧ;
            float var9 = var5 + ВеЛ;
            if (var12.ЦЩ() && WorldProjection.containsPoint(var1, var2, var8 - Ве诶, var9 - Ве1, ВеЬ, Ве衣)) {
               this.В = MusicPlayerHud.DragMode.VOLUME;
               this.Ъ(var12, var1);
               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private int юъ() {
      return ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT);
   }

   @Override
   public void Б5() {
      this.В = MusicPlayerHud.DragMode.NONE;
      this.field_4П = this.field_4西 = 0.0F;
      this.ЯИ = 0L;
   }

   private float Ъ_f67ab10a(WindowsMediaSession var1) {
      String var2 = var1.Цl() ? var1.Сй() : ВВ西;
      float var3 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var2, ВВд, 0.0F);
      float var4 = var1.Ц7() ? ВВБ : 0.0F;
      return Math.clamp(var3 + ВВС + var4, ВВз, ВВц + var4);
   }

   private void Ъ(float var1, float var2, float var3, float var4, WindowsMediaSession var5, float var6) {
      this.г(var1, var2, var3, var4, var5, var6);
   }

   public MusicPlayerHud() {
      super(ВВР, 0.0F, 0.0F, ВВ3, ВВр);
   }

   private void юI(float var1) {
      boolean var2 = this.Яь != 0L && System.currentTimeMillis() - this.Яь < ВВк;
      boolean var3 = (this.Ях || var2) && MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen;
      float var4 = var3 ? 1.0F : 0.0F;
      float var5 = var3 ? ВВг : ВВ0;
      this.field_4П = К(this.field_4П, var4, var5, var1);
      this.field_4西 = К(this.field_4西, var3 ? 1.0F : 0.0F, var5 * ВВх, var1);
      if (Math.abs(this.field_4П - var4) < ВВУ) {
         this.field_4П = var4;
      }

      if (Math.abs(this.field_4西 - (var3 ? 1.0F : 0.0F)) < ВВЗ) {
         this.field_4西 = var3 ? 1.0F : 0.0F;
      }
   }

   private void ь(float var1, float var2) {
      this.Ях = WorldProjection.containsPoint(var1, var2, this.x, this.getY(), this.getWidth(), this.getHeight()) || this.В != MusicPlayerHud.DragMode.NONE;
      if (this.Ях) {
         this.Яь = 0L;
      } else if (this.Яь == 0L) {
         this.Яь = System.currentTimeMillis();
      }
   }

   private static float method_6(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * var2;
   }

   private void у(float var1, float var2, int var3) {
      UiDrawing.drawRoundedRect(var1 - ВЯл, var2 + ВЯэ, ВЯю, ВЯЩ, ВЯу, var3);
      UiDrawing.drawRoundedRect(var1 + ВЯ2, var2 - ВЯф, ВЯи, ВЯ9, ВЯо, var3);
      UiDrawing.drawRoundedRect(var1 + ВЯO, var2 - ВЯ7, ВЯР, ВЯ3, ВЯр, var3);
   }

   private void Ъ_de5a6f78(WindowsMediaSession var1) {
      float var2 = method_6(this.x + ВЯЮ, this.x + ВЯЧ, this.field_4П);
      float var3 = method_6(this.getY() + ВЯЛ, this.getY() + ВЯ诶 + 2.0F, this.field_4П);
      float var4 = method_6(ВЯ1, ВЯЬ, this.field_4П);
      Identifier var5 = var1.Ь();
       HudBackground.draw(var2, var3, var4, var4, ВЯ衣);
      if (var5 != null) {
         UiDrawing.drawTexture(var2, var3, var4, var4, ВЯш, new Vector4f(1.0F), -1, var5);
      } else {
         this.у(var2 + var4 * ВЯИ, var3 + var4 * ВЯь, ColorMath.withAlpha(this.юШ(), ВЯж));
      }
   }

   private static String Ъ(long var0) {
      long var2 = Math.max(0L, var0) / ВХъ;
      return String.format(ВХМ, var2 / ВХН, var2 % ВХн);
   }

   private void г_de5a6f78(WindowsMediaSession var1) {
      String var2 = var1.Цl() ? var1.Сй() : ВЯ6;
      String var3 = var1.Цl() ? this.г_388008bb(var1) : this.method_9_388008bb(var1);
      float var4 = this.x + ВЯ_ + ВЯШ + ВЯы;
      float var5 = ВЯй;
      this.Ъ(var2, var4, this.getY() + ВЯЖ, 14, var5, this.юн());
      this.Ъ(var3, var4, this.getY() + ВЯм, 12, var5, this.юъ());
   }

   private String г_388008bb(WindowsMediaSession var1) {
      String var2 = var1.Сф();
      return var2.isBlank() ? var1.СЗ() : var2;
   }

   private void Ъ(WindowsMediaSession var1, float var2) {
      if (this.В == MusicPlayerHud.DragMode.SEEK) {
         this.field_4з = а(var2, this.x + ВЯН, ВЯн);
      } else {
         MusicPlayer var3 = г_e2b88ac7();
         if (var3 != null) {
            float var4 = this.x + ВЯЦ - ВЯч - ВЯЭ;
            var3.method__(Math.round(а(var2, var4, ВЯ必) * ВЯщ));
         }
      }
   }

   private static float К(float var0, float var1, float var2, float var3) {
      return var0 + (var1 - var0) * (1.0F - (float)Math.exp(-var2 * var3));
   }

   private void Ъ(float var1, float var2, float var3, WindowsMediaSession var4, float var5) {
      if (!(var3 <= ВВя)) {
         float var6 = method_4(0.0F, ВЯс, var3);
         float var7 = this.getY() + var2 * ВЯВ;
         float var8 = (1.0F - var6) * ВЯЯ;
         String var9 = var4.Цl() ? var4.Сй() : ВЯХ;
         float var10 = this.x + ВЯе;
         boolean var11 = var4.Ц7();
         float var12 = var11 ? ВЯА : 0.0F;
         float var13 = this.x + var1 - ВЯ4 - var12;
         float var14 = Math.max(1.0F, var13 - (var11 ? ВЯп : ВЯД) - var10);
         this.Ъ(var9, var10, var7 - ВЯЪ - var8, var14, ColorMath.withAlpha(this.юн(), var6), var5);
         if (var11) {
            this.Ъ(var13, this.getY() + ВЯт, var12, ВЯа, var4, var6);
         }
      }
   }

   @Override
   public void иВ() {
      if (this.В == MusicPlayerHud.DragMode.SEEK) {
         WindowsMediaSession var1 = WindowsMediaSession.П_1751811a();
         long var2 = var1.о_6da43b94();
         if (var2 > 0L) {
            var1.O((long)((float)var2 * this.field_4з));
         }
      }

      this.В = MusicPlayerHud.DragMode.NONE;
   }

   private boolean уЙ() {
      MusicPlayer var1 = г_e2b88ac7();
      return var1 != null && var1.isEnabled();
   }

   private void К(String var1, float var2, float var3, int var4, int var5) {
      float var6 = UiDrawing.getTextWidth(ClientFonts.getIconsFont(), var1, var4);
      float var7 = UiDrawing.alignTextCenterY(ClientFonts.getIconsFont(), var1, var3, var4);
      UiDrawing.drawText(ClientFonts.getIconsFont(), var1, var2 - var6 * ВХц, var7, var4, var5);
   }

   private int юШ() {
      return ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
   }

   private void Ъ(String var1, float var2, float var3, float var4, int var5, float var6) {
      if (!var1.equals(this.Вэ)) {
         this.Вэ = var1;
         this.field_4д = 0.0F;
      }

      float var7 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var1, ВЯ弟, 0.0F);
      UiDrawing.pushScissor(var2, var3 - 2.0F, var4, ВЯб);
      if (var7 <= var4) {
         this.field_4д = 0.0F;
         UiDrawing.drawText(ClientFonts.getMediumFont(), var1, var2, var3, 13, var5);
      } else {
         this.field_4д = this.field_4д + var6 * ВЯЫ;
         float var8 = var7 + ВЯl;
         float var9 = var2 + this.field_4д % var8;
         int var10 = (int)Math.ceil(var4 / var8) + 3;

         for (int var11 = -var10; var11 <= 1; var11++) {
            UiDrawing.drawText(ClientFonts.getMediumFont(), var1, var9 + var11 * var8, var3, 13, var5);
         }
      }

      UiDrawing.popScissor();
   }

   private void г(WindowsMediaSession var1, float var2, float var3) {
      float var4 = this.getY() + ВХЦ;

      for (int var5 = 0; var5 < 3; var5++) {
         float var6 = this.I(var5);
         boolean var7 = WorldProjection.containsPoint(var2, var3, var6, var4, ВХч, ВХЭ);
         int var8 = var7 ? this.юШ() : this.юн();
         if (var7) {
            UiDrawing.drawRoundedRect(var6, var4, ВХ必, ВХщ, ВХГ, ColorMath.withAlpha(this.юШ(), ВХл));
         }

         float var9 = var6 + ВХэ;
         float var10 = var4 + ВХю;
         switch (var5) {
            case 0:
               this.К(ВХЩ, var9, var10, 10, var8);
               break;
            case 1:
               this.К(var1.Ц7() ? ВХу : ВХ2, var9, var10, 10, var8);
               break;
            default:
               this.К(ВХф, var9, var10, 10, var8);
         }
      }
   }

   private static float а(float var0, float var1, float var2) {
      return Math.clamp((var0 - var1) / var2, 0.0F, 1.0F);
   }

   private int юн() {
      return ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT);
   }

   private void К(WindowsMediaSession var1) {
      float var2 = this.x + ВХЮ;
      float var3 = this.getY() + ВХЧ;
      float var4 = ВХЛ;
      long var5 = var1.о_6da43b94();
      long var7 = var1.Е();
      float var9 = var5 > 0L ? Math.clamp((float)var7 / (float)var5, 0.0F, 1.0F) : 0.0F;
      if (this.В == MusicPlayerHud.DragMode.SEEK) {
         var9 = this.field_4з;
         var7 = (long)((float)var5 * this.field_4з);
      }

      int var10 = ColorMath.withAlpha(this.юн(), ВХ诶);
      int var11 = this.юШ();
      UiDrawing.drawRoundedRect(var2, var3, var4, ВХ1, ВХЬ, var10);
      if (var9 > 0.0F) {
         float var12 = Math.max(ВХ衣, var4 * var9);
         UiDrawing.drawRoundedRect(var2, var3, var12, ВХш, ВХИ, var11);
         UiDrawing.drawRoundedRect(var2 + var12 - ВХь, var3 - ВХж, ВХ弟, ВХб, ВХЫ, var11);
      }

      String var15 = Ъ(var7);
      String var13 = var5 > 0L ? "-" + Ъ(var5 - var7) : ВХl;
      float var14 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var13, ВХ8, 0.0F);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var15, var2, var3 + ВХ5, 10, this.юъ());
      UiDrawing.drawText(ClientFonts.getMediumFont(), var13, var2 + var4 - var14, var3 + ВХв, 10, this.юъ());
   }

   private boolean уС() {
      return this.field_4П > ВХя;
   }

   private float ЪР() {
      long var1 = System.nanoTime();
      float var3 = this.ЯИ == 0L ? ВВТ : (float)(var1 - this.ЯИ) / ВВО;
      this.ЯИ = var1;
      return Math.clamp(var3, ВВФ, ВВЙ);
   }

   private String method_9_388008bb(WindowsMediaSession var1) {
      String var2 = var1.Сю();
      return var2.isBlank() ? ВЯК : var2;
   }

   public void л(HudRenderEvent var1) {
      if (!this.уЙ()) {
         this.field_4П = this.field_4西 = 0.0F;
         this.ЯИ = 0L;
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else {
         float var2 = var1.getMouseX();
         float var3 = var1.getMouseY();
         float var4 = HudScaleCoordinates.unscaleX(var2, this.getX());
         float var5 = HudScaleCoordinates.unscaleY(var3, this.getY());
         float var6 = HudScaleCoordinates.getHudScale();
         float var7 = this.ЪР();
         float var8 = MINECRAFT_CLIENT.getWindow().getScreenWidth();
         WindowsMediaSession var9 = WindowsMediaSession.П_1751811a();
         float var10 = this.Ъ_f67ab10a(var9);
         float var11 = this.getWidth() > 1.0F ? this.getWidth() : var10;
         float var12 = var11 * var6;
         if (!Float.isFinite(this.field_4Б)) {
            this.field_4Б = Float.isFinite(this.x) && !(this.x <= 1.0F) ? this.x + var12 * ВВ_ : var8 * ВВ6;
         } else if (Float.isFinite(this.field_4С) && Math.abs(this.x - this.field_4С) > ВВШ) {
            this.field_4Б = this.x + var12 * ВВы;
         }

         this.ь(var4, var5);
         this.юI(var7);
         float var13 = method_6(var10, ВВй, this.field_4П);
         float var14 = method_6(ВВЖ, ВВм, this.field_4П);
         float var15 = var13 * var6;
         this.x = Math.clamp(this.field_4Б - var15 * ВВК, 0.0F, Math.max(0.0F, var8 - var15));
         this.field_4С = this.x;
         this.setWidth(var13);
         this.setHeight(var14);
         this.pushGuiScaleTransform();

         try {
            UiDrawing.setTransform(new Matrix4f());
            float var16 = HudScaleCoordinates.unscaleX(var2, this.getX());
            float var17 = HudScaleCoordinates.unscaleY(var3, this.getY());
            if (this.В != MusicPlayerHud.DragMode.NONE) {
               this.Ъ(var9, var16);
            }

            this.ч(var13, var14);
            UiDrawing.pushScissor(this.x, this.getY(), var13, var14);
            this.Ъ_de5a6f78(var9);
            this.Ъ(var13, var14, 1.0F - this.field_4西, var9, var7);
            if (this.field_4西 > ВВI) {
               this.Ъ(var9, var16, var17);
            }

            UiDrawing.popScissor();
         } finally {
            this.popGuiScaleTransform();
         }
      }
   }

   private static MusicPlayer г_e2b88ac7() {
      return RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null && RoseClient.getInstance().getServices().getModules() != null
         ? RoseClient.getInstance().getServices().getModules().getMusicPlayer()
         : null;
   }

   private void method_9_de5a6f78(WindowsMediaSession var1) {
      float var2 = this.x + ВЯк;
      float var3 = this.getY() + ВЯг;
      float var4 = ВЯ0;
      this.г(var2, var3, var4, ВЯх, var1, 1.0F);
   }

   private void Ъ(WindowsMediaSession var1, float var2, float var3) {
      float var4 = method_4(ВЯ8, ВЯ5, this.field_4西);
      if (!(var4 <= ВЯв)) {
         float var5 = (1.0F - var4) * ВЯъ;
         float var6 = this.baseY;
         this.baseY += var5;
         UiDrawing.setGlobalAlpha(ВЯМ * var4);

         try {
            this.г_de5a6f78(var1);
            this.method_9_de5a6f78(var1);
            this.К(var1);
            this.г(var1, var2, var3);
            this.method_9(var1, var2, var3);
         } finally {
            UiDrawing.resetGlobalAlpha();
            this.baseY = var6;
         }
      }
   }

   private void ч(float var1, float var2) {
      float var3 = method_6(ВВЕ, ВВП, this.field_4П);
       HudBackground.draw(this.x, this.getY(), var1, var2, var3);
   }

   private void Ъ(String var1, float var2, float var3, int var4, float var5, int var6) {
      if (var1 != null && !var1.isEmpty()) {
         float var7 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var1, var4, 0.0F);
         float var8 = 0.0F;
         if (var7 > var5) {
            float var9 = var7 - var5;
            float var10 = (float)(System.currentTimeMillis() % ВЯI) / ВЯТ;
            float var11 = (1.0F - (float)Math.cos(var10 * ВЯО * ВЯФ)) * ВЯЙ;
            var8 = -var9 * var11;
         }

         UiDrawing.pushScissor(var2, var3 - 2.0F, var5, var4 + 6);
         UiDrawing.drawText(ClientFonts.getMediumFont(), var1, var2 + var8, var3, var4, var6);
         UiDrawing.popScissor();
      }
   }

   private float I(int var1) {
      float var2 = ВХ西;
      return this.x + (ВХд - var2) * ВХБ - ВХС + var1 * ВХз;
   }

   private void г(float var1, float var2, float var3, float var4, WindowsMediaSession var5, float var6) {
      if (!(var3 <= 2.0F) && !(var4 <= 2.0F)) {
         float var7 = var3 < ВЯУ ? ВЯЗ : 2.0F;
         float var8 = var3 < ВЯЕ ? ВЯП : 2.0F;
         int var9 = Math.max(4, Math.min(18, (int)((var3 + var7) / (var8 + var7))));
         float var10 = (var3 - var7 * (var9 - 1)) / var9;
         if (var10 < ВЯ西) {
            var9 = Math.max(4, (int)((var3 + var7) / (ВЯд + var7)));
            var10 = (var3 - var7 * (var9 - 1)) / var9;
         }

         long var11 = System.currentTimeMillis();
         boolean var13 = var5.Ц7() && var5.Цl();
         int var14 = (var5.Сй() + var5.Сф()).hashCode();
         int var15 = ColorMath.withAlpha(this.юШ(), var6);
         int var16 = ColorMath.withAlpha(this.юн(), ВЯБ * var6);
         UiDrawing.pushScissor(var1, var2, var3, var4);

         try {
            for (int var17 = 0; var17 < var9; var17++) {
               double var18 = Math.sin(var11 / ВЯС + var17 * ВЯз + (var14 & 15)) * ВЯц + ВЯя;
               double var20 = Math.sin(var11 / ВХс + var17 * ВХВ) * ВХЯ + ВХХ;
               float var22 = var13 ? (float)Math.clamp(ВХе + ВХА * (ВХ4 * var18 + ВХп * var20), ВХД, 1.0) : ВХЪ;
               float var23 = Math.max(ВХт, var4 * var22);
               float var24 = var1 + var17 * (var10 + var7);
               float var25 = var2 + var4 - var23;
               UiDrawing.drawRoundedRect(var24, var25, var10, var23, Math.min(2.0F, var10 * ВХа), var13 ? var15 : var16);
            }
         } finally {
            UiDrawing.popScissor();
         }
      }
   }

   private static float method_4(float var0, float var1, float var2) {
      float var3 = Math.clamp((var2 - var0) / (var1 - var0), 0.0F, 1.0F);
      return var3 * var3 * (ВЯГ - 2.0F * var3);
   }

   private void method_9(WindowsMediaSession var1, float var2, float var3) {
      float var4 = this.x + ВХи - ВХ9 - ВХо;
      float var5 = this.getY() + ВХO + ВХ7;
      boolean var6 = var1.ЦЩ();
      boolean var7 = var6 && (this.В == MusicPlayerHud.DragMode.VOLUME || WorldProjection.containsPoint(var2, var3, var4 - ВХР, var5 - ВХ3, ВХр, ВХ6));
      int var8 = ColorMath.withAlpha(var7 ? this.юШ() : this.юъ(), var6 ? 1.0F : ВХ_);
      int var9 = ColorMath.withAlpha(this.юШ(), var6 ? 1.0F : ВХШ);
      float var10 = Math.max(ВХы, ВХй * (var1.ПТ() / ВХЖ));
      this.К(this.а(var1.ПТ()), var4 - ВХм, var5 + ВХК, 9, var8);
      UiDrawing.drawRoundedRect(var4, var5, ВХI, ВХТ, ВХО, ColorMath.withAlpha(this.юн(), ВХФ));
      UiDrawing.drawRoundedRect(var4, var5, var10, ВХЙ, ВХк, var9);
      if (var6) {
         UiDrawing.drawRoundedRect(var4 + var10 - ВХг, var5 - 2.0F, ВХ0, ВХх, ВХУ, var9);
      }
   }

   private enum DragMode {
      NONE,
      SEEK,
      VOLUME;
   }
}
