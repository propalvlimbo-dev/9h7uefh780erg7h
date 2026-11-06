package sg.gui.hud;

import com.google.gson.annotations.SerializedName;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.gui.screens.ChatScreen;
import org.joml.Matrix4f;
import sg.ec.MinecraftAccess;
import sg.rotation.AimGeometryMath;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.network.ClientChat;
import sg.render.WorldProjection;
import sg.render.pipeline.StaffNameText;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.events.impl.HudRenderEvent;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.MultiChoiceSetting;
import sg.settings.SingleChoiceSetting;
import sg.modules.impl.init.NotificationManager;
import sg.modules.impl.init.PanelBackgroundRenderer;

public final class HudSettingsPanel implements MinecraftAccess {
   private static final float Вя = 158.0F;
   private static final float Яс = 26.0F;
   private static final float ЯВ = 54.0F;
   private static final float ЯЯ = 7.0F;
   private static final float ЯХ = 138.0F;
   private static final float Яе = 92.0F;
   private static final float ЯА = 40.0F;
   private static final float Я4 = 22.0F;
   private static final float Яп = 11.0F;
   private final String сЮ;
   private final String сЧ;
   @SerializedName("4")
   private final MultiChoiceSetting field_4;
   @SerializedName("В")
   private final Supplier<SingleChoiceSetting> В_f1b4d92a;
   private final SingleChoiceSetting И;
   private final SingleChoiceSetting ь;
   private final BooleanSetting р;
   @SerializedName("6")
   private final BooleanSetting field_6;
   private final String сЛ;
   @SerializedName("_")
   private final BooleanSetting field__;
   private final String с诶;
   private final FloatSetting I;
   @SerializedName("В")
   private final Function<Float, String> В_8d9078c6;
   private BooleanSetting Ш;
   private FloatSetting Т;
   private String с1;
   private final FloatAnimation л;
   private final FloatAnimation э;
   private final FloatAnimation ю;
   private final FloatAnimation Щ;
   private final FloatAnimation у;
   @SerializedName("2")
   private final FloatAnimation field_2;
   private final FloatAnimation ф;
   private final IdentityHashMap<ChoiceOption, FloatAnimation> Я;
   private final IdentityHashMap<ChoiceOption, FloatAnimation> Х;
   private final EventListener<HudRenderEvent> сс;
   private boolean сщ;
   private boolean сГ;
   private boolean сл;
   private float ЯД;
   private float ЯЪ;
   private float Ят;
   private float Яа;
   private float ЯЮ;
   private static final String ЯУА = "3D модель";
   private static final float ЯУ4 = 10.0F;
   private static final float ЯУп = 7.0F;
   private static final float ЯУД = 7.0F;
   private static final float ЯУЪ = 7.0F;
   private static final float ЯУт = 7.0F;
   private static final float ЯУа = 14.0F;
   private static final float ЯУЮ = 14.0F;
   private static final String ЯУЧ = "3D модель";
   private static final float ЯУЛ = 10.0F;
   private static final float ЯУ诶 = 7.0F;
   private static final float ЯУ1 = 7.0F;
   private static final float ЯУЬ = 7.0F;
   private static final float ЯУ衣 = 7.0F;
   private static final float ЯУш = 14.0F;
   private static final float ЯУИ = 14.0F;
   private static final String ЯУь = "3D модель";
   private static final float ЯУж = 10.0F;
   private static final float ЯУ弟 = 7.0F;
   private static final float ЯУб = 7.0F;
   private static final float ЯУЫ = 7.0F;
   private static final float ЯУl = 7.0F;
   private static final float ЯУ8 = 14.0F;
   private static final float ЯУ5 = 14.0F;
   private static final String ЯУв = "Частицы";
   private static final String ЯУъ = "3D модель";
   private static final float ЯУМ = 10.0F;
   private static final float ЯУН = 7.0F;
   private static final float ЯУн = 7.0F;
   private static final float ЯУЦ = 7.0F;
   private static final float ЯУч = 7.0F;
   private static final float ЯУЭ = 14.0F;
   private static final float ЯУ必 = 14.0F;
   private static final String ЯУщ = "3D модель";
   private static final float ЯУГ = 10.0F;
   private static final float ЯУл = 7.0F;
   private static final float ЯУэ = 7.0F;
   private static final float ЯУю = 7.0F;
   private static final float ЯУЩ = 7.0F;
   private static final float ЯУу = 14.0F;
   private static final float ЯУ2 = 14.0F;
   private static final float ЯУф = 30.0F;
   private static final float ЯУи = 26.0F;
   private static final float ЯУ9 = 26.0F;
   private static final float ЯУо = 26.0F;
   private static final float ЯУO = 48.0F;
   private static final float ЯУ7 = 26.0F;
   private static final float ЯУР = 26.0F;
   private static final float ЯУ3 = 26.0F;
   private static final float ЯУр = 40.0F;
   private static final float ЯУ6 = 26.0F;
   private static final String ЯУ_ = "Выкл";
   private static final String ЯУШ = "Вкл";
   private static final String ЯУы = "Вкл";
   private static final String ЯУй = "Выкл";
   private static final float ЯУЖ = 6.0F;
   private static final float ЯУм = 4.0F;
   private static final float ЯУК = 158.0F;
   private static final float ЯУI = 4.0F;
   private static final float ЯУТ = 6.0F;
   private static final float ЯУО = 4.0F;
   private static final float ЯУФ = 4.0F;
   private static final float ЯУЙ = 158.0F;
   private static final float ЯУк = 4.0F;
   private static final float ЯУг = 4.0F;
   private static final float ЯУ0 = 8.0F;
   private static final float ЯУх = 158.0F;
   private static final float ЯУУ = 4.0F;
   private static final float ЯУЗ = 4.0F;
   private static final float ЯУЕ = 4.0F;
   private static final float ЯУП = 4.0F;
   private static final float ЯУ西 = 4.0F;
   private static final float ЯУд = 54.0F;
   private static final float ЯУБ = 26.0F;
   private static final float ЯУС = 26.0F;
   private static final float ЯУз = 7.0F;
   private static final double ЯУц = 144.0;
   private static final float ЯУя = 26.0F;
   private static final float ЯЗс = 158.0F;
   private static final float ЯЗВ = 41.0F;
   private static final double ЯЗЯ = 30.0;
   private static final double ЯЗХ = 18.0;
   private static final float ЯЗе = 26.0F;
   private static final double ЯЗА = 30.0;
   private static final double ЯЗ4 = 18.0;
   private static final float ЯЗп = 26.0F;
   private static final double ЯЗД = 30.0;
   private static final double ЯЗЪ = 18.0;
   private static final double ЯЗт = 30.0;
   private static final double ЯЗа = 18.0;
   private static final float ЯЗЮ = 11.0F;
   private static final float ЯЗЧ = 136.0F;
   private static final float ЯЗЛ = 26.0F;
   private static final float ЯЗ诶 = 22.0F;
   private static final float ЯЗ1 = 8.0F;
   private static final double ЯЗЬ = 20.0;
   private static final float ЯЗ衣 = 11.0F;
   private static final float ЯЗш = 136.0F;
   private static final float ЯЗИ = 54.0F;
   private static final float ЯЗь = 22.0F;
   private static final float ЯЗж = 8.0F;
   private static final double ЯЗ弟 = 20.0;
   private static final float ЯЗб = 54.0F;
   private static final float ЯЗЫ = 22.0F;
   private static final float ЯЗl = 28.0F;
   private static final float ЯЗ8 = 26.0F;
   private static final float ЯЗ5 = 7.0F;
   private static final double ЯЗв = 144.0;
   private static final float ЯЗъ = 26.0F;
   private static final double ЯЗМ = 158.0;
   private static final float ЯЗН = 158.0F;
   private static final float ЯЗн = 41.0F;
   private static final float ЯЗЦ = 54.0F;
   private static final double ЯЗч = 30.0;
   private static final double ЯЗЭ = 18.0;
   private static final double ЯЗ必 = 158.0;
   private static final float ЯЗщ = 11.0F;
   private static final float ЯЗГ = 136.0F;
   private static final float ЯЗл = 54.0F;
   private static final float ЯЗэ = 8.0F;
   private static final double ЯЗю = 20.0;
   private static final double ЯЗЩ = 158.0;
   private static final float ЯЗу = 54.0F;
   private static final float ЯЗ2 = 26.0F;
   private static final float ЯЗф = 26.0F;
   private static final float ЯЗи = 7.0F;
   private static final float ЯЗ9 = 54.0F;
   private static final double ЯЗо = 144.0;
   private static final double ЯЗO = 158.0;
   private static final String ЯЗ7 = "I";
   private static final String ЯЗР = "Отключите Заменение скина";
   private static final String ЯЗ3 = "Отключите Заменение скина";
   private static final float ЯЗр = 0.01F;
   private static final float ЯЗ6 = 158.0F;
   private static final float ЯЗ_ = 158.0F;
   private static final float ЯЗШ = 12.0F;
   private static final float ЯЗы = 158.0F;
   private static final float ЯЗй = 26.0F;
   private static final float ЯЗЖ = 0.75F;
   private static final float ЯЗм = 158.0F;
   private static final float ЯЗК = 12.0F;
   private static final float ЯЗI = 158.0F;
   private static final float ЯЗТ = 12.0F;
   private static final float ЯЗО = 11.0F;
   private static final float ЯЗФ = 8.0F;
   private static final String ЯЗЙ = "F";
   private static final String ЯЗк = "F";
   private static final float ЯЗг = 158.0F;
   private static final float ЯЗ0 = 11.0F;
   private static final float ЯЗх = 8.0F;
   private static final float ЯЗУ = 33.0F;
   private static final float ЯЗЗ = 0.96F;
   private static final float ЯЗЕ = 0.04F;
   private static final float ЯЗП = 79.0F;
   private static final float ЯЗ西 = 0.5F;
   private static final float ЯЗд = 255.0F;
   private static final float ЯЗБ = 11.0F;
   private static final float ЯЗС = 158.0F;
   private static final float ЯЗз = 11.0F;
   private static final float ЯЗц = 8.0F;
   private static final float ЯЗя = 54.0F;
   private static final String ЯЕс = "Вид";
   private static final float ЯЕВ = 5.0F;
   private static final float ЯЕЯ = 26.0F;
   private static final float ЯЕХ = 0.05F;
   private static final float ЯЕе = 0.5F;
   private static final float ЯЕА = 3.0F;
   private static final float ЯЕ4 = 26.0F;
   private static final String ЯЕп = "От головы";
   private static final String ЯЕД = "Включить частицы";
   private static final float ЯЕЪ = 11.0F;
   private static final float ЯЕт = 5.0F;
   private static final float ЯЕа = 26.0F;
   private static final float ЯЕЮ = 11.0F;
   private static final float ЯЕЧ = 5.0F;
   private static final float ЯЕЛ = 26.0F;
   private static final float ЯЕ诶 = 11.0F;
   private static final float ЯЕ1 = 5.0F;
   private static final float ЯЕЬ = 11.0F;
   private static final float ЯЕ衣 = 5.0F;
   private static final float ЯЕш = 26.0F;
   private static final float ЯЕИ = 100.0F;
   private static final float ЯЕь = 100.0F;
   private static final String ЯЕж = "Размер 3D";
   private static final float ЯЕ弟 = 11.0F;
   private static final float ЯЕб = 158.0F;
   private static final float ЯЕЫ = 11.0F;
   private static final float ЯЕl = 22.0F;
   private static final float ЯЕ8 = 0.01F;
   private static final String ЯЕ5 = "Частицы";
   private static final float ЯЕв = 11.0F;
   private static final float ЯЕъ = 158.0F;
   private static final float ЯЕМ = 11.0F;
   private static final float ЯЕН = 22.0F;
   private static final float ЯЕн = 22.0F;
   private static final float ЯЕЦ = 28.0F;
   private static final float ЯЕч = 0.05F;
   private static final float ЯЕЭ = 0.5F;
   private static final float ЯЕ必 = 3.0F;
   private static final float ЯЕщ = 26.0F;
   private static final float ЯЕГ = 54.0F;
   private static final float ЯЕл = 11.0F;
   private static final float ЯЕэ = 5.0F;
   private static final float ЯЕю = 158.0F;
   private static final float ЯЕЩ = 41.0F;
   private static final float ЯЕу = 30.0F;
   private static final float ЯЕ2 = 18.0F;
   private static final float ЯЕф = 7.0F;
   private static final float ЯЕи = 12.0F;
   private static final float ЯЕ9 = 4.0F;
   private static final float ЯЕо = 14.0F;
   private static final float ЯЕO = 14.0F;
   private static final float ЯЕ7 = 6.0F;
   private static final float ЯЕР = 54.0F;
   private static final float ЯЕ3 = 11.0F;
   private static final float ЯЕр = 14.0F;
   private static final float ЯЕ6 = 158.0F;
   private static final float ЯЕ_ = 11.0F;
   private static final float ЯЕШ = 14.0F;
   private static final float ЯЕы = 11.0F;
   private static final float ЯЕй = 136.0F;
   private static final float ЯЕЖ = 4.0F;
   private static final float ЯЕм = 13.0F;
   private static final float ЯЕК = 6.5F;
   private static final float ЯЕI = 0.35F;
   private static final float ЯЕТ = 5.5F;
   private static final float ЯЕО = 11.0F;
   private static final float ЯЕФ = 136.0F;
   private static final float ЯЕЙ = 11.0F;
   private static final float ЯЕк = 136.0F;
   private static final float ЯЕг = 4.0F;
   private static final float ЯЕ0 = 13.0F;
   private static final float ЯЕх = 6.5F;
   private static final float ЯЕУ = 0.35F;
   private static final float ЯЕЗ = 5.5F;
   private static final float ЯЕЕ = 54.0F;
   private static final float ЯЕП = 26.0F;
   private static final float ЯЕ西 = 7.0F;
   private static final double ЯЕд = 144.0;
   private static final double ЯЕБ = 24.0;
   private static final float ЯЕС = 6.0F;
   private static final float ЯЕз = 0.35F;
   private static final String ЯЕц = "r";
   private static final float ЯЕя = 11.0F;
   private static final float ЯПс = 4.0F;
   private static final float ЯПВ = 6.0F;
   private static final float ЯПЯ = 12.0F;
   private static final float ЯПХ = 16.0F;
   private static final float ЯПе = 4.0F;
   private static final float ЯПА = 138.0F;
   private static final float ЯП4 = 26.0F;
   private static final float ЯПп = 8.0F;
   private static final float ЯПД = 87.0F;
   private static final float ЯПЪ = 92.0F;
   private static final float ЯПт = 54.0F;
   private static final float ЯПа = 26.0F;
   private static final float ЯПЮ = 7.0F;
   private static final float ЯПЧ = 12.0F;
   private static final float ЯПЛ = 7.0F;

