package sg.mixin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sg.annotation.LuaExport;

@Mixin(targets = "org/luaj/vm2/lib/jse/JavaClass", remap = false)
public class JavaClassLuaMemberNamesMixin {
   @Redirect(method = "getMethod", at = @At(value = "INVOKE", target = "Ljava/lang/reflect/Method;getName()Ljava/lang/String;"), remap = false)
   private String hmiStableMethodName(Method var1) {
      LuaExport var2 = var1.getAnnotation(LuaExport.class);
      return var2 != null && !var2.value().isEmpty() ? var2.value() : var1.getName();
   }

   @Redirect(method = "getField", at = @At(value = "INVOKE", target = "Ljava/lang/reflect/Field;getName()Ljava/lang/String;"), remap = false)
   private String hmiStableFieldName(Field var1) {
      LuaExport var2 = var1.getAnnotation(LuaExport.class);
      return var2 != null && !var2.value().isEmpty() ? var2.value() : var1.getName();
   }
}
