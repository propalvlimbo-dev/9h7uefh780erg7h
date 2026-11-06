package sg.handler;

import java.util.UUID;

public record Marker(
   UUID id,
   String name,
   String server,
   String dimension,
   double x,
   double y,
   double z,
   int remainingMillis,
   UUID entityUuid,
   String entityType,
   String entityName
) {
   public static final int MAX_LIFETIME_MILLIS = 180000;

   public Marker remaining(int var1) {
      return new Marker(this.id, this.name, this.server, this.dimension, this.x, this.y, this.z, var1, this.entityUuid, this.entityType, this.entityName);
   }

   public boolean valid() {
      return this.id != null
         && this.name != null
         && !this.name.isBlank()
         && this.name.length() <= 32
         && this.name.codePoints().noneMatch(var0 -> Character.isISOControl(var0) || var0 == 167)
         && this.server != null
         && !this.server.isBlank()
         && this.server.length() <= 255
         && this.dimension != null
         && !this.dimension.isBlank()
         && this.dimension.length() <= 128
         && Double.isFinite(this.x)
         && Double.isFinite(this.y)
         && Double.isFinite(this.z)
         && Math.abs(this.x) <= 3.0E7
         && Math.abs(this.y) <= 3.0E7
         && Math.abs(this.z) <= 3.0E7
         && this.remainingMillis > 0
         && this.remainingMillis <= 180000
         && (
            this.entityUuid == null
               ? this.entityType == null && this.entityName == null
               : this.entityType != null
                  && this.entityType.length() <= 128
                  && this.entityType.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")
                  && this.entityName != null
                  && !this.entityName.isBlank()
                  && this.entityName.length() <= 64
                  && this.entityName.codePoints().noneMatch(Character::isISOControl)
         );
   }

   public Marker position(double var1, double var3, double var5) {
      return new Marker(
         this.id, this.name, this.server, this.dimension, var1, var3, var5, this.remainingMillis, this.entityUuid, this.entityType, this.entityName
      );
   }

   public Marker(UUID var1, String var2, String var3, String var4, double var5, double var7, double var9, int var11) {
      this(var1, var2, var3, var4, var5, var7, var9, var11, null, null, null);
   }
}
