package sg.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import sg.annotation.LuaExport;

public class LuaSoundAccess {
   @LuaExport("playSound")
   public void playSound(String soundId, double volume) {
      Minecraft.getInstance().player.playSound(SoundEvent.createVariableRangeEvent(Identifier.withDefaultNamespace(soundId)), (float)volume, 1.0F);
   }
}
