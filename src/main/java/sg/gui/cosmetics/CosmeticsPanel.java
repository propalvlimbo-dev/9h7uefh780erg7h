package sg.gui.cosmetics;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import settings.render.ClientResourceIds;
import settings.render.ClientLocalization;
import sg.ec.EmojiMaskPreview;
import sg.ec.ClientFonts;
import sg.modules.impl.init.ClickGuiPreferences;
import sg.client.RoseClient;
import sg.gui.click.ClickGuiScreen;
import sg.gui.ClickGuiPanelHost;
import sg.render.WingSelection;
import sg.render.CosmeticCatalog;
import sg.render.HatCatalog;
import sg.render.HatDefinition;
import sg.render.CosmeticCategory;
import sg.render.GuiCoordinateTransform;
import sg.render.CosmeticDefinition;
import sg.render.BackpackPreviewQueue;
import sg.render.WingPreviewQueue;
import sg.render.HatPreviewQueue;
import sg.render.EmojiMaskVariant;
import sg.render.WeaponSkinVariant;
import sg.render.BackpackColorVariant;
import sg.render.ClientCosmeticSelections;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ColorMath;
import sg.animation.Easings;
import sg.animation.FloatAnimation;
import sg.util.time.ThemeEditor;

public final class CosmeticsPanel {
   private static float До = 1.0F;
   private static boolean Хд;
   private static final int по = 3;
   private static final float ДO = 138.0F;
   private static final float Д7 = 18.0F;
   private static final float ДР = 48.0F;
   private static final float Д3 = 78.0F;
   private static final float Др = 14.0F;
   private static final float Д6 = 16.0F;
   private static final float Д_ = 66.0F;
   private static final float ДШ = 104.0F;
   private static final float Ды = 62.0F;
   private static final float Дй = 14.0F;
   private static final float ДЖ = 30.0F;
   private int пO;
   private int п7;
   private int пР;
   private int п3;
   private int пр;
   private int п6;
   private int п_;
   private int пШ;
   private int пы;
   private int пй;
   private int пЖ;
   private int пм;
   private int пК;
   private int пI;
   private static final String Я诶 = "1";
   private static final String Я1 = "8";
   private static final String ЯЬ = "9";
   private final ClickGuiPanelHost А;
   @SerializedName("В")
   private final WingPreviewQueue В_72939cdb = new WingPreviewQueue();
   @SerializedName("В")
   private final HatPreviewQueue В_baa6d7e8 = new HatPreviewQueue();
   @SerializedName("В")
   private final BackpackPreviewQueue В_82353cf7 = new BackpackPreviewQueue();
   private final Map<WingSelection, FloatAnimation> с7 = new EnumMap<>(WingSelection.class);
   private final Map<EmojiMaskVariant, FloatAnimation> сР = new EnumMap<>(EmojiMaskVariant.class);
   private final Map<String, FloatAnimation> с3 = new HashMap<>();
   private final Map<WeaponSkinVariant, FloatAnimation> ср = new EnumMap<>(WeaponSkinVariant.class);
   private final Map<WeaponSkinVariant, FloatAnimation> с6 = new EnumMap<>(WeaponSkinVariant.class);
   private final Map<BackpackColorVariant, FloatAnimation> с_ = new EnumMap<>(
      BackpackColorVariant.class
   );
   private final Map<String, FloatAnimation> сШ = new HashMap<>();
   private final Map<CosmeticsPanel.PanelTab, FloatAnimation> сы = new EnumMap<>(CosmeticsPanel.PanelTab.class);
   private final Map<CosmeticsPanel.PanelTab, FloatAnimation> сй = new EnumMap<>(CosmeticsPanel.PanelTab.class);
   private final Map<CosmeticsPanel.PanelTab, FloatAnimation> сЖ = new EnumMap<>(CosmeticsPanel.PanelTab.class);
   @SerializedName("см")
   private final Map<CosmeticsPanel.WeaponCategory, FloatAnimation> см_ea411899 = new EnumMap<>(CosmeticsPanel.WeaponCategory.class);
   @SerializedName("сК")
   private final Map<CosmeticsPanel.WeaponCategory, FloatAnimation> сК_ea411899 = new EnumMap<>(CosmeticsPanel.WeaponCategory.class);
   private final Map<CosmeticsPanel.FurryCategory, FloatAnimation> сI = new EnumMap<>(CosmeticsPanel.FurryCategory.class);
   private final Map<CosmeticsPanel.FurryCategory, FloatAnimation> сТ = new EnumMap<>(CosmeticsPanel.FurryCategory.class);
   private final FloatAnimation сб;
   private final FloatAnimation сЫ;
   private final FloatAnimation сl;
   private final FloatAnimation с8;
   private long ХН;
   private float Дм;
   @SerializedName("В")
   private CosmeticsPanel.PanelTab В_ed2fe0c8;
   @SerializedName("В")
   private CosmeticsPanel.WeaponCategory В_c7082522;
   @SerializedName("В")
   private CosmeticsPanel.FurryCategory В_1320c117;
   private String Я衣;
   private boolean ХБ;
   private boolean ХС;
   private boolean Хз;
   private boolean Хц;
   @SerializedName("8")
   private int[] field_8;
   private int пТ;
   @SerializedName("см")
   private List<HatDefinition> см_2571477d;
   @SerializedName("сК")
   private List<CosmeticDefinition> сК_2571477d;
   private static final float Вчй = 10.0F;
   private static final float ВчЖ = 10.0F;
   private static final float Вчм = 10.0F;
   private static final float ВчК = 8.0F;
   private static final float ВчI = 24.0F;
   private static final float ВчТ = 28.0F;
   private static final float ВчО = 12.0F;
   private static final float ВчФ = 16.0F;
   private static final float ВчЙ = 28.0F;
   private static final float Вчк = 3.0F;
   private static final float Вчг = 36.0F;
   private static final float Вч0 = 32.0F;
   private static final float Вчх = 5.0F;
   private static final float ВчУ = 220.0F;
   private static final float ВчЗ = 0.4F;
   private static final float ВчЕ = 16.0F;
   private static final float ВчП = 10.0F;
   private static final float Вч西 = -1.0F;
   private static final long Вчд = 150L;
   private static final float ВчБ = 16.0F;
   private static final float ВчС = 18.0F;
   private static final String Вчз = "Ничего не найдено";
   private static final String Вчц = "No cosmetics found";
   private static final float Вчя = 0.5F;
   private static final float ВЭс = 124.0F;
   private static final String ВЭВ = "Попробуйте другое название";
   private static final String ВЭЯ = "Try a different name";
   private static final float ВЭХ = 0.5F;
   private static final float ВЭе = 147.0F;
   private static final float ВЭА = 0.2F;
   private static final float ВЭ4 = 24.0F;
   private static final float ВЭп = 22.0F;
   private static final float ВЭД = 16.0F;
   private static final float ВЭЪ = 16.0F;
   private static final float ВЭт = 16.0F;
   private static final float ВЭа = 54.0F;
   private static final float ВЭЮ = 10.0F;
   private static final float ВЭЧ = 60.0F;
   private static final float ВЭЛ = 20.0F;
   private static final float ВЭ诶 = 54.0F;
   private static final String ВЭ1 = "P";
   private static final float ВЭЬ = 0.5F;
   private static final String ВЭ衣 = "P";
   private static final float ВЭш = 0.5F;
   private static final float ВЭИ = 14.5F;
   private static final float ВЭь = 0.01F;
   private static final float ВЭж = 20.0F;
   private static final float ВЭ弟 = 64.0F;
   private static final float ВЭб = 30.0F;
   private static final float ВЭЫ = 120.0F;
   private static final String ВЭl = "ГЛАВНОЕ";
   private static final String ВЭ8 = "MAIN";
   private static final float ВЭ5 = 24.0F;
   private static final float ВЭв = 77.0F;
   private static final String ВЭъ = "ОБЩЕЕ";
   private static final String ВЭМ = "COMMON";
   private static final float ВЭН = 24.0F;
   private static final float ВЭн = 180.0F;
   private static final String ВЭЦ = "ПРОЧЕЕ";
   private static final String ВЭч = "OTHER";
   private static final float ВЭЭ = 24.0F;
   private static final float ВЭ必 = 283.0F;
   private static final float ВЭщ = 0.001F;
   private static final float ВЭГ = 14.0F;
   private static final float ВЭл = 30.0F;
   private static final float ВЭэ = 8.0F;
   private static final float ВЭю = 255.0F;
   private static final float ВЭЩ = 24.0F;
   private static final float ВЭу = 8.0F;
   private static final float ВЭ2 = 13.0F;
   private static final float ВЭф = 0.01F;
   private static final float ВЭи = 42.0F;
   private static final float ВЭ9 = 54.0F;
   private static final float ВЭо = 30.0F;
   private static final float ВЭO = 46.0F;
   private static final float ВЭ7 = 7.0F;
   private static final float ВЭР = 12.0F;
   private static final float ВЭ3 = 96.0F;
   private static final float ВЭр = 97.0F;
   private static final float ВЭ6 = 132.0F;
   private static final float ВЭ_ = 200.0F;
   private static final float ВЭШ = 235.0F;
   private static final float ВЭы = 303.0F;
   private static final float ВЭй = 338.0F;
   private static final float ВЭЖ = 373.0F;
   private static final float ВЭм = 14.0F;
   private static final float ВЭК = 24.0F;
   private static final String ВЭI = "Детали, которые делают образ вашим.";
   private static final String ВЭТ = "Cosmetic items for your character's own style.";
   private static final float ВЭО = 48.0F;
   private static final float ВЭФ = 24.0F;
   private static final float ВЭЙ = 15.0F;
   private static final float ВЭк = 24.0F;
   private static final float ВЭг = 20.0F;
   private static final float ВЭ0 = 230.0F;
   private static final float ВЭх = 24.0F;
   private static final float ВЭУ = 22.0F;
   private static final float ВЭЗ = 16.0F;
   private static final float ВЭЕ = 16.0F;
   private static final float ВЭП = 16.0F;
   private static final float ВЭ西 = 54.0F;
   private static final String ВЭд = "P";
   private static final float ВЭБ = 16.0F;
   private static final float ВЭС = 18.0F;
   private static final float ВЭз = 16.0F;
   private static final float ВЭц = 18.0F;
   private static final String ВЭя = "Предпросмотр";
   private static final String В必с = "Preview";
   private static final float В必В = 42.0F;
   private static final float В必Я = 20.0F;
   private static final float В必Х = 72.0F;
   private static final float В必е = 6.0F;
   private static final float В必А = 42.0F;
   private static final float В必4 = 6.0F;
   private static final float В必п = 186.0F;
   private static final float В必Д = 255.0F;
   private static final float В必Ъ = 78.0F;
   private static final float В必т = 504.0F;
   private static final float В必а = 0.12F;
   private static final float В必Ю = 10.0F;
   private static final float В必Ч = 20.0F;
   private static final float В必Л = 112.0F;
   private static final float В必诶 = 12.0F;
   private static final float В必1 = 32.0F;
   private static final float В必Ь = 24.0F;
   private static final String В必衣 = "Показывать косметику";
   private static final String В必ш = "Show cosmetics on";
   private static final float В必И = 14.0F;
   private static final float В必ь = 22.0F;
   private static final String В必ж = "На себя";
   private static final float В必弟 = 28.0F;
   private static final float В必б = 36.0F;
   private static final String В必Ы = "На друзей";
   private static final float В必l = 36.0F;
   private static final float В必8 = 28.0F;
   private static final float В必5 = 72.0F;
   private static final String В必в = "На остальных";
   private static final float В必ъ = 72.0F;
   private static final float В必М = 28.0F;
   private static final String В必Н = "Включено";
   private static final String В必н = "Enabled";
   private static final String В必Ц = "Выключено";
   private static final String В必ч = "Disabled";
   private static final float В必Э = 28.0F;
   private static final float В必必 = 28.0F;
   private static final float В必щ = 8.0F;
   private static final float В必Г = 0.01F;
   private static final float В必л = 8.0F;
   private static final float В必э = 48.0F;
   private static final float В必ю = 28.0F;
   private static final float В必Щ = 12.0F;
   private static final float В必у = 7.0F;
   private static final float В必2 = 0.45F;
   private static final float В必ф = 28.0F;
   private static final float В必и = 0.5F;
   private static final float В必9 = 38.0F;
   private static final float В必о = 6.0F;
   private static final float В必O = 28.0F;
   private static final float В必7 = 7.0F;
   private static final float В必Р = 9.0F;
   private static final float В必3 = 8.0F;
   private static final float В必р = 31.0F;
   private static final float В必6 = 7.0F;
   private static final float В必_ = 0.45F;
   private static final float В必Ш = 0.4F;
   private static final float В必ы = 28.0F;
   private static final float В必й = 16.0F;
   private static final float В必Ж = 8.0F;
   private static final float В必м = 3.0F;
   private static final float В必К = 12.0F;
   private static final float В必I = 3.0F;
   private static final float В必Т = 10.0F;
   private static final float В必О = 10.0F;
   private static final float В必Ф = 5.0F;
   private static final float В必Й = 13.0F;
   private static final float В必к = 13.0F;
   private static final float В必г = 3.0F;
   private static final float В必0 = 13.0F;
   private static final float В必х = 13.0F;
   private static final float В必У = 3.0F;
   private static final float В必З = 0.01F;
   private static final String В必Е = "8";
   private static final float В必П = 0.5F;
   private static final float В必西 = 11.0F;
   private static final float В必д = 11.0F;
   private static final String В必Б = "1";
   private static final float В必С = 15.0F;
   private static final float В必з = 15.0F;
   private static final float В必ц = 15.0F;
   private static final float В必я = 15.0F;
   private static final float Вщс = 18.0F;
   private static final float ВщВ = 48.0F;
   private static final float ВщЯ = 78.0F;
   private static final float ВщХ = 0.76F;
   private static final float Вще = 0.24F;
   private static final float ВщА = 18.0F;
   private static final float Вщ4 = 48.0F;
   private static final float Вщп = 78.0F;
   private static final float ВщД = 0.76F;
   private static final float ВщЪ = 0.24F;
   private static final float Вщт = 0.76F;
   private static final float Вща = 0.24F;
   private static final float ВщЮ = 18.0F;
   private static final float ВщЧ = 48.0F;
   private static final float ВщЛ = 78.0F;
   private static final float Вщ诶 = 18.0F;
   private static final float Вщ1 = 48.0F;
   private static final float ВщЬ = 78.0F;
   private static final float Вщ衣 = 0.76F;
   private static final float Вщш = 0.24F;
   private static final float ВщИ = 64.0F;
   private static final float Вщь = 104.0F;
   private static final float Вщж = 8.0F;
   private static final float Вщ弟 = 28.0F;
   private static final float Вщб = 28.0F;
   private static final float ВщЫ = 10.0F;
   private static final String Вщl = "Мечи";
   private static final String Вщ8 = "Swords";
   private static final String Вщ5 = "Булава";
   private static final String Вщв = "Mace";
   private static final String Вщъ = "hat";
   private static final String ВщМ = "mask";
   private static final String ВщН = "wings";
   private static final String Вщн = "backpack";
   private static final String ВщЦ = "pets";
   private static final String Вщч = "furry";
   private static final float ВщЭ = 0.5F;
   private static final float Вщ必 = 0.5F;
   private static final float Вщщ = 0.76F;
   private static final float ВщГ = 0.24F;
   private static final float Вщл = 18.0F;
   private static final float Вщэ = 48.0F;
   private static final float Вщю = 78.0F;
   private static final float ВщЩ = 64.0F;
   private static final float Вщу = 8.0F;
   private static final float Вщ2 = 28.0F;
   private static final float Вщф = 28.0F;
   private static final float Вщи = 10.0F;
   private static final float Вщ9 = 0.35F;
   private static final float Вщо = 0.76F;
   private static final float ВщO = 0.24F;
   private static final float Вщ7 = 18.0F;
   private static final float ВщР = 48.0F;
   private static final float Вщ3 = 78.0F;
   private static final float Вщр = 138.0F;
   private static final float Вщ6 = 12.0F;
   private static final float Вщ_ = 136.0F;
   private static final float ВщШ = 11.0F;
   private static final float Вщы = 255.0F;
   private static final float Вщй = 0.35F;
   private static final float ВщЖ = 138.0F;
   private static final float Вщм = 12.0F;
   private static final float ВщК = 38.0F;
   private static final float ВщI = 11.0F;
   private static final float ВщТ = 12.0F;
   private static final float ВщО = 12.0F;
   private static final float ВщФ = 25.0F;
   private static final float ВщЙ = 13.0F;
   private static final float Вщк = 0.001F;
   private static final float Вщг = 12.0F;
   private static final float Вщ0 = 30.0F;
   private static final float Вщх = 26.0F;
   private static final float ВщУ = 26.0F;
   private static final float ВщЗ = 30.0F;
   private static final float ВщЕ = 14.0F;
   private static final float ВщП = 30.0F;
   private static final float Вщ西 = 96.0F;
   private static final float Вщд = 28.0F;
   private static final float ВщБ = 30.0F;
   private static final float ВщС = 112.0F;
   private static final float Вщз = 210.0F;
   private static final float Вщц = 28.0F;
   private static final float Вщя = 36.0F;
   private static final float ВГс = 210.0F;
   private static final float ВГВ = 28.0F;
   private static final float ВГЯ = 72.0F;
   private static final float ВГХ = 210.0F;
   private static final float ВГе = 28.0F;
   private static final float ВГА = 64.0F;
   private static final float ВГ4 = 104.0F;
   private static final float ВГп = 8.0F;
   private static final float ВГД = 28.0F;
   private static final float ВГЪ = 64.0F;
   private static final float ВГт = 8.0F;
   private static final float ВГа = 28.0F;
   private static final float ВГЮ = 138.0F;
   private static final float ВГЧ = 138.0F;
   private static final float ВГЛ = 138.0F;
   private static final float ВГ诶 = 138.0F;
   private static final float ВГ1 = 138.0F;
   private static final float ВГЬ = 138.0F;
   private static final float ВГ衣 = 62.0F;
   private static final float ВГш = 154.0F;
   private static final float ВГИ = 16.0F;
   private static final float ВГь = 4.0F;
   private static final float ВГж = 78.0F;
   private static final float ВГ弟 = 30.0F;
   private static final float ВГб = 8.0F;
   private static final float ВГЫ = 0.55F;
   private static final float ВГl = 30.0F;
   private static final String ВГ8 = "t";
   private static final float ВГ5 = 10.0F;
   private static final float ВГв = 9.0F;
   private static final float ВГъ = 12.0F;
   private static final float ВГМ = 12.0F;
   private static final float ВГН = 30.0F;
   private static final float ВГн = 62.0F;
   private static final float ВГЦ = 4.0F;
   private static final float ВГч = 22.0F;
   private static final float ВГЭ = 6.0F;
   private static final float ВГ必 = 18.0F;
   private static final float ВГщ = 0.25F;
   private static final String ВГГ = "Поиск в категории…";
   private static final String ВГл = "Search category…";
   private static final float ВГэ = 8.0F;
   private static final long ВГю = 1000L;
   private static final long ВГЩ = 500L;
   private static final float ВГу = 8.0F;
   private static final float ВГ2 = 13.0F;
   private static final String ВГф = "9";
   private static final float ВГи = 23.0F;
   private static final float ВГ9 = 9.0F;
   private static final float ВГо = 12.0F;
   private static final float ВГO = 12.0F;
   private static final float ВГ7 = 62.0F;
   private static final float ВГР = 26.0F;
   private static final float ВГ3 = 6.0F;
   private static final float ВГр = 6.0F;
   private static final float ВГ6 = 104.0F;
   private static final float ВГ_ = 66.0F;
   private static final float ВГШ = 62.0F;
   private static final float ВГы = 66.0F;
   private static final float ВГй = 62.0F;
   private static final float ВГЖ = 14.0F;
   private static final float ВГм = 154.0F;
   private static final float ВГК = 138.0F;
   private static final float ВГI = 62.0F;
   private static final float ВГТ = 7.0F;
   private static final float ВГО = 0.5F;
   private static final float ВГФ = 68.0F;
   private static final float ВГЙ = 30.0F;
   private static final float ВГк = 13.0F;
   private static final float ВГг = 10.0F;
   private static final float ВГ0 = 10.0F;
   private static final float ВГх = 12.0F;
   private static final float ВГУ = 10.0F;
   private static final float ВГЗ = 12.0F;
   private static final float ВГЕ = 10.0F;
   private static final float ВГП = 10.0F;
   private static final float ВГ西 = 10.0F;

