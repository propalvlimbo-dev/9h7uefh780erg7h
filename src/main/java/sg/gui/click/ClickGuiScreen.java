package sg.gui.click;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.platform.Window;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import settings.render.ClientLocalization;
import sg.ec.MinecraftAccess;
import sg.ec.KeyboardModifiers;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.events.impl.TextSearchMatcher;
import sg.gui.ClickGuiBackground;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.ThemeWave;
import sg.settings.EntityEspSettingsPanel;
import sg.theme.ThemeColorSlot;
import sg.gui.widgets.HoverHint;
import sg.gui.widgets.ModuleCard;
import sg.gui.widgets.ModuleKeybindWidget;
import sg.util.time.ThemeEditor;
import sg.animation.Easings;
import sg.gui.animation.GuiLayoutTransition;
import sg.util.time.ColorMath;
import sg.gui.profile.ProfileSettingsPanel;
import sg.gui.cosmetics.CosmeticsPanel;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.misc.ClientSounds;
import sg.modules.impl.render.ClickGui;
import sg.modules.impl.render.EntityESP;
import sg.events.impl.HudRenderEvent;
import sg.hud.HudElementRegistry;
import sg.settings.ValueSetting;
import sg.gui.autobuy.AutoBuyPanel;
import sg.modules.impl.init.ClickGuiPreferences;
import sg.util.math.EasingFunctions;
import sg.modules.impl.init.TickAnimation;

public class ClickGuiScreen extends Screen implements MinecraftAccess {
   public static final float в_f67ab10a = 800.0F;
   public static final float ъ_f67ab10a = 504.0F;
   private static final float М_f67ab10a = 74.0F;
   private static final float Н_f67ab10a = 18.0F;
   private static final float н = 30.0F;
   private static final float Ц = 12.0F;
   private static final float ч = 380.0F;
   private static final float Э = 32.0F;
   private static final float 必 = 38.0F;
   private static final int Ю_a83dd0cc = 7;
   private static final float щ = 20.0F;
   private static final float Г = 14.0F;
   private static ModuleCategory В_e0c4705a = ModuleCategory.COMBAT;
   private static final EnumMap<ModuleCategory, Float> В_03947583 = new EnumMap<>(ModuleCategory.class);
   @SerializedName("В")
   private final CopyOnWriteArrayList<ModuleKeybindWidget> В_b1d77d5c = new CopyOnWriteArrayList<>();
   @SerializedName("В")
   private final ArrayList<ModuleCard> В_c1edc18e = new ArrayList<>();
   private HoverHint.Content pendingHint;
   @SerializedName("В")
   private final AutoBuyPanel В_ec501b46 = new AutoBuyPanel(this);
   @SerializedName("В")
   private final CosmeticsPanel В_77119740 = new CosmeticsPanel(this);
   @SerializedName("В")
   private final ThemeEditor В_ec58113c = new ThemeEditor(this);
   @SerializedName("В")
   private final ProfileSettingsPanel В_98dcdd56 = new ProfileSettingsPanel(this);
   @SerializedName("В")
   private final GuiLayoutTransition В_65c4f33c = new GuiLayoutTransition();
   @SerializedName("В")
   private final Matrix4f В_20497804 = new Matrix4f();
   private float л;
   private float э;
   private float ю;
   @SerializedName("В")
   private final EntityEspSettingsPanel В_6ba12e85 = new EntityEspSettingsPanel(this);
   @SerializedName("В")
   private final ClickGuiBackground В_9d0ffde2 = new ClickGuiBackground();
   @SerializedName("В")
   private final TickAnimation В_4bd80188 = new TickAnimation();
   @SerializedName("Х")
   private final FloatAnimation Х_04d91340;
   private final FloatAnimation е;
   @SerializedName("А")
   private final FloatAnimation А_at_c0aafeb1;
   @SerializedName("4")
   private final FloatAnimation field_4;
   private final FloatAnimation п;
   private final FloatAnimation Д;
   @SerializedName("Я")
   private final ArrayList<ClickGuiScreen.SearchResult> Я_c1edc18e;
   @SerializedName("Х")
   private final ArrayList<ClickGuiScreen.IndexedSearchResult> Х_c1edc18e;
   @SerializedName("В")
   private final HashSet<ClickGuiScreen.SearchEntry> В_2e343f72;
   @SerializedName("В")
   private final HashMap<ClickGuiScreen.SearchEntry, Integer> В_e12d7b95;
   @SerializedName("Я")
   private final EnumMap<ModuleCategory, Float> Я_03947583;
   @SerializedName("Ю")
   private boolean Ю_bbeebd87;
   @SerializedName("Ч")
   private boolean Ч_bbeebd87;
   private float Щ;
   private float у;
   @SerializedName("2")
   private float field_2;
   private float ф;
   private float и;
   @SerializedName("9")
   private float field_9;
   private float о;
   private float O;
   @SerializedName("7")
   private float field_7;
   private float Р;
   @SerializedName("Л")
   private boolean Л_bbeebd87;
   @SerializedName("3")
   private float field_3;
   @SerializedName("в")
   private long в_6da43b94;
   @SerializedName("ъ")
   private long ъ_6da43b94;
   @SerializedName("М")
   private long М_6da43b94;
   @SerializedName("В")
   private GuiGraphicsExtractor В_4a396a32;
   @SerializedName("诶")
   private String 诶_388008bb;
   @SerializedName("1")
   private String field_1_388008bb;
   @SerializedName("Ь")
   private String Ь_388008bb;
   @SerializedName("Н")
   private long Н_6da43b94;
   @SerializedName("诶")
   private boolean 诶_bbeebd87;
   @SerializedName("Ч")
   private int Ч_a83dd0cc;
   @SerializedName("1")
   private boolean field_1_bbeebd87;
   @SerializedName("Ь")
   private boolean Ь_bbeebd87;
   private float р;
   @SerializedName("6")
   private float field_6;
   @SerializedName("_")
   private float field__;
   private float Ш;
   @SerializedName("衣")
   private String 衣_388008bb;
   @SerializedName("Л")
   private int Л_a83dd0cc;
   private float ы;
   private float й;
   private float Ж;
   private float м;
   @SerializedName("衣")
   private boolean 衣_bbeebd87;
   @SerializedName("В")
   private ModuleCard В_b10e1aaa;
   @SerializedName("Я")
   private ModuleCard Я_b10e1aaa;
   @SerializedName("Х")
   private ModuleCard Х_b10e1aaa;
   @SerializedName("Я")
   private ModuleCategory Я_e0c4705a;
   private boolean ш;
   private final FloatAnimation Ъ;
   @SerializedName("Х")
   private final EventListener<HudRenderEvent.GuiRender> Х_f8db0d09;
   private static final String сгр = "selfcode";
   private static final float сг6 = 8.0F;
   private static final float сг_ = 7.5F;
   private static final float сгШ = 9.0F;
   private static final float сгы = 10.0F;
   private static final float сгй = 9.0F;
   private static final float сгЖ = 9.0F;
   private static final float сгм = 8.0F;
   private static final float сгК = 0.001F;
   private static final float сгI = 0.22F;
   private static final float сгТ = 0.16F;
   private static final float сгО = 0.9F;
   private static final float сгФ = 0.1F;
   private static final float сгЙ = 0.18F;
   private static final float сгк = 24.0F;
   private static final float сгг = 12.0F;
   private static final float сг0 = -20.0F;
   private static final float сгх = 255.0F;
   private static final float сгУ = 24.0F;
   private static final float сгЗ = 22.0F;
   private static final float сгЕ = 20.0F;
   private static final double сгП = 255.0;
   private static final float сг西 = 20.0F;
   private static final float сгд = 20.0F;
   private static final float сгБ = 20.0F;
   private static final float сгС = 20.0F;
   private static final float сгз = 20.0F;
   private static final float сгц = 0.016666668F;
   private static final float сгя = 1.0E9F;
   private static final float с0с = 0.05F;
   private static final float с0В = 10.0F;
   private static final float с0Я = 20.0F;
   private static final float с0Х = 10.0F;
   private static final float с0е = 60.0F;
   private static final float с0А = 20.0F;
   private static final float с04 = 0.016666668F;
   private static final float с0п = 1.0E9F;
   private static final float с0Д = 0.05F;
   private static final float с0Ъ = 10.0F;
   private static final float с0т = 0.05F;
   private static final float с0а = 0.25F;
   private static final float с0Ю = 74.0F;
   private static final float с0Ч = 38.0F;
   private static final float с0Л = 0.5F;
   private static final float с0诶 = 8.0F;
   private static final float с01 = 38.0F;
   private static final float с0Ь = 0.5F;
   private static final float с0衣 = 14.0F;
   private static final float с0ш = 74.0F;
   private static final float с0И = 74.0F;
   private static final float с0ь = 0.88F;
   private static final float с0ж = 0.12F;
   private static final double с0弟 = Math.PI;
   private static final float с0б = 0.025F;
   private static final float с0Ы = -18.0F;
   private static final float с0l = 18.0F;
   private static final float с08 = 24.0F;
   private static final float с05 = 0.001F;
   private static final float с0в = 8.0F;
   private static final float с0ъ = 8.0F;
   private static final String с0М = "Нет избранных. Нажми на звезду на карточке модуля.";
   private static final String с0Н = "No favorites. Tap the star on a module card.";
   private static final float с0н = 26.0F;
   private static final float с0Ц = 74.0F;
   private static final float с0ч = 18.0F;
   private static final float с0Э = 74.0F;
   private static final float с0必 = 12.0F;
   private static final float с0щ = 12.0F;
   private static final float с0Г = 74.0F;
   private static final float с0л = 3.0F;
   private static final float с0э = 1.5F;
   private static final float с0ю = 74.0F;
   private static final float с0Щ = 0.001F;
   private static final float с0у = 74.0F;
   private static final float с02 = 20.0F;
   private static final float с0ф = 0.28F;
   private static final double с0и = Math.PI;
   private static final float с09 = 18.0F;
   private static final double с0о = Math.PI;
   private static final float с0O = 250.0F;
   private static final float с07 = 26.0F;
   private static final float с0Р = 12.0F;
   private static final float с03 = 0.001F;
   private static final float с0р = 0.98F;
   private static final float с06 = 74.0F;
   private static final float с0_ = 42.0F;
   private static final float с0Ш = 14.0F;
   private static final double с0ы = 30.0;
   private static final float с0й = 18.0F;
   private static final double с0Ж = 30.0;
   private static final float с0м = 4.0F;
   private static final float с0К = 4.0F;
   private static final float с0I = 8.0F;
   private static final float с0Т = 8.0F;
   private static final float с0О = 14.0F;
   private static final double с0Ф = 30.0;
   private static final float с0Й = 42.0F;
   private static final float с0к = 32.0F;
   private static final float с0г = 74.0F;
   private static final float с00 = 42.0F;
   private static final float с0х = 74.0F;
   private static final float с0У = 0.001F;
   private static final float с0З = 255.0F;
   private static final float с0Е = 27.0F;
   private static final String с0П = "P";
   private static final float с0西 = 22.0F;
   private static final String с0д = "Client";
   private static final float с0Б = 47.0F;
   private static final String с0С = "Client";
   private static final float с0з = 51.0F;
   private static final String с0ц = "/";
   private static final String с0я = "Избранное";
   private static final String схс = "Favorites";
   private static final String схВ = "*";
   private static final float схЯ = 17.0F;
   private static final float схХ = 37.0F;
   private static final double схе = Math.PI;
   private static final float схА = 20.0F;
   private static final float сх4 = 0.24F;
   private static final float схп = 0.1F;
   private static final float схД = 0.45F;
   private static final String схЪ = "P";
   private static final float схт = 0.5F;
   private static final String сха = "P";
   private static final float схЮ = 0.5F;
   private static final float схЧ = 14.5F;
   private static final float схЛ = 10.0F;
   private static final float сх诶 = 60.0F;
   private static final float сх1 = 20.0F;
   private static final float схЬ = 0.99F;
   private static final float сх衣 = 20.0F;
   private static final float схш = 180.0F;
   private static final float схИ = 40.0F;
   private static final float схь = 20.0F;
   private static final float схж = 283.0F;
   private static final float сх弟 = 40.0F;
   private static final float схб = 0.01F;
   private static final float схЫ = 20.0F;
   private static final float схl = 64.0F;
   private static final float сх8 = 30.0F;
   private static final float сх5 = 120.0F;
   private static final String схв = "ГЛАВНОЕ";
   private static final String схъ = "MAIN";
   private static final float схМ = 24.0F;
   private static final float схН = 77.0F;
   private static final String схн = "ОБЩЕЕ";
   private static final String схЦ = "COMMON";
   private static final float схч = 24.0F;
   private static final float схЭ = 180.0F;
   private static final String сх必 = "ПРОЧЕЕ";
   private static final String схщ = "OTHER";
   private static final float схГ = 24.0F;
   private static final float схл = 283.0F;
   private static final float схэ = 14.0F;
   private static final float схю = 30.0F;
   private static final float схЩ = 8.0F;
   private static final float сху = 0.01F;
   private static final float сх2 = 42.0F;
   private static final float схф = 54.0F;
   private static final float схи = 30.0F;
   private static final float сх9 = 46.0F;
   private static final float схо = 7.0F;
   private static final float схO = 24.0F;
   private static final float сх7 = 8.0F;
   private static final float схР = 0.999F;
   private static final float сх3 = 0.016666668F;
   private static final float схр = 1.0E9F;
   private static final float сх6 = 0.05F;
   private static final float сх_ = 14.0F;
   private static final float схШ = 0.05F;
   private static final float схы = 18.0F;
   private static final float схй = 255.0F;
   private static final float схЖ = 0.18F;
   private static final float схм = 0.08F;
   private static final float схК = 0.001F;
   private static final float схI = 30.0F;
   private static final float схТ = 9.0F;
   private static final float схО = 0.13F;
   private static final float схФ = 30.0F;
   private static final float схЙ = 22.0F;
   private static final float схк = 9.0F;
   private static final double схг = 255.0;
   private static final float сх0 = 30.0F;
   private static final float схх = 9.0F;
   private static final float схУ = 30.0F;
   private static final float схЗ = 9.0F;
   private static final String схЕ = "t";
   private static final float схП = 11.0F;
   private static final float сх西 = 9.0F;
   private static final String схд = "CTRL + F";
   private static final float схБ = 22.0F;
   private static final float схС = 7.0F;
   private static final float схз = 5.0F;
   private static final float схц = 10.0F;
   private static final String схя = "@";
   private static final float сУс = 8.0F;
   private static final float сУВ = 46.0F;
   private static final float сУЯ = 14.0F;
   private static final float сУХ = 0.05F;
   private static final float сУе = 29.0F;
   private static final float сУА = 37.0F;
   private static final float сУ4 = 30.0F;
   private static final float сУп = 32.0F;
   private static final float сУД = 7.0F;
   private static final float сУЪ = 4.0F;
   private static final float сУт = 16.0F;
   private static final float сУа = 0.55F;
   private static final float сУЮ = 34.0F;
   private static final String сУЧ = "Поиск...";
   private static final float сУЛ = 8.0F;
   private static final float сУ诶 = 8.0F;
   private static final float сУ1 = 22.0F;
   private static final float сУЬ = 0.38F;
   private static final float сУ衣 = 0.62F;
   private static final float сУш = 0.5F;
   private static final float сУИ = 0.5F;
   private static final double сУь = 1.0E9;
   private static final double сУж = 5.4;
   private static final float сУ弟 = 7.5F;
   private static final float сУб = 1.1F;
   private static final float сУЫ = 15.0F;
   private static final float сУl = 0.55F;
   private static final float сУ8 = 0.999F;
   private static final float сУ5 = 0.18F;
   private static final float сУв = 0.045F;
   private static final float сУъ = 0.01F;
   private static final float сУМ = 11.15F;
   private static final float сУН = 0.85F;
   private static final float сУн = 0.5F;
   private static final float сУЦ = 2.2F;
   private static final float сУч = 255.0F;
   private static final float сУЭ = 0.5F;
   private static final float сУ必 = 0.001F;
   private static final float сУщ = 255.0F;
   private static final float сУГ = 0.9F;
   private static final float сУл = 0.1F;
   private static final float сУэ = 255.0F;
   private static final float сУю = 24.0F;
   private static final float сУЩ = 255.0F;
   private static final float сУу = 255.0F;
   private static final float сУ2 = 255.0F;
   private static final float сУф = 22.0F;
   private static final float сУи = 20.0F;
   private static final float сУ9 = 20.0F;
   private static final float сУо = 255.0F;
   private static final float сУO = 0.2F;
   private static final float сУ7 = 20.0F;
   private static final float сУР = 255.0F;
   private static final String сУ3 = "t";
   private static final float сУр = 15.0F;
   private static final float сУ6 = 13.0F;
   private static final float сУ_ = 255.0F;
   private static final float сУШ = 0.999F;
   private static final String сУы = "Открыть";
   private static final float сУй = 15.0F;
   private static final float сУЖ = 13.0F;
   private static final float сУм = 255.0F;
   private static final float сУК = 12.0F;
   private static final float сУI = 38.0F;
   private static final float сУТ = 5.0F;
   private static final float сУО = 24.0F;
   private static final float сУФ = 0.5F;
   private static final float сУЙ = 255.0F;
   private static final float сУк = 38.0F;
   private static final float сУг = 38.0F;
   private static final float сУ0 = 7.0F;
   private static final float сУх = 6.0F;
   private static final float сУУ = 12.0F;
   private static final float сУЗ = 0.999F;
   private static final float сУЕ = 224.0F;
   private static final float сУП = 32.0F;
   private static final float сУ西 = 24.0F;
   private static final float сУд = 5.0F;
   private static final float сУБ = 255.0F;
   private static final float сУС = 0.001F;
   private static final String сУз = "Результаты поиска";
   private static final float сУц = 35.0F;
   private static final float сУя = 12.0F;
   private static final float сЗс = 255.0F;
   private static final float сЗВ = 0.001F;
   private static final String сЗЯ = "Ничего не найдено";
   private static final float сЗХ = 14.0F;
   private static final float сЗе = 8.0F;
   private static final float сЗА = 13.0F;
   private static final float сЗ4 = 4.0F;
   private static final float сЗп = 255.0F;
   private static final float сЗД = 32.0F;
   private static final float сЗЪ = 32.0F;
   private static final float сЗт = 224.0F;
   private static final float сЗа = 10.0F;
   private static final float сЗЮ = 8.0F;
   private static final float сЗЧ = 3.0F;
   private static final float сЗЛ = 0.001F;
   private static final String сЗ诶 = "Ничего не найдено";
   private static final float сЗ1 = 14.0F;
   private static final float сЗЬ = 5.0F;
   private static final float сЗ衣 = 13.0F;
   private static final float сЗш = 4.0F;
   private static final float сЗИ = 255.0F;
   private static final float сЗь = 0.55F;
   private static final float сЗж = 0.45F;
   private static final float сЗ弟 = 3.0F;
   private static final float сЗб = 32.0F;
   private static final float сЗЫ = 0.16F;
   private static final float сЗl = 0.025F;
   private static final float сЗ8 = 0.01F;
   private static final float сЗ5 = 3.0F;
   private static final float сЗв = 5.0F;
   private static final float сЗъ = 32.0F;
   private static final float сЗМ = 32.0F;
   private static final float сЗН = 224.0F;
   private static final float сЗн = 0.24F;
   private static final float сЗЦ = 0.035F;
   private static final float сЗч = 0.01F;
   private static final float сЗЭ = 10.0F;
   private static final float сЗ必 = 8.0F;
   private static final float сЗщ = 0.9F;
   private static final float сЗГ = 20.0F;
   private static final double сЗл = 27.0;
   private static final float сЗэ = 0.001F;
   private static final float сЗю = 20.0F;
   private static final float сЗЩ = 27.0F;
   private static final float сЗу = 6.0F;
   private static final float сЗ2 = 255.0F;
   private static final String сЗф = "Module";
   private static final float сЗи = 24.0F;
   private static final float сЗ9 = 0.52F;
   private static final float сЗо = 18.0F;
   private static final float сЗO = 18.0F;
   private static final float сЗ7 = 78.0F;
   private static final float сЗР = 12.0F;
   private static final float сЗ3 = 8.0F;
   private static final float сЗр = 255.0F;
   private static final float сЗ6 = 36.0F;
   private static final float сЗ_ = 7.0F;
   private static final float сЗШ = 255.0F;
   private static final float сЗы = 8.0F;
   private static final float сЗй = 9.0F;
   private static final float сЗЖ = 8.0F;
   private static final float сЗм = 255.0F;
   private static final float сЗК = 0.001F;
   private static final long сЗI = 110000000L;
   private static final float сЗТ = 0.98F;
   private static final float сЗО = 38.0F;
   private static final float сЗФ = 32.0F;
   private static final float сЗЙ = 288.0F;
   private static final float сЗк = 380.0F;
   private static final float сЗг = 48.0F;
   private static final float сЗ0 = 0.5F;
   private static final float сЗх = 0.5F;
   private static final float сЗУ = 269.0F;
   private static final float сЗЗ = 32.0F;
   private static final float сЗЕ = 0.001F;
   private static final float сЗП = 20.0F;
   private static final float сЗ西 = 128.0F;
   private static final double сЗд = 0.1;
   private static final float сЗБ = 74.0F;
   private static final float сЗС = 108.0F;
   private static final float сЗз = 62.0F;
   private static final float сЗц = 204.0F;
   private static final float сЗя = 170.0F;
   private static final float сЕс = 920.0F;
   private static final float сЕВ = 800.0F;
   private static final float сЕЯ = 584.0F;
   private static final float сЕХ = 504.0F;
   private static final float сЕе = 32.0F;
   private static final float сЕА = 32.0F;
   private static final float сЕ4 = 0.1F;
   private static final float сЕп = 0.5F;
   private static final float сЕД = 0.5F;
   private static final float сЕЪ = 255.0F;
   private static final float сЕт = 28.0F;
   private static final float сЕа = 16.0F;
   private static final float сЕЮ = 74.0F;
   private static final double сЕЧ = 12.0;
   private static final float сЕЛ = 74.0F;
   private static final float сЕ诶 = 74.0F;
   private static final float сЕ1 = 97.0F;
   private static final float сЕЬ = 132.0F;
   private static final float сЕ衣 = 200.0F;
   private static final float сЕш = 235.0F;
   private static final float сЕИ = 303.0F;
   private static final float сЕь = 338.0F;
   private static final float сЕж = 373.0F;
   private static final double сЕ弟 = 1.0E9;
   private static final double сЕб = 0.045;
   private static final double сЕЫ = 0.32;
   private static final float сЕl = 38.0F;
   private static final float сЕ8 = 18.0F;
   private static final double сЕ5 = 30.0;
   private static final double сЕв = 30.0;
   private static final float сЕъ = 18.0F;
   private static final double сЕМ = 255.0;
   private static final double сЕН = 255.0;
   private static final float сЕн = 15.0F;
   private static final float сЕЦ = 15.0F;
   private static final float сЕч = 8.0F;

