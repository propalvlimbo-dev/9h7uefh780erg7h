package sg.settings;

import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.Window;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import settings.render.ClientLocalization;
import sg.ec.MinecraftAccess;
import sg.ec.ClientFonts;
import sg.modules.impl.render.EntityESP;
import sg.gui.widgets.FloatSettingWidget;
import sg.gui.widgets.BooleanSettingWidget;
import sg.util.math.EasingFunctions;
import sg.gui.click.ClickGuiScreen;
import sg.settings.ValueSetting;
import sg.settings.ColorSetting;
import sg.settings.SingleChoiceSetting;
import sg.gui.widgets.SingleChoiceSettingWidget;
import sg.settings.FloatSetting;
import sg.render.WorldProjection;
import sg.render.pipeline.PreviewFade;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.gui.widgets.ColorSettingWidget;
import sg.gui.widgets.SettingWidget;
import sg.animation.Easings;
import sg.animation.FloatAnimation;

public final class EntityEspSettingsPanel implements MinecraftAccess {
   private static final float field_4Э = 0.0F;
   private static final float field_4必 = 0.0F;
   private static final float field_4щ = 48.0F;
   private static final float field_4Г = 0.97F;
   private static final float field_4л = 70.0F;
   private static final float field_4э = 25.0F;
   private static final float field_4ю = 3.0F;
   private static final float field_4Щ = 4.0F;
   private static final float field_4у = 0.8F;
   private static final float field_42 = 10.0F;
   private static final float field_4ф = 12.0F;
   private static final float field_4и = 5.0F;
   private static final float field_49 = 26.0F;
   private final ClickGuiScreen Х;
   private final FloatAnimation з;
   private final FloatAnimation ц;
   private final FloatAnimation я;
   private final ArrayList<SettingWidget<? extends ValueSetting<?>>> Д;
   @SerializedName("Я")
   private EntityESP Я_aaf997af;
   @SerializedName("В")
   private EntityEspStyleSettings В_ac031899;
   @SerializedName("В")
   private EntityEspSettingsPanel.PreviewCategory В_44db94ff;
   @SerializedName("В")
   private EntityEspSettingsPanel.PlayerScope В_e57973e4;
   private final FloatAnimation[] е;
   @SerializedName("Я")
   private EntityEspSettingsPanel.PreviewCategory Я_44db94ff;
   @SerializedName("В")
   private RemotePlayer В_07050678;
   @SerializedName("В")
   private Zombie В_f20cde87;
   @SerializedName("В")
   private Villager В_ef75dc81;
   @SerializedName("Я")
   private ItemEntity Я_3a07ee9c;
   private int АК;
   private boolean ЯI;
   private boolean ЯТ;
   @SerializedName("4о")
   private float field_4о;
   @SerializedName("4O")
   private float field_4O;
   private long ЯЛ;
   private long Я诶;
   @SerializedName("47")
   private float field_47;
   @SerializedName("4Р")
   private float field_4Р;
   @SerializedName("43")
   private float field_43;
   @SerializedName("4р")
   private float field_4р;
   @SerializedName("46")
   private float field_46;
   @SerializedName("4_")
   private float field_4_;
   @SerializedName("4Ш")
   private float field_4Ш;
   private boolean ЯО;
   private boolean ЯФ;
   private static final float сП衣 = 5.5F;
   private static final float сПш = 10.0F;
   private static final float сПИ = 14.0F;
   private static final float сПь = 170.0F;
   private static final float сПж = 170.0F;
   private static final float сП弟 = 180.0F;
   private static final float сПб = 60.0F;
   private static final float сПЫ = 344.0F;
   private static final float сПl = 20.0F;
   private static final float сП8 = 324.0F;
   private static final float сП5 = 324.0F;
   private static final float сПв = 16.0F;
   private static final float сПъ = 162.0F;
   private static final float сПМ = 18.0F;
   private static final float сПН = 0.029999971F;
   private static final float сПн = 0.97F;
   private static final float сПЦ = 0.029999971F;
   private static final float сПч = 48.0F;
   private static final float сПЭ = 48.0F;
   private static final String сП必 = "EntityESP";
   private static final float сПщ = 184.0F;
   private static final float сПГ = 12.0F;
   private static final String сПл = "Подсвечивает выбранные сущности";
   private static final float сПэ = 184.0F;
   private static final float сПю = 12.0F;
   private static final float сПЩ = 23.5F;
   private static final float сПу = 170.0F;
   private static final float сП2 = 170.0F;
   private static final float сПф = 314.0F;
   private static final float сПи = 28.0F;
   private static final float сП9 = 70.0F;
   private static final float сПо = 8.0F;
   private static final double сПO = 32.0;
   private static final float сП7 = 32.0F;
   private static final float сПР = 12.0F;
   private static final float сП3 = 10.0F;
   private static final float сПр = 5.0F;
   private static final float сП6 = 10.0F;
   private static final float сП_ = 12.0F;
   private static final float сПШ = 3.0F;
   private static final float сПы = 32.0F;
   private static final double сПй = 29.0;
   private static final float сПЖ = 0.01F;
   private static final float сПм = 1.4F;
   private static final float сПК = 3.2F;
   private static final float сПI = 3.2F;
   private static final float сПТ = 0.5F;
   private static final float сПО = 0.5F;
   private static final float сПФ = 2.2F;
   private static final float сПЙ = -1.0F;
   private static final float сПк = 0.5F;
   private static final float сПг = 0.5F;
   private static final float сП0 = 0.5F;
   private static final float сПх = 0.001F;
   private static final float сПУ = 170.0F;
   private static final float сПЗ = 170.0F;
   private static final float сПЕ = 314.0F;
   private static final float сПП = 70.0F;
   private static final float сП西 = 3.0F;
   private static final float сПд = 35.0F;
   private static final float сПБ = 28.0F;
   private static final float сПС = 44.0F;
   private static final float сПз = -4.0F;
   private static final float сПц = 73.0F;
   private static final double сПя = 70.0;
   private static final double с西с = 25.0;
   private static final float с西В = 70.0F;
   private static final float с西Я = 25.0F;
   private static final float с西Х = 5.0F;
   private static final float с西е = 11.0F;
   private static final float с西А = 35.0F;
   private static final float с西4 = 7.0F;
   private static final float с西п = 170.0F;
   private static final float с西Д = 324.0F;
   private static final float с西Ъ = 170.0F;
   private static final float с西т = 12.0F;
   private static final float с西а = 76.0F;
   private static final float с西Ю = 120.0F;
   private static final float с西Ч = 24.0F;
   private static final float с西Л = 40.0F;
   private static final float с西诶 = 88.0F;
   private static final String с西1 = "Настройки: ";
   private static final float с西Ь = 22.0F;
   private static final float с西衣 = 18.0F;
   private static final float с西ш = 22.0F;
   private static final float с西И = 15.0F;
   private static final float с西ь = 0.2F;
   private static final float с西ж = 0.001F;
   private static final float с西弟 = 5.0F;
   private static final float с西б = 0.001F;
   private static final float с西Ы = 18.0F;
   private static final float с西l = 125.0F;
   private static final float с西8 = 150.0F;
   private static final float с西5 = (float) Math.PI;
   private static final int с西в = 15728880;
   private static final float с西ъ = 180.0F;
   private static final float с西М = -0.0F;
   private static final float с西Н = 0.05F;
   private static final float с西н = 20.0F;
   private static final int с西Ц = -2147483647;
   private static final float с西ч = 170.0F;
   private static final float с西Э = 170.0F;
   private static final float с西必 = 314.0F;
   private static final float с西щ = 28.0F;
   private static final float с西Г = 70.0F;
   private static final float с西л = 0.01F;
   private static final float с西э = 3.0F;
   private static final float с西ю = 44.0F;
   private static final float с西Щ = -4.0F;
   private static final float с西у = 3.0F;
   private static final double с西2 = 25.0;
   private static final float с西ф = 8.0F;
   private static final double с西и = 32.0;
   private static final float с西9 = 26.0F;
   private static final float с西о = 1.0E9F;
   private static final float с西O = 0.05F;
   private static final float с西7 = 0.8F;
   private static final float с西Р = -10.0F;
   private static final float с西3 = 0.01F;
   private static final float с西р = 162.0F;
   private static final float с西6 = 18.0F;
   private static final float с西_ = 125.0F;
   private static final float с西Ш = 150.0F;
   private static final double с西ы = 250.0;
   private static final double с西й = 300.0;
   private static final float с西Ж = 5.0E7F;
   private static final float с西м = 360.0F;
   private static final float с西К = 180.0F;
   private static final float с西I = 360.0F;
   private static final float с西Т = -180.0F;
   private static final float с西О = 360.0F;
   private static final float с西Ф = 10.0F;
   private static final int с西Й = 16777215;
   private static final int с西к = 16777215;
   private static final int с西г = 16777215;

