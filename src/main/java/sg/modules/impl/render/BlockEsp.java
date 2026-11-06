package sg.modules.impl.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.WorldEffectsEvent;
import sg.modules.impl.init.RoseXrayLines;
import sg.client.RoseClient;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;
import sg.modules.impl.init.BlockEspTargets;
import sg.settings.FloatSetting;
import sg.events.impl.PlayerTickEvent;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;

public final class BlockEsp extends ClientModule {
   private static final int Ър = 70000;
   private static final double ЯС = 0.002;
   private final @com.google.gson.annotations.SerializedName("е诶")  FloatSetting radius;
   private final @com.google.gson.annotations.SerializedName("ВЧ")  Map<Integer, List<BlockPos>> blockPositionsByColor;
   private final @com.google.gson.annotations.SerializedName("ВЛ")  Map<Block, Integer> blockColors;
   private final @com.google.gson.annotations.SerializedName("В诶")  Map<EntityType<?>, Integer> entityTypeColors;
   private final @com.google.gson.annotations.SerializedName("еЪ")  EventListener<PlayerTickEvent> tickListener;
   private final @com.google.gson.annotations.SerializedName("ет")  EventListener<WorldEffectsEvent> worldEffectsListener;
   private @com.google.gson.annotations.SerializedName("В")  BlockEsp.ScanProgress scanProgress;
   private @com.google.gson.annotations.SerializedName("Ъ6")  int lastTargetRevision;
   private @com.google.gson.annotations.SerializedName("Ъ_")  int lastChunkX;
   private @com.google.gson.annotations.SerializedName("ЪШ")  int lastChunkZ;
   private @com.google.gson.annotations.SerializedName("Ъы")  int lastRadius;
   private static final String MODULE_NAME = "BlockESP";
   private static final String MODULE_DESCRIPTION = "Подсвечивает выбранные блоки и сущности";
   private static final String RADIUS_SETTING_NAME = "Радиус";
   private static final float сАч = 48.0F;
   private static final float сАЭ = 8.0F;
   private static final float сА必 = 64.0F;
   private static final int сАщ = Integer.MIN_VALUE;
   private static final int сАГ = Integer.MIN_VALUE;
   private static final int сАл = Integer.MIN_VALUE;
   private static final int сАэ = 16777215;
   private static final int сАю = 70000;
   private static final int сАЩ = -16777216;
   private static final double сАу = 0.002;
   private static final double сА2 = 0.002;
   private static final double сАф = 0.002;
   private static final double сАи = 0.002;
   private static final double сА9 = 0.002;
   private static final double сАо = 0.002;
   private static final double сАO = 0.002;
   private static final int сА7 = -16777216;
   private static final int сАР = Integer.MIN_VALUE;
   private static final int сА3 = Integer.MIN_VALUE;
   private static final int сАр = Integer.MIN_VALUE;

   private void processScanBatch() {
      if (this.scanProgress != null && MINECRAFT_CLIENT.level != null) {
         MutableBlockPos mutablePosition = new MutableBlockPos();
         int visitedPositions = 0;

         while (visitedPositions++ < сАю && this.scanProgress.nextIndex < this.scanProgress.totalPositions) {
            int flatIndex = this.scanProgress.nextIndex++;
            int sliceArea = this.scanProgress.sizeY * this.scanProgress.sizeZ;
            int offsetX = flatIndex / sliceArea;
            int indexWithinSlice = flatIndex % sliceArea;
            int offsetY = indexWithinSlice / this.scanProgress.sizeZ;
            int offsetZ = indexWithinSlice % this.scanProgress.sizeZ;
            mutablePosition.set(this.scanProgress.originX + offsetX, this.scanProgress.originY + offsetY, this.scanProgress.originZ + offsetZ);
            BlockState blockState = MINECRAFT_CLIENT.level.getBlockState(mutablePosition);
            if (!blockState.isAir()) {
               Integer rgb = this.scanProgress.blockColorsSnapshot.get(blockState.getBlock());
               if (rgb != null) {
                  this.scanProgress.positionsByColor.computeIfAbsent(rgb, var0 -> new ArrayList<>()).add(mutablePosition.immutable());
               }
            }
         }

         if (this.scanProgress.nextIndex >= this.scanProgress.totalPositions) {
            this.blockPositionsByColor.clear();
            this.blockPositionsByColor.putAll(this.scanProgress.positionsByColor);
            this.scanProgress = null;
         }
      }
   }

