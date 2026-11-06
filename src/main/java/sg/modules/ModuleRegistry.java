package sg.modules;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Comparator;
import lombok.Generated;
import net.fabricmc.loader.api.FabricLoader;
import sg.modules.impl.misc.AutoRespawn;
import sg.modules.impl.misc.PacketDebug;
import sg.modules.impl.misc.NoInteract;
import sg.modules.impl.combat.NoEntityTrace;
import sg.modules.impl.movement.NoWebController;
import sg.modules.impl.misc.AutoPotionSwap;
import sg.modules.impl.render.BlockEsp;
import sg.modules.impl.misc.TapeMouse;
import sg.modules.impl.misc.ScoreboardHealth;
import sg.modules.impl.render.ItemPhysics;
import sg.modules.impl.misc.IrcModule;
import sg.modules.impl.misc.InventorySort;
import sg.modules.impl.combat.NoFriendDamage;
import sg.modules.impl.render.Cursor;
import sg.modules.impl.render.Ambience;
import sg.modules.impl.render.BlockOverlay;
import sg.modules.impl.render.ChinaHat;
import sg.modules.impl.render.Cubes;
import sg.modules.impl.render.FogAtmosphere;
import sg.modules.impl.render.HitMarkers;
import sg.modules.impl.render.JumpCircle;
import sg.modules.impl.render.Trails;
import sg.modules.impl.autobuy.FunTime;
import sg.modules.impl.combat.AiAimbot;
import sg.modules.impl.combat.AiTester;
import sg.modules.impl.combat.AntiBot;
import sg.modules.impl.combat.AntiThors;
import sg.modules.impl.combat.ArrowSwap;
import sg.modules.impl.combat.Aura;
import sg.modules.impl.combat.AutoAnchor;
import sg.modules.impl.combat.AutoExplosion;
import sg.modules.impl.combat.AutoShift;
import sg.modules.impl.combat.AutoSwap;
import sg.modules.impl.combat.AutoTotem;
import sg.modules.impl.combat.AutoWebTrap;
import sg.modules.impl.combat.Backtrack;
import sg.modules.impl.combat.Blocker;
import sg.modules.impl.combat.Criticals;
import sg.modules.impl.combat.CrystalAura;
import sg.modules.impl.combat.CrystalOptimizer;
import sg.modules.impl.combat.ElytraTarget;
import sg.modules.impl.combat.FastBow;
import sg.modules.impl.combat.PvPHelper;
import sg.modules.impl.combat.TargetPearl;
import sg.modules.impl.combat.Velocity;
import sg.modules.impl.misc.AhHelper;
import sg.modules.impl.combat.AimAssist;
import sg.modules.impl.combat.Aimbot;
import sg.modules.impl.misc.AppleFarmer;
import sg.modules.impl.render.ArmorDurability;
import sg.modules.impl.misc.AutoAccept;
import sg.modules.impl.misc.AutoContract;
import sg.modules.impl.misc.AutoDuel;
import sg.modules.impl.misc.AutoInventory;
import sg.modules.impl.misc.AutoLeave;
import sg.modules.impl.combat.AutoPotion;
import sg.modules.impl.combat.AutoThrow;
import sg.modules.impl.misc.AutoWarden;
import sg.modules.impl.render.Beautiful;
import sg.modules.impl.render.CatPetModule;
import sg.modules.impl.misc.ChestStealer;
import sg.modules.impl.render.CubeField;
import sg.modules.impl.render.CustomCrystal;
import sg.modules.impl.misc.EcExploit;
import sg.modules.impl.render.FakePlayer;
import sg.modules.impl.render.FireworkESP;
import sg.modules.impl.combat.HitBox;
import sg.modules.impl.render.HitWave;
import sg.modules.impl.render.HoleESP;
import sg.modules.impl.render.ImpactNova;
import sg.modules.impl.misc.ItemScroller;
import sg.modules.impl.misc.LeaveTracker;
import sg.modules.impl.misc.Optimization;
import sg.modules.impl.misc.Party;
import sg.modules.impl.misc.PlayerEmotes;
import sg.modules.impl.misc.PotionSwap;
import sg.modules.impl.misc.PvpSafe;
import sg.modules.impl.render.ShulkerView;
import sg.modules.impl.render.TNTEffect;
import sg.modules.impl.render.TNTTimer;
import sg.modules.impl.render.TrapIndicator;
import sg.modules.impl.combat.TriggerBot;
import sg.modules.impl.misc.UseTracker;
import sg.modules.impl.render.ViewModel;
import sg.modules.impl.render.VoidButterflies;
import sg.modules.impl.render.WardenESP;
import sg.modules.impl.misc.AimingItems;
import sg.modules.impl.misc.AntiServerSwap;
import sg.modules.impl.misc.AutoBrewerBuilder;
import sg.modules.impl.misc.AutoEvent;
import sg.modules.impl.misc.AutoFish;
import sg.modules.impl.misc.AutoJoiner;
import sg.modules.impl.misc.AutoMessage;
import sg.modules.impl.misc.AutoTool;
import sg.modules.impl.misc.ClickFriend;
import sg.modules.impl.misc.ClickPearl;
import sg.modules.impl.misc.ClientSounds;
import sg.modules.impl.misc.CreamyKeys;
import sg.modules.impl.misc.DiscordRPC;
import sg.modules.impl.misc.ElytraSwap;
import sg.modules.impl.misc.FastBreak;
import sg.modules.impl.misc.FreeCam;
import sg.modules.impl.misc.FreeLook;
import sg.modules.impl.misc.FuntimeHelper;
import sg.modules.impl.misc.GroundLoot;
import sg.modules.impl.misc.HeadSwap;
import sg.modules.impl.misc.HolyWorldHelper;
import sg.modules.impl.misc.HpHelper;
import sg.modules.impl.misc.KeyFinder;
import sg.modules.impl.misc.KillMessage;
import sg.modules.impl.misc.KitHelper;
import sg.modules.impl.misc.LootTracker;
import sg.modules.impl.misc.MusicPlayer;
import sg.modules.impl.misc.NameProtect;
import sg.modules.impl.misc.OpenWalls;
import sg.modules.impl.misc.RPSpoofer;
import sg.modules.impl.misc.RWFixer;
import sg.modules.impl.misc.RwHelper;
import sg.modules.impl.movement.AirJump;
import sg.modules.impl.movement.AirStuck;
import sg.modules.impl.movement.AutoSprint;
import sg.modules.impl.movement.BlockFly;
import sg.modules.impl.movement.DragonFly;
import sg.modules.impl.movement.ElytraBooster;
import sg.modules.impl.movement.ElytraFly;
import sg.modules.impl.movement.ElytraMotion;
import sg.modules.impl.movement.FakeLag;
import sg.modules.impl.movement.Fly;
import sg.modules.impl.movement.GrimGlide;
import sg.modules.impl.movement.GuiMove;
import sg.modules.impl.movement.Jesus;
import sg.modules.impl.movement.LongJump;
import sg.modules.impl.movement.NoClip;
import sg.modules.impl.movement.NoDelay;
import sg.modules.impl.movement.NoFall;
import sg.modules.impl.movement.NoPush;
import sg.modules.impl.movement.NoSlow;
import sg.modules.impl.movement.NoWeb;
import sg.modules.impl.movement.PearlBoost;
import sg.modules.impl.movement.Speed;
import sg.modules.impl.movement.Spider;
import sg.modules.impl.movement.Strafe;
import sg.modules.impl.movement.TPLoot;
import sg.modules.impl.movement.TargetStrafe;
import sg.modules.impl.movement.TimerModule;
import sg.modules.impl.movement.TpBack;
import sg.modules.impl.movement.WaterSpeed;
import sg.modules.impl.movement.WindHop;
import sg.modules.impl.render.AntiAim;
import sg.modules.impl.render.Arrows;
import sg.modules.impl.render.AspectRatio;
import sg.modules.impl.render.BetterMinecraft;
import sg.modules.impl.render.Chams;
import sg.modules.impl.render.ClickGui;
import sg.modules.impl.render.Crosshair;
import sg.modules.impl.render.DeathManager;
import sg.modules.impl.render.ESP;
import sg.modules.impl.render.EntityESP;
import sg.modules.impl.render.FireFly;
import sg.modules.impl.render.FullBright;
import sg.modules.impl.render.GhostHit;
import sg.modules.impl.render.Hands;
import sg.modules.impl.render.HitEffect;
import sg.modules.impl.render.Hud;
import sg.modules.impl.render.Invoker;
import sg.modules.impl.render.KillCards;
import sg.modules.impl.render.KillEffects;
import sg.modules.impl.render.KillSpray;
import sg.modules.impl.render.LineGlyphs;
import sg.modules.impl.render.MotionBlur;
import sg.modules.impl.render.Particles;
import sg.modules.impl.render.Picture;
import sg.modules.impl.render.PlayerAlert;
import sg.modules.impl.render.Prediction;
import sg.modules.impl.render.Removals;
import sg.modules.impl.render.Saturation;
import sg.modules.impl.render.Sakura;
import sg.modules.impl.render.SeeInvisible;
import sg.modules.impl.render.ShaderMist;
import sg.modules.impl.render.SkeletonESP;
import sg.modules.impl.render.SwingAnimations;
import sg.modules.impl.render.Tracers;
import sg.modules.impl.render.WorldParticles;

