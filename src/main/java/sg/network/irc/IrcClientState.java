package sg.network.irc;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import sg.network.irc.IrcLocalization;
import sg.network.irc.IrcPartyMembers;
import sg.network.irc.IrcPacketRegistry;
import sg.network.irc.IrcClientUpdates;
import sg.network.irc.IrcCustomPackets;
import sg.network.irc.PendingIrcRequests;

public class IrcClientState {
   @SerializedName("В")
   private final IrcPacketRegistry В_62946491 = new IrcPacketRegistry();
   @SerializedName("В")
   private final IrcCustomPackets В_4c863fd0 = new IrcCustomPackets();
   @SerializedName("В")
   private final IrcLocalization В_5774a79f = new IrcLocalization();
   @SerializedName("В")
   private final IrcClientUpdates В_7a59fa43 = new IrcClientUpdates();
   @SerializedName("В")
   private final IrcPartyMembers В_8cef9e59 = new IrcPartyMembers();
   @SerializedName("В")
   private final PendingIrcRequests В_6aee10a2 = new PendingIrcRequests();

   @Generated
   public IrcPartyMembers М_8cef9e59() {
      return this.В_8cef9e59;
   }

   @Generated
   public IrcLocalization М_5774a79f() {
      return this.В_5774a79f;
   }

   @Generated
   public IrcPacketRegistry М_62946491() {
      return this.В_62946491;
   }

   @Generated
   public PendingIrcRequests М_6aee10a2() {
      return this.В_6aee10a2;
   }

   @Generated
   public IrcClientUpdates М_7a59fa43() {
      return this.В_7a59fa43;
   }

   @Generated
   public IrcCustomPackets М_4c863fd0() {
      return this.В_4c863fd0;
   }
}