   @Override
   public boolean keyReleased(KeyEvent var1) {
      if (this.В_98dcdd56.хП()) {
         return true;
      }

      if (this.В_6ba12e85.цш()) {
         return this.В_6ba12e85.Х(var1.key());
      }

      for (ModuleCard var3 : this.В_c1edc18e) {
         var3.keyReleased(var1.key());
      }

      return true;
   }

   public void clearFocusedModule() {
      if (this.Я_b10e1aaa != null) {
         this.Я_b10e1aaa.setSettingsExpanded(false);
      }

      this.Я_b10e1aaa = null;
   }

   private void renderGui() {
      this.衣_bbeebd87 = false;
      Window var1 = MINECRAFT_CLIENT.getWindow();
      if (!var1.isIconified() && var1.getScreenWidth() > 0 && var1.getScreenHeight() > 0) {
         RoseClient.getInstance().getServices().getModules().getClickGui().Юр();
         UiDrawing.setTransform(new Matrix4f());
         UiDrawing.disableScissor();
         this.Д.updateTarget(this.Я_b10e1aaa != null ? 1.0F : 0.0F);
         if (this.Я_b10e1aaa == null && this.Д.getValue() < сгК) {
            this.Х_b10e1aaa = null;
         }

         this.В_4bd80188.configureAndInterpolate(0.0F, 1.0F, this.Ю_bbeebd87 ? сгI : сгТ, EasingFunctions.SINE_OUT, MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));

         for (ModuleCategory var5 : ModuleCategory.values()) {
            var5.ц().updateTarget(var5 == this.Я_e0c4705a ? 1.0F : 0.0F);
         }

         float var41 = Math.clamp(this.В_4bd80188.getInterpolatedValue(), 0.0F, 1.0F);
         float var42 = this.Ю_bbeebd87 ? 0.0F : 1.0F - var41;
         float var43 = this.Щ;
         float var44 = this.у;
         boolean var6 = this.Я_e0c4705a == ModuleCategory.COSMETICS;
         float var7 = var6
            ? 1.0F
            : (this.Ю_bbeebd87 ? сгО + сгФ * (float)Easings.BACK_OUT.apply(var41) : 1.0F - сгЙ * (float)Easings.CUBIC_IN.apply(var42));
         if (!var6) {
            this.Щ = this.Щ + сгк * var42 * var42;
            this.у = this.у + (this.Ю_bbeebd87 ? сгг * (1.0F - var41) : сг0 * var42 * var42);
         }

         var7 *= this.getInterfaceScale();
         WorldProjection.scaleAroundRect(this.Щ - this.guiLeftInset(), this.у, this.totalGuiWidth(), this.ф, var7);
         this.В_20497804.set(UiDrawing.getTransform());
         this.л = this.Щ;
         this.э = this.у;
         this.ю = var41;
         UiDrawing.setGlobalAlpha(сгх * var41);
         float var8 = this.field_2;
         if (!var6) {
            UiDrawing.drawShadow(this.Щ, this.у, var8, this.ф, сгУ, ThemeEditor.getColorArgb(ThemeColorSlot.SHADOW));
            if (ClickGuiPreferences.х弟() > 0) {
               UiDrawing.drawBlur(this.Щ, this.у, var8, this.ф, сгЗ, сгЕ, ColorMath.packArgb(255, 255, 255, сгП));
            }

            UiDrawing.drawRoundedRect(this.Щ, this.у, var8, this.ф, new Vector4f(сг西, сгд, сгБ, сгС), ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND));
         }

