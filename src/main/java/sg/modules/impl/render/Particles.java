package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.IndexType;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.AutoStorageIndexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import settings.render.ClientResourceIds;
import sg.ec.TransientGpuUpload;
import sg.ec.MinecraftAccess;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.network.ClientChat;
import sg.render.avatar.CustomParticleTextureCatalog;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.util.time.EntityModelPointSampler;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.combat.Aura;
import sg.settings.ActionSetting;
import sg.events.impl.AttackEntityEvent;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.settings.MultiChoiceSetting;
import sg.modules.impl.misc.Optimization;
import sg.settings.SingleChoiceSetting;

public class Particles extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("ы")  Map<Identifier, List<Particles.Particle>> particlesByTexture = new LinkedHashMap<>();
   private final @com.google.gson.annotations.SerializedName("O")  List<Identifier> textureDrawOrder = new ArrayList<>();
   @SerializedName("7")
   private final List<Particles.Particle> glowPassParticles = new ArrayList<>();
   private final @com.google.gson.annotations.SerializedName("Ч")  Matrix4f billboardTransform = new Matrix4f();
   @SerializedName("В")
   private final Quaternionf cameraYawRotation = new Quaternionf();
   @SerializedName("Я")
   private final Quaternionf cameraPitchRotation = new Quaternionf();
   @SerializedName("Х")
   private final Quaternionf particleRollRotation = new Quaternionf();
   private @com.google.gson.annotations.SerializedName("ХВ")  float cachedCameraYaw = Float.NaN;
   private @com.google.gson.annotations.SerializedName("ХЯ")  float cachedCameraPitch = Float.NaN;
   private static final int Яl = 3600;
   private static final int Я8 = 260;
   private static final long с7 = 2500L;
   private static final long сР = 4000L;
   private static final float ХХ = 2.2F;
   private static final float Хе = 0.45F;
   private static final float ХА = 0.55F;
   private static final float Х4 = 1.0F;
   private static final float Хп = 0.36F;
   private static final int[] Д = new int[]{
      Particles.юл, Particles.юэ, -256, Particles.юю, Particles.юЩ, Particles.юу, Particles.ю2, Particles.юф, Particles.юи, -1, Particles.ю9, Particles.юо
   };
   private static final int[] Ъ_28f5b604 = new int[]{
      Particles.юO, Particles.ю7, Particles.юР, Particles.ю3, Particles.юр, Particles.ю6, Particles.ю_, Particles.юШ
   };
   @SerializedName("В")
   private final ChoiceOption[] particleShapeOptions;
   @SerializedName("Я")
   private final ChoiceOption randomShapeOption;
   private final @com.google.gson.annotations.SerializedName("й")  Map<String, ChoiceOption> customShapeOptionsByName;
   private final @com.google.gson.annotations.SerializedName("б")  SingleChoiceSetting modeSetting;
   private final @com.google.gson.annotations.SerializedName("Ы")  SingleChoiceSetting shapeSetting;
   @SerializedName("Я")
   private final ActionSetting loadCustomShapesActionSetting;
   @SerializedName("Ъ")
   private final MultiChoiceSetting triggersSetting;
   private final @com.google.gson.annotations.SerializedName("сХ")  FloatSetting particleCountSetting;
   private final @com.google.gson.annotations.SerializedName("се")  FloatSetting walkingParticleCountSetting;
   private final @com.google.gson.annotations.SerializedName("сА")  FloatSetting spreadSetting;
   private final @com.google.gson.annotations.SerializedName("с4")  FloatSetting speedSetting;
   private final @com.google.gson.annotations.SerializedName("сп")  FloatSetting lifetimeSetting;
   private final @com.google.gson.annotations.SerializedName("сД")  FloatSetting sizeSetting;
   private final @com.google.gson.annotations.SerializedName("О")  BooleanSetting originalColorSetting;
   private final @com.google.gson.annotations.SerializedName("Ф")  BooleanSetting randomColorSetting;
   private final @com.google.gson.annotations.SerializedName("Й")  BooleanSetting themeColorSetting;
   private final @com.google.gson.annotations.SerializedName("Ь")  ColorSetting customColorSetting;
   private final @com.google.gson.annotations.SerializedName("т")  MultiChoiceSetting spawnEventsSetting;
   private final @com.google.gson.annotations.SerializedName("l")  SingleChoiceSetting particleColorModeSetting;
   private final @com.google.gson.annotations.SerializedName("сЪ")  FloatSetting hitSpawnCountSetting;
   private final @com.google.gson.annotations.SerializedName("ст")  FloatSetting worldSpawnCountSetting;
   private final @com.google.gson.annotations.SerializedName("а")  MultiChoiceSetting particleElementsSetting;
   @SerializedName("к")
   private final BooleanSetting glowSetting;
   @SerializedName("г")
   private final BooleanSetting killTotemParticlesSetting;
   @SerializedName("8")
   private final SingleChoiceSetting killColorModeSetting;
   private final @com.google.gson.annotations.SerializedName("Р")  List<Particles.Particle> activeParticles;
   @SerializedName("Я")
   private final Identifier[] randomShapeTextureIds;
   @SerializedName("3")
   private final List<Particles.EntityTrail> entityTrails;
   private final @com.google.gson.annotations.SerializedName("р")  List<Particles.TrackedVictim> trackedVictims;
   private final @com.google.gson.annotations.SerializedName("Ж")  Map<Integer, Long> lastModelBurstMillisByEntityId;
   @SerializedName("0")
   private final RenderPipeline particleRenderPipeline;
   private final @com.google.gson.annotations.SerializedName("с弟")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("сб")  EventListener<AttackEntityEvent> attackListener;
   private final @com.google.gson.annotations.SerializedName("сЫ")  EventListener<PacketReceiveEvent> packetReceiveListener;
   private final @com.google.gson.annotations.SerializedName("сl")  EventListener<WorldEffectsEvent> worldEffectsListener;
   private @com.google.gson.annotations.SerializedName("ХД")  float walkingSpawnRemainder;
   private @com.google.gson.annotations.SerializedName("Я5")  int clientTickCounter;
   private static final String щ诶 = "Particles";
   private static final String щ1 = "Частицы от ударов, тотемов и движения";
   private static final String щЬ = "Корона";
   private static final String щ衣 = "Взрыв куба";
   private static final String щш = "Доллар";
   private static final String щИ = "Сердце";
   private static final String щь = "Молния";
   private static final String щж = "Линия";
   private static final String щ弟 = "Ромб";
   private static final String щб = "Снежинка";
   private static final String щЫ = "Звезда";
   private static final String щl = "Звезда 2";
   private static final String щ8 = "Треугольник";
   private static final String щ5 = "Рандом";
   private static final String щв = "Режим";
   private static final String щъ = "1";
   private static final String щМ = "2";
   private static final String щН = "Форма";
   private static final String щн = "Загрузить свои";
   private static final String щЦ = "Триггеры";
   private static final String щч = "Удар";
   private static final String щЭ = "Тотем";
   private static final String щ必 = "Ходьба";
   private static final String щщ = "Летящие предметы";
   private static final String щГ = "Количество";
   private static final float щл = 40.0F;
   private static final float щэ = 10.0F;
   private static final float щю = 40.0F;
   private static final String щЩ = "Количество при ходьбе";
   private static final float щу = 30.0F;
   private static final float щ2 = 10.0F;
   private static final float щф = 30.0F;
   private static final String щи = "Разброс";
   private static final float щ9 = 0.5F;
   private static final float що = 3.0F;
   private static final float щO = 0.1F;
   private static final String щ7 = "Скорость";
   private static final float щР = 0.1F;
   private static final float щ3 = 3.0F;
   private static final float щр = 0.1F;
   private static final String щ6 = "Время жизни";
   private static final float щ_ = 2.5F;
   private static final float щШ = 0.5F;
   private static final float щы = 10.0F;
   private static final float щй = 0.1F;
   private static final String щЖ = "Размер";
   private static final float щм = 0.1F;
   private static final float щК = 0.05F;
   private static final String щI = "Оригинальный цвет";
   private static final String щТ = "Случайный цвет";
   private static final String щО = "Авто цвет под тему";
   private static final String щФ = "Цвет";
   private static final int щЙ = -10962689;
   private static final String щк = "Спавнить при";
   private static final String щг = "Ударе";
   private static final String щ0 = "Поп тотема";
   private static final String щх = "Бездействие";
   private static final String щУ = "Ходьбе";
   private static final String щЗ = "Эндер жемчуг";
   private static final String щЕ = "Стрела";
   private static final String щП = "Цвет частиц";
   private static final String щ西 = "От темы";
   private static final String щд = "Радужные";
   private static final String щБ = "Спавнить при ударе";
   private static final float щС = 15.0F;
   private static final float щз = 5.0F;
   private static final float щц = 50.0F;
   private static final String щя = "Спавнить в мире";
   private static final float Гс = 20.0F;
   private static final float ГВ = 3.0F;
   private static final float ГЯ = 50.0F;
   private static final String ГХ = "Элементы";
   private static final String Ге = "Орбиз";
   private static final String ГА = "Звезды";
   private static final String Г4 = "Сердечки";
   private static final String Гп = "Доллары";
   private static final String ГД = "Снежинки";
   private static final String ГЪ = "Короны";
   private static final String Гт = "Молнии";
   private static final String Га = "Линии";
   private static final String ГЮ = "Кругляши";
   private static final String ГЧ = "Ромбы";
   private static final String ГЛ = "Искры";
   private static final String Г诶 = "Свечение";
   private static final String Г1 = "От Убийства/Тотема";
   private static final String ГЬ = "Цвет убийства";
   private static final String Г衣 = "От темы";
   private static final String Гш = "Радужные";
   private static final String ГИ = "rose/particles";
   private static final String Гь = "dead_chams";
   private static final String Гж = "dead_chams";
   private static final String Г弟 = "Sampler0";
   private static final String Гб = "explorer.exe";
   private static final String ГЫ = "2";
   private static final String Гl = "Ходьба";
   private static final String Г8 = "Летящие предметы";
   private static final double Г5 = 6400.0;
   private static final String Гв = "Ходьбе";
   private static final String Гъ = "Эндер жемчуг";
   private static final String ГМ = "Стрела";
   private static final String ГН = "Бездействие";
   private static final double Гн = 4.0E-4;
   private static final float ГЦ = 20.0F;
   private static final float Гч = 90.0F;
   private static final double ГЭ = 0.5;
   private static final double Г必 = 0.5;
   private static final float Гщ = 0.05F;
   private static final double ГГ = -0.15;
   private static final double Гл = 0.15;
   private static final double Гэ = 0.3;
   private static final double Гю = 0.3;
   private static final double ГЩ = -0.15;
   private static final double Гу = 0.15;
   private static final double Г2 = -0.5;
   private static final double Гф = 0.5;
   private static final double Ги = -0.5;
   private static final double Г9 = 0.5;
   private static final double Го = 0.5;
   private static final double ГO = -0.5;
   private static final double Г7 = 0.5;
   private static final float ГР = 0.6F;
   private static final float Г3 = 0.5F;
   private static final float Гр = 0.99F;
   private static final float Г6 = 40.0F;
   private static final float Г_ = 0.45F;
   private static final double ГШ = 4096.0;
   private static final double Гы = 0.004;
   private static final double Гй = 0.002;
   private static final double ГЖ = 0.5;
   private static final double Гм = 0.5;
   private static final double ГК = 1.0E-8;
   private static final double ГI = 1.0E-8;
   private static final float ГТ = 0.45F;
   private static final float ГО = 0.37F;
   private static final float ГФ = 0.07F;
   private static final float ГЙ = 0.04F;
   private static final double Гк = 2.0;
   private static final double Гг = 4.0;
   private static final float Г0 = 0.12F;
   private static final double Гх = 0.025;
   private static final double ГУ = 1.0E-5;
   private static final double ГЗ = -0.012;
   private static final double ГЕ = 0.6;
   private static final double ГП = 1.1;
   private static final double Г西 = -1.0;
   private static final double Гд = 0.008;
   private static final double ГБ = -1.0;
   private static final double ГС = 0.008;
   private static final float Гз = 0.7F;
   private static final float Гц = 0.35F;
   private static final float Гя = 0.18F;
   private static final float лс = 0.12F;
   private static final float лВ = 0.96F;
   private static final double лЯ = 4.0;
   private static final double лХ = 10.0;
   private static final double ле = 2.0;
   private static final double лА = 0.5;
   private static final double л4 = 1.1;
   private static final double лп = -0.012;
   private static final double лД = 0.03;
   private static final double лЪ = 0.006;
   private static final float лт = 0.55F;
   private static final float ла = 0.28F;
   private static final float лЮ = 0.975F;
   private static final double лЧ = 8.0;
   private static final double лЛ = 14.0;
   private static final double л诶 = 1.5;
   private static final double л1 = Math.PI * 2;
   private static final double лЬ = 0.01;
   private static final double л衣 = 0.06;
   private static final double лш = 0.02;
   private static final double лИ = 0.07;
   private static final double ль = -0.02;
   private static final double лж = 0.03;
   private static final double л弟 = 0.008;
   private static final double лб = 0.035;
   private static final double лЫ = 0.3;
   private static final double лl = 0.48;
   private static final double л8 = 0.1;
   private static final double л5 = 0.2;
   private static final float лв = 0.95F;
   private static final double лъ = 12.0;
   private static final double лМ = 22.0;
   private static final double лН = 0.99;
   private static final double лн = 1.0E-8;
   private static final String лЦ = "Ударе";
   private static final float лч = 0.12F;
   private static final float лЭ = 0.08F;
   private static final float л必 = 0.5F;
   private static final double лщ = 0.35;
   private static final float лГ = 0.35F;
   private static final float лл = 0.2F;
   private static final double лэ = 0.25;
   private static final float лю = 0.5F;
   private static final double лЩ = 0.35;
   private static final double лу = 0.2;
   private static final double л2 = 0.5;
   private static final double лф = 0.5;
   private static final double ли = 0.1;
   private static final double л9 = 0.5;
   private static final double ло = 0.5;
   private static final float лO = 2.2F;
   private static final float л7 = 20.0F;
   private static final float лР = 0.9F;
   private static final float л3 = 4.0F;
   private static final float лр = 8.0F;
   private static final String л6 = "Удар";
   private static final float л_ = 0.15F;
   private static final double лШ = -1.0;
   private static final double лы = -1.0;
   private static final double лй = -1.0;
   private static final float лЖ = 0.99F;
   private static final String лм = "Поп тотема";
   private static final String лК = "Тотем";
   private static final float лI = 0.5F;
   private static final double лТ = -1.0;
   private static final double лО = -1.0;
   private static final double лФ = -1.0;
   private static final double лЙ = 0.5;
   private static final double лк = 0.5;
   private static final double лг = 0.5;
   private static final double л0 = 0.5;
   private static final double лх = 0.22;
   private static final double лУ = 0.45;
   private static final double лЗ = 0.18;
   private static final double лЕ = 0.42;
   private static final double лП = 0.05;
   private static final double л西 = 0.14;
   private static final float лд = 0.85F;
   private static final float лБ = 0.8F;
   private static final float лС = 0.98F;
   private static final long лз = 4000L;
   private static final double лц = 4096.0;
   private static final double ля = 31.0;
   private static final double эс = 17.0;
   private static final double эВ = 1.0E-4;
   private static final double эЯ = 0.55F;
   private static final double эХ = 0.55;
   private static final double эе = 0.95;
   private static final double эА = 0.55F;
   private static final double э4 = 0.2;
   private static final double эп = 0.6;
   private static final float эД = 0.36F;
   private static final float эЪ = 0.85F;
   private static final float эт = 1.15F;
   private static final float эа = 2.2F;
   private static final double эЮ = 0.5;
   private static final double эЧ = 0.2;
   private static final double эЛ = 0.5;
   private static final String э诶 = "Радужные";
   private static final float э1 = 0.01F;
   private static final int эЬ = -16777216;
   private static final float э衣 = 1000.0F;
   private static final double эш = 4.0E-4;
   private static final double эИ = -0.5;
   private static final double эь = 0.5;
   private static final double эж = -0.5;
   private static final double э弟 = 0.5;
   private static final double эб = -0.1;
   private static final double эЫ = 0.1;
   private static final double эl = -0.1;
   private static final double э8 = 0.1;
   private static final double э5 = 0.07;
   private static final float эв = 0.1F;
   private static final float эъ = 0.125F;
   private static final float эМ = 0.8F;
   private static final float эН = 0.98F;
   private static final float эн = 3.0F;
   private static final String эЦ = "Эндер жемчуг";
   private static final String эч = "Стрела";
   private static final double эЭ = 4096.0;
   private static final double э必 = 0.35;
   private static final double эщ = 0.07;
   private static final float эГ = 0.1F;
   private static final float эл = 0.125F;
   private static final float ээ = 0.98F;
   private static final float эю = 8.0F;
   private static final float эЩ = 29.0F;
   private static final float эу = 360.0F;
   private static final double э2 = -15.0;
   private static final double эф = 15.0;
   private static final double эи = -3.0;
   private static final double э9 = 3.0;
   private static final double эо = -1.0;
   private static final double эO = -3.0;
   private static final double э7 = 3.0;
   private static final double эР = 0.07;
   private static final float э3 = 0.1F;
   private static final float эр = 0.125F;
   private static final float э6 = 2.5F;
   private static final float э_ = 0.98F;
   private static final double эШ = -0.4;
   private static final double эы = 0.4;
   private static final double эй = 0.5;
   private static final double эЖ = -0.3;
   private static final double эм = 0.3;
   private static final double эК = -0.4;
   private static final double эI = 0.4;
   private static final double эТ = -0.55;
   private static final double эО = 0.55;
   private static final double эФ = 0.12;
   private static final double эЙ = 0.55;
   private static final double эк = -0.55;
   private static final double эг = 0.55;
   private static final float э0 = 0.1F;
   private static final float эх = 0.225F;
   private static final double эУ = 0.12;
   private static final float эЗ = 2.15F;
   private static final float эЕ = 1.35F;
   private static final float эП = 0.97F;
   private static final float э西 = 10.0F;
   private static final String эд = "Радужные";
   private static final int эБ = -16777216;
   private static final String эС = "Орбиз";
   private static final String эз = "Кругляши";
   private static final String эц = "Звезды";
   private static final String эя = "Сердечки";
   private static final String юс = "Доллары";
   private static final String юВ = "Снежинки";
   private static final String юЯ = "Короны";
   private static final String юХ = "Молнии";
   private static final String юе = "Линии";
   private static final String юА = "Ромбы";
   private static final String ю4 = "Искры";
   private static final String юп = "Рандом";
   private static final String юД = "Корона";
   private static final String юЪ = "Взрыв куба";
   private static final String ют = "Доллар";
   private static final String юа = "Сердце";
   private static final String юЮ = "Молния";
   private static final String юЧ = "Линия";
   private static final String юЛ = "Ромб";
   private static final String ю诶 = "Снежинка";
   private static final String ю1 = "Звезда 2";
   private static final String юЬ = "Треугольник";
   private static final double ю衣 = 4096.0;
   private static final float юш = 0.01F;
   private static final float юИ = 0.5F;
   private static final int юь = 16777215;
   private static final float юж = 255.0F;
   private static final float ю弟 = 7.5F;
   private static final float юб = 0.2F;
   private static final float юЫ = 1.35F;
   private static final int юl = 16777215;
   private static final float ю8 = 255.0F;
   private static final float ю5 = 0.65F;
   private static final float юв = 0.549F;
   private static final String юъ = "Sampler0";
   private static final float юМ = 0.5F;
   private static final float юН = (float) (Math.PI / 180.0);
   private static final float юн = (float) (Math.PI / 180.0);
   private static final float юЦ = (float) (Math.PI / 180.0);
   private static final String юч = "particles";
   private static final long юЭ = 16000L;
   private static final String ю必 = "Бездействие";
   private static final String ющ = "Ударе";
   private static final String юГ = "Ходьба";
   private static final int юл = -65536;
   private static final int юэ = -33024;
   private static final int юю = -16711936;
   private static final int юЩ = -16711681;
   private static final int юу = -16776961;
   private static final int ю2 = -7667457;
   private static final int юф = -65281;
   private static final int юи = -60269;
   private static final int ю9 = -16711809;
   private static final int юо = -40121;
   private static final int юO = -16711936;
   private static final int ю7 = -13447886;
   private static final int юР = -8586240;
   private static final int ю3 = -5374161;
   private static final int юр = -16711809;
   private static final int ю6 = -12976364;
   private static final int ю_ = -10027264;
   private static final int юШ = -6619392;

   private void method_2ю() {
      ArrayList var1 = new ArrayList<>(List.of(this.particleShapeOptions));
      HashSet var2 = new HashSet<>(CustomParticleTextureCatalog.ц());
      this.customShapeOptionsByName.keySet().removeIf(var1x -> !var2.contains(var1x));

      for (String var4 : CustomParticleTextureCatalog.ц()) {
         var1.add(this.customShapeOptionsByName.computeIfAbsent(var4, ChoiceOption::new));
      }

      var1.add(this.randomShapeOption);
      this.shapeSetting.setOptions(var1);
   }

   private void method_2(LivingEntity var1, boolean var2) {
      if (this.killTotemParticlesSetting.getValue() && var1 != null && MINECRAFT_CLIENT.level != null) {
         if (MINECRAFT_CLIENT.player == null || !(var1.distanceToSqr(MINECRAFT_CLIENT.player) > лц)) {
            if (Optimization.method_7(var1.getX(), var1.getY(), var1.getZ())) {
               if (var2 || this.э(var1.getId())) {
                  int var3 = Optimization.method_2(260);
                  int var4 = Optimization.method_2(3600);
                  List<Vec3> var5 = EntityModelPointSampler.sampleWorldPoints(var1, var3);
                  if (!var5.isEmpty()) {
                     int var6 = Math.min(var5.size(), var3);
                     int var7 = this.activeParticles.size() + var6 - var4;
                     if (var7 > 0) {
                        this.activeParticles.subList(0, Math.min(var7, this.activeParticles.size())).clear();
                     }

                     Vec3 var8 = this.ф(var1);
                     Random var9 = new Random(Double.doubleToLongBits(var8.x + var8.y * ля + var8.z * эс) ^ System.currentTimeMillis());
                     int var10 = 0;

                     for (Vec3 var12 : var5) {
                        if (var10 >= var6 || !this.method_22()) {
                           break;
                        }

                        Vec3 var13 = var12.subtract(var8);
                        Vec3 var14 = var13.lengthSqr() < эВ ? ф(var9) : var13.normalize();
                        Vec3 var15 = var14.scale(эЯ * (эХ + var9.nextDouble() * эе)).add(0.0, эА * (э4 + var9.nextDouble() * эп), 0.0);
                        float var16 = эД * (эЪ + var9.nextFloat() * эт);
                        int var17 = var2 ? this.Щ8() : this.ф(var1, var12);
                        this.ф(var12, var15, var17, var16, эа, 1.0F, Particles.MotionKind.MODEL, 0.0F, Particles.Texture.ORB.id, false);
                        var10++;
                     }
                  }
               }
            }
         }
      }
   }

   private Identifier method_6_c635a640() {
      ChoiceOption var1 = this.shapeSetting.getSelectedOption();
      if (var1 == null) {
         return Particles.Texture.STAR.id;
      }

      String var2 = var1.getName();
      if (юп.equals(var2)) {
         return this.Э_c635a640();
      }

      Identifier var3 = CustomParticleTextureCatalog.Ш(var2);
      if (var3 != null) {
         return var3;
      }

      return switch (var2) {
         case юД -> Particles.Texture.CROWN.id;
         case юЪ -> Particles.Texture.CUBE_BLAST.id;
         case ют -> Particles.Texture.DOLLAR.id;
         case юа -> Particles.Texture.HEART.id;
         case юЮ -> Particles.Texture.LIGHTNING.id;
         case юЧ -> Particles.Texture.LINE.id;
         case юЛ -> Particles.Texture.RHOMBUS.id;
         case ю诶 -> Particles.Texture.SNOWFLAKE.id;
         case ю1 -> Particles.Texture.STAR_ALT.id;
         case юЬ -> Particles.Texture.TRIANGLE.id;
         default -> Particles.Texture.STAR.id;
      };
   }

   private int Щ8() {
      return Ъ_28f5b604[ThreadLocalRandom.current().nextInt(Ъ_28f5b604.length)];
   }

   private void method_2ь() {
      try {
         Aura var1 = RoseClient.getInstance().getServices().getModules().getAura();
         if (var1 != null && var1.isEnabled() && var1.Ж_95bedd6e() != null && var1.Ж_95bedd6e().isAlive()) {
            this.ф(var1.Ж_95bedd6e(), false);
         }
      } catch (Throwable var5) {
      }

      Iterator var6 = this.trackedVictims.iterator();

      while (var6.hasNext()) {
         Particles.TrackedVictim var2 = (Particles.TrackedVictim)var6.next();
         if (!var2.ъЙ()) {
            var6.remove();
         } else {
            LivingEntity var3 = var2.entity;
            boolean var4 = MINECRAFT_CLIENT.level.getEntity(var3.getId()) == null;
            if (var3.getHealth() <= 0.0F || !var3.isAlive() || var4) {
               this.method_2(var3);
               var6.remove();
            }
         }
      }

      this.trackedVictims.removeIf(var0 -> !var0.ъЙ());
   }

   public Particles() {
      super(щ诶, щ1, ModuleCategory.RENDER);
      this.particleShapeOptions = new ChoiceOption[]{
         new ChoiceOption(щЬ),
         new ChoiceOption(щ衣),
         new ChoiceOption(щш),
         new ChoiceOption(щИ),
         new ChoiceOption(щь),
         new ChoiceOption(щж),
         new ChoiceOption(щ弟),
         new ChoiceOption(щб),
         new ChoiceOption(щЫ),
         new ChoiceOption(щl),
         new ChoiceOption(щ8)
      };
      this.randomShapeOption = new ChoiceOption(щ5);
      this.customShapeOptionsByName = new LinkedHashMap<>();
      this.modeSetting = new SingleChoiceSetting(щв, new ChoiceOption(щъ), new ChoiceOption(щМ));
      this.shapeSetting = new SingleChoiceSetting(щН, 8, this.ф(this.particleShapeOptions)).visibleWhen(this::method_2Щ);
      this.loadCustomShapesActionSetting = new ActionSetting(щн).onClick(this::method_2и).showOtherSettings().visibleWhen(this::method_2Щ);
      this.triggersSetting = new MultiChoiceSetting(
            щЦ, new ChoiceOption(щч, true), new ChoiceOption(щЭ, true), new ChoiceOption(щ必, true), new ChoiceOption(щщ, true)
         )
         .visibleWhen(this::method_2Щ);
      this.particleCountSetting = new FloatSetting(щГ, щл, щэ, щю).withStep(1.0F).visibleWhen(this::method_2Щ);
      this.walkingParticleCountSetting = new FloatSetting(щЩ, щу, щ2, щф).withStep(1.0F).visibleWhen(() -> this.method_2Щ() && this.triggersSetting.isSelected(юГ));
      this.spreadSetting = new FloatSetting(щи, 1.0F, щ9, що).withStep(щO).visibleWhen(this::method_2Щ);
      this.speedSetting = new FloatSetting(щ7, 2.0F, щР, щ3).withStep(щр).visibleWhen(this::method_2Щ);
      this.lifetimeSetting = new FloatSetting(щ6, щ_, щШ, щы).withStep(щй).visibleWhen(this::method_2Щ);
      this.sizeSetting = new FloatSetting(щЖ, 1.0F, щм, 1.0F).withStep(щК).visibleWhen(this::method_2Щ);
      this.originalColorSetting = new BooleanSetting(щI, true).visibleWhen(() -> this.method_2Щ() && this.method_2Я());
      this.randomColorSetting = new BooleanSetting(щТ, false).visibleWhen(() -> this.method_2Щ() && !this.method_26());
      this.themeColorSetting = new BooleanSetting(щО, true).visibleWhen(() -> this.method_2Щ() && !this.method_26() && !this.randomColorSetting.getValue());
      this.customColorSetting = new ColorSetting(щФ, false, щЙ).visibleWhen(() -> this.method_2Щ() && !this.method_26() && !this.randomColorSetting.getValue() && !this.themeColorSetting.getValue());
      this.spawnEventsSetting = new MultiChoiceSetting(
            щк,
            new ChoiceOption(щг, true),
            new ChoiceOption(щ0, true),
            new ChoiceOption(щх, false),
            new ChoiceOption(щУ, true),
            new ChoiceOption(щЗ, false),
            new ChoiceOption(щЕ, false)
         )
         .visibleWhen(this::method_2ф);
      this.particleColorModeSetting = new SingleChoiceSetting(щП, 1, new ChoiceOption(щ西), new ChoiceOption(щд)).visibleWhen(this::method_2ф);
      this.hitSpawnCountSetting = new FloatSetting(щБ, щС, щз, щц).withStep(1.0F).visibleWhen(() -> this.method_2ф() && this.spawnEventsSetting.isSelected(ющ));
      this.worldSpawnCountSetting = new FloatSetting(щя, Гс, ГВ, ГЯ).withStep(1.0F).visibleWhen(() -> this.method_2ф() && this.spawnEventsSetting.isSelected(ю必));
      this.particleElementsSetting = new MultiChoiceSetting(
            ГХ,
            new ChoiceOption(Ге, true),
            new ChoiceOption(ГА, false),
            new ChoiceOption(Г4, false),
            new ChoiceOption(Гп, false),
            new ChoiceOption(ГД, false),
            new ChoiceOption(ГЪ, false),
            new ChoiceOption(Гт, false),
            new ChoiceOption(Га, false),
            new ChoiceOption(ГЮ, false),
            new ChoiceOption(ГЧ, false),
            new ChoiceOption(ГЛ, false)
         )
         .visibleWhen(this::method_2ф);
      this.glowSetting = new BooleanSetting(Г诶, false).visibleWhen(this::method_2ф);
      this.killTotemParticlesSetting = new BooleanSetting(Г1, true);
      this.killColorModeSetting = new SingleChoiceSetting(ГЬ, new ChoiceOption(Г衣), new ChoiceOption(Гш)).visibleWhen(this.killTotemParticlesSetting::getValue);
      this.activeParticles = new ArrayList<>();
      this.randomShapeTextureIds = new Identifier[11];
      this.entityTrails = new ArrayList<>();
      this.trackedVictims = new ArrayList<>();
      this.lastModelBurstMillisByEntityId = new HashMap<>();
      this.particleRenderPipeline = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ГИ)
         .withVertexShader(ClientResourceIds.getCoreId(Гь))
         .withFragmentShader(ClientResourceIds.getCoreId(Гж))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.ADDITIVE)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(Г弟)
         .build();
      this.playerTickListener = this::method_4;
      this.attackListener = this::method_6;
      this.packetReceiveListener = this::Т;
      this.worldEffectsListener = this::O;
      this.addSettings(
         this.modeSetting,
         this.killTotemParticlesSetting,
         this.killColorModeSetting,
         this.shapeSetting,
         this.loadCustomShapesActionSetting,
         this.triggersSetting,
         this.particleCountSetting,
         this.walkingParticleCountSetting,
         this.spreadSetting,
         this.speedSetting,
         this.lifetimeSetting,
         this.sizeSetting,
         this.originalColorSetting,
         this.randomColorSetting,
         this.themeColorSetting,
         this.customColorSetting,
         this.spawnEventsSetting,
         this.particleColorModeSetting,
         this.hitSpawnCountSetting,
         this.worldSpawnCountSetting,
         this.particleElementsSetting,
         this.glowSetting
      );
      CustomParticleTextureCatalog.О9();
      CustomParticleTextureCatalog.с(false, this::method_2ю);
   }

   private int ЩO() {
      if (this.method_26()) {
         return -1;
      } else if (this.randomColorSetting.getValue()) {
         return Д[ThreadLocalRandom.current().nextInt(Д.length)];
      } else {
         return this.themeColorSetting.getValue() ? ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) : this.customColorSetting.getValue();
      }
   }

   private boolean method_22() {
      return this.activeParticles.size() < Optimization.method_2(3600);
   }

   private boolean method_2Щ() {
      return !this.method_2ф();
   }

   private static double Я(double var0, double var2) {
      return !(var2 > var0) ? var0 : ThreadLocalRandom.current().nextDouble(var0, var2);
   }

   private boolean method_26() {
      return this.method_2Я() && this.originalColorSetting.getValue();
   }

   private void method_2Ь() {
      if ((this.clientTickCounter & 1) == 0) {
         float var1 = this.spreadSetting.getValue();
         float var2 = this.speedSetting.getValue();
         float var3 = this.particleCountSetting.getValue() / Г6;
         float var4 = this.sizeSetting.getValue() * Г_;
         float var5 = this.lifetimeSetting.getValue();
         int var6 = this.ЩO();
         int var7 = 0;

         for (Entity var9 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
            if (var7 >= 48 || !this.method_22()) {
               break;
            }

            if ((var9 instanceof ThrowableProjectile || var9 instanceof AbstractArrow || var9 instanceof FireworkRocketEntity)
               && !(var9.distanceToSqr(MINECRAFT_CLIENT.player) > ГШ)) {
               Vec3 var10 = var9.getDeltaMovement();
               double var11 = var10.lengthSqr();
               double var13 = var9.getX() - var9.xo;
               double var15 = var9.getY() - var9.yo;
               double var17 = var9.getZ() - var9.zo;
               double var19 = Math.sqrt(var13 * var13 + var15 * var15 + var17 * var17);
               if (!(var19 < Гы) || !(var11 < Гй)) {
                  var7++;
                  Vec3 var21 = new Vec3(var9.xo, var9.yo + var9.getBbHeight() * ГЖ, var9.zo);
                  Vec3 var22 = new Vec3(var9.getX(), var9.getY() + var9.getBbHeight() * Гм, var9.getZ());
                  Vec3 var23 = var11 > ГК ? var10.normalize() : (var19 > ГI ? new Vec3(var13 / var19, var15 / var19, var17 / var19) : new Vec3(0.0, 0.0, 1.0));
                  Vec3[] var24 = ф(var23);
                  Vec3 var25 = var24[0];
                  Vec3 var26 = var24[1];
                  double var27 = Math.max(Math.sqrt(var11), var19);
                  float var29 = this.clientTickCounter * ГТ + var9.getId() * ГО;
                  float var30 = ГФ + var1 * ГЙ;
                  int var31 = Mth.clamp((int)((Гк + var27 * Гг) * var3), 2, 7);
                  int var32 = ColorMath.blendArgb(var6, -1, Г0);

                  for (int var33 = 0; var33 < var31 && this.method_22(); var33++) {
                     double var34 = var31 == 1 ? 1.0 : (double)var33 / (var31 - 1);
                     double var36 = (1.0 - var34) * Гх;
                     Vec3 var38 = var21.lerp(var22, var34);
                     if (var36 > ГУ) {
                        var38 = var38.add(var25.scale(Я(-var36, var36)).add(var26.scale(Я(-var36, var36))));
                     }

                     Vec3 var39 = var23.scale(ГЗ * var2 * Я(ГЕ, ГП)).add(var25.scale(Я(Г西, 1.0) * Гд * var1)).add(var26.scale(Я(ГБ, 1.0) * ГС * var1));
                     this.ф(
                        var38,
                        var39,
                        var32,
                        var4 * (Гз + (float)var34 * Гц),
                        var5 * (Гя + (float)var34 * лс),
                        лВ,
                        Particles.MotionKind.COMET,
                        (float)Я(лЯ, лХ)
                     );
                  }

                  int var43 = Mth.clamp((int)(ле + var27 * var3), 2, 4);

                  for (int var44 = 0; var44 < var43 && this.method_22(); var44++) {
                     double var35 = var43 == 1 ? лА : (double)var44 / (var43 - 1);
                     double var37 = var29 + var35 * л4;
                     Vec3 var49 = var25.scale(Math.cos(var37) * var30).add(var26.scale(Math.sin(var37) * var30));
                     Vec3 var40 = var21.lerp(var22, var35).add(var49);
                     Vec3 var41 = var25.scale(-Math.sin(var37)).add(var26.scale(Math.cos(var37)));
                     Vec3 var42 = var23.scale(лп * var2).add(var41.scale(лД * var2)).add(var49.normalize().scale(лЪ * var1));
                     this.ф(var40, var42, var6, var4 * лт, var5 * ла, лЮ, Particles.MotionKind.SWIRL, (float)Я(лЧ, лЛ));
                  }

                  int var45 = Mth.clamp((int)((1.0 + var27 * л诶) * var3), 1, 3);

                  for (int var46 = 0; var46 < var45 && this.method_22(); var46++) {
                     double var47 = Я(0.0, л1);
                     Vec3 var48 = var25.scale(Math.cos(var47)).add(var26.scale(Math.sin(var47)));
                     Vec3 var50 = var22.add(var48.scale(Я(лЬ, л衣 * var1)));
                     Vec3 var51 = var48.scale(Я(лш, лИ) * var1 * var2).add(var23.scale(Я(ль, лж) * var2)).add(0.0, Я(л弟, лб) * var2, 0.0);
                     this.ф(var50, var51, var32, var4 * (float)Я(лЫ, лl), var5 * (float)Я(л8, л5), лв, Particles.MotionKind.SPARK, (float)Я(лъ, лМ));
                  }
               }
            }
         }
      }
   }

   private boolean э(int var1) {
      long var2 = System.currentTimeMillis();
      this.lastModelBurstMillisByEntityId.entrySet().removeIf(var2x -> var2 - var2x.getValue() > юЭ);
      Long var4 = this.lastModelBurstMillisByEntityId.get(var1);
      if (var4 != null && var2 - var4 < лз) {
         return false;
      }

      this.lastModelBurstMillisByEntityId.put(var1, var2);
      return true;
   }

   private void method_2ы() {
      this.activeParticles.clear();
      this.particlesByTexture.clear();
      this.textureDrawOrder.clear();
      this.glowPassParticles.clear();
      this.entityTrails.clear();
      this.trackedVictims.clear();
      this.lastModelBurstMillisByEntityId.clear();
      EntityModelPointSampler.clearCaches();
      this.walkingSpawnRemainder = 0.0F;
      this.clientTickCounter = 0;
   }

   private void ф(Particles.EntityTrail var1) {
      Entity var2 = var1.entity;
      if (var2 != null && !var2.isRemoved()) {
         float var3 = 1.0F - var1.юI() * лI;
         float var4 = this.speedSetting.getValue();

         for (int var5 = 0; var5 < 8 && this.method_22(); var5++) {
            double var6 = Я(лТ, 1.0);
            double var8 = Я(лО, 1.0);
            double var10 = Я(лФ, 1.0);
            if (!(var6 * var6 + var8 * var8 + var10 * var10 > 1.0)) {
               Vec3 var12 = new Vec3(
                  var2.getX() + var6 * var2.getBbWidth() * лЙ, var2.getY(лк) + var8 * var2.getBbHeight() * лг, var2.getZ() + var10 * var2.getBbWidth() * л0
               );
               double var13 = this.spreadSetting.getValue().floatValue() * лх * var3 * var4;
               double var15 = Math.random() < лУ ? Я(лЗ, лЕ) * var4 : Я(лП, л西) * var4;
               Vec3 var17 = new Vec3(var6 * var13, var15, var10 * var13);
               this.ф(var12, var17, this.Щ8(), this.sizeSetting.getValue() * лд, this.lifetimeSetting.getValue() * лБ, лС);
            }
         }
      }
   }

   private void ф(
      Vec3 var1, Vec3 var2, int var3, float var4, float var5, float var6, Particles.MotionKind var7, float var8, Identifier var9, boolean var10
   ) {
      if (this.method_22() && Optimization.method_7(var1.x, var1.y, var1.z)) {
         this.activeParticles.add(new Particles.Particle(var1, var2, var3, var4, (long)(var5 * э衣), var6, var9, var7, var8, var10));
      }
   }

   private void ЯЩ() {
      if (!(MINECRAFT_CLIENT.player.getDeltaMovement().horizontalDistanceSqr() <= эш)) {
         for (int var1 = 0; var1 < 2 && this.method_22(); var1++) {
            float var2 = MINECRAFT_CLIENT.player.getBbHeight() * ThreadLocalRandom.current().nextFloat();
            Vec3 var3 = new Vec3(MINECRAFT_CLIENT.player.getX() + Я(эИ, эь), MINECRAFT_CLIENT.player.getY() + var2, MINECRAFT_CLIENT.player.getZ() + Я(эж, э弟));
            Vec3 var4 = new Vec3(MINECRAFT_CLIENT.player.getDeltaMovement().x + Я(эб, эЫ), 0.0, MINECRAFT_CLIENT.player.getDeltaMovement().z + Я(эl, э8)).scale(э5);
            float var5 = эв + ThreadLocalRandom.current().nextFloat() * эъ;
            this.ф(var3, var4, this.ЩТ(), var5 * 2.0F, эМ, эН, Particles.MotionKind.ZENITH_FLOAT, эн, this.Я(), this.glowSetting.getValue());
         }
      }
   }

   private int ф(LivingEntity var1, Vec3 var2) {
      if (this.killColorModeSetting.isSelected(э诶)) {
         float var3 = var1.getBbHeight() <= э1
            ? ThreadLocalRandom.current().nextFloat()
            : Mth.clamp((float)((var2.y - var1.getY()) / var1.getBbHeight()), 0.0F, 1.0F);
         return Color.HSBtoRGB(var3, 1.0F, 1.0F) | эЬ;
      } else {
         return ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      }
   }

   private void O(WorldEffectsEvent var1) {
      if (!this.activeParticles.isEmpty()) {
         this.particlesByTexture.values().forEach(List::clear);
         this.textureDrawOrder.clear();
         this.glowPassParticles.clear();
         float var2 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         Vec3 var3 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         double var4 = Optimization.мн(ю衣);
         long var6 = System.currentTimeMillis();
         boolean var8 = this.method_2ф();
         boolean var9 = this.method_2Щ() || this.glowSetting.getValue();

         for (Particles.Particle var11 : this.activeParticles) {
            double var12;
            double var14;
            double var16;
            if (var11.motionKind == Particles.MotionKind.MODEL) {
               var11.и(var6);
               var12 = var11.x;
               var14 = var11.y;
               var16 = var11.z;
            } else {
               var12 = Mth.lerp(var2, var11.previousX, var11.x);
               var14 = Mth.lerp(var2, var11.previousY, var11.y);
               var16 = Mth.lerp(var2, var11.previousZ, var11.z);
            }

            double var18 = var12 - var3.x;
            double var20 = var14 - var3.y;
            double var22 = var16 - var3.z;
            if (!(var18 * var18 + var20 * var20 + var22 * var22 > var4)) {
               float var24 = var11.Н(var6);
               if (!(var24 <= юш)) {
                  float var25 = Mth.clamp((float)(var6 - var11.createdAtMillis) / (float)Math.max(1L, var11.lifetimeMillis), 0.0F, 1.0F);
                  float var26 = var11.必(var25, var6);
                  var11.cameraRelativeX = var18;
                  var11.cameraRelativeY = var20;
                  var11.cameraRelativeZ = var22;
                  var11.renderSize = var11.baseSize * юИ * var26;
                  var11.renderColorArgb = var11.baseColorArgb & юь | (int)(юж * var24) << 24;
                  List var27 = this.particlesByTexture.computeIfAbsent(var11.textureId, var0 -> new ArrayList<>());
                  if (var27.isEmpty()) {
                     this.textureDrawOrder.add(var11.textureId);
                  }

                  var27.add(var11);
                  if (var9 && var11.motionKind != Particles.MotionKind.MODEL && (!var8 || var11.forceGlowPass)) {
                     var11.glowSize = var11.renderSize * ю弟 * юб * var11.必8() * (var11.forceGlowPass ? юЫ : 1.0F);
                     var11.glowColorArgb = var11.baseColorArgb & юl | (int)(ю8 * (var24 * (var11.forceGlowPass ? ю5 : юв))) << 24;
                     this.glowPassParticles.add(var11);
                  }
               }
            }
         }

         this.particlesByTexture.values().removeIf(List::isEmpty);

         try {
            for (Identifier var32 : this.textureDrawOrder) {
               this.ф(var1, var32, this.particlesByTexture.get(var32), false);
            }

            if (!this.glowPassParticles.isEmpty()) {
               this.ф(var1, Particles.Texture.GLOW_PASS_TEXTURE_ID, this.glowPassParticles, true);
            }
         } finally {
            this.particlesByTexture.values().forEach(List::clear);
            this.textureDrawOrder.clear();
            this.glowPassParticles.clear();
         }
      }
   }

   private void ф(Vec3 var1, Vec3 var2, int var3, float var4, float var5, float var6) {
      this.ф(var1, var2, var3, var4, var5, var6, Particles.MotionKind.NORMAL, 2.0F, this.method_6_c635a640(), false);
   }

   private void method_2(LivingEntity var1) {
      if (this.killTotemParticlesSetting.getValue() && var1 != null && var1 != MINECRAFT_CLIENT.player) {
         Particles.TrackedVictim var2 = null;

         for (Particles.TrackedVictim var4 : this.trackedVictims) {
            if (var4.entity.getId() == var1.getId()) {
               var2 = var4;
               break;
            }
         }

         if (var2 != null && var2.attackRegistered && !var2.deathEffectSpawned) {
            var2.deathEffectSpawned = true;
            this.method_2(var1, false);
         }
      }
   }

   private Vec3 ф(LivingEntity var1) {
      float var2 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
      double var3 = Mth.lerp(var2, var1.xOld, var1.getX());
      double var5 = Mth.lerp(var2, var1.yOld, var1.getY());
      double var7 = Mth.lerp(var2, var1.zOld, var1.getZ());
      return var1.getBoundingBox().move(var3 - var1.getX(), var5 - var1.getY(), var7 - var1.getZ()).getCenter();
   }

   @Override
   public void onDisable() {
      this.method_2ы();
      EntityModelPointSampler.clearCaches();
   }

   private void method_2(Particles.EntityTrail var1) {
      Entity var2 = var1.entity;
      if (var2 != null && !var2.isRemoved()) {
         for (int var3 = 0; var3 < 10 && this.method_22(); var3++) {
            Vec3 var4 = new Vec3(var2.getX() + Я(эШ, эы), var2.getY(эй) + Я(эЖ, эм), var2.getZ() + Я(эК, эI));
            Vec3 var5 = new Vec3(Я(эТ, эО), Я(эФ, эЙ), Я(эк, эг));
            float var6 = ThreadLocalRandom.current().nextFloat(э0, эх);
            this.ф(var4, var5.scale(эУ), this.Щ8(), var6 * эЗ, эЕ, эП, Particles.MotionKind.ZENITH_FLOAT, э西, this.Я(), this.glowSetting.getValue());
         }
      }
   }

   private static Vec3[] ф(Vec3 var0) {
      Vec3 var1 = Math.abs(var0.y) < лН ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
      Vec3 var2 = var0.cross(var1);
      if (var2.lengthSqr() < лн) {
         var2 = new Vec3(1.0, 0.0, 0.0);
      } else {
         var2 = var2.normalize();
      }

      Vec3 var3 = var2.cross(var0).normalize();
      return new Vec3[]{var2, var3};
   }

   private void ф(Vec3 var1, Vec3 var2, int var3, float var4, float var5, float var6, Particles.MotionKind var7, float var8) {
      this.ф(var1, var2, var3, var4, var5, var6, var7, var8, this.method_6_c635a640(), false);
   }

   private void method_2и() {
      try {
         CustomParticleTextureCatalog.О9();
         new ProcessBuilder(Гб, CustomParticleTextureCatalog.В_f0e5dd92.getAbsolutePath()).start();
         CustomParticleTextureCatalog.с(true, this::method_2ю);
      } catch (Exception var3) {
         String var2 = var3.getMessage() != null ? var3.getMessage() : var3.getClass().getSimpleName();
         if (MINECRAFT_CLIENT.player != null) {
            ClientChat.З("Не удалось открыть папку: " + var2);
            ClientChat.З("Путь: " + CustomParticleTextureCatalog.В_f0e5dd92.getAbsolutePath());
         }
      }
   }

   private void method_6(AttackEntityEvent var1) {
      if (var1.getPhase() == AttackEntityEvent.Phase.POST && var1.getTarget() != null) {
         if (var1.getTarget() instanceof LivingEntity var2 && var2.isAlive() && !(var2.getHealth() <= 0.0F)) {
            if (this.killTotemParticlesSetting.getValue() && var1.getAttacker() == MINECRAFT_CLIENT.player) {
               this.ф(var2, true);
            }

            if (this.method_2ф()) {
               if (this.spawnEventsSetting.isSelected(лЦ)) {
                  Entity var19 = var1.getTarget();
                  Vec3 var20 = var19.getDeltaMovement();
                  int var21 = Math.max(1, Math.round(this.hitSpawnCountSetting.getValue()));

                  for (int var22 = 0; var22 < var21 && this.method_22(); var22++) {
                     float var23 = лч + ThreadLocalRandom.current().nextFloat() * лЭ;
                     int var24 = 40 + ThreadLocalRandom.current().nextInt(20);
                     double var25 = (ThreadLocalRandom.current().nextFloat() * 2.0F - 1.0F) * л必 + var20.x * лщ;
                     double var11 = ThreadLocalRandom.current().nextFloat() * лГ - лл + Math.max(0.0, var20.y) * лэ;
                     double var13 = (ThreadLocalRandom.current().nextFloat() * 2.0F - 1.0F) * лю + var20.z * лЩ;
                     double var15 = Math.max(лу, var19.getBbHeight());
                     Vec3 var17 = new Vec3(
                        var19.getX() + (ThreadLocalRandom.current().nextDouble() - л2) * лф,
                        var19.getY() + ли + ThreadLocalRandom.current().nextDouble() * var15,
                        var19.getZ() + (ThreadLocalRandom.current().nextDouble() - л9) * ло
                     );
                     this.ф(
                        var17,
                        new Vec3(var25, var11, var13),
                        this.ЩТ(),
                        var23 * лO,
                        var24 / л7,
                        лР,
                        Particles.MotionKind.ZENITH,
                        л3 + ThreadLocalRandom.current().nextFloat() * лр,
                        this.Я(),
                        this.glowSetting.getValue()
                     );
                  }
               }
            } else if (this.triggersSetting.isSelected(л6)) {
               Entity var18 = var1.getTarget();
               float var4 = this.spreadSetting.getValue() * л_;
               float var5 = this.speedSetting.getValue();
               int var6 = Math.round(this.particleCountSetting.getValue());

               for (int var7 = 0; var7 < var6 && this.method_22(); var7++) {
                  Vec3 var8 = new Vec3(var18.getX(), var18.getY() + Math.random() * var18.getBbHeight(), var18.getZ());
                  Vec3 var9 = new Vec3(Я(лШ, 1.0) * var4 * var5, Я(лы, 1.0) * var4 * var5, Я(лй, 1.0) * var4 * var5);
                  this.ф(var8, var9, this.ЩO(), this.sizeSetting.getValue(), this.lifetimeSetting.getValue(), лЖ);
               }
            }
         }
      }
   }

   private boolean method_2Я() {
      ChoiceOption var1 = this.shapeSetting.getSelectedOption();
      return var1 != null && CustomParticleTextureCatalog.ц(var1.getName());
   }

   private void method_2(BufferBuilder var1, Identifier var2, int var3) {
      MeshData var4 = var1.build();
      if (var4 != null) {
         if (var3 == 0) {
            var4.close();
         } else {
            try {
               DrawState var5 = var4.drawState();
               int var6 = var5.indexCount();
               AutoStorageIndexBuffer var7 = RenderSystem.getSequentialBuffer(var5.primitiveTopology());
               GpuBuffer var8 = var7.getBuffer(var6);
               IndexType var9 = var7.type();
               GpuBufferSlice var10 = TransientGpuUpload.uploadVertices(var4.vertexBuffer(), var5.format().getVertexSize());
               RenderTarget var11 = MINECRAFT_CLIENT.gameRenderer.mainRenderTarget();
               CommandEncoder var12 = RenderSystem.getDevice().createCommandEncoder();
               AbstractTexture var13 = MINECRAFT_CLIENT.getTextureManager().getTexture(var2);

               try (RenderPass var14 = var12.createRenderPass(
                     () -> юч, var11.getColorTextureView(), Optional.empty(), var11.getDepthTextureView(), OptionalDouble.empty()
                  )) {
                  RenderSystem.bindDefaultUniforms(var14);
                  var14.setPipeline(this.particleRenderPipeline);
                  var14.bindTexture(юъ, var13.getTextureView(), var13.getSampler());
                  var14.setVertexBuffer(0, var10);
                  var14.setIndexBuffer(var8, var9);
                  var14.drawIndexed(var6, 1, 0, 0, 0);
               }
            } finally {
               var4.close();
            }
         }
      }
   }

   @Override
   public void onEnable() {
      CustomParticleTextureCatalog.с(false, this::method_2ю);
   }

   private void Т(PacketReceiveEvent var1) {
      if (MINECRAFT_CLIENT.level != null) {
         if (var1.getPacket() instanceof ClientboundEntityEventPacket var2) {
            Entity var7 = var2.getEntity(MINECRAFT_CLIENT.level);
            if (var7 != null) {
               if (var2.getEventId() == 35) {
                  boolean var5 = this.method_2ф() ? this.spawnEventsSetting.isSelected(лм) : this.triggersSetting.isSelected(лК);
                  if (this.killTotemParticlesSetting.getValue() && var7 instanceof LivingEntity var6) {
                     MINECRAFT_CLIENT.execute(() -> this.method_2(var6, true));
                  } else if (var5) {
                     MINECRAFT_CLIENT.execute(() -> this.entityTrails.add(new Particles.EntityTrail(var7)));
                  }
               } else if (var2.getEventId() == 3 && this.killTotemParticlesSetting.getValue() && var7 instanceof LivingEntity var4) {
                  MINECRAFT_CLIENT.execute(() -> this.method_2(var4));
               }
            }
         }
      }
   }

   private Identifier Я() {
      int var1 = 0;
      if (this.particleElementsSetting.isSelected(эС)) {
         this.randomShapeTextureIds[var1++] = Particles.Texture.ORB.id;
      }

      if (this.particleElementsSetting.isSelected(эз)) {
         this.randomShapeTextureIds[var1++] = Particles.Texture.GLOW.id;
      }

      if (this.particleElementsSetting.isSelected(эц)) {
         this.randomShapeTextureIds[var1++] = Particles.Texture.STAR.id;
      }

      if (this.particleElementsSetting.isSelected(эя)) {
         this.randomShapeTextureIds[var1++] = Particles.Texture.HEART.id;
      }

      if (this.particleElementsSetting.isSelected(юс)) {
         this.randomShapeTextureIds[var1++] = Particles.Texture.DOLLAR.id;
      }

      if (this.particleElementsSetting.isSelected(юВ)) {
         this.randomShapeTextureIds[var1++] = Particles.Texture.SNOWFLAKE.id;
      }

      if (this.particleElementsSetting.isSelected(юЯ)) {
         this.randomShapeTextureIds[var1++] = Particles.Texture.CROWN.id;
      }

      if (this.particleElementsSetting.isSelected(юХ)) {
         this.randomShapeTextureIds[var1++] = Particles.Texture.LIGHTNING.id;
      }

      if (this.particleElementsSetting.isSelected(юе)) {
         this.randomShapeTextureIds[var1++] = Particles.Texture.LINE.id;
      }

      if (this.particleElementsSetting.isSelected(юА)) {
         this.randomShapeTextureIds[var1++] = Particles.Texture.RHOMBUS.id;
      }

      if (this.particleElementsSetting.isSelected(ю4)) {
         this.randomShapeTextureIds[var1++] = Particles.Texture.TRIANGLE.id;
      }

      if (var1 == 0) {
         this.randomShapeTextureIds[var1++] = Particles.Texture.STAR.id;
      }

      return this.randomShapeTextureIds[ThreadLocalRandom.current().nextInt(var1)];
   }

   private void ф(LivingEntity var1, boolean var2) {
      if (var1 != null && var1 != MINECRAFT_CLIENT.player) {
         if (var1.isAlive() && !(var1.getHealth() <= 0.0F)) {
            for (Particles.TrackedVictim var4 : this.trackedVictims) {
               if (var4.entity.getId() == var1.getId()) {
                  if (var4.deathEffectSpawned) {
                     return;
                  }

                  var4.O(var1);
                  if (var2) {
                     var4.КК();
                  }

                  return;
               }
            }

            if (var2) {
               Particles.TrackedVictim var5 = new Particles.TrackedVictim(var1);
               var5.КК();
               this.trackedVictims.add(var5);
            }
         }
      }
   }

   private static Vec3 ф(Random var0) {
      return new Vec3(var0.nextDouble() - эЮ, var0.nextDouble() - эЧ, var0.nextDouble() - эЛ).normalize();
   }

   private Identifier Э_c635a640() {
      List var1 = CustomParticleTextureCatalog.诶_2571477d();
      int var2 = Particles.Texture.DEFAULT_TEXTURES.length + var1.size();
      if (var2 == 0) {
         return Particles.Texture.STAR.id;
      }

      int var3 = ThreadLocalRandom.current().nextInt(var2);
      return var3 < Particles.Texture.DEFAULT_TEXTURES.length
         ? Particles.Texture.DEFAULT_TEXTURES[var3].id
         : (Identifier)var1.get(var3 - Particles.Texture.DEFAULT_TEXTURES.length);
   }

   public boolean фь() {
      return this.killTotemParticlesSetting.getValue();
   }

   private void ф(BufferBuilder var1, Matrix4f var2, float var3, float var4, float var5, float var6, float var7, Vector4f var8) {
      var1.addVertex(var2, var3, var4, var5).setUv(var6, var7).setColor(var8.x, var8.y, var8.z, var8.w);
   }

   private void method_4(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         this.clientTickCounter++;
         int var2 = this.activeParticles.size() - Optimization.method_2(3600);
         if (var2 > 0) {
            this.activeParticles.subList(0, var2).clear();
         }

         if (this.killTotemParticlesSetting.getValue()) {
            EntityModelPointSampler.purgeExpiredSamples();
            this.method_2ь();
         }

         if (this.method_2ф()) {
            this.method_2Г();
         } else {
            if (this.triggersSetting.isSelected(Гl)) {
               this.method_2в();
            }

            if (this.triggersSetting.isSelected(Г8)) {
               this.method_2Ь();
            }

            Iterator var3 = this.entityTrails.iterator();

            while (var3.hasNext()) {
               Particles.EntityTrail var4 = (Particles.EntityTrail)var3.next();
               var4.ageTicks++;
               if (var4.СЦ()) {
                  this.ф(var4);
               } else {
                  var3.remove();
               }
            }
         }

         long var11 = System.currentTimeMillis();
         Vec3 var5 = MINECRAFT_CLIENT.player.position();
         Vec3 var6 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         double var7 = Optimization.мн(Double.POSITIVE_INFINITY);
         Iterator var9 = this.activeParticles.iterator();

         while (var9.hasNext()) {
            Particles.Particle var10 = (Particles.Particle)var9.next();
            if (!var10.к_bbeebd87(var11) && !(var10.г(var5) > Г5) && !(var10.г(var6) > var7)) {
               var10.Бм();
            } else {
               var9.remove();
            }
         }
      } else {
         this.method_2ы();
      }
   }

   private boolean method_2ф() {
      return this.modeSetting.isSelected(ГЫ);
   }

   private void method_2в() {
      if (!(MINECRAFT_CLIENT.player.getDeltaMovement().horizontalDistanceSqr() <= Гн) && !MINECRAFT_CLIENT.player.isShiftKeyDown()) {
         this.walkingSpawnRemainder = this.walkingSpawnRemainder + this.walkingParticleCountSetting.getValue() / ГЦ;
         int var1 = (int)this.walkingSpawnRemainder;
         this.walkingSpawnRemainder -= var1;
         if (var1 > 0) {
            double var2 = Math.toRadians(MINECRAFT_CLIENT.player.getYRot() + Гч);
            double var4 = Math.cos(var2) * ГЭ;
            double var6 = Math.sin(var2) * Г必;
            float var8 = this.spreadSetting.getValue() * Гщ;
            float var9 = this.speedSetting.getValue();

            for (int var10 = 0; var10 < var1 && this.method_22(); var10++) {
               Vec3 var11 = new Vec3(
                  MINECRAFT_CLIENT.player.getX() - var4 + Я(ГГ, Гл), MINECRAFT_CLIENT.player.getY() + Гэ + Math.random() * (MINECRAFT_CLIENT.player.getBbHeight() - Гю), MINECRAFT_CLIENT.player.getZ() - var6 + Я(ГЩ, Гу)
               );
               Vec3 var12 = new Vec3(Я(Г2, Гф) * var8 * var9, Я(Ги, Г9) * var8 * Го * var9, Я(ГO, Г7) * var8 * var9);
               this.ф(var11, var12, this.ЩO(), this.sizeSetting.getValue() * ГР, this.lifetimeSetting.getValue() * Г3, Гр);
            }
         }
      } else {
         this.walkingSpawnRemainder = 0.0F;
      }
   }

   private ChoiceOption[] ф(ChoiceOption[] var1) {
      ChoiceOption[] var2 = new ChoiceOption[var1.length + 1];
      System.arraycopy(var1, 0, var2, 0, var1.length);
      var2[var1.length] = this.randomShapeOption;
      return var2;
   }

   private void method_2Г() {
      if (this.spawnEventsSetting.isSelected(Гв)) {
         this.ЯЩ();
      }

      if (this.spawnEventsSetting.isSelected(Гъ) || this.spawnEventsSetting.isSelected(ГМ)) {
         this.Яф();
      }

      if (this.spawnEventsSetting.isSelected(ГН)) {
         this.Я2();
      }

      Iterator var1 = this.entityTrails.iterator();

      while (var1.hasNext()) {
         Particles.EntityTrail var2 = (Particles.EntityTrail)var1.next();
         var2.ageTicks++;
         if (var2.СЦ()) {
            this.method_2(var2);
         } else {
            var1.remove();
         }
      }
   }

   private void Яф() {
      if ((this.clientTickCounter & 1) == 0) {
         boolean var1 = this.spawnEventsSetting.isSelected(эЦ);
         boolean var2 = this.spawnEventsSetting.isSelected(эч);
         int var3 = 0;

         for (Entity var5 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
            if (var3 >= 32 || !this.method_22()) {
               break;
            }

            boolean var6 = var1 && var5 instanceof ThrowableProjectile || var2 && var5 instanceof AbstractArrow;
            if (var6 && !(var5.distanceToSqr(MINECRAFT_CLIENT.player) > эЭ)) {
               Vec3 var7 = var5.getDeltaMovement();
               if (!(var7.length() < э必)) {
                  var3++;

                  for (int var8 = 0; var8 < 5 && this.method_22(); var8++) {
                     float var9 = ThreadLocalRandom.current().nextFloat();
                     Vec3 var10 = var5.position();
                     Vec3 var11 = new Vec3(Я(-var9, var9) + var7.x, Я(-var9, var9) + var7.y, Я(-var9, var9) + var7.z).scale(эщ);
                     float var12 = эГ + ThreadLocalRandom.current().nextFloat() * эл;
                     this.ф(var10, var11, this.ЩТ(), var12 * 2.0F, 1.0F, ээ, Particles.MotionKind.ZENITH_FLOAT, эю, this.Я(), this.glowSetting.getValue());
                  }
               }
            }
         }
      }
   }

   private int ф(BufferBuilder var1, PoseStack var2, double var3, double var5, double var7, float var9, float var10, float var11, float var12, int var13) {
      Vector4f var14 = ColorMath.toRgbaVector(var13);
      float var15 = var9 * юМ;
      if (var11 != this.cachedCameraYaw) {
         this.cameraYawRotation.rotationY(-var11 * юН);
         this.cachedCameraYaw = var11;
      }

      if (var12 != this.cachedCameraPitch) {
         this.cameraPitchRotation.rotationX(var12 * юн);
         this.cachedCameraPitch = var12;
      }

      Matrix4f var16 = this.billboardTransform
         .set(var2.last().pose())
         .translate((float)var3, (float)var5, (float)var7)
         .rotate(this.cameraYawRotation)
         .rotate(this.cameraPitchRotation);
      if (var10 != 0.0F) {
         var16.rotate(this.particleRollRotation.rotationZ(var10 * юЦ));
      }

      this.ф(var1, var16, -var15, -var15, 0.0F, 0.0F, 0.0F, var14);
      this.ф(var1, var16, -var15, var15, 0.0F, 0.0F, 1.0F, var14);
      this.ф(var1, var16, var15, var15, 0.0F, 1.0F, 1.0F, var14);
      this.ф(var1, var16, var15, -var15, 0.0F, 1.0F, 0.0F, var14);
      return 4;
   }

   private void Я2() {
      if (this.clientTickCounter % 15 == 0) {
         Vec3 var1 = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         int var2 = Math.max(1, Math.round(this.worldSpawnCountSetting.getValue())) * 10;

         for (int var3 = 0; var3 < var2 && this.method_22(); var3++) {
            double var4 = 1.0 + ThreadLocalRandom.current().nextFloat() * эЩ;
            double var6 = Math.toRadians(ThreadLocalRandom.current().nextFloat() * эу);
            double var8 = var1.x - Math.sin(var6) * var4;
            double var10 = var1.z + Math.cos(var6) * var4;
            double var12 = var1.y + Я(э2, эф);
            Vec3 var14 = new Vec3(Я(эи, э9), Я(эо, 1.0), Я(эO, э7)).scale(эР);
            float var15 = э3 + ThreadLocalRandom.current().nextFloat() * эр;
            this.ф(
               new Vec3(var8, var12, var10),
               var14,
               this.ЩТ(),
               var15 * 2.0F,
               э6,
               э_,
               Particles.MotionKind.ZENITH_FLOAT,
               2.0F,
               this.Я(),
               this.glowSetting.getValue()
            );
         }
      }
   }

   private void ф(WorldEffectsEvent var1, Identifier var2, List<Particles.Particle> var3, boolean var4) {
      float var5 = MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot();
      float var6 = MINECRAFT_CLIENT.gameRenderer.mainCamera().xRot();
      BufferBuilder var7 = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      int var8 = 0;

      for (Particles.Particle var10 : var3) {
         var8 += this.ф(
            var7,
            var1.getPoses(),
            var10.cameraRelativeX,
            var10.cameraRelativeY,
            var10.cameraRelativeZ,
            var4 ? var10.glowSize : var10.renderSize,
            var4 ? 0.0F : var10.rotationDegrees,
            var5,
            var6,
            var4 ? var10.glowColorArgb : var10.renderColorArgb
         );
      }

      this.method_2(var7, var2, var8);
   }

   private int ЩТ() {
      return this.particleColorModeSetting.isSelected(эд) ? Color.HSBtoRGB(ThreadLocalRandom.current().nextFloat(), 1.0F, 1.0F) | эБ : ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
   }

   private enum MotionKind {
      NORMAL,
      COMET,
      SWIRL,
      SPARK,
      ZENITH,
      ZENITH_FLOAT,
      MODEL;
   }

   private enum Texture {
      CROWN("crown.png"),
      CUBE_BLAST("cubeblast1.png"),
      DOLLAR("dollar.png"),
      HEART("heart.png"),
      LIGHTNING("lightning.png"),
      LINE("line.png"),
      RHOMBUS("rhombus.png"),
      SNOWFLAKE("snowflake.png"),
      STAR("star.png"),
      STAR_ALT("star1.png"),
      TRIANGLE("triangle.png"),
      GLOW("glow.png"),
      ORB("dashbloomsample.png");

      private static final Particles.Texture[] DEFAULT_TEXTURES = new Particles.Texture[]{
         CROWN, CUBE_BLAST, DOLLAR, HEART, LIGHTNING, LINE, RHOMBUS, SNOWFLAKE, STAR, STAR_ALT, TRIANGLE
      };
      private static final Identifier GLOW_PASS_TEXTURE_ID = Identifier.fromNamespaceAndPath("rose", "textures/world/dashbloomsample.png");
      private final @com.google.gson.annotations.SerializedName("Ъ")  Identifier id;

      Texture(String var3) {
         this.id = Identifier.fromNamespaceAndPath("rose", "textures/world/" + var3);
      }
   }

   private static final class Particle {
      private @com.google.gson.annotations.SerializedName("В9")  double x;
      private @com.google.gson.annotations.SerializedName("Во")  double y;
      private @com.google.gson.annotations.SerializedName("ВO")  double z;
      private @com.google.gson.annotations.SerializedName("В7")  double previousX;
      private @com.google.gson.annotations.SerializedName("ВР")  double previousY;
      private @com.google.gson.annotations.SerializedName("В3")  double previousZ;
      private @com.google.gson.annotations.SerializedName("Вр")  double velocityX;
      private @com.google.gson.annotations.SerializedName("В6")  double velocityY;
      private @com.google.gson.annotations.SerializedName("В_")  double velocityZ;
      private @com.google.gson.annotations.SerializedName("ВШ")  double cameraRelativeX;
      private @com.google.gson.annotations.SerializedName("Вы")  double cameraRelativeY;
      private @com.google.gson.annotations.SerializedName("Вй")  double cameraRelativeZ;
      @SerializedName("4ц")
      private float renderSize;
      @SerializedName("4я")
      private float glowSize;
      private @com.google.gson.annotations.SerializedName("Ах")  int renderColorArgb;
      private @com.google.gson.annotations.SerializedName("АУ")  int glowColorArgb;
      private final @com.google.gson.annotations.SerializedName("ВЖ")  double originX;
      private final @com.google.gson.annotations.SerializedName("Вм")  double originY;
      private final @com.google.gson.annotations.SerializedName("ВК")  double originZ;
      private final @com.google.gson.annotations.SerializedName("АЗ")  int baseColorArgb;
      private final @com.google.gson.annotations.SerializedName("пс")  float baseSize;
      private final @com.google.gson.annotations.SerializedName("пВ")  float modelOpacityScale;
      private final @com.google.gson.annotations.SerializedName("Яж")  long createdAtMillis = System.currentTimeMillis();
      private final @com.google.gson.annotations.SerializedName("Я弟")  long lifetimeMillis;
      private final @com.google.gson.annotations.SerializedName("пЯ")  float dragFactor;
      private final @com.google.gson.annotations.SerializedName("О")  Identifier textureId;
      private final @com.google.gson.annotations.SerializedName("В")  Particles.MotionKind motionKind;
      private final @com.google.gson.annotations.SerializedName("пХ")  float rotationSpeed;
      private final @com.google.gson.annotations.SerializedName("ЯЗ")  boolean forceGlowPass;
      private final @com.google.gson.annotations.SerializedName("пе")  float phaseOffset;
      private @com.google.gson.annotations.SerializedName("пА")  float rotationDegrees;
      private @com.google.gson.annotations.SerializedName("ВI")  double highestY;
      private @com.google.gson.annotations.SerializedName("п4")  float bounceFactor;
      private @com.google.gson.annotations.SerializedName("ЯЕ")  boolean settled;
      private static final float В6ч = (float) (Math.PI * 2);
      private static final float В6Э = 360.0F;
      private static final float В6必 = 1.95F;
      private static final float В6щ = 0.42F;
      private static final float В6Г = 0.18F;
      private static final double В6л = 0.004;
      private static final double В6э = 0.2;
      private static final double В6ю = 0.2;
      private static final double В6Щ = 0.2;
      private static final double В6у = 0.012;
      private static final double В62 = 0.28;
      private static final double В6ф = 0.035;
      private static final double В6и = 0.88;
      private static final double В69 = 0.88;
      private static final float В6о = 0.012F;
      private static final double В6O = 1.7;
      private static final double В67 = 0.0011;
      private static final double В6Р = 1.9;
      private static final double В63 = 0.0011;
      private static final double В6р = 0.0012;
      private static final double В66 = 0.14;
      private static final double В6_ = 0.0018;
      private static final double В6Ш = 0.02;
      private static final double В6ы = 9.0E-4;
      private static final double В6й = 0.02;
      private static final double В6Ж = 9.0E-4;
      private static final double В6м = 0.0065;
      private static final double В6К = 0.15;
      private static final double В6I = 0.15;
      private static final double В6Т = 0.15;
      private static final double В6О = 0.02;
      private static final double В6Ф = 0.22;
      private static final double В6Й = 0.04;
      private static final double В6к = 0.75;
      private static final double В6г = 0.75;
      private static final double В60 = 0.008;
      private static final double В6х = 0.04;
      private static final double В6У = 0.25;
      private static final double В6З = 0.08;
      private static final double В6Е = 0.5;
      private static final double В6П = 1.0E-7;
      private static final double В6西 = 1.0E-7;
      private static final double В6д = 1.0E-7;
      private static final double В6Б = 0.01;
      private static final double В6С = 0.2;
      private static final float В6з = 0.9F;
      private static final double В6ц = 0.055;
      private static final double В6я = 0.7;
      private static final double В_с = 0.7;
      private static final float В_В = 0.9F;
      private static final float В_Я = 0.9F;
      private static final float В_Х = 0.9F;
      private static final double В_е = 0.18;
      private static final float В_А = 450.0F;
      private static final float В_4 = 50.0F;
      private static final float В_п = 3.0F;
      private static final double В_Д = 0.0014;
      private static final float В_Ъ = 0.85F;
      private static final float В_т = 0.25F;
      private static final double В_а = Math.PI;
      private static final float В_Ю = 0.9F;
      private static final float В_Ч = 0.15F;
      private static final double В_Л = 0.02;
      private static final float В_诶 = 1.05F;
      private static final float В_1 = 0.45F;
      private static final float В_Ь = 0.95F;
      private static final float В_衣 = 0.25F;
      private static final float В_ш = 1.25F;
      private static final float В_И = 1.15F;
      private static final float В_ь = 1.1F;
      private static final float В_ж = 55.0F;
      private static final float В_弟 = 150.0F;
      private static final float В_б = 180.0F;
      private static final float В_Ы = 250.0F;
      private static final float В_l = 0.82F;
      private static final float В_8 = 0.18F;
      private static final double В_5 = 0.05;
      private static final long В_в = 250L;
      private static final float В_ъ = 0.5F;
      private static final float В_М = -2.0F;
      private static final double В_Н = 2.0;
      private static final float В_н = 0.5F;

      private void Бм() {
         this.previousX = this.x;
         this.previousY = this.y;
         this.previousZ = this.z;
         switch (this.motionKind) {
            case COMET:
               this.Бс();
               break;
            case SWIRL:
               this.БЯ();
               break;
            case SPARK:
               this.Бщ();
               break;
            case ZENITH:
               this.Бф();
               break;
            case ZENITH_FLOAT:
               this.Бй();
               break;
            case MODEL:
               this.Бо();
               break;
            default:
               this.Б衣();
         }
      }

      private void и(long var1) {
         float var3 = this.к_f67ab10a(var1);
         float var4 = э0(var3);
         this.x = this.originX + this.velocityX * var4;
         this.y = this.originY + this.velocityY * var4 - В_е * var3 * var3;
         this.z = this.originZ + this.velocityZ * var4;
      }

      private float э9(float var1) {
         return var1 < В_ъ ? 2.0F * var1 * var1 : 1.0F - (float)Math.pow(В_М * var1 + 2.0F, В_Н) * В_н;
      }

      private double г(Vec3 var1) {
         double var2 = this.x - var1.x;
         double var4 = this.y - var1.y;
         double var6 = this.z - var1.z;
         return var2 * var2 + var4 * var4 + var6 * var6;
      }

      private float Н(long var1) {
         if (this.motionKind == Particles.MotionKind.MODEL) {
            return (1.0F - э0(this.Е(var1))) * this.modelOpacityScale;
         }

         long var3 = var1 - this.createdAtMillis;
         float var5 = this.motionKind != Particles.MotionKind.COMET && this.motionKind != Particles.MotionKind.SPARK ? В_弟 : В_ж;
         float var6 = this.motionKind == Particles.MotionKind.SPARK ? В_б : В_Ы;
         float var7 = this.э9(Mth.clamp((float)var3 / var5, 0.0F, 1.0F));
         float var8 = 1.0F - this.э9(Mth.clamp((float)(var3 - this.lifetimeMillis) / var6, 0.0F, 1.0F));
         float var9 = this.motionKind == Particles.MotionKind.SPARK ? В_l + В_8 * (float)Math.sin((var1 - this.createdAtMillis) * В_5 + this.phaseOffset) : 1.0F;
         return var7 * var8 * var9;
      }

      private void Б衣() {
         this.velocityY = this.velocityY - В6л;
         this.x = this.x + this.velocityX;
         this.y = this.y + this.velocityY;
         this.z = this.z + this.velocityZ;
         if (this.velocityY <= 0.0 && this.л(this.x, this.y - this.baseSize * В6э, this.z)) {
            double var1 = Math.abs(this.velocityY);
            this.y = Math.floor(this.y - this.baseSize * В6ю) + 1.0 + this.baseSize * В6Щ;
            this.velocityY = var1 > В6у ? Math.min(var1 * В62, В6ф) : 0.0;
            this.velocityX = this.velocityX * В6и;
            this.velocityZ = this.velocityZ * В69;
         }

         this.velocityX = this.velocityX * this.dragFactor;
         this.velocityY = this.velocityY * this.dragFactor;
         this.velocityZ = this.velocityZ * this.dragFactor;
         this.rotationDegrees = this.rotationDegrees + this.rotationSpeed;
      }

      private void Бф() {
         if (!this.settled && MinecraftAccess.MINECRAFT_CLIENT.level != null) {
            this.velocityY = this.velocityY - В60;
            double var1 = Math.max(В6х, this.baseSize * В6У);
            double var3 = Math.max(В6З, this.baseSize * В6Е);
            AABB var5 = new AABB(this.x - var1, this.y, this.z - var1, this.x + var1, this.y + var3, this.z + var1);
            Vec3 var6 = new Vec3(this.velocityX, this.velocityY, this.velocityZ);
            Vec3 var7 = Entity.collideBoundingBox((Entity)null, var6, var5, MinecraftAccess.MINECRAFT_CLIENT.level, List.of());
            this.x = this.x + var7.x;
            this.y = this.y + var7.y;
            this.z = this.z + var7.z;
            this.highestY = Math.max(this.highestY, this.y);
            boolean var8 = Math.abs(var6.y - var7.y) > В6П;
            boolean var9 = Math.abs(var6.x - var7.x) > В6西;
            boolean var10 = Math.abs(var6.z - var7.z) > В6д;
            if (var8) {
               if (var6.y < 0.0) {
                  double var11 = Math.sqrt(Math.max(this.highestY - this.y, В6Б)) * В6С * this.bounceFactor;
                  this.bounceFactor = this.bounceFactor * В6з;
                  if (!(var11 > В6ц)) {
                     this.velocityX = 0.0;
                     this.velocityY = 0.0;
                     this.velocityZ = 0.0;
                     this.settled = true;
                     this.rotationDegrees = this.rotationDegrees + this.rotationSpeed;
                     return;
                  }

                  this.velocityY = var11;
                  this.velocityX = this.velocityX * В6я;
                  this.velocityZ = this.velocityZ * В_с;
               } else {
                  this.velocityY = -Math.abs(var6.y) * this.bounceFactor;
                  this.bounceFactor = this.bounceFactor * В_В;
               }
            }

            if (var9) {
               this.velocityX = -var6.x * this.bounceFactor;
               this.bounceFactor = this.bounceFactor * В_Я;
            }

            if (var10) {
               this.velocityZ = -var6.z * this.bounceFactor;
               this.bounceFactor = this.bounceFactor * В_Х;
            }

            this.velocityX = this.velocityX * this.dragFactor;
            this.velocityY = this.velocityY * this.dragFactor;
            this.velocityZ = this.velocityZ * this.dragFactor;
            this.rotationDegrees = this.rotationDegrees + this.rotationSpeed;
         } else {
            this.rotationDegrees = this.rotationDegrees + this.rotationSpeed;
         }
      }

      private float 必(float var1, long var2) {
         return switch (this.motionKind) {
            case COMET -> В_Ъ + В_т * (float)Math.sin(var1 * В_а);
            case SWIRL -> В_Ю + В_Ч * (float)Math.sin((var2 - this.createdAtMillis) * В_Л + this.phaseOffset);
            case SPARK -> В_诶 - В_1 * var1;
            case ZENITH -> 1.0F;
            case ZENITH_FLOAT -> 1.0F;
            case MODEL -> В_Ь + this.Е(var2) * В_衣;
            default -> 1.0F;
         };
      }

      private boolean л(double var1, double var3, double var5) {
         if (MinecraftAccess.MINECRAFT_CLIENT.level == null) {
            return false;
         }

         BlockPos var7 = BlockPos.containing(var1, var3, var5);
         return MinecraftAccess.MINECRAFT_CLIENT.level.getBlockState(var7).isCollisionShapeFullBlock(MinecraftAccess.MINECRAFT_CLIENT.level, var7);
      }

      private float 必8() {
         return switch (this.motionKind) {
            case COMET -> В_ш;
            case SWIRL -> В_И;
            case SPARK -> В_ь;
            case ZENITH -> 1.0F;
            case ZENITH_FLOAT -> 1.0F;
            case MODEL -> 1.0F;
            default -> 1.0F;
         };
      }

      private void Бй() {
         this.velocityY = this.velocityY - В_Д;
         this.x = this.x + this.velocityX;
         this.y = this.y + this.velocityY;
         this.z = this.z + this.velocityZ;
         this.velocityX = this.velocityX * this.dragFactor;
         this.velocityY = this.velocityY * this.dragFactor;
         this.velocityZ = this.velocityZ * this.dragFactor;
         this.rotationDegrees = this.rotationDegrees + this.rotationSpeed;
      }

      private void Бс() {
         float var1 = (float)(System.currentTimeMillis() - this.createdAtMillis) * В6о + this.phaseOffset;
         this.velocityX = this.velocityX + Math.sin(var1 * В6O) * В67;
         this.velocityZ = this.velocityZ + Math.cos(var1 * В6Р) * В63;
         this.velocityY = this.velocityY - В6р;
         this.x = this.x + this.velocityX;
         this.y = this.y + this.velocityY;
         this.z = this.z + this.velocityZ;
         this.velocityX = this.velocityX * this.dragFactor;
         this.velocityY = this.velocityY * this.dragFactor;
         this.velocityZ = this.velocityZ * this.dragFactor;
         this.rotationDegrees = this.rotationDegrees + this.rotationSpeed;
      }

      private float к_f67ab10a(long var1) {
         float var3 = Math.min(В_А, Math.max(0.0F, (float)this.lifetimeMillis - В_4));
         float var4 = Math.max(1.0F, (float)this.lifetimeMillis - var3);
         return Mth.clamp(((float)(var1 - this.createdAtMillis) - var3) / var4, 0.0F, 1.0F);
      }

      private boolean к_bbeebd87(long var1) {
         return this.motionKind == Particles.MotionKind.MODEL ? this.Е(var1) >= 1.0F : var1 - this.createdAtMillis > this.lifetimeMillis + В_в;
      }

      private void Бщ() {
         this.velocityY = this.velocityY - В6м;
         this.x = this.x + this.velocityX;
         this.y = this.y + this.velocityY;
         this.z = this.z + this.velocityZ;
         if (this.velocityY <= 0.0 && this.л(this.x, this.y - this.baseSize * В6К, this.z)) {
            double var1 = Math.abs(this.velocityY);
            this.y = Math.floor(this.y - this.baseSize * В6I) + 1.0 + this.baseSize * В6Т;
            this.velocityY = var1 > В6О ? Math.min(var1 * В6Ф, В6Й) : 0.0;
            this.velocityX = this.velocityX * В6к;
            this.velocityZ = this.velocityZ * В6г;
         }

         this.velocityX = this.velocityX * this.dragFactor;
         this.velocityY = this.velocityY * this.dragFactor;
         this.velocityZ = this.velocityZ * this.dragFactor;
         this.rotationDegrees = this.rotationDegrees + this.rotationSpeed;
      }

      private Particle(
         Vec3 var1, Vec3 var2, int var3, float var4, long var5, float var7, Identifier var8, Particles.MotionKind var9, float var10, boolean var11
      ) {
         this.phaseOffset = ThreadLocalRandom.current().nextFloat() * В6ч;
         this.rotationDegrees = ThreadLocalRandom.current().nextFloat() * В6Э;
         this.bounceFactor = В6必;
         this.originX = this.x = this.previousX = var1.x;
         this.originY = this.y = this.previousY = var1.y;
         this.originZ = this.z = this.previousZ = var1.z;
         this.velocityX = var2.x;
         this.velocityY = var2.y;
         this.velocityZ = var2.z;
         this.baseColorArgb = var3;
         this.baseSize = var4;
         this.modelOpacityScale = var9 == Particles.MotionKind.MODEL ? В6щ + ThreadLocalRandom.current().nextFloat() * В6Г : 1.0F;
         this.lifetimeMillis = var5;
         this.dragFactor = var7;
         this.textureId = var8;
         this.motionKind = var9;
         this.rotationSpeed = var10;
         this.forceGlowPass = var11;
         this.highestY = this.y;
         if (var9 == Particles.MotionKind.MODEL) {
            this.rotationDegrees = 0.0F;
         }
      }

      private void БЯ() {
         double var1 = В66 * Math.signum(this.rotationSpeed == 0.0F ? 1.0F : this.rotationSpeed);
         double var3 = Math.cos(var1);
         double var5 = Math.sin(var1);
         double var7 = this.velocityX * var3 - this.velocityZ * var5;
         double var9 = this.velocityX * var5 + this.velocityZ * var3;
         this.velocityX = var7;
         this.velocityZ = var9;
         this.velocityY = this.velocityY - В6_;
         this.velocityX = this.velocityX + Math.cos(this.phaseOffset + this.rotationDegrees * В6Ш) * В6ы;
         this.velocityZ = this.velocityZ + Math.sin(this.phaseOffset + this.rotationDegrees * В6й) * В6Ж;
         this.x = this.x + this.velocityX;
         this.y = this.y + this.velocityY;
         this.z = this.z + this.velocityZ;
         this.velocityX = this.velocityX * this.dragFactor;
         this.velocityY = this.velocityY * this.dragFactor;
         this.velocityZ = this.velocityZ * this.dragFactor;
         this.rotationDegrees = this.rotationDegrees + this.rotationSpeed;
      }

      private float Е(long var1) {
         return Mth.clamp((float)(var1 - this.createdAtMillis) / (float)Math.max(1L, this.lifetimeMillis), 0.0F, 1.0F);
      }

      private void Бо() {
         this.и(System.currentTimeMillis());
      }

      private static float э0(float var0) {
         return var0 * var0 * (В_п - 2.0F * var0);
      }
   }

   private static final class TrackedVictim {
      private @com.google.gson.annotations.SerializedName("е")  LivingEntity entity;
      private @com.google.gson.annotations.SerializedName("с3")  long lastRefreshMillis = System.currentTimeMillis();
      private @com.google.gson.annotations.SerializedName("ср")  long retentionMillis;
      private @com.google.gson.annotations.SerializedName("сП")  boolean attackRegistered;
      private @com.google.gson.annotations.SerializedName("с西")  boolean deathEffectSpawned;
      private static final long ВьЩ = 2500L;
      private static final long Вьу = 2500L;

      private void O(LivingEntity var1) {
         this.entity = var1;
         this.lastRefreshMillis = System.currentTimeMillis();
         this.retentionMillis = Вьу;
      }

      private boolean ъЙ() {
         return this.entity != null && System.currentTimeMillis() - this.lastRefreshMillis < this.retentionMillis;
      }

      private void КК() {
         this.attackRegistered = true;
         this.lastRefreshMillis = System.currentTimeMillis();
      }

      private TrackedVictim(LivingEntity var1) {
         this.retentionMillis = ВьЩ;
         this.entity = var1;
      }
   }

   private static final class EntityTrail {
      private final @com.google.gson.annotations.SerializedName("е")  Entity entity;
      private @com.google.gson.annotations.SerializedName("ео")  int ageTicks;
      private static final float ЯЧй = 20.0F;

      private EntityTrail(Entity var1) {
         this.entity = var1;
      }

      private boolean СЦ() {
         return this.ageTicks < 20 && this.entity != null && !this.entity.isRemoved();
      }

      private float юI() {
         return this.ageTicks / ЯЧй;
      }
   }
}
