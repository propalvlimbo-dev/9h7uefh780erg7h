package sg.modules.impl.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Generated;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.ec.EntityHealth;
import sg.events.EventListener;
import sg.network.ClientChat;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.combat.Aura;
import sg.events.impl.AttackEntityEvent;

public class KillMessage extends ClientModule {
   private static final long Хж = 3000L;
   private final BooleanSetting В_;
   private final @com.google.gson.annotations.SerializedName("ВШ")  BooleanSetting killMessagesSetting;
   private final BooleanSetting Вы;
   private final List<String> ср;
   private final List<String> с6;
   private final List<KillMessage.TrackedVictim> с_;
   LivingEntity ш;
   private final EventListener<AttackEntityEvent> Яг;
   private final EventListener<PacketReceiveEvent> Я0;
   private final EventListener<PlayerTickEvent> Ях;
   private static final String ЯЧЖ = "KillMessage";
   private static final String ЯЧм = "Пишет кастомные сообщения после убийства и сноса тотема";
   private static final String ЯЧК = "Писать в глобал";
   private static final String ЯЧI = "Сообщения убийств";
   private static final String ЯЧТ = "Сообщения тотема";
   private static final long ЯЧО = 300L;
   private static final String ЯЧФ = "!";
   private static final String ЯЧЙ = "!";
   private static final String ЯЧк = "{name}";
   private static final String ЯЧг = "{nick}";
   private static final String ЯЧ0 = "%name%";
   private static final String ЯЧх = "{name}";
   private static final String ЯЧУ = "{nick}";
   private static final String ЯЧЗ = "%name%";
   private static final String ЯЧЕ = "Settings";
   private static final String ЯЧП = "Settings";
   private static final String ЯЧ西 = "KillMessages";
   private static final String ЯЧд = "TotemMessages";
   private static final String ЯЧБ = "Settings";
   private static final String ЯЧС = "Settings";
   private static final String ЯЧз = "Settings";
   private static final String ЯЧц = "KillMessages";
   private static final String ЯЧя = "KillMessages";
   private static final String ЯЛс = "KillMessages";
   private static final String ЯЛВ = "TotemMessages";
   private static final String ЯЛЯ = "TotemMessages";
   private static final String ЯЛХ = "TotemMessages";

   private boolean Т(String var1) {
      return var1.contains(ЯЧк) || var1.contains(ЯЧг) || var1.contains(ЯЧ0);
   }

   public boolean Ы(int var1) {
      if (var1 >= 0 && var1 < this.с6.size()) {
         this.с6.remove(var1);
         return true;
      } else {
         return false;
      }
   }

   public boolean method_8(int var1) {
      if (var1 >= 0 && var1 < this.ср.size()) {
         this.ср.remove(var1);
         return true;
      } else {
         return false;
      }
   }

   public boolean ы(String var1) {
      return this.с6.removeIf(var1x -> var1x.equalsIgnoreCase(var1.trim()));
   }

   private String й(String var1, String var2) {
      return var1.replace(ЯЧх, var2).replace(ЯЧУ, var2).replace(ЯЧЗ, var2);
   }

   @Generated
   public List<String> к() {
      return this.с6;
   }

   public KillMessage() {
      super(ЯЧЖ, ЯЧм, ModuleCategory.MISC);
      this.В_ = new BooleanSetting(ЯЧК, true);
      this.killMessagesSetting = new BooleanSetting(ЯЧI, true);
      this.Вы = new BooleanSetting(ЯЧТ, true);
      this.ср = new ArrayList<>();
      this.с6 = new ArrayList<>();
      this.с_ = new ArrayList<>();
      this.Яг = this::П;
      this.Я0 = this::Б;
      this.Ях = this::method_0З;
      this.addSettings(this.В_, this.killMessagesSetting, this.Вы);
   }

   public boolean в(String var1) {
      return this.ср.removeIf(var1x -> var1x.equalsIgnoreCase(var1.trim()));
   }

   @Generated
   public BooleanSetting getKillMessagesSetting() {
      return this.killMessagesSetting;
   }

   @Generated
   public BooleanSetting ч_3276742a() {
      return this.В_;
   }

   @Override
   public void onDisable() {
      this.с_.clear();
   }

   private void Ф(LivingEntity var1) {
      KillMessage.TrackedVictim var2 = this.O(var1);
      if (var2 != null && var2.В9 && !var2.Во) {
         var2.Во = true;
         if (!this.ср.isEmpty()) {
            String var3 = this.O(this.ср);
            String var4 = var1.getName().getString();
            ClientChat.т(this.Г(this.К(var3, var4)));
         }
      }
   }

