package sg.manager;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import sg.annotation.LuaExport;
import sg.render.ScriptedParticle;

public class HeldItemScriptContext {
   @LuaExport("matrices")
   public @com.google.gson.annotations.SerializedName("Х")  PoseStack matrices;
   @LuaExport("bl")
   public boolean Ял;
   @LuaExport("swingProgress")
   @SerializedName("4Ь")
   public float swingProgress;
   @LuaExport("item")
   public @com.google.gson.annotations.SerializedName("Ч")  ItemStack item;
   @LuaExport("player")
   public @com.google.gson.annotations.SerializedName("Я")  AbstractClientPlayer player;
   @LuaExport("hand")
   public @com.google.gson.annotations.SerializedName("А")  InteractionHand hand;
   @LuaExport("mainHand")
   public @com.google.gson.annotations.SerializedName("Яэ")  boolean mainHand;
   @LuaExport("deltaTime")
   @SerializedName("4衣")
   public float deltaTime;
   @LuaExport("equipProgress")
   @SerializedName("4ш")
   public float equipProgress;
   @LuaExport("mainHandSwingProgress")
   @SerializedName("4И")
   public float mainHandSwingProgress;
   @LuaExport("offHandSwingProgress")
   @SerializedName("4ь")
   public float offHandSwingProgress;
   @LuaExport("mainHandSwitchEvent")
   public @com.google.gson.annotations.SerializedName("Яю")  boolean mainHandSwitchEvent;
   @LuaExport("offHandSwitchEvent")
   public @com.google.gson.annotations.SerializedName("ЯЩ")  boolean offHandSwitchEvent;
   @LuaExport("swingMHand")
   public @com.google.gson.annotations.SerializedName("Яу")  boolean swingMainHand;
   @LuaExport("swingOHand")
   public @com.google.gson.annotations.SerializedName("Я2")  boolean swingOffHand;
   @LuaExport("interact")
   public @com.google.gson.annotations.SerializedName("Яф")  boolean interact;
   @LuaExport("blockBreaking")
   public @com.google.gson.annotations.SerializedName("Яи")  boolean blockBreaking;
   @LuaExport("particles")
   public @com.google.gson.annotations.SerializedName("с8")  List<ScriptedParticle> particles;

   public void х(
      PoseStack var1,
      boolean var2,
      float var3,
      ItemStack var4,
      AbstractClientPlayer var5,
      InteractionHand var6,
      boolean var7,
      float var8,
      float var9,
      float var10,
      float var11,
      boolean var12,
      boolean var13,
      boolean var14,
      boolean var15,
      boolean var16,
      boolean var17,
      List<ScriptedParticle> var18
   ) {
      this.matrices = var1;
      this.Ял = var2;
      this.swingProgress = var3;
      this.item = var4;
      this.player = var5;
      this.hand = var6;
      this.mainHand = var7;
      this.deltaTime = var8;
      this.equipProgress = var9;
      this.mainHandSwingProgress = var10;
      this.offHandSwingProgress = var11;
      this.mainHandSwitchEvent = var12;
      this.offHandSwitchEvent = var13;
      this.swingMainHand = var14;
      this.swingOffHand = var15;
      this.interact = var16;
      this.blockBreaking = var17;
      this.particles = var18;
   }
}
