package sg.modules.impl.movement;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.Items;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.MultiChoiceSetting;

public class NoDelay extends ClientModule {
   @SerializedName("Я")
   private final MultiChoiceSetting Я_at_201ae223;
   private final EventListener<PlayerTickEvent> у;
   private static final String ВН诶 = "NoDelay";
   private static final String ВН1 = "Убирает задержки";
   private static final String ВНЬ = "Элементы";
   private static final String ВН衣 = "Прыжок";
   private static final String ВНш = "ПКМ";
   private static final String ВНИ = "Бутыльки с опытом";
   private static final String ВНь = "Прыжок";
   private static final String ВНж = "ПКМ";
   private static final String ВН弟 = "Бутыльки с опытом";

   public void н(PlayerTickEvent var1) {
      if (this.Я_at_201ae223.isSelected(ВНь)) {
         MINECRAFT_CLIENT.player.noJumpDelay = 0;
      }

      if (this.Я_at_201ae223.isSelected(ВНж)) {
         MINECRAFT_CLIENT.rightClickDelay = 0;
      }

      if (this.Я_at_201ae223.isSelected(ВН弟)) {
         boolean var2 = MINECRAFT_CLIENT.player.getMainHandItem().getItem() == Items.EXPERIENCE_BOTTLE || MINECRAFT_CLIENT.player.getOffhandItem().getItem() == Items.EXPERIENCE_BOTTLE;
         if (var2) {
            MINECRAFT_CLIENT.rightClickDelay = 0;
         }
      }
   }

   public NoDelay() {
      super(ВН诶, ВН1, ModuleCategory.MOVEMENT);
      this.Я_at_201ae223 = new MultiChoiceSetting(ВНЬ, new ChoiceOption(ВН衣, true), new ChoiceOption(ВНш, false), new ChoiceOption(ВНИ, true));
      this.у = this::н;
      this.addSettings(this.Я_at_201ae223);
   }
}
