package sg.modules.impl.render;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import sg.events.impl.EntityRenderStateEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.SingleChoiceSetting;

public class SeeInvisible extends ClientModule {
   private final SingleChoiceSetting сМ;
   private final EventListener<EntityRenderStateEvent> Хя;
   private static final String сЧЪ = "SeeInvisible";
   private static final String сЧт = "Показывает невидимых сущностей";
   private static final String сЧа = "Режим";
   private static final String сЧЮ = "Прозрачный";
   private static final String сЧЧ = "Непрозрачный";
   private static final String сЧЛ = "Непрозрачный";

   public SeeInvisible() {
      super(сЧЪ, сЧт, ModuleCategory.RENDER);
      this.сМ = new SingleChoiceSetting(сЧа, new ChoiceOption(сЧЮ), new ChoiceOption(сЧЧ));
      this.Хя = this::Щ;
      this.addSettings(this.сМ);
   }

   private void Щ(EntityRenderStateEvent var1) {
      if (this.isEnabled() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (var1.getEntity() instanceof LivingEntity var3 && var3 != MINECRAFT_CLIENT.player && var3.isInvisible()) {
            if (var1.getRenderState() instanceof LivingEntityRenderState var4) {
               if (this.сМ.isSelected(сЧЛ)) {
                  var4.isInvisible = false;
                  var4.isInvisibleToPlayer = false;
               } else {
                  var4.isInvisible = true;
                  var4.isInvisibleToPlayer = false;
               }
            }
         }
      }
   }
}
