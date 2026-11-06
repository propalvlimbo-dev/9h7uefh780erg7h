package sg.util.time;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.Generated;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import sg.ec.MinecraftAccess;

public final class ClientMetrics implements MinecraftAccess {
   private static final long BYTES_PER_MEBIBYTE = 1048576L;
   private static final double MAX_GPU_UTILIZATION_PERCENT = 100.0;
   private static final double CPU_LOAD_PERCENT_MULTIPLIER = 100.0;
   private static final String ZERO_SPEED_TEXT = "0.0";
   private static final String SPEED_FORMAT = "%.2f";
   private static final double TICKS_PER_SECOND = 20.0;
   private static final String MOSCOW_ZONE_ID = "Europe/Moscow";
   private static final String CLOCK_FORMAT = "HH:mm";
   private static final String UTILITY_CONSTRUCTOR_MESSAGE = "This is a utility class and cannot be instantiated";

   @Generated
   private ClientMetrics() {
      throw new UnsupportedOperationException(UTILITY_CONSTRUCTOR_MESSAGE);
   }

   public static String getMovementSpeedText() {
      LocalPlayer player = MINECRAFT_CLIENT.player;
      double deltaX = player.getX() - player.xo;
      double deltaZ = player.getZ() - player.zo;
      double deltaY = player.getY() - player.yo;
      double distancePerTick = Math.hypot(deltaX, deltaZ);
      distancePerTick = Math.hypot(distancePerTick, deltaY);
      return distancePerTick == 0.0 ? ZERO_SPEED_TEXT : String.format(Locale.US, SPEED_FORMAT, distancePerTick * TICKS_PER_SECOND);
   }

   public static String э衣() {
      return "";
   }

   public static int getPlayerLatencyMillis(Player player) {
      PlayerInfo playerInfo = MINECRAFT_CLIENT.getConnection().getPlayerInfo(player.getUUID());
      return playerInfo != null ? playerInfo.getLatency() : 0;
   }

   public static String getGpuUsageText() {
      String utilizationText = MINECRAFT_CLIENT.getGpuUtilization() > MAX_GPU_UTILIZATION_PERCENT ? ChatFormatting.RED + "100%" : Math.round(MINECRAFT_CLIENT.getGpuUtilization()) + "%";
      return utilizationText + " GPU";
   }

   public static int getUsedHeapMebibytes() {
      Runtime runtime = Runtime.getRuntime();
      long allocatedHeapBytes = runtime.totalMemory();
      long freeHeapBytes = runtime.freeMemory();
      long usedHeapBytes = allocatedHeapBytes - freeHeapBytes;
      return (int)(usedHeapBytes / BYTES_PER_MEBIBYTE);
   }

   public static String getMoscowTimeText() {
      ZonedDateTime moscowTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of(MOSCOW_ZONE_ID));
      return moscowTime.format(DateTimeFormatter.ofPattern(CLOCK_FORMAT));
   }

   public static int getProcessCpuUsagePercent() {
      OperatingSystemMXBean operatingSystem = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
      double processCpuLoad = operatingSystem.getProcessCpuLoad();
      return processCpuLoad < 0.0 ? 0 : (int)Math.round(processCpuLoad * CPU_LOAD_PERCENT_MULTIPLIER);
   }
}
