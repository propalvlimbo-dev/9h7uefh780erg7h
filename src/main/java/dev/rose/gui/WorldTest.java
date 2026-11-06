package dev.rose.gui;

import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import dev.rose.RoseClient;
import dev.rose.gui.menu.MenuScreen;
import dev.rose.hud.HudElement;
import dev.rose.hud.HudLayouts;
import dev.rose.hud.HudManager;
import dev.rose.looks.Look;
import dev.rose.module.Module;
import dev.rose.module.Modules;
import dev.rose.module.impl.cosmetic.KillEffectsModule;
import dev.rose.music.NowPlaying;
import dev.rose.notify.Notifications;
import dev.rose.setting.ChoiceSetting;
import java.util.function.Consumer;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;


public final class WorldTest {
   private static final String WORLD = "RoseTest";
   private static final long STEP_MS = 1400L;
   private static final long SETTLE_MS = 3500L;
   private static WorldTest instance;
   private static boolean entering;
   private final Minecraft mc = Minecraft.getInstance();
   private long joinedAt = -1L;
   private int step;
   private int shotTicks;
   private String shotName;
   private int walkTicks;
   private float turnPerTick;
   private boolean walkJumps = true;
   public static boolean tooltipDemo;
   private int spinFrames;

   private WorldTest() {
   }

   public static boolean handles(String mode) {
      return "world".equals(mode)
         || "visuals".equals(mode)
         || "cosmetics".equals(mode)
         || "viewmodel".equals(mode)
         || "newmods".equals(mode)
         || "life".equals(mode)
         || "wings".equals(mode)
         || mode != null && mode.startsWith("stage11");
   }

   public static void tick(Minecraft mc) {
      String mode = RoseClient.uiTest();
      if (handles(mode)) {
         if (mc.level == null) {
            if (!entering && mc.screen instanceof TitleScreen) {
               entering = true;
               enterWorld(mc);
            }
         } else {
            if (instance == null) {
               instance = new WorldTest();
            }

            instance.run();
         }
      }
   }

   public static void enterWorldForTest(Minecraft mc) {
      entering = true;
      enterWorld(mc);
   }

   private static void enterWorld(Minecraft mc) {
      if (mc.getLevelSource().levelExists("RoseTest")) {
         mc.createWorldOpenFlows().openWorld("RoseTest", () -> entering = false);
      } else {
         GameRules rules = new GameRules(WorldDataConfiguration.DEFAULT.enabledFeatures());
         rules.set(GameRules.ADVANCE_TIME, false, null);
         rules.set(GameRules.ADVANCE_WEATHER, false, null);
         rules.set(GameRules.SPAWN_MOBS, false, null);
         LevelSettings settings = new LevelSettings("RoseTest", GameType.CREATIVE, false, Difficulty.PEACEFUL, true, rules, WorldDataConfiguration.DEFAULT);
         mc.createWorldOpenFlows()
            .createFreshLevel(
               "RoseTest",
               settings,
               new WorldOptions(20260914L, false, false),
               registries -> ((WorldPreset)registries.lookupOrThrow(Registries.WORLD_PRESET).getOrThrow(WorldPresets.FLAT).value()).createWorldDimensions(),
               mc.screen
            );
      }
   }

   private void run() {
      if (this.mc.player != null) {
         long now = System.currentTimeMillis();
         if (this.joinedAt < 0L) {
            this.joinedAt = now;
            this.mc.options.pauseOnLostFocus = false;
            RoseClient.LOGGER.info("[Rose][worldtest] joined");
         }

         if (this.walkTicks > 0) {
            this.walkTicks--;
            if (this.turnPerTick != 0.0F) {
               this.mc.player.setYRot(this.mc.player.getYRot() + this.turnPerTick);
            }

            this.mc
               .player
               .setDeltaMovement(this.mc.player.getLookAngle().multiply(0.25, 0.0, 0.25).add(0.0, this.mc.player.getDeltaMovement().y, 0.0));
            if (this.walkJumps && this.walkTicks % 8 == 0 && this.mc.player.onGround()) {
               this.mc.player.jumpFromGround();
            }
         }

         if (this.spinFrames > 0) {
            this.spinFrames--;
            this.mc.player.setYRot(this.mc.player.getYRot() + 9.0F);
         }

         if (("cosmetics".equals(RoseClient.uiTest()) || stage11Mode()) && this.walkTicks == 0) {
            this.mc.player.yBodyRot = this.mc.player.getYRot();
            this.mc.player.yBodyRotO = this.mc.player.yBodyRot;
            this.mc.player.yHeadRot = this.mc.player.getYRot();
         }

         if (this.mc.screen == null && this.mc.mouseHandler.isMouseGrabbed()) {
            this.mc.mouseHandler.releaseMouse();
         }

         if (stage11Mode() && this.mc.screen instanceof PauseScreen) {
            this.mc.setScreen(null);
         }

         if (this.shotTicks > 0 && --this.shotTicks == 0) {
            this.shot(this.shotName);
         }

         if (now - this.joinedAt >= 3500L + this.step * 1400L) {
            if (stage11Mode()) {
               boolean more = switch (RoseClient.uiTest()) {
                  case "stage11" -> this.stage11();
                  case "stage11b" -> this.stage11b();
                  case "stage11c" -> this.stage11c();
                  case "stage11d" -> this.stage11d();
                  case "stage11e" -> this.stage11e();
                  case "stage11lock" -> this.stage11lock();
                  case "stage11g" -> this.stage11g();
                  case "stage11h" -> this.stage11h();
                  case "stage11i" -> this.stage11i();
                  case "stage11j" -> this.stage11j();
                  case "stage11k" -> this.stage11k();
                  default -> this.stage11f();
               };
               if (more) {
                  RoseClient.LOGGER.info("[Rose][worldtest] step {} done, {} fps", this.step, this.mc.getFps());
                  this.step++;
               }
            } else if ("newmods".equals(RoseClient.uiTest())) {
               if (this.newModules()) {
                  RoseClient.LOGGER.info("[Rose][worldtest] step {} done, {} fps", this.step, this.mc.getFps());
                  this.step++;
               }
            } else if ("viewmodel".equals(RoseClient.uiTest())) {
               if (this.viewmodel()) {
                  RoseClient.LOGGER.info("[Rose][worldtest] step {} done, {} fps", this.step, this.mc.getFps());
                  this.step++;
               }
            } else if ("wings".equals(RoseClient.uiTest())) {
               if (this.wings()) {
                  RoseClient.LOGGER.info("[Rose][worldtest] step {} done, {} fps", this.step, this.mc.getFps());
                  this.step++;
               }
            } else if ("life".equals(RoseClient.uiTest())) {
               if (this.life()) {
                  RoseClient.LOGGER.info("[Rose][worldtest] step {} done, {} fps", this.step, this.mc.getFps());
                  this.step++;
               }
            } else if (!"visuals".equals(RoseClient.uiTest()) && !"cosmetics".equals(RoseClient.uiTest())) {
               switch (this.step) {
                  case 0:
                     this.command("time set 6000");
                     this.command("weather clear");
                     this.command("kill @e[type=minecraft:villager]");
                     this.command("tp @p 0 -60 0 0 12");
                     this.command("item replace entity @p armor.head with minecraft:diamond_helmet[minecraft:damage=330]");
                     this.command("item replace entity @p armor.chest with minecraft:netherite_chestplate[minecraft:damage=120]");
                     this.command("item replace entity @p armor.legs with minecraft:diamond_leggings");
                     this.command("item replace entity @p armor.feet with minecraft:iron_boots[minecraft:damage=190]");
                     this.command("item replace entity @p weapon.mainhand with minecraft:netherite_sword[minecraft:damage=1900]");
                     this.command("item replace entity @p hotbar.8 with minecraft:stick");
                     this.command("give @p minecraft:golden_apple 12");
                     this.command("give @p minecraft:ender_pearl 16");
                     this.command("give @p minecraft:totem_of_undying 1");
                     this.command("give @p minecraft:cooked_beef 48");
                     this.command("effect give @p minecraft:speed 90 1");
                     this.command("effect give @p minecraft:strength 8 0");
                     this.command("effect give @p minecraft:fire_resistance 600 0");
                     this.mc.options.hideGui = false;
                     break;
                  case 1:
                     this.command(
                        "summon minecraft:villager 0 -60 3.2 {NoAI:1b,Rotation:[180f,0f],Health:200f,attributes:[{id:\"minecraft:max_health\",base:200d}]}"
                     );
                     HudLayouts.apply(HudLayouts.Layout.CLASSIC);

                     for (String id : new String[]{"inventory", "clock", "session", "speed", "cps", "stopwatch"}) {
                        enable(id);
                     }
                     break;
                  case 2:
                     this.checkpoint("hud elements visible");
                     break;
                  case 3:
                     this.attackVillager();
                     break;
                  case 4:
                     this.attackVillager();
                     break;
                  case 5:
                     this.checkpoint("target hud");
                     break;
                  case 6:
                     this.mc.options.hideGui = true;
                     break;
                  case 7:
                     this.mc.options.hideGui = false;
                     break;
                  case 8:
                     HudEditorScreen.open();
                     break;
                  case 9:
                     this.checkpoint("editor with all elements");
                     break;
                  case 10:
                     this.editor(screen -> screen.testClick("tool:done"));
                     break;
                  case 11:
                     setChoice("watermark", "style", "LOGO");
                     setChoice("potions", "layout", "COMPACT");
                     setChoice("armor", "layout", "VERTICAL");
                     setChoice("array_list", "coloring", "ACCENT");
                     setBool("array_list", "hud_modules", true);
                     break;
                  case 12:
                     this.checkpoint("alternative styles");
                     break;
                  case 13:
                     setChoice("watermark", "style", "MINIMAL");

                     for (HudElement element : HudManager.elements()) {
                        setChoice(element.id(), "background", "NONE");
                     }
                     break;
                  case 14:
                     this.checkpoint("no backgrounds");
                     break;
                  case 15:
                     for (HudElement element : HudManager.elements()) {
                        setChoice(element.id(), "background", "GLASS");
                     }

                     setChoice("watermark", "style", "CAPSULE");

                     for (String id : new String[]{"inventory", "clock", "session", "speed", "cps", "stopwatch", "warnings", "keybinds"}) {
                        disable(id);
                     }

                     for (String id : new String[]{"glass_hotbar", "glass_tab", "glass_scoreboard", "glass_bossbar", "titles"}) {
                        enable(id);
                     }

                     this.command("bossbar add rose:event \"Ивент: Воздушный дроп\"");
                     this.command("bossbar set rose:event players @a");
                     this.command("bossbar set rose:event color purple");
                     this.command("bossbar set rose:event style notched_10");
                     this.command("bossbar set rose:event value 64");
                     this.command("scoreboard objectives add rose dummy \"FunTime\"");
                     this.command("scoreboard objectives setdisplay sidebar rose");
                     this.command("scoreboard objectives setdisplay list rose");
                     this.command("scoreboard players set Монеты rose 1250");
                     this.command("scoreboard players set Убийства rose 37");
                     this.command("scoreboard players set Онлайн rose 912");
                     break;
                  case 16:
                     this.command("title @a title {\"text\":\"Rose\",\"color\":\"aqua\"}");
                     break;
                  case 17:
                     this.checkpoint("glass vanilla ui");
                     break;
                  case 18:
                     this.mc.player.getInventory().setSelectedSlot(4);
                     break;
                  case 19:
                     this.mc.options.keyPlayerList.setDown(true);
                     break;
                  case 20:
                     this.checkpoint("glass tab");
                     break;
                  case 21:
                     this.mc.options.keyPlayerList.setDown(false);
                     break;
                  case 22:
                     RoseClient.LOGGER.info("[Rose][worldtest] world finished");
                     break;
                  default:
                     return;
               }

               RoseClient.LOGGER.info("[Rose][worldtest] step {} done, {} fps", this.step, this.mc.getFps());
               this.step++;
            } else if ("visuals".equals(RoseClient.uiTest()) ? this.visuals() : this.cosmetics()) {
               RoseClient.LOGGER.info("[Rose][worldtest] step {} done, {} fps", this.step, this.mc.getFps());
               this.step++;
            }
         }
      }
   }

