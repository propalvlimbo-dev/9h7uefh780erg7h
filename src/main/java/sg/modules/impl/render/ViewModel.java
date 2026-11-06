package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import settings.render.ClientLocalization;
import settings.render.HmiIntegration;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.ViewModelEditorCompositor;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ActionSetting;
import sg.settings.FloatSetting;
import sg.events.impl.HeldItemPoseEvent;

public final class ViewModel extends ClientModule {
   private static final float ЮН = 0.65F;
   public static volatile boolean АI;
   public static RenderTarget Ъ;
   private static volatile ViewModel.CoordinateSpace В = ViewModel.CoordinateSpace.ITEM;
   @SerializedName("п")
   private final ActionSetting п_at_b4de284f;
   @SerializedName("еь")
   private final FloatSetting еь_989fed9a;
   private final FloatSetting еж;
   private final FloatSetting е弟;
   private final FloatSetting еб;
   private final FloatSetting еЫ;
   private final FloatSetting еl;
   private final FloatSetting е8;
   private final FloatSetting е5;
   private final FloatSetting ев;
   private final FloatSetting еъ;
   private final FloatSetting еМ;
   private final FloatSetting еН;
   private final FloatSetting ен;
   private final FloatSetting еЦ;
   private final FloatSetting еч;
   private final FloatSetting еЭ;
   @SerializedName("еь")
   private final EventListener<HeldItemPoseEvent> еь_f8db0d09;
   private boolean АТ;
   private float Юн;
   private float ЮЦ;
   private float Юч;
   private float ЮЭ;
   private float Ю必;
   private float Ющ;
   private volatile boolean АО;
   private volatile boolean АФ;
   private static final String с_诶 = "ViewModel";
   private static final String с_1 = "Положение предметов в руках";
   private static final String с_Ь = "Настроить положение";
   private static final String с_衣 = "Основная рука X";
   private static final float с_ш = -2.5F;
   private static final float с_И = 2.5F;
   private static final float с_ь = 0.05F;
   private static final String с_ж = "Основная рука Y";
   private static final float с_弟 = -2.5F;
   private static final float с_б = 2.5F;
   private static final float с_Ы = 0.05F;
   private static final String с_l = "Основная рука Z";
   private static final float с_8 = -2.5F;
   private static final float с_5 = 2.5F;
   private static final float с_в = 0.05F;
   private static final String с_ъ = "Основная рука масштаб";
   private static final float с_М = 0.35F;
   private static final float с_Н = 0.05F;
   private static final String с_н = "Левая рука X";
   private static final float с_Ц = -2.5F;
   private static final float с_ч = 2.5F;
   private static final float с_Э = 0.05F;
   private static final String с_必 = "Левая рука Y";
   private static final float с_щ = -2.5F;
   private static final float с_Г = 2.5F;
   private static final float с_л = 0.05F;
   private static final String с_э = "Левая рука Z";
   private static final float с_ю = -2.5F;
   private static final float с_Щ = 2.5F;
   private static final float с_у = 0.05F;
   private static final String с_2 = "Левая рука масштаб";
   private static final float с_ф = 0.35F;
   private static final float с_и = 0.05F;
   private static final String с_9 = "Основная рука отдельно X";
   private static final float с_о = -2.5F;
   private static final float с_O = 2.5F;
   private static final String с_7 = "Основная рука отдельно Y";
   private static final float с_Р = -2.5F;
   private static final float с_3 = 2.5F;
   private static final String с_р = "Предмет основной руки X";
   private static final float с_6 = -2.5F;
   private static final float с__ = 2.5F;
   private static final String с_Ш = "Предмет основной руки Y";
   private static final float с_ы = -2.5F;
   private static final float с_й = 2.5F;
   private static final String с_Ж = "Левая рука отдельно X";
   private static final float с_м = -2.5F;
   private static final float с_К = 2.5F;
   private static final String с_I = "Левая рука отдельно Y";
   private static final float с_Т = -2.5F;
   private static final float с_О = 2.5F;
   private static final String с_Ф = "Предмет левой руки X";
   private static final float с_Й = -2.5F;
   private static final float с_к = 2.5F;
   private static final String с_г = "Предмет левой руки Y";
   private static final float с_0 = -2.5F;
   private static final float с_х = 2.5F;
   private static final double с_У = 2.0;
   private static final double с_З = 0.5;
   private static final float с_Е = 0.72F;
   private static final float с_П = 0.28F;
   private static final float с_西 = 0.74F;

