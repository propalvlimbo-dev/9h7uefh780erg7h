package dev.rose.hud;

import java.util.List;


public final class HudLayouts {
   private HudLayouts() {
   }

   private static List<HudLayouts.Spot> spots(HudLayouts.Layout layout) {
      return switch (layout) {
         case CLASSIC -> List.of(
            new HudLayouts.Spot("watermark", Anchor.TOP_LEFT, 6.0F, 6.0F, 1.0F),
            new HudLayouts.Spot("fps", Anchor.TOP_LEFT, 6.0F, 28.0F, 1.0F),
            new HudLayouts.Spot("ping", Anchor.TOP_LEFT, 6.0F, 48.0F, 1.0F),
            new HudLayouts.Spot("array_list", Anchor.TOP_RIGHT, -6.0F, 6.0F, 1.0F),
            new HudLayouts.Spot("keybinds", Anchor.LEFT, 6.0F, 10.0F, 1.0F),
            new HudLayouts.Spot("target_hud", Anchor.CENTER, 0.0F, 52.0F, 1.0F),
            new HudLayouts.Spot("warnings", Anchor.CENTER, 0.0F, -46.0F, 1.0F),
            new HudLayouts.Spot("potions", Anchor.RIGHT, -6.0F, 0.0F, 1.0F),
            new HudLayouts.Spot("armor", Anchor.BOTTOM, 118.0F, -4.0F, 1.0F),
            new HudLayouts.Spot("coordinates", Anchor.TOP_LEFT, 6.0F, 110.0F, 1.0F),
            new HudLayouts.Spot("compass", Anchor.TOP, 0.0F, 34.0F, 1.0F)
         );
         case MINIMAL -> List.of(
            new HudLayouts.Spot("watermark", Anchor.TOP_LEFT, 6.0F, 6.0F, 0.9F),
            new HudLayouts.Spot("target_hud", Anchor.CENTER, 0.0F, 48.0F, 0.9F),
            new HudLayouts.Spot("coordinates", Anchor.TOP_LEFT, 6.0F, 28.0F, 0.9F)
         );
         case FOCUS -> List.of(
            new HudLayouts.Spot("watermark", Anchor.TOP_LEFT, 6.0F, 6.0F, 1.0F),
            new HudLayouts.Spot("target_hud", Anchor.CENTER, 70.0F, 0.0F, 1.0F),
            new HudLayouts.Spot("potions", Anchor.CENTER, -70.0F, 0.0F, 0.9F),
            new HudLayouts.Spot("armor", Anchor.CENTER, 0.0F, 34.0F, 0.9F),
            new HudLayouts.Spot("array_list", Anchor.TOP_RIGHT, -6.0F, 6.0F, 0.9F),
            new HudLayouts.Spot("keybinds", Anchor.LEFT, 6.0F, 0.0F, 0.9F),
            new HudLayouts.Spot("coordinates", Anchor.TOP_LEFT, 6.0F, 28.0F, 1.0F)
         );
      };
   }

   public static void apply(HudLayouts.Layout layout) {
      List<HudLayouts.Spot> spots = spots(layout);

      for (HudElement element : HudManager.elements()) {
         HudLayouts.Spot spot = spots.stream().filter(candidate -> candidate.id().equals(element.id())).findFirst().orElse(null);
         if (spot == null) {
            if (layout.strict) {
               element.setEnabled(false);
            }
         } else {
            element.place(spot.anchor(), spot.x(), spot.y());
            element.setScale(spot.scale());
            element.setEnabled(true);
         }
      }
   }

   public static void resetPlacements() {
      for (HudElement element : HudManager.elements()) {
         element.resetPlacement();
         element.setScale(1.0F);
      }
   }

   public enum Layout {
      CLASSIC(false),
      MINIMAL(true),
      FOCUS(false);

      private final boolean strict;

      Layout(boolean strict) {
         this.strict = strict;
      }
   }

   private record Spot(String id, Anchor anchor, float x, float y, float scale) {
   }
}
