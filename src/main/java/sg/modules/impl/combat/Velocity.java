package sg.modules.impl.combat;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.MultiChoiceSetting;

public class Velocity extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("б")  MultiChoiceSetting enabledContextsSetting;
   private final @com.google.gson.annotations.SerializedName("Яь")  EventListener<PacketReceiveEvent> packetReceiveListener;
   private static final String ЫП = "Velocity";
   private static final String Ы西 = "Отключает откидывание";
   private static final String Ыд = "Работать";
   private static final String ЫБ = "Элитра";
   private static final String ЫС = "Земля";
   private static final String Ыз = "Вода";
   private static final String Ыц = "Элитра";
   private static final String Ыя = "Вода";
   private static final String lс = "Земля";

   public void Ы(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundSetEntityMotionPacket var2 && this.method_92() && var2.id() == MINECRAFT_CLIENT.player.getId()) {
         var1.setCancelled(true);
      }
   }

   public Velocity() {
      super(ЫП, Ы西, ModuleCategory.COMBAT);
      this.enabledContextsSetting = new MultiChoiceSetting(Ыд, new ChoiceOption(ЫБ), new ChoiceOption(ЫС), new ChoiceOption(Ыз));
      this.packetReceiveListener = this::Ы;
      this.addSettings(this.enabledContextsSetting);
   }

   private boolean method_92() {
      boolean var1 = MINECRAFT_CLIENT.player.isFallFlying() && this.enabledContextsSetting.isSelected(Ыц);
      boolean var2 = MINECRAFT_CLIENT.player.isInWater() && this.enabledContextsSetting.isSelected(Ыя);
      return var1 || var2 || this.enabledContextsSetting.isSelected(lс);
   }
}
