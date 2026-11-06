package com.deluxe.modules.impl.player;

import com.deluxe.util.time.Setting;

public final class BindSetting extends Setting<BindSetting> {
   private InputBinding binding;
   private final sg.settings.KeybindSetting backing;
   private BindSetting.BindMode mode = BindSetting.BindMode.HOLD;

   public BindSetting(String name, String description, InputBinding binding) {
      super(name, description);
      this.backing = null;
      this.binding = binding == null ? InputBinding.INSTANCE : binding;
   }

   public BindSetting(sg.settings.KeybindSetting backing) {
      super(backing.getName(), "");
      this.backing = backing;
      this.binding = InputBinding.keyboard(backing.getValue());
      this.setVisibility(() -> backing.getVisibilityCondition().getAsBoolean());
   }

   public BindSetting withMode(BindSetting.BindMode mode) {
      this.mode = mode;
      return this;
   }

   public BindSetting withKeyCode(int keyCode) {
      return this.setBinding(keyCode == -1 ? InputBinding.INSTANCE : InputBinding.keyboard(keyCode));
   }

   public BindSetting(String name, String description) {
      this(name, description, InputBinding.INSTANCE);
   }

   public String getBindingName() {
      return this.binding.getDisplayName();
   }

   public int getKeyCode() {
      return this.binding.getCode();
   }

   public InputBinding getBinding() {
      return this.binding;
   }

   public boolean isBound() {
      return this.binding.isBound();
   }

   public BindSetting setBinding(InputBinding binding) {
      this.binding = binding == null ? InputBinding.INSTANCE : binding;
      if (this.backing != null) {
         int keyCode = binding != null && binding.getType() == InputBinding.BindingType.KEYBOARD ? binding.getCode() : -1;
         this.backing.setValue(keyCode);
      }

      this.notifyChanged();
      return this;
   }

   public BindSetting.BindMode getMode() {
      return this.mode;
   }

   public enum BindMode {
      HOLD,
      TOGGLE;
   }
}
