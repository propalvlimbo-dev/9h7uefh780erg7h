package sg.ec;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;

public final class HiddenBlockPositions {
   public static final Set<BlockPos> 弟 = ConcurrentHashMap.newKeySet();

   public static boolean О(BlockPos var0) {
      return var0 != null && 弟.contains(var0);
   }

   private HiddenBlockPositions() {
   }
}
