package sg.ec;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import sg.render.ScriptedParticle;

public class HandParticleRenderer {
   public static void ю(
      ArrayList<ScriptedParticle> var0,
      PoseStack var1,
      SubmitNodeCollector var2,
      String var3,
      InteractionHand var4,
      int var5,
      AbstractClientPlayer var6,
      float var7
   ) {
      Iterator var8 = var0.iterator();

      while (var8.hasNext()) {
         ScriptedParticle var9 = (ScriptedParticle)var8.next();
         if (var9.К_bbeebd87) {
            var8.remove();
         } else if (var9.й_388008bb == var3 && var9.В_8332a9ec == var4) {
            if (var9.В_e9a76781 != null) {
               var9.method_8(var9.В_e9a76781, var2, var5, var6, var7);
            } else {
               var9.method_8(var1, var2, var5, var6, var7);
            }

            var9.method_8А();
         }
      }
   }
}
