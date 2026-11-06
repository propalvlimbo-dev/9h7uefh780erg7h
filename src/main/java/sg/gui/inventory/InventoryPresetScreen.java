package sg.gui.inventory;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2f;
import settings.render.ClientLocalization;
import sg.ec.FontRenderer;
import sg.modules.impl.misc.InventorySort;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.modules.impl.init.InventoryPresetStore;

public final class InventoryPresetScreen extends Screen {
   private static final float К = 940.0F;
   private static final float I = 654.0F;
   private static final float Т = 64.0F;
   private static final float О = 38.0F;
   private static final float Ф = 43.0F;
   private static final float Й = 38.0F;
   private static final float к = 244.0F;
   private static final float г = 396.0F;
   private static final float field_0 = 476.0F;
   private static final float х = 154.0F;
   private static final float У = 64.0F;
   private static final float З = 70.0F;
   private static final int Ь = 6;
   private static final int 衣 = 4;
   private static final float Е = 414.0F;
   private static final float П = 274.0F;
   private static final int ш = -986118;
   private static final int И = -6576196;
   private static final int ь = -13616564;
   private static final int[] В_28f5b604 = new int[]{39, 38, 37, 36, 40};
   private static final String[] Я = new String[]{InventoryPresetScreen.ХВК, InventoryPresetScreen.ХВI, InventoryPresetScreen.ХВТ, InventoryPresetScreen.ХВО, InventoryPresetScreen.ХВФ};
   @SerializedName("В")
   private final InventorySort В_d5f06454;
   private final FloatAnimation 诶;
   private List<InventorySort.CatalogItem> Х;
   private List<InventorySort.CatalogItem> е;
   private float 西;
   private float д;
   private float Б;
   private float С;
   private float з;
   @SerializedName("ж")
   private int ж_a83dd0cc;
   @SerializedName("弟")
   private int 弟_a83dd0cc;
   @SerializedName("ж")
   private String ж_388008bb;
   @SerializedName("弟")
   private String 弟_388008bb;
   @SerializedName("Ы")
   private boolean Ы_bbeebd87;
   @SerializedName("б")
   private int б_a83dd0cc;
   private float ц;
   @SerializedName("В")
   private InventoryPresetScreen.DialogKind В_7c835ec4;
   @SerializedName("б")
   private String б_388008bb;
   @SerializedName("Ы")
   private String Ы_388008bb;
   @SerializedName("Ы")
   private int Ы_a83dd0cc;
   @SerializedName("l")
   private int l_a83dd0cc;
   @SerializedName("l")
   private String l_388008bb;
   @SerializedName("l")
   private boolean l_bbeebd87;
   @SerializedName("8")
   private boolean field_8_bbeebd87;
   @SerializedName("8")
   private int field_8_a83dd0cc;
   @SerializedName("В")
   private InventorySort.CatalogItem В_5c154634;
   @SerializedName("В")
   private InventorySort.SlotRule В_461faf9f;
   private float я;
   private float сс;
   @SerializedName("5")
   private boolean field_5;
   private static final String Я西6 = "InventorySort";
   private static final float Я西_ = 7.0F;
   private static final float Я西Ш = 0.1F;
   private static final float Я西ы = 32.0F;
   private static final float Я西й = 940.0F;
   private static final float Я西Ж = 32.0F;
   private static final float Я西м = 654.0F;
   private static final float Я西К = 940.0F;
   private static final float Я西I = 0.5F;
   private static final float Я西Т = 654.0F;
   private static final float Я西О = 0.5F;
   private static final float Я西Ф = 255.0F;
   private static final int Я西Й = -16777216;
   private static final int Я西к = -5453057;
   private static final float Я西г = 0.35F;
   private static final float Я西0 = 0.2F;
   private static final int Я西х = 1711671568;
   private static final float Я西У = 940.0F;
   private static final float Я西З = 654.0F;
   private static final float Я西Е = 28.0F;
   private static final int Я西П = -1207959552;
   private static final float Я西西 = 940.0F;
   private static final float Я西д = 654.0F;
   private static final float Я西Б = 18.0F;
   private static final float Я西С = 20.0F;
   private static final float Я西з = 940.0F;
   private static final float Я西ц = 654.0F;
   private static final float Я西я = 20.0F;
   private static final int Ядс = -233629403;
   private static final float ЯдВ = 940.0F;
   private static final float ЯдЯ = 654.0F;
   private static final float ЯдХ = 20.0F;
   private static final int Яде = -12958376;
   private static final float ЯдА = -1.0F;
   private static final float Яд4 = -1.0F;
   private static final float Ядп = 64.0F;
   private static final float ЯдД = 64.0F;
   private static final float ЯдЪ = 24.0F;
   private static final float Ядт = 24.0F;
   private static final float Яда = 40.0F;
   private static final float ЯдЮ = 40.0F;
   private static final float ЯдЧ = 12.0F;
   private static final float ЯдЛ = 5.0F;
   private static final float Яд诶 = 5.0F;
   private static final float Яд1 = 1.5F;
   private static final String ЯдЬ = "InventorySort";
   private static final float Яд衣 = 78.0F;
   private static final float Ядш = 23.0F;
   private static final int ЯдИ = -986118;
   private static final String Ядь = "Твой инвентарь. Твой порядок.";
   private static final float Ядж = 79.0F;
   private static final float Яд弟 = 52.0F;
   private static final int Ядб = -6576196;
   private static final float ЯдЫ = 432.0F;
   private static final float Ядl = 26.0F;
   private static final float Яд8 = 256.0F;
   private static final float Яд5 = 38.0F;
   private static final float Ядв = 11.0F;
   private static final int Ядъ = -15788252;
   private static final float ЯдМ = 436.0F;
   private static final float ЯдН = 126.0F;
   private static final float Ядн = 566.0F;
   private static final float ЯдЦ = 118.0F;
   private static final float Ядч = 706.0F;
   private static final float ЯдЭ = 30.0F;
   private static final float Яд必 = 142.0F;
   private static final float Ядщ = 28.0F;
   private static final float ЯдГ = 14.0F;
   private static final int Ядл = -14537667;
   private static final String Ядэ = " / 41 слотов";
   private static final float Ядю = 706.0F;
   private static final float ЯдЩ = 38.0F;
   private static final float Яду = 142.0F;
   private static final int Яд2 = -6576196;
   private static final float Ядф = 868.0F;
   private static final float Яди = 26.0F;
   private static final float Яд9 = 44.0F;
   private static final float Ядо = 36.0F;
   private static final String ЯдO = "×";
   private static final float Яд7 = 24.0F;
   private static final float ЯдР = 84.0F;
   private static final float Яд3 = 892.0F;
   private static final int Ядр = -13616564;
   private static final float Яд6 = 24.0F;
   private static final float Яд_ = 102.0F;
   private static final float ЯдШ = 414.0F;
   private static final float Яды = 394.0F;
   private static final float Ядй = 14.0F;
   private static final int ЯдЖ = -15130321;
   private static final String Ядм = "01";
   private static final String ЯдК = "Раскладка";
   private static final float ЯдI = 38.0F;
   private static final float ЯдТ = 114.0F;
   private static final String ЯдО = "ЭКИПИРОВКА";
   private static final float ЯдФ = 38.0F;
   private static final float ЯдЙ = 145.0F;
   private static final int Ядк = -6576196;
   private static final float Ядг = 166.0F;
   private static final float Яд0 = 5.0F;
   private static final float Ядх = 209.0F;
   private static final float ЯдУ = 48.0F;
   private static final int ЯдЗ = -6576196;
   private static final String ЯдЕ = "ИНВЕНТАРЬ";
   private static final float ЯдП = 38.0F;
   private static final float Яд西 = 227.0F;
   private static final int Ядд = -6576196;
   private static final float ЯдБ = 38.0F;
   private static final float ЯдС = 43.0F;
   private static final float Ядз = 244.0F;
   private static final float Ядц = 43.0F;
   private static final String Ядя = "БЫСТРЫЙ ДОСТУП";
   private static final float ЯБс = 38.0F;
   private static final float ЯБВ = 376.0F;
   private static final int ЯБЯ = -6576196;
   private static final float ЯБХ = 38.0F;
   private static final float ЯБе = 43.0F;
   private static final float ЯБА = 396.0F;
   private static final float ЯБ4 = 38.0F;
   private static final float ЯБп = 43.0F;
   private static final float ЯБД = 439.0F;
   private static final float ЯБЪ = 38.0F;
   private static final int ЯБт = -6576196;
   private static final String ЯБа = "Зажми ЛКМ и перетащи предмет в нужный слот";
   private static final float ЯБЮ = 38.0F;
   private static final float ЯБЧ = 458.0F;
   private static final int ЯБЛ = -6576196;
   private static final String ЯБ诶 = "ПКМ / Del  очистить   ·   Ctrl+Z  отменить";
   private static final float ЯБ1 = 38.0F;
   private static final float ЯБЬ = 477.0F;
   private static final int ЯБ衣 = -6576196;
   private static final float ЯБш = 30.0F;
   private static final float ЯБИ = 30.0F;
   private static final float ЯБь = 30.0F;
   private static final float ЯБж = 30.0F;
   private static final float ЯБ弟 = 8.0F;
   private static final int ЯБб = -14076085;
   private static final float ЯБЫ = 39.0F;
   private static final int ЯБl = -6576196;
   private static final float ЯБ8 = 24.0F;
   private static final float ЯБ5 = 100.0F;
   private static final float ЯБв = 282.0F;
   private static final float ЯБъ = 36.0F;
   private static final float ЯБМ = 226.0F;
   private static final float ЯБН = 38.0F;
   private static final float ЯБн = 111.0F;
   private static final int ЯБЦ = -986118;
   private static final String ЯБч = "−";
   private static final String ЯБЭ = "+";
   private static final float ЯБ必 = 281.0F;
   private static final float ЯБщ = 111.0F;
   private static final float ЯБГ = 320.0F;
   private static final float ЯБл = 100.0F;
   private static final float ЯБэ = 176.0F;
   private static final float ЯБю = 36.0F;
   private static final String ЯБЩ = "Сохранить как…";
   private static final float ЯБу = 508.0F;
   private static final float ЯБ2 = 100.0F;
   private static final float ЯБф = 146.0F;
   private static final float ЯБи = 36.0F;
   private static final String ЯБ9 = "Переименовать";
   private static final float ЯБо = 666.0F;
   private static final float ЯБO = 100.0F;
   private static final float ЯБ7 = 94.0F;
   private static final float ЯБР = 36.0F;
   private static final String ЯБ3 = "Удалить";
   private static final float ЯБр = 796.0F;
   private static final float ЯБ6 = 114.0F;
   private static final float ЯБ_ = 6.0F;
   private static final float ЯБШ = 6.0F;
   private static final float ЯБы = 3.0F;
   private static final int ЯБй = -7944026;
   private static final String ЯБЖ = "Сохранено";
   private static final float ЯБм = 812.0F;
   private static final float ЯБК = 111.0F;
   private static final int ЯБI = -7944026;
   private static final float ЯБТ = 780.0F;
   private static final float ЯБО = 100.0F;
   private static final float ЯБФ = 136.0F;
   private static final float ЯБЙ = 36.0F;
   private static final String ЯБк = "Повторить запись";
   private static final String ЯБг = "Изменения выбранного пресета сохраняются автоматически";
   private static final String ЯБ0 = "Не удалось сохранить на диск. Нажми «Повторить запись».";
   private static final float ЯБх = 38.0F;
   private static final float ЯБУ = 143.0F;
   private static final int ЯБЗ = -6576196;
   private static final float ЯБЕ = 24.0F;
   private static final float ЯБП = 140.0F;
   private static final float ЯБ西 = 282.0F;
   private static final float ЯБд = 12.0F;
   private static final int ЯББ = -15788252;
   private static final float ЯБС = 24.0F;
   private static final float ЯБз = 140.0F;
   private static final float ЯБц = 282.0F;
   private static final float ЯБя = 12.0F;
   private static final int ЯСс = -11312518;
   private static final float ЯСВ = 30.0F;
   private static final float ЯСЯ = 270.0F;
   private static final float ЯСХ = 32.0F;
   private static final float ЯСе = 30.0F;
   private static final float ЯСА = 270.0F;
   private static final float ЯС4 = 32.0F;
   private static final float ЯСп = 8.0F;
   private static final int ЯСД = -14076085;
   private static final float ЯСЪ = 232.0F;
   private static final float ЯСт = 42.0F;
   private static final float ЯСа = 10.0F;
   private static final int ЯСЮ = -986118;
   private static final float ЯСЧ = 281.0F;
   private static final float ЯСЛ = 14.0F;
   private static final float ЯС诶 = 5.0F;
   private static final float ЯС1 = 5.0F;
   private static final float ЯСЬ = 2.5F;
   private static final float ЯС衣 = 302.0F;
   private static final float ЯСш = 148.0F;
   private static final float ЯСИ = 214.0F;
   private static final float ЯСь = 940.0F;
   private static final float ЯСж = 654.0F;
   private static final float ЯС弟 = 20.0F;
   private static final int ЯСб = -1257697768;
   private static final float ЯСЫ = 252.0F;
   private static final float ЯСl = 436.0F;
   private static final float ЯС8 = 226.0F;
   private static final float ЯС5 = 16.0F;
   private static final int ЯСв = -15129806;
   private static final float ЯСъ = 252.0F;
   private static final float ЯСМ = 436.0F;
   private static final float ЯСН = 226.0F;
   private static final float ЯСн = 16.0F;
   private static final int ЯСЦ = -11312518;
   private static final String ЯСч = "Сохранить раскладку как…";
   private static final String ЯСЭ = "Переименовать пресет";
   private static final String ЯС必 = "Удалить пресет?";
   private static final float ЯСщ = 274.0F;
   private static final float ЯСГ = 22.0F;
   private static final int ЯСл = -986118;
   private static final String ЯСэ = "Новый пресет получит текущую раскладку";
   private static final String ЯСю = "Название до 32 символов";
   private static final String ЯСЩ = "Другие пресеты останутся на месте";
   private static final float ЯСу = 274.0F;
   private static final float ЯС2 = 54.0F;
   private static final int ЯСф = -6576196;
   private static final float ЯСи = 352.0F;
   private static final float ЯС9 = 274.0F;
   private static final float ЯСо = 94.0F;
   private static final int ЯСO = -986118;
   private static final float ЯС7 = 274.0F;
   private static final float ЯСР = 82.0F;
   private static final float ЯС3 = 392.0F;
   private static final float ЯСр = 38.0F;
   private static final float ЯС6 = 9.0F;
   private static final int ЯС_ = -15853788;
   private static final float ЯСШ = 274.0F;
   private static final float ЯСы = 82.0F;
   private static final float ЯСй = 392.0F;
   private static final float ЯСЖ = 38.0F;
   private static final float ЯСм = 9.0F;
   private static final float ЯСК = 362.0F;
   private static final float ЯСI = 284.0F;
   private static final float ЯСТ = 84.0F;
   private static final float ЯСО = 372.0F;
   private static final float ЯСФ = 34.0F;
   private static final float ЯСЙ = 286.0F;
   private static final float ЯСк = 91.0F;
   private static final float ЯСг = 19.0F;
   private static final String ЯС0 = "Например, Burrr";
   private static final float ЯСх = 286.0F;
   private static final float ЯСУ = 94.0F;
   private static final int ЯСЗ = -6576196;
   private static final int ЯСЕ = -986118;
   private static final long ЯСП = 500L;
   private static final long ЯС西 = 2L;
   private static final float ЯСд = 286.0F;
   private static final float ЯСБ = 91.0F;
   private static final float ЯСС = 18.0F;
   private static final float ЯСз = 274.0F;
   private static final float ЯСц = 132.0F;
   private static final float ЯСя = 274.0F;
   private static final float Язс = 166.0F;
   private static final float ЯзВ = 182.0F;
   private static final float ЯзЯ = 38.0F;
   private static final String ЯзХ = "Отмена";
   private static final float Язе = 468.0F;
   private static final float ЯзА = 166.0F;
   private static final float Яз4 = 198.0F;
   private static final float Язп = 38.0F;
   private static final String ЯзД = "Удалить";
   private static final String ЯзЪ = "Сохранить";
   private static final float Язт = 38.0F;
   private static final float Яза = 38.0F;
   private static final float ЯзЮ = 38.0F;
   private static final float ЯзЧ = 38.0F;
   private static final float ЯзЛ = 9.0F;
   private static final int Яз诶 = -13615791;
   private static final int Яз1 = -15656667;
   private static final float ЯзЬ = 38.0F;
   private static final float Яз衣 = 38.0F;
   private static final float Язш = 9.0F;
   private static final int ЯзИ = -10390643;
   private static final int Язь = -13616564;
   private static final float Язж = 38.0F;
   private static final float Яз弟 = 38.0F;
   private static final float Язб = 9.0F;
   private static final int ЯзЫ = -7944026;
   private static final float Язl = 6.0F;
   private static final float Яз8 = 5.0F;
   private static final float Яз5 = 26.0F;
   private static final float Язв = 6.0F;
   private static final float Язъ = 38.0F;
   private static final float ЯзМ = 38.0F;
   private static final float ЯзН = 14.0F;
   private static final float Язн = 12.0F;
   private static final float ЯзЦ = 4.0F;
   private static final int Язч = -267578336;
   private static final float ЯзЭ = 38.0F;
   private static final float Яз必 = 38.0F;
   private static final float Язщ = 13.0F;
   private static final int ЯзГ = -986118;
   private static final String Язл = "+";
   private static final String Язэ = "·";
   private static final float Язю = 11.0F;
   private static final float ЯзЩ = 38.0F;
   private static final int Язу = -12430998;
   private static final float Яз2 = 36.0F;
   private static final float Язф = 36.0F;
   private static final float Язи = 8.0F;
   private static final int Яз9 = -1609492187;
   private static final String Язо = "Перетащи сюда предмет или выбери справа";
   private static final String ЯзO = "ЛКМ: перетащить  ·  Колесо: количество";
   private static final float Яз7 = 458.0F;
   private static final float ЯзР = 102.0F;
   private static final float Яз3 = 458.0F;
   private static final float Язр = 334.0F;
   private static final float Яз6 = 14.0F;
   private static final int Яз_ = -15130321;
   private static final String ЯзШ = "02";
   private static final String Язы = "Предметы";
   private static final float Язй = 476.0F;
   private static final float ЯзЖ = 114.0F;
   private static final float Язм = 678.0F;
   private static final float ЯзК = 111.0F;
   private static final float ЯзI = 216.0F;
   private static final float ЯзТ = 28.0F;
   private static final float ЯзО = 8.0F;
   private static final int ЯзФ = -15722203;
   private static final float ЯзЙ = 678.0F;
   private static final float Язк = 111.0F;
   private static final float Язг = 216.0F;
   private static final float Яз0 = 28.0F;
   private static final float Язх = 8.0F;
   private static final int ЯзУ = -13616564;
   private static final float ЯзЗ = 686.0F;
   private static final float ЯзЕ = 116.0F;
   private static final float ЯзП = 174.0F;
   private static final float Яз西 = 17.0F;
   private static final String Язд = "Поиск предметов…";
   private static final float ЯзБ = 174.0F;
   private static final float ЯзС = 687.0F;
   private static final float Язз = 120.0F;
   private static final int Язц = -6576196;
   private static final int Язя = -986118;
   private static final String Яцс = "×";
   private static final float ЯцВ = 875.0F;
   private static final float ЯцЯ = 118.0F;
   private static final int ЯцХ = -6576196;
   private static final float Яце = 476.0F;
   private static final float ЯцА = 154.0F;
   private static final float Яц4 = 414.0F;
   private static final float Яцп = 274.0F;
   private static final float ЯцД = 476.0F;
   private static final float ЯцЪ = 70.0F;
   private static final float Яцт = 154.0F;
   private static final float Яца = 70.0F;
   private static final float ЯцЮ = 64.0F;
   private static final float ЯцЧ = 154.0F;
   private static final float ЯцЛ = 428.0F;
   private static final float Яц诶 = 476.0F;
   private static final float Яц1 = 154.0F;
   private static final float ЯцЬ = 414.0F;
   private static final float Яц衣 = 274.0F;
   private static final float Яцш = 64.0F;
   private static final float ЯцИ = 64.0F;
   private static final float Яць = 64.0F;
   private static final float Яцж = 64.0F;
   private static final float Яц弟 = 10.0F;
   private static final int Яцб = -13615014;
   private static final int ЯцЫ = -14537668;
   private static final float Яцl = 64.0F;
   private static final float Яц8 = 64.0F;
   private static final float Яц5 = 10.0F;
   private static final float Яцв = 18.0F;
   private static final float Яцъ = 8.0F;
   private static final float ЯцМ = 28.0F;
   private static final String ЯцН = "Перетащи в слот или нажми для выбора";
   private static final String Яцн = "Ничего не найдено";
   private static final float ЯцЦ = 476.0F;
   private static final float Яцч = 244.0F;
   private static final float ЯцЭ = 414.0F;
   private static final int Яц必 = -6576196;
   private static final float Яцщ = 75076.0F;
   private static final float ЯцГ = 274.0F;
   private static final float Яцл = 900.0F;
   private static final float Яцэ = 154.0F;
   private static final float Яцю = 3.0F;
   private static final float ЯцЩ = 274.0F;
   private static final float Яцу = 1.5F;
   private static final int Яц2 = -13616564;
   private static final float Яцф = 900.0F;
   private static final float Яци = 154.0F;
   private static final float Яц9 = 274.0F;
   private static final float Яцо = 3.0F;
   private static final float ЯцO = 1.5F;
   private static final float Яц7 = 58.0F;
   private static final String ЯцР = " ";
   private static final float Яц3 = 3.0F;
   private static final float Яцр = 46.0F;
   private static final int Яц6 = -986118;
   private static final float Яц_ = 3.0F;
   private static final float ЯцШ = 42.0F;
   private static final int Яцы = -6576196;
   private static final float Яцй = 3.0F;
   private static final float ЯцЖ = 53.0F;
   private static final int Яцм = -986118;
   private static final float ЯцК = 458.0F;
   private static final float ЯцI = 446.0F;
   private static final float ЯцТ = 458.0F;
   private static final float ЯцО = 50.0F;
   private static final float ЯцФ = 12.0F;
   private static final int ЯцЙ = -14537408;
   private static final float Яцк = 474.0F;
   private static final float Яцг = 455.0F;
   private static final String Яц0 = "Нажми на предмет, чтобы назначить";
   private static final float Яцх = 240.0F;
   private static final float ЯцУ = 474.0F;
   private static final float ЯцЗ = 474.0F;
   private static final int ЯцЕ = -6576196;
   private static final int ЯцП = -986118;
   private static final float Яц西 = 786.0F;
   private static final float Яцд = 455.0F;
   private static final float ЯцБ = 28.0F;
   private static final float ЯцС = 30.0F;
   private static final String Яцз = "−";
   private static final String Яцц = "—";
   private static final float Яця = 819.0F;
   private static final float Яяс = 464.0F;
   private static final float ЯяВ = 42.0F;
   private static final int ЯяЯ = -986118;
   private static final float ЯяХ = 866.0F;
   private static final float Яяе = 455.0F;
   private static final float ЯяА = 28.0F;
   private static final float Яя4 = 30.0F;
   private static final String Яяп = "+";
   private static final float ЯяД = 24.0F;
   private static final float ЯяЪ = 512.0F;
   private static final float Яят = 892.0F;
   private static final int Яяа = -13616564;
   private static final float ЯяЮ = 24.0F;
   private static final float ЯяЧ = 530.0F;
   private static final float ЯяЛ = 204.0F;
   private static final float Яя诶 = 38.0F;
   private static final String Яя1 = "Взять текущий инвентарь";
   private static final float ЯяЬ = 238.0F;
   private static final float Яя衣 = 530.0F;
   private static final float Яяш = 100.0F;
   private static final float ЯяИ = 38.0F;
   private static final String Яяь = "Очистить";
   private static final float Яяж = 350.0F;
   private static final float Яя弟 = 530.0F;
   private static final float Яяб = 174.0F;
   private static final float ЯяЫ = 38.0F;
   private static final String Яяl = "Отменить · Ctrl+Z";
   private static final String Яя8 = "Колесо: кол-во";
   private static final float Яя5 = 544.0F;
   private static final float Яяв = 543.0F;
   private static final int Яяъ = -6576196;
   private static final float ЯяМ = 704.0F;
   private static final float ЯяН = 528.0F;
   private static final float Яян = 212.0F;
   private static final float ЯяЦ = 42.0F;
   private static final String Яяч = "Применить раскладку";
   private static final float ЯяЭ = 24.0F;
   private static final float Яя必 = 24.0F;
   private static final float Яящ = 7.0F;
   private static final float ЯяГ = 6.0F;
   private static final float Яял = 24.0F;
   private static final float Яяэ = 34.0F;
   private static final float Яяю = 4.0F;
   private static final int ЯяЩ = -986118;
   private static final float Яяу = 0.18F;
   private static final int Яя2 = -13286050;
   private static final int Яяф = -14471874;
   private static final float Яяи = 10.0F;
   private static final float Яя9 = 10.0F;
   private static final int Яяо = -13616564;
   private static final float ЯяO = 12.0F;
   private static final float Яя7 = 0.5F;
   private static final int ЯяР = -9931123;
   private static final int Яя3 = -15722203;
   private static final int Яяр = -986118;
   private static final float Яя6 = 300.0F;
   private static final float Яя_ = 300.0F;
   private static final float ЯяШ = 24.0F;
   private static final float Яяы = 14.0F;
   private static final float Яяй = 8.0F;
   private static final float ЯяЖ = 940.0F;
   private static final float Яям = 8.0F;
   private static final float ЯяК = 18.0F;
   private static final float ЯяI = 8.0F;
   private static final float ЯяТ = 600.0F;
   private static final float ЯяО = 46.0F;
   private static final float ЯяФ = 10.0F;
   private static final int ЯяЙ = -15985888;
   private static final float Яяк = 46.0F;
   private static final float Яяг = 10.0F;
   private static final int Яя0 = -11312518;
   private static final float Яях = 12.0F;
   private static final float ЯяУ = 8.0F;
   private static final int ЯяЗ = -986118;
   private static final float ЯяЕ = 12.0F;
   private static final float ЯяП = 27.0F;
   private static final int Яя西 = -6576196;
   private static final float Яяд = 214.0F;
   private static final float ЯяБ = 274.0F;
   private static final float ЯяС = 166.0F;
   private static final float Яяз = 182.0F;
   private static final float Яяц = 38.0F;
   private static final float Яяя = 468.0F;
   private static final float Хсс = 166.0F;
   private static final float ХсВ = 198.0F;
   private static final float ХсЯ = 38.0F;
   private static final float ХсХ = 274.0F;
   private static final float Хсе = 82.0F;
   private static final float ХсА = 392.0F;
   private static final float Хс4 = 38.0F;
   private static final float Хсп = 30.0F;
   private static final float ХсД = 146.0F;
   private static final float ХсЪ = 270.0F;
   private static final float Хст = 146.0F;
   private static final float Хса = 34.0F;
   private static final float ХсЮ = 868.0F;
   private static final float ХсЧ = 26.0F;
   private static final float ХсЛ = 44.0F;
   private static final float Хс诶 = 36.0F;
   private static final float Хс1 = 436.0F;
   private static final float ХсЬ = 30.0F;
   private static final float Хс衣 = 126.0F;
   private static final float Хсш = 30.0F;
   private static final float ХсИ = 566.0F;
   private static final float Хсь = 30.0F;
   private static final float Хсж = 118.0F;
   private static final float Хс弟 = 30.0F;
   private static final float Хсб = 24.0F;
   private static final float ХсЫ = 100.0F;
   private static final float Хсl = 282.0F;
   private static final float Хс8 = 36.0F;
   private static final float Хс5 = 320.0F;
   private static final float Хсв = 100.0F;
   private static final float Хсъ = 176.0F;
   private static final float ХсМ = 36.0F;
   private static final float ХсН = 508.0F;
   private static final float Хсн = 100.0F;
   private static final float ХсЦ = 146.0F;
   private static final float Хсч = 36.0F;
   private static final float ХсЭ = 666.0F;
   private static final float Хс必 = 100.0F;
   private static final float Хсщ = 94.0F;
   private static final float ХсГ = 36.0F;
   private static final float Хсл = 780.0F;
   private static final float Хсэ = 100.0F;
   private static final float Хсю = 136.0F;
   private static final float ХсЩ = 36.0F;
   private static final float Хсу = 64.0F;
   private static final float Хс2 = 678.0F;
   private static final float Хсф = 111.0F;
   private static final float Хси = 216.0F;
   private static final float Хс9 = 28.0F;
   private static final float Хсо = 868.0F;
   private static final float ХсO = 111.0F;
   private static final float Хс7 = 26.0F;
   private static final float ХсР = 28.0F;
   private static final float Хс3 = 24.0F;
   private static final float Хср = 530.0F;
   private static final float Хс6 = 204.0F;
   private static final float Хс_ = 38.0F;
   private static final float ХсШ = 238.0F;
   private static final float Хсы = 530.0F;
   private static final float Хсй = 100.0F;
   private static final float ХсЖ = 38.0F;
   private static final float Хсм = 350.0F;
   private static final float ХсК = 530.0F;
   private static final float ХсI = 174.0F;
   private static final float ХсТ = 38.0F;
   private static final float ХсО = 786.0F;
   private static final float ХсФ = 455.0F;
   private static final float ХсЙ = 28.0F;
   private static final float Хск = 30.0F;
   private static final float Хсг = 866.0F;
   private static final float Хс0 = 455.0F;
   private static final float Хсх = 28.0F;
   private static final float ХсУ = 30.0F;
   private static final float ХсЗ = 704.0F;
   private static final float ХсЕ = 528.0F;
   private static final float ХсП = 212.0F;
   private static final float Хс西 = 42.0F;
   private static final float Хсд = 20.0F;
   private static final float ХсБ = 20.0F;
   private static final float ХсС = 40.0F;
   private static final float Хсз = 40.0F;
   private static final float Хсц = 10.0F;
   private static final int Хся = -433967808;
   private static final float ХВс = 20.0F;
   private static final float ХВВ = 20.0F;
   private static final float ХВЯ = 40.0F;
   private static final float ХВХ = 40.0F;
   private static final float ХВе = 10.0F;
   private static final float ХВА = 15.0F;
   private static final float ХВ4 = 16.0F;
   private static final float ХВп = 30.0F;
   private static final float ХВД = 8.0F;
   private static final float ХВЪ = 9.0F;
   private static final int ХВт = -986118;
   private static final float ХВа = 64.0F;
   private static final float ХВЮ = 16.0F;
   private static final float ХВЧ = 64.0F;
   private static final float ХВЛ = 16.0F;
   private static final float ХВ诶 = 64.0F;
   private static final float ХВ1 = 476.0F;
   private static final float ХВЬ = 154.0F;
   private static final float ХВ衣 = 414.0F;
   private static final float ХВш = 274.0F;
   private static final float ХВИ = 70.0F;
   private static final float ХВь = 166.0F;
   private static final float ХВж = 38.0F;
   private static final float ХВ弟 = 38.0F;
   private static final float ХВб = 38.0F;
   private static final float ХВЫ = 43.0F;
   private static final float ХВl = 244.0F;
   private static final float ХВ8 = 43.0F;
   private static final float ХВ5 = 38.0F;
   private static final float ХВв = 38.0F;
   private static final float ХВъ = 38.0F;
   private static final float ХВМ = 43.0F;
   private static final float ХВН = 396.0F;
   private static final float ХВн = 38.0F;
   private static final float ХВЦ = 38.0F;
   private static final float ХВч = 476.0F;
   private static final float ХВЭ = 154.0F;
   private static final float ХВ必 = 414.0F;
   private static final float ХВщ = 274.0F;
   private static final float ХВГ = 476.0F;
   private static final float ХВл = 70.0F;
   private static final float ХВэ = 154.0F;
   private static final float ХВю = 70.0F;
   private static final float ХВЩ = 64.0F;
   private static final float ХВу = 64.0F;
   private static final float ХВ2 = 70.0F;
   private static final float ХВф = 6.0F;
   private static final float ХВи = 274.0F;
   private static final float ХВ9 = 382.0F;
   private static final float ХВо = 38.0F;
   private static final float ХВO = 57.0F;
   private static final String ХВ7 = "Голова";
   private static final String ХВР = "Корпус";
   private static final String ХВ3 = "Ноги";
   private static final String ХВр = "Ступни";
   private static final String ХВ6 = "Левая рука";
   private static final String ХВ_ = "Быстрый доступ · ";
   private static final String ХВШ = "Инвентарь · ";
   private static final int ХВы = 16777215;
   private static final float ХВй = 16.0F;
   private static final float ХВЖ = 0.5F;
   private static final float ХВм = 0.5F;
   private static final String ХВК = "Голова";
   private static final String ХВI = "Корпус";
   private static final String ХВТ = "Ноги";
   private static final String ХВО = "Ступни";
   private static final String ХВФ = "Левая рука";

