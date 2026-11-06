package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import com.mojang.math.Axis;
import java.awt.Color;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.ec.MinecraftAccess;
import sg.ec.GlowQuadRendering;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.client.RoseClient;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.FloatSetting;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.render.pipeline.EntityEspOuterGlowRenderer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.TrailPathHistory;

public class Trails extends ClientModule {
   private static final Identifier сЪ = Identifier.fromNamespaceAndPath(Trails.ш6, Trails.ш_);
   private static final Identifier ст = Identifier.fromNamespaceAndPath(Trails.шШ, Trails.шы);
   private static final Identifier са = Identifier.fromNamespaceAndPath(Trails.шй, Trails.шЖ);
   private static final int[] Щ = new int[]{11, 23, 32, 16, 32};
   private static final int тЯ = 160;
   private static final int тХ = 220;
   private static final double Хс = 0.12;
   private final @com.google.gson.annotations.SerializedName("сэ")  SingleChoiceSetting modeSetting;
   private final @com.google.gson.annotations.SerializedName("ек")  FloatSetting dimaParticleLifetimeSetting;
   private final @com.google.gson.annotations.SerializedName("ег")  FloatSetting dimaParticleDensitySetting;
   private final @com.google.gson.annotations.SerializedName("е0")  FloatSetting dimaParticleScaleSetting;
   private final @com.google.gson.annotations.SerializedName("Я7")  BooleanSetting dimaThemeColorSetting;
   private final @com.google.gson.annotations.SerializedName("Ш")  ColorSetting dimaCustomColorSetting;
   private final @com.google.gson.annotations.SerializedName("ЯР")  BooleanSetting dashSelfSetting;
   private final @com.google.gson.annotations.SerializedName("Я3")  BooleanSetting dashFirstPersonSetting;
   private final @com.google.gson.annotations.SerializedName("Яр")  BooleanSetting dashPlayersSetting;
   private final @com.google.gson.annotations.SerializedName("Я6")  BooleanSetting dashFriendsSetting;
   private final @com.google.gson.annotations.SerializedName("сю")  SingleChoiceSetting dashColorModeSetting;
   private final @com.google.gson.annotations.SerializedName("ы")  ColorSetting dashSelectedColorSetting;
   private final @com.google.gson.annotations.SerializedName("ех")  FloatSetting dashMaxDistanceSetting;
   private final @com.google.gson.annotations.SerializedName("Я_")  BooleanSetting dashSmoothMovementSetting;
   private final @com.google.gson.annotations.SerializedName("ЯШ")  BooleanSetting dashSegmentsSetting;
   private final @com.google.gson.annotations.SerializedName("Яы")  BooleanSetting dashPointsSetting;
   private final @com.google.gson.annotations.SerializedName("Яй")  BooleanSetting dashLightingSetting;
   private final @com.google.gson.annotations.SerializedName("еУ")  FloatSetting dashTrailLengthSetting;
   private final @com.google.gson.annotations.SerializedName("сЩ")  SingleChoiceSetting dashRenderQualitySetting;
   private final @com.google.gson.annotations.SerializedName("еЗ")  FloatSetting energyTrailLengthSetting;
   private final @com.google.gson.annotations.SerializedName("еЕ")  FloatSetting energyTrailWidthSetting;
   private final @com.google.gson.annotations.SerializedName("ЯЖ")  BooleanSetting energyAutoThemeColorSetting;
   private final @com.google.gson.annotations.SerializedName("й")  ColorSetting energyFirstColorSetting;
   private final @com.google.gson.annotations.SerializedName("Ж")  ColorSetting energySecondColorSetting;
   private final @com.google.gson.annotations.SerializedName("м")  ColorSetting energyThirdColorSetting;
   private final @com.google.gson.annotations.SerializedName("еП")  FloatSetting ghostTrailLengthSetting;
   private final @com.google.gson.annotations.SerializedName("е西")  FloatSetting ghostParticleSizeSetting;
   private final @com.google.gson.annotations.SerializedName("ед")  FloatSetting ghostSpeedSetting;
   private final @com.google.gson.annotations.SerializedName("В")  TrailPathHistory ghostPathHistory;
   private @com.google.gson.annotations.SerializedName("Ц")  ClientLevel ghostHistoryLevel;
   private static final Identifier сЮ = ClientResourceIds.getImageId(Trails.шм);
   @SerializedName("Вб")
   private final RenderPipeline ghostParticlePipeline;
   private final @com.google.gson.annotations.SerializedName("Вж")  List<Trails.DashTrail> dashTrails;
   private final @com.google.gson.annotations.SerializedName("В弟")  List<Trails.GhostParticle> dashGhostParticles;
   @SerializedName("Вl")
   private final Map<Integer, Vec3> lastDashPositionsByEntityId;
   @SerializedName("Вб")
   private final List<Trails.TextureFrame> dashCubicFrames;
   @SerializedName("ВЫ")
   private final List<List<Trails.TextureFrame>> dashAnimatedFrames;
   @SerializedName("Вl")
   private final List<Trails.TimedPosition> energyTimedPositions;
   private final @com.google.gson.annotations.SerializedName("诶")  Random random;
   private @com.google.gson.annotations.SerializedName("АЕ")  boolean dashTexturesLoaded;
   private @com.google.gson.annotations.SerializedName("еш")  long lastEnergySampleMillis;
   @SerializedName("9")
   private TextureTarget energyGlowTarget;
   @SerializedName("ВЫ")
   private final RenderPipeline dashTrailPipeline;
   @SerializedName("Вl")
   private final RenderPipeline energyRibbonPipeline;
   private final @com.google.gson.annotations.SerializedName("еЦ")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("еч")  EventListener<WorldEffectsEvent> worldEffectsListener;
   private @com.google.gson.annotations.SerializedName("сЯ")  Vec3 lastDashSpawnPosition;
   private @com.google.gson.annotations.SerializedName("ч")  ClientLevel worldEffectsLevel;
   private static final String 诶ю = "Trails";
   private static final String 诶Щ = "Шлейф рывка";
   private static final String 诶у = "Режим";
   private static final String 诶2 = "Дима";
   private static final String 诶ф = "Рывок";
   private static final String 诶и = "Энергия";
   private static final String 诶9 = "Призраки";
   private static final String 诶о = "Время жизни";
   private static final float 诶O = 1.1F;
   private static final float 诶7 = 0.35F;
   private static final float 诶Р = 2.5F;
   private static final float 诶3 = 0.05F;
   private static final String 诶р = "Плотность";
   private static final float 诶6 = 12.0F;
   private static final float 诶_ = 4.0F;
   private static final float 诶Ш = 20.0F;
   private static final float 诶ы = 0.5F;
   private static final String 诶й = "Масштаб";
   private static final float 诶Ж = 0.55F;
   private static final float 诶м = 1.8F;
   private static final float 诶К = 0.05F;
   private static final String 诶I = "Цвет темы";
   private static final String 诶Т = "Цвет";
   private static final int 诶О = -2342913;
   private static final String 诶Ф = "Себе";
   private static final String 诶Й = "В первом лице";
   private static final String 诶к = "Игроки";
   private static final String 诶г = "Друзья";
   private static final String 诶0 = "Режим цвета";
   private static final String 诶х = "Случайная палитра";
   private static final String 诶У = "Свой";
   private static final String 诶З = "Клиент";
   private static final String 诶Е = "Радуга";
   private static final String 诶П = "Выбранный цвет";
   private static final double 诶西 = 255.0;
   private static final String 诶д = "Максимальная дистанция";
   private static final float 诶Б = 25.0F;
   private static final float 诶С = 15.0F;
   private static final float 诶з = 100.0F;
   private static final String 诶ц = "Сглаживание движения";
   private static final String 诶я = "Сегменты";
   private static final String field_1с = "Точки";
   private static final String field_1В = "Освещение";
   private static final String field_1Я = "Длина следа";
   private static final float field_1Х = 0.75F;
   private static final float field_1е = 0.5F;
   private static final float field_1А = 1.5F;
   private static final float field_14 = 0.05F;
   private static final String field_1п = "Качество отрисовки";
   private static final String field_1Д = "Производительность";
   private static final String field_1Ъ = "Качество";
   private static final String field_1т = "Максимальное качество";
   private static final String field_1а = "Длина";
   private static final float field_1Ю = 1.9F;
   private static final float field_1Ч = 0.35F;
   private static final float field_1Л = 0.05F;
   private static final String field_1诶 = "Ширина";
   private static final float field_11 = 0.38F;
   private static final float field_1Ь = 0.08F;
   private static final float field_1衣 = 0.6F;
   private static final float field_1ш = 0.02F;
   private static final String field_1И = "Авто цвет под тему";
   private static final String field_1ь = "Цвет 1";
   private static final int field_1ж = -2342913;
   private static final String field_1弟 = "Цвет 2";
   private static final int field_1б = -10709505;
   private static final String field_1Ы = "Цвет 3";
   private static final int field_1l = -44927;
   private static final String field_18 = "Длина шлейфа";
   private static final float field_15 = 25.0F;
   private static final float field_1в = 10.0F;
   private static final float field_1ъ = 50.0F;
   private static final String field_1М = "Размер частиц";
   private static final float field_1Н = 0.6F;
   private static final float field_1н = 0.3F;
   private static final float field_1Ц = 1.2F;
   private static final float field_1ч = 0.05F;
   private static final String field_1Э = "Скорость призраков";
   private static final float field_1必 = 3.0F;
   private static final float field_1щ = 10.0F;
   private static final float field_1Г = 0.5F;
   private static final String field_1л = "rose/trails_ghosts";
   private static final long field_1э = 1234567891L;
   private static final String field_1ю = "rose/dima_trails";
   private static final String field_1Щ = "dead_chams";
   private static final String field_1у = "dead_chams";
   private static final String field_12 = "Sampler0";
   private static final String field_1ф = "rose/trails_ribbon";
   private static final String field_1и = "trails_ribbon";
   private static final String field_19 = "trails_ribbon";
   private static final String field_1о = "TrailsStyle";
   private static final String field_1O = "rose";
   private static final String field_17 = "rose";
   private static final String field_1Р = "Призраки";
   private static final String field_13 = "Рывок";
   private static final double field_1р = 0.08;
   private static final double field_16 = 0.08;
   private static final float field_1_ = 16.0F;
   private static final float field_1Ш = 0.05F;
   private static final String field_1ы = "Производительность";
   private static final String field_1й = "Максимальное качество";
   private static final float field_1Ж = 0.3333333F;
   private static final float field_1м = 5.0F;
   private static final String field_1К = "Производительность";
   private static final String field_1I = "Максимальное качество";
   private static final float field_1Т = 1.1F;
   private static final float field_1О = 0.333333F;
   private static final float field_1Ф = 0.035F;
   private static final String field_1Й = "Производительность";
   private static final String field_1к = "Максимальное качество";
   private static final float field_1г = 0.5F;
   private static final String field_10 = "Свой";
   private static final String field_1х = "Случайная палитра";
   private static final String field_1У = "Радуга";
   private static final float field_1З = 360.0F;
   private static final int field_1Е = -16777216;
   private static final long field_1П = 1000L;
   private static final float field_1西 = 1000.0F;
   private static final float field_1д = 0.8F;
   private static final int field_1Б = -16777216;
   private static final String field_1С = "Призраки";
   private static final String field_1з = "Рывок";
   private static final String field_1ц = "Энергия";
   private static final double field_1я = 20.0;
   private static final double Ьс = 2.0;
   private static final float ЬВ = 0.65F;
   private static final double ЬЯ = 40.0;
   private static final float ЬХ = 0.6F;
   private static final float Ье = 1.25F;
   private static final double ЬА = 0.8;
   private static final float Ь4 = 220.0F;
   private static final double Ьп = 0.015;
   private static final double ЬД = 0.5;
   private static final double ЬЪ = 50.0;
   private static final double Ьт = 360.0;
   private static final double Ьа = 0.1;
   private static final double ЬЮ = 1.7;
   private static final double ЬЧ = 1.8;
   private static final double ЬЛ = 1.7;
   private static final float Ь诶 = 0.08F;
   private static final float Ь1 = 0.16F;
   private static final float ЬЬ = 0.3F;
   private static final float Ь衣 = 0.56F;
   private static final float Ьш = 12.0F;
   private static final float ЬИ = 12.0F;
   private static final float Ьь = 12.0F;
   private static final float Ьж = 0.05F;
   private static final double Ь弟 = 100.0;
   private static final double Ьб = 360.0;
   private static final double ЬЫ = 180.0;
   private static final double Ьl = 360.0;
   private static final double Ь8 = 180.0;
   private static final float Ь5 = 0.25F;
   private static final float Ьв = 0.05F;
   private static final float Ьъ = 0.033F;
   private static final float ЬМ = 0.1F;
   private static final float ЬН = 0.45F;
   private static final float Ьн = 0.033F;
   private static final float ЬЦ = 0.1F;
   private static final float Ьч = 0.15F;
   private static final float ЬЭ = 1.75F;
   private static final float Ь必 = 1.75F;
   private static final float Ьщ = 0.21568628F;
   private static final float ЬГ = 6.0F;
   private static final float Ьл = 4.0F;
   private static final float Ьэ = 0.43137255F;
   private static final float Ью = 0.8F;
   private static final float ЬЩ = 0.02F;
   private static final float Ьу = 255.0F;
   private static final float Ь2 = 255.0F;
   private static final float Ьф = 1.33333F;
   private static final float Ьи = 0.5F;
   private static final float Ь9 = 0.5F;
   private static final double Ьо = 0.008;
   private static final long ЬO = 8L;
   private static final long Ь7 = 16L;
   private static final double ЬР = 0.0025;
   private static final float Ь3 = 1000.0F;
   private static final double Ьр = 0.9;
   private static final double Ь6 = 1.0E-8;
   private static final long Ь_ = 1000000L;
   private static final float ЬШ = 0.001F;
   private static final float Ьы = 0.5F;
   private static final float Ьй = 1.1F;
   private static final float ЬЖ = 3.0F;
   private static final float Ьм = 3.0F;
   private static final float ЬК = 0.35F;
   private static final float ЬI = 1.9F;
   private static final float ЬТ = 0.06F;
   private static final float ЬО = 0.94F;
   private static final float ЬФ = 0.024F;
   private static final float ЬЙ = 0.06F;
   private static final double Ьк = 1.0E-8;
   private static final float Ьг = 1.7F;
   private static final float Ь0 = 0.35F;
   private static final float Ьх = 1.4F;
   private static final float ЬУ = 1.2F;
   private static final float ЬЗ = 1.1F;
   private static final float ЬЕ = 0.7F;
   private static final float ЬП = 1.7F;
   private static final float Ь西 = 0.58F;
   private static final float Ьд = 1.05F;
   private static final double ЬБ = 0.5;
   private static final float ЬС = 0.1F;
   private static final float Ьз = 0.02F;
   private static final float Ьц = 0.6F;
   private static final float Ья = 0.4F;
   private static final float 衣с = (float) (Math.PI * 2);
   private static final float 衣В = 2.65F;
   private static final float 衣Я = (float) (Math.PI * 2);
   private static final float 衣Х = 1.45F;
   private static final float 衣е = 1.8F;
   private static final float 衣А = (float) (Math.PI * 2);
   private static final float 衣4 = 0.72F;
   private static final float 衣п = 0.46F;
   private static final float 衣Д = 0.14F;
   private static final float 衣Ъ = 0.42F;
   private static final float 衣т = 0.68F;
   private static final float 衣а = 0.08F;
   private static final double 衣Ю = 1.0E-8;
   private static final float 衣Ч = 0.28F;
   private static final float 衣Л = 0.72F;
   private static final float 衣诶 = (float) (Math.PI * 2);
   private static final float 衣1 = 0.86F;
   private static final float 衣Ь = 3.0F;
   private static final double 衣衣 = 1.0E-8;
   private static final double 衣ш = 1.0E-8;
   private static final double 衣И = 1.0E-6;
   private static final double 衣ь = 1.22;
   private static final double 衣ж = 0.5;
   private static final double 衣弟 = 0.92;
   private static final double 衣б = 1.0E-8;
   private static final double 衣Ы = 0.2;
   private static final double 衣l = 0.6;
   private static final double 衣8 = 0.2;
   private static final float 衣5 = 255.0F;
   private static final long 衣в = 1000000L;
   private static final float 衣ъ = 1000.0F;
   private static final float 衣М = 0.5F;
   private static final float 衣Н = 1.1F;
   private static final float 衣н = 3.0F;
   private static final float 衣Ц = 0.7F;
   private static final float 衣ч = 2.6F;
   private static final float 衣Э = 0.5F;
   private static final float 衣必 = 3.0F;
   private static final float 衣щ = 0.35F;
   private static final float 衣Г = 1.9F;
   private static final float 衣л = 4.0F;
   private static final String 衣э = "TrailsStyle";
   private static final float 衣ю = 5.5F;
   private static final float 衣Щ = 1.35F;
   private static final String 衣у = "TrailsStyle";
   private static final String 衣2 = "trails-glow-mask";
   private static final float 衣ф = -1.0F;
   private static final float 衣и = 0.02F;
   private static final float 衣9 = 255.0F;
   private static final float 衣о = 0.2F;
   private static final float 衣O = 1.05F;
   private static final float 衣7 = 0.12F;
   private static final float 衣Р = 0.28F;
   private static final double 衣3 = 1.55;
   private static final double 衣р = 0.2;
   private static final double 衣6 = 1.15;
   private static final float 衣_ = 0.95F;
   private static final float 衣Ш = 0.65F;
   private static final double 衣ы = 0.002;
   private static final double 衣й = 0.004;
   private static final double 衣Ж = 0.72;
   private static final double 衣м = 0.72;
   private static final double 衣К = 4.0;
   private static final double 衣I = 0.12;
   private static final double 衣Т = -1.0;
   private static final double 衣О = 1.0E-4;
   private static final float 衣Ф = 0.15F;
   private static final double 衣Й = 0.06;
   private static final double 衣к = 0.12;
   private static final double 衣г = 12.0;
   private static final double 衣0 = 4.0;
   private static final double 衣х = 0.2;
   private static final long 衣У = 350L;
   private static final float 衣З = 1000.0F;
   private static final double 衣Е = 8.0;
   private static final double 衣П = 0.1;
   private static final double 衣西 = 0.7;
   private static final double 衣д = 0.5;
   private static final double 衣Б = 0.22;
   private static final double 衣С = 0.12;
   private static final double 衣з = 0.14;
   private static final double 衣ц = 0.12;
   private static final double 衣я = 0.5;
   private static final double шс = 0.3;
   private static final double шВ = 0.5;
   private static final double шЯ = 0.14;
   private static final double шХ = 1.0E-4;
   private static final float ше = 0.42F;
   private static final float шА = 0.14F;
   private static final float ш4 = 0.08F;
   private static final float шп = 0.9F;
   private static final float шД = 0.1F;
   private static final double шЪ = 0.5;
   private static final double шт = 12.0;
   private static final String ша = "Sampler0";
   private static final double шЮ = 0.5;
   private static final double шЧ = 0.5;
   private static final float шЛ = 0.5F;
   private static final float ш诶 = -2.0F;
   private static final double ш1 = 2.0;
   private static final float шЬ = 360.0F;
   private static final float ш衣 = 180.0F;
   private static final float шш = 360.0F;
   private static final String шИ = "trails";
   private static final String шь = "trailsVertices";
   private static final String шж = "trailsCore";
   private static final String ш弟 = "trailsGlowMask";
   private static final String шб = "trailsStyleUniforms";
   private static final String шЫ = "trailsRibbonVertices";
   private static final float шl = 0.02F;
   private static final String ш8 = "Призраки";
   private static final String ш5 = "Призраки";
   private static final String шв = "Призраки";
   private static final String шъ = "Энергия";
   private static final String шМ = "Энергия";
   private static final String шН = "Энергия";
   private static final String шн = "Энергия";
   private static final String шЦ = "Энергия";
   private static final String шч = "Энергия";
   private static final String шЭ = "Рывок";
   private static final String ш必 = "Рывок";
   private static final String шщ = "Рывок";
   private static final String шГ = "Рывок";
   private static final String шл = "Рывок";
   private static final String шэ = "Рывок";
   private static final String шю = "Рывок";
   private static final String шЩ = "Рывок";
   private static final String шу = "Свой";
   private static final String ш2 = "Рывок";
   private static final String шф = "Рывок";
   private static final String ши = "Рывок";
   private static final String ш9 = "Рывок";
   private static final String шо = "Рывок";
   private static final String шO = "Дима";
   private static final String ш7 = "Дима";
   private static final String шР = "Дима";
   private static final String ш3 = "Дима";
   private static final String шр = "Дима";
   private static final String ш6 = "rose";
   private static final String ш_ = "images/particles/bloom.png";
   private static final String шШ = "rose";
   private static final String шы = "images/trails/waithporno.png";
   private static final String шй = "rose";
   private static final String шЖ = "images/dashtrail/dashbloomsample.png";
   private static final String шм = "targetesp/glow";

