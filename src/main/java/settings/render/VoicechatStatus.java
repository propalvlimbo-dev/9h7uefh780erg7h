package settings.render;

import net.fabricmc.loader.api.FabricLoader;

public final class VoicechatStatus {
   private static volatile boolean сч;
   private static volatile boolean сЭ;
   private static volatile boolean с必;
   private static final String ЯЮГ = "voicechat";
   private static final String ЯЮл = "voicechat_api";

   public static boolean Ич() {
      return с必;
   }

   private VoicechatStatus() {
   }

   public static boolean Иг() {
      return сЭ;
   }

   static void щ(boolean var0) {
      сч = var0;
      if (!var0) {
         сЭ = false;
         с必 = false;
      }
   }

   static void н(boolean var0) {
      с必 = var0;
   }

   public static boolean ИМ() {
      FabricLoader var0 = FabricLoader.getInstance();
      return var0.isModLoaded(ЯЮГ) && var0.isModLoaded(ЯЮл);
   }

   public static boolean ИЛ() {
      return ИМ() && сч && сЭ && !с必;
   }

   public static boolean Ид() {
      return сч;
   }

   static void у(boolean var0) {
      сЭ = var0;
      if (!var0) {
         с必 = false;
      }
   }
}