   public InventoryPresetScreen(InventorySort var1) {
      super(Component.literal(Я西6));
      this.诶 = new FloatAnimation(0.0F, Я西_, Easings.CUBIC_OUT);
      this.Х = List.of();
      this.е = List.of();
      this.Б = 1.0F;
      this.В_7c835ec4 = InventoryPresetScreen.DialogKind.NONE;
      this.б_388008bb = "";
      this.Ы_388008bb = "";
      this.l_388008bb = "";
      this.field_8_a83dd0cc = -1;
      this.В_d5f06454 = var1;
   }

   private void openDialog(InventoryPresetScreen.DialogKind var1) {
      this.cancelDrag();
      this.l_bbeebd87 = false;
      this.Ы_bbeebd87 = false;
      this.В_7c835ec4 = var1;
      this.Ы_388008bb = "";
      this.б_388008bb = var1 == InventoryPresetScreen.DialogKind.RENAME ? this.В_d5f06454.иБ() : "";
      this.Ы_a83dd0cc = this.б_388008bb.length();
      this.l_a83dd0cc = 0;
   }

   private float py(float var1) {
      return this.д + (var1 + this.ц) * this.Б;
   }

   private void confirmDialog() {
      try {
         switch (this.В_7c835ec4) {
            case CREATE:
               this.В_d5f06454.method_3(this.б_388008bb);
               break;
            case RENAME:
               this.В_d5f06454.Н(this.б_388008bb);
               break;
            case DELETE:
               this.В_d5f06454.Э5();
               break;
            default:
               return;
         }

         this.В_7c835ec4 = InventoryPresetScreen.DialogKind.NONE;
         this.refreshEntries();
      } catch (IllegalArgumentException var2) {
         this.Ы_388008bb = var2.getMessage();
      }
   }

