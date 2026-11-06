package sg.mixinbridge;

import java.util.concurrent.atomic.AtomicInteger;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sg.animation.LuaAnimationLoader;

public class HmiClientInitializer implements net.fabricmc.api.ModInitializer {
   public static final String Хб = "holdmyitems";
   public static Matrix4f field__ = new Matrix4f();
   public static Matrix4f Ш = new Matrix4f();
   public static float ЮР = 0.0F;
   public static final Logger Ъ = LoggerFactory.getLogger(HmiClientInitializer.ВШТ);
   public static Minecraft Д = Minecraft.getInstance();
   public static float Ю3 = 0.0F;
   public static float Юр = 0.0F;
   private static final String ВШ_ = "example";
   private static final String ВШШ = "hud";
   private static final String ВШы = "holdmyitems";
   private static final String ВШй = "pack_test";
   private static final String ВШЖ = "rose";
   private static final String ВШм = "What you staring at?";
   private static final String ВШК = "Example Pack!";
   private static final String ВШI = "Registered embedded resource pack: {}";
   private static final String ВШТ = "holdmyitems";

   @Override
   public void onInitialize() {
      ClassLoader var1 = this.getClass().getClassLoader();
      System.out.println("HMI ClassLoader: " + var1);
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(ВШ_, ВШШ), (var0, var1x) -> {
         AtomicInteger var2x = new AtomicInteger(10);
         HmiScriptState.В_700015e0.с().forEach(var2xx -> {
            var0.text(Minecraft.getInstance().font, var2xx, 10, var2x.get(), -1);
            var2x.addAndGet(10);
         });
         HmiScriptState.В_700015e0.必弟();
      });
      ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new LuaAnimationLoader());
      Identifier var2 = Identifier.fromNamespaceAndPath(ВШы, ВШй);
      FabricLoader.getInstance().getModContainer(ВШЖ).ifPresent(var1x -> {
         if (ResourceLoader.registerBuiltinPack(var2, var1x, Component.literal(ВШК), PackActivationType.NORMAL)) {
            Ъ.info(ВШI, var2);
         } else {
            Ъ.warn("Embedded resource pack {} is absent from the client resources", var2);
         }
      });
      Ъ.info(ВШм);
   }
}