   private void ОТ(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.options.getCameraType().isFirstPerson()) {
         this.СУ();
      } else {
         long var2 = System.currentTimeMillis();
         float var4 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         Vec3 var5 = new Vec3(
            Mth.lerp(var4, MINECRAFT_CLIENT.player.xo, MINECRAFT_CLIENT.player.getX()), Mth.lerp(var4, MINECRAFT_CLIENT.player.yo, MINECRAFT_CLIENT.player.getY()), Mth.lerp(var4, MINECRAFT_CLIENT.player.zo, MINECRAFT_CLIENT.player.getZ())
         );
         Vec3 var6 = MINECRAFT_CLIENT.player.getDeltaMovement();
         double var7 = Math.sqrt(var6.x * var6.x + var6.z * var6.z);
         if (var7 > Ьо && var2 - this.lastEnergySampleMillis >= ЬO) {
            if (!this.energyTimedPositions.isEmpty() && this.energyTimedPositions.getLast().position.distanceToSqr(var5) > 1.0) {
               this.energyTimedPositions.clear();
            }

            if (this.energyTimedPositions.isEmpty()) {
               this.energyTimedPositions.add(new Trails.TimedPosition(new Vec3(MINECRAFT_CLIENT.player.xo, MINECRAFT_CLIENT.player.yo, MINECRAFT_CLIENT.player.zo), var2 - Ь7));
            }

            if (this.energyTimedPositions.getLast().position.distanceToSqr(var5) > ЬР) {
               this.energyTimedPositions.add(new Trails.TimedPosition(var5, var2));
               if (this.energyTimedPositions.size() > 220) {
                  this.energyTimedPositions.removeFirst();
               }
            } else {
               this.energyTimedPositions.set(this.energyTimedPositions.size() - 1, new Trails.TimedPosition(var5, var2));
            }

            this.lastEnergySampleMillis = var2;
         }

         long var9 = Math.round(this.energyTrailLengthSetting.getValue() * Ь3);
         this.energyTimedPositions.removeIf(var4x -> var2 - var4x.time > var9);
         if (this.energyTimedPositions.size() >= 2) {
            BufferBuilder var11 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.TRIANGLES, DefaultVertexFormat.POSITION_TEX_COLOR);
            Matrix4f var12 = var1.getPoses().last().pose();
            Vec3 var13 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
            int var14 = this.ъ(var11, var12, var13, var2, var9);
            this.ъ(var11, var14, var2);
         }
      }
   }

   private static void г(BufferBuilder var0, Matrix4f var1, Vec3 var2, float var3, float var4, int var5) {
      Vector4f var6 = ColorMath.toRgbaVector(var5);
      var0.addVertex(var1, (float)var2.x, (float)var2.y, (float)var2.z).setUv(var3, var4).setColor(var6.x, var6.y, var6.z, var6.w);
   }

   private static float 西(float var0, float var1) {
      float var2 = Math.abs(var0 - var1) % шЬ;
      return var2 > ш衣 ? шш - var2 : var2;
   }

   private void ОO(PlayerTickEvent var1) {
      if (!this.modeSetting.isSelected(field_1Р)) {
         this.Сч();
      }

      if (this.modeSetting.isSelected(field_13) && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         this.СД();

         for (Player var3 : MINECRAFT_CLIENT.level.players()) {
            if (this.必(var3)) {
               Vec3 var4 = var3.position();
               Vec3 var5 = this.lastDashPositionsByEntityId.get(var3.getId());
               this.lastDashPositionsByEntityId.put(var3.getId(), var4);
               if (var5 != null) {
                  double var6 = var4.x - var5.x;
                  double var8 = var4.y - var5.y;
                  double var10 = var4.z - var5.z;
                  double var12 = Math.sqrt(var6 * var6 + var10 * var10);
                  if (!(var12 < field_1р)) {
                     float var14 = this.П弟();
                     double var15 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
                     int var17 = Mth.clamp((int)(var15 / field_16 * var14), 1, (int)(field_1_ * var14));
                     boolean var18 = this.dashSegmentsSetting.getValue() || this.dashPointsSetting.getValue();

                     for (int var19 = 0; var19 < var17; var19++) {
                        float var20 = (float)var19 / var17;

                        while (this.dashGhostParticles.size() >= 220) {
                           this.dashGhostParticles.removeFirst();
                        }

                        this.dashGhostParticles.add(new Trails.GhostParticle(var3, var5, var4, var20, this.ъ8(), var18));
                     }
                  }
               }
            }
         }

         this.lastDashPositionsByEntityId.keySet().removeIf(var0 -> MINECRAFT_CLIENT.level.getEntity(var0) == null);

         for (Trails.GhostParticle var23 : this.dashGhostParticles) {
            if (var23.ъО() >= 1.0F && var23.targetOpacity != 0.0F) {
               var23.targetOpacity = 0.0F;
            }
         }

         this.dashGhostParticles.removeIf(var0 -> var0.ъО() >= 1.0F && var0.targetOpacity == 0.0F && var0.opacity < шl);
         boolean var22 = this.dashSmoothMovementSetting.getValue();
         Trails.GhostParticle var24 = null;

         for (Trails.GhostParticle var26 : this.dashGhostParticles) {
            if (var26.opacity > field_1Ш) {
               if (var24 != null) {
                  var24.ъ(var22 ? var26 : null);
               }

               var24 = var26;
            }
         }

         if (var24 != null) {
            var24.ъ(null);
         }
      }
   }

   private static int э(float var0) {
      return ColorMath.packArgb(255, 255, 255, Math.round(衣5 * Mth.clamp(var0, 0.0F, 1.0F)));
   }

   private void Сч() {
      this.ghostPathHistory.method_1О();
      this.ghostHistoryLevel = null;
   }

   @Override
   public void onEnable() {
      this.СЯ();
      this.СД();
   }

   private Vec3[] г(Vec3[] var1) {
      Vec3[] var2 = new Vec3[var1.length + 1];
      var2[0] = var1[0];
      var2[var2.length - 1] = var1[var1.length - 1];

      for (int var3 = 1; var3 < var2.length - 1; var3++) {
         Vec3 var4 = var1[var3 - 1].add(var1[var3]);
         if (var4.lengthSqr() < 衣И) {
            var2[var3] = var1[var3];
         } else {
            Vec3 var5 = var4.normalize();
            double var6 = Math.abs(var5.dot(var1[var3]));
            double var8 = Math.min(衣ь, 1.0 / Math.max(衣ж, var6));
            var2[var3] = var5.scale(var8);
         }
      }

      return var2;
   }

   private int ъ(
      BufferBuilder var1,
      Matrix4f var2,
      Vec3 var3,
      Vec3 var4,
      Vec3 var5,
      Vec3 var6,
      double var7,
      double var9,
      double var11,
      int var13,
      int var14,
      int var15,
      int var16
   ) {
      double var17 = var9 * шЮ;
      double var19 = var11 * шЧ;
      Vec3 var21 = var3.add(var6.scale(var7));
      Vec3 var22 = var21.add(var4.scale(-var17)).add(var5.scale(var19));
      Vec3 var23 = var21.add(var4.scale(var17)).add(var5.scale(var19));
      Vec3 var24 = var21.add(var4.scale(var17)).add(var5.scale(-var19));
      Vec3 var25 = var21.add(var4.scale(-var17)).add(var5.scale(-var19));
      this.П(var1, var2, var22, 0.0F, 0.0F, var13);
      this.П(var1, var2, var23, 1.0F, 0.0F, var14);
      this.П(var1, var2, var24, 1.0F, 1.0F, var15);
      this.П(var1, var2, var25, 0.0F, 1.0F, var16);
      return 4;
   }

   private void ОЕ(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.options.getCameraType().isFirstPerson()) {
         this.Сч();
      } else {
         if (this.ghostHistoryLevel != MINECRAFT_CLIENT.level) {
            this.Сч();
         }

         this.ghostHistoryLevel = MINECRAFT_CLIENT.level;
         int var2 = this.ghostTrailLengthSetting.getValue().intValue() * 30;
         float var3 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         double var4 = ((double)MINECRAFT_CLIENT.level.getGameTime() + var3) / field_1я;
         Vec3 var6 = new Vec3(
            Mth.lerp(var3, MINECRAFT_CLIENT.player.xo, MINECRAFT_CLIENT.player.getX()), Mth.lerp(var3, MINECRAFT_CLIENT.player.yo, MINECRAFT_CLIENT.player.getY()), Mth.lerp(var3, MINECRAFT_CLIENT.player.zo, MINECRAFT_CLIENT.player.getZ())
         );
         this.ghostPathHistory.西(var6, var4, var2);
         TrailPathHistory.TrailSnapshot var7 = this.ghostPathHistory.西(var2);
         Camera var8 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         Vec3 var9 = ъ(new Vector3f(1.0F, 0.0F, 0.0F).rotate(var8.rotation()));
         Vec3 var10 = ъ(new Vector3f(0.0F, 1.0F, 0.0F).rotate(var8.rotation()));
         Matrix4f var11 = var1.getPoses().last().pose();
         BufferBuilder var12 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         int var13 = 0;
         double var14 = var4 * Ьс * this.ghostSpeedSetting.getValue().floatValue();
         float var16 = ЬВ;
         int var17 = var7.points().size();
         float var18 = (float)Math.min(1.0, ЬЯ / var7.length());
         float var19 = this.ghostParticleSizeSetting.getValue() / ЬХ * Ье;

         for (int var20 = 0; var20 < 3; var16 = -var16) {
            for (int var21 = 0; var21 < var17; var21++) {
               TrailPathHistory.TimedPoint var22 = var7.points().get(var21);
               float var23 = (float)Mth.clamp((var4 - var22.time()) / ЬА, 0.0, 1.0);
               float var24 = 1.0F - var21 / (float)var7.length();
               float var25 = 1.0F - var23 * var23;
               int var26 = (int)(Ь4 * var24 * var25 * var18);
               if (var26 > 5) {
                  double var27 = var14 - var21 * Ьп;
                  double var29 = (Math.sin(var27) + 1.0) * ЬД;
                  double var31 = Math.toRadians(var20 * 90 + var27 * ЬЪ % Ьт);
                  Vec3 var33 = var22.position()
                     .subtract(var8.position())
                     .add(Math.cos(var31) * var16, var20 % 2 == 0 ? Ьа + ЬЮ * var29 : ЬЧ - ЬЛ * var29, Math.sin(var31) * var16);
                  float var34 = (Ь诶 + Ь1 * var24) * var25 * var19;
                  float var35 = (ЬЬ + Ь衣 * var24) * var25 * var19;
                  var33 = var33.add(-var34 / Ьш, -var34 / ЬИ, 0.0).add(var9.scale(var34 / Ьь)).add(var10.scale(var34 / 2.0F));
                  int var36 = Math.round(var26 * Ьж);
                  var13 += this.ъ(
                     var12,
                     var11,
                     var33,
                     var9,
                     var10,
                     Vec3.ZERO,
                     0.0,
                     var35,
                     var35,
                     this.ъ(var21 * 2, var36, var4),
                     this.ъ(var21 * 4, var36, var4),
                     this.ъ(var21 * 8, var36, var4),
                     this.ъ(var21 * 16, var36, var4)
                  );
                  var13 += this.ъ(
                     var12,
                     var11,
                     var33,
                     var9,
                     var10,
                     Vec3.ZERO,
                     0.0,
                     var34,
                     var34,
                     this.ъ(var21 * 2, var26, var4),
                     this.ъ(var21 * 4, var26, var4),
                     this.ъ(var21 * 8, var26, var4),
                     this.ъ(var21 * 16, var26, var4)
                  );
               }
            }

            var20++;
         }

         this.ъ(var12, сЮ, var13, this.ghostParticlePipeline);
      }
   }

   private void П(BufferBuilder var1, Matrix4f var2, Vec3 var3, float var4, float var5, int var6) {
      Vector4f var7 = ColorMath.toRgbaVector(var6);
      var1.addVertex(var2, (float)var3.x, (float)var3.y, (float)var3.z).setUv(var4, var5).setColor(var7.x, var7.y, var7.z, var7.w);
   }

   private float П2() {
      return switch (this.dashRenderQualitySetting.getSelectedOption().getName()) {
         case field_1К -> field_1Т;
         case field_1I -> field_1О;
         default -> 1.0F;
      };
   }

   private Vec3[] ъ(Vec3[] var1) {
      Vec3[] var2 = new Vec3[var1.length];
      Vec3 var3 = this.method_2(this.ъ(var1, 0));

      for (int var4 = 0; var4 < var1.length; var4++) {
         Vec3 var5 = this.ъ(var1, var4);
         Vec3 var6 = var3.subtract(var5.scale(var3.dot(var5)));
         if (var6.lengthSqr() < 衣ш) {
            var6 = this.method_2(var5);
         }

         var6 = var6.normalize();
         if (var6.dot(var3) < 0.0) {
            var6 = var6.reverse();
         }

         var2[var4] = var6;
         var3 = var6;
      }

      return var2;
   }

   private void С3() {
      long var1 = System.currentTimeMillis();
      this.dashTrails.removeIf(var2 -> var2.ю_bbeebd87(var1));
   }

   private void СЯ() {
      this.Сч();
      this.dashTrails.clear();
      this.dashGhostParticles.clear();
      this.lastDashPositionsByEntityId.clear();
      this.lastDashSpawnPosition = null;
      this.worldEffectsLevel = null;
      this.СУ();
   }

   private float ъ(Trails.TimedPosition var1, long var2, long var4) {
      return 1.0F - Mth.clamp((float)(var2 - var1.time) / (float)var4, 0.0F, 1.0F);
   }

   private void ъ(Vec3 var1, Vec3 var2, Vec3 var3, float var4, double var5) {
      while (this.dashTrails.size() >= 160) {
         this.dashTrails.removeFirst();
      }

      ThreadLocalRandom var7 = ThreadLocalRandom.current();
      long var8 = Math.max(衣У, (long)(this.dimaParticleLifetimeSetting.getValue() * 衣З));
      float var10 = this.dimaParticleScaleSetting.getValue();
      float var11 = Mth.clamp((float)(var5 * 衣Е), 0.0F, 1.0F);
      int var12 = 2 + var7.nextInt(2);

      for (int var13 = 0; var13 < var12; var13++) {
         if (this.dashTrails.size() >= 160) {
            this.dashTrails.removeFirst();
         }

         double var14 = (var7.nextDouble() - 衣П) * var4 * 衣西;
         double var16 = (var7.nextDouble() - 衣д) * (衣Б + var11 * 衣С);
         double var18 = var7.nextDouble() * (衣з + var11 * 衣ц);
         Vec3 var20 = var1.add(0.0, var14, 0.0).add(var3.scale(var16)).add(var2.scale(var18));
         Vec3 var21 = var2.add(var3.scale((var7.nextDouble() - 衣я) * шс)).add(0.0, (var7.nextDouble() - шВ) * шЯ, 0.0);
         var21 = var21.lengthSqr() < шХ ? var2 : var21.normalize();
         float var22 = var10 * (ше + var7.nextFloat() * шА + var11 * ш4);
         float var23 = var22 * (шп + var7.nextFloat() * шД);
         float var24 = (float)((var7.nextDouble() - шЪ) * шт);
         this.dashTrails.add(new Trails.DashTrail(var20, var21, var22, var23, var24, var8));
      }
   }

   private void Е(BufferBuilder var1, Identifier var2, int var3) {
      this.ъ(var1, var2, var3, this.dashTrailPipeline);
   }

   private static float Од(float var0) {
      var0 = Mth.clamp(var0, 0.0F, 1.0F);
      return var0 < шЛ ? 2.0F * var0 * var0 : 1.0F - (float)Math.pow(ш诶 * var0 + 2.0F, ш1) / 2.0F;
   }

   private int г(BufferBuilder var1, PoseStack var2, double var3, double var5, double var7, float var9, float var10, float var11, float var12, int var13) {
      float var14 = var9 * Ьи;
      float var15 = var10 * Ь9;
      var2.pushPose();
      var2.translate(var3, var5, var7);
      var2.mulPose(Axis.YP.rotationDegrees(-var11));
      var2.mulPose(Axis.XP.rotationDegrees(var12));
      Matrix4f var16 = var2.last().pose();
      Vector4f var17 = ColorMath.toRgbaVector(var13);
      var1.addVertex(var16, -var14, -var15, 0.0F).setUv(0.0F, 0.0F).setColor(var17.x, var17.y, var17.z, var17.w);
      var1.addVertex(var16, -var14, var15, 0.0F).setUv(0.0F, 1.0F).setColor(var17.x, var17.y, var17.z, var17.w);
      var1.addVertex(var16, var14, var15, 0.0F).setUv(1.0F, 1.0F).setColor(var17.x, var17.y, var17.z, var17.w);
      var1.addVertex(var16, var14, -var15, 0.0F).setUv(1.0F, 0.0F).setColor(var17.x, var17.y, var17.z, var17.w);
      var2.popPose();
      return 4;
   }

   private int ъl() {
      return switch (this.dashColorModeSetting.getSelectedOption().getName()) {
         case field_10 -> this.dashSelectedColorSetting.getValue();
         case field_1х -> Color.getHSBColor(this.random.nextInt(361) / field_1З, 1.0F, 1.0F).getRGB() | field_1Е;
         case field_1У -> Color.getHSBColor((float)(System.currentTimeMillis() % field_1П) / field_1西, field_1д, 1.0F).getRGB() | field_1Б;
         default -> ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      };
   }

   private void СУ() {
      this.energyTimedPositions.clear();
      this.lastEnergySampleMillis = 0L;
   }

   private static Vec3 ъ(Vector3f var0) {
      return new Vec3(var0.x(), var0.y(), var0.z());
   }

   private int ь(int var1, float var2) {
      var2 = Mth.clamp(var2, 0.0F, 1.0F);
      return ColorMath.packArgb(
         (int)(ColorMath.getRed(var1) * var2), (int)(ColorMath.getGreen(var1) * var2), (int)(ColorMath.getBlue(var1) * var2), ColorMath.getAlpha(var1)
      );
   }

   private void О弟(WorldEffectsEvent var1) {
      if (!MINECRAFT_CLIENT.options.getCameraType().isFirstPerson()) {
         float var2 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         Vec3 var3 = new Vec3(
            Mth.lerp(var2, MINECRAFT_CLIENT.player.xo, MINECRAFT_CLIENT.player.getX()), Mth.lerp(var2, MINECRAFT_CLIENT.player.yo, MINECRAFT_CLIENT.player.getY()), Mth.lerp(var2, MINECRAFT_CLIENT.player.zo, MINECRAFT_CLIENT.player.getZ())
         );
         this.ь(var3);
         this.С3();
         if (!this.dashTrails.isEmpty()) {
            Camera var4 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
            Vec3 var5 = var4.position();
            Vec3 var6 = ъ(new Vector3f(0.0F, 1.0F, 0.0F).rotate(var4.rotation())).normalize();
            Vec3 var7 = ъ(new Vector3f(衣ф, 0.0F, 0.0F).rotate(var4.rotation())).normalize();
            Vec3 var8 = ъ(new Vector3f(0.0F, 0.0F, 1.0F).rotate(var4.rotation())).normalize();
            Matrix4f var9 = var1.getPoses().last().pose();
            int var10 = this.dimaThemeColorSetting.getValue() ? ColorMath.getAnimatedThemeColor(0) : this.dimaCustomColorSetting.getValue();
            int var11 = ColorMath.getRed(var10);
            int var12 = ColorMath.getGreen(var10);
            int var13 = ColorMath.getBlue(var10);
            long var14 = System.currentTimeMillis();
            this.ъ(var9, var5, var7, var6, var8, var14, var11, var12, var13, true);
            this.ъ(var9, var5, var7, var6, var8, var14, var11, var12, var13, false);
         }
      }
   }

   private Vec3 ъ(Vec3[] var1, int var2) {
      Vec3 var3 = var1[Math.max(0, var2 - 1)];
      Vec3 var4 = var1[Math.min(var1.length - 1, var2 + 1)];
      Vec3 var5 = var4.subtract(var3);
      return var5.lengthSqr() < 衣衣 ? new Vec3(0.0, 0.0, 1.0) : var5.normalize();
   }

   private void Ощ(WorldEffectsEvent var1) {
      this.СД();
      if (!this.dashGhostParticles.isEmpty()) {
         float var2 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         Camera var3 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         Vec3 var4 = var3.position();
         PoseStack var5 = var1.getPoses();
         float var6 = this.П2();
         float var7 = this.dashLightingSetting.getValue() ? 1.0F : 0.0F;
         boolean var8 = this.dashPointsSetting.getValue() || this.dashSegmentsSetting.getValue();
         ArrayList<Trails.GhostParticle> var9 = new ArrayList<>();

         for (Trails.GhostParticle var11 : this.dashGhostParticles) {
            var11.ВО();
            if (var11.opacity >= Ьв && var11.animatedTexture.ш() != null) {
               var9.add(var11);
            }
         }

         if (!var9.isEmpty()) {
            HashMap<Identifier, List<Trails.GhostParticle>> var32 = new HashMap<>();

            for (Trails.GhostParticle var12 : var9) {
               var32.computeIfAbsent(var12.animatedTexture.ш().id, var0 -> new ArrayList<>()).add(var12);
            }

            for (Entry<Identifier, List<Trails.GhostParticle>> var36 : var32.entrySet()) {
               BufferBuilder var13 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
               int var14 = 0;

               for (Trails.GhostParticle var16 : var36.getValue()) {
                  Trails.TextureFrame var17 = var16.animatedTexture.ш();
                  if (var17 != null) {
                     float var18 = var16.opacity;
                     double var19 = var16.е(var2) - var4.x;
                     double var21 = var16.Л(var2) - var4.y;
                     double var23 = var16.O(var2) - var4.z;
                     float var25 = Ьъ * var18 * var6 * ЬМ;
                     float var26 = var17.w * var25;
                     float var27 = var17.h * var25;
                     int var28 = ColorMath.lerpArgb(var16.colorArgb, -1, ЬН);
                     var28 = ColorMath.withAlpha(this.ь(var28, 1.0F), var18);
                     var14 += this.г(var13, var5, var19, var21, var23, var26, var27, var16.billboardAngles[0], var16.billboardAngles[1], var28);
                  }
               }

               this.Е(var13, (Identifier)var36.getKey(), var14);
            }

            BufferBuilder var35 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            int var37 = 0;

            for (Trails.GhostParticle var40 : var9) {
               Trails.TextureFrame var43 = var40.animatedTexture.ш();
               if (var43 != null) {
                  float var45 = var40.opacity;
                  double var47 = var40.е(var2) - var4.x;
                  double var50 = var40.Л(var2) - var4.y;
                  double var51 = var40.O(var2) - var4.z;
                  float var52 = Ьн * var45 * var6 * ЬЦ;
                  float var24 = var43.w * var52;
                  float var54 = var43.h * var52;
                  float var56 = (float)Math.sqrt(var24 * var24 + var54 * var54);
                  float var58 = 1.0F - var40.ъО();
                  int var61 = ColorMath.lerpArgb(var40.colorArgb, -1, Ьч);
                  var37 += this.г(
                     var35, var5, var47, var50, var51, var56 / ЬЭ * 2.0F, var56 / Ь必 * 2.0F, var3.yRot(), var3.xRot(), ColorMath.withAlpha(var61, Ьщ * var45)
                  );
                  if (var7 > 0.0F) {
                     float var29 = var45 * var7;
                     float var30 = var56 * (1.0F + ЬГ * var58 * var29);
                     int var31 = ColorMath.withAlpha(this.ь(var61, var29 / Ьл), Math.min(var29 / 2.0F / var6, 1.0F) * Ьэ);
                     var37 += this.г(var35, var5, var47, var50, var51, var30, var30, var3.yRot(), var3.xRot(), var31);
                  }
               }
            }

            this.Е(var35, са, var37);
            if (var8) {
               BufferBuilder var39 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
               int var41 = 0;

               for (Trails.GhostParticle var46 : var9) {
                  if (!var46.sparks.isEmpty()) {
                     float var48 = var46.opacity;
                     double var49 = var46.е(var2) - var4.x;
                     double var20 = var46.Л(var2) - var4.y;
                     double var22 = var46.O(var2) - var4.z;
                     float var53 = this.ПС() * (Ью + var48);

                     for (Trails.SparkParticle var57 : var46.sparks) {
                        float var59 = Од((float)(var57.т() * var48));
                        if (!(var59 < ЬЩ)) {
                           int var62 = ColorMath.lerpArgb(var46.colorArgb, ColorMath.withAlpha(-1, ColorMath.getAlpha(var46.colorArgb) / Ьу), var59);
                           var62 = ColorMath.withAlpha(var62, ColorMath.getAlpha(var62) / Ь2 * var59 / Ьф);
                           var41 += this.г(
                              var39, var5, var49 + var57.衣(var2), var20 + var57.с(var2), var22 + var57.I(var2), var53, var53, var3.yRot(), var3.xRot(), var62
                           );
                           var41 += this.г(
                              var39, var5, var49 - var57.衣(var2), var20 - var57.с(var2), var22 - var57.I(var2), var53, var53, var3.yRot(), var3.xRot(), var62
                           );
                        }
                     }
                  }
               }

               this.Е(var39, са, var41);
            }
         }
      }
   }

   private int ъ8() {
      return (int)((550 + this.random.nextInt(300)) * this.dashTrailLengthSetting.getValue());
   }

   private int ъ(BufferBuilder var1, Matrix4f var2, Vec3 var3, Vec3[][] var4, float var5, long var6, long var8, float var10) {
      int var11 = 0;
      int var12 = var4.length;
      int var13 = var4[0].length;

      for (int var14 = 1; var14 < var12; var14++) {
         float var15 = this.ъ(this.energyTimedPositions.get(var14 - 1), var6, var8);
         float var16 = this.ъ(this.energyTimedPositions.get(var14), var6, var8);
         float var17 = 1.0F - var15;
         float var18 = 1.0F - var16;
         int var19 = э(var15 * var10);
         int var20 = э(var16 * var10);

         for (int var21 = 0; var21 < var13; var21++) {
            int var22 = (var21 + 1) % var13;
            Vec3 var23 = var4[var14 - 1][var21].subtract(var3);
            Vec3 var24 = var4[var14 - 1][var22].subtract(var3);
            Vec3 var25 = var4[var14][var21].subtract(var3);
            Vec3 var26 = var4[var14][var22].subtract(var3);
            г(var1, var2, var23, var17, var5, var19);
            г(var1, var2, var24, var17, var5, var19);
            г(var1, var2, var26, var18, var5, var20);
            г(var1, var2, var23, var17, var5, var19);
            г(var1, var2, var26, var18, var5, var20);
            г(var1, var2, var25, var18, var5, var20);
            var11 += 6;
         }
      }

      return var11;
   }

   private boolean 必(Player var1) {
      boolean var2 = var1 == MINECRAFT_CLIENT.player;
      if (var2) {
         return !this.dashSelfSetting.getValue() ? false : this.dashFirstPersonSetting.getValue() || !MINECRAFT_CLIENT.options.getCameraType().isFirstPerson();
      }

      double var3 = MINECRAFT_CLIENT.player.distanceTo(var1);
      if (var3 > this.dashMaxDistanceSetting.getValue().floatValue()) {
         return false;
      }

      boolean var5 = RoseClient.getInstance().getServices().getFriends().О(var1.getGameProfile().name());
      return var5 ? this.dashFriendsSetting.getValue() : this.dashPlayersSetting.getValue();
   }

   private float П弟() {
      return switch (this.dashRenderQualitySetting.getSelectedOption().getName()) {
         case field_1ы -> field_1Ж;
         case field_1й -> field_1м;
         default -> 1.0F;
      };
   }

   private float т(float var1, float var2, float var3) {
      float var4 = Mth.clamp((var3 - var1) / (var2 - var1), 0.0F, 1.0F);
      return var4 * var4 * (衣Ь - 2.0F * var4);
   }

   private void СД() {
      if (!this.dashTexturesLoaded) {
         this.dashTexturesLoaded = true;
         this.dashCubicFrames.clear();
         this.dashAnimatedFrames.clear();

         for (int var1 = 1; var1 <= 21; var1++) {
            this.dashCubicFrames.add(this.ъ(Identifier.fromNamespaceAndPath(field_1O, "images/dashtrail/dashcubics/dashcubic" + var1 + ".png")));
         }

         for (int var5 = 0; var5 < Щ.length; var5++) {
            ArrayList var2 = new ArrayList();
            int var3 = Щ[var5];

            for (int var4 = 1; var4 <= var3; var4++) {
               var2.add(this.ъ(Identifier.fromNamespaceAndPath(field_17, "images/dashtrail/group_dashs/group" + (var5 + 1) + "/dashcubic" + var4 + ".png")));
            }

            this.dashAnimatedFrames.add(var2);
         }
      }
   }

   private void Д(int var1, int var2) {
      if (this.energyGlowTarget == null) {
         this.energyGlowTarget = new TextureTarget(衣2, var1, var2, true, GpuFormat.RGBA8_UNORM);
      } else if (this.energyGlowTarget.width != var1 || this.energyGlowTarget.height != var2) {
         this.energyGlowTarget.resize(var1, var2);
      }
   }

   private float ПС() {
      float var1 = field_1Ф;

      return switch (this.dashRenderQualitySetting.getSelectedOption().getName()) {
         case field_1Й -> var1 * 2.0F;
         case field_1к -> var1 * field_1г;
         default -> var1;
      };
   }

   public Trails() {
      super(诶ю, 诶Щ, ModuleCategory.RENDER);
      this.modeSetting = new SingleChoiceSetting(诶у, new ChoiceOption(诶2), new ChoiceOption(诶ф), new ChoiceOption(诶и), new ChoiceOption(诶9));
      this.dimaParticleLifetimeSetting = new FloatSetting(诶о, 诶O, 诶7, 诶Р).withStep(诶3).visibleWhen(() -> this.modeSetting.isSelected(шр));
      this.dimaParticleDensitySetting = new FloatSetting(诶р, 诶6, 诶_, 诶Ш).withStep(诶ы).visibleWhen(() -> this.modeSetting.isSelected(ш3));
      this.dimaParticleScaleSetting = new FloatSetting(诶й, 1.0F, 诶Ж, 诶м).withStep(诶К).visibleWhen(() -> this.modeSetting.isSelected(шР));
      this.dimaThemeColorSetting = new BooleanSetting(诶I, true).visibleWhen(() -> this.modeSetting.isSelected(ш7));
      this.dimaCustomColorSetting = new ColorSetting(诶Т, false, 诶О).visibleWhen(() -> this.modeSetting.isSelected(шO) && !this.dimaThemeColorSetting.getValue());
      this.dashSelfSetting = new BooleanSetting(诶Ф, true).visibleWhen(() -> this.modeSetting.isSelected(шо));
      this.dashFirstPersonSetting = new BooleanSetting(诶Й, true).visibleWhen(() -> this.modeSetting.isSelected(ш9) && this.dashSelfSetting.getValue());
      this.dashPlayersSetting = new BooleanSetting(诶к, false).visibleWhen(() -> this.modeSetting.isSelected(ши));
      this.dashFriendsSetting = new BooleanSetting(诶г, true).visibleWhen(() -> this.modeSetting.isSelected(шф));
      this.dashColorModeSetting = new SingleChoiceSetting(诶0, 0, new ChoiceOption(诶х), new ChoiceOption(诶У), new ChoiceOption(诶З), new ChoiceOption(诶Е)).visibleWhen(() -> this.modeSetting.isSelected(ш2));
      this.dashSelectedColorSetting = new ColorSetting(诶П, false, ColorMath.packArgb(170, 40, 255, 诶西)).visibleWhen(() -> this.modeSetting.isSelected(шЩ) && this.dashColorModeSetting.isSelected(шу));
      this.dashMaxDistanceSetting = new FloatSetting(诶д, 诶Б, 诶С, 诶з).withStep(1.0F).visibleWhen(() -> this.modeSetting.isSelected(шю) && (this.dashPlayersSetting.getValue() || this.dashFriendsSetting.getValue()));
      this.dashSmoothMovementSetting = new BooleanSetting(诶ц, false).visibleWhen(() -> this.modeSetting.isSelected(шэ));
      this.dashSegmentsSetting = new BooleanSetting(诶я, false).visibleWhen(() -> this.modeSetting.isSelected(шл));
      this.dashPointsSetting = new BooleanSetting(field_1с, true).visibleWhen(() -> this.modeSetting.isSelected(шГ));
      this.dashLightingSetting = new BooleanSetting(field_1В, true).visibleWhen(() -> this.modeSetting.isSelected(шщ));
      this.dashTrailLengthSetting = new FloatSetting(field_1Я, field_1Х, field_1е, field_1А).withStep(field_14).visibleWhen(() -> this.modeSetting.isSelected(ш必));
      this.dashRenderQualitySetting = new SingleChoiceSetting(field_1п, 1, new ChoiceOption(field_1Д), new ChoiceOption(field_1Ъ), new ChoiceOption(field_1т)).visibleWhen(() -> this.modeSetting.isSelected(шЭ));
      this.energyTrailLengthSetting = new FloatSetting(field_1а, field_1Ю, field_1Ч, 2.0F).withStep(field_1Л).visibleWhen(() -> this.modeSetting.isSelected(шч));
      this.energyTrailWidthSetting = new FloatSetting(field_1诶, field_11, field_1Ь, field_1衣).withStep(field_1ш).visibleWhen(() -> this.modeSetting.isSelected(шЦ));
      this.energyAutoThemeColorSetting = new BooleanSetting(field_1И, true).visibleWhen(() -> this.modeSetting.isSelected(шн));
      this.energyFirstColorSetting = new ColorSetting(field_1ь, false, field_1ж).visibleWhen(() -> this.modeSetting.isSelected(шН) && !this.energyAutoThemeColorSetting.getValue());
      this.energySecondColorSetting = new ColorSetting(field_1弟, false, field_1б).visibleWhen(() -> this.modeSetting.isSelected(шМ) && !this.energyAutoThemeColorSetting.getValue());
      this.energyThirdColorSetting = new ColorSetting(field_1Ы, false, field_1l).visibleWhen(() -> this.modeSetting.isSelected(шъ) && !this.energyAutoThemeColorSetting.getValue());
      this.ghostTrailLengthSetting = new FloatSetting(field_18, field_15, field_1в, field_1ъ).withStep(1.0F).visibleWhen(() -> this.modeSetting.isSelected(шв));
      this.ghostParticleSizeSetting = new FloatSetting(field_1М, field_1Н, field_1н, field_1Ц).withStep(field_1ч).visibleWhen(() -> this.modeSetting.isSelected(ш5));
      this.ghostSpeedSetting = new FloatSetting(field_1Э, field_1必, 1.0F, field_1щ).withStep(field_1Г).visibleWhen(() -> this.modeSetting.isSelected(ш8));
      this.ghostPathHistory = new TrailPathHistory();
      this.ghostParticlePipeline = GlowQuadRendering.Г(field_1л, CompareOp.ALWAYS_PASS);
      this.dashTrails = new ArrayList<>();
      this.dashGhostParticles = new ArrayList<>();
      this.lastDashPositionsByEntityId = new HashMap<>();
      this.dashCubicFrames = new ArrayList<>();
      this.dashAnimatedFrames = new ArrayList<>();
      this.energyTimedPositions = new ArrayList<>();
      this.random = new Random(field_1э);
      this.dashTrailPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(field_1ю)
         .withVertexShader(ClientResourceIds.getCoreId(field_1Щ))
         .withFragmentShader(ClientResourceIds.getCoreId(field_1у))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(field_12)
         .build();
      this.energyRibbonPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(field_1ф)
         .withVertexShader(ClientResourceIds.getCoreId(field_1и))
         .withFragmentShader(ClientResourceIds.getCoreId(field_19))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.TRIANGLES)
         .withUniform(field_1о, UniformType.UNIFORM_BUFFER)
         .build();
      this.playerTickListener = this::ОO;
      this.worldEffectsListener = this::ОI;
      this.addSettings(
         this.modeSetting,
         this.dimaParticleLifetimeSetting,
         this.dimaParticleDensitySetting,
         this.dimaParticleScaleSetting,
         this.dimaThemeColorSetting,
         this.dimaCustomColorSetting,
         this.dashSelfSetting,
         this.dashFirstPersonSetting,
         this.dashPlayersSetting,
         this.dashFriendsSetting,
         this.dashColorModeSetting,
         this.dashSelectedColorSetting,
         this.dashMaxDistanceSetting,
         this.dashSmoothMovementSetting,
         this.dashSegmentsSetting,
         this.dashPointsSetting,
         this.dashLightingSetting,
         this.dashTrailLengthSetting,
         this.dashRenderQualitySetting,
         this.energyTrailLengthSetting,
         this.energyTrailWidthSetting,
         this.energyAutoThemeColorSetting,
         this.energyFirstColorSetting,
         this.energySecondColorSetting,
         this.energyThirdColorSetting,
         this.ghostTrailLengthSetting,
         this.ghostParticleSizeSetting,
         this.ghostSpeedSetting
      );
   }

   private int ъ(BufferBuilder var1, Matrix4f var2, Vec3 var3, long var4, long var6) {
      int var8 = this.energyTimedPositions.size();
      if (var8 < 2) {
         return 0;
      }

      Vec3[] var9 = new Vec3[var8];

      for (int var10 = 0; var10 < var8; var10++) {
         var9[var10] = this.г(var10).add(0.0, Ьр, 0.0);
      }

      Vec3[] var52 = new Vec3[var8 - 1];
      Vec3 var11 = null;

      for (int var12 = 0; var12 < var52.length; var12++) {
         Vec3 var13 = var9[var12 + 1].subtract(var9[var12]);
         if (var13.lengthSqr() < Ь6) {
            var52[var12] = var11 == null ? new Vec3(1.0, 0.0, 0.0) : var11;
         } else {
            Vec3 var14 = this.method_2(var13.normalize());
            if (var11 != null && var14.dot(var11) < 0.0) {
               var14 = var14.reverse();
            }

            var52[var12] = var14;
            var11 = var14;
         }
      }

      Vec3[] var53 = this.г(var52);
      int var54 = 0;
      byte var55 = 4;
      byte var15 = 7;
      float var16 = (float)(var4 % Ь_) * ЬШ;
      float var17 = Ьы;
      float var18 = Ьй;
      float var19 = ЬЖ;
      float var20 = Ьм;
      float var21 = ЬК;
      float var22 = ЬI;
      float var23 = ЬТ + var21 * ЬО;
      float var24 = ЬФ + this.energyTrailWidthSetting.getValue() * ЬЙ;

      for (int var25 = 0; var25 < var55; var25++) {
         Vec3[] var26 = new Vec3[var8];

         for (int var27 = 0; var27 < var8; var27++) {
            float var28 = 1.0F - this.ъ(this.energyTimedPositions.get(var27), var4, var6);
            Vec3 var29 = this.ъ(var9, var27);
            Vec3 var30 = var53[var27].normalize();
            Vec3 var31 = var30.cross(var29);
            if (var31.lengthSqr() < Ьк) {
               var31 = new Vec3(0.0, 1.0, 0.0);
            }

            var31 = var31.normalize();
            float var32 = var25;
            float var33 = var28 * 2.0F / var22;
            float var34 = var32 * Ьг * var20;
            float var35 = (2.0F + var32 * Ь0) * var19;
            float var36 = Ьх + var32 * ЬУ;
            float var37 = var16 * var17;
            float var38 = var33 * var35 + var37 * var36 + var34;
            float var39 = var33 * var35 * ЬЗ - var37 * var36 * ЬЕ + var34 * ЬП;
            float var40 = (float)Math.pow(Math.max(1.0F - this.т(Ь西, Ьд, var33), 0.0F), ЬБ);
            float var41 = (ЬС + Ьз * var23) * var40 * var18;
            float var42 = Mth.sin(var38) * Ьц + Mth.sin(var39) * Ья;
            float var43 = this.energyTrailWidthSetting.getValue() * 2.0F * var22;
            float var44 = var25 * 衣с / var55;
            float var45 = var16 * 衣В + var33 * 衣Я * 衣Х + var44;
            float var46 = var16 * 衣е - var33 * 衣А * 衣4 + var44 * 2.0F;
            float var47 = this.energyTrailWidthSetting.getValue() * 衣п * var40;
            float var48 = Mth.sin(var46) * this.energyTrailWidthSetting.getValue() * 衣Д * var40;
            float var49 = Mth.sin(var45) * var47 + var48 + var42 * var41 * var43 * 衣Ъ;
            float var50 = Mth.cos(var45) * var47 * 衣т + Mth.cos(var46) * this.energyTrailWidthSetting.getValue() * 衣а * var40;
            Vec3 var51 = var30.scale(var49).add(var31.scale(var50));
            var26[var27] = var9[var27].add(var51);
         }

         Vec3[] var56 = this.ъ(var26);
         Vec3[][] var57 = new Vec3[var8][var15];

         for (int var58 = 0; var58 < var8; var58++) {
            Vec3 var60 = this.ъ(var26, var58);
            Vec3 var62 = var56[var58];
            Vec3 var63 = var62.cross(var60);
            if (var63.lengthSqr() < 衣Ю) {
               var63 = new Vec3(0.0, 1.0, 0.0);
            }

            var63 = var63.normalize();
            float var65 = this.ъ(this.energyTimedPositions.get(var58), var4, var6);
            float var66 = var24 * (衣Ч + var65 * 衣Л);

            for (int var67 = 0; var67 < var15; var67++) {
               float var68 = 衣诶 * var67 / var15;
               Vec3 var69 = var62.scale(Mth.cos(var68)).add(var63.scale(Mth.sin(var68)));
               var57[var58][var67] = var26[var58].add(var69.scale(var66));
            }
         }

         float var59 = (float)var25 / var55;
         var54 += this.ъ(var1, var2, var3, var57, var59, var4, var6, 衣1);
      }

      return var54;
   }

   private void ъ(BufferBuilder var1, Identifier var2, int var3, RenderPipeline var4) {
      MeshData var5 = var1.build();
      if (var5 != null) {
         if (var3 == 0) {
            var5.close();
         } else {
            GpuBuffer var6 = null;

            try {
               DrawState var7 = var5.drawState();
               int var8 = var7.indexCount();
               AutoStorageIndexBuffer var9 = RenderSystem.getSequentialBuffer(var7.primitiveTopology());
               GpuBuffer var10 = var9.getBuffer(var8);
               IndexType var11 = var9.type();
               var6 = RenderSystem.getDevice().createBuffer(() -> шь, 32, var5.vertexBuffer());
               RenderTarget var12 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder var13 = RenderSystem.getDevice().createCommandEncoder();
               AbstractTexture var14 = MINECRAFT_CLIENT.getTextureManager().getTexture(var2);

               try (RenderPass var15 = var13.createRenderPass(
                     () -> шИ, var12.getColorTextureView(), Optional.empty(), var12.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var15);
                  var15.setPipeline(var4);
                  var15.bindTexture(ша, var14.getTextureView(), var14.getSampler());
                  var15.setVertexBuffer(0, var6.slice());
                  var15.setIndexBuffer(var10, var11);
                  var15.drawIndexed(var8, 1, 0, 0, 0);
               }
            } finally {
               if (var6 != null) {
                  var6.close();
               }

               var5.close();
            }
         }
      }
   }

   private void ъ(BufferBuilder var1, int var2, long var3) {
      MeshData var5 = var1.build();
      if (var5 != null) {
         if (var2 == 0) {
            var5.close();
         } else {
            GpuBuffer var6 = null;
            GpuBuffer var7 = null;

            try {
               DrawState var8 = var5.drawState();
               int var9 = var8.indexCount();
               AutoStorageIndexBuffer var10 = RenderSystem.getSequentialBuffer(var8.primitiveTopology());
               GpuBuffer var11 = var10.getBuffer(var9);
               IndexType var12 = var10.type();
               var6 = RenderSystem.getDevice().createBuffer(() -> шЫ, 32, var5.vertexBuffer());
               RenderTarget var13 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               this.Д(var13.width, var13.height);
               CommandEncoder var14 = RenderSystem.getDevice().createCommandEncoder();
               int var15 = this.energyAutoThemeColorSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.energyFirstColorSetting.getValue();
               int var16 = this.energyAutoThemeColorSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO) : this.energySecondColorSetting.getValue();
               int var17 = this.energyAutoThemeColorSetting.getValue() ? ColorMath.getAnimatedThemeColor(0) : this.energyThirdColorSetting.getValue();

               try (MemoryStack var18 = MemoryStack.stackPush()) {
                  ByteBuffer var19 = Std140Builder.onStack(var18, 112)
                     .putVec4(ColorMath.toRgbaVector(var15))
                     .putVec4(ColorMath.toRgbaVector(var16))
                     .putVec4(ColorMath.toRgbaVector(var17))
                     .putVec4((float)(var3 % 衣в) / 衣ъ, 衣М, 衣Н, 衣н)
                     .putVec4(衣Ц, 衣ч, 衣Э, 衣必)
                     .putVec4(0.0F, 衣щ, 1.0F, 衣Г)
                     .putVec4(2.0F, 衣л, 0.0F, 0.0F)
                     .get();
                  var7 = RenderSystem.getDevice().createBuffer(() -> шб, 128, var19);
               }

               try (RenderPass var36 = var14.createRenderPass(
                     () -> ш弟, this.energyGlowTarget.getColorTextureView(), Optional.of(new Vector4f(0.0F)), var13.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var36);
                  var36.setUniform(衣э, var7);
                  var36.setPipeline(this.energyRibbonPipeline);
                  var36.setVertexBuffer(0, var6.slice());
                  var36.setIndexBuffer(var11, var12);
                  var36.drawIndexed(var9, 1, 0, 0, 0);
               }

               EntityEspOuterGlowRenderer.compositeOuterGlow(this.energyGlowTarget, var13, var15, 衣ю, 2.0F, 衣Щ);

               try (RenderPass var37 = var14.createRenderPass(
                     () -> шж, var13.getColorTextureView(), Optional.empty(), var13.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var37);
                  var37.setUniform(衣у, var7);
                  var37.setPipeline(this.energyRibbonPipeline);
                  var37.setVertexBuffer(0, var6.slice());
                  var37.setIndexBuffer(var11, var12);
                  var37.drawIndexed(var9, 1, 0, 0, 0);
               }
            } finally {
               if (var6 != null) {
                  var6.close();
               }

               if (var7 != null) {
                  var7.close();
               }

               var5.close();
            }
         }
      }
   }

   private void ъ(Matrix4f var1, Vec3 var2, Vec3 var3, Vec3 var4, Vec3 var5, long var6, int var8, int var9, int var10, boolean var11) {
      BufferBuilder var12 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int var13 = 0;

      for (Trails.DashTrail var15 : this.dashTrails) {
         float var16 = var15.ю_f67ab10a(var6);
         float var17 = var15.Ф(var6);
         if (!(var17 <= 衣и)) {
            int var18 = Mth.clamp((int)(衣9 * var17), 0, 255);
            if (var18 > 4) {
               Vec3 var19 = var15.position.subtract(var2);
               float var20 = (float)var15.direction.dot(var3);
               float var21 = (float)var15.direction.dot(var4);
               float var22 = (float)Math.toDegrees(Math.atan2(var21, var20)) + var15.rotationOffsetDegrees;
               double var23 = Math.toRadians(var22);
               double var25 = Math.cos(var23);
               double var27 = Math.sin(var23);
               Vec3 var29 = var3.scale(var25).add(var4.scale(-var27));
               Vec3 var30 = var3.scale(var27).add(var4.scale(var25));
               float var31 = 1.0F - var16 * 衣о;
               double var32 = var15.primaryRadius * var31;
               double var34 = var15.secondaryRadius * (衣O - var16 * 衣7);
               if (var11) {
                  int var36 = ColorMath.packArgb(var8, var9, var10, Mth.clamp((int)(var18 * 衣Р), 0, 255));
                  double var37 = var32 * (衣3 - var16 * 衣р);
                  var13 += this.ъ(var12, var1, var19, var29, var30, var5, 0.0, var37, var37, var36, var36, var36, var36);
               } else {
                  double var41 = Math.max(var32, var34) * 衣6;
                  int var38 = ColorMath.packArgb(var8, var9, var10, Mth.clamp((int)(var18 * 衣_), 0, 255));
                  int var39 = ColorMath.packArgb(255, 255, 255, Mth.clamp((int)(var18 * 衣Ш), 0, 255));
                  var13 += this.ъ(var12, var1, var19, var29, var30, var5, 衣ы, var41, var41, var38, var38, var38, var38);
                  var13 += this.ъ(var12, var1, var19, var29, var30, var5, 衣й, var41 * 衣Ж, var41 * 衣м, var39, var39, var39, var39);
               }
            }
         }
      }

      this.Е(var12, var11 ? сЪ : ст, var13);
   }

   private int ъ(int var1, int var2, double var3) {
      double var5 = (var3 * Ь弟 + var1) % Ьб;
      float var7 = (float)((var5 >= ЬЫ ? Ьl - var5 : var5) / Ь8);
      int var8 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      int var9 = ColorMath.lerpArgb(var8, this.ь(var8, Ь5), var7);
      return ColorMath.packArgb(ColorMath.getRed(var9), ColorMath.getGreen(var9), ColorMath.getBlue(var9), var2);
   }

   private Trails.TextureFrame ъ(Identifier var1) {
      int var2 = 48;
      int var3 = 48;

      try {
         Optional var4 = MINECRAFT_CLIENT.getResourceManager().getResource(var1);
         if (var4.isPresent()) {
            try (
               InputStream var5 = ((Resource)var4.get()).open();
               NativeImage var6 = NativeImage.read(var5);
            ) {
               var2 = var6.getWidth();
               var3 = var6.getHeight();
            }
         }
      } catch (Exception var13) {
      }

      return new Trails.TextureFrame(var1, var2, var3);
   }

   private void ь(Vec3 var1) {
      if (MINECRAFT_CLIENT.player.isHandsBusy()) {
         this.lastDashSpawnPosition = var1;
      } else if (this.lastDashSpawnPosition == null) {
         this.lastDashSpawnPosition = var1;
      } else if (this.lastDashSpawnPosition.distanceToSqr(var1) > 衣К) {
         this.lastDashSpawnPosition = var1;
      } else {
         Vec3 var2 = var1.subtract(this.lastDashSpawnPosition);
         double var3 = var2.length();
         if (!(var3 < 衣I)) {
            Vec3 var5 = var2.scale(1.0 / var3);
            Vec3 var6 = var5.scale(衣Т);
            Vec3 var7 = new Vec3(-var5.z, 0.0, var5.x);
            var7 = var7.lengthSqr() < 衣О ? new Vec3(1.0, 0.0, 0.0) : var7.normalize();
            float var8 = MINECRAFT_CLIENT.player.getBbHeight() - (MINECRAFT_CLIENT.player.isShiftKeyDown() ? 衣Ф : 0.0F);
            double var9 = Math.max(衣Й, 衣к * (衣г / Math.max(衣0, this.dimaParticleDensitySetting.getValue().floatValue())));
            int var11 = Mth.clamp((int)Math.floor(var3 / var9), 1, 12);

            for (int var12 = 1; var12 <= var11; var12++) {
               double var13 = (double)var12 / var11;
               Vec3 var15 = this.lastDashSpawnPosition.add(var2.scale(var13)).add(0.0, var8 * 衣х, 0.0);
               this.ъ(var15, var6, var7, var8, var9);
            }

            this.lastDashSpawnPosition = var1;
         }
      }
   }

   private void ОI(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.worldEffectsLevel != null && this.worldEffectsLevel != MINECRAFT_CLIENT.level) {
            this.dashTrails.clear();
            this.dashGhostParticles.clear();
            this.lastDashSpawnPosition = null;
            this.lastDashPositionsByEntityId.clear();
            this.СУ();
         }

         this.worldEffectsLevel = MINECRAFT_CLIENT.level;
         if (this.modeSetting.isSelected(field_1С)) {
            this.СУ();
            this.dashTrails.clear();
            this.dashGhostParticles.clear();
            this.lastDashSpawnPosition = null;
            this.ОЕ(var1);
         } else if (this.modeSetting.isSelected(field_1з)) {
            this.СУ();
            this.Ощ(var1);
         } else if (this.modeSetting.isSelected(field_1ц)) {
            this.dashTrails.clear();
            this.lastDashSpawnPosition = null;
            this.ОТ(var1);
         } else {
            this.СУ();
            this.О弟(var1);
         }
      } else {
         this.СЯ();
      }
   }

   private Vec3 method_2(Vec3 var1) {
      Vec3 var2 = Math.abs(var1.y) < 衣弟 ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
      Vec3 var3 = var1.cross(var2);
      return var3.lengthSqr() < 衣б ? new Vec3(1.0, 0.0, 0.0) : var3.normalize();
   }

   @Override
   public void onDisable() {
      this.СЯ();
   }

   private Vec3 г(int var1) {
      Vec3 var2 = this.energyTimedPositions.get(var1).position;
      Vec3 var3 = this.energyTimedPositions.get(Math.max(0, var1 - 1)).position;
      Vec3 var4 = this.energyTimedPositions.get(Math.min(this.energyTimedPositions.size() - 1, var1 + 1)).position;
      return var3.scale(衣Ы).add(var2.scale(衣l)).add(var4.scale(衣8));
   }

   private final class GhostParticle {
      private final @com.google.gson.annotations.SerializedName("п")  Player player;
      private final @com.google.gson.annotations.SerializedName("Хо")  int playerId;
      private final @com.google.gson.annotations.SerializedName("ХO")  int colorArgb;
      private final @com.google.gson.annotations.SerializedName("В")  Trails.AnimatedTexture animatedTexture;
      private final @com.google.gson.annotations.SerializedName("В衣")  long createdAtMillis = System.currentTimeMillis();
      private final @com.google.gson.annotations.SerializedName("Х7")  int lifetimeMillis;
      private final @com.google.gson.annotations.SerializedName("В6")  boolean showSparks;
      private final @com.google.gson.annotations.SerializedName("т")  float[] billboardAngles = new float[2];
      private final @com.google.gson.annotations.SerializedName("ц")  List<Trails.SparkParticle> sparks = new ArrayList<>();
      private @com.google.gson.annotations.SerializedName("с西")  double velocityX;
      private @com.google.gson.annotations.SerializedName("сд")  double velocityY;
      private @com.google.gson.annotations.SerializedName("сБ")  double velocityZ;
      private @com.google.gson.annotations.SerializedName("сС")  double x;
      private @com.google.gson.annotations.SerializedName("сз")  double y;
      private @com.google.gson.annotations.SerializedName("сц")  double z;
      private @com.google.gson.annotations.SerializedName("ся")  double previousX;
      private @com.google.gson.annotations.SerializedName("Вс")  double previousY;
      private @com.google.gson.annotations.SerializedName("ВВ")  double previousZ;
      private @com.google.gson.annotations.SerializedName("ео")  float opacity = 0.0F;
      private @com.google.gson.annotations.SerializedName("еO")  float targetOpacity = 1.0F;
      private static final double сшн = -0.0875;
      private static final double сшЦ = 0.175;
      private static final double сшч = 3.0;
      private static final double сшЭ = 4.0;
      private static final double сш必 = 0.7;
      private static final double сшщ = -0.0875;
      private static final double сшГ = 0.175;
      private static final double сшл = 0.04;
      private static final double сшэ = 0.04;
      private static final double сшю = 0.04;
      private static final float сшЩ = 0.035F;
      private static final double сшу = 90.0;
      private static final double сш2 = 5.0E-4;
      private static final double сшф = 360.0;
      private static final float сши = 3.0F;
      private static final float сш9 = 45.0F;
      private static final float сшо = 15.0F;
      private static final float сшO = 26.3F;
      private static final float сш7 = 10.0F;
      private static final float сшР = 160.0F;
      private static final float сш3 = -90.0F;
      private static final double сшр = 1.05;
      private static final double сш6 = 1.05;
      private static final double сш_ = 1.05;
      private static final double сшШ = 5.0;
      private static final double сшы = 5.0;
      private static final double сшй = 3.5;
      private static final double сшЖ = 5.0;
      private static final float сшм = 0.3F;

      private double O(float var1) {
         return this.previousZ + (this.z - this.previousZ) * var1;
      }

      private void ъ(Trails.GhostParticle var1) {
         if (var1 != null && var1.playerId != this.playerId) {
            var1 = null;
         }

         this.previousX = this.x;
         this.previousY = this.y;
         this.previousZ = this.z;
         this.velocityX = (var1 != null ? var1.velocityX : this.velocityX) / сшр;
         this.velocityY = (var1 != null ? var1.velocityY : this.velocityY) / сш6;
         this.velocityZ = (var1 != null ? var1.velocityZ : this.velocityZ) / сш_;
         this.x = this.x + сшШ * this.velocityX;
         this.y = this.y + сшы * this.velocityY / (this.velocityY < 0.0 ? 1.0 : сшй);
         this.z = this.z + сшЖ * this.velocityZ;
         this.ВГ();
         if (this.showSparks) {
            if (this.ъО() < сшм && Trails.this.random.nextInt(12) > 5) {
               int var2 = Trails.this.dashSegmentsSetting.getValue() ? 1 : 2;

               for (int var3 = 0; var3 < var2; var3++) {
                  this.sparks.add(new Trails.SparkParticle(Trails.this.П2()));
               }
            }

            this.sparks.forEach(Trails.SparkParticle::衣衣);
            this.sparks.removeIf(Trails.SparkParticle::Oч);
         } else {
            this.sparks.clear();
         }
      }

      private void ВГ() {
         Camera var1 = MinecraftAccess.MINECRAFT_CLIENT.gameRenderer.mainCamera();
         if (Math.sqrt(this.velocityX * this.velocityX + this.velocityZ * this.velocityZ) < сш2) {
            this.billboardAngles[0] = (float)(сшф * Math.random());
            this.billboardAngles[1] = var1.xRot();
         } else {
            float var2 = this.шЯ();
            float var3 = this.player == null ? 0.0F : (this.player.yRotO - this.player.getYRot()) * сши;
            this.billboardAngles[0] = var2 - сш9 - сшо - var3;
            float var4 = Trails.西(var2 + сшO, this.player != null ? this.player.getYRot() : var2);
            this.billboardAngles[1] = !(var4 < сш7) && !(var4 > сшР) ? var1.xRot() : сш3;
         }
      }

      private double Л(float var1) {
         return this.previousY + (this.y - this.previousY) * var1;
      }

      private int шЯ() {
         int var1 = (int)Math.toDegrees(Math.atan2(this.velocityZ, this.velocityX) - сшу);
         return var1 < 0 ? var1 + 360 : var1;
      }

      private float ъО() {
         return Math.min((float)(System.currentTimeMillis() - this.createdAtMillis) / this.lifetimeMillis, 1.0F);
      }

      private GhostParticle(Player var2, Vec3 var3, Vec3 var4, float var5, int var6, boolean var7) {
         this.player = var2;
         this.playerId = var2.getId();
         this.colorArgb = Trails.this.ъl();
         this.animatedTexture = Trails.this.new AnimatedTexture();
         this.lifetimeMillis = Math.max(200, var6);
         this.showSparks = var7;
         this.velocityX = var4.x - var3.x;
         this.velocityY = var4.y - var3.y;
         this.velocityZ = var4.z - var3.z;
         double var8 = var2.getBbHeight();
         this.x = var3.x - this.velocityX * var5 + (сшн + сшЦ * Math.random());
         this.y = var3.y - this.velocityY * var5 + (var8 / сшч + var8 / сшЭ * Math.random() * сш必);
         this.z = var3.z - this.velocityZ * var5 + (сшщ + сшГ * Math.random());
         this.previousX = this.x;
         this.previousY = this.y;
         this.previousZ = this.z;
         this.velocityX = this.velocityX * сшл;
         this.velocityY = this.velocityY * сшэ;
         this.velocityZ = this.velocityZ * сшю;
         this.ВГ();
      }

      private void ВО() {
         float var1 = сшЩ;
         if (this.opacity < this.targetOpacity) {
            this.opacity = Math.min(this.targetOpacity, this.opacity + var1);
         } else if (this.opacity > this.targetOpacity) {
            this.opacity = Math.max(this.targetOpacity, this.opacity - var1);
         }
      }

      private double е(float var1) {
         return this.previousX + (this.x - this.previousX) * var1;
      }
   }

   private record TimedPosition(Vec3 position, long time) {
   }

   private static final class DashTrail {
      @SerializedName("9")
      private final Vec3 position;
      private final @com.google.gson.annotations.SerializedName("о")  Vec3 direction;
      private final @com.google.gson.annotations.SerializedName("АЕ")  float primaryRadius;
      private final @com.google.gson.annotations.SerializedName("АП")  float secondaryRadius;
      private final @com.google.gson.annotations.SerializedName("А西")  float rotationOffsetDegrees;
      private final @com.google.gson.annotations.SerializedName("Вх")  long createdAtMillis;
      private final @com.google.gson.annotations.SerializedName("ВУ")  long lifetimeMillis;
      private static final double Вфб = Math.PI;
      private static final double ВфЫ = 0.5;
      private static final double Вфl = 0.85;

      private boolean ю_bbeebd87(long var1) {
         return var1 - this.createdAtMillis >= this.lifetimeMillis;
      }

      private float Ф(long var1) {
         float var3 = this.ю_f67ab10a(var1);
         float var4 = 1.0F - var3;
         return Mth.clamp((float)Math.pow(Math.sin(var4 * Вфб * ВфЫ), Вфl), 0.0F, 1.0F);
      }

      private DashTrail(Vec3 var1, Vec3 var2, float var3, float var4, float var5, long var6) {
         this.position = var1;
         this.direction = var2;
         this.primaryRadius = var3;
         this.secondaryRadius = var4;
         this.rotationOffsetDegrees = var5;
         this.createdAtMillis = System.currentTimeMillis();
         this.lifetimeMillis = var6;
      }

      private float ю_f67ab10a(long var1) {
         return Mth.clamp((float)(var1 - this.createdAtMillis) / (float)this.lifetimeMillis, 0.0F, 1.0F);
      }
   }

   private record TextureFrame(Identifier id, int w, int h) {
   }

   private final class AnimatedTexture {
      private final @com.google.gson.annotations.SerializedName("сЭ")  List<Trails.TextureFrame> frames;
      private final @com.google.gson.annotations.SerializedName("Хт")  boolean animated = !Trails.this.dashCubicFrames.isEmpty() && Trails.this.random.nextInt(100) > 40 && !Trails.this.dashAnimatedFrames.isEmpty();
      private final @com.google.gson.annotations.SerializedName("Ящ")  long createdAtMillis = System.currentTimeMillis();
      private final @com.google.gson.annotations.SerializedName("ЯГ")  long frameCycleMillis;

      private Trails.TextureFrame ш() {
         if (this.animated && !this.frames.isEmpty()) {
            float var1 = this.frames.size();
            int var2 = (int)(System.currentTimeMillis() - this.createdAtMillis);
            float var3 = var2 % (int)Math.max(this.frameCycleMillis, 1L) / (float)Math.max(this.frameCycleMillis, 1L);
            int var4 = Mth.clamp((int)(var3 * var1), 0, this.frames.size() - 1);
            return this.frames.get(var4);
         } else {
            return this.frames.isEmpty() ? null : this.frames.getFirst();
         }
      }

      private AnimatedTexture() {
         if (this.animated) {
            this.frames = Trails.this.dashAnimatedFrames.get(Trails.this.random.nextInt(Trails.this.dashAnimatedFrames.size()));
            this.frameCycleMillis = Trails.this.ъ8();
         } else {
            this.frames = List.of(Trails.this.dashCubicFrames.get(Trails.this.random.nextInt(Trails.this.dashCubicFrames.size())));
            this.frameCycleMillis = 1L;
         }
      }
   }

   private static final class SparkParticle {
      private @com.google.gson.annotations.SerializedName("O")  double offsetX;
      @SerializedName("7")
      private double offsetY;
      private @com.google.gson.annotations.SerializedName("Р")  double offsetZ;
      @SerializedName("3")
      private double previousOffsetX;
      private @com.google.gson.annotations.SerializedName("р")  double previousOffsetY;
      @SerializedName("6")
      private double previousOffsetZ;
      @SerializedName("_")
      private double speed;
      private final @com.google.gson.annotations.SerializedName("сЕ")  float yawDegrees;
      private final @com.google.gson.annotations.SerializedName("сП")  float pitchDegrees;
      private final @com.google.gson.annotations.SerializedName("西")  long createdAtMillis;
      private static final float ВбЮ = 360.0F;
      private static final float ВбЧ = -90.0F;
      private static final float ВбЛ = 180.0F;
      private static final double Вб诶 = 50.0;
      private static final double Вб1 = 1000.0;
      private static final float ВбЬ = 90.0F;

      private double 衣(float var1) {
         return this.previousOffsetX + (this.offsetX - this.previousOffsetX) * var1;
      }

      private double Л() {
         return Mth.clamp((System.currentTimeMillis() - this.createdAtMillis) / Вб1, 0.0, 1.0);
      }

      private boolean Oч() {
         return this.Л() >= 1.0;
      }

      private double I(float var1) {
         return this.previousOffsetZ + (this.offsetZ - this.previousOffsetZ) * var1;
      }

      private double с(float var1) {
         return this.previousOffsetY + (this.offsetY - this.previousOffsetY) * var1;
      }

      private SparkParticle(float var1) {
         this.yawDegrees = (float)Math.random() * ВбЮ;
         this.pitchDegrees = ВбЧ + (float)Math.random() * ВбЛ;
         this.createdAtMillis = System.currentTimeMillis();
         this.speed = Math.random() / Вб诶 * var1;
      }

      private void 衣衣() {
         float var1 = (float)Math.toRadians(this.yawDegrees);
         this.previousOffsetX = this.offsetX;
         this.previousOffsetY = this.offsetY;
         this.previousOffsetZ = this.offsetZ;
         this.offsetX = this.offsetX + Mth.sin(var1) * this.speed;
         this.offsetY = this.offsetY + Mth.cos((float)Math.toRadians(this.pitchDegrees - ВбЬ)) * this.speed;
         this.offsetZ = this.offsetZ + Mth.cos(var1) * this.speed;
      }

      private double т() {
         return 1.0 - this.Л();
      }
   }
}
