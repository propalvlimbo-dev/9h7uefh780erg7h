package sg.messenger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.textures.FilterMode;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import settings.render.ClientResourceIds;
import sg.ec.MessengerIcons;
import sg.ec.ClientFonts;
import sg.modules.impl.init.ClickGuiPreferences;
import sg.gui.click.ClickGuiScreen;
import sg.network.IrcRoles;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ColorMath;
import sg.animation.Easings;
import sg.animation.FloatAnimation;
import sg.util.time.ThemeEditor;

public final class MessengerScreen extends Screen {
   private static final float Вп = 0.7F;
   private static final float ВД = 20.0F;
   private static final float ВЪ = 27.0F;
   private static final float Вт = 16.0F;
   private static final float Ва = 38.0F;
   private static final float ВЮ = 34.0F;
   private static final float ВЧ = 24.0F;
   private static final float ВЛ = 16.0F;
   private static final float В诶 = 6.0F;
   private static final float В1 = 15.0F;
   private static final float ВЬ = 28.0F;
   private static final float В衣 = 18.0F;
   private static final float Вш = 12.0F;
   private static final int сФ = -1;
   private int сЙ;
   private int ск;
   private int сг;
   private int с0;
   private int сх;
   private int сУ;
   private int сЗ;
   private int сЕ;
   private int сП;
   private int с西;
   private int сд;
   private int сБ;
   private int сС;
   private int сз;
   private final Map<String, String> ь = new HashMap<>();
   private final Map<String, JsonObject> ж = new HashMap<>();
   @SerializedName("сХ")
   private long сХ_6da43b94;
   private final long се;
   private float ВИ;
   private static final Map<String, Identifier> 弟 = new HashMap<>();
   private final Map<Long, String> б;
   private static final String[] А_0447afad = new String[]{MessengerScreen.ЯДЙ, MessengerScreen.ЯДк, MessengerScreen.ЯДг, MessengerScreen.ЯД0, MessengerScreen.ЯДх, MessengerScreen.ЯДУ, MessengerScreen.ЯДЗ};
   private static final Identifier[] В_e1fe312e = IntStream.range(0, 7).mapToObj(var0 -> ClientResourceIds.getImageId("messenger/reaction_" + var0)).toArray(Identifier[]::new);
   private static final Vector4f[] В_ef6a4edb = new Vector4f[]{
      new Vector4f(MessengerScreen.ЯДЕ, MessengerScreen.ЯДП, MessengerScreen.ЯД西, MessengerScreen.ЯДд).div(MessengerScreen.ЯДБ),
      new Vector4f(MessengerScreen.ЯДС, 0.0F, MessengerScreen.ЯДз, MessengerScreen.ЯДц).div(MessengerScreen.ЯДя),
      new Vector4f(MessengerScreen.ЯЪс, 0.0F, MessengerScreen.ЯЪВ, MessengerScreen.ЯЪЯ).div(MessengerScreen.ЯЪХ),
      new Vector4f(MessengerScreen.ЯЪе, 0.0F, MessengerScreen.ЯЪА, MessengerScreen.ЯЪ4).div(MessengerScreen.ЯЪп),
      new Vector4f(MessengerScreen.ЯЪД, 0.0F, MessengerScreen.ЯЪЪ, MessengerScreen.ЯЪт).div(MessengerScreen.ЯЪа),
      new Vector4f(MessengerScreen.ЯЪЮ, 2.0F, MessengerScreen.ЯЪЧ, MessengerScreen.ЯЪЛ).div(MessengerScreen.ЯЪ诶),
      new Vector4f(MessengerScreen.ЯЪ1, 0.0F, MessengerScreen.ЯЪЬ, MessengerScreen.ЯЪ衣).div(MessengerScreen.ЯЪш)
   };
   private static final DateTimeFormatter В_0551e0cc = DateTimeFormatter.ofPattern(MessengerScreen.ЯЪИ);
   @SerializedName("Я")
   private final Screen Я_af10102c;
   @SerializedName("Я")
   private final MessengerClient Я_8dfd5497;
   @SerializedName("5")
   private final List<MessengerScreen.HitRegion> field_5;
   private final Map<String, EditBox> Ы;
   private final Map<String, Identifier> l;
   private final Set<String> т;
   private final Set<Long> а;
   private static final Map<String, List<String>> field_8 = new LinkedHashMap<>();
   @SerializedName("В")
   private EditBox В_00e801f9;
   @SerializedName("Я")
   private EditBox Я_00e801f9;
   @SerializedName("Х")
   private EditBox Х_00e801f9;
   private String 西;
   private String д;
   @SerializedName("А")
   private JsonObject А_e1451646;
   @SerializedName("4")
   private JsonObject field_4;
   private JsonObject п;
   private JsonObject Д;
   private JsonObject Ъ;
   private float Вь;
   private float Вж;
   private float В弟;
   private float Вб;
   private float ВЫ;
   private float Вl;
   private float В8;
   private float В5;
   private float Вв;
   private float Въ;
   private float ВМ;
   private float ВН;
   private float Вн;
   private long сА;
   private float ВЦ;
   private boolean я;
   private boolean сс;
   private boolean сВ;
   private boolean сЯ;
   @SerializedName("сХ")
   private boolean сХ_bbeebd87;
   private final FloatAnimation ъ;
   private final FloatAnimation М;
   private final FloatAnimation Н;
   private final FloatAnimation н;
   @SerializedName("В")
   private final MessengerScreen.OverlayTransition В_c38ce5db;
   @SerializedName("Я")
   private final MessengerScreen.OverlayTransition Я_c38ce5db;
   @SerializedName("Х")
   private final MessengerScreen.OverlayTransition Х_c38ce5db;
   @SerializedName("В")
   private final FloatAnimation[] В_b9e95bbb;
   private String Б;
   private final List<MessengerScreen.PendingMessage> в;
   private static final String ВСр = "Мессенджер";
   private static final long ВС6 = -1L;
   private static final String ВС_ = "global";
   private static final long ВСШ = -1L;
   private static final float ВСы = 10.0F;
   private static final float ВСй = 12.0F;
   private static final float ВСЖ = 12.0F;
   private static final float ВСм = 12.0F;
   private static final float ВСК = 0.28F;
   private static final float ВСI = 0.2F;
   private static final float ВСТ = 0.55F;
   private static final float ВСО = 0.22F;
   private static final float ВСФ = 255.0F;
   private static final float ВСЙ = 0.12F;
   private static final String ВСк = "chats";
   private static final String ВСг = "profile";
   private static final String ВС0 = "people";
   private static final String ВСх = "peer";
   private static final String ВСУ = "self";
   private static final String ВСЗ = "id";
   private static final String ВСЕ = "role";
   private static final String ВСП = "role";
   private static final String ВС西 = "author";
   private static final String ВСд = "DEFAULT";
   private static final String ВСБ = "MEDIA";
   private static final String ВСС = "YOUTUBER";
   private static final float ВСз = 15.0F;
   private static final float ВСц = 1.5F;
   private static final String ВСя = "ADMIN";
   private static final String Взс = "BETA";
   private static final String ВзВ = "YOUTUBER";
   private static final int ВзЯ = -36495;
   private static final int ВзХ = -10101362;
   private static final int Взе = -8992001;
   private static final String ВзА = "author";
   private static final String Вз4 = "id";
   private static final String Взп = "id";
   private static final String ВзД = "id";
   private static final String ВзЪ = "deleted";
   private static final String Взт = "ADMIN";
   private static final String Вза = "BETA";
   private static final String ВзЮ = "YOUTUBER";
   private static final int ВзЧ = -2424858;
   private static final int ВзЛ = -2494209;
   private static final float Вз诶 = 15.0F;
   private static final float Вз1 = 15.0F;
   private static final String ВзЬ = "name";
   private static final String Вз衣 = "author";
   private static final float Взш = 8.0F;
   private static final float ВзИ = 15.0F;
   private static final float Взь = 8.0F;
   private static final float Взж = 15.0F;
   private static final float Вз弟 = 0.001F;
   private static final long Взб = 2000000000L;
   private static final float ВзЫ = 2.0E9F;
   private static final float Взl = 32.0F;
   private static final float Вз8 = 800.0F;
   private static final float Вз5 = 32.0F;
   private static final float Взв = 504.0F;
   private static final float Взъ = 0.1F;
   private static final float ВзМ = 0.7F;
   private static final float ВзН = 0.97F;
   private static final float Взн = 0.03F;
   private static final float ВзЦ = 1142.8572F;
   private static final float Взч = 720.0F;
   private static final float ВзЭ = 330.0F;
   private static final float Вз必 = 12.0F;
   private static final float Взщ = 255.0F;
   private static final float ВзГ = 24.0F;
   private static final float Взл = 31.428572F;
   private static final float Взэ = 28.571428F;
   private static final float Взю = 28.571428F;
   private static final float ВзЩ = 28.571428F;
   private static final float Взу = 0.001F;
   private static final float Вз2 = 18.0F;
   private static final float Взф = 36.0F;
   private static final float Взи = 16.0F;
   private static final float Вз9 = 19.0F;
   private static final float Взо = 44.0F;
   private static final String ВзO = "back";
   private static final String Вз7 = "Чаты";
   private static final float ВзР = 30.0F;
   private static final float Вз3 = 22.0F;
   private static final float Взр = 62.0F;
   private static final float Вз6 = 19.0F;
   private static final float Вз_ = 46.0F;
   private static final String ВзШ = "compose";
   private static final float Взы = 16.0F;
   private static final float Взй = 78.0F;
   private static final float ВзЖ = 32.0F;
   private static final float Взм = 38.0F;
   private static final String ВзК = "Найти пользователя";
   private static final String ВзI = "Поиск";
   private static final String ВзТ = "search";
   private static final float ВзО = 128.0F;
   private static final float ВзФ = 87.0F;
   private static final String ВзЙ = "chat";
   private static final float Взк = 79.0F;
   private static final float Взг = 78.0F;
   private static final float Вз0 = 78.0F;
   private static final String Взх = "profile";
   private static final float ВзУ = 16.0F;
   private static final float ВзЗ = 8.0F;
   private static final float ВзЕ = 60.0F;
   private static final String ВзП = "global";
   private static final String Вз西 = "title";
   private static final float Взд = 151.0F;
   private static final float ВзБ = 17.0F;
   private static final float ВзС = 89.0F;
   private static final float Взз = 10.0F;
   private static final float Взц = 17.0F;
   private static final String Взя = "preview";
   private static final String Вцс = "Начните переписку";
   private static final float ВцВ = 126.0F;
   private static final float ВцЯ = 15.0F;
   private static final float ВцХ = 89.0F;
   private static final float Вце = 37.0F;
   private static final float ВцА = 15.0F;
   private static final String Вц4 = "unread";
   private static final float Вцп = 42.0F;
   private static final float ВцД = 38.0F;
   private static final float ВцЪ = 25.0F;
   private static final float Вцт = 20.0F;
   private static final float Вца = 10.0F;
   private static final long ВцЮ = 99L;
   private static final String ВцЧ = "99+";
   private static final float ВцЛ = 29.5F;
   private static final float Вц诶 = 41.0F;
   private static final float Вц1 = 12.0F;
   private static final String ВцЬ = "time";
   private static final String Вц衣 = "time";
   private static final float Вцш = 54.0F;
   private static final float ВцИ = 12.0F;
   private static final float Вць = 12.0F;
   private static final float Вцж = 78.0F;
   private static final float Вц弟 = 89.0F;
   private static final float Вцб = 78.0F;
   private static final float ВцЫ = 105.0F;
   private static final float Вцl = 0.7F;
   private static final float Вц8 = 79.0F;
   private static final String Вц5 = "Никого не найдено";
   private static final String Вцв = "Подключение…";
   private static final float Вцъ = 22.0F;
   private static final float ВцМ = 25.0F;
   private static final float ВцН = 12.0F;
   private static final float Вцн = 77.0F;
   private static final float ВцЦ = 16.0F;
   private static final float Вцч = 32.0F;
   private static final float ВцЭ = 61.0F;
   private static final float Вц必 = 30.0F;
   private static final float Вцщ = 4.0F;
   private static final float ВцГ = 3.0F;
   private static final float Вцл = 4.0F;
   private static final float Вцэ = 3.0F;
   private static final float Вцю = 8.0F;
   private static final float ВцЩ = 53.0F;
   private static final float Вцу = 16.0F;
   private static final float Вц2 = 3.0F;
   private static final String Вцф = "people";
   private static final String Вци = "Контакты";
   private static final float Вц9 = 3.0F;
   private static final float Вцо = 3.0F;
   private static final String ВцO = "chat";
   private static final String Вц7 = "Чаты";
   private static final float ВцР = 3.0F;
   private static final float Вц3 = 3.0F;
   private static final String Вцр = "person";
   private static final String Вц6 = "Профиль";
   private static final String Вц_ = "chats";
   private static final String ВцШ = "title";
   private static final String Вцы = "chat";
   private static final String Вцй = "global";
   private static final String ВцЖ = "chat";
   private static final String Вцм = "global";
   private static final String ВцК = "title";
   private static final String ВцI = "Общий чат";
   private static final String ВцТ = "preview";
   private static final String ВцО = "Все пользователи Rose";
   private static final String ВцФ = "people";
   private static final String ВцЙ = "id";
   private static final String Вцк = "chat";
   private static final String Вцг = "title";
   private static final String Вц0 = "name";
   private static final String Вцх = "preview";
   private static final String ВцУ = "online";
   private static final String ВцЗ = "В сети";
   private static final String ВцЕ = "seen";
   private static final String ВцП = "profile";
   private static final String Вц西 = "global";
   private static final String Вцд = "chat";
   private static final String ВцБ = "peer";
   private static final String ВцС = "Общий чат";
   private static final String Вцз = "name";
   private static final String Вцц = "pinned";
   private static final String Вця = "id";
   private static final String Вяс = "messages";
   private static final String ВяВ = "author";
   private static final String ВяЯ = "id";
   private static final String ВяХ = "middle";
   private static final String Вяе = "bottom";
   private static final String ВяА = "top";
   private static final String Вя4 = "single";
   private static final String Вяп = "hasMore";
   private static final float ВяД = 44.0F;
   private static final float ВяЪ = 8.0F;
   private static final float Вят = 38.0F;
   private static final float Вяа = 35.0F;
   private static final float ВяЮ = 7.0F;
   private static final float ВяЧ = 14.0F;
   private static final float ВяЛ = 14.0F;
   private static final String Вя诶 = "hasMore";
   private static final float Вя1 = 85.0F;
   private static final float ВяЬ = 170.0F;
   private static final float Вя衣 = 30.0F;
   private static final String Вяш = "Ранние сообщения";
   private static final float ВяИ = 44.0F;
   private static final float Вяь = 8.0F;
   private static final String Вяж = "time";
   private static final float Вя弟 = 12.0F;
   private static final float Вяб = 26.0F;
   private static final float ВяЫ = 25.0F;
   private static final float Вяl = 13.0F;
   private static final float Вя8 = 6.0F;
   private static final float Вя5 = 12.0F;
   private static final float Вяв = 38.0F;
   private static final String Вяъ = "Загружаем переписку…";
   private static final String ВяМ = "Пока нет сообщений";
   private static final float ВяН = 15.0F;
   private static final float Вян = 36.0F;
   private static final float ВяЦ = 22.0F;
   private static final float Вяч = 42.0F;
   private static final float ВяЭ = 21.0F;
   private static final float Вя必 = 8.0F;
   private static final float Вящ = 15.0F;
   private static final float ВяГ = 4.0F;
   private static final float Вял = 95.0F;
   private static final float Вяэ = 220.0F;
   private static final float Вяю = 330.0F;
   private static final float ВяЩ = 18.0F;
   private static final float Вяу = 85.0F;
   private static final float Вя2 = 18.0F;
   private static final float Вяф = 54.0F;
   private static final float Вяи = 27.0F;
   private static final float Вя9 = 28.0F;
   private static final float Вяо = 18.0F;
   private static final float ВяO = 25.0F;
   private static final float Вя7 = 18.0F;
   private static final String ВяР = "Сообщество Rose";
   private static final float Вя3 = 49.0F;
   private static final float Вяр = 13.0F;
   private static final float Вя6 = 18.0F;
   private static final float Вя_ = 54.0F;
   private static final float ВяШ = 70.0F;
   private static final float Вяы = 18.0F;
   private static final float Вяй = 54.0F;
   private static final float ВяЖ = 54.0F;
   private static final float Вям = 27.0F;
   private static final float ВяК = 66.0F;
   private static final float ВяI = 22.0F;
   private static final float ВяТ = 46.0F;
   private static final float ВяО = 70.0F;
   private static final float ВяФ = 18.0F;
   private static final float ВяЙ = 54.0F;
   private static final float Вяк = 54.0F;
   private static final String Вяг = "id";
   private static final float Вя0 = 20.0F;
   private static final float Вях = 88.0F;
   private static final float ВяУ = 40.0F;
   private static final float ВяЗ = 50.0F;
   private static final float ВяЕ = 25.0F;
   private static final float ВяП = 39.0F;
   private static final float Вя西 = 98.0F;
   private static final float Вяд = 8.0F;
   private static final String ВяБ = "Закреплённое сообщение";
   private static final float ВяС = 51.0F;
   private static final float Вяз = 95.0F;
   private static final float Вяц = 15.0F;
   private static final String Вяя = "text";
   private static final float Ясс = 135.0F;
   private static final float ЯсВ = 13.0F;
   private static final float ЯсЯ = 51.0F;
   private static final float ЯсХ = 116.0F;
   private static final float Ясе = 13.0F;
   private static final String ЯсА = "pin";
   private static final float Яс4 = 58.0F;
   private static final float Ясп = 101.0F;
   private static final float ЯсД = 24.0F;
   private static final float ЯсЪ = 20.0F;
   private static final float Яст = 88.0F;
   private static final float Яса = 40.0F;
   private static final float ЯсЮ = 50.0F;
   private static final float ЯсЧ = 197.0F;
   private static final float ЯсЛ = 39.0F;
   private static final float Яс诶 = 176.0F;
   private static final float Яс1 = 31.0F;
   private static final String ЯсЬ = "К новым сообщениям";
   private static final float Яс衣 = 100.0F;
   private static final float Ясш = 61.0F;
   private static final float ЯсИ = 47.0F;
   private static final float Ясь = 39.0F;
   private static final String Ясж = "down";
   private static final float Яс弟 = 8.0F;
   private static final String Ясб = "id";
   private static final float ЯсЫ = 109.0F;
   private static final float Ясl = 108.0F;
   private static final float Яс8 = 76.0F;
   private static final float Яс5 = 117.0F;
   private static final float Ясв = 153.0F;
   private static final float Ясъ = 37.0F;
   private static final float ЯсМ = 15.0F;
   private static final String ЯсН = "edit";
   private static final String Ясн = "select";
   private static final String ЯсЦ = "reply";
   private static final float Ясч = 88.0F;
   private static final float ЯсЭ = 108.0F;
   private static final float Яс必 = 19.0F;
   private static final String Ясщ = "Изменить: ";
   private static final String ЯсГ = "Ответ: ";
   private static final String Ясл = "text";
   private static final float Ясэ = 245.0F;
   private static final float Ясю = 12.0F;
   private static final float ЯсЩ = 119.0F;
   private static final float Ясу = 105.0F;
   private static final float Яс2 = 12.0F;
   private static final float Ясф = 76.0F;
   private static final float Яси = 117.0F;
   private static final float Яс9 = 195.0F;
   private static final float Ясо = 37.0F;
   private static final float ЯсO = 111.0F;
   private static final float Яс7 = 111.0F;
   private static final float ЯсР = 25.0F;
   private static final String Яс3 = "close";
   private static final float Яср = 20.0F;
   private static final float Яс6 = 69.0F;
   private static final float Яс_ = 47.0F;
   private static final String ЯсШ = "smile";
   private static final float Ясы = 76.0F;
   private static final float Ясй = 69.0F;
   private static final float ЯсЖ = 153.0F;
   private static final float Ясм = 47.0F;
   private static final String ЯсК = "Сообщение";
   private static final float ЯсI = 67.0F;
   private static final float ЯсТ = 69.0F;
   private static final float ЯсО = 47.0F;
   private static final String ЯсФ = "more";
   private static final String ЯсЙ = "send";
   private static final String Яск = "muted";
   private static final String Ясг = "muteUntil";
   private static final String Яс0 = "Подключение…";
   private static final String Ясх = "muteUntil";
   private static final String ЯсУ = "muteReason";
   private static final float ЯсЗ = 45.0F;
   private static final float ЯсЕ = 10.0F;
   private static final float ЯсП = 16.0F;
   private static final float Яс西 = 10.0F;
   private static final String Ясд = "author";
   private static final float ЯсБ = 540.0F;
   private static final float ЯсС = 125.0F;
   private static final float Ясз = 32.0F;
   private static final String Ясц = "deleted";
   private static final String Яся = "Сообщение удалено";
   private static final String ЯВс = "text";
   private static final float ЯВВ = 38.0F;
   private static final float ЯВЯ = 20.0F;
   private static final float ЯВХ = 27.0F;
   private static final float ЯВе = 20.0F;
   private static final float ЯВА = 210.0F;
   private static final float ЯВ4 = 28.0F;
   private static final float ЯВп = 15.0F;
   private static final float ЯВД = 8.0F;
   private static final String ЯВЪ = "name";
   private static final String ЯВт = "author";
   private static final float ЯВа = 15.0F;
   private static final String ЯВЮ = "forwarded";
   private static final float ЯВЧ = 24.0F;
   private static final float ЯВЛ = 240.0F;
   private static final String ЯВ诶 = "reply";
   private static final float ЯВ1 = 51.0F;
   private static final float ЯВЬ = 260.0F;
   private static final String ЯВ衣 = "edited";
   private static final String ЯВш = "ред. ";
   private static final String ЯВИ = "time";
   private static final float ЯВь = 12.0F;
   private static final float ЯВж = 12.0F;
   private static final float ЯВ弟 = 12.0F;
   private static final String ЯВб = "reactions";
   private static final String ЯВЫ = "count";
   private static final float ЯВl = 58.0F;
   private static final float ЯВ8 = 46.0F;
   private static final float ЯВ5 = 16.0F;
   private static final float ЯВв = 40.0F;
   private static final String ЯВъ = "emoji";
   private static final String ЯВМ = "mine";
   private static final float ЯВН = 6.0F;
   private static final float ЯВн = 34.0F;
   private static final float ЯВЦ = 22.0F;
   private static final float ЯВч = 8.0F;
   private static final String ЯВЭ = "author";
   private static final float ЯВ必 = 22.0F;
   private static final String ЯВщ = "global";
   private static final float ЯВГ = 18.0F;
   private static final float ЯВл = 24.0F;
   private static final String ЯВэ = "pending";
   private static final float ЯВю = 0.999F;
   private static final String ЯВЩ = "global";
   private static final float ЯВу = 30.0F;
   private static final float ЯВ2 = 18.0F;
   private static final float ЯВф = 32.0F;
   private static final float ЯВи = 30.0F;
   private static final float ЯВ9 = 17.0F;
   private static final float ЯВо = 32.0F;
   private static final float ЯВO = 32.0F;
   private static final float ЯВ7 = 32.0F;
   private static final float ЯВР = 9.0F;
   private static final float ЯВ3 = 18.0F;
   private static final float ЯВр = 12.0F;
   private static final float ЯВ6 = 28.0F;
   private static final float ЯВ_ = 8.0F;
   private static final float ЯВШ = 16.0F;
   private static final float ЯВы = 34.0F;
   private static final float ЯВй = 34.0F;
   private static final String ЯВЖ = "author";
   private static final String ЯВм = "deleted";
   private static final String ЯВК = "id";
   private static final String ЯВI = "extracted";
   private static final String ЯВТ = "id";
   private static final String ЯВО = "single";
   private static final float ЯВФ = 12.0F;
   private static final float ЯВЙ = 16.0F;
   private static final float ЯВк = 32.0F;
   private static final float ЯВг = 28.0F;
   private static final String ЯВ0 = "forwarded";
   private static final String ЯВх = "forwarded";
   private static final float ЯВУ = 32.0F;
   private static final float ЯВЗ = 14.0F;
   private static final float ЯВЕ = 16.0F;
   private static final float ЯВП = 14.0F;
   private static final float ЯВ西 = 24.0F;
   private static final String ЯВд = "reply";
   private static final float ЯВБ = 9.0F;
   private static final float ЯВС = 18.0F;
   private static final float ЯВз = 44.0F;
   private static final float ЯВц = 5.0F;
   private static final float ЯВя = 9.0F;
   private static final float ЯЯс = 44.0F;
   private static final String ЯЯВ = "replyName";
   private static final float ЯЯЯ = 38.0F;
   private static final float ЯЯХ = 13.0F;
   private static final float ЯЯе = 18.0F;
   private static final float ЯЯА = 4.0F;
   private static final float ЯЯ4 = 13.0F;
   private static final String ЯЯп = "replyText";
   private static final float ЯЯД = 38.0F;
   private static final float ЯЯЪ = 15.0F;
   private static final float ЯЯт = 18.0F;
   private static final float ЯЯа = 24.0F;
   private static final float ЯЯЮ = 15.0F;
   private static final float ЯЯЧ = 51.0F;
   private static final float ЯЯЛ = 16.0F;
   private static final float ЯЯ诶 = 12.0F;
   private static final String ЯЯ1 = "edited";
   private static final String ЯЯЬ = "ред. ";
   private static final String ЯЯ衣 = "time";
   private static final float ЯЯш = 16.0F;
   private static final float ЯЯИ = 12.0F;
   private static final float ЯЯь = 12.0F;
   private static final String ЯЯж = "pending";
   private static final String ЯЯ弟 = "more";
   private static final String ЯЯб = "read";
   private static final String ЯЯЫ = "checks";
   private static final String ЯЯl = "check";
   private static final float ЯЯ8 = 16.0F;
   private static final float ЯЯ5 = 18.0F;
   private static final float ЯЯв = 3.0F;
   private static final float ЯЯъ = 18.0F;
   private static final float ЯЯМ = 8.0F;
   private static final float ЯЯН = 16.0F;
   private static final float ЯЯн = 34.0F;
   private static final float ЯЯЦ = 17.0F;
   private static final float ЯЯч = 8.0F;
   private static final float ЯЯЭ = 5.0F;
   private static final float ЯЯ必 = 24.0F;
   private static final float ЯЯщ = 8.0F;
   private static final float ЯЯГ = 6.0F;
   private static final float ЯЯл = 16.0F;
   private static final float ЯЯэ = 17.0F;
   private static final String ЯЯю = "0";
   private static final float ЯЯЩ = 16.0F;
   private static final float ЯЯу = 8.0F;
   private static final float ЯЯ2 = 24.0F;
   private static final float ЯЯф = 6.0F;
   private static final float ЯЯи = 16.0F;
   private static final String ЯЯ9 = "time";
   private static final String ЯЯо = "time";
   private static final float ЯЯO = 3.0F;
   private static final float ЯЯ7 = 9.0F;
   private static final String ЯЯР = "author";
   private static final String ЯЯ3 = "author";
   private static final String ЯЯр = "time";
   private static final String ЯЯ6 = "time";
   private static final long ЯЯ_ = 300000L;
   private static final String ЯЯШ = "time";
   private static final String ЯЯы = "time";
   private static final String ЯЯй = "out_";
   private static final String ЯЯЖ = "in_";
   private static final float ЯЯм = 18.0F;
   private static final float ЯЯК = 24.0F;
   private static final float ЯЯI = 42.0F;
   private static final float ЯЯТ = 10.0F;
   private static final float ЯЯО = 4.0F;
   private static final float ЯЯФ = 43.0F;
   private static final float ЯЯЙ = 19.0F;
   private static final float ЯЯк = 20.0F;
   private static final float ЯЯг = 37.0F;
   private static final float ЯЯ0 = 19.0F;
   private static final float ЯЯх = 17.0F;
   private static final float ЯЯУ = 43.0F;
   private static final float ЯЯЗ = 37.0F;
   private static final float ЯЯЕ = 43.0F;
   private static final float ЯЯП = 37.0F;
   private static final String ЯЯ西 = "Сегодня";
   private static final String ЯЯд = "Вчера";
   private static final String ЯЯБ = "d MMMM";
   private static final String ЯЯС = "ru";
   private static final String ЯЯз = "online";
   private static final String ЯЯц = "в сети";
   private static final String ЯЯя = "seen";
   private static final String ЯХс = "был(а) недавно";
   private static final long ЯХВ = 60000L;
   private static final String ЯХЯ = "был(а) только что";
   private static final long ЯХХ = 60L;
   private static final String ЯХе = "author";
   private static final String ЯХА = "deleted";
   private static final float ЯХ4 = 24.0F;
   private static final float ЯХп = 26.0F;
   private static final float ЯХД = 26.0F;
   private static final int ЯХЪ = -1862270976;
   private static final String ЯХт = "reply";
   private static final String ЯХа = "Ответить";
   private static final String ЯХЮ = "copy";
   private static final String ЯХЧ = "Копировать";
   private static final String ЯХЛ = "edit";
   private static final String ЯХ诶 = "Изменить";
   private static final String ЯХ1 = "canPin";
   private static final String ЯХЬ = "pinned";
   private static final String ЯХ衣 = "id";
   private static final String ЯХш = "id";
   private static final String ЯХИ = "pin";
   private static final String ЯХь = "Открепить";
   private static final String ЯХж = "Закрепить";
   private static final String ЯХ弟 = "forward";
   private static final String ЯХб = "Переслать";
   private static final String ЯХЫ = "admin";
   private static final String ЯХl = "global";
   private static final String ЯХ8 = "delete";
   private static final String ЯХ5 = "Удалить";
   private static final String ЯХв = "select";
   private static final String ЯХъ = "Выбрать";
   private static final float ЯХМ = 240.0F;
   private static final float ЯХН = 510.0F;
   private static final float ЯХн = 124.0F;
   private static final String ЯХЦ = "text";
   private static final String ЯХч = "\n";
   private static final String ЯХЭ = "reactions";
   private static final String ЯХ必 = "reply";
   private static final String ЯХщ = "forwarded";
   private static final float ЯХГ = 510.0F;
   private static final float ЯХл = 358.0F;
   private static final float ЯХэ = 12.0F;
   private static final float ЯХю = 16.0F;
   private static final float ЯХЩ = 20.0F;
   private static final float ЯХу = 56.0F;
   private static final float ЯХ2 = 18.0F;
   private static final float ЯХф = 18.0F;
   private static final float ЯХи = 12.0F;
   private static final float ЯХ9 = 46.0F;
   private static final float ЯХо = 23.0F;
   private static final float ЯХO = 6.0F;
   private static final float ЯХ7 = 4.0F;
   private static final float ЯХР = 44.0F;
   private static final float ЯХ3 = 46.0F;
   private static final float ЯХр = 30.0F;
   private static final float ЯХ6 = 14.0F;
   private static final float ЯХ_ = 36.0F;
   private static final float ЯХШ = 5.0F;
   private static final float ЯХы = 36.0F;
   private static final float ЯХй = 4.0F;
   private static final float ЯХЖ = 44.0F;
   private static final float ЯХм = 46.0F;
   private static final float ЯХК = 42.0F;
   private static final float ЯХI = 7.0F;
   private static final float ЯХТ = 32.0F;
   private static final float ЯХО = 32.0F;
   private static final float ЯХФ = 16.0F;
   private static final String ЯХЙ = "down";
   private static final float ЯХк = 36.0F;
   private static final float ЯХг = 13.0F;
   private static final float ЯХ0 = 20.0F;
   private static final float ЯХх = 42.0F;
   private static final float ЯХУ = 7.0F;
   private static final float ЯХЗ = 32.0F;
   private static final float ЯХЕ = 32.0F;
   private static final float ЯХП = 56.0F;
   private static final float ЯХ西 = 12.0F;
   private static final float ЯХд = 30.0F;
   private static final float ЯХБ = 11.0F;
   private static final String ЯХС = "read";
   private static final String ЯХз = "checks";
   private static final String ЯХц = "check";
   private static final float ЯХя = 24.5F;
   private static final float Яес = 11.0F;
   private static final float ЯеВ = 23.0F;
   private static final String ЯеЯ = "read";
   private static final String ЯеХ = "Прочитано";
   private static final String Яее = "Доставлено";
   private static final float ЯеА = 60.0F;
   private static final float Яе4 = 14.0F;
   private static final float Яеп = 14.0F;
   private static final float ЯеД = 22.0F;
   private static final float ЯеЪ = 44.0F;
   private static final float Яет = 44.0F;
   private static final float Яеа = 0.8F;
   private static final float ЯеЮ = 53.0F;
   private static final float ЯеЧ = 22.0F;
   private static final float ЯеЛ = 44.0F;
   private static final float Яе诶 = 0.8F;
   private static final float Яе1 = 8.0F;
   private static final float ЯеЬ = 7.0F;
   private static final float Яе衣 = 14.0F;
   private static final float Яеш = 43.0F;
   private static final float ЯеИ = 7.0F;
   private static final float Яеь = 14.0F;
   private static final float Яеж = 43.0F;
   private static final float Яе弟 = 12.0F;
   private static final int Яеб = -43687;
   private static final float ЯеЫ = 24.0F;
   private static final float Яеl = 10.0F;
   private static final float Яе8 = 24.0F;
   private static final float Яе5 = 60.0F;
   private static final float Яев = 13.0F;
   private static final float Яеъ = 17.0F;
   private static final float ЯеМ = 7.0F;
   private static final float ЯеН = 14.0F;
   private static final float Яен = 43.0F;
   private static final float ЯеЦ = 44.0F;
   private static final float Яеч = 425.0F;
   private static final float ЯеЭ = 133.0F;
   private static final float Яе必 = 10.0F;
   private static final float Яещ = 330.0F;
   private static final float ЯеГ = 54.0F;
   private static final float Яел = 27.0F;
   private static final float Яеэ = 7.0F;
   private static final float Яею = 5.0F;
   private static final float ЯеЩ = 11.0F;
   private static final float Яеу = 32.0F;
   private static final float Яе2 = 4.0F;
   private static final float Яеф = 44.0F;
   private static final float Яеи = 46.0F;
   private static final float Яе9 = 22.0F;
   private static final float Яео = 26.0F;
   private static final float ЯеO = 26.0F;
   private static final int Яе7 = -1862270976;
   private static final float ЯеР = 185.0F;
   private static final float Яе3 = 35.0F;
   private static final float Яер = 14.0F;
   private static final float Яе6 = 370.0F;
   private static final float Яе_ = 70.0F;
   private static final float ЯеШ = 28.0F;
   private static final String Яеы = "Переслать сообщение";
   private static final float Яей = 22.0F;
   private static final float ЯеЖ = 20.0F;
   private static final float Яем = 18.0F;
   private static final float ЯеК = 370.0F;
   private static final float ЯеI = 70.0F;
   private static final float ЯеТ = 322.0F;
   private static final float ЯеО = 15.0F;
   private static final float ЯеФ = 30.0F;
   private static final String ЯеЙ = "close";
   private static final float Яек = 64.0F;
   private static final String Яег = "chats";
   private static final String Яе0 = "chat";
   private static final String Яех = "people";
   private static final String ЯеУ = "id";
   private static final String ЯеЗ = "chat";
   private static final String ЯеЕ = "title";
   private static final String ЯеП = "name";
   private static final float Яе西 = 45.0F;
   private static final float Яед = 10.0F;
   private static final float ЯеБ = 350.0F;
   private static final String ЯеС = "chat";
   private static final String Яез = "global";
   private static final float Яец = 36.0F;
   private static final float Яея = 16.0F;
   private static final float ЯАс = 338.0F;
   private static final float ЯАВ = 36.0F;
   private static final String ЯАЯ = "title";
   private static final float ЯАХ = 43.0F;
   private static final float ЯАе = 22.0F;
   private static final float ЯАА = 26.0F;
   private static final float ЯА4 = 26.0F;
   private static final int ЯАп = Integer.MIN_VALUE;
   private static final float ЯАД = 510.0F;
   private static final float ЯАЪ = 642.0F;
   private static final float ЯАт = 15.0F;
   private static final float ЯАа = 14.0F;
   private static final float ЯАЮ = 29.0F;
   private static final float ЯАЧ = 0.28F;
   private static final float ЯАЛ = 0.16F;
   private static final float ЯА诶 = 338.0F;
   private static final float ЯА1 = 28.0F;
   private static final float ЯАЬ = 310.0F;
   private static final float ЯА衣 = 28.0F;
   private static final float ЯАш = 22.0F;
   private static final float ЯАИ = 19.0F;
   private static final float ЯАь = 44.0F;
   private static final String ЯАж = "back";
   private static final String ЯА弟 = "id";
   private static final float ЯАб = 92.0F;
   private static final float ЯАЫ = 21.0F;
   private static final float ЯАl = 71.0F;
   private static final float ЯА8 = 40.0F;
   private static final String ЯА5 = "Править";
   private static final String ЯАв = "Чат";
   private static final float ЯАъ = 60.0F;
   private static final float ЯАМ = 38.0F;
   private static final float ЯАН = 120.0F;
   private static final String ЯАн = "name";
   private static final String ЯАЦ = "Профиль";
   private static final float ЯАч = 50.0F;
   private static final float ЯАЭ = 30.0F;
   private static final float ЯА必 = 178.0F;
   private static final float ЯАщ = 30.0F;
   private static final float ЯАГ = 219.0F;
   private static final float ЯАл = 17.0F;
   private static final float ЯАэ = 66.0F;
   private static final float ЯАю = 3.0F;
   private static final float ЯАЩ = 21.0F;
   private static final float ЯАу = 265.0F;
   private static final String ЯА2 = "chat";
   private static final String ЯАф = "Мои чаты";
   private static final String ЯАи = "Сообщение";
   private static final float ЯА9 = 33.0F;
   private static final float ЯАо = 265.0F;
   private static final String ЯАO = "copy";
   private static final String ЯА7 = "Копировать ник";
   private static final float ЯАР = 45.0F;
   private static final float ЯА3 = 265.0F;
   private static final String ЯАр = "edit";
   private static final String ЯА6 = "search";
   private static final String ЯА_ = "Статус";
   private static final String ЯАШ = "Найти";
   private static final float ЯАы = 365.0F;
   private static final float ЯАй = 21.0F;
   private static final float ЯАЖ = 42.0F;
   private static final float ЯАм = 172.0F;
   private static final float ЯАК = 24.0F;
   private static final String ЯАI = "имя пользователя";
   private static final float ЯАТ = 42.0F;
   private static final float ЯАО = 19.0F;
   private static final float ЯАФ = 15.0F;
   private static final String ЯАЙ = "id";
   private static final float ЯАк = 42.0F;
   private static final float ЯАг = 46.0F;
   private static final float ЯА0 = 18.0F;
   private static final float ЯАх = 65.0F;
   private static final float ЯАУ = 30.0F;
   private static final float ЯАЗ = 27.0F;
   private static final String ЯАЕ = "copy";
   private static final float ЯАП = 42.0F;
   private static final float ЯА西 = 81.0F;
   private static final float ЯАд = 84.0F;
   private static final float ЯАБ = 0.7F;
   private static final String ЯАС = "статус";
   private static final float ЯАз = 42.0F;
   private static final float ЯАц = 99.0F;
   private static final float ЯАя = 15.0F;
   private static final float Я4с = 32.0F;
   private static final float Я4В = 122.0F;
   private static final float Я4Я = 64.0F;
   private static final float Я4Х = 37.0F;
   private static final String Я4е = "Ваш статус";
   private static final String Я4А = "status";
   private static final String Я44 = "Не указан";
   private static final String Я4п = "status";
   private static final float Я4Д = 86.0F;
   private static final float Я4Ъ = 16.0F;
   private static final float Я4т = 42.0F;
   private static final float Я4а = 128.0F;
   private static final float Я4Ю = 16.0F;
   private static final float Я4Ч = 21.0F;
   private static final float Я4Л = 566.0F;
   private static final float Я4诶 = 42.0F;
   private static final float Я41 = 48.0F;
   private static final String Я4Ь = "Сохранить статус";
   private static final float Я4衣 = 21.0F;
   private static final float Я4ш = 566.0F;
   private static final float Я4И = 42.0F;
   private static final float Я4ь = 49.0F;
   private static final float Я4ж = 25.0F;
   private static final float Я4弟 = 26.0F;
   private static final float Я4б = 571.0F;
   private static final float Я4Ы = 54.0F;
   private static final float Я4l = 39.0F;
   private static final float Я48 = 14.0F;
   private static final String Я45 = "Профиль";
   private static final float Я4в = 21.0F;
   private static final float Я4ъ = 42.0F;
   private static final float Я4М = 4.0F;
   private static final float Я4Н = 581.0F;
   private static final float Я4н = 16.0F;
   private static final String Я4Ц = "Переписка";
   private static final float Я4ч = 21.0F;
   private static final float Я4Э = 42.0F;
   private static final float Я4必 = 3.0F;
   private static final float Я4щ = 4.0F;
   private static final float Я4Г = 581.0F;
   private static final float Я4л = 16.0F;
   private static final float Я4э = 566.0F;
   private static final float Я4ю = 21.0F;
   private static final float Я4Щ = 49.0F;
   private static final float Я4у = 29.0F;
   private static final float Я42 = 62.0F;
   private static final float Я4ф = 20.0F;
   private static final float Я4и = 62.0F;
   private static final float Я49 = 27.0F;
   private static final float Я4о = 8.0F;
   private static final float Я4O = 27.0F;
   private static final float Я47 = 42.0F;
   private static final float Я4Р = 12.0F;
   private static final float Я43 = 62.0F;
   private static final String Я4р = "author";
   private static final String Я46 = "self";
   private static final String Я4_ = "peer";
   private static final String Я4Ш = "id";
   private static final String Я4ы = "people";
   private static final String Я4й = "id";
   private static final String Я4Ж = "id";
   private static final String Я4м = "name";
   private static final String Я4К = "name";
   private static final String Я4I = "id";
   private static final String Я4Т = "author";
   private static final String Я4О = "self";
   private static final String Я4Ф = "peer";
   private static final String Я4Й = "id";
   private static final String Я4к = "people";
   private static final String Я4г = "id";
   private static final String Я40 = "(?is)^/(ban|mute)(?:\\s.*)?$";
   private static final String Я4х = "global";
   private static final String Я4У = "Команды /ban и /mute доступны только ADMIN в общем чате";
   private static final String Я4З = "global";
   private static final String Я4Е = "muted";
   private static final String Я4П = "muteUntil";
   private static final String Я4西 = "muteUntil";
   private static final String Я4д = "muteReason";
   private static final String Я4Б = "text";
   private static final String Я4С = "id";
   private static final String Я4з = "id";
   private static final String Я4ц = "reply";
   private static final String Я4я = "id";
   private static final String Япс = "id";
   private static final String ЯпВ = "author";
   private static final String ЯпЯ = "name";
   private static final String ЯпХ = "self";
   private static final String Япе = "name";
   private static final String ЯпА = "role";
   private static final String Яп4 = "time";
   private static final String Япп = "pending";
   private static final String ЯпД = "replyText";
   private static final String ЯпЪ = "text";
   private static final String Япт = "replyName";
   private static final String Япа = "name";
   private static final String ЯпЮ = "edit";
   private static final String ЯпЧ = "send";
   private static final String ЯпЛ = "nonce";
   private static final String Яп诶 = "id";
   private static final String Яп1 = "id";
   private static final String ЯпЬ = "emoji";
   private static final String Яп衣 = "react";
   private static final String Япш = "id";
   private static final String ЯпИ = "\n";
   private static final float Япь = 16.0F;
   private static final float Япж = 40.0F;
   private static final float Яп弟 = 12.0F;
   private static final float Япб = 19.0F;
   private static final float ЯпЫ = 19.0F;
   private static final float Япl = 20.0F;
   private static final float Яп8 = 18.0F;
   private static final float Яп5 = 14.0F;
   private static final float Япв = 20.0F;
   private static final float Япъ = 4.0F;
   private static final float ЯпМ = 9.0F;
   private static final float ЯпН = 8.0F;
   private static final float Япн = 20.0F;
   private static final float ЯпЦ = 9.0F;
   private static final float Япч = 20.0F;
   private static final float ЯпЭ = 18.0F;
   private static final float Яп必 = 20.0F;
   private static final float Япщ = 20.0F;
   private static final long ЯпГ = 1000L;
   private static final long Япл = 600L;
   private static final float Япэ = 20.0F;
   private static final float Япю = 10.0F;
   private static final float ЯпЩ = 1.2F;
   private static final float Япу = 20.0F;
   private static final String Яп2 = "globe";
   private static final float Япф = 0.23F;
   private static final float Япи = 0.23F;
   private static final float Яп9 = 0.54F;
   private static final String Япо = "skin";
   private static final float ЯпO = 0.125F;
   private static final float Яп7 = 0.625F;
   private static final float ЯпР = 0.125F;
   private static final float Яп3 = 0.125F;
   private static final float Япр = 0.125F;
   private static final String Яп6 = "name";
   private static final String Яп_ = "?";
   private static final String ЯпШ = "?";
   private static final float Япы = 0.3F;
   private static final float Япй = 0.25F;
   private static final float ЯпЖ = 0.45F;
   private static final String Япм = "online";
   private static final float ЯпК = 11.0F;
   private static final float ЯпI = 11.0F;
   private static final float ЯпТ = 12.0F;
   private static final float ЯпО = 12.0F;
   private static final float ЯпФ = 6.0F;
   private static final float ЯпЙ = 9.0F;
   private static final float Япк = 9.0F;
   private static final float Япг = 8.0F;
   private static final float Яп0 = 8.0F;
   private static final float Япх = 4.0F;
   private static final int ЯпУ = -10037428;
   private static final float ЯпЗ = 18.0F;
   private static final float ЯпЕ = 37.0F;
   private static final float ЯпП = 15.0F;
   private static final float Яп西 = 12.0F;
   private static final float Япд = 37.0F;
   private static final float ЯпБ = 37.0F;
   private static final float ЯпС = 15.0F;
   private static final float Япз = 12.0F;
   private static final String Япц = "person";
   private static final String Япя = "people";
   private static final float ЯДс = 61.0F;
   private static final float ЯДВ = 24.0F;
   private static final float ЯДЯ = 9.0F;
   private static final float ЯДХ = 24.0F;
   private static final float ЯДе = 39.0F;
   private static final float ЯДА = 11.0F;
   private static final float ЯД4 = 61.0F;
   private static final float ЯДп = 0.23F;
   private static final float ЯДД = 0.23F;
   private static final float ЯДЪ = 0.54F;
   private static final float ЯДт = 0.8F;
   private static final float ЯДа = 17.142857F;
   private static final float ЯДЮ = 0.7F;
   private static final int ЯДЧ = -16777216;
   private static final int ЯДЛ = 16777215;
   private static final float ЯД诶 = 3.0F;
   private static final String ЯД1 = "H";
   private static final float ЯДЬ = 0.5F;
   private static final int ЯД衣 = 65039;
   private static final float ЯДш = 0.5F;
   private static final float ЯДИ = 3.0F;
   private static final int ЯДь = 65039;
   private static final int ЯДж = 65039;
   private static final String ЯД弟 = "\n";
   private static final String ЯДб = " ";
   private static final String ЯДЫ = "недавно";
   private static final float ЯДl = 43.0F;
   private static final double ЯД8 = 42.0;
   private static final float ЯД5 = 46.0F;
   private static final float ЯДв = 14.0F;
   private static final String ЯДъ = "name";
   private static final String ЯДМ = "Player";
   private static final float ЯДН = 20.0F;
   private static final String ЯДн = "name";
   private static final String ЯДЦ = "text";
   private static final String ЯДч = "id";
   private static final String ЯДЭ = "resultId";
   private static final String ЯД必 = "id";
   private static final String ЯДщ = "status";
   private static final String ЯДГ = "profile";
   private static final String ЯДл = "self";
   private static final String ЯДэ = "id";
   private static final String ЯДю = "status";
   private static final String ЯДЩ = "id";
   private static final String ЯДу = "name";
   private static final String ЯД2 = "id";
   private static final String ЯДф = "status";
   private static final String ЯДи = "id";
   private static final String ЯД9 = "chat";
   private static final String ЯДо = "sourceChat";
   private static final String ЯДO = "id";
   private static final String ЯД7 = "id";
   private static final String ЯДР = "forward";
   private static final String ЯД3 = "id";
   private static final String ЯДр = "delete";
   private static final String ЯД6 = "hide";
   private static final String ЯД_ = "id";
   private static final String ЯДШ = "pin";
   private static final String ЯДы = "id";
   private static final String ЯДй = "text";
   private static final String ЯДЖ = "text";
   private static final String ЯДм = "id";
   private static final float ЯДК = 0.999F;
   private static final String ЯДI = "chat";
   private static final String ЯДТ = "global";
   private static final String ЯДО = "self";
   private static final float ЯДФ = 16.0F;
   private static final String ЯДЙ = "❤";
   private static final String ЯДк = "\ud83d\udc4d";
   private static final String ЯДг = "\ud83d\udc4e";
   private static final String ЯД0 = "\ud83d\udd25";
   private static final String ЯДх = "\ud83e\udd70";
   private static final String ЯДУ = "\ud83d\udc4f";
   private static final String ЯДЗ = "\ud83d\ude01";
   private static final float ЯДЕ = 9.0F;
   private static final float ЯДП = 12.0F;
   private static final float ЯД西 = 78.0F;
   private static final float ЯДд = 68.0F;
   private static final float ЯДБ = 96.0F;
   private static final float ЯДС = 17.0F;
   private static final float ЯДз = 62.0F;
   private static final float ЯДц = 75.0F;
   private static final float ЯДя = 96.0F;
   private static final float ЯЪс = 18.0F;
   private static final float ЯЪВ = 61.0F;
   private static final float ЯЪЯ = 75.0F;
   private static final float ЯЪХ = 96.0F;
   private static final float ЯЪе = 17.0F;
   private static final float ЯЪА = 59.0F;
   private static final float ЯЪ4 = 75.0F;
   private static final float ЯЪп = 96.0F;
   private static final float ЯЪД = 7.0F;
   private static final float ЯЪЪ = 83.0F;
   private static final float ЯЪт = 80.0F;
   private static final float ЯЪа = 96.0F;
   private static final float ЯЪЮ = 9.0F;
   private static final float ЯЪЧ = 78.0F;
   private static final float ЯЪЛ = 78.0F;
   private static final float ЯЪ诶 = 96.0F;
   private static final float ЯЪ1 = 9.0F;
   private static final float ЯЪЬ = 78.0F;
   private static final float ЯЪ衣 = 78.0F;
   private static final float ЯЪш = 96.0F;
   private static final String ЯЪИ = "HH:mm";

