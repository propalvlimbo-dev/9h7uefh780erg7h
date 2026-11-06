package sg.modules.impl.movement;

import com.google.gson.annotations.SerializedName;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.network.protocol.Packet;
import sg.ec.PacketEventBypass;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.events.impl.PacketSendEvent;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public class FakeLag extends ClientModule {
   private final SingleChoiceSetting ф;
   private final FloatSetting ся;
   @SerializedName("А")
   private final CopyOnWriteArrayList<FakeLag.DelayedPacket> А_at_3775ed2b;
   private final Stopwatch т;
   private final EventListener<PacketSendEvent> сУ;
   private final EventListener<PacketReceiveEvent> сЗ;
   private static final String ЯЩ衣 = "FakeLag";
   private static final String ЯЩш = "Добавляет лаги для игрока";
   private static final String ЯЩИ = "Режим";
   private static final String ЯЩь = "Задержка";
   private static final String ЯЩж = "Лаги";
   private static final String ЯЩ弟 = "Задержка";
   private static final float ЯЩб = 100.0F;
   private static final float ЯЩЫ = 1000.0F;
   private static final float ЯЩl = 50.0F;
   private static final String ЯЩ8 = "Задержка";
   private static final String ЯЩ5 = "Лаги";

   public void method_5(PacketSendEvent var1) {
      this.Х(var1.getPacket());
      if (this.ф.isSelected(ЯЩ8)) {
         this.шн();
      } else if (this.ф.isSelected(ЯЩ5)) {
         this.шф();
      }

      var1.setCancelled(true);
   }

   private void Х(Packet<?> var1) {
      this.А_at_3775ed2b.add(new FakeLag.DelayedPacket(var1, System.currentTimeMillis() + this.ся.getValue().longValue()));
   }

   private void шн() {
      if (!this.А_at_3775ed2b.isEmpty()) {
         for (FakeLag.DelayedPacket var2 : this.А_at_3775ed2b) {
            if (var2.delay <= System.currentTimeMillis()) {
               PacketEventBypass.А(var2.packet);
               this.А_at_3775ed2b.remove(var2);
            }
         }
      }
   }

   public void O(PacketReceiveEvent var1) {
   }

   public FakeLag() {
      super(ЯЩ衣, ЯЩш, ModuleCategory.MOVEMENT);
      this.ф = new SingleChoiceSetting(ЯЩИ, new ChoiceOption(ЯЩь), new ChoiceOption(ЯЩж));
      this.ся = new FloatSetting(ЯЩ弟, ЯЩб, 0.0F, ЯЩЫ).withStep(ЯЩl);
      this.А_at_3775ed2b = new CopyOnWriteArrayList<>();
      this.т = new Stopwatch();
      this.сУ = this::method_5;
      this.сЗ = this::O;
      this.addSettings(this.ф, this.ся);
   }

   @Override
   public void onDisable() {
      if (!this.А_at_3775ed2b.isEmpty()) {
         for (FakeLag.DelayedPacket var2 : this.А_at_3775ed2b) {
            PacketEventBypass.А(var2.packet);
            this.А_at_3775ed2b.remove(var2);
         }
      }
   }

   private void шф() {
      if (!this.А_at_3775ed2b.isEmpty() && this.т.method_0(this.ся.getValue().longValue())) {
         for (FakeLag.DelayedPacket var2 : this.А_at_3775ed2b) {
            PacketEventBypass.А(var2.packet);
            this.А_at_3775ed2b.remove(var2);
         }

         this.т.ВД();
      }
   }

   public record DelayedPacket(Packet<?> packet, long delay) {
   }
}
