package sg.gui.account;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.textures.FilterMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.User;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.ec.ClientFonts;
import sg.gui.manager.ClientMainMenuScreen;
import sg.mixin.MinecraftUserAccessor;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.account.AccountStore;
import sg.theme.ThemeColorSlot;
import sg.util.time.ColorMath;
import sg.animation.Easings;
import sg.animation.FloatAnimation;
import sg.util.time.ThemeEditor;

public final class AccountManagerScreen extends Screen implements MinecraftAccess {
   private static final float В_ = 799.0F;
   private static final float ВШ = 437.0F;
   private static final float Вы = 175.0F;
   private static final float Вй = 177.0F;
   private static final float ВЖ = 48.0F;
   private static final float Вм = 16.0F;
   private static final float ВК = 14.0F;
   private static final DateTimeFormatter Я_0551e0cc = DateTimeFormatter.ofPattern(AccountManagerScreen.сзЩ);
   private static final String[] field_4 = new String[]{
      AccountManagerScreen.сзу,
      AccountManagerScreen.сз2,
      AccountManagerScreen.сзф,
      AccountManagerScreen.сзи,
      AccountManagerScreen.сз9,
      AccountManagerScreen.сзо,
      AccountManagerScreen.сзO,
      AccountManagerScreen.сз7,
      AccountManagerScreen.сзР,
      AccountManagerScreen.сз3,
      AccountManagerScreen.сзр,
      AccountManagerScreen.сз6,
      AccountManagerScreen.сз_,
      AccountManagerScreen.сзШ,
      AccountManagerScreen.сзы,
      AccountManagerScreen.сзй
   };
   private static final String[] п = new String[]{
      AccountManagerScreen.сзЖ,
      AccountManagerScreen.сзм,
      AccountManagerScreen.сзК,
      AccountManagerScreen.сзI,
      AccountManagerScreen.сзТ,
      AccountManagerScreen.сзО,
      AccountManagerScreen.сзФ,
      AccountManagerScreen.сзЙ,
      AccountManagerScreen.сзк,
      AccountManagerScreen.сзг,
      AccountManagerScreen.сз0,
      AccountManagerScreen.сзх,
      AccountManagerScreen.сзУ,
      AccountManagerScreen.сзЗ,
      AccountManagerScreen.сзЕ,
      AccountManagerScreen.сзП
   };
   @SerializedName("Х")
   private final Screen Х_af10102c;
   private final AccountStore В = new AccountStore();
   @SerializedName("必")
   private final List<AccountManagerScreen.AccountRow> 必_2571477d = new ArrayList<>();
   private final FloatAnimation ч;
   private final FloatAnimation Э;
   @SerializedName("必")
   private final FloatAnimation 必_04d91340;
   private final FloatAnimation щ;
   @SerializedName("Я")
   private final FloatAnimation[] Я_b9e95bbb;
   @SerializedName("Х")
   private final FloatAnimation[] Х_b9e95bbb;
   private String сХ;
   private String се;
   private boolean сш;
   private boolean сИ;
   private boolean сь;
   private long с诶;
   private long с1;
   private long сЬ;
   private float ВI;
   private float ВТ;
   private long с衣;
   private float ВО;
   private float ВФ;
   private static final String с西0 = "Account Manager";
   private static final float с西х = 5.5F;
   private static final float с西У = 9.0F;
   private static final float с西З = 8.0F;
   private static final float с西Е = 8.0F;
   private static final float с西П = 9.0F;
   private static final float с西西 = 9.0F;
   private static final float с西д = 9.0F;
   private static final float с西Б = 13.0F;
   private static final float с西С = 13.0F;
   private static final float с西з = 13.0F;
   private static final float с西ц = 799.0F;
   private static final float с西я = 0.5F;
   private static final float сдс = 437.0F;
   private static final float сдВ = 0.5F;
   private static final float сдЯ = 0.016666668F;
   private static final float сдХ = 1.0E9F;
   private static final float сде = 0.05F;
   private static final float сдА = 18.0F;
   private static final float сд4 = 0.05F;
   private static final float сдп = 10.0F;
   private static final float сдД = 210.0F;
   private static final float сдЪ = 46.0F;
   private static final float сдт = 0.94F;
   private static final float сда = 0.06F;
   private static final float сдЮ = 799.0F;
   private static final float сдЧ = 437.0F;
   private static final float сдЛ = 255.0F;
   private static final float сд诶 = 799.0F;
   private static final float сд1 = 437.0F;
   private static final float сдЬ = 0.01F;
   private static final float сд衣 = 24.0F;
   private static final float сдш = 799.0F;
   private static final float сдИ = 437.0F;
   private static final float сдь = 30.0F;
   private static final float сдж = 799.0F;
   private static final float сд弟 = 437.0F;
   private static final double сдб = 25.0;
   private static final float сдЫ = 174.0F;
   private static final float сдl = 99.0F;
   private static final float сд8 = 23.0F;
   private static final float сд5 = 23.0F;
   private static final float сдв = 51.0F;
   private static final float сдъ = 799.0F;
   private static final float сдМ = 437.0F;
   private static final float сдН = 175.0F;
   private static final float сдн = 436.0F;
   private static final float сдЦ = 100.0F;
   private static final float сдч = 174.0F;
   private static final String сдЭ = "accounts";
   private static final float сд必 = 71.0F;
   private static final float сдщ = 37.0F;
   private static final float сдГ = 34.0F;
   private static final float сдл = 34.0F;
   private static final String сдэ = "Account Create";
   private static final float сдю = 45.0F;
   private static final float сдЩ = 110.0F;
   private static final float сду = 255.0F;
   private static final float сд2 = 11.0F;
   private static final float сдф = 130.0F;
   private static final double сди = 120.0;
   private static final double сд9 = 30.0;
   private static final float сдо = 120.0F;
   private static final float сдO = 30.0F;
   private static final float сд7 = 4.0F;
   private static final float сдР = 10.0F;
   private static final float сд3 = 255.0F;
   private static final float сдр = 33.0F;
   private static final float сд6 = 22.0F;
   private static final float сд_ = 190.0F;
   private static final float сдШ = 120.0F;
   private static final float сды = 30.0F;
   private static final float сдй = 0.5F;
   private static final float сдЖ = 10.0F;
   private static final String сдм = "Write nick...";
   private static final float сдК = 255.0F;
   private static final float сдI = 255.0F;
   private static final float сдТ = 9.0F;
   private static final float сдО = 4.0F;
   private static final float сдФ = 102.0F;
   private static final float сдЙ = 22.0F;
   private static final float сдк = 10.0F;
   private static final float сдг = 8.0F;
   private static final long сд0 = 500L;
   private static final long сдх = 2L;
   private static final float сдУ = 10.0F;
   private static final float сдЗ = 7.0F;
   private static final float сдЕ = 16.0F;
   private static final String сдП = "Other";
   private static final float сд西 = 70.0F;
   private static final float сдд = 169.0F;
   private static final float сдБ = 255.0F;
   private static final String сдС = "Generate random";
   private static final float сдз = 189.0F;
   private static final String сдц = "random";
   private static final String сдя = "Clear all";
   private static final float сБс = 223.0F;
   private static final String сБВ = "trash";
   private static final float сБЯ = 135.0F;
   private static final float сБХ = 130.0F;
   private static final double сБе = 30.0;
   private static final double сБА = 30.0;
   private static final float сБ4 = 30.0F;
   private static final float сБп = 30.0F;
   private static final float сБД = 4.0F;
   private static final float сБЪ = 10.0F;
   private static final float сБт = 255.0F;
   private static final float сБа = 30.0F;
   private static final float сБЮ = 30.0F;
   private static final float сБЧ = 0.5F;
   private static final float сБЛ = 10.0F;
   private static final float сБ诶 = 33.0F;
   private static final float сБ1 = 18.0F;
   private static final float сБЬ = 12.0F;
   private static final float сБ衣 = 1.5F;
   private static final String сБш = "add";
   private static final float сБИ = 30.0F;
   private static final float сБь = 0.5F;
   private static final float сБж = 30.0F;
   private static final float сБ弟 = 0.5F;
   private static final float сБб = 11.0F;
   private static final double сБЫ = 154.0;
   private static final double сБl = 30.0;
   private static final float сБ8 = 154.0F;
   private static final float сБ5 = 30.0F;
   private static final float сБв = 4.0F;
   private static final float сБъ = 10.0F;
   private static final float сБМ = 255.0F;
   private static final float сБН = 154.0F;
   private static final float сБн = 30.0F;
   private static final float сБЦ = 10.0F;
   private static final float сБч = 26.0F;
   private static final float сБЭ = 14.0F;
   private static final float сБ必 = 18.0F;
   private static final float сБщ = 255.0F;
   private static final float сБГ = 154.0F;
   private static final float сБл = 30.0F;
   private static final float сБэ = 10.0F;
   private static final float сБю = 0.035F;
   private static final float сБЩ = 0.08F;
   private static final float сБу = 154.0F;
   private static final float сБ2 = 30.0F;
   private static final float сБф = 0.5F;
   private static final float сБи = 10.0F;
   private static final float сБ9 = 33.0F;
   private static final float сБо = 16.0F;
   private static final float сБO = 108.0F;
   private static final float сБ7 = 50.0F;
   private static final float сБР = 255.0F;
   private static final float сБ3 = 255.0F;
   private static final float сБр = 10.0F;
   private static final float сБ6 = 7.0F;
   private static final float сБ_ = 132.0F;
   private static final float сБШ = 9.0F;
   private static final float сБы = 12.0F;
   private static final String сБй = "trash";
   private static final float сБЖ = 13.0F;
   private static final float сБм = 12.0F;
   private static final float сБК = 0.01F;
   private static final double сБI = 255.0;
   private static final float сБТ = 268.0F;
   private static final float сБО = 4.0F;
   private static final float сБФ = 11.0F;
   private static final float сБЙ = 188.0F;
   private static final float сБк = 18.0F;
   private static final float сБг = 599.0F;
   private static final float сБ0 = 371.0F;
   private static final float сБх = 0.01F;
   private static final float сБУ = 206.0F;
   private static final float сБЗ = 193.0F;
   private static final float сБЕ = 30.0F;
   private static final float сБП = 62.0F;
   private static final float сБ西 = 14.0F;
   private static final float сБд = 5.0F;
   private static final double сББ = 177.0;
   private static final double сБС = 48.0;
   private static final float сБз = 5.0F;
   private static final float сБц = 4.0F;
   private static final float сБя = 4.0F;
   private static final float сСс = 5.0F;
   private static final float сСВ = 51.0F;
   private static final float сСЯ = 20.0F;
   private static final float сСХ = 177.0F;
   private static final float сСе = 48.0F;
   private static final float сСА = 12.0F;
   private static final float сС4 = 177.0F;
   private static final float сСп = 48.0F;
   private static final float сСД = 0.5F;
   private static final float сСЪ = 12.0F;
   private static final float сСт = 31.0F;
   private static final float сСа = 17.0F;
   private static final float сСЮ = 10.0F;
   private static final float сСЧ = 10.0F;
   private static final float сСЛ = 28.0F;
   private static final double сС诶 = 255.0;
   private static final double сС1 = 255.0;
   private static final float сСЬ = 255.0F;
   private static final float сС衣 = 84.0F;
   private static final float сСш = 44.0F;
   private static final float сСИ = 9.0F;
   private static final float сСь = 44.0F;
   private static final float сСж = 26.0F;
   private static final double сС弟 = 255.0;
   private static final String сСб = "copy";
   private static final float сСЫ = 135.0F;
   private static final float сСl = 17.0F;
   private static final float сС8 = 12.0F;
   private static final float сС5 = 14.0F;
   private static final float сСв = 12.0F;
   private static final float сСъ = 0.16F;
   private static final String сСМ = "star";
   private static final String сСН = "star_inactive";
   private static final float сСн = 161.0F;
   private static final float сСЦ = 0.5F;
   private static final float сСч = 24.0F;
   private static final float сСЭ = 0.5F;
   private static final float сС必 = 12.0F;
   private static final float сСщ = 396.0F;
   private static final float сСГ = 28.0F;
   private static final float сСл = 112.0F;
   private static final String сСэ = "Current session:";
   private static final float сСю = 49.0F;
   private static final float сСЩ = 398.0F;
   private static final float сСу = 255.0F;
   private static final float сС2 = 48.0F;
   private static final float сСф = 407.0F;
   private static final float сСи = 255.0F;
   private static final float сС9 = 6.0F;
   private static final float сСо = 0.125F;
   private static final float сСO = 0.125F;
   private static final float сС7 = 0.125F;
   private static final float сСР = 0.125F;
   private static final float сС3 = 255.0F;
   private static final String сСр = "mainmenu/account_manager/demo_skin";
   private static final String сС6 = "accounts";
   private static final String сС_ = "trash";
   private static final float сСШ = 255.0F;
   private static final float сСы = 188.0F;
   private static final float сСй = 18.0F;
   private static final double сСЖ = 599.0;
   private static final double сСм = 371.0;
   private static final double сСК = 177.0;
   private static final double сСI = 48.0;
   private static final float сСТ = 11.0F;
   private static final float сСО = 130.0F;
   private static final double сСФ = 120.0;
   private static final double сСЙ = 30.0;
   private static final float сСк = 135.0F;
   private static final float сСг = 130.0F;
   private static final double сС0 = 30.0;
   private static final double сСх = 30.0;
   private static final float сСУ = 11.0F;
   private static final float сСЗ = 189.0F;
   private static final double сСЕ = 154.0;
   private static final double сСП = 30.0;
   private static final float сС西 = 11.0F;
   private static final float сСд = 223.0F;
   private static final double сСБ = 154.0;
   private static final double сСС = 30.0;
   private static final float сСз = 188.0F;
   private static final float сСц = 18.0F;
   private static final double сСя = 599.0;
   private static final double сзс = 371.0;
   private static final double сзВ = 177.0;
   private static final double сзЯ = 48.0;
   private static final float сзХ = 128.0F;
   private static final float сзе = 10.0F;
   private static final double сзА = 25.0;
   private static final double сз4 = 28.0;
   private static final float сзп = 151.0F;
   private static final float сзД = 10.0F;
   private static final double сзЪ = 25.0;
   private static final double сзт = 28.0;
   private static final float сза = 0.35F;
   private static final float сзЮ = 0.65F;
   private static final String сзЧ = "Added to favorites";
   private static final String сзЛ = "Removed from favorites";
   private static final float сз诶 = 188.0F;
   private static final float сз1 = 18.0F;
   private static final double сзЬ = 599.0;
   private static final double сз衣 = 371.0;
   private static final float сзш = 62.0F;
   private static final float сзИ = 14.0F;
   private static final float сзь = 351.0F;
   private static final float сзж = 42.0F;
   private static final long сз弟 = 700L;
   private static final String сзб = "Use 3-16 letters, numbers or _";
   private static final long сзЫ = 700L;
   private static final String сзl = "This account already exists";
   private static final String сз8 = "Account added";
   private static final String сз5 = "No accounts to clear";
   private static final String сзв = "Accounts cleared";
   private static final float сзъ = 62.0F;
   private static final float сзМ = 14.0F;
   private static final float сзН = 351.0F;
   private static final float сзн = 0.25F;
   private static final String сзЦ = "_";
   private static final float сзч = 0.58F;
   private static final int сзЭ = 100000;
   private static final int сз必 = 1000000;
   private static final float сзщ = 98.0F;
   private static final String сзГ = "...";
   private static final String сзл = "[A-Za-z0-9_]+";
   private static final long сзэ = 1800L;
   private static final float сзю = 0.01F;
   private static final String сзЩ = "dd.MM.yyyy";
   private static final String сзу = "Silent";
   private static final String сз2 = "Night";
   private static final String сзф = "Void";
   private static final String сзи = "Lunar";
   private static final String сз9 = "Neon";
   private static final String сзо = "Frost";
   private static final String сзO = "Shadow";
   private static final String сз7 = "Pixel";
   private static final String сзР = "Nova";
   private static final String сз3 = "Dark";
   private static final String сзр = "Wild";
   private static final String сз6 = "Ghost";
   private static final String сз_ = "Crimson";
   private static final String сзШ = "Aero";
   private static final String сзы = "Cyber";
   private static final String сзй = "Velvet";
   private static final String сзЖ = "Fox";
   private static final String сзм = "Soul";
   private static final String сзК = "Byte";
   private static final String сзI = "Raven";
   private static final String сзТ = "Wolf";
   private static final String сзО = "Bloom";
   private static final String сзФ = "Storm";
   private static final String сзЙ = "Echo";
   private static final String сзк = "Flame";
   private static final String сзг = "Blade";
   private static final String сз0 = "Mist";
   private static final String сзх = "Viper";
   private static final String сзУ = "Comet";
   private static final String сзЗ = "Shade";
   private static final String сзЕ = "Pulse";
   private static final String сзП = "Dream";