   private void closeProfile() {
      this.Ъ = null;
      this.focus(this.В_00e801f9);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.сХ_bbeebd87) {
         return true;
      }

      Vector2f var3 = WorldProjection.guiToScreen(var1.x(), var1.y());
      this.Вl = var3.x / this.ВЦ;
      this.В8 = var3.y / this.ВЦ;

      for (int var4 = this.field_5.size() - 1; var4 >= 0; var4--) {
         MessengerScreen.HitRegion var5 = this.field_5.get(var4);
         if (this.hovered(var5.x, var5.y, var5.w, var5.h)) {
            Runnable var6 = var1.button() == 1 ? var5.right : (var1.button() == 0 ? var5.left : null);
            if (var6 != null) {
               var6.run();
               return true;
            }
         }
      }

      return true;
   }

   @Override
   public void removed() {
      this.Ы.forEach((var1, var2) -> this.Я_8dfd5497.Я_ea411899.put(var1, var2.getValue()));
   }

   private void button(float var1, float var2, float var3, float var4, String var5, Runnable var6) {
      this.pill(var1, var2, var3, var4, var5, var6);
   }

   private void drawForward() {
      float var1 = UiDrawing.getGlobalAlpha();
      float var2 = this.Я_c38ce5db.к.getValue();
      UiDrawing.setGlobalAlpha(var1 * var2);
      JsonObject var3 = this.Я_c38ce5db.т;
      this.field_5.clear();
      this.blur(this.Вь, this.Вж, this.В弟, this.Вб, Яе9, Яео);
      this.rect(this.Вь, this.Вж, this.В弟, this.Вб, ЯеO, Яе7);
      this.hit(this.Вь, this.Вж, this.В弟, this.Вб, () -> this.Д = null, null);
      float var4 = this.Вь + this.В弟 / 2.0F - ЯеР;
      float var5 = this.Вж + Яе3 + Яер * (1.0F - var2);
      this.glass(var4, var5, Яе6, this.Вб - Яе_, ЯеШ);
      this.text(Яеы, var4 + Яей, var5 + ЯеЖ, Яем, this.сЙ);
      this.hit(var4, var5, ЯеК, this.Вб - ЯеI, () -> {}, null);
      this.iconButton(var4 + ЯеТ, var5 + ЯеО, ЯеФ, ЯеЙ, () -> this.Д = null);
      float var6 = var5 + Яек;
      HashSet var7 = new HashSet();
      ArrayList<JsonObject> var8 = new ArrayList<>();

      for (JsonElement var10 : MessengerClient.method_4_fea5d33f(this.Я_8dfd5497.method_4_e1451646(), Яег)) {
         JsonObject var11 = var10.getAsJsonObject();
         if (var7.add(MessengerClient.method_4(var11, Яе0, ""))) {
            var8.add(var11);
         }
      }

      for (JsonElement var17 : MessengerClient.method_4_fea5d33f(this.Я_8dfd5497.method_4_e1451646(), Яех)) {
         JsonObject var19 = var17.getAsJsonObject();
         String var12 = MessengerClient.method_4(var19, ЯеУ, "");
         if (var7.add(var12)) {
            JsonObject var13 = new JsonObject();
            var13.addProperty(ЯеЗ, var12);
            var13.addProperty(ЯеЕ, MessengerClient.method_4(var19, ЯеП, var12));
            var8.add(var13);
         }
      }

      float var16 = var6;
      float var18 = this.Вж + this.Вб - Яе西;
      this.Вн = Math.clamp(this.Вн, 0.0F, Math.max(0.0F, var8.size() * 43 - (var18 - var16)));
      var6 -= this.Вн;
      this.scissor(var4 + Яед, var16, ЯеБ, var18 - var16);

      for (JsonObject var21 : var8) {
         String var22 = MessengerClient.method_4(var21, ЯеС, Яез);
         if (var6 >= var16 && var6 + Яец <= var18) {
            this.button(var4 + Яея, var6, ЯАс, ЯАВ, MessengerClient.method_4(var21, ЯАЯ, var22), () -> {
               JsonObject var3x = new JsonObject();
               var3x.addProperty(ЯД9, var22);
               var3x.addProperty(ЯДо, this.Я_8dfd5497.必());
               var3x.addProperty(ЯДO, MessengerClient.method_4_6da43b94(var3, ЯД7));
               this.Я_8dfd5497.method_4(ЯДР, var3x, () -> {
                  this.Д = null;
                  this.choose(var22);
               });
            });
         }

         var6 += ЯАХ;
      }

      UiDrawing.popScissor();
      UiDrawing.setGlobalAlpha(var1);
   }

   private void glass(float var1, float var2, float var3, float var4, float var5) {
      float var6 = Math.min(var5, ЯДа);
      this.rect(var1, var2, var3, var4, var6, this.сЗ);
      UiDrawing.drawOutline(var1 * this.ВЦ, var2 * this.ВЦ, var3 * this.ВЦ, var4 * this.ВЦ, this.ВЦ / ЯДЮ, var6 * this.ВЦ, this.сЕ);
   }

   private boolean hasProfileOverlay() {
      return this.Ъ != null || this.Х_c38ce5db.т != null;
   }

   private void scissor(float var1, float var2, float var3, float var4) {
      UiDrawing.pushScissor(var1 * this.ВЦ, var2 * this.ВЦ, var3 * this.ВЦ, var4 * this.ВЦ);
   }

   public MessengerScreen(Screen var1) {
      super(net.minecraft.network.chat.Component.literal(ВСр));
      this.сХ_6da43b94 = ВС6;
      this.се = System.nanoTime();
      this.б = new HashMap<>();
      this.Я_8dfd5497 = MessengerClient.method_4_8dfd5497();
      this.field_5 = new ArrayList<>();
      this.Ы = new HashMap<>();
      this.l = new HashMap<>();
      this.т = new HashSet<>();
      this.а = new LinkedHashSet<>();
      this.西 = ВС_;
      this.д = "";
      this.сА = ВСШ;
      this.ВЦ = 1.0F;
      this.ъ = new FloatAnimation(0.0F, ВСы, Easings.CUBIC_OUT);
      this.М = new FloatAnimation(1.0F, ВСй, Easings.CUBIC_OUT);
      this.Н = new FloatAnimation(1.0F, ВСЖ, Easings.CUBIC_OUT);
      this.н = new FloatAnimation(0.0F, ВСм, Easings.CUBIC_OUT);
      this.В_c38ce5db = new MessengerScreen.OverlayTransition();
      this.Я_c38ce5db = new MessengerScreen.OverlayTransition();
      this.Х_c38ce5db = new MessengerScreen.OverlayTransition();
      this.В_b9e95bbb = IntStream.range(0, 7).mapToObj(var0 -> new FloatAnimation(0.0F, ЯДФ, Easings.CUBIC_OUT)).toArray(FloatAnimation[]::new);
      this.в = new ArrayList<>();
      this.Я_af10102c = var1;
   }

   private void choose(String var1) {
      if (!var1.equals(this.Я_8dfd5497.必())) {
         this.Б = null;
         this.М.setImmediately(0.0F);
      }

      this.Ы.put(this.西, this.В_00e801f9);
      this.В_00e801f9.setFocused(false);
      this.Я_8dfd5497.诶(var1);
      this.西 = var1;
      this.В_00e801f9 = this.Ы.computeIfAbsent(var1, this::draft);
      this.focus(this.В_00e801f9);
      this.В5 = 0.0F;
      this.сЯ = false;
      this.field_4 = this.п = this.А_e1451646 = null;
      this.я = false;
      this.а.clear();
   }

   private void drawContext() {
      JsonObject var1 = this.В_c38ce5db.т;
      float var2 = UiDrawing.getGlobalAlpha();
      float var3 = this.В_c38ce5db.к.getValue();
      UiDrawing.setGlobalAlpha(var2 * var3);
      boolean var4 = MessengerClient.method_4(var1, ЯХе, "").equals(this.Я_8dfd5497.Й());
      boolean var5 = MessengerClient.method_4_bbeebd87(var1, ЯХА);
      this.field_5.clear();
      this.blur(this.Вь, this.Вж, this.В弟, this.Вб, ЯХ4, ЯХп);
      this.rect(this.Вь, this.Вж, this.В弟, this.Вб, ЯХД, ЯХЪ);
      this.hit(this.Вь, this.Вж, this.В弟, this.Вб, () -> this.А_e1451646 = null, () -> this.А_e1451646 = null);
      ArrayList var6 = new ArrayList();
      if (!var5) {
         var6.add(new MessengerScreen.ContextAction(ЯХт, ЯХа, () -> {
            this.field_4 = var1;
            this.п = null;
            this.focus(this.В_00e801f9);
         }, false));
         var6.add(new MessengerScreen.ContextAction(ЯХЮ, ЯХЧ, () -> this.minecraft.keyboardHandler.setClipboard(MessengerClient.method_4(var1, ЯДЖ, "")), false));
         if (var4) {
            var6.add(new MessengerScreen.ContextAction(ЯХЛ, ЯХ诶, () -> {
               this.п = var1;
               this.field_4 = null;
               this.В_00e801f9.setValue(MessengerClient.method_4(var1, ЯДй, ""));
               this.focus(this.В_00e801f9);
            }, false));
         }

         if (MessengerClient.method_4_bbeebd87(this.Я_8dfd5497.method_4_e1451646(), ЯХ1)) {
            boolean var7 = MessengerClient.method_4_6da43b94(MessengerClient.method_4_e1451646(this.Я_8dfd5497.method_4_e1451646(), ЯХЬ), ЯХ衣)
               == MessengerClient.method_4_6da43b94(var1, ЯХш);
            var6.add(new MessengerScreen.ContextAction(ЯХИ, var7 ? ЯХь : ЯХж, () -> this.command(ЯДШ, MessengerClient.method_4_6da43b94(var1, ЯДы), null), false));
         }

         var6.add(new MessengerScreen.ContextAction(ЯХ弟, ЯХб, () -> {
            this.Д = var1;
            this.Вн = 0.0F;
         }, false));
         boolean var24 = var4 || MessengerClient.method_4_bbeebd87(this.Я_8dfd5497.method_4_e1451646(), ЯХЫ) && this.Я_8dfd5497.必().equals(ЯХl);
         var6.add(new MessengerScreen.ContextAction(ЯХ8, ЯХ5, () -> this.command(var24 ? ЯДр : ЯД6, MessengerClient.method_4_6da43b94(var1, ЯД_), null), true));
      }

      var6.add(new MessengerScreen.ContextAction(ЯХв, ЯХъ, () -> {
         this.я = true;
         this.toggle(MessengerClient.method_4_6da43b94(var1, ЯД3));
      }, false));
      float var25 = ЯХМ;
      float var8 = var6.size() * 44 + 30 + (var4 && !var5 ? 53 : 0);
      MessengerScreen.BubbleLayout var9 = this.bubbleLayout(var1, ЯХН);
      JsonObject var10 = var1.deepCopy();
      if (var9.height > ЯХн) {
         if (var9.lines.size() > 3) {
            var10.addProperty(ЯХЦ, String.join(ЯХч, var9.lines.subList(0, 3)) + "…");
         }

         var10.remove(ЯХЭ);
         var10.remove(ЯХ必);
         var10.remove(ЯХщ);
      }

      MessengerScreen.BubbleLayout var11 = this.bubbleLayout(var10, ЯХГ);
      float var12 = ЯХл;
      float var13 = Math.max(var12, var11.width);
      float var14 = (var5 ? 0 : 56) + var11.height + ЯХэ + var8;
      float var15 = Math.clamp(this.Въ, this.Вь + this.ВЫ + var13 + ЯХю, this.Вь + this.В弟 - ЯХЩ);
      float var16 = var15 - var13;
      float var17 = Math.clamp(this.ВМ - ЯХу, this.Вж + ЯХ2, this.Вж + this.Вб - var14 - ЯХф) + ЯХи * (1.0F - var3);
      if (!var5) {
         this.glass(var15 - var12, var17, var12, ЯХ9, ЯХо);

         for (int var18 = 0; var18 < А_0447afad.length; var18++) {
            String var19 = А_0447afad[var18];
            float var20 = var15 - var12 + ЯХO + var18 * 44;
            this.В_b9e95bbb[var18].updateTarget(this.А_e1451646 != null && this.hovered(var20 - ЯХ7, var17, ЯХР, ЯХ3) ? 1.0F : 0.0F);
            float var21 = ЯХр + ЯХ6 * this.В_b9e95bbb[var18].getValue();
            this.emoji(var18, var20 + (ЯХ_ - var21) / 2.0F, var17 + ЯХШ + (ЯХы - var21), var21, -1);
            this.hit(var20 - ЯХй, var17, ЯХЖ, ЯХм, () -> {
               this.react(var1, var19);
               this.А_e1451646 = null;
            }, null);
         }

         this.rect(var15 - ЯХК, var17 + ЯХI, ЯХТ, ЯХО, ЯХФ, this.сУ);
         this.icon(ЯХЙ, var15 - ЯХк, var17 + ЯХг, ЯХ0, this.сЙ);
         this.hit(var15 - ЯХх, var17 + ЯХУ, ЯХЗ, ЯХЕ, () -> this.А_e1451646 = null, null);
         var17 += ЯХП;
      }

      this.bubbleBody(var10, var4 ? var15 - var11.width : var16, var17, var11, true);
      var17 += var11.height + ЯХ西;
      float var27 = var4 ? var15 - var25 : var16;
      this.glass(var27, var17, var25, var8, ЯХд);
      this.hit(var27, var17, var25, var8, () -> {}, null);
      float var28 = var17 + ЯХБ;
      if (var4 && !var5) {
         this.icon(MessengerClient.method_4_bbeebd87(var1, ЯХС) ? ЯХз : ЯХц, var27 + ЯХя, var28 + Яес, ЯеВ, this.сЙ);
         this.text(MessengerClient.method_4_bbeebd87(var1, ЯеЯ) ? ЯеХ : Яее, var27 + ЯеА, var28 + Яе4, Яеп, this.сЙ);
         this.rect(var27 + ЯеД, var28 + ЯеЪ, var25 - Яет, Яеа, 0.0F, this.сП);
         var28 += ЯеЮ;
      }

      for (int var29 = 0; var29 < var6.size(); var29++) {
         MessengerScreen.ContextAction var30 = (MessengerScreen.ContextAction)var6.get(var29);
         if (var29 == var6.size() - 1) {
            this.rect(var27 + ЯеЧ, var28 + 1.0F, var25 - ЯеЛ, Яе诶, 0.0F, this.сП);
            var28 += Яе1;
         }

         float var22 = var28;
         if (this.hovered(var27 + ЯеЬ, var28, var25 - Яе衣, Яеш)) {
            this.rect(var27 + ЯеИ, var28, var25 - Яеь, Яеж, Яе弟, this.сБ);
         }

         int var23 = var30.danger ? Яеб : this.сЙ;
         this.icon(var30.icon, var27 + ЯеЫ, var28 + Яеl, Яе8, var23);
         this.text(var30.label, var27 + Яе5, var28 + Яев, Яеъ, var23);
         this.hit(var27 + ЯеМ, var22, var25 - ЯеН, Яен, () -> {
            this.А_e1451646 = null;
            var30.action.run();
         }, null);
         var28 += ЯеЦ;
      }

      UiDrawing.setGlobalAlpha(var2);
   }

   private void drawSidebar() {
      this.rect(this.Вь + this.ВЫ - 1.0F, this.Вж + Вз2, 1.0F, this.Вб - Взф, 0.0F, this.сП);
      this.iconButton(this.Вь + Взи, this.Вж + Вз9, Взо, ВзO, this::onClose);
      this.centered(Вз7, this.Вь + this.ВЫ / 2.0F, this.Вж + ВзР, Вз3, this.сЙ);
      this.iconButton(this.Вь + this.ВЫ - Взр, this.Вж + Вз6, Вз_, ВзШ, () -> {
         this.сс = !this.сс;
         this.focus(this.Я_00e801f9);
      });
      this.input(this.Я_00e801f9, this.Вь + Взы, this.Вж + Взй, this.ВЫ - ВзЖ, Взм, this.сс ? ВзК : ВзI, ВзТ);
      if (!this.Я_00e801f9.getValue().equals(this.д)) {
         this.д = this.Я_00e801f9.getValue();
         this.Я_8dfd5497.method_6(this.д);
         this.Вв = 0.0F;
      }

      float var1 = this.Вж + ВзО;
      float var2 = this.Вж + this.Вб - ВзФ;
      List<JsonObject> var3 = this.rows();
      this.Вв = Math.clamp(this.Вв, 0.0F, Math.max(0.0F, var3.size() * 79 - (var2 - var1)));
      this.scissor(this.Вь + 1.0F, var1, this.ВЫ - 2.0F, var2 - var1);
      float var4 = var1 - this.Вв;

      for (JsonObject var6 : var3) {
         String var7 = MessengerClient.method_4(var6, ВзЙ, "");
         if (var4 + Взк >= var1 && var4 <= var2) {
            boolean var8 = var7.equals(this.Я_8dfd5497.必());
            if (var8 || this.hovered(this.Вь + 1.0F, var4, this.ВЫ - 2.0F, Взг)) {
               this.rect(this.Вь + 1.0F, var4, this.ВЫ - 2.0F, Вз0, 0.0F, var8 ? this.сд : this.сБ);
            }

            this.avatar(MessengerClient.method_4_e1451646(var6, Взх), this.Вь + ВзУ, var4 + ВзЗ, ВзЕ, var7.equals(ВзП));
            this.text(trim(MessengerClient.method_4(var6, Вз西, var7), this.ВЫ - Взд, ВзБ), this.Вь + ВзС, var4 + Взз, Взц, this.сЙ);
            this.text(trim(MessengerClient.method_4(var6, Взя, Вцс), this.ВЫ - ВцВ, ВцЯ), this.Вь + ВцХ, var4 + Вце, ВцА, this.ск);
            long var9 = MessengerClient.method_4_6da43b94(var6, Вц4);
            if (var9 > 0L) {
               this.rect(this.Вь + this.ВЫ - Вцп, var4 + ВцД, ВцЪ, Вцт, Вца, this.сд);
               this.centered(var9 > ВцЮ ? ВцЧ : var9 + "", this.Вь + this.ВЫ - ВцЛ, var4 + Вц诶, Вц1, this.сг);
            }

            if (MessengerClient.method_4_6da43b94(var6, ВцЬ) > 0L) {
               this.text(time(MessengerClient.method_4_6da43b94(var6, Вц衣)), this.Вь + this.ВЫ - Вцш, var4 + ВцИ, Вць, this.ск);
            }

            this.hit(this.Вь + 1.0F, Math.max(var1, var4), this.ВЫ - 2.0F, Math.min(var2, var4 + Вцж) - Math.max(var1, var4), () -> this.choose(var7), null);
            this.rect(this.Вь + Вц弟, var4 + Вцб, this.ВЫ - ВцЫ, Вцl, 0.0F, this.сП);
         }

         var4 += Вц8;
      }

      if (var3.isEmpty()) {
         this.text(this.Я_8dfd5497.п() ? Вц5 : Вцв, this.Вь + Вцъ, var1 + ВцМ, ВцН, this.ск);
      }

      UiDrawing.popScissor();
      float var11 = this.Вж + this.Вб - Вцн;
      float var12 = this.Вь + ВцЦ;
      float var13 = this.ВЫ - Вцч;
      this.glass(var12, var11, var13, ВцЭ, Вц必);
      this.rect(var12 + Вцщ + this.Н.getValue() * var13 / ВцГ, var11 + Вцл, var13 / Вцэ - Вцю, ВцЩ, Вцу, this.сд);
      this.nav(var12, var11, var13 / Вц2, Вцф, Вци, () -> {
         this.сс = true;
         this.focus(this.Я_00e801f9);
      });
      this.nav(var12 + var13 / Вц9, var11, var13 / Вцо, ВцO, Вц7, () -> {
         this.сс = false;
         this.Я_00e801f9.setValue("");
         this.focus(this.В_00e801f9);
      });
      this.nav(
         var12 + var13 * 2.0F / ВцР, var11, var13 / Вц3, Вцр, Вц6, () -> this.showProfile(MessengerClient.method_4_e1451646(this.Я_8dfd5497.method_4_e1451646(), ЯДО))
      );
   }

   @Override
   protected void extractMenuBackground(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5) {
   }

   private void text(String var1, float var2, float var3, float var4, int var5) {
      StringBuilder var6 = new StringBuilder();
      float var7 = var2;

      for (int var11 : var1.codePoints().toArray()) {
         int var12 = reactionIndex(var11);
         if (var12 < 0 && !fallbackPunctuation(var11)) {
            if (var11 != ЯД衣) {
               var6.appendCodePoint(var11);
            }
         } else {
            if (!var6.isEmpty()) {
               String var13 = var6.toString();
               this.plainText(var13, var7, var3, var4, var5);
               var7 += width(var13, var4);
               var6.setLength(0);
            }

            if (var12 >= 0) {
               float var15 = var4 + ЯД诶;
               float var14 = var3 + ClientFonts.getMainMenuTextFont().getFontFace().getTextCenterOffsetY(ЯД1, var4) - var15 / 2.0F;
               this.emoji(var12, var7, var14, var15, var5);
               var7 += var15;
            } else {
               String var16 = Character.toString(var11);
               UiDrawing.drawText(ClientFonts.getMediumFont(), var16, var7 * this.ВЦ, var3 * this.ВЦ, var4 * this.ВЦ, var5, 0.0F, ЯДЬ, 1.0F);
               var7 += UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var16, var4, 1.0F);
            }
         }
      }

      if (!var6.isEmpty()) {
         this.plainText(var6.toString(), var7, var3, var4, var5);
      }
   }

   private void profileTile(float var1, float var2, float var3, String var4, String var5, Runnable var6) {
      this.rect(var1, var2, var3, Я42, Я4ф, this.hovered(var1, var2, var3, Я4и) ? this.сд : this.сУ);
      this.icon(var4, var1 + (var3 - Я49) / 2.0F, var2 + Я4о, Я4O, this.сЙ);
      this.centered(var5, var1 + var3 / 2.0F, var2 + Я47, Я4Р, this.сЙ);
      this.hit(var1, var2, var3, Я43, var6, null);
   }

   private boolean hasMessageOverlay() {
      return this.А_e1451646 != null || this.Д != null || this.В_c38ce5db.т != null || this.Я_c38ce5db.т != null;
   }

   @Override
   protected void extractPanorama(GuiGraphicsExtractor var1, float var2) {
   }

   private static boolean emojiOnly(String var0) {
      int var1 = 0;
      int var2 = 0;

      while (var2 < var0.length()) {
         int var3 = var0.codePointAt(var2);
         var2 += Character.charCount(var3);
         if (var3 != ЯДж && !Character.isWhitespace(var3)) {
            if (reactionIndex(var3) < 0 || ++var1 > 3) {
               return false;
            }
         }
      }

      return var1 > 0;
   }

   private boolean hovered(float var1, float var2, float var3, float var4) {
      return this.Вl >= var1 && this.Вl <= var1 + var3 && this.В8 >= var2 && this.В8 <= var2 + var4;
   }

   private void gradient(float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      UiDrawing.drawRoundedRect(var1 * this.ВЦ, var2 * this.ВЦ, var3 * this.ВЦ, var4 * this.ВЦ, var5 * this.ВЦ, new Vector4i(var7, var6, var7, var6));
   }

   private void messageAuthor(JsonObject var1, float var2, float var3, float var4) {
      String var5 = this.roleLabel(var1);
      float var6 = width(var5, ВСз);
      float var7 = var3 + ВСц;

      int var8 = switch (var5) {
         case ВСя -> ВзЯ;
         case Взс -> ВзХ;
         case ВзВ -> -30065;
         default -> Взе;
      };
      JsonObject var13 = this.ж.get(MessengerClient.method_4(var1, ВзА, ""));
      if (var13 != null
         && (var13 == var1 || MessengerClient.method_4_6da43b94(var1, Вз4) > 0L && MessengerClient.method_4_6da43b94(var13, Взп) == MessengerClient.method_4_6da43b94(var1, ВзД))
         && !MessengerClient.method_4_bbeebd87(var1, ВзЪ)) {
         int var14 = switch (var5) {
            case Взт -> -11309;
            case Вза -> ВзЧ;
            case ВзЮ -> -8992;
            default -> ВзЛ;
         };
         UiDrawing.drawShinyText(ClientFonts.getMainMenuTextFont(), var5, var2 * this.ВЦ, var7 * this.ВЦ, Вз诶 * this.ВЦ, var8, var14, this.ВИ);
      } else {
         this.text(var5, var2, var7, Вз1, var8);
      }

      String var15 = MessengerClient.method_4(var1, ВзЬ, MessengerClient.method_4(var1, Вз衣, ""));
      this.text(trim(var15, Math.max(0.0F, var4 - var6 - Взш), ВзИ), var2 + var6 + Взь, var7, Взж, this.сг);
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.Я_af10102c != null && !(this.Я_af10102c instanceof ClickGuiScreen)) {
         this.Я_af10102c.extractRenderState(var1, var2, var3, var4);
      }

      UiDrawing.setTransform(new Matrix4f());
      UiDrawing.disableScissor();
      UiDrawing.resetGlobalAlpha();
      this.ъ.updateTarget(this.сХ_bbeebd87 ? 0.0F : 1.0F);
      if (this.сХ_bbeebd87 && this.ъ.getValue() < Вз弟) {
         this.minecraft.gui.setScreen(this.Я_af10102c);
      } else {
         this.В_c38ce5db.Ц(this.А_e1451646);
         this.Я_c38ce5db.Ц(this.Д);
         this.Х_c38ce5db.Ц(this.Ъ);
         this.н.updateTarget(this.сЯ && !this.hasMessageOverlay() && !this.hasProfileOverlay() ? 1.0F : 0.0F);
         this.Н.updateTarget(this.Ъ != null ? 2.0F : (this.сс ? 0.0F : 1.0F));
         float var5 = this.ъ.getValue();
         this.updateColors();
         this.updateRoles();
         this.ВИ = (float)((System.nanoTime() - this.се) % Взб) / ВзЫ;
         if (this.Я_af10102c instanceof ClickGuiScreen var6) {
            var6.renderBackdrop(var2, var3, var5);
         }

         float var15 = this.minecraft.getWindow().getScreenWidth();
         float var16 = this.minecraft.getWindow().getScreenHeight();
         float var8 = Math.min((var15 - Взl) / Вз8, (var16 - Вз5) / Взв);
         float var9 = this.Я_af10102c instanceof ClickGuiScreen var10 ? var10.getInterfaceScale() : Math.max(Взъ, Math.min(ClickGuiPreferences.х8(), var8));
         this.ВЦ = var9 * ВзМ * (ВзН + Взн * var5);
         Vector2f var17 = WorldProjection.transformScreenPoint(var2, var3);
         this.Вl = var17.x / this.ВЦ;
         this.В8 = var17.y / this.ВЦ;
         this.В弟 = ВзЦ;
         this.Вб = Взч;
         this.ВЫ = ВзЭ;
         this.Вь = (var15 / this.ВЦ - this.В弟) / 2.0F;
         this.Вж = (var16 / this.ВЦ - this.Вб) / 2.0F + Вз必 * (1.0F - var5);
         this.field_5.clear();
         UiDrawing.setGlobalAlpha(Взщ * var5);

         try {
            UiDrawing.drawShadow(this.Вь * this.ВЦ, this.Вж * this.ВЦ, this.В弟 * this.ВЦ, this.Вб * this.ВЦ, ВзГ * var9, this.с西);
            this.blur(this.Вь, this.Вж, this.В弟, this.Вб, Взл, Взэ);
            this.rect(this.Вь, this.Вж, this.В弟, this.Вб, Взю, this.сх);
            this.drawSidebar();
            this.drawConversation();
            this.outline(this.Вь, this.Вж, this.В弟, this.Вб, ВзЩ, this.сЕ);
            if (this.н.getValue() > Взу) {
               int var18 = this.field_5.size();
               this.drawEmojiPicker();
               if (!this.сЯ) {
                  this.field_5.subList(var18, this.field_5.size()).clear();
               }
            }

            if (this.В_c38ce5db.т != null) {
               this.drawContext();
               if (this.А_e1451646 == null) {
                  this.field_5.clear();
               }
            }

            if (this.Я_c38ce5db.т != null) {
               this.drawForward();
               if (this.Д == null) {
                  this.field_5.clear();
               }
            }

            if (this.Х_c38ce5db.т != null) {
               this.drawProfile();
               if (this.Ъ == null) {
                  this.field_5.clear();
               }
            }
         } finally {
            UiDrawing.resetGlobalAlpha();
            UiDrawing.disableScissor();
            UiDrawing.setTransform(new Matrix4f());
         }
      }
   }

   private EditBox draft(String var1) {
      EditBox var2 = this.field(2000);
      var2.setValue(this.Я_8dfd5497.Я_ea411899.getOrDefault(var1, ""));
      return var2;
   }

   private void command(String var1, long var2, Runnable var4) {
      JsonObject var5 = new JsonObject();
      var5.addProperty(Япш, var2);
      this.Я_8dfd5497.method_4(var1, var5, var4);
   }

   private MessengerScreen.BubbleLayout bubbleLayout(JsonObject var1, float var2) {
      boolean var3 = MessengerClient.method_4(var1, Ясд, "").equals(this.Я_8dfd5497.Й());
      float var4 = Math.min(ЯсБ, var2 - ЯсС);
      float var5 = Ясз;
      String var6 = MessengerClient.method_4_bbeebd87(var1, Ясц) ? Яся : MessengerClient.method_4(var1, ЯВс, "");
      float var7 = emojiOnly(var6) ? ЯВВ : ЯВЯ;
      float var8 = ЯВХ + var7 - ЯВе;
      List<String> var9 = wrap(var6, var4, var7);
      float var10 = ЯВА;

      for (String var12 : var9) {
         var10 = Math.max(var10, width(var12, var7) + var5);
      }

      float var27 = ЯВ4;
      float var28 = width(this.roleLabel(var1), ЯВп) + ЯВД + width(MessengerClient.method_4(var1, ЯВЪ, MessengerClient.method_4(var1, ЯВт, "")), ЯВа);
      var10 = Math.max(var10, (float)Math.ceil(var28 + var5) + 2.0F);
      if (!MessengerClient.method_4(var1, ЯВЮ, "").isEmpty()) {
         var27 += ЯВЧ;
         var10 = Math.max(var10, ЯВЛ);
      }

      if (MessengerClient.method_4_6da43b94(var1, ЯВ诶) > 0L) {
         var27 += ЯВ1;
         var10 = Math.max(var10, ЯВЬ);
      }

      float var13 = width((MessengerClient.method_4_6da43b94(var1, ЯВ衣) > 0L ? ЯВш : "") + time(MessengerClient.method_4_6da43b94(var1, ЯВИ)), ЯВь) + (var3 ? 25 : 0);
      float var14 = width((String)var9.getLast(), var7);
      boolean var15 = var14 + var13 + ЯВж <= var4;
      if (var15) {
         var10 = Math.max(var10, var14 + var13 + ЯВ弟 + var5);
      }

      JsonArray var16 = MessengerClient.method_4_fea5d33f(var1, ЯВб);
      List var17 = var16.isEmpty() ? List.of() : new ArrayList(var16.size());
      float var18 = 0.0F;
      float var19 = 0.0F;
      float var20 = 0.0F;

      for (JsonElement var22 : var16) {
         JsonObject var23 = var22.getAsJsonObject();
         String var24 = Long.toString(MessengerClient.method_4_6da43b94(var23, ЯВЫ));
         float var25 = Math.min(var4, Math.max(ЯВl, ЯВ8 + width(var24, ЯВ5)));
         if (var18 > 0.0F && var18 + var25 > var4) {
            var18 = 0.0F;
            var19 += ЯВв;
         }

         var17.add(new MessengerScreen.ReactionLayout(MessengerClient.method_4(var23, ЯВъ, ""), var24, MessengerClient.method_4_bbeebd87(var23, ЯВМ), var18, var19, var25));
         var10 = Math.max(var10, var18 + var25 + var5);
         var18 += var25 + ЯВН;
         var20 = var19 + ЯВн;
      }

      float var29 = ЯВЦ + var27 + var9.size() * var8 + (var15 ? 0 : 18) + (var17.isEmpty() ? 0.0F : var20 + ЯВч);
      return new MessengerScreen.BubbleLayout(var9, Math.min(var4 + var5, var10), var29, var7, var8, var15, var17, var20);
   }

   private void nav(float var1, float var2, float var3, String var4, String var5, Runnable var6) {
      boolean var7 = var4.equals(Япц) ? this.Ъ != null : this.Ъ == null && (var4.equals(Япя) ? this.сс : !this.сс);
      int var8 = var7 ? this.сг : (this.hovered(var1, var2, var3, ЯДс) ? this.сЙ : this.ск);
      this.icon(var4, var1 + (var3 - ЯДВ) / 2.0F, var2 + ЯДЯ, ЯДХ, var8);
      this.centered(var5, var1 + var3 / 2.0F, var2 + ЯДе, ЯДА, var8);
      this.hit(var1, var2, var3, ЯД4, var6, null);
   }

   private static String time(long var0) {
      return var0 <= 0L ? ЯДЫ : В_0551e0cc.format(Instant.ofEpochMilli(var0).atZone(ZoneId.systemDefault()));
   }

   private void showProfile(JsonObject var1) {
      if (var1.has(Я4I)) {
         this.сЯ = false;
         this.Ъ = var1;
         this.сВ = false;
         this.А_e1451646 = null;
      }
   }

   private void drawBubble(JsonObject var1, float var2, float var3, float var4, MessengerScreen.BubbleLayout var5, float var6, float var7) {
      boolean var8 = MessengerClient.method_4(var1, ЯВЭ, "").equals(this.Я_8dfd5497.Й());
      float var9 = var8 ? var2 + var4 - var5.width - ЯВ必 : var2 + (this.Я_8dfd5497.必().equals(ЯВщ) ? 57 : 22);
      float var10 = this.sendProgress(var1);
      float var11 = UiDrawing.getGlobalAlpha();
      UiDrawing.setGlobalAlpha(var11 * var10);

      try {
         this.bubbleBody(var1, var9 + ЯВГ * (1.0F - var10), var3 + ЯВл * (1.0F - var10), var5, false);
      } finally {
         UiDrawing.setGlobalAlpha(var11);
      }

      if (!MessengerClient.method_4_bbeebd87(var1, ЯВэ) && !(var10 < ЯВю)) {
         float var12 = Math.max(var6, var3);
         float var13 = Math.min(var7, var3 + var5.height);
         this.hit(var9, var12, var5.width, var13 - var12, () -> {
            if (this.я) {
               this.toggle(MessengerClient.method_4_6da43b94(var1, ЯДм));
            }
         }, () -> {
            this.А_e1451646 = var1;
            this.Въ = var9 + var5.width;
            this.ВМ = var3;
         });
         if (this.Я_8dfd5497.必().equals(ЯВЩ) && !var8) {
            JsonObject var14 = this.author(var1);
            if (var3 >= var6 && var3 + ЯВу <= var7) {
               this.avatar(var14, var2 + ЯВ2, var3 + var5.height - ЯВф, ЯВи, false);
               this.hit(var2 + ЯВ9, var3 + var5.height - ЯВо, ЯВO, ЯВ7, () -> this.showProfile(var14), null);
            }

            this.hit(
               var9 + ЯВР,
               Math.max(var6, var3),
               var5.width - ЯВ3,
               Math.max(0.0F, Math.min(var7, var3 + ЯВр + ЯВ6) - Math.max(var6, var3)),
               () -> this.showAuthor(var1),
               null
            );
         }

         float var21 = var3 + var5.height - var5.reactionHeight - ЯВ_;

         for (MessengerScreen.ReactionLayout var16 : var5.reactions) {
            float var17 = var9 + ЯВШ + var16.x;
            float var18 = var21 + var16.y;
            if (var18 >= var6 && var18 + ЯВы <= var7) {
               this.hit(var17, var18, var16.width, ЯВй, () -> this.react(var1, var16.emoji), null);
            }
         }
      }
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.сХ_bbeebd87 || this.hasMessageOverlay()) {
         return true;
      }

      if (this.hasProfileOverlay()) {
         if (this.Ъ != null && this.сВ) {
            this.Х_00e801f9.charTyped(var1);
         }

         return true;
      } else {
         return this.Я_00e801f9.isFocused() ? this.Я_00e801f9.charTyped(var1) : this.В_00e801f9.charTyped(var1);
      }
   }

   private void bubbleBody(JsonObject var1, float var2, float var3, MessengerScreen.BubbleLayout var4, boolean var5) {
      boolean var6 = MessengerClient.method_4(var1, ЯВЖ, "").equals(this.Я_8dfd5497.Й());
      boolean var7 = MessengerClient.method_4_bbeebd87(var1, ЯВм);
      int var8 = this.а.contains(MessengerClient.method_4_6da43b94(var1, ЯВК)) ? this.сд : (var6 ? this.сС : this.сз);
      String var9 = var5 ? ЯВI : this.б.getOrDefault(MessengerClient.method_4_6da43b94(var1, ЯВТ), ЯВО);
      this.bubbleShape(var2, var3, var4.width, var4.height, var6, var9, var8);
      float var10 = var3 + ЯВФ;
      this.messageAuthor(var1, var2 + ЯВЙ, var10, var4.width - ЯВк);
      var10 += ЯВг;
      if (!MessengerClient.method_4(var1, ЯВ0, "").isEmpty()) {
         this.text(trim("Переслано от " + MessengerClient.method_4(var1, ЯВх, ""), var4.width - ЯВУ, ЯВЗ), var2 + ЯВЕ, var10, ЯВП, this.сг);
         var10 += ЯВ西;
      }

      if (MessengerClient.method_4_6da43b94(var1, ЯВд) > 0L) {
         this.rect(var2 + ЯВБ, var10, var4.width - ЯВС, ЯВз, ЯВц, this.сБ);
         this.rect(var2 + ЯВя, var10, 2.0F, ЯЯс, 1.0F, this.сг);
         this.text(trim(MessengerClient.method_4(var1, ЯЯВ, ""), var4.width - ЯЯЯ, ЯЯХ), var2 + ЯЯе, var10 + ЯЯА, ЯЯ4, this.сг);
         this.text(trim(MessengerClient.method_4(var1, ЯЯп, ""), var4.width - ЯЯД, ЯЯЪ), var2 + ЯЯт, var10 + ЯЯа, ЯЯЮ, this.сЙ);
         var10 += ЯЯЧ;
      }

      for (String var12 : var4.lines) {
         this.text(var12, var2 + ЯЯЛ, var10, var4.font, var7 ? this.ск : this.сЙ);
         var10 += var4.lineHeight;
      }

      float var21 = var10 + (var4.inlineStamp ? -var4.lineHeight + var4.font - ЯЯ诶 : 0.0F);
      String var22 = (MessengerClient.method_4_6da43b94(var1, ЯЯ1) > 0L ? ЯЯЬ : "") + time(MessengerClient.method_4_6da43b94(var1, ЯЯ衣));
      this.text(var22, var2 + var4.width - ЯЯш - width(var22, ЯЯИ) - (var6 ? 25 : 0), var21, ЯЯь, this.ск);
      if (var6) {
         this.icon(
            MessengerClient.method_4_bbeebd87(var1, ЯЯж) ? ЯЯ弟 : (MessengerClient.method_4_bbeebd87(var1, ЯЯб) ? ЯЯЫ : ЯЯl),
            var2 + var4.width - ЯЯ8 - ЯЯ5,
            var21 - ЯЯв,
            ЯЯъ,
            this.сг
         );
      }

      float var13 = var3 + var4.height - var4.reactionHeight - ЯЯМ;

      for (MessengerScreen.ReactionLayout var15 : var4.reactions) {
         float var16 = var2 + ЯЯН + var15.x;
         float var17 = var13 + var15.y;
         this.rect(var16, var17, var15.width, ЯЯн, ЯЯЦ, var15.mine ? this.сд : this.сУ);
         int var18 = var15.emoji.isEmpty() ? -1 : reactionIndex(var15.emoji.codePointAt(0));
         if (var18 >= 0) {
            this.emoji(var18, var16 + ЯЯч, var17 + ЯЯЭ, ЯЯ必, -1);
         } else {
            this.text(var15.emoji, var16 + ЯЯщ, var17 + ЯЯГ, ЯЯл, this.сЙ);
         }

         float var19 = var17 + ЯЯэ - ClientFonts.getMainMenuTextFont().getFontFace().getTextCenterOffsetY(ЯЯю, ЯЯЩ);
         this.text(var15.count, var16 + ЯЯу + ЯЯ2 + ЯЯф, var19, ЯЯи, this.сЙ);
      }
   }

   private void toggle(long var1) {
      if (!this.а.remove(var1)) {
         this.а.add(var1);
      }
   }

   private void updateColors() {
      this.сЙ = ThemeEditor.getColorArgb(ThemeColorSlot.ACTIVE_TEXT);
      this.ск = ThemeEditor.getColorArgb(ThemeColorSlot.DEACTIVE_TEXT);
      this.сг = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      this.с0 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO);
      this.сх = ThemeEditor.getColorArgb(ThemeColorSlot.BACKGROUND);
      this.сЕ = ThemeEditor.getColorArgb(ThemeColorSlot.OUTLINE);
      this.сП = ThemeEditor.getColorArgb(ThemeColorSlot.LINE);
      this.с西 = ThemeEditor.getColorArgb(ThemeColorSlot.SHADOW);
      this.сд = ThemeEditor.getColorArgb(ThemeColorSlot.SELECTED);
      this.сУ = ColorMath.lerpArgb(this.сх, this.сд, ВСК);
      this.сЗ = ColorMath.withAlpha(this.сх, ВСI);
      this.сз = ColorMath.lerpArgb(this.сх, this.сд, ВСТ);
      this.сС = ColorMath.lerpArgb(this.сх, this.сг, ВСО);
      this.сБ = ColorMath.withAlpha(this.сг, (this.сг >>> 24) / ВСФ * ВСЙ);
   }

   private void centered(String var1, float var2, float var3, float var4, int var5) {
      this.text(var1, var2 - width(var1, var4) / 2.0F, var3, var4, var5);
   }

   private static String presence(JsonObject var0) {
      if (MessengerClient.method_4_bbeebd87(var0, ЯЯз)) {
         return ЯЯц;
      }

      long var1 = MessengerClient.method_4_6da43b94(var0, ЯЯя);
      if (var1 <= 0L) {
         return ЯХс;
      }

      long var3 = Math.max(0L, (System.currentTimeMillis() - var1) / ЯХВ);
      return var3 < 1L ? ЯХЯ : (var3 < ЯХХ ? "был(а) " + var3 + " мин. назад" : "был(а) в " + time(var1));
   }

   private float sendProgress(JsonObject var1) {
      for (MessengerScreen.PendingMessage var3 : this.в) {
         if (var3.Яц.equals(this.Я_8dfd5497.必()) && var3.б(var1)) {
            return var3.см.getValue();
         }
      }

      return 1.0F;
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.сХ_bbeebd87) {
         return true;
      }

      if (this.Д != null) {
         this.Вн = Math.max(0.0F, this.Вн - (float)var7 * ЯДl);
         return true;
      }

      if (!this.hasMessageOverlay() && !this.hasProfileOverlay()) {
         Vector2f var9 = WorldProjection.guiToScreen(var1, var3);
         if (var9.x / this.ВЦ < this.Вь + this.ВЫ) {
            this.Вв = (float)(this.Вв - var7 * ЯД8);
         } else {
            this.В5 = Math.max(0.0F, this.В5 + (float)var7 * ЯД5);
         }

         return true;
      } else {
         return true;
      }
   }

   private static float width(String var0, float var1) {
      float var2 = 0.0F;
      StringBuilder var3 = new StringBuilder();

      for (int var7 : var0.codePoints().toArray()) {
         if (reactionIndex(var7) >= 0) {
            var2 += var1 + ЯДИ;
         } else if (fallbackPunctuation(var7)) {
            var2 += UiDrawing.getTextWidth(ClientFonts.getMediumFont(), Character.toString(var7), var1, 1.0F);
         } else if (var7 != ЯДь) {
            var3.appendCodePoint(var7);
         }
      }

      return var2 + UiDrawing.getTextWidth(ClientFonts.getMainMenuTextFont(), var3.toString(), var1, 1.0F);
   }

   private void plainText(String var1, float var2, float var3, float var4, int var5) {
      UiDrawing.drawText(ClientFonts.getMainMenuTextFont(), var1, var2 * this.ВЦ, var3 * this.ВЦ, var4 * this.ВЦ, var5, 0.0F, ЯДш, 1.0F);
   }

   private void send() {
      if (!this.В_00e801f9.getValue().isBlank()) {
         EditBox var1 = this.В_00e801f9;
         String var2 = this.В_00e801f9.getValue();
         boolean var3 = var2.stripLeading().matches(Я40);
         if (!var3 || this.Я_8dfd5497.必().equals(Я4х) && IrcRoles.рП()) {
            if (!var3
               && this.Я_8dfd5497.必().equals(Я4З)
               && MessengerClient.method_4_bbeebd87(this.Я_8dfd5497.method_4_e1451646(), Я4Е)
               && MessengerClient.method_4_6da43b94(this.Я_8dfd5497.method_4_e1451646(), Я4П) > System.currentTimeMillis()) {
               this.Я_8dfd5497
                  .method_4_de5a6f78(
                     "Мут до "
                        + time(MessengerClient.method_4_6da43b94(this.Я_8dfd5497.method_4_e1451646(), Я4西))
                        + ": "
                        + MessengerClient.method_4(this.Я_8dfd5497.method_4_e1451646(), Я4д, "")
                  );
            } else {
               JsonObject var4 = new JsonObject();
               var4.addProperty(Я4Б, var2);
               if (this.п != null) {
                  var4.addProperty(Я4С, MessengerClient.method_4_6da43b94(this.п, Я4з));
               }

               if (this.field_4 != null) {
                  var4.addProperty(Я4ц, MessengerClient.method_4_6da43b94(this.field_4, Я4я));
               }

               JsonObject var5 = var4.deepCopy();
               var5.addProperty(Япс, -1);
               var5.addProperty(ЯпВ, this.Я_8dfd5497.Й());
               var5.addProperty(ЯпЯ, MessengerClient.method_4(MessengerClient.method_4_e1451646(this.Я_8dfd5497.method_4_e1451646(), ЯпХ), Япе, ""));
               var5.addProperty(ЯпА, this.ь.getOrDefault(this.Я_8dfd5497.Й(), IrcRoles.Яп()));
               var5.addProperty(Яп4, System.currentTimeMillis());
               var5.addProperty(Япп, true);
               if (this.field_4 != null) {
                  var5.addProperty(ЯпД, MessengerClient.method_4(this.field_4, ЯпЪ, ""));
                  var5.addProperty(Япт, MessengerClient.method_4(this.field_4, Япа, ""));
               }

               MessengerScreen.PendingMessage var6 = this.п == null && !var3 ? new MessengerScreen.PendingMessage(this.Я_8dfd5497.必(), var5) : null;
               boolean var7 = this.Я_8dfd5497.method_4(this.п != null ? ЯпЮ : ЯпЧ, var4, () -> {
                  if (var6 != null) {
                     var6.l(MessengerClient.method_4_6da43b94(this.Я_8dfd5497.method_4_e1451646(), ЯДЭ));
                  }

                  if (var1.getValue().equals(var2)) {
                     var1.setValue("");
                  }

                  if (this.В_00e801f9 == var1) {
                     this.field_4 = this.п = null;
                     this.В5 = 0.0F;
                     this.Я_8dfd5497.Ф();
                  }
               });
               if (var7 && var6 != null) {
                  var6.Яя = MessengerClient.method_4(var4, ЯпЛ, "");
                  this.в.add(var6);
                  this.В5 = 0.0F;
                  this.Я_8dfd5497.Ф();
               }
            }
         } else {
            this.Я_8dfd5497.method_4_de5a6f78(Я4У);
         }
      }
   }

   private void input(EditBox var1, float var2, float var3, float var4, float var5, String var6, String var7) {
      this.glass(var2, var3, var4, var5, var5 / 2.0F);
      float var8 = var7 == null ? Япь : Япж;
      if (var7 != null) {
         this.icon(var7, var2 + Яп弟, var3 + (var5 - Япб) / 2.0F, ЯпЫ, this.ск);
      }

      String var9 = var1.getValue();
      int var10 = var1.getCursorPosition();
      int var11 = 0;

      while (var11 < var10 && width(var9.substring(var11, var10), Япl) > var4 - var8 - Яп8) {
         var11 = var9.offsetByCodePoints(var11, 1);
      }

      String var12 = trim(var9.substring(var11), var4 - var8 - Яп5, Япв);
      this.scissor(var2 + var8, var3 + Япъ, var4 - var8 - ЯпМ, var5 - ЯпН);
      if (var1.isFocused()) {
         String var13 = var1.getHighlighted();
         if (!var13.isEmpty()) {
            int var14 = var9.indexOf(var13, Math.max(0, var10 - var13.length()));
            if (var14 >= var11) {
               this.rect(var2 + var8 + width(var9.substring(var11, var14), Япн), var3 + ЯпЦ, width(var13, Япч), var5 - ЯпЭ, 2.0F, this.сд);
            }
         }
      }

      this.text(var9.isEmpty() ? var6 : var12, var2 + var8, var3 + (var5 - Яп必) / 2.0F, Япщ, var9.isEmpty() ? this.ск : this.сЙ);
      if (var1.isFocused() && System.currentTimeMillis() % ЯпГ < Япл) {
         this.rect(var2 + var8 + width(var9.substring(var11, var10), Япэ), var3 + Япю, ЯпЩ, var5 - Япу, 0.0F, this.сЙ);
      }

      UiDrawing.popScissor();
      int var15 = var11;
      this.hit(var2, var3, var4, var5, () -> {
         this.focus(var1);
         int var6x = var15;

         while (var6x < var9.length() && width(var9.substring(var15, var6x), ЯДН) < this.Вl - var2 - var8) {
            var6x = var9.offsetByCodePoints(var6x, 1);
         }

         var1.moveCursorTo(var6x, false);
      }, null);
   }

   private JsonObject author(JsonObject var1) {
      String var2 = MessengerClient.method_4(var1, Я4р, "");
      if (var2.equals(this.Я_8dfd5497.Й())) {
         return MessengerClient.method_4_e1451646(this.Я_8dfd5497.method_4_e1451646(), Я46);
      }

      JsonObject var3 = MessengerClient.method_4_e1451646(this.Я_8dfd5497.method_4_e1451646(), Я4_);
      if (var2.equals(MessengerClient.method_4(var3, Я4Ш, ""))) {
         return var3;
      }

      for (JsonElement var5 : MessengerClient.method_4_fea5d33f(this.Я_8dfd5497.method_4_e1451646(), Я4ы)) {
         if (var2.equals(MessengerClient.method_4(var5.getAsJsonObject(), Я4й, ""))) {
            return var5.getAsJsonObject();
         }
      }

      JsonObject var6 = new JsonObject();
      var6.addProperty(Я4Ж, var2);
      var6.addProperty(Я4м, MessengerClient.method_4(var1, Я4К, var2));
      return var6;
   }

   @Override
   protected void init() {
      if (this.Я_00e801f9 == null) {
         this.Я_00e801f9 = this.field(64);
         this.Х_00e801f9 = this.field(80);
         this.В_00e801f9 = this.Ы.computeIfAbsent(this.Я_8dfd5497.必(), this::draft);
         this.西 = this.Я_8dfd5497.必();
         this.В_00e801f9.setFocused(true);
      }

      for (KeyMapping var4 : new KeyMapping[]{
         this.minecraft.options.keyUp,
         this.minecraft.options.keyDown,
         this.minecraft.options.keyLeft,
         this.minecraft.options.keyRight,
         this.minecraft.options.keyJump,
         this.minecraft.options.keyShift,
         this.minecraft.options.keySprint
      }) {
         var4.setDown(false);
      }

      this.Я_8dfd5497.т();
   }

   private void copySelection(List<JsonObject> var1) {
      StringJoiner var2 = new StringJoiner(ЯпИ);
      var1.stream()
         .filter(var1x -> this.а.contains(MessengerClient.method_4_6da43b94(var1x, ЯДч)))
         .forEach(var1x -> var2.add(MessengerClient.method_4(var1x, ЯДн, "") + ": " + MessengerClient.method_4(var1x, ЯДЦ, "")));
      this.minecraft.keyboardHandler.setClipboard(var2.toString());
   }

   private void blur(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (ClickGuiPreferences.х弟() > 0) {
         UiDrawing.drawBlur(var1 * this.ВЦ, var2 * this.ВЦ, var3 * this.ВЦ, var4 * this.ВЦ, var5 * this.ВЦ, var6 * this.ВЦ, -1);
      }
   }

   private void hit(float var1, float var2, float var3, float var4, Runnable var5, Runnable var6) {
      if (var4 > 0.0F) {
         this.field_5.add(new MessengerScreen.HitRegion(var1, var2, var3, var4, var5, var6));
      }
   }

   private void plainIconButton(float var1, float var2, float var3, String var4, Runnable var5) {
      if (this.hovered(var1, var2, var3, var3)) {
         this.rect(var1, var2, var3, var3, var3 / 2.0F, this.сБ);
      }

      this.icon(var4, var1 + var3 * ЯДп, var2 + var3 * ЯДД, var3 * ЯДЪ, this.сЙ);
      this.hit(var1, var2, var3, var3, var5, null);
   }

   private void focus(EditBox var1) {
      this.Я_00e801f9.setFocused(var1 == this.Я_00e801f9);
      this.В_00e801f9.setFocused(var1 == this.В_00e801f9);
      this.Х_00e801f9.setFocused(var1 == this.Х_00e801f9);
   }

   private void icon(String var1, float var2, float var3, float var4, int var5) {
      MessengerIcons.ф(var1, var2 * this.ВЦ, var3 * this.ВЦ, var4 * this.ВЦ, var5);
   }

   private void texture(float var1, float var2, float var3, float var4, float var5, int var6, Identifier var7) {
      UiDrawing.drawTexture(var1 * this.ВЦ, var2 * this.ВЦ, var3 * this.ВЦ, var4 * this.ВЦ, var5 * this.ВЦ, new Vector4f(0.0F, 0.0F, 1.0F, 1.0F), var6, var7);
   }

   private void bubbleShape(float var1, float var2, float var3, float var4, boolean var5, String var6, int var7) {
      String var8 = (var5 ? ЯЯй : ЯЯЖ) + var6;
      Identifier var9 = 弟.computeIfAbsent(var8, var0 -> ClientResourceIds.getImageId("messenger/telegram/bubble_" + var0));
      float var10 = var5 ? ЯЯм : ЯЯК;
      float var11 = ЯЯI - var10;
      float var12 = var3 + ЯЯТ;
      float var13 = var4 + ЯЯО;
      float[] var14 = new float[]{0.0F, var10, var10 + 1.0F, ЯЯФ};
      float[] var15 = new float[]{0.0F, ЯЯЙ, ЯЯк, ЯЯг};
      float[] var16 = new float[]{0.0F, var10, var12 - var11, var12};
      float[] var17 = new float[]{0.0F, ЯЯ0, var13 - ЯЯх, var13};
      float var18 = var1 - (var5 ? 2 : 8);
      float var19 = var2 - 2.0F;

      for (int var20 = 0; var20 < 3; var20++) {
         for (int var21 = 0; var21 < 3; var21++) {
            UiDrawing.drawTexture(
               (var18 + var16[var21]) * this.ВЦ,
               (var19 + var17[var20]) * this.ВЦ,
               (var16[var21 + 1] - var16[var21]) * this.ВЦ,
               (var17[var20 + 1] - var17[var20]) * this.ВЦ,
               0.0F,
               new Vector4f(var14[var21] / ЯЯУ, var15[var20] / ЯЯЗ, (var14[var21 + 1] - var14[var21]) / ЯЯЕ, (var15[var20 + 1] - var15[var20]) / ЯЯП),
               var7,
               var9
            );
         }
      }
   }

   private static List<String> wrap(String var0, float var1, float var2) {
      String var3 = var1 + ":" + var2 + ":" + var0;
      List var4 = field_8.get(var3);
      if (var4 != null) {
         return var4;
      }

      ArrayList var5 = new ArrayList();

      for (String var9 : var0.split(ЯД弟, -1)) {
         StringBuilder var10 = new StringBuilder();

         for (int var14 : var9.codePoints().toArray()) {
            String var15 = new String(Character.toChars(var14));

            while (!var10.isEmpty() && width(var10 + var15, var2) > var1) {
               int var16 = var10.lastIndexOf(ЯДб);
               if (var16 > 0) {
                  var5.add(var10.substring(0, var16));
                  var10.delete(0, var16 + 1);
               } else {
                  var5.add(var10.toString());
                  var10.setLength(0);
               }
            }

            var10.append(var15);
         }

         var5.add(var10.toString());
      }

      if (field_8.size() > 512) {
         field_8.clear();
      }

      field_8.put(var3, var5);
      return var5;
   }

   @Override
   public void onClose() {
      if (!this.сХ_bbeebd87) {
         this.сХ_bbeebd87 = true;
         this.ъ.setSpeed(ЯДв);
      }
   }

   private void drawConversation() {
      float var1 = this.Вь + this.ВЫ;
      float var2 = this.В弟 - this.ВЫ;
      JsonObject var3 = this.Я_8dfd5497.method_4_e1451646();
      boolean var4 = this.Я_8dfd5497.必().equals(Вц西);
      boolean var5 = this.Я_8dfd5497.必().equals(MessengerClient.method_4(var3, Вцд, ""));
      if (var5 && !this.Я_8dfd5497.必().equals(this.Б)) {
         this.Б = this.Я_8dfd5497.必();
         this.М.setImmediately(0.0F);
      }

      this.М.updateTarget(1.0F);
      JsonObject var6 = var5 ? MessengerClient.method_4_e1451646(var3, ВцБ) : new JsonObject();
      String var7 = var4 ? ВцС : MessengerClient.method_4(var6, Вцз, this.Я_8dfd5497.必());
      JsonObject var8 = var5 ? MessengerClient.method_4_e1451646(var3, Вцц) : new JsonObject();
      boolean var9 = this.field_4 != null || this.п != null || this.я;
      float var10 = this.Вж + (var8.has(Вця) ? 144 : 88);
      float var11 = this.Вж + this.Вб - (var9 ? 123 : 86);
      ArrayList var12 = new ArrayList();
      if (var5) {
         for (JsonElement var14 : MessengerClient.method_4_fea5d33f(var3, Вяс)) {
            var12.add(var14.getAsJsonObject());
         }
      }

      this.в.forEach(var0 -> var0.см.updateTarget(1.0F));
      this.в.removeIf(var1x -> var1x.еА < 0L || var1x.Iх() && !this.Я_8dfd5497.method_4_bbeebd87(var1x.Яя) || !var1x.Iх() && var1x.см.getValue() >= ЯДК);
      if (var5) {
         for (MessengerScreen.PendingMessage var25 : this.в) {
            if (var25.Iх() && var25.Яц.equals(this.Я_8dfd5497.必())) {
               var12.add(var25.Ч);
            }
         }
      }

      this.б.clear();
      this.ж.clear();

      for (int var23 = 0; var23 < var12.size(); var23++) {
         JsonObject var26 = (JsonObject)var12.get(var23);
         String var15 = MessengerClient.method_4(var26, ВяВ, "");
         if (!var15.isEmpty()) {
            this.ж.put(var15, var26);
         }

         boolean var16 = var23 > 0 && joined((JsonObject)var12.get(var23 - 1), (JsonObject)var12.get(var23));
         boolean var17 = var23 + 1 < var12.size() && joined((JsonObject)var12.get(var23), (JsonObject)var12.get(var23 + 1));
         this.б.put(MessengerClient.method_4_6da43b94((JsonObject)var12.get(var23), ВяЯ), var16 ? (var17 ? ВяХ : Вяе) : (var17 ? ВяА : Вя4));
      }

      float var24 = MessengerClient.method_4_bbeebd87(var3, Вяп) ? ВяД : ВяЪ;

      for (int var27 = 0; var27 < var12.size(); var27++) {
         float var29 = this.sendProgress((JsonObject)var12.get(var27));
         if (dayStart(var12, var27)) {
            var24 += Вят * var29;
         }

         var24 += (this.bubbleLayout((JsonObject)var12.get(var27), var2).height + messageGap(var12, var27)) * var29;
      }

      this.ВН = var24;
      if (this.сА != this.Я_8dfd5497.method_4_6da43b94()) {
         this.сА = this.Я_8dfd5497.method_4_6da43b94();
         if (this.В5 < Вяа) {
            this.В5 = 0.0F;
         }
      }

      this.В5 = Math.clamp(this.В5, 0.0F, Math.max(0.0F, var24 - (var11 - var10)));
      float var28 = UiDrawing.getGlobalAlpha();
      UiDrawing.setGlobalAlpha(var28 * this.М.getValue());
      this.scissor(var1 + ВяЮ, var10, var2 - ВяЧ, var11 - var10);
      float var30 = var11 - var24 + this.В5 + ВяЛ * (1.0F - this.М.getValue());
      if (var5 && MessengerClient.method_4_bbeebd87(var3, Вя诶)) {
         this.pill(var1 + var2 / 2.0F - Вя1, Math.max(var10, var30), ВяЬ, Вя衣, Вяш, () -> {
            this.Я_8dfd5497.Ч();
            this.В5 = 0.0F;
         });
         var30 += ВяИ;
      } else {
         var30 += Вяь;
      }

      for (int var32 = 0; var32 < var12.size(); var32++) {
         JsonObject var35 = (JsonObject)var12.get(var32);
         float var18 = this.sendProgress(var35);
         if (dayStart(var12, var32)) {
            String var19 = date(MessengerClient.method_4_6da43b94(var35, Вяж));
            float var20 = width(var19, Вя弟) + Вяб;
            float var21 = UiDrawing.getGlobalAlpha();
            UiDrawing.setGlobalAlpha(var21 * var18);
            this.rect(var1 + (var2 - var20) / 2.0F, var30, var20, ВяЫ, Вяl, this.сУ);
            this.centered(var19, var1 + var2 / 2.0F, var30 + Вя8, Вя5, this.ск);
            var30 += Вяв * var18;
            UiDrawing.setGlobalAlpha(var21);
         }

         MessengerScreen.BubbleLayout var41 = this.bubbleLayout(var35, var2);
         if (var30 + var41.height > var10 && var30 < var11) {
            this.drawBubble(var35, var1, var30, var2, var41, var10, var11);
         }

         var30 += (var41.height + messageGap(var12, var32)) * var18;
      }

      if (var12.isEmpty()) {
         String var33 = !var5 ? Вяъ : ВяМ;
         float var36 = width(var33, ВяН) + Вян;
         this.rect(var1 + (var2 - var36) / 2.0F, this.Вж + this.Вб / 2.0F - ВяЦ, var36, Вяч, ВяЭ, this.сУ);
         this.centered(var33, var1 + var2 / 2.0F, this.Вж + this.Вб / 2.0F - Вя必, Вящ, this.сЙ);
      }

      UiDrawing.popScissor();
      UiDrawing.setGlobalAlpha(var28);
      this.gradient(var1 + 2.0F, this.Вж + 1.0F, var2 - ВяГ, Вял, 0.0F, this.сх, ColorMath.withAlpha(this.сх, 0.0F));
      float var34 = Math.max(Вяэ, Math.min(Вяю, width(var7, ВяЩ) + Вяу));
      float var37 = var1 + (var2 - var34) / 2.0F;
      this.glass(var37, this.Вж + Вя2, var34, Вяф, Вяи);
      this.centered(trim(var7, var34 - Вя9, Вяо), var1 + var2 / 2.0F, this.Вж + ВяO, Вя7, this.сЙ);
      this.centered(var4 ? ВяР : presence(var6), var1 + var2 / 2.0F, this.Вж + Вя3, Вяр, this.ск);
      if (!var4) {
         this.hit(var37, this.Вж + Вя6, var34, Вя_, () -> this.showProfile(var6), null);
      }

      this.glass(var1 + var2 - ВяШ, this.Вж + Вяы, Вяй, ВяЖ, Вям);
      this.avatar(var6, var1 + var2 - ВяК, this.Вж + ВяI, ВяТ, var4);
      this.hit(var1 + var2 - ВяО, this.Вж + ВяФ, ВяЙ, Вяк, () -> {
         if (!var4) {
            this.showProfile(var6);
         }
      }, null);
      if (var8.has(Вяг)) {
         this.glass(var1 + Вя0, this.Вж + Вях, var2 - ВяУ, ВяЗ, ВяЕ);

         for (int var38 = 0; var38 < 3; var38++) {
            this.rect(var1 + ВяП, this.Вж + Вя西 + var38 * 11, 2.0F, Вяд, 1.0F, this.сг);
         }

         this.text(ВяБ, var1 + ВяС, this.Вж + Вяз, Вяц, this.сЙ);
         this.text(trim(MessengerClient.method_4(var8, Вяя, ""), var2 - Ясс, ЯсВ), var1 + ЯсЯ, this.Вж + ЯсХ, Ясе, this.ск);
         this.icon(ЯсА, var1 + var2 - Яс4, this.Вж + Ясп, ЯсД, this.сЙ);
         this.hit(var1 + ЯсЪ, this.Вж + Яст, var2 - Яса, ЯсЮ, () -> {
            this.А_e1451646 = var8;
            this.Въ = this.Вl;
            this.ВМ = this.В8;
         }, null);
      }

      if (this.Я_8dfd5497.я()) {
         this.pill(var1 + var2 - ЯсЧ, var11 - ЯсЛ, Яс诶, Яс1, ЯсЬ, () -> {
            this.Я_8dfd5497.Ф();
            this.В5 = 0.0F;
         });
      } else if (this.В5 > Яс衣) {
         this.iconButton(var1 + var2 - Ясш, var11 - ЯсИ, Ясь, Ясж, () -> this.В5 = 0.0F);
      }

      if (var5 && !var12.isEmpty() && this.В5 < Яс弟 && !this.сХ_bbeebd87 && !this.hasMessageOverlay() && !this.hasProfileOverlay()) {
         this.Я_8dfd5497.method_4(MessengerClient.method_4_6da43b94((JsonObject)var12.getLast(), Ясб));
      }

      this.gradient(var1 + 1.0F, this.Вж + this.Вб - ЯсЫ, var2 - 2.0F, Ясl, 0.0F, ColorMath.withAlpha(this.сх, 0.0F), this.сх);
      if (var9) {
         this.glass(var1 + Яс8, this.Вж + this.Вб - Яс5, var2 - Ясв, Ясъ, ЯсМ);
         this.icon(this.п != null ? ЯсН : (this.я ? Ясн : ЯсЦ), var1 + Ясч, this.Вж + this.Вб - ЯсЭ, Яс必, this.сг);
         String var39 = this.я
            ? "Выбрано: " + this.а.size() + " · Копировать"
            : (this.п != null ? Ясщ : ЯсГ) + MessengerClient.method_4(this.п != null ? this.п : this.field_4, Ясл, "");
         this.text(trim(var39, var2 - Ясэ, Ясю), var1 + ЯсЩ, this.Вж + this.Вб - Ясу, Яс2, this.сЙ);
         this.hit(var1 + Ясф, this.Вж + this.Вб - Яси, var2 - Яс9, Ясо, () -> {
            if (this.я) {
               this.copySelection(var12);
            }
         }, null);
         this.plainIconButton(var1 + var2 - ЯсO, this.Вж + this.Вб - Яс7, ЯсР, Яс3, () -> {
            this.field_4 = this.п = null;
            this.я = false;
            this.а.clear();
         });
      }

      this.iconButton(var1 + Яср, this.Вж + this.Вб - Яс6, Яс_, ЯсШ, () -> this.сЯ = !this.сЯ);
      this.input(this.В_00e801f9, var1 + Ясы, this.Вж + this.Вб - Ясй, var2 - ЯсЖ, Ясм, ЯсК, null);
      this.iconButton(var1 + var2 - ЯсI, this.Вж + this.Вб - ЯсТ, ЯсО, this.Я_8dfd5497.method_1_bbeebd87() ? ЯсФ : ЯсЙ, this::send);
      boolean var40 = var5 && var4 && MessengerClient.method_4_bbeebd87(var3, Яск) && MessengerClient.method_4_6da43b94(var3, Ясг) > System.currentTimeMillis();
      if (!this.Я_8dfd5497.method_1_388008bb().isEmpty() || !this.Я_8dfd5497.п() || var40 || !this.Я_8dfd5497.Ы_388008bb().isEmpty()) {
         String var42 = !this.Я_8dfd5497.method_1_388008bb().isEmpty()
            ? this.Я_8dfd5497.method_1_388008bb()
            : (
               !this.Я_8dfd5497.п()
                  ? Яс0
                  : (
                     !this.Я_8dfd5497.Ы_388008bb().isEmpty()
                        ? this.Я_8dfd5497.Ы_388008bb()
                        : "Мут до " + time(MessengerClient.method_4_6da43b94(var3, Ясх)) + ": " + MessengerClient.method_4(var3, ЯсУ, "")
                  )
            );
         this.centered(
            trim(var42, var2 - ЯсЗ, ЯсЕ), var1 + var2 / 2.0F, this.Вж + this.Вб - ЯсП, Яс西, this.Я_8dfd5497.method_1_388008bb().isEmpty() ? this.ск : -27500
         );
      }
   }

   private static String trim(String var0, float var1, float var2) {
      if (var1 <= 0.0F) {
         return "";
      }

      if (width(var0, var2) <= var1) {
         return var0;
      }

      int var3 = var0.length();

      while (var3 > 0 && width(var0.substring(0, var3) + "…", var2) > var1) {
         var3 = var0.offsetByCodePoints(var3, -1);
      }

      return var0.substring(0, var3) + "…";
   }

   private static boolean fallbackPunctuation(int var0) {
      return var0 == 8230 || var0 == 183;
   }

   private void rect(float var1, float var2, float var3, float var4, float var5, int var6) {
      UiDrawing.drawRoundedRect(var1 * this.ВЦ, var2 * this.ВЦ, var3 * this.ВЦ, var4 * this.ВЦ, var5 * this.ВЦ, var6);
   }

   private void rememberRole(JsonObject var1) {
      String var2 = MessengerClient.method_4(var1, ВСЗ, "");
      String var3 = MessengerClient.method_4(var1, ВСЕ, "");
      if (!var2.isEmpty() && !var3.isBlank()) {
         this.ь.put(var2, var3);
      }
   }

   private void avatar(JsonObject var1, float var2, float var3, float var4, boolean var5) {
      if (var5) {
         this.gradient(var2, var3, var4, var4, var4 / 2.0F, this.сг, this.с0);
         this.icon(Яп2, var2 + var4 * Япф, var3 + var4 * Япи, var4 * Яп9, this.сЙ);
      } else {
         String var6 = MessengerClient.method_4(var1, Япо, "");
         Identifier var7 = this.l.get(var6);
         if (var7 == null && !var6.isEmpty()) {
            try {
               UUID var8 = UUID.fromString(var6);
               var7 = DefaultPlayerSkin.get(var8).body().texturePath();
               if (this.т.add(var6)) {
                  CompletableFuture.<GameProfile>supplyAsync(
                        () -> this.minecraft.services().profileResolver().fetchById(var8).orElse(new GameProfile(var8, MessengerClient.method_4(var1, ЯДъ, ЯДМ)))
                     )
                     .thenCompose(var1x -> this.minecraft.getSkinManager().get(var1x))
                     .thenAccept(var2x -> this.minecraft.execute(() -> var2x.ifPresent(var2xxx -> this.l.put(var6, var2xxx.body().texturePath()))))
                     .exceptionally(var0 -> null);
               }
            } catch (RuntimeException var9) {
            }
         }

         this.gradient(var2, var3, var4, var4, var4 / 2.0F, this.сг, this.с0);
         if (var7 != null) {
            UiDrawing.drawTexture(
               var2 * this.ВЦ,
               var3 * this.ВЦ,
               var4 * this.ВЦ,
               var4 * this.ВЦ,
               new Vector4f(var4 * this.ВЦ / 2.0F),
               new Vector4f(ЯпO),
               new Vector4i(-1),
               var7,
               FilterMode.NEAREST
            );
            UiDrawing.drawTexture(
               var2 * this.ВЦ,
               var3 * this.ВЦ,
               var4 * this.ВЦ,
               var4 * this.ВЦ,
               new Vector4f(var4 * this.ВЦ / 2.0F),
               new Vector4f(Яп7, ЯпР, Яп3, Япр),
               new Vector4i(-1),
               var7,
               FilterMode.NEAREST
            );
         } else {
            String var10 = MessengerClient.method_4(var1, Яп6, Яп_);
            if (var10.isBlank()) {
               var10 = ЯпШ;
            }

            this.text(var10.substring(0, var10.offsetByCodePoints(0, 1)).toUpperCase(Locale.ROOT), var2 + var4 * Япы, var3 + var4 * Япй, var4 * ЯпЖ, this.сЙ);
         }

         if (MessengerClient.method_4_bbeebd87(var1, Япм)) {
            this.rect(var2 + var4 - ЯпК, var3 + var4 - ЯпI, ЯпТ, ЯпО, ЯпФ, this.сх);
            this.rect(var2 + var4 - ЯпЙ, var3 + var4 - Япк, Япг, Яп0, Япх, ЯпУ);
         }
      }
   }

   private static int reactionIndex(int var0) {
      for (int var1 = 0; var1 < А_0447afad.length; var1++) {
         if (А_0447afad[var1].codePointAt(0) == var0) {
            return var1;
         }
      }

      return -1;
   }

   private void updateRoles() {
      if (this.сХ_6da43b94 != this.Я_8dfd5497.method_4_6da43b94()) {
         this.сХ_6da43b94 = this.Я_8dfd5497.method_4_6da43b94();
         this.ь.clear();
         JsonObject var1 = this.Я_8dfd5497.method_4_e1451646();

         for (JsonElement var3 : MessengerClient.method_4_fea5d33f(var1, ВСк)) {
            this.rememberRole(MessengerClient.method_4_e1451646(var3.getAsJsonObject(), ВСг));
         }

         for (JsonElement var5 : MessengerClient.method_4_fea5d33f(var1, ВС0)) {
            this.rememberRole(var5.getAsJsonObject());
         }

         this.rememberRole(MessengerClient.method_4_e1451646(var1, ВСх));
         this.rememberRole(MessengerClient.method_4_e1451646(var1, ВСУ));
         if (!this.Я_8dfd5497.Й().isEmpty()) {
            this.ь.putIfAbsent(this.Я_8dfd5497.Й(), IrcRoles.Яп());
         }
      }
   }

   private static String date(long var0) {
      LocalDate var2 = Instant.ofEpochMilli(var0).atZone(ZoneId.systemDefault()).toLocalDate();
      if (var2.equals(LocalDate.now())) {
         return ЯЯ西;
      } else {
         return var2.equals(LocalDate.now().minusDays(1L)) ? ЯЯд : DateTimeFormatter.ofPattern(ЯЯБ, Locale.forLanguageTag(ЯЯС)).format(var2);
      }
   }

   private void iconButton(float var1, float var2, float var3, String var4, Runnable var5) {
      this.glass(var1, var2, var3, var3, var3 / 2.0F);
      this.plainIconButton(var1, var2, var3, var4, var5);
   }

   private void pill(float var1, float var2, float var3, float var4, String var5, Runnable var6) {
      this.glass(var1, var2, var3, var4, var4 / 2.0F);
      if (this.hovered(var1, var2, var3, var4)) {
         this.rect(var1, var2, var3, var4, var4 / 2.0F, this.сБ);
      }

      String var7 = trim(var5, var3 - ЯпЗ, var4 > ЯпЕ ? ЯпП : Яп西);
      this.centered(var7, var1 + var3 / 2.0F, var2 + (var4 - (var4 > Япд ? 15 : 12)) / 2.0F, var4 > ЯпБ ? ЯпС : Япз, this.сЙ);
      this.hit(var1, var2, var3, var4, var6, null);
   }

   private void outline(float var1, float var2, float var3, float var4, float var5, int var6) {
      UiDrawing.drawOutline(var1 * this.ВЦ, var2 * this.ВЦ, var3 * this.ВЦ, var4 * this.ВЦ, ЯДт * this.ВЦ, var5 * this.ВЦ, var6);
   }

   private List<JsonObject> rows() {
      ArrayList<JsonObject> var1 = new ArrayList<>();
      HashSet var2 = new HashSet();

      for (JsonElement var4 : MessengerClient.method_4_fea5d33f(this.Я_8dfd5497.method_4_e1451646(), Вц_)) {
         JsonObject var5 = var4.getAsJsonObject();
         if (!this.сс && MessengerClient.method_4(var5, ВцШ, "").toLowerCase(Locale.ROOT).contains(this.д.toLowerCase(Locale.ROOT))) {
            var1.add(var5);
            var2.add(MessengerClient.method_4(var5, Вцы, ""));
         }
      }

      if (!var2.contains(Вцй) && this.д.isEmpty() && !this.сс) {
         JsonObject var8 = new JsonObject();
         var8.addProperty(ВцЖ, Вцм);
         var8.addProperty(ВцК, ВцI);
         var8.addProperty(ВцТ, ВцО);
         var1.addFirst(var8);
      }

      if (this.сс || !this.д.isBlank()) {
         for (JsonElement var10 : MessengerClient.method_4_fea5d33f(this.Я_8dfd5497.method_4_e1451646(), ВцФ)) {
            JsonObject var11 = var10.getAsJsonObject();
            String var6 = MessengerClient.method_4(var11, ВцЙ, "");
            if (var2.add(var6)) {
               JsonObject var7 = new JsonObject();
               var7.addProperty(Вцк, var6);
               var7.addProperty(Вцг, MessengerClient.method_4(var11, Вц0, var6));
               var7.addProperty(Вцх, MessengerClient.method_4_bbeebd87(var11, ВцУ) ? ВцЗ : "Был(а) " + time(MessengerClient.method_4_6da43b94(var11, ВцЕ)));
               var7.add(ВцП, var11);
               var1.add(var7);
            }
         }
      }

      var1.sort(Comparator.comparingInt(var0 -> MessengerClient.method_4(var0, ЯДI, "").equals(ЯДТ) ? 0 : 1));
      return var1;
   }

   private void drawEmojiPicker() {
      float var1 = UiDrawing.getGlobalAlpha();
      float var2 = this.н.getValue();
      UiDrawing.setGlobalAlpha(var1 * var2);
      float var3 = this.Вь + this.В弟 - Яеч;
      float var4 = this.Вж + this.Вб - ЯеЭ + Яе必 * (1.0F - var2);
      this.glass(var3, var4, Яещ, ЯеГ, Яел);

      for (int var5 = 0; var5 < А_0447afad.length; var5++) {
         String var6 = А_0447afad[var5];
         float var7 = var3 + Яеэ + var5 * 45;
         this.emoji(var5, var7 + Яею, var4 + ЯеЩ, Яеу, -1);
         this.hit(var7, var4 + Яе2, Яеф, Яеи, () -> {
            this.В_00e801f9.insertText(var6);
            this.сЯ = false;
            this.focus(this.В_00e801f9);
         }, null);
      }

      UiDrawing.setGlobalAlpha(var1);
   }

   private EditBox field(int var1) {
      EditBox var2 = new EditBox(this.minecraft.font, net.minecraft.network.chat.Component.empty());
      var2.setMaxLength(var1);
      var2.setBordered(false);
      return var2;
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   private static boolean dayStart(List<JsonObject> var0, int var1) {
      return var1 == 0
         || !date(MessengerClient.method_4_6da43b94((JsonObject)var0.get(var1 - 1), ЯЯ9)).equals(date(MessengerClient.method_4_6da43b94((JsonObject)var0.get(var1), ЯЯо)));
   }

   private static boolean joined(JsonObject var0, JsonObject var1) {
      return MessengerClient.method_4(var0, ЯЯР, "").equals(MessengerClient.method_4(var1, ЯЯ3, ""))
         && Math.abs(MessengerClient.method_4_6da43b94(var0, ЯЯр) - MessengerClient.method_4_6da43b94(var1, ЯЯ6)) < ЯЯ_
         && date(MessengerClient.method_4_6da43b94(var0, ЯЯШ)).equals(date(MessengerClient.method_4_6da43b94(var1, ЯЯы)));
   }

   private String roleLabel(JsonObject var1) {
      String var2 = MessengerClient.method_4(var1, ВСП, "");
      if (var2.isBlank()) {
         var2 = this.ь.getOrDefault(MessengerClient.method_4(var1, ВС西, ""), ВСд);
      }

      String var3 = IrcRoles.Ц7(var2);
      return var3.equals(ВСБ) ? ВСС : var3;
   }

   private void drawProfile() {
      float var1 = UiDrawing.getGlobalAlpha();
      float var2 = this.Х_c38ce5db.к.getValue();
      UiDrawing.setGlobalAlpha(var1 * var2);
      JsonObject var3 = this.Х_c38ce5db.т;
      this.field_5.clear();
      this.blur(this.Вь, this.Вж, this.В弟, this.Вб, ЯАе, ЯАА);
      this.rect(this.Вь, this.Вж, this.В弟, this.Вб, ЯА4, ЯАп);
      this.hit(this.Вь, this.Вж, this.В弟, this.Вб, this::closeProfile, null);
      float var4 = ЯАД;
      float var5 = ЯАЪ;
      float var6 = this.Вь + this.ВЫ + (this.В弟 - this.ВЫ - var4) / ЯАт;
      float var7 = this.Вж + (this.Вб - var5) / 2.0F + ЯАа * (1.0F - var2);
      this.rect(var6, var7, var4, var5, ЯАЮ, this.сх);
      this.hit(var6, var7, var4, var5, () -> {}, null);
      int var8 = ColorMath.lerpArgb(this.сх, this.сг, ЯАЧ);
      int var9 = ColorMath.lerpArgb(this.сх, this.с0, ЯАЛ);
      this.gradient(var6, var7, var4, ЯА诶, ЯА1, var8, var9);
      this.rect(var6, var7 + ЯАЬ, var4, ЯА衣, 0.0F, var9);
      this.iconButton(var6 + ЯАш, var7 + ЯАИ, ЯАь, ЯАж, this::closeProfile);
      boolean var10 = MessengerClient.method_4(var3, ЯА弟, "").equals(this.Я_8dfd5497.Й());
      this.pill(var6 + var4 - ЯАб, var7 + ЯАЫ, ЯАl, ЯА8, var10 ? ЯА5 : ЯАв, () -> {
         if (var10) {
            this.сВ = true;
            this.Х_00e801f9.setValue(MessengerClient.method_4(var3, ЯДф, ""));
            this.focus(this.Х_00e801f9);
         } else {
            this.choose(MessengerClient.method_4(var3, ЯДи, ""));
            this.Ъ = null;
         }
      });
      this.avatar(var3, var6 + var4 / 2.0F - ЯАъ, var7 + ЯАМ, ЯАН, false);
      String var11 = trim(MessengerClient.method_4(var3, ЯАн, ЯАЦ), var4 - ЯАч, ЯАЭ);
      this.centered(var11, var6 + var4 / 2.0F, var7 + ЯА必, ЯАщ, this.сЙ);
      this.centered(presence(var3), var6 + var4 / 2.0F, var7 + ЯАГ, ЯАл, this.ск);
      float var12 = (var4 - ЯАэ) / ЯАю;
      this.profileTile(var6 + ЯАЩ, var7 + ЯАу, var12, ЯА2, var10 ? ЯАф : ЯАи, () -> {
         if (!var10) {
            this.choose(MessengerClient.method_4(var3, ЯД2, ""));
         }

         this.Ъ = null;
      });
      this.profileTile(var6 + ЯА9 + var12, var7 + ЯАо, var12, ЯАO, ЯА7, () -> this.minecraft.keyboardHandler.setClipboard(MessengerClient.method_4(var3, ЯДу, "")));
      this.profileTile(var6 + ЯАР + var12 * 2.0F, var7 + ЯА3, var12, var10 ? ЯАр : ЯА6, var10 ? ЯА_ : ЯАШ, () -> {
         if (var10) {
            this.сВ = true;
            this.Х_00e801f9.setValue(MessengerClient.method_4(var3, ЯДю, ""));
            this.focus(this.Х_00e801f9);
         } else {
            this.Я_00e801f9.setValue(MessengerClient.method_4(var3, ЯДЩ, ""));
            this.сс = true;
            this.focus(this.Я_00e801f9);
            this.Ъ = null;
         }
      });
      float var13 = var7 + ЯАы;
      this.rect(var6 + ЯАй, var13, var4 - ЯАЖ, ЯАм, ЯАК, this.сУ);
      this.text(ЯАI, var6 + ЯАТ, var13 + ЯАО, ЯАФ, this.сЙ);
      this.text("@" + MessengerClient.method_4(var3, ЯАЙ, ""), var6 + ЯАк, var13 + ЯАг, ЯА0, this.ск);
      this.plainIconButton(var6 + var4 - ЯАх, var13 + ЯАУ, ЯАЗ, ЯАЕ, () -> this.minecraft.keyboardHandler.setClipboard(MessengerClient.method_4(var3, ЯДэ, "")));
      this.rect(var6 + ЯАП, var13 + ЯА西, var4 - ЯАд, ЯАБ, 0.0F, this.сП);
      this.text(ЯАС, var6 + ЯАз, var13 + ЯАц, ЯАя, this.сЙ);
      if (var10 && this.сВ) {
         this.input(this.Х_00e801f9, var6 + Я4с, var13 + Я4В, var4 - Я4Я, Я4Х, Я4е, null);
      } else {
         this.text(
            trim(MessengerClient.method_4(var3, Я4А, "").isBlank() ? Я44 : MessengerClient.method_4(var3, Я4п, ""), var4 - Я4Д, Я4Ъ), var6 + Я4т, var13 + Я4а, Я4Ю, this.ск
         );
      }

      if (var10 && this.сВ) {
         this.pill(var6 + Я4Ч, var7 + Я4Л, var4 - Я4诶, Я41, Я4Ь, () -> {
            JsonObject var1x = new JsonObject();
            var1x.addProperty(ЯДщ, this.Х_00e801f9.getValue());
            this.Я_8dfd5497.method_4(ЯДГ, var1x, () -> {
               this.Ъ = MessengerClient.method_4_e1451646(this.Я_8dfd5497.method_4_e1451646(), ЯДл);
               this.сВ = false;
            });
         });
      } else {
         this.glass(var6 + Я4衣, var7 + Я4ш, var4 - Я4И, Я4ь, Я4ж);
         this.rect(var6 + Я4弟, var7 + Я4б, (var4 - Я4Ы) / 2.0F, Я4l, Я48, this.сд);
         this.centered(Я45, var6 + Я4в + (var4 - Я4ъ) / Я4М, var7 + Я4Н, Я4н, this.сЙ);
         this.centered(Я4Ц, var6 + Я4ч + (var4 - Я4Э) * Я4必 / Я4щ, var7 + Я4Г, Я4л, this.сЙ);
         this.hit(var6 + var4 / 2.0F, var7 + Я4э, var4 / 2.0F - Я4ю, Я4Щ, () -> {
            if (!var10) {
               this.choose(MessengerClient.method_4(var3, ЯД必, ""));
            }

            this.Ъ = null;
         }, null);
      }

      this.outline(var6, var7, var4, var5, Я4у, this.сЕ);
      UiDrawing.setGlobalAlpha(var1);
   }

   private static float messageGap(List<JsonObject> var0, int var1) {
      return var1 + 1 < var0.size() && joined((JsonObject)var0.get(var1), (JsonObject)var0.get(var1 + 1)) ? ЯЯO : ЯЯ7;
   }

   private void react(JsonObject var1, String var2) {
      JsonObject var3 = new JsonObject();
      var3.addProperty(Яп诶, MessengerClient.method_4_6da43b94(var1, Яп1));
      var3.addProperty(ЯпЬ, var2);
      this.Я_8dfd5497.method_4(Яп衣, var3, null);
   }

   private void emoji(int var1, float var2, float var3, float var4, int var5) {
      Vector4f var6 = В_ef6a4edb[var1];
      float var7 = var4 / Math.max(var6.z, var6.w);
      float var8 = var6.z * var7;
      float var9 = var6.w * var7;
      UiDrawing.drawTexture(
         (var2 + (var4 - var8) / 2.0F) * this.ВЦ,
         (var3 + (var4 - var9) / 2.0F) * this.ВЦ,
         var8 * this.ВЦ,
         var9 * this.ВЦ,
         0.0F,
         var6,
         var5 & ЯДЧ | ЯДЛ,
         В_e1fe312e[var1],
         FilterMode.LINEAR
      );
   }

   private void showAuthor(JsonObject var1) {
      String var2 = MessengerClient.method_4(var1, Я4Т, "");
      if (var2.equals(this.Я_8dfd5497.Й())) {
         this.showProfile(MessengerClient.method_4_e1451646(this.Я_8dfd5497.method_4_e1451646(), Я4О));
      } else {
         JsonObject var3 = MessengerClient.method_4_e1451646(this.Я_8dfd5497.method_4_e1451646(), Я4Ф);
         if (var2.equals(MessengerClient.method_4(var3, Я4Й, ""))) {
            this.showProfile(var3);
         } else {
            for (JsonElement var5 : MessengerClient.method_4_fea5d33f(this.Я_8dfd5497.method_4_e1451646(), Я4к)) {
               if (var2.equals(MessengerClient.method_4(var5.getAsJsonObject(), Я4г, ""))) {
                  this.showProfile(var5.getAsJsonObject());
                  return;
               }
            }
         }
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.сХ_bbeebd87) {
         return true;
      }

      if (var1.key() != 256) {
         if (this.hasMessageOverlay()) {
            return true;
         }

         if (this.hasProfileOverlay()) {
            if (this.Ъ != null && this.сВ) {
               this.Х_00e801f9.keyPressed(var1);
            }

            return true;
         } else {
            if (var1.key() != 257 && var1.key() != 335) {
               return this.Я_00e801f9.isFocused() ? this.Я_00e801f9.keyPressed(var1) : this.В_00e801f9.keyPressed(var1);
            }

            if (this.В_00e801f9.isFocused()) {
               this.send();
            }

            return true;
         }
      } else {
         if (this.Ъ != null) {
            this.closeProfile();
         } else if (this.Д != null) {
            this.Д = null;
         } else if (this.А_e1451646 != null) {
            this.А_e1451646 = null;
         } else {
            if (this.hasMessageOverlay() || this.hasProfileOverlay()) {
               return true;
            }

            if (this.сЯ) {
               this.сЯ = false;
            } else if (this.я) {
               this.я = false;
               this.а.clear();
            } else {
               this.onClose();
            }
         }

         return true;
      }
   }

   private record ReactionLayout(String emoji, String count, boolean mine, float x, float y, float width) {
   }

   private record HitRegion(float x, float y, float w, float h, Runnable left, Runnable right) {
   }

   private record BubbleLayout(
      List<String> lines,
      float width,
      float height,
      float font,
      float lineHeight,
      boolean inlineStamp,
      List<MessengerScreen.ReactionLayout> reactions,
      float reactionHeight
   ) {
   }

   private static final class OverlayTransition {
      private final FloatAnimation к;
      private JsonObject т;
      private static final float ХД0 = 12.0F;
      private static final String ХДх = "id";
      private static final String ХДУ = "id";
      private static final float ХДЗ = 0.001F;

      private OverlayTransition() {
         this.к = new FloatAnimation(0.0F, ХД0, Easings.CUBIC_OUT);
      }

      private void Ц(JsonObject var1) {
         if (var1 != null) {
            if (this.т != null && !MessengerClient.method_4(this.т, ХДх, "").equals(MessengerClient.method_4(var1, ХДУ, ""))) {
               this.к.setImmediately(0.0F);
            }

            this.т = var1;
         }

         this.к.updateTarget(var1 == null ? 0.0F : 1.0F);
         if (var1 == null && this.к.getValue() < ХДЗ) {
            this.т = null;
         }
      }
   }

   private static final class PendingMessage {
      private final String Яц;
      private final JsonObject Ч;
      private final FloatAnimation см;
      private long еА;
      private String Яя;
      private static final float ЯЧХ = 7.0F;
      private static final long ЯЧе = -1L;
      private static final String ЯЧА = "id";
      private static final long ЯЧ4 = -1L;

      private boolean Iх() {
         return this.еА == 0L;
      }

      private PendingMessage(String var1, JsonObject var2) {
         this.см = new FloatAnimation(0.0F, ЯЧХ, Easings.CUBIC_OUT);
         this.Яя = "";
         this.Яц = var1;
         this.Ч = var2;
      }

      private void l(long var1) {
         this.еА = var1 > 0L ? var1 : ЯЧе;
      }

      private boolean б(JsonObject var1) {
         return MessengerClient.method_4_6da43b94(var1, ЯЧА) == (this.Iх() ? ЯЧ4 : this.еА);
      }
   }

   private record ContextAction(String icon, String label, Runnable action, boolean danger) {
   }
}
