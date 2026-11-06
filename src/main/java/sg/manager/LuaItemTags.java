package sg.manager;

import net.fabricmc.fabric.impl.tag.convention.v2.TagRegistration;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import sg.annotation.LuaExport;

public class LuaItemTags {
   @LuaExport("getVanillaTag")
   public TagKey<Item> getVanillaTag(String tagId) {
      return TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace(tagId));
   }

   @LuaExport("getFabricTag")
   public TagKey<Item> getFabricTag(String tagId) {
      return TagRegistration.ITEM_TAG.registerC(tagId);
   }
}