   private void Ф(float var1, float var2, CosmeticCategory var3, int var4) {
      List var5 = CosmeticCatalog.getModels(var3);
      CosmeticDefinition var6 = ClientCosmeticSelections.getSelectedCatalogModel(var3);

      for (int var7 = 0; var7 < var5.size(); var7++) {
         int var8 = var4 + var7;
         CosmeticDefinition var9 = (CosmeticDefinition)var5.get(var7);
         float var10 = this.Ф(var1, var8);
         float var11 = this.Г(var8, var2);
         if (this.Л(var11)) {
            boolean var12 = ClientCosmeticSelections.isCatalogModelSelected(var3) && var6 != null && var6.name().equals(var9.name());
            String var13 = var3.getConfigKey() + "/" + var9.name();
            float var14 = this.Ф(this.сШ, var13, var12);
            this.Ф(var10, var11, var12);
            float var15 = (Вщо + var14 * ВщO) * this.А.getGuiAlpha() * this.с8.getValue() * this.А.getLayoutContentAlpha(0);
            this.В_baa6d7e8.queuePreview(var9.preview(), var10 + Вщ7, var11 + ВщР, this.Ьъ(), Вщ3, var15);
            this.Ф(var9.displayName(), var10, var11, var12);
         }
      }
   }

   public boolean O(String var1) {
      if (!this.ХБ) {
         return false;
      }

      String var2 = this.ХС ? "" : this.Я衣;
      int var3 = 64 - var2.codePointCount(0, var2.length());
      StringBuilder var4 = new StringBuilder(var2);
      var1.codePoints().filter(var0 -> !Character.isISOControl(var0)).limit(Math.max(0, var3)).forEach(var4::appendCodePoint);
      this.пФ(var4.toString());
      this.ХС = false;
      return true;
   }

