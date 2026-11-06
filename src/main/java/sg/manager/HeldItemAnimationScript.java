package sg.manager;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.SystemToast.SystemToastId;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import settings.render.LuaMapUserdata;
import sg.ec.LuaIdentifiers;
import sg.ec.LuaRuntime;
import sg.mixinbridge.HmiScriptState;
import sg.render.ScriptParticleFactory;
import sg.render.ScriptedParticle;
import sg.render.LuaTransformMath;

public class HeldItemAnimationScript {
   public static int Аю = 9;
   private final Globals Я;
   private final LuaValue Х;
   private boolean ЯР = true;
   private static final HeldItemScriptContext В_78cbea20 = new HeldItemScriptContext();
   @SerializedName("В")
   private final LuaTransformMath В_266aed50 = new LuaTransformMath();
   @SerializedName("В")
   private final LuaItemAccess В_d8915667 = new LuaItemAccess();
   @SerializedName("В")
   private final LuaIdentifiers В_9d495b15 = new LuaIdentifiers();
   @SerializedName("В")
   private final LuaItemRegistry В_cd7a8a78 = new LuaItemRegistry();
   @SerializedName("В")
   private final LuaItemTags В_5b6925d7 = new LuaItemTags();
   @SerializedName("В")
   private final LuaPlayerAccess В_149b370f = new LuaPlayerAccess();
   @SerializedName("В")
   private final LuaEasings В_1b66675e = new LuaEasings();
   @SerializedName("В")
   private final LuaKeyInput В_60af3e75 = new LuaKeyInput();
   @SerializedName("В")
   private final LuaSoundAccess В_5157f7c1 = new LuaSoundAccess();
   @SerializedName("В")
   private final LuaCameraAccess В_a814719c = new LuaCameraAccess();
   @SerializedName("В")
   private final ScriptParticleFactory В_9f39b551 = new ScriptParticleFactory();
   private static final String ВИЖ = "M";
   private static final String ВИм = "I";
   private static final String ВИК = "Texture";
   private static final String ВИI = "Items";
   private static final String ВИТ = "Tags";
   private static final String ВИО = "P";
   private static final String ВИФ = "Easings";
   private static final String ВИЙ = "KeyBindManager";
   private static final String ВИк = "S";
   private static final String ВИг = "C";
   private static final String ВИ0 = "particleManager";
   private static final String ВИх = "swingSpeed";
   private static final String ВИУ = "registry";
   private static final String ВИЗ = "renderAsBlock";
   private static final String ВИЕ = "translateItem";
   private static final String ВИП = "itemSwingSpeed";
   private static final String ВИ西 = "animator";
   private static final String ВИд = "renderAsBlock";
   private static final String ВИБ = "translateItem";
   private static final String ВИС = "itemSwingSpeed";
   private static final String ВИз = "useDuration";
   private static final String ВИц = "usingItem";
   private static final String ВИя = "debugger";
   private static final String Вьс = "applyBlockRotation";
   private static final String ВьВ = "context";
   private static final String ВьЯ = "HMI Lua Runtime error!";

   public void ф(
      PoseStack var1,
      boolean var2,
      HashMap<String, Object> var3,
      float var4,
      ItemStack var5,
      AbstractClientPlayer var6,
      InteractionHand var7,
      boolean var8,
      float var9,
      float var10,
      float var11,
      float var12,
      boolean var13,
      boolean var14,
      boolean var15,
      boolean var16,
      boolean var17,
      boolean var18,
      List<ScriptedParticle> var19
   ) {
      if (this.ЯР) {
         В_78cbea20.х(var1, var2, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19);

         try {
            this.Х.invoke();
         } catch (Exception var21) {
            System.err.println("HMI Lua Runtime error! " + var21.getMessage());
            SystemToast.addOrUpdate(
               Minecraft.getInstance().gui.toastManager(), SystemToastId.PACK_LOAD_FAILURE, Component.literal(ВьЯ), Component.literal(var21.getMessage())
            );
            this.ЯР = false;
         }
      }
   }

   public HeldItemAnimationScript(String var1) throws IOException {
      this.Я = LuaRuntime.Д_166b3054().В_b22603a6;
      this.Я.set(ВИЖ, LuaApiUserdata.of(this.В_266aed50));
      this.Я.set(ВИм, LuaApiUserdata.of(this.В_d8915667));
      this.Я.set(ВИК, LuaApiUserdata.of(this.В_9d495b15));
      this.Я.set(ВИI, LuaApiUserdata.of(this.В_cd7a8a78));
      this.Я.set(ВИТ, LuaApiUserdata.of(this.В_5b6925d7));
      this.Я.set(ВИО, LuaApiUserdata.of(this.В_149b370f));
      this.Я.set(ВИФ, LuaApiUserdata.of(this.В_1b66675e));
      this.Я.set(ВИЙ, LuaApiUserdata.of(this.В_60af3e75));
      this.Я.set(ВИк, LuaApiUserdata.of(this.В_5157f7c1));
      this.Я.set(ВИг, LuaApiUserdata.of(this.В_a814719c));
      this.Я.set(ВИ0, LuaApiUserdata.of(this.В_9f39b551));
      this.Я.set(ВИх, LuaValue.valueOf(Аю));
      this.Я.set(ВИУ, LuaMapUserdata.of(HmiScriptState.Д));
      this.Я.set(ВИЗ, LuaMapUserdata.of(HmiScriptState.Я_e12d7b95));
      this.Я.set(ВИЕ, LuaMapUserdata.of(HmiScriptState.Х_e12d7b95));
      this.Я.set(ВИП, LuaMapUserdata.of(HmiScriptState.А));
      this.Я.set(ВИ西, CoerceJavaToLua.coerce(HmiScriptState.В_95877909));
      this.Я.set(ВИд, LuaMapUserdata.of(HmiScriptState.Я_e12d7b95));
      this.Я.set(ВИБ, LuaMapUserdata.of(HmiScriptState.Х_e12d7b95));
      this.Я.set(ВИС, LuaMapUserdata.of(HmiScriptState.А));
      this.Я.set(ВИз, LuaMapUserdata.of(HmiScriptState.field_4));
      this.Я.set(ВИц, LuaMapUserdata.of(HmiScriptState.п));
      this.Я.set(ВИя, CoerceJavaToLua.coerce(HmiScriptState.В_700015e0));
      this.Я.set(Вьс, LuaMapUserdata.of(HmiScriptState.е));
      this.Я.set(ВьВ, CoerceJavaToLua.coerce(В_78cbea20));
      this.Х = this.Я.load(var1);
   }
}
