package sg.util.time;

import com.google.gson.annotations.SerializedName;
import java.util.function.Consumer;
import sg.network.irc.custom.IrcAudience;
import sg.network.irc.custom.IrcPacket;
import sg.network.irc.custom.IrcCustomPacket;

final class EmoteSyncPayloadHandler extends IrcCustomPacket {
   @SerializedName("В")
   private final EmoteSyncMessage.Scope В_at_9c937ae8;
   private final Consumer<EmoteSyncMessage> Ъ;
   private static final String Яо西 = "rose_emote_v1";

   EmoteSyncPayloadHandler(String var1, EmoteSyncMessage.Scope var2, Consumer<EmoteSyncMessage> var3) {
      super(new IrcPacket(Яо西, var2 == EmoteSyncMessage.Scope.PARTY ? IrcAudience.PARTY_ONLY : IrcAudience.ALL_USERS, var1));
      this.В_at_9c937ae8 = var2;
      this.Ъ = var3;
   }

   @Override
   public void Жу() {
      EmoteSyncMessage var1 = EmoteSyncMessage.decode(this.method_0().method_1п());
      if (var1 != null && var1.scope() == this.В_at_9c937ae8 && this.Ъ != null) {
         this.Ъ.accept(var1);
      }
   }

   EmoteSyncMessage.Scope З() {
      return this.В_at_9c937ae8;
   }
}
