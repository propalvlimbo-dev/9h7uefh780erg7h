package dev.rose.module.impl.sound;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.ActionSetting;
import dev.rose.setting.NumberSetting;
import dev.rose.setting.SoundSetting;
import dev.rose.sound.SoundLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;


public final class EventSoundsModule extends Module {
   private final NumberSetting volume = this.add(new NumberSetting("volume", 80.0, 0.0, 100.0, 5.0, "rose.unit.percent"));
   private final SoundSetting totem;
   private final SoundSetting lowHealth;
   private final NumberSetting threshold;
   private final SoundSetting death;
   private final SoundSetting join;
   private final SoundSetting levelUp;
   private final ActionSetting openFolder;
   private long lastHeartbeat;
   private int lastLevel;
   private boolean wasDead;

   public EventSoundsModule() {
      super("event_sounds", Category.SOUNDS, false);
      this.section("totem", 58647);
      this.totem = this.add(new SoundSetting("totem", "totem_chime"));
      this.section("health", 57586);
      this.lowHealth = this.add(new SoundSetting("low_health", "alarm"));
      this.threshold = this.add(new NumberSetting("threshold", 3.0, 1.0, 10.0, 0.5, "rose.unit.hearts"));
      this.death = this.add(new SoundSetting("death", "death_down"));
      this.section("other", 57433);
      this.join = this.add(new SoundSetting("join", "join_confirm"));
      this.levelUp = this.add(new SoundSetting("level_up", "none"));
      this.openFolder = this.add(new ActionSetting("open_folder", 57927, HitSoundsModule::openFolder));
      this.lastLevel = -1;
   }

   @Override
   public int icon() {
      return 57433;
   }

   private void play(SoundSetting sound) {
      SoundLibrary.play(sound.get(), this.volume.floatValue() / 100.0F, 1.0F);
   }

   public void onTotem(Entity entity) {
      if (this.isEnabled() && entity == Minecraft.getInstance().player) {
         this.play(this.totem);
      }
   }

   public void onJoin() {
      if (this.isEnabled()) {
         this.play(this.join);
      }
   }

   @Override
   public void onTick() {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player == null) {
         this.lastLevel = -1;
      } else {
         boolean dead = player.isDeadOrDying();
         if (dead && !this.wasDead) {
            this.play(this.death);
         }

         this.wasDead = dead;
         long now = System.currentTimeMillis();
         if (!dead
            && player.getHealth() <= this.threshold.floatValue() * 2.0F
            && now - this.lastHeartbeat > 1100L
            && !player.isCreative()
            && !player.isSpectator()) {
            this.lastHeartbeat = now;
            this.play(this.lowHealth);
         }

         if (this.lastLevel >= 0 && player.experienceLevel > this.lastLevel) {
            this.play(this.levelUp);
         }

         this.lastLevel = player.experienceLevel;
      }
   }
}