public class ModuleRegistry extends ArrayList<ClientModule> {
   @SerializedName("В")
   private final AutoSprint autoSprint = new AutoSprint();
   @SerializedName("В")
   private final ClickGui clickGui = new ClickGui();
   @SerializedName("В")
   private final AirStuck airStuck = new AirStuck();
   @SerializedName("В")
   private final PearlBoost pearlBoost = new PearlBoost();
   @SerializedName("В")
   private final Aura aura = new Aura();
   @SerializedName("В")
   private final Backtrack backtrack = new Backtrack();
   @SerializedName("В")
   private final HitBox hitBox = new HitBox();
   @SerializedName("В")
   private final Aimbot aimbot = new Aimbot();
   @SerializedName("В")
   private final AimAssist aimAssist = new AimAssist();
   @SerializedName("В")
   private final TargetPearl targetPearl = new TargetPearl();
   @SerializedName("В")
   private final PacketDebug packetDebug = new PacketDebug();
   @SerializedName("В")
   private final ScoreboardHealth scoreboardHealth = new ScoreboardHealth();
   @SerializedName("В")
   private final NameProtect nameProtect = new NameProtect();
   @SerializedName("В")
   private final Saturation saturation = new Saturation();
   @SerializedName("В")
   private final FullBright fullBright = new FullBright();
   @SerializedName("Я")
   private final Beautiful beautiful = new Beautiful();
   @SerializedName("В")
   private final MotionBlur motionBlur = new MotionBlur();
   @SerializedName("В")
   private final JumpCircle jumpCircle = new JumpCircle();
   @SerializedName("Я")
   private final CubeField cubeField = new CubeField();
   @SerializedName("В")
   private final WorldParticles worldParticles = new WorldParticles();
   @SerializedName("В")
   private final VoidButterflies voidButterflies = new VoidButterflies();
   @SerializedName("В")
   private final Cubes cubes = new Cubes();
   @SerializedName("В")
   private final Particles particles = new Particles();
   @SerializedName("В")
   private final ImpactNova impactNova = new ImpactNova();
   @SerializedName("В")
   private final FreeCam freeCam = new FreeCam();
   @SerializedName("В")
   private final Criticals criticals = new Criticals();
   @SerializedName("В")
   private final TriggerBot triggerBot = new TriggerBot();
   @SerializedName("В")
   private final AutoShift autoShift = new AutoShift();
   @SerializedName("В")
   private final AutoWebTrap autoWebTrap = new AutoWebTrap();
   @SerializedName("В")
   private final AntiBot antiBot = new AntiBot();
   @SerializedName("В")
   private final NoWeb noWeb = new NoWeb();
   @SerializedName("В")
   private final NoWebController noWebController = new NoWebController();
   @SerializedName("В")
   private final NoSlow noSlow = new NoSlow();
   @SerializedName("В")
   private final ElytraBooster elytraBooster = new ElytraBooster();
   @SerializedName("В")
   private final ElytraFly elytraFly = new ElytraFly();
   @SerializedName("В")
   private final ElytraMotion elytraMotion = new ElytraMotion();
   @SerializedName("В")
   private final Ambience ambience = new Ambience();
   @SerializedName("В")
   private final ESP esp = new ESP();
   @SerializedName("В")
   private final BlockEsp blockEsp = new BlockEsp();
   @SerializedName("В")
   private final BlockOverlay blockOverlay = new BlockOverlay();
   @SerializedName("В")
   private final CustomCrystal customCrystal = new CustomCrystal();
   @SerializedName("В")
   private final ItemScroller itemScroller = new ItemScroller();
   @SerializedName("В")
   private final Hands hands = new Hands();
   @SerializedName("В")
   private final Hud hud = new Hud();
   @SerializedName("В")
   private final Spider spider = new Spider();
   @SerializedName("В")
   private final Jesus jesus = new Jesus();
   @SerializedName("В")
   private final WaterSpeed waterSpeed = new WaterSpeed();
   @SerializedName("В")
   private final Removals removals = new Removals();
   @SerializedName("В")
   private final NoFriendDamage noFriendDamage = new NoFriendDamage();
   @SerializedName("В")
   private final NoEntityTrace noEntityTrace = new NoEntityTrace();
   @SerializedName("В")
   private final TapeMouse tapeMouse = new TapeMouse();
   @SerializedName("В")
   private final AutoAccept autoAccept = new AutoAccept();
   @SerializedName("В")
   private final AutoDuel autoDuel = new AutoDuel();
   @SerializedName("В")
   private final AutoContract autoContract = new AutoContract();
   @SerializedName("В")
   private final FakeLag fakeLag = new FakeLag();
   @SerializedName("В")
   private final AutoFish autoFish = new AutoFish();
   @SerializedName("В")
   private final RPSpoofer rpSpoofer = new RPSpoofer();
   @SerializedName("В")
   private final RWFixer rwFixer = new RWFixer();
   @SerializedName("В")
   private final OpenWalls openWalls = new OpenWalls();
   @SerializedName("В")
   private final NoPush noPush = new NoPush();
   @SerializedName("В")
   private final NoDelay noDelay = new NoDelay();
   @SerializedName("В")
   private final NoClip noClip = new NoClip();
   @SerializedName("В")
   private final BlockFly blockFly = new BlockFly();
   @SerializedName("В")
   private final DragonFly dragonFly = new DragonFly();
   @SerializedName("В")
   private final WindHop windHop = new WindHop();
   @SerializedName("В")
   private final FastBreak fastBreak = new FastBreak();
   @SerializedName("Я")
   private final SwingAnimations swingAnimations = new SwingAnimations();
   @SerializedName("В")
   private final ViewModel viewModel = new ViewModel();
   @SerializedName("В")
   private final GuiMove guiMove = new GuiMove();
   @SerializedName("В")
   private final AutoSwap autoSwap = new AutoSwap();
   @SerializedName("В")
   private final ArrowSwap arrowSwap = new ArrowSwap();
   @SerializedName("В")
   private final FogAtmosphere fogAtmosphere = new FogAtmosphere();
   @SerializedName("В")
   private final ShaderMist shaderMist = new ShaderMist();
   private final @com.google.gson.annotations.SerializedName("е")  PvPHelper pvpHelper = new PvPHelper();
   @SerializedName("В")
   private final Arrows arrows = new Arrows();
   @SerializedName("В")
   private final Party party = new Party();
   @SerializedName("В")
   private final PlayerAlert playerAlert = new PlayerAlert();
   @SerializedName("В")
   private final Speed speed = new Speed();
   @SerializedName("В")
   private final LongJump longJump = new LongJump();
   @SerializedName("В")
   private final Fly fly = new Fly();
   @SerializedName("В")
   private final Strafe strafe = new Strafe();
   @SerializedName("В")
   private final TargetStrafe targetStrafe = new TargetStrafe();
   @SerializedName("В")
   private final AirJump airJump = new AirJump();
   @SerializedName("В")
   private final DiscordRPC discordRpc = new DiscordRPC();
   @SerializedName("В")
   private final IrcModule ircModule = new IrcModule();
   @SerializedName("В")
   private final HolyWorldHelper holyWorldHelper = new HolyWorldHelper();
   @SerializedName("В")
   private final RwHelper rwHelper = new RwHelper();
   @SerializedName("В")
   private final FreeLook freeLook = new FreeLook();
   @SerializedName("В")
   private final FuntimeHelper funtimeHelper = new FuntimeHelper();
   @SerializedName("В")
   private final ClickPearl clickPearl = new ClickPearl();
   @SerializedName("В")
   private final PotionSwap potionSwap = new PotionSwap();
   @SerializedName("В")
   private final FakePlayer fakePlayer = new FakePlayer();
   @SerializedName("В")
   private final AspectRatio aspectRatio = new AspectRatio();
   @SerializedName("В")
   private final Trails trails = new Trails();
   @SerializedName("В")
   private final NoInteract noInteract = new NoInteract();
   @SerializedName("В")
   private final ElytraTarget elytraTarget = new ElytraTarget();
   @SerializedName("Я")
   private final AutoTotem autoTotem = new AutoTotem();
   @SerializedName("В")
   private final AutoPotion autoPotion = new AutoPotion();
   @SerializedName("В")
   private final AutoThrow autoThrow = new AutoThrow();
   @SerializedName("В")
   private final ElytraSwap elytraSwap = new ElytraSwap();
   @SerializedName("В")
   private final HeadSwap headSwap = new HeadSwap();
   @SerializedName("В")
   private final AutoExplosion autoExplosion = new AutoExplosion();
   @SerializedName("В")
   private final GrimGlide grimGlide = new GrimGlide();
   @SerializedName("В")
   private final LeaveTracker leaveTracker = new LeaveTracker();
   @SerializedName("Я")
   private final UseTracker useTracker = new UseTracker();
   @SerializedName("В")
   private final AutoLeave autoLeave = new AutoLeave();
   @SerializedName("В")
   private final PvpSafe pvpSafe = new PvpSafe();
   @SerializedName("В")
   private final AhHelper ahHelper = new AhHelper();
   @SerializedName("В")
   private final AutoEvent autoEvent = new AutoEvent();
   @SerializedName("В")
   private final AimingItems aimingItems = new AimingItems();
   @SerializedName("В")
   private final AutoJoiner autoJoiner = new AutoJoiner();
   @SerializedName("В")
   private final FireworkESP fireworkEsp = new FireworkESP();
   @SerializedName("В")
   private final ChestStealer chestStealer = new ChestStealer();
   @SerializedName("В")
   private final AppleFarmer appleFarmer = new AppleFarmer();
   @SerializedName("В")
   private final AutoWarden autoWarden = new AutoWarden();
   @SerializedName("В")
   private final WardenESP wardenEsp = new WardenESP();
   @SerializedName("В")
   private final AiAimbot aiAimbot = new AiAimbot();
   @SerializedName("В")
   private final AiTester aiTester = new AiTester();
   @SerializedName("В")
   private final Blocker blocker = new Blocker();
   @SerializedName("В")
   private final Picture picture = new Picture();
   @SerializedName("В")
   private final FunTime funTime = new FunTime();
   @SerializedName("Я")
   private final AutoInventory autoInventory = new AutoInventory();
   @SerializedName("Я")
   private final InventorySort inventorySort = new InventorySort();
   @SerializedName("В")
   private final Velocity velocity = new Velocity();
   @SerializedName("В")
   private final AutoRespawn autoRespawn = new AutoRespawn();
   @SerializedName("В")
   private final FastBow fastBow = new FastBow();
   @SerializedName("В")
   private final AutoMessage autoMessage = new AutoMessage();
   @SerializedName("Я")
   private final CrystalAura crystalAura = new CrystalAura();
   @SerializedName("В")
   private final AutoAnchor autoAnchor = new AutoAnchor();
   @SerializedName("В")
   private final CrystalOptimizer crystalOptimizer = new CrystalOptimizer();
   @SerializedName("В")
   private final EcExploit ecExploit = new EcExploit();
   @SerializedName("В")
   private final ClickFriend clickFriend = new ClickFriend();
   @SerializedName("В")
   private final AutoTool autoTool = new AutoTool();
   @SerializedName("В")
   private final HitWave hitWave = new HitWave();
   @SerializedName("В")
   private final AntiThors antiThors = new AntiThors();
   @SerializedName("В")
   private final KeyFinder keyFinder = new KeyFinder();
   @SerializedName("В")
   private final TPLoot tpLoot = new TPLoot();
   @SerializedName("В")
   private final Optimization optimization = new Optimization();
   @SerializedName("В")
   private final TimerModule timerModule = new TimerModule();
   @SerializedName("В")
   private final MusicPlayer musicPlayer = new MusicPlayer();
   @SerializedName("В")
   private final GhostHit ghostHit = new GhostHit();
   @SerializedName("В")
   private final HitEffect hitEffect = new HitEffect();
   @SerializedName("В")
   private final HitMarkers hitMarkers = new HitMarkers();
   @SerializedName("В")
   private final AntiAim antiAim = new AntiAim();
   @SerializedName("В")
   private final Prediction prediction = new Prediction();
   @SerializedName("В")
   private final CatPetModule catPetModule = new CatPetModule();
   @SerializedName("В")
   private final ChinaHat chinaHat = new ChinaHat();
   @SerializedName("В")
   private final Invoker invoker = new Invoker();
   @SerializedName("В")
   private final LineGlyphs lineGlyphs = new LineGlyphs();
   @SerializedName("В")
   private final HoleESP holeEsp = new HoleESP();
   @SerializedName("В")
   private final ItemPhysics itemPhysics = new ItemPhysics();
   @SerializedName("В")
   private final ClientSounds clientSounds = new ClientSounds();
   @SerializedName("В")
   private final CreamyKeys creamyKeys = new CreamyKeys();
   @SerializedName("В")
   private final LootTracker lootTracker = new LootTracker();
   @SerializedName("В")
   private final FireFly fireFly = new FireFly();
   @SerializedName("В")
   private final KillEffects killEffects = new KillEffects();
   @SerializedName("В")
   private final KillSpray killSpray = new KillSpray();
   @SerializedName("В")
   private final KillCards killCards = new KillCards();
   @SerializedName("В")
   private final DeathManager deathManager = new DeathManager();
   @SerializedName("В")
   private final TNTTimer tntTimer = new TNTTimer();
   @SerializedName("В")
   private final TNTEffect tntEffect = new TNTEffect();
   @SerializedName("В")
   private final TrapIndicator trapIndicator = new TrapIndicator();
   @SerializedName("В")
   private final AutoBrewerBuilder autoBrewerBuilder = new AutoBrewerBuilder();
   @SerializedName("В")
   private final Tracers tracers = new Tracers();
   private final Sakura sakura = new Sakura();
   @SerializedName("В")
   private final SkeletonESP skeletonEsp = new SkeletonESP();
   @SerializedName("В")
   private final SeeInvisible seeInvisible = new SeeInvisible();
   @SerializedName("Я")
   private final ShulkerView shulkerView = new ShulkerView();
   @SerializedName("В")
   private final Crosshair crosshair = new Crosshair();
   @SerializedName("В")
   private final Cursor cursor = new Cursor();
   @SerializedName("В")
   private final AntiServerSwap antiServerSwap = new AntiServerSwap();
   @SerializedName("Я")
   private final ArmorDurability armorDurability = new ArmorDurability();
   @SerializedName("В")
   private final KillMessage killMessage = new KillMessage();
   @SerializedName("Я")
   private final HpHelper hpHelper = new HpHelper();
   @SerializedName("В")
   private final KitHelper kitHelper = new KitHelper();
   @SerializedName("В")
   private final GroundLoot groundLoot = new GroundLoot();
   @SerializedName("Я")
   private final PlayerEmotes playerEmotes = new PlayerEmotes();
   @SerializedName("Я")
   private final BetterMinecraft betterMinecraft = new BetterMinecraft();
   @SerializedName("В")
   private final Chams chams = new Chams();
   @SerializedName("В")
   private final AutoPotionSwap autoPotionSwap = new AutoPotionSwap();
   @SerializedName("В")
   private final NoFall noFall = new NoFall();
   @SerializedName("В")
   private final TpBack tpBack = new TpBack();
   @SerializedName("В")
   private final EntityESP entityEsp = new EntityESP();

