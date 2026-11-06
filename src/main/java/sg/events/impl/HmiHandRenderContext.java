package sg.events.impl;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import sg.annotation.LuaExport;

public class HmiHandRenderContext {
   @LuaExport("bl")
   public @com.google.gson.annotations.SerializedName("сб")  boolean rightHand;
   @LuaExport("swingProgress")
   public @com.google.gson.annotations.SerializedName("В西")  float swingProgress;
   @LuaExport("item")
   public @com.google.gson.annotations.SerializedName("А")  ItemStack item;
   @LuaExport("player")
   public @com.google.gson.annotations.SerializedName("В")  AbstractClientPlayer player;
   @LuaExport("hand")
   public @com.google.gson.annotations.SerializedName("Я")  InteractionHand hand;
   @LuaExport("mainHand")
   public @com.google.gson.annotations.SerializedName("сЫ")  boolean mainHand;
   @LuaExport("deltaTime")
   public @com.google.gson.annotations.SerializedName("Вд")  float deltaTime;
   @LuaExport("equipProgress")
   public @com.google.gson.annotations.SerializedName("ВБ")  float equipProgress;
   @LuaExport("mainHandSwingProgress")
   public @com.google.gson.annotations.SerializedName("ВС")  float mainHandSwingProgress;
   @LuaExport("offHandSwingProgress")
   public @com.google.gson.annotations.SerializedName("Вз")  float offHandSwingProgress;
   @LuaExport("mainHandSwitchEvent")
   public @com.google.gson.annotations.SerializedName("сl")  boolean mainHandItemChanged;
   @LuaExport("offHandSwitchEvent")
   public @com.google.gson.annotations.SerializedName("с8")  boolean offHandItemChanged;
   @LuaExport("swingMHand")
   public @com.google.gson.annotations.SerializedName("с5")  boolean mainHandSwingEvent;
   @LuaExport("swingOHand")
   public @com.google.gson.annotations.SerializedName("св")  boolean offHandSwingEvent;
   @LuaExport("interact")
   public @com.google.gson.annotations.SerializedName("съ")  boolean interactionSwing;
   @LuaExport("blockBreaking")
   public @com.google.gson.annotations.SerializedName("сМ")  boolean blockBreaking;

   public void Ж(HmiHandRenderContext source) {
      this.rightHand = source.rightHand;
      this.swingProgress = source.swingProgress;
      this.player = source.player;
      this.hand = source.hand;
      this.mainHand = source.mainHand;
      this.deltaTime = source.deltaTime;
      this.equipProgress = source.equipProgress;
      this.mainHandSwingProgress = source.mainHandSwingProgress;
      this.offHandSwingProgress = source.offHandSwingProgress;
      this.mainHandItemChanged = source.mainHandItemChanged;
      this.offHandItemChanged = source.offHandItemChanged;
      this.mainHandSwingEvent = source.mainHandSwingEvent;
      this.offHandSwingEvent = source.offHandSwingEvent;
      this.interactionSwing = source.interactionSwing;
      this.blockBreaking = source.blockBreaking;
      this.item = source.item;
   }

   public HmiHandRenderContext(
      boolean rightHand,
      float swingProgress,
      AbstractClientPlayer player,
      InteractionHand hand,
      boolean mainHand,
      float deltaTime,
      float equipProgress,
      float mainHandSwingProgress,
      float offHandSwingProgress,
      boolean mainHandItemChanged,
      boolean offHandItemChanged,
      boolean mainHandSwingEvent,
      boolean offHandSwingEvent,
      boolean interactionSwing,
      boolean blockBreaking,
      ItemStack item
   ) {
      this.rightHand = rightHand;
      this.swingProgress = swingProgress;
      this.player = player;
      this.hand = hand;
      this.mainHand = mainHand;
      this.deltaTime = deltaTime;
      this.equipProgress = equipProgress;
      this.mainHandSwingProgress = mainHandSwingProgress;
      this.offHandSwingProgress = offHandSwingProgress;
      this.mainHandItemChanged = mainHandItemChanged;
      this.offHandItemChanged = offHandItemChanged;
      this.mainHandSwingEvent = mainHandSwingEvent;
      this.offHandSwingEvent = offHandSwingEvent;
      this.interactionSwing = interactionSwing;
      this.blockBreaking = blockBreaking;
      this.item = item;
   }
}
