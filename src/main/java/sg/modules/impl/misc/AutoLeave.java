package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.entity.player.Player;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.EntityAttackEvent;
import sg.events.EventListener;
import sg.ec.BaritoneIntegration;
import sg.network.ClientChat;
import sg.settings.BooleanSetting;
import sg.util.time.StaffTracker;
import sg.hud.StaffList;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.MultiChoiceSetting;
import sg.settings.SingleChoiceSetting;

public final class AutoLeave extends ClientModule {
   private static final long ВП = 15000L;
   private final MultiChoiceSetting ж;
   @SerializedName("0")
   private final SingleChoiceSetting field_0_at_32376789;
   private final BooleanSetting сУ;
   private final FloatSetting ЯЮ;
   private final FloatSetting ЯЧ;
   private final BooleanSetting сЗ;
   @SerializedName("Я")
   private final StaffTracker Я_at_32376789;
   private final EventListener<PlayerTickEvent> ВФ;
   private final EventListener<EntityAttackEvent> ВЙ;
   private long В西;
   private boolean ЯГ;
   private String В8;
   private static final String у9 = "AutoLeave";
   private static final String уо = "Уходит с сервера при опасных условиях выбранным способом";
   private static final String уO = "Ливать если";
   private static final String у7 = "Мало здоровья";
   private static final String уР = "Рядом игрок";
   private static final String у3 = "Администратор в ванише";
   private static final String ур = "Тип ухода";
   private static final String у6 = "Спавн";
   private static final String у_ = "Домой";
   private static final String уШ = "Хаб";
   private static final String уы = "Ливать только в конце PvP";
   private static final String уй = "Здоровье";
   private static final float уЖ = 15.0F;
   private static final float ум = 20.0F;
   private static final String уК = "Радиус";
   private static final float уI = 10.0F;
   private static final float уТ = 50.0F;
   private static final String уО = "Отключать Baritone";
   private static final String уФ = "Мало здоровья";
   private static final String уЙ = "мало здоровья";
   private static final String ук = "Рядом игрок";
   private static final String уг = "Администратор в ванише";
   private static final String у0 = "Домой";
   private static final String ух = "home";
   private static final String уУ = "Хаб";
   private static final String уЗ = "hub";
   private static final String уЕ = "spawn";
   private static final String уП = "Мало здоровья";
   private static final String у西 = "Рядом игрок";
   private static final String уд = "Администратор в ванише";
   private static final long уБ = 15000L;
   private static final String уС = "Рядом игрок";
   private static final String уз = "Мало здоровья";

   private boolean щИ() {
      return this.ж.isSelected(уП) || this.ж.isSelected(у西) || this.ж.isSelected(уд);
   }

   private void ч(EntityAttackEvent var1) {
      if (MINECRAFT_CLIENT.player != null && var1.getAttacker() == MINECRAFT_CLIENT.player) {
         if (var1.getTarget() instanceof Player var2) {
            if (!this.д(var2)) {
               this.щЯ();
            }
         }
      }
   }

   private Player Э_d012b85c() {
      double var1 = this.ЯЧ.getValue() * this.ЯЧ.getValue();

      for (Player var4 : MINECRAFT_CLIENT.level.players()) {
         if (var4 != MINECRAFT_CLIENT.player && var4.isAlive() && !var4.isSpectator() && !this.д(var4) && MINECRAFT_CLIENT.player.distanceToSqr(var4) <= var1) {
            return var4;
         }
      }

      return null;
   }

   private void х(String var1) {
      if (this.сУ.getValue() && this.щЗ()) {
         this.ЯГ = true;
         this.В8 = var1;
         ClientChat.З("Auto Leave ждёт окончания PvP: " + var1);
      } else {
         this.а(var1);
      }
   }

   private void Ам(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player.connection != null) {
         if (MINECRAFT_CLIENT.player.hurtTime > 0 && MINECRAFT_CLIENT.player.getLastHurtByMob() instanceof Player var2 && !this.д(var2)) {
            this.щЯ();
         }

         if (!this.ЯГ) {
            if (this.ж.isSelected(уФ) && MINECRAFT_CLIENT.player.getHealth() <= this.ЯЮ.getValue()) {
               this.х(уЙ);
            } else {
               if (this.ж.isSelected(ук)) {
                  Player var4 = this.Э_d012b85c();
                  if (var4 != null) {
                     this.х("рядом игрок " + var4.getGameProfile().name());
                     return;
                  }
               }

               if (this.ж.isSelected(уг)) {
                  this.Я_at_32376789.Вс();
                  StaffTracker.StaffEntry var5 = this.Я_at_32376789
                     .ш_c1edc18e()
                     .stream()
                     .filter(var0 -> var0.г() == StaffList.StaffStatus.VANISH)
                     .findFirst()
                     .orElse(null);
                  if (var5 != null) {
                     this.х("администратор в ванише: " + var5.г_());
                  }
               }
            }
         } else {
            if (!this.сУ.getValue() || !this.щЗ()) {
               this.а(this.В8);
            }
         }
      }
   }

   private void щЯ() {
      this.В西 = System.currentTimeMillis();
   }

   private void а(String var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.player.connection != null) {
         if (this.сЗ.getValue() && BaritoneIntegration.ЮЧ()) {
            BaritoneIntegration.г2();
         }

         String var2;
         if (this.field_0_at_32376789.isSelected(у0)) {
            var2 = ух;
         } else if (this.field_0_at_32376789.isSelected(уУ)) {
            var2 = уЗ;
         } else {
            var2 = уЕ;
         }

         MINECRAFT_CLIENT.player.connection.sendCommand(var2);
         ClientChat.З("Auto Leave: /" + var2 + " — " + var1);
         this.disable();
      }
   }

   private boolean д(Player var1) {
      return RoseClient.getInstance().getServices().getFriends().О(var1.getGameProfile().name());
   }

   @Override
   public void onDisable() {
      this.В西 = 0L;
      this.ЯГ = false;
      this.В8 = "";
      super.onDisable();
   }

   public AutoLeave() {
      super(у9, уо, ModuleCategory.MISC);
      this.ж = new MultiChoiceSetting(уO, new ChoiceOption(у7, true), new ChoiceOption(уР, true), new ChoiceOption(у3, true));
      this.field_0_at_32376789 = new SingleChoiceSetting(ур, new ChoiceOption(у6), new ChoiceOption(у_), new ChoiceOption(уШ)).visibleWhen(this::щИ);
      this.сУ = new BooleanSetting(уы, false).visibleWhen(this::щИ);
      this.ЯЮ = new FloatSetting(уй, уЖ, 1.0F, ум).withStep(1.0F).visibleWhen(() -> this.ж.isSelected(уз));
      this.ЯЧ = new FloatSetting(уК, уI, 1.0F, уТ).withStep(1.0F).visibleWhen(() -> this.ж.isSelected(уС));
      this.сЗ = new BooleanSetting(уО, true).visibleWhen(this::щИ);
      this.Я_at_32376789 = new StaffTracker();
      this.ВФ = this::Ам;
      this.ВЙ = this::ч;
      this.В8 = "";
      this.addSettings(this.ж, this.field_0_at_32376789, this.сУ, this.ЯЮ, this.ЯЧ, this.сЗ);
   }

   @Override
   public void onEnable() {
      this.В西 = 0L;
      this.ЯГ = false;
      this.В8 = "";
      super.onEnable();
   }

   private boolean щЗ() {
      return this.В西 > 0L && System.currentTimeMillis() - this.В西 < уБ;
   }
}