   public void method_2(float var1, float var2) {
      if (this.сщ) {
         this.сС();
      } else {
         float var3 = this.l();
         this.ЯД = Math.clamp(var1 + ЯУЖ, ЯУм, MINECRAFT_CLIENT.getWindow().getScreenWidth() - ЯУК - ЯУI);
         this.ЯЪ = Math.clamp(var2 + ЯУТ, ЯУО, MINECRAFT_CLIENT.getWindow().getScreenHeight() - var3 - ЯУФ);
         this.сщ = true;
         this.ся();
      }
   }

   private void Ю(float var1, float var2, float var3) {
      if (this.сГ && this.с5()) {
         this.б(var1);
      }

      this.э.updateTarget(this.р.getValue() ? 1.0F : 0.0F);
      if (this.field__ != null) {
         this.Щ.updateTarget(this.field__.getValue() ? 1.0F : 0.0F);
      }

      float var4 = this.э.getValue();
      float var5 = var3;
      float var6 = this.Ят + ЯЗя;
      if (this.И != null) {
         this.с(ЯЕс, this.И.getSelectedOption().getName(), var6 + ЯЕВ, с(ThemeColorSlot.WINDOW_TEXT, var5), с(ThemeColorSlot.WINDOW_SUR_TEXT, var5));
         List var7 = this.И.getValue();
         float var8 = var6 + ЯЕЯ;
         float var9 = Math.min(ЯЕХ, ЯЕе / var7.size());

         for (int var10 = 0; var10 < var7.size(); var10++) {
            float var11 = Math.clamp(var3 * (1.0F + var7.size() * var9) - var10 * var9, 0.0F, 1.0F);
            var11 = var11 * var11 * (ЯЕА - 2.0F * var11);
            this.с(this.И, (ChoiceOption)var7.get(var10), var8 + var10 * ЯЕ4, var3, var11, var1, var2);
         }
      }

      float var17 = var6 + this.М();
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), this.field__ != null ? ЯЕп : ЯЕД, this.ЯД + ЯЕЪ, var17 + ЯЕт, 11, с(ThemeColorSlot.WINDOW_TEXT, var5));
      this.ж(var17, var4, var5);
      if (this.field__ != null) {
         float var18 = var17 + ЯЕа;
         UiBatchDrawing.drawText(ClientFonts.getMediumFont(), this.с诶, this.ЯД + ЯЕЮ, var18 + ЯЕЧ, 11, с(ThemeColorSlot.WINDOW_TEXT, var5));
         this.ж(var18, this.Щ.getValue(), var5);
      }

      if (this.field_6 != null) {
         this.ю.updateTarget(this.field_6.getValue() ? 1.0F : 0.0F);
         float var19 = var17 + ЯЕЛ + this.А_f67ab10a();
         UiBatchDrawing.drawText(ClientFonts.getMediumFont(), this.сЛ, this.ЯД + ЯЕ诶, var19 + ЯЕ1, 11, с(ThemeColorSlot.WINDOW_TEXT, var5));
         this.ж(var19, this.ю.getValue(), var5);
      }

      if (this.Ш != null) {
         this.у.updateTarget(this.Ш.getValue() ? 1.0F : 0.0F);
         float var20 = this.ж(var17);
         UiBatchDrawing.drawText(ClientFonts.getMediumFont(), this.с1, this.ЯД + ЯЕЬ, var20 + ЯЕ衣, 11, с(ThemeColorSlot.WINDOW_TEXT, var5));
         this.ж(var20, this.у.getValue(), var5);
         if (this.сФ()) {
            if (this.сл) {
               this.Б(var1);
            }

            float var22 = var20 + ЯЕш;
            String var24 = Math.round(this.Т.getValue() * ЯЕИ) / ЯЕь + "";
            UiBatchDrawing.drawText(ClientFonts.getMediumFont(), ЯЕж, this.ЯД + ЯЕ弟, var22, 11, с(ThemeColorSlot.WINDOW_TEXT, var5));
            UiBatchDrawing.drawText(
               ClientFonts.getMediumFont(),
               var24,
               this.ЯД + ЯЕб - ЯЕЫ - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var24, 11),
               var22,
               11,
               с(ThemeColorSlot.WINDOW_SUR_TEXT, var5)
            );
            this.Ю(var22 + ЯЕl, var5);
         }
      }

      float var21 = this.field__ == null ? var4 : Math.max(var4, this.Щ.getValue());
      float var23 = var5 * var21;
      if (!(var23 <= ЯЕ8)) {
         float var25 = var6 + this.У_f67ab10a();
         String var27 = this.В_8d9078c6.apply(this.I.getValue());
         UiBatchDrawing.drawText(ClientFonts.getMediumFont(), ЯЕ5, this.ЯД + ЯЕв, var25, 11, с(ThemeColorSlot.WINDOW_TEXT, var23));
         UiBatchDrawing.drawText(
            ClientFonts.getMediumFont(),
            var27,
            this.ЯД + ЯЕъ - ЯЕМ - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var27, 11),
            var25,
            11,
            с(ThemeColorSlot.WINDOW_SUR_TEXT, var23)
         );
         this.м(var25 + ЯЕН, var23);
         if (this.ь != null) {
            List var12 = this.ь.getValue();
            float var13 = var25 + ЯЕн + ЯЕЦ;
            float var14 = Math.min(ЯЕч, ЯЕЭ / var12.size());

            for (int var15 = 0; var15 < var12.size(); var15++) {
               float var16 = Math.clamp(var3 * (1.0F + var12.size() * var14) - var15 * var14, 0.0F, 1.0F);
               var16 = var16 * var16 * (ЯЕ必 - 2.0F * var16);
               this.с((ChoiceOption)var12.get(var15), var13 + var15 * ЯЕщ, var3 * var23, var16, var1, var2);
            }
         }
      }
   }

   public HudSettingsPanel(String var1, BooleanSetting var2, FloatSetting var3, SingleChoiceSetting var4) {
      this(var1, var2, var3, var4, null, null);
   }

   private void Б(float var1) {
      if (this.Т != null) {
         this.с(this.Т, var1);
      }
   }

   private float и_f67ab10a() {
      return this.с(this.I);
   }

   private float ш(float var1) {
      float var2 = this.Ят + this.Яа * ЯЗ西;
      return var2 + (var1 - var2) / this.ЯЮ;
   }

   private float с(FloatSetting var1) {
      float var2 = var1.getMaximum() - var1.getMinimum();
      return var2 == 0.0F ? 0.0F : (var1.getValue() - var1.getMinimum()) / var2;
   }

   private void с(ChoiceOption var1, float var2, float var3, float var4, float var5, float var6) {
      this.с(var1, this.method_2_bbeebd87(var1), var2, var3, var4, var5, var6);
   }

   private void с(ChoiceOption var1) {
      if (this.ь != null) {
         this.ь.setSelectedOption(var1);
      } else if (this.field_4 != null) {
         var1.setSelected(!var1.isSelected());
      } else {
         this.В_f1b4d92a.get().setSelectedOption(var1);
      }
   }

   private void б(float var1) {
      this.с(this.I, var1);
   }

   public boolean ю(float var1, float var2, int var3) {
      if (!this.сщ) {
         return false;
      }

      var1 = this.Ч(var1);
      var2 = this.ш(var2);
      if (var3 == 0 && this.сх()) {
         float var18 = this.Ят + ЯУд;
         if (this.И != null) {
            float var21 = var18 + ЯУБ;
            int var23 = (int)((var2 - var21) / ЯУС);
            if (var23 >= 0 && var23 < this.И.getValue().size() && WorldProjection.containsPoint(var1, var2, this.ЯД + ЯУз, var21, ЯУц, this.И.getValue().size() * ЯУя)) {
               this.И.setSelectedOption(this.И.getValue().get(var23));
               return true;
            }
         }

         float var22 = var18 + this.М();
         float var24 = this.ЯД + ЯЗс - ЯЗВ;
         float var7 = var22 + 2.0F;
         if (WorldProjection.containsPoint(var1, var2, var24, var7, ЯЗЯ, ЯЗХ)) {
            this.р.setValue(!this.р.getValue());
            return true;
         }

         if (this.field__ != null) {
            float var8 = var22 + ЯЗе + 2.0F;
            if (WorldProjection.containsPoint(var1, var2, var24, var8, ЯЗА, ЯЗ4)) {
               this.field__.setValue(!this.field__.getValue());
               return true;
            }
         }

         if (this.field_6 != null) {
            float var25 = var22 + ЯЗп + this.А_f67ab10a() + 2.0F;
            if (WorldProjection.containsPoint(var1, var2, var24, var25, ЯЗД, ЯЗЪ)) {
               boolean var29 = !this.field_6.getValue();
               this.field_6.setValue(var29);
               if (var29 && this.Ш != null) {
                  this.Ш.setValue(false);
               }

               return true;
            }
         }

         if (this.Ш != null) {
            float var26 = this.ж(var22);
            if (WorldProjection.containsPoint(var1, var2, var24, var26 + 2.0F, ЯЗт, ЯЗа)) {
               if (!this.Ш.getValue() && this.field_6 != null && this.field_6.getValue()) {
                  this.сА();
                  return true;
               }

               this.Ш.setValue(!this.Ш.getValue());
               return true;
            }

            if (this.сФ()) {
               float var9 = this.ЯД + ЯЗЮ;
               float var10 = ЯЗЧ;
               float var11 = var26 + ЯЗЛ + ЯЗ诶;
               if (WorldProjection.containsPoint(var1, var2, var9, var11 - ЯЗ1, var10, ЯЗЬ)) {
                  this.сл = true;
                  this.Б(var1);
                  return true;
               }
            }
         }

         if (this.с5()) {
            float var27 = this.ЯД + ЯЗ衣;
            float var28 = ЯЗш;
            float var30 = this.Ят + ЯЗИ + this.У_f67ab10a() + ЯЗь;
            if (WorldProjection.containsPoint(var1, var2, var27, var30 - ЯЗж, var28, ЯЗ弟)) {
               this.сГ = true;
               this.б(var1);
               return true;
            }

            if (this.ь != null) {
               List var31 = this.ь.getValue();
               float var12 = this.Ят + ЯЗб + this.У_f67ab10a() + ЯЗЫ + ЯЗl;
               int var13 = (int)((var2 - var12) / ЯЗ8);
               if (var13 >= 0 && var13 < var31.size() && WorldProjection.containsPoint(var1, var2, this.ЯД + ЯЗ5, var12, ЯЗв, var31.size() * ЯЗъ)) {
                  this.ь.setSelectedOption((ChoiceOption)var31.get(var13));
                  return true;
               }
            }
         }

         if (WorldProjection.containsPoint(var1, var2, this.ЯД, this.Ят, ЯЗМ, this.l())) {
            return true;
         }

         this.сС();
         return false;
      } else if (var3 == 0 && this.ск()) {
         float var17 = this.ЯД + ЯЗН - ЯЗн;
         float var20 = this.Ят + ЯЗЦ + 2.0F;
         if (WorldProjection.containsPoint(var1, var2, var17, var20, ЯЗч, ЯЗЭ)) {
            this.р.setValue(!this.р.getValue());
            return true;
         }

         if (WorldProjection.containsPoint(var1, var2, this.ЯД, this.Ят, ЯЗ必, this.l())) {
            return true;
         }

         this.сС();
         return false;
      } else if (var3 == 0 && this.сз()) {
         float var16 = this.ЯД + ЯЗщ;
         float var19 = ЯЗГ;
         if (WorldProjection.containsPoint(var1, var2, var16, this.Ят + ЯЗл - ЯЗэ, var19, ЯЗю)) {
            this.сГ = true;
            this.б(var1);
            return true;
         }

         if (WorldProjection.containsPoint(var1, var2, this.ЯД, this.Ят, ЯЗЩ, this.l())) {
            return true;
         }

         this.сС();
         return false;
      } else {
         if (var3 == 0 && !this.сх() && !this.ск()) {
            List var4 = this.method__();
            int var5 = (int)((var2 - this.Ят - ЯЗу) / ЯЗ2);
            float var6 = var4.size() * ЯЗф;
            if (var5 >= 0 && var5 < var4.size() && WorldProjection.containsPoint(var1, var2, this.ЯД + ЯЗи, this.Ят + ЯЗ9, ЯЗо, var6)) {
               this.с((ChoiceOption)var4.get(var5));
               return true;
            }
         }

         if (WorldProjection.containsPoint(var1, var2, this.ЯД, this.Ят, ЯЗO, this.l())) {
            return true;
         }

         this.сС();
         return false;
      }
   }

   public HudSettingsPanel(String var1, String var2, MultiChoiceSetting var3) {
      this.с1 = ЯУА;
      this.л = new FloatAnimation(0.0F, ЯУ4, Easings.CUBIC_OUT);
      this.э = new FloatAnimation(0.0F, ЯУп, Easings.CUBIC_IN_OUT);
      this.ю = new FloatAnimation(0.0F, ЯУД, Easings.CUBIC_IN_OUT);
      this.Щ = new FloatAnimation(0.0F, ЯУЪ, Easings.CUBIC_IN_OUT);
      this.у = new FloatAnimation(0.0F, ЯУт, Easings.CUBIC_IN_OUT);
      this.field_2 = new FloatAnimation(0.0F, ЯУа, Easings.CUBIC_OUT);
      this.ф = new FloatAnimation(0.0F, ЯУЮ, Easings.CUBIC_OUT);
      this.Я = new IdentityHashMap<>();
      this.Х = new IdentityHashMap<>();
      this.сс = new EventListener<HudRenderEvent>() {
         public void onEvent(HudRenderEvent var1) {
            HudSettingsPanel.this.method_5(var1);
         }

         @Override
         public int getPriority() {
            return -100;
         }
      };
      this.ЯЮ = 1.0F;
      this.сЮ = var1;
      this.сЧ = var2;
      this.field_4 = var3;
      this.В_f1b4d92a = null;
      this.И = null;
      this.ь = null;
      this.р = null;
      this.field_6 = null;
      this.сЛ = null;
      this.field__ = null;
      this.с诶 = null;
      this.I = null;
      this.В_8d9078c6 = null;
   }

   private void ж(float var1, float var2, float var3) {
      int var4 = ColorMath.lerpArgb(с(ThemeColorSlot.DEACTIVE_STATE, var3), с(ThemeColorSlot.ACTIVE_STATE, var3), var2);
      int var5 = с(ThemeColorSlot.CIRCLE, var3);
      float var6 = this.ЯД + ЯЕю - ЯЕЩ;
      UiBatchDrawing.drawRoundedRect(var6, var1 + 2.0F, ЯЕу, ЯЕ2, ЯЕф, var4);
      UiBatchDrawing.drawRoundedRect(var6 + 2.0F + ЯЕи * var2, var1 + ЯЕ9, ЯЕо, ЯЕO, ЯЕ7, var5);
   }

   private boolean method_2_bbeebd87(ChoiceOption var1) {
      if (this.ь != null) {
         return this.ь.getSelectedOption() == var1;
      } else {
         return this.field_4 != null ? var1.isSelected() : this.В_f1b4d92a.get().getSelectedOption() == var1;
      }
   }

   private void Ю(float var1, float var2) {
      this.ф.updateTarget(this.с(this.Т));
      float var3 = Math.clamp(this.ф.getValue(), 0.0F, 1.0F);
      float var4 = this.ЯД + ЯЕЙ;
      float var5 = ЯЕк;
      float var6 = ЯЕг;
      UiBatchDrawing.drawRoundedRect(var4, var1, var5, var6, 2.0F, с(ThemeColorSlot.WINDOW_BACKGROUND_TWO, var2));
      UiBatchDrawing.drawRoundedRect(var4, var1, Math.max(var6, var5 * var3), var6, 2.0F, с(ThemeColorSlot.MAIN, var2));
      float var7 = ЯЕ0;
      float var8 = var4 + var5 * var3 - var7 / 2.0F;
      float var9 = var1 + var6 / 2.0F - var7 / 2.0F;
      UiBatchDrawing.drawRoundedRect(var8 - 1.0F, var9 - 1.0F, var7 + 2.0F, var7 + 2.0F, ЯЕх, с(ThemeColorSlot.MAIN, ЯЕУ * var2));
      UiBatchDrawing.drawRoundedRect(var8, var9, var7, var7, ЯЕЗ, с(ThemeColorSlot.CIRCLE, var2));
   }

   private void с(FloatSetting var1, float var2) {
      float var3 = this.ЯД + ЯЕО;
      float var4 = ЯЕФ;
      float var5 = (var2 - var3) / var4 * (var1.getMaximum() - var1.getMinimum()) + var1.getMinimum();
      if (var1.getStep() > 0.0F) {
         var5 = (float)AimGeometryMath.roundToStep(var5, var1.getStep());
      }

      var1.setValue(var5);
   }

   private boolean сх() {
      return this.р != null && this.I != null;
   }

   private String ИЪ() {
      if (this.сх()) {
         if (!this.с5()) {
            return ЯУ_;
         } else {
            return this.ь != null ? this.ь.getSelectedOption().getName() : ЯУШ;
         }
      } else if (this.ск()) {
         return this.р.getValue() ? ЯУы : ЯУй;
      } else if (this.сз()) {
         return this.В_8d9078c6.apply(this.I.getValue());
      } else if (this.field_4 != null) {
         long var1 = this.field_4.getValue().stream().filter(ChoiceOption::isSelected).count();
         return var1 + " из " + this.field_4.getValue().size();
      } else {
         return this.В_f1b4d92a.get().getSelectedOption().getName();
      }
   }

   public HudSettingsPanel(String var1, String var2, BooleanSetting var3) {
      this.с1 = ЯУщ;
      this.л = new FloatAnimation(0.0F, ЯУГ, Easings.CUBIC_OUT);
      this.э = new FloatAnimation(0.0F, ЯУл, Easings.CUBIC_IN_OUT);
      this.ю = new FloatAnimation(0.0F, ЯУэ, Easings.CUBIC_IN_OUT);
      this.Щ = new FloatAnimation(0.0F, ЯУю, Easings.CUBIC_IN_OUT);
      this.у = new FloatAnimation(0.0F, ЯУЩ, Easings.CUBIC_IN_OUT);
      this.field_2 = new FloatAnimation(0.0F, ЯУу, Easings.CUBIC_OUT);
      this.ф = new FloatAnimation(0.0F, ЯУ2, Easings.CUBIC_OUT);
      this.Я = new IdentityHashMap<>();
      this.Х = new IdentityHashMap<>();
      this.сс = new EventListener<HudRenderEvent>() {
         public void onEvent(HudRenderEvent var1) {
            HudSettingsPanel.this.method_5(var1);
         }

         @Override
         public int getPriority() {
            return -100;
         }
      };
      this.ЯЮ = 1.0F;
      this.сЮ = var1;
      this.сЧ = var2;
      this.field_4 = null;
      this.В_f1b4d92a = null;
      this.И = null;
      this.ь = null;
      this.р = var3;
      this.field_6 = null;
      this.сЛ = null;
      this.field__ = null;
      this.с诶 = null;
      this.I = null;
      this.В_8d9078c6 = null;
   }

   private void ю(float var1, float var2) {
      if (this.сГ) {
         this.б(var1);
      }

      float var3 = this.Ят + ЯЕР;
      this.м(var3, var2);
      String var4 = this.В_8d9078c6.apply(this.I.getMinimum());
      String var5 = this.В_8d9078c6.apply(this.I.getMaximum());
      int var6 = с(ThemeColorSlot.WINDOW_SUR_TEXT, var2);
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var4, this.ЯД + ЯЕ3, var3 + ЯЕр, 10, var6);
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var5, this.ЯД + ЯЕ6 - ЯЕ_ - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var5, 10), var3 + ЯЕШ, 10, var6);
   }

   private void 诶(float var1) {
      this.э.updateTarget(this.р.getValue() ? 1.0F : 0.0F);
      float var2 = this.э.getValue();
      float var3 = this.Ят + ЯЕГ;
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), this.сЧ, this.ЯД + ЯЕл, var3 + ЯЕэ, 11, с(ThemeColorSlot.WINDOW_TEXT, var1));
      this.ж(var3, var2, var1);
   }

   public HudSettingsPanel с(BooleanSetting var1, FloatSetting var2) {
      this.Ш = var1;
      this.Т = var2;
      return this;
   }

   private float Ч(float var1) {
      float var2 = this.ЯД + ЯЗП;
      return var2 + (var1 - var2) / this.ЯЮ;
   }

   public void сУ() {
      this.сГ = false;
      this.сл = false;
   }

   private void method_5(HudRenderEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (!(MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen)) {
            this.сС();
         }

         this.л.updateTarget(this.сщ ? 1.0F : 0.0F);
         float var2 = Math.clamp(this.л.getValue(), 0.0F, 1.0F);
         if (!(var2 <= ЯЗр)) {
            if (MINECRAFT_CLIENT.getWindow().getScreenWidth() > 0 && MINECRAFT_CLIENT.getWindow().getScreenHeight() > 0) {
               this.ся();
               float var3 = this.Ч(var1.getMouseX());
               float var4 = this.ш(var1.getMouseY());
               Matrix4f var5 = new Matrix4f(UiDrawing.getTransform());
               float var6 = UiDrawing.getGlobalAlpha();

               try {
                  UiDrawing.setTransform(new Matrix4f());
                  WorldProjection.scaleAroundRect(this.ЯД, this.Ят, ЯЗ6, this.Яа, this.ЯЮ);
                  UiDrawing.setGlobalAlpha(var6 * var2);
                  UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());

                  try {
                      sg.hud.HudBackground.drawBatched(this.ЯД, this.Ят, ЯЗм, this.Яа, ЯЗК);

                     UiBatchDrawing.drawText(ClientFonts.getMediumFont(), this.сЮ, this.ЯД + ЯЗО, this.Ят + ЯЗФ, 13, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
                     float var7 = UiDrawing.getTextWidth(ClientFonts.getIconsFont(), ЯЗЙ, 12);
                     UiBatchDrawing.drawText(ClientFonts.getIconsFont(), ЯЗк, this.ЯД + ЯЗг - ЯЗ0 - var7, this.Ят + ЯЗх, 13, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
                     int var8 = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT);
                     this.с(this.сЧ, this.ИЪ(), this.Ят + ЯЗУ, var8, var8);
                     if (this.сх()) {
                        this.Ю(var3, var4, 1.0F);
                     } else if (this.ск()) {
                        this.诶(1.0F);
                     } else if (this.сз()) {
                        this.ю(var3, 1.0F);
                     } else {
                        List var9 = this.method__();

                        for (int var10 = 0; var10 < var9.size(); var10++) {
                           this.с((ChoiceOption)var9.get(var10), var10, 1.0F, 1.0F, var3, var4);
                        }
                     }
                  } finally {
                     UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
                     PanelBackgroundRenderer.Ен();
                  }
               } finally {
                  UiDrawing.getTransform().set(var5);
                  UiDrawing.setGlobalAlpha(var6);
               }
            }
         }
      } else {
         this.сС();
         this.л.setImmediately(0.0F);
      }
   }

   private float А_f67ab10a() {
      return this.field__ != null ? ЯУ6 : 0.0F;
   }

   public HudSettingsPanel(String var1, BooleanSetting var2, FloatSetting var3) {
      this(var1, var2, var3, null, null, null);
   }

   private void сА() {
      NotificationManager.р(ЯЗ7, ЯЗР, ColorMath.packRgb(255, 70, 70));
      if (MINECRAFT_CLIENT.player != null) {
         ClientChat.З(ЯЗ3);
      }
   }

   private void с(SingleChoiceSetting var1, ChoiceOption var2, float var3, float var4, float var5, float var6, float var7) {
      this.с(var2, var1.getSelectedOption() == var2, var3, var4, var5, var6, var7);
   }

   private void ся() {
      this.Ят = this.ЯЪ;
      this.Яа = this.l();
      this.ЯЮ = ЯЗЗ + ЯЗЕ * Math.clamp(this.л.getValue(), 0.0F, 1.0F);
   }

   private void м(float var1, float var2) {
      this.field_2.updateTarget(this.и_f67ab10a());
      float var3 = Math.clamp(this.field_2.getValue(), 0.0F, 1.0F);
      float var4 = this.ЯД + ЯЕы;
      float var5 = ЯЕй;
      float var6 = ЯЕЖ;
      UiBatchDrawing.drawRoundedRect(var4, var1, var5, var6, 2.0F, с(ThemeColorSlot.WINDOW_BACKGROUND_TWO, var2));
      UiBatchDrawing.drawRoundedRect(var4, var1, Math.max(var6, var5 * var3), var6, 2.0F, с(ThemeColorSlot.MAIN, var2));
      float var7 = ЯЕм;
      float var8 = var4 + var5 * var3 - var7 / 2.0F;
      float var9 = var1 + var6 / 2.0F - var7 / 2.0F;
      UiBatchDrawing.drawRoundedRect(var8 - 1.0F, var9 - 1.0F, var7 + 2.0F, var7 + 2.0F, ЯЕК, с(ThemeColorSlot.MAIN, ЯЕI * var2));
      UiBatchDrawing.drawRoundedRect(var8, var9, var7, var7, ЯЕТ, с(ThemeColorSlot.CIRCLE, var2));
   }

   private void с(ChoiceOption var1, boolean var2, float var3, float var4, float var5, float var6, float var7) {
      FloatAnimation var8 = this.Я.computeIfAbsent(var1, var0 -> new FloatAnimation(0.0F, ЯПЛ, Easings.CUBIC_IN_OUT));
      FloatAnimation var9 = this.Х.computeIfAbsent(var1, var0 -> new FloatAnimation(0.0F, ЯПЧ, Easings.CUBIC_OUT));
      var8.updateTarget(var2 ? 1.0F : 0.0F);
      boolean var10 = this.сщ && WorldProjection.containsPoint(var6, var7, this.ЯД + ЯЕ西, var3 - 2.0F, ЯЕд, ЯЕБ);
      var9.updateTarget(var10 ? 1.0F : 0.0F);
      float var11 = var8.getValue();
      float var12 = var9.getValue();
      float var13 = var4 * var5;
      float var14 = ЯЕС * (1.0F - var5);
      int var15 = ColorMath.lerpArgb(с(ThemeColorSlot.WINDOW_SUR_TEXT, var13), с(ThemeColorSlot.WINDOW_TEXT, var13), Math.max(var11, var12 * ЯЕз));
      UiBatchDrawing.drawText(ClientFonts.getIconsFont(), ЯЕц, this.ЯД + ЯЕя + ЯПс * (1.0F - var11) + var14, var3 + ЯПВ, 11, с(ThemeColorSlot.MAIN, var11 * var13));
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var1.getName(), this.ЯД + ЯПЯ + ЯПХ * var11 + 2.0F * var12 + var14, var3 + ЯПе, 11, var15);
   }

   public void м(float var1, float var2, float var3) {
      if (this.сщ) {
         this.сС();
      } else {
         float var4 = this.l();
         float var5 = MINECRAFT_CLIENT.getWindow().getScreenWidth();
         float var6 = MINECRAFT_CLIENT.getWindow().getScreenHeight();
         float var7 = var5 - ЯУЙ - ЯУк;
         float var8 = var6 - var4 - ЯУг;
         float var9 = ЯУ0;
         float var10 = var1 + var3 + var9;
         float var11 = var1 - ЯУх - var9;
         if (var10 <= var7) {
            this.ЯД = Math.max(ЯУУ, var10);
         } else if (var11 >= ЯУЗ) {
            this.ЯД = Math.min(var11, var7);
         } else {
            this.ЯД = Math.clamp(var10, ЯУЕ, var7);
         }

         this.ЯЪ = Math.clamp(var2, ЯУП, Math.max(ЯУ西, var8));
         this.сщ = true;
         this.ся();
      }
   }

   private float l() {
      if (this.сх()) {
         float var1 = ЯПА + this.а_f67ab10a();
         if (this.ь != null) {
            var1 += this.ь.getValue().size() * ЯП4 + ЯПп;
         }

         return var1;
      } else if (this.ск()) {
         return ЯПД;
      } else {
         return this.сз() ? ЯПЪ : ЯПт + this.method__().size() * ЯПа + ЯПЮ;
      }
   }

   public void сС() {
      this.сщ = false;
      this.сГ = false;
      this.сл = false;
   }

   private float У_f67ab10a() {
      return ЯУр + this.а_f67ab10a();
   }

   private float М() {
      return this.И == null ? 0.0F : (this.И.getValue().size() + 1) * ЯУ3;
   }

   private float ж(float var1) {
      return var1 + ЯУ7 + this.А_f67ab10a() + (this.field_6 != null ? ЯУР : 0.0F);
   }

   private boolean сз() {
      return this.р == null && this.I != null;
   }

   public HudSettingsPanel(String var1, String var2, Supplier<SingleChoiceSetting> var3) {
      this.с1 = ЯУЧ;
      this.л = new FloatAnimation(0.0F, ЯУЛ, Easings.CUBIC_OUT);
      this.э = new FloatAnimation(0.0F, ЯУ诶, Easings.CUBIC_IN_OUT);
      this.ю = new FloatAnimation(0.0F, ЯУ1, Easings.CUBIC_IN_OUT);
      this.Щ = new FloatAnimation(0.0F, ЯУЬ, Easings.CUBIC_IN_OUT);
      this.у = new FloatAnimation(0.0F, ЯУ衣, Easings.CUBIC_IN_OUT);
      this.field_2 = new FloatAnimation(0.0F, ЯУш, Easings.CUBIC_OUT);
      this.ф = new FloatAnimation(0.0F, ЯУИ, Easings.CUBIC_OUT);
      this.Я = new IdentityHashMap<>();
      this.Х = new IdentityHashMap<>();
      this.сс = new EventListener<HudRenderEvent>() {
         public void onEvent(HudRenderEvent var1) {
            HudSettingsPanel.this.method_5(var1);
         }

         @Override
         public int getPriority() {
            return -100;
         }
      };
      this.ЯЮ = 1.0F;
      this.сЮ = var1;
      this.сЧ = var2;
      this.field_4 = null;
      this.В_f1b4d92a = var3;
      this.И = null;
      this.ь = null;
      this.р = null;
      this.field_6 = null;
      this.сЛ = null;
      this.field__ = null;
      this.с诶 = null;
      this.I = null;
      this.В_8d9078c6 = null;
   }

   public HudSettingsPanel(String var1, BooleanSetting var2, FloatSetting var3, SingleChoiceSetting var4, BooleanSetting var5, String var6, SingleChoiceSetting var7) {
      this(var1, var2, var3, var4, var5, var6, var7, null, null);
   }

   private boolean с5() {
      return this.р.getValue() || this.field__ != null && this.field__.getValue();
   }

   public HudSettingsPanel(String var1, String var2, FloatSetting var3, Function<Float, String> var4) {
      this.с1 = ЯУъ;
      this.л = new FloatAnimation(0.0F, ЯУМ, Easings.CUBIC_OUT);
      this.э = new FloatAnimation(0.0F, ЯУН, Easings.CUBIC_IN_OUT);
      this.ю = new FloatAnimation(0.0F, ЯУн, Easings.CUBIC_IN_OUT);
      this.Щ = new FloatAnimation(0.0F, ЯУЦ, Easings.CUBIC_IN_OUT);
      this.у = new FloatAnimation(0.0F, ЯУч, Easings.CUBIC_IN_OUT);
      this.field_2 = new FloatAnimation(0.0F, ЯУЭ, Easings.CUBIC_OUT);
      this.ф = new FloatAnimation(0.0F, ЯУ必, Easings.CUBIC_OUT);
      this.Я = new IdentityHashMap<>();
      this.Х = new IdentityHashMap<>();
      this.сс = new EventListener<HudRenderEvent>() {
         public void onEvent(HudRenderEvent var1) {
            HudSettingsPanel.this.method_5(var1);
         }

         @Override
         public int getPriority() {
            return -100;
         }
      };
      this.ЯЮ = 1.0F;
      this.сЮ = var1;
      this.сЧ = var2;
      this.field_4 = null;
      this.В_f1b4d92a = null;
      this.И = null;
      this.ь = null;
      this.р = null;
      this.field_6 = null;
      this.сЛ = null;
      this.field__ = null;
      this.с诶 = null;
      this.I = var3;
      this.В_8d9078c6 = var4;
   }

   private boolean сФ() {
      return this.Ш != null && this.Ш.getValue() && this.Т != null;
   }

   private void с(ChoiceOption var1, int var2, float var3, float var4, float var5, float var6) {
      this.с(var1, this.Ят + ЯЕЕ + var2 * ЯЕП, var3, var4, var5, var6);
   }

   private List<ChoiceOption> method__() {
      if (this.ь != null) {
         return this.ь.getValue();
      } else {
         return this.field_4 != null ? this.field_4.getValue() : this.В_f1b4d92a.get().getValue();
      }
   }

   private void с(String var1, String var2, float var3, int var4, int var5) {
      float var6 = this.ЯД + ЯЗБ;
      float var7 = this.ЯД + ЯЗС - ЯЗз;
      float var8 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, 11);
      float var9 = Math.max(0.0F, var7 - var6 - var8 - ЯЗц);
      String var10 = StaffNameText.I(ClientFonts.getMediumFont(), var2, var9, 11);
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var1, var6, var3, 11, var4);
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var10, var7 - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var10, 11), var3, 11, var5);
   }

   public HudSettingsPanel(String var1, BooleanSetting var2, FloatSetting var3, SingleChoiceSetting var4, BooleanSetting var5, String var6, SingleChoiceSetting var7, BooleanSetting var8, String var9) {
      this.с1 = ЯУь;
      this.л = new FloatAnimation(0.0F, ЯУж, Easings.CUBIC_OUT);
      this.э = new FloatAnimation(0.0F, ЯУ弟, Easings.CUBIC_IN_OUT);
      this.ю = new FloatAnimation(0.0F, ЯУб, Easings.CUBIC_IN_OUT);
      this.Щ = new FloatAnimation(0.0F, ЯУЫ, Easings.CUBIC_IN_OUT);
      this.у = new FloatAnimation(0.0F, ЯУl, Easings.CUBIC_IN_OUT);
      this.field_2 = new FloatAnimation(0.0F, ЯУ8, Easings.CUBIC_OUT);
      this.ф = new FloatAnimation(0.0F, ЯУ5, Easings.CUBIC_OUT);
      this.Я = new IdentityHashMap<>();
      this.Х = new IdentityHashMap<>();
      this.сс = new EventListener<HudRenderEvent>() {
         public void onEvent(HudRenderEvent var1) {
            HudSettingsPanel.this.method_5(var1);
         }

         @Override
         public int getPriority() {
            return -100;
         }
      };
      this.ЯЮ = 1.0F;
      this.сЮ = var1;
      this.сЧ = ЯУв;
      this.field_4 = null;
      this.В_f1b4d92a = null;
      this.И = var7;
      this.ь = var4;
      this.р = var2;
      this.field_6 = var5;
      this.сЛ = var6;
      this.field__ = var8;
      this.с诶 = var9;
      this.I = var3;
      this.В_8d9078c6 = var0 -> Math.round(var0) + "";
   }

   private static int с(ThemeColorSlot var0, float var1) {
      int var2 = ThemeEditor.getColorArgb(var0);
      return ColorMath.withAlpha(var2, ColorMath.getAlpha(var2) / ЯЗд * Math.clamp(var1, 0.0F, 1.0F));
   }

   public HudSettingsPanel(String var1, BooleanSetting var2, FloatSetting var3, SingleChoiceSetting var4, BooleanSetting var5, String var6) {
      this(var1, var2, var3, var4, var5, var6, null);
   }

   private float а_f67ab10a() {
      float var1 = this.field_6 != null ? ЯУф : 0.0F;
      if (this.field__ != null) {
         var1 += ЯУи;
      }

      if (this.И != null) {
         var1 += (this.И.getValue().size() + 1) * ЯУ9;
      }

      if (this.Ш != null) {
         var1 += ЯУо;
         if (this.Ш.getValue() && this.Т != null) {
            var1 += ЯУO;
         }
      }

      return var1;
   }

   private boolean ск() {
      return this.р != null && this.I == null;
   }
}