   @Override
   public void onClose() {
      this.cancelDrag();
      super.onClose();
   }

   @Override
   protected void init() {
      this.cancelDrag();
      this.refreshEntries();
      this.layout();
   }

   private static boolean hovered(Vector2f var0, float var1, float var2, float var3, float var4) {
      return var0.x >= var1 && var0.x < var1 + var3 && var0.y >= var2 && var0.y < var2 + var4;
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (var1.button() == 0 && this.В_461faf9f != null) {
         Vector2f var6 = this.mouse(var1.x(), var1.y());
         var6.y = var6.y - ХВа;
         if (var6.distanceSquared(this.я, this.сс) >= ХВЮ) {
            this.field_5 = true;
         }

         return true;
      } else {
         return super.mouseDragged(var1, var2, var4);
      }
   }

   private static float equipmentX(int var0) {
      return var0 == 4 ? ХВ9 : ХВо + var0 * ХВO;
   }

   private Vector2f mouse(double var1, double var3) {
      Vector2f var5 = WorldProjection.guiToScreen(var1, var3);
      return new Vector2f((var5.x - this.西) / this.Б, (var5.y - this.д) / this.Б);
   }

   private void button(Vector2f var1, float var2, float var3, float var4, float var5, String var6, boolean var7, boolean var8) {
      boolean var9 = var8 && hovered(var1, var2, var3, var4, var5);
      int var10 = var7 && var8 ? (var9 ? ColorMath.blendArgb(this.弟_a83dd0cc, -1, Яяу) : this.弟_a83dd0cc) : (var9 ? Яя2 : Яяф);
      this.rect(var2, var3, var4, var5, Яяи, var10);
      if (!var7) {
         this.outline(var2, var3, var4, var5, Яя9, var9 ? tint(this.弟_a83dd0cc, 140) : Яяо);
      }

      this.centered(var6, var2, var3 + (var5 - ЯяO) * Яя7 - 1.0F, var4, 12, !var8 ? ЯяР : (var7 ? Яя3 : Яяр));
   }

