package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.render.Theme;
import dev.rose.setting.BoolSetting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;


public final class BlockInfoModule extends HudElement {
   private final BoolSetting tool = this.add(new BoolSetting("tool", true));

   public BlockInfoModule() {
      super("block_info", false, Anchor.TOP, 0.0F, 6.0F);
   }

   @Override
   public int icon() {
      return 58679;
   }

   private BlockInfoModule.Info info(HudContext hud) {
      if (hud.preview) {
         return new BlockInfoModule.Info(
            new ItemStack(Items.DEEPSLATE_DIAMOND_ORE),
            I18n.get("block.minecraft.deepslate_diamond_ore", new Object[0]),
            58822,
            I18n.get("rose.hud.block_info.pickaxe", new Object[0]),
            true
         );
      }

      if (hud.mc.level != null && hud.mc.player != null && hud.mc.hitResult instanceof BlockHitResult hit && hit.getType() == HitResult.Type.BLOCK) {
         BlockPos pos = hit.getBlockPos();
         BlockState state = hud.mc.level.getBlockState(pos);
         if (state.isAir()) {
            return null;
         }

         ItemStack icon = state.getCloneItemStack(hud.mc.level, pos, false);
         int toolIcon = 0;
         String toolName = "";
         if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            toolIcon = 58822;
            toolName = I18n.get("rose.hud.block_info.pickaxe", new Object[0]);
         } else if (state.is(BlockTags.MINEABLE_WITH_AXE)) {
            toolIcon = 57424;
            toolName = I18n.get("rose.hud.block_info.axe", new Object[0]);
         } else if (state.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
            toolIcon = 57693;
            toolName = I18n.get("rose.hud.block_info.shovel", new Object[0]);
         } else if (state.is(BlockTags.MINEABLE_WITH_HOE)) {
            toolIcon = 57835;
            toolName = I18n.get("rose.hud.block_info.hoe", new Object[0]);
         }

         boolean correct = !state.requiresCorrectToolForDrops() || hud.mc.player.getMainHandItem().isCorrectToolForDrops(state);
         return new BlockInfoModule.Info(icon, state.getBlock().getName().getString(), toolIcon, toolName, correct);
      } else {
         return null;
      }
   }

   @Override
   protected boolean hasContent(HudContext hud) {
      return this.info(hud) != null;
   }

   @Override
   protected float defaultRoundness() {
      return 0.6F;
   }

   @Override
   protected Size measure(HudContext hud) {
      BlockInfoModule.Info info = this.info(hud);
      if (info == null) {
         return new Size(60.0F, 22.0F);
      }

      float text = hud.semibold.width(info.name(), 7.4F);
      if (this.tool.isOn() && !info.toolName().isEmpty()) {
         text = Math.max(text, 9.0F + hud.regular.width(info.toolName(), 6.2F));
      }

      boolean twoLines = this.tool.isOn() && !info.toolName().isEmpty();
      return new Size(29.0F + text + 9.0F, twoLines ? 28.0F : 22.0F);
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      BlockInfoModule.Info info = this.info(hud);
      if (info != null) {
         hud.item(info.icon(), 7.0F, (h - 16.0F) / 2.0F, 16.0F);
         float textX = 29.0F;
         boolean twoLines = this.tool.isOn() && !info.toolName().isEmpty();
         hud.text(hud.semibold, info.name(), textX, twoLines ? 5.5F : HudContext.centered(0.0F, h, 7.4F), 7.4F, hud.label);
         if (twoLines) {
            int color = info.correct() ? Theme.positive : Theme.danger;
            hud.icon(info.toolIcon(), textX + 3.5F, 19.5F, 7.0F, color);
            hud.text(hud.regular, info.toolName(), textX + 9.0F, 16.0F, 6.2F, hud.secondary);
         }
      }
   }

   private record Info(ItemStack icon, String name, int toolIcon, String toolName, boolean correct) {
   }
}
