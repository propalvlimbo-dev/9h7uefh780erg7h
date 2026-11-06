package sg.ec;

import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import sg.util.time.Stopwatch;
import sg.events.EventListener;
import sg.events.impl.Frame60Event;
import sg.events.impl.PacketReceiveEvent;

public class CommandPingProbe extends ClientService {
   public static final Stopwatch field_2 = new Stopwatch();
   public static final Stopwatch ф = new Stopwatch();
   public static long е诶;
   public static int ЪУ;
   public static boolean АЙ;
   private final EventListener<Frame60Event> еж = this::method_7;
   private final EventListener<PacketReceiveEvent> е弟 = this::I;
   private static final double сМг = 5000.0;
   private static final double сМ0 = 180.0;
   private static final String сМх = "w ";

   private void method_7(Frame60Event var1) {
      if (ф.Т(сМг) || АЙ && ф.Т(сМ0)) {
         PacketEventBypass.method_4(new ServerboundCommandSuggestionPacket(1337, сМх));
         е诶 = System.currentTimeMillis();
         field_2.ВД();
         ф.ВД();
         АЙ = false;
      }
   }

   private void I(PacketReceiveEvent var1) {
      if (var1.getPacket() instanceof ClientboundCommandSuggestionsPacket var2 && var2.id() == 1337) {
         ЪУ = Math.clamp(System.currentTimeMillis() - е诶, 0, 1000);
         АЙ = true;
      }
   }
}
