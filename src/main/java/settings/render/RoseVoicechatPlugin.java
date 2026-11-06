package settings.render;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.ClientSoundEvent;
import de.maxhenkel.voicechat.api.events.ClientVoicechatConnectionEvent;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MergeClientSoundEvent;
import de.maxhenkel.voicechat.api.events.VoicechatDisableEvent;

public final class RoseVoicechatPlugin implements VoicechatPlugin {
   private static final String ЯЫщ = "rose";

   private static void onClientSound(ClientSoundEvent var0) {
      short[] var1 = VoicechatAudioPlayback.ь();
      if (var1 != null) {
         var0.setRawAudio(var1);
      }
   }

   private static void onConnection(ClientVoicechatConnectionEvent var0) {
      VoicechatStatus.у(var0.isConnected());
      VoicechatStatus.н(var0.isConnected() && var0.getVoicechat().isDisabled());
      if (!var0.isConnected()) {
         VoicechatAudioPlayback.жК();
      }
   }

   private static void onDisabled(VoicechatDisableEvent var0) {
      VoicechatStatus.н(var0.isDisabled());
      if (var0.isDisabled()) {
         VoicechatAudioPlayback.жК();
      }
   }

   private static void onMergeClientSound(MergeClientSoundEvent var0) {
      if (VoicechatAudioPlayback.жЭ()) {
         if (var0.getVoicechat().isDisconnected()) {
            VoicechatStatus.у(false);
            VoicechatAudioPlayback.жК();
         } else if (var0.getVoicechat().isDisabled()) {
            VoicechatStatus.н(true);
            VoicechatAudioPlayback.жК();
         } else {
            VoicechatStatus.у(true);
            VoicechatStatus.н(false);
            short[] var1 = VoicechatAudioPlayback.ь(960);
            if (var1 != null) {
               var0.mergeAudio(var1);
            }
         }
      }
   }

   @Override
   public String getPluginId() {
      return ЯЫщ;
   }

   @Override
   public void initialize(VoicechatApi var1) {
      VoicechatStatus.щ(true);
   }

   @Override
   public void registerEvents(EventRegistration var1) {
      var1.registerEvent(MergeClientSoundEvent.class, RoseVoicechatPlugin::onMergeClientSound);
      var1.registerEvent(ClientSoundEvent.class, RoseVoicechatPlugin::onClientSound);
      var1.registerEvent(ClientVoicechatConnectionEvent.class, RoseVoicechatPlugin::onConnection);
      var1.registerEvent(VoicechatDisableEvent.class, RoseVoicechatPlugin::onDisabled);
   }
}
