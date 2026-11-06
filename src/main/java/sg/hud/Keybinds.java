package sg.hud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.gui.screens.ChatScreen;
import org.joml.Vector4f;
import org.joml.Vector4i;
import sg.events.EventListener;
import sg.ec.KeyDisplayNames;
import sg.ec.ClientFonts;
import sg.modules.ModuleRegistry;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.hud.HudElement;
import sg.settings.Keybind;
import sg.events.impl.HudRenderEvent;
import sg.settings.ValueSetting;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.settings.BooleanSetting;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

public class Keybinds extends HudElement {
   private final FloatAnimation сК;
   private final FloatAnimation сI;
   private final FloatAnimation сТ;
   private final Map<Keybinds.BindEntry, FloatAnimation[]> Вб;
   private final List<Keybinds.BindEntry> Вш;
   private final List<Keybinds.CheckboxBind> ВИ;
   private final Map<ClientModule, Integer> ВЫ;
   private List<ClientModule> Вь;
   private long е衣;
   private final EventListener<HudRenderEvent> ен;
   private static final String с48 = "Keybinds";
   private static final float с45 = 239.0F;
   private static final float с4в = 820.0F;
   private static final float с4ъ = 120.0F;
   private static final float с4М = 35.0F;
   private static final float с4Н = 15.0F;
   private static final float с4н = 15.0F;
   private static final float с4Ц = 10.0F;
   private static final long с4ч = Long.MIN_VALUE;
   private static final String с4Э = "Горячие клавиши";
   private static final float с4必 = 0.001F;
   private static final float с4щ = 13.0F;
   private static final float с4Г = 13.0F;
   private static final float с4л = 13.0F;
   private static final float с4э = 110.0F;
   private static final float с4ю = 50.0F;
   private static final float с4Щ = 255.0F;
   private static final float с4у = 12.0F;
   private static final float с42 = 33.0F;
   private static final float с4ф = 10.0F;
   private static final float с4и = 0.01F;
   private static final float с49 = 0.5F;
   private static final float с4о = 0.5F;
   private static final double с4O = 700.0;
   private static final float с47 = 0.55F;
   private static final float с4Р = 10.0F;
   private static final float с43 = 33.0F;
   private static final float с4р = 20.0F;
   private static final float с46 = 1.4F;
   private static final String с4_ = "@";
   private static final float с4Ш = 10.0F;
   private static final float с4ы = 8.0F;
   private static final String с4й = "Keybinds";
   private static final float с4Ж = 32.0F;
   private static final float с4м = 9.0F;
   private static final float с4К = -10.0F;
   private static final float с4I = 36.0F;
   private static final float с4Т = 255.0F;
   private static final float с4О = 32.0F;
   private static final float с4Ф = 2.5F;
   private static final float с4Й = 10.0F;
   private static final float с4к = 13.0F;
   private static final float с4г = 2.5F;
   private static final float с40 = 10.0F;
   private static final float с4х = 3.0F;
   private static final float с4У = 36.0F;
   private static final float с4З = 15.0F;
   private static final float с4Е = -10.0F;
   private static final float с4П = 15.0F;
   private static final float с4西 = 15.0F;

   private List<Keybinds.BindEntry> method_8К() {
      ModuleRegistry var1 = RoseClient.getInstance().getServices().getModules();
      long var2 = Keybind.getChangeRevision();
      boolean var4 = this.е衣 != var2 || !this.Вь.equals(var1);
      if (!var4) {
         for (ClientModule var6 : var1) {
            if (this.ВЫ.get(var6) != var6.getSettings().size()) {
               var4 = true;
               break;
            }
         }
      }

      if (var4) {
         this.method_4(var1, var2);
      }

      this.Вш.clear();

      for (Keybinds.CheckboxBind var8 : this.ВИ) {
         if (var8.module().isEnabled() && (var8.setting() == null || var8.setting().getValue())) {
            this.Вш.add(var8.item());
         }
      }

      return this.Вш;
   }

   private void method_4(List<ClientModule> var1, long var2) {
      this.ВИ.clear();
      this.ВЫ.clear();
      this.Вь = List.copyOf(var1);
      this.е衣 = var2;

      for (ClientModule var5 : var1) {
         this.ВЫ.put(var5, var5.getSettings().size());
         if (var5.getKeybind().getKeyCode() != -1) {
            this.ВИ
               .add(
                  new Keybinds.CheckboxBind(
                     var5,
                     null,
                     new Keybinds.BindEntry("module:" + var5.getName(), var5.getName(), var5.getCategory()._Н(), var5.getKeybind().getKeyCode())
                  )
               );
         }

         for (ValueSetting var7 : var5.getSettings()) {
            if (var7 instanceof BooleanSetting var8) {
               int var9 = var8.getKeybind().getKeyCode();
               if (var9 >= 0) {
                  this.ВИ
                     .add(
                        new Keybinds.CheckboxBind(
                           var5,
                           var8,
                           new Keybinds.BindEntry("checkbox:" + var5.getName() + ":" + var8.getName(), var8.getName(), var5.getCategory()._Н(), var9)
                        )
                     );
               }
            }
         }
      }
   }

   public Keybinds() {
      super(с48, с45, с4в, с4ъ, с4М);
      this.сК = new FloatAnimation(0.0F, с4Н);
      this.сI = new FloatAnimation(0.0F, с4н);
      this.сТ = new FloatAnimation(0.0F, с4Ц);
      this.Вб = new HashMap<>();
      this.Вш = new ArrayList<>();
      this.ВИ = new ArrayList<>();
      this.ВЫ = new IdentityHashMap<>();
      this.Вь = List.of();
      this.е衣 = с4ч;
      this.ен = this::method_8Ы;
   }

