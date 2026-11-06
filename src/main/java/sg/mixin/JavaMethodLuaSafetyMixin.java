package sg.mixin;

import java.lang.reflect.Method;
import java.util.Map;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sg.annotation.LuaExport;

@Mixin(targets = "org/luaj/vm2/lib/jse/JavaMethod", remap = false)
public class JavaMethodLuaSafetyMixin {
   @Shadow(remap = false)
   @Final
   private Method method;
   @Shadow(remap = false)
   @Final
   private static Map methods;
   private static final String В7_ = "getOrDefault";
   private static final String В7Ш = "put";
   private static final String В7ы = "HMI Lua safety layer loaded!";

   @Inject(method = "invokeMethod", at = @At("RETURN"), cancellable = true, remap = false)
   private void hmiIsSafe(Object var1, Varargs var2, CallbackInfoReturnable<LuaValue> var3) {
      if (!this.method.isAnnotationPresent(LuaExport.class) && !this.method.getName().equals(В7_) && !this.method.getName().equals(В7Ш)) {
         var3.setReturnValue(LuaValue.NIL);
      }
   }

   static {
      System.out.println(В7ы);
   }
}
