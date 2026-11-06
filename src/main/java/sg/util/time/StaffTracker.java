package sg.util.time;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.PlayerTeam;
import settings.render.PlayerNamePatterns;
import sg.ec.MinecraftAccess;
import sg.client.RoseClient;
import sg.player.manager.VoicechatPlayerDirectory;
import sg.render.pipeline.StaffNameText;
import sg.hud.StaffList;
import sg.animation.FloatAnimation;

public class StaffTracker implements MinecraftAccess {
   @SerializedName("А")
   private final ArrayList<StaffTracker.StaffEntry> А_at_9c253277 = new ArrayList<>();
   @SerializedName("4")
   private final ArrayList<StaffTracker.StaffEntry> field_4 = new ArrayList<>();
   private final Map<String, Integer> ш = new HashMap<>();
   private static final String сРь = "Персонал";
   private static final int сРж = Integer.MIN_VALUE;
   private static final int сР弟 = Integer.MIN_VALUE;
   private static final float сРб = 0.1F;

   public void Вс() {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getConnection() != null && RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(сРь)) {
         this.field_4.clear();
         HashSet var1 = new HashSet();
         HashSet var2 = new HashSet();

         for (PlayerInfo var4 : MINECRAFT_CLIENT.player.connection.getListedOnlinePlayers()) {
            var1.add(var4.getProfile().name().toLowerCase(Locale.ROOT));
            var2.add(var4.getProfile().id());
         }

         for (VoicechatPlayerDirectory.PlayerEntry var15 : VoicechatPlayerDirectory.хч()) {
            String var5 = var15.name();
            if (PlayerNamePatterns.必().matcher(var5).matches() && !var2.contains(var15.uuid()) && !var1.contains(var5.toLowerCase(Locale.ROOT))) {
               PlayerTeam var6 = this.В(var5);
               this.field_4
                  .add(
                     new StaffTracker.StaffEntry(
                        var5,
                        var6 == null ? net.minecraft.network.chat.Component.empty() : var6.getPlayerPrefix(),
                        StaffList.StaffStatus.VANISH,
                        System.currentTimeMillis(),
                        true
                     )
                  );
            }
         }

         for (PlayerInfo var16 : MINECRAFT_CLIENT.player.connection.getListedOnlinePlayers()) {
            try {
               this.field_4.removeIf(var1x -> var1x.г_().equals(var16.getProfile().name()));
            } catch (Exception var11) {
            }

            if (var16.getTeam() != null && var16.getGameMode() == GameType.SPECTATOR) {
               this.field_4
                  .add(
                     new StaffTracker.StaffEntry(
                        var16.getProfile().name(), var16.getTeam().getPlayerPrefix(), StaffList.StaffStatus.SPECTATOR, System.currentTimeMillis(), true
                     )
                  );
            }
         }

         HashSet var14 = new HashSet();

         for (StaffTracker.StaffEntry var20 : this.field_4) {
            var14.add(var20.г_().toLowerCase(Locale.ROOT));
         }

         for (PlayerTeam var21 : MINECRAFT_CLIENT.level.getScoreboard().getPlayerTeams()) {
            int var25 = сРж;

            for (String var8 : var21.getPlayers()) {
               String var9 = var8.toLowerCase(Locale.ROOT);
               if (var1.contains(var9)) {
                  boolean var10 = RoseClient.getInstance().getServices().getStaffNames().щ(var8);
                  if (!var10) {
                     if (var25 == сР弟) {
                        var25 = this.В(var21.getPlayerPrefix());
                     }

                     if (var25 < 0 || !PlayerNamePatterns.必().matcher(var8).matches()) {
                        continue;
                     }
                  }

                  if (var14.add(var9)) {
                     this.field_4
                        .add(
                           new StaffTracker.StaffEntry(
                              var8, var21.getPlayerPrefix(), StaffList.StaffStatus.NONE, System.currentTimeMillis(), true
                           )
                        );
                  }
               }
            }
         }

         HashMap var19 = new HashMap();

         for (StaffTracker.StaffEntry var26 : this.А_at_9c253277) {
            var19.put(var26.г_(), var26);
         }

         for (StaffTracker.StaffEntry var27 : this.field_4) {
            StaffTracker.StaffEntry var32 = (StaffTracker.StaffEntry)var19.get(var27.г_());
            if (var32 == null) {
               this.А_at_9c253277.add(var27);
            } else {
               if (var32.г() != var27.г()) {
                  var32.г(var27.г());
               }

               var32.дг(true);
            }
         }

         HashSet var24 = new HashSet();

         for (StaffTracker.StaffEntry var33 : this.field_4) {
            var24.add(var33.г_());
         }

         for (StaffTracker.StaffEntry var34 : this.А_at_9c253277) {
            if (!var24.contains(var34.г_())) {
               var34.дг(false);
            }
         }

         for (StaffTracker.StaffEntry var35 : this.А_at_9c253277) {
            int var37 = Math.max(0, this.В(var35.п()));
            var35.е西 = var35.г() == StaffList.StaffStatus.NONE ? -var37 : -999 - var37;
         }

         this.А_at_9c253277.sort(Comparator.comparingInt(var0 -> var0.е西));
         this.А_at_9c253277.removeIf(var0 -> var0.Ъ().getValue() < сРб && !var0.Чв());

         for (StaffTracker.StaffEntry var36 : this.А_at_9c253277) {
            var36.Ъ().updateTarget(var36.Чв() ? 1.0F : 0.0F);
         }
      } else {
         this.А_at_9c253277.clear();
         this.field_4.clear();
         this.ш.clear();
      }
   }

   private PlayerTeam В(String var1) {
      for (PlayerTeam var3 : MINECRAFT_CLIENT.level.getScoreboard().getPlayerTeams()) {
         for (String var5 : var3.getPlayers()) {
            if (var5.equalsIgnoreCase(var1)) {
               return var3;
            }
         }
      }

      return null;
   }

   @Generated
   public ArrayList<StaffTracker.StaffEntry> ш_c1edc18e() {
      return this.field_4;
   }

   @Generated
   public ArrayList<StaffTracker.StaffEntry> Ф() {
      return this.А_at_9c253277;
   }

   @Generated
   public Map<String, Integer> ц() {
      return this.ш;
   }

   private int В(net.minecraft.network.chat.Component var1) {
      String var2 = var1.getString().toLowerCase(Locale.ROOT);
      Integer var3 = this.ш.get(var2);
      if (var3 != null) {
         return var3;
      }

      int var4 = StaffNameText.ЫO(var2) ? StaffNameText.Ч(var2) : -1;
      if (this.ш.size() >= 512) {
         this.ш.clear();
      }

      this.ш.put(var2, var4);
      return var4;
   }

   public static class StaffEntry {
      private final String ВЧ;
      @SerializedName("В")
      private final net.minecraft.network.chat.Component В_f022d65c;
      @SerializedName("В")
      private StaffList.StaffStatus В_b31586d5;
      private final long ВШ;
      private final FloatAnimation С;
      private boolean Яь;
      private int е西;
      private static final float Я0必 = 15.0F;

      @Generated
      public boolean Чв() {
         return this.Яь;
      }

      @Generated
      public long у() {
         return this.ВШ;
      }

      @Generated
      public void г(StaffList.StaffStatus var1) {
         this.В_b31586d5 = var1;
      }

      @Generated
      public int дЮ() {
         return this.е西;
      }

      public StaffEntry(String var1, net.minecraft.network.chat.Component var2, StaffList.StaffStatus var3, long var4, boolean var6) {
         this.С = new FloatAnimation(0.0F, Я0必);
         this.ВЧ = var1;
         this.В_f022d65c = var2;
         this.В_b31586d5 = var3;
         this.ВШ = var4;
         this.Яь = var6;
      }

      @Generated
      public FloatAnimation Ъ() {
         return this.С;
      }

      @Generated
      public void дг(boolean var1) {
         this.Яь = var1;
      }

      @Generated
      public String г_() {
         return this.ВЧ;
      }

      @Generated
      public StaffList.StaffStatus г() {
         return this.В_b31586d5;
      }

      @Generated
      public net.minecraft.network.chat.Component п() {
         return this.В_f022d65c;
      }

      @Generated
      public void М(int var1) {
         this.е西 = var1;
      }
   }
}