   public void method_8Ы(HudRenderEvent var1) {
      if (!RoseClient.getInstance().getServices().getModules().getHud().я().isSelected(с4Э)) {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      } else {
         List<Keybinds.BindEntry> var2 = this.method_8К();
         boolean var3 = MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen || !var2.isEmpty();
         this.сТ.updateTarget(var3 ? 1.0F : 0.0F);
         if (!var3 && this.сТ.getValue() <= с4必) {
            this.Вб.clear();
            this.setWidth(0.0F);
            this.setHeight(0.0F);
         } else {
            float var4 = this.getX();
            float var5 = this.getY();
            float var6 = 0.0F;
            float var7 = 0.0F;
            float var8 = 0.0F;

            for (Keybinds.BindEntry var10 : var2) {
               var6 = Math.max(var6, ClientFonts.getMediumFont().getFontFace().measureTextWidth(var10.name(), с4щ, 0.0F));
               var7 = Math.max(var7, ClientFonts.getMediumFont().getFontFace().measureTextWidth(KeyDisplayNames.О(var10.key()), с4Г, 0.0F));
               var8 = Math.max(var8, ClientFonts.getMediumFont().getFontFace().measureTextWidth(var10.icon(), с4л, 0.0F));
            }

            float var23 = Math.max(с4э, var6 + var7 + var8 + с4ю);
            this.сК.updateTarget(var23);
            UiDrawing.setGlobalAlpha(с4Щ * this.сТ.getValue());
            this.pushGuiScaleTransform();
            UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());

            try {
               UiBatchDrawing.drawHudWindowBackground(var4, var5, this.сК.getValue(), this.сI.getValue(), с4у);
               float var24 = Math.clamp((this.сI.getValue() - с42) / с4ф, 0.0F, 1.0F);
               if (var24 > с4и) {
                  int var11 = ColorMath.withAlpha(
                     ColorMath.blendArgb(
                        ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), ThemeEditor.getColorArgb(ThemeColorSlot.MAIN_TWO), с49 + с4о * (float)Math.sin(System.currentTimeMillis() / с4O)
                     ),
                     с47 * var24
                  );
                  int var12 = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), 0.0F);
                  UiBatchDrawing.drawRoundedRect(var4 + с4Р, var5 + с43, this.сК.getValue() - с4р, с46, new Vector4f(0.0F), new Vector4i(var12, var11, var11, var12));
               }

               UiBatchDrawing.drawText(ClientFonts.getIconsFont(), с4_, var4 + с4Ш, var5 + с4ы, 16, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
               UiBatchDrawing.drawText(ClientFonts.getMediumFont(), с4й, var4 + с4Ж, var5 + с4м, 14, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
               ArrayList var25 = new ArrayList();

               for (int var26 = 0; var26 < var2.size(); var26++) {
                  Keybinds.BindEntry var13 = (Keybinds.BindEntry)var2.get(var26);
                  int var14 = var26;
                  FloatAnimation[] var15 = this.Вб.computeIfAbsent(var13, var1x -> new FloatAnimation[]{new FloatAnimation(0.0F, с4З), new FloatAnimation(с4Е, с4П), new FloatAnimation(var14 * 20, с4西)});
                  var15[0].updateTarget(1.0F);
                  var15[1].updateTarget(0.0F);
                  float var16 = var26 * 20;
                  var15[2].updateTarget(var16);
               }

               for (Entry var29 : this.Вб.entrySet()) {
                  if (!var2.contains(var29.getKey())) {
                     FloatAnimation[] var31 = (FloatAnimation[])var29.getValue();
                     var31[0].updateTarget(0.0F);
                     var31[1].updateTarget(с4К);
                     if (var31[0].isFinished() && var31[1].isFinished()) {
                        var25.add((Keybinds.BindEntry)var29.getKey());
                     }
                  }
               }

               var25.forEach(this.Вб::remove);

               for (Entry var30 : this.Вб.entrySet()) {
                  Keybinds.BindEntry var32 = (Keybinds.BindEntry)var30.getKey();
                  FloatAnimation[] var33 = (FloatAnimation[])var30.getValue();
                  float var34 = var33[0].getValue();
                  if (!(var34 <= 0.0F) || !var33[0].isFinished()) {
                     float var17 = var5 + с4I + var33[2].getValue();
                     String var18 = KeyDisplayNames.О(var32.key());
                     float var19 = var33[1].getValue();
                     UiDrawing.setGlobalAlpha(var34 * с4Т);
                     UiBatchDrawing.drawText(
                        ClientFonts.getMediumFont(), var32.name(), var4 + с4О, var17 + с4Ф + var19, 13, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT)
                     );
                     UiBatchDrawing.drawText(
                        ClientFonts.getMediumFont(),
                        var18,
                        var4 + this.сК.getValue() - с4Й - ClientFonts.getMediumFont().getFontFace().measureTextWidth(var18, с4к, 0.0F),
                        var17 + с4г + var19,
                        13,
                        ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_SUR_TEXT)
                     );
                     UiBatchDrawing.drawText(ClientFonts.getIconsFont(), var32.icon(), var4 + с40, var17 + с4х + var19, 13, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
                     UiDrawing.resetGlobalAlpha();
                  }
               }
            } finally {
               UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
               UiDrawing.resetGlobalAlpha();
               this.popGuiScaleTransform();
            }

            this.сI.updateTarget(var2.isEmpty() ? с4У : 36 + var2.size() * 20 + 7);
            this.setWidth(this.сК.getValue());
            this.setHeight(this.сI.getValue());
         }
      }
   }

   private record BindEntry(String id, String name, String icon, int key) {
   }

   private record CheckboxBind(ClientModule module, BooleanSetting setting, Keybinds.BindEntry item) {
   }
}