         if (this.В_6ba12e85.цш()) {
            this.renderCategorySidebar();
            this.renderProfileBlock();
            this.В_6ba12e85.В(this.В_4a396a32, this.и, this.field_9, MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));
            RoseClient.getInstance().getServices().getModules().getClickGui().Э(this.Щ, this.у, this.field_2, this.ф, сгз);
            UiDrawing.resetGlobalAlpha();
            WorldProjection.unscaleAroundRect(this.Щ - this.guiLeftInset(), this.у, this.totalGuiWidth(), this.ф, var7);
            this.Щ = var43;
            this.у = var44;
         } else if (this.Я_e0c4705a == ModuleCategory.COSMETICS) {
            long var46 = System.nanoTime();
            float var11 = this.в_6da43b94 == 0L ? сгц : Math.clamp((float)(var46 - this.в_6da43b94) / сгя, 0.0F, с0с);
            this.в_6da43b94 = var46;
            float var47 = 1.0F - (float)Math.exp(-var11 * с0В);
            this.о = this.о + (this.O - this.о) * var47;
            this.В_77119740.衣(this.В_4a396a32, this.и, this.field_9, this.о);
            this.renderProfileBlock();
            this.renderLayoutAccent();
            this.Р = this.В_77119740.Ь2();
            float var48 = this.getMinScroll();
            this.O = Math.clamp(this.O, var48, 0.0F);
            this.о = Math.clamp(this.о, var48, 0.0F);
            UiDrawing.resetGlobalAlpha();
            WorldProjection.unscaleAroundRect(this.Щ - this.guiLeftInset(), this.у, this.totalGuiWidth(), this.ф, var7);
            this.Щ = var43;
            this.у = var44;
         } else {
            this.renderCategorySidebar();
            UiDrawing.drawOutline(
               this.Щ + this.getSidebarWidth(), this.у, this.field_2 - this.getSidebarWidth(), this.ф, 1.0F, с0Я, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE)
            );
            UiDrawing.drawRoundedRect(
               this.Щ + this.getSidebarWidth() + с0Х,
               this.у + с0е,
               this.field_2 - this.getSidebarWidth() - с0А,
               1.0F,
               0.0F,
               ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE)
            );
            this.renderBreadcrumb();
            this.renderProfileBlock();
            float var9 = this.getMinScroll();
            this.O = Math.clamp(this.O, var9, 0.0F);
            long var10 = System.nanoTime();
            float var12 = this.в_6da43b94 == 0L ? с04 : Math.clamp((float)(var10 - this.в_6da43b94) / с0п, 0.0F, с0Д);
            this.в_6da43b94 = var10;
            float var13 = 1.0F - (float)Math.exp(-var12 * с0Ъ);
            this.о = this.о + (this.O - this.о) * var13;
            if (Math.abs(this.O - this.о) < с0т) {
               this.о = this.O;
            }

            if (Math.abs(this.о - this.field_7) > с0а) {
               this.closeSettingPopups();
            }

            this.field_7 = this.о;
            boolean var14 = !this.ш
               && (this.Я_e0c4705a == ModuleCategory.AUTOBUY || this.Я_e0c4705a == ModuleCategory.COSMETICS || this.Я_e0c4705a == ModuleCategory.THEMES);
            if (!var14) {
               UiDrawing.setScissor(this.getModuleViewportX(), this.у + с0Ю, this.getModuleViewportWidth(), this.getModuleViewportHeight());
            }

            int var15 = 0;
            int var16 = 0;
            float var17 = 0.0F;
            float var18 = 0.0F;
            float var19 = 0.0F;
            if (!this.ш && this.Я_e0c4705a == ModuleCategory.AUTOBUY) {
               this.В_ec501b46.诶(this.В_4a396a32, this.и, this.field_9, this.о);
               this.Р = this.В_ec501b46.ЛЛ();
            } else if (!this.ш && this.Я_e0c4705a == ModuleCategory.COSMETICS) {
               this.В_77119740.衣(this.В_4a396a32, this.и, this.field_9, this.о);
               this.Р = this.В_77119740.Ь2();
            } else if (!this.ш && this.Я_e0c4705a == ModuleCategory.THEMES) {
               this.В_ec58113c.render(this.В_4a396a32, this.и, this.field_9, this.о);
               this.Р = this.В_ec58113c.getHeight();
            } else {
               for (ModuleCard var21 : this.В_c1edc18e) {
                  if (this.visibleInCurrentView(var21)) {
                     float var22 = 0.0F;
                     if (var15 % 2 != 0) {
                        var22 += (this.field_2 - this.getSidebarWidth() - с0Ч) * с0Л + с0诶;
                     }

                     float var23 = var15 % 2 == 0 ? var18 : var19;
                     var21.setWidth((this.field_2 - this.getSidebarWidth() - с01) * с0Ь);
                     var21.updateLayout(MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));
                     float var24 = this.Щ + this.getSidebarWidth() + с0衣 + var22;
                     float var25 = this.у + var23 + с0ш + this.о;
                     float var26 = this.у + с0И;
                     float var27 = var26 + this.getModuleViewportHeight();
                     boolean var28 = var25 + var21.getHeight() > var26 && var25 < var27;
                     float var29 = var28 ? this.getModuleEntrancePhase(var16++) : 1.0F;
                     float var30 = (float)Easings.CUBIC_OUT.apply(var29);
                     float var31 = (float)Easings.BACK_OUT.apply(var29);
                     float var32 = с0ь + с0ж * var31;
                     float var33 = (float)Math.sin(с0弟 * this.getLayoutReveal(var15));
                     var32 *= 1.0F - с0б * var33;
                     var21.setX(var24 + (var15 % 2 == 0 ? с0Ы : с0l) * (1.0F - var30));
                     var21.setY(var25 + с08 * (1.0F - var30) + this.getLayoutOffset(var15));
                     float var34 = UiDrawing.getGlobalAlpha();
                     boolean var35 = var21 == this.Х_b10e1aaa && this.Д.getValue() > с05;
                     boolean var36 = !this.Ю_bbeebd87 && this.Х_b10e1aaa != null && var21 != this.Х_b10e1aaa;
                     Matrix4f var37 = new Matrix4f(UiDrawing.getTransform());

                     try {
                        WorldProjection.scaleAroundRect(var21.getX(), var21.getY(), var21.getWidth(), var21.getHeight(), var32);
                        UiDrawing.setGlobalAlpha(!var35 && !var36 ? var34 * var30 * this.getLayoutContentAlpha(var15) : 0.0F);
                        var21.render(this.и, this.field_9, MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));
                     } finally {
                        UiDrawing.setTransform(var37);
                        UiDrawing.setGlobalAlpha(var34);
                     }

                     if (var15 % 2 == 0) {
                        var18 += var21.getHeight() + с0в;
                     } else {
                        var19 += var21.getHeight() + с0ъ;
                     }

                     var15++;
                  }
               }
            }

            if (this.ш && var15 == 0) {
               UiDrawing.drawText(
                  ClientFonts.getMediumFont(),
                  ClientLocalization.selectLocalizedText(с0М, с0Н),
                  this.Щ + this.getSidebarWidth() + с0н,
                  this.у + с0Ц + с0ч,
                  13,
                  ThemeEditor.getColorArgb(ThemeColorSlot.SUR_TEXT)
               );
            }

            if (!var14) {
               this.Р = Math.max(var18, var19);
            }

            float var49 = this.getMinScroll();
            if (this.В_b10e1aaa != null && this.visibleInCurrentView(this.В_b10e1aaa)) {
               float var50 = this.В_b10e1aaa.getY() - this.о;
               float var54 = this.у + с0Э + с0必 - var50;
               this.O = Math.clamp(var54, var49, 0.0F);
               this.о = this.O;
               this.В_b10e1aaa = null;
            }

            this.O = Math.clamp(this.O, var49, 0.0F);
            this.о = Math.clamp(this.о, var49, 0.0F);
            if (this.Л_bbeebd87) {
               this.updateScrollbar(this.field_9);
            }

            if (!var14) {
               UiDrawing.disableScissor();
            }

            if (this.Я_e0c4705a != ModuleCategory.COSMETICS && this.Р > this.getModuleViewportHeight()) {
               float var51 = this.getScrollbarThumbHeight();
               float var55 = this.getModuleViewportHeight() - var51;
               float var58 = -this.о / Math.max(1.0F, this.Р - this.getModuleViewportHeight());
               UiDrawing.drawRoundedRect(this.Щ + this.field_2 - с0щ, this.у + с0Г + var55 * var58, с0л, var51, с0э, ThemeEditor.getColorArgb(ThemeColorSlot.SCROLL));
            }

            if (!var14) {
               UiDrawing.setScissor(this.getModuleViewportX(), this.у + с0ю, this.getModuleViewportWidth(), this.getModuleViewportHeight());
            }

            for (ModuleCard var56 : this.В_c1edc18e) {
               if (var14) {
                  break;
               }

               if (this.visibleInCurrentView(var56) && var56 != this.Я_b10e1aaa && (this.Ю_bbeebd87 || this.Х_b10e1aaa == null)) {
                  var56.renderOverlay(this.и, this.field_9, MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));
               }
            }

            if (!var14) {
               UiDrawing.disableScissor();
            }

            for (ModuleKeybindWidget var57 : this.В_b1d77d5c) {
               var57.render(this.и, this.field_9, MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));
            }

            if (this.Я_e0c4705a != ModuleCategory.COSMETICS) {
               this.renderFavoritesButton();
            }

            this.renderModuleFocus();
            this.renderLayoutAccent();
            if (this.Х_b10e1aaa != null && this.Д.getValue() > с0Щ) {
               UiDrawing.setScissor(this.getModuleViewportX(), this.у + с0у, this.getModuleViewportWidth(), this.getModuleViewportHeight());
               this.Х_b10e1aaa.render(this.и, this.field_9, MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));
               UiDrawing.disableScissor();
               this.Х_b10e1aaa.renderOverlay(this.и, this.field_9, MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true));
            }

            RoseClient.getInstance().getServices().getModules().getClickGui().Э(this.Щ, this.у, this.field_2, this.ф, с02);
            if (this.Я_e0c4705a == ModuleCategory.THEMES) {
               this.В_ec58113c.renderOverlays(this.В_4a396a32, this.и, this.field_9, this.о);
            }

            if (this.Я_e0c4705a != ModuleCategory.COSMETICS) {
               this.ы = this.Щ;
               this.й = this.у;
               this.Ж = var7;
               this.м = var41;
               this.衣_bbeebd87 = true;
               this.renderSearch();
            }

            this.renderHoverHint();
            UiDrawing.resetGlobalAlpha();
            WorldProjection.unscaleAroundRect(this.Щ - this.guiLeftInset(), this.у, this.totalGuiWidth(), this.ф, var7);
            this.Щ = var43;
            this.у = var44;
         }
      }
   }

   /** Подсказка рисуется последней и без обрезки — так её не режет ни скролл, ни панели. */
   private void renderHintTopmost() {
      HoverHint.Content content = this.pendingHint;
      this.pendingHint = null;
      if (content == null) {
         return;
      }

      Matrix4f previous = UiDrawing.getTransform();
      UiDrawing.disableScissor();
      UiDrawing.setTransform(new Matrix4f());

      try {
         HoverHint.render(this.и, this.field_9, 0.0F, 0.0F, this.width, this.height, content);
      } finally {
         UiDrawing.setTransform(previous);
      }
   }

   private void renderHoverHint() {
      if (!ClickGuiPreferences.йЩ()) {
         this.pendingHint = null;
         HoverHint.reset();
         return;
      }

      HoverHint.Content content = null;

      for (ModuleCard card : this.В_c1edc18e) {
         if (card != null && this.visibleInCurrentView(card)) {
            content = card.hintAt(this.и, this.field_9);
            if (content != null) {
               break;
            }
         }
      }

      this.pendingHint = content;

      if (content == null) {
         HoverHint.reset();
      }
   }

   private void renderFavoritesButton() {
      this.Ъ.updateTarget(this.ш ? 1.0F : 0.0F);
      float var1 = this.favoritesButtonX();
      float var2 = this.у + сЕъ;
      float var3 = this.Ъ.getValue();
      int var4 = ColorMath.lerpArgb(ColorMath.packArgb(186, 190, 200, сЕМ), ColorMath.packArgb(255, 210, 64, сЕН), var3);
      UiDrawing.drawCenteredAsteriskIcon(var1 + сЕн, var2 + сЕЦ, сЕч, var4);
   }

   @Override
   protected void init() {
      if (!this.Ч_bbeebd87) {
         ClientSounds.к(ClientSounds.GuiSound.OPEN);
      }

      this.Ч_bbeebd87 = true;
      this.В_98dcdd56.Хл();
      if (this.Я_e0c4705a == ModuleCategory.COSMETICS) {
         this.Я_e0c4705a = ModuleCategory.COMBAT;
      }

      this.updateCoordinates();
      this.В_6ba12e85.Ро();
      this.clearFocusedModule();
      this.Х_b10e1aaa = null;
      this.Д.setImmediately(0.0F);
      this.closeSettingPopups();
      this.Ю_bbeebd87 = true;
      this.restoreCategoryScroll();
      this.М_6da43b94 = System.nanoTime();
      if (this.Я_e0c4705a == ModuleCategory.THEMES) {
         this.В_ec58113c.restartEntranceTransitions();
      }
   }

   private void renderSearch() {
      if (!this.诶_388008bb.equals(this.衣_388008bb)) {
         this.Л_a83dd0cc = this.commonPrefixLength(this.衣_388008bb, this.诶_388008bb);
         this.衣_388008bb = this.诶_388008bb;
         this.field_4.setImmediately(0.0F);
         this.Ь_388008bb = this.诶_388008bb.trim().toLowerCase(Locale.ROOT);
         this.Н_6da43b94 = System.nanoTime();
      }

      this.Х_04d91340.updateTarget(this.field_1_bbeebd87 ? 1.0F : 0.0F);
      this.field_4.updateTarget(1.0F);
      this.updateSearchResults();
      this.е.updateTarget(this.isSearchDropdownVisible() ? 1.0F : 0.0F);
      this.А_at_c0aafeb1.updateTarget(this.isSearchDropdownVisible() ? 1.0F : 0.0F);
      this.п.updateTarget(1.0F);
      if (this.п.getValue() >= схР) {
         this.Х_c1edc18e.clear();
         this.В_2e343f72.clear();
         this.В_e12d7b95.clear();
         this.诶_bbeebd87 = false;
         this.Ч_a83dd0cc = 0;
      }

      long var1 = System.nanoTime();
      float var3 = this.ъ_6da43b94 == 0L ? сх3 : Math.clamp((float)(var1 - this.ъ_6da43b94) / схр, 0.0F, сх6);
      this.ъ_6da43b94 = var1;
      this.field_6 = Math.clamp(this.field_6, 0.0F, this.getSearchMaxScroll());
      float var4 = 1.0F - (float)Math.exp(-var3 * сх_);
      this.р = this.р + (this.field_6 - this.р) * var4;
      if (Math.abs(this.field_6 - this.р) < схШ) {
         this.р = this.field_6;
      }

      float var5 = Math.clamp(this.Х_04d91340.getValue(), 0.0F, 1.0F);
      float var6 = this.getSearchWidth();
      float var7 = this.searchX();
      float var8 = this.у + схы;
      float var9 = ThemeEditor.getColorAlpha(ThemeColorSlot.BACKGROUND) / схй;
      int var10 = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND), var9 * (схЖ + var5 * схм));
      if (var5 > схК) {
         UiDrawing.drawShadow(var7, var8, var6, схI, схТ, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), схО * var5));
      }

      UiDrawing.drawBlur(var7, var8, var6, схФ, схЙ, схк, ColorMath.packArgb(255, 255, 255, схг));
      UiDrawing.drawRoundedRect(var7, var8, var6, сх0, схх, var10);
      UiDrawing.drawOutline(var7, var8, var6, схУ, 1.0F, схЗ, ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE), ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), var5));
      int var11 = ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), var5);
      UiDrawing.drawText(ClientFonts.getIconsFont(), схЕ, var7 + схП, var8 + сх西, 12, var11);
      String var12 = схд;
      float var13 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var12, 9);
      float var14 = var7 + var6 - схБ;
      float var15 = var14 - var13 - схС;
      int var16 = ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var12, var15 - схз, var8 + схц, 10, var16);
      UiDrawing.drawText(ClientFonts.getIconsFont(), схя, var14, var8 + сУс, 13, var16);
      int var17 = this.诶_388008bb.isEmpty() ? ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT) : ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT);
      float var18 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), this.诶_388008bb, 12);
      float var19 = Math.max(0.0F, var15 - var7 - сУВ);
      float var20 = this.诶_388008bb.isEmpty() ? 0.0F : Math.max(0.0F, var18 - var19);
      float var21 = 1.0F - (float)Math.exp(-var3 * сУЯ);
      this.field__ = this.field__ + (var20 - this.field__) * var21;
      if (Math.abs(var20 - this.field__) < сУХ) {
         this.field__ = var20;
      }

      UiDrawing.setScissor(var7 + сУе, var8, Math.max(0.0F, var15 - var7 - сУА), сУ4);
      if (this.Ь_bbeebd87 && !this.诶_388008bb.isEmpty()) {
         float var22 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), this.诶_388008bb, 12);
         UiDrawing.drawRoundedRect(var7 + сУп - this.field__, var8 + сУД, var22 + сУЪ, сУт, 2.0F, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), сУа));
      }

      float var25 = var7 + сУЮ - this.field__;
      if (this.诶_388008bb.isEmpty()) {
         UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.translate(сУЧ), var25, var8 + сУЛ, 12, var17);
      } else {
         this.renderAnimatedSearchText(var25, var8 + сУ诶, var17);
      }

      float var23 = var25 + var18 + 1.0F;
      if (!Float.isNaN(this.Ш) && !(Math.abs(this.Ш - var23) > var6)) {
         this.Ш = this.Ш + (var23 - this.Ш) * (1.0F - (float)Math.exp(-var3 * сУ1));
      } else {
         this.Ш = var23;
      }

      if (this.field_1_bbeebd87 && !this.Ь_bbeebd87) {
         float var24 = сУЬ + сУ衣 * (сУш + сУИ * (float)Math.sin(System.nanoTime() / сУь * сУж));
         UiDrawing.drawRoundedRect(this.Ш, var8 + сУ弟, сУб, сУЫ, сУl, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), var24));
      }

      UiDrawing.disableScissor();
   }

   public float getLayoutOffset(int var1) {
      float var2 = this.getLayoutReveal(var1);
      return с09 * (float)Math.sin(с0о * var2) * (1.0F - var2);
   }

   private void drawSearchTitle(float var1, float var2, int var3, float var4) {
      if (!(var4 <= сУС)) {
         String var5 = ClientLocalization.translate(сУз) + "  " + var3;
         UiDrawing.drawText(
            ClientFonts.getMediumFont(),
            var5,
            var1 + сУц,
            var2 + сУя,
            12,
            ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT), ThemeEditor.getColorAlpha(ThemeColorSlot.ACTIVE_TEXT) / сЗс * var4)
         );
      }
   }

   public boolean isAutoBuyViewportHovered() {
      return WorldProjection.containsPoint(
         this.и, this.field_9, this.getModuleViewportX(), this.у + с06, this.getModuleViewportWidth(), this.getModuleViewportHeight()
      );
   }

   @Generated
   public CopyOnWriteArrayList<ModuleKeybindWidget> getBinds() {
      return this.В_b1d77d5c;
   }

   public float getGuiScroll() {
      return this.о;
   }

   private void drawSearchResultRow(ClickGuiScreen.SearchResult var1, float var2, float var3, float var4, float var5, boolean var6) {
      if (!(var5 <= сЗэ)) {
         float var7 = var4 - сЗю;
         float var8 = var3 + 2.0F;
         float var9 = сЗЩ;
         if (var6) {
            UiDrawing.drawRoundedRect(
               var2, var8, var7, var9, сЗу, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SELECTED), ThemeEditor.getColorAlpha(ThemeColorSlot.SELECTED) / сЗ2 * var5)
            );
         }

         ClientModule var10 = var1.component().getModule();
         String var11 = ClientLocalization.translate(var10.getName());
         String var12 = var1.setting() == null ? сЗф : ClientLocalization.translate(var1.setting().getName());
         float var13 = Math.max(сЗи, var7 * сЗ9 - сЗо);
         var12 = this.fitSearchText(var12, var13, 11);
         float var14 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var12, 11);
         float var15 = var14 + сЗO;
         float var16 = Math.max(0.0F, var4 - var15 - сЗ7);
         var11 = this.fitSearchText(var11, var16, 12);
         UiDrawing.pushScissor(var2 + 1.0F, var8 + 1.0F, var7 - 2.0F, var9 - 2.0F);
         UiDrawing.drawText(
            ClientFonts.getIconsFont(),
            var10.getCategory()._Н(),
            var2 + сЗР,
            var8 + сЗ3,
            11,
            ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), ThemeEditor.getColorAlpha(ThemeColorSlot.MAIN) / сЗр * var5)
         );
         ThemeColorSlot var17 = var6 ? ThemeColorSlot.ACTIVE_TEXT : ThemeColorSlot.DEACTIVE_TEXT;
         UiDrawing.drawText(
            ClientFonts.getMediumFont(), var11, var2 + сЗ6, var8 + сЗ_, 12, ColorMath.withAlpha(ThemeEditor.getColorArgb(var17), ThemeEditor.getColorAlpha(var17) / сЗШ * var5)
         );
         float var18 = var2 + var7 - var15 - сЗы;
         ThemeColorSlot var19 = var1.setting() == null ? ThemeColorSlot.DEACTIVE_TEXT : ThemeColorSlot.ACTIVE_TEXT;
         UiDrawing.drawText(
            ClientFonts.getMediumFont(), var12, var18 + сЗй, var8 + сЗЖ, 10, ColorMath.withAlpha(ThemeEditor.getColorArgb(var19), ThemeEditor.getColorAlpha(var19) / сЗм * var5)
         );
         UiDrawing.popScissor();
      }
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.В_98dcdd56.хП()) {
         if (var1.button() == 0) {
            this.В_98dcdd56.Йч(this.toGuiPosition(var1.x(), var1.y()).x);
         }

         return true;
      } else {
         return this.В_6ba12e85.цш() ? true : super.mouseDragged(var1, var2, var4);
      }
   }

   private float getSearchPanelY() {
      return this.у + (this.ф - this.getSearchDropdownHeight()) * сЗх;
   }

   private boolean isExpandedLayout() {
      return this.areSettingsPinned() && !this.В_6ba12e85.цш();
   }

   private void updateSearchResults() {
      String var1 = this.诶_388008bb.trim().toLowerCase(Locale.ROOT);
      if (var1.isEmpty()) {
         this.field_1_388008bb = var1;
         if (this.е.getValue() <= сЗК) {
            this.Я_c1edc18e.clear();
            this.Х_c1edc18e.clear();
            this.В_2e343f72.clear();
            this.В_e12d7b95.clear();
            this.诶_bbeebd87 = false;
            this.Ч_a83dd0cc = 0;
         }
      } else if (!var1.equals(this.Ь_388008bb)) {
         this.Ь_388008bb = var1;
         this.Н_6da43b94 = System.nanoTime();
      } else if (!var1.equals(this.field_1_388008bb)) {
         if (System.nanoTime() - this.Н_6da43b94 >= сЗI) {
            if (this.field_1_388008bb == null || this.field_1_388008bb.isEmpty() || !(this.п.getValue() < сЗТ)) {
               ArrayList<ClickGuiScreen.SearchResult> var2 = new ArrayList<>();

               for (ModuleCard var4 : this.В_c1edc18e) {
                  ClientModule var5 = var4.getModule();
                  int var6 = this.searchScoreLocalized(var1, var5.getName(), var5.getDescription(), var5.getCategory()._А());
                  if (var6 >= 0) {
                     var2.add(new ClickGuiScreen.SearchResult(var4, null, var6));
                  }

                  for (ValueSetting var8 : var5.getSettingsList()) {
                     int var9 = this.searchScoreLocalized(var1, var8.getName());
                     if (var9 >= 0) {
                        var2.add(new ClickGuiScreen.SearchResult(var4, var8, var9 - 5));
                     }
                  }
               }

               var2.sort(
                  Comparator.comparingInt(ClickGuiScreen.SearchResult::score)
                     .reversed()
                     .thenComparing(var0 -> var0.component().getModule().getName(), String.CASE_INSENSITIVE_ORDER)
                     .thenComparing(var0 -> var0.setting() == null ? "" : var0.setting().getName(), String.CASE_INSENSITIVE_ORDER)
               );
               HashMap var10 = new HashMap();

               for (ClickGuiScreen.SearchResult var13 : var2) {
                  var10.put(this.searchKey(var13), var13);
               }

               this.Ч_a83dd0cc = this.Я_c1edc18e.size();
               this.诶_bbeebd87 = this.Я_c1edc18e.isEmpty() && !var2.isEmpty() && this.field_1_388008bb != null && !this.field_1_388008bb.isEmpty();
               this.Х_c1edc18e.clear();
               this.В_2e343f72.clear();
               this.В_e12d7b95.clear();
               ArrayList var12 = new ArrayList();

               for (int var14 = 0; var14 < this.Я_c1edc18e.size(); var14++) {
                  ClickGuiScreen.SearchResult var16 = this.Я_c1edc18e.get(var14);
                  ClickGuiScreen.SearchEntry var18 = this.searchKey(var16);
                  ClickGuiScreen.SearchResult var20 = (ClickGuiScreen.SearchResult)var10.remove(var18);
                  if (var20 != null) {
                     this.В_e12d7b95.put(var18, var14);
                     var12.add(var20);
                  } else {
                     this.Х_c1edc18e.add(new ClickGuiScreen.IndexedSearchResult(var16, var14));
                  }
               }

               for (ClickGuiScreen.SearchResult var17 : var2) {
                  ClickGuiScreen.SearchEntry var19 = this.searchKey(var17);
                  if (var10.remove(var19) != null) {
                     this.В_2e343f72.add(var19);
                     var12.add(var17);
                  }
               }

               this.Я_c1edc18e.clear();
               this.Я_c1edc18e.addAll(var12);
               this.field_1_388008bb = var1;
               if (this.Х_c1edc18e.isEmpty() && this.В_2e343f72.isEmpty() && !this.诶_bbeebd87) {
                  this.п.setImmediately(1.0F);
                  this.В_e12d7b95.clear();
               } else {
                  this.п.setImmediately(0.0F);
               }

               this.field_6 = 0.0F;
            }
         }
      }
   }

   public void renders(HudRenderEvent.GuiRender var1) {
   }

   private void renderCategorySidebar() {
      float var1 = this.getSidebarWidth();
       sg.render.ClientLogo.drawCentered(this.Щ + var1 * схт + 2.0F,
          this.у + схЧ + 15.0F, 28.0F, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
      UiDrawing.drawRoundedRect(this.Щ + схЛ, this.у + сх诶, var1 - сх1, 1.0F, 0.0F, ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE));
      float var2 = this.getSidebarLabelAlpha();
      if (var2 < схЬ) {
         UiDrawing.drawRoundedRect(this.Щ + сх衣, this.у + схш, var1 - схИ, 1.0F, 0.0F, ThemeEditor.getColorArgb(ThemeColorSlot.LINE));
         UiDrawing.drawRoundedRect(this.Щ + схь, this.у + схж, var1 - сх弟, 1.0F, 0.0F, ThemeEditor.getColorArgb(ThemeColorSlot.LINE));
      }

      float var3 = UiDrawing.getGlobalAlpha();
      if (var2 > схб) {
         UiDrawing.pushScissor(this.Щ + схЫ, this.у + схl, Math.max(0.0F, var1 - сх8), this.ф - сх5);
         UiDrawing.setGlobalAlpha(var3 * var2);
         UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.selectLocalizedText(схв, схъ), this.Щ + схМ, this.у + схН, 11, ThemeEditor.getColorArgb(ThemeColorSlot.CATEGORY_SURTEST));
         UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.selectLocalizedText(схн, схЦ), this.Щ + схч, this.у + схЭ, 11, ThemeEditor.getColorArgb(ThemeColorSlot.CATEGORY_SURTEST));
         UiDrawing.drawText(ClientFonts.getMediumFont(), ClientLocalization.selectLocalizedText(сх必, схщ), this.Щ + схГ, this.у + схл, 11, ThemeEditor.getColorArgb(ThemeColorSlot.CATEGORY_SURTEST));
         UiDrawing.setGlobalAlpha(var3);
         UiDrawing.popScissor();
      }

      for (ModuleCategory var7 : ModuleCategory.values()) {
         float var8 = this.categoryButtonY(var7);
         if (!Float.isNaN(var8)) {
            float var9 = var7.ц().getValue();
            this.drawCategoryButton(this.Щ + схэ, var8, this.categoryButtonWidth(), схю, схЩ, var9);
            if (var2 > сху) {
               UiDrawing.pushScissor(this.Щ + сх2, var8, Math.max(0.0F, var1 - схф), схи);
               UiDrawing.setGlobalAlpha(var3 * var2);
               UiDrawing.drawText(
                  ClientFonts.getMediumFont(),
                  var7._А(),
                  this.Щ + сх9,
                  var8 + схо,
                  13,
                  ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DESELECT_CATEGORY_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.CATEGORY_TEXT), var9)
               );
               UiDrawing.setGlobalAlpha(var3);
               UiDrawing.popScissor();
            }

            UiDrawing.drawText(
               ClientFonts.getIconsFont(),
               var7._Н(),
               this.Щ + схO,
               var8 + сх7,
               13,
               ColorMath.lerpArgb(ThemeEditor.getColorArgb(ThemeColorSlot.DESELECT_CATEGORY_TEXT), ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), var9)
            );
         }
      }
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   private void drawCategoryButton(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (!(var6 <= с0У)) {
         UiDrawing.drawRoundedRect(
            var1, var2, var3, var4, var5, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SELECTED), ThemeEditor.getColorAlpha(ThemeColorSlot.SELECTED) / с0З * var6)
         );
      }
   }

   private int searchScoreLocalized(String var1, String... var2) {
      return TextSearchMatcher.score(var1, var2);
   }

   public boolean isSearchOverlayVisible() {
      return this.isSearchDropdownVisible() || this.е.getValue() > с03;
   }

   private float getSearchDropdownHeight() {
      return сЗУ;
   }

   private void closeSettingPopups() {
      for (ModuleCard var2 : this.В_c1edc18e) {
         var2.resetSettingWidgetTransientState();
      }
   }

   private void renderLayoutAccent() {
      float var1 = this.В_65c4f33c.getTransitionProgress();
      if (!(var1 >= 1.0F)) {
         float var2 = (float)Math.sin(схе * var1);
         UiDrawing.drawOutline(this.Щ, this.у, this.field_2, this.ф, 1.0F, схА, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), сх4 * var2));
         if (ClickGuiPreferences.х弟() > 0) {
            UiDrawing.drawGlare(this.Щ, this.у, this.field_2, this.ф, var1, схп * var2, схД);
         }
      }
   }

   public float getInterfaceScale() {
      float var1 = Math.min((MINECRAFT_CLIENT.getWindow().getScreenWidth() - сЕе) / this.totalGuiWidth(), (MINECRAFT_CLIENT.getWindow().getScreenHeight() - сЕА) / this.ф);
      return Math.max(сЕ4, Math.min(ClickGuiPreferences.х8(), var1));
   }

   @Override
   protected void extractBlurredBackground(GuiGraphicsExtractor var1) {
   }

   private float getSearchPanelX() {
      return this.Щ + (this.field_2 - this.getSearchExpandedWidth()) * сЗ0;
   }

   public float getSearchOverlayAlpha() {
      float var1 = Math.clamp(this.е.getValue(), 0.0F, 1.0F);
      return var1 > с0р ? 1.0F : var1;
   }

   private void renderProfileOverlay() {
      float var1 = this.Щ;
      float var2 = this.у;
      float var3 = UiDrawing.getGlobalAlpha();
      Matrix4f var4 = new Matrix4f(UiDrawing.getTransform());
      this.Щ = this.л;
      this.у = this.э;
      UiDrawing.setTransform(new Matrix4f(this.В_20497804));

      try {
         UiDrawing.setGlobalAlpha(сЕЪ * this.ю);
         this.В_98dcdd56.П(this.и, this.field_9);
      } finally {
         UiDrawing.setGlobalAlpha(var3);
         UiDrawing.setTransform(var4);
         this.Щ = var1;
         this.у = var2;
      }
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.В_4a396a32 = var1;
      this.updateCoordinates();
      Vector2f var5 = this.toGuiPosition(var2, var3);
      this.и = var5.x;
      this.field_9 = var5.y;
      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      ThemeWave.INSTANCE.beginFrame();
      this.renderBackdrop(var2, var3, this.getGuiAlpha());
      this.renderGui();
      ThemeWave.INSTANCE.renderTransition();
      this.renderSearchOverlayTopmost();
      this.renderProfileOverlay();
      this.renderHintTopmost();
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.В_98dcdd56.Т(var1.key())) {
         return true;
      }

      if (this.В_6ba12e85.цш()) {
         return this.В_6ba12e85.Ъ_bbeebd87(var1.key());
      }

      if (this.Я_e0c4705a == ModuleCategory.COSMETICS) {
         return this.В_77119740.Л(var1.key(), var1.modifiers()) ? true : super.keyPressed(var1);
      }

      if (this.Я_e0c4705a == ModuleCategory.AUTOBUY && this.В_ec501b46.к_bbeebd87(var1.key())) {
         return true;
      }

      if (this.Я_e0c4705a == ModuleCategory.THEMES && this.В_ec58113c.isPresetPickerOpen()) {
         if (var1.key() == 256) {
            this.В_ec58113c.setPresetPickerOpen(false);
            return true;
         } else {
            return true;
         }
      } else {
         if (this.Я_e0c4705a == ModuleCategory.THEMES && this.В_ec58113c.isNewPresetDialogOpen()) {
            return this.В_ec58113c.getNewPresetDialog().Ь(var1);
         }

         if (this.Я_e0c4705a != ModuleCategory.COSMETICS && var1.key() == 70 && (var1.modifiers() & 2) != 0) {
            this.field_1_bbeebd87 = true;
            this.Ь_bbeebd87 = !this.诶_388008bb.isEmpty();
            return true;
         }

         if (this.field_1_bbeebd87) {
            if (var1.key() == 65 && (var1.modifiers() & 2) != 0) {
               this.Ь_bbeebd87 = !this.诶_388008bb.isEmpty();
               return true;
            }

            if (var1.key() == 259) {
               if (this.Ь_bbeebd87) {
                  this.诶_388008bb = "";
               } else if (!this.诶_388008bb.isEmpty()) {
                  int var5 = this.诶_388008bb.offsetByCodePoints(this.诶_388008bb.length(), -1);
                  this.诶_388008bb = this.诶_388008bb.substring(0, var5);
               }

               this.Ь_bbeebd87 = false;
               this.field_6 = 0.0F;
               return true;
            } else {
               if (var1.key() == 261) {
                  this.诶_388008bb = "";
                  this.Ь_bbeebd87 = false;
                  this.field_6 = 0.0F;
                  return true;
               }

               if (var1.key() != 257 && var1.key() != 335 && var1.key() != 258) {
                  if (var1.key() == 256) {
                     this.field_1_bbeebd87 = false;
                     this.Ь_bbeebd87 = false;
                     return true;
                  } else {
                     return true;
                  }
               } else {
                  if (!this.Я_c1edc18e.isEmpty()) {
                     this.activateSearchResult(this.Я_c1edc18e.getFirst());
                  }

                  return true;
               }
            }
         } else {
            for (ModuleCard var3 : this.В_c1edc18e) {
               if (this.visibleInCurrentView(var3)) {
                  var3.keyPressed(var1.key());
               }
            }

            for (ModuleKeybindWidget var6 : this.В_b1d77d5c) {
               var6.keyPressed(var1.key());
            }

            return super.keyPressed(var1);
         }
      }
   }

   public boolean isUnderSearch(float var1, float var2, float var3, float var4) {
      if (!this.isSearchOverlayVisible()) {
         return false;
      }

      float var5 = this.getSearchPanelX();
      float var6 = this.getSearchPanelY();
      float var7 = this.getSearchExpandedWidth();
      float var8 = this.getSearchDropdownHeight();
      return var1 + var3 > var5 && var1 < var5 + var7 && var2 + var4 > var6 && var2 < var6 + var8;
   }

   private void renderProfileBlock() {
      this.В_98dcdd56.Т(this.и, this.field_9);
   }

   private void renderModuleFocus() {
      float var1 = Math.clamp(this.Д.getValue(), 0.0F, 1.0F);
      if (!(var1 < сЗЕ) && this.Х_b10e1aaa != null) {
         UiDrawing.drawRoundedRect(this.Щ, this.у, this.field_2, this.ф, сЗП, ColorMath.packArgb(3, 5, 10, сЗ西 * var1));
      }
   }

   public void setLayout(ClickGuiPreferences.Layout var1) {
      if (var1 != ClickGuiPreferences.衣()) {
         this.В_98dcdd56.Х必();
         this.closeSettingPopups();
         this.clearFocusedModule();
         this.Х_b10e1aaa = null;
         this.Д.setImmediately(0.0F);

         for (ModuleCard var3 : this.В_c1edc18e) {
            var3.setSettingsExpanded(false);
         }

         ClickGuiPreferences.衣(var1);
         this.Я_03947583.clear();
         В_03947583.clear();
         this.resetGuiScroll();
         this.closeSearch();
         this.updateCoordinates();
         if (this.Я_e0c4705a == ModuleCategory.THEMES) {
            this.В_ec58113c.restartEntranceTransitions();
         }
      }
   }

   private boolean visibleInCurrentView(ModuleCard var1) {
      return this.ш ? var1.getModule().isFavorite() : var1.getModule().getCategory() == this.Я_e0c4705a;
   }

   private float getMinScroll() {
      float var1 = this.Я_e0c4705a == ModuleCategory.COSMETICS ? this.В_77119740.ЬЮ() : this.getModuleViewportHeight();
      return Math.min(0.0F, var1 - this.Р);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      Vector2f var3 = this.toGuiPosition(var1.x(), var1.y());
      if (var1.button() == 0 && this.В_98dcdd56.Ф(var3.x, var3.y)) {
         this.clearFocusedModule();
         this.closeSearch();
         this.closeSettingPopups();
         this.Л_bbeebd87 = false;
         this.В_ec58113c.setNewPresetDialogOpen(false);
         this.В_ec58113c.setPresetPickerOpen(false);
         this.В_98dcdd56.Хг();
         return true;
      }

      if (this.В_98dcdd56.method_8(var3.x, var3.y, var1.button())) {
         return true;
      }

      if (this.В_6ba12e85.цш()) {
         this.Л_bbeebd87 = false;
         if (var1.button() == 0) {
            for (ModuleCategory var27 : ModuleCategory.values()) {
               float var30 = this.categoryButtonY(var27);
               if (!Float.isNaN(var30) && WorldProjection.containsPoint(var3.x, var3.y, this.Щ + с0Ш, var30, this.categoryButtonWidth(), с0ы)) {
                  this.В_6ba12e85.Ро();
                  this.selectCategory(var27);
                  return true;
               }
            }
         }

         return this.В_6ba12e85.й(var3.x, var3.y, var1.button());
      } else {
         if (this.Я_e0c4705a == ModuleCategory.COSMETICS) {
            return this.В_77119740.щ(var3.x, var3.y, var1.button()) ? true : super.mouseClicked(var1, var2);
         }

         boolean var4 = this.Я_e0c4705a == ModuleCategory.THEMES && this.В_ec58113c.isAnyDialogOpen();
         if (!var4 && this.isSearchDropdownVisible()) {
            float var5 = this.getSearchDropdownHeight();
            if (WorldProjection.containsPoint(var3.x, var3.y, this.getSearchPanelX(), this.getSearchPanelY(), this.getSearchExpandedWidth(), var5)) {
               if (var1.button() == 0) {
                  int var22 = this.getSearchResultIndex(var3.y);
                  if (var22 >= 0 && var22 < this.Я_c1edc18e.size()) {
                     this.activateSearchResult(this.Я_c1edc18e.get(var22));
                  }
               }

               return true;
            }

            if (var1.button() == 0) {
               this.field_1_bbeebd87 = false;
               this.Ь_bbeebd87 = false;
               return true;
            }
         }

         if (!var4 && var1.button() == 0 && WorldProjection.containsPoint(var3.x, var3.y, this.searchX(), this.у + с0й, this.getSearchWidth(), с0Ж)) {
            this.clearFocusedModule();
            this.field_1_bbeebd87 = true;
            this.Ь_bbeebd87 = false;
            return true;
         }

         for (ModuleCard var6 : this.В_c1edc18e) {
            if (this.visibleInCurrentView(var6) && var6.handlePriorityMouseButton(var3.x, var3.y, var1.button())) {
               this.Л_bbeebd87 = false;
               return true;
            }
         }

         if (var1.button() == 0 && this.Р > this.getModuleViewportHeight() && this.isScrollbarHovered(var3.x, var3.y)) {
            this.beginScrollbarDragging(var3.y);
            return true;
         }

         if (this.Я_e0c4705a != ModuleCategory.COSMETICS) {
            for (ModuleKeybindWidget var17 : this.В_b1d77d5c) {
               if (WorldProjection.containsPoint(var3.x, var3.y, var17.getX(), var17.getY(), var17.getWidth(), var17.getHeight())) {
                  var17.mouseClicked(var3.x, var3.y, var1.button());
                  return true;
               }
            }
         }

         if (!WorldProjection.containsPoint(var3.x, var3.y, this.Щ, this.у, this.field_2, this.ф)) {
            HudElementRegistry var14 = RoseClient.getInstance().getServices().getHudElements();
            Vector2f var18 = WorldProjection.guiToScreen(var1.x(), var1.y());
            if (var14 != null && (var14.dispatchMouseButton(var18.x, var18.y, var1.button()) || var14.beginDrag(var18.x, var18.y, var1.button()))) {
               return true;
            }
         }

         if (!var4 && var1.button() == 0 && this.isFavoritesButtonHovered(var3.x, var3.y)) {
            this.ш = !this.ш;
            this.field_1_bbeebd87 = false;
            this.clearFocusedModule();
            this.restoreCategoryScroll();
            this.М_6da43b94 = System.nanoTime();
            return true;
         }

         if (var1.button() == 0) {
            this.field_1_bbeebd87 = false;
            this.Ь_bbeebd87 = false;
         }

         boolean var15 = this.isModuleViewportHovered(var3.x, var3.y);
         if (var15) {
            for (ModuleCard var7 : this.В_c1edc18e) {
               if (this.visibleInCurrentView(var7) && var7.handlePriorityMouseButton(var3.x, var3.y, var1.button())) {
                  return true;
               }
            }
         }

         if (this.Я_b10e1aaa != null
            && !WorldProjection.containsPoint(
               var3.x, var3.y, this.Я_b10e1aaa.getX() - с0м, this.Я_b10e1aaa.getY() - с0К, this.Я_b10e1aaa.getWidth() + с0I, this.Я_b10e1aaa.getHeight() + с0Т
            )) {
            this.clearFocusedModule();
            return true;
         }

         for (ModuleCategory var9 : ModuleCategory.values()) {
            float var10 = this.categoryButtonY(var9);
            if (!Float.isNaN(var10) && WorldProjection.containsPoint(var3.x, var3.y, this.Щ + с0О, var10, this.categoryButtonWidth(), с0Ф)) {
               this.selectCategory(var9);
               break;
            }
         }

         if (!this.ш && this.Я_e0c4705a == ModuleCategory.AUTOBUY && this.В_ec501b46.х(var3.x, var3.y, var1.button())) {
            return true;
         }

         if (!this.ш && this.Я_e0c4705a == ModuleCategory.THEMES && this.В_ec58113c.mouseClicked(var3.x, var3.y, var1.button())) {
            return true;
         }

         boolean var21 = false;
         if (var15) {
            for (ModuleCard var28 : this.В_c1edc18e) {
               if (this.visibleInCurrentView(var28) && var28.handlePriorityMouseButton(var3.x, var3.y, var1.button())) {
                  var21 = true;
                  break;
               }
            }
         }

         if (!var21 && var15) {
            for (ModuleCard var29 : this.В_c1edc18e) {
               if (this.visibleInCurrentView(var29)) {
                  var29.mouseClicked(var3.x, var3.y, var1.button());
               }
            }
         }

         return true;
      }
   }

   private int commonPrefixLength(String var1, String var2) {
      int var3 = Math.min(var1.length(), var2.length());
      int var4 = 0;

      while (var4 < var3 && var1.charAt(var4) == var2.charAt(var4)) {
         var4++;
      }

      return var4;
   }

   private void activateSearchResult(ClickGuiScreen.SearchResult var1) {
      ModuleCard var2 = var1.component();
      this.selectCategory(var2.getModule().getCategory());
      this.о = 0.0F;
      this.O = 0.0F;
      this.В_b10e1aaa = var2;
      var2.focusSettings();
      this.field_1_bbeebd87 = false;
      this.Ь_bbeebd87 = false;
      this.诶_388008bb = "";
      this.resetSearchScroll();
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.В_98dcdd56.хП()) {
         return true;
      }

      Vector2f var9 = this.toGuiPosition(var1, var3);
      if (this.В_6ba12e85.цш()) {
         return this.В_6ba12e85.В(var9.x, var9.y, var7);
      }

      if (this.Я_e0c4705a == ModuleCategory.COSMETICS) {
         if (this.В_77119740.И(var9.x, var9.y)) {
            float var15 = this.O;
            this.O = Math.clamp(this.O + (float)var7 * с0Й, this.getMinScroll(), 0.0F);
            if (var15 != this.O) {
               ClientSounds.к(ClientSounds.GuiSound.SCROLL);
            }
         }

         return true;
      } else if (this.isSearchDropdownVisible()
         && WorldProjection.containsPoint(
            var9.x, var9.y, this.getSearchPanelX(), this.getSearchPanelY(), this.getSearchExpandedWidth(), this.getSearchDropdownHeight()
         )) {
         float var14 = this.field_6;
         this.field_6 = Math.clamp(this.field_6 - (float)var7 * с0к * 2.0F, 0.0F, this.getSearchMaxScroll());
         if (var14 != this.field_6) {
            ClientSounds.к(ClientSounds.GuiSound.SCROLL);
         }

         return true;
      } else {
         for (ModuleCard var11 : this.В_c1edc18e) {
            if (this.visibleInCurrentView(var11) && var11.mouseScrolled(var9.x, var9.y, var7)) {
               return true;
            }
         }

         this.closeSettingPopups();
         if (!this.ш && this.Я_e0c4705a == ModuleCategory.AUTOBUY && this.В_ec501b46.Л(var9.x, var9.y, (float)var7)) {
            return true;
         }

         if (this.Я_e0c4705a == ModuleCategory.THEMES && this.В_ec58113c.mouseScrolled(var9.x, var9.y, var7)) {
            return true;
         }

         if (WorldProjection.containsPoint(var9.x, var9.y, this.getModuleViewportX(), this.у + с0г, this.getModuleViewportWidth(), this.getModuleViewportHeight())) {
            float var13 = this.getMinScroll();
            float var16 = this.O;
            float var12 = this.O + (float)var7 * с00;
            this.O = Math.clamp(var12, var13, 0.0F);
            if (var16 != this.O) {
               ClientSounds.к(ClientSounds.GuiSound.SCROLL);
            }

            if (this.O == 0.0F && this.о > 0.0F) {
               this.о = 0.0F;
            }

            if (this.O == var13 && this.о < var13) {
               this.о = var13;
            }

            return true;
         } else {
            return super.mouseScrolled(var1, var3, var5, var7);
         }
      }
   }

   private float favoritesButtonX() {
      return this.searchX() - сЕl;
   }

   private float getSearchMaxScroll() {
      return Math.max(0.0F, (this.Я_c1edc18e.size() - 7) * сЗЗ);
   }

   public float getLayoutContentAlpha(int var1) {
      return 1.0F - с0ф * (float)Math.sin(с0и * this.getLayoutReveal(var1));
   }

   private void resetSearchScroll() {
      this.р = 0.0F;
      this.field_6 = 0.0F;
   }

   public float getGuiHeight() {
      return this.ф;
   }

   private void closeSearch() {
      this.field_1_bbeebd87 = false;
      this.Ь_bbeebd87 = false;
      this.诶_388008bb = "";
      this.resetSearchScroll();
   }

   public float getLayoutReveal(int var1) {
      return this.В_65c4f33c.getSectionRevealProgress(var1);
   }

   public float getModuleViewportY() {
      return this.у + сЗБ;
   }

   public float getGuiX() {
      return this.Щ;
   }

   public void focusModule(ModuleCard var1) {
      if (!this.areSettingsPinned()) {
         if (var1 == null) {
            this.clearFocusedModule();
         } else {
            if (this.Я_b10e1aaa != null && this.Я_b10e1aaa != var1) {
               this.Я_b10e1aaa.setSettingsExpanded(false);
            }

            this.Я_b10e1aaa = var1;
            this.Х_b10e1aaa = var1;
            this.closeSearch();
         }
      }
   }

   public float getGuiWidth() {
      return this.field_2;
   }

   private void renderAnimatedSearchText(float var1, float var2, int var3) {
      float var4 = Math.clamp(this.field_4.getValue(), 0.0F, 1.0F);
      if (!(var4 >= сУ8) && this.Л_a83dd0cc < this.诶_388008bb.length()) {
         int var5 = Math.min(this.Л_a83dd0cc, this.诶_388008bb.length());
         String var6 = this.诶_388008bb.substring(0, var5);
         if (!var6.isEmpty()) {
            UiDrawing.drawText(ClientFonts.getMediumFont(), var6, var1, var2, 12, var3);
         }

         float var7 = var1 + UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var6, 12);
         int var8 = var5;

         for (int var9 = 0; var8 < this.诶_388008bb.length(); var9++) {
            int var10 = this.诶_388008bb.offsetByCodePoints(var8, 1);
            String var11 = this.诶_388008bb.substring(var8, var10);
            float var12 = Math.min(сУ5, var9 * сУв);
            float var13 = Math.clamp((var4 - var12) / Math.max(сУъ, 1.0F - var12), 0.0F, 1.0F);
            float var14 = (float)Easings.CUBIC_OUT.apply(var13);
            float var15 = сУМ + сУН * var14;
            float var16 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var11, 12);
            float var17 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var11, var15, 1.0F);
            UiDrawing.drawText(
               ClientFonts.getMediumFont(),
               var11,
               var7 + (var16 - var17) * сУн,
               var2 + (1.0F - var14) * сУЦ,
               var15,
               ColorMath.withAlpha(var3, ColorMath.getAlpha(var3) / сУч * var14),
               0.0F,
               сУЭ,
               1.0F
            );
            var7 += var16;
            var8 = var10;
         }
      } else {
         UiDrawing.drawText(ClientFonts.getMediumFont(), this.诶_388008bb, var1, var2, 12, var3);
      }
   }

   private float getSearchExpandedWidth() {
      return Math.min(сЗк, this.field_2 - сЗг);
   }

   private void renderDepartingSearchResults(float var1, float var2, float var3, float var4, float var5) {
      float var6 = Math.clamp(this.А_at_c0aafeb1.getValue(), 0.0F, 1.0F);
      float var7 = Math.clamp(var4 * (1.0F - var5), 0.0F, 1.0F);
      if (!(var7 <= сЗВ)) {
         if (this.Х_c1edc18e.isEmpty()) {
            if (this.诶_bbeebd87) {
               float var11 = var7 * var6;
               UiDrawing.drawText(
                  ClientFonts.getMediumFont(),
                  ClientLocalization.translate(сЗЯ),
                  var1 + сЗХ - сЗе * var5,
                  var3 + сЗА + (1.0F - var6) * сЗ4,
                  12,
                  ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorAlpha(ThemeColorSlot.DEACTIVE_TEXT) / сЗп * var11)
               );
            }
         } else {
            for (ClickGuiScreen.IndexedSearchResult var9 : this.Х_c1edc18e) {
               float var10 = var3 + var9.index() * сЗД - this.р;
               if (!(var10 + сЗЪ < var3) && !(var10 > var3 + сЗт)) {
                  this.drawSearchResultRow(var9.result(), var1 + сЗа - сЗЮ * var5, var10, var2, var7 * var6, false);
               }
            }
         }
      }
   }

   private float guiLeftInset() {
      float var1 = this.В_98dcdd56.б诶();
      return this.Я_e0c4705a == ModuleCategory.COSMETICS ? Math.max(с0O, var1) : var1;
   }

   public void adjustAutoBuyScroll(float var1) {
      this.O = Math.clamp(this.O + var1 * с0_, this.getMinScroll(), 0.0F);
   }

   private void renderSearchDropdown(float var1) {
      float var2 = this.getSearchPanelX();
      float var3 = this.getSearchPanelY();
      float var4 = this.getSearchExpandedWidth();
      float var5 = this.getSearchDropdownHeight();
      float var6 = (float)Easings.CUBIC_OUT.apply(var1);
      float var7 = сУГ + сУл * var6;
      WorldProjection.scaleAroundRect(var2, var3, var4, var5, var7);
      float var8 = UiDrawing.getGlobalAlpha();
      UiDrawing.setGlobalAlpha(сУэ);
      UiDrawing.drawShadow(
         var2, var3, var4, var5, сУю, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SHADOW), ThemeEditor.getColorAlpha(ThemeColorSlot.SHADOW) / сУЩ * var6)
      );
      int var9 = ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND);
      float var10 = this.isSearchDropdownVisible() ? сУу : сУ2 * var1;
      UiDrawing.drawBlur(var2, var3, var4, var5, сУф, сУи, ColorMath.packArgb(255, 255, 255, var10));
      UiDrawing.drawRoundedRect(var2, var3, var4, var5, сУ9, ColorMath.withAlpha(var9, ThemeEditor.getColorAlpha(ThemeColorSlot.BACKGROUND) / сУо * сУO * var6));
      UiDrawing.drawOutline(
         var2, var3, var4, var5, 1.0F, сУ7, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE), ThemeEditor.getColorAlpha(ThemeColorSlot.OUTLINE) / сУР * var6)
      );
      UiDrawing.setGlobalAlpha(var8);
      UiDrawing.drawText(
         ClientFonts.getIconsFont(),
         сУ3,
         var2 + сУр,
         var3 + сУ6,
         11,
         ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), ThemeEditor.getColorAlpha(ThemeColorSlot.MAIN) / сУ_ * var1)
      );
      float var11 = Math.clamp(this.п.getValue(), 0.0F, 1.0F);
      float var12 = (float)Easings.CUBIC_OUT.apply(var11);
      boolean var13 = !this.Х_c1edc18e.isEmpty() || this.诶_bbeebd87 || !this.В_2e343f72.isEmpty();
      if (var13 && var12 < сУШ) {
         this.drawSearchTitle(var2, var3, this.Ч_a83dd0cc, var1 * (1.0F - var12));
         this.drawSearchTitle(var2, var3, this.Я_c1edc18e.size(), var1 * var12);
      } else {
         this.drawSearchTitle(var2, var3, this.Я_c1edc18e.size(), var1);
      }

      String var14 = "TAB — " + ClientLocalization.translate(сУы);
      float var15 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var14, 10);
      UiDrawing.drawText(
         ClientFonts.getMediumFont(),
         var14,
         var2 + var4 - var15 - сУй,
         var3 + сУЖ,
         10,
         ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorAlpha(ThemeColorSlot.DEACTIVE_TEXT) / сУм * var1)
      );
      UiDrawing.drawRoundedRect(
         var2 + сУК,
         var3 + сУI - сУТ,
         var4 - сУО,
         1.0F,
         сУФ,
         ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.LINE), ThemeEditor.getColorAlpha(ThemeColorSlot.LINE) / сУЙ * var1)
      );
      float var16 = var3 + сУк;
      float var17 = var5 - сУг - сУ0;
      UiDrawing.setScissor(var2 + сУх, var16, var4 - сУУ, var17);
      if ((!this.Х_c1edc18e.isEmpty() || this.诶_bbeebd87) && var12 < сУЗ) {
         this.renderDepartingSearchResults(var2, var4, var16, var1, var12);
      }

      this.renderCurrentSearchResults(var2, var4, var16, var1, var11, var13);
      UiDrawing.disableScissor();
      float var18 = this.getSearchMaxScroll();
      if (var18 > 0.0F) {
         float var19 = сУЕ;
         float var20 = this.Я_c1edc18e.size() * сУП;
         float var21 = Math.max(сУ西, var19 * var19 / var20);
         float var22 = var19 - var21;
         float var23 = this.р / var18;
         UiDrawing.drawRoundedRect(
            var2 + var4 - сУд,
            var16 + var22 * var23,
            2.0F,
            var21,
            1.0F,
            ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.SCROLL), ThemeEditor.getColorAlpha(ThemeColorSlot.SCROLL) / сУБ * var1)
         );
      }

      WorldProjection.unscaleAroundRect(var2, var3, var4, var5, var7);
   }

   public float getSidebarLabelAlpha() {
      return this.В_65c4f33c.getSidebarLabelAlpha();
   }

   public float getGuiAlpha() {
      return Math.clamp(this.В_4bd80188.getInterpolatedValue(), 0.0F, 1.0F);
   }

   private Vector2f toGuiPosition(double var1, double var3) {
      Vector2f var5 = WorldProjection.guiToScreen(var1, var3);
      float var6 = this.getInterfaceScale();
      float var7 = MINECRAFT_CLIENT.getWindow().getScreenWidth() * сЕп;
      float var8 = MINECRAFT_CLIENT.getWindow().getScreenHeight() * сЕД;
      return new Vector2f(var7 + (var5.x - var7) / var6, var8 + (var5.y - var8) / var6);
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.В_98dcdd56.хП()) {
         return true;
      }

      if (this.В_6ba12e85.цш()) {
         return this.В_6ba12e85.й(var1.codepointAsString(), var1.codepoint(), KeyboardModifiers.Ф5());
      }

      if (this.Я_e0c4705a != ModuleCategory.COSMETICS) {
         if (this.Я_e0c4705a == ModuleCategory.AUTOBUY && this.В_ec501b46.Н(var1.codepointAsString())) {
            return true;
         }

         if (this.Я_e0c4705a == ModuleCategory.THEMES && this.В_ec58113c.isNewPresetDialogOpen()) {
            return this.В_ec58113c.getNewPresetDialog().т(var1.codepointAsString());
         }

         if (this.field_1_bbeebd87) {
            if (!Character.isISOControl(var1.codepoint())) {
               if (this.Ь_bbeebd87) {
                  this.诶_388008bb = "";
                  this.Ь_bbeebd87 = false;
               }

               if (this.诶_388008bb.codePointCount(0, this.诶_388008bb.length()) < 64) {
                  this.诶_388008bb = this.诶_388008bb + var1.codepointAsString();
                  this.field_6 = 0.0F;
                  ClientSounds.Чж();
               }
            }

            return true;
         } else {
            for (ModuleCard var3 : this.В_c1edc18e) {
               if (this.visibleInCurrentView(var3)) {
                  var3.charTyped(var1.codepointAsString(), var1.codepoint(), KeyboardModifiers.Ф5());
               }
            }

            return super.charTyped(var1);
         }
      } else {
         return this.В_77119740.O(var1.codepointAsString()) || super.charTyped(var1);
      }
   }

   private boolean isModuleViewportHovered(float var1, float var2) {
      return WorldProjection.containsPoint(var1, var2, this.getModuleViewportX(), this.у + с0х, this.getModuleViewportWidth(), this.getModuleViewportHeight());
   }

   private void restoreCategoryScroll() {
      float var1 = this.Я_03947583.getOrDefault(this.Я_e0c4705a, 0.0F);
      this.о = var1;
      this.O = var1;
      this.Л_bbeebd87 = false;
   }

   private void updateScrollbar(float var1) {
      float var2 = this.getScrollbarOverflow();
      float var3 = this.getScrollbarThumbHeight();
      float var4 = this.getModuleViewportHeight() - var3;
      if (!(var2 <= 0.0F) && !(var4 <= 0.0F)) {
         float var5 = Math.clamp((var1 - this.field_3 - (this.у + сЕ诶)) / var4, 0.0F, 1.0F);
         float var6 = this.O;
         this.о = -var5 * var2;
         this.O = this.о;
         if (var6 != this.O) {
            ClientSounds.к(ClientSounds.GuiSound.SCROLL);
         }
      } else {
         this.о = 0.0F;
         this.O = 0.0F;
         this.Л_bbeebd87 = false;
      }
   }

   private boolean isSearchDropdownVisible() {
      return this.field_1_bbeebd87 && !this.诶_388008bb.isBlank();
   }

   private int searchScore(String var1, String var2, String var3) {
      return this.searchScoreLocalized(var1, var2, var3);
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      Vector2f var2 = this.toGuiPosition(var1.x(), var1.y());
      if (var1.button() == 0) {
         HudElementRegistry var3 = RoseClient.getInstance().getServices().getHudElements();
         if (var3 != null) {
            var3.finishDrag();
         }
      }

      if (this.В_98dcdd56.хП()) {
         if (var1.button() == 0) {
            this.В_98dcdd56.Х西();
         }

         return true;
      } else {
         if (this.В_6ba12e85.цш()) {
            return this.В_6ba12e85.Ю(var2.x, var2.y, var1.button());
         }

         if (var1.button() == 0) {
            this.Л_bbeebd87 = false;
         }

         if (this.Я_e0c4705a == ModuleCategory.THEMES) {
            this.В_ec58113c.mouseReleased(var2.x, var2.y, var1.button());
            return true;
         }

         for (ModuleCard var4 : this.В_c1edc18e) {
            if (this.visibleInCurrentView(var4)) {
               var4.mouseReleased(var2.x, var2.y, var1.button());
            }
         }

         return true;
      }
   }

   public float getModuleViewportWidth() {
      return this.field_2 - this.getSidebarWidth() - 2.0F;
   }

   private ClickGuiScreen.SearchEntry searchKey(ClickGuiScreen.SearchResult var1) {
      return new ClickGuiScreen.SearchEntry(var1.component(), var1.setting());
   }

   private String fitSearchText(String var1, float var2, int var3) {
      if (UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, var3) <= var2) {
         return var1;
      }

      String var4 = var1;

      while (!var4.isEmpty() && UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var4 + "...", var3) > var2) {
         var4 = var4.substring(0, var4.length() - 1);
      }

      return var4 + "...";
   }

   private float totalGuiWidth() {
      return this.field_2 + this.guiLeftInset() * (this.Я_e0c4705a == ModuleCategory.COSMETICS ? 2.0F : 1.0F);
   }

   @Override
   public void onClose() {
      if (this.Ч_bbeebd87) {
         ClientSounds.к(ClientSounds.GuiSound.CLOSE);
      }

      this.Ч_bbeebd87 = false;
      this.В_98dcdd56.Х必();
      this.В_77119740.method_36();
      this.saveCategoryScroll();
      this.Ю_bbeebd87 = false;
   }

   public float getGuiY() {
      return this.у;
   }

   private float getSearchWidth() {
      return сЗЙ;
   }

   @Override
   protected void extractPanorama(GuiGraphicsExtractor var1, float var2) {
   }

   public ClickGuiScreen() {
      super(net.minecraft.network.chat.Component.nullToEmpty(сгр));
      this.Х_04d91340 = new FloatAnimation(0.0F, сг6, Easings.CUBIC_OUT);
      this.е = new FloatAnimation(0.0F, сг_, Easings.CUBIC_OUT);
      this.А_at_c0aafeb1 = new FloatAnimation(0.0F, сгШ, Easings.CUBIC_OUT);
      this.field_4 = new FloatAnimation(1.0F, сгы, Easings.CUBIC_OUT);
      this.п = new FloatAnimation(1.0F, сгй, Easings.CUBIC_OUT);
      this.Д = new FloatAnimation(0.0F, сгЖ, Easings.CUBIC_OUT);
      this.Я_c1edc18e = new ArrayList<>();
      this.Х_c1edc18e = new ArrayList<>();
      this.В_2e343f72 = new HashSet<>();
      this.В_e12d7b95 = new HashMap<>();
      this.Я_03947583 = new EnumMap<>(ModuleCategory.class);
      this.Ю_bbeebd87 = true;
      this.М_6da43b94 = System.nanoTime();
      this.诶_388008bb = "";
      this.Ь_388008bb = "";
      this.Ш = Float.NaN;
      this.衣_388008bb = "";
      this.Ж = 1.0F;
      this.м = 1.0F;
      this.Я_e0c4705a = В_e0c4705a;
      this.Ъ = new FloatAnimation(0.0F, сгм, Easings.CUBIC_OUT);
      this.Х_f8db0d09 = this::renders;
      this.Я_03947583.putAll(В_03947583);

      for (ClientModule var2 : RoseClient.getInstance().getServices().getModules()) {
         this.В_c1edc18e.add(new ModuleCard(var2, this));
      }

      RoseClient.getInstance().getEventBus().register(this);
   }

   @Override
   public void tick() {
      this.updateCoordinates();
      this.В_4bd80188.tick(this.Ю_bbeebd87);

      for (ModuleCard var2 : this.В_c1edc18e) {
         var2.tick();
      }

      if (this.В_6ba12e85.цш()) {
         this.В_6ba12e85.Р8();
      }

      if (this.Я_e0c4705a == ModuleCategory.THEMES) {
         this.В_ec58113c.tick();
      }

      for (ModuleKeybindWidget var4 : this.В_b1d77d5c) {
         var4.tick();
      }

      if (!this.Ю_bbeebd87 && this.В_4bd80188.getInterpolatedValue() < сЗд) {
         this.Ю_bbeebd87 = true;
          this.minecraft.gui.setScreen(RoseClient.getInstance().getSelfcodeService().returnScreen());
      }
   }

   private float categoryButtonWidth() {
      return this.getSidebarWidth() - с07;
   }

   public void renderBackdrop(int var1, int var2, float var3) {
      ClickGui var4 = RoseClient.getInstance().getServices().getModules().getClickGui();
      if (var4.иШ() && ClickGuiPreferences.х弟() == 2) {
         this.В_9d0ffde2
            .з(
               var1 * MINECRAFT_CLIENT.getWindow().getGuiScale(),
               var2 * MINECRAFT_CLIENT.getWindow().getGuiScale(),
               var3,
               ThemeEditor.getColorArgb(ThemeColorSlot.MAIN),
               ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO)
            );
      }
   }

   @Override
   protected void extractMenuBackground(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5) {
   }

   public void openEntityEsp1Editor(EntityESP var1) {
      this.clearFocusedModule();
      this.closeSearch();
      ClientSounds.к(ClientSounds.GuiSound.EDITOR);
      this.В_6ba12e85.й(var1);
   }

   public float getSidebarWidth() {
      return this.В_65c4f33c.getSidebarWidth();
   }

   public boolean intersectsModuleViewport(float var1, float var2, float var3, float var4) {
      float var5 = this.getModuleViewportX();
      float var6 = this.getModuleViewportY();
      float var7 = var5 + this.getModuleViewportWidth();
      float var8 = var6 + this.getModuleViewportHeight();
      return var1 + var3 > var5 && var1 < var7 && var2 + var4 > var6 && var2 < var8;
   }

   private boolean isFavoritesButtonHovered(float var1, float var2) {
      return this.Я_e0c4705a != ModuleCategory.COSMETICS && WorldProjection.containsPoint(var1, var2, this.favoritesButtonX(), this.у + сЕ8, сЕ5, сЕв);
   }

   private void renderCurrentSearchResults(float var1, float var2, float var3, float var4, float var5, boolean var6) {
      float var7 = Math.clamp(this.А_at_c0aafeb1.getValue(), 0.0F, 1.0F);
      float var8 = var5 * var5 * (сЗЧ - 2.0F * var5);
      if (this.Я_c1edc18e.isEmpty()) {
         float var28 = var4 * var7 * (var6 ? var8 : 1.0F);
         if (!(var28 <= сЗЛ)) {
            UiDrawing.drawText(
               ClientFonts.getMediumFont(),
               ClientLocalization.translate(сЗ诶),
               var1 + сЗ1 + (var6 ? сЗЬ * (1.0F - var8) : 0.0F),
               var3 + сЗ衣 + (1.0F - var7) * сЗш,
               12,
               ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT), ThemeEditor.getColorAlpha(ThemeColorSlot.DEACTIVE_TEXT) / сЗИ * var28)
            );
         }
      } else {
         float var9 = Math.clamp((var5 - сЗь) / сЗж, 0.0F, 1.0F);
         var9 = var9 * var9 * (сЗ弟 - 2.0F * var9);
         int var10 = Math.max(0, (int)(this.р / сЗб));
         int var11 = Math.min(this.Я_c1edc18e.size(), var10 + 7 + 2);
         int var12 = 0;

         for (int var13 = var10; var13 < var11; var13++) {
            ClickGuiScreen.SearchResult var14 = this.Я_c1edc18e.get(var13);
            ClickGuiScreen.SearchEntry var15 = this.searchKey(var14);
            boolean var16 = this.В_2e343f72.contains(var15);
            float var17 = 1.0F;
            float var18 = 0.0F;
            if (var16 && var6) {
               float var19 = Math.min(сЗЫ, var12++ * сЗl);
               float var20 = Math.clamp((var5 - var19) / Math.max(сЗ8, 1.0F - var19), 0.0F, 1.0F);
               float var21 = var20 * var20 * (сЗ5 - 2.0F * var20);
               var17 = var21;
               var18 = сЗв * (1.0F - var21);
            }

            Integer var29 = this.В_e12d7b95.get(var15);
            float var30 = var29 == null ? var13 : var29.intValue() + (var13 - var29) * var9;
            float var31 = var3 + var30 * сЗъ - this.р;
            if (!(var31 + сЗМ < var3) && !(var31 > var3 + сЗН)) {
               float var22 = Math.min(сЗн, (var13 - var10) * сЗЦ);
               float var23 = Math.clamp((var7 - var22) / Math.max(сЗч, 1.0F - var22), 0.0F, 1.0F);
               float var24 = var4 * var17 * (float)Easings.CUBIC_OUT.apply(var23);
               float var25 = var1 + сЗЭ + var18 + (1.0F - var7) * сЗ必;
               boolean var26 = (!var6 || var5 > сЗщ) && WorldProjection.containsPoint(this.и, this.field_9, var25, var31 + 2.0F, var2 - сЗГ, сЗл);
               this.drawSearchResultRow(var14, var25, var31, var2, var24, var26);
            }
         }
      }
   }

   public float getModuleViewportHeight() {
      return this.ф - сЗС;
   }

   public void resetGuiScroll() {
      this.о = 0.0F;
      this.O = 0.0F;
      this.Я_03947583.put(this.Я_e0c4705a, 0.0F);
      this.Л_bbeebd87 = false;
   }

   private float getScrollbarOverflow() {
      return Math.max(0.0F, this.Р - this.getModuleViewportHeight());
   }

   private void renderBreadcrumb() {
      float var1 = this.у + с0Е;
      int var2 = ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT);
      int var3 = ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT);
      int var4 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
       sg.render.ClientLogo.drawInBox(this.Щ + this.getSidebarWidth() + с0西 + 0.75F, var1 - 4.15F, 20.5F, 20.5F, var2);
      UiDrawing.drawText(ClientFonts.getMediumFont(), с0д, this.Щ + this.getSidebarWidth() + с0Б, var1, 13, var2);
      float var5 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), с0С, 13);
      float var6 = this.Щ + this.getSidebarWidth() + с0з + var5;
      UiDrawing.drawText(ClientFonts.getMediumFont(), с0ц, var6, var1, 13, var2);
      String var7 = this.ш ? ClientLocalization.selectLocalizedText(с0я, схс) : this.Я_e0c4705a._А();
      String var8 = this.ш ? схВ : this.Я_e0c4705a._Н();
      UiDrawing.drawText(ClientFonts.getIconsFont(), var8, var6 + схЯ, var1, 13, var4);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var7, var6 + схХ, var1, 13, var3);
   }

   private float getModuleEntrancePhase(int var1) {
      double var2 = (System.nanoTime() - this.М_6da43b94) / сЕ弟 - var1 * сЕб;
      return (float)Math.clamp(var2 / сЕЫ, 0.0, 1.0);
   }

   private void beginScrollbarDragging(float var1) {
      float var2 = this.getScrollbarOverflow();
      if (var2 <= 0.0F) {
         this.Л_bbeebd87 = false;
      } else {
         float var3 = this.getScrollbarThumbHeight();
         float var4 = this.getModuleViewportHeight() - var3;
         float var5 = -this.о / var2;
         float var6 = this.у + сЕЛ + var4 * var5;
         this.field_3 = var1 >= var6 && var1 <= var6 + var3 ? var1 - var6 : var3 / 2.0F;
         this.Л_bbeebd87 = true;
         this.updateScrollbar(var1);
      }
   }

   private boolean isScrollbarHovered(float var1, float var2) {
      return WorldProjection.containsPoint(var1, var2, this.Щ + this.field_2 - сЕа, this.у + сЕЮ, сЕЧ, this.getModuleViewportHeight());
   }

   private int getSearchResultIndex(float var1) {
      float var2 = this.getSearchPanelY() + сЗО;
      float var3 = var1 - var2 + this.р;
      return var3 < 0.0F ? -1 : (int)(var3 / сЗФ);
   }

   @Override
   public void clearFocus() {
   }

   private float searchX() {
      return this.Щ + this.field_2 - с0Р - this.getSearchWidth();
   }

   private void renderSearchOverlayTopmost() {
      float var1 = Math.clamp(this.е.getValue(), 0.0F, 1.0F);
      if (this.衣_bbeebd87 && !(var1 <= сУ必)) {
         float var2 = this.Щ;
         float var3 = this.у;
         float var4 = UiDrawing.getGlobalAlpha();
         this.Щ = this.ы;
         this.у = this.й;
         WorldProjection.scaleAroundRect(this.Щ - this.guiLeftInset(), this.у, this.totalGuiWidth(), this.ф, this.Ж);
         UiDrawing.setGlobalAlpha(сУщ * this.м);
         this.renderSearchDropdown(var1);
         UiDrawing.setGlobalAlpha(var4);
         WorldProjection.unscaleAroundRect(this.Щ - this.guiLeftInset(), this.у, this.totalGuiWidth(), this.ф, this.Ж);
         this.Щ = var2;
         this.у = var3;
      }
   }

   private void saveCategoryScroll() {
      this.Я_03947583.put(this.Я_e0c4705a, this.O);
      В_03947583.put(this.Я_e0c4705a, this.O);
      В_e0c4705a = this.Я_e0c4705a == ModuleCategory.COSMETICS ? ModuleCategory.COMBAT : this.Я_e0c4705a;
   }

   public boolean areSettingsPinned() {
      return ClickGuiPreferences.衣().ПЪ();
   }

   private void updateCoordinates() {
      this.В_98dcdd56.ХУ();
      boolean var1 = this.isExpandedLayout();
      float var2 = this.isSidebarCompact() ? сЗз : (this.Я_e0c4705a == ModuleCategory.COSMETICS ? сЗц : сЗя);
      this.В_65c4f33c.updateTargets(var1 ? сЕс : сЕВ, var1 ? сЕЯ : сЕХ, var2);
      this.field_2 = this.В_65c4f33c.getContentWidth();
      this.ф = this.В_65c4f33c.getGuiHeight();
      this.Щ = MINECRAFT_CLIENT.getWindow().getScreenWidth() / 2.0F - this.totalGuiWidth() / 2.0F + this.guiLeftInset();
      this.у = MINECRAFT_CLIENT.getWindow().getScreenHeight() / 2.0F - this.ф / 2.0F;
   }

   private float categoryButtonY(ModuleCategory var1) {
      return this.у + switch (var1) {
         case COMBAT -> сЕ1;
         case MOVEMENT -> сЕЬ;
         case RENDER -> сЕ衣;
         case MISC -> сЕш;
         case AUTOBUY -> сЕИ;
         case COSMETICS -> сЕь;
         case THEMES -> сЕж;
      };
   }

   public boolean isSidebarCompact() {
      return ClickGuiPreferences.衣().Пж() && !this.В_6ba12e85.цш();
   }

   public float getModuleViewportX() {
      return this.Щ + this.getSidebarWidth() + 2.0F;
   }

   private float getScrollbarThumbHeight() {
      return Math.max(сЕт, this.getModuleViewportHeight() * this.getModuleViewportHeight() / Math.max(1.0F, this.Р));
   }

   public AutoBuyPanel getAutoBuyPanel() {
      return this.В_ec501b46;
   }

   public CosmeticsPanel getCosmeticsPanel() {
      return this.В_77119740;
   }

   public ThemeEditor getThemeEditor() {
      return this.В_ec58113c;
   }

   public void selectCategory(ModuleCategory var1) {
      if (this.Я_e0c4705a != var1) {
         ClientSounds.к(ClientSounds.GuiSound.TAB, (float)var1.ordinal() / Math.max(1, ModuleCategory.values().length - 1));
         this.saveCategoryScroll();
         this.Я_e0c4705a = var1;
         this.ш = false;
         В_e0c4705a = var1 == ModuleCategory.COSMETICS ? ModuleCategory.COMBAT : var1;
         this.updateCoordinates();
         this.restoreCategoryScroll();
         this.М_6da43b94 = System.nanoTime();
         this.Л_bbeebd87 = false;
         this.clearFocusedModule();
         if (var1 == ModuleCategory.THEMES) {
            this.В_ec58113c.restartEntranceTransitions();
         }
      }
   }

   @Generated
   public GuiGraphicsExtractor getFrameContext() {
      return this.В_4a396a32;
   }

   private record SearchEntry(ModuleCard component, ValueSetting<?> setting) {
   }

   private record IndexedSearchResult(ClickGuiScreen.SearchResult result, int index) {
   }

   private record SearchResult(ModuleCard component, ValueSetting<?> setting, int score) {
   }
}
