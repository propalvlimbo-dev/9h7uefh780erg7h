package sg.modules.impl.misc;

import java.util.Set;
import lombok.Generated;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BeaconBlock;
import net.minecraft.world.level.block.BlastFurnaceBlock;
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CartographyTableBlock;
import net.minecraft.world.level.block.CopperChestBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.LoomBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SmokerBlock;
import net.minecraft.world.level.block.StonecutterBlock;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;

public class OpenWalls extends ClientModule {
   private final @com.google.gson.annotations.SerializedName("ш")  Set<Class<?>> interactableBlockClasses = Set.of(
      AbstractChestBlock.class,
      FurnaceBlock.class,
      CraftingTableBlock.class,
      CopperChestBlock.class,
      ShulkerBoxBlock.class,
      BaseEntityBlock.class,
      AnvilBlock.class,
      BeaconBlock.class,
      BlastFurnaceBlock.class,
      BrewingStandBlock.class,
      CampfireBlock.class,
      CartographyTableBlock.class,
      GrindstoneBlock.class,
      LecternBlock.class,
      LoomBlock.class,
      SmokerBlock.class,
      StonecutterBlock.class,
      LeverBlock.class
   );
   private static final String MODULE_NAME = "Open Walls";
   private static final String MODULE_DESCRIPTION = "Позволяет открывать сундуки через стены";

   public OpenWalls() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.MISC);
   }

   @Generated
   public Set<Class<?>> С() {
      return this.interactableBlockClasses;
   }
}