   private boolean visuals() {
      switch (this.step) {
         case 0:
            this.command("time set 6000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("fill -4 -60 5 4 -57 7 minecraft:oak_planks");
            this.command("fill -1 -60 4 1 -59 4 minecraft:glass");
            this.command("setblock 3 -60 3 minecraft:chest");
            this.command("tp @p 0 -60 0 0 18");
            this.command("clear @p");
            this.command("item replace entity @p hotbar.0 with minecraft:diamond_sword[minecraft:enchantments={\"minecraft:sharpness\":5}]");
            this.command("item replace entity @p hotbar.1 with minecraft:ender_pearl 16");
            this.command("item replace entity @p hotbar.2 with minecraft:bow");
            this.command("summon minecraft:villager 0 -60 2.6 {NoAI:1b,Rotation:[180f,0f],Health:200f,attributes:[{id:\"minecraft:max_health\",base:200d}]}");
            this.command("summon minecraft:item -1.2 -60 1.6 {Item:{id:\"minecraft:golden_apple\",count:3},PickupDelay:32767}");
            this.command("summon minecraft:item 1.3 -60 1.4 {Item:{id:\"minecraft:diamond\",count:5},PickupDelay:32767}");
            this.mc.player.getInventory().setSelectedSlot(0);
            this.mc.options.hideGui = false;
            break;
         case 1:
            for (String id : new String[]{
               "crosshair",
               "block_outline",
               "item_physics",
               "hand",
               "entity_colors",
               "nametags",
               "camera",
               "hit_effects",
               "chat",
               "glass_tooltips",
               "container_animation"
            }) {
               enable(id);
            }

            RoseClient.LOGGER.info("[Rose][worldtest] visuals on");
            break;
         case 2:
            this.attackVillager();
            break;
         case 3:
            this.mc.gui.getChat().addMessage(Component.literal("Rose: визуалы включены"));
            this.mc.gui.getChat().addMessage(Component.literal("Повтор сообщения"));
            this.mc.gui.getChat().addMessage(Component.literal("Повтор сообщения"));
            break;
         case 4:
            this.checkpoint("hit and chat");
            break;
         case 5:
            this.mc.player.getInventory().setSelectedSlot(1);
            enable("trajectories");
            break;
         case 6:
            this.checkpoint("trajectory");
            break;
         case 7:
            this.mc.player.getInventory().setSelectedSlot(0);
            this.mc.setScreen(new InventoryScreen(this.mc.player));
            tooltipDemo = true;
            break;
         case 8:
            this.checkpoint("inventory and tooltip");
            break;
         case 9:
            tooltipDemo = false;
            this.mc.setScreen(null);
            enable("color_grading");
            enable("time_weather");
            setNumber("time_weather", "hour", 18.5);
            enable("sky");
            break;
         case 10:
            this.checkpoint("color grading sunset sky");
            break;
         case 11:
            setNumber("time_weather", "hour", 23.0);
            enable("fullbright");
            break;
         case 12:
            this.checkpoint("night fullbright");
            break;
         case 13:
            disable("fullbright");
            setNumber("time_weather", "hour", 12.0);
            enable("motion_blur");
            this.spinFrames = 24;
            break;
         case 14:
            this.checkpoint("motion blur");
            break;
         case 15:
            disable("motion_blur");
            this.mc.levelRenderer.allChanged();
            break;
         case 16:
            this.checkpoint("chunk animation");
            break;
         case 17:
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            break;
         case 18:
            this.checkpoint("third person nametag");
            break;
         case 19:
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            break;
         case 20:
            disable("color_grading");
            this.command("tp @p 0 -60 0 0 55");
            break;
         case 21:
            this.checkpoint("outline and items");
            break;
         case 22:
            this.mc.player.getInventory().setSelectedSlot(1);
            this.command("tp @p 0 -60 0 180 -12");
            break;
         case 23:
            this.checkpoint("trajectory open field");
            break;
         case 24:
            RoseClient.LOGGER.info("[Rose][worldtest] visuals finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private boolean cosmetics() {
      switch (this.step) {
         case 0:
            this.command("time set 6000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("fill -8 -60 -8 8 -54 12 minecraft:air");
            this.command("effect clear @p");
            this.command("tp @p 0 -60 0 0 10");
            this.command("clear @p");
            this.mc.options.hideGui = true;
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);

            for (String id : new String[]{"hats", "wings", "aura", "trail", "jump_circles", "pet", "cape"}) {
               disable(id);
            }
            break;
         case 1:
            enable("hats");
            enable("wings");
            enable("aura");
            setChoice("hats", "hat", "CHINA");
            setChoice("wings", "style", "ANGEL");
            setChoice("aura", "style", "RING");
            this.shotLater("cos_china_angel", 16);
            break;
         case 2:
            setChoice("hats", "hat", "HALO");
            setChoice("wings", "style", "DEMON");
            setChoice("aura", "style", "ORBIT");
            this.shotLater("cos_halo_demon", 16);
            break;
         case 3:
            setChoice("hats", "hat", "CROWN");
            setChoice("wings", "style", "BUTTERFLY");
            setChoice("aura", "style", "HELIX");
            setJson("wings", "colors", "{\"mode\":\"GRADIENT\",\"colors\":[\"#FFFF9F0A\",\"#FFFF375F\",\"#FFFFD60A\"],\"count\":3,\"speed\":1}");
            this.shotLater("cos_crown_butterfly", 16);
            break;
         case 4:
            setChoice("hats", "hat", "HORNS");
            setChoice("wings", "style", "CRYSTAL");
            setChoice("aura", "style", "PILLARS");
            setJson("wings", "colors", "{\"mode\":\"THEME\"}");
            this.shotLater("cos_horns_crystal", 16);
            break;
         case 5:
            setChoice("hats", "hat", "EARS");
            setChoice("wings", "style", "ENERGY");
            setChoice("aura", "style", "SPARKS");
            enable("pet");
            setChoice("pet", "pet", "FAIRY");
            setJson("wings", "colors", "{\"mode\":\"RAINBOW\",\"speed\":1}");
            this.shotLater("cos_ears_energy", 24);
            break;
         case 6:
            setChoice("hats", "hat", "TOP_HAT");
            setChoice("wings", "style", "ANGEL");
            setChoice("aura", "style", "WAVES");
            setChoice("pet", "pet", "CRYSTAL");
            setJson("wings", "colors", "{\"mode\":\"SINGLE\",\"colors\":[\"#FFFFFFFF\",\"#FFFFFFFF\",\"#FFFFFFFF\"]}");
            this.shotLater("cos_tophat_angel", 16);
            break;
         case 7:
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_FRONT);
            setChoice("hats", "hat", "CHINA");
            disable("pet");
            this.shotLater("cos_front_china", 16);
            break;
         case 8:
            setChoice("hats", "hat", "CROWN");
            this.shotLater("cos_front_crown", 16);
            break;
         case 9:
            setChoice("hats", "hat", "HORNS");
            this.shotLater("cos_front_horns", 16);
            break;
         case 10:
            setChoice("hats", "hat", "HALO");
            setChoice("wings", "style", "DEMON");
            this.shotLater("cos_front_halo", 16);
            break;
         case 11:
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            disable("aura");
            setChoice("hats", "hat", "CHINA");
            enable("trail");
            enable("jump_circles");
            setChoice("trail", "style", "RIBBON");
            setJson("trail", "colors", "{\"mode\":\"RAINBOW\",\"speed\":1}");
            this.walkTicks = 40;
            break;
         case 12:
            this.shot("cos_trail_ribbon");
            break;
         case 13:
            setChoice("trail", "style", "HELIX");
            this.mc.player.setYRot(this.mc.player.getYRot() + 180.0F);
            this.walkTicks = 40;
            break;
         case 14:
            this.shot("cos_trail_helix");
            break;
         case 15:
            setChoice("jump_circles", "style", "HEXAGON");
            this.mc.player.jumpFromGround();
            this.shotLater("cos_jump_hexagon", 5);
            break;
         case 16:
            setChoice("jump_circles", "style", "RING");
            this.mc.player.jumpFromGround();
            this.shotLater("cos_jump_ring", 5);
            break;
         case 17:
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            this.mc.options.hideGui = false;
            disable("trail");
            setChoice("pet", "pet", "FAIRY");
            this.mc.player.setXRot(10.0F);
            this.shotLater("cos_pet_first_person", 30);
            break;
         case 18:
            setChoice("pet", "pet", "STAR");
            enable("aura");
            setChoice("aura", "style", "RING");
            this.mc.player.setXRot(55.0F);
            this.shotLater("cos_first_person_ground", 20);
            break;
         case 19:
            RoseClient.LOGGER.info("[Rose][worldtest] cosmetics finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private boolean wings() {
      switch (this.step) {
         case 0:
            this.command("time set 6000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("effect clear @p");
            this.command("clear @p");
            this.command("tp @p 0 -60 0 0 0");
            this.mc.options.hideGui = true;
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_FRONT);

            for (String id : new String[]{"hats", "aura", "trail", "jump_circles", "pet", "cape"}) {
               disable(id);
            }

            enable("wings");
            setNumber("wings", "size", 200.0);
            setNumber("wings", "life", 100.0);
            setJson("wings", "colors", "{\"mode\":\"SINGLE\",\"colors\":[\"#FFFFFFFF\",\"#FFFFFFFF\",\"#FFFFFFFF\"]}");
            break;
         case 1:
            setChoice("wings", "style", "ANGEL");
            setNumber("wings", "spread", 65.0);
            this.shotLater("wg_angel_65", 18);
            break;
         case 2:
            setNumber("wings", "spread", 100.0);
            this.shotLater("wg_angel_100", 18);
            break;
         case 3:
            setNumber("wings", "spread", 25.0);
            this.shotLater("wg_angel_25", 18);
            break;
         case 4:
            setNumber("wings", "spread", 65.0);
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            this.shotLater("wg_angel_back", 18);
            break;
         case 5:
            setChoice("wings", "style", "DEMON");
            this.shotLater("wg_demon", 18);
            break;
         case 6:
            setChoice("wings", "style", "BUTTERFLY");
            this.shotLater("wg_butterfly", 18);
            break;
         case 7:
            setChoice("wings", "style", "CRYSTAL");
            this.shotLater("wg_crystal", 18);
            break;
         case 8:
            setChoice("wings", "style", "ENERGY");
            this.shotLater("wg_energy", 18);
            break;
         case 9:
            RoseClient.LOGGER.info("[Rose][worldtest] wings finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private boolean life() {
      switch (this.step) {
         case 0:
            this.command("time set 6000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("effect clear @p");
            this.command("clear @p");
            this.command("tp @p 0 -60 0 0 0");
            this.mc.options.hideGui = true;
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);

            for (String id : new String[]{"hats", "wings", "aura", "trail", "jump_circles", "pet", "cape"}) {
               disable(id);
            }
            break;
         case 1:
            this.trailPass("RIBBON", 100.0);
            break;
         case 2:
            this.lookAcross("life_trail_alive");
            break;
         case 3:
            this.shotLater("life_trail_hang_alive", 26);
            break;
         case 4:
            this.trailPass("RIBBON", 0.0);
            break;
         case 5:
            this.lookAcross("life_trail_dead");
            break;
         case 6:
            this.shotLater("life_trail_hang_dead", 26);
            break;
         case 7:
            this.trailPass("SPARKS", 100.0);
            break;
         case 8:
            this.lookAcross("life_sparks_alive");
            break;
         case 9:
            this.trailPass("SPARKS", 0.0);
            break;
         case 10:
            this.lookAcross("life_sparks_dead");
            break;
         case 11:
            disable("trail");
            enable("wings");
            setChoice("wings", "style", "ANGEL");
            setNumber("wings", "life", 100.0);
            this.spinFrames = 22;
            this.shotLater("life_wings_alive", 14);
            break;
         case 12:
            setNumber("wings", "life", 0.0);
            this.spinFrames = 22;
            this.shotLater("life_wings_dead", 14);
            break;
         case 13:
            disable("wings");
            enable("aura");
            setChoice("aura", "style", "RING");
            setNumber("aura", "life", 100.0);
            this.command("tp @p 0 -60 0 0 0");
            this.arc(26, 3.2F);
            this.shotLater("life_aura_alive", 18);
            break;
         case 14:
            setNumber("aura", "life", 0.0);
            this.command("tp @p 0 -60 0 0 0");
            this.arc(26, 3.2F);
            this.shotLater("life_aura_dead", 18);
            break;
         case 15:
            disable("aura");
            enable("pet");
            setChoice("pet", "pet", "DRAGON");
            setNumber("pet", "life", 100.0);
            this.command("tp @p 0 -60 0 0 0");
            this.arc(26, 4.5F);
            this.shotLater("life_pet_alive", 20);
            break;
         case 16:
            setNumber("pet", "life", 0.0);
            this.command("tp @p 0 -60 0 0 0");
            this.arc(26, 4.5F);
            this.shotLater("life_pet_dead", 20);
            break;
         case 17:
            disable("pet");
            enable("hats");
            setChoice("hats", "hat", "HALO");
            setNumber("hats", "life", 100.0);
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_FRONT);
            this.spinFrames = 24;
            this.shotLater("life_hat_alive", 16);
            break;
         case 18:
            setNumber("hats", "life", 0.0);
            this.spinFrames = 24;
            this.shotLater("life_hat_dead", 16);
            break;
         case 19:
            disable("hats");
            this.command("time set 18000");
            enable("sky");
            setJson("sky", "black_hole", "true");
            setJson("sky", "nebula", "true");
            setJson("sky", "hide_clouds", "true");
            setChoice("sky", "nebula_when", "ALWAYS");
            setNumber("sky", "nebula_density", 55.0);
            setNumber("sky", "black_hole_size", 170.0);
            setNumber("sky", "black_hole_height", 30.0);
            setChoice("sky", "black_hole_when", "ALWAYS");
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            this.mc.player.setYRot(-38.0F);
            this.mc.player.setXRot(-30.0F);
            this.shotLater("life_black_hole", 26);
            break;
         case 20:
            setNumber("sky", "black_hole_size", 240.0);
            this.shotLater("life_black_hole_big", 22);
            break;
         case 21:
            setJson("sky", "black_hole", "false");
            this.mc.player.setYRot(125.0F);
            this.mc.player.setXRot(-42.0F);
            this.shotLater("life_nebula", 24);
            break;
         case 22:
            RoseClient.LOGGER.info("[Rose][worldtest] life finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private void trailPass(String style, double lifeValue) {
      disable("trail");
      enable("trail");
      setChoice("trail", "style", style);
      setJson("trail", "colors", "{\"mode\":\"RAINBOW\",\"speed\":1}");
      setNumber("trail", "length", 6.0);
      setNumber("trail", "width", 170.0);
      setNumber("trail", "life", lifeValue);
      this.command("tp @p 0 -60 0 0 0");
      this.arc(26, 9.0F);
   }

   private void arc(int ticks, float turn) {
      this.walkTicks = ticks;
      this.turnPerTick = turn;
      this.walkJumps = false;
   }

   private void lookAcross(String name) {
      this.walkTicks = 0;
      this.mc.player.setYRot(this.mc.player.getYRot() + 150.0F);
      this.shotLater(name, 8);
   }

   private static void setJson(String moduleId, String settingId, String json) {
      Module module = Modules.byId(moduleId);
      if (module != null) {
         module.settings()
            .stream()
            .filter(setting -> setting.id().equals(settingId))
            .findFirst()
            .ifPresent(setting -> setting.fromJson(JsonParser.parseString(json)));
      }
   }

   private void shotLater(String name, int ticks) {
      this.shotName = name;
      this.shotTicks = ticks;
   }

   private void shot(String name) {
      Screenshot.grab(this.mc.gameDirectory, "rose_" + name + ".png", this.mc.getMainRenderTarget(), 1, message -> {});
      RoseClient.LOGGER.info("[Rose][worldtest] shot {}", name);
   }

   private void checkpoint(String name) {
      RoseClient.LOGGER.info("[Rose][worldtest] {}", name);
      this.shot("chk_" + name.replace(' ', '_'));
   }

   private static boolean stage11Mode() {
      String mode = RoseClient.uiTest();
      return mode != null && mode.startsWith("stage11");
   }

   private boolean stage11k() {
      switch (this.step) {
         case 0:
            this.command("time set 6000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("gamemode survival @p");
            this.command("tp @p 0 -60 0 0 62");
            this.command("clear @p");
            this.command("item replace entity @p armor.chest with minecraft:netherite_chestplate");
            this.command("item replace entity @p armor.legs with minecraft:diamond_leggings");
            this.command("item replace entity @p armor.feet with minecraft:golden_boots");
            this.command("item replace entity @p hotbar.0 with minecraft:netherite_sword");
            this.mc.player.getInventory().setSelectedSlot(0);
            this.mc.options.hideGui = false;
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            if (Modules.looks() != null) {
               Modules.looks().apply(Look.CLEAN, false);
            }

            disable("self_view");
            break;
         case 1:
            this.shotLater("s11_self_off", 6);
            break;
         case 2:
            enable("self_view");
            this.shotLater("s11_self_on", 8);
            break;
         case 3:
            setBool("self_view", "arms", true);
            this.shotLater("s11_self_arms", 8);
            break;
         case 4:
            setBool("self_view", "arms", false);
            enable("wings");
            this.mc.player.setXRot(35.0F);
            this.shotLater("s11_self_wings", 10);
            break;
         case 5:
            disable("self_view");
            disable("wings");
            this.command("gamemode creative @p");
            RoseClient.LOGGER.info("[Rose][worldtest] stage11 finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private boolean stage11j() {
      switch (this.step) {
         case 0:
            this.command("time set 6000");
            this.command("weather clear");
            this.command("gamemode creative @p");
            this.mc.options.hideGui = true;
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            if (Modules.looks() != null) {
               Modules.looks().apply(Look.CLEAN, false);
            }

            enable("chunk_fade");
            setChoice("chunk_fade", "style", "FOG");
            setNumber("chunk_fade", "duration", 1.5);
            setNumber("chunk_fade", "spread", 1.5);
            setBool("chunk_fade", "nearby", true);
            this.command("tp @p 7000 -55 7000 -45 6");
            break;
         case 1:
            this.shotLater("s11_fade_1", 8);
            break;
         case 2:
            this.command("tp @p 9000 -55 9000 -45 6");
            this.shotLater("s11_fade_2", 16);
            break;
         case 3:
            this.command("tp @p 11000 -55 11000 -45 6");
            setChoice("chunk_fade", "style", "DUSK");
            break;
         case 4:
            this.shotLater("s11_fade_dusk", 10);
            break;
         case 5:
            this.command("tp @p 0 -60 0 0 0");
            disable("chunk_fade");
            this.mc.options.hideGui = false;
            RoseClient.LOGGER.info("[Rose][worldtest] stage11 finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private boolean stage11i() {
      switch (this.step) {
         case 0:
            this.command("time set 6000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("gamemode creative @p");
            this.command("tp @p 0 -60 0 0 -8");
            this.mc.options.hideGui = false;
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            if (Modules.looks() != null) {
               Modules.looks().apply(Look.SIGNATURE, false);
            }

            enable("island");
            disable("music");
            NowPlaying.override(null);
            break;
         case 1:
            this.shot("s11_island_idle");
            break;
         case 2:
            Notifications.show(Notifications.Kind.SUCCESS, "Аура", "Модуль включён");
            this.shotLater("s11_island_notice", 12);
            break;
         case 3:
            enable("music");
            NowPlaying.override(new NowPlaying.Track("Daft Punk", "Harder, Better, Faster, Stronger (Alive 2007)", true, "Spotify"));
            break;
         case 4:
            this.shotLater("s11_island_music", 18);
            break;
         case 5:
            RoseClient.LOGGER.info("[Rose][worldtest] island music settle");
            break;
         case 6:
            if (Modules.island() != null) {
               Modules.island().victory(this.mc.player);
            }

            this.shotLater("s11_island_victory", 12);
            break;
         case 7:
            NowPlaying.override(null);
            disable("music");
            RoseClient.LOGGER.info("[Rose][worldtest] stage11 finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private void openBlock() {
      BlockHitResult hit = new BlockHitResult(new Vec3(0.5, -59.5, 2.0), Direction.NORTH, new BlockPos(0, -60, 2), false);
      this.mc.gameMode.useItemOn(this.mc.player, InteractionHand.MAIN_HAND, hit);
   }

   private boolean stage11h() {
      switch (this.step) {
         case 0:
            this.command("time set 6000");
            this.command("kill @e[type=!minecraft:player]");
            this.command("gamemode survival @p");
            this.command("tp @p 0 -60 0 0 30");
            this.command("clear @p");
            this.command("item replace entity @p hotbar.0 with minecraft:diamond_sword");
            this.command("give @p minecraft:iron_ingot 5");
            this.command("experience set @p 30 levels");
            this.mc.player.getInventory().setSelectedSlot(0);
            this.mc.options.hideGui = false;
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            if (Modules.looks() != null) {
               Modules.looks().apply(Look.SIGNATURE, false);
            }

            this.command(
               "setblock 0 -60 2 minecraft:furnace[lit=true]{BurnTime:900s,CookTime:120s,CookTimeTotal:200s,Items:[{Slot:0b,id:\"minecraft:raw_iron\",count:16},{Slot:1b,id:\"minecraft:coal\",count:8},{Slot:2b,id:\"minecraft:iron_ingot\",count:3}]}"
            );
            break;
         case 1:
            this.openBlock();
            break;
         case 2:
            this.shot("s11_glass_furnace");
            break;
         case 3:
            this.mc.setScreen(null);
            this.command(
               "setblock 0 -60 2 minecraft:brewing_stand{BrewTime:230s,Fuel:13b,Items:[{Slot:0b,id:\"minecraft:potion\",count:1,components:{\"minecraft:potion_contents\":{potion:\"minecraft:water\"}}},{Slot:2b,id:\"minecraft:potion\",count:1,components:{\"minecraft:potion_contents\":{potion:\"minecraft:water\"}}},{Slot:3b,id:\"minecraft:nether_wart\",count:4}]}"
            );
            break;
         case 4:
            this.openBlock();
            break;
         case 5:
            this.shot("s11_glass_brewing");
            break;
         case 6:
            this.mc.setScreen(null);
            this.command("setblock 0 -60 2 minecraft:anvil");
            break;
         case 7:
            this.openBlock();
            break;
         case 8:
            if (this.mc.screen instanceof AbstractContainerScreen<?> screen) {
               for (Slot slot : screen.getMenu().slots) {
                  if (slot.container instanceof Inventory && slot.getContainerSlot() == 0) {
                     this.mc.gameMode.handleInventoryMouseClick(screen.getMenu().containerId, slot.index, 0, ClickType.QUICK_MOVE, this.mc.player);
                  }
               }
            }
            break;
         case 9:
            this.shot("s11_glass_anvil");
            break;
         case 10:
            this.mc.setScreen(null);
            this.command("setblock 0 -60 2 minecraft:air");
            this.command("gamemode creative @p");
            RoseClient.LOGGER.info("[Rose][worldtest] stage11 finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private boolean stage11g() {
      switch (this.step) {
         case 0:
            this.command("time set 14000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("gamemode creative @p");
            this.command("tp @p 0 -60 0 0 4");
            this.command("clear @p");
            this.command("item replace entity @p hotbar.0 with minecraft:netherite_sword");
            this.command("item replace entity @p hotbar.1 with minecraft:golden_apple");
            this.command("item replace entity @p hotbar.2 with minecraft:diamond_pickaxe");
            this.command("item replace entity @p weapon.offhand with minecraft:totem_of_undying");
            this.mc.player.getInventory().setSelectedSlot(0);
            this.mc.options.hideGui = false;
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            if (Modules.looks() != null) {
               Modules.looks().apply(Look.BLOOD, false);
            }

            disable("item_glow");
            break;
         case 1:
            this.shot("s11_item_glow_off");
            break;
         case 2:
            enable("item_glow");
            this.shotLater("s11_item_glow_sword", 14);
            break;
         case 3:
            if (Modules.looks() != null) {
               Modules.looks().apply(Look.POLAR, false);
            }

            this.mc.player.getInventory().setSelectedSlot(2);
            this.shotLater("s11_item_glow_pickaxe", 14);
            break;
         case 4:
            this.command("time set 6000");
            if (Modules.looks() != null) {
               Modules.looks().apply(Look.SIGNATURE, false);
            }

            this.mc.player.getInventory().setSelectedSlot(1);
            this.shotLater("s11_item_glow_day", 14);
            break;
         case 5:
            disable("item_glow");
            RoseClient.LOGGER.info("[Rose][worldtest] stage11 finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private boolean stage11lock() {
      switch (this.step) {
         case 0:
            this.command("time set 6000");
            this.command("gamemode survival @p");
            this.command("tp @p 0 -60 0 0 6");
            this.mc.options.hideGui = false;
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            break;
         case 1:
            this.shot("s11_lock_world");
            break;
         case 2:
            this.mc.setScreen(new InventoryScreen(this.mc.player));
            break;
         case 3:
            this.shot("s11_lock_inventory");
            break;
         case 4:
            this.mc.setScreen(null);
            this.mc.player.swing(InteractionHand.MAIN_HAND);
            this.mc.player.jumpFromGround();
            break;
         case 5:
            this.command("gamemode creative @p");
            RoseClient.LOGGER.info("[Rose][worldtest] stage11 finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private boolean stage11f() {
      switch (this.step) {
         case 0:
            this.command("time set 13000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("fill -6 -61 -6 6 -57 12 minecraft:air");
            this.command("fill -6 -61 -6 6 -61 12 minecraft:grass_block");
            this.command("gamemode survival @p");
            this.command("tp @p 0 -60 0 0 6");
            this.command("clear @p");
            this.command("item replace entity @p hotbar.0 with minecraft:netherite_sword");
            this.command("item replace entity @p hotbar.1 with minecraft:totem_of_undying");
            this.command("give @p minecraft:golden_apple 12");
            this.command("setblock 0 -60 2 minecraft:chest");
            this.command("item replace block 0 -60 2 container.0 with minecraft:diamond 32");
            this.command("item replace block 0 -60 2 container.2 with minecraft:diamond_sword");
            this.command("item replace block 0 -60 2 container.4 with minecraft:emerald 12");
            this.command("item replace block 0 -60 2 container.10 with minecraft:iron_ingot 40");
            this.command("item replace block 0 -60 2 container.13 with minecraft:diamond_pickaxe");
            this.command("item replace block 0 -60 2 container.20 with minecraft:cobblestone 64");
            this.mc.player.getInventory().setSelectedSlot(0);
            this.mc.options.hideGui = false;
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            if (Modules.looks() != null) {
               Modules.looks().apply(Look.CLEAN, false);
            }

            enable("item_locks");
            enable("container_search");
            enable("waypoints");
            if (Modules.waypoints() != null) {
               Modules.waypoints().clearAll();
            }
            break;
         case 1:
            this.mc.setScreen(new InventoryScreen(this.mc.player));
            break;
         case 2:
            if (this.mc.screen instanceof AbstractContainerScreen<?> screen && Modules.itemLocks() != null) {
               for (Slot slot : screen.getMenu().slots) {
                  if (slot.container instanceof Inventory && (slot.getContainerSlot() == 0 || slot.getContainerSlot() == 1)) {
                     Modules.itemLocks().toggle(slot);
                  }
               }
            }

            this.shotLater("s11_item_locks", 8);
            break;
         case 3:
            this.mc.setScreen(null);
            this.mc.player.setXRot(30.0F);
            BlockHitResult hit = new BlockHitResult(new Vec3(0.5, -59.5, 2.0), Direction.NORTH, new BlockPos(0, -60, 2), false);
            this.mc.gameMode.useItemOn(this.mc.player, InteractionHand.MAIN_HAND, hit);
            break;
         case 4:
            if (this.mc.screen != null && Modules.containerSearch() != null && Modules.containerSearch().box(this.mc.screen) != null) {
               Modules.containerSearch().box(this.mc.screen).setValue("diam");
            }

            this.shotLater("s11_container_search", 10);
            break;
         case 5:
            this.mc.setScreen(null);
            this.command("tp @p 0 -60 0 -16 1");
            if (Modules.waypoints() != null) {
               Modules.waypoints().mark("База", 6.5, -60.0, 22.5, false);
               Modules.waypoints().mark("Смерть", -9.5, -60.0, 30.5, true);
               Modules.waypoints().mark("Портал", 180.5, -60.0, 420.5, false);
            }

            this.shotLater("s11_waypoints", 16);
            break;
         case 6:
            if (Modules.waypoints() != null) {
               Modules.waypoints().clearAll();
            }

            this.command("tp @p 0 -60 0 0 6");
            enable("swing_trail");
            setJson("swing_trail", "colors", "{\"mode\":\"GRADIENT\",\"colors\":[\"#FFFF2D3D\",\"#FFFF8A3D\",\"#FFFFFFFF\"],\"count\":2,\"speed\":1}");
            this.mc.player.setXRot(6.0F);
            break;
         case 7:
            this.mc.player.swing(InteractionHand.MAIN_HAND);
            this.shotLater("s11_swing_trail_3", 2);
            break;
         case 8:
            this.mc.player.swing(InteractionHand.MAIN_HAND);
            this.shotLater("s11_swing_trail_4", 3);
            break;
         case 9:
            disable("swing_trail");
            this.command("gamemode creative @p");
            if (this.mc.screen == null && Modules.itemLocks() != null) {
               this.mc.setScreen(new InventoryScreen(this.mc.player));
            }

            RoseClient.LOGGER.info("[Rose][worldtest] stage11 finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private boolean stage11e() {
      switch (this.step) {
         case 0:
            this.command("time set 18000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("fill -6 -61 -6 6 -57 12 minecraft:air");
            this.command("fill -6 -61 -6 6 -61 12 minecraft:grass_block");
            this.command("setblock 0 -60 3 minecraft:deepslate_diamond_ore");
            this.command("tp @p 0 -60 0 0 6");
            this.command("clear @p");
            this.command("item replace entity @p hotbar.0 with minecraft:netherite_sword");
            this.mc.player.getInventory().setSelectedSlot(0);
            this.mc.options.hideGui = false;
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            disable("hand");
            disable("aspect_ratio");
            if (Modules.looks() != null) {
               MenuScreen.remember(Modules.looks());
            }

            this.mc.setScreen(new MenuScreen(null));
            break;
         case 1:
            this.shot("s11_looks_page");
            break;
         case 2:
            this.mc.setScreen(null);
            Modules.looks().apply(Look.BLOOD, true);
            this.mc.options.hideGui = true;
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            this.shotLater("s11_look_blood", 34);
            break;
         case 3:
            RoseClient.LOGGER.info("[Rose][worldtest] look settle");
            break;
         case 4:
            Modules.looks().apply(Look.POLAR, false);
            this.shotLater("s11_look_polar", 34);
            break;
         case 5:
            RoseClient.LOGGER.info("[Rose][worldtest] look settle");
            break;
         case 6:
            this.command("time set 6000");
            Modules.looks().apply(Look.SAKURA, false);
            this.shotLater("s11_look_sakura", 34);
            break;
         case 7:
            RoseClient.LOGGER.info("[Rose][worldtest] look settle");
            break;
         case 8:
            this.command("time set 13000");
            Modules.looks().apply(Look.CLEAN, false);
            this.mc.options.hideGui = false;
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            enable("swing_trail");
            setJson("swing_trail", "colors", "{\"mode\":\"GRADIENT\",\"colors\":[\"#FFFF2D3D\",\"#FFFF8A3D\",\"#FFFFFFFF\"],\"count\":2,\"speed\":1}");
            break;
         case 9:
            this.mc.player.swing(InteractionHand.MAIN_HAND);
            this.shotLater("s11_swing_trail_1", 3);
            break;
         case 10:
            this.mc.player.swing(InteractionHand.MAIN_HAND);
            this.shotLater("s11_swing_trail_2", 5);
            break;
         case 11:
            disable("swing_trail");
            enable("block_info");
            this.mc.player.setXRot(18.0F);
            this.shotLater("s11_block_info", 14);
            break;
         case 12:
            enable("aspect_ratio");
            this.shotLater("s11_aspect_4_3", 10);
            break;
         case 13:
            disable("aspect_ratio");
            disable("block_info");
            RoseClient.LOGGER.info("[Rose][worldtest] stage11 finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private boolean stage11d() {
      float[][] poses = new float[][]{
         {-30.0F, -6.0F, -10.0F, 0.0F, 0.0F, 0.0F, 150.0F},
         {-30.0F, -6.0F, -10.0F, -12.0F, 0.0F, 14.0F, 150.0F},
         {-26.0F, -10.0F, 0.0F, 0.0F, 12.0F, -18.0F, 160.0F},
         {-26.0F, -10.0F, 0.0F, 10.0F, -8.0F, 22.0F, 160.0F},
         {-40.0F, -4.0F, -30.0F, -20.0F, 8.0F, 30.0F, 170.0F},
         {-22.0F, -12.0F, 6.0F, 14.0F, 6.0F, -30.0F, 165.0F}
      };
      if (this.step == 0) {
         this.command("time set 6000");
         this.command("kill @e[type=!minecraft:player]");
         this.command("fill -6 -61 -6 6 -57 12 minecraft:air");
         this.command("fill -6 -61 -6 6 -61 12 minecraft:grass_block");
         this.command("tp @p 0 -60 0 0 8");
         this.command("clear @p");
         this.command("item replace entity @p hotbar.0 with minecraft:netherite_sword");
         this.command("give @p minecraft:golden_apple 12");
         this.command("give @p minecraft:ender_pearl 16");
         this.command("give @p minecraft:totem_of_undying 1");
         this.command("item replace entity @p armor.chest with minecraft:netherite_chestplate");
         this.command("item replace entity @p armor.head with minecraft:diamond_helmet");
         this.mc.player.getInventory().setSelectedSlot(0);
         this.mc.options.hideGui = false;
         this.mc.options.setCameraType(CameraType.FIRST_PERSON);

         for (String id : new String[]{
            "hats",
            "wings",
            "aura",
            "trail",
            "jump_circles",
            "pet",
            "kill_effects",
            "ambient_particles",
            "film_effects",
            "keystrokes",
            "item_counter",
            "music",
            "block_outline",
            "hit_effects",
            "afterimage",
            "sky"
         }) {
            disable(id);
         }

         enable("hand");
         return true;
      } else if (this.step >= 1 && this.step <= poses.length) {
         float[] pose = poses[this.step - 1];
         String[] ids = new String[]{"main_x", "main_y", "main_z", "main_rot_x", "main_rot_y", "main_rot_z", "main_scale"};

         for (int i = 0; i < ids.length; i++) {
            setNumber("hand", ids[i], pose[i]);
         }

         this.shotLater("s11_pose_" + this.step, 8);
         return true;
      } else if (this.step == poses.length + 1) {
         choose("hand", "preset", "NORMAL");
         disable("hand");
         this.command("gamemode survival @p");
         return true;
      } else if (this.step == poses.length + 2) {
         this.mc.setScreen(new InventoryScreen(this.mc.player));
         this.shotLater("s11_inventory", 16);
         return true;
      } else if (this.step == poses.length + 3) {
         this.mc.setScreen(null);
         this.command("gamemode creative @p");
         RoseClient.LOGGER.info("[Rose][worldtest] stage11 finished");
         return true;
      } else {
         return false;
      }
   }

   private boolean stage11c() {
      switch (this.step) {
         case 0:
            this.command("time set 6000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("fill -6 -61 -6 6 -57 12 minecraft:air");
            this.command("fill -6 -61 -6 6 -61 12 minecraft:grass_block");
            this.command("tp @p 0 -60 0 0 8");
            this.command("clear @p");
            this.command("item replace entity @p hotbar.0 with minecraft:netherite_sword");
            this.command("give @p minecraft:golden_apple 12");
            this.command("give @p minecraft:ender_pearl 16");
            this.command("give @p minecraft:totem_of_undying 1");
            this.command("give @p minecraft:diamond_pickaxe 1");
            this.command("give @p minecraft:oak_planks 64");
            this.command("item replace entity @p armor.chest with minecraft:netherite_chestplate");
            this.command("setblock 0 -60 2 minecraft:chest");
            this.command("item replace block 0 -60 2 container.0 with minecraft:diamond 32");
            this.command("item replace block 0 -60 2 container.4 with minecraft:emerald 12");
            this.command("item replace block 0 -60 2 container.13 with minecraft:enchanted_golden_apple 3");
            this.mc.player.getInventory().setSelectedSlot(0);
            this.mc.options.hideGui = true;
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_FRONT);

            for (String id : new String[]{
               "hats",
               "wings",
               "aura",
               "trail",
               "jump_circles",
               "hand",
               "kill_effects",
               "ambient_particles",
               "film_effects",
               "keystrokes",
               "item_counter",
               "music",
               "block_outline",
               "hit_effects",
               "afterimage",
               "sky"
            }) {
               disable(id);
            }

            enable("pet");
            setNumber("pet", "size", 140.0);
            setChoice("pet", "pet", "KING");
            break;
         case 1:
            this.shotLater("s11_pet_king", 24);
            break;
         case 2:
            setChoice("pet", "pet", "CAT");
            this.shotLater("s11_pet_cat", 20);
            break;
         case 3:
            setChoice("pet", "pet", "DRAGON");
            this.shotLater("s11_pet_dragon", 20);
            break;
         case 4:
            setChoice("pet", "pet", "GHOST");
            this.shotLater("s11_pet_ghost", 20);
            break;
         case 5:
            setChoice("pet", "pet", "BEE");
            this.shotLater("s11_pet_bee", 20);
            break;
         case 6:
            setChoice("pet", "pet", "SLIME");
            this.shotLater("s11_pet_slime", 20);
            break;
         case 7:
            setChoice("pet", "pet", "KING");
            setNumber("pet", "size", 100.0);
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            this.mc.options.hideGui = false;
            this.shotLater("s11_pet_first_person", 24);
            break;
         case 8:
            disable("pet");
            this.mc.setScreen(new InventoryScreen(this.mc.player));
            this.shotLater("s11_inventory", 16);
            break;
         case 9:
            this.mc.setScreen(null);
            this.mc.player.setXRot(30.0F);
            BlockHitResult hit = new BlockHitResult(new Vec3(0.5, -59.5, 2.0), Direction.NORTH, new BlockPos(0, -60, 2), false);
            this.mc.gameMode.useItemOn(this.mc.player, InteractionHand.MAIN_HAND, hit);
            this.shotLater("s11_chest", 18);
            break;
         case 10:
            this.mc.setScreen(null);
            this.mc.player.setXRot(8.0F);
            enable("watermark");
            Module watermark = Modules.byId("watermark");
            if (watermark != null) {
               MenuScreen.remember(watermark);
            }

            this.mc.setScreen(new MenuScreen(null));
            this.shotLater("s11_hud_page", 16);
            break;
         case 11:
            if (Modules.byId("watermark") instanceof HudElement element) {
               HudEditorScreen.open(element);
            }

            this.shotLater("s11_hud_editor_focus", 22);
            break;
         case 12:
            this.mc.setScreen(null);
            enable("hand");
            choose("hand", "preset", "AURA");
            this.shotLater("s11_hand_aura", 12);
            break;
         case 13:
            this.mc.player.swing(InteractionHand.MAIN_HAND);
            this.shotLater("s11_hand_aura_swing", 3);
            break;
         case 14:
            choose("hand", "preset", "NORMAL");
            disable("hand");
            RoseClient.LOGGER.info("[Rose][worldtest] stage11 finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private boolean stage11b() {
      switch (this.step) {
         case 0:
            this.command("time set 18000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("tp @p 0 -60 0 0 -30");
            this.command("clear @p");
            this.mc.options.hideGui = true;
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);

            for (String id : new String[]{
               "hats",
               "wings",
               "aura",
               "trail",
               "jump_circles",
               "pet",
               "hand",
               "kill_effects",
               "ambient_particles",
               "film_effects",
               "keystrokes",
               "item_counter",
               "music",
               "block_outline",
               "hit_effects",
               "afterimage"
            }) {
               disable(id);
            }

            enable("sky");
            choose("sky", "preset", "POLAR");
            break;
         case 1:
            this.shotLater("s11_sky_polar", 26);
            break;
         case 2:
            this.mc.player.setYRot(120.0F);
            this.mc.player.setXRot(-52.0F);
            this.shotLater("s11_sky_polar_up", 20);
            break;
         case 3:
            choose("sky", "preset", "BLOOD");
            this.mc.player.setYRot(0.0F);
            this.mc.player.setXRot(-24.0F);
            this.shotLater("s11_sky_blood", 20);
            break;
         case 4:
            choose("sky", "preset", "SPACE");
            this.mc.player.setXRot(-40.0F);
            this.shotLater("s11_sky_space", 20);
            break;
         case 5:
            choose("sky", "preset", "POLAR");
            this.mc.player.setXRot(6.0F);
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            enable("aura");
            setChoice("aura", "style", "SURGE");
            this.shotLater("s11_aura_surge", 20);
            break;
         case 6:
            setChoice("aura", "style", "FLAME");
            this.shotLater("s11_aura_flame", 20);
            break;
         case 7:
            choose("sky", "preset", "BLOOD");
            setChoice("aura", "style", "SURGE");
            setJson("aura", "colors", "{\"mode\":\"GRADIENT\",\"colors\":[\"#FFFF2D3D\",\"#FFFF8A3D\",\"#FFFFFFFF\"],\"count\":2,\"speed\":1}");
            this.shotLater("s11_aura_surge_red", 20);
            break;
         case 8:
            disable("aura");
            choose("sky", "preset", "POLAR");
            enable("afterimage");
            setChoice("afterimage", "style", "GHOST");
            setNumber("afterimage", "lifetime", 1.6);
            this.mc.player.setYRot(0.0F);
            this.mc.player.setXRot(10.0F);
            this.walkTicks = 36;
            this.shotLater("s11_echo_ghost", 26);
            break;
         case 9:
            RoseClient.LOGGER.info("[Rose][worldtest] echo settle");
            break;
         case 10:
            setChoice("afterimage", "style", "HOLOGRAM");
            setJson("afterimage", "colors", "{\"mode\":\"GRADIENT\",\"colors\":[\"#FFFF2D3D\",\"#FFFF8A3D\",\"#FFFFFFFF\"],\"count\":2,\"speed\":1}");
            this.mc.player.setYRot(0.0F);
            this.mc.player.setXRot(10.0F);
            this.walkTicks = 36;
            this.shotLater("s11_echo_hologram", 26);
            break;
         case 11:
            RoseClient.LOGGER.info("[Rose][worldtest] echo settle");
            break;
         case 12:
            setChoice("afterimage", "style", "STATUE");
            this.mc.player.setYRot(0.0F);
            this.mc.player.setXRot(10.0F);
            this.walkTicks = 36;
            this.shotLater("s11_echo_statue", 26);
            break;
         case 13:
            RoseClient.LOGGER.info("[Rose][worldtest] echo settle");
            break;
         case 14:
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            this.mc.options.hideGui = false;
            disable("afterimage");
            disable("sky");
            RoseClient.LOGGER.info("[Rose][worldtest] stage11 finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private boolean stage11() {
      switch (this.step) {
         case 0:
            this.command("time set 13000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("fill -6 -61 -6 6 -57 12 minecraft:air");
            this.command("fill -6 -61 -6 6 -61 12 minecraft:grass_block");
            this.command("tp @p 0 -60 0 0 8");
            this.command("clear @p");
            this.command("item replace entity @p hotbar.0 with minecraft:netherite_sword");
            this.command(
               "summon minecraft:villager 0 -60 3.0 {NoAI:1b,Silent:1b,Rotation:[180f,0f],Health:400f,attributes:[{id:\"minecraft:max_health\",base:400d}]}"
            );
            this.mc.player.getInventory().setSelectedSlot(0);
            this.mc.options.hideGui = true;
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);

            for (String id : new String[]{
               "hats",
               "wings",
               "aura",
               "trail",
               "jump_circles",
               "pet",
               "hand",
               "kill_effects",
               "ambient_particles",
               "film_effects",
               "keystrokes",
               "item_counter",
               "music",
               "block_outline"
            }) {
               disable(id);
            }

            enable("hit_effects");
            break;
         case 1:
            this.hit("STARS", "GLOW", "s11_hit_stars", 5);
            break;
         case 2:
            this.hit("HEARTS", "BOX", "s11_hit_hearts", 4);
            break;
         case 3:
            this.hit("SPARKS", "OUTLINE", "s11_hit_sparks", 3);
            break;
         case 4:
            this.hit("SLASHES", "CORNERS", "s11_hit_slashes", 3);
            break;
         case 5:
            this.hit("RINGS", "GLOW", "s11_hit_rings", 3);
            break;
         case 6:
            this.hit("BUBBLES", "NONE", "s11_hit_bubbles", 9);
            break;
         case 7:
            this.hit("CRYSTALS", "BOX", "s11_hit_crystals", 3);
            break;
         case 8:
            setJson("hit_effects", "colors", "{\"mode\":\"GRADIENT\",\"colors\":[\"#FFFF2D3D\",\"#FFFF8A3D\",\"#FFFFFFFF\"],\"count\":2,\"speed\":1}");
            this.hit("SLASHES", "GLOW", "s11_hit_slashes_red", 3);
            break;
         case 9:
            disable("hit_effects");
            this.command("tp @e[type=minecraft:villager] 60 -60 60");
            this.mc.options.hideGui = false;
            this.command("setblock 0 -60 3 minecraft:stone");
            this.command("setblock 1 -60 3 minecraft:oak_stairs");
            this.command("setblock -1 -60 3 minecraft:oak_fence");
            this.mc.player.setXRot(20.0F);
            enable("block_outline");
            setChoice("block_outline", "style", "GLASS");
            this.shotLater("s11_block_glass", 14);
            break;
         case 10:
            setChoice("block_outline", "style", "AURA");
            this.shotLater("s11_block_aura", 14);
            break;
         case 11:
            setChoice("block_outline", "style", "FILL");
            setNumber("block_outline", "fill", 60.0);
            this.shotLater("s11_block_fill", 14);
            break;
         case 12:
            setChoice("block_outline", "style", "DASH");
            this.shotLater("s11_block_dash", 14);
            break;
         case 13:
            setChoice("block_outline", "style", "CORNERS");
            this.mc.player.setYRot(-22.0F);
            this.shotLater("s11_block_corners_stairs", 14);
            break;
         case 14:
            disable("block_outline");
            this.mc.options.hideGui = true;
            this.command("fill -2 -60 3 2 -60 3 minecraft:air");
            this.mc.player.setYRot(0.0F);
            this.mc.player.setXRot(12.0F);
            this.mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            enable("wings");
            setChoice("wings", "style", "ANGEL");
            this.shotLater("s11_wings_ground", 16);
            break;
         case 15:
            this.mc.player.jumpFromGround();
            this.shotLater("s11_wings_jump_up", 4);
            break;
         case 16:
            this.mc.player.jumpFromGround();
            this.shotLater("s11_wings_jump_fall", 10);
            break;
         case 17:
            this.walkTicks = 30;
            this.shotLater("s11_wings_run_jump", 14);
            break;
         case 18:
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            this.mc.options.hideGui = false;
            disable("wings");
            RoseClient.LOGGER.info("[Rose][worldtest] stage11 finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private void hit(String style, String flash, String name, int ticks) {
      setChoice("hit_effects", "style", style);
      setChoice("hit_effects", "flash", flash);
      this.attackVillager();
      this.shotLater(name, ticks);
   }

   private boolean newModules() {
      switch (this.step) {
         case 0:
            this.command("time set 13000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("tp @p 0 -60 0 0 -5");
            this.command("clear @p");
            this.command("give @p minecraft:totem_of_undying 3");
            this.command("give @p minecraft:golden_apple 5");
            this.command("give @p minecraft:ender_pearl 16");
            this.mc.options.hideGui = false;
            this.mc.options.setCameraType(CameraType.FIRST_PERSON);
            NowPlaying.override(new NowPlaying.Track("Rose", "Ночной город · Liquid Glass", true, "Spotify"));

            for (String id : new String[]{"hats", "wings", "aura", "trail", "jump_circles", "pet", "hand", "kill_effects"}) {
               disable(id);
            }

            for (String id : new String[]{"keystrokes", "item_counter", "music", "film_effects", "ambient_particles"}) {
               enable(id);
            }

            setChoice("ambient_particles", "style", "SAKURA");
            break;
         case 1:
            this.mc.options.keyUp.setDown(true);
            this.mc.options.keyAttack.setDown(true);
            this.shotLater("nm_sakura", 20);
            break;
         case 2:
            this.mc.options.keyUp.setDown(false);
            this.mc.options.keyAttack.setDown(false);
            setChoice("ambient_particles", "style", "SNOW");
            this.shotLater("nm_snow", 22);
            break;
         case 3:
            setChoice("ambient_particles", "style", "EMBERS");
            setNumber("film_effects", "letterbox", 10.0);
            setNumber("film_effects", "tint", 14.0);
            this.shotLater("nm_embers_film", 22);
            break;
         case 4:
            setNumber("film_effects", "letterbox", 0.0);
            setNumber("film_effects", "tint", 0.0);
            disable("ambient_particles");
            this.killPreview("SOULS");
            this.shotLater("nm_kill_souls", 16);
            break;
         case 5:
            this.killPreview("LIGHTNING");
            this.shotLater("nm_kill_lightning", 6);
            break;
         case 6:
            this.killPreview("CONFETTI");
            this.shotLater("nm_kill_confetti", 14);
            break;
         case 7:
            this.killPreview("SHOCKWAVE");
            this.shotLater("nm_kill_shockwave", 8);
            break;
         case 8:
            this.killPreview("FLASH");
            this.shotLater("nm_kill_flash", 3);
            break;
         case 9:
            Module hits = Modules.byId("hit_sounds");
            if (hits != null) {
               MenuScreen.remember(hits);
            }

            this.mc.setScreen(new MenuScreen(null));
            break;
         case 10:
            this.shot("nm_menu_sounds");
            break;
         case 11:
            Module music = Modules.byId("music");
            if (music != null) {
               MenuScreen.remember(music);
            }

            this.mc.setScreen(new MenuScreen(null));
            break;
         case 12:
            this.shot("nm_menu_music");
            break;
         case 13:
            this.mc.setScreen(null);
            NowPlaying.override(null);
            RoseClient.LOGGER.info("[Rose][worldtest] newmods finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private void killPreview(String style) {
      disable("kill_effects");
      enable("kill_effects");
      setChoice("kill_effects", "style", style);
      KillEffectsModule effects = Modules.killEffects();
      if (effects != null) {
         Vec3 look = this.mc.player.getLookAngle();
         effects.preview(this.mc.player.position().add(look.x * 4.0, 0.0, look.z * 4.0));
      }
   }

   private boolean viewmodel() {
      switch (this.step) {
         case 0:
            this.command("time set 6000");
            this.command("weather clear");
            this.command("kill @e[type=!minecraft:player]");
            this.command("fill -6 -61 3 6 -57 12 minecraft:air");
            this.command("tp @p 0 -60 0 0 8");
            this.command("clear @p");
            this.command("item replace entity @p hotbar.0 with minecraft:diamond_sword[minecraft:enchantments={\"minecraft:sharpness\":5}]");
            this.command("item replace entity @p weapon.offhand with minecraft:totem_of_undying");
            this.mc.player.getInventory().setSelectedSlot(0);
            this.mc.options.hideGui = false;
            disable("pet");
            enable("hand");
            setChoice("hand", "swing_style", "SWIPE");
            break;
         case 1:
            choose("hand", "preset", "NORMAL");
            this.shotLater("vm_normal", 6);
            break;
         case 2:
            choose("hand", "preset", "FAR");
            this.shotLater("vm_far", 6);
            break;
         case 3:
            choose("hand", "preset", "SIDEWAYS");
            this.shotLater("vm_sideways", 6);
            break;
         case 4:
            choose("hand", "preset", "TILTED");
            this.shotLater("vm_tilted", 6);
            break;
         case 5:
            choose("hand", "preset", "HUGE");
            this.shotLater("vm_huge", 6);
            break;
         case 6:
            choose("hand", "preset", "NORMAL");
            setChoice("hand", "swing_style", "SWIPE");
            setNumber("hand", "swing_speed", 50.0);
            this.mc.player.swing(InteractionHand.MAIN_HAND);
            this.shotLater("vm_swipe", 4);
            break;
         case 7:
            setChoice("hand", "swing_style", "CHOP");
            this.mc.player.swing(InteractionHand.MAIN_HAND);
            this.shotLater("vm_chop", 5);
            break;
         case 8:
            setChoice("hand", "swing_style", "SPIN");
            this.mc.player.swing(InteractionHand.MAIN_HAND);
            this.shotLater("vm_spin", 5);
            break;
         case 9:
            setNumber("hand", "swing_speed", 100.0);
            this.mc.options.hideGui = false;
            MenuScreen.remember(Modules.hand());
            this.mc.setScreen(new MenuScreen(null));
            break;
         case 10:
            this.shot("vm_menu");
            break;
         case 11:
            if (this.mc.screen instanceof MenuScreen menu) {
               menu.testScroll("page:view", -8.0);
            }
            break;
         case 12:
            this.shot("vm_menu_scrolled");
            break;
         case 13:
            this.mc.setScreen(null);
            RoseClient.LOGGER.info("[Rose][worldtest] viewmodel finished");
            break;
         default:
            return false;
      }

      return true;
   }

   private static void setNumber(String moduleId, String settingId, double value) {
      Module module = Modules.byId(moduleId);
      if (module != null) {
         module.settings()
            .stream()
            .filter(setting -> setting.id().equals(settingId))
            .findFirst()
            .ifPresent(setting -> setting.fromJson(new JsonPrimitive(value)));
      }
   }

   private void attackVillager() {
      for (Entity entity : this.mc.level.entitiesForRendering()) {
         if (entity instanceof Villager) {
            this.mc.gameMode.attack(this.mc.player, entity);
            this.mc.player.swing(InteractionHand.MAIN_HAND);
            return;
         }
      }

      RoseClient.LOGGER.warn("[Rose][worldtest] villager not found");
   }

   private static void disable(String id) {
      Module module = Modules.byId(id);
      if (module != null) {
         module.setEnabled(false);
      }
   }

   private static void enable(String id) {
      Module module = Modules.byId(id);
      if (module != null) {
         module.setEnabled(true);
      }
   }

   private static void setChoice(String moduleId, String settingId, String value) {
      Module module = Modules.byId(moduleId);
      if (module != null) {
         module.settings()
            .stream()
            .filter(setting -> setting.id().equals(settingId))
            .findFirst()
            .ifPresent(setting -> setting.fromJson(new JsonPrimitive(value)));
      }
   }

   private static void choose(String moduleId, String settingId, String value) {
      Module module = Modules.byId(moduleId);
      if (module != null) {
         module.settings().stream().filter(setting -> setting.id().equals(settingId) && setting instanceof ChoiceSetting).findFirst().ifPresent(setting -> {
            ChoiceSetting choice = (ChoiceSetting)setting;

            for (Enum<?> option : choice.options()) {
               if (option.name().equals(value)) {
                  choice.set(option);
               }
            }
         });
      }
   }

   private static void setBool(String moduleId, String settingId, boolean value) {
      Module module = Modules.byId(moduleId);
      if (module != null) {
         module.settings()
            .stream()
            .filter(setting -> setting.id().equals(settingId))
            .findFirst()
            .ifPresent(setting -> setting.fromJson(new JsonPrimitive(value)));
      }
   }

   private void editor(Consumer<HudEditorScreen> action) {
      if (this.mc.screen instanceof HudEditorScreen screen) {
         action.accept(screen);
      } else {
         RoseClient.LOGGER.warn("[Rose][worldtest] editor is not open");
      }
   }

   private void command(String command) {
      IntegratedServer server = this.mc.getSingleplayerServer();
      if (server != null) {
         server.execute(() -> server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(), command));
      }
   }
}