   @Generated
   public AiAimbot getAiAimbot() {
      return this.aiAimbot;
   }

   @Generated
   public FastBow getFastBow() {
      return this.fastBow;
   }

   public ModuleRegistry() {
      this.add(this.aura);
      this.add(this.backtrack);
      this.add(this.hitBox);
      this.add(this.musicPlayer);
      this.add(this.ghostHit);
      this.add(this.hitEffect);
      this.add(this.hitMarkers);
      this.add(this.antiAim);
      this.add(this.optimization);
      this.add(this.keyFinder);
      this.add(this.tpLoot);
      this.add(this.ecExploit);
      this.add(this.hitWave);
      this.add(this.aimbot);
      this.add(this.aimAssist);
      this.add(this.targetPearl);
      this.add(this.fastBreak);
      this.add(this.autoRespawn);
      this.add(this.grimGlide);
      this.add(this.windHop);
      this.add(this.esp);
      this.add(this.blockEsp);
      this.add(this.blockOverlay);
      this.add(this.customCrystal);
      this.add(this.fastBow);
      this.add(this.noDelay);
      this.add(this.autoPotionSwap);
      this.add(this.noClip);
      this.add(this.blockFly);
      this.add(this.antiServerSwap);
      this.add(this.aimingItems);
      this.add(this.tapeMouse);
      this.add(this.autoEvent);
      this.add(this.fireworkEsp);
      this.add(this.guiMove);
      this.add(this.autoJoiner);
      this.add(this.fakeLag);
      this.add(this.chestStealer);
      this.add(this.appleFarmer);
      this.add(this.autoWarden);
      this.add(this.blocker);
      this.add(this.wardenEsp);
      this.add(this.dragonFly);
      this.add(this.discordRpc);
      this.add(this.ircModule);
      this.add(this.anyDesk);
      this.ircModule.enable();
      if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
         this.add(this.aiAimbot);
         this.add(this.aiTester);
      }

