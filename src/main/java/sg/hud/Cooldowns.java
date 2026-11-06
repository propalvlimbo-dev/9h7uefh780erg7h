package sg.hud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import lombok.Generated;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector4f;
import org.joml.Vector4i;
import sg.events.impl.ItemUseStartEvent;
import sg.events.impl.PlayerTickEvent;
import sg.ec.ItemCooldownSeconds;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ReallyWorldItemCooldowns;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.events.impl.HudRenderEvent;

public class Cooldowns extends HudElement {
   private final FloatAnimation ср;
   private final FloatAnimation с6;
   private final FloatAnimation с_;
   private final Map<Cooldowns.CooldownEntry, FloatAnimation[]> сП;
   private final Set<Item> у;
   private final Map<Integer, String> с西;
   private final EventListener<PlayerTickEvent> Хб;
   private final EventListener<ItemUseStartEvent> ХЫ;
   private final EventListener<HudRenderEvent> Хl;
   private static final String нж = "Cooldowns";
   private static final float н弟 = 192.0F;
   private static final float нб = 425.0F;
   private static final float нЫ = 120.0F;
   private static final float нl = 35.0F;
   private static final float н8 = 15.0F;
   private static final float н5 = 15.0F;
   private static final float нв = 10.0F;
   private static final String нъ = "Задержки";
   private static final float нМ = 0.001F;
   private static final float нН = 13.0F;
   private static final float нн = 13.0F;
   private static final float нЦ = 120.0F;
   private static final float нч = 50.0F;
   private static final float нЭ = 255.0F;
   private static final float н必 = 12.0F;
   private static final float нщ = 33.0F;
   private static final float нГ = 10.0F;
   private static final float нл = 0.01F;
   private static final float нэ = 0.5F;
   private static final float ню = 0.5F;
   private static final double нЩ = 700.0;
   private static final float ну = 0.55F;
   private static final float н2 = 10.0F;
   private static final float нф = 33.0F;
   private static final float ни = 20.0F;
   private static final float н9 = 1.4F;
   private static final String но = "O";
   private static final float нO = 10.0F;
   private static final float н7 = 8.0F;
   private static final String нР = "Cooldowns";
   private static final float н3 = 32.0F;
   private static final float нр = 9.0F;
   private static final float н6 = 15.0F;
   private static final float н_ = -10.0F;
   private static final float нШ = 15.0F;
   private static final float ны = 20.0F;
   private static final float нй = 15.0F;
   private static final float нЖ = 20.0F;
   private static final float нм = -10.0F;
   private static final float нК = 36.0F;
   private static final float нI = 255.0F;
   private static final String нТ = "enchanted_golden_apple";
   private static final String нО = "crossbow";
   private static final String нФ = "golden_apple";
   private static final String нЙ = "crossbow_arrow";
   private static final float нк = 10.0F;
   private static final float нг = 2.5F;
   private static final float н0 = 16.0F;
   private static final float нх = 16.0F;
   private static final float нУ = 30.0F;
   private static final float нЗ = 2.5F;
   private static final float нЕ = 13.0F;
   private static final float нП = 10.0F;
   private static final float н西 = 2.5F;
   private static final float нд = 36.0F;
   private static final float нБ = 36.0F;
   private static final float нС = 20.0F;
   private static final float нз = 7.0F;
   private static final String нц = "%d:%02d";

   private List<Cooldowns.CooldownEntry> method_6б() {
      ArrayList var1 = new ArrayList();
      if (MINECRAFT_CLIENT.player == null) {
         return var1;
      }

      ReallyWorldItemCooldowns var2 = ReallyWorldItemCooldowns.Ы();
      float var3 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
      this.у.clear();

      for (ReallyWorldItemCooldowns.CooldownEntry var5 : var2.必_2571477d()) {
         ItemStack var6 = var5.icon().getDefaultInstance();
         if (this.у.add(var6.getItem())) {
            var1.add(new Cooldowns.CooldownEntry("rw:" + var5.item().name(), var6, this.м(var5.remaining())));
         }
      }

      for (int var8 = 0; var8 < MINECRAFT_CLIENT.player.getInventory().getContainerSize(); var8++) {
         ItemStack var9 = MINECRAFT_CLIENT.player.getInventory().getItem(var8);
         if (!var9.isEmpty()) {
            Item var10 = var9.getItem();
            if (this.у.add(var10) && !var2.O(var10)) {
               float var7 = ItemCooldownSeconds.getRemainingSeconds(var9, var3);
               if (!(var7 <= 0.0F)) {
                  var1.add(new Cooldowns.CooldownEntry(var10.getDescriptionId(), var9.copy(), this.м(var7)));
               }
            }
         }
      }

      return var1;
   }

