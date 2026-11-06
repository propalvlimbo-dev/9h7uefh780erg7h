package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import sg.network.irc.RoseIrc;
import sg.network.irc.packet.IrcPacket;
import sg.network.irc.packet.IrcPacketMetadata;

public abstract class CallbackIrcPacket extends IrcPacket {
   @SerializedName("В")
   private IrcResponseCallback В_at_73e4ac47;

   public CallbackIrcPacket П(IrcResponseCallback var1) {
      RoseIrc.З_3e6e5516().З_677a0540().М_6aee10a2().add(this);
      this.В_at_73e4ac47 = var1;
      return this;
   }

   @Override
   public void З() {
   }

   @Generated
   public IrcResponseCallback П() {
      return this.В_at_73e4ac47;
   }

   public CallbackIrcPacket(IrcPacketMetadata var1) {
      super(var1);
   }
}