      this.add(this.noPush);
      this.add(this.openWalls);
      this.add(this.autoAccept);
      this.add(this.autoDuel);
      this.add(this.autoContract);
      this.add(this.autoFish);
      this.add(this.picture);
      this.add(this.rpSpoofer);
      this.add(this.rwFixer);
      this.rwFixer.enable();
      this.add(this.autoSprint);
      this.add(this.autoSwap);
      this.add(this.arrowSwap);
      this.add(this.prediction);
      this.add(this.elytraTarget);
      this.add(this.noEntityTrace);
      this.add(this.fakePlayer);
      this.add(this.noFriendDamage);
      this.add(this.leaveTracker);
      this.add(this.useTracker);
      this.add(this.autoLeave);
      this.add(this.pvpSafe);
      this.add(this.ahHelper);
      this.add(this.clickPearl);
      this.add(this.autoInventory);
      this.add(this.inventorySort);
      this.add(this.autoMessage);
      this.add(this.itemScroller);
      this.add(this.clickGui);
      this.add(this.airStuck);
      this.add(this.pvpHelper);
      this.add(this.trails);
      this.add(this.freeLook);
      this.add(this.spider);
      this.add(this.jesus);
      this.add(this.waterSpeed);
      this.add(this.pearlBoost);
      this.add(this.elytraSwap);
      this.add(this.headSwap);
      this.add(this.fogAtmosphere);
      this.add(this.arrows);
      this.add(this.party);
      this.add(this.playerAlert);
      this.add(this.noInteract);
      this.add(this.aspectRatio);
      this.add(this.packetDebug);
      this.add(this.scoreboardHealth);
      this.add(this.nameProtect);
      this.add(this.potionSwap);
      this.add(this.saturation);
      this.add(this.fullBright);
      this.add(this.beautiful);
      this.add(this.motionBlur);
      this.add(this.jumpCircle);
      this.add(this.cubeField);
      this.add(this.speed);
      this.add(this.longJump);
      this.add(this.fly);
      this.add(this.strafe);
      this.add(this.targetStrafe);
      this.add(this.airJump);
      this.add(this.autoTotem);
      this.add(this.autoPotion);
      this.add(this.autoThrow);
      this.add(this.worldParticles);
      this.add(this.voidButterflies);
      this.add(this.cubes);
      this.add(this.particles);
      this.add(this.impactNova);
      this.add(this.freeCam);
      this.add(this.criticals);
      this.add(this.triggerBot);
      this.add(this.autoShift);
      this.add(this.antiBot);
      this.add(this.autoWebTrap);
      this.add(this.noWeb);
      this.add(this.noWebController);
      this.add(this.noSlow);
      this.add(this.elytraBooster);
      this.add(this.elytraFly);
      this.add(this.elytraMotion);
      this.add(this.ambience);
      this.add(this.hands);
      this.add(this.hud);
      this.add(this.removals);
      this.add(this.swingAnimations);
      this.add(this.viewModel);
      this.add(this.holyWorldHelper);
      this.add(this.rwHelper);
      this.add(this.funtimeHelper);
      this.add(this.autoExplosion);
      this.add(this.velocity);
      this.add(this.crystalAura);
      this.add(this.autoAnchor);
      this.add(this.crystalOptimizer);
      this.add(this.autoTool);
      this.add(this.antiThors);
      this.add(this.timerModule);
      this.add(this.catPetModule);
      this.add(this.chinaHat);
      this.add(this.invoker);
      this.add(this.holeEsp);
      this.add(this.itemPhysics);
      this.add(this.clientSounds);
      this.add(this.creamyKeys);
      this.add(this.lootTracker);
      this.add(this.fireFly);
      this.add(this.killEffects);
      this.add(this.killSpray);
      this.add(this.killCards);
      this.add(this.deathManager);
      this.add(this.tntTimer);
      this.add(this.tntEffect);
      this.add(this.trapIndicator);
      this.add(this.autoBrewerBuilder);
      this.add(this.tracers);
      this.add(this.sakura);
      this.add(this.skeletonEsp);
      this.add(this.seeInvisible);
      this.add(this.shulkerView);
      this.add(this.crosshair);
      this.add(this.cursor);
      this.add(this.armorDurability);
      this.add(this.killMessage);
      this.add(this.hpHelper);
      this.add(this.kitHelper);
      this.add(this.groundLoot);
      this.add(this.playerEmotes);
      this.add(this.betterMinecraft);
      this.add(this.chams);
      this.add(this.noFall);
      this.add(this.entityEsp);
      this.sortModulesByName();
   }

   @Generated
   public BlockEsp getBlockEsp() {
      return this.blockEsp;
   }

   @Generated
   public HitMarkers getHitMarkers() {
      return this.hitMarkers;
   }

   @Generated
   public ClickPearl getClickPearl() {
      return this.clickPearl;
   }

   @Generated
   public Removals getRemovals() {
      return this.removals;
   }

   @Generated
   public CrystalOptimizer getCrystalOptimizer() {
      return this.crystalOptimizer;
   }

   @Generated
   public KillSpray getKillSpray() {
      return this.killSpray;
   }

   @Generated
   public PvPHelper getPvPHelper() {
      return this.pvpHelper;
   }

   @Generated
   public AspectRatio getAspectRatio() {
      return this.aspectRatio;
   }

   @Generated
   public IrcModule getIrcModule() {
      return this.ircModule;
   }

   @Generated
   public NoFall getNoFall() {
      return this.noFall;
   }

   @Generated
   public ElytraBooster getElytraBooster() {
      return this.elytraBooster;
   }

   @Generated
   public ItemPhysics getItemPhysics() {
      return this.itemPhysics;
   }

   @Generated
   public FastBreak getFastBreak() {
      return this.fastBreak;
   }

   @Generated
   public AutoThrow getAutoThrow() {
      return this.autoThrow;
   }

   @Generated
   public HitEffect getHitEffect() {
      return this.hitEffect;
   }

   @Generated
   public CatPetModule getCatPetModule() {
      return this.catPetModule;
   }

   @Generated
   public TNTEffect getTNTEffect() {
      return this.tntEffect;
   }

   @Generated
   public Cursor getCursor() {
      return this.cursor;
   }

   @Generated
   public ItemScroller getItemScroller() {
      return this.itemScroller;
   }

   @Generated
   public BlockOverlay getBlockOverlay() {
      return this.blockOverlay;
   }


   @Generated
   public AutoSwap getAutoSwap() {
      return this.autoSwap;
   }

   @Generated
   public FireworkESP getFireworkESP() {
      return this.fireworkEsp;
   }

   @Generated
   public TimerModule getTimerModule() {
      return this.timerModule;
   }

   @Generated
   public ESP getESP() {
      return this.esp;
   }

   @Generated
   public Hud getHud() {
      return this.hud;
   }

   @Generated
   public KillEffects getKillEffects() {
      return this.killEffects;
   }

   @Generated
   public ElytraSwap getElytraSwap() {
      return this.elytraSwap;
   }

   @Generated
   public EntityESP getEntityESP() {
      return this.entityEsp;
   }

   @Generated
   public AntiThors getAntiThors() {
      return this.antiThors;
   }

   @Generated
   public AntiServerSwap getAntiServerSwap() {
      return this.antiServerSwap;
   }

   @Generated
   public FullBright getFullBright() {
      return this.fullBright;
   }

   @Generated
   public DiscordRPC getDiscordRPC() {
      return this.discordRpc;
   }

   @Generated
   public ClientSounds getClientSounds() {
      return this.clientSounds;
   }

   @Generated
   public ClickGui getClickGui() {
      return this.clickGui;
   }

   @Generated
   public ShulkerView getShulkerView() {
      return this.shulkerView;
   }

   @Generated
   public CrystalAura getCrystalAura() {
      return this.crystalAura;
   }

   @Generated
   public NoFriendDamage getNoFriendDamage() {
      return this.noFriendDamage;
   }

   @Generated
   public LongJump getLongJump() {
      return this.longJump;
   }

   @Generated
   public AutoFish getAutoFish() {
      return this.autoFish;
   }

   @Generated
   public AutoPotionSwap getAutoPotionSwap() {
      return this.autoPotionSwap;
   }

   @Generated
   public AimAssist getAimAssist() {
      return this.aimAssist;
   }

   @Generated
   public ShaderMist getShaderMist() {
      return this.shaderMist;
   }

   @Generated
   public AutoJoiner getAutoJoiner() {
      return this.autoJoiner;
   }

   @Generated
   public AppleFarmer getAppleFarmer() {
      return this.appleFarmer;
   }

   @Generated
   public Crosshair getCrosshair() {
      return this.crosshair;
   }

   @Generated
   public Saturation getSaturation() {
      return this.saturation;
   }

   @Generated
   public GuiMove getGuiMove() {
      return this.guiMove;
   }


   @Generated
   public AutoExplosion getAutoExplosion() {
      return this.autoExplosion;
   }

   @Generated
   public TPLoot getTPLoot() {
      return this.tpLoot;
   }

   @Generated
   public PlayerAlert getPlayerAlert() {
      return this.playerAlert;
   }

   @Generated
   public FreeCam getFreeCam() {
      return this.freeCam;
   }

   @Generated
   public EcExploit getEcExploit() {
      return this.ecExploit;
   }

   @Generated
   public DeathManager getDeathManager() {
      return this.deathManager;
   }

   @Generated
   public KillMessage getKillMessage() {
      return this.killMessage;
   }

   @Generated
   public FogAtmosphere getFogAtmosphere() {
      return this.fogAtmosphere;
   }

   @Generated
   public AutoInventory getAutoInventory() {
      return this.autoInventory;
   }

   @Generated
   public HeadSwap getHeadSwap() {
      return this.headSwap;
   }

   @Generated
   public ClickFriend getClickFriend() {
      return this.clickFriend;
   }

   @Generated
   public Blocker getBlocker() {
      return this.blocker;
   }

   @Generated
   public ElytraFly getElytraFly() {
      return this.elytraFly;
   }

   @Generated
   public AutoEvent getAutoEvent() {
      return this.autoEvent;
   }

   @Generated
   public PlayerEmotes getPlayerEmotes() {
      return this.playerEmotes;
   }

   @Generated
   public AiTester getAiTester() {
      return this.aiTester;
   }

   @Generated
   public Optimization getOptimization() {
      return this.optimization;
   }

   @Generated
   public Arrows getArrows() {
      return this.arrows;
   }

   @Generated
   public Velocity getVelocity() {
      return this.velocity;
   }

   @Generated
   public GroundLoot getGroundLoot() {
      return this.groundLoot;
   }

   @Generated
   public ArmorDurability getArmorDurability() {
      return this.armorDurability;
   }

   @Generated
   public BetterMinecraft getBetterMinecraft() {
      return this.betterMinecraft;
   }

   @Generated
   public FuntimeHelper getFuntimeHelper() {
      return this.funtimeHelper;
   }

   @Generated
   public DragonFly getDragonFly() {
      return this.dragonFly;
   }

   @Generated
   public TapeMouse getTapeMouse() {
      return this.tapeMouse;
   }

   @Generated
   public AutoTotem getAutoTotem() {
      return this.autoTotem;
   }

   @Generated
   public AutoTool getAutoTool() {
      return this.autoTool;
   }

   @Generated
   public NameProtect getNameProtect() {
      return this.nameProtect;
   }

   @Generated
   public Beautiful getBeautiful() {
      return this.beautiful;
   }

   @Generated
   public JumpCircle getJumpCircle() {
      return this.jumpCircle;
   }

   @Generated
   public ScoreboardHealth getScoreboardHealth() {
      return this.scoreboardHealth;
   }

   @Generated
   public TpBack getTpBack() {
      return this.tpBack;
   }

   @Generated
   public Chams getChams() {
      return this.chams;
   }

   @Generated
   public PearlBoost getPearlBoost() {
      return this.pearlBoost;
   }

   @Generated
   public NoClip getNoClip() {
      return this.noClip;
   }

   @Generated
   public ChinaHat getChinaHat() {
      return this.chinaHat;
   }

   @Generated
   public Prediction getPrediction() {
      return this.prediction;
   }

   @Generated
   public Picture getPicture() {
      return this.picture;
   }

   @Generated
   public TrapIndicator getTrapIndicator() {
      return this.trapIndicator;
   }

   @Generated
   public KillCards getKillCards() {
      return this.killCards;
   }

   @Generated
   public AutoLeave getAutoLeave() {
      return this.autoLeave;
   }

   @Generated
   public Trails getTrails() {
      return this.trails;
   }

   @Generated
   public FakePlayer getFakePlayer() {
      return this.fakePlayer;
   }

   @Generated
   public ImpactNova getImpactNova() {
      return this.impactNova;
   }

   @Generated
   public NoEntityTrace getNoEntityTrace() {
      return this.noEntityTrace;
   }

   @Generated
   public AutoDuel getAutoDuel() {
      return this.autoDuel;
   }

   @Generated
   public FunTime getFunTime() {
      return this.funTime;
   }

   @Generated
   public Particles getParticles() {
      return this.particles;
   }

   @Generated
   public ElytraMotion getElytraMotion() {
      return this.elytraMotion;
   }

   @Generated
   public RPSpoofer getRPSpoofer() {
      return this.rpSpoofer;
   }

   @Generated
   public AntiAim getAntiAim() {
      return this.antiAim;
   }

   @Generated
   public TargetPearl getTargetPearl() {
      return this.targetPearl;
   }

   @Generated
   public TargetStrafe getTargetStrafe() {
      return this.targetStrafe;
   }

   @Generated
   public AutoWebTrap getAutoWebTrap() {
      return this.autoWebTrap;
   }

   @Generated
   public Ambience getAmbience() {
      return this.ambience;
   }

   @Generated
   public NoSlow getNoSlow() {
      return this.noSlow;
   }

   @Generated
   public Fly getFly() {
      return this.fly;
   }

   @Generated
   public InventorySort getInventorySort() {
      return this.inventorySort;
   }

   @Generated
   public CubeField getCubeField() {
      return this.cubeField;
   }

   @Generated
   public NoWeb getNoWeb() {
      return this.noWeb;
   }

   @Generated
   public HpHelper getHpHelper() {
      return this.hpHelper;
   }

   @Generated
   public HolyWorldHelper getHolyWorldHelper() {
      return this.holyWorldHelper;
   }

   @Generated
   public HitWave getHitWave() {
      return this.hitWave;
   }

   @Generated
   public Invoker getInvoker() {
      return this.invoker;
   }

   @Generated
   public CustomCrystal getCustomCrystal() {
      return this.customCrystal;
   }

   @Generated
   public OpenWalls getOpenWalls() {
      return this.openWalls;
   }

   @Generated
   public NoDelay getNoDelay() {
      return this.noDelay;
   }

   @Generated
   public LootTracker getLootTracker() {
      return this.lootTracker;
   }

   @Generated
   public WaterSpeed getWaterSpeed() {
      return this.waterSpeed;
   }

   @Generated
   public MotionBlur getMotionBlur() {
      return this.motionBlur;
   }

   @Generated
   public Aimbot getAimbot() {
      return this.aimbot;
   }

   @Generated
   public AirJump getAirJump() {
      return this.airJump;
   }

   @Generated
   public Speed getSpeed() {
      return this.speed;
   }

   @Generated
   public Cubes getCubes() {
      return this.cubes;
   }

   @Generated
   public FakeLag getFakeLag() {
      return this.fakeLag;
   }

   @Generated
   public KeyFinder getKeyFinder() {
      return this.keyFinder;
   }

   @Generated
   public ChestStealer getChestStealer() {
      return this.chestStealer;
   }

   @Generated
   public CreamyKeys getCreamyKeys() {
      return this.creamyKeys;
   }

   @Generated
   public WorldParticles getWorldParticles() {
      return this.worldParticles;
   }

   @Generated
   public TriggerBot getTriggerBot() {
      return this.triggerBot;
   }

   @Generated
   public AutoContract getAutoContract() {
      return this.autoContract;
   }

   @Generated
   public TNTTimer getTNTTimer() {
      return this.tntTimer;
   }

   @Generated
   public Backtrack getBacktrack() {
      return this.backtrack;
   }

   @Generated
   public SkeletonESP getSkeletonESP() {
      return this.skeletonEsp;
   }

   @Generated
   public SeeInvisible getSeeInvisible() {
      return this.seeInvisible;
   }

   @Generated
   public NoPush getNoPush() {
      return this.noPush;
   }

   @Generated
   public RwHelper getRwHelper() {
      return this.rwHelper;
   }

   @Generated
   public WindHop getWindHop() {
      return this.windHop;
   }

   @Generated
   public PacketDebug getPacketDebug() {
      return this.packetDebug;
   }

   @Generated
   public ViewModel getViewModel() {
      return this.viewModel;
   }

   @Generated
   public WardenESP getWardenESP() {
      return this.wardenEsp;
   }

   @Generated
   public Criticals getCriticals() {
      return this.criticals;
   }

   @Generated
   public KitHelper getKitHelper() {
      return this.kitHelper;
   }

   @Generated
   public AutoRespawn getAutoRespawn() {
      return this.autoRespawn;
   }

   @Generated
   public GhostHit getGhostHit() {
      return this.ghostHit;
   }

   @Generated
   public Strafe getStrafe() {
      return this.strafe;
   }

   @Generated
   public AutoAccept getAutoAccept() {
      return this.autoAccept;
   }

   @Generated
   public AutoSprint getAutoSprint() {
      return this.autoSprint;
   }

   @Generated
   public BlockFly getBlockFly() {
      return this.blockFly;
   }

   @Generated
   public Jesus getJesus() {
      return this.jesus;
   }

   @Generated
   public LineGlyphs getLineGlyphs() {
      return this.lineGlyphs;
   }

   @Generated
   public AutoBrewerBuilder getAutoBrewerBuilder() {
      return this.autoBrewerBuilder;
   }

   @Generated
   public AhHelper getAhHelper() {
      return this.ahHelper;
   }

   @Generated
   public FireFly getFireFly() {
      return this.fireFly;
   }

   @Generated
   public HitBox getHitBox() {
      return this.hitBox;
   }

   @Generated
   public AutoShift getAutoShift() {
      return this.autoShift;
   }

   @Generated
   public SwingAnimations getSwingAnimations() {
      return this.swingAnimations;
   }

   @Generated
   public Tracers getTracers() {
      return this.tracers;
   }

   @Generated
   public Sakura getSakura() {
      return this.sakura;
   }

   @Generated
   public AutoWarden getAutoWarden() {
      return this.autoWarden;
   }

   @Generated
   public AutoPotion getAutoPotion() {
      return this.autoPotion;
   }

   @Generated
   public UseTracker getUseTracker() {
      return this.useTracker;
   }

   @Generated
   public PvpSafe getPvpSafe() {
      return this.pvpSafe;
   }

   @Generated
   public ArrowSwap getArrowSwap() {
      return this.arrowSwap;
   }

   @Generated
   public AimingItems getAimingItems() {
      return this.aimingItems;
   }

   @Generated
   public LeaveTracker getLeaveTracker() {
      return this.leaveTracker;
   }

   @Generated
   public GrimGlide getGrimGlide() {
      return this.grimGlide;
   }

   @Generated
   public Party getParty() {
      return this.party;
   }

   @Generated
   public ElytraTarget getElytraTarget() {
      return this.elytraTarget;
   }

   @Generated
   public AutoMessage getAutoMessage() {
      return this.autoMessage;
   }

   @Generated
   public Aura getAura() {
      return this.aura;
   }

   @Generated
   public Hands getHands() {
      return this.hands;
   }

   private void sortModulesByName() {
      this.sort(Comparator.comparing(ClientModule::getName, String.CASE_INSENSITIVE_ORDER));
   }

   @Generated
   public VoidButterflies getVoidButterflies() {
      return this.voidButterflies;
   }

   @Generated
   public FreeLook getFreeLook() {
      return this.freeLook;
   }

   @Generated
   public AntiBot getAntiBot() {
      return this.antiBot;
   }

   @Generated
   public MusicPlayer getMusicPlayer() {
      return this.musicPlayer;
   }

   @Generated
   public HoleESP getHoleESP() {
      return this.holeEsp;
   }

   @Generated
   public RWFixer getRWFixer() {
      return this.rwFixer;
   }

   @Generated
   public AutoAnchor getAutoAnchor() {
      return this.autoAnchor;
   }

   @Generated
   public NoInteract getNoInteract() {
      return this.noInteract;
   }

   @Generated
   public PotionSwap getPotionSwap() {
      return this.potionSwap;
   }

   @Generated
   public NoWebController getNoWebController() {
      return this.noWebController;
   }

   @Generated
   public Spider getSpider() {
      return this.spider;
   }

   @Generated
   public AirStuck getAirStuck() {
      return this.airStuck;
   }
}