   private void filterEntries() {
      String var1 = this.l_388008bb.toLowerCase(Locale.ROOT).replace('ё', 'е').strip();
      this.Х = this.е
         .stream()
         .filter(
            var1x -> var1x.label().toLowerCase(Locale.ROOT).replace('ё', 'е').contains(var1)
               || ClientLocalization.translate(var1x.label()).toLowerCase(Locale.ROOT).replace('ё', 'е').contains(var1)
         )
         .toList();
      this.С = this.з = 0.0F;
   }

   private void drawPicker(Vector2f var1) {
      this.rect(Яз7, ЯзР, Яз3, Язр, Яз6, Яз_);
      this.section(ЯзШ, ClientLocalization.translate(Язы), Язй, ЯзЖ);
      this.rect(Язм, ЯзК, ЯзI, ЯзТ, ЯзО, ЯзФ);
      this.outline(ЯзЙ, Язк, Язг, Яз0, Язх, this.l_bbeebd87 ? this.弟_a83dd0cc : ЯзУ);
      if (this.l_bbeebd87 && this.field_8_bbeebd87) {
         this.rect(ЯзЗ, ЯзЕ, ЯзП, Яз西, 2.0F, tint(this.弟_a83dd0cc, 60));
      }

      this.text(
         ClientFonts.getRegularFont(), this.l_388008bb.isEmpty() ? ClientLocalization.translate(Язд) : fit(this.l_388008bb, ЯзБ, 11), ЯзС, Язз, 11, this.l_388008bb.isEmpty() ? Язц : Язя
      );
      if (!this.l_388008bb.isEmpty()) {
         this.text(ClientFonts.getMediumFont(), Яцс, ЯцВ, ЯцЯ, 13, ЯцХ);
      }

      UiDrawing.pushScissor(this.px(Яце), this.py(ЯцА), Яц4 * this.Б, Яцп * this.Б);

      try {
         for (int var2 = 0; var2 < this.Х.size(); var2++) {
            float var3 = ЯцД + var2 % 6 * ЯцЪ;
            float var4 = Яцт + var2 / 6 * Яца - this.С;
            if (!(var4 + ЯцЮ <= ЯцЧ) && !(var4 >= ЯцЛ)) {
               InventorySort.CatalogItem var5 = this.Х.get(var2);
               boolean var6 = hovered(var1, Яц诶, Яц1, ЯцЬ, Яц衣) && hovered(var1, var3, var4, Яцш, ЯцИ);
               InventorySort.SlotRule var7 = this.В_d5f06454.Э_d8cfd864()[this.ж_a83dd0cc];
               boolean var8 = var7 != null && var7.kind() == var5.kind() && var7.name().equals(var5.name());
               this.rect(var3, var4, Яць, Яцж, Яц弟, var6 ? Яцб : (var8 ? tint(this.弟_a83dd0cc, 28) : ЯцЫ));
               if (var6 || var8) {
                  this.outline(var3, var4, Яцl, Яц8, Яц5, var6 ? this.弟_a83dd0cc : tint(this.弟_a83dd0cc, 110));
               }

               this.stack(var5.stack(), var3 + Яцв, var4 + Яцъ, ЯцМ);
               this.drawEntryLabel(ClientLocalization.translate(var5.label()), var3, var4);
               if (var6) {
                  this.ж_388008bb = ClientLocalization.translate(var5.label());
                  this.弟_388008bb = ClientLocalization.translate(ЯцН);
               }
            }
         }

         if (this.Х.isEmpty()) {
            this.centered(ClientLocalization.translate(Яцн), ЯцЦ, Яцч, ЯцЭ, 13, Яц必);
         }
      } finally {
         UiDrawing.popScissor();
      }

      if (this.maxScroll() > 0.0F) {
         float var12 = Яцщ / (ЯцГ + this.maxScroll());
         this.rect(Яцл, Яцэ, Яцю, ЯцЩ, Яцу, Яц2);
         this.rect(Яцф, Яци + this.С / this.maxScroll() * (Яц9 - var12), Яцо, var12, ЯцO, this.弟_a83dd0cc);
      }
   }

