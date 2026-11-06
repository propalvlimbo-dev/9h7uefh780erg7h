package sg.modules.impl.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import sg.command.KeyMacro;
import sg.ec.ClientService;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.network.ClientChat;
import sg.settings.BooleanSetting;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.events.impl.InputEvent;
import sg.settings.ChoiceOption;
import sg.settings.KeybindMode;
import sg.settings.MultiChoiceSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.ValueSetting;

public class KeybindDispatcher extends ClientService {
   private String Вв = null;
   private int А7 = 0;
   private final EventListener<InputEvent> Вя = this::О;
   private final EventListener<PlayerTickEvent> Яс = this::method_9l;
   private static final String ВгЫ = "Никакой";
   private static final String Вгl = "Grim";

   public void method_9l(PlayerTickEvent var1) {
      if (RoseClient.getInstance().getServices().getUnhookFiles().аА()) {
         this.Вв = null;
         this.А7 = 0;
      } else {
         if (this.Вв != null && !this.Вв.isEmpty() && RoseClient.getInstance().getServices().getModules().getGuiMove().Ь().isSelected(Вгl)) {
            KeyMapping[] var2 = new KeyMapping[]{
               MINECRAFT_CLIENT.options.keyUp, MINECRAFT_CLIENT.options.keyDown, MINECRAFT_CLIENT.options.keyLeft, MINECRAFT_CLIENT.options.keyRight, MINECRAFT_CLIENT.options.keyJump, MINECRAFT_CLIENT.options.keySprint
            };

            for (KeyMapping var6 : var2) {
               var6.setDown(false);
            }

            RoseClient.getInstance().getServices().getModules().getGuiMove().з(true);
            if (!MovementUtils.hasMovementInput()) {
               if (this.А7 > 0) {
                  this.А7--;
                  return;
               }

               ClientChat.т(this.Вв);
               this.Вв = null;
               RoseClient.getInstance().getServices().getModules().getGuiMove().з(false);
               if (MINECRAFT_CLIENT.gui.screen() == null) {
                  for (KeyMapping var10 : var2) {
                     var10.setDown(InputConstants.isKeyDown(MINECRAFT_CLIENT.getWindow(), var10.key.getValue()));
                  }
               }
            }
         }
      }
   }

   private void method_8(int var1) {
      for (ClientModule var3 : RoseClient.getInstance().getServices().getModules()) {
         if (var3.getKeybind().getKeyCode() == var1 && var3.getKeybind().getMode() == KeybindMode.HOLD && var3.isEnabled()) {
            var3.disable();
         }
      }
   }

   public void О(InputEvent var1) {
      if (!RoseClient.getInstance().getServices().getUnhookFiles().аА()) {
         if (var1.getInputCode() >= 0) {
            int var2 = var1.getAction();
             if (var2 == 1 || var2 == 0) {
                sg.modules.impl.render.ClickGui clickGui = RoseClient.getInstance().getServices().getModules().getClickGui();
                if (var2 == 1 && (var1.getInputCode() == 344 || clickGui.getKeybind().getKeyCode() == var1.getInputCode())) {
                   clickGui.toggle();
                   return;
                }
               if (var2 == 0) {
                  this.method_8(var1.getInputCode());
               } else if (MINECRAFT_CLIENT.gui.screen() == null) {
                  RoseClient.getInstance().getServices().getSoundPadManager().м(var1.getInputCode());

                  for (ClientModule var4 : RoseClient.getInstance().getServices().getModules()) {
                     if (var4.getKeybind().getKeyCode() == var1.getInputCode()) {
                        if (var4.getKeybind().getMode() == KeybindMode.HOLD) {
                           if (!var4.isEnabled()) {
                              var4.enable();
                           }
                        } else {
                           var4.toggle();
                        }
                     }

                     for (ValueSetting var6 : var4.getSettings()) {
                        if (var6 instanceof MultiChoiceSetting var7) {
                           for (ChoiceOption var17 : var7.getValue()) {
                              int var18 = var17.getKeybind().getKeyCode();
                              if (var18 >= 0 && var18 == var1.getInputCode()) {
                                 var17.setSelected(!var17.isSelected());
                              }
                           }
                        } else if (var6 instanceof SingleChoiceSetting var8) {
                           for (ChoiceOption var11 : var8.getValue()) {
                              int var12 = var11.getKeybind().getKeyCode();
                              if (var12 >= 0 && var12 == var1.getInputCode()) {
                                 var8.setSelectedOption(var11);
                                 break;
                              }
                           }
                        } else if (var6 instanceof BooleanSetting var9) {
                           int var10 = var9.getKeybind().getKeyCode();
                           if (var10 >= 0 && var10 == var1.getInputCode()) {
                              var9.setValue(!var9.getValue());
                           }
                        }
                     }
                  }

                  for (KeyMacro var14 : RoseClient.getInstance().getServices().getCommandShortcuts()) {
                     if (var14.О() == var1.getInputCode()) {
                        if (RoseClient.getInstance().getServices().getModules().getGuiMove().Ь().isSelected(ВгЫ)) {
                           ClientChat.т(var14.ещ());
                        } else {
                           this.Вв = var14.ещ();
                           this.А7 = 2;
                        }
                     }
                  }

               }
            }
         }
      }
   }
}
