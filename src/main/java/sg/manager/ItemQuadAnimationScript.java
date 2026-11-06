package sg.manager;

import com.google.gson.annotations.SerializedName;
import java.io.IOException;
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
import sg.ec.LuaRuntime;
import sg.events.impl.HmiHandRenderContext;
import sg.mixinbridge.HmiClientInitializer;
import sg.mixinbridge.HmiScriptState;
import sg.mixinbridge.BakedQuadAnimator;
import sg.render.LuaTransformMath;

public class ItemQuadAnimationScript {
   private static HmiHandRenderContext В = new HmiHandRenderContext(
      false,
      0.0F,
      Minecraft.getInstance().player,
      InteractionHand.MAIN_HAND,
      false,
      HmiClientInitializer.Юр,
      0.0F,
      0.0F,
      0.0F,
      false,
      false,
      false,
      false,
      false,
      false,
      ItemStack.EMPTY
   );
   @SerializedName("Х")
   private final Globals Х_b22603a6;
   private final LuaValue Ъ;
   private boolean Аг = true;
   @SerializedName("Я")
   private final LuaTransformMath Я_266aed50 = new LuaTransformMath();
   @SerializedName("Я")
   private final LuaItemAccess Я_d8915667 = new LuaItemAccess();
   @SerializedName("Х")
   private final LuaItemRegistry Х_cd7a8a78 = new LuaItemRegistry();
   @SerializedName("Я")
   private final LuaItemTags Я_5b6925d7 = new LuaItemTags();
   @SerializedName("Я")
   private final LuaPlayerAccess Я_149b370f = new LuaPlayerAccess();
   @SerializedName("Я")
   private final LuaEasings Я_1b66675e = new LuaEasings();
   @SerializedName("Я")
   private final LuaKeyInput Я_60af3e75 = new LuaKeyInput();
   private static final String юы = "M";
   private static final String юй = "I";
   private static final String юЖ = "Items";
   private static final String юм = "Tags";
   private static final String юК = "P";
   private static final String юI = "Easings";
   private static final String юТ = "KeyBindManager";
   private static final String юО = "registry";
   private static final String юФ = "animator";
   private static final String юЙ = "debugger";
   private static final String юк = "data";
   private static final String юг = "HMI Lua Runtime error!";

   public ItemQuadAnimationScript(String var1) throws IOException {
      this.Х_b22603a6 = LuaRuntime.Д_166b3054().В_b22603a6;
      this.Х_b22603a6.set(юы, LuaApiUserdata.of(this.Я_266aed50));
      this.Х_b22603a6.set(юй, LuaApiUserdata.of(this.Я_d8915667));
      this.Х_b22603a6.set(юЖ, LuaApiUserdata.of(this.Х_cd7a8a78));
      this.Х_b22603a6.set(юм, LuaApiUserdata.of(this.Я_5b6925d7));
      this.Х_b22603a6.set(юК, LuaApiUserdata.of(this.Я_149b370f));
      this.Х_b22603a6.set(юI, LuaApiUserdata.of(this.Я_1b66675e));
      this.Х_b22603a6.set(юТ, LuaApiUserdata.of(this.Я_60af3e75));
      this.Х_b22603a6.set(юО, LuaMapUserdata.of(HmiScriptState.Д));
      this.Х_b22603a6.set(юФ, CoerceJavaToLua.coerce(HmiScriptState.В_95877909));
      this.Х_b22603a6.set(юЙ, CoerceJavaToLua.coerce(HmiScriptState.В_700015e0));
      this.Х_b22603a6.set(юк, CoerceJavaToLua.coerce(В));
      this.Ъ = this.Х_b22603a6.load(var1);
   }

   public void с(HmiHandRenderContext var1, ItemStack var2, AbstractClientPlayer var3, BakedQuadAnimator var4) {
      if (this.Аг) {
         В.Ж(var1);

         try {
            this.Ъ.call();
         } catch (Exception var6) {
            System.err.println("HMI Lua Runtime error! " + var6.getMessage());
            SystemToast.addOrUpdate(
               Minecraft.getInstance().gui.toastManager(), SystemToastId.PACK_LOAD_FAILURE, Component.literal(юг), Component.literal(var6.getMessage())
            );
            this.Аг = false;
         }
      }
   }
}