   @Override
   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.Х_af10102c != null) {
         this.Х_af10102c.extractRenderState(var1, var2, var3, var4);
      }

      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      UiDrawing.resetGlobalAlpha();
      Vector2f var5 = WorldProjection.transformScreenPoint(var2, var3);
      float var6 = this.minecraft.getWindow().getScreenWidth();
      float var7 = this.minecraft.getWindow().getScreenHeight();
      this.ВО = (float)Math.floor((var6 - с西ц) * с西я);
      this.ВФ = Math.round((var7 - сдс) * сдВ);
      this.ч.updateTarget(this.сь ? 0.0F : 1.0F);
      this.必_04d91340.updateTarget(System.currentTimeMillis() < this.с1 ? 1.0F : 0.0F);
      this.щ.updateTarget(System.currentTimeMillis() < this.с诶 ? 1.0F : 0.0F);
      long var8 = System.nanoTime();
      float var10 = this.с衣 == 0L ? сдЯ : Math.clamp((float)(var8 - this.с衣) / сдХ, 0.0F, сде);
      this.с衣 = var8;
      float var11 = 1.0F - (float)Math.exp(-var10 * сдА);
      this.ВТ = this.ВТ + (this.ВI - this.ВТ) * var11;
      if (Math.abs(this.ВI - this.ВТ) < сд4) {
         this.ВТ = this.ВI;
      }

      float var12 = this.ч.getValue();
      UiDrawing.drawBlur(0.0F, 0.0F, var6, var7, сдп, 0.0F, new Vector4i(ColorMath.packArgb(255, 255, 255, сдД * var12)));
      UiDrawing.drawRoundedRect(0.0F, 0.0F, var6, var7, 0.0F, ColorMath.packArgb(4, 6, 9, сдЪ * var12));
      float var13 = сдт + сда * var12;
      WorldProjection.scaleAroundRect(this.ВО, this.ВФ, сдЮ, сдЧ, var13);
      UiDrawing.setGlobalAlpha(сдЛ * var12);
      this.drawPanel(var5.x, var5.y, var12);
      UiDrawing.resetGlobalAlpha();
      WorldProjection.unscaleAroundRect(this.ВО, this.ВФ, сд诶, сд1, var13);
      this.必_2571477d.removeIf(var0 -> var0.Х2 && var0.сЪ.getValue() <= сзю && var0.сЪ.isFinished());
      if (this.сь && var12 <= сдЬ && this.ч.isFinished()) {
         if (this.Х_af10102c instanceof ClientMainMenuScreen var14) {
            var14.resumeFromOverlay();
         }

         this.minecraft.gui.setScreen(this.Х_af10102c);
      }
   }

   private void deleteAccount(AccountManagerScreen.AccountRow var1) {
      var1.Х2 = true;
      this.saveAccounts();
      int var2 = (int)this.必_2571477d.stream().filter(var0 -> !var0.Х2).count();
      int var3 = (var2 + 2) / 3;
      float var4 = var3 == 0 ? 0.0F : var3 * сзъ - сзМ;
      this.ВI = Math.min(this.ВI, Math.max(0.0F, var4 - сзН));
      this.showFeedback("Deleted " + var1.ВБ, false);
   }

   private String sanitize(String var1) {
      if (var1 == null) {
         return "";
      }

      StringBuilder var2 = new StringBuilder();

      for (int var3 = 0; var3 < var1.length() && var2.length() < 16; var3++) {
         char var4 = var1.charAt(var3);
         if (var4 < 128 && (Character.isLetterOrDigit(var4) || var4 == '_')) {
            var2.append(var4);
         }
      }

      return var2.toString();
   }

   @Override
   public boolean shouldCloseOnEsc() {
      return false;
   }

   private boolean isValid(String var1) {
      return var1.length() >= 3 && var1.length() <= 16 && var1.matches(сзл);
   }

   private void drawIcon(String var1, float var2, float var3, float var4, float var5, float var6) {
      boolean var7 = var1.equals(сС6) || var1.equals(сС_);
      int var8 = var7 ? ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), var6) : ColorMath.packArgb(255, 255, 255, сСШ * var6);
      float var9 = Math.round(var2);
      float var10 = Math.round(var3);
      float var11 = Math.max(1, Math.round(var4));
      float var12 = Math.max(1, Math.round(var5));
      UiDrawing.drawTexture(
         var9, var10, var11, var12, 0.0F, new Vector4f(0.0F, 0.0F, 1.0F, 1.0F), var8, ClientResourceIds.getImageId("mainmenu/account_manager/" + var1), FilterMode.LINEAR
      );
   }

   private void drawCurrentSession(float var1) {
      this.drawFace(this.ВО + сС必, this.ВФ + сСщ, сСГ, var1);
      String var2 = this.fitText(this.minecraft.getUser().getName(), сСл, 12);
      UiDrawing.drawText(ClientFonts.getMediumFont(), сСэ, this.ВО + сСю, this.ВФ + сСЩ, 9, ColorMath.packArgb(108, 108, 108, сСу * var1));
      UiDrawing.drawText(ClientFonts.getMediumFont(), var2, this.ВО + сС2, this.ВФ + сСф, 12, ColorMath.packArgb(255, 255, 255, сСи * var1));
   }

   private AccountManagerScreen.AccountRow findAccount(String var1) {
      String var2 = var1.toLowerCase(Locale.ROOT);

      for (AccountManagerScreen.AccountRow var4 : this.必_2571477d) {
         if (!var4.Х2 && var4.ВБ.toLowerCase(Locale.ROOT).equals(var2)) {
            return var4;
         }
      }

      return null;
   }

   private void showFeedback(String var1, boolean var2) {
      this.се = var1;
      this.сш = var2;
      this.с诶 = System.currentTimeMillis() + сзэ;
      this.щ.setImmediately(0.0F);
   }

   private void drawFace(float var1, float var2, float var3, float var4) {
      UiDrawing.drawTexture(
         var1,
         var2,
         var3,
         var3,
         сС9,
         new Vector4f(сСо, сСO, сС7, сСР),
         ColorMath.packArgb(255, 255, 255, сС3 * var4),
         ClientResourceIds.getImageId(сСр),
         FilterMode.NEAREST
      );
   }

   @Override
   protected void extractPanorama(GuiGraphicsExtractor var1, float var2) {
   }

   private void drawAccount(AccountManagerScreen.AccountRow var1, float var2) {
      float var3 = var1.сЪ.getValue();
      float var4 = var1.ст.getValue();
      float var5 = var1.са.getValue();
      float var6 = var1.сЮ.getValue();
      float var7 = var1.п6;
      float var8 = var1.п_ + сБз * (1.0F - var3);
      UiDrawing.setGlobalAlpha(var2 * var3);
      int var9 = ColorMath.packArgb(15 + (int)(сБц * var4), 19 + (int)(сБя * var4), 28 + (int)(сСс * var4), сСВ + сСЯ * var4);
      UiDrawing.drawRoundedRect(var7, var8, сСХ, сСе, сСА, var9);
      UiDrawing.drawOutline(var7, var8, сС4, сСп, сСД, сСЪ, ColorMath.packArgb(255, 255, 255, сСт + сСа * var4));
      this.drawFace(var7 + сСЮ, var8 + сСЧ, сСЛ, 1.0F);
      int var10 = ColorMath.lerpArgb(ColorMath.packArgb(190, 190, 195, сС诶), ColorMath.packArgb(255, 255, 255, сС1), var4);
      int var11 = ColorMath.lerpArgb(var10, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), var5);
      var11 = ColorMath.withAlpha(var11, var3 * var2 / сСЬ);
      UiDrawing.drawText(ClientFonts.getMediumFont(), this.fitText(var1.ВБ, сС衣, 12), var7 + сСш, var8 + сСИ, 12, var11);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var1.ВС, var7 + сСь, var8 + сСж, 10, ColorMath.packArgb(108, 108, 108, сС弟));
      this.drawIcon(сСб, var7 + сСЫ, var8 + сСl, сС8, сС5, 1.0F);
      float var12 = сСв * (1.0F + сСъ * var6);
      this.drawIcon(var1.Ху ? сСМ : сСН, var7 + сСн - var12 * сСЦ, var8 + сСч - var12 * сСЭ, var12, var12, 1.0F);
      UiDrawing.setGlobalAlpha(var2);
   }

   public AccountManagerScreen(Screen var1) {
      super(net.minecraft.network.chat.Component.literal(с西0));
      this.ч = new FloatAnimation(0.0F, с西х, Easings.CUBIC_OUT);
      this.Э = new FloatAnimation(0.0F, с西У, Easings.CUBIC_OUT);
      this.必_04d91340 = new FloatAnimation(0.0F, с西З, Easings.CUBIC_OUT);
      this.щ = new FloatAnimation(0.0F, с西Е, Easings.CUBIC_OUT);
      this.Я_b9e95bbb = new FloatAnimation[]{
         new FloatAnimation(0.0F, с西П, Easings.CUBIC_OUT), new FloatAnimation(0.0F, с西西, Easings.CUBIC_OUT), new FloatAnimation(0.0F, с西д, Easings.CUBIC_OUT)
      };
      this.Х_b9e95bbb = new FloatAnimation[]{
         new FloatAnimation(0.0F, с西Б, Easings.CUBIC_OUT), new FloatAnimation(0.0F, с西С, Easings.CUBIC_OUT), new FloatAnimation(0.0F, с西з, Easings.CUBIC_OUT)
      };
      this.сХ = "";
      this.се = "";
      this.Х_af10102c = var1;
      this.В.load();

      for (AccountStore.AccountEntry var3 : this.В.getAccountsSnapshot()) {
         String var4 = this.sanitize(var3.name());
         if (this.isValid(var4) && this.findAccount(var4) == null) {
            this.必_2571477d.add(new AccountManagerScreen.AccountRow(var4, var3.date(), var3.favorite(), this.сЬ++));
         }
      }

      this.sortAccounts();
   }

   private void drawAccounts(float var1, float var2, float var3) {
      int var4 = 0;
      float var5 = UiDrawing.getGlobalAlpha();
      UiDrawing.pushScissor(this.ВО + сБЙ, this.ВФ + сБк, сБг, сБ0);

      for (AccountManagerScreen.AccountRow var7 : this.必_2571477d) {
         var7.сЪ.updateTarget(var7.Х2 ? 0.0F : 1.0F);
         if (!var7.Х2 || !(var7.сЪ.getValue() <= сБх)) {
            int var8 = var4 % 3;
            int var9 = var4 / 3;
            float var10 = this.ВО + сБУ + var8 * сБЗ;
            float var11 = this.ВФ + сБЕ + var9 * сБП;
            if (!var7.Хф) {
               var7.сЧ.setImmediately(var10 + сБ西);
               var7.сЛ.setImmediately(var11 + сБд);
               var7.Хф = true;
            }

            var7.сЧ.updateTarget(var10);
            var7.сЛ.updateTarget(var11);
            var7.п6 = var7.сЧ.getValue();
            var7.п_ = var7.сЛ.getValue() - this.ВТ;
            boolean var12 = WorldProjection.containsPoint(var1, var2, var7.п6, var7.п_, сББ, сБС);
            var7.ст.updateTarget(var12 ? 1.0F : 0.0F);
            boolean var13 = this.minecraft.getUser().getName().equalsIgnoreCase(var7.ВБ);
            var7.са.updateTarget(var13 ? 1.0F : 0.0F);
            var7.сЮ.updateTarget(var7.Ху ? 1.0F : 0.0F);
            this.drawAccount(var7, var5);
            var4++;
         }
      }

      UiDrawing.setGlobalAlpha(var5);
      UiDrawing.popScissor();
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.сИ && this.сХ.length() < 16) {
         int var2 = var1.codepoint();
         if (var2 < 128 && (Character.isLetterOrDigit(var2) || var2 == 95)) {
            this.сХ = this.сХ + var1.codepointAsString();
         }

         return true;
      } else {
         return true;
      }
   }

   private void clearAccounts() {
      if (this.必_2571477d.stream().noneMatch(var0 -> !var0.Х2)) {
         this.showFeedback(сз5, true);
      } else {
         for (AccountManagerScreen.AccountRow var2 : this.必_2571477d) {
            var2.Х2 = true;
         }

         this.ВI = 0.0F;
         this.saveAccounts();
         this.showFeedback(сзв, false);
      }
   }

   private void drawSidebarButton(int var1, String var2, float var3, float var4, float var5, float var6, String var7, boolean var8) {
      float var9 = this.ВО + сБб;
      boolean var10 = WorldProjection.containsPoint(var4, var5, var9, var3, сБЫ, сБl);
      this.Я_b9e95bbb[var1].updateTarget(var10 ? 1.0F : 0.0F);
      this.Х_b9e95bbb[var1].updateTarget(0.0F);
      float var11 = this.Я_b9e95bbb[var1].getValue();
      float var12 = this.Х_b9e95bbb[var1].getValue();
      UiDrawing.drawBlur(var9, var3, сБ8, сБ5, сБв, сБъ, ColorMath.packArgb(255, 255, 255, сБМ * var6));
      if (var8) {
         UiDrawing.drawRoundedRect(var9, var3, сБН, сБн, сБЦ, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), (сБч + сБЭ * var11 + сБ必 * var12) / сБщ * var6));
      } else {
         UiDrawing.drawRoundedRect(var9, var3, сБГ, сБл, сБэ, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), (сБю * var11 + сБЩ * var12) * var6));
      }

      UiDrawing.drawOutline(var9, var3, сБу, сБ2, сБф, сБи, ColorMath.packArgb(255, 255, 255, (сБ9 + сБо * var11) * var6));
      int var13 = Math.round(сБO + сБ7 * var11);
      int var14 = var8 ? ColorMath.packArgb(255, 255, 255, сБР * var6) : ColorMath.packArgb(var13, var13, var13, сБ3 * var6);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var2, var9 + сБр, var3 + сБ6, 13, var14);
      this.drawIcon(var7, var9 + сБ_, var3 + сБШ, сБы, var7.equals(сБй) ? сБЖ : сБм, var6);
   }

   @Override
   protected void init() {
      this.ч.setImmediately(0.0F);
      this.сИ = false;
      this.сь = false;
      this.с衣 = 0L;
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   private String visibleNickname() {
      String var1 = this.сХ;

      while (!var1.isEmpty() && UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, 13) > сзщ) {
         var1 = var1.substring(1);
      }

      return var1;
   }

   private void drawSquareButton(float var1, float var2, float var3) {
      float var4 = this.ВО + сБЯ;
      float var5 = this.ВФ + сБХ;
      boolean var6 = WorldProjection.containsPoint(var1, var2, var4, var5, сБе, сБА);
      this.Я_b9e95bbb[0].updateTarget(var6 ? 1.0F : 0.0F);
      this.Х_b9e95bbb[0].updateTarget(0.0F);
      float var7 = this.Я_b9e95bbb[0].getValue();
      float var8 = this.Х_b9e95bbb[0].getValue();
      UiDrawing.drawBlur(var4, var5, сБ4, сБп, сБД, сБЪ, ColorMath.packArgb(255, 255, 255, сБт * var3));
      UiDrawing.drawOutline(var4, var5, сБа, сБЮ, сБЧ, сБЛ, ColorMath.packArgb(255, 255, 255, (сБ诶 + сБ1 * var7) * var3));
      float var9 = сБЬ - var8 * сБ衣;
      this.drawIcon(сБш, var4 + (сБИ - var9) * сБь, var5 + (сБж - var9) * сБ弟, var9, var9, var3);
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      Vector2f var9 = WorldProjection.guiToScreen(var1, var3);
      if (!WorldProjection.containsPoint(var9.x, var9.y, this.ВО + сз诶, this.ВФ + сз1, сзЬ, сз衣)) {
         return true;
      }

      int var10 = (int)this.必_2571477d.stream().filter(var0 -> !var0.Х2).count();
      int var11 = (var10 + 2) / 3;
      float var12 = var11 == 0 ? 0.0F : var11 * сзш - сзИ;
      float var13 = Math.max(0.0F, var12 - сзь);
      this.ВI = Math.clamp(this.ВI - (float)var7 * сзж, 0.0F, var13);
      return true;
   }

   @Override
   protected void extractMenuBackground(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5) {
   }

   private void sortAccounts() {
      this.必_2571477d.sort(Comparator.<AccountManagerScreen.AccountRow, Boolean>comparing(var0 -> !var0.Ху).thenComparingLong(var0 -> var0.Ха));
   }

   private void switchSession(AccountManagerScreen.AccountRow var1) {
      UUID var2 = UUID.nameUUIDFromBytes(("OfflinePlayer:" + var1.ВБ).getBytes(StandardCharsets.UTF_8));
      User var3 = new User(var1.ВБ, var2, "", Optional.empty(), Optional.empty());
      ((MinecraftUserAccessor)this.minecraft).rose$setSession(var3);
      var1.са.setImmediately(сзн);
      this.showFeedback("Switched to " + var1.ВБ, false);
   }

   private void saveAccounts() {
      List var1 = this.必_2571477d.stream().filter(var0 -> !var0.Х2).map(var0 -> new AccountStore.AccountEntry(var0.ВБ, var0.ВС, var0.Ху)).toList();
      this.В.replaceAccounts(var1);
   }

   private void drawPanel(float var1, float var2, float var3) {
      float var4 = сд衣;
      UiDrawing.drawBlur(this.ВО, this.ВФ, сдш, сдИ, сдь, var4, new Vector4i(-1));
      UiDrawing.drawRoundedRect(this.ВО, this.ВФ, сдж, сд弟, var4, ColorMath.packArgb(0, 0, 0, сдб));
      UiDrawing.drawRoundedRect(this.ВО + 1.0F, this.ВФ + 1.0F, сдЫ, сдl, new Vector4f(сд8, сд5, 0.0F, 0.0F), ColorMath.packArgb(15, 19, 28, сдв * var3));
      UiDrawing.drawOutline(this.ВО, this.ВФ, сдъ, сдМ, 1.0F, var4, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      UiDrawing.drawRoundedRect(Math.round(this.ВО + сдН), this.ВФ, 1.0F, сдн, 0.0F, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      UiDrawing.drawRoundedRect(this.ВО + 1.0F, Math.round(this.ВФ + сдЦ), сдч, 1.0F, 0.0F, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      this.drawIcon(сдЭ, this.ВО + сд必, this.ВФ + сдщ, сдГ, сдл, var3);
      this.drawSidebar(var1, var2, var3);
      this.drawAccounts(var1, var2, var3);
      this.drawCurrentSession(var3);
   }

   private void drawSidebar(float var1, float var2, float var3) {
      UiDrawing.drawText(ClientFonts.getMediumFont(), сдэ, this.ВО + сдю, this.ВФ + сдЩ, 13, ColorMath.packArgb(108, 108, 108, сду * var3));
      float var4 = this.ВО + сд2;
      float var5 = this.ВФ + сдф;
      boolean var6 = WorldProjection.containsPoint(var1, var2, var4, var5, сди, сд9);
      this.Э.updateTarget(!this.сИ && !var6 ? 0.0F : 1.0F);
      float var7 = this.Э.getValue();
      float var8 = this.必_04d91340.getValue();
      UiDrawing.drawBlur(var4, var5, сдо, сдO, сд7, сдР, ColorMath.packArgb(255, 255, 255, сд3 * var3));
      int var9 = ColorMath.packArgb(255, 255, 255, (сдр + сд6 * var7) * var3);
      int var10 = ColorMath.packArgb(214, 91, 101, сд_ * var3);
      UiDrawing.drawOutline(var4, var5, сдШ, сды, сдй, сдЖ, ColorMath.lerpArgb(var9, var10, var8));
      String var11 = this.visibleNickname();
      if (var11.isEmpty()) {
         var11 = сдм;
      }

      int var12 = this.сХ.isEmpty() ? ColorMath.packArgb(108, 108, 108, сдК * var3) : ColorMath.packArgb(255, 255, 255, сдI * var3);
      UiDrawing.pushScissor(var4 + сдТ, var5 + сдО, сдФ, сдЙ);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var11, var4 + сдк, var5 + сдг, 13, var12);
      if (this.сИ && System.currentTimeMillis() / сд0 % сдх == 0L) {
         float var13 = var4 + сдУ + UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var11, 13) + 1.0F;
         UiDrawing.drawRoundedRect(var13, var5 + сдЗ, 1.0F, сдЕ, 0.0F, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
      }

      UiDrawing.popScissor();
      this.drawSquareButton(var1, var2, var3);
      UiDrawing.drawText(ClientFonts.getMediumFont(), сдП, this.ВО + сд西, this.ВФ + сдд, 13, ColorMath.packArgb(108, 108, 108, сдБ * var3));
      this.drawSidebarButton(1, сдС, this.ВФ + сдз, var1, var2, var3, сдц, false);
      this.drawSidebarButton(2, сдя, this.ВФ + сБс, var1, var2, var3, сБВ, true);
      this.drawFeedback(var3);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.сь) {
         return true;
      }

      Vector2f var3 = WorldProjection.guiToScreen(var1.x(), var1.y());
      if (var1.button() == 1) {
         this.сИ = false;
         if (!WorldProjection.containsPoint(var3.x, var3.y, this.ВО + сСы, this.ВФ + сСй, сСЖ, сСм)) {
            return true;
         }

         for (int var6 = this.必_2571477d.size() - 1; var6 >= 0; var6--) {
            AccountManagerScreen.AccountRow var7 = this.必_2571477d.get(var6);
            if (!var7.Х2 && WorldProjection.containsPoint(var3.x, var3.y, var7.п6, var7.п_, сСК, сСI)) {
               this.deleteAccount(var7);
               return true;
            }
         }

         return true;
      } else {
         if (var1.button() != 0) {
            return true;
         }

         this.сИ = WorldProjection.containsPoint(var3.x, var3.y, this.ВО + сСТ, this.ВФ + сСО, сСФ, сСЙ);
         if (WorldProjection.containsPoint(var3.x, var3.y, this.ВО + сСк, this.ВФ + сСг, сС0, сСх)) {
            this.Х_b9e95bbb[0].setImmediately(1.0F);
            this.addAccount();
            return true;
         }

         if (WorldProjection.containsPoint(var3.x, var3.y, this.ВО + сСУ, this.ВФ + сСЗ, сСЕ, сСП)) {
            this.Х_b9e95bbb[1].setImmediately(1.0F);
            this.сХ = this.generateNickname();
            this.сИ = true;
            this.showFeedback("Generated " + this.сХ, false);
            return true;
         }

         if (WorldProjection.containsPoint(var3.x, var3.y, this.ВО + сС西, this.ВФ + сСд, сСБ, сСС)) {
            this.Х_b9e95bbb[2].setImmediately(1.0F);
            this.clearAccounts();
            return true;
         }

         if (!WorldProjection.containsPoint(var3.x, var3.y, this.ВО + сСз, this.ВФ + сСц, сСя, сзс)) {
            return true;
         }

         for (int var4 = this.必_2571477d.size() - 1; var4 >= 0; var4--) {
            AccountManagerScreen.AccountRow var5 = this.必_2571477d.get(var4);
            if (!var5.Х2 && WorldProjection.containsPoint(var3.x, var3.y, var5.п6, var5.п_, сзВ, сзЯ)) {
               if (WorldProjection.containsPoint(var3.x, var3.y, var5.п6 + сзХ, var5.п_ + сзе, сзА, сз4)) {
                  this.minecraft.keyboardHandler.setClipboard(var5.ВБ);
                  this.showFeedback("Copied " + var5.ВБ, false);
               } else if (WorldProjection.containsPoint(var3.x, var3.y, var5.п6 + сзп, var5.п_ + сзД, сзЪ, сзт)) {
                  var5.Ху = !var5.Ху;
                  var5.сЮ.setImmediately(var5.Ху ? сза : сзЮ);
                  this.sortAccounts();
                  this.saveAccounts();
                  this.showFeedback(var5.Ху ? сзЧ : сзЛ, false);
               } else {
                  this.switchSession(var5);
               }

               return true;
            }
         }

         return true;
      }
   }

   private String fitText(String var1, float var2, int var3) {
      if (var1 != null && !var1.isEmpty()) {
         if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, var3) <= var2) {
            return var1;
         }

         String var4 = сзГ;
         float var5 = Math.max(0.0F, var2 - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var4, var3));
         int var6 = 0;

         while (var6 < var1.length()) {
            int var7 = var1.offsetByCodePoints(var6, 1);
            if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1.substring(0, var7), var3) > var5) {
               break;
            }

            var6 = var7;
         }

         return var1.substring(0, var6) + var4;
      } else {
         return "";
      }
   }

   private void drawFeedback(float var1) {
      float var2 = this.щ.getValue();
      if (!(var2 <= сБК) && !this.се.isEmpty()) {
         int var3 = this.сш ? ColorMath.packArgb(214, 91, 101, сБI) : ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
         float var4 = this.ВФ + сБТ - сБО * (1.0F - var2);
         UiDrawing.drawText(ClientFonts.getMediumFont(), this.се, this.ВО + сБФ, var4, 11, ColorMath.withAlpha(var3, var2 * var1));
      }
   }

   private String generateNickname() {
      ThreadLocalRandom var1 = ThreadLocalRandom.current();

      for (int var2 = 0; var2 < 100; var2++) {
         String var3 = var1.nextBoolean() ? "" : сзЦ;
         String var4 = field_4[var1.nextInt(field_4.length)] + var3 + п[var1.nextInt(п.length)];
         if (var1.nextFloat() < сзч) {
            var4 = var4 + var1.nextInt(10, 1000);
         }

         if (var4.length() > 16) {
            var4 = var4.substring(0, 16);
         }

         if (this.findAccount(var4) == null) {
            return var4;
         }
      }

      return "Player" + var1.nextInt(сзЭ, сз必);
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (var1.key() == 256) {
         this.сь = true;
         this.сИ = false;
         return true;
      } else if (!this.сИ) {
         return true;
      } else if (var1.key() == 257 || var1.key() == 335) {
         this.addAccount();
         return true;
      } else if (var1.key() == 259 && !this.сХ.isEmpty()) {
         int var2 = this.сХ.offsetByCodePoints(this.сХ.length(), -1);
         this.сХ = this.сХ.substring(0, var2);
         return true;
      } else if (var1.key() == 86 && (var1.modifiers() & 2) != 0) {
         this.сХ = this.sanitize(this.minecraft.keyboardHandler.getClipboard());
         return true;
      } else {
         return true;
      }
   }

   private void addAccount() {
      String var1 = this.sanitize(this.сХ);
      if (!this.isValid(var1)) {
         this.с1 = System.currentTimeMillis() + сз弟;
         this.showFeedback(сзб, true);
      } else if (this.findAccount(var1) != null) {
         this.с1 = System.currentTimeMillis() + сзЫ;
         this.showFeedback(сзl, true);
      } else {
         AccountManagerScreen.AccountRow var2 = new AccountManagerScreen.AccountRow(var1, LocalDate.now().format(Я_0551e0cc), false, this.сЬ++);
         this.必_2571477d.add(var2);
         this.sortAccounts();
         this.сХ = "";
         this.saveAccounts();
         this.switchSession(var2);
         this.showFeedback(сз8, false);
      }
   }

   private static final class AccountRow {
      private final String ВБ;
      private final String ВС;
      private final long Ха;
      private final FloatAnimation сЪ;
      private final FloatAnimation ст;
      private final FloatAnimation са;
      private final FloatAnimation сЮ;
      private final FloatAnimation сЧ;
      private final FloatAnimation сЛ;
      private boolean Ху;
      private boolean Х2;
      private boolean Хф;
      private float п6;
      private float п_;
      private static final float сжУ = 7.0F;
      private static final float сжЗ = 10.0F;
      private static final float сжЕ = 9.0F;
      private static final float сжП = 4.5F;
      private static final float сж西 = 4.5F;
      private static final float сжд = 11.0F;

      private AccountRow(String var1, String var2, boolean var3, long var4) {
         this.сЪ = new FloatAnimation(0.0F, сжУ, Easings.CUBIC_OUT);
         this.ст = new FloatAnimation(0.0F, сжЗ, Easings.CUBIC_OUT);
         this.са = new FloatAnimation(0.0F, сжЕ, Easings.CUBIC_OUT);
         this.сЧ = new FloatAnimation(0.0F, сжП, Easings.CUBIC_IN_OUT);
         this.сЛ = new FloatAnimation(0.0F, сж西, Easings.CUBIC_IN_OUT);
         this.ВБ = var1;
         this.ВС = var2;
         this.Ху = var3;
         this.Ха = var4;
         this.сЮ = new FloatAnimation(var3 ? 1.0F : 0.0F, сжд, Easings.CUBIC_OUT);
      }
   }
}
