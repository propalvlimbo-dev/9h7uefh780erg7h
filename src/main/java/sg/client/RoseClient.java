package sg.client;

import com.google.gson.annotations.SerializedName;
import lombok.Generated;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import sg.ec.SelfcodeService;
import sg.ec.RussianClientLocalization;
import sg.network.irc.IrcDebugMessagePacket;
import sg.network.irc.IrcMessagePacket;
import sg.ec.GuiEnglishTranslations;
import sg.network.irc.IrcResponseCallbacks;
import sg.handler.PartyClient;
import sg.network.ClientChat;
import sg.network.IrcRoles;
import sg.network.irc.RoseIrc;
import sg.messenger.MessengerClient;
import sg.events.EventBus;

public class RoseClient {
   private static RoseClient instance;
   @SerializedName("В")
   private final ClientServices services;
   @SerializedName("В")
   private final ClientControllerRegistry controllers;
   @SerializedName("В")
   private final EventBus eventBus;
   @SerializedName("В")
   private final SelfcodeService selfcodeService;
   private static final String ХЬк = "jold1k1";
   private static final String IRC_HOST = "217.60.245.54";
   private static final String IRC_AUTH_VALUE = "rose";
   private static final String INITIALIZED_MESSAGE = "Inited!";

   @Generated
   public ClientServices getServices() {
      return this.services;
   }

   public void scheduleConfigurationLoad() {
      this.services.getConfigManager().scheduleLoadAll();
   }

   @Generated
   public EventBus getEventBus() {
      return this.eventBus;
   }

   @Generated
   public ClientControllerRegistry getControllers() {
      return this.controllers;
   }

   @Generated
   public SelfcodeService getSelfcodeService() {
      return this.selfcodeService;
   }

   public RoseClient() {
      instance = this;
      GuiEnglishTranslations.еВ();
      this.eventBus = new EventBus();
      this.services = new ClientServices();
      this.controllers = new ClientControllerRegistry();
      this.selfcodeService = new SelfcodeService();
      Object var10004 = null;
      var10004 = null;
      RoseIrc ircClient = new RoseIrc(IRC_HOST, 4892, "@pasterplace", IRC_AUTH_VALUE, false);
      ircClient.З_677a0540().М_5774a79f().д(new RussianClientLocalization());
      PartyClient.Х_4d6328fc().ХП();
      MessengerClient.method_4_8dfd5497().method_9();
      IrcResponseCallbacks.н(IrcMessagePacket.class, messageParts -> {
         if (this.services.getModules().getIrcModule().isEnabled()) {
            Component chatMessage = IrcRoles.method_4(messageParts[0], messageParts[2]);
            Minecraft.getInstance().execute(() -> ClientChat.З(chatMessage));
         }
      });
      IrcResponseCallbacks.н(IrcDebugMessagePacket.class, responseParts -> {
         if (Integer.parseInt(responseParts[0]) == 1) {
            this.controllers.getClientTicks().getPendingChatMessages().add(responseParts[1]);
         } else if (Integer.parseInt(responseParts[0]) == 2) {
            System.out.println(responseParts[1]);
         }
      });
      ClientLifecycleEvents.CLIENT_STOPPING.register(minecraftClient -> ircClient.close());
      ircClient.жв();
      this.scheduleConfigurationLoad();
      System.out.println(INITIALIZED_MESSAGE);
   }

   @Generated
   public static RoseClient getInstance() {
      return instance;
   }
}