   private Vector2f л(InteractionHand var1) {
      boolean var2 = var1 == InteractionHand.MAIN_HAND;
      FloatSetting var3 = var2 ? this.еь_989fed9a : this.еЫ;
      FloatSetting var4 = var2 ? this.еж : this.еl;
      float var5 = Math.max(1, MINECRAFT_CLIENT.getWindow().getScreenWidth());
      float var6 = Math.max(1, MINECRAFT_CLIENT.getWindow().getScreenHeight());
      float var7 = MINECRAFT_CLIENT.options.fov().get().intValue();
      float var8 = (float)(var6 / (с_У * Math.tan(Math.toRadians(var7) * с_З)));
      float var9 = (var2 ? с_Е : с_П) + var3.getValue() * var8 / var5;
      float var10 = с_西 - var4.getValue() * var8 / var6;
      return new Vector2f(var9, 1.0F - var10);
   }

   public static boolean 弟O() {
      return АI && В == ViewModel.CoordinateSpace.ITEM;
   }

   public void л(InteractionHand var1, ItemStack var2, PoseStack var3) {
      if (var1 != InteractionHand.MAIN_HAND || !(var2.getItem() instanceof CrossbowItem)) {
         if (var1 == InteractionHand.MAIN_HAND) {
            var3.translate(this.еь_989fed9a.getValue(), this.еж.getValue(), this.е弟.getValue());
         } else {
            var3.translate(this.еЫ.getValue(), this.еl.getValue(), this.е8.getValue());
         }
      }
   }

   public void Ъ(InteractionHand var1, boolean var2, PoseStack var3) {
      boolean var4 = var1 == InteractionHand.MAIN_HAND;
      float var5 = (var2 ? (var4 ? this.еМ : this.еч) : (var4 ? this.ев : this.ен)).getValue();
      float var6 = (var2 ? (var4 ? this.еН : this.еЭ) : (var4 ? this.еъ : this.еЦ)).getValue();
      var3.translate(var5, var6, 0.0F);
   }

   public void л(InteractionHand var1, boolean var2, PoseStack var3) {
      boolean var4 = var1 == InteractionHand.MAIN_HAND;
      float var5 = (var2 ? (var4 ? this.еМ : this.еч) : (var4 ? this.ев : this.ен)).getValue();
      float var6 = (var2 ? (var4 ? this.еН : this.еЭ) : (var4 ? this.еъ : this.еЦ)).getValue();
      Vector3f var7 = MINECRAFT_CLIENT.gameRenderer.mainCamera().rotation().transform(new Vector3f(var5, var6, 0.0F));
      Matrix4f var8 = var3.last().pose();
      var8.m30(var8.m30() + var7.x);
      var8.m31(var8.m31() + var7.y);
      var8.m32(var8.m32() + var7.z);
   }

   public ViewModel() {
      super(с_诶, с_1, ModuleCategory.RENDER);
      this.п_at_b4de284f = new ActionSetting(с_Ь).onClick(this::кШ).showOtherSettings();
      this.еь_989fed9a = new FloatSetting(с_衣, 0.0F, с_ш, с_И).withStep(с_ь);
      this.еж = new FloatSetting(с_ж, 0.0F, с_弟, с_б).withStep(с_Ы);
      this.е弟 = new FloatSetting(с_l, 0.0F, с_8, с_5).withStep(с_в);
      this.еб = new FloatSetting(с_ъ, 1.0F, с_М, 2.0F).withStep(с_Н);
      this.еЫ = new FloatSetting(с_н, 0.0F, с_Ц, с_ч).withStep(с_Э);
      this.еl = new FloatSetting(с_必, 0.0F, с_щ, с_Г).withStep(с_л);
      this.е8 = new FloatSetting(с_э, 0.0F, с_ю, с_Щ).withStep(с_у);
      this.е5 = new FloatSetting(с_2, 1.0F, с_ф, 2.0F).withStep(с_и);
      this.ев = new FloatSetting(с_9, 0.0F, с_о, с_O).visibleWhen(() -> false);
      this.еъ = new FloatSetting(с_7, 0.0F, с_Р, с_3).visibleWhen(() -> false);
      this.еМ = new FloatSetting(с_р, 0.0F, с_6, с__).visibleWhen(() -> false);
      this.еН = new FloatSetting(с_Ш, 0.0F, с_ы, с_й).visibleWhen(() -> false);
      this.ен = new FloatSetting(с_Ж, 0.0F, с_м, с_К).visibleWhen(() -> false);
      this.еЦ = new FloatSetting(с_I, 0.0F, с_Т, с_О).visibleWhen(() -> false);
      this.еч = new FloatSetting(с_Ф, 0.0F, с_Й, с_к).visibleWhen(() -> false);
      this.еЭ = new FloatSetting(с_г, 0.0F, с_0, с_х).visibleWhen(() -> false);
      this.еь_f8db0d09 = this::л;
      this.addSettings(
         this.п_at_b4de284f,
         this.еь_989fed9a,
         this.еж,
         this.е弟,
         this.еб,
         this.еЫ,
         this.еl,
         this.е8,
         this.е5,
         this.ев,
         this.еъ,
         this.еМ,
         this.еН,
         this.ен,
         this.еЦ,
         this.еч,
         this.еЭ
      );
   }

