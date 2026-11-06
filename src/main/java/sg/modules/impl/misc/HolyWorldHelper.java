package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import org.joml.Vector2f;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.events.impl.WorldEffectsEvent;
import sg.render.WorldProjection;
import sg.render.avatar.HolyWorldStunRenderer;
import sg.render.pipeline.UiDrawing;
import sg.settings.BooleanSetting;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.BoundItemIndicator;
import sg.modules.impl.init.ExpiringLootLocation;
import sg.events.impl.HudRenderEvent;
import sg.events.impl.InputEvent;
import sg.settings.KeybindSetting;
import sg.events.impl.MovementInputEvent;

public class HolyWorldHelper extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("弟")  KeybindSetting fireChargeKeybind;
   @SerializedName("б")
   private final KeybindSetting stunKeybind;
   private final @com.google.gson.annotations.SerializedName("Ы")  KeybindSetting explosionTrapKeybind;
   private final @com.google.gson.annotations.SerializedName("l")  KeybindSetting chorusTrapKeybind;
   @SerializedName("8")
   private final KeybindSetting snowballKeybind;
   @SerializedName("5")
   private final KeybindSetting shulkerKeybind;
   @SerializedName("2")
   private final BooleanSetting stopMovementSetting;
   private final @com.google.gson.annotations.SerializedName("ф")  BooleanSetting effectTimersSetting;
   @SerializedName("Х")
   private final Queue<HolyWorldHelper.TimedItemLocation> pendingLocations;
   @SerializedName("б")
   private final List<ExpiringLootLocation> timedLootLocations;
   @SerializedName("В")
   private final HolyWorldStunRenderer stunRenderer;
   @SerializedName("е")
   private ClientLevel trackedWorld;
   @SerializedName("В")
   private HolyWorldHelper.ItemUseStage itemUseStage;
   @SerializedName("Я")
   private Item pendingItem;
   private @com.google.gson.annotations.SerializedName("си")  int swapContainerSlot;
   private @com.google.gson.annotations.SerializedName("с9")  int selectedHotbarSlot;
   private @com.google.gson.annotations.SerializedName("з")  long restoreAtMillis;
   private final @com.google.gson.annotations.SerializedName("O")  EventListener<InputEvent> inputListener;
   @SerializedName("7")
   private final EventListener<PlayerTickEvent> tickListener;
   private final @com.google.gson.annotations.SerializedName("Р")  EventListener<MovementInputEvent> movementInputListener;
   @SerializedName("3")
   private final EventListener<PacketReceiveEvent> packetReceiveListener;
   private final @com.google.gson.annotations.SerializedName("р")  EventListener<HudRenderEvent> hudRenderListener;
   @SerializedName("6")
   private final EventListener<WorldEffectsEvent> worldEffectsListener;
   private static final String ВсЮ = "HolyWorld Helper";
   private static final String ВсЧ = "Помощник для сервера HolyWorld";
   private static final String ВсЛ = "Штучка";
   private static final String Вс诶 = "Стан";
   private static final String Вс1 = "Взрывная трапка";
   private static final String ВсЬ = "Трапка (Хорус)";
   private static final String Вс衣 = "Снежок";
   private static final String Всш = "Рюкзак (Шалкер)";
   private static final String ВсИ = "Полная остановка";
   private static final String Всь = "Таймеры эффектов";
   private static final String Всж = "minecraft:entity.generic.explode";
   private static final long Вс弟 = 15000L;
   private static final long Всб = 11000L;
   private static final String ВсЫ = "minecraft:block.beacon.deactivate";
   private static final float Всl = 1.5F;
   private static final long Вс8 = 15000L;
   private static final long Вс5 = 11000L;
   private static final double Всв = 1000.0;
   private static final String Всъ = "%.1fs";
   private static final double ВсМ = 10.0;
   private static final double ВсН = 10.0;
   private static final float Всн = 34.0F;
   private static final float ВсЦ = 24.0F;
   private static final float Всч = 0.5F;
   private static final float ВсЭ = 0.5F;
   private static final float Вс必 = 9.0F;
   private static final double Всщ = 205.0;
   private static final float ВсГ = 7.0F;
   private static final double Всл = 140.0;
   private static final float Всэ = 7.0F;
   private static final double Всю = 38.0;
   private static final float ВсЩ = 6.0F;
   private static final float Всу = 4.0F;
   private static final float Вс2 = 27.0F;
   private static final float Всф = 4.0F;
   private static final double Вси = 255.0;
   private static final String Вс9 = "null";
   private static final String Всо = "(?:-|#)(\\d+)";
   private static final long ВсO = 100L;
   private static final double Вс7 = 2.25;

   private int toContainerSlot(int inventorySlot) {
      return inventorySlot < 9 ? inventorySlot + 36 : inventorySlot;
   }

   private void usePendingItem() {
      int inventorySlot = this.findInventorySlot(this.pendingItem);
      if (inventorySlot == -1) {
         this.resetItemUse();
      } else {
         int hotbarSlot = MINECRAFT_CLIENT.player.getInventory().selected;
         MINECRAFT_CLIENT.player.setSprinting(false);
         this.swapContainerSlot = this.toContainerSlot(inventorySlot);
         this.selectedHotbarSlot = hotbarSlot;
         MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.containerMenu.containerId, this.swapContainerSlot, this.selectedHotbarSlot, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         MINECRAFT_CLIENT.player.swing(InteractionHand.MAIN_HAND);
         if (MINECRAFT_CLIENT.gui.screen() == null) {
            MINECRAFT_CLIENT.player.connection.send(new ServerboundContainerClosePacket(MINECRAFT_CLIENT.player.containerMenu.containerId));
         }

         this.restoreAtMillis = System.currentTimeMillis() + ВсO;
         this.itemUseStage = HolyWorldHelper.ItemUseStage.RESTORE;
      }
   }

   public boolean queueChorusTrapUse() {
      if (this.isGameReady() && this.itemUseStage == HolyWorldHelper.ItemUseStage.IDLE && this.findInventorySlot(Items.POPPED_CHORUS_FRUIT) != -1) {
         this.pendingItem = Items.POPPED_CHORUS_FRUIT;
         this.itemUseStage = HolyWorldHelper.ItemUseStage.USE;
         return true;
      } else {
         return false;
      }
   }

   private void resetItemUse() {
      this.itemUseStage = HolyWorldHelper.ItemUseStage.IDLE;
      this.pendingItem = null;
      this.swapContainerSlot = -1;
      this.selectedHotbarSlot = -1;
      this.restoreAtMillis = 0L;
   }

   private void onPlayerTick(PlayerTickEvent event) {
      if (!this.isGameReady()) {
         this.resetItemUse();
         this.pendingLocations.clear();
         this.timedLootLocations.clear();
         this.stunRenderer.Н3();
         this.trackedWorld = null;
      } else {
         if (this.trackedWorld != MINECRAFT_CLIENT.level) {
            this.pendingLocations.clear();
            this.timedLootLocations.clear();
            this.stunRenderer.Н3();
            this.trackedWorld = MINECRAFT_CLIENT.level;
         }

         HolyWorldHelper.TimedItemLocation pendingLocation;
         while ((pendingLocation = this.pendingLocations.poll()) != null) {
            this.trackTimedLocation(pendingLocation);
         }

         long now = System.currentTimeMillis();
         Iterator iterator = this.timedLootLocations.iterator();

         while (iterator.hasNext()) {
            ExpiringLootLocation location = (ExpiringLootLocation)iterator.next();
            if (location.expiresAt() <= now) {
               if (location.item() == Items.NETHER_STAR) {
                  this.stunRenderer.method__(location.position());
               }

               iterator.remove();
            }
         }

         if (this.itemUseStage == HolyWorldHelper.ItemUseStage.USE) {
            this.usePendingItem();
         } else if (this.itemUseStage == HolyWorldHelper.ItemUseStage.RESTORE && System.currentTimeMillis() >= this.restoreAtMillis) {
            this.restoreSwappedItem();
         }
      }
   }

   @Override
   public void onDisable() {
      this.pendingLocations.clear();
      this.timedLootLocations.clear();
      this.stunRenderer.Н3();
      this.trackedWorld = null;
      if (this.itemUseStage == HolyWorldHelper.ItemUseStage.RESTORE && this.isGameReady()) {
         this.restoreSwappedItem();
      } else {
         this.resetItemUse();
      }
   }

   private String currentWorldId() {
      return MINECRAFT_CLIENT.level == null ? Вс9 : MINECRAFT_CLIENT.level.dimension().identifier().getPath();
   }

   private static String soundIdentifier(ClientboundSoundPacket packet) {
      Identifier soundId = packet.getSound().value().location();
      return soundId == null ? "" : soundId.toString();
   }

   private int findInventorySlot(Item item) {
      if (MINECRAFT_CLIENT.player != null && item != null) {
         for (int slot = 0; slot < 36; slot++) {
            if (MINECRAFT_CLIENT.player.getInventory().getItem(slot).is(item)) {
               return slot;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private void onMovementInput(MovementInputEvent event) {
      if (this.itemUseStage != HolyWorldHelper.ItemUseStage.IDLE && this.stopMovementSetting.getValue()) {
         event.setForwardAmount(0.0F);
         event.setLeftAmount(0.0F);
         event.setForwardPressed(false);
         event.setBackwardPressed(false);
         event.setLeftPressed(false);
         event.setRightPressed(false);
      }
   }

   private boolean isGameReady() {
      return MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null && MINECRAFT_CLIENT.getConnection() != null;
   }

   private int currentAnarchyNumber() {
      if (MINECRAFT_CLIENT.level == null) {
         return -1;
      }

      Objective sidebarObjective = MINECRAFT_CLIENT.level.getScoreboard().getDisplayObjective(DisplaySlot.SIDEBAR);
      if (sidebarObjective == null) {
         return -1;
      }

      Matcher anarchyMatcher = Pattern.compile(Всо).matcher(sidebarObjective.getDisplayName().getString());
      return anarchyMatcher.find() ? Integer.parseInt(anarchyMatcher.group(1)) : -1;
   }

   private boolean matchesKeybind(KeybindSetting setting, int inputCode) {
      return setting.getValue() >= 0 && setting.getValue() == inputCode;
   }

   private boolean isExplosionParticle(ClientboundLevelParticlesPacket packet) {
      return packet.getParticle().getType() == ParticleTypes.POOF
         || packet.getParticle().getType() == ParticleTypes.EXPLOSION
         || packet.getParticle().getType() == ParticleTypes.EXPLOSION_EMITTER;
   }

   private void onPacketReceive(PacketReceiveEvent event) {
      if (this.effectTimersSetting.getValue()) {
         if (!(event.getPacket() instanceof ClientboundSoundPacket var2)) {
            if (event.getPacket() instanceof ClientboundLevelParticlesPacket var7 && this.isExplosionParticle(var7)) {
               this.pendingLocations.add(new HolyWorldHelper.TimedItemLocation(Items.PRISMARINE_SHARD, new Vec3(var7.getX(), var7.getY(), var7.getZ()), Вс5));
            }
         } else {
            String soundId = soundIdentifier(var2);
            float volume = var2.getVolume();
            float pitch = var2.getPitch();
            BlockPos soundPosition = BlockPos.containing(var2.getX(), var2.getY(), var2.getZ());
            if (soundId.contains(Всж) && (volume == 1.0F || pitch == 1.0F)) {
               this.pendingLocations.add(new HolyWorldHelper.TimedItemLocation(Items.NETHER_STAR, Vec3.atCenterOf(soundPosition), Вс弟));
               this.pendingLocations.add(new HolyWorldHelper.TimedItemLocation(Items.PRISMARINE_SHARD, Vec3.atCenterOf(soundPosition.above()), Всб));
            }

            if (soundId.contains(ВсЫ) && (volume == Всl || pitch == 1.0F)) {
               this.pendingLocations.add(new HolyWorldHelper.TimedItemLocation(Items.NETHER_STAR, Vec3.atCenterOf(soundPosition), Вс8));
            }
         }
      }
   }

   public HolyWorldHelper() {
      super(ВсЮ, ВсЧ, ModuleCategory.MISC);
      this.fireChargeKeybind = new KeybindSetting(ВсЛ);
      this.stunKeybind = new KeybindSetting(Вс诶);
      this.explosionTrapKeybind = new KeybindSetting(Вс1);
      this.chorusTrapKeybind = new KeybindSetting(ВсЬ);
      this.snowballKeybind = new KeybindSetting(Вс衣);
      this.shulkerKeybind = new KeybindSetting(Всш);
      this.stopMovementSetting = new BooleanSetting(ВсИ, false);
      this.effectTimersSetting = new BooleanSetting(Всь, true);
      this.pendingLocations = new ConcurrentLinkedQueue<>();
      this.timedLootLocations = new ArrayList<>();
      this.stunRenderer = new HolyWorldStunRenderer();
      this.itemUseStage = HolyWorldHelper.ItemUseStage.IDLE;
      this.swapContainerSlot = -1;
      this.selectedHotbarSlot = -1;
      this.inputListener = this::onInput;
      this.tickListener = this::onPlayerTick;
      this.movementInputListener = this::onMovementInput;
      this.packetReceiveListener = this::onPacketReceive;
      this.hudRenderListener = this::onHudRender;
      this.worldEffectsListener = this::onWorldEffects;
      this.addSettings(this.fireChargeKeybind, this.stunKeybind, this.explosionTrapKeybind, this.chorusTrapKeybind, this.snowballKeybind, this.shulkerKeybind, this.stopMovementSetting, this.effectTimersSetting);
   }

   private void onHudRender(HudRenderEvent event) {
      if (this.effectTimersSetting.getValue() && event.getGraphics() != null && MINECRAFT_CLIENT.level != null) {
         String worldId = this.currentWorldId();
         int anarchyNumber = this.currentAnarchyNumber();

         for (ExpiringLootLocation location : this.timedLootLocations) {
            if (location.world().equals(worldId) && (location.anarchy() < 0 || anarchyNumber < 0 || location.anarchy() == anarchyNumber)) {
               Vector2f screenPosition = WorldProjection.projectToScreen(location.position());
               if (screenPosition != null) {
                  double secondsRemaining = Math.max(0.0, (location.expiresAt() - System.currentTimeMillis()) / Всв);
                  String timerText = String.format(Locale.US, Всъ, Math.round(secondsRemaining * ВсМ) / ВсН);
                  float width = Всн + UiDrawing.getTextWidth(ClientFonts.getMediumFont(), timerText, 12);
                  float height = ВсЦ;
                  float x = screenPosition.x - width * Всч;
                  float y = screenPosition.y - height * ВсЭ;
                  UiDrawing.drawShadow(x, y, width, height, Вс必, ColorMath.packArgb(0, 0, 0, Всщ));
                  UiDrawing.drawRoundedRect(x, y, width, height, ВсГ, ColorMath.packArgb(9, 14, 22, Всл));
                  UiDrawing.drawOutline(x, y, width, height, 1.0F, Всэ, ColorMath.packArgb(93, 184, 255, Всю));
                  UiDrawing.drawItem(location.item().getDefaultInstance(), x + ВсЩ, y + Всу, 1.0F, false);
                  UiDrawing.drawText(ClientFonts.getMediumFont(), timerText, x + Вс2, y + Всф, 12, ColorMath.packArgb(232, 246, 255, Вси));
               }
            }
         }
      }
   }

   private void restoreSwappedItem() {
      if (this.swapContainerSlot != -1 && this.selectedHotbarSlot != -1) {
         MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.containerMenu.containerId, this.swapContainerSlot, this.selectedHotbarSlot, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
         if (MINECRAFT_CLIENT.gui.screen() == null) {
            MINECRAFT_CLIENT.player.connection.send(new ServerboundContainerClosePacket(MINECRAFT_CLIENT.player.containerMenu.containerId));
         }
      }

      this.resetItemUse();
   }

   public List<BoundItemIndicator> getBoundItemIndicators() {
      return List.of(
         new BoundItemIndicator(Items.FIRE_CHARGE, this.fireChargeKeybind.getValue()),
         new BoundItemIndicator(Items.NETHER_STAR, this.stunKeybind.getValue()),
         new BoundItemIndicator(Items.PRISMARINE_SHARD, this.explosionTrapKeybind.getValue()),
         new BoundItemIndicator(Items.POPPED_CHORUS_FRUIT, this.chorusTrapKeybind.getValue()),
         new BoundItemIndicator(Items.SNOWBALL, this.snowballKeybind.getValue()),
         new BoundItemIndicator(Items.SHULKER_BOX, this.shulkerKeybind.getValue())
      );
   }

   private void trackTimedLocation(HolyWorldHelper.TimedItemLocation location) {
      if (!this.timedLootLocations.stream().anyMatch(var1x -> var1x.item() == location.item() && var1x.position().distanceToSqr(location.position()) < Вс7)) {
         this.timedLootLocations.add(new ExpiringLootLocation(location.item(), location.position(), this.currentWorldId(), this.currentAnarchyNumber(), System.currentTimeMillis() + location.duration()));
      }
   }

   private void onInput(InputEvent event) {
      if (event.getAction() == 1 && event.getInputCode() >= 0 && MINECRAFT_CLIENT.gui.screen() == null && this.isGameReady() && this.itemUseStage == HolyWorldHelper.ItemUseStage.IDLE) {
         Item item = this.itemForInputCode(event.getInputCode());
         if (item != null && this.findInventorySlot(item) != -1) {
            this.pendingItem = item;
            this.itemUseStage = HolyWorldHelper.ItemUseStage.USE;
         }
      }
   }

   private void onWorldEffects(WorldEffectsEvent event) {
      if (this.effectTimersSetting.getValue() && MINECRAFT_CLIENT.level != null) {
         ArrayList stunPositions = new ArrayList();
         String worldId = this.currentWorldId();
         int anarchyNumber = this.currentAnarchyNumber();

         for (ExpiringLootLocation location : this.timedLootLocations) {
            if (location.item() == Items.NETHER_STAR && location.world().equals(worldId) && (location.anarchy() < 0 || anarchyNumber < 0 || location.anarchy() == anarchyNumber)) {
               stunPositions.add(location.position());
            }
         }

         this.stunRenderer.method_7(event, stunPositions);
      }
   }

   private Item itemForInputCode(int inputCode) {
      if (this.matchesKeybind(this.fireChargeKeybind, inputCode)) {
         return Items.FIRE_CHARGE;
      } else if (this.matchesKeybind(this.stunKeybind, inputCode)) {
         return Items.NETHER_STAR;
      } else if (this.matchesKeybind(this.explosionTrapKeybind, inputCode)) {
         return Items.PRISMARINE_SHARD;
      } else if (this.matchesKeybind(this.chorusTrapKeybind, inputCode)) {
         return Items.POPPED_CHORUS_FRUIT;
      } else if (this.matchesKeybind(this.snowballKeybind, inputCode)) {
         return Items.SNOWBALL;
      } else {
         return this.matchesKeybind(this.shulkerKeybind, inputCode) ? Items.SHULKER_BOX : null;
      }
   }

   private enum ItemUseStage {
      IDLE,
      USE,
      RESTORE;
   }

   private record TimedItemLocation(Item item, Vec3 position, long duration) {
   }
}