   private void drawPresetList(Vector2f var1) {
      List var2 = this.В_d5f06454.method_3();
      int var3 = Math.min(6, var2.size());
      this.б_a83dd0cc = Math.clamp(this.б_a83dd0cc, 0, Math.max(0, var2.size() - var3));
      this.rect(ЯБЕ, ЯБП, ЯБ西, var3 * 34 + 12, ЯБд, ЯББ);
      this.outline(ЯБС, ЯБз, ЯБц, var3 * 34 + 12, ЯБя, ЯСс);

      for (int var4 = 0; var4 < var3; var4++) {
         String var5 = (String)var2.get(this.б_a83dd0cc + var4);
         float var6 = 146 + var4 * 34;
         boolean var7 = var5.equals(this.В_d5f06454.иБ());
         if (var7 || hovered(var1, ЯСВ, var6, ЯСЯ, ЯСХ)) {
            this.rect(ЯСе, var6, ЯСА, ЯС4, ЯСп, var7 ? tint(this.弟_a83dd0cc, 40) : ЯСД);
         }

         this.text(ClientFonts.getMediumFont(), fit(var5, ЯСЪ, 12), ЯСт, var6 + ЯСа, 12, var7 ? this.弟_a83dd0cc : ЯСЮ);
         if (var7) {
            this.rect(ЯСЧ, var6 + ЯСЛ, ЯС诶, ЯС1, ЯСЬ, this.弟_a83dd0cc);
         }
      }

      if (var2.size() > var3) {
         float var8 = var3 * 34 - 4;
         float var9 = var8 * var3 / var2.size();
         this.rect(ЯС衣, ЯСш + (var8 - var9) * this.б_a83dd0cc / (var2.size() - var3), 2.0F, var9, 1.0F, this.弟_a83dd0cc);
      }
   }

   private void drawDialog(Vector2f var1) {
      float var2 = ЯСИ;
      this.rect(0.0F, 0.0F, ЯСь, ЯСж, ЯС弟, ЯСб);
      this.rect(ЯСЫ, var2, ЯСl, ЯС8, ЯС5, ЯСв);
      this.outline(ЯСъ, var2, ЯСМ, ЯСН, ЯСн, ЯСЦ);

      String var3 = switch (this.В_7c835ec4) {
         case CREATE -> ClientLocalization.translate(ЯСч);
         case RENAME -> ClientLocalization.translate(ЯСЭ);
         default -> ClientLocalization.translate(ЯС必);
      };
      this.text(ClientFonts.getMediumFont(), var3, ЯСщ, var2 + ЯСГ, 19, ЯСл);
      this.text(
         ClientFonts.getRegularFont(),
         this.В_7c835ec4 == InventoryPresetScreen.DialogKind.CREATE
            ? ClientLocalization.translate(ЯСэ)
            : (this.В_7c835ec4 == InventoryPresetScreen.DialogKind.RENAME ? ClientLocalization.translate(ЯСю) : ClientLocalization.translate(ЯСЩ)),
         ЯСу,
         var2 + ЯС2,
         12,
         ЯСф
      );
      if (this.В_7c835ec4 == InventoryPresetScreen.DialogKind.DELETE) {
         this.text(ClientFonts.getMediumFont(), "«" + fit(this.В_d5f06454.иБ(), ЯСи, 15) + "»", ЯС9, var2 + ЯСо, 15, ЯСO);
      } else {
         this.rect(ЯС7, var2 + ЯСР, ЯС3, ЯСр, ЯС6, ЯС_);
         this.outline(ЯСШ, var2 + ЯСы, ЯСй, ЯСЖ, ЯСм, this.弟_a83dd0cc);
         int var4 = 0;

         while (var4 < this.Ы_a83dd0cc && textWidth(ClientFonts.getRegularFont(), this.б_388008bb.substring(var4, this.Ы_a83dd0cc), 13) > ЯСК) {
            var4 = this.б_388008bb.offsetByCodePoints(var4, 1);
         }

         UiDrawing.pushScissor(this.px(ЯСI), this.py(var2 + ЯСТ), ЯСО * this.Б, ЯСФ * this.Б);

         try {
            int var5 = Math.max(var4, Math.min(this.Ы_a83dd0cc, this.l_a83dd0cc));
            int var6 = Math.max(var4, Math.max(this.Ы_a83dd0cc, this.l_a83dd0cc));
            float var7 = ЯСЙ + textWidth(ClientFonts.getRegularFont(), this.б_388008bb.substring(var4, var5), 13);
            if (var5 != var6) {
               this.rect(var7, var2 + ЯСк, textWidth(ClientFonts.getRegularFont(), this.б_388008bb.substring(var5, var6), 13), ЯСг, 2.0F, tint(this.弟_a83dd0cc, 75));
            }

            this.text(
               ClientFonts.getRegularFont(),
               this.б_388008bb.isEmpty() ? ClientLocalization.translate(ЯС0) : this.б_388008bb.substring(var4),
               ЯСх,
               var2 + ЯСУ,
               13,
               this.б_388008bb.isEmpty() ? ЯСЗ : ЯСЕ
            );
            if (System.currentTimeMillis() / ЯСП % ЯС西 == 0L) {
               this.rect(
                  ЯСд + textWidth(ClientFonts.getRegularFont(), this.б_388008bb.substring(var4, this.Ы_a83dd0cc), 13), var2 + ЯСБ, 1.0F, ЯСС, 0.0F, this.弟_a83dd0cc
               );
            }
         } finally {
            UiDrawing.popScissor();
         }
      }

      this.text(ClientFonts.getRegularFont(), ClientLocalization.translate(this.Ы_388008bb), ЯСз, var2 + ЯСц, 11, -20310);
      this.button(var1, ЯСя, var2 + Язс, ЯзВ, ЯзЯ, ClientLocalization.translate(ЯзХ), false, true);
      this.button(var1, Язе, var2 + ЯзА, Яз4, Язп, this.В_7c835ec4 == InventoryPresetScreen.DialogKind.DELETE ? ClientLocalization.translate(ЯзД) : ClientLocalization.translate(ЯзЪ), true, true);
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.В_7c835ec4 != InventoryPresetScreen.DialogKind.NONE || this.В_461faf9f != null) {
         return true;
      }

      if (var7 == 0.0) {
         return false;
      }

      Vector2f var9 = this.mouse(var1, var3);
      if (this.Ы_bbeebd87) {
         this.б_a83dd0cc = Math.clamp(this.б_a83dd0cc + (var7 < 0.0 ? 1 : -1), 0, Math.max(0, this.В_d5f06454.method_3().size() - 6));
         return true;
      }

      var9.y = var9.y - ХВ诶;
      int var10 = this.inventorySlot(var9);
      if (var10 >= 0) {
         this.ж_a83dd0cc = var10;
         this.В_d5f06454.Н(var10, var7 > 0.0 ? 1 : -1);
      } else if (hovered(var9, ХВ1, ХВЬ, ХВ衣, ХВш)) {
         this.з = Math.clamp(this.з - (float)var7 * ХВИ, 0.0F, this.maxScroll());
      }

