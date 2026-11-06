package sg.modules.impl.combat;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.HashSet;
import java.util.List;
import lombok.Generated;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.ec.AttackReadiness;
import sg.ec.PacketEventBypass;
import sg.rotation.AimGeometryMath;
import sg.rotation.SnapAimRotation;
import sg.events.impl.PlayerRotationEvent;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.Frame60Event;
import sg.events.impl.WorldEffectsEvent;
import sg.network.ClientChat;
import sg.network.InventoryInteractions;
import sg.util.input.MouseSensitivityMath;
import sg.render.pipeline.TargetEspMask;
import sg.render.pipeline.AimRaycast;
import sg.rotation.HumanAimRotation;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.ModuleRegistry;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.modules.impl.init.CombatTargetSelector;
import sg.modules.impl.render.Sakura;
import sg.settings.FloatSetting;
import sg.events.impl.InputEvent;
import sg.events.impl.MovementInputEvent;
import sg.modules.impl.init.MovementUtils;
import sg.settings.MultiChoiceSetting;
import sg.rotation.RotationController;
import sg.events.impl.RotationFrameEvent;
import sg.rotation.RotationRegistry;
import sg.rotation.RotationStrategy;
import sg.settings.SingleChoiceSetting;

public class Aura extends ClientModule {
   @SerializedName("сК")
   public final FloatSetting attackDistanceSetting;
   @SerializedName("сI")
   public final FloatSetting rotationDistanceSetting;
   private final @com.google.gson.annotations.SerializedName("ч")  SingleChoiceSetting rotationTypeSetting;
   @SerializedName("сТ")
   private final FloatSetting heightFactorSetting;
   @SerializedName("сО")
   private final FloatSetting generationSpeedSetting;
   private final @com.google.gson.annotations.SerializedName("Э")  SingleChoiceSetting hvhRotationStyleSetting;
   @SerializedName("сФ")
   private final FloatSetting predictionSetting;
   @SerializedName("сЙ")
   private final FloatSetting aimSpeedSetting;
   @SerializedName("ск")
   private final FloatSetting aimFovSetting;
   private final @com.google.gson.annotations.SerializedName("сг")  FloatSetting speedMultiplierSetting;
   private final @com.google.gson.annotations.SerializedName("с0")  FloatSetting verticalShareSetting;
   private final @com.google.gson.annotations.SerializedName("сх")  FloatSetting snapSharpnessSetting;
   private final @com.google.gson.annotations.SerializedName("сУ")  FloatSetting smoothnessSetting;
   private final @com.google.gson.annotations.SerializedName("сЗ")  FloatSetting horizontalJitterSetting;
   private final @com.google.gson.annotations.SerializedName("сЕ")  FloatSetting verticalJitterSetting;
   private final @com.google.gson.annotations.SerializedName("必")  SingleChoiceSetting aimPointSetting;
   private final @com.google.gson.annotations.SerializedName("щ")  SingleChoiceSetting targetSortSetting;
   private final @com.google.gson.annotations.SerializedName("сП")  FloatSetting rotationAccelerationSetting;
   private final @com.google.gson.annotations.SerializedName("с西")  FloatSetting snapTicksSetting;
   public @com.google.gson.annotations.SerializedName("Л")  MultiChoiceSetting combatOptionsSetting;
   private final @com.google.gson.annotations.SerializedName("Г")  SingleChoiceSetting movementCorrectionSetting;
   private final @com.google.gson.annotations.SerializedName("诶")  MultiChoiceSetting targetTypesSetting;
   private final @com.google.gson.annotations.SerializedName("са")  BooleanSetting clientLookSetting;
   private final @com.google.gson.annotations.SerializedName("л")  SingleChoiceSetting targetVisualSetting;
   private final @com.google.gson.annotations.SerializedName("сЮ")  BooleanSetting visualThroughWallsSetting;
   private final @com.google.gson.annotations.SerializedName("сд")  FloatSetting jellyParticleSizeSetting;
   private final @com.google.gson.annotations.SerializedName("сБ")  FloatSetting jellyLayersSetting;
   private final @com.google.gson.annotations.SerializedName("э")  SingleChoiceSetting ringTypeSetting;
   private final @com.google.gson.annotations.SerializedName("ю")  SingleChoiceSetting squareTypeSetting;
   private final @com.google.gson.annotations.SerializedName("Щ")  SingleChoiceSetting ghostTypeSetting;
   private final @com.google.gson.annotations.SerializedName("у")  SingleChoiceSetting crystalTypeSetting;
   private final @com.google.gson.annotations.SerializedName("сЧ")  BooleanSetting seeThroughCrystalsSetting;
   @SerializedName("2")
   private final SingleChoiceSetting lightningTypeSetting;
   private final @com.google.gson.annotations.SerializedName("сЛ")  BooleanSetting lightningThroughWallsSetting;
   private final @com.google.gson.annotations.SerializedName("с诶")  BooleanSetting visualLightingSetting;
   private final @com.google.gson.annotations.SerializedName("сС")  FloatSetting ringSpeedSetting;
   private final @com.google.gson.annotations.SerializedName("сз")  FloatSetting soulCountSetting;
   private final @com.google.gson.annotations.SerializedName("с1")  BooleanSetting redOnHitSetting;
   private final @com.google.gson.annotations.SerializedName("сЬ")  BooleanSetting themeColorSetting;
   private final @com.google.gson.annotations.SerializedName("б")  ColorSetting displayColorSetting;
   private final @com.google.gson.annotations.SerializedName("Ы")  ColorSetting crystalGradientColorSetting;
   private final @com.google.gson.annotations.SerializedName("с衣")  BooleanSetting glowSetting;
   private final @com.google.gson.annotations.SerializedName("сш")  BooleanSetting ghostSeeThroughSetting;
   private final @com.google.gson.annotations.SerializedName("сИ")  BooleanSetting distortionSetting;
   private final @com.google.gson.annotations.SerializedName("сц")  FloatSetting distortionStrengthSetting;
   private final @com.google.gson.annotations.SerializedName("В")  Aura.DirectRotation directRotation;
   @SerializedName("Х")
   private RotationStrategy selectedRotationStrategy;
   private @com.google.gson.annotations.SerializedName("Д")  LivingEntity currentTarget;
   private @com.google.gson.annotations.SerializedName("ВЩ")  boolean targetingPaused;
   private final @com.google.gson.annotations.SerializedName("см")  EventListener<MovementInputEvent> movementInputListener;
   @SerializedName("сК")
   private final EventListener<PlayerTickEvent.Post> postTickListener;
   @SerializedName("сI")
   private final EventListener<PlayerTickEvent> playerTickListener;
   @SerializedName("сТ")
   private final EventListener<WorldEffectsEvent> worldEffectsListener;
   @SerializedName("сО")
   private final EventListener<RotationFrameEvent> rotationFrameListener;
   @SerializedName("сФ")
   private final EventListener<Frame60Event> frame60Listener;
   @SerializedName("сЙ")
   private final EventListener<InputEvent> inputListener;
   private @com.google.gson.annotations.SerializedName("ХЦ")  int remainingSnapAimTicks;
   private @com.google.gson.annotations.SerializedName("ВА")  long lastLegitAimUpdateNanos;
   private @com.google.gson.annotations.SerializedName("В4")  long lastFixedStepUpdateNanos;
   private @com.google.gson.annotations.SerializedName("Хч")  int pendingFixedSteps;
   private @com.google.gson.annotations.SerializedName("Вп")  long lastDirectAimUpdateNanos;
   private @com.google.gson.annotations.SerializedName("еж")  float smoothedYawVelocity;
   private @com.google.gson.annotations.SerializedName("е弟")  float smoothedPitchVelocity;
   private @com.google.gson.annotations.SerializedName("еб")  float yawQuantizationRemainder;
   private @com.google.gson.annotations.SerializedName("еЫ")  float pitchQuantizationRemainder;
   private @com.google.gson.annotations.SerializedName("ВД")  long lastRandomOffsetUpdateNanos;
   private @com.google.gson.annotations.SerializedName("еl")  float yawRandomOffset;
   private @com.google.gson.annotations.SerializedName("е8")  float pitchRandomOffset;
   private @com.google.gson.annotations.SerializedName("е5")  float clientLookYaw;
   private @com.google.gson.annotations.SerializedName("ев")  float clientLookPitch;
   private @com.google.gson.annotations.SerializedName("Ву")  boolean clientLookInitialized;
   private @com.google.gson.annotations.SerializedName("ВЪ")  long lastClientLookUpdateNanos;
   private @com.google.gson.annotations.SerializedName("В2")  boolean attackReady;
   private @com.google.gson.annotations.SerializedName("Вф")  boolean snapHoldRequiresClear;
   @SerializedName("ск")
   private final EventListener<PlayerRotationEvent> playerRotationListener;
   private static final String ЯТы = "Aura";
   private static final String ЯТй = "Автоматически наводится и бьет таргетов";
   private static final String ЯТЖ = "Дистанция атаки";
   private static final float ЯТм = 3.0F;
   private static final float ЯТК = 5.0F;
   private static final float ЯТI = 0.1F;
   private static final String ЯТТ = "Дистанция ротации";
   private static final float ЯТО = 1.5F;
   private static final float ЯТФ = 5.0F;
   private static final float ЯТЙ = 0.1F;
   private static final String ЯТк = "Тип ротации";
   private static final String ЯТг = "Smooth";
   private static final String ЯТ0 = "Snap";
   private static final String ЯТх = "Sloth";
   private static final String ЯТУ = "FuntimeSnap";
   private static final String ЯТЗ = "BMW";
   private static final String ЯТЕ = "Legit";
   private static final String ЯТП = "HvH";
   private static final String ЯТ西 = "Neuro";
   private static final String ЯТд = "Фактор высоты";
   private static final float ЯТБ = 0.5F;
   private static final float ЯТС = 0.05F;
   private static final String ЯТз = "Скорость Генерации";
   private static final float ЯТц = 1.5F;
   private static final float ЯТя = 6.0F;
   private static final float ЯОс = 0.1F;
   private static final String ЯОВ = "Вид ротации";
   private static final String ЯОЯ = "Обычная";
   private static final String ЯОХ = "Дефенсив";
   private static final String ЯОе = "Джиттер";
   private static final String ЯОА = "Предсказание";
   private static final float ЯО4 = 0.15F;
   private static final float ЯОп = 0.01F;
   private static final String ЯОД = "Скорость аима";
   private static final float ЯОЪ = 3.5F;
   private static final float ЯОт = 0.5F;
   private static final float ЯОа = 20.0F;
   private static final float ЯОЮ = 0.1F;
   private static final String ЯОЧ = "FOV аима";
   private static final float ЯОЛ = 40.0F;
   private static final float ЯО诶 = 5.0F;
   private static final float ЯО1 = 120.0F;
   private static final String ЯОЬ = "Множитель скорости";
   private static final float ЯО衣 = 4.3F;
   private static final float ЯОш = 5.0F;
   private static final float ЯОИ = 60.0F;
   private static final float ЯОь = 0.1F;
   private static final String ЯОж = "Доля вертикали";
   private static final float ЯО弟 = 0.1F;
   private static final float ЯОб = 0.1F;
   private static final float ЯОЫ = 1.5F;
   private static final float ЯОl = 0.05F;
   private static final String ЯО8 = "Резкость доводки";
   private static final float ЯО5 = 12.0F;
   private static final float ЯОв = 15.0F;
   private static final float ЯОъ = 0.5F;
   private static final String ЯОМ = "Плавность";
   private static final float ЯОН = 0.45F;
   private static final float ЯОн = 0.05F;
   private static final float ЯОЦ = 0.5F;
   private static final float ЯОч = 0.01F;
   private static final String ЯОЭ = "Дрожание гориз.";
   private static final float ЯО必 = 3.0F;
   private static final float ЯОщ = 0.1F;
   private static final String ЯОГ = "Дрожание верт.";
   private static final float ЯОл = 3.0F;
   private static final float ЯОэ = 3.0F;
   private static final float ЯОю = 0.1F;
   private static final String ЯОЩ = "Точка прицела";
   private static final String ЯОу = "Проекция";
   private static final String ЯО2 = "Центр тела";
   private static final String ЯОф = "Голова";
   private static final String ЯОи = "Ближайшая точка";
   private static final String ЯО9 = "Тип сортировки";
   private static final String ЯОо = "Умная";
   private static final String ЯОO = "Дистанция";
   private static final String ЯО7 = "Здоровье";
   private static final String ЯОР = "Броня";
   private static final String ЯО3 = "Ускорение";
   private static final float ЯОр = 6.0F;
   private static final float ЯО6 = 0.1F;
   private static final String ЯО_ = "Тики";
   private static final float ЯОШ = 8.0F;
   private static final String ЯОы = "Настройки";
   private static final String ЯОй = "Только криты";
   private static final String ЯОЖ = "Только в прыжке";
   private static final String ЯОм = "Проверять кулдаун";
   private static final String ЯОК = "Проверять наводку";
   private static final String ЯОI = "Ломать щит";
   private static final String ЯОТ = "Отжимать щит";
   private static final String ЯОО = "Не бить когда ешь";
   private static final String ЯОФ = "Обход через стены";
   private static final String ЯОЙ = "Тпс синх";
   private static final String ЯОк = "Коррекция движения";
   private static final String ЯОг = "Полноценная";
   private static final String ЯО0 = "Сфокусированная";
   private static final String ЯОх = "Незаметная";
   private static final String ЯОУ = "Таргеты";
   private static final String ЯОЗ = "Игроки";
   private static final String ЯОЕ = "Голые";
   private static final String ЯОП = "Друзья";
   private static final String ЯО西 = "Мобы";
   private static final String ЯОд = "Включить ClientLook";
   private static final String ЯОБ = "Отображение цели";
   private static final String ЯОС = "Квадрат";
   private static final String ЯОз = "Кольцо";
   private static final String ЯОц = "Кристалики";
   private static final String ЯОя = "Куб";
   private static final String ЯФс = "Молнии";
   private static final String ЯФВ = "Обруч";
   private static final String ЯФЯ = "Пентограмма";
   private static final String ЯФХ = "Призраки";
   private static final String ЯФе = "Ромбики";
   private static final String ЯФА = "Треугольник";
   private static final String ЯФСакура = "Сакура";
   private static final String ЯФ4 = "Сквозь стены";
   private static final String ЯФп = "Размер частиц желе";
   private static final float ЯФД = 0.2F;
   private static final float ЯФЪ = 0.08F;
   private static final float ЯФт = 0.45F;
   private static final float ЯФа = 0.01F;
   private static final String ЯФЮ = "Слои желе";
   private static final float ЯФЧ = 14.0F;
   private static final float ЯФЛ = 6.0F;
   private static final float ЯФ诶 = 22.0F;
   private static final String ЯФ1 = "Тип кольца";
   private static final String ЯФЬ = "Тип 1";
   private static final String ЯФ衣 = "Тип 2";
   private static final String ЯФш = "Тип 3";
   private static final String ЯФИ = "Тип квадрата";
   private static final String ЯФь = "Тип 1";
   private static final String ЯФж = "Тип 2";
   private static final String ЯФ弟 = "Тип призраков";
   private static final String ЯФб = "Тип 1";
   private static final String ЯФЫ = "Тип 2";
   private static final String ЯФl = "Тип 3";
   private static final String ЯФ8 = "Тип 4";
   private static final String ЯФ5 = "Тип 5";
   private static final String ЯФв = "Тип 6";
   private static final String ЯФъ = "Тип 7";
   private static final String ЯФМ = "Тип 8";
   private static final String ЯФН = "Тип кристалликов";
   private static final String ЯФн = "Тип 1";
   private static final String ЯФЦ = "Тип 2";
   private static final String ЯФч = "Тип 3";
   private static final String ЯФЭ = "Тип 4";
   private static final String ЯФ必 = "Просвечивать кристаллы";
   private static final String ЯФщ = "Тип молний";
   private static final String ЯФГ = "Тип 1";
   private static final String ЯФл = "Тип 2";
   private static final String ЯФэ = "Тип 3";
   private static final String ЯФю = "Просвечивать";
   private static final String ЯФЩ = "Освещение";
   private static final String ЯФу = "Скорость кольца";
   private static final float ЯФ2 = 1.2F;
   private static final float ЯФф = 0.1F;
   private static final float ЯФи = 5.0F;
   private static final float ЯФ9 = 0.1F;
   private static final String ЯФо = "Кол-во душ";
   private static final float ЯФO = 3.0F;
   private static final float ЯФ7 = 4.0F;
   private static final String ЯФР = "Красный при ударе";
   private static final String ЯФ3 = "Авто цвет под тему";
   private static final String ЯФр = "Цвет отображения";
   private static final int ЯФ6 = -65400;
   private static final String ЯФ_ = "Цвет градиента кристаллов";
   private static final int ЯФШ = -11486721;
   private static final String ЯФы = "Свечение";
   private static final String ЯФй = "Просвечивать";
   private static final String ЯФЖ = "Искажение";
   private static final String ЯФм = "Сила искажения";
   private static final float ЯФК = 12.0F;
   private static final float ЯФI = 3.0F;
   private static final float ЯФТ = 30.0F;
   private static final String ЯФО = "FuntimeSnap";
   private static final double ЯФФ = 0.5;
   private static final double ЯФЙ = 1.0E-6;
   private static final float ЯФк = -90.0F;
   private static final float ЯФг = 90.0F;
   private static final float ЯФ0 = 1.0E-9F;
   private static final float ЯФх = 0.05F;
   private static final float ЯФУ = 7.5F;
   private static final float ЯФЗ = -7.5F;
   private static final float ЯФЕ = -90.0F;
   private static final float ЯФП = 90.0F;
   private static final String ЯФ西 = "Кольцо";
   private static final String ЯФд = "Тип 3";
   private static final String ЯФБ = "Желе";
   private static final String ЯФС = "Кольцо";
   private static final String ЯФз = "Тип 3";
   private static final String ЯФц = "FuntimeSnap";
   private static final float ЯФя = 6.0F;
   private static final String ЯЙс = "FuntimeSnap";
   private static final String ЯЙВ = "Legit";
   private static final String ЯЙЯ = "Spooky";
   private static final String ЯЙХ = "BMW";
   private static final String ЯЙе = "Snap";
   private static final String ЯЙА = "Snap";
   private static final String ЯЙ4 = "Только криты";
   private static final String ЯЙп = "Только в прыжке";
   private static final String ЯЙД = "Проверять кулдаун";
   private static final float ЯЙЪ = 20.0F;
   private static final String ЯЙт = "Model loaded successfully!";
   private static final String ЯЙа = "FuntimeSnap";
   private static final String ЯЙЮ = "BMW";
   private static final String ЯЙЧ = "Snap";
   private static final String ЯЙЛ = "FuntimeSnap";
   private static final String ЯЙ诶 = "BMW";
   private static final String ЯЙ1 = "Snap";
   private static final String ЯЙЬ = "Legit";
   private static final String ЯЙ衣 = "Сфокусированная";
   private static final String ЯЙш = "Незаметная";
   private static final String ЯЙИ = "Полноценная";
   private static final String ЯЙь = "Spooky";
   private static final String ЯЙж = "FuntimeSnap";
   private static final String ЯЙ弟 = "HvH";
   private static final String ЯЙб = "HvH";
   private static final String ЯЙЫ = "Smooth";
   private static final String ЯЙl = "Snap";
   private static final String ЯЙ8 = "FuntimeSnap";
   private static final String ЯЙ5 = "Sloth";
   private static final String ЯЙв = "Funtime";
   private static final String ЯЙъ = "Human";
   private static final String ЯЙМ = "Smooth";
   private static final String ЯЙН = "Snap";
   private static final String ЯЙн = "Neuro";
   private static final String ЯЙЦ = "Не бить когда ешь";
   private static final String ЯЙч = "Только криты";
   private static final String ЯЙЭ = "Только в прыжке";
   private static final String ЯЙ必 = "Проверять кулдаун";
   private static final String ЯЙщ = "Отжимать щит";
   private static final String ЯЙГ = "HvH";
   private static final String ЯЙл = "Smooth";
   private static final String ЯЙэ = "Ломать щит";
   private static final String ЯЙю = "FuntimeSnap";
   private static final double ЯЙЩ = 1.0E-4;
   private static final double ЯЙу = 1.25;
   private static final String ЯЙ2 = "HvH";
   private static final String ЯЙф = "Smooth";
   private static final String ЯЙи = "Только криты";
   private static final String ЯЙ9 = "Только в прыжке";
   private static final String ЯЙо = "Проверять кулдаун";
   private static final String ЯЙO = "Только криты";
   private static final String ЯЙ7 = "Только в прыжке";
   private static final String ЯЙР = "Проверять кулдаун";
   private static final String ЯЙ3 = "FuntimeSnap";
   private static final double ЯЙр = 1.0E-8;
   private static final float ЯЙ6 = 90.0F;
   private static final String ЯЙ_ = "Проверять наводку";
   private static final String ЯЙШ = "Игроки";
   private static final String ЯЙы = "Голые";
   private static final String ЯЙй = "Друзья";
   private static final String ЯЙЖ = "Мобы";
   private static final double ЯЙм = Double.MAX_VALUE;
   private static final double ЯЙК = 1.0E-6;
   private static final double ЯЙI = 80.0;
   private static final double ЯЙТ = 0.01;
   private static final double ЯЙО = 10000.0;
   private static final float ЯЙФ = 6.0F;
   private static final String ЯЙЙ = "Игроки";
   private static final String ЯЙк = "Голые";
   private static final String ЯЙг = "Друзья";
   private static final String ЯЙ0 = "Мобы";
   private static final String ЯЙх = "Игроки";
   private static final String ЯЙУ = "Голые";
   private static final String ЯЙЗ = "Друзья";
   private static final String ЯЙЕ = "Мобы";
   private static final String ЯЙП = "Не бить когда ешь";
   private static final String ЯЙ西 = "Только криты";
   private static final String ЯЙд = "Только в прыжке";
   private static final String ЯЙБ = "Проверять кулдаун";
   private static final float ЯЙС = 0.5F;
   private static final float ЯЙз = 0.5F;
   private static final double ЯЙц = 90.0;
   private static final float ЯЙя = 1.0E9F;
   private static final float Якс = 0.05F;
   private static final long ЯкВ = 4166666L;
   private static final long ЯкЯ = 4166666L;
   private static final String ЯкХ = "Legit";
   private static final String Яке = "Spooky";
   private static final float ЯкА = 1.0E9F;
   private static final float Як4 = 0.05F;
   private static final double Якп = 90.0;
   private static final long ЯкД = 140000000L;
   private static final float ЯкЪ = -1.1F;
   private static final float Якт = 1.1F;
   private static final float Яка = -0.9F;
   private static final float ЯкЮ = 0.9F;
   private static final float ЯкЧ = -90.0F;
   private static final float ЯкЛ = 90.0F;
   private static final float Як诶 = 650.0F;
   private static final float Як1 = 11.0F;
   private static final float ЯкЬ = -9.5F;
   private static final float Як衣 = 1.0E-5F;
   private static final float Якш = -90.0F;
   private static final float ЯкИ = 90.0F;
   private static final double Якь = 0.6;
   private static final double Якж = 0.6;
   private static final String Як弟 = "Центр тела";
   private static final String Якб = "Голова";
   private static final double ЯкЫ = 0.5;
   private static final double Якl = 0.15;
   private static final double Як8 = 0.5;
   private static final String Як5 = "Ближайшая точка";
   private static final double Якв = 1.0E-4;
   private static final double Якъ = 0.5;
   private static final double ЯкМ = 90.0;
   private static final double ЯкН = Double.MAX_VALUE;
   private static final float Якн = -1.0F;
   private static final float ЯкЦ = -1.0F;
   private static final float Якч = 0.02F;
   private static final String ЯкЭ = "Обход через стены";
   private static final String Як必 = "BMW";
   private static final String Якщ = "HvH";
   private static final String ЯкГ = "Smooth";
   private static final float Якл = 20.0F;
   private static final float Якэ = 5.0F;
   private static final String Якю = "Призраки";
   private static final String ЯкЩ = "Тип 2";
   private static final String Яку = "Тип 4";
   private static final String Як2 = "Тип 5";
   private static final String Якф = "Призраки";
   private static final String Яки = "Тип 2";
   private static final String Як9 = "Тип 4";
   private static final String Яко = "Тип 5";
   private static final String ЯкO = "Призраки";
   private static final String Як7 = "Тип 2";
   private static final String ЯкР = "Тип 4";
   private static final String Як3 = "Тип 5";
   private static final String Якр = "Призраки";
   private static final String Як6 = "Тип 2";
   private static final String Як_ = "Ромбики";
   private static final String ЯкШ = "Кольцо";
   private static final String Якы = "Тип 2";
   private static final String Якй = "Призраки";
   private static final String ЯкЖ = "Тип 2";
   private static final String Якм = "Тип 3";
   private static final String ЯкК = "Тип 4";
   private static final String ЯкI = "Тип 5";
   private static final String ЯкТ = "Призраки";
   private static final String ЯкО = "Тип 6";
   private static final String ЯкФ = "Кольцо";
   private static final String ЯкЙ = "Призраки";
   private static final String Якк = "Кристалики";
   private static final String Якг = "Молнии";
   private static final String Як0 = "Молнии";
   private static final String Якх = "Молнии";
   private static final String ЯкУ = "Кристалики";
   private static final String ЯкЗ = "Кристалики";
   private static final String ЯкЕ = "Призраки";
   private static final String ЯкП = "Квадрат";
   private static final String Як西 = "Кольцо";
   private static final String Якд = "Только криты";
   private static final String ЯкБ = "Snap";
   private static final String ЯкС = "Sloth";
   private static final String Якз = "Legit";
   private static final String Якц = "Legit";
   private static final String Якя = "Legit";
   private static final String Ягс = "Legit";
   private static final String ЯгВ = "Legit";
   private static final String ЯгЯ = "Legit";
   private static final String ЯгХ = "Legit";
   private static final String Яге = "Legit";
   private static final String ЯгА = "Legit";
   private static final String Яг4 = "HvH";
   private static final String Ягп = "Обычная";
   private static final String ЯгД = "HvH";
   private static final String ЯгЪ = "Neuro";
   private static final String Ягт = "Neuro";

