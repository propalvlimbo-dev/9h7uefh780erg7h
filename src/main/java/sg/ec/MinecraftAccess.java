package sg.ec;

import java.util.Random;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public interface MinecraftAccess {
   @NotNull
   Minecraft MINECRAFT_CLIENT = Minecraft.getInstance();

   default float randomOffset(float amplitude) {
      return (new Random().nextFloat() * 2.0F - 1.0F) * amplitude;
   }
}