   public boolean 弟К() {
      if (MINECRAFT_CLIENT.gui.screen() instanceof ViewModel.EditorScreen && !this.АТ && MINECRAFT_CLIENT.gameRenderer.mainRenderTarget() != null) {
         RenderTarget var1 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
         ViewModelEditorCompositor.В.ы(Math.max(1, var1.width), Math.max(1, var1.height));
         В = В == ViewModel.CoordinateSpace.HAND ? ViewModel.CoordinateSpace.ITEM : ViewModel.CoordinateSpace.HAND;
         Ъ = В == ViewModel.CoordinateSpace.HAND ? ViewModelEditorCompositor.В.Я() : ViewModelEditorCompositor.В.method_4();
         АI = Ъ != null;
         this.АТ = АI;
         return АI;
      } else {
         return false;
      }
   }

   private void кШ() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (!this.isEnabled()) {
            this.enable();
         }

         MINECRAFT_CLIENT.gui.setScreen(new ViewModel.EditorScreen(this));
      }
   }

   private void Ъ(InteractionHand var1, ItemStack var2, PoseStack var3) {
      if (var1 != InteractionHand.MAIN_HAND || !(var2.getItem() instanceof CrossbowItem)) {
         boolean var4 = var1 == InteractionHand.MAIN_HAND;
         float var5 = (var4 ? this.еь_989fed9a : this.еЫ).getValue();
         float var6 = (var4 ? this.еж : this.еl).getValue();
         float var7 = (var4 ? this.е弟 : this.е8).getValue();
         var3.translate(var5, var6, var7);
      }
   }

   private void л(HeldItemPoseEvent var1) {
      if (HmiIntegration.п弟()) {
         this.л(var1.hand(), var1.stack(), var1.matrices());
      } else {
         this.Ъ(var1.hand(), var1.stack(), var1.matrices());
      }
   }

   @Override
   public void onDisable() {
      АI = false;
      Ъ = null;
      this.АТ = false;
      this.АО = false;
      this.АФ = false;
      ViewModelEditorCompositor.В.о弟();
   }

   public void к诶() {
      if (this.АТ) {
         АI = false;
         Ъ = null;
         this.АТ = false;
         if (MINECRAFT_CLIENT.gui.screen() instanceof ViewModel.EditorScreen && MINECRAFT_CLIENT.gameRenderer.mainRenderTarget() != null) {
            ViewModelEditorCompositor.В.Н(Math.round(this.Ю必), Math.round(MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().height - this.Ющ - 1.0F), var1 -> this.АО = var1);
            ViewModelEditorCompositor.В.Я(Math.round(this.Ю必), Math.round(MINECRAFT_CLIENT.gameRenderer.mainRenderTarget().height - this.Ющ - 1.0F), var1 -> this.АФ = var1);
            ViewModelEditorCompositor.В
               .Н(
                  MINECRAFT_CLIENT.gameRenderer.mainRenderTarget(),
                  this.Юн,
                  this.ЮЦ,
                  this.Юч,
                  this.ЮЭ,
                  this.л(InteractionHand.MAIN_HAND),
                  this.л(InteractionHand.OFF_HAND),
                  ThemeEditor.getColorArgb(ThemeColorSlot.MAIN)
               );
         }
      }
   }

   public void л(InteractionHand var1, PoseStack var2) {
      float var3 = var1 == InteractionHand.MAIN_HAND ? this.еб.getValue() : this.е5.getValue();
      var2.scale(var3, var3, var3);
   }

   public static boolean 弟Ю() {
      return АI && В == ViewModel.CoordinateSpace.HAND;
   }

   private static final class EditorScreen extends ChatScreen {
      private final ViewModel Я;
      private final FloatAnimation сЯ;
      private final FloatAnimation сХ;
      private final FloatAnimation се;
      private final FloatAnimation сА;
      @SerializedName("4")
      private InteractionHand field_4;
      private ViewModel.EditorScreen.Part В;
      private float пl;
      private float п8;
      private float п5;
      private float пв;
      private float пъ;
      private float пМ;
      private long Ял;
      private static final float Я西а = 10.0F;
      private static final float Я西Ю = 10.0F;
      private static final float Я西Ч = 10.0F;
      private static final float Я西Л = 10.0F;
      private static final float Я西诶 = 600.0F;
      private static final float Я西1 = 0.5F;
      private static final float Я西Ь = 24.0F;
      private static final String Я西衣 = "Редактор ViewModel";
      private static final String Я西ш = "Тяни руку или предмет отдельно • Ctrl + наведение — выбрать и двигать вместе • колесо — масштаб";
      private static final float Я西И = 0.5F;
      private static final float Я西ь = 11.0F;
      private static final double Я西ж = 255.0;
      private static final float Я西弟 = 0.5F;
      private static final float Я西б = 34.0F;
      private static final double Я西Ы = 255.0;
      private static final double Я西l = 2.0;
      private static final double Я西8 = 0.5;
      private static final float Я西5 = 0.65F;
      private static final float Я西в = 0.1F;
      private static final float Я西ъ = 0.1F;
      private static final float Я西М = 310.0F;
      private static final float Я西Н = 370.0F;
      private static final float Я西н = 0.5F;
      private static final float Я西Ц = 0.5F;
      private static final float Я西ч = 0.016666668F;
      private static final float Я西Э = 1.0E9F;
      private static final float Я西必 = 0.05F;
      private static final float Я西щ = -15.0F;

      private InteractionHand ownerHand(float var1, float var2) {
         ViewModel.EditorScreen.HandBounds var3 = this.bounds(InteractionHand.MAIN_HAND);
         ViewModel.EditorScreen.HandBounds var4 = this.bounds(InteractionHand.OFF_HAND);
         return var3.distanceSquared(var1, var2) <= var4.distanceSquared(var1, var2) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
      }

      @Override
      public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
         if (this.field_4 == null) {
            return super.mouseDragged(var1, var2, var4);
         }

         Vector2f var6 = WorldProjection.guiToScreen(var1.x(), var1.y());
         float var7 = var6.x - this.пl;
         float var8 = var6.y - this.п8;
         FloatSetting var9 = this.xSetting(this.field_4, this.В);
         FloatSetting var10 = this.ySetting(this.field_4, this.В);
         float var11 = Math.max(1, this.minecraft.getWindow().getScreenHeight());
         float var12 = this.minecraft.options.fov().get().intValue();
         float var13 = (float)(var11 / (Я西l * Math.tan(Math.toRadians(var12) * Я西8)));
         float var14 = Я西5 / Math.max(1.0F, var13);
         var9.setClampedValue(this.п5 + var7 * var14);
         var10.setClampedValue(this.пв - var8 * var14);
         return true;
      }

      @Override
      public boolean isPauseScreen() {
         return false;
      }

      private EditorScreen(ViewModel var1) {
         super("", false);
         this.сЯ = new FloatAnimation(0.0F, Я西а, Easings.CUBIC_OUT);
         this.сХ = new FloatAnimation(0.0F, Я西Ю, Easings.CUBIC_OUT);
         this.се = new FloatAnimation(0.0F, Я西Ч, Easings.CUBIC_OUT);
         this.сА = new FloatAnimation(0.0F, Я西Л, Easings.CUBIC_OUT);
         this.Я = var1;
         this.пъ = var1.еб.getValue();
         this.пМ = var1.е5.getValue();
      }

      private FloatSetting xSetting(InteractionHand var1, ViewModel.EditorScreen.Part var2) {
         if (var2 == ViewModel.EditorScreen.Part.GROUP) {
            return var1 == InteractionHand.MAIN_HAND ? this.Я.еь_989fed9a : this.Я.еЫ;
         } else if (var2 == ViewModel.EditorScreen.Part.ITEM) {
            return var1 == InteractionHand.MAIN_HAND ? this.Я.еМ : this.Я.еч;
         } else {
            return var1 == InteractionHand.MAIN_HAND ? this.Я.ев : this.Я.ен;
         }
      }

      @Override
      public void removed() {
         this.Я.Юн = 0.0F;
         this.Я.ЮЦ = 0.0F;
         this.Я.Юч = 0.0F;
         this.Я.ЮЭ = 0.0F;
         this.Я.АО = false;
         this.Я.АФ = false;
         super.removed();
      }

      @Override
      public boolean mouseReleased(MouseButtonEvent var1) {
         if (var1.button() == 0 && this.field_4 != null) {
            this.field_4 = null;
            this.В = null;
            return true;
         } else {
            return super.mouseReleased(var1);
         }
      }

      private void updateScaleAnimation() {
         long var1 = System.nanoTime();
         float var3 = this.Ял == 0L ? Я西ч : Math.clamp((float)(var1 - this.Ял) / Я西Э, 0.0F, Я西必);
         this.Ял = var1;
         float var4 = 1.0F - (float)Math.exp(Я西щ * var3);
         this.Я.еб.setClampedValue(this.Я.еб.getValue() + (this.пъ - this.Я.еб.getValue()) * var4);
         this.Я.е5.setClampedValue(this.Я.е5.getValue() + (this.пМ - this.Я.е5.getValue()) * var4);
      }

      private void drawHelp() {
         float var1 = this.minecraft.getWindow().getScreenWidth();
         float var2 = Я西诶;
         float var3 = (var1 - var2) * Я西1;
         float var4 = Я西Ь;
         String var5 = ClientLocalization.translate(Я西衣);
         String var6 = ClientLocalization.translate(Я西ш);
         UiDrawing.drawText(
            ClientFonts.getMediumFont(),
            var5,
            var3 + (var2 - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var5, 15)) * Я西И,
            var4 + Я西ь,
            15,
            ColorMath.packArgb(245, 247, 252, Я西ж)
         );
         UiDrawing.drawText(
            ClientFonts.getRegularFont(),
            var6,
            var3 + (var2 - UiDrawing.getTextWidth(ClientFonts.getRegularFont(), var6, 12)) * Я西弟,
            var4 + Я西б,
            12,
            ColorMath.packArgb(172, 181, 198, Я西Ы)
         );
      }

      private FloatSetting ySetting(InteractionHand var1, ViewModel.EditorScreen.Part var2) {
         if (var2 == ViewModel.EditorScreen.Part.GROUP) {
            return var1 == InteractionHand.MAIN_HAND ? this.Я.еж : this.Я.еl;
         } else if (var2 == ViewModel.EditorScreen.Part.ITEM) {
            return var1 == InteractionHand.MAIN_HAND ? this.Я.еН : this.Я.еЭ;
         } else {
            return var1 == InteractionHand.MAIN_HAND ? this.Я.еъ : this.Я.еЦ;
         }
      }

      @Override
      public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
         super.extractRenderState(var1, var2, var3, var4);
         UiDrawing.setTransform(new Matrix4f());
         UiDrawing.disableScissor();
         this.updateScaleAnimation();
         Vector2f var5 = WorldProjection.transformScreenPoint(var2, var3);
         this.Я.Ю必 = var5.x;
         this.Я.Ющ = var5.y;
         ViewModel.EditorScreen.Selection var6 = this.hoveredSelection(var5.x, var5.y);
         boolean var7 = var6 != null && var6.hand == InteractionHand.MAIN_HAND;
         boolean var8 = var6 != null && var6.hand == InteractionHand.OFF_HAND;
         boolean var9 = var6 != null && var6.part == ViewModel.EditorScreen.Part.GROUP;
         this.сЯ
            .updateTarget(
               (!var7 || !var9 && var6.part != ViewModel.EditorScreen.Part.HAND)
                     && (
                        this.field_4 != InteractionHand.MAIN_HAND
                           || this.В != ViewModel.EditorScreen.Part.GROUP
                              && this.В != ViewModel.EditorScreen.Part.HAND
                     )
                  ? 0.0F
                  : 1.0F
            );
         this.сХ
            .updateTarget(
               (!var7 || !var9 && var6.part != ViewModel.EditorScreen.Part.ITEM)
                     && (
                        this.field_4 != InteractionHand.MAIN_HAND
                           || this.В != ViewModel.EditorScreen.Part.GROUP
                              && this.В != ViewModel.EditorScreen.Part.ITEM
                     )
                  ? 0.0F
                  : 1.0F
            );
         this.се
            .updateTarget(
               (!var8 || !var9 && var6.part != ViewModel.EditorScreen.Part.HAND)
                     && (
                        this.field_4 != InteractionHand.OFF_HAND
                           || this.В != ViewModel.EditorScreen.Part.GROUP
                              && this.В != ViewModel.EditorScreen.Part.HAND
                     )
                  ? 0.0F
                  : 1.0F
            );
         this.сА
            .updateTarget(
               (!var8 || !var9 && var6.part != ViewModel.EditorScreen.Part.ITEM)
                     && (
                        this.field_4 != InteractionHand.OFF_HAND
                           || this.В != ViewModel.EditorScreen.Part.GROUP
                              && this.В != ViewModel.EditorScreen.Part.ITEM
                     )
                  ? 0.0F
                  : 1.0F
            );
         this.Я.Юн = this.сЯ.getValue();
         this.Я.ЮЦ = this.сХ.getValue();
         this.Я.Юч = this.се.getValue();
         this.Я.ЮЭ = this.сА.getValue();
         this.drawHelp();
      }

      private ViewModel.EditorScreen.Selection hoveredSelection(float var1, float var2) {
         ViewModel.EditorScreen.Part var3 = this.Я.АФ
            ? ViewModel.EditorScreen.Part.ITEM
            : (this.Я.АО ? ViewModel.EditorScreen.Part.HAND : null);
         if (var3 == null) {
            return null;
         }

         if (this.controlDown()) {
            var3 = ViewModel.EditorScreen.Part.GROUP;
         }

         return new ViewModel.EditorScreen.Selection(this.ownerHand(var1, var2), var3);
      }

      @Override
      public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
         if (var1.button() == 0) {
            Vector2f var3 = WorldProjection.guiToScreen(var1.x(), var1.y());
            ViewModel.EditorScreen.Selection var4 = this.hoveredSelection(var3.x, var3.y);
            if (var4 != null) {
               this.field_4 = var4.hand;
               this.В = var4.part;
               this.пl = var3.x;
               this.п8 = var3.y;
               this.п5 = this.xSetting(var4.hand, var4.part).getValue();
               this.пв = this.ySetting(var4.hand, var4.part).getValue();
               return true;
            }
         }

         return super.mouseClicked(var1, var2);
      }

      private ViewModel.EditorScreen.HandBounds bounds(InteractionHand var1) {
         boolean var2 = var1 == InteractionHand.MAIN_HAND;
         FloatSetting var3 = var2 ? this.Я.еб : this.Я.е5;
         float var4 = this.minecraft.getWindow().getScreenWidth();
         float var5 = this.minecraft.getWindow().getScreenHeight();
         float var6 = var3.getValue();
         Vector2f var7 = this.Я.л(var1);
         float var8 = var7.x * var4;
         float var9 = (1.0F - var7.y) * var5;
         float var10 = Я西М * var6;
         float var11 = Я西Н * var6;
         return new ViewModel.EditorScreen.HandBounds(var1, var8 - var10 * Я西н, var9 - var11 * Я西Ц, var10, var11);
      }

      @Override
      public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
         Vector2f var9 = WorldProjection.guiToScreen(var1, var3);
         ViewModel.EditorScreen.Selection var10 = this.hoveredSelection(var9.x, var9.y);
         if (var10 != null) {
            if (var10.hand == InteractionHand.MAIN_HAND) {
               this.пъ = Math.clamp(this.пъ + (float)var7 * Я西в, this.Я.еб.getMinimum(), this.Я.еб.getMaximum());
            } else {
               this.пМ = Math.clamp(this.пМ + (float)var7 * Я西ъ, this.Я.е5.getMinimum(), this.Я.е5.getMaximum());
            }

            return true;
         } else {
            return super.mouseScrolled(var1, var3, var5, var7);
         }
      }

      private boolean controlDown() {
         long var1 = this.minecraft.getWindow().handle();
         return GLFW.glfwGetKey(var1, 341) == 1 || GLFW.glfwGetKey(var1, 345) == 1;
      }

      private record Selection(InteractionHand hand, ViewModel.EditorScreen.Part part) {
      }

      private record HandBounds(InteractionHand hand, float x, float y, float width, float height) {
         private float distanceSquared(float var1, float var2) {
            float var3 = this.x + this.width * 0.5F;
            float var4 = this.y + this.height * 0.5F;
            float var5 = var1 - var3;
            float var6 = var2 - var4;
            return var5 * var5 + var6 * var6;
         }
      }

      private enum Part {
         HAND,
         ITEM,
         GROUP;
      }
   }

   private enum CoordinateSpace {
      HAND,
      ITEM;
   }
}