   public BlockEsp() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.RENDER);
      this.radius = new FloatSetting(RADIUS_SETTING_NAME, сАч, сАЭ, сА必).withStep(1.0F);
      this.blockPositionsByColor = new HashMap<>();
      this.blockColors = new IdentityHashMap<>();
      this.entityTypeColors = new IdentityHashMap<>();
      this.tickListener = this::onPlayerTick;
      this.worldEffectsListener = this::onWorldEffects;
      this.lastTargetRevision = сАщ;
      this.lastChunkX = сАГ;
      this.lastChunkZ = сАл;
      this.lastRadius = -1;
      this.addSettings(this.radius);
   }

   @Override
   public void onEnable() {
      this.resetScanState();
      super.onEnable();
   }

   private void resetScanState() {
      this.lastTargetRevision = сАР;
      this.lastChunkX = сА3;
      this.lastChunkZ = сАр;
      this.lastRadius = -1;
      this.scanProgress = null;
   }

   private void beginScan(List<BlockEspTargets.Target> targets, BlockPos center, int radius) {
      this.blockColors.clear();
      this.entityTypeColors.clear();

      for (BlockEspTargets.Target target : targets) {
         Identifier targetId = Identifier.tryParse(target.target());
         if (targetId != null) {
            int rgb = target.color() & сАэ;
            if (BuiltInRegistries.BLOCK.containsKey(targetId)) {
               this.blockColors.put(BuiltInRegistries.BLOCK.getValue(targetId), rgb);
            }

            if (BuiltInRegistries.ENTITY_TYPE.containsKey(targetId)) {
               this.entityTypeColors.put(BuiltInRegistries.ENTITY_TYPE.getValue(targetId), rgb);
            }
         }
      }

      if (this.blockColors.isEmpty()) {
         this.blockPositionsByColor.clear();
         this.scanProgress = null;
      } else {
         int minY = Math.max(MINECRAFT_CLIENT.level.getMinY(), center.getY() - radius);
         int maxY = Math.min(MINECRAFT_CLIENT.level.getMaxY(), center.getY() + radius);
         int horizontalSize = radius * 2 + 1;
         this.scanProgress = new BlockEsp.ScanProgress(
            center.getX() - radius, minY, center.getZ() - radius, horizontalSize, maxY - minY + 1, horizontalSize, new IdentityHashMap<>(this.blockColors)
         );
      }
   }

   private void onWorldEffects(WorldEffectsEvent event) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (!this.blockPositionsByColor.isEmpty() || !this.entityTypeColors.isEmpty()) {
            VertexConsumer vertices = event.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
            Vec3 cameraPosition = event.getCameraPosition();
            this.renderBlockOutlines(event, vertices, cameraPosition);
            this.renderEntityOutlines(event, vertices, cameraPosition);
         }
      }
   }

   private void clearCachedTargets() {
      this.blockPositionsByColor.clear();
      this.blockColors.clear();
      this.entityTypeColors.clear();
      this.resetScanState();
   }

   private void renderEntityOutlines(WorldEffectsEvent event, VertexConsumer vertices, Vec3 cameraPosition) {
      if (!this.entityTypeColors.isEmpty()) {
         float partialTick = event.getTickDelta();

         for (Entity entity : MINECRAFT_CLIENT.level.entitiesForRendering()) {
            if (entity != MINECRAFT_CLIENT.player && !entity.isRemoved()) {
               Integer rgb = this.entityTypeColors.get(entity.getType());
               if (rgb != null) {
                  Vec3 interpolatedPosition = new Vec3(Mth.lerp(partialTick, entity.xo, entity.getX()), Mth.lerp(partialTick, entity.yo, entity.getY()), Mth.lerp(partialTick, entity.zo, entity.getZ()));
                  AABB interpolatedBounds = entity.getBoundingBox().move(interpolatedPosition.x - entity.getX(), interpolatedPosition.y - entity.getY(), interpolatedPosition.z - entity.getZ()).inflate(сАO);
                  WorldBoxGeometry.appendBox(event.getPoses(), vertices, cameraPosition, interpolatedBounds.minX, interpolatedBounds.minY, interpolatedBounds.minZ, interpolatedBounds.maxX, interpolatedBounds.maxY, interpolatedBounds.maxZ, сА7 | rgb, false);
               }
            }
         }
      }
   }

   private void onPlayerTick(PlayerTickEvent event) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         BlockEspTargets targetRegistry = this.getTargetRegistry();
         int targetRevision = targetRegistry.РО();
         int radius = this.radius.getValue().intValue();
         BlockPos playerPosition = MINECRAFT_CLIENT.player.blockPosition();
         int chunkX = playerPosition.getX() >> 4;
         int chunkZ = playerPosition.getZ() >> 4;
         if (targetRevision != this.lastTargetRevision || chunkX != this.lastChunkX || chunkZ != this.lastChunkZ || radius != this.lastRadius) {
            this.beginScan(targetRegistry.Т2(), playerPosition, radius);
            this.lastTargetRevision = targetRevision;
            this.lastChunkX = chunkX;
            this.lastChunkZ = chunkZ;
            this.lastRadius = radius;
         }

         this.processScanBatch();
      } else {
         this.clearCachedTargets();
      }
   }

   private BlockEspTargets getTargetRegistry() {
      return RoseClient.getInstance().getServices().getBlockEspTargets();
   }

   private void renderBlockOutlines(WorldEffectsEvent event, VertexConsumer vertices, Vec3 cameraPosition) {
      for (Entry colorGroup : this.blockPositionsByColor.entrySet()) {
         int argb = сАЩ | (Integer)colorGroup.getKey();
         Iterator positions = ((List)colorGroup.getValue()).iterator();

         while (positions.hasNext()) {
            BlockPos blockPosition = (BlockPos)positions.next();
            BlockState blockState = MINECRAFT_CLIENT.level.getBlockState(blockPosition);
            Integer currentRgb = this.blockColors.get(blockState.getBlock());
            if (currentRgb != null && currentRgb.intValue() == ((Integer)colorGroup.getKey()).intValue()) {
               WorldBoxGeometry.appendBox(
                  event.getPoses(),
                  vertices,
                  cameraPosition,
                  blockPosition.getX() - сАу,
                  blockPosition.getY() - сА2,
                  blockPosition.getZ() - сАф,
                  blockPosition.getX() + 1.0 + сАи,
                  blockPosition.getY() + 1.0 + сА9,
                  blockPosition.getZ() + 1.0 + сАо,
                  argb,
                  false
               );
            } else {
               positions.remove();
            }
         }
      }
   }

   @Override
   public void onDisable() {
      this.clearCachedTargets();
      super.onDisable();
   }

   private static final class ScanProgress {
      private final @com.google.gson.annotations.SerializedName("еъ")  int originX;
      private final @com.google.gson.annotations.SerializedName("еМ")  int originY;
      private final @com.google.gson.annotations.SerializedName("еН")  int originZ;
      private final @com.google.gson.annotations.SerializedName("ен")  int sizeX;
      private final @com.google.gson.annotations.SerializedName("еЦ")  int sizeY;
      private final @com.google.gson.annotations.SerializedName("еч")  int sizeZ;
      private final @com.google.gson.annotations.SerializedName("еЭ")  int totalPositions;
      private final @com.google.gson.annotations.SerializedName("ст")  Map<Block, Integer> blockColorsSnapshot;
      private final @com.google.gson.annotations.SerializedName("са")  Map<Integer, List<BlockPos>> positionsByColor = new HashMap<>();
      private @com.google.gson.annotations.SerializedName("е必")  int nextIndex;

      private ScanProgress(int originX, int originY, int originZ, int sizeX, int sizeY, int sizeZ, Map<Block, Integer> blockColorsSnapshot) {
         this.originX = originX;
         this.originY = originY;
         this.originZ = originZ;
         this.sizeX = sizeX;
         this.sizeY = sizeY;
         this.sizeZ = sizeZ;
         this.totalPositions = sizeX * sizeY * sizeZ;
         this.blockColorsSnapshot = blockColorsSnapshot;
      }
   }
}