   private void Л(GuiGraphicsExtractor var1, float var2, float var3) {
      this.В_baa6d7e8.clearPreviews();
      EmojiMaskVariant[] var4 = EmojiMaskVariant.values();

      for (int var5 = 0; var5 < var4.length; var5++) {
         EmojiMaskVariant var6 = var4[var5];
         float var7 = this.Ф(var2, var5);
         float var8 = this.Г(var5, var3);
         if (this.Л(var8)) {
            boolean var9 = !ClientCosmeticSelections.isCatalogModelSelected(CosmeticCategory.MASKS) && ClientCosmeticSelections.getSelectedMask() == var6;
            float var10 = this.Ф(this.сР, var6, var9);
            this.Ф(var7, var8, var9);
            EmojiMaskPreview.ъ(var6, var7 + ВщА, var8 + Вщ4, this.Ьъ(), Вщп, ВщД + var10 * ВщЪ);
            this.Ф(var6.getDisplayName(), var7, var8, var9);
         }
      }

      this.Ф(var2, var3, CosmeticCategory.MASKS, var4.length);
      this.В_baa6d7e8.submitPreviews(var1, var2, this.А.getGuiY() + this.Ья(), this.ЬЩ(), this.ЬА());
   }

   private float Ьъ() {
      return this.Ьщ() - Вчг;
   }

   private void method_3н() {
      RoseClient.getInstance().getServices().getConfigManager().getModuleConfig().save();
   }

   private float ЬЩ() {
      return this.А.getGuiWidth() - this.Ь3() - ВчФ;
   }