   public LivingEntity Б(float var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !(var1 <= 0.0F)) {
         List<LivingEntity> var2 = CombatTargetSelector.method_4(var1, this.targetTypesSetting.isSelected(ЯЙШ), this.targetTypesSetting.isSelected(ЯЙы), this.targetTypesSetting.isSelected(ЯЙй), this.targetTypesSetting.isSelected(ЯЙЖ));
         Vec3 var3 = MINECRAFT_CLIENT.player.position();
         Vec3 var4 = MINECRAFT_CLIENT.player.getDeltaMovement();
         LivingEntity var5 = null;
         double var6 = ЯЙм;

         for (LivingEntity var9 : var2) {
            Vec3 var10 = var9.position().subtract(var3);
            Vec3 var11 = var9.getDeltaMovement().subtract(var4);
            double var12 = var11.lengthSqr();
            double var14 = var12 < ЯЙК ? 0.0 : -var10.dot(var11) / var12;
            var14 = Mth.clamp(var14, 0.0, ЯЙI);
            Vec3 var16 = var10.add(var11.scale(var14));
            double var17 = var10.dot(var4);
            double var19 = var16.lengthSqr() + var14 * ЯЙТ + (var17 <= 0.0 ? ЯЙО : 0.0);
            if (var19 < var6) {
               var6 = var19;
               var5 = var9;
            }
         }

         return var5;
      } else {
         return null;
      }
   }

   private void Ой() {
      long var1 = System.nanoTime();
      if (this.lastFixedStepUpdateNanos == 0L) {
         this.lastFixedStepUpdateNanos = var1;
      }

      long var3 = var1 - this.lastFixedStepUpdateNanos;
      this.pendingFixedSteps = this.pendingFixedSteps + (int)(var3 / ЯкВ);
      this.lastFixedStepUpdateNanos = this.lastFixedStepUpdateNanos + this.pendingFixedSteps * ЯкЯ;

      for (this.pendingFixedSteps = Math.min(this.pendingFixedSteps, 240); this.pendingFixedSteps > 0; this.pendingFixedSteps--) {
         if (this.rotationTypeSetting.isSelected(ЯкХ) && !this.targetingPaused) {
            this.ОВ();
         }

      }
   }

   public double[] Б(double var1, double var3) {
      if (MINECRAFT_CLIENT.player != null && this.currentTarget != null) {
         double var5 = this.currentTarget.getX() - MINECRAFT_CLIENT.player.getX();
         double var7 = this.currentTarget.getZ() - MINECRAFT_CLIENT.player.getZ();
         double var9 = Math.hypot(var5, var7);
         if (!(var9 < ЯЙЩ) && !(var9 > this.attackDistanceSetting.getValue().floatValue() + ЯЙу)) {
            double var11 = var5 / var9;
            double var13 = var7 / var9;
            double var15 = var1 * var11 + var3 * var13;
            if (var15 < 0.0) {
               var1 -= var11 * var15;
               var3 -= var13 * var15;
            }

            return new double[]{var1, var3};
         } else {
            return new double[]{var1, var3};
         }
      } else {
         return new double[]{var1, var3};
      }
   }

   @Generated
   public BooleanSetting с_3276742a() {
      return this.redOnHitSetting;
   }

   public boolean method_5I() {
      return this.isEnabled() && (this.rotationTypeSetting.isSelected(ЯЙ2) || this.rotationTypeSetting.isSelected(ЯЙф)) && this.currentTarget != null;
   }

   private void ЩБ() {
      RotationRegistry var1 = RoseClient.getInstance().getServices().getRotations();
      if (this.selectedRotationStrategy == var1.getHumanizedRotation() || var1.getActiveStrategy() == var1.getHumanizedRotation()) {
         RotationController var2 = RoseClient.getInstance().getControllers().getRotations();
         var2.getCameraRotation().set(var2.getAppliedRotation());
         if (MINECRAFT_CLIENT.player != null) {
            MINECRAFT_CLIENT.player.setYRot(var2.getAppliedRotation().x);
            MINECRAFT_CLIENT.player.setXRot(var2.getAppliedRotation().y);
         }
      }
   }

   @Generated
   public SingleChoiceSetting э_f1816549() {
      return this.movementCorrectionSetting;
   }

   @Generated
   public void 必(long var1) {
      this.lastDirectAimUpdateNanos = var1;
   }

   private void ОI() {
      if (this.currentTarget != null && MINECRAFT_CLIENT.gui.screen() == null) {
         long var1 = System.nanoTime();
         float var3 = this.lastDirectAimUpdateNanos == 0L ? 0.0F : (float)(var1 - this.lastDirectAimUpdateNanos) / ЯкА;
         this.lastDirectAimUpdateNanos = var1;
         var3 = Mth.clamp(var3, 0.0F, Як4);
         if (!(var3 <= 0.0F)) {
            Vec3 var4 = this.directionToTargetBounds(this.currentTarget);
            float var5 = (float)Mth.wrapDegrees(Math.toDegrees(Math.atan2(var4.z, var4.x)) - Якп);
            float var6 = (float)(-Math.toDegrees(Math.atan2(var4.y, Math.hypot(var4.x, var4.z))));
            long var7 = ЯкД;
            if (var1 - this.lastRandomOffsetUpdateNanos >= var7) {
               this.yawRandomOffset = AimGeometryMath.randomizedInterpolation(ЯкЪ, Якт);
               this.pitchRandomOffset = AimGeometryMath.randomizedInterpolation(Яка, ЯкЮ);
               this.lastRandomOffsetUpdateNanos = var1;
            }

            float var9 = MINECRAFT_CLIENT.player.getYRot();
            float var10 = MINECRAFT_CLIENT.player.getXRot();
            float var11;
            float var12;
            if (AimRaycast.method_2(var9, var10, this.attackDistanceSetting.getValue().floatValue(), this.currentTarget, false)) {
               var11 = 0.0F;
               var12 = 0.0F;
            } else {
               var11 = Mth.wrapDegrees(var5 + this.yawRandomOffset - var9);
               var12 = Mth.wrapDegrees(var6 + this.pitchRandomOffset - var10);
               var12 = Mth.clamp(var10 + var12, ЯкЧ, ЯкЛ) - var10;
            }

            float var13 = Як诶;
            float var14 = Як1;
            float var15 = Mth.clamp(Math.abs(var11) * var14, 0.0F, var13) * Math.signum(var11);
            float var16 = Mth.clamp(Math.abs(var12) * var14, 0.0F, var13) * Math.signum(var12);
            float var17 = 1.0F - (float)Math.exp(ЯкЬ * var3);
            this.smoothedYawVelocity = this.smoothedYawVelocity + (var15 - this.smoothedYawVelocity) * var17;
            this.smoothedPitchVelocity = this.smoothedPitchVelocity + (var16 - this.smoothedPitchVelocity) * var17;
            float var18 = this.smoothedYawVelocity * var3;
            float var19 = this.smoothedPitchVelocity * var3;
            if (Math.abs(var18) > Math.abs(var11)) {
               var18 = var11;
            }

            if (Math.abs(var19) > Math.abs(var12)) {
               var19 = var12;
            }

            float var20 = MouseSensitivityMath.getRotationStep();
            if (var20 > Як衣) {
               float var21 = var18 + this.yawQuantizationRemainder;
               float var22 = Math.round(var21 / var20) * var20;
               this.yawQuantizationRemainder = var21 - var22;
               var18 = var22;
               float var23 = var19 + this.pitchQuantizationRemainder;
               float var24 = Math.round(var23 / var20) * var20;
               this.pitchQuantizationRemainder = var23 - var24;
               var19 = var24;
            }

            float var27 = var9 + var18;
            float var28 = Mth.clamp(var10 + var19, Якш, ЯкИ);
            this.directRotation.setTargetPosition(this.currentTarget.position().add(0.0, this.currentTarget.getBbHeight() * Якь, 0.0));
            this.directRotation.Н(var27, var28);
            if (RoseClient.getInstance().getServices().getRotations().scheduleStrategy(this.directRotation, 1, 6)) {
               RoseClient.getInstance().getControllers().getRotations().getAppliedRotation().set(var27, var28);
               MINECRAFT_CLIENT.player.setYRot(var27);
               MINECRAFT_CLIENT.player.setXRot(var28);
            }
         }
      } else {
         this.lastDirectAimUpdateNanos = System.nanoTime();
         this.smoothedYawVelocity = 0.0F;
         this.smoothedPitchVelocity = 0.0F;
         this.yawQuantizationRemainder = 0.0F;
         this.pitchQuantizationRemainder = 0.0F;
      }
   }

   @Generated
   public FloatSetting ф() {
      return this.verticalJitterSetting;
   }

   @Generated
   public FloatSetting Р_989fed9a() {
      return this.rotationDistanceSetting;
   }

   @Generated
   public float БТ() {
      return this.smoothedPitchVelocity;
   }

   @Override
   public void onDisable() {
      this.targetingPaused = false;
      this.ЩБ();
      this.ЩС();
      this.currentTarget = null;
      this.selectedRotationStrategy = null;
      this.remainingSnapAimTicks = 0;
      this.lastLegitAimUpdateNanos = 0L;
      this.lastFixedStepUpdateNanos = 0L;
      this.pendingFixedSteps = 0;
      this.lastDirectAimUpdateNanos = 0L;
      this.smoothedYawVelocity = 0.0F;
      this.smoothedPitchVelocity = 0.0F;
      this.yawQuantizationRemainder = 0.0F;
      this.pitchQuantizationRemainder = 0.0F;
      this.lastRandomOffsetUpdateNanos = 0L;
      this.yawRandomOffset = 0.0F;
      this.pitchRandomOffset = 0.0F;
      this.clientLookInitialized = false;
      this.lastClientLookUpdateNanos = 0L;
       RoseClient.getInstance().getServices().getRotations().getDefensiveJitterRotation().resetState();
       RoseClient.getInstance().getServices().getRotations().getSpookyTimeSmoothMode().resetState();
      if (this.rotationTypeSetting.isSelected(Як必)) {
         RoseClient.getInstance().getServices().getRotations().activateStrategy(null);
      }
   }

   @Generated
   public FloatSetting Ы() {
      return this.smoothnessSetting;
   }

   @Generated
   public EventListener<PlayerTickEvent.Post> П_f8db0d09() {
      return this.postTickListener;
   }

   private boolean updateSnapAimPhase(float defaultAttackRange) {
      RotationRegistry rotationRegistry = RoseClient.getInstance().getServices().getRotations();
      SnapAimRotation snapStrategy = rotationRegistry.getSnapAimRotation();
      if (!this.ОБ()) {
         if (snapStrategy.shouldContinueReturn()) {
            return true;
         }

         rotationRegistry.removeStrategy(snapStrategy);
         RoseClient.getInstance().getControllers().getRotations().releaseStrategy(snapStrategy);
         this.snapHoldRequiresClear = true;
         return false;
      } else {
         if (this.snapHoldRequiresClear) {
            snapStrategy.clearHoldState();
            this.snapHoldRequiresClear = false;
         }

         if (this.currentTarget != null
            && !RoseClient.getInstance().getServices().getModules().getWindHop().ЫЙ()
            && (!MINECRAFT_CLIENT.player.isUsingItem() || MINECRAFT_CLIENT.player.isBlocking() || !this.combatOptionsSetting.isSelected(ЯЙП))) {
            ElytraTarget elytraTarget = RoseClient.getInstance().getServices().getModules().getElytraTarget();
            float attackRange = MINECRAFT_CLIENT.player.isFallFlying()
                  && elytraTarget.isEnabled()
                  && this.currentTarget.isFallFlying()
                  && AimGeometryMath.getEntityMovementPerSecond(this.currentTarget) > elytraTarget.РР().getValue().floatValue()
                  && elytraTarget.е_3276742a().getValue()
               ? elytraTarget.с_989fed9a().getValue()
               : defaultAttackRange;
            int targetPhase = AttackReadiness.Х(this.currentTarget, this.combatOptionsSetting.isSelected(ЯЙ西), this.combatOptionsSetting.isSelected(ЯЙд), true, attackRange, this.combatOptionsSetting.isSelected(ЯЙБ), 5);
            snapStrategy.updateTargetPhase(this.currentTarget, targetPhase);
            return true;
         } else {
            snapStrategy.updateTargetPhase(this.currentTarget, -1);
            return true;
         }
      }
   }

   @Generated
   public void в(boolean var1) {
      this.snapHoldRequiresClear = var1;
   }

   private boolean ОЩ() {
      ModuleRegistry var1 = RoseClient.getInstance().getServices().getModules();
      return this.currentTarget != null && this.combatOptionsSetting.isSelected(ЯкЭ) && !MINECRAFT_CLIENT.player.hasLineOfSight(this.currentTarget) && !var1.getAirStuck().isEnabled() && !var1.getFreeCam().isEnabled();
   }

   @Generated
   public void ж(boolean var1) {
      this.clientLookInitialized = var1;
   }

   @Generated
   public FloatSetting г_989fed9a() {
      return this.heightFactorSetting;
   }

   @Generated
   public FloatSetting Ц_989fed9a() {
      return this.predictionSetting;
   }

   @Generated
   public SingleChoiceSetting е_f1816549() {
      return this.targetSortSetting;
   }

   @Generated
   public boolean ОГ() {
      return this.targetingPaused;
   }

   @Generated
   public void л(float var1) {
      this.clientLookYaw = var1;
   }

   @Generated
   public BooleanSetting К() {
      return this.distortionSetting;
   }

   @Generated
   public FloatSetting getAimSpeedSetting() {
      return this.aimSpeedSetting;
   }

   @Generated
   public float Бд() {
      return this.yawRandomOffset;
   }

   @Generated
   public BooleanSetting У_3276742a() {
      return this.seeThroughCrystalsSetting;
   }

   @Generated
   public void Т(LivingEntity var1) {
      this.currentTarget = var1;
   }

   @Generated
   public EventListener<WorldEffectsEvent> д_f8db0d09() {
      return this.worldEffectsListener;
   }

   @Generated
   public ColorSetting Г_f4ff5ac1() {
      return this.crystalGradientColorSetting;
   }

   public void 衣(boolean var1) {
      this.targetingPaused = var1;
      if (var1) {
         this.ЩС();
      }
   }

   @Generated
   public void 诶(long var1) {
      this.lastClientLookUpdateNanos = var1;
   }

   private void Г(MovementInputEvent var1) {
      float var2 = var1.getForwardAmount();
      float var3 = var1.getLeftAmount();
      if (var2 != 0.0F || var3 != 0.0F) {
         double var4 = this.currentTarget.getX() - MINECRAFT_CLIENT.player.getX();
         double var6 = this.currentTarget.getZ() - MINECRAFT_CLIENT.player.getZ();
         float var8 = (float)Mth.wrapDegrees(Math.toDegrees(Math.atan2(var6, var4)) - ЯкМ);
         double var9 = Mth.wrapDegrees(Math.toDegrees(MovementUtils.calculateMovementDirectionRadians(var8, var2, var3)));
         float var11 = var2;
         float var12 = var3;
         double var13 = ЯкН;

         for (float var15 = Якн; var15 <= 1.0F; var15++) {
            for (float var16 = ЯкЦ; var16 <= 1.0F; var16++) {
               if (var15 != 0.0F || var16 != 0.0F) {
                  double var17 = Mth.wrapDegrees(Math.toDegrees(MovementUtils.calculateMovementDirectionRadians(MINECRAFT_CLIENT.player.getYRot(), var15, var16)));
                  double var19 = Math.abs(Mth.wrapDegrees(var9 - var17));
                  if (var19 < var13) {
                     var13 = var19;
                     var11 = var15;
                     var12 = var16;
                  }
               }
            }
         }

         var1.setForwardAmount(var11);
         var1.setLeftAmount(var12);
      }
   }

   @Generated
   public BooleanSetting method__() {
      return this.ghostSeeThroughSetting;
   }

   @Generated
   public void Й(float var1) {
      this.smoothedPitchVelocity = var1;
   }

   @Generated
   public FloatSetting Ъ_989fed9a() {
      return this.speedMultiplierSetting;
   }

   @Generated
   public void method_0(int var1) {
      this.pendingFixedSteps = var1;
   }

   private void Ол() {
      if (this.selectedRotationStrategy != null && this.currentTarget != null && !this.targetingPaused) {
         ElytraTarget var1 = RoseClient.getInstance().getServices().getModules().getElytraTarget();
         this.selectedRotationStrategy.setTargetPosition(var1.К(this.currentTarget, this.currentTarget.position().add(0.0, this.currentTarget.getBbHeight() / 2.0F + (MINECRAFT_CLIENT.player.isFallFlying() ? ЯЙз : 0.0F), 0.0)));
         RoseClient.getInstance().getServices().getRotations().activateStrategy(this.selectedRotationStrategy);
      }
   }

   @Generated
   public FloatSetting Е() {
      return this.jellyParticleSizeSetting;
   }

   @Generated
   public float Б西() {
      return this.clientLookYaw;
   }

   @Generated
   public EventListener<PlayerTickEvent> 必_f8db0d09() {
      return this.playerTickListener;
   }

   private void О2() {
      ElytraTarget var1 = RoseClient.getInstance().getServices().getModules().getElytraTarget();
      float var2 = this.attackDistanceSetting.getValue();
      if (var1.isEnabled()) {
         var2 = var1.с_989fed9a().getValue();
      }

      if (RoseClient.getInstance().getServices().getModules().getAirStuck().isEnabled() || RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled()) {
         var2 = ЯЙФ;
      }

      if (MINECRAFT_CLIENT.player.isFallFlying() && var1.isEnabled()) {
         this.currentTarget = CombatTargetSelector.method_4(this.currentTarget, var2 + var1.Т_989fed9a().getValue(), this.targetTypesSetting.isSelected(ЯЙЙ), this.targetTypesSetting.isSelected(ЯЙк), this.targetTypesSetting.isSelected(ЯЙг), this.targetTypesSetting.isSelected(ЯЙ0), this.targetSortSetting);
      } else {
         this.currentTarget = CombatTargetSelector.method_4(this.currentTarget, var2 + this.rotationDistanceSetting.getValue(), this.targetTypesSetting.isSelected(ЯЙх), this.targetTypesSetting.isSelected(ЯЙУ), this.targetTypesSetting.isSelected(ЯЙЗ), this.targetTypesSetting.isSelected(ЯЙЕ), this.targetSortSetting);
      }
   }

   public void я(float var1) {
      if (!this.method_5В()) {
         this.clientLookInitialized = false;
         this.lastClientLookUpdateNanos = 0L;
      } else {
         Vec3 var2 = MINECRAFT_CLIENT.player.getEyePosition(var1);
         Vec3 var3 = this.currentTarget.getPosition(var1).add(0.0, this.currentTarget.getBbHeight() * ЯФФ, 0.0);
         Vec3 var4 = var3.subtract(var2);
         double var5 = Math.sqrt(var4.x * var4.x + var4.z * var4.z);
         float var7 = (float)Math.toDegrees(Math.atan2(-var4.x, var4.z));
         float var8 = (float)Math.toDegrees(-Math.atan2(var4.y, Math.max(var5, ЯФЙ)));
         var8 = Mth.clamp(var8, ЯФк, ЯФг);
         long var9 = System.nanoTime();
         RotationController var11 = RoseClient.getInstance().getControllers().getRotations();
         if (!this.clientLookInitialized) {
            Vector2f var12 = var11.getCameraRotation();
            if (var11.getEffectiveStrategy() != null) {
               this.clientLookYaw = var12.x;
               this.clientLookPitch = var12.y;
            } else {
               this.clientLookYaw = MINECRAFT_CLIENT.player.getYRot();
               this.clientLookPitch = MINECRAFT_CLIENT.player.getXRot();
            }

            this.clientLookInitialized = true;
            this.lastClientLookUpdateNanos = var9;
         }

         float var16 = (float)(var9 - this.lastClientLookUpdateNanos) * ЯФ0;
         this.lastClientLookUpdateNanos = var9;
         var16 = Mth.clamp(var16, 0.0F, ЯФх);
         float var13 = ЯФУ;
         float var14 = 1.0F - (float)Math.exp(ЯФЗ * var16);
         this.clientLookYaw = this.clientLookYaw + Mth.wrapDegrees(var7 - this.clientLookYaw) * var14;
         this.clientLookPitch = this.clientLookPitch + (var8 - this.clientLookPitch) * var14;
         this.clientLookPitch = Mth.clamp(this.clientLookPitch, ЯФЕ, ЯФП);
         var11.getCameraRotation().set(this.clientLookYaw, this.clientLookPitch);
      }
   }

   @Generated
   public void Д(float var1) {
      this.yawQuantizationRemainder = var1;
   }

   @Generated
   public SingleChoiceSetting п() {
      return this.targetVisualSetting;
   }

   public Aura() {
      super(ЯТы, ЯТй, ModuleCategory.COMBAT);
      this.attackDistanceSetting = new FloatSetting(ЯТЖ, ЯТм, 1.0F, ЯТК).withStep(ЯТI);
      this.rotationDistanceSetting = new FloatSetting(ЯТТ, ЯТО, 0.0F, ЯТФ).withStep(ЯТЙ);
      this.rotationTypeSetting = new SingleChoiceSetting(
         ЯТк,
         new ChoiceOption(ЯТг),
         new ChoiceOption(ЯТ0),
         new ChoiceOption(ЯТх),
         new ChoiceOption(ЯТУ),
         new ChoiceOption(ЯТЗ),
         new ChoiceOption(ЯТЕ),
         new ChoiceOption(ЯТП),
          new ChoiceOption(ЯТ西),
          new ChoiceOption("Funtime"),
          new ChoiceOption("Spooky")
      );
      this.heightFactorSetting = new FloatSetting(ЯТд, ЯТБ, 0.0F, 1.0F).withStep(ЯТС).visibleWhen(() -> this.rotationTypeSetting.isSelected(Ягт));
      this.generationSpeedSetting = new FloatSetting(ЯТз, ЯТц, 1.0F, ЯТя).withStep(ЯОс).visibleWhen(() -> this.rotationTypeSetting.isSelected(ЯгЪ) && FabricLoader.getInstance().isDevelopmentEnvironment());
      this.hvhRotationStyleSetting = new SingleChoiceSetting(ЯОВ, new ChoiceOption(ЯОЯ), new ChoiceOption(ЯОХ), new ChoiceOption(ЯОе)).visibleWhen(() -> this.rotationTypeSetting.isSelected(ЯгД));
      this.predictionSetting = new FloatSetting(ЯОА, ЯО4, 0.0F, 1.0F).withStep(ЯОп).visibleWhen(() -> this.rotationTypeSetting.isSelected(Яг4) && this.hvhRotationStyleSetting.isSelected(Ягп));
      this.aimSpeedSetting = new FloatSetting(ЯОД, ЯОЪ, ЯОт, ЯОа).withStep(ЯОЮ).visibleWhen(() -> this.rotationTypeSetting.isSelected(ЯгА));
      this.aimFovSetting = new FloatSetting(ЯОЧ, ЯОЛ, ЯО诶, ЯО1).withStep(1.0F).visibleWhen(() -> this.rotationTypeSetting.isSelected(Яге));
      this.speedMultiplierSetting = new FloatSetting(ЯОЬ, ЯО衣, ЯОш, ЯОИ).withStep(ЯОь).visibleWhen(() -> this.rotationTypeSetting.isSelected(ЯгХ));
      this.verticalShareSetting = new FloatSetting(ЯОж, ЯО弟, ЯОб, ЯОЫ).withStep(ЯОl).visibleWhen(() -> this.rotationTypeSetting.isSelected(ЯгЯ));
      this.snapSharpnessSetting = new FloatSetting(ЯО8, ЯО5, 1.0F, ЯОв).withStep(ЯОъ).visibleWhen(() -> this.rotationTypeSetting.isSelected(ЯгВ));
      this.smoothnessSetting = new FloatSetting(ЯОМ, ЯОН, ЯОн, ЯОЦ).withStep(ЯОч).visibleWhen(() -> this.rotationTypeSetting.isSelected(Ягс));
      this.horizontalJitterSetting = new FloatSetting(ЯОЭ, 1.0F, 0.0F, ЯО必).withStep(ЯОщ).visibleWhen(() -> this.rotationTypeSetting.isSelected(Якя));
      this.verticalJitterSetting = new FloatSetting(ЯОГ, ЯОл, 0.0F, ЯОэ).withStep(ЯОю).visibleWhen(() -> this.rotationTypeSetting.isSelected(Якц));
      this.aimPointSetting = new SingleChoiceSetting(ЯОЩ, 2, new ChoiceOption(ЯОу), new ChoiceOption(ЯО2), new ChoiceOption(ЯОф), new ChoiceOption(ЯОи))
         .visibleWhen(() -> this.rotationTypeSetting.isSelected(Якз));
      this.targetSortSetting = new SingleChoiceSetting(ЯО9, new ChoiceOption(ЯОо), new ChoiceOption(ЯОO), new ChoiceOption(ЯО7), new ChoiceOption(ЯОР));
      this.rotationAccelerationSetting = new FloatSetting(ЯО3, 1.0F, 1.0F, ЯОр).withStep(ЯО6).visibleWhen(() -> this.rotationTypeSetting.isSelected(ЯкС));
      this.snapTicksSetting = new FloatSetting(ЯО_, 2.0F, 1.0F, ЯОШ).withStep(1.0F).visibleWhen(() -> this.rotationTypeSetting.isSelected(ЯкБ));
      this.combatOptionsSetting = new MultiChoiceSetting(
         ЯОы,
         new ChoiceOption(ЯОй, true),
         new ChoiceOption(ЯОЖ, true).visibleWhen(() -> this.combatOptionsSetting.isSelected(Якд)),
         new ChoiceOption(ЯОм, true),
         new ChoiceOption(ЯОК, false),
         new ChoiceOption(ЯОI, true),
         new ChoiceOption(ЯОТ, true),
         new ChoiceOption(ЯОО, false),
         new ChoiceOption(ЯОФ, false),
         new ChoiceOption(ЯОЙ, false)
      );
      this.movementCorrectionSetting = new SingleChoiceSetting(ЯОк, new ChoiceOption(ЯОг), new ChoiceOption(ЯО0), new ChoiceOption(ЯОх));
      this.targetTypesSetting = new MultiChoiceSetting(
         ЯОУ, new ChoiceOption(ЯОЗ, true), new ChoiceOption(ЯОЕ, true), new ChoiceOption(ЯОП, false), new ChoiceOption(ЯО西, true)
      );
      this.clientLookSetting = new BooleanSetting(ЯОд, false);
      this.targetVisualSetting = new SingleChoiceSetting(
         ЯОБ,
         new ChoiceOption(ЯОС),
         new ChoiceOption(ЯОз),
         new ChoiceOption(ЯОц),
         new ChoiceOption(ЯОя),
         new ChoiceOption(ЯФс),
         new ChoiceOption(ЯФВ),
         new ChoiceOption(ЯФЯ),
         new ChoiceOption(ЯФХ),
         new ChoiceOption(ЯФе),
         new ChoiceOption(ЯФА),
          new ChoiceOption(ЯФСакура)
      );
      this.visualThroughWallsSetting = new BooleanSetting(ЯФ4, true).visibleWhen(this::isThroughWallsVisible);
      this.jellyParticleSizeSetting = new FloatSetting(ЯФп, ЯФД, ЯФЪ, ЯФт).withStep(ЯФа).visibleWhen(this::method_5й);
      this.jellyLayersSetting = new FloatSetting(ЯФЮ, ЯФЧ, ЯФЛ, ЯФ诶).withStep(1.0F).visibleWhen(this::method_5й);
      this.ringTypeSetting = new SingleChoiceSetting(ЯФ1, new ChoiceOption(ЯФЬ), new ChoiceOption(ЯФ衣), new ChoiceOption(ЯФш)).visibleWhen(() -> this.targetVisualSetting.isSelected(Як西));
      this.squareTypeSetting = new SingleChoiceSetting(ЯФИ, new ChoiceOption(ЯФь), new ChoiceOption(ЯФж)).visibleWhen(() -> this.targetVisualSetting.isSelected(ЯкП));
      this.ghostTypeSetting = new SingleChoiceSetting(
            ЯФ弟,
            new ChoiceOption(ЯФб),
            new ChoiceOption(ЯФЫ),
            new ChoiceOption(ЯФl),
            new ChoiceOption(ЯФ8),
            new ChoiceOption(ЯФ5),
            new ChoiceOption(ЯФв),
            new ChoiceOption(ЯФъ),
            new ChoiceOption(ЯФМ)
         )
         .visibleWhen(() -> this.targetVisualSetting.isSelected(ЯкЕ));
      this.crystalTypeSetting = new SingleChoiceSetting(ЯФН, new ChoiceOption(ЯФн), new ChoiceOption(ЯФЦ), new ChoiceOption(ЯФч), new ChoiceOption(ЯФЭ)).visibleWhen(() -> this.targetVisualSetting.isSelected(ЯкЗ));
      this.seeThroughCrystalsSetting = new BooleanSetting(ЯФ必, true).visibleWhen(() -> this.targetVisualSetting.isSelected(ЯкУ));
      this.lightningTypeSetting = new SingleChoiceSetting(ЯФщ, new ChoiceOption(ЯФГ), new ChoiceOption(ЯФл), new ChoiceOption(ЯФэ)).visibleWhen(() -> this.targetVisualSetting.isSelected(Якх));
      this.lightningThroughWallsSetting = new BooleanSetting(ЯФю, false).visibleWhen(() -> this.targetVisualSetting.isSelected(Як0));
      this.visualLightingSetting = new BooleanSetting(ЯФЩ, true).visibleWhen(() -> this.targetVisualSetting.isSelected(ЯкЙ) || this.targetVisualSetting.isSelected(Якк) || this.targetVisualSetting.isSelected(Якг));
      this.ringSpeedSetting = new FloatSetting(ЯФу, ЯФ2, ЯФф, ЯФи).withStep(ЯФ9).visibleWhen(() -> this.targetVisualSetting.isSelected(ЯкФ) && !this.method_5й());
      this.soulCountSetting = new FloatSetting(ЯФо, ЯФO, 2.0F, ЯФ7).withStep(1.0F).visibleWhen(() -> this.targetVisualSetting.isSelected(ЯкТ) && this.ghostTypeSetting.isSelected(ЯкО));
      this.redOnHitSetting = new BooleanSetting(ЯФР, true);
      this.themeColorSetting = new BooleanSetting(ЯФ3, true);
      this.displayColorSetting = new ColorSetting(ЯФр, false, ЯФ6).visibleWhen(() -> !this.themeColorSetting.getValue());
      this.crystalGradientColorSetting = new ColorSetting(ЯФ_, false, ЯФШ)
         .visibleWhen(
            () -> (this.targetVisualSetting.isSelected(Як_) || this.targetVisualSetting.isSelected(ЯкШ) && this.ringTypeSetting.isSelected(Якы) || this.targetVisualSetting.isSelected(Якй) && (this.ghostTypeSetting.isSelected(ЯкЖ) || this.ghostTypeSetting.isSelected(Якм) || this.ghostTypeSetting.isSelected(ЯкК) || this.ghostTypeSetting.isSelected(ЯкI)))
               && !this.themeColorSetting.getValue()
         );
      this.glowSetting = new BooleanSetting(ЯФы, false).visibleWhen(() -> this.targetVisualSetting.isSelected(Якр) && this.ghostTypeSetting.isSelected(Як6));
      this.ghostSeeThroughSetting = new BooleanSetting(ЯФй, true).visibleWhen(() -> this.targetVisualSetting.isSelected(ЯкO) && (this.ghostTypeSetting.isSelected(Як7) || this.ghostTypeSetting.isSelected(ЯкР) || this.ghostTypeSetting.isSelected(Як3)));
      this.distortionSetting = new BooleanSetting(ЯФЖ, true).visibleWhen(() -> !this.method_5й() && (!this.targetVisualSetting.isSelected(Якф) || !this.ghostTypeSetting.isSelected(Яки) && !this.ghostTypeSetting.isSelected(Як9) && !this.ghostTypeSetting.isSelected(Яко)));
      this.distortionStrengthSetting = new FloatSetting(ЯФм, ЯФК, ЯФI, ЯФТ)
         .withStep(1.0F)
         .visibleWhen(() -> !this.method_5й() && (!this.targetVisualSetting.isSelected(Якю) || !this.ghostTypeSetting.isSelected(ЯкЩ) && !this.ghostTypeSetting.isSelected(Яку) && !this.ghostTypeSetting.isSelected(Як2)) && this.distortionSetting.getValue());
      this.directRotation = new Aura.DirectRotation();
      this.movementInputListener = this::Ж;
      this.postTickListener = this::О;
      this.playerTickListener = this::З;
      this.worldEffectsListener = this::щ;
      this.rotationFrameListener = this::Б;
      this.frame60Listener = this::Щ;
      this.inputListener = this::е;
      this.attackReady = false;
      this.playerRotationListener = this::Б;
      this.rotationDistanceSetting.withMaximum(() -> !this.rotationTypeSetting.isSelected(Якщ) && !this.rotationTypeSetting.isSelected(ЯкГ) ? Якэ : Якл);
      this.addSettings(
         this.rotationTypeSetting,
         this.hvhRotationStyleSetting,
         this.generationSpeedSetting,
         this.heightFactorSetting,
         this.predictionSetting,
         this.aimSpeedSetting,
         this.aimFovSetting,
         this.speedMultiplierSetting,
         this.verticalShareSetting,
         this.snapSharpnessSetting,
         this.smoothnessSetting,
         this.horizontalJitterSetting,
         this.verticalJitterSetting,
         this.aimPointSetting,
         this.targetSortSetting,
         this.combatOptionsSetting,
         this.attackDistanceSetting,
         this.rotationDistanceSetting,
         this.rotationAccelerationSetting,
         this.snapTicksSetting,
         this.movementCorrectionSetting,
         this.targetTypesSetting,
         this.clientLookSetting,
         this.targetVisualSetting,
         this.squareTypeSetting,
         this.ringTypeSetting,
         this.ghostTypeSetting,
         this.crystalTypeSetting,
         this.seeThroughCrystalsSetting,
         this.lightningTypeSetting,
         this.lightningThroughWallsSetting,
         this.visualLightingSetting,
         this.ringSpeedSetting,
         this.soulCountSetting,
         this.themeColorSetting,
         this.displayColorSetting,
         this.crystalGradientColorSetting,
         this.glowSetting,
         this.ghostSeeThroughSetting,
         this.distortionSetting,
         this.distortionStrengthSetting,
         this.redOnHitSetting,
         this.visualThroughWallsSetting,
         this.jellyParticleSizeSetting,
         this.jellyLayersSetting
      );
   }

   @Generated
   public SingleChoiceSetting П_f1816549() {
      return this.crystalTypeSetting;
   }

   @Generated
   public void д(long var1) {
      this.lastRandomOffsetUpdateNanos = var1;
   }

   public boolean ОО() {
      return false;
   }

   public boolean е(Entity var1) {
      return this.method_5I() && var1 == this.currentTarget;
   }

   @Generated
   public EventListener<RotationFrameEvent> щ() {
      return this.rotationFrameListener;
   }

   @Generated
   public FloatSetting Й() {
      return this.soulCountSetting;
   }

   @Generated
   public long П_6da43b94() {
      return this.lastFixedStepUpdateNanos;
   }

   private void ЩС() {
      RotationRegistry var1 = RoseClient.getInstance().getServices().getRotations();
      var1.getSpookyTimeSmoothMode().resetState();
      if (this.selectedRotationStrategy == this.directRotation || var1.getActiveStrategy() == this.directRotation || var1.isScheduledStrategyExpired(this.directRotation)) {
         this.Щ5();
      } else if (this.selectedRotationStrategy != var1.getSnapAimRotation() && var1.getActiveStrategy() != var1.getSnapAimRotation() && !var1.isScheduledStrategyExpired(var1.getSnapAimRotation())) {
         if (this.selectedRotationStrategy == var1.getDefensiveJitterRotation() || var1.getActiveStrategy() == var1.getDefensiveJitterRotation()) {
            var1.getDefensiveJitterRotation().resetState();
         }

         var1.activateStrategy(null);
      } else {
         this.ЩЩ();
      }
   }

   @Generated
   public boolean О8() {
      return this.snapHoldRequiresClear;
   }

   @Generated
   public boolean ОИ() {
      return this.clientLookInitialized;
   }

   @Generated
   public FloatSetting о_989fed9a() {
      return this.distortionStrengthSetting;
   }

   private boolean ОБ() {
      if (!this.rotationTypeSetting.isSelected(ЯЙ3)) {
         return true;
      }

      if (MINECRAFT_CLIENT.player != null && this.currentTarget != null) {
         Vec3 var1 = this.currentTarget.getBoundingBox().getCenter().subtract(MINECRAFT_CLIENT.player.getEyePosition());
         if (var1.x * var1.x + var1.z * var1.z < ЯЙр) {
            return true;
         }

         float var2 = (float)Math.toDegrees(Math.atan2(-var1.x, var1.z));
         float var3 = RoseClient.getInstance().getControllers().getRotations().getCameraRotation().x;
         return Math.abs(Mth.wrapDegrees(var2 - var3)) <= ЯЙ6;
      } else {
         return false;
      }
   }

   @Generated
   public void А(int var1) {
      this.remainingSnapAimTicks = var1;
   }

   private boolean method_5й() {
      return this.targetVisualSetting.isSelected(ЯФ西) && this.ringTypeSetting.isSelected(ЯФд);
   }

   private boolean isThroughWallsVisible() {
      return this.method_5й() || this.targetVisualSetting.isSelected(ЯФСакура);
   }

   public boolean isSakuraTargetVisualSelected() {
      return this.targetVisualSetting.isSelected(ЯФСакура);
   }

   @Generated
   public void ш(float var1) {
      this.pitchQuantizationRemainder = var1;
   }

   @Generated
   public long д_6da43b94() {
      return this.lastRandomOffsetUpdateNanos;
   }

   @Generated
   public ColorSetting method_8_f4ff5ac1() {
      return this.displayColorSetting;
   }

   @Generated
   public SingleChoiceSetting Т_f1816549() {
      return this.ghostTypeSetting;
   }

   @Generated
   public long 必_6da43b94() {
      return this.lastDirectAimUpdateNanos;
   }

   @Generated
   public EventListener<InputEvent> ц() {
      return this.inputListener;
   }

   @Generated
   public BooleanSetting н_3276742a() {
      return this.clientLookSetting;
   }

   private void ЩИ() {
      if (this.ОЩ()) {
         Vec3 var1 = MINECRAFT_CLIENT.player.getEyePosition();
         Vec3 var2 = this.currentTarget.position().add(0.0, this.currentTarget.getBbHeight() / 2.0F, 0.0);
         double var3 = var1.distanceTo(var2);
         HashSet<BlockPos> var5 = new HashSet<>();

         for (float var6 = 0.0F; var6 <= 1.0F; var6 += Якч) {
            Vec3 var7 = AimGeometryMath.lerpVector(var1, var2, var6);
            BlockPos var8 = BlockPos.containing(var7);
            if (!MINECRAFT_CLIENT.level.getBlockState(var8).isAir()) {
               var5.add(var8);
            }
         }

         BlockPos var11 = null;

         for (BlockPos var14 : var5) {
            if (var11 != null && var11.equals(var14)) {
               var5.remove(var14);
               var11 = var14;
            } else if (var11 == null) {
               var11 = var14;
            }
         }

         for (BlockPos var15 : var5) {
            Vec3 var9 = var1.subtract(var2);
            Direction var10 = Direction.getApproximateNearest(var9);
            PacketEventBypass.method_4(new ServerboundPlayerActionPacket(Action.START_DESTROY_BLOCK, var15, var10));
            PacketEventBypass.method_4(new ServerboundPlayerActionPacket(Action.STOP_DESTROY_BLOCK, var15, var10));
         }
      }
   }

   @Generated
   public FloatSetting р() {
      return this.horizontalJitterSetting;
   }

   public void е(InputEvent var1) {
      if (MINECRAFT_CLIENT.gui.screen() == null && var1.getAction() == 1 && var1.getInputCode() == 295 && this.selectedRotationStrategy instanceof HumanAimRotation var2 && var2.loadModel()) {
         ClientChat.З(ЯЙт);
      }
   }

   @Generated
   public float БП() {
      return this.yawQuantizationRemainder;
   }

   public void Щ(Frame60Event var1) {
      if (!this.rotationTypeSetting.isSelected(ЯЙа)) {
         if (this.selectedRotationStrategy != null && this.selectedRotationStrategy.getSpeed() == RotationStrategy.Speed.MEDIUM && !this.rotationTypeSetting.isSelected(ЯЙЮ) && !this.rotationTypeSetting.isSelected(ЯЙЧ)) {
            this.ОН();
         }
      } else {
         RotationRegistry var2 = RoseClient.getInstance().getServices().getRotations();
         if (this.selectedRotationStrategy != null
            && (this.selectedRotationStrategy.getSpeed() == RotationStrategy.Speed.FAST || this.selectedRotationStrategy == var2.getDirectAimRotation())
            && this.updateSnapAimPhase(this.attackDistanceSetting.getValue())) {
            this.Ол();
         }
      }
   }

   @Generated
   public long 西_6da43b94() {
      return this.lastClientLookUpdateNanos;
   }

   @Generated
   public FloatSetting method_6_989fed9a() {
      return this.generationSpeedSetting;
   }

   @Generated
   public MultiChoiceSetting Щ() {
      return this.combatOptionsSetting;
   }

   private void ЩЩ() {
      RotationRegistry var1 = RoseClient.getInstance().getServices().getRotations();
      RotationController var2 = RoseClient.getInstance().getControllers().getRotations();
      Vector2f var3 = MINECRAFT_CLIENT.player == null ? new Vector2f(var2.getAppliedRotation()) : new Vector2f(MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot());
      var2.getCameraRotation().set(var3);
      SnapAimRotation var4 = var1.getSnapAimRotation();
      var1.removeStrategy(var4);
      var2.releaseStrategy(var4);
      var2.getAppliedRotation().set(var3);
      var4.resetState();
      if (MINECRAFT_CLIENT.player != null) {
         MINECRAFT_CLIENT.player.setYRot(var3.x);
         MINECRAFT_CLIENT.player.setXRot(var3.y);
      }
   }

   @Generated
   public long Т_6da43b94() {
      return this.lastLegitAimUpdateNanos;
   }

   @Generated
   public void П(long var1) {
      this.lastFixedStepUpdateNanos = var1;
   }

   @Generated
   public void setAttackReady(boolean attackReady) {
      this.attackReady = attackReady;
   }

   @Override
   public void loadConfig(JsonObject var1) {
      super.loadConfig(var1);
      if (ЯФБ.equals(this.targetVisualSetting.getPendingSelectionName())) {
         this.targetVisualSetting.selectByName(ЯФС);
         this.ringTypeSetting.selectByName(ЯФз);
      }
   }

   @Generated
   public SingleChoiceSetting Г_f1816549() {
      return this.hvhRotationStyleSetting;
   }

   @Generated
   public FloatSetting З_989fed9a() {
      return this.snapTicksSetting;
   }

   @Generated
   public float Би() {
      return this.smoothedYawVelocity;
   }

   @Generated
   public BooleanSetting Э_3276742a() {
      return this.visualThroughWallsSetting;
   }

   public void О(PlayerTickEvent.Post var1) {
      if (RoseClient.getInstance().getServices().getModules().getCriticals().Э5()) {
         this.Оъ();
      }
   }

   @Generated
   public float Б必() {
      return this.pitchQuantizationRemainder;
   }

   @Generated
   public BooleanSetting Ф() {
      return this.glowSetting;
   }

   @Generated
   public FloatSetting А_989fed9a() {
      return this.aimFovSetting;
   }

   @Generated
   public FloatSetting в_989fed9a() {
      return this.jellyLayersSetting;
   }

   public boolean ОС() {
      return this.currentTarget != null
         && this.ОБ()
         && AttackReadiness.Х(this.currentTarget, this.combatOptionsSetting.isSelected(ЯЙO), this.combatOptionsSetting.isSelected(ЯЙ7), false, true, this.attackDistanceSetting.getValue(), this.О5(), this.combatOptionsSetting.isSelected(ЯЙР));
   }

   @Generated
   public void Б(MultiChoiceSetting var1) {
      this.combatOptionsSetting = var1;
   }

   private void ОВ() {
      if (this.currentTarget != null && MINECRAFT_CLIENT.gui.screen() == null) {
         Vec3 var1 = this.О(this.currentTarget);
         float var2 = (float)Mth.wrapDegrees(Math.toDegrees(Math.atan2(var1.z, var1.x)) - ЯЙц);
         float var3 = (float)(-Math.toDegrees(Math.atan2(var1.y, Math.hypot(var1.x, var1.z))));
         float var4 = Mth.wrapDegrees(var2 - MINECRAFT_CLIENT.player.getYRot());
         float var5 = Mth.wrapDegrees(var3 - MINECRAFT_CLIENT.player.getXRot());
         if ((float)Math.hypot(var4, var5) > this.aimFovSetting.getValue()) {
            this.lastLegitAimUpdateNanos = System.nanoTime();
         } else {
            long var6 = System.nanoTime();
            float var8 = this.lastLegitAimUpdateNanos == 0L ? 0.0F : (float)(var6 - this.lastLegitAimUpdateNanos) / ЯЙя;
            this.lastLegitAimUpdateNanos = var6;
            var8 = Mth.clamp(var8, 0.0F, Якс);
            if (!(var8 <= 0.0F)) {
               if (this.horizontalJitterSetting.getValue() > 0.0F) {
                  var4 += AimGeometryMath.randomizedInterpolation(-this.horizontalJitterSetting.getValue(), this.horizontalJitterSetting.getValue());
               }

               if (this.verticalJitterSetting.getValue() > 0.0F) {
                  var5 += AimGeometryMath.randomizedInterpolation(-this.verticalJitterSetting.getValue(), this.verticalJitterSetting.getValue());
               }

               float var9 = this.aimSpeedSetting.getValue() * this.speedMultiplierSetting.getValue();
               float var10 = var9 * this.verticalShareSetting.getValue();
               float var11 = this.snapSharpnessSetting.getValue();
               float var12 = Mth.clamp(var4 * var11, -var9, var9);
               float var13 = Mth.clamp(var5 * var11, -var10, var10);
               float var14 = this.smoothnessSetting.getValue();
               MINECRAFT_CLIENT.player.turn(var12 * var8 / var14, var13 * var8 / var14);
            }
         }
      } else {
         this.lastLegitAimUpdateNanos = System.nanoTime();
      }
   }

   @Generated
   public LivingEntity Ж_95bedd6e() {
      return this.currentTarget;
   }

   @Generated
   public SingleChoiceSetting method_8_f1816549() {
      return this.rotationTypeSetting;
   }

   @Generated
   public boolean isAttackReady() {
      return this.attackReady;
   }

   private Vec3 directionToTargetBounds(LivingEntity target) {
      Vec3 eyePosition = MINECRAFT_CLIENT.player.getEyePosition();
      AABB targetBounds = target.getBoundingBox();
      double nearestX = Mth.clamp(eyePosition.x, targetBounds.minX, targetBounds.maxX);
      double nearestZ = Mth.clamp(eyePosition.z, targetBounds.minZ, targetBounds.maxZ);
      double aimY = targetBounds.minY + (targetBounds.maxY - targetBounds.minY) * Якж;
      return new Vec3(nearestX, aimY, nearestZ).subtract(eyePosition);
   }

   @Generated
   public int Сю() {
      return this.remainingSnapAimTicks;
   }

   @Generated
   public FloatSetting т() {
      return this.attackDistanceSetting;
   }

   private void щ(WorldEffectsEvent var1) {
      this.Ой();
      TargetEspMask.setDistortionOptions(this.distortionSetting.getValue(), this.distortionStrengthSetting.getValue());
      TargetEspMask.setGhostOptions(this.glowSetting.getValue(), this.ghostSeeThroughSetting.getValue());
      TargetEspMask.setRingParticleOptions(this.visualThroughWallsSetting.getValue(), this.jellyParticleSizeSetting.getValue(), this.jellyLayersSetting.getValue().intValue());
      TargetEspMask.setStyleOptions(
         this.ringSpeedSetting.getValue(),
         this.ringTypeSetting.getSelectedOption().getName(),
         this.soulCountSetting.getValue().intValue(),
         this.redOnHitSetting.getValue(),
         this.ghostTypeSetting.getSelectedOption().getName(),
         this.crystalTypeSetting.getSelectedOption().getName(),
         this.seeThroughCrystalsSetting.getValue(),
         this.lightningTypeSetting.getSelectedOption().getName(),
         this.lightningThroughWallsSetting.getValue(),
         this.visualLightingSetting.getValue(),
         this.squareTypeSetting.getSelectedOption().getName()
      );
      int var2 = this.themeColorSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.displayColorSetting.getValue();
      int var3 = this.themeColorSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO) : this.crystalGradientColorSetting.getValue();
      TargetEspMask.render(var1, this.currentTarget, this.targetVisualSetting.getSelectedOption().getName(), var2, var3);
      Sakura sakura = RoseClient.getInstance().getServices().getModules().getSakura();
      if (this.isSakuraTargetVisualSelected()) {
         sakura.renderTarget(var1, this.currentTarget, var2, this.visualThroughWallsSetting.getValue());
      } else {
         sakura.clearTarget();
      }
   }

   @Generated
   public EventListener<MovementInputEvent> Т_f8db0d09() {
      return this.movementInputListener;
   }

   @Generated
   public EventListener<PlayerRotationEvent> М() {
      return this.playerRotationListener;
   }

   @Generated
   public Aura.DirectRotation Б_53f45fa7() {
      return this.directRotation;
   }

   @Generated
   public FloatSetting б() {
      return this.verticalShareSetting;
   }

   public boolean method_5В() {
      if (!this.isEnabled() || !this.clientLookSetting.getValue() || this.currentTarget == null || MINECRAFT_CLIENT.player == null) {
         return false;
      } else {
         return this.rotationTypeSetting.isSelected(ЯФО) && !this.ОБ() ? false : !RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled();
      }
   }

   private void adjustHorizontalVelocityAfterAttack(Vec3 previousVelocity) {
      if (MINECRAFT_CLIENT.player != null && this.currentTarget != null && previousVelocity != null) {
         double previousX = previousVelocity.x;
         double previousZ = previousVelocity.z;
         double[] adjustedVelocity = this.Б(previousX, previousZ);
         MINECRAFT_CLIENT.player.setDeltaMovement(adjustedVelocity[0], MINECRAFT_CLIENT.player.getDeltaMovement().y, adjustedVelocity[1]);
      }
   }

   @Generated
   public void setYawRandomOffset(float yawRandomOffset) {
      this.yawRandomOffset = yawRandomOffset;
   }

   @Generated
   public float Бц() {
      return this.clientLookPitch;
   }

   private Vec3 О(LivingEntity var1) {
      Vec3 var2 = MINECRAFT_CLIENT.player.getEyePosition();
      AABB var3 = var1.getBoundingBox();
      if (this.aimPointSetting.isSelected(Як弟)) {
         return var3.getCenter().subtract(var2);
      } else if (this.aimPointSetting.isSelected(Якб)) {
         Vec3 var16 = new Vec3((var3.minX + var3.maxX) * ЯкЫ, var3.maxY - var1.getEyeHeight(var1.getPose()) * Якl, (var3.minZ + var3.maxZ) * Як8);
         return var16.subtract(var2);
      } else if (this.aimPointSetting.isSelected(Як5)) {
         double var15 = Mth.clamp(var2.x, var3.minX, var3.maxX);
         double var17 = Mth.clamp(var2.y, var3.minY, var3.maxY);
         double var18 = Mth.clamp(var2.z, var3.minZ, var3.maxZ);
         return new Vec3(var15, var17, var18).subtract(var2);
      } else {
         double var4 = Mth.clamp(var2.x, var3.minX, var3.maxX);
         double var6 = Mth.clamp(var2.z, var3.minZ, var3.maxZ);
         Vec3 var8 = MINECRAFT_CLIENT.player.getViewVector(1.0F);
         double var9 = Math.hypot(var4 - var2.x, var6 - var2.z);
         double var11 = Math.hypot(var8.x, var8.z);
         double var13 = var11 > Якв ? var2.y + var8.y * (var9 / var11) : var3.minY + (var3.maxY - var3.minY) * Якъ;
         var13 = Mth.clamp(var13, var3.minY, var3.maxY);
         return new Vec3(var4, var13, var6).subtract(var2);
      }
   }

   @Generated
   public BooleanSetting Х() {
      return this.themeColorSetting;
   }

   @Generated
   public SingleChoiceSetting 必_f1816549() {
      return this.lightningTypeSetting;
   }

   @Generated
   public void Т(long var1) {
      this.lastLegitAimUpdateNanos = var1;
   }

   private void Оу() {
      if (!this.rotationTypeSetting.isSelected(ЯЙь) && this.selectedRotationStrategy == this.directRotation) {
         this.Щ5();
         this.selectedRotationStrategy = null;
      }

      RotationRegistry var1 = RoseClient.getInstance().getServices().getRotations();
      if (!this.rotationTypeSetting.isSelected("Spooky")) var1.getSpookyTimeSmoothMode().resetState();
      if (!this.rotationTypeSetting.isSelected(ЯЙж) && (this.selectedRotationStrategy == var1.getSnapAimRotation() || var1.getActiveStrategy() == var1.getSnapAimRotation())) {
         this.ЩЩ();
         this.selectedRotationStrategy = null;
      }

      if (!this.rotationTypeSetting.isSelected(ЯЙ弟) && (this.selectedRotationStrategy == var1.getDefensiveJitterRotation() || var1.getActiveStrategy() == var1.getDefensiveJitterRotation())) {
         var1.getDefensiveJitterRotation().resetState();
         if (var1.getActiveStrategy() == var1.getDefensiveJitterRotation()) {
            var1.activateStrategy(null);
         }

         this.selectedRotationStrategy = null;
      }

       if (this.rotationTypeSetting.isSelected("Spooky")) {
          this.selectedRotationStrategy = var1.getSpookyTimeSmoothMode();
       } else if (this.rotationTypeSetting.isSelected(ЯЙб)) {
         this.selectedRotationStrategy = var1.getDefensiveJitterRotation();
      } else if ((this.rotationTypeSetting.isSelected(ЯЙЫ) || this.rotationTypeSetting.isSelected(ЯЙl)) && !MINECRAFT_CLIENT.player.isFallFlying()) {
         this.selectedRotationStrategy = RoseClient.getInstance().getServices().getRotations().getSlowAimRotation();
      } else if (this.rotationTypeSetting.isSelected(ЯЙ8)) {
         this.selectedRotationStrategy = RoseClient.getInstance().getServices().getRotations().getSnapAimRotation();
      } else if (this.rotationTypeSetting.isSelected(ЯЙ5)) {
         this.selectedRotationStrategy = RoseClient.getInstance().getServices().getRotations().getOscillatingAimRotation();
      } else if (this.rotationTypeSetting.isSelected(ЯЙв)) {
          this.selectedRotationStrategy = var1.getFunTimeSmoothMode();
      } else if (this.rotationTypeSetting.isSelected(ЯЙъ)) {
         this.selectedRotationStrategy = RoseClient.getInstance().getServices().getRotations().getHumanizedRotation();
      } else if ((this.rotationTypeSetting.isSelected(ЯЙМ) || this.rotationTypeSetting.isSelected(ЯЙН)) && MINECRAFT_CLIENT.player.isFallFlying()) {
         this.selectedRotationStrategy = RoseClient.getInstance().getServices().getRotations().getDirectAimRotation();
      } else if (this.rotationTypeSetting.isSelected(ЯЙн)) {
         this.selectedRotationStrategy = RoseClient.getInstance().getServices().getRotations().getHumanAimRotation();
      }
   }

   @Generated
   public SingleChoiceSetting и_f1816549() {
      return this.squareTypeSetting;
   }

   @Generated
   public void в(float var1) {
      this.smoothedYawVelocity = var1;
   }

   private boolean О5() {
      ElytraTarget var1 = RoseClient.getInstance().getServices().getModules().getElytraTarget();
      return this.combatOptionsSetting.isSelected(ЯЙ_) && (!var1.isEnabled() || MINECRAFT_CLIENT.player == null || !MINECRAFT_CLIENT.player.isFallFlying());
   }

   @Generated
   public BooleanSetting Л() {
      return this.lightningThroughWallsSetting;
   }

   @Generated
   public void Н(float var1) {
      this.pitchRandomOffset = var1;
   }

   public void Ж(MovementInputEvent var1) {
      if (!this.targetingPaused) {
         RotationStrategy var2 = RoseClient.getInstance().getServices().getRotations().getActiveStrategy();
         if (var2 == null || !var2.shouldCaptureCameraRotationOnActivate()) {
            if (this.rotationTypeSetting.isSelected(ЯЙЬ) && this.movementCorrectionSetting.isSelected(ЯЙ衣) && this.currentTarget != null) {
               this.Г(var1);
            } else if (this.movementCorrectionSetting.isSelected(ЯЙш)) {
               MovementUtils.correctMovementInput(var1);
            } else if (this.movementCorrectionSetting.isSelected(ЯЙИ) && this.currentTarget != null) {
               Vec3 var3 = MINECRAFT_CLIENT.player.getEyePosition();
               Vec3 var4 = this.currentTarget.position().add(0.0, this.currentTarget.getBbHeight() / 2.0F, 0.0);
               Vec3 var5 = var4.subtract(var3).normalize();
               float var6 = (float)Math.toDegrees(Math.atan2(-var5.x, var5.z));
               MovementUtils.correctMovementInput(var1, var6);
            }
         }
      }
   }

   @Generated
   public FloatSetting Я() {
      return this.snapSharpnessSetting;
   }

   @Generated
   public MultiChoiceSetting И_3bdb855e() {
      return this.targetTypesSetting;
   }

   @Generated
   public float Б诶() {
      return this.pitchRandomOffset;
   }

   @Generated
   public RotationStrategy О() {
      return this.selectedRotationStrategy;
   }

   public int и(int var1) {
      if (this.currentTarget != null && var1 >= 0 && this.ОБ()) {
         ElytraTarget var2 = RoseClient.getInstance().getServices().getModules().getElytraTarget();
         float var3 = MINECRAFT_CLIENT.player.isFallFlying()
               && var2.isEnabled()
               && this.currentTarget.isFallFlying()
               && AimGeometryMath.getEntityMovementPerSecond(this.currentTarget) > var2.РР().getValue().floatValue()
               && var2.е_3276742a().getValue()
            ? var2.с_989fed9a().getValue()
            : this.attackDistanceSetting.getValue();
         return AttackReadiness.Х(this.currentTarget, this.combatOptionsSetting.isSelected(ЯЙи), this.combatOptionsSetting.isSelected(ЯЙ9), true, var3, this.combatOptionsSetting.isSelected(ЯЙо), var1);
      } else {
         return -1;
      }
   }

   private void ОН() {
      if (this.selectedRotationStrategy != null && this.currentTarget != null && !this.targetingPaused) {
         ElytraTarget var1 = RoseClient.getInstance().getServices().getModules().getElytraTarget();
         this.selectedRotationStrategy.setTargetPosition(var1.К(this.currentTarget, this.currentTarget.position().add(0.0, this.currentTarget.getBbHeight() / 2.0F + (MINECRAFT_CLIENT.player.isFallFlying() ? ЯЙС : 0.0F), 0.0)));
         RoseClient.getInstance().getServices().getRotations().activateStrategy(this.selectedRotationStrategy);
      }
   }

   public void З(PlayerTickEvent var1) {
      if (this.rotationTypeSetting.isSelected(ЯФц)) {
         this.О2();
      }

      if (this.currentTarget == null) {
         this.ЩБ();
         this.ЩС();
      } else if (this.targetingPaused) {
         this.ЩС();
         if (!RoseClient.getInstance().getServices().getModules().getCriticals().Э5()) {
            this.Оъ();
         }
      } else {
         this.Оу();
         if (this.selectedRotationStrategy != null) {
            float var2 = this.attackDistanceSetting.getValue();
            if (RoseClient.getInstance().getServices().getModules().getAirStuck().isEnabled()
               || RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled()) {
               var2 = ЯФя;
            }

            boolean var3 = !this.rotationTypeSetting.isSelected(ЯЙс) || this.updateSnapAimPhase(var2);
            if (this.rotationTypeSetting.isSelected(ЯЙВ)) {
               this.ЩС();
               this.selectedRotationStrategy = null;
             } else if (this.selectedRotationStrategy.getSpeed() == RotationStrategy.Speed.SLOW && !this.rotationTypeSetting.isSelected(ЯЙХ) && !this.rotationTypeSetting.isSelected(ЯЙе) && var3) {
               this.ОН();
            }

            if (this.rotationTypeSetting.isSelected(ЯЙА) && !MINECRAFT_CLIENT.player.isFallFlying()) {
               if (AttackReadiness.Х(this.currentTarget, this.combatOptionsSetting.isSelected(ЯЙ4), this.combatOptionsSetting.isSelected(ЯЙп), false, true, var2, this.О5(), this.combatOptionsSetting.isSelected(ЯЙД))) {
                  this.remainingSnapAimTicks = this.snapTicksSetting.getValue().intValue();
               }

               if (this.remainingSnapAimTicks > 0) {
                  this.ОН();
                  this.remainingSnapAimTicks--;
               } else {
                  this.ЩС();
               }
            }
         }

         if (!RoseClient.getInstance().getServices().getModules().getCriticals().Э5()) {
            this.Оъ();
         }
      }
   }

   @Generated
   public void В(float var1) {
      this.clientLookPitch = var1;
   }

   private void Щ5() {
      RotationRegistry var1 = RoseClient.getInstance().getServices().getRotations();
      RotationController var2 = RoseClient.getInstance().getControllers().getRotations();
      Vector2f var3 = MINECRAFT_CLIENT.player == null ? new Vector2f(var2.getAppliedRotation()) : new Vector2f(MINECRAFT_CLIENT.player.getYRot(), MINECRAFT_CLIENT.player.getXRot());
      var2.getCameraRotation().set(var3);
      var1.removeStrategy(this.directRotation);
      var2.releaseStrategy(this.directRotation);
      var2.getAppliedRotation().set(var3);
      if (MINECRAFT_CLIENT.player != null) {
         MINECRAFT_CLIENT.player.setYRot(var3.x);
         MINECRAFT_CLIENT.player.setXRot(var3.y);
      }
   }

   @Generated
   public FloatSetting getRotationAccelerationSetting() {
      return this.rotationAccelerationSetting;
   }

   @Generated
   public SingleChoiceSetting Ч() {
      return this.aimPointSetting;
   }

   @Generated
   public void Б(RotationStrategy var1) {
      this.selectedRotationStrategy = var1;
   }

   private void Оъ() {
      if (!RoseClient.getInstance().getServices().getModules().getWindHop().ЫЙ()) {
         if (!this.ОБ()) {
            this.attackReady = false;
         } else {
            ElytraTarget var1 = RoseClient.getInstance().getServices().getModules().getElytraTarget();
            float var2 = MINECRAFT_CLIENT.player.isFallFlying()
                  && var1.isEnabled()
                  && this.currentTarget.isFallFlying()
                  && AimGeometryMath.getEntityMovementPerSecond(this.currentTarget) > var1.РР().getValue().floatValue()
                  && var1.е_3276742a().getValue()
               ? var1.с_989fed9a().getValue()
               : this.attackDistanceSetting.getValue();
            boolean var3 = MINECRAFT_CLIENT.player.getMainHandItem().is(Items.MACE);
            if (!MINECRAFT_CLIENT.player.isUsingItem() || MINECRAFT_CLIENT.player.isBlocking() || !this.combatOptionsSetting.isSelected(ЯЙЦ)) {
               this.attackReady = AttackReadiness.Х(this.currentTarget, this.combatOptionsSetting.isSelected(ЯЙч), this.combatOptionsSetting.isSelected(ЯЙЭ), true, true, var2, this.О5(), this.combatOptionsSetting.isSelected(ЯЙ必));
               if (this.attackReady) {
                  this.ЩИ();
                  if (this.combatOptionsSetting.isSelected(ЯЙщ) && MINECRAFT_CLIENT.player.isBlocking()) {
                     MINECRAFT_CLIENT.player.releaseUsingItem();
                  }

                  Vec3 var4 = MINECRAFT_CLIENT.player.getDeltaMovement();
                   MINECRAFT_CLIENT.gameMode.attack(MINECRAFT_CLIENT.player, this.currentTarget);
                   MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
                   if (this.rotationTypeSetting.isSelected("Spooky")) {
                      RoseClient.getInstance().getServices().getRotations().getSpookyTimeSmoothMode()
                         .onAttack(RoseClient.getInstance().getControllers().getRotations().getAppliedRotation());
                   }
                  if ((this.rotationTypeSetting.isSelected(ЯЙГ) || this.rotationTypeSetting.isSelected(ЯЙл)) && RoseClient.getInstance().getServices().getModules().getStrafe().isEnabled() && !MINECRAFT_CLIENT.player.isFallFlying()) {
                     this.adjustHorizontalVelocityAfterAttack(var4);
                  }

                  if (this.combatOptionsSetting.isSelected(ЯЙэ)) {
                     this.Ош();
                  }

                  if (this.rotationTypeSetting.isSelected(ЯЙю)) {
                     RoseClient.getInstance().getServices().getRotations().getSnapAimRotation().requestReturnToCamera();
                  }
               }
            }
         }
      }
   }

   @Generated
   public FloatSetting getRingSpeedSetting() {
      return this.ringSpeedSetting;
   }

   @Generated
   public SingleChoiceSetting ч_f1816549() {
      return this.ringTypeSetting;
   }

   @Generated
   public EventListener<Frame60Event> 西_f8db0d09() {
      return this.frame60Listener;
   }

   private void Ош() {
      int var1 = InventoryInteractions.Ч();
      if (this.currentTarget.getUseItem().getItem() == Items.SHIELD && var1 != -1) {
         if (var1 < 9) {
            MINECRAFT_CLIENT.getConnection().send(new ServerboundSetCarriedItemPacket(var1));
            MINECRAFT_CLIENT.gameMode.attack(MINECRAFT_CLIENT.player, this.currentTarget);
            MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
            MINECRAFT_CLIENT.getConnection().send(new ServerboundSetCarriedItemPacket(MINECRAFT_CLIENT.player.getInventory().selected));
         } else {
            MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, var1, MINECRAFT_CLIENT.player.getInventory().selected, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
            MINECRAFT_CLIENT.gameMode.attack(MINECRAFT_CLIENT.player, this.currentTarget);
            MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
            MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, var1, MINECRAFT_CLIENT.player.getInventory().selected, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
         }
      }
   }

   @Generated
   public BooleanSetting а_3276742a() {
      return this.visualLightingSetting;
   }

   @Generated
   public int СХ() {
      return this.pendingFixedSteps;
   }

   public void Б(RotationFrameEvent var1) {
      if (!this.rotationTypeSetting.isSelected(ЯЙЛ)) {
         this.О2();
         if (this.selectedRotationStrategy != null && this.selectedRotationStrategy.getSpeed() == RotationStrategy.Speed.FAST && !this.rotationTypeSetting.isSelected(ЯЙ诶) && !this.rotationTypeSetting.isSelected(ЯЙ1)) {
            this.ОН();
         }
      }
   }

   public void Б(PlayerRotationEvent var1) {
      if (this.ОЩ() && !this.attackReady) {
         Vector2f var10000 = var1.getRotation();
         var10000.x = var10000.x + ЯЙЪ;
      }
   }

   private static final class DirectRotation extends RotationStrategy {
      private final Vector2f е = new Vector2f();

      private DirectRotation() {
         super(Vec3.ZERO);
      }

      private void Н(float var1, float var2) {
         this.е.set(var1, var2);
      }

      @Override
      public Vector2f calculateRotation(Vector2f var1, Vector2f var2) {
         return new Vector2f(var2);
      }

      @Override
      public Vector2f getTargetRotation() {
         return new Vector2f(this.е);
      }
   }
}
