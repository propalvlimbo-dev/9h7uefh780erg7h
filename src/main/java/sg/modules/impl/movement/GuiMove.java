package sg.modules.impl.movement;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import lombok.Generated;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.messenger.MessengerScreen;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public class GuiMove extends ClientModule {
   private final SingleChoiceSetting 衣;
   private final SingleChoiceSetting ш;
   private final FloatSetting й;
   private boolean с1;
   private final Set<Object> Л;
   private final EventListener<PlayerTickEvent> П;
   private static final String суз = "GuiMove";
   private static final String суц = "Свободное перемещение в гуишках";
   private static final String суя = "Обход";
   private static final String с2с = "Никакой";
   private static final String с2В = "Grim";
   private static final String с2Я = "Режим свапов";
   private static final String с2Х = "Рейджовый";
   private static final String с2е = "Легитный";
   private static final String с2А = "Тики остановки";
   private static final float с24 = 4.0F;
   private static final float с2п = 20.0F;
   private static final String с2Д = "Легитный";

   public boolean method_3ю() {
      return this.с1 || !this.Л.isEmpty();
   }

   @Generated
   public Set<Object> д_2f23a69a() {
      return this.Л;
   }

   public void з(PlayerTickEvent var1) {
      KeyMapping[] var2 = new KeyMapping[]{MINECRAFT_CLIENT.options.keyUp, MINECRAFT_CLIENT.options.keyDown, MINECRAFT_CLIENT.options.keyLeft, MINECRAFT_CLIENT.options.keyRight, MINECRAFT_CLIENT.options.keyJump};
      if (MINECRAFT_CLIENT.gui.screen() instanceof MessengerScreen) {
         for (KeyMapping var11 : var2) {
            var11.setDown(false);
         }
      } else if (!this.method_3ю() && !(MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen) && !(MINECRAFT_CLIENT.gui.screen() instanceof SignEditScreen)) {
         for (KeyMapping var6 : var2) {
            boolean var7 = InputConstants.isKeyDown(MINECRAFT_CLIENT.getWindow(), var6.key.getValue());
            var6.setDown(var7);
         }
      }
   }

   @Generated
   public SingleChoiceSetting Щ() {
      return this.ш;
   }

   @Generated
   public FloatSetting И_989fed9a() {
      return this.й;
   }

   public GuiMove() {
      super(суз, суц, ModuleCategory.MOVEMENT);
      this.衣 = new SingleChoiceSetting(суя, new ChoiceOption(с2с), new ChoiceOption(с2В));
      this.ш = new SingleChoiceSetting(с2Я, new ChoiceOption(с2Х), new ChoiceOption(с2е));
      this.й = new FloatSetting(с2А, с24, 0.0F, с2п).withStep(1.0F).visibleWhen(() -> this.ш.isSelected(с2Д));
      this.с1 = false;
      this.Л = Collections.newSetFromMap(new IdentityHashMap<>());
      this.П = this::з;
      this.addSettings(this.衣, this.ш, this.й);
   }

   @Generated
   public EventListener<PlayerTickEvent> l() {
      return this.П;
   }

   public void method_5(Object var1) {
      if (var1 != null) {
         this.Л.remove(var1);
      }
   }

   public void Ь(Object var1) {
      if (var1 != null) {
         this.Л.add(var1);
      }
   }

   @Generated
   public SingleChoiceSetting Ь() {
      return this.衣;
   }

   @Generated
   public void з(boolean var1) {
      this.с1 = var1;
   }
}
