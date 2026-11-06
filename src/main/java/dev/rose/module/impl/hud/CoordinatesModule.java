package dev.rose.module.impl.hud;

import dev.rose.hud.Anchor;
import dev.rose.hud.HudContext;
import dev.rose.hud.HudElement;
import dev.rose.hud.Size;
import dev.rose.setting.BoolSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;


public final class CoordinatesModule extends HudElement {
   private static final float ROW = 12.0F;
   private static final float PAD = 7.0F;
   private static final float TEXT = 6.8F;
   private final BoolSetting direction = this.add(new BoolSetting("direction", true));
   private final BoolSetting biome = this.add(new BoolSetting("biome", true));
   private final BoolSetting nether = this.add(new BoolSetting("nether", true));
   private final BoolSetting decimals = this.add(new BoolSetting("decimals", false));
   private List<CoordinatesModule.Row> rows = List.of();

   public CoordinatesModule() {
      super("coordinates", false, Anchor.TOP_LEFT, 6.0F, 110.0F);
   }

   @Override
   public int icon() {
      return 57617;
   }

   private static boolean hiddenByServer(Minecraft mc) {
      return mc.showOnlyReducedInfo();
   }

   @Override
   public void onTick() {
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player != null && mc.level != null && !hiddenByServer(mc)) {
         List<CoordinatesModule.Row> next = new ArrayList<>();
         next.add(new CoordinatesModule.Row(57617, this.position(player.getX(), player.getY(), player.getZ()), 0.0F));
         if (this.direction.isOn()) {
            Direction facing = player.getDirection();

            String axis = switch (facing) {
               case NORTH -> "−Z";
               case SOUTH -> "+Z";
               case WEST -> "−X";
               default -> "+X";
            };
            next.add(
               new CoordinatesModule.Row(
                  57636, I18n.get("rose.hud.direction." + facing.getName(), new Object[0]) + "  " + axis, player.getYRot() + 180.0F
               )
            );
         }

         if (this.biome.isOn()) {
            BlockPos pos = player.blockPosition();
            String name = mc.level
               .getBiome(pos)
               .unwrapKey()
               .map(key -> I18n.get("biome." + key.identifier().getNamespace() + "." + key.identifier().getPath(), new Object[0]))
               .orElse("?");
            next.add(new CoordinatesModule.Row(58100, name, 0.0F));
         }

         if (this.nether.isOn()) {
            if (mc.level.dimension() == Level.NETHER) {
               next.add(
                  new CoordinatesModule.Row(
                     57576,
                     I18n.get("rose.hud.coordinates.overworld", new Object[0])
                        + "  "
                        + blocks(player.getX() * 8.0)
                        + ", "
                        + blocks(player.getZ() * 8.0),
                     0.0F
                  )
               );
            } else if (mc.level.dimension() == Level.OVERWORLD) {
               next.add(
                  new CoordinatesModule.Row(
                     57554,
                     I18n.get("rose.hud.coordinates.nether", new Object[0])
                        + "  "
                        + blocks(player.getX() / 8.0)
                        + ", "
                        + blocks(player.getZ() / 8.0),
                     0.0F
                  )
               );
            }
         }

         this.rows = next;
      } else {
         this.rows = List.of();
      }
   }

   private String position(double x, double y, double z) {
      return this.decimals.isOn() ? String.format(Locale.ROOT, "%.1f, %.1f, %.1f", x, y, z) : blocks(x) + ", " + blocks(y) + ", " + blocks(z);
   }

   private static String blocks(double value) {
      return Long.toString((long)Math.floor(value));
   }

   private List<CoordinatesModule.Row> rows(HudContext hud) {
      return this.rows.isEmpty() && hud.preview
         ? List.of(
            new CoordinatesModule.Row(57617, "128, 64, −342", 0.0F),
            new CoordinatesModule.Row(57636, I18n.get("rose.hud.direction.north", new Object[0]) + "  −Z", 0.0F),
            new CoordinatesModule.Row(58100, I18n.get("biome.minecraft.plains", new Object[0]), 0.0F),
            new CoordinatesModule.Row(57554, I18n.get("rose.hud.coordinates.nether", new Object[0]) + "  16, −43", 0.0F)
         )
         : this.rows;
   }

   @Override
   protected boolean hasContent(HudContext hud) {
      return !this.rows.isEmpty();
   }

   @Override
   protected Size measure(HudContext hud) {
      if (this.rows.isEmpty() && hud.mc.player != null) {
         this.onTick();
      }

      float width = 0.0F;
      List<CoordinatesModule.Row> shown = this.rows(hud);

      for (CoordinatesModule.Row row : shown) {
         width = Math.max(width, hud.medium.width(row.text(), 6.8F));
      }

      return new Size(27.0F + width, 12.0F + shown.size() * 12.0F);
   }

   @Override
   protected void render(HudContext hud, float w, float h) {
      float y = 6.0F;

      for (CoordinatesModule.Row row : this.rows(hud)) {
         float iconX = 11.5F;
         float iconY = y + 6.0F;
         if (row.icon() == 57636) {
            LocalPlayer player = hud.mc.player;
            float rotation = player != null && !hud.preview
               ? Mth.rotLerp(hud.partialTick, player.yRotO, player.getYRot()) + 180.0F
               : row.iconRotation();
            hud.g.pose().pushMatrix();
            hud.g.pose().translate(iconX, iconY);
            hud.g.pose().rotate((float)Math.toRadians(rotation));
            hud.icon(row.icon(), 0.0F, 0.0F, 8.5F, hud.accent());
            hud.g.pose().popMatrix();
         } else {
            hud.icon(row.icon(), iconX, iconY, 8.5F, hud.accent());
         }

         hud.text(hud.medium, row.text(), 20.0F, HudContext.centered(y, 12.0F, 6.8F), 6.8F, hud.label);
         y += 12.0F;
      }
   }

   private record Row(int icon, String text, float iconRotation) {
   }
}
