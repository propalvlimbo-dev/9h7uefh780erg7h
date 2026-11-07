package sg.modules.impl.render;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import settings.render.ClientResourceIds;
import settings.render.ClientLocalization;
import sg.events.impl.ArmRenderEvent;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ActionSetting;
import sg.settings.ChoiceOption;
import sg.events.impl.EatBobbingEvent;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;
import sg.events.impl.SwingDurationEvent;
import sg.settings.TextSetting;

public class SwingAnimations extends ClientModule {
   public SingleChoiceSetting сГ;
   private FloatSetting еф;
   private FloatSetting еи;
   private FloatSetting е9;
   private BooleanSetting Яф;
   private BooleanSetting Яи;
   private FloatSetting ео;
   public BooleanSetting Я9;
   public BooleanSetting Яо;
   private final ActionSetting Ъ;
   private final FloatSetting еO;
   private final FloatSetting е7;
   private final FloatSetting еР;
   private final FloatSetting е3;
   private final FloatSetting ер;
   private final FloatSetting е6;
   private final FloatSetting е_;
   private final FloatSetting еШ;
   private final FloatSetting еы;
   private final FloatSetting ей;
   private final FloatSetting еЖ;
   private final FloatSetting ем;
   private final FloatSetting еК;
   private final FloatSetting еI;
   private final FloatSetting еТ;
   private final FloatSetting еО;
   private final BooleanSetting ЯO;
   private final FloatSetting еФ;
   private final FloatSetting еЙ;
   @SerializedName("п")
   private final TextSetting п_at_d75ef7b2;
   public SingleChoiceSetting сл;
   private final EventListener<ArmRenderEvent> еъ;
   private final EventListener<SwingDurationEvent> еМ;
   private final EventListener<EatBobbingEvent> еН;
   private static final String ПЭ = "SwingAnimations";
   private static final String П必 = "Анимирует вашу руку";
   private static final String Пщ = "Свинги";
   private static final String ПГ = "Обычный";
   private static final String Пл = "Мод 1";
   private static final String Пэ = "Мод 2";
   private static final String Пю = "Мод 3";
   private static final String ПЩ = "Мод 4";
   private static final String Пу = "Мод 5";
   private static final String П2 = "Мод 6";
   private static final String Пф = "Мод 7";
   private static final String Пи = "Мод 8";
   private static final String П9 = "Мод 9";
   private static final String По = "Мод 10";
   private static final String ПO = "Мод 11";
   private static final String П7 = "Мод 12";
   private static final String ПР = "Мод 13";
   private static final String П3 = "Мод 14";
   private static final String Пр = "Custom";
   private static final String П6 = "HMI";
   private static final String П_ = "Угол";
   private static final float ПШ = 100.0F;
   private static final float Пы = 10.0F;
   private static final float Пй = 180.0F;
   private static final String ПЖ = "Размер предмета";
   private static final float Пм = 0.2F;
   private static final float ПК = 0.05F;
   private static final String ПI = "Сила наклона";
   private static final float ПТ = 5.0F;
   private static final float ПО = 10.0F;
   private static final String ПФ = "Менять скорость свинга";
   private static final String ПЙ = "Кубическое ускорение";
   private static final String Пк = "Скорость";
   private static final float Пг = 7.0F;
   private static final float П0 = 10.0F;
   private static final String Пх = "Только с аурой";
   private static final String ПУ = "Только при ударе";
   private static final String ПЗ = "Open";
   private static final String ПЕ = "Custom Start X";
   private static final float ПП = -2.0F;
   private static final String П西 = "Custom Start Y";
   private static final float Пд = 0.08F;
   private static final float ПБ = -2.0F;
   private static final String ПС = "Custom Start Z";
   private static final float Пз = -0.05F;
   private static final float Пц = -2.0F;
   private static final String Пя = "Custom Start Pitch";
   private static final float 西с = -65.0F;
   private static final float 西В = -180.0F;
   private static final float 西Я = 180.0F;
   private static final String 西Х = "Custom Start Yaw";
   private static final float 西е = 24.0F;
   private static final float 西А = -180.0F;
   private static final float 西4 = 180.0F;
   private static final String 西п = "Custom Start Roll";
   private static final float 西Д = -4.0F;
   private static final float 西Ъ = -180.0F;
   private static final float 西т = 180.0F;
   private static final String 西а = "Custom End X";
   private static final float 西Ю = 0.04F;
   private static final float 西Ч = -2.0F;
   private static final String 西Л = "Custom End Y";
   private static final float 西诶 = -0.92F;
   private static final float 西1 = -2.0F;
   private static final String 西Ь = "Custom End Z";
   private static final float 西衣 = -0.32F;
   private static final float 西ш = -2.0F;
   private static final String 西И = "Custom End Pitch";
   private static final float 西ь = -142.0F;
   private static final float 西ж = -180.0F;
   private static final float 西弟 = 180.0F;
   private static final String 西б = "Custom End Yaw";
   private static final float 西Ы = 34.0F;
   private static final float 西l = -180.0F;
   private static final float 西8 = 180.0F;
   private static final String 西5 = "Custom End Roll";
   private static final float 西в = -16.0F;
   private static final float 西ъ = -180.0F;
   private static final float 西М = 180.0F;
   private static final String 西Н = "Custom Curve 1 X";
   private static final float 西н = 0.18F;
   private static final String 西Ц = "Custom Curve 1 Y";
   private static final float 西ч = 0.02F;
   private static final String 西Э = "Custom Curve 2 X";
   private static final float 西必 = 0.58F;
   private static final String 西щ = "Custom Curve 2 Y";
   private static final String 西Г = "Custom Back";
   private static final String 西л = "Custom Back Speed";
   private static final float 西э = 8.0F;
   private static final float 西ю = 10.0F;
   private static final String 西Щ = "Custom Preset";
   private static final float 西у = 2.1474836E9F;
   private static final String 西2 = "Custom Preset Data";
   private static final String 西ф = "Анимация использования";
   private static final String 西и = "Обычная";
   private static final String 西9 = "Укороченная";
   private static final String 西о = "Полная отмена";
   private static final String 西O = "Обычная";
   private static final String 西7 = "Укороченная";
   private static final String 西Р = "Полная отмена";
   private static final float 西3 = 0.02F;
   private static final String 西р = "HMI";
   private static final double 西6 = Math.PI;
   private static final String 西_ = "Мод 13";
   private static final String 西Ш = "Мод 14";
   private static final String 西ы = "Custom";
   private static final String 西й = "Мод 1";
   private static final String 西Ж = "Мод 2";
   private static final String 西м = "Мод 3";
   private static final String 西К = "Мод 4";
   private static final String 西I = "Мод 5";
   private static final String 西Т = "Мод 12";
   private static final String 西О = "Мод 6";
   private static final String 西Ф = "Мод 7";
   private static final String 西Й = "Мод 8";
   private static final String 西к = "Мод 9";
   private static final String 西г = "Мод 10";
   private static final String 西0 = "Мод 11";
   private static final float 西х = 10.0F;
   private static final float 西У = 5.0F;
   private static final double 西З = 0.1;
   private static final float 西Е = 360.0F;
   private static final double 西П = -0.1;
   private static final float 西西 = 45.0F;
   private static final float 西д = (float) Math.PI;
   private static final float 西Б = 4.0F;
   private static final float 西С = -20.0F;
   private static final float 西з = (float) Math.PI;
   private static final float 西ц = -20.0F;
   private static final float 西я = (float) Math.PI;
   private static final float дс = 10.0F;
   private static final float дВ = (float) Math.PI;
   private static final float дЯ = 10.0F;
   private static final double дХ = 0.15;
   private static final double де = -0.3F;
   private static final float дА = -90.0F;
   private static final float д4 = 90.0F;
   private static final float дп = 60.0F;
   private static final float дД = -60.0F;
   private static final float дЪ = 10.0F;
   private static final double дт = 0.15;
   private static final double да = -0.3F;
   private static final float дЮ = -70.0F;
   private static final float дЧ = 70.0F;
   private static final float дЛ = 10.0F;
   private static final float д诶 = -10.0F;
   private static final float д1 = 10.0F;
   private static final double дЬ = 0.15;
   private static final double д衣 = -0.3F;
   private static final float дш = -70.0F;
   private static final float дИ = 70.0F;
   private static final float дь = 10.0F;
   private static final float дж = -10.0F;
   private static final float д弟 = -70.0F;
   private static final float дб = 10.0F;
   private static final float дЫ = -1.0F;
   private static final float дl = (float) Math.PI;
   private static final float д8 = 0.56F;
   private static final float д5 = 0.72F;
   private static final float дв = 0.72F;
   private static final float дъ = 0.56F;
   private static final float дМ = -0.52F;
   private static final float дН = 24.0F;
   private static final float дн = -0.72F;
   private static final float дЦ = 90.0F;
   private static final float дч = -30.0F;
   private static final float дЭ = -90.0F;
   private static final double д必 = 0.15;
   private static final double дщ = -0.3F;
   private static final float дГ = -70.0F;
   private static final float дл = 20.0F;
   private static final float дэ = 70.0F;
   private static final float дю = 20.0F;
   private static final float дЩ = 10.0F;
   private static final float ду = 60.0F;
   private static final float д2 = -10.0F;
   private static final float дф = 60.0F;
   private static final float ди = -100.0F;
   private static final float д9 = 10.0F;
   private static final double до = 0.15;
   private static final double дO = -0.3F;
   private static final float д7 = -90.0F;
   private static final float дР = 90.0F;
   private static final float д3 = 60.0F;
   private static final float др = -60.0F;
   private static final float д6 = 10.0F;
   private static final float д_ = -50.0F;
   private static final float дШ = 0.5F;
   private static final float ды = -0.15F;
   private static final float дй = -0.65F;
   private static final float дЖ = 45.0F;
   private static final float дм = (float) Math.PI;
   private static final float дК = (float) Math.PI;
   private static final float дI = -20.0F;
   private static final float дТ = -20.0F;
   private static final float дО = 10.0F;
   private static final float дФ = -0.5F;
   private static final float дЙ = 0.2F;
   private static final float дк = -80.0F;
   private static final float дг = 45.0F;
   private static final float д0 = -20.0F;
   private static final float дх = -20.0F;
   private static final float дУ = -80.0F;
   private static final float дЗ = 0.4F;
   private static final float дЕ = 0.2F;
   private static final float дП = 0.2F;
   private static final float д西 = -0.5F;
   private static final float дд = 0.08F;
   private static final float дБ = 20.0F;
   private static final float дС = -80.0F;
   private static final float дз = 20.0F;
   private static final double дц = 0.5;
   private static final double дя = -0.15F;
   private static final double Бс = -0.65F;
   private static final float БВ = -0.2F;
   private static final float БЯ = (float) Math.PI;
   private static final float БХ = 45.0F;
   private static final float Бе = (float) Math.PI;
   private static final float БА = -20.0F;
   private static final float Б4 = -20.0F;
   private static final float Бп = -80.0F;
   private static final float БД = 30.0F;
   private static final float БЪ = -80.0F;
   private static final float Бт = 60.0F;
   private static final long Ба = 255L;
   private static final long БЮ = 255L;
   private static final long БЧ = 127L;
   private static final long БЛ = 255L;
   private static final long Б诶 = 255L;
   private static final long Б1 = 255L;
   private static final long БЬ = 2L;
   private static final double Б衣 = 0.5;
   private static final float Бш = 90.0F;
   private static final float БИ = -90.0F;
   private static final float Бь = -10.0F;
   private static final float Бж = 10.0F;
   private static final float Б弟 = 10.0F;
   private static final float Бб = 110.0F;
   private static final float БЫ = 10.0F;
   private static final float Бl = 0.8F;
   private static final float Б8 = 1.25F;
   private static final float Б5 = 0.8F;
   private static final float Бв = 5.0F;
   private static final String Бъ = "Обычный";
   private static final float БМ = 9.0F;
   private static final float БН = 0.45F;
   private static final float Бн = 0.78F;
   private static final float БЦ = 0.5F;
   private static final float Бч = 0.5F;
   private static final float БЭ = 3.0F;
   private static final float Б必 = 3.0F;
   private static final String Бщ = "HMI";
   private static final String БГ = "Custom";
   private static final String Бл = "HMI";
   private static final String Бэ = "HMI";
   private static final String Бю = "HMI";
   private static final String БЩ = "Custom";
   private static final String Бу = "HMI";
   private static final String Б2 = "Custom";
   private static final String Бф = "Мод 3";
   private static final String Би = "Мод 4";

