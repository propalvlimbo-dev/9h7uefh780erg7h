package settings.render;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;
import sg.annotation.RequiresMods;
import sg.ec.ViaFabricPlusDefaults;

public final class ConditionalMixinPlugin implements IMixinConfigPlugin {
   private static final String Я必O = "!";

   @Override
   public void preApply(String var1, ClassNode var2, String var3, IMixinInfo var4) {
   }

   @Override
   public List<String> getMixins() {
      return null;
   }

   @Override
   public boolean shouldApplyMixin(String var1, String var2) {
      try {
         ClassNode var3 = MixinService.getService().getBytecodeProvider().getClassNode(var2, false);
         return matchesMods(var3, FabricLoader.getInstance()::isModLoaded);
      } catch (IOException | ClassNotFoundException var4) {
         throw new IllegalStateException("Cannot read mixin conditions: " + var2, var4);
      }
   }

   @Override
   public String getRefMapperConfig() {
      return null;
   }

   @Override
   public void acceptTargets(Set<String> var1, Set<String> var2) {
   }

   @Override
   public void onLoad(String var1) {
      ViaFabricPlusDefaults.ЖП();
   }

   public static boolean matchesMods(ClassNode var0, Predicate<String> var1) {
      if (var0.visibleAnnotations == null) {
         return true;
      }

      Iterator var2 = var0.visibleAnnotations.iterator();

      while (true) {
         if (!var2.hasNext()) {
            return true;
         }

         AnnotationNode var3 = (AnnotationNode)var2.next();
         if (Type.getDescriptor(RequiresMods.class).equals(var3.desc)) {
            if (var3.values == null || var3.values.size() != 2) {
               break;
            }

            Object var5 = var3.values.get(1);
            if (!(var5 instanceof List)) {
               break;
            }

            for (Object var6 : (List)var5) {
               String var7 = (String)var6;
               boolean var8 = var7.startsWith(Я必O);
               if (var1.test(var8 ? var7.substring(1) : var7) == var8) {
                  return false;
               }
            }
         }
      }

      throw new IllegalStateException("Invalid mixin conditions: " + var0.name);
   }

   @Override
   public void postApply(String var1, ClassNode var2, String var3, IMixinInfo var4) {
   }
}
