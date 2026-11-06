package sg.render.pipeline;

import java.util.Arrays;
import java.util.IdentityHashMap;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import sg.ec.MinecraftAccess;

final class OcclusionVoxelGrid implements MinecraftAccess {
   static final int ЪМ = 1024;
   private final @com.google.gson.annotations.SerializedName("Э")  int[] occluderWords = new int[1024];
   private final @com.google.gson.annotations.SerializedName("е")  IdentityHashMap<BlockState, Integer> occlusionShapeMasks = new IdentityHashMap<>();
   private final @com.google.gson.annotations.SerializedName("Х")  MutableBlockPos blockPosition = new MutableBlockPos();
   private final @com.google.gson.annotations.SerializedName("Ъ")  Vector3f cameraRelativeLightPosition = new Vector3f();
   private @com.google.gson.annotations.SerializedName("ее")  long sampledGameTime;
   private @com.google.gson.annotations.SerializedName("ЪН")  int gridOriginX;
   private @com.google.gson.annotations.SerializedName("Ън")  int gridOriginY;
   private @com.google.gson.annotations.SerializedName("ЪЦ")  int gridOriginZ;
   private @com.google.gson.annotations.SerializedName("Ъч")  int sampledMinX;
   private @com.google.gson.annotations.SerializedName("ЪЭ")  int sampledMinY;
   private @com.google.gson.annotations.SerializedName("Ъ必")  int sampledMinZ;
   private @com.google.gson.annotations.SerializedName("Ъщ")  int sampledMaxX;
   private @com.google.gson.annotations.SerializedName("ЪГ")  int sampledMaxY;
   private @com.google.gson.annotations.SerializedName("Ъл")  int sampledMaxZ;
   private static final long INITIAL_UNSAMPLED_GAME_TIME = Long.MIN_VALUE;
   private static final double BOUNDS_CENTER_X_FACTOR = 0.5;
   private static final double BOUNDS_CENTER_Y_FACTOR = 0.5;
   private static final double BOUNDS_CENTER_Z_FACTOR = 0.5;
   private static final long RESET_UNSAMPLED_GAME_TIME = Long.MIN_VALUE;
   private static final double SUBCELL_SAMPLE_X_OFFSET = 0.25;
   private static final double SUBCELL_SAMPLE_X_STEP = 0.5;
   private static final double SUBCELL_SAMPLE_Y_OFFSET = 0.25;
   private static final double SUBCELL_SAMPLE_Y_STEP = 0.5;
   private static final double SUBCELL_SAMPLE_Z_OFFSET = 0.25;
   private static final double SUBCELL_SAMPLE_Z_STEP = 0.5;

   float getCameraRelativeOriginZ(Vec3 cameraPosition) {
      return (float)(this.gridOriginZ - cameraPosition.z);
   }

   private static int computeOcclusionMask(BlockState blockState) {
      int[] maskHolder = new int[]{0};
      blockState.getOcclusionShape().forAllBoxes((boxMinX, boxMinY, boxMinZ, boxMaxX, boxMaxY, boxMaxZ) -> {
         for (int subcellIndex = 0; subcellIndex < 8; subcellIndex++) {
            double sampleX = SUBCELL_SAMPLE_X_OFFSET + (subcellIndex & 1) * SUBCELL_SAMPLE_X_STEP;
            double sampleY = SUBCELL_SAMPLE_Y_OFFSET + (subcellIndex >> 1 & 1) * SUBCELL_SAMPLE_Y_STEP;
            double sampleZ = SUBCELL_SAMPLE_Z_OFFSET + (subcellIndex >> 2 & 1) * SUBCELL_SAMPLE_Z_STEP;
            if (sampleX >= boxMinX && sampleX < boxMaxX && sampleY >= boxMinY && sampleY < boxMaxY && sampleZ >= boxMinZ && sampleZ < boxMaxZ) {
               maskHolder[0] |= 1 << subcellIndex;
            }
         }
      });
      return maskHolder[0];
   }

   int[] getOccluderWords() {
      return this.occluderWords;
   }

   void clear() {
      this.sampledGameTime = RESET_UNSAMPLED_GAME_TIME;
      this.occlusionShapeMasks.clear();
      Arrays.fill(this.occluderWords, 0);
   }

