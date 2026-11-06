package sg.network.irc;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import lombok.Generated;

public class IrcConnectionContext {
   private Channel Я;
   private ChannelPipeline В;
   private String У;
   private String З;

   @Generated
   public void Т(String var1) {
      this.У = var1;
   }

   @Generated
   public void Ъ(String var1) {
      this.З = var1;
   }

   @Generated
   public void е(Channel var1) {
      this.Я = var1;
   }

   @Generated
   public void ч(ChannelPipeline var1) {
      this.В = var1;
   }

   @Generated
   public Channel ч_04e5b147() {
      return this.Я;
   }

   @Generated
   public String ЮЩ() {
      return this.З;
   }

   @Generated
   public String Юс() {
      return this.У;
   }

   @Generated
   public ChannelPipeline ч_b50448e0() {
      return this.В;
   }
}