   @Generated
   public BooleanSetting в_3276742a() {
      return this.Вы;
   }

   @Generated
   public List<String> ч_2571477d() {
      return this.ср;
   }

   public boolean method_5(String var1) {
      String var2 = this.И(var1);
      return var2 != null && !this.ср.contains(var2) ? this.ср.add(var2) : false;
   }

   private String К(String var1, String var2) {
      String var3 = this.й(var1, var2);
      return this.Т(var1) ? var3 : var2 + " " + var3;
   }

   private void method_0З(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         try {
            Aura var2 = RoseClient.getInstance().getServices().getModules().getAura();
            if (var2 != null && var2.isEnabled() && var2.Ж_95bedd6e() instanceof Player var3 && var3.isAlive()) {
               this.ж(var3, false);
            }
         } catch (Throwable var7) {
         }

         Aura var8 = RoseClient.getInstance().getServices().getModules().getAura();
         if (var8.isEnabled() && var8.Ж_95bedd6e() != null && var8.Ж_95bedd6e() != this.ш) {
            this.ш = var8.Ж_95bedd6e();
         }

         Iterator var9 = this.с_.iterator();

         while (var9.hasNext()) {
            KillMessage.TrackedVictim var10 = (KillMessage.TrackedVictim)var9.next();
            if (!var10.Ф弟()) {
               var9.remove();
            } else {
               LivingEntity var5 = var10.Ъ;
               boolean var6 = MINECRAFT_CLIENT.level.getEntity(var5.getId()) == null;
               if ((var5.getHealth() <= 0.0F || !var5.isAlive() || var6) && var10.В9 && !var10.Во) {
                  if (this.killMessagesSetting.getValue()) {
                     this.Ф(var5);
                  }

                  var10.Во = true;
                  var10.М(ЯЧО);
               }
            }
         }

         this.с_.removeIf(var0 -> !var0.Ф弟());
      } else {
         this.с_.clear();
      }
   }

   private KillMessage.TrackedVictim O(LivingEntity var1) {
      if (var1 == null) {
         return null;
      }

      for (KillMessage.TrackedVictim var3 : this.с_) {
         if (var3.Ъ.getId() == var1.getId()) {
            return var3;
         }
      }

      return null;
   }

   @Generated
   public EventListener<PlayerTickEvent> 弟() {
      return this.Ях;
   }

   private void ж(LivingEntity var1, boolean var2) {
      if (var1 != null && var1 != MINECRAFT_CLIENT.player && var1 instanceof Player) {
         for (KillMessage.TrackedVictim var4 : this.с_) {
            if (var4.Ъ.getId() == var1.getId()) {
               var4.г(var1);
               if (var2) {
                  var4.ъЗ();
               }

               return;
            }
         }

         if (var2) {
            this.с_.clear();
            KillMessage.TrackedVictim var5 = new KillMessage.TrackedVictim(var1);
            var5.ъЗ();
            this.с_.add(var5);
         }
      }
   }

   public void й4() {
      this.ср.clear();
   }

   private String И(String var1) {
      if (var1 == null) {
         return null;
      }

      String var2 = var1.trim();
      return var2.isEmpty() ? null : var2;
   }

   @Override
   public JsonObject saveConfig() {
      JsonObject var1 = super.saveConfig();
      JsonObject var2 = var1.has(ЯЧЕ) ? var1.getAsJsonObject(ЯЧП) : new JsonObject();
      JsonArray var3 = new JsonArray();

      for (String var5 : this.ср) {
         var3.add(var5);
      }

      var2.add(ЯЧ西, var3);
      JsonArray var7 = new JsonArray();

      for (String var6 : this.с6) {
         var7.add(var6);
      }

      var2.add(ЯЧд, var7);
      var1.add(ЯЧБ, var2);
      return var1;
   }

   @Generated
   public LivingEntity э_95bedd6e() {
      return this.ш;
   }

   private boolean Ы(LivingEntity var1) {
      KillMessage.TrackedVictim var2 = this.O(var1);
      return var2 != null && var2.Ф弟() && var2.В9;
   }

   private void м(LivingEntity var1) {
      KillMessage.TrackedVictim var2 = this.O(var1);
      if (var2 != null && var2.В9) {
         if (!this.с6.isEmpty()) {
            String var3 = this.O(this.с6);
            String var4 = var1.getName().getString();
            ClientChat.т(this.Г(this.К(var3, var4)));
         }
      }
   }

   private String O(List<String> var1) {
      return (String)var1.get(ThreadLocalRandom.current().nextInt(var1.size()));
   }

   @Override
   public void loadConfig(JsonObject var1) {
      super.loadConfig(var1);
      if (var1 != null && var1.has(ЯЧС)) {
         JsonObject var2 = var1.getAsJsonObject(ЯЧз);
         this.ср.clear();
         this.с6.clear();
         if (var2.has(ЯЧц) && var2.get(ЯЧя).isJsonArray()) {
            for (JsonElement var4 : var2.getAsJsonArray(ЯЛс)) {
               if (var4.isJsonPrimitive()) {
                  this.method_5(var4.getAsString());
               }
            }
         }

         if (var2.has(ЯЛВ) && var2.get(ЯЛЯ).isJsonArray()) {
            for (JsonElement var6 : var2.getAsJsonArray(ЯЛХ)) {
               if (var6.isJsonPrimitive()) {
                  this.н(var6.getAsString());
               }
            }
         }
      }
   }

   private void П(AttackEntityEvent var1) {
      if (this.isEnabled() && var1.getPhase() == AttackEntityEvent.Phase.POST && var1.getAttacker() == MINECRAFT_CLIENT.player) {
         if (var1.getTarget() instanceof LivingEntity var2 && var2.isAlive()) {
            if (var2 instanceof Player) {
               this.ж(var2, true);
            }
         }
      }
   }

   @Generated
   public EventListener<PacketReceiveEvent> м_f8db0d09() {
      return this.Я0;
   }

   @Generated
   public List<KillMessage.TrackedVictim> в_2571477d() {
      return this.с_;
   }

   private void Б(PacketReceiveEvent var1) {
      if (this.isEnabled() && MINECRAFT_CLIENT.level != null) {
         if (var1.getPacket() instanceof ClientboundEntityEventPacket var2) {
            if (var2.getEntity(MINECRAFT_CLIENT.level) instanceof Player var4 && var4 != MINECRAFT_CLIENT.player) {
               if (this.Ы(var4)) {
                  Aura var5 = RoseClient.getInstance().getServices().getModules().getAura();
                  if (var2.getEventId() == 35) {
                     if (!this.Вы.getValue()) {
                        return;
                     }

                     MINECRAFT_CLIENT.execute(() -> this.м(var4));
                  } else if (this.ш != null && EntityHealth.getHealth(this.ш) <= 0.0F && !this.ш.isAlive()) {
                     if (!this.killMessagesSetting.getValue()) {
                        return;
                     }

                     MINECRAFT_CLIENT.execute(() -> this.Ф(var4));
                  }
               }
            }
         }
      }
   }

   public void й6() {
      this.с6.clear();
   }

   @Generated
   public EventListener<AttackEntityEvent> ш_f8db0d09() {
      return this.Яг;
   }

   public boolean н(String var1) {
      String var2 = this.И(var1);
      return var2 != null && !this.с6.contains(var2) ? this.с6.add(var2) : false;
   }

   private String Г(String var1) {
      if (var1 != null && !var1.isBlank()) {
         String var2 = var1.trim();
         if (!this.В_.getValue()) {
            return var2.startsWith(ЯЧЙ) ? var2.substring(1).trim() : var2;
         } else {
            return var2.startsWith(ЯЧФ) ? var2 : "!" + var2;
         }
      } else {
         return var1;
      }
   }

   private static final class TrackedVictim {
      private LivingEntity Ъ;
      private long ВЧ;
      private long ВЛ;
      private boolean В9;
      private boolean Во;
      private static final long Вй诶 = 3000L;
      private static final long Вй1 = 3000L;

      private void М(long var1) {
         this.ВЛ = var1;
         this.ВЧ = System.currentTimeMillis();
      }

      private boolean Ф弟() {
         return this.Ъ != null && System.currentTimeMillis() - this.ВЧ < this.ВЛ;
      }

      private void ъЗ() {
         this.В9 = true;
         this.ВЧ = System.currentTimeMillis();
      }

      private void г(LivingEntity var1) {
         this.Ъ = var1;
         this.ВЧ = System.currentTimeMillis();
         this.ВЛ = Вй1;
         if (var1.getHealth() > 0.0F) {
            this.Во = false;
         }
      }

      private TrackedVictim(LivingEntity var1) {
         this.ВЛ = Вй诶;
         this.г(var1);
      }
   }
}
