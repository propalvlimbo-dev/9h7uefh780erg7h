package sg.render;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import sg.annotation.LuaExport;
import sg.ec.LuaParticleCallback;

public class ScriptParticleFactory {
   private static final String сТф = "KEYFRAME";

   @LuaExport("addParticle")
   public void addParticle(
      ArrayList<ScriptedParticle> particles,
      boolean var2,
      double var3,
      double var5,
      double var7,
      double var9,
      double var11,
      double var13,
      double var15,
      double var17,
      double var19,
      double var21,
      double var23,
      double var25,
      double var27,
      Identifier var29,
      String var30,
      InteractionHand var31,
      String var32,
      String var33,
      double var34,
      double var36
   ) {
      var33 = var33.intern();
      var32 = var32.intern();
      var30 = var30.intern();
      particles.add(
         new ScriptedParticle(
            var2, var3, var5, var7, var9, var11, var13, var15, var17, var19, var21, var23, var25, var27, var29, var30, var31, var32, var33, var34, var36
         )
      );
   }

   @LuaExport("addParticle")
   public void addParticle(
      ArrayList<ScriptedParticle> particles,
      boolean var2,
      double var3,
      double var5,
      double var7,
      double var9,
      double var11,
      double var13,
      double var15,
      double var17,
      double var19,
      double var21,
      double var23,
      double var25,
      double var27,
      Identifier var29,
      List<Identifier> var30,
      String var31,
      InteractionHand var32,
      String var33,
      String var34,
      double var35,
      double var37,
      LuaValue var39
   ) {
      var34 = var34.intern();
      var33 = var33.intern();
      var31 = var31.intern();
      LuaParticleCallback var40 = null;
      if (var39.isfunction()) {
         var40 = new LuaParticleCallback((LuaFunction)var39);
      }

      if (particles.stream().noneMatch(var1x -> var1x.ы_388008bb == сТф && var1x.В_8332a9ec == var32)) {
         particles.add(
            new ScriptedParticle(
               var2,
               var3,
               var5,
               var7,
               var9,
               var11,
               var13,
               var15,
               var17,
               var19,
               var21,
               var23,
               var25,
               var27,
               var29,
               var30,
               var31,
               var32,
               var33,
               var34,
               var35,
               var37,
               var40
            )
         );
      }
   }

   @LuaExport("addParticle")
   public void addParticle(
      ArrayList<ScriptedParticle> particles,
      boolean var2,
      double var3,
      double var5,
      double var7,
      double var9,
      double var11,
      double var13,
      double var15,
      double var17,
      double var19,
      double var21,
      double var23,
      double var25,
      double var27,
      Identifier var29,
      String var30,
      InteractionHand var31,
      String var32,
      String var33,
      double var34,
      double var36,
      LuaValue var38
   ) {
      var33 = var33.intern();
      var32 = var32.intern();
      var30 = var30.intern();
      LuaParticleCallback var39 = null;
      if (var38.isfunction()) {
         var39 = new LuaParticleCallback((LuaFunction)var38);
      }

      particles.add(
         new ScriptedParticle(
            var2, var3, var5, var7, var9, var11, var13, var15, var17, var19, var21, var23, var25, var27, var29, var30, var31, var32, var33, var34, var36, var39
         )
      );
   }

   @LuaExport("addParticle")
   public void addParticle(
      ArrayList<ScriptedParticle> particles,
      boolean var2,
      double var3,
      double var5,
      double var7,
      double var9,
      double var11,
      double var13,
      double var15,
      double var17,
      double var19,
      double var21,
      double var23,
      double var25,
      double var27,
      Identifier var29,
      String var30,
      InteractionHand var31,
      String var32,
      String var33,
      double var34,
      double var36,
      LuaValue var38,
      PoseStack var39
   ) {
      var33 = var33.intern();
      var32 = var32.intern();
      var30 = var30.intern();
      LuaParticleCallback var40 = null;
      if (var38.isfunction()) {
         var40 = new LuaParticleCallback((LuaFunction)var38);
      }

      particles.add(
         new ScriptedParticle(
            var2,
            var3,
            var5,
            var7,
            var9,
            var11,
            var13,
            var15,
            var17,
            var19,
            var21,
            var23,
            var25,
            var27,
            var29,
            var30,
            var31,
            var32,
            var33,
            var34,
            var36,
            var40,
            var39
         )
      );
   }
}