   public SwingAnimations() {
      super(ПЭ, П必, ModuleCategory.RENDER);
      this.сГ = new SingleChoiceSetting(
         Пщ,
         new ChoiceOption(ПГ),
         new ChoiceOption(Пл),
         new ChoiceOption(Пэ),
         new ChoiceOption(Пю),
         new ChoiceOption(ПЩ),
         new ChoiceOption(Пу),
         new ChoiceOption(П2),
         new ChoiceOption(Пф),
         new ChoiceOption(Пи),
         new ChoiceOption(П9),
         new ChoiceOption(По),
         new ChoiceOption(ПO),
         new ChoiceOption(П7),
         new ChoiceOption(ПР),
         new ChoiceOption(П3),
         new ChoiceOption(Пр),
         new ChoiceOption(П6)
      );
      this.еф = new FloatSetting(П_, ПШ, Пы, Пй).visibleWhen(() -> this.сГ.isSelected(Бф) || this.сГ.isSelected(Би)).withStep(1.0F);
      this.еи = new FloatSetting(ПЖ, 1.0F, Пм, 2.0F).withStep(ПК);
      this.е9 = new FloatSetting(ПI, ПТ, 1.0F, ПО).visibleWhen(() -> !this.сГ.isSelected(Бу) && !this.сГ.isSelected(Б2)).withStep(1.0F);
      this.Яф = new BooleanSetting(ПФ, false);
      this.Яи = new BooleanSetting(ПЙ, false).visibleWhen(() -> !this.сГ.isSelected(Бю) && !this.сГ.isSelected(БЩ));
      this.ео = new FloatSetting(Пк, Пг, 0.0F, П0).withStep(1.0F);
      this.Я9 = new BooleanSetting(Пх, true).visibleWhen(() -> !this.сГ.isSelected(Бэ));
      this.Яо = new BooleanSetting(ПУ, false).visibleWhen(() -> !this.сГ.isSelected(Бл));
      this.Ъ = new ActionSetting(ПЗ).onClick(this::Д弟).showOtherSettings().visibleWhen(() -> this.сГ.isSelected(БГ));
      this.еO = method_2(ПЕ, 0.0F, ПП, 2.0F);
      this.е7 = method_2(П西, Пд, ПБ, 2.0F);
      this.еР = method_2(ПС, Пз, Пц, 2.0F);
      this.е3 = method_2(Пя, 西с, 西В, 西Я);
      this.ер = method_2(西Х, 西е, 西А, 西4);
      this.е6 = method_2(西п, 西Д, 西Ъ, 西т);
      this.е_ = method_2(西а, 西Ю, 西Ч, 2.0F);
      this.еШ = method_2(西Л, 西诶, 西1, 2.0F);
      this.еы = method_2(西Ь, 西衣, 西ш, 2.0F);
      this.ей = method_2(西И, 西ь, 西ж, 西弟);
      this.еЖ = method_2(西б, 西Ы, 西l, 西8);
      this.ем = method_2(西5, 西в, 西ъ, 西М);
      this.еК = method_2(西Н, 西н, 0.0F, 1.0F);
      this.еI = method_2(西Ц, 西ч, 0.0F, 1.0F);
      this.еТ = method_2(西Э, 西必, 0.0F, 1.0F);
      this.еО = method_2(西щ, 1.0F, 0.0F, 1.0F);
      this.ЯO = new BooleanSetting(西Г, true).visibleWhen(() -> false);
      this.еФ = method_2(西л, 西э, 1.0F, 西ю);
      this.еЙ = method_2(西Щ, 1.0F, 0.0F, 西у);
      this.п_at_d75ef7b2 = new TextSetting(西2, "").visibleWhen(() -> false);
      this.сл = new SingleChoiceSetting(西ф, new ChoiceOption(西и), new ChoiceOption(西9), new ChoiceOption(西о));
      this.еъ = this::method_2;
      this.еМ = this::method_2;
      this.еН = this::method_2;
      this.addSettings(
         this.сГ,
         this.Ъ,
         this.еф,
         this.еи,
         this.е9,
         this.Яф,
         this.Яи,
         this.ео,
         this.Я9,
         this.Яо,
         this.сл,
         this.еO,
         this.е7,
         this.еР,
         this.е3,
         this.ер,
         this.е6,
         this.е_,
         this.еШ,
         this.еы,
         this.ей,
         this.еЖ,
         this.ем,
         this.еК,
         this.еI,
         this.еТ,
         this.еО,
         this.ЯO,
         this.еФ,
         this.еЙ,
         this.п_at_d75ef7b2
      );
   }

   public static boolean Ъ(Screen var0) {
      return var0 instanceof SwingAnimations.EditorScreen;
   }

