package sg.modules.impl.render;

import sg.client.RoseClient;
import sg.modules.ModuleCategory;
import sg.modules.ClientModule;

public class ItemPhysics extends ClientModule {
   private static final String рэ = "ItemPhysics";
   private static final String рю = "Реалистичная физика предметов на земле";

   public ItemPhysics() {
      super(рэ, рю, ModuleCategory.RENDER);
   }

   public static boolean пК() {
      ItemPhysics var0 = RoseClient.getInstance().getServices().getModules().getItemPhysics();
      return var0 != null && var0.isEnabled();
   }
}
