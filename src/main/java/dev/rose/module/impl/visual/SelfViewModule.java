package dev.rose.module.impl.visual;

import dev.rose.module.Category;
import dev.rose.module.Module;
import dev.rose.setting.BoolSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;


public final class SelfViewModule extends Module {
   private final BoolSetting arms = this.add(new BoolSetting("arms", false));
   private final BoolSetting cape = this.add(new BoolSetting("cape", true));
   private final BoolSetting whileEating = this.add(new BoolSetting("while_eating", true));

   public SelfViewModule() {
      super("self_view", Category.VISUALS, false);
   }

   @Override
   public int icon() {
      return 58472;
   }

   public boolean showsSelf() {
      Minecraft mc = Minecraft.getInstance();
      if (!this.isEnabled() || mc.player == null || !mc.options.getCameraType().isFirstPerson()) {
         return false;
      } else {
         return !mc.player.isSleeping() && !mc.player.isScoping() ? this.whileEating.isOn() || !mc.player.isUsingItem() : false;
      }
   }

   public void hideParts(PlayerModel model) {
      model.head.visible = false;
      model.hat.visible = false;
      if (!this.arms.isOn()) {
         model.leftArm.visible = false;
         model.rightArm.visible = false;
         model.leftSleeve.visible = false;
         model.rightSleeve.visible = false;
      }
   }

   public boolean showsCape() {
      return this.cape.isOn();
   }

   public boolean isSelf(Object entity) {
      return entity instanceof LocalPlayer && this.showsSelf();
   }

   public boolean isSelf(AvatarRenderState state) {
      LocalPlayer player = Minecraft.getInstance().player;
      return player != null && state.id == player.getId() && this.showsSelf();
   }
}