   public void method_2(ArmRenderEvent var1) {
      if (!this.сГ.isSelected(西р)) {
         if (this.Чг()) {
            int var2 = var1.arm == HumanoidArm.RIGHT ? 1 : -1;
            float var3 = Mth.clamp(var1.swingProgress, 0.0F, 1.0F);
            float var4 = this.Яи.getValue() ? И西(var3) : var3;
            float var5 = (float)Math.sin(var4 * 西6);
            if (!this.Я9.getValue()) {
               var1.poses.scale(this.еи.getValue(), this.еи.getValue(), this.еи.getValue());
               if (var1.arm == MINECRAFT_CLIENT.player.getMainArm()) {
                  switch (this.сГ.getSelectedOption().getName()) {
                     case 西_:
                        var1.poses.translate(0.0F, 0.0F, -var5 * this.е9.getValue() / 西х);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var2 * var5 * this.е9.getValue() * 西У));
                        break;
                     case 西Ш:
                        var1.poses.translate(0.0, 西З, 0.0);
                        var1.poses.mulPose(Axis.XP.rotationDegrees(var4 * 西Е));
                        var1.poses.translate(0.0, 西П, 0.0);
                        break;
                     case 西ы:
                        this.method_2(var1, var2, var3);
                        break;
                     case 西й:
                        float var16 = Mth.sqrt(var4);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var2 * (西西 + Mth.sin(var4 * var4 * 西д) / 西Б * 西С)));
                        var1.poses.mulPose(Axis.ZP.rotationDegrees(var2 * Mth.sin(var16 * 西з) * 西ц));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(Mth.sin(var16 * 西я) * -this.е9.getValue() * дс));
                        var1.poses.mulPose(Axis.YP.rotationDegrees(-var2 * 45));
                        break;
                     case 西Ж:
                        var1.poses.mulPose(Axis.XP.rotationDegrees(var2 * (var4 * дВ - this.е9.getValue() * дЯ * var5)));
                        break;
                     case 西м:
                        var1.poses.translate(0.0, дХ, де);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var1.arm == HumanoidArm.LEFT ? дА : д4));
                        var1.poses.mulPose(Axis.ZP.rotationDegrees(var1.arm == HumanoidArm.LEFT ? дп : дД));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(-this.еф.getValue() - this.е9.getValue() * дЪ * var5));
                        break;
                     case 西К:
                        var1.poses.translate(0.0, дт, да);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var1.arm == HumanoidArm.LEFT ? дЮ : дЧ));
                        var1.poses.mulPose(Axis.ZP.rotationDegrees(var1.arm == HumanoidArm.LEFT ? дЛ : д诶));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(-this.еф.getValue() - this.е9.getValue() * д1 * var5));
                        break;
                     case 西I:
                        var1.poses.translate(0.0, дЬ, д衣);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var1.arm == HumanoidArm.LEFT ? дш : дИ));
                        var1.poses.mulPose(Axis.ZP.rotationDegrees(var1.arm == HumanoidArm.LEFT ? дь : дж));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(д弟 - this.е9.getValue() * дб * var5));
                        break;
                     case 西Т:
                        float var15 = var1.arm == HumanoidArm.LEFT ? дЫ : 1.0F;
                        float var19 = Mth.sin(Mth.sqrt(var4) * дl);
                        var1.poses.translate(-var15 * д8, д5, дв);
                        var1.poses.translate(дъ * var15, дМ - var19 * this.е9.getValue() / дН, дн);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(дЦ * var15));
                        var1.poses.mulPose(Axis.ZP.rotationDegrees(дч * var15));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(дЭ));
                        break;
                     case 西О:
                        var1.poses.translate(0.0, д必, дщ);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var1.arm == HumanoidArm.LEFT ? дГ - дл * var5 : дэ + дю * var5));
                        var1.poses.mulPose(Axis.ZP.rotationDegrees(var1.arm == HumanoidArm.LEFT ? дЩ + ду * var5 : д2 - дф * var5));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(ди - this.е9.getValue() * д9 * var5));
                        break;
                     case 西Ф:
                        var1.poses.translate(0.0, до, дO);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var1.arm == HumanoidArm.LEFT ? д7 : дР));
                        var1.poses.mulPose(Axis.ZP.rotationDegrees(var1.arm == HumanoidArm.LEFT ? д3 : др - this.е9.getValue() * д6 * var5));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(д_));
                        break;
                     case 西Й:
                        var1.poses.translate(дШ, ды, дй);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(дЖ));
                        float var14 = Mth.sin(var4 * var4 * дм);
                        float var18 = Mth.sin(Mth.sqrt(var4) * дК);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var14 * дI));
                        var1.poses.mulPose(Axis.ZP.rotationDegrees(var18 * дТ));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(var18 * (-this.е9.getValue() * дО)));
                        var1.poses.translate(дФ, дЙ, 0.0F);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var1.arm == HumanoidArm.LEFT ? -60 : 30));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(дк));
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var1.arm == HumanoidArm.LEFT ? -60 : 60));
                        break;
                     case 西к:
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var2 * дг));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(var5 * д0));
                        var1.poses.mulPose(Axis.ZP.rotationDegrees(var2 * var5 * дх));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(var5 * дУ));
                        var1.poses.translate(var2 * дЗ, дЕ, дП);
                        var1.poses.translate(var2 * д西, дд, 0.0F);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var2 * дБ));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(дС));
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var2 * дз));
                        break;
                     case 西г:
                        var1.poses.translate(дц, дя, Бс);
                        float var13 = БВ * Mth.sin(var4 * БЯ);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(БХ));
                        float var17 = Mth.sin(0.0);
                        float var10 = Mth.sin(Mth.sqrt(0.0F) * Бе);
                        var1.poses.mulPose(Axis.YP.rotationDegrees(var17 * БА));
                        var1.poses.mulPose(Axis.ZP.rotationDegrees(var10 * Б4));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(var10 * Бп));
                        var1.poses.mulPose(Axis.YP.rotationDegrees(БД));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(БЪ));
                        var1.poses.mulPose(Axis.YP.rotationDegrees(Бт));
                        int var11 = (int)Math.min(
                           Ба,
                           (System.currentTimeMillis() % БЮ > БЧ ? Math.abs(Math.abs(System.currentTimeMillis()) % БЛ - Б诶) : System.currentTimeMillis() % Б1)
                              * БЬ
                        );
                        float var12 = var13 > Б衣 ? 1.0F - var13 : var13;
                        var1.poses.mulPose(Axis.ZP.rotationDegrees(0.0F));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(Бш));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(БИ));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(Бь));
                        var1.poses.mulPose(Axis.XP.rotationDegrees(-var12 * Бж));
                        var1.poses.mulPose(Axis.YP.rotationDegrees(Б弟));
                        var1.poses.mulPose(Axis.ZP.rotationDegrees(MINECRAFT_CLIENT.player.swinging ? -var11 / (Бб - this.е9.getValue() * БЫ) : 1.0F));
                        break;
                     case 西0:
                        float var8;
                        if (var4 < Бl) {
                           var8 = -var4 * Б8;
                        } else {
                           float var9 = (var4 - Б5) * Бв;
                           var8 = -(1.0F - var9);
                        }

                        var1.poses.translate(0.0F, var8, 0.0F);
                  }

                  if (!this.сГ.isSelected(Бъ)) {
                     var1.setCancelled(true);
                  }
               }
            }
         }
      }
   }

   public float Ъа() {
      return this.еи.getValue();
   }

   private float И4(float var1) {
      float var2 = 0.0F;
      float var3 = 1.0F;

      for (int var4 = 0; var4 < 12; var4++) {
         float var5 = (var2 + var3) * БЦ;
         if (Ф(var5, 0.0F, this.еК.getValue(), this.еТ.getValue(), 1.0F) < var1) {
            var2 = var5;
         } else {
            var3 = var5;
         }
      }

      return Math.clamp(Ф((var2 + var3) * Бч, 0.0F, this.еI.getValue(), this.еО.getValue(), 1.0F), 0.0F, 1.0F);
   }

   private static float И西(float var0) {
      var0 = Mth.clamp(var0, 0.0F, 1.0F);
      float var1 = 1.0F - var0;
      return 1.0F - var1 * var1 * var1;
   }

   private float ИХ(float var1) {
      if (this.ЯO.getValue()) {
         float var2 = Mth.lerp((this.еФ.getValue() - 1.0F) / БМ, БН, Бн);
         if (var1 <= var2) {
            return this.И4(Mth.clamp(var1 / var2, 0.0F, 1.0F));
         }

         float var3 = Mth.clamp((var1 - var2) / (1.0F - var2), 0.0F, 1.0F);
         float var4 = 1.0F - var3;
         return var4 * var4 * var4;
      } else {
         return this.И4(Mth.clamp(var1, 0.0F, 1.0F));
      }
   }

   private void method_2(ArmRenderEvent var1, int var2, float var3) {
      float var4 = this.ИХ(var3);
      float var5 = Mth.lerp(var4, this.еO.getValue(), this.е_.getValue()) * var2;
      float var6 = Mth.lerp(var4, this.е7.getValue(), this.еШ.getValue());
      float var7 = Mth.lerp(var4, this.еР.getValue(), this.еы.getValue());
      float var8 = Mth.lerp(var4, this.е3.getValue(), this.ей.getValue());
      float var9 = Mth.lerp(var4, this.ер.getValue(), this.еЖ.getValue()) * var2;
      float var10 = Mth.lerp(var4, this.е6.getValue(), this.ем.getValue()) * var2;
      var1.poses.translate(var5, var6, var7);
      var1.poses.mulPose(Axis.YP.rotationDegrees(var9));
      var1.poses.mulPose(Axis.ZP.rotationDegrees(var10));
      var1.poses.mulPose(Axis.XP.rotationDegrees(var8));
   }

   private static FloatSetting method_2(String var0, float var1, float var2, float var3) {
      return new FloatSetting(var0, var1, var2, var3).visibleWhen(() -> false);
   }

   public void method_2(SwingDurationEvent var1) {
      if (!this.сГ.isSelected(Бщ)) {
         if (this.Чг()) {
            if (!this.Я9.getValue()) {
               if (var1.getHand() == InteractionHand.MAIN_HAND && this.Яф.getValue()) {
                  var1.setDurationControlValue(this.ео.getValue().intValue());
                  var1.setCancelled(true);
               }
            }
         }
      }
   }

   public void method_2(EatBobbingEvent var1) {
      if (!this.сл.isSelected(西O)) {
         switch (this.сл.getSelectedOption().getName()) {
            case 西7:
               var1.setBobbingScale(西3);
               break;
            case 西Р:
               var1.setBobbingScale(0.0F);
         }
      }
   }

   private void Д弟() {
      Screen var1 = MINECRAFT_CLIENT.gui.screen();
      MINECRAFT_CLIENT.gui.setScreen(new SwingAnimations.EditorScreen(this, var1));
   }

   public boolean Чг() {
      return !this.Яо.getValue() || MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.swinging;
   }

   private static float Ф(float var0, float var1, float var2, float var3, float var4) {
      float var5 = 1.0F - var0;
      return var5 * var5 * var5 * var1 + БЭ * var5 * var5 * var0 * var2 + Б必 * var5 * var0 * var0 * var3 + var0 * var0 * var0 * var4;
   }

   private static final class EditorScreen extends Screen {
      private static final Gson В_26ee8fae = new Gson();
      private static int В9 = ColorMath.packArgb(15, 19, 28, SwingAnimations.EditorScreen.ЯИБ);
      private static int Во = ColorMath.packArgb(15, 19, 28, SwingAnimations.EditorScreen.ЯИС);
      private static int ВO = ColorMath.packArgb(255, 255, 255, SwingAnimations.EditorScreen.ЯИз);
      private static int В7 = ColorMath.packArgb(244, 244, 247, SwingAnimations.EditorScreen.ЯИц);
      private static int ВР = ColorMath.packArgb(116, 117, 126, SwingAnimations.EditorScreen.ЯИя);
      private static int В3 = ColorMath.packArgb(246, 145, 255, SwingAnimations.EditorScreen.Яьс);
      private static final float ЯГ = 220.0F;
      private static final float Ял = 303.0F;
      private static final float Яэ = 250.0F;
      private static final float Яю = 352.0F;
      private static final float ЯЩ = 346.0F;
      private static final float Яу = 328.0F;
      private static final float Я2 = 16.0F;
      private static final float Яф = 848.0F;
      private static final Identifier ж = ClientResourceIds.getImageId(SwingAnimations.EditorScreen.ЯьВ);
      private static final Identifier 弟 = ClientResourceIds.getImageId(SwingAnimations.EditorScreen.ЯьЯ);
      private static final Identifier б = ClientResourceIds.getImageId(SwingAnimations.EditorScreen.ЯьХ);
      private static final Identifier Ы = ClientResourceIds.getImageId(SwingAnimations.EditorScreen.Яье);
      private static final Identifier l = ClientResourceIds.getImageId(SwingAnimations.EditorScreen.ЯьА);
      private static final Identifier field_8 = ClientResourceIds.getImageId(SwingAnimations.EditorScreen.Яь4);
      private static final Identifier field_5 = ClientResourceIds.getImageId(SwingAnimations.EditorScreen.Яьп);
      @SerializedName("В")
      private final SwingAnimations В_bbefb8af;
      private final Screen е;
      @SerializedName("7")
      private final FloatAnimation field_7;
      private final FloatAnimation Р;
      @SerializedName("3")
      private final FloatAnimation field_3;
      private final FloatAnimation р;
      private final List<SwingAnimations.EditorScreen.Preset> Щ;
      private final List<FloatAnimation> у;
      @SerializedName("2")
      private final List<FloatAnimation> field_2;
      @SerializedName("В")
      private SwingAnimations.EditorScreen.EditorTab В_8b72c019;
      private float Яи;
      private float Я9;
      private float Яо;
      private float ЯO;
      private int Вр;
      @SerializedName("В")
      private SwingAnimations.EditorScreen.TransformAxis В_238df2f2;
      private boolean сO;
      private String с弟;
      private float Я7;
      private float ЯР;
      private float Я3;
      private float Яр;
      private float Я6;
      private float Я_;
      private float ЯШ;
      private float Яы;
      private static final String Я诶ф = "Custom Swing";
      private static final float Я诶и = 7.0F;
      private static final float Я诶9 = 9.0F;
      private static final float Я诶о = 10.0F;
      private static final float Я诶O = 12.0F;
      private static final float Я诶7 = 11.0F;
      private static final float Я诶Р = 9.0F;
      private static final float Я诶3 = 848.0F;
      private static final float Я诶р = 0.5F;
      private static final float Я诶6 = 352.0F;
      private static final float Я诶_ = 0.5F;
      private static final float Я诶Ш = 12.0F;
      private static final float Я诶ы = 255.0F;
      private static final float Я诶й = 220.0F;
      private static final float Я诶Ж = 303.0F;
      private static final float Я诶м = 250.0F;
      private static final float Я诶К = 352.0F;
      private static final float Я诶I = 346.0F;
      private static final float Я诶Т = 328.0F;
      private static final float Я诶О = 9.0F;
      private static final float Я诶Ф = 220.0F;
      private static final float Я诶Й = 303.0F;
      private static final float Я诶к = 250.0F;
      private static final float Я诶г = 352.0F;
      private static final float Я诶0 = 346.0F;
      private static final float Я诶х = 328.0F;
      private static final float Я诶У = 255.0F;
      private static final float Я诶З = 255.0F;
      private static final float Я诶Е = 0.5F;
      private static final float Я诶П = 0.08F;
      private static final float Я诶西 = 40.0F;
      private static final float Я诶д = 30.0F;
      private static final float Я诶Б = 20.0F;
      private static final double Я诶С = 255.0;
      private static final float Я诶з = 20.0F;
      private static final float Я诶ц = 220.0F;
      private static final float Я诶я = 16.0F;
      private static final float Я1с = 250.0F;
      private static final float Я1В = 16.0F;
      private static final String Я1Я = "Presets";
      private static final float Я1Х = 15.0F;
      private static final float Я1е = 14.0F;
      private static final float Я1А = 44.0F;
      private static final float Я14 = 220.0F;
      private static final float Я1п = 259.0F;
      private static final float Я1Д = 20.0F;
      private static final float Я1Ъ = 220.0F;
      private static final float Я1т = 259.0F;
      private static final float Я1а = 0.5F;
      private static final float Я1Ю = 20.0F;
      private static final float Я1Ч = 48.0F;
      private static final float Я1Л = 220.0F;
      private static final float Я1诶 = 197.0F;
      private static final float Я11 = 52.0F;
      private static final float Я1Ь = 8.0F;
      private static final float Я1衣 = 245.0F;
      private static final float Я1ш = 6.0F;
      private static final float Я1И = 16.0F;
      private static final float Я1ь = 257.0F;
      private static final float Я1ж = 154.0F;
      private static final float Я1弟 = 30.0F;
      private static final float Я1б = 10.0F;
      private static final float Я1Ы = 154.0F;
      private static final float Я1l = 30.0F;
      private static final float Я18 = 0.5F;
      private static final float Я15 = 10.0F;
      private static final String Я1в = "New CFG name...";
      private static final float Я1ъ = 8.0F;
      private static final float Я1М = 10.0F;
      private static final long Я1Н = 450L;
      private static final long Я1н = 2L;
      private static final float Я1Ц = 8.0F;
      private static final float Я1ч = 8.0F;
      private static final float Я1Э = 14.0F;
      private static final float Я1必 = 0.5F;
      private static final float Я1щ = 173.0F;
      private static final float Я1Г = 256.0F;
      private static final float Я1л = 30.0F;
      private static final float Я1э = 30.0F;
      private static final float Я1ю = 10.0F;
      private static final float Я1Щ = 173.0F;
      private static final float Я1у = 256.0F;
      private static final float Я12 = 30.0F;
      private static final float Я1ф = 30.0F;
      private static final float Я1и = 0.5F;
      private static final float Я19 = 10.0F;
      private static final float Я1о = 183.0F;
      private static final float Я1O = 266.0F;
      private static final float Я17 = 10.0F;
      private static final float Я1Р = 10.0F;
      private static final float Я13 = 208.0F;
      private static final float Я1р = 36.0F;
      private static final float Я16 = 0.12F;
      private static final float Я1_ = 208.0F;
      private static final float Я1Ш = 36.0F;
      private static final float Я1ы = 6.0F;
      private static final float Я1й = 0.1F;
      private static final float Я1Ж = 208.0F;
      private static final float Я1м = 36.0F;
      private static final float Я1К = 0.5F;
      private static final float Я1I = 6.0F;
      private static final float Я1Т = 0.72F;
      private static final float Я1О = 0.4F;
      private static final float Я1Ф = 7.0F;
      private static final float Я1Й = 10.0F;
      private static final float Я1к = 153.0F;
      private static final float Я1г = 9.0F;
      private static final float Я10 = 48.0F;
      private static final float Я1х = 18.0F;
      private static final float Я1У = 4.0F;
      private static final float Я1З = 9.0F;
      private static final float Я1Е = 48.0F;
      private static final float Я1П = 18.0F;
      private static final float Я1西 = 0.5F;
      private static final float Я1д = 4.0F;
      private static final String Я1Б = "Save";
      private static final String Я1С = "Load";
      private static final float Я1з = 48.0F;
      private static final float Я1ц = 0.5F;
      private static final float Я1я = 12.0F;
      private static final String ЯЬс = "Main";
      private static final float ЯЬВ = 18.0F;
      private static final float ЯЬЯ = 14.0F;
      private static final String ЯЬХ = "Тайминг атаки — перетаскивай обе точки.";
      private static final float ЯЬе = 17.0F;
      private static final float ЯЬА = 46.0F;
      private static final float ЯЬ4 = 16.0F;
      private static final float ЯЬп = 65.0F;
      private static final float ЯЬД = 218.0F;
      private static final float ЯЬЪ = 218.0F;
      private static final float ЯЬт = 20.0F;
      private static final float ЯЬа = 16.0F;
      private static final float ЯЬЮ = 65.0F;
      private static final float ЯЬЧ = 218.0F;
      private static final float ЯЬЛ = 218.0F;
      private static final float ЯЬ诶 = 0.5F;
      private static final float ЯЬ1 = 20.0F;
      private static final float ЯЬЬ = 60.0F;
      private static final float ЯЬ衣 = 109.0F;
      private static final float ЯЬш = 130.0F;
      private static final float ЯЬИ = 130.0F;
      private static final float ЯЬь = 12.0F;
      private static final float ЯЬж = 77.0F;
      private static final float ЯЬ弟 = 123.0F;
      private static final float ЯЬб = 104.0F;
      private static final float ЯЬЫ = 103.0F;
      private static final String ЯЬl = "Плавный возврат назад";
      private static final float ЯЬ8 = 16.0F;
      private static final float ЯЬ5 = 292.0F;
      private static final float ЯЬв = 213.0F;
      private static final float ЯЬъ = 293.0F;
      private static final String ЯЬМ = "Скорость возрата";
      private static final float ЯЬН = 16.0F;
      private static final float ЯЬн = 315.0F;
      private static final float ЯЬЦ = 234.0F;
      private static final float ЯЬч = 315.0F;
      private static final float ЯЬЭ = 16.0F;
      private static final float ЯЬ必 = 332.0F;
      private static final float ЯЬщ = 218.0F;
      private static final float ЯЬГ = 4.0F;
      private static final float ЯЬл = 0.1F;
      private static final float ЯЬэ = 0.16F;
      private static final float ЯЬю = 9.0F;
      private static final float ЯЬЩ = 218.0F;
      private static final float ЯЬу = 4.0F;
      private static final float ЯЬ2 = 3.0F;
      private static final float ЯЬф = 218.0F;
      private static final float ЯЬи = 5.0F;
      private static final float ЯЬ9 = 3.0F;
      private static final float ЯЬо = 10.0F;
      private static final float ЯЬO = 10.0F;
      private static final float ЯЬ7 = 5.0F;
      private static final float ЯЬР = 256.0F;
      private static final float ЯЬ3 = 4.0F;
      private static final float ЯЬр = 4.0F;
      private static final float ЯЬ6 = 4.0F;
      private static final float ЯЬ_ = 4.0F;
      private static final float ЯЬШ = 20.0F;
      private static final float ЯЬы = 0.5F;
      private static final float ЯЬй = 0.5F;
      private static final float ЯЬЖ = 0.5F;
      private static final float ЯЬм = 0.42F;
      private static final float ЯЬК = 4.0F;
      private static final float ЯЬI = 4.0F;
      private static final float ЯЬТ = 8.0F;
      private static final float ЯЬО = 8.0F;
      private static final float ЯЬФ = 4.0F;
      private static final String ЯЬЙ = "Editor";
      private static final float ЯЬк = 18.0F;
      private static final float ЯЬг = 14.0F;
      private static final float ЯЬ0 = 43.0F;
      private static final float ЯЬх = 16.0F;
      private static final float ЯЬУ = 99.0F;
      private static final float ЯЬЗ = 123.0F;
      private static final float ЯЬЕ = 99.0F;
      private static final float ЯЬП = 230.0F;
      private static final float ЯЬ西 = 99.0F;
      private static final float ЯЬд = 16.0F;
      private static final float ЯЬБ = 82.0F;
      private static final float ЯЬС = 314.0F;
      private static final float ЯЬз = 222.0F;
      private static final float ЯЬц = 16.0F;
      private static final float ЯЬя = 16.0F;
      private static final float Я衣с = 82.0F;
      private static final float Я衣В = 314.0F;
      private static final float Я衣Я = 222.0F;
      private static final float Я衣Х = 0.5F;
      private static final float Я衣е = 16.0F;
      private static final float Я衣А = 16.0F;
      private static final float Я衣4 = 82.0F;
      private static final float Я衣п = 314.0F;
      private static final float Я衣Д = 174.0F;
      private static final float Я衣Ъ = 173.0F;
      private static final float Я衣т = 174.0F;
      private static final float Я衣а = 173.0F;
      private static final float Я衣Ю = 64.0F;
      private static final float Я衣Ч = 174.0F;
      private static final float Я衣Л = 64.0F;
      private static final String Я衣诶 = "Select Start or End to edit";
      private static final float Я衣1 = 173.0F;
      private static final float Я衣Ь = 0.5F;
      private static final float Я衣衣 = 283.0F;
      private static final float Я衣ш = 91.0F;
      private static final float Я衣И = 268.0F;
      private static final float Я衣ь = 164.0F;
      private static final float Я衣ж = 24.0F;
      private static final float Я衣弟 = 24.0F;
      private static final float Я衣б = 8.0F;
      private static final float Я衣Ы = 0.16F;
      private static final float Я衣l = 24.0F;
      private static final float Я衣8 = 0.7F;
      private static final float Я衣5 = 8.0F;
      private static final float Я衣в = 0.78F;
      private static final String Я衣ъ = "Copy Start → End";
      private static final float Я衣М = 0.5F;
      private static final float Я衣Н = 6.0F;
      private static final float Я衣н = 0.82F;
      private static final float Я衣Ц = 30.0F;
      private static final float Я衣ч = 30.0F;
      private static final float Я衣Э = 10.0F;
      private static final float Я衣必 = 0.1F;
      private static final float Я衣щ = 30.0F;
      private static final float Я衣Г = 0.5F;
      private static final float Я衣л = 10.0F;
      private static final float Я衣э = 12.0F;
      private static final float Я衣ю = 14.0F;
      private static final float Я衣Щ = 10.0F;
      private static final float Я衣у = 30.0F;
      private static final float Я衣2 = 0.5F;
      private static final float Я衣ф = 14.0F;
      private static final float Я衣и = 32.0F;
      private static final float Я衣9 = 9.0F;
      private static final long Я衣о = 1500000000L;
      private static final float Я衣O = 1.5E9F;
      private static final float Я衣7 = 64.0F;
      private static final float Я衣Р = 64.0F;
      private static final float Я衣3 = 0.45F;
      private static final float Я衣р = 0.68F;
      private static final float Я衣6 = 1.35F;
      private static final float Я衣_ = 88.0F;
      private static final float Я衣Ш = 0.5F;
      private static final float Я衣ы = 0.5F;
      private static final float Я衣й = 16.0F;
      private static final double Я衣Ж = 255.0;
      private static final double Я衣м = 255.0;
      private static final double Я衣К = 255.0;
      private static final float Я衣I = 25.0F;
      private static final float Я衣Т = 54.0F;
      private static final float Я衣О = 54.0F;
      private static final float Я衣Ф = 25.0F;
      private static final float Я衣Й = 48.0F;
      private static final float Я衣к = 48.0F;
      private static final String Я衣г = "X";
      private static final float Я衣0 = 62.0F;
      private static final String Я衣х = "Y";
      private static final float Я衣У = 62.0F;
      private static final String Я衣З = "Z";
      private static final float Я衣Е = 40.0F;
      private static final float Я衣П = 42.0F;
      private static final float Я衣西 = -1.0F;
      private static final float Я衣д = 3.4F;
      private static final float Я衣Б = 2.6F;
      private static final float Я衣С = 0.98F;
      private static final float Я衣з = 0.72F;
      private static final float Я衣ц = 10.0F;
      private static final float Я衣я = 9.0F;
      private static final float Яшс = 6.0F;
      private static final float ЯшВ = 6.0F;
      private static final float ЯшЯ = 12.0F;
      private static final float ЯшХ = 12.0F;
      private static final float Яше = 6.0F;
      private static final float ЯшА = 0.88F;
      private static final float Яш4 = 0.5F;
      private static final float Яшп = 4.0F;
      private static final float ЯшД = 25.0F;
      private static final float ЯшЪ = 54.0F;
      private static final float Яшт = 54.0F;
      private static final float Яша = 25.0F;
      private static final float ЯшЮ = 48.0F;
      private static final float ЯшЧ = 48.0F;
      private static final float ЯшЛ = 5.0F;
      private static final float Яш诶 = 220.0F;
      private static final float Яш1 = 303.0F;
      private static final float ЯшЬ = 250.0F;
      private static final float Яш衣 = 352.0F;
      private static final float Яшш = 346.0F;
      private static final float ЯшИ = 328.0F;
      private static final float Яшь = 52.0F;
      private static final float Яшж = 48.0F;
      private static final float Яш弟 = 220.0F;
      private static final float Яшб = 197.0F;
      private static final float ЯшЫ = 6.0F;
      private static final float Яшl = 208.0F;
      private static final float Яш8 = 36.0F;
      private static final float Яш5 = 159.0F;
      private static final float Яшв = 9.0F;
      private static final float Яшъ = 48.0F;
      private static final float ЯшМ = 18.0F;
      private static final float ЯшН = 16.0F;
      private static final float Яшн = 257.0F;
      private static final float ЯшЦ = 154.0F;
      private static final float Яшч = 30.0F;
      private static final float ЯшЭ = 173.0F;
      private static final float Яш必 = 256.0F;
      private static final float Яшщ = 30.0F;
      private static final float ЯшГ = 30.0F;
      private static final float Яшл = 77.0F;
      private static final float Яшэ = 123.0F;
      private static final float Яшю = 104.0F;
      private static final float ЯшЩ = 103.0F;
      private static final float Яшу = 104.0F;
      private static final float Яш2 = 103.0F;
      private static final float Яшф = 14.0F;
      private static final float Яши = 14.0F;
      private static final float Яш9 = 213.0F;
      private static final float Яшо = 291.0F;
      private static final float ЯшO = 25.0F;
      private static final float Яш7 = 13.0F;
      private static final float ЯшР = 16.0F;
      private static final float Яш3 = 327.0F;
      private static final float Яшр = 218.0F;
      private static final float Яш6 = 18.0F;
      private static final float Яш_ = 43.0F;
      private static final float ЯшШ = 16.0F;
      private static final float Яшы = 99.0F;
      private static final float Яшй = 30.0F;
      private static final float ЯшЖ = 123.0F;
      private static final float Яшм = 99.0F;
      private static final float ЯшК = 30.0F;
      private static final float ЯшI = 230.0F;
      private static final float ЯшТ = 99.0F;
      private static final float ЯшО = 30.0F;
      private static final float ЯшФ = 91.0F;
      private static final float ЯшЙ = 268.0F;
      private static final float Яшк = 164.0F;
      private static final float Яшг = 24.0F;
      private static final float Яш0 = 16.0F;
      private static final float Яшх = 82.0F;
      private static final float ЯшУ = 314.0F;
      private static final float ЯшЗ = 174.0F;
      private static final float ЯшЕ = 173.0F;
      private static final float ЯшП = 64.0F;
      private static final float Яш西 = 174.0F;
      private static final float Яшд = 64.0F;
      private static final float ЯшБ = 42.0F;
      private static final float ЯшС = 64.0F;
      private static final float Яшз = 64.0F;
      private static final double Яшц = Math.PI;
      private static final float Яшя = (float) (Math.PI * 2);
      private static final double ЯИс = -Math.PI;
      private static final float ЯИВ = (float) (Math.PI * 2);
      private static final float ЯИЯ = 25.0F;
      private static final float ЯИХ = 54.0F;
      private static final float ЯИе = 48.0F;
      private static final float ЯИА = 54.0F;
      private static final float ЯИ4 = 25.0F;
      private static final float ЯИп = 48.0F;
      private static final float ЯИД = 180.0F;
      private static final float ЯИЪ = 360.0F;
      private static final float ЯИт = -180.0F;
      private static final float ЯИа = 360.0F;
      private static final float ЯИЮ = 77.0F;
      private static final float ЯИЧ = 104.0F;
      private static final float ЯИЛ = 123.0F;
      private static final float ЯИ诶 = 103.0F;
      private static final float ЯИ1 = 0.04F;
      private static final float ЯИЬ = 0.04F;
      private static final float ЯИ衣 = 16.0F;
      private static final float ЯИш = 327.0F;
      private static final float ЯИИ = 218.0F;
      private static final float ЯИь = 18.0F;
      private static final float ЯИж = 44.0F;
      private static final float ЯИ弟 = 220.0F;
      private static final float ЯИб = 205.0F;
      private static final float ЯИЫ = 34.0F;
      private static final float ЯИl = 16.0F;
      private static final float ЯИ8 = 82.0F;
      private static final float ЯИ5 = 314.0F;
      private static final float ЯИв = 174.0F;
      private static final float ЯИъ = 4.0F;
      private static final float ЯИМ = 0.08F;
      private static final float ЯИН = 16.0F;
      private static final float ЯИн = 218.0F;
      private static final float ЯИЦ = 9.0F;
      private static final float ЯИч = 0.25F;
      private static final String ЯИЭ = "Smooth";
      private static final float ЯИ必 = -82.0F;
      private static final float ЯИщ = 30.0F;
      private static final float ЯИГ = 0.12F;
      private static final float ЯИл = -0.34F;
      private static final float ЯИэ = -0.22F;
      private static final float ЯИю = -128.0F;
      private static final float ЯИЩ = 54.0F;
      private static final float ЯИу = -48.0F;
      private static final float ЯИ2 = 0.26F;
      private static final float ЯИф = 0.04F;
      private static final float ЯИи = 0.68F;
      private static final float ЯИ9 = 0.94F;
      private static final float ЯИо = 8.0F;
      private static final String ЯИO = "Down";
      private static final float ЯИ7 = 0.08F;
      private static final float ЯИР = -0.05F;
      private static final float ЯИ3 = -65.0F;
      private static final float ЯИр = 24.0F;
      private static final float ЯИ6 = -4.0F;
      private static final float ЯИ_ = 0.04F;
      private static final float ЯИШ = -0.92F;
      private static final float ЯИы = -0.32F;
      private static final float ЯИй = -142.0F;
      private static final float ЯИЖ = 34.0F;
      private static final float ЯИм = -16.0F;
      private static final float ЯИК = 0.18F;
      private static final float ЯИI = 0.02F;
      private static final float ЯИТ = 0.58F;
      private static final float ЯИО = 8.0F;
      private static final float ЯИФ = 0.35F;
      private static final float ЯИЙ = 0.35F;
      private static final float ЯИк = 11.0F;
      private static final float ЯИг = 9.0F;
      private static final float ЯИ0 = 25.0F;
      private static final float ЯИх = 13.0F;
      private static final float ЯИУ = 4.0F;
      private static final float ЯИЗ = 0.12F;
      private static final float ЯИЕ = 2.5F;
      private static final float ЯИП = 8.0F;
      private static final float ЯИ西 = 8.0F;
      private static final float ЯИд = 4.0F;
      private static final double ЯИБ = 199.0;
      private static final double ЯИС = 80.0;
      private static final double ЯИз = 33.0;
      private static final double ЯИц = 255.0;
      private static final double ЯИя = 255.0;
      private static final double Яьс = 255.0;
      private static final String ЯьВ = "swing/presets";
      private static final String ЯьЯ = "swing/main";
      private static final String ЯьХ = "swing/editor";
      private static final String Яье = "swing/start";
      private static final String ЯьА = "swing/end";
      private static final String Яь4 = "swing/preview";
      private static final String Яьп = "swing/pencil";

      private void drawPresets(float var1, float var2, float var3) {
         float var4 = this.Яи + var3;
         UiDrawing.drawText(ClientFonts.getMediumFont(), Я1Я, var4 + Я1Х, this.Я9 + Я1е, 17, В7);
         float var5 = this.Я9 + Я1А;
         UiDrawing.drawRoundedRect(var4, var5, Я14, Я1п, Я1Д, Во);
         UiDrawing.drawOutline(var4, var5, Я1Ъ, Я1т, Я1а, Я1Ю, ВO);
         this.field_3.updateTarget(this.Я7);
         UiDrawing.pushScissor(var4, this.Я9 + Я1Ч, Я1Л, Я1诶);

         for (int var6 = 0; var6 < this.Щ.size(); var6++) {
            float var7 = this.Я9 + Я11 + var6 * 40 - this.field_3.getValue();
            if (var7 > this.Я9 + Я1Ь && var7 < this.Я9 + Я1衣) {
               SwingAnimations.EditorScreen.Preset var8 = this.Щ.get(var6);
               this.drawPresetRow(var6, var8.name(), var4 + Я1ш, var7, var1, var2);
            }
         }

         UiDrawing.popScissor();
         float var10 = var4 + Я1И;
         float var11 = this.Я9 + Я1ь;
         UiDrawing.drawRoundedRect(var10, var11, Я1ж, Я1弟, Я1б, Во);
         UiDrawing.drawOutline(var10, var11, Я1Ы, Я1l, Я18, Я15, ВO);
         String var12 = this.с弟.isEmpty() ? Я1в : this.с弟;
         UiDrawing.drawText(ClientFonts.getRegularFont(), var12, var10 + Я1ъ, var11 + Я1М, 11, this.с弟.isEmpty() ? ВР : В7);
         if (this.сO && System.currentTimeMillis() / Я1Н % Я1н == 0L) {
            float var9 = var10 + Я1Ц + UiDrawing.getTextWidth(ClientFonts.getRegularFont(), this.с弟, 11);
            UiDrawing.drawRoundedRect(var9, var11 + Я1ч, 1.0F, Я1Э, Я1必, В3);
         }

         UiDrawing.drawRoundedRect(var4 + Я1щ, this.Я9 + Я1Г, Я1л, Я1э, Я1ю, Во);
         UiDrawing.drawOutline(var4 + Я1Щ, this.Я9 + Я1у, Я12, Я1ф, Я1и, Я19, ВO);
         drawIcon(var4 + Я1о, this.Я9 + Я1O, Я17, Я1Р, ВР, field_5);
      }

      private SwingAnimations.EditorScreen.Preset capturePreset(String var1) {
         return new SwingAnimations.EditorScreen.Preset(
            var1,
            this.В_bbefb8af.еO.getValue(),
            this.В_bbefb8af.е7.getValue(),
            this.В_bbefb8af.еР.getValue(),
            this.В_bbefb8af.е3.getValue(),
            this.В_bbefb8af.ер.getValue(),
            this.В_bbefb8af.е6.getValue(),
            this.В_bbefb8af.е_.getValue(),
            this.В_bbefb8af.еШ.getValue(),
            this.В_bbefb8af.еы.getValue(),
            this.В_bbefb8af.ей.getValue(),
            this.В_bbefb8af.еЖ.getValue(),
            this.В_bbefb8af.ем.getValue(),
            this.В_bbefb8af.еК.getValue(),
            this.В_bbefb8af.еI.getValue(),
            this.В_bbefb8af.еТ.getValue(),
            this.В_bbefb8af.еО.getValue(),
            this.В_bbefb8af.ЯO.getValue(),
            this.В_bbefb8af.еФ.getValue()
         );
      }

      @Override
      public boolean mouseReleased(MouseButtonEvent var1) {
         this.Вр = 0;
         this.В_238df2f2 = SwingAnimations.EditorScreen.TransformAxis.NONE;
         return true;
      }

      private static void drawIcon(float var0, float var1, float var2, float var3, int var4, Identifier var5) {
         UiDrawing.drawTexture(var0, var1, var2, var3, 0.0F, var4, var5);
      }

      @Override
      public boolean keyPressed(KeyEvent var1) {
         if (this.сO && var1.key() == 259 && !this.с弟.isEmpty()) {
            int var2 = this.с弟.offsetByCodePoints(this.с弟.length(), -1);
            this.с弟 = this.с弟.substring(0, var2);
            return true;
         }

         if (!this.сO || var1.key() != 257 && var1.key() != 335) {
            if (var1.key() != 256) {
               return super.keyPressed(var1);
            } else if (this.сO) {
               this.сO = false;
               return true;
            } else {
               this.onClose();
               return true;
            }
         } else {
            this.createPreset();
            this.сO = false;
            return true;
         }
      }

      private void drawMain(float var1, float var2, float var3) {
         float var4 = this.mainX() + var3;
         UiDrawing.drawText(ClientFonts.getMediumFont(), ЯЬс, var4 + ЯЬВ, this.Я9 + ЯЬЯ, 17, В7);
         UiDrawing.drawText(ClientFonts.getRegularFont(), ClientLocalization.translate(ЯЬХ), var4 + ЯЬе, this.Я9 + ЯЬА, 10, ВР);
         UiDrawing.drawRoundedRect(var4 + ЯЬ4, this.Я9 + ЯЬп, ЯЬД, ЯЬЪ, ЯЬт, Во);
         UiDrawing.drawOutline(var4 + ЯЬа, this.Я9 + ЯЬЮ, ЯЬЧ, ЯЬЛ, ЯЬ诶, ЯЬ1, ВO);
         UiDrawing.drawOutline(var4 + ЯЬЬ, this.Я9 + ЯЬ衣, ЯЬш, ЯЬИ, 1.0F, ЯЬь, ВO);
         this.drawBezier(var4 + ЯЬж, this.Я9 + ЯЬ弟, ЯЬб, ЯЬЫ);
         UiDrawing.drawText(ClientFonts.getRegularFont(), ClientLocalization.translate(ЯЬl), var4 + ЯЬ8, this.Я9 + ЯЬ5, 12, В7);
         this.р.updateTarget(this.В_bbefb8af.ЯO.getValue() ? 1.0F : 0.0F);
         this.drawSwitch(var4 + ЯЬв, this.Я9 + ЯЬъ, this.р.getValue());
         int var5 = ColorMath.lerpArgb(ВР, В7, this.р.getValue());
         UiDrawing.drawText(ClientFonts.getRegularFont(), ClientLocalization.translate(ЯЬМ), var4 + ЯЬН, this.Я9 + ЯЬн, 11, var5);
         String var6 = String.valueOf(this.В_bbefb8af.еФ.getValue().intValue());
         UiDrawing.drawText(ClientFonts.getRegularFont(), var6, var4 + ЯЬЦ - UiDrawing.getTextWidth(ClientFonts.getRegularFont(), var6, 11), this.Я9 + ЯЬч, 11, var5);
         float var7 = var4 + ЯЬЭ;
         float var8 = this.Я9 + ЯЬ必;
         UiDrawing.drawRoundedRect(var7, var8, ЯЬщ, ЯЬГ, 2.0F, ColorMath.lerpArgb(ColorMath.withAlpha(ВР, ЯЬл), ColorMath.withAlpha(В3, ЯЬэ), this.р.getValue()));
         float var9 = (this.В_bbefb8af.еФ.getValue() - 1.0F) / ЯЬю;
         UiDrawing.drawRoundedRect(var7, var8, ЯЬЩ * var9, ЯЬу, ЯЬ2, ColorMath.lerpArgb(ВР, В3, this.р.getValue()));
         UiDrawing.drawRoundedRect(var7 + ЯЬф * var9 - ЯЬи, var8 - ЯЬ9, ЯЬо, ЯЬO, ЯЬ7, var5);
      }

      @Override
      public boolean isPauseScreen() {
         return false;
      }

      private void drawEditorTab(SwingAnimations.EditorScreen.EditorTab var1, float var2, float var3, float var4, float var5, float var6) {
         boolean var7 = this.В_8b72c019 == var1;
         boolean var8 = inside(var5, var6, var2, var3, var4, Я衣Ц);
         UiDrawing.drawRoundedRect(var2, var3, var4, Я衣ч, Я衣Э, var8 ? ColorMath.withAlpha(В7, Я衣必) : Во);
         UiDrawing.drawOutline(var2, var3, var4, Я衣щ, Я衣Г, Я衣л, var7 ? В3 : ВO);
         int var9 = var7 ? В7 : ВР;
         Identifier var10 = var1 == SwingAnimations.EditorScreen.EditorTab.START
            ? Ы
            : (var1 == SwingAnimations.EditorScreen.EditorTab.END ? l : field_8);
         float var11 = var1 == SwingAnimations.EditorScreen.EditorTab.PREVIEW ? Я衣э : Я衣ю;
         drawIcon(var2 + Я衣Щ, var3 + (Я衣у - var11) * Я衣2, Я衣ф, var11, var9, var10);
         UiDrawing.drawText(ClientFonts.getRegularFont(), var1.Ям, var2 + Я衣и, var3 + Я衣9, 11, var9);
      }

      private boolean shiftDown() {
         long var1 = this.minecraft.getWindow().handle();
         return GLFW.glfwGetKey(var1, 340) == 1 || GLFW.glfwGetKey(var1, 344) == 1;
      }

      private void applyPresetValues(SwingAnimations.EditorScreen.Preset var1) {
         set(this.В_bbefb8af.еO, var1.startX());
         set(this.В_bbefb8af.е7, var1.startY());
         set(this.В_bbefb8af.еР, var1.startZ());
         set(this.В_bbefb8af.е3, var1.startPitch());
         set(this.В_bbefb8af.ер, var1.startYaw());
         set(this.В_bbefb8af.е6, var1.startRoll());
         set(this.В_bbefb8af.е_, var1.endX());
         set(this.В_bbefb8af.еШ, var1.endY());
         set(this.В_bbefb8af.еы, var1.endZ());
         set(this.В_bbefb8af.ей, var1.endPitch());
         set(this.В_bbefb8af.еЖ, var1.endYaw());
         set(this.В_bbefb8af.ем, var1.endRoll());
         set(this.В_bbefb8af.еК, var1.curve1X());
         set(this.В_bbefb8af.еI, var1.curve1Y());
         set(this.В_bbefb8af.еТ, var1.curve2X());
         set(this.В_bbefb8af.еО, var1.curve2Y());
         this.В_bbefb8af.ЯO.setValue(var1.back());
         set(this.В_bbefb8af.еФ, var1.backSpeed());
      }

      private boolean editorClicked(float var1, float var2, int var3) {
         float var4 = this.editorX();
         float var5 = this.Я9 + Яш_;
         if (var3 == 0) {
            if (inside(var1, var2, var4 + ЯшШ, var5, Яшы, Яшй)) {
               this.selectEditorTab(SwingAnimations.EditorScreen.EditorTab.START);
            } else if (inside(var1, var2, var4 + ЯшЖ, var5, Яшм, ЯшК)) {
               this.selectEditorTab(SwingAnimations.EditorScreen.EditorTab.END);
            } else if (inside(var1, var2, var4 + ЯшI, var5, ЯшТ, ЯшО)) {
               this.selectEditorTab(SwingAnimations.EditorScreen.EditorTab.PREVIEW);
            } else if (this.В_8b72c019 != SwingAnimations.EditorScreen.EditorTab.PREVIEW && inside(var1, var2, var4 + ЯшФ, this.Я9 + ЯшЙ, Яшк, Яшг)
               )
             {
               this.copyStartToEnd();
            } else if (this.В_8b72c019 != SwingAnimations.EditorScreen.EditorTab.PREVIEW && inside(var1, var2, var4 + Яш0, this.Я9 + Яшх, ЯшУ, ЯшЗ)
               )
             {
               SwingAnimations.EditorScreen.Pose var6 = this.currentTransform();
               float var7 = var4 + ЯшЕ + var6.x * ЯшП;
               float var8 = this.Я9 + Яш西 - var6.y * Яшд;
               SwingAnimations.EditorScreen.TransformAxis var9 = this.hitRotationRing(var1, var2, var7, var8);
               if (var9 != SwingAnimations.EditorScreen.TransformAxis.NONE) {
                  this.beginSwordControl(var9, var1, var2, var7, var8);
               } else if (distance(var1, var2, var7, var8) <= ЯшБ) {
                  this.beginSwordControl(SwingAnimations.EditorScreen.TransformAxis.MOVE, var1, var2, var7, var8);
               }
            }
         }

         return true;
      }

      @Override
      protected void extractBlurredBackground(GuiGraphicsExtractor var1) {
      }

      private FloatSetting zSetting() {
         return this.В_8b72c019 == SwingAnimations.EditorScreen.EditorTab.START ? this.В_bbefb8af.еР : this.В_bbefb8af.еы;
      }

      private void drawWindow(float var1, float var2, float var3, float var4) {
         if (RoseClient.getInstance().getServices().getModules().getHud().В().getValue()) {
            UiDrawing.drawShadow(var1, var2, var3, var4, Я诶西, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SHADOW));
         }

         UiDrawing.drawBlur(var1, var2, var3, var4, Я诶д, Я诶Б, ColorMath.packArgb(255, 255, 255, Я诶С));
         UiDrawing.drawRoundedRect(var1, var2, var3, var4, Я诶з, В9);
      }

      private void storePresets() {
         this.В_bbefb8af.п_at_d75ef7b2.setValue(В_26ee8fae.toJson(this.Щ));
         RoseClient.getInstance().getServices().getConfigManager().getSwingAnimationsConfig().save();
      }

      private void updateSwordControl(float var1, float var2) {
         float var3 = var1 - this.ЯР;
         float var4 = var2 - this.Я3;
         switch (this.В_238df2f2) {
            case MOVE:
               this.xSetting().setClampedValue(this.Яр + var3 / ЯшС);
               this.ySetting().setClampedValue(this.Я6 - var4 / Яшз);
               break;
            case ROTATE_X:
            case ROTATE_Y:
            case ROTATE_Z:
               float var5 = this.rotationAngle(this.В_238df2f2, var1, var2, this.ЯШ, this.Яы);
               float var6 = var5 - this.Я_;

               while (var6 > Яшц) {
                  var6 -= Яшя;
               }

               while (var6 < ЯИс) {
                  var6 += ЯИВ;
               }

               float var7 = this.wrapAngle(this.Яр + (float)Math.toDegrees(var6));
               if (this.В_238df2f2 == SwingAnimations.EditorScreen.TransformAxis.ROTATE_X) {
                  this.pitchSetting().setClampedValue(var7);
               } else if (this.В_238df2f2 == SwingAnimations.EditorScreen.TransformAxis.ROTATE_Y) {
                  this.yawSetting().setClampedValue(var7);
               } else {
                  this.rollSetting().setClampedValue(var7);
               }
         }
      }

      private void drawBezier(float var1, float var2, float var3, float var4) {
         this.drawHandleLine(var1, var2 + var4, var1 + this.В_bbefb8af.еК.getValue() * var3, var2 + (1.0F - this.В_bbefb8af.еI.getValue()) * var4);
         this.drawHandleLine(var1 + var3, var2, var1 + this.В_bbefb8af.еТ.getValue() * var3, var2 + (1.0F - this.В_bbefb8af.еО.getValue()) * var4);

         for (int var5 = 0; var5 <= 256; var5++) {
            float var6 = var5 / ЯЬР;
            float var7 = SwingAnimations.Ф(var6, 0.0F, this.В_bbefb8af.еК.getValue(), this.В_bbefb8af.еТ.getValue(), 1.0F);
            float var8 = SwingAnimations.Ф(var6, 0.0F, this.В_bbefb8af.еI.getValue(), this.В_bbefb8af.еО.getValue(), 1.0F);
            UiDrawing.drawRoundedRect(var1 + var7 * var3 - 1.0F, var2 + (1.0F - var8) * var4 - 1.0F, 2.0F, 2.0F, 1.0F, В7);
         }

         UiDrawing.drawRoundedRect(var1 - 2.0F, var2 + var4 - 2.0F, ЯЬ3, ЯЬр, 2.0F, ВР);
         UiDrawing.drawRoundedRect(var1 + var3 - 2.0F, var2 - 2.0F, ЯЬ6, ЯЬ_, 2.0F, ВР);
         this.drawCurvePoint(var1 + this.В_bbefb8af.еК.getValue() * var3, var2 + (1.0F - this.В_bbefb8af.еI.getValue()) * var4);
         this.drawCurvePoint(var1 + this.В_bbefb8af.еТ.getValue() * var3, var2 + (1.0F - this.В_bbefb8af.еО.getValue()) * var4);
      }

      private float wrapAngle(float var1) {
         while (var1 > ЯИД) {
            var1 -= ЯИЪ;
         }

         while (var1 < ЯИт) {
            var1 += ЯИа;
         }

         return var1;
      }

      private boolean presetsClicked(float var1, float var2, int var3) {
         if (var3 != 0 && var3 != 1) {
            return true;
         }

         for (int var4 = 0; var4 < this.Щ.size(); var4++) {
            float var5 = this.Я9 + Яшь + var4 * 40 - this.field_3.getValue();
            if (inside(var1, var2, this.Яи, this.Я9 + Яшж, Яш弟, Яшб) && inside(var1, var2, this.Яи + ЯшЫ, var5, Яшl, Яш8)) {
               if (var3 == 1) {
                  this.deletePreset(var4);
                  this.сO = false;
                  return true;
               }

               if (inside(var1, var2, this.Яи + Яш5, var5 + Яшв, Яшъ, ЯшМ)) {
                  if (Math.round(this.В_bbefb8af.еЙ.getValue()) == var4) {
                     this.savePreset(var4);
                  } else {
                     this.selectPreset(var4);
                  }
               } else {
                  this.selectPreset(var4);
               }

               this.сO = false;
               return true;
            }
         }

         if (var3 != 0) {
            return true;
         }

         if (inside(var1, var2, this.Яи + ЯшН, this.Я9 + Яшн, ЯшЦ, Яшч)) {
            this.сO = true;
            return true;
         }

         if (inside(var1, var2, this.Яи + ЯшЭ, this.Я9 + Яш必, Яшщ, ЯшГ)) {
            this.createPreset();
            this.сO = false;
         }

         return true;
      }

      private SwingAnimations.EditorScreen.Pose currentTransform() {
         if (this.В_8b72c019 == SwingAnimations.EditorScreen.EditorTab.START) {
            return this.readTransform(true);
         }

         if (this.В_8b72c019 == SwingAnimations.EditorScreen.EditorTab.END) {
            return this.readTransform(false);
         }

         float var1 = (float)(System.nanoTime() % Я衣о) / Я衣O;
         float var2 = this.В_bbefb8af.ИХ(var1);
         return SwingAnimations.EditorScreen.Pose.lerp(this.readTransform(true), this.readTransform(false), var2);
      }

      private void copyStartToEnd() {
         set(this.В_bbefb8af.е_, this.В_bbefb8af.еO.getValue());
         set(this.В_bbefb8af.еШ, this.В_bbefb8af.е7.getValue());
         set(this.В_bbefb8af.еы, this.В_bbefb8af.еР.getValue());
         set(this.В_bbefb8af.ей, this.В_bbefb8af.е3.getValue());
         set(this.В_bbefb8af.еЖ, this.В_bbefb8af.ер.getValue());
         set(this.В_bbefb8af.ем, this.В_bbefb8af.е6.getValue());
         this.selectEditorTab(SwingAnimations.EditorScreen.EditorTab.END);
      }

      private void drawAxisBadge(String var1, float var2, float var3, int var4, boolean var5) {
         float var6 = var5 ? Я衣ц : Я衣я;
         UiDrawing.drawRoundedRect(var2 - Яшс, var3 - ЯшВ, ЯшЯ, ЯшХ, Яше, ColorMath.withAlpha(В9, ЯшА));
         float var7 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, (int)var6);
         UiDrawing.drawText(ClientFonts.getMediumFont(), var1, var2 - var7 * Яш4, var3 - Яшп, (int)var6, var4);
      }

      private float editorX() {
         return this.mainX() + Я1с + Я1В;
      }

      @Override
      public void clearFocus() {
      }

      private void drawPresetRow(int var1, String var2, float var3, float var4, float var5, float var6) {
         boolean var7 = inside(var5, var6, var3, var4, Я13, Я1р);
         this.у.get(var1).updateTarget(var7 ? 1.0F : 0.0F);
         boolean var8 = Math.round(this.В_bbefb8af.еЙ.getValue()) == var1;
         this.field_2.get(var1).updateTarget(var8 ? 1.0F : 0.0F);
         float var9 = this.у.get(var1).getValue();
         float var10 = this.field_2.get(var1).getValue();
         int var11 = ColorMath.lerpArgb(Во, ColorMath.withAlpha(В3, Я16), var10);
         UiDrawing.drawRoundedRect(var3, var4, Я1_, Я1Ш, Я1ы, ColorMath.lerpArgb(var11, ColorMath.withAlpha(В7, Я1й), var9));
         UiDrawing.drawOutline(var3, var4, Я1Ж, Я1м, Я1К, Я1I, ColorMath.lerpArgb(ВO, В3, Math.max(var10 * Я1Т, var9 * Я1О)));
         int var12 = ColorMath.lerpArgb(ВР, В7, var10);
         UiDrawing.drawText(ClientFonts.getRegularFont(), var2, var3 + Я1Ф, var4 + Я1Й, 13, var12);
         float var13 = var3 + Я1к;
         if (!var8) {
            UiDrawing.drawRoundedRect(var13, var4 + Я1г, Я10, Я1х, Я1У, В3);
         } else {
            UiDrawing.drawOutline(var13, var4 + Я1З, Я1Е, Я1П, Я1西, Я1д, В3);
         }

         String var14 = var8 ? Я1Б : Я1С;
         float var15 = UiDrawing.getTextWidth(ClientFonts.getRegularFont(), var14, 10);
         UiDrawing.drawText(ClientFonts.getRegularFont(), var14, var13 + (Я1з - var15) * Я1ц, var4 + Я1я, 10, В7);
      }

      private void createPreset() {
         String var1 = this.с弟.trim();
         if (!var1.isEmpty()) {
            this.Щ.add(this.capturePreset(var1));
            this.у.add(new FloatAnimation(0.0F, ЯИк, Easings.CUBIC_OUT));
            this.field_2.add(new FloatAnimation(0.0F, ЯИг, Easings.CUBIC_OUT));
            int var2 = this.Щ.size() - 1;
            this.В_bbefb8af.еЙ.setClampedValue(var2);
            this.с弟 = "";
            this.Я7 = Math.max(0, this.Щ.size() * 40 - 193);
            this.storePresets();
            this.Р.setImmediately(0.0F);
         }
      }

      private static boolean inside(float var0, float var1, float var2, float var3, float var4, float var5) {
         return var0 >= var2 && var1 >= var3 && var0 <= var2 + var4 && var1 <= var3 + var5;
      }

      private FloatSetting rollSetting() {
         return this.В_8b72c019 == SwingAnimations.EditorScreen.EditorTab.START ? this.В_bbefb8af.е6 : this.В_bbefb8af.ем;
      }

      @Override
      public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
         UiDrawing.setTransform(new Matrix4f());
         UiDrawing.disableScissor();
         this.field_7.updateTarget(1.0F);
         this.Р.updateTarget(1.0F);
         this.syncTheme();
         Vector2f var5 = WorldProjection.transformScreenPoint(var2, var3);
         this.Яо = var5.x;
         this.ЯO = var5.y;
         float var6 = this.minecraft.getWindow().getScreenWidth();
         float var7 = this.minecraft.getWindow().getScreenHeight();
         this.Яи = (var6 - Я诶3) * Я诶р;
         this.Я9 = (var7 - Я诶6) * Я诶_ + (1.0F - this.field_7.getValue()) * Я诶Ш;
         UiDrawing.setGlobalAlpha(Я诶ы * this.field_7.getValue());
         float var8 = this.mainX();
         float var9 = this.editorX();
         this.drawWindow(this.Яи, this.Я9, Я诶й, Я诶Ж);
         this.drawWindow(var8, this.Я9, Я诶м, Я诶К);
         this.drawWindow(var9, this.Я9, Я诶I, Я诶Т);
         float var10 = (1.0F - this.Р.getValue()) * Я诶О;
         UiDrawing.pushScissor(this.Яи, this.Я9, Я诶Ф, Я诶Й);
         this.drawPresets(var5.x, var5.y, 0.0F);
         UiDrawing.popScissor();
         UiDrawing.pushScissor(var8, this.Я9, Я诶к, Я诶г);
         this.drawMain(var5.x, var5.y, 0.0F);
         UiDrawing.popScissor();
         UiDrawing.pushScissor(var9, this.Я9, Я诶0, Я诶х);
         UiDrawing.setGlobalAlpha(Я诶У * this.field_7.getValue() * this.Р.getValue());
         this.drawEditor(var5.x, var5.y, var10);
         UiDrawing.popScissor();
         UiDrawing.setGlobalAlpha(Я诶З * this.field_7.getValue());
         UiDrawing.resetGlobalAlpha();
      }

      @Override
      public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
         Vector2f var6 = WorldProjection.guiToScreen(var1.x(), var1.y());
         if (this.В_238df2f2 != SwingAnimations.EditorScreen.TransformAxis.NONE) {
            this.updateSwordControl(var6.x, var6.y);
            return true;
         }

         if (this.Вр != 0) {
            float var7 = Math.clamp((var6.x - (this.mainX() + ЯИЮ)) / ЯИЧ, 0.0F, 1.0F);
            float var8 = Math.clamp(1.0F - (var6.y - (this.Я9 + ЯИЛ)) / ЯИ诶, 0.0F, 1.0F);
            if (this.Вр == 1) {
               this.В_bbefb8af.еК.setClampedValue(Math.min(var7, this.В_bbefb8af.еТ.getValue() - ЯИ1));
               this.В_bbefb8af.еI.setClampedValue(var8);
            } else {
               this.В_bbefb8af.еТ.setClampedValue(Math.max(var7, this.В_bbefb8af.еК.getValue() + ЯИЬ));
               this.В_bbefb8af.еО.setClampedValue(var8);
            }

            return true;
         } else if (this.В_bbefb8af.ЯO.getValue() && inside(var6.x, var6.y, this.mainX() + ЯИ衣, this.Я9 + ЯИш, ЯИИ, ЯИь)) {
            this.updateBackSpeed(var6.x);
            return true;
         } else {
            return true;
         }
      }

      private void loadPresets() {
         String var1 = this.В_bbefb8af.п_at_d75ef7b2.getValue();
         boolean var2 = false;
         if (!var1.isBlank()) {
            try {
               SwingAnimations.EditorScreen.Preset[] var3 = В_26ee8fae.fromJson(var1, SwingAnimations.EditorScreen.Preset[].class);
               if (var3 != null) {
                  var2 = true;

                  for (SwingAnimations.EditorScreen.Preset var7 : var3) {
                     if (var7 != null && var7.name() != null) {
                        this.Щ.add(var7);
                     }
                  }
               }
            } catch (RuntimeException var8) {
               this.Щ.clear();
            }
         }

         if (!var2 && this.Щ.isEmpty()) {
            this.Щ
               .add(
                  new SwingAnimations.EditorScreen.Preset(
                     ЯИЭ, 0.0F, 0.0F, 0.0F, ЯИ必, ЯИщ, 0.0F, ЯИГ, ЯИл, ЯИэ, ЯИю, ЯИЩ, ЯИу, ЯИ2, ЯИф, ЯИи, ЯИ9, true, ЯИо
                  )
               );
            this.Щ
               .add(
                  new SwingAnimations.EditorScreen.Preset(
                     ЯИO, 0.0F, ЯИ7, ЯИР, ЯИ3, ЯИр, ЯИ6, ЯИ_, ЯИШ, ЯИы, ЯИй, ЯИЖ, ЯИм, ЯИК, ЯИI, ЯИТ, 1.0F, true, ЯИО
                  )
               );
         }
      }

      private float mainX() {
         return this.Яи + Я诶ц + Я诶я;
      }

      private void drawEditor(float var1, float var2, float var3) {
         float var4 = this.editorX() + var3;
         UiDrawing.drawText(ClientFonts.getMediumFont(), ЯЬЙ, var4 + ЯЬк, this.Я9 + ЯЬг, 17, В7);
         float var5 = this.Я9 + ЯЬ0;
         this.drawEditorTab(SwingAnimations.EditorScreen.EditorTab.START, var4 + ЯЬх, var5, ЯЬУ, var1, var2);
         this.drawEditorTab(SwingAnimations.EditorScreen.EditorTab.END, var4 + ЯЬЗ, var5, ЯЬЕ, var1, var2);
         this.drawEditorTab(SwingAnimations.EditorScreen.EditorTab.PREVIEW, var4 + ЯЬП, var5, ЯЬ西, var1, var2);
         UiDrawing.drawRoundedRect(var4 + ЯЬд, this.Я9 + ЯЬБ, ЯЬС, ЯЬз, ЯЬц, Во);
         UiDrawing.drawOutline(var4 + ЯЬя, this.Я9 + Я衣с, Я衣В, Я衣Я, Я衣Х, Я衣е, ВO);
         SwingAnimations.EditorScreen.Pose var6 = this.currentTransform();
         UiDrawing.pushScissor(var4 + Я衣А, this.Я9 + Я衣4, Я衣п, Я衣Д);
         this.drawSword(var4 + Я衣Ъ, this.Я9 + Я衣т, var6);
         if (this.В_8b72c019 != SwingAnimations.EditorScreen.EditorTab.PREVIEW) {
            float var7 = var4 + Я衣а + var6.x * Я衣Ю;
            float var8 = this.Я9 + Я衣Ч - var6.y * Я衣Л;
            this.drawSwordControl(var7, var8, var1, var2);
         }

         UiDrawing.popScissor();
         if (this.В_8b72c019 == SwingAnimations.EditorScreen.EditorTab.PREVIEW) {
            String var13 = Я衣诶;
            float var15 = UiDrawing.getTextWidth(ClientFonts.getRegularFont(), var13, 9);
            UiDrawing.drawText(ClientFonts.getRegularFont(), var13, var4 + Я衣1 - var15 * Я衣Ь, this.Я9 + Я衣衣, 9, ВР);
         } else {
            float var14 = var4 + Я衣ш;
            float var16 = this.Я9 + Я衣И;
            float var9 = Я衣ь;
            boolean var10 = inside(var1, var2, var14, var16, var9, Я衣ж);
            UiDrawing.drawRoundedRect(var14, var16, var9, Я衣弟, Я衣б, var10 ? ColorMath.withAlpha(В3, Я衣Ы) : Во);
            UiDrawing.drawOutline(var14, var16, var9, Я衣l, Я衣8, Я衣5, var10 ? ColorMath.withAlpha(В3, Я衣в) : ВO);
            String var11 = Я衣ъ;
            float var12 = UiDrawing.getTextWidth(ClientFonts.getRegularFont(), var11, 10);
            UiDrawing.drawText(ClientFonts.getRegularFont(), var11, var14 + (var9 - var12) * Я衣М, var16 + Я衣Н, 10, var10 ? В7 : ColorMath.withAlpha(В7, Я衣н));
         }
      }

      private void syncTheme() {
         В9 = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_BACKGROUND);
         Во = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_BACKGROUND_TWO), Я诶Е);
         Во = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_BACKGROUND_TWO), 0.0F);
         ВO = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE), Я诶П);
         В7 = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT);
         ВР = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT);
         В3 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      }

      private void drawRotationRing(float var1, float var2, float var3, float var4, int var5, boolean var6) {
         float var7 = Math.max(var3, var4);
         float var8 = this.minecraft.getWindow().getScreenWidth();
         float var9 = this.minecraft.getWindow().getScreenHeight();
         float var10 = Я衣西 + var1 * 2.0F / var8;
         float var11 = 1.0F - var2 * 2.0F / var9;
         Matrix4f var12 = new Matrix4f(UiDrawing.getTransform());
         Matrix4f var13 = new Matrix4f().translate(var10, var11, 0.0F).scale(var3 / var7, var4 / var7, 1.0F).translate(-var10, -var11, 0.0F);
         UiDrawing.setTransform(var13);
         UiDrawing.drawOutline(var1 - var7, var2 - var7, var7 * 2.0F, var7 * 2.0F, var6 ? Я衣д : Я衣Б, var7, ColorMath.withAlpha(var5, var6 ? Я衣С : Я衣з));
         UiDrawing.setTransform(var12);
      }

      private boolean mainClicked(float var1, float var2, int var3) {
         if (var3 != 0) {
            return true;
         }

         float var4 = this.mainX();
         float var5 = var4 + Яшл;
         float var6 = this.Я9 + Яшэ;
         float var7 = var5 + this.В_bbefb8af.еК.getValue() * Яшю;
         float var8 = var6 + (1.0F - this.В_bbefb8af.еI.getValue()) * ЯшЩ;
         float var9 = var5 + this.В_bbefb8af.еТ.getValue() * Яшу;
         float var10 = var6 + (1.0F - this.В_bbefb8af.еО.getValue()) * Яш2;
         if (distance(var1, var2, var7, var8) <= Яшф) {
            this.Вр = 1;
         } else if (distance(var1, var2, var9, var10) <= Яши) {
            this.Вр = 2;
         } else if (inside(var1, var2, var4 + Яш9, this.Я9 + Яшо, ЯшO, Яш7)) {
            this.В_bbefb8af.ЯO.setValue(!this.В_bbefb8af.ЯO.getValue());
         } else if (this.В_bbefb8af.ЯO.getValue() && inside(var1, var2, var4 + ЯшР, this.Я9 + Яш3, Яшр, Яш6)) {
            this.updateBackSpeed(var1);
         }

         return true;
      }

      private FloatSetting xSetting() {
         return this.В_8b72c019 == SwingAnimations.EditorScreen.EditorTab.START ? this.В_bbefb8af.еO : this.В_bbefb8af.е_;
      }

      private void deletePreset(int var1) {
         if (var1 >= 0 && var1 < this.Щ.size()) {
            int var2 = Math.round(this.В_bbefb8af.еЙ.getValue());
            this.Щ.remove(var1);
            this.у.remove(var1);
            this.field_2.remove(var1);
            if (this.Щ.isEmpty()) {
               this.В_bbefb8af.еЙ.setClampedValue(0.0F);
            } else {
               int var3 = var2 > var1 ? var2 - 1 : Math.min(var2, this.Щ.size() - 1);
               this.В_bbefb8af.еЙ.setClampedValue(var3);
               if (var2 == var1) {
                  this.applyPresetValues(this.Щ.get(var3));
               }
            }

            float var4 = Math.max(0, this.Щ.size() * 40 - 193);
            this.Я7 = Math.clamp(this.Я7, 0.0F, var4);
            this.storePresets();
            this.Р.setImmediately(ЯИЙ);
         }
      }

      private void drawCurvePoint(float var1, float var2) {
         UiDrawing.drawRoundedRect(var1 - ЯЬК, var2 - ЯЬI, ЯЬТ, ЯЬО, ЯЬФ, В7);
      }

      private void drawSword(float var1, float var2, SwingAnimations.EditorScreen.Pose var3) {
         float var4 = var1 + var3.x * Я衣7;
         float var5 = var2 - var3.y * Я衣Р;
         float var6 = Math.clamp(1.0F / (1.0F - var3.z * Я衣3), Я衣р, Я衣6);
         float var7 = Я衣_ * var6;
         UiDrawing.drawRotatedItem(Items.DIAMOND_SWORD.getDefaultInstance(), var4 - var7 * Я衣Ш, var5 - var7 * Я衣ы, var7 / Я衣й, var3.pitch, var3.yaw, var3.roll);
      }

      private void drawHandleLine(float var1, float var2, float var3, float var4) {
         for (int var5 = 0; var5 <= 20; var5++) {
            float var6 = var5 / ЯЬШ;
            UiDrawing.drawRoundedRect(Mth.lerp(var6, var1, var3) - ЯЬы, Mth.lerp(var6, var2, var4) - ЯЬй, 1.0F, 1.0F, ЯЬЖ, ColorMath.withAlpha(ВР, ЯЬм));
         }
      }

      private EditorScreen(SwingAnimations var1, Screen var2) {
         super(Component.literal(Я诶ф));
         this.field_7 = new FloatAnimation(0.0F, Я诶и, Easings.CUBIC_OUT);
         this.Р = new FloatAnimation(0.0F, Я诶9, Easings.CUBIC_OUT);
         this.field_3 = new FloatAnimation(0.0F, Я诶о, Easings.CUBIC_OUT);
         this.Щ = new ArrayList<>();
         this.у = new ArrayList<>();
         this.field_2 = new ArrayList<>();
         this.В_8b72c019 = SwingAnimations.EditorScreen.EditorTab.START;
         this.В_238df2f2 = SwingAnimations.EditorScreen.TransformAxis.NONE;
         this.с弟 = "";
         this.В_bbefb8af = var1;
         this.е = var2;
         this.р = new FloatAnimation(var1.ЯO.getValue() ? 1.0F : 0.0F, Я诶O, Easings.CUBIC_OUT);
         this.loadPresets();
         int var3 = this.Щ.isEmpty() ? 0 : Math.clamp(Math.round(var1.еЙ.getValue()), 0, this.Щ.size() - 1);
         var1.еЙ.setClampedValue(var3);

         for (int var4 = 0; var4 < this.Щ.size(); var4++) {
            this.у.add(new FloatAnimation(0.0F, Я诶7, Easings.CUBIC_OUT));
            this.field_2.add(new FloatAnimation(var4 == var3 ? 1.0F : 0.0F, Я诶Р, Easings.CUBIC_OUT));
         }
      }

      private void savePreset(int var1) {
         if (var1 >= 0 && var1 < this.Щ.size()) {
            SwingAnimations.EditorScreen.Preset var2 = this.Щ.get(var1);
            this.Щ.set(var1, this.capturePreset(var2.name()));
            this.storePresets();
            this.field_2.get(var1).setImmediately(ЯИФ);
         }
      }

      @Override
      public void removed() {
         this.storePresets();
         super.removed();
      }

      private void updateBackSpeed(float var1) {
         float var2 = 1.0F + Math.clamp((var1 - (this.mainX() + ЯИН)) / ЯИн, 0.0F, 1.0F) * ЯИЦ;
         this.В_bbefb8af.еФ.setClampedValue(var2);
      }

      private SwingAnimations.EditorScreen.TransformAxis hitRotationRing(float var1, float var2, float var3, float var4) {
         float var5 = this.ellipseDistance(var1 - var3, var2 - var4, ЯшД, ЯшЪ);
         float var6 = this.ellipseDistance(var1 - var3, var2 - var4, Яшт, Яша);
         float var7 = this.ellipseDistance(var1 - var3, var2 - var4, ЯшЮ, ЯшЧ);
         float var8 = Math.min(var5, Math.min(var6, var7));
         if (var8 > ЯшЛ) {
            return SwingAnimations.EditorScreen.TransformAxis.NONE;
         } else if (var8 == var5) {
            return SwingAnimations.EditorScreen.TransformAxis.ROTATE_X;
         } else {
            return var8 == var6 ? SwingAnimations.EditorScreen.TransformAxis.ROTATE_Y : SwingAnimations.EditorScreen.TransformAxis.ROTATE_Z;
         }
      }

      private float rotationAngle(SwingAnimations.EditorScreen.TransformAxis var1, float var2, float var3, float var4, float var5) {
         float var6 = var1 == SwingAnimations.EditorScreen.TransformAxis.ROTATE_X
            ? ЯИЯ
            : (var1 == SwingAnimations.EditorScreen.TransformAxis.ROTATE_Y ? ЯИХ : ЯИе);
         float var7 = var1 == SwingAnimations.EditorScreen.TransformAxis.ROTATE_X
            ? ЯИА
            : (var1 == SwingAnimations.EditorScreen.TransformAxis.ROTATE_Y ? ЯИ4 : ЯИп);
         return (float)Math.atan2((var3 - var5) / var7, (var2 - var4) / var6);
      }

      private void beginSwordControl(SwingAnimations.EditorScreen.TransformAxis var1, float var2, float var3, float var4, float var5) {
         this.В_238df2f2 = var1;
         this.ЯР = var2;
         this.Я3 = var3;
         this.ЯШ = var4;
         this.Яы = var5;

         this.Яр = switch (var1) {
            case MOVE -> this.xSetting().getValue();
            case ROTATE_X -> this.pitchSetting().getValue();
            case ROTATE_Y -> this.yawSetting().getValue();
            case ROTATE_Z -> this.rollSetting().getValue();
            default -> 0.0F;
         };
         this.Я6 = this.ySetting().getValue();
         this.Я_ = this.rotationAngle(var1, var2, var3, var4, var5);
      }

      private void selectPreset(int var1) {
         if (var1 >= 0 && var1 < this.Щ.size()) {
            this.В_bbefb8af.еЙ.setClampedValue(var1);
            this.applyPresetValues(this.Щ.get(var1));
            this.storePresets();
            this.Р.setImmediately(0.0F);
         }
      }

      private void drawSwitch(float var1, float var2, float var3) {
         UiDrawing.drawRoundedRect(var1, var2, ЯИ0, ЯИх, ЯИУ, ColorMath.lerpArgb(ColorMath.withAlpha(В3, ЯИЗ), В3, var3));
         UiDrawing.drawRoundedRect(var1 + Mth.lerpInt(var3, 2, 15), var2 + ЯИЕ, ЯИП, ЯИ西, ЯИд, В7);
      }

      @Override
      public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
         Vector2f var9 = WorldProjection.guiToScreen(var1, var3);
         if (inside(var9.x, var9.y, this.Яи, this.Я9 + ЯИж, ЯИ弟, ЯИб)) {
            float var10 = Math.max(0, this.Щ.size() * 40 - 193);
            this.Я7 = Math.clamp(this.Я7 - (float)var7 * ЯИЫ, 0.0F, var10);
            return true;
         }

         if (this.В_8b72c019 != SwingAnimations.EditorScreen.EditorTab.PREVIEW
            && inside(var9.x, var9.y, this.editorX() + ЯИl, this.Я9 + ЯИ8, ЯИ5, ЯИв)) {
            if (this.shiftDown()) {
               this.rollSetting().setClampedValue(this.rollSetting().getValue() + (float)var7 * ЯИъ);
            } else {
               this.zSetting().setClampedValue(this.zSetting().getValue() + (float)var7 * ЯИМ);
            }

            return true;
         } else {
            return true;
         }
      }

      private FloatSetting yawSetting() {
         return this.В_8b72c019 == SwingAnimations.EditorScreen.EditorTab.START ? this.В_bbefb8af.ер : this.В_bbefb8af.еЖ;
      }

      private void selectEditorTab(SwingAnimations.EditorScreen.EditorTab var1) {
         if (this.В_8b72c019 != var1) {
            this.В_8b72c019 = var1;
            this.В_238df2f2 = SwingAnimations.EditorScreen.TransformAxis.NONE;
            this.Р.setImmediately(ЯИч);
         }
      }

      private void drawSwordControl(float var1, float var2, float var3, float var4) {
         SwingAnimations.EditorScreen.TransformAxis var5 = this.В_238df2f2 == SwingAnimations.EditorScreen.TransformAxis.NONE
            ? this.hitRotationRing(var3, var4, var1, var2)
            : this.В_238df2f2;
         int var6 = ColorMath.packArgb(242, 104, 124, Я衣Ж);
         int var7 = ColorMath.packArgb(91, 210, 151, Я衣м);
         int var8 = ColorMath.packArgb(99, 158, 255, Я衣К);
         this.drawRotationRing(var1, var2, Я衣I, Я衣Т, var6, var5 == SwingAnimations.EditorScreen.TransformAxis.ROTATE_X);
         this.drawRotationRing(var1, var2, Я衣О, Я衣Ф, var7, var5 == SwingAnimations.EditorScreen.TransformAxis.ROTATE_Y);
         this.drawRotationRing(var1, var2, Я衣Й, Я衣к, var8, var5 == SwingAnimations.EditorScreen.TransformAxis.ROTATE_Z);
         this.drawAxisBadge(Я衣г, var1, var2 - Я衣0, var6, var5 == SwingAnimations.EditorScreen.TransformAxis.ROTATE_X);
         this.drawAxisBadge(Я衣х, var1 + Я衣У, var2, var7, var5 == SwingAnimations.EditorScreen.TransformAxis.ROTATE_Y);
         this.drawAxisBadge(Я衣З, var1 - Я衣Е, var2 + Я衣П, var8, var5 == SwingAnimations.EditorScreen.TransformAxis.ROTATE_Z);
      }

      private FloatSetting ySetting() {
         return this.В_8b72c019 == SwingAnimations.EditorScreen.EditorTab.START ? this.В_bbefb8af.е7 : this.В_bbefb8af.еШ;
      }

      private static void set(FloatSetting var0, float var1) {
         var0.setClampedValue(var1);
      }

      private float ellipseDistance(float var1, float var2, float var3, float var4) {
         float var5 = (float)Math.sqrt(var1 * var1 / (var3 * var3) + var2 * var2 / (var4 * var4));
         return Math.abs(var5 - 1.0F) * Math.min(var3, var4);
      }

      private SwingAnimations.EditorScreen.Pose readTransform(boolean var1) {
         return new SwingAnimations.EditorScreen.Pose(
            (var1 ? this.В_bbefb8af.еO : this.В_bbefb8af.е_).getValue(),
            (var1 ? this.В_bbefb8af.е7 : this.В_bbefb8af.еШ).getValue(),
            (var1 ? this.В_bbefb8af.еР : this.В_bbefb8af.еы).getValue(),
            (var1 ? this.В_bbefb8af.е3 : this.В_bbefb8af.ей).getValue(),
            (var1 ? this.В_bbefb8af.ер : this.В_bbefb8af.еЖ).getValue(),
            (var1 ? this.В_bbefb8af.е6 : this.В_bbefb8af.ем).getValue()
         );
      }

      private static float distance(float var0, float var1, float var2, float var3) {
         float var4 = var0 - var2;
         float var5 = var1 - var3;
         return (float)Math.sqrt(var4 * var4 + var5 * var5);
      }

      private FloatSetting pitchSetting() {
         return this.В_8b72c019 == SwingAnimations.EditorScreen.EditorTab.START ? this.В_bbefb8af.е3 : this.В_bbefb8af.ей;
      }

      @Override
      public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
         Vector2f var3 = WorldProjection.guiToScreen(var1.x(), var1.y());
         if (inside(var3.x, var3.y, this.Яи, this.Я9, Яш诶, Яш1)) {
            return this.presetsClicked(var3.x, var3.y, var1.button());
         }

         if (inside(var3.x, var3.y, this.mainX(), this.Я9, ЯшЬ, Яш衣)) {
            return this.mainClicked(var3.x, var3.y, var1.button());
         }

         if (inside(var3.x, var3.y, this.editorX(), this.Я9, Яшш, ЯшИ)) {
            return this.editorClicked(var3.x, var3.y, var1.button());
         }

         this.сO = false;
         return true;
      }

      @Override
      public void onClose() {
         this.minecraft.gui.setScreen(this.е);
      }

      @Override
      public boolean charTyped(CharacterEvent var1) {
         if (this.сO && this.с弟.length() < 18) {
            int var2 = var1.codepoint();
            if (!Character.isISOControl(var2)) {
               this.с弟 = this.с弟 + var1.codepointAsString();
            }

            return true;
         } else {
            return true;
         }
      }

      private enum EditorTab {
         START("Start"),
         END("End"),
         PREVIEW("Preview");

         private final String Ям;

         EditorTab(String var3) {
            this.Ям = var3;
         }
      }

      private record Preset(
         String name,
         float startX,
         float startY,
         float startZ,
         float startPitch,
         float startYaw,
         float startRoll,
         float endX,
         float endY,
         float endZ,
         float endPitch,
         float endYaw,
         float endRoll,
         float curve1X,
         float curve1Y,
         float curve2X,
         float curve2Y,
         boolean back,
         float backSpeed
      ) {
      }

      private record Pose(float x, float y, float z, float pitch, float yaw, float roll) {
         private static SwingAnimations.EditorScreen.Pose lerp(
            SwingAnimations.EditorScreen.Pose var0, SwingAnimations.EditorScreen.Pose var1, float var2
         ) {
            return new SwingAnimations.EditorScreen.Pose(
               Mth.lerp(var2, var0.x, var1.x),
               Mth.lerp(var2, var0.y, var1.y),
               Mth.lerp(var2, var0.z, var1.z),
               Mth.lerp(var2, var0.pitch, var1.pitch),
               Mth.lerp(var2, var0.yaw, var1.yaw),
               Mth.lerp(var2, var0.roll, var1.roll)
            );
         }
      }

      private enum TransformAxis {
         NONE,
         MOVE,
         ROTATE_X,
         ROTATE_Y,
         ROTATE_Z;
      }
   }
}
