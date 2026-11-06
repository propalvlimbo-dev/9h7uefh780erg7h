package dev.rose.gui.menu;

import dev.rose.gui.UiSounds;
import dev.rose.module.Modules;
import dev.rose.module.impl.ClickGuiModule;
import dev.rose.notify.Notifications;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.character.CharacterModel;
import dev.rose.render.character.CharacterRig;
import dev.rose.render.style.Corners;
import dev.rose.util.ColorUtil;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;


public final class MenuCharacter {
   private static final int LINES = 7;
   private static CharacterModel custom;
   private static Path customPath;
   private static boolean customWarned;
   private final Random random = new Random();
   private final List<float[]> notes = new ArrayList<>();
   private CharacterRig rig;
   private CharacterModel rigModel;
   private String line = "";
   private long lineStart;
   private long lineEnd;
   private boolean greeted;

   public static void reloadCustom() {
      customPath = null;
      custom = null;
   }

   void draw(MenuUi ui, float appear) {
      ClickGuiModule settings = Modules.clickGui();
      CharacterModel model = model(settings);
      if (model == null) {
         this.rig = null;
      } else {
         if (this.rig == null || this.rigModel != model) {
            this.rig = new CharacterRig(model);
            this.rigModel = model;
         }

         float scale = ui.screenHeight * 1.02F / model.height() * settings.characterScale();
         float x = 12.0F - (1.0F - appear) * 60.0F;
         float y = ui.screenHeight + 16.0F - model.height() * scale;
         this.rig.place(x, y, scale);
         this.rig.hairPhysics = settings.hairPhysics();
         this.rig.motion = settings.reduceMotion() ? 0.15F : 1.0F;
         this.rig.update(ui.mouseX, ui.mouseY);
         this.rig.draw(ui.g, appear, 1711276032, 7.0F, 5.0F);
         if (!this.greeted && appear > 0.6F && settings.reactions()) {
            this.greeted = true;
            this.say(this.greeting(), 3200L);
         }
      }
   }

   void drawOverlay(MenuUi ui, float appear) {
      if (this.rig != null) {
         MenuPalette p = ui.palette;
         String credit = this.rig.model().credit();
         if (!credit.isEmpty()) {
            ui.text(ui.regular, credit, 8.0F, ui.screenHeight - 10.0F, 5.4F, ColorUtil.withAlpha(-1, 144));
         }

         long now = System.currentTimeMillis();
         float bubble = ui.anim("character:bubble", !this.line.isEmpty() && now < this.lineEnd ? 1.0F : 0.0F, 14.0F);
         if (bubble > 0.01F && !this.line.isEmpty()) {
            float size = 7.4F;
            float padX = 8.0F;
            float h = 20.0F;
            int shown = Math.min(this.line.length(), (int)((now - this.lineStart) / 26L));
            float fullW = ui.medium.width(this.line, size) + padX * 2.0F;
            float bx = Math.max(4.0F, this.rig.headScreenX() + 18.0F);
            float by = Math.max(4.0F, this.rig.headScreenY() - 34.0F);
            float previous = ui.alpha;
            ui.alpha = previous * bubble;
            MenuDraw.popup(ui, bx, by + (1.0F - bubble) * 4.0F, fullW, h, 10.0F);
            ui.textCentered(ui.medium, this.line.substring(0, shown), bx + padX, by + (1.0F - bubble) * 4.0F + h / 2.0F, size, p.text());
            ui.alpha = previous;
         }

         float dt = ui.delta();
         int accent = Theme.accent();
         int accent2 = Theme.accent2();

         for (int i = this.notes.size() - 1; i >= 0; i--) {
            float[] note = this.notes.get(i);
            note[4] -= dt / 1.5F;
            if (note[4] <= 0.0F) {
               this.notes.remove(i);
            } else {
               note[2] *= (float)Math.pow(0.4, dt);
               note[0] += (note[2] + (float)Math.sin(note[4] * 9.0F + i) * 10.0F) * dt;
               note[1] += note[3] * dt;
               float pop = Math.min(1.0F, (1.0F - note[4]) * 6.0F);
               int color = ColorUtil.lerp(accent, accent2, i % 3 / 2.0F);
               ui.icon((int)note[5], note[0], note[1], 11.0F * pop, ColorUtil.fade(color, Math.min(1.0F, note[4] * 2.5F) * appear));
            }
         }

         Surfaces.fill(ui.g, 0.0F, ui.screenHeight - 24.0F, ui.screenWidth * 0.35F, 24.0F, Corners.NONE, 0, 0, 0, 1073741824, appear);
      }
   }

   boolean click(float x, float y) {
      if (this.rig != null && this.rig.hit(x, y) && Modules.clickGui().reactions()) {
         this.rig.hop(1.0F);
         this.rig.nod(this.random.nextBoolean() ? 1.0F : -1.0F);
         this.burstNotes(3);
         this.say(I18n.get("rose.character.line." + (1 + this.random.nextInt(7)), new Object[]{playerName()}), 2600L);
         UiSounds.play(UiSounds.Sound.CHARACTER);
         return true;
      } else {
         return false;
      }
   }

   void moduleToggled(String moduleName, boolean enabled) {
      if (this.rig != null && Modules.clickGui().reactions()) {
         if (enabled) {
            this.rig.hop(0.55F);
            this.burstNotes(1);
         } else {
            this.rig.nod(-0.8F);
         }

         this.say(I18n.get(enabled ? "rose.character.enabled" : "rose.character.disabled", new Object[]{moduleName}), 2000L);
      }
   }

   private void say(String text, long millis) {
      this.line = text;
      this.lineStart = System.currentTimeMillis();
      this.lineEnd = this.lineStart + millis;
   }

   private void burstNotes(int count) {
      float x = this.rig.headScreenX() + 14.0F;
      float y = this.rig.headScreenY() - 16.0F;

      for (int i = 0; i < count; i++) {
         int icon = this.random.nextBoolean() ? 57634 : 58186;
         this.notes
            .add(
               new float[]{
                  x + this.random.nextFloat() * 10.0F, y, 20.0F + this.random.nextFloat() * 50.0F, -34.0F - this.random.nextFloat() * 26.0F, 1.0F, icon
               }
            );
      }
   }

   private String greeting() {
      int hour = LocalTime.now().getHour();
      String key = hour < 5 ? "night" : (hour < 12 ? "morning" : (hour < 18 ? "day" : "evening"));
      return I18n.get("rose.character.greeting." + key, new Object[]{playerName()});
   }

   private static String playerName() {
      return Minecraft.getInstance().getUser().getName();
   }

   private static CharacterModel model(ClickGuiModule settings) {
      if (settings.character() != ClickGuiModule.Companion.CUSTOM) {
         return null;
      }

      Path file = settings.characterImage();
      if (file == null) {
         if (!customWarned) {
            customWarned = true;
            Notifications.show(
               Notifications.Kind.INFO,
               I18n.get("rose.character.custom_missing.title", new Object[0]),
               I18n.get("rose.character.custom_missing.subtitle", new Object[0])
            );
         }

         return null;
      } else {
         if (!file.equals(customPath)) {
            customPath = file;
            custom = CharacterModel.file(file);
         }

         return custom;
      }
   }
}
