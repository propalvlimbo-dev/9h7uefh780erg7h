package sg.modules.impl.misc;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.render.pipeline.InventoryActions;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.SingleChoiceSetting;

public class AutoTool extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("Ь")  SingleChoiceSetting modeSetting;
   private @com.google.gson.annotations.SerializedName("Вт")  int nextSelectedSlot;
   private @com.google.gson.annotations.SerializedName("Ва")  int originalSelectedSlot;
   private final @com.google.gson.annotations.SerializedName("Е")  EventListener<PlayerTickEvent> playerTickListener;
   private static final String Я必4 = "AutoTool";
   private static final String Я必п = "Автоматически берет в руку лучший инструмент для ломания блока";
   private static final String Я必Д = "Режим";
   private static final String Я必Ъ = "Обычный";
   private static final String Я必т = "Скрытый";
   private static final String Я必а = "Обычный";

   public void в(PlayerTickEvent var1) {
      if (this.шб()) {
         if (this.modeSetting.isSelected(Я必а)) {
            this.诶ч();
         }
      }
   }

   public AutoTool() {
      super(Я必4, Я必п, ModuleCategory.MISC);
      this.modeSetting = new SingleChoiceSetting(Я必Д, new ChoiceOption(Я必Ъ), new ChoiceOption(Я必т));
      this.nextSelectedSlot = -1;
      this.originalSelectedSlot = -1;
      this.playerTickListener = this::в;
      this.addSettings(this.modeSetting);
   }

   private void 诶ч() {
      MINECRAFT_CLIENT.player.getInventory().setSelectedSlot(this.nextSelectedSlot);
      this.nextSelectedSlot = -1;
   }

   private boolean шб() {
      if (MINECRAFT_CLIENT.options.keyAttack.isDown()) {
         if (MINECRAFT_CLIENT.hitResult instanceof BlockHitResult var1 && var1.getType().equals(Type.BLOCK)) {
            this.nextSelectedSlot = InventoryActions.б(MINECRAFT_CLIENT.level.getBlockState(var1.getBlockPos()));
            if (this.originalSelectedSlot == -1) {
               this.originalSelectedSlot = MINECRAFT_CLIENT.player.getInventory().selected;
            }
         }
      } else if (this.originalSelectedSlot != -1) {
         this.nextSelectedSlot = this.originalSelectedSlot;
         this.originalSelectedSlot = -1;
      }

      return this.nextSelectedSlot >= 0;
   }
}