   private void РЬ() {
      final PlayerSkin var1 = MINECRAFT_CLIENT.player.getSkin();
      ClientLevel var10004 = MINECRAFT_CLIENT.level;
      UUID var10007 = UUID.randomUUID();
      Object var10008 = null;
      var10008 = null;
      this.В_07050678 = new RemotePlayer(var10004, new GameProfile(var10007, "Unavailable")) {
         @Override
         public PlayerSkin getSkin() {
            return var1;
         }
      };
      this.В_07050678.setHealth(с西н);
      this.В_07050678.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_SWORD));
      this.В_07050678.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
      this.В_07050678.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
      this.В_07050678.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
      this.В_07050678.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
      this.В_07050678.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS));
      this.В_f20cde87 = new Zombie(EntityTypes.ZOMBIE, MINECRAFT_CLIENT.level);
      this.В_ef75dc81 = new Villager(EntityTypes.VILLAGER, MINECRAFT_CLIENT.level);
      this.Я_3a07ee9c = new EntityEspSettingsPanel.PreviewItemEntity(MINECRAFT_CLIENT.level, new ItemStack(Items.ENDER_PEARL, 16));
      this.Я_3a07ee9c.setNoGravity(true);
      this.Я_3a07ee9c.setNeverPickUp();
      int var2 = с西Ц;

      for (Entity var6 : new Entity[]{this.В_07050678, this.В_f20cde87, this.В_ef75dc81, this.Я_3a07ee9c}) {
         while (MINECRAFT_CLIENT.level.getEntity(var2) != null) {
            var2++;
         }

         var6.setId(var2++);
      }
   }

   private void В(float var1, float var2, float var3, float var4, int var5) {
      float var6 = (var1 + var3) * сПТ;
      float var7 = (var2 + var4) * сПО;
      float var8 = сПФ;
      float var9 = (float)Math.hypot(var3 - var1, var4 - var2) + var8;
      float var10 = MINECRAFT_CLIENT.getWindow().getScreenWidth();
      float var11 = MINECRAFT_CLIENT.getWindow().getScreenHeight();
      float var12 = var6 * 2.0F / var10 - 1.0F;
      float var13 = 1.0F - var7 * 2.0F / var11;
      Matrix4f var14 = UiDrawing.getTransform();
      Matrix4f var15 = new Matrix4f(var14);

      try {
         var14.translate(var12, var13, 0.0F)
            .scale(1.0F / var10, сПЙ / var11, 1.0F)
            .rotateZ((float)Math.atan2(var4 - var2, var3 - var1))
            .scale(var10, -var11, 1.0F)
            .translate(-var12, -var13, 0.0F);
         UiDrawing.drawRoundedRect(var6 - var9 * сПк, var7 - var8 * сПг, var9, var8, var8 * сП0, var5);
      } finally {
         var14.set(var15);
      }
   }

   private void й_de5a6f78(EntityEspSettingsPanel.PreviewCategory var1) {
      if (var1 != this.В_44db94ff) {
         this.Я_44db94ff = this.В_44db94ff;
         this.АК = Integer.compare(var1.ordinal(), this.В_44db94ff.ordinal());
         if (this.АК == 0) {
            this.АК = 1;
         }

         this.В_44db94ff = var1;
         this.з.setImmediately(0.0F);
      }
   }

   private void б(float var1, float var2, float var3) {
      UiDrawing.drawText(ClientFonts.getMediumFont(), сП必, var1 + сПщ, var2 + сПГ, 15, ThemeEditor.getColorArgb(ThemeColorSlot.TEXT));
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(сПл), var1 + сПэ, var2 + сПю + сПЩ, 13, ThemeEditor.getColorArgb(ThemeColorSlot.SUR_TEXT));
   }

   private void й(GuiGraphicsExtractor var1, EntityEspSettingsPanel.PreviewCategory var2, float var3, float var4, float var5, float var6) {
      if (!(var6 <= с西б)) {
         Entity var7 = this.й_f553a72a(var2);
         EntityEspStyleSettings var8 = this.й_ac031899(var2);
         if (var7 != null && var8 != null) {
            this.й(var1, var7, var8, var3, var4, var2.еП, var5, var6);
            float var9 = var4 + (1.0F - var5) * var2.еП;
            float var10 = var9 - var2.еП * var5 - с西Ы;
            if (var8.getEnabled().getValue()) {
               this.Я_aaf997af.getLabelRenderer().renderPreviewLabel(var7, var8, var3, var10, var6);
            }
         }
      }
   }

   private void И4(float var1) {
      long var2 = System.nanoTime();
      float var4 = this.ЯЛ == 0L ? 0.0F : Math.min((float)(var2 - this.ЯЛ) / с西о, с西O);
      this.ЯЛ = var2;
      if (this.ЯТ) {
         boolean var5 = GLFW.glfwGetMouseButton(MINECRAFT_CLIENT.getWindow().handle(), 1) == 1;
         if (!var5) {
            this.ЯТ = false;
         } else {
            float var6 = var1 - this.field_4O;
            this.field_4о = Иж(this.field_4о + var6 * с西7);
         }

         this.field_4O = var1;
      }

      if (!this.ЯТ && var4 > 0.0F) {
         float var7 = 1.0F - (float)Math.exp(с西Р * var4);
         this.field_4о = this.field_4о + (0.0F - this.field_4о) * var7;
         if (Math.abs(this.field_4о) < с西3) {
            this.field_4о = 0.0F;
         }
      }
   }

   private boolean method_4(float var1, float var2, int var3) {
      for (SettingWidget var5 : this.Д) {
         if (var5 instanceof SingleChoiceSettingWidget var6 && var6.isPopupOpen()) {
            var6.handlePriorityMouseButton(var1, var2, var3);
            if (var6.getVisibilityAnimation().isVisible()) {
               var6.mouseClicked(var1, var2, var3);
            }

            return true;
         }
      }

      boolean var7 = false;

      for (SettingWidget var10 : this.Д) {
         if (var10.handlePriorityMouseButton(var1, var2, var3)) {
            var7 = true;
            break;
         }
      }

      if (!var7 && !this.В(var1, var2)) {
         return false;
      }

      for (SettingWidget var11 : this.Д) {
         if (var11.getVisibilityAnimation().isVisible()) {
            var11.mouseClicked(var1, var2, var3);
         }
      }

      return true;
   }

   private void й(float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.РЕ();
      if (this.В_ac031899 != null) {
         float var8 = var3 - с西п;
         float var9 = var8 - с西Д;
         float var10 = var1 + с西Ъ + с西т;
         float var11 = var2 + с西а;
         float var12 = Math.max(с西Ю, var9 - с西Ч);
         float var13 = Math.max(с西Л, var4 - с西诶);
         UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(с西1) + ClientLocalization.translate(this.В_ac031899.getDisplayName()), var10, var11, 13, ThemeEditor.getColorArgb(ThemeColorSlot.TEXT));
         this.field_4р = var10;
         this.field_46 = var11 + с西Ь;
         this.field_4_ = var12;
         this.field_4Ш = Math.max(с西衣, var13 - с西ш);
         this.field_4Р = Math.clamp(this.field_4Р, 0.0F, this.field_43);
         this.я.updateTarget(this.field_4Р);
         this.field_47 = this.я.getValue();
         UiDrawing.pushScissor(this.field_4р, this.field_46, this.field_4_ + с西И, this.field_4Ш);
         float var14 = 0.0F;

         for (SettingWidget var16 : this.Д) {
            var16.getVisibilityAnimation().configureAndInterpolate(0.0F, 1.0F, с西ь, EasingFunctions.SINE_OUT, var7);
            float var17 = Math.clamp(var16.getVisibilityAnimation().getInterpolatedValue(), 0.0F, 1.0F);
            if (!(var17 <= с西ж)) {
               var16.setX(this.field_4р);
               var16.setY(this.field_46 + var14 - this.field_47);
               var16.setWidth(this.field_4_);
               if (var16.getY() + var16.getHeight() >= this.field_46 && var16.getY() <= this.field_46 + this.field_4Ш) {
                  float var18 = UiDrawing.getGlobalAlpha();
                  UiDrawing.setGlobalAlpha(var18 * var17);
                  var16.render(var5, var6, var7);
                  UiDrawing.setGlobalAlpha(var18);
               }

               var14 += (var16.getHeight() + с西弟) * var17;
            }
         }

         UiDrawing.popScissor();
         this.field_43 = Math.max(0.0F, var14 - this.field_4Ш);
         this.field_4Р = Math.clamp(this.field_4Р, 0.0F, this.field_43);
         if (this.field_43 <= 0.0F) {
            this.Ру();
         } else if (this.field_47 > this.field_43) {
            this.field_47 = this.field_43;
            this.я.setImmediately(this.field_43);
         }
      }
   }

   private void method_8(float var1, float var2, float var3, float var4, float var5) {
      float var6 = var1 + сПу + var3 - сП2 - сПф;
      float var7 = var2 + сПи;
      EntityEspSettingsPanel.PreviewCategory[] var8 = EntityEspSettingsPanel.PreviewCategory.values();
      float var9 = сП9;
      boolean var10 = false;

      for (int var11 = 0; var11 < var8.length; var11++) {
         EntityEspSettingsPanel.PreviewCategory var12 = var8[var11];
         float var13 = var6 + var11 * (var9 + сПо);
         boolean var14 = this.В_44db94ff == var12;
         boolean var15 = WorldProjection.containsPoint(var4, var5, var13, var7, var9, сПO);
         if (var12 == EntityEspSettingsPanel.PreviewCategory.PLAYERS) {
            var10 = var15;
         }

         int var16;
         if (var14) {
            var16 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
         } else {
            var16 = ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE);
         }

         UiDrawing.drawRoundedRect(var13, var7 + сП7, var9, 1.0F, 0.0F, var16);
         int var17 = var14 ? ThemeEditor.getColorArgb(ThemeColorSlot.TEXT) : ThemeEditor.getColorArgb(ThemeColorSlot.SUR_TEXT);
         String var18 = ClientLocalization.translate(var12.Ва);
         float var19 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var18, сПР, 0.0F);
         boolean var20 = var12 == EntityEspSettingsPanel.PreviewCategory.PLAYERS;
         float var21 = сП3;
         float var22 = сПр;
         float var23 = var19 + (var20 ? var22 + var21 : 0.0F);
         float var24 = var13 + (var9 - var23) / 2.0F;
         float var25 = var7 + сП6;
         UiDrawing.drawText(ClientFonts.getMediumFont(), var18, var24, var25, 12, var17);
         if (var20) {
            float var26 = Math.clamp(this.ц.getValue(), 0.0F, 1.0F);
            int var27 = В(var17, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), var15 ? 1.0F : var26);
            float var28 = var25 + ClientFonts.getMediumFont().getFontFace().getTextCenterOffsetY(var18, сП_);
            this.й(var24 + var19 + var22 + var21 / 2.0F, var28, var26, var27);
         }
      }

      float var29 = EntityEspSettingsPanel.PlayerScope.values().length * var9 + (EntityEspSettingsPanel.PlayerScope.values().length - 1) * сПШ;
      float var30 = var6 + var9 / 2.0F - var29 / 2.0F;
      boolean var31 = WorldProjection.containsPoint(var4, var5, var30, var7 + сПы, var29, сПй);
      boolean var32 = this.ЯI || this.В_44db94ff != EntityEspSettingsPanel.PreviewCategory.PLAYERS && (var10 || this.ц.getValue() > сПЖ && var31);
      this.ц.updateTarget(var32 ? 1.0F : 0.0F);
   }

   private static int т(int var0, float var1) {
      int var2 = var0 >>> 24;
      if (var2 == 0 && (var0 & с西к) != 0) {
         var2 = 255;
      }

      int var3 = Math.round(var2 * Math.clamp(var1, 0.0F, 1.0F));
      return var3 << 24 | var0 & с西г;
   }

   private void Е(float var1, float var2, float var3, float var4, float var5) {
      float var6 = Math.clamp(this.ц.getValue(), 0.0F, 1.0F);
      if (!(var6 <= сПх)) {
         EntityEspSettingsPanel.PlayerScope[] var7 = EntityEspSettingsPanel.PlayerScope.values();
         float var8 = var1 + сПУ + var3 - сПЗ - сПЕ;
         float var9 = var7.length * сПП + (var7.length - 1) * сП西;
         float var10 = var8 + сПд - var9 / 2.0F;
         float var11 = var2 + сПБ + сПС;
         float var12 = (1.0F - var6) * сПз;

         for (int var13 = 0; var13 < var7.length; var13++) {
            EntityEspSettingsPanel.PlayerScope var14 = var7[var13];
            float var15 = var10 + var13 * сПц;
            float var16 = var11 + var12;
            boolean var17 = WorldProjection.containsPoint(var4, var5, var15, var16, сПя, с西с);
            boolean var18 = this.В_e57973e4 == var14;
            FloatAnimation var19 = this.е[var13];
            var19.updateTarget(!var18 && !var17 ? 0.0F : 1.0F);
            float var20 = Math.clamp(var19.getValue(), 0.0F, 1.0F);
            int var21 = В(ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE), ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), var20);
            int var22 = В(ThemeEditor.getColorArgb(ThemeColorSlot.SUR_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.TEXT), var20);
            UiDrawing.drawRoundedRect(var15, var16, с西В, с西Я, с西Х, т(var21, var6));
            String var23 = ClientLocalization.translate(var14.ЯЖ);
            float var24 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var23, с西е, 0.0F);
            UiDrawing.drawText(ClientFonts.getMediumFont(), var23, var15 + с西А - var24 / 2.0F, var16 + с西4, 11, т(var22, var6));
         }
      }
   }

   private static int В(int var0, int var1, float var2) {
      float var3 = Math.clamp(var2, 0.0F, 1.0F);
      int var4 = I(var0);
      int var5 = I(var1);
      int var6 = Math.round(var4 + (var5 - var4) * var3);
      int var7 = Math.round((var0 >> 16 & 0xFF) + ((var1 >> 16 & 0xFF) - (var0 >> 16 & 0xFF)) * var3);
      int var8 = Math.round((var0 >> 8 & 0xFF) + ((var1 >> 8 & 0xFF) - (var0 >> 8 & 0xFF)) * var3);
      int var9 = Math.round((var0 & 0xFF) + ((var1 & 0xFF) - (var0 & 0xFF)) * var3);
      return var6 << 24 | var7 << 16 | var8 << 8 | var9;
   }

   public boolean Ъ_bbeebd87(int var1) {
      if (!this.цш()) {
         return false;
      }

      for (SettingWidget var3 : this.Д) {
         var3.keyPressed(var1);
      }

      if (var1 == 256) {
         this.Ро();
         return true;
      } else {
         return true;
      }
   }

   private boolean В(float var1, float var2) {
      return WorldProjection.containsPoint(var1, var2, this.field_4р, this.field_46, this.field_4_, this.field_4Ш);
   }

   public void Р8() {
      if (this.цш()) {
         this.РЕ();

         for (SettingWidget var2 : this.Д) {
            var2.getVisibilityAnimation().tick(var2.getSetting().getVisibilityCondition().getAsBoolean());
            var2.tick();
         }
      }
   }

   public EntityEspSettingsPanel(ClickGuiScreen var1) {
      this.з = new FloatAnimation(1.0F, сП衣, Easings.SINE_IN_OUT);
      this.ц = new FloatAnimation(0.0F, сПш, Easings.CUBIC_OUT);
      this.я = new FloatAnimation(0.0F, сПИ, Easings.CUBIC_OUT);
      this.Д = new ArrayList<>();
      this.В_44db94ff = EntityEspSettingsPanel.PreviewCategory.PLAYERS;
      this.В_e57973e4 = EntityEspSettingsPanel.PlayerScope.SELF;
      this.е = й_b9e95bbb();
      this.АК = 1;
      this.Х = var1;
   }

   private static float Иж(float var0) {
      float var1 = var0 % с西м;
      if (var1 >= с西К) {
         var1 -= с西I;
      }

      if (var1 < с西Т) {
         var1 += с西О;
      }

      return var1;
   }

   private EntityEspStyleSettings й_ac031899() {
      return this.й_ac031899(this.В_44db94ff);
   }

   public boolean В(double var1, double var3, double var5) {
      if (!this.цш()) {
         return false;
      }

      for (SettingWidget var8 : this.Д) {
         if (var8.mouseScrolled((float)var1, (float)var3, var5)) {
            return true;
         }
      }

      if (this.В((float)var1, (float)var3) && !(this.field_43 <= 0.0F)) {
         this.field_4Р = Math.clamp(this.field_4Р - (float)var5 * с西9, 0.0F, this.field_43);
         return true;
      } else {
         return false;
      }
   }

   private void Р西() {
      for (SettingWidget var2 : this.Д) {
         var2.resetTransientState();
      }
   }

   private static int I(int var0) {
      int var1 = var0 >>> 24;
      return var1 == 0 && (var0 & с西Й) != 0 ? 255 : var1;
   }

   public boolean й(String var1, int var2, int var3) {
      if (!this.цш()) {
         return false;
      }

      for (SettingWidget var5 : this.Д) {
         var5.charTyped(var1, var2, var3);
      }

      return true;
   }

   private static FloatAnimation[] й_b9e95bbb() {
      EntityEspSettingsPanel.PlayerScope[] var0 = EntityEspSettingsPanel.PlayerScope.values();
      FloatAnimation[] var1 = new FloatAnimation[var0.length];

      for (int var2 = 0; var2 < var0.length; var2++) {
         var1[var2] = new FloatAnimation(var2 == EntityEspSettingsPanel.PlayerScope.SELF.ordinal() ? 1.0F : 0.0F, с西Ф, Easings.CUBIC_OUT);
      }

      return var1;
   }

   private void ъ(float var1, float var2) {
      if (this.ЯО) {
         boolean var3 = GLFW.glfwGetMouseButton(MINECRAFT_CLIENT.getWindow().handle(), 0) == 1;
         if (!var3) {
            this.С(var1, var2, 0);
            this.ЯО = false;
         }
      }
   }

   public boolean цш() {
      return this.ЯФ && this.Я_aaf997af != null;
   }

   public boolean й(double var1, double var3, int var5) {
      if (!this.цш()) {
         return false;
      }

      if (var5 == 1) {
         if (!this.С((float)var1, (float)var3)) {
            return false;
         }

         this.ЯТ = true;
         this.field_4O = (float)var1;
         this.ЯЛ = System.nanoTime();
         return true;
      } else {
         if (var5 != 0) {
            return false;
         }

         if (this.method_4((float)var1, (float)var3, var5)) {
            this.ЯО = true;
            return true;
         }

         float var6 = this.Х.getGuiX();
         float var7 = this.Х.getGuiY();
         float var8 = this.Х.getGuiWidth();
         float var9 = var6 + с西ч + var8 - с西Э - с西必;
         float var10 = var7 + с西щ;
         EntityEspSettingsPanel.PreviewCategory[] var11 = EntityEspSettingsPanel.PreviewCategory.values();
         float var12 = с西Г;
         if (this.ц.getValue() > с西л) {
            float var13 = Math.clamp(this.ц.getValue(), 0.0F, 1.0F);
            EntityEspSettingsPanel.PlayerScope[] var14 = EntityEspSettingsPanel.PlayerScope.values();
            float var15 = var14.length * var12 + (var14.length - 1) * с西э;
            float var16 = var9 + var12 / 2.0F - var15 / 2.0F;
            float var17 = var10 + с西ю;
            float var18 = (1.0F - var13) * с西Щ;
            float var19 = var17 + var18;

            for (int var20 = 0; var20 < var14.length; var20++) {
               float var21 = var16 + var20 * (var12 + с西у);
               if (WorldProjection.containsPoint((float)var1, (float)var3, var21, var19, var12, с西2)) {
                  this.В_e57973e4 = var14[var20];
                  this.ЯI = false;
                  this.й_de5a6f78(EntityEspSettingsPanel.PreviewCategory.PLAYERS);
                  return true;
               }
            }
         }

         for (int var22 = 0; var22 < var11.length; var22++) {
            float var23 = var9 + var22 * (var12 + с西ф);
            if (WorldProjection.containsPoint((float)var1, (float)var3, var23, var10, var12, с西и)) {
               EntityEspSettingsPanel.PreviewCategory var24 = var11[var22];
               if (var24 == EntityEspSettingsPanel.PreviewCategory.PLAYERS && this.В_44db94ff == EntityEspSettingsPanel.PreviewCategory.PLAYERS) {
                  this.ЯI = !this.ЯI;
                  return true;
               }

               this.ЯI = false;
               this.й_de5a6f78(var24);
               if (var24 == EntityEspSettingsPanel.PreviewCategory.PLAYERS) {
                  this.ц.setImmediately(0.0F);
               }

               return true;
            }
         }

         return true;
      }
   }

   private float йя() {
      return this.Я诶 == 0L ? 0.0F : (float)(System.nanoTime() - this.Я诶) / с西Ж;
   }

   private Entity й_f553a72a(EntityEspSettingsPanel.PreviewCategory var1) {
      return switch (var1) {
         case PLAYERS -> this.В_07050678;
         case MOBS -> this.В_f20cde87;
         case VILLAGERS -> this.В_ef75dc81;
         case ITEMS -> this.Я_3a07ee9c;
      };
   }

   public void й(EntityESP var1) {
      if (var1 != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         this.Я_aaf997af = var1;
         this.В_44db94ff = EntityEspSettingsPanel.PreviewCategory.PLAYERS;
         this.Я_44db94ff = null;
         this.з.setImmediately(1.0F);
         this.ц.setImmediately(0.0F);
         this.РН();
         this.ЯI = false;
         this.В_ac031899 = null;
         this.Ру();
         this.field_43 = 0.0F;
         this.ЯО = false;
         this.РI();
         this.Я诶 = System.nanoTime();
         this.ЯФ = true;
         this.РЬ();
      }
   }

   private void Л(float var1, float var2, float var3) {
      for (SettingWidget var5 : this.Д) {
         if (var5.getVisibilityAnimation().isVisible()) {
            var5.renderOverlay(var1, var2, var3);
         }
      }
   }

   public EntityEspSettingsPanel.PlayerScope й_e57973e4() {
      return this.В_e57973e4;
   }

   public static boolean В(ItemEntity var0) {
      return var0 instanceof EntityEspSettingsPanel.PreviewItemEntity;
   }

   public void Ро() {
      this.Р西();
      this.Д.clear();
      this.В_ac031899 = null;
      this.ЯФ = false;
      this.Я_aaf997af = null;
      this.Я_44db94ff = null;
      this.В_07050678 = null;
      this.В_f20cde87 = null;
      this.В_ef75dc81 = null;
      this.Я_3a07ee9c = null;
      this.з.setImmediately(1.0F);
      this.ц.setImmediately(0.0F);
      this.РН();
      this.ЯI = false;
      this.Ру();
      this.field_43 = 0.0F;
      this.ЯО = false;
      this.РI();
      this.Я诶 = 0L;
   }

   private void РН() {
      for (int var1 = 0; var1 < this.е.length; var1++) {
         float var2 = var1 == this.В_e57973e4.ordinal() ? 1.0F : 0.0F;
         this.е[var1].setImmediately(var2);
      }
   }

   public boolean Х(int var1) {
      if (!this.цш()) {
         return false;
      }

      for (SettingWidget var3 : this.Д) {
         var3.keyReleased(var1);
      }

      return true;
   }

   private void й(GuiGraphicsExtractor var1, Entity var2, EntityEspStyleSettings var3, float var4, float var5, int var6, float var7, float var8) {
      Window var9 = MINECRAFT_CLIENT.getWindow();
      if (!var9.isIconified() && var9.getScreenWidth() > 0 && var9.getScreenHeight() > 0) {
         float var10 = var9.getGuiScale();
         if (Float.isFinite(var10) && !(var10 <= 0.0F) && Float.isFinite(var7) && !(var7 <= 0.0F)) {
            float var11 = с西l;
            float var12 = с西8;
            float var13 = var5 + (1.0F - var7) * var6;
            Vector2f var14 = WorldProjection.transformScreenPoint(var4 - var11, var13 - var12);
            Vector2f var15 = WorldProjection.transformScreenPoint(var4 + var11, var13 + var12);
            if (Float.isFinite(var14.x) && Float.isFinite(var14.y) && Float.isFinite(var15.x) && Float.isFinite(var15.y)) {
               float var16 = (var15.x - var14.x) / (var11 * 2.0F);
               int var17 = Math.round(var14.x / var10);
               int var18 = Math.round(var14.y / var10);
               int var19 = Math.round(var15.x / var10);
               int var20 = Math.round(var15.y / var10);
               if (var19 > var17 && var20 > var18) {
                  int var21 = Math.max(1, Math.round(var6 * var7 * var16 / var10));
                  this.й(var1, var2, var3, var17, var18, var19, var20, var21, var8);
               }
            }
         }
      }
   }

   public void В(GuiGraphicsExtractor var1, float var2, float var3, float var4) {
      if (this.цш()) {
         this.ъ(var2, var3);
         if (MINECRAFT_CLIENT.level == null) {
            this.Ро();
         } else {
            float var5 = this.Х.getGuiX();
            float var6 = this.Х.getGuiY();
            float var7 = this.Х.getGuiWidth();
            float var8 = this.Х.getGuiHeight();
            this.И4(var2);
            float var9 = var5 + сПь;
            float var10 = var6;
            float var11 = var7 - сПж;
            float var12 = var8;
            UiDrawing.drawRoundedRect(var5 + сП弟, var6 + сПб, var11 - сПЫ, 1.0F, 0.0F, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
            UiDrawing.drawOutline(var9, var10, var11, var12, 1.0F, сПl, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
            UiDrawing.drawOutline(var9 + var11 - сП8, var10, сП5, var12, 1.0F, сПв, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
            this.б(var5, var6, var7);
            this.method_8(var5, var6, var7, var2, var3);
            this.й(var5, var6, var7, var8, var2, var3, var4);
            float var13 = var9 + var11 - сПъ;
            float var14 = var10 + var12 / 2.0F + сПМ;
            if (this.Я_44db94ff != null) {
               this.з.updateTarget(1.0F);
               float var15 = Math.clamp(this.з.getValue(), 0.0F, 1.0F);
               float var16 = 1.0F - var15;
               float var17 = var15;
               float var18 = 1.0F - сПН * var15;
               float var19 = сПн + сПЦ * var15;
               float var20 = -this.АК * сПч * var15;
               float var21 = this.АК * сПЭ * (1.0F - var15);
               if (!this.з.isFinished()) {
                  this.й(var1, this.Я_44db94ff, var13 + var20, var14, var18, var16);
                  this.й(var1, this.В_44db94ff, var13 + var21, var14, var19, var17);
               } else {
                  this.Я_44db94ff = null;
                  this.з.setImmediately(1.0F);
                  this.й(var1, this.В_44db94ff, var13, var14, 1.0F, 1.0F);
               }
            } else {
               this.й(var1, this.В_44db94ff, var13, var14, 1.0F, 1.0F);
            }

            this.Е(var5, var6, var7, var2, var3);
            this.Л(var2, var3, var4);
         }
      }
   }

   private void й(GuiGraphicsExtractor var1, Entity var2, EntityEspStyleSettings var3, int var4, int var5, int var6, int var7, int var8, float var9) {
      Quaternionf var10 = new Quaternionf().rotateZ(с西5 + (float)Math.toRadians(0.0));
      Quaternionf var11 = new Quaternionf().rotateY((float)Math.toRadians(this.field_4о));
      var10.mul(var11);
      EntityRenderDispatcher var12 = MINECRAFT_CLIENT.getEntityRenderDispatcher();
      EntityRenderState var13 = var12.extractEntity(var2, 1.0F);
      var13.lightCoords = с西в;
      var13.shadowPieces.clear();
      var13.outlineColor = 0;
      if (var13 instanceof ItemEntityRenderState var14 && var2 instanceof ItemEntity var15 && В(var15)) {
         var14.ageInTicks = this.йя();
      }

      if (var13 instanceof LivingEntityRenderState var16) {
         var16.isInvisible = false;
         var16.isInvisibleToPlayer = false;
         var16.bodyRot = с西ъ;
         var16.yRot = с西М;
         var16.xRot = 0.0F;
         var16.pose = Pose.STANDING;
         if (var16.scale != 0.0F) {
            var16.boundingBoxWidth = var16.boundingBoxWidth / var16.scale;
            var16.boundingBoxHeight = var16.boundingBoxHeight / var16.scale;
         }

         var16.scale = 1.0F;
      }

      if (var3.getEnabled().getValue()) {
         PreviewFade.registerPreview(var13, var9, this.Я_aaf997af.getSurfaceRenderer(), var3);
      } else {
         PreviewFade.registerPreview(var13, var9);
      }

      Vector3f var17 = new Vector3f(0.0F, var13.boundingBoxHeight / 2.0F + с西Н, 0.0F);
      var1.entity(var13, var8, var17, var10, var11, var4, var5, var6, var7);
   }

   private void С(float var1, float var2, int var3) {
      for (SettingWidget var5 : this.Д) {
         var5.mouseReleased(var1, var2, var3);
      }
   }

   private EntityEspStyleSettings й_ac031899(EntityEspSettingsPanel.PreviewCategory var1) {
      if (this.Я_aaf997af != null && var1 != null) {
         return switch (var1) {
            case PLAYERS -> {
               switch (this.В_e57973e4) {
                  case SELF:
                     yield this.Я_aaf997af.getSelfStyle();
                  case FRIENDS:
                     yield this.Я_aaf997af.getFriendStyle();
                  case PLAYERS:
                     yield this.Я_aaf997af.getPlayerStyle();
                  default:
                     throw new MatchException(null, null);
               }
            }
            case MOBS -> this.Я_aaf997af.getMobStyle();
            case VILLAGERS -> this.Я_aaf997af.getVillagerStyle();
            case ITEMS -> this.Я_aaf997af.getItemStyle();
         };
      } else {
         return null;
      }
   }

   private boolean С(float var1, float var2) {
      float var3 = this.Х.getGuiX();
      float var4 = this.Х.getGuiY();
      float var5 = this.Х.getGuiWidth();
      float var6 = this.Х.getGuiHeight();
      float var7 = var3 + var5 - с西р;
      float var8 = var4 + var6 / 2.0F + с西6;
      return WorldProjection.containsPoint(var1, var2, var7 - с西_, var8 - с西Ш, с西ы, с西й);
   }

   private void РЕ() {
      EntityEspStyleSettings var1 = this.й_ac031899();
      if (var1 != this.В_ac031899) {
         this.Р西();
         this.Д.clear();
         this.В_ac031899 = var1;
         this.Ру();
         this.field_43 = 0.0F;
         if (var1 != null) {
            for (ValueSetting var3 : var1.getSettings()) {
               if (var3 instanceof BooleanSetting var4) {
                  this.Д.add(new BooleanSettingWidget(var4));
               } else if (var3 instanceof ColorSetting var5) {
                  this.Д.add(new ColorSettingWidget(var5));
               } else if (var3 instanceof FloatSetting var6) {
                  this.Д.add(new FloatSettingWidget(var6));
               } else if (var3 instanceof SingleChoiceSetting var7) {
                  this.Д.add(new SingleChoiceSettingWidget(var7));
               }
            }
         }
      }
   }

   private void й(float var1, float var2, float var3, int var4) {
      float var5 = сПм * (1.0F - 2.0F * var3);
      this.В(var1 - сПК, var2 - var5, var1, var2 + var5, var4);
      this.В(var1, var2 + var5, var1 + сПI, var2 - var5, var4);
   }

   private void РI() {
      this.ЯТ = false;
      this.field_4о = 0.0F;
      this.field_4O = 0.0F;
      this.ЯЛ = 0L;
   }

   public boolean Ю(double var1, double var3, int var5) {
      this.С((float)var1, (float)var3, var5);
      if (var5 == 0) {
         this.ЯО = false;
      }

      if (!this.цш()) {
         return false;
      } else if (var5 == 1 && this.ЯТ) {
         this.ЯТ = false;
         this.ЯЛ = System.nanoTime();
         return true;
      } else {
         return this.В((float)var1, (float)var3);
      }
   }

   private void Ру() {
      this.field_47 = 0.0F;
      this.field_4Р = 0.0F;
      this.я.setImmediately(0.0F);
   }

   private static final class PreviewItemEntity extends ItemEntity {
      private PreviewItemEntity(Level var1, ItemStack var2) {
         super(var1, 0.0, 0.0, 0.0, var2);
      }
   }

   public enum PlayerScope {
      SELF("Себя"),
      FRIENDS("Друзей"),
      PLAYERS("Игроков");

      private final String ЯЖ;

      PlayerScope(String var3) {
         this.ЯЖ = var3;
      }
   }

   private enum PreviewCategory {
      PLAYERS("Игроки", "Player763", 94),
      MOBS("Мобы", "Зомби", 94),
      VILLAGERS("Жители", "Житель", 80),
      ITEMS("Предметы", "Эндер-жемчуг ×16", 80);

      private final String Ва;
      private final String ВЮ;
      private final int еП;

      PreviewCategory(String var3, String var4, int var5) {
         this.Ва = var3;
         this.ВЮ = var4;
         this.еП = var5;
      }
   }
}