   private void Ф(float var1, float var2, float var3, String var4, float var5, float var6) {
      var4 = ClientLocalization.translate(var4);
      float var7 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var4, 11);
      UiDrawing.drawText(
         ClientFonts.getMediumFont(), var4, var1 + (var3 - var7) / 2.0F, var2 + ВГТ, 11, ColorMath.lerpArgb(this.п6, this.пр, Math.max(var5, var6 * ВГО))
      );
   }

   private void пФ(String var1) {
      if (!this.Я衣.equals(var1)) {
         this.Я衣 = var1;
         this.Хз = true;
         this.А.resetGuiScroll();
      }
   }

   private float Г(int var1, float var2) {
      int var3 = this.field_8[var1];
      return var3 < 0 ? Float.NaN : this.А.getGuiY() + this.Ьь() + var3 / 3 * ВГм + var2 + this.Дм + this.А.getLayoutOffset(var3);
   }

   private void л(float var1, float var2) {
      String var3 = this.В_ed2fe0c8 == CosmeticsPanel.PanelTab.FURRY
         ? ClientLocalization.translate(this.В_ed2fe0c8.сь) + " · " + ClientLocalization.translate(this.В_1320c117.Хщ)
         : ClientLocalization.translate(this.В_ed2fe0c8.сь);
      this.Ф(this.В_ed2fe0c8, var1 + 1.0F, var2 + 1.0F, ВЭм, this.п3);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var3, var1 + ВЭК, var2 - 2.0F, 12, this.пр);
      String var4 = ClientLocalization.selectLocalizedText(ВЭI, ВЭТ);
      UiDrawing.drawText(ClientFonts.getMediumFont(), this.Ф(var4, this.ЬЩ() - this.Ьв() - ВЭО), var1 + ВЭФ, var2 + ВЭЙ, 10, this.п6);
      String var5 = this.пТ + " / " + this.field_8.length;
      UiDrawing.drawText(ClientFonts.getMediumFont(), var5, var1 + this.ЬЩ() - UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var5, 9), var2 + ВЭк, 9, this.п6);
   }

   private WeaponSkinVariant[] Ф() {
      return WeaponSkinVariant.values();
   }

   public boolean И(float var1, float var2) {
      return this.Ф(var1, var2, this.А.getGuiX() + this.Ь3(), this.А.getGuiY() + this.Ья(), this.ЬЩ(), this.ЬА());
   }

   private float Ф(CosmeticsPanel.PanelTab var1, float var2) {
      return var2 + switch (var1) {
         case HATS -> ВЭр;
         case MASKS -> ВЭ6;
         case WINGS -> ВЭ_;
         case BACKPACKS -> ВЭШ;
         case PETS -> ВЭы;
         case FURRY -> ВЭй;
         case ITEMS -> ВЭЖ;
      };
   }

   private void Ь(GuiGraphicsExtractor var1, float var2, float var3) {
      this.В_72939cdb.clearPreviews();
      this.В_baa6d7e8.clearPreviews();
      WingSelection[] var4 = WingSelection.values();

      for (int var5 = 0; var5 < var4.length; var5++) {
         WingSelection var6 = var4[var5];
         float var7 = this.Ф(var2, var5);
         float var8 = this.Г(var5, var3);
         if (this.Л(var8)) {
            boolean var9 = !ClientCosmeticSelections.isCatalogModelSelected(CosmeticCategory.WINGS) && ClientCosmeticSelections.getSelectedWings() == var6;
            float var10 = this.Ф(this.с7, var6, var9);
            this.Ф(var7, var8, var9);
            this.В_72939cdb
               .queuePreview(var6, var7 + Вщс, var8 + ВщВ, this.Ьъ(), ВщЯ, (ВщХ + var10 * Вще) * this.А.getGuiAlpha() * this.с8.getValue() * this.А.getLayoutContentAlpha(0));
            this.Ф(var6.getDisplayName(), var7, var8, var9);
         }
      }

      this.Ф(var2, var3, CosmeticCategory.WINGS, var4.length);
      this.В_72939cdb.submitPreviews(var1, var2, this.А.getGuiY() + this.Ья(), this.ЬЩ(), this.ЬА());
      this.В_baa6d7e8.submitPreviews(var1, var2, this.А.getGuiY() + this.Ья(), this.ЬЩ(), this.ЬА());
   }

   private float ЬИ() {
      return this.А.getSidebarWidth() - ВчI;
   }

   private void А(float var1, float var2, float var3) {
      float var4 = this.А.getGuiY() + ВщЩ;
      float var5 = this.Ьб();
      float var6 = Вщу;
      CosmeticsPanel.FurryCategory[] var7 = CosmeticsPanel.FurryCategory.values();

      for (int var8 = 0; var8 < var7.length; var8++) {
         CosmeticsPanel.FurryCategory var9 = var7[var8];
         float var10 = var1 + var8 * (var5 + var6);
         boolean var11 = this.В_1320c117 == var9;
         boolean var12 = this.Ф(var2, var3, var10, var4, var5, Вщ2);
         FloatAnimation var13 = this.сI.computeIfAbsent(var9, var1x -> new FloatAnimation(var11 ? 1.0F : 0.0F, ВГх, Easings.CUBIC_OUT));
         FloatAnimation var14 = this.сТ.computeIfAbsent(var9, var0 -> new FloatAnimation(0.0F, ВГ0, Easings.CUBIC_OUT));
         var13.updateTarget(var11 ? 1.0F : 0.0F);
         var14.updateTarget(var12 ? 1.0F : 0.0F);
         float var15 = ClientCosmeticSelections.isCategoryEnabled(var9.В) ? 1.0F : 0.0F;
         this.Ь(var10, var4, var5, Вщф, Вщи, Math.max(var13.getValue(), var15 * Вщ9), var14.getValue());
         this.Ф(var10, var4, var5, var9.Хщ, var13.getValue(), var14.getValue());
      }
   }

   private void Ь(float var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = Math.clamp(var6, 0.0F, 1.0F);
      float var9 = Math.clamp(var7, 0.0F, 1.0F);
      UiDrawing.drawRoundedRect(var1, var2, var3, var4, var5, ColorMath.lerpArgb(this.п7, this.пы, Math.max(var8, var9 * В必Ш)));
      UiDrawing.drawOutline(var1, var2, var3, var4, 1.0F, var5, this.пР);
   }

   private float Ьб() {
      return (this.ЬЩ() - Вч0) / Вчх;
   }

   private void method_3ч() {
      CosmeticCategory var1 = switch (this.В_ed2fe0c8) {
         case HATS -> CosmeticCategory.HATS;
         case MASKS -> CosmeticCategory.MASKS;
         case WINGS -> CosmeticCategory.WINGS;
         case BACKPACKS -> CosmeticCategory.BACKPACKS;
         case PETS -> CosmeticCategory.PETS;
         case FURRY -> this.В_1320c117.В;
         case ITEMS -> null;
      };
      List var2 = var1 == null ? List.of() : CosmeticCatalog.getModels(var1);
      if (var2 != this.сК_2571477d) {
         this.сК_2571477d = var2;
         this.Хз = true;
      }

      if (this.В_ed2fe0c8 == CosmeticsPanel.PanelTab.HATS) {
         List var3 = HatCatalog.getHats();
         if (!var3.equals(this.см_2571477d)) {
            this.см_2571477d = var3;
            this.Хз = true;
         }
      }

      if (this.Хз || this.Хц != ClientLocalization.isEnglishEnabled()) {
         ArrayList var8 = new ArrayList();
         switch (this.В_ed2fe0c8) {
            case HATS:
               for (HatDefinition var18 : this.см_2571477d) {
                  var8.add(var18.displayName());
               }
               break;
            case MASKS:
               for (EmojiMaskVariant var27 : EmojiMaskVariant.values()) {
                  var8.add(var27.getDisplayName());
               }
               break;
            case WINGS:
               for (WingSelection var26 : WingSelection.values()) {
                  var8.add(var26.getDisplayName());
               }
               break;
            case BACKPACKS:
               for (BackpackColorVariant var25 : BackpackColorVariant.values()) {
                  var8.add(var25.getDisplayName());
               }
            case PETS:
            case FURRY:
            default:
               break;
            case ITEMS:
               for (WeaponSkinVariant var7 : this.Ф()) {
                  var8.add(var7.getDisplayName());
               }
         }

         for (CosmeticDefinition var19 : this.сК_2571477d) {
            var8.add(var19.displayName());
         }

         this.field_8 = new int[var8.size()];
         Arrays.fill(this.field_8, -1);
         this.пТ = 0;
         String var14 = this.Я衣.strip().toLowerCase(Locale.ROOT);

         for (int var20 = 0; var20 < var8.size(); var20++) {
            String var24 = (String)var8.get(var20);
            if (var24.toLowerCase(Locale.ROOT).contains(var14) || ClientLocalization.translate(var24).toLowerCase(Locale.ROOT).contains(var14)) {
               this.field_8[var20] = this.пТ++;
            }
         }

         this.Хз = false;
         this.Хц = ClientLocalization.isEnglishEnabled();
      }
   }

   public static float ЬО() {
      if (!Хд) {
         return Вч西;
      }

      Хд = false;
      return До;
   }

   private void method_38() {
       this.пO = this.А.getPanelBackgroundColor();
       this.п7 = this.А.usesCustomBackground() ? this.А.getPanelCardColor() : ColorMath.withAlpha(this.пO, ВЭА);
      this.пР = ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE);
      this.п3 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      this.пр = ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT);
      this.п6 = ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT);
      this.п_ = ThemeEditor.getColorArgb(ThemeColorSlot.SHADOW);
      this.пШ = ThemeEditor.getColorArgb(ThemeColorSlot.LINE);
      this.пы = ThemeEditor.getColorArgb(ThemeColorSlot.SELECTED);
      this.пй = ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_STATE);
      this.пЖ = ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_STATE);
      this.пм = ThemeEditor.getColorArgb(ThemeColorSlot.CATEGORY_TEXT);
      this.пК = ThemeEditor.getColorArgb(ThemeColorSlot.DESELECT_CATEGORY_TEXT);
      this.пI = ThemeEditor.getColorArgb(ThemeColorSlot.SCROLL);
   }

   private boolean Ф(float var1, float var2, float var3, float var4, CosmeticCategory var5, int var6) {
      List var7 = CosmeticCatalog.getModels(var5);

      for (int var8 = 0; var8 < var7.size(); var8++) {
         int var9 = var6 + var8;
         if (this.Ф(var1, var2, this.Ф(var3, var9), this.Г(var9, var4), this.Ьщ(), ВГЬ)) {
            ClientCosmeticSelections.selectCatalogModel((CosmeticDefinition)var7.get(var8));
            if (var5.isFurryCategory()) {
               ClientCosmeticSelections.setCategoryEnabled(var5, true);
            }

            this.method_3н();
            return true;
         }
      }

      return false;
   }

   private void method_3з() {
      float var1 = this.А.getGuiX();
      float var2 = this.А.getGuiY();
      float var3 = this.А.getGuiWidth();
      float var4 = this.А.getGuiHeight();
      UiDrawing.drawShadow(var1, var2, var3, var4, ВЭ4, this.п_);
      if (ClickGuiPreferences.х弟() > 0) {
         UiDrawing.drawBlur(var1, var2, var3, var4, ВЭп, ВЭД, -1);
      }

      UiDrawing.drawRoundedRect(var1, var2, var3, var4, ВЭЪ, this.пO);
      UiDrawing.drawOutline(var1, var2, var3, var4, 1.0F, ВЭт, this.пР);
      UiDrawing.drawRoundedRect(var1 + this.А.getSidebarWidth(), var2 + ВЭа, var3 - this.А.getSidebarWidth(), 1.0F, 0.0F, this.пШ);
      UiDrawing.drawRoundedRect(var1 + ВЭЮ, var2 + ВЭЧ, this.А.getSidebarWidth() - ВЭЛ, 1.0F, 0.0F, this.пР);
      UiDrawing.drawRoundedRect(var1 + this.А.getSidebarWidth(), var2, 1.0F, var4, 0.0F, this.пШ);
      UiDrawing.drawRoundedRect(var1, var2 + var4 - ВЭ诶, this.А.getSidebarWidth(), 1.0F, 0.0F, this.пШ);
   }

   private void И(GuiGraphicsExtractor var1, float var2, float var3) {
      this.В_82353cf7.clearPreviews();
      this.В_baa6d7e8.clearPreviews();
      BackpackColorVariant[] var4 = BackpackColorVariant.values();

      for (int var5 = 0; var5 < var4.length; var5++) {
         BackpackColorVariant var6 = var4[var5];
         float var7 = this.Ф(var2, var5);
         float var8 = this.Г(var5, var3);
         if (this.Л(var8)) {
            boolean var9 = !ClientCosmeticSelections.isCatalogModelSelected(CosmeticCategory.BACKPACKS) && ClientCosmeticSelections.getSelectedBackpackColor() == var6;
            float var10 = this.Ф(this.с_, var6, var9);
            this.Ф(var7, var8, var9);
            float var11 = (Вщщ + var10 * ВщГ) * this.А.getGuiAlpha() * this.с8.getValue() * this.А.getLayoutContentAlpha(0);
            this.В_82353cf7.queuePreview(var6, var7 + Вщл, var8 + Вщэ, this.Ьъ(), Вщю, var11);
            this.Ф(var6.getDisplayName(), var7, var8, var9);
         }
      }

      this.Ф(var2, var3, CosmeticCategory.BACKPACKS, var4.length);
      this.В_82353cf7.submitPreviews(var1, var2, this.А.getGuiY() + this.Ья(), this.ЬЩ(), this.ЬА());
      this.В_baa6d7e8.submitPreviews(var1, var2, this.А.getGuiY() + this.Ья(), this.ЬЩ(), this.ЬА());
   }

   private void Ч(float var1, float var2) {
      float var3 = this.Ь2() - (this.Ья() - ВГ7);
      float var4 = this.ЬА();
      if (!(var3 <= var4)) {
         float var5 = Math.max(ВГР, var4 * var4 / var3);
         float var6 = Math.clamp(-var2 / (var3 - var4), 0.0F, 1.0F);
         float var7 = this.А.getGuiY() + this.Ья();
         UiDrawing.drawRoundedRect(var1 + this.ЬЩ() + ВГ3, var7, 2.0F, var4, 1.0F, this.пР);
         UiDrawing.drawRoundedRect(var1 + this.ЬЩ() + ВГр, var7 + (var4 - var5) * var6, 2.0F, var5, 1.0F, this.пI);
      }
   }

   private void Г(GuiGraphicsExtractor var1, float var2, float var3) {
      this.В_baa6d7e8.clearPreviews();
      this.Ф(var2, var3, CosmeticCategory.PETS, 0);
      this.В_baa6d7e8.submitPreviews(var1, var2, this.А.getGuiY() + this.Ья(), this.ЬЩ(), this.ЬА());
   }

   public boolean щ(float var1, float var2, int var3) {
      if (var3 != 0) {
         return false;
      }

      this.method_3ч();
      float var4 = this.А.getGuiX() + Вщг;
      float var5 = this.А.getGuiY();
      float var6 = this.ЬД();
      if (this.Ф(var1, var2, this.Ью(), var6, this.Ьв(), Вщ0)) {
         this.ХБ = true;
         this.ХС = false;
         if (!this.Я衣.isEmpty() && this.Ф(var1, var2, this.Ью() + this.Ьв() - Вщх, var6, ВщУ, ВщЗ)) {
            this.пФ("");
         }

         return true;
      } else {
         this.method_36();
         CosmeticsPanel.PanelTab[] var7 = CosmeticsPanel.PanelTab.values();

         for (CosmeticsPanel.PanelTab var11 : var7) {
            float var12 = this.Ф(var11, var5);
            if (this.Ф(var1, var2, this.А.getGuiX() + ВщЕ, var12, this.ЬГ(), ВщП)) {
               if (this.В_ed2fe0c8 != var11) {
                  this.В_ed2fe0c8 = var11;
                  this.Хз = true;
                  this.А.resetGuiScroll();
               }

               return true;
            }
         }

         float var17 = this.А.getGuiX() + this.Ь3();
         float var18 = this.А.getGuiScroll();
         float var19 = this.А.getGuiY() + this.А.getGuiHeight() - Вщ西;
         if (this.Ф(var1, var2, var4, var19, this.ЬИ(), Вщд)) {
            switch (this.В_ed2fe0c8) {
               case HATS:
                  ClientCosmeticSelections.setHatsEnabled(!ClientCosmeticSelections.areHatsEnabled());
                  break;
               case MASKS:
                  ClientCosmeticSelections.setMasksEnabled(!ClientCosmeticSelections.areMasksEnabled());
                  break;
               case WINGS:
                  ClientCosmeticSelections.toggleWings();
                  break;
               case BACKPACKS:
                  ClientCosmeticSelections.setBackpacksEnabled(!ClientCosmeticSelections.areBackpacksEnabled());
                  break;
               case PETS:
                  ClientCosmeticSelections.setPetsEnabled(!ClientCosmeticSelections.arePetsEnabled());
                  break;
               case FURRY:
                  ClientCosmeticSelections.setCategoryEnabled(this.В_1320c117.В, !ClientCosmeticSelections.isCategoryEnabled(this.В_1320c117.В));
                  break;
               case ITEMS:
                  ClientCosmeticSelections.setWeaponSkinsEnabled(!ClientCosmeticSelections.areWeaponSkinsEnabled());
            }

            this.method_3н();
            return true;
         } else {
            if (this.В_ed2fe0c8 != CosmeticsPanel.PanelTab.ITEMS) {
               float var20 = this.А.getGuiX() + this.А.getGuiWidth() + ВщБ;
               float var28 = this.А.getGuiY() + this.А.getGuiHeight() - ВщС;
               if (this.Ф(var1, var2, var20, var28, Вщз, Вщц)) {
                  ClientCosmeticSelections.setShownOnSelf(!ClientCosmeticSelections.isShownOnSelf());
                  this.method_3н();
                  return true;
               }

               if (this.Ф(var1, var2, var20, var28 + Вщя, ВГс, ВГВ)) {
                  ClientCosmeticSelections.setShownOnFriends(!ClientCosmeticSelections.isShownOnFriends());
                  this.method_3н();
                  return true;
               }

               if (this.Ф(var1, var2, var20, var28 + ВГЯ, ВГХ, ВГе)) {
                  ClientCosmeticSelections.setShownOnOtherPlayers(!ClientCosmeticSelections.isShownOnOtherPlayers());
                  this.method_3н();
                  return true;
               }
            }

            if (this.В_ed2fe0c8 == CosmeticsPanel.PanelTab.ITEMS) {
               float var21 = this.А.getGuiY() + ВГА;
               float var29 = ВГ4;
               float var13 = ВГп;

               for (int var14 = 0; var14 < CosmeticsPanel.WeaponCategory.values().length; var14++) {
                  if (this.Ф(var1, var2, var17 + var14 * (var29 + var13), var21, var29, ВГД)) {
                     CosmeticsPanel.WeaponCategory var15 = CosmeticsPanel.WeaponCategory.values()[var14];
                     if (this.В_c7082522 != var15) {
                        this.В_c7082522 = var15;
                        this.А.resetGuiScroll();
                     }

                     return true;
                  }
               }
            } else if (this.В_ed2fe0c8 == CosmeticsPanel.PanelTab.FURRY) {
               float var22 = this.А.getGuiY() + ВГЪ;
               float var30 = this.Ьб();
               float var36 = ВГт;
               CosmeticsPanel.FurryCategory[] var37 = CosmeticsPanel.FurryCategory.values();

               for (int var38 = 0; var38 < var37.length; var38++) {
                  if (this.Ф(var1, var2, var17 + var38 * (var30 + var36), var22, var30, ВГа)) {
                     CosmeticsPanel.FurryCategory var16 = var37[var38];
                     if (this.В_1320c117 != var16) {
                        this.В_1320c117 = var16;
                        this.Хз = true;
                        this.А.resetGuiScroll();
                     }

                     return true;
                  }
               }
            }

            if (!this.И(var1, var2)) {
               return false;
            }

            switch (this.В_ed2fe0c8) {
               case HATS:
                  List var27 = this.см_2571477d;

                  for (int var35 = 0; var35 < var27.size(); var35++) {
                     if (this.Ф(var1, var2, this.Ф(var17, var35), this.Г(var35, var18), this.Ьщ(), ВГЛ)) {
                        ClientCosmeticSelections.selectHat(((HatDefinition)var27.get(var35)).key());
                        this.method_3н();
                        return true;
                     }
                  }

                  if (this.Ф(var1, var2, var17, var18, CosmeticCategory.HATS, var27.size())) {
                     return true;
                  }
                  break;
               case MASKS:
                  EmojiMaskVariant[] var26 = EmojiMaskVariant.values();

                  for (int var34 = 0; var34 < var26.length; var34++) {
                     if (this.Ф(var1, var2, this.Ф(var17, var34), this.Г(var34, var18), this.Ьщ(), ВГЧ)) {
                        ClientCosmeticSelections.selectMask(var26[var34]);
                        this.method_3н();
                        return true;
                     }
                  }

                  if (this.Ф(var1, var2, var17, var18, CosmeticCategory.MASKS, var26.length)) {
                     return true;
                  }
                  break;
               case WINGS:
                  WingSelection[] var25 = WingSelection.values();

                  for (int var33 = 0; var33 < var25.length; var33++) {
                     if (this.Ф(var1, var2, this.Ф(var17, var33), this.Г(var33, var18), this.Ьщ(), ВГЮ)) {
                        ClientCosmeticSelections.selectWings(var25[var33]);
                        this.method_3н();
                        return true;
                     }
                  }

                  if (this.Ф(var1, var2, var17, var18, CosmeticCategory.WINGS, var25.length)) {
                     return true;
                  }
                  break;
               case BACKPACKS:
                  BackpackColorVariant[] var24 = BackpackColorVariant.values();

                  for (int var32 = 0; var32 < var24.length; var32++) {
                     if (this.Ф(var1, var2, this.Ф(var17, var32), this.Г(var32, var18), this.Ьщ(), ВГ1)) {
                        ClientCosmeticSelections.selectBackpackColor(var24[var32]);
                        this.method_3н();
                        return true;
                     }
                  }

                  if (this.Ф(var1, var2, var17, var18, CosmeticCategory.BACKPACKS, var24.length)) {
                     return true;
                  }
                  break;
               case PETS:
                  if (this.Ф(var1, var2, var17, var18, CosmeticCategory.PETS, 0)) {
                     return true;
                  }
                  break;
               case FURRY:
                  if (this.Ф(var1, var2, var17, var18, this.В_1320c117.В, 0)) {
                     return true;
                  }
                  break;
               case ITEMS:
                  WeaponSkinVariant[] var23 = this.Ф();

                  for (int var31 = 0; var31 < var23.length; var31++) {
                     if (this.Ф(var1, var2, this.Ф(var17, var31), this.Г(var31, var18), this.Ьщ(), ВГ诶)) {
                        if (this.В_c7082522 == CosmeticsPanel.WeaponCategory.MACE) {
                           ClientCosmeticSelections.selectMaceSkin(var23[var31]);
                        } else {
                           ClientCosmeticSelections.selectSwordSkin(var23[var31]);
                        }

                        this.method_3н();
                        return true;
                     }
                  }
            }

            return false;
         }
      }
   }

   private float Ф(float var1, int var2) {
      int var3 = this.field_8[var2];
      return var3 < 0 ? Float.NaN : var1 + var3 % 3 * (this.Ьщ() + ВГЖ);
   }

   private void Ф(String var1, float var2, float var3, boolean var4) {
      var1 = ClientLocalization.translate(var1);
      String var5 = var1;

      while (var5.length() > 1 && UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var5 + "…", 12) > this.Ьщ() - ВГФ) {
         var5 = var5.substring(0, var5.length() - 1);
      }

      if (!var5.equals(var1)) {
         var5 = var5 + "…";
      }

      UiDrawing.drawText(ClientFonts.getMediumFont(), var5, var2 + ВГЙ, var3 + ВГк, 12, var4 ? this.пр : this.п6);
   }

   public void method_36() {
      this.ХБ = false;
      this.ХС = false;
   }

   private void Ф(CosmeticsPanel.PanelTab var1, float var2, float var3, float var4, int var5) {
      if (var1 == CosmeticsPanel.PanelTab.ITEMS) {
         this.Ф(В必Б, var2, var3, В必С, В必з, Math.round(var4), var5);
      } else {
         UiDrawing.drawTexture(var2, var3, В必ц, В必я, 0.0F, var5, ClientResourceIds.getImageId("gui/" + this.Ф_388008bb(var1)));
      }
   }

   public float Ь2() {
      this.method_3ч();
      int var1 = (this.пТ + 3 - 1) / 3;
      return this.Ьь() - ВГ衣 + var1 * ВГш - (var1 == 0 ? 0.0F : ВГИ) + ВГь;
   }

   private String Ф_388008bb(CosmeticsPanel.PanelTab var1) {
      return switch (var1) {
         case HATS -> Вщъ;
         case MASKS -> ВщМ;
         case WINGS -> ВщН;
         case BACKPACKS -> Вщн;
         case PETS -> ВщЦ;
         case FURRY -> Вщч;
         case ITEMS -> null;
      };
   }

   private void Ф(GuiGraphicsExtractor var1, float var2, float var3) {
      float var4 = this.А.getGuiX() + this.А.getGuiWidth() + ВЭг;
      float var5 = this.А.getGuiY();
      float var6 = ВЭ0;
      float var7 = this.А.getGuiHeight();
      UiDrawing.drawShadow(var4, var5, var6, var7, ВЭх, this.п_);
      if (ClickGuiPreferences.х弟() > 0) {
         UiDrawing.drawBlur(var4, var5, var6, var7, ВЭУ, ВЭЗ, -1);
      }

      UiDrawing.drawRoundedRect(var4, var5, var6, var7, ВЭЕ, this.пO);
      UiDrawing.drawOutline(var4, var5, var6, var7, 1.0F, ВЭП, this.пР);
      UiDrawing.drawRoundedRect(var4, var5 + ВЭ西, var6, 1.0F, 0.0F, this.пШ);
      sg.render.ClientLogo.drawInBox(var4 + ВЭБ, var5 + ВЭС, ВЭз, ВЭц, this.п3);
      UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.selectLocalizedText(ВЭя, В必с), var4 + В必В, var5 + В必Я, 12, this.пр);
      if (Minecraft.getInstance().player != null) {
         float var8 = В必Х;
         float var9 = var4 + В必е - var8;
         float var10 = var5 + В必А - var8;
         float var11 = var4 + var6 - В必4 + var8;
         float var12 = var5 + var7 - В必п + var8;
         GuiCoordinateTransform var13 = GuiCoordinateTransform.of(var9, var10, var11 - var9, var12 - var10);
         До = Math.clamp(UiDrawing.getGlobalAlpha() / В必Д, 0.0F, 1.0F);
         Хд = true;
         InventoryScreen.extractEntityInInventoryFollowsMouse(
            var1,
            var13.x1(),
            var13.y1(),
            var13.x2(),
            var13.y2(),
            Math.max(1, Math.round((В必Ъ + (var7 - В必т) * В必а) * var13.scaleY())),
            1.0F,
            var13.screenX(var2),
            var13.screenY(var3),
            Minecraft.getInstance().player
         );
      }

      if (this.В_ed2fe0c8 != CosmeticsPanel.PanelTab.ITEMS) {
         float var14 = var4 + В必Ю;
         float var15 = var6 - В必Ч;
         float var16 = var5 + var7 - В必Л;
         UiDrawing.drawRoundedRect(var4 + В必诶, var16 - В必1, var6 - В必Ь, 1.0F, 0.0F, this.пШ);
         UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.selectLocalizedText(В必衣, В必ш), var4 + В必И, var16 - В必ь, 10, this.п6);
         this.Ф(var14, var16, var15, ClientLocalization.translate(В必ж), ClientCosmeticSelections.isShownOnSelf(), this.сб, this.Ф(var2, var3, var14, var16, var15, В必弟) ? 1.0F : 0.0F);
         this.Ф(var14, var16 + В必б, var15, ClientLocalization.translate(В必Ы), ClientCosmeticSelections.isShownOnFriends(), this.сЫ, this.Ф(var2, var3, var14, var16 + В必l, var15, В必8) ? 1.0F : 0.0F);
         this.Ф(var14, var16 + В必5, var15, ClientLocalization.translate(В必в), ClientCosmeticSelections.isShownOnOtherPlayers(), this.сl, this.Ф(var2, var3, var14, var16 + В必ъ, var15, В必М) ? 1.0F : 0.0F);
      }
   }

   private float ЬА() {
      return ВГй + this.ЬЮ() - this.Ья();
   }

   private void Ф(float var1, float var2, boolean var3) {
      UiDrawing.drawRoundedRect(var1, var2, this.Ьщ(), Вщр, Вщ6, this.п7);
      if (var3) {
         UiDrawing.drawRoundedRect(var1 + 1.0F, var2 + 1.0F, this.Ьщ() - 2.0F, Вщ_, ВщШ, ColorMath.withAlpha(this.пы, ColorMath.getAlpha(this.пы) / Вщы * Вщй));
      }

      UiDrawing.drawOutline(var1, var2, this.Ьщ(), ВщЖ, 1.0F, Вщм, this.пР);
      UiDrawing.drawRoundedRect(var1 + 1.0F, var2 + ВщК, this.Ьщ() - 2.0F, 1.0F, 0.0F, this.пШ);
      this.Ф(this.В_ed2fe0c8, var1 + ВщI, var2 + ВщТ, ВщО, var3 ? this.п3 : this.п6);
      this.method_2(var1 + this.Ьщ() - ВщФ, var2 + ВщЙ, var3 ? 1.0F : 0.0F);
   }

   public void 衣(GuiGraphicsExtractor var1, float var2, float var3, float var4) {
      this.method_38();
      this.method_3ч();
      long var5 = System.currentTimeMillis();
      if (var5 - this.ХН > Вчд) {
         this.с8.setImmediately(0.0F);
      }

      this.ХН = var5;
      this.с8.updateTarget(1.0F);
      float var7 = this.с8.getValue();
      this.Дм = ВчБ * (1.0F - var7);
      float var8 = UiDrawing.getGlobalAlpha();
      UiDrawing.setGlobalAlpha(var8 * var7 * this.А.getLayoutContentAlpha(0));
      float var9 = this.А.getGuiX() + this.Ь3();
      this.method_3з();
      this.и(var2, var3);
      this.л(var9, this.А.getGuiY() + ВчС);
      this.щ(this.Ью(), this.ЬД(), var2, var3);
      this.Ф(var1, var2, var3);
      if (this.В_ed2fe0c8 == CosmeticsPanel.PanelTab.FURRY) {
         this.А(var9, var2, var3);
      }

      if (this.В_ed2fe0c8 == CosmeticsPanel.PanelTab.ITEMS) {
         this.ь(var9, var2, var3);
      }

      this.衣(var1, var9, this.А.getGuiY() + this.Ья(), this.ЬЩ(), this.ЬА());
      switch (this.В_ed2fe0c8) {
         case HATS:
            this.衣(var1, var9, var4);
            break;
         case MASKS:
            this.Л(var1, var9, var4);
            break;
         case WINGS:
            this.Ь(var1, var9, var4);
            break;
         case BACKPACKS:
            this.И(var1, var9, var4);
            break;
         case PETS:
            this.Г(var1, var9, var4);
            break;
         case FURRY:
            this.method_3(var1, var9, var4);
            break;
         case ITEMS:
            this.З(var1, var9, var4);
      }

      this.Ф(var1);
      if (this.пТ == 0) {
         this.З(ClientLocalization.selectLocalizedText(Вчз, Вчц), var9 + this.ЬЩ() * Вчя, this.А.getGuiY() + this.Ьь() + ВЭс, 14, this.пр);
         this.З(ClientLocalization.selectLocalizedText(ВЭВ, ВЭЯ), var9 + this.ЬЩ() * ВЭХ, this.А.getGuiY() + this.Ьь() + ВЭе, 11, this.п6);
      }

      this.Ч(var9, var4);
      UiDrawing.setGlobalAlpha(var8);
   }

   public float ЬЮ() {
      return this.А.getGuiHeight() - ВГж;
   }

   private void и(float var1, float var2) {
      float var3 = this.А.getGuiX();
      float var4 = this.А.getGuiY();
      float logoSize = 26.0F;
      sg.render.ClientLogo.drawCentered(
         var3 + this.А.getSidebarWidth() * ВЭЬ + 2.0F,
         var4 + ВЭИ + logoSize * 0.5F,
         logoSize,
         this.п3
      );
      float var5 = this.А.getSidebarLabelAlpha();
      float var6 = UiDrawing.getGlobalAlpha();
      if (var5 > ВЭь) {
         UiDrawing.pushScissor(var3 + ВЭж, var4 + ВЭ弟, Math.max(0.0F, this.А.getSidebarWidth() - ВЭб), this.А.getGuiHeight() - ВЭЫ);
         UiDrawing.setGlobalAlpha(var6 * var5);
         int var7 = ThemeEditor.getColorArgb(ThemeColorSlot.CATEGORY_SURTEST);
         UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.selectLocalizedText(ВЭl, ВЭ8), var3 + ВЭ5, var4 + ВЭв, 11, var7);
         UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.selectLocalizedText(ВЭъ, ВЭМ), var3 + ВЭН, var4 + ВЭн, 11, var7);
         UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.selectLocalizedText(ВЭЦ, ВЭч), var3 + ВЭЭ, var4 + ВЭ必, 11, var7);
         UiDrawing.setGlobalAlpha(var6);
         UiDrawing.popScissor();
      }

      CosmeticsPanel.PanelTab[] var15 = CosmeticsPanel.PanelTab.values();

      for (CosmeticsPanel.PanelTab var11 : var15) {
         float var12 = this.Ф(var11, var4);
         FloatAnimation var13 = this.сы.computeIfAbsent(var11, var1x -> new FloatAnimation(var1x == this.В_ed2fe0c8 ? 1.0F : 0.0F, ВГ西));
         var13.updateTarget(var11 == this.В_ed2fe0c8 ? 1.0F : 0.0F);
         float var14 = var13.getValue();
         if (var14 > ВЭщ) {
            UiDrawing.drawRoundedRect(var3 + ВЭГ, var12, this.ЬГ(), ВЭл, ВЭэ, ColorMath.withAlpha(this.пы, ColorMath.getAlpha(this.пы) / ВЭю * var14));
         }

         this.Ф(var11, var3 + ВЭЩ, var12 + ВЭу, ВЭ2, ColorMath.lerpArgb(this.пК, this.п3, var14));
         if (var5 > ВЭф) {
            UiDrawing.pushScissor(var3 + ВЭи, var12, Math.max(0.0F, this.А.getSidebarWidth() - ВЭ9), ВЭо);
            UiDrawing.setGlobalAlpha(var6 * var5);
            UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(var11.сь), var3 + ВЭO, var12 + ВЭ7, 13, ColorMath.lerpArgb(this.пК, this.пм, var14));
            UiDrawing.setGlobalAlpha(var6);
            UiDrawing.popScissor();
         }
      }

      this.Щ(var3 + ВЭР, var4 + this.А.getGuiHeight() - ВЭ3, var1, var2);
   }

   private void 衣(GuiGraphicsExtractor var1, float var2, float var3, float var4, float var5) {
      UiDrawing.setScissor(var2, var3, var4, var5);
      GuiCoordinateTransform var6 = GuiCoordinateTransform.of(var2, var3, var4, var5);
      var1.enableScissor(var6.x1(), var6.y1(), var6.x2(), var6.y2());
   }

   public CosmeticsPanel(ClickGuiScreen var1) {
      this(ClickGuiPanelHost.of(var1));
   }

   public CosmeticsPanel(ClickGuiPanelHost var1) {
      this.сб = new FloatAnimation(1.0F, Вчй, Easings.CUBIC_OUT);
      this.сЫ = new FloatAnimation(1.0F, ВчЖ, Easings.CUBIC_OUT);
      this.сl = new FloatAnimation(1.0F, Вчм, Easings.CUBIC_OUT);
      this.с8 = new FloatAnimation(0.0F, ВчК, Easings.CUBIC_OUT);
      this.В_ed2fe0c8 = CosmeticsPanel.PanelTab.WINGS;
      this.В_c7082522 = CosmeticsPanel.WeaponCategory.SWORDS;
      this.В_1320c117 = CosmeticsPanel.FurryCategory.EARS;
      this.Я衣 = "";
      this.Хз = true;
      this.field_8 = new int[0];
      this.см_2571477d = List.of();
      this.сК_2571477d = List.of();
      this.А = var1;
   }

   private boolean Ф(float var1, float var2, float var3, float var4, float var5, float var6) {
      return var1 >= var3 && var1 <= var3 + var5 && var2 >= var4 && var2 <= var4 + var6;
   }

   private void Ф(String var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      float var8 = UiDrawing.getTextWidth(ClientFonts.getIconsFont(), var1, var6);
      float var9 = UiDrawing.alignTextCenterY(ClientFonts.getIconsFont(), var1, var3 + var5 * ВщЭ, var6);
      UiDrawing.drawText(ClientFonts.getIconsFont(), var1, var2 + (var4 - var8) * Вщ必, var9, var6, var7);
   }

   private String Ф(String var1, float var2) {
      if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, 10) <= var2) {
         return var1;
      }

      int var3 = var1.length();

      while (var3 > 0 && UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1.substring(0, var3) + "…", 10) > var2) {
         var3 = var1.offsetByCodePoints(var3, -1);
      }

      return var1.substring(0, var3) + "…";
   }

   private float Ьь() {
      return this.В_ed2fe0c8 != CosmeticsPanel.PanelTab.ITEMS && this.В_ed2fe0c8 != CosmeticsPanel.PanelTab.FURRY ? ВГ_ : ВГ6;
   }

   private void Ф(float var1, float var2, float var3, String var4, boolean var5, FloatAnimation var6, float var7) {
      var6.updateTarget(var5 ? 1.0F : 0.0F);
      float var8 = Math.clamp(var6.getValue(), 0.0F, 1.0F);
      this.Ь(var1, var2, var3, В必O, В必7, 0.0F, var7);
      this.method_2(var1 + В必Р, var2 + В必3, var8);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var4, var1 + В必р, var2 + В必6, 11, ColorMath.lerpArgb(this.п6, this.пр, Math.max(var8, var7 * В必_)));
   }

   private float ЬД() {
      return this.А.getGuiY() + ВчП;
   }

   private boolean Л(float var1) {
      return var1 + ВГК >= this.А.getGuiY() + this.Ья() && var1 <= this.А.getGuiY() + ВГI + this.ЬЮ();
   }

   private <T> float Ф(Map<T, FloatAnimation> var1, T var2, boolean var3) {
      FloatAnimation var4 = var1.computeIfAbsent(var2, var1x -> new FloatAnimation(var3 ? 1.0F : 0.0F, ВГг, Easings.CUBIC_OUT));
      var4.updateTarget(var3 ? 1.0F : 0.0F);
      return var4.getValue();
   }

   private float Ью() {
      return this.А.getGuiX() + this.А.getGuiWidth() - ВчЕ - this.Ьв();
   }

   private void З(String var1, float var2, float var3, int var4, int var5) {
      float var6 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, var4);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var1, var2 - var6 / 2.0F, var3, var4, var5);
   }

   private float ЬГ() {
      return this.А.getSidebarWidth() - ВчТ;
   }

   private void Щ(float var1, float var2, float var3, float var4) {
      boolean var5 = switch (this.В_ed2fe0c8) {
         case HATS -> ClientCosmeticSelections.areHatsEnabled();
         case MASKS -> ClientCosmeticSelections.areMasksEnabled();
         case WINGS -> ClientCosmeticSelections.areWingsEnabled();
         case BACKPACKS -> ClientCosmeticSelections.areBackpacksEnabled();
         case PETS -> ClientCosmeticSelections.arePetsEnabled();
         case FURRY -> ClientCosmeticSelections.isCategoryEnabled(this.В_1320c117.В);
         case ITEMS -> ClientCosmeticSelections.areWeaponSkinsEnabled();
      };
      String var6 = var5 ? ClientLocalization.selectLocalizedText(В必Н, В必н) : ClientLocalization.selectLocalizedText(В必Ц, В必ч);
      FloatAnimation var7 = this.сй.computeIfAbsent(this.В_ed2fe0c8, var1x -> new FloatAnimation(var5 ? 1.0F : 0.0F, ВГП, Easings.CUBIC_OUT));
      FloatAnimation var8 = this.сЖ.computeIfAbsent(this.В_ed2fe0c8, var0 -> new FloatAnimation(0.0F, ВГЕ, Easings.CUBIC_OUT));
      var7.updateTarget(var5 ? 1.0F : 0.0F);
      var8.updateTarget(this.Ф(var3, var4, var1, var2, this.ЬИ(), В必Э) ? 1.0F : 0.0F);
      float var9 = Math.clamp(var7.getValue(), 0.0F, 1.0F);
      float var10 = var8.getValue();
      this.Ь(var1, var2, this.ЬИ(), В必必, В必щ, 0.0F, var10);
      float var11 = this.А.getSidebarLabelAlpha();
      float var12 = UiDrawing.getGlobalAlpha();
      if (var11 > В必Г) {
         UiDrawing.pushScissor(var1 + В必л, var2, Math.max(0.0F, this.ЬИ() - В必э), В必ю);
         UiDrawing.setGlobalAlpha(var12 * var11);
         UiDrawing.drawText(ClientFonts.getMediumFont(), var6, var1 + В必Щ, var2 + В必у, 11, ColorMath.lerpArgb(this.п6, this.пр, Math.max(var9, var10 * В必2)));
         UiDrawing.setGlobalAlpha(var12);
         UiDrawing.popScissor();
      }

      float var13 = var1 + (this.ЬИ() - В必ф) * В必и;
      this.О(var13 + (var1 + this.ЬИ() - В必9 - var13) * var11, var2 + В必о, var9);
   }

   private void щ(float var1, float var2, float var3, float var4) {
      this.Ь(var1, var2, this.Ьв(), ВГ弟, ВГб, this.ХБ ? ВГЫ : 0.0F, this.Ф(var3, var4, var1, var2, this.Ьв(), ВГl) ? 1.0F : 0.0F);
      this.Ф(ВГ8, var1 + ВГ5, var2 + ВГв, ВГъ, ВГМ, 11, this.п3);
      float var5 = var1 + ВГН;
      float var6 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), this.Я衣, 11);
      float var7 = this.Ьв() - ВГн;
      float var8 = this.ХБ ? Math.max(0.0F, var6 - var7 + 2.0F) : 0.0F;
      UiDrawing.setScissor(var5, var2 + ВГЦ, var7, ВГч);
      if (this.ХС && !this.Я衣.isEmpty()) {
         UiDrawing.drawRoundedRect(var5 - var8, var2 + ВГЭ, var6, ВГ必, 2.0F, ColorMath.withAlpha(this.п3, ВГщ));
      }

      UiDrawing.drawText(
         ClientFonts.getMediumFont(), this.Я衣.isEmpty() ? ClientLocalization.selectLocalizedText(ВГГ, ВГл) : this.Я衣, var5 - var8, var2 + ВГэ, 11, this.Я衣.isEmpty() ? this.п6 : this.пр
      );
      if (this.ХБ && !this.ХС && System.currentTimeMillis() % ВГю < ВГЩ) {
         UiDrawing.drawRoundedRect(var5 + var6 - var8, var2 + ВГу, 1.0F, ВГ2, 0.0F, this.п3);
      }

      UiDrawing.disableScissor();
      if (!this.Я衣.isEmpty()) {
         this.Ф(ВГф, var1 + this.Ьв() - ВГи, var2 + ВГ9, ВГо, ВГO, 9, this.п6);
      }
   }

   private void method_3(GuiGraphicsExtractor var1, float var2, float var3) {
      this.В_baa6d7e8.clearPreviews();
      this.Ф(var2, var3, this.В_1320c117.В, 0);
      this.В_baa6d7e8.submitPreviews(var1, var2, this.А.getGuiY() + this.Ья(), this.ЬЩ(), this.ЬА());
   }

   private void О(float var1, float var2, float var3) {
      float var4 = Math.clamp(var3, 0.0F, 1.0F);
      int var5 = ColorMath.lerpArgb(this.пЖ, this.пй, var4);
      UiDrawing.drawRoundedRect(var1, var2, В必ы, В必й, В必Ж, var5);
      UiDrawing.drawRoundedRect(var1 + В必м + В必К * var4, var2 + В必I, В必Т, В必О, В必Ф, ColorMath.lerpArgb(this.п6, this.пм, var4));
   }

   public boolean Л(int var1, int var2) {
      boolean var3 = (var2 & 2) != 0;
      if (var1 == 70 && var3) {
         this.ХБ = true;
         this.ХС = !this.Я衣.isEmpty();
         return true;
      }

      if (!this.ХБ) {
         return false;
      }

      if (var1 == 256 || var1 == 257 || var1 == 335 || var1 == 258) {
         this.method_36();
      } else if (var1 == 65 && var3) {
         this.ХС = !this.Я衣.isEmpty();
      } else if (var1 == 86 && var3) {
         this.O(Minecraft.getInstance().keyboardHandler.getClipboard());
      } else if (var1 == 259) {
         if (this.ХС || var3) {
            this.пФ("");
         } else if (!this.Я衣.isEmpty()) {
            this.пФ(this.Я衣.substring(0, this.Я衣.offsetByCodePoints(this.Я衣.length(), -1)));
         }

         this.ХС = false;
      } else if (var1 == 261) {
         this.пФ("");
         this.ХС = false;
      }

      return true;
   }

   private void З(GuiGraphicsExtractor var1, float var2, float var3) {
      WeaponSkinVariant[] var4 = this.Ф();

      for (int var5 = 0; var5 < var4.length; var5++) {
         WeaponSkinVariant var6 = var4[var5];
         float var7 = this.Ф(var2, var5);
         float var8 = this.Г(var5, var3);
         if (this.Л(var8)) {
            boolean var9 = this.В_c7082522 == CosmeticsPanel.WeaponCategory.MACE;
            boolean var10 = (var9 ? ClientCosmeticSelections.getSelectedMaceSkin() : ClientCosmeticSelections.getSelectedSwordSkin()) == var6;
            float var11 = this.Ф(var9 ? this.с6 : this.ср, var6, var10);
            this.Ф(var7, var8, var10);
            this.Ф(var6, var9, var7 + Вщ诶, var8 + Вщ1, this.Ьъ(), ВщЬ, Вщ衣 + var11 * Вщш);
            String var12 = var6.getDisplayName();
            this.Ф(var12, var7, var8, var10);
         }
      }
   }

   private float Ьв() {
      return Math.min(ВчУ, this.ЬЩ() * ВчЗ);
   }

   private void Ф(GuiGraphicsExtractor var1) {
      var1.disableScissor();
      UiDrawing.disableScissor();
   }

   private float Ьщ() {
      return (this.ЬЩ() - ВчЙ) / Вчк;
   }

   private void method_2(float var1, float var2, float var3) {
      UiDrawing.drawRoundedRect(var1, var2, В必Й, В必к, В必г, ColorMath.lerpArgb(this.пЖ, this.пй, var3));
      UiDrawing.drawOutline(var1, var2, В必0, В必х, 1.0F, В必У, this.пР);
      if (var3 > В必З) {
         this.Ф(В必Е, var1 + В必П, var2 + 1.0F, В必西, В必д, 8, ColorMath.withAlpha(this.пр, var3));
      }
   }

   private float Ья() {
      return ВГШ + this.Ьь() - ВГы;
   }

   private void Ф(WeaponSkinVariant var1, boolean var2, float var3, float var4, float var5, float var6, float var7) {
      if (!(var7 <= Вщк)) {
         ItemStack var8 = new ItemStack(var2 ? Items.MACE : Items.DIAMOND_SWORD);
         var8.set(DataComponents.ITEM_MODEL, var1.getItemModelId());
         float var9 = UiDrawing.getGlobalAlpha();
         UiDrawing.setGlobalAlpha(var9 * var7);

         try {
            UiDrawing.drawFittedItem(var8, var3, var4, var5, var6);
         } finally {
            UiDrawing.setGlobalAlpha(var9);
         }
      }
   }

   private void 衣(GuiGraphicsExtractor var1, float var2, float var3) {
      this.В_baa6d7e8.clearPreviews();
      List var4 = this.см_2571477d;

      for (int var5 = 0; var5 < var4.size(); var5++) {
         HatDefinition var6 = (HatDefinition)var4.get(var5);
         float var7 = this.Ф(var2, var5);
         float var8 = this.Г(var5, var3);
         if (this.Л(var8)) {
            boolean var9 = !ClientCosmeticSelections.isCatalogModelSelected(CosmeticCategory.HATS) && ClientCosmeticSelections.getSelectedHatKey().equals(var6.key());
            float var10 = this.Ф(this.с3, var6.key(), var9);
            this.Ф(var7, var8, var9);
            float var11 = (Вщт + var10 * Вща) * this.А.getGuiAlpha() * this.с8.getValue() * this.А.getLayoutContentAlpha(0);
            this.В_baa6d7e8.queuePreview(var6, var7 + ВщЮ, var8 + ВщЧ, this.Ьъ(), ВщЛ, var11);
            this.Ф(var6.displayName(), var7, var8, var9);
         }
      }

      this.Ф(var2, var3, CosmeticCategory.HATS, var4.size());
      this.В_baa6d7e8.submitPreviews(var1, var2, this.А.getGuiY() + this.Ья(), this.ЬЩ(), this.ЬА());
   }

   private float Ь3() {
      return this.А.getSidebarWidth() + ВчО;
   }

   private void ь(float var1, float var2, float var3) {
      float var4 = this.А.getGuiY() + ВщИ;
      float var5 = Вщь;
      float var6 = Вщж;

      for (int var7 = 0; var7 < CosmeticsPanel.WeaponCategory.values().length; var7++) {
         CosmeticsPanel.WeaponCategory var8 = CosmeticsPanel.WeaponCategory.values()[var7];
         float var9 = var1 + var7 * (var5 + var6);
         boolean var10 = this.В_c7082522 == var8;
         boolean var11 = this.Ф(var2, var3, var9, var4, var5, Вщ弟);
         FloatAnimation var12 = this.см_ea411899.computeIfAbsent(var8, var1x -> new FloatAnimation(var10 ? 1.0F : 0.0F, ВГЗ, Easings.CUBIC_OUT));
         FloatAnimation var13 = this.сК_ea411899.computeIfAbsent(var8, var0 -> new FloatAnimation(0.0F, ВГУ, Easings.CUBIC_OUT));
         var12.updateTarget(var10 ? 1.0F : 0.0F);
         var13.updateTarget(var11 ? 1.0F : 0.0F);
         this.Ь(var9, var4, var5, Вщб, ВщЫ, var12.getValue(), var13.getValue());
         this.Ф(var9, var4, var5, var8 == CosmeticsPanel.WeaponCategory.SWORDS ? ClientLocalization.selectLocalizedText(Вщl, Вщ8) : ClientLocalization.selectLocalizedText(Вщ5, Вщв), var12.getValue(), var13.getValue());
      }
   }

   private enum FurryCategory {
      EARS("Ушки", CosmeticCategory.EARS),
      TAILS("Хвостики", CosmeticCategory.TAILS),
      PAWS("Лапки", CosmeticCategory.PAWS),
      STOCKINGS("Чулочки", CosmeticCategory.STOCKINGS),
      HEADS("Головы", CosmeticCategory.HEADS);

      private final String Хщ;
      private final CosmeticCategory В;

      FurryCategory(String var3, CosmeticCategory var4) {
         this.Хщ = var3;
         this.В = var4;
      }
   }

   private enum WeaponCategory {
      SWORDS,
      MACE;
   }

   private enum PanelTab {
      HATS("Шляпы"),
      MASKS("Маски"),
      WINGS("Крылья"),
      BACKPACKS("Рюкзаки"),
      PETS("Питомцы"),
      FURRY("Фурри"),
      ITEMS("SkinChanger");

      private final String сь;

      PanelTab(String var3) {
         this.сь = var3;
      }
   }
}