   public void Й(HudRenderEvent var1) {
      if (!RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(нъ)) {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else {
         List<Cooldowns.CooldownEntry> var2 = this.method_6б();
         boolean var3 = MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen || !var2.isEmpty();
         this.с_.updateTarget(var3 ? 1.0F : 0.0F);
         if (!var3 && this.с_.getValue() <= нМ) {
            this.сП.clear();
            this.setWidth(0.0F);
            this.setHeight(0.0F);
         } else {
            float var4 = this.getX();
            float var5 = this.getY();
            float var6 = 0.0F;
            float var7 = 0.0F;

            for (Cooldowns.CooldownEntry var9 : var2) {
               var6 = Math.max(var6, ClientFonts.getMediumFont().getFontFace().measureTextWidth(var9.я().getString(), нН, 0.0F));
               var7 = Math.max(var7, ClientFonts.getMediumFont().getFontFace().measureTextWidth(var9.method_31(), нн, 0.0F));
            }

            float var29 = Math.max(нЦ, var6 + var7 + нч);
            this.ср.updateTarget(var29);
            UiDrawing.setGlobalAlpha(нЭ * this.с_.getValue());
            this.pushGuiScaleTransform();
            UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());

            try {
               UiBatchDrawing.drawHudWindowBackground(var4, var5, this.ср.getValue(), this.с6.getValue(), н必);
               float var30 = Math.clamp((this.с6.getValue() - нщ) / нГ, 0.0F, 1.0F);
               if (var30 > нл) {
                  int var10 = ColorMath.withAlpha(
                     ColorMath.blendArgb(
                        ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO), нэ + ню * (float)Math.sin(System.currentTimeMillis() / нЩ)
                     ),
                     ну * var30
                  );
                  int var11 = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), 0.0F);
                  UiBatchDrawing.drawRoundedRect(var4 + н2, var5 + нф, this.ср.getValue() - ни, н9, new Vector4f(0.0F), new Vector4i(var11, var10, var10, var11));
               }

               UiBatchDrawing.drawText(ClientFonts.getIconsFont(), но, var4 + нO, var5 + н7, 16, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
               UiBatchDrawing.drawText(ClientFonts.getMediumFont(), нР, var4 + н3, var5 + нр, 14, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
               ArrayList var31 = new ArrayList();

               for (int var32 = 0; var32 < var2.size(); var32++) {
                  Cooldowns.CooldownEntry var12 = (Cooldowns.CooldownEntry)var2.get(var32);
                  FloatAnimation[] var13 = this.сП.remove(var12);
                  if (var13 == null) {
                     var13 = new FloatAnimation[]{new FloatAnimation(0.0F, н6), new FloatAnimation(н_, нШ), new FloatAnimation(var32 * ны, нй)};
                  }

                  this.сП.put(var12, var13);
                  var13[0].updateTarget(1.0F);
                  var13[1].updateTarget(0.0F);
                  var13[2].updateTarget(var32 * нЖ);
               }

               for (Entry var35 : this.сП.entrySet()) {
                  if (!var2.contains(var35.getKey())) {
                     FloatAnimation[] var37 = (FloatAnimation[])var35.getValue();
                     var37[0].updateTarget(0.0F);
                     var37[1].updateTarget(нм);
                     if (var37[0].isFinished() && var37[1].isFinished()) {
                        var31.add((Cooldowns.CooldownEntry)var35.getKey());
                     }
                  }
               }

               var31.forEach(this.сП::remove);

               for (Entry var36 : this.сП.entrySet()) {
                  Cooldowns.CooldownEntry var38 = (Cooldowns.CooldownEntry)var36.getKey();
                  FloatAnimation[] var14 = (FloatAnimation[])var36.getValue();
                  float var15 = var14[0].getValue();
                  if (!(var15 <= 0.0F) || !var14[0].isFinished()) {
                     float var16 = var5 + нК + var14[2].getValue();
                     float var17 = var14[1].getValue();
                     String var18 = var38.method_31();
                     UiDrawing.setGlobalAlpha(var15 * нI);

                     ChatFormatting var19 = switch (var38.й().getRarity()) {
                        case UNCOMMON -> ChatFormatting.YELLOW;
                        case RARE -> ChatFormatting.AQUA;
                        case EPIC -> ChatFormatting.LIGHT_PURPLE;
                        default -> null;
                     };
                     Component var20 = var19 == null ? var38.я() : var38.я().copy().setStyle(Style.EMPTY.withColor(var19));
                     Identifier var21 = BuiltInRegistries.ITEM.getKey(var38.й().getItem());
                     String var22 = var21.getPath();

                     String var23 = switch (var22) {
                        case нТ -> нФ;
                        case нО -> нЙ;
                        default -> var22;
                     };
                     Identifier var39 = Identifier.fromNamespaceAndPath(var21.getNamespace(), "textures/item/" + var23 + ".png");
                     UiBatchDrawing.drawTexture(
                        var4 + нк, var16 + нг + var17, н0, нх, new Vector4f(0.0F), new Vector4f(0.0F, 0.0F, 1.0F, 1.0F), new Vector4i(-1), var39
                     );
                     UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var20, var4 + нУ, var16 + нЗ + var17, 13, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
                     float var40 = ClientFonts.getMediumFont().getFontFace().measureTextWidth(var18, нЕ, 0.0F);
                     UiBatchDrawing.drawText(
                        ClientFonts.getMediumFont(), var18, var4 + this.ср.getValue() - нП - var40, var16 + н西 + var17, 13, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
                     );
                     UiDrawing.resetGlobalAlpha();
                  }
               }
            } finally {
               UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
               UiDrawing.resetGlobalAlpha();
               this.popGuiScaleTransform();
            }

            this.с6.updateTarget(var2.isEmpty() ? нд : нБ + var2.size() * нС + нз);
            this.setWidth(this.ср.getValue());
            this.setHeight(this.с6.getValue());
         }
      }
   }

   public Cooldowns() {
      super(нж, н弟, нб, нЫ, нl);
      this.ср = new FloatAnimation(0.0F, н8);
      this.с6 = new FloatAnimation(0.0F, н5);
      this.с_ = new FloatAnimation(0.0F, нв);
      this.сП = new HashMap<>();
      this.у = new HashSet<>();
      this.с西 = new HashMap<>();
      this.Хб = var0 -> ReallyWorldItemCooldowns.Ы().Лб();
      this.ХЫ = var0 -> ReallyWorldItemCooldowns.Ы().ЛР();
      this.Хl = this::Й;
   }

   private String м(float var1) {
      return this.с西.computeIfAbsent((int)Math.ceil(var1), var0 -> {
         int var1x = var0 / 60;
         int var2 = var0 % 60;
         return String.format(нц, var1x, var2);
      });
   }

   private static class CooldownEntry {
      private final String ВЪ;
      private final ItemStack т;
      private final String Вт;

      @Override
      public int hashCode() {
         return Objects.hash(this.ВЪ);
      }

      @Generated
      public String method_3Л() {
         return this.ВЪ;
      }

      public CooldownEntry(String var1, ItemStack var2, String var3) {
         this.ВЪ = var1;
         this.т = var2;
         this.Вт = var3;
      }

      @Override
      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else {
            return var1 instanceof Cooldowns.CooldownEntry var2 ? this.ВЪ.equals(var2.ВЪ) : false;
         }
      }

      public Component я() {
         return this.т.getHoverName();
      }

      @Generated
      public String method_31() {
         return this.Вт;
      }

      @Generated
      public ItemStack й() {
         return this.т;
      }
   }
}