   void update(Matrix4f inverseViewMatrix, Vector3f[] lightViewPositions, float[] lightRanges, int lightCount, Vec3 cameraPosition) {
      double minWorldX = Double.POSITIVE_INFINITY;
      double minWorldY = minWorldX;
      double minWorldZ = minWorldX;
      double maxWorldX = Double.NEGATIVE_INFINITY;
      double maxWorldY = maxWorldX;
      double maxWorldZ = maxWorldX;

      for (int lightIndex = 0; lightIndex < lightCount; lightIndex++) {
         inverseViewMatrix.transformPosition(lightViewPositions[lightIndex], this.cameraRelativeLightPosition);
         double worldX = cameraPosition.x + this.cameraRelativeLightPosition.x;
         double worldY = cameraPosition.y + this.cameraRelativeLightPosition.y;
         double worldZ = cameraPosition.z + this.cameraRelativeLightPosition.z;
         float lightRange = lightRanges[lightIndex];
         minWorldX = Math.min(minWorldX, worldX - lightRange);
         maxWorldX = Math.max(maxWorldX, worldX + lightRange);
         minWorldY = Math.min(minWorldY, worldY - lightRange);
         maxWorldY = Math.max(maxWorldY, worldY + lightRange);
         minWorldZ = Math.min(minWorldZ, worldZ - lightRange);
         maxWorldZ = Math.max(maxWorldZ, worldZ + lightRange);
      }

      int originX = (int)Math.floor((minWorldX + maxWorldX) * BOUNDS_CENTER_X_FACTOR) - 8;
      int originY = (int)Math.floor((minWorldY + maxWorldY) * BOUNDS_CENTER_Y_FACTOR) - 8;
      int originZ = (int)Math.floor((minWorldZ + maxWorldZ) * BOUNDS_CENTER_Z_FACTOR) - 8;
      int minBlockX = Math.max(originX, (int)Math.floor(minWorldX));
      int maxBlockX = Math.min(originX + 15, (int)Math.floor(maxWorldX));
      int minBlockY = Math.max(originY, (int)Math.floor(minWorldY));
      int maxBlockY = Math.min(originY + 15, (int)Math.floor(maxWorldY));
      int minBlockZ = Math.max(originZ, (int)Math.floor(minWorldZ));
      int maxBlockZ = Math.min(originZ + 15, (int)Math.floor(maxWorldZ));
      long gameTime = MINECRAFT_CLIENT.level.getGameTime();
      if (this.sampledGameTime != gameTime
         || this.gridOriginX != originX
         || this.gridOriginY != originY
         || this.gridOriginZ != originZ
         || this.sampledMinX != minBlockX
         || this.sampledMaxX != maxBlockX
         || this.sampledMinY != minBlockY
         || this.sampledMaxY != maxBlockY
         || this.sampledMinZ != minBlockZ
         || this.sampledMaxZ != maxBlockZ) {
         this.gridOriginX = originX;
         this.gridOriginY = originY;
         this.gridOriginZ = originZ;
         this.sampledMinX = minBlockX;
         this.sampledMaxX = maxBlockX;
         this.sampledMinY = minBlockY;
         this.sampledMaxY = maxBlockY;
         this.sampledMinZ = minBlockZ;
         this.sampledMaxZ = maxBlockZ;
         this.sampledGameTime = gameTime;
         Arrays.fill(this.occluderWords, 0);

         for (int blockZ = minBlockZ; blockZ <= maxBlockZ; blockZ++) {
            for (int blockY = minBlockY; blockY <= maxBlockY; blockY++) {
               for (int blockX = minBlockX; blockX <= maxBlockX; blockX++) {
                  BlockState blockState = MINECRAFT_CLIENT.level.getBlockState(this.blockPosition.set(blockX, blockY, blockZ));
                  if (blockState.canOcclude()) {
                     int occlusionMask = this.occlusionShapeMasks.computeIfAbsent(blockState, OcclusionVoxelGrid::computeOcclusionMask);

                     for (int subcellIndex = 0; subcellIndex < 8; subcellIndex++) {
                        if ((occlusionMask & 1 << subcellIndex) != 0) {
                           int cellX = (blockX - originX) * 2 + (subcellIndex & 1);
                           int cellY = (blockY - originY) * 2 + (subcellIndex >> 1 & 1);
                           int cellZ = (blockZ - originZ) * 2 + (subcellIndex >> 2 & 1);
                           int bitIndex = cellX + cellY * 32 + cellZ * 1024;
                           this.occluderWords[bitIndex >> 5] = this.occluderWords[bitIndex >> 5] | 1 << (bitIndex & 31);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   OcclusionVoxelGrid() {
      this.sampledGameTime = INITIAL_UNSAMPLED_GAME_TIME;
   }

   float getCameraRelativeOriginY(Vec3 cameraPosition) {
      return (float)(this.gridOriginY - cameraPosition.y);
   }

   float getCameraRelativeOriginX(Vec3 cameraPosition) {
      return (float)(this.gridOriginX - cameraPosition.x);
   }
}