      return true;
   }

   private void drawEntryLabel(String var1, float var2, float var3) {
      float var4 = Яц7;
      if (!(textWidth(ClientFonts.getMediumFont(), var1, 10) <= var4) && var1.contains(ЯцР)) {
         int var5 = var1.indexOf(32);

         for (int var6 = var1.indexOf(32, var5 + 1);
            var6 >= 0 && !(textWidth(ClientFonts.getMediumFont(), var1.substring(0, var6), 9) > var4);
            var6 = var1.indexOf(32, var6 + 1)
         ) {
            var5 = var6;
         }

         this.centered(fit(var1.substring(0, var5), var4, 9), var2 + Яц_, var3 + ЯцШ, var4, 9, Яцы);
         this.centered(fit(var1.substring(var5 + 1), var4, 9), var2 + Яцй, var3 + ЯцЖ, var4, 9, Яцм);
      } else {
         this.centered(fit(var1, var4, 10), var2 + Яц3, var3 + Яцр, var4, 10, Яц6);
      }
   }

   private float px(float var1) {
      return this.西 + var1 * this.Б;
   }

   private void drawTooltip(Vector2f var1) {
      if (this.ж_388008bb != null) {
         String var2 = fit(this.ж_388008bb, Яя6, 12);
         String var3 = fit(this.弟_388008bb == null ? "" : this.弟_388008bb, Яя_, 10);
         float var4 = Math.max(textWidth(ClientFonts.getMediumFont(), var2, 12), textWidth(ClientFonts.getMediumFont(), var3, 10)) + ЯяШ;
         float var5 = Math.clamp(var1.x + Яяы, Яяй, ЯяЖ - var4 - Яям);
         float var6 = Math.clamp(var1.y + ЯяК, ЯяI, ЯяТ);
         this.rect(var5, var6, var4, ЯяО, ЯяФ, ЯяЙ);
         this.outline(var5, var6, var4, Яяк, Яяг, Яя0);
         this.text(ClientFonts.getMediumFont(), var2, var5 + Яях, var6 + ЯяУ, 12, ЯяЗ);
         this.text(ClientFonts.getMediumFont(), var3, var5 + ЯяЕ, var6 + ЯяП, 10, Яя西);
      }
   }

   private int inventorySlot(Vector2f var1) {
      for (int var2 = 0; var2 < В_28f5b604.length; var2++) {
         if (hovered(var1, equipmentX(var2), ХВь, ХВж, ХВ弟)) {
            return В_28f5b604[var2];
         }
      }

      for (int var3 = 0; var3 < 27; var3++) {
         if (hovered(var1, ХВб + var3 % 9 * ХВЫ, ХВl + var3 / 9 * ХВ8, ХВ5, ХВв)) {
            return var3 + 9;
         }
      }

      for (int var4 = 0; var4 < 9; var4++) {
         if (hovered(var1, ХВъ + var4 * ХВМ, ХВН, ХВн, ХВЦ)) {
            return var4;
         }
      }

      return -1;
   }

   private boolean dialogKey(KeyEvent var1) {
      int var2 = var1.key();
      if (var2 == 256) {
         this.В_7c835ec4 = InventoryPresetScreen.DialogKind.NONE;
         return true;
      }

      if (var2 == 257 || var2 == 335) {
         this.confirmDialog();
         return true;
      }

      if (this.В_7c835ec4 == InventoryPresetScreen.DialogKind.DELETE) {
         return true;
      }

      boolean var3 = (var1.modifiers() & 2) != 0;
      boolean var4 = (var1.modifiers() & 1) != 0;
      if (var3 && var2 == 65) {
         this.l_a83dd0cc = 0;
         this.Ы_a83dd0cc = this.б_388008bb.length();
      } else if (var3 && var2 == 86) {
         this.insertName(this.minecraft.keyboardHandler.getClipboard());
      } else if (!var3 || var2 != 67 && var2 != 88) {
         if (var2 != 259 && var2 != 261) {
            if (var2 == 268 || var2 == 269 || var2 == 263 || var2 == 262) {
               if (var2 == 268) {
                  this.Ы_a83dd0cc = 0;
               } else if (var2 == 269) {
                  this.Ы_a83dd0cc = this.б_388008bb.length();
               } else if (!var4 && this.Ы_a83dd0cc != this.l_a83dd0cc) {
                  this.Ы_a83dd0cc = var2 == 263 ? Math.min(this.Ы_a83dd0cc, this.l_a83dd0cc) : Math.max(this.Ы_a83dd0cc, this.l_a83dd0cc);
               } else if (var2 == 263 && this.Ы_a83dd0cc > 0) {
                  this.Ы_a83dd0cc = this.б_388008bb.offsetByCodePoints(this.Ы_a83dd0cc, -1);
               } else if (var2 == 262 && this.Ы_a83dd0cc < this.б_388008bb.length()) {
                  this.Ы_a83dd0cc = this.б_388008bb.offsetByCodePoints(this.Ы_a83dd0cc, 1);
               }

               if (!var4) {
                  this.l_a83dd0cc = this.Ы_a83dd0cc;
               }
            }
         } else {
            if (this.Ы_a83dd0cc == this.l_a83dd0cc) {
               if (var2 == 259 && this.Ы_a83dd0cc > 0) {
                  this.l_a83dd0cc = this.б_388008bb.offsetByCodePoints(this.Ы_a83dd0cc, -1);
               }

               if (var2 == 261 && this.Ы_a83dd0cc < this.б_388008bb.length()) {
                  this.l_a83dd0cc = this.б_388008bb.offsetByCodePoints(this.Ы_a83dd0cc, 1);
               }
            }

            this.insertName("");
         }
      } else if (this.Ы_a83dd0cc != this.l_a83dd0cc) {
         this.minecraft
            .keyboardHandler
            .setClipboard(this.б_388008bb.substring(Math.min(this.Ы_a83dd0cc, this.l_a83dd0cc), Math.max(this.Ы_a83dd0cc, this.l_a83dd0cc)));
         if (var2 == 88) {
            this.insertName("");
         }
      }

      return true;
   }

   private static String slotName(int var0) {
      return switch (var0) {
         case 36 -> ClientLocalization.translate(ХВр);
         case 37 -> ClientLocalization.translate(ХВ3);
         case 38 -> ClientLocalization.translate(ХВР);
         case 39 -> ClientLocalization.translate(ХВ7);
         case 40 -> ClientLocalization.translate(ХВ6);
         default -> var0 < 9 ? ClientLocalization.translate(ХВ_) + (var0 + 1) : ClientLocalization.translate(ХВШ) + (var0 - 8);
      };
   }

   private void stack(ItemStack var1, float var2, float var3, float var4) {
      UiDrawing.drawItem(var1, this.px(var2), this.py(var3), var4 / ХВй * this.Б, false);
   }

   private float maxScroll() {
      int var1 = (this.Х.size() + 6 - 1) / 6;
      return Math.max(0.0F, var1 * ХВ2 - ХВф - ХВи);
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.В_7c835ec4 == InventoryPresetScreen.DialogKind.CREATE || this.В_7c835ec4 == InventoryPresetScreen.DialogKind.RENAME) {
         this.insertName(var1.codepointAsString());
         return true;
      } else if (this.В_7c835ec4 == InventoryPresetScreen.DialogKind.NONE && this.l_bbeebd87 && !this.Ы_bbeebd87) {
         this.insertSearch(var1.codepointAsString());
         return true;
      } else {
         return this.В_7c835ec4 != InventoryPresetScreen.DialogKind.NONE || super.charTyped(var1);
      }
   }

   private static String fit(String var0, float var1, int var2) {
      if (textWidth(ClientFonts.getMediumFont(), var0, var2) <= var1) {
         return var0;
      }

      int var3 = var0.length();

      while (var3 > 0 && textWidth(ClientFonts.getMediumFont(), var0.substring(0, var3) + "…", var2) > var1) {
         var3--;
      }

      return var0.substring(0, var3) + "…";
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      Vector2f var3 = this.mouse(var1.x(), var1.y());
      if (this.В_461faf9f != null && var1.button() != 0) {
         this.cancelDrag();
         return true;
      }

      if (this.В_7c835ec4 != InventoryPresetScreen.DialogKind.NONE) {
         float var8 = Яяд;
         if (var1.button() == 0) {
            if (hovered(var3, ЯяБ, var8 + ЯяС, Яяз, Яяц)) {
               this.В_7c835ec4 = InventoryPresetScreen.DialogKind.NONE;
            } else if (hovered(var3, Яяя, var8 + Хсс, ХсВ, ХсЯ)) {
               this.confirmDialog();
            } else if (this.В_7c835ec4 != InventoryPresetScreen.DialogKind.DELETE && hovered(var3, ХсХ, var8 + Хсе, ХсА, Хс4)) {
               this.Ы_a83dd0cc = this.б_388008bb.length();
               this.l_a83dd0cc = var2 ? 0 : this.Ы_a83dd0cc;
            }
         }

         return true;
      } else if (this.Ы_bbeebd87) {
         if (var1.button() == 0 && hovered(var3, Хсп, ХсД, ХсЪ, Math.min(6, this.В_d5f06454.method_3().size()) * 34)) {
            int var7 = this.б_a83dd0cc + (int)((var3.y - Хст) / Хса);
            this.В_d5f06454.И(this.В_d5f06454.method_3().get(var7));
            this.l_388008bb = "";
            this.refreshEntries();
         }

         this.Ы_bbeebd87 = false;
         return true;
      } else {
         if (var1.button() == 0) {
            this.cancelDrag();
            this.l_bbeebd87 = false;
            if (hovered(var3, ХсЮ, ХсЧ, ХсЛ, Хс诶)) {
               this.onClose();
               return true;
            }

            if (hovered(var3, Хс1, ХсЬ, Хс衣, Хсш)) {
               this.selectMode(InventoryPresetStore.ServerMode.REALLY_WORLD);
               return true;
            }

            if (hovered(var3, ХсИ, Хсь, Хсж, Хс弟)) {
               this.selectMode(InventoryPresetStore.ServerMode.FUNTIME);
               return true;
            }

            if (hovered(var3, Хсб, ХсЫ, Хсl, Хс8)) {
               this.Ы_bbeebd87 = true;
               this.б_a83dd0cc = Math.clamp(this.В_d5f06454.method_3().indexOf(this.В_d5f06454.иБ()), 0, Math.max(0, this.В_d5f06454.method_3().size() - 6));
               return true;
            }

            if (hovered(var3, Хс5, Хсв, Хсъ, ХсМ)) {
               this.openDialog(InventoryPresetScreen.DialogKind.CREATE);
               return true;
            }

            if (hovered(var3, ХсН, Хсн, ХсЦ, Хсч)) {
               this.openDialog(InventoryPresetScreen.DialogKind.RENAME);
               return true;
            }

            if (hovered(var3, ХсЭ, Хс必, Хсщ, ХсГ) && this.В_d5f06454.method_3().size() > 1) {
               this.openDialog(InventoryPresetScreen.DialogKind.DELETE);
               return true;
            }

            if (hovered(var3, Хсл, Хсэ, Хсю, ХсЩ) && !this.В_d5f06454.иЖ()) {
               this.В_d5f06454.Эl();
               return true;
            }
         }

         var3.y = var3.y - Хсу;
         if (var1.button() == 0 && hovered(var3, Хс2, Хсф, Хси, Хс9)) {
            this.l_bbeebd87 = true;
            this.field_8_bbeebd87 = var2;
            if (hovered(var3, Хсо, ХсO, Хс7, ХсР)) {
               this.l_388008bb = "";
               this.filterEntries();
            }

            return true;
         } else {
            int var4 = this.inventorySlot(var3);
            if (var4 < 0 || var1.button() != 0 && var1.button() != 1) {
               if (var1.button() != 0) {
                  return super.mouseClicked(var1, var2);
               }

               int var5 = this.pickerEntry(var3);
               if (var5 >= 0) {
                  InventorySort.CatalogItem var6 = this.Х.get(var5);
                  this.beginDrag(var3, -1, var6, new InventorySort.SlotRule(var6.stack(), var6.name(), 1, var6.kind()));
               } else if (hovered(var3, Хс3, Хср, Хс6, Хс_)) {
                  this.В_d5f06454.Эо();
                  this.refreshEntries();
               } else if (hovered(var3, ХсШ, Хсы, Хсй, ХсЖ)) {
                  this.В_d5f06454.Эф();
               } else if (hovered(var3, Хсм, ХсК, ХсI, ХсТ)) {
                  this.В_d5f06454.ЭЯ();
               } else if (hovered(var3, ХсО, ХсФ, ХсЙ, Хск)) {
                  this.В_d5f06454.Н(this.ж_a83dd0cc, -1);
               } else if (hovered(var3, Хсг, Хс0, Хсх, ХсУ)) {
                  this.В_d5f06454.Н(this.ж_a83dd0cc, 1);
               } else if (hovered(var3, ХсЗ, ХсЕ, ХсП, Хс西) && this.В_d5f06454.иэ()) {
                  this.В_d5f06454.ЭГ();
                  this.onClose();
               }

               return true;
            } else {
               this.ж_a83dd0cc = var4;
               if (var1.button() == 1) {
                  this.В_d5f06454.Д(var4);
               } else if (this.В_d5f06454.Э_d8cfd864()[var4] != null) {
                  this.beginDrag(var3, var4, null, this.В_d5f06454.Э_d8cfd864()[var4]);
               }

               return true;
            }
         }
      }
   }

   private void drawPresetBar(Vector2f var1) {
      this.button(var1, ЯБ8, ЯБ5, ЯБв, ЯБъ, "", false, true);
      this.text(ClientFonts.getMediumFont(), fit(this.В_d5f06454.иБ(), ЯБМ, 13), ЯБН, ЯБн, 13, ЯБЦ);
      this.text(ClientFonts.getMediumFont(), this.Ы_bbeebd87 ? ЯБч : ЯБЭ, ЯБ必, ЯБщ, 13, this.弟_a83dd0cc);
      this.button(var1, ЯБГ, ЯБл, ЯБэ, ЯБю, ClientLocalization.translate(ЯБЩ), false, true);
      this.button(var1, ЯБу, ЯБ2, ЯБф, ЯБи, ClientLocalization.translate(ЯБ9), false, true);
      this.button(var1, ЯБо, ЯБO, ЯБ7, ЯБР, ClientLocalization.translate(ЯБ3), false, this.В_d5f06454.method_3().size() > 1);
      if (this.В_d5f06454.иЖ()) {
         this.rect(ЯБр, ЯБ6, ЯБ_, ЯБШ, ЯБы, ЯБй);
         this.text(ClientFonts.getMediumFont(), ClientLocalization.translate(ЯБЖ), ЯБм, ЯБК, 12, ЯБI);
      } else {
         this.button(var1, ЯБТ, ЯБО, ЯБФ, ЯБЙ, ClientLocalization.translate(ЯБк), false, true);
      }

      this.text(ClientFonts.getRegularFont(), this.В_d5f06454.иЖ() ? ClientLocalization.translate(ЯБг) : ClientLocalization.translate(ЯБ0), ЯБх, ЯБУ, 10, this.В_d5f06454.иЖ() ? ЯБЗ : -20310);
   }

   private void insertSearch(String var1) {
      if (this.field_8_bbeebd87) {
         this.l_388008bb = "";
      }

      this.field_8_bbeebd87 = false;
      StringBuilder var2 = new StringBuilder(this.l_388008bb);
      var1.codePoints()
         .filter(var0 -> !Character.isISOControl(var0) && var0 != 167)
         .limit(Math.max(0, 40 - this.l_388008bb.codePointCount(0, this.l_388008bb.length())))
         .forEach(var2::appendCodePoint);
      this.l_388008bb = var2.toString();
      this.filterEntries();
   }

   private void drawFooter(Vector2f var1) {
      this.rect(ЯяД, ЯяЪ, Яят, 1.0F, 0.0F, Яяа);
      this.button(var1, ЯяЮ, ЯяЧ, ЯяЛ, Яя诶, ClientLocalization.translate(Яя1), false, true);
      this.button(var1, ЯяЬ, Яя衣, Яяш, ЯяИ, ClientLocalization.translate(Яяь), false, this.В_d5f06454.иэ());
      this.button(var1, Яяж, Яя弟, Яяб, ЯяЫ, ClientLocalization.translate(Яяl), false, this.В_d5f06454.и7());
      this.text(ClientFonts.getRegularFont(), ClientLocalization.translate(Яя8), Яя5, Яяв, 11, Яяъ);
      this.button(var1, ЯяМ, ЯяН, Яян, ЯяЦ, ClientLocalization.translate(Яяч), true, this.В_d5f06454.иэ());
   }

   private void rect(float var1, float var2, float var3, float var4, float var5, int var6) {
      UiDrawing.drawRoundedRect(this.px(var1), this.py(var2), var3 * this.Б, var4 * this.Б, var5 * this.Б, var6);
   }

   private void text(FontRenderer var1, String var2, float var3, float var4, int var5, int var6) {
      UiDrawing.drawText(var1, var2, this.px(var3), this.py(var4), var5 * this.Б, var6, 0.0F, ХВЖ, 1.0F);
   }

   private void drawInventory(Vector2f var1) {
      this.rect(Яд6, Яд_, ЯдШ, Яды, Ядй, ЯдЖ);
      this.section(Ядм, ClientLocalization.translate(ЯдК), ЯдI, ЯдТ);
      this.text(ClientFonts.getRegularFont(), ClientLocalization.translate(ЯдО), ЯдФ, ЯдЙ, 10, Ядк);

      for (int var2 = 0; var2 < В_28f5b604.length; var2++) {
         float var3 = equipmentX(var2);
         this.drawSlot(var1, В_28f5b604[var2], var3, Ядг);
         this.centered(ClientLocalization.translate(Я[var2]), var3 - Яд0, Ядх, ЯдУ, 9, ЯдЗ);
      }

      this.text(ClientFonts.getRegularFont(), ClientLocalization.translate(ЯдЕ), ЯдП, Яд西, 10, Ядд);

      for (int var4 = 0; var4 < 27; var4++) {
         this.drawSlot(var1, var4 + 9, ЯдБ + var4 % 9 * ЯдС, Ядз + var4 / 9 * Ядц);
      }

      this.text(ClientFonts.getRegularFont(), ClientLocalization.translate(Ядя), ЯБс, ЯБВ, 10, ЯБЯ);

      for (int var5 = 0; var5 < 9; var5++) {
         this.drawSlot(var1, var5, ЯБХ + var5 * ЯБе, ЯБА);
         this.centered(String.valueOf(var5 + 1), ЯБ4 + var5 * ЯБп, ЯБД, ЯБЪ, 10, this.ж_a83dd0cc == var5 ? this.弟_a83dd0cc : ЯБт);
      }

      this.text(ClientFonts.getRegularFont(), ClientLocalization.translate(ЯБа), ЯБЮ, ЯБЧ, 11, ЯБЛ);
      this.text(ClientFonts.getRegularFont(), ClientLocalization.translate(ЯБ诶), ЯБ1, ЯБЬ, 10, ЯБ衣);
   }

   private static int tint(int var0, int var1) {
      return var0 & ХВы | var1 << 24;
   }

   private void selectMode(InventoryPresetStore.ServerMode var1) {
      this.cancelDrag();
      this.l_388008bb = "";
      this.l_bbeebd87 = this.field_8_bbeebd87 = false;
      this.В_d5f06454.Э(var1);
      this.refreshEntries();
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.В_7c835ec4 != InventoryPresetScreen.DialogKind.NONE) {
         return this.dialogKey(var1);
      }

      if (this.В_461faf9f != null && var1.key() == 256) {
         this.cancelDrag();
         return true;
      }

      if (this.Ы_bbeebd87) {
         if (var1.key() == 256) {
            this.Ы_bbeebd87 = false;
         }

         return true;
      } else {
         boolean var2 = (var1.modifiers() & 2) != 0;
         if (!var2 || var1.key() != 90 && var1.key() != 89) {
            if (!this.l_bbeebd87) {
               if (var1.key() != 261 && var1.key() != 259) {
                  return super.keyPressed(var1);
               }

               this.В_d5f06454.Д(this.ж_a83dd0cc);
               return true;
            } else {
               if (var1.key() == 256 || var1.key() == 257) {
                  this.l_bbeebd87 = false;
               } else if (var2 && var1.key() == 65) {
                  this.field_8_bbeebd87 = true;
               } else if (var2 && var1.key() == 86) {
                  this.insertSearch(this.minecraft.keyboardHandler.getClipboard());
               } else if (var1.key() == 259 || var1.key() == 261) {
                  if (this.field_8_bbeebd87) {
                     this.l_388008bb = "";
                  } else if (!this.l_388008bb.isEmpty()) {
                     this.l_388008bb = this.l_388008bb.substring(0, this.l_388008bb.offsetByCodePoints(this.l_388008bb.length(), -1));
                  }

                  this.field_8_bbeebd87 = false;
                  this.filterEntries();
               }

               return true;
            }
         } else {
            this.cancelDrag();
            if (var1.key() != 89 && (var1.modifiers() & 1) == 0) {
               this.В_d5f06454.ЭЯ();
            } else {
               this.В_d5f06454.ЭI();
            }

            return true;
         }
      }
   }

   @Override
   public void clearFocus() {
   }

   private boolean validDrop(int var1) {
      return this.В_461faf9f != null
         && (this.field_8_a83dd0cc >= 0 ? this.В_d5f06454.Э_bbeebd87(this.field_8_a83dd0cc, var1) : this.В_d5f06454.Э(var1, this.В_461faf9f.stack()));
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      if (var1.button() == 0 && this.В_461faf9f != null) {
         Vector2f var2 = this.mouse(var1.x(), var1.y());
         var2.y = var2.y - ХВЧ;
         this.field_5 = this.field_5 | var2.distanceSquared(this.я, this.сс) >= ХВЛ;
         int var3 = this.inventorySlot(var2);
         if (this.field_5 && this.validDrop(var3)) {
            if (this.field_8_a83dd0cc >= 0) {
               this.В_d5f06454.method_3(this.field_8_a83dd0cc, var3);
            } else {
               this.В_d5f06454.Э(var3, this.В_5c154634.stack(), this.В_5c154634.name(), this.В_5c154634.kind());
            }

            this.ж_a83dd0cc = var3;
         } else if (!this.field_5 && this.В_5c154634 != null) {
            int var4 = this.pickerEntry(var2);
            if (var4 >= 0 && this.Х.get(var4) == this.В_5c154634) {
               this.В_d5f06454.Э(this.ж_a83dd0cc, this.В_5c154634.stack(), this.В_5c154634.name(), this.В_5c154634.kind());
            }
         }

         this.cancelDrag();
         return true;
      } else {
         return super.mouseReleased(var1);
      }
   }

   private void insertName(String var1) {
      int var2 = Math.min(this.Ы_a83dd0cc, this.l_a83dd0cc);
      int var3 = Math.max(this.Ы_a83dd0cc, this.l_a83dd0cc);
      int var4 = 32 - this.б_388008bb.codePointCount(0, var2) - this.б_388008bb.codePointCount(var3, this.б_388008bb.length());
      StringBuilder var5 = new StringBuilder();
      var1.codePoints().filter(var0 -> !Character.isISOControl(var0) && var0 != 167).limit(Math.max(0, var4)).forEach(var5::appendCodePoint);
      this.б_388008bb = this.б_388008bb.substring(0, var2) + var5 + this.б_388008bb.substring(var3);
      this.Ы_a83dd0cc = var2 + var5.length();
      this.l_a83dd0cc = this.Ы_a83dd0cc;
      this.Ы_388008bb = "";
   }

   private int pickerEntry(Vector2f var1) {
      if (!hovered(var1, ХВч, ХВЭ, ХВ必, ХВщ)) {
         return -1;
      }

      for (int var2 = 0; var2 < this.Х.size(); var2++) {
         if (hovered(var1, ХВГ + var2 % 6 * ХВл, ХВэ + var2 / 6 * ХВю - this.С, ХВЩ, ХВу)) {
            return var2;
         }
      }

      return -1;
   }

   private void outline(float var1, float var2, float var3, float var4, float var5, int var6) {
      UiDrawing.drawOutline(this.px(var1), this.py(var2), var3 * this.Б, var4 * this.Б, this.Б, var5 * this.Б, var6);
   }

   private static float textWidth(FontRenderer var0, String var1, int var2) {
      return UiDrawing.getTextWidth(var0, var1, var2);
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   private void cancelDrag() {
      this.field_8_a83dd0cc = -1;
      this.В_5c154634 = null;
      this.В_461faf9f = null;
      this.field_5 = false;
      this.setDragging(false);
   }

   private void beginDrag(Vector2f var1, int var2, InventorySort.CatalogItem var3, InventorySort.SlotRule var4) {
      this.field_8_a83dd0cc = var2;
      this.В_5c154634 = var3;
      this.В_461faf9f = var4;
      this.я = var1.x;
      this.сс = var1.y;
      this.field_5 = false;
      this.setDragging(true);
   }

   @Override
   protected void extractBlurredBackground(GuiGraphicsExtractor var1) {
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.layout();
      UiDrawing.clearTransforms();
      UiDrawing.disableScissor();
      this.诶.updateTarget(1.0F);
      UiDrawing.setGlobalAlpha(Я西Ф * Math.clamp(this.诶.getValue(), 0.0F, 1.0F));

      try {
         Vector2f var5 = this.mouse(var2, var3);
         this.ж_388008bb = null;
         this.弟_388008bb = null;
         this.弟_a83dd0cc = ColorMath.blendArgb(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) | Я西Й, Я西к, Я西г);
         this.з = Math.clamp(this.з, 0.0F, this.maxScroll());
         this.С = this.С + (this.з - this.С) * Я西0;
         UiDrawing.drawRoundedRect(0.0F, 0.0F, this.minecraft.getWindow().getScreenWidth(), this.minecraft.getWindow().getScreenHeight(), 0.0F, Я西х);
         UiDrawing.drawShadow(this.西, this.д, Я西У * this.Б, Я西З * this.Б, Я西Е * this.Б, Я西П);
         UiDrawing.drawBlur(this.西, this.д, Я西西 * this.Б, Я西д * this.Б, Я西Б, Я西С * this.Б, -1);
         this.rect(0.0F, 0.0F, Я西з, Я西ц, Я西я, Ядс);
         this.outline(0.0F, 0.0F, ЯдВ, ЯдЯ, ЯдХ, Яде);
         this.drawHeader(var5);
         this.drawPresetBar(var5);
         Vector2f var6 = this.В_7c835ec4 == InventoryPresetScreen.DialogKind.NONE && !this.Ы_bbeebd87
            ? new Vector2f(var5.x, var5.y - Ядп)
            : new Vector2f(ЯдА, Яд4);
         this.ц = ЯдД;
         this.drawInventory(var6);
         this.drawPicker(var6);
         this.drawSelection(var6);
         this.drawFooter(var6);
         this.ц = 0.0F;
         if (this.field_5) {
            this.drawDrag(var5);
         } else if (this.В_7c835ec4 == InventoryPresetScreen.DialogKind.NONE && !this.Ы_bbeebd87) {
            this.drawTooltip(var5);
         }

         if (this.Ы_bbeebd87) {
            this.drawPresetList(var5);
         }

         if (this.В_7c835ec4 != InventoryPresetScreen.DialogKind.NONE) {
            this.drawDialog(var5);
         }
      } finally {
         this.ц = 0.0F;
         UiDrawing.disableScissor();
         UiDrawing.resetGlobalAlpha();
         UiDrawing.clearTransforms();
      }
   }

   private void refreshEntries() {
      this.е = this.В_d5f06454.Н();
      this.filterEntries();
   }

   private void layout() {
      float var1 = this.minecraft.getWindow().getScreenWidth();
      float var2 = this.minecraft.getWindow().getScreenHeight();
      this.Б = Math.max(Я西Ш, Math.min(1.0F, Math.min((var1 - Я西ы) / Я西й, (var2 - Я西Ж) / Я西м)));
      this.西 = (var1 - Я西К * this.Б) * Я西I;
      this.д = (var2 - Я西Т * this.Б) * Я西О;
   }

   private void drawDrag(Vector2f var1) {
      if (this.В_461faf9f != null) {
         this.rect(var1.x - Хсд, var1.y - ХсБ, ХсС, Хсз, Хсц, Хся);
         this.outline(var1.x - ХВс, var1.y - ХВВ, ХВЯ, ХВХ, ХВе, this.弟_a83dd0cc);
         this.stack(this.В_461faf9f.stack(), var1.x - ХВА, var1.y - ХВ4, ХВп);
         if (this.В_461faf9f.count() > 1) {
            this.text(ClientFonts.getMediumFont(), String.valueOf(this.В_461faf9f.count()), var1.x + ХВД, var1.y + ХВЪ, 10, ХВт);
         }
      }
   }

   private void drawHeader(Vector2f var1) {
      this.rect(ЯдЪ, Ядт, Яда, ЯдЮ, ЯдЧ, tint(this.弟_a83dd0cc, 36));

      for (int var2 = 0; var2 < 9; var2++) {
         this.rect(34 + var2 % 3 * 8, 34 + var2 / 3 * 8, ЯдЛ, Яд诶, Яд1, this.弟_a83dd0cc);
      }

      this.text(ClientFonts.getMediumFont(), ЯдЬ, Яд衣, Ядш, 23, ЯдИ);
      this.text(ClientFonts.getRegularFont(), ClientLocalization.translate(Ядь), Ядж, Яд弟, 12, Ядб);
      this.rect(ЯдЫ, Ядl, Яд8, Яд5, Ядв, Ядъ);
      this.modeButton(var1, ЯдМ, ЯдН, InventoryPresetStore.ServerMode.REALLY_WORLD);
      this.modeButton(var1, Ядн, ЯдЦ, InventoryPresetStore.ServerMode.FUNTIME);
      int var7 = 0;

      for (InventorySort.SlotRule var6 : this.В_d5f06454.Э_d8cfd864()) {
         if (var6 != null && !var6.stack().isEmpty()) {
            var7++;
         }
      }

      this.rect(Ядч, ЯдЭ, Яд必, Ядщ, ЯдГ, Ядл);
      this.centered(var7 + ClientLocalization.translate(Ядэ), Ядю, ЯдЩ, Яду, 12, Яд2);
      this.button(var1, Ядф, Яди, Яд9, Ядо, ЯдO, false, true);
      this.rect(Яд7, ЯдР, Яд3, 1.0F, 0.0F, Ядр);
   }

   private void section(String var1, String var2, float var3, float var4) {
      this.rect(var3, var4, ЯяЭ, Яя必, Яящ, tint(this.弟_a83dd0cc, 28));
      this.centered(var1, var3, var4 + ЯяГ, Яял, 11, this.弟_a83dd0cc);
      this.text(ClientFonts.getMediumFont(), var2, var3 + Яяэ, var4 + Яяю, 16, ЯяЩ);
   }

   private void drawSelection(Vector2f var1) {
      InventorySort.SlotRule var2 = this.В_d5f06454.Э_d8cfd864()[this.ж_a83dd0cc];
      this.rect(ЯцК, ЯцI, ЯцТ, ЯцО, ЯцФ, ЯцЙ);
      this.text(ClientFonts.getMediumFont(), slotName(this.ж_a83dd0cc), Яцк, Яцг, 12, this.弟_a83dd0cc);
      this.text(ClientFonts.getRegularFont(), var2 == null ? ClientLocalization.translate(Яц0) : fit(ClientLocalization.translate(var2.label()), Яцх, 11), ЯцУ, ЯцЗ, 11, var2 == null ? ЯцЕ : ЯцП);
      this.button(var1, Яц西, Яцд, ЯцБ, ЯцС, Яцз, false, var2 != null && var2.count() > 1);
      this.centered(var2 == null ? Яцц : String.valueOf(var2.count()), Яця, Яяс, ЯяВ, 12, ЯяЯ);
      this.button(var1, ЯяХ, Яяе, ЯяА, Яя4, Яяп, false, var2 != null && var2.count() < var2.stack().getMaxStackSize());
   }

   private void centered(String var1, float var2, float var3, float var4, int var5, int var6) {
      this.text(ClientFonts.getMediumFont(), var1, var2 + (var4 - textWidth(ClientFonts.getMediumFont(), var1, var5)) * ХВм, var3, var5, var6);
   }

   private void drawSlot(Vector2f var1, int var2, float var3, float var4) {
      boolean var5 = var2 == this.ж_a83dd0cc;
      boolean var6 = hovered(var1, var3, var4, Язт, Яза);
      this.rect(var3, var4, ЯзЮ, ЯзЧ, ЯзЛ, var5 ? tint(this.弟_a83dd0cc, 42) : (var6 ? Яз诶 : Яз1));
      this.outline(var3, var4, ЯзЬ, Яз衣, Язш, var5 ? this.弟_a83dd0cc : (var6 ? ЯзИ : Язь));
      if (this.field_5 && var6) {
         this.outline(var3, var4, Язж, Яз弟, Язб, this.validDrop(var2) ? ЯзЫ : -29299);
      }

      InventorySort.SlotRule var7 = this.В_d5f06454.Э_d8cfd864()[var2];
      if (var7 != null && !var7.stack().isEmpty()) {
         this.stack(var7.stack(), var3 + Язl, var4 + Яз8, Яз5);
         if (var7.count() > 1) {
            String var8 = String.valueOf(var7.count());
            float var9 = textWidth(ClientFonts.getMediumFont(), var8, 10) + Язв;
            this.rect(var3 + Язъ - var9 - 2.0F, var4 + ЯзМ - ЯзН, var9, Язн, ЯзЦ, Язч);
            this.text(ClientFonts.getMediumFont(), var8, var3 + ЯзЭ - var9 + 1.0F, var4 + Яз必 - Язщ, 10, ЯзГ);
         }
      } else {
         this.centered(var5 ? Язл : Язэ, var3, var4 + Язю, ЯзЩ, 17, var5 ? this.弟_a83dd0cc : Язу);
      }

      if (this.field_5 && this.field_8_a83dd0cc == var2) {
         this.rect(var3 + 1.0F, var4 + 1.0F, Яз2, Язф, Язи, Яз9);
      }

      if (var6) {
         this.ж_388008bb = var7 == null ? slotName(var2) : ClientLocalization.translate(var7.label());
         this.弟_388008bb = var7 == null ? ClientLocalization.translate(Язо) : ClientLocalization.translate(ЯзO);
      }
   }

   private void modeButton(Vector2f var1, float var2, float var3, InventoryPresetStore.ServerMode var4) {
      boolean var5 = this.В_d5f06454.Э_4f9d66f8() == var4;
      if (var5 || hovered(var1, var2, ЯБш, var3, ЯБИ)) {
         this.rect(var2, ЯБь, var3, ЯБж, ЯБ弟, var5 ? tint(this.弟_a83dd0cc, 42) : ЯБб);
      }

      this.centered(var4.щ弟(), var2, ЯБЫ, var3, 12, var5 ? this.弟_a83dd0cc : ЯБl);
   }

   private enum DialogKind {
      NONE,
      CREATE,
      RENAME,
      DELETE;
   }
}
