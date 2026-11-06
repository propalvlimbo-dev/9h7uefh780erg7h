package sg.animation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import sg.manager.ItemQuadAnimationScript;
import sg.manager.HeldItemAnimationScript;
import sg.manager.HmiScriptRegistry;
import sg.mixinbridge.HmiScriptState;

public class LuaAnimationLoader implements SimpleSynchronousResourceReloadListener {
   private static final String сйТ = "holdmyitems";
   private static final String сйО = "lua_animation_loader";
   private static final String сйФ = "global\\.(\\w+)\\s*=\\s*([^;]+)\\s*;";
   private static final String сйЙ = "\\$\\{(\\w+)\\}";
   private static final String сйк = "respackopts";
   private static final String сйг = "\\r?\\n";
   private static final String сй0 = "0";
   private static final String сйх = "\n-- PERSIST VARIABLES\n";
   private static final String сйУ = "registry:put('";
   private static final String сйЗ = "',";
   private static final String сйЕ = ")\n";
   private static final String сйП = "holdmyitems/hand_pose.lua";
   private static final String сй西 = "holdmyitems/item_pose.lua";
   private static final String сйд = "holdmyitems/hand_relative_pose.lua";
   private static final String сйБ = "holdmyitems/item_model.lua";
   private static final String сйС = "holdmyitems/hand_addon.lua";
   private static final String сйз = "holdmyitems/hand_relative_addon.lua";
   private static final String сйц = "holdmyitems/item_addon.lua";
   private static final String сйя = "holdmyitems/item_model_addon.lua";
   private static final String сЖс = "minecraft";
   private static final String сЖВ = "minecraft";
   private static final String сЖЯ = "minecraft";

   private String preprocessScript(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         Pattern var2 = Pattern.compile(сйФ);
         Pattern var3 = Pattern.compile(сйЙ);
         HashSet<String> var4 = new HashSet<>();
         StringBuilder var5 = new StringBuilder();
         boolean var6 = FabricLoader.getInstance().isModLoaded(сйк);

         for (String var10 : var1.split(сйг)) {
            String var11 = var10;
            if (!var6) {
               Matcher var12 = var3.matcher(var11);
               StringBuffer var13 = new StringBuffer();

               while (var12.find()) {
                  var12.appendReplacement(var13, сй0);
               }

               var12.appendTail(var13);
               var11 = var13.toString();
            }

            Matcher var19 = var2.matcher(var11);
            StringBuffer var20 = new StringBuffer();

            boolean var14;
            for (var14 = false; var19.find(); var14 = true) {
               String var15 = var19.group(1);
               String var16 = var19.group(2).trim();
               var4.add(var15);
               var19.appendReplacement(var20, "local " + var15 + " = registry:getOrDefault('" + var15 + "', " + var16 + ")");
            }

            var19.appendTail(var20);
            var5.append(var14 ? var20.toString() : var11).append('\n');
         }

         if (!var4.isEmpty()) {
            var5.append(сйх);

            for (String var18 : var4) {
               var5.append(сйУ).append(var18).append(сйЗ).append(var18).append(сйЕ);
            }
         }

         return var5.toString();
      } else {
         return var1;
      }
   }

   private void loadSingle(ResourceManager var1, String var2, Consumer<String> var3) {
      Identifier var4 = Identifier.fromNamespaceAndPath(сЖс, var2);

      try {
         Resource var5 = var1.getResource(var4).orElse(null);
         if (var5 == null) {
            var3.accept("");
            return;
         }

         try (InputStream var6 = var5.open()) {
            var3.accept(this.preprocessScript(new String(var6.readAllBytes(), StandardCharsets.UTF_8)));
         }
      } catch (Exception var11) {
         var3.accept("");
         var11.printStackTrace();
      }
   }

   @Override
   public void onResourceManagerReload(ResourceManager var1) {
      HmiScriptState.Я_e12d7b95.clear();
      HmiScriptState.Х_e12d7b95.clear();
      HmiScriptState.Д.clear();
      HmiScriptState.field_4.clear();
      HmiScriptState.е.clear();
      this.loadSingle(var1, сйП, var0 -> {
         try {
            HmiScriptRegistry.В_464ec6e0 = new HeldItemAnimationScript(var0);
         } catch (IOException var2) {
            throw new RuntimeException(var2);
         }
      });
      this.loadSingle(var1, сй西, var0 -> {
         try {
            HmiScriptRegistry.Х = new HeldItemAnimationScript(var0);
         } catch (IOException var2) {
            throw new RuntimeException(var2);
         }
      });
      this.loadSingle(var1, сйд, var0 -> {
         try {
            HmiScriptRegistry.Я = new HeldItemAnimationScript(var0);
         } catch (IOException var2) {
            throw new RuntimeException(var2);
         }
      });
      this.loadSingle(var1, сйБ, var0 -> {
         try {
            HmiScriptRegistry.В_673b7c17 = new ItemQuadAnimationScript(var0);
         } catch (IOException var2) {
            throw new RuntimeException(var2);
         }
      });
      HmiScriptRegistry.Ъ = this.loadMultiple(var1, сйС);
      HmiScriptRegistry.т = this.loadMultiple(var1, сйз);
      HmiScriptRegistry.а = this.loadMultiple(var1, сйц);
      HmiScriptRegistry.Ю = this.loadMultipleModel(var1, сйя);
   }

   @Override
   public Identifier getFabricId() {
      return Identifier.fromNamespaceAndPath(сйТ, сйО);
   }

   private ArrayList<HeldItemAnimationScript> loadMultiple(ResourceManager var1, String var2) {
      Identifier var3 = Identifier.fromNamespaceAndPath(сЖВ, var2);
      ArrayList var4 = new ArrayList();

      try {
         for (Resource var7 : var1.getResourceStack(var3)) {
            try (InputStream var8 = var7.open()) {
               String var9 = this.preprocessScript(new String(var8.readAllBytes(), StandardCharsets.UTF_8));
               var4.add(new HeldItemAnimationScript(var9));
            }
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

      return var4;
   }

   private ArrayList<ItemQuadAnimationScript> loadMultipleModel(ResourceManager var1, String var2) {
      Identifier var3 = Identifier.fromNamespaceAndPath(сЖЯ, var2);
      ArrayList var4 = new ArrayList();

      try {
         for (Resource var7 : var1.getResourceStack(var3)) {
            try (InputStream var8 = var7.open()) {
               String var9 = this.preprocessScript(new String(var8.readAllBytes(), StandardCharsets.UTF_8));
               var4.add(new ItemQuadAnimationScript(var9));
            }
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

      return var4;
   }
}
