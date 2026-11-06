package dev.rose.gui;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.rose.RoseClient;
import dev.rose.gui.menu.MenuCharacter;
import dev.rose.notify.Notifications;
import dev.rose.render.ImageFiles;
import dev.rose.render.Images;
import dev.rose.render.LiquidGlass;
import dev.rose.render.Render2D;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.character.CharacterModel;
import dev.rose.render.character.CharacterRig;
import dev.rose.render.font.FontWeight;
import dev.rose.render.font.Fonts;
import dev.rose.render.font.SdfFont;
import dev.rose.render.gpu.RoseTexture;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Glass;
import dev.rose.render.style.Paint;
import dev.rose.util.ColorUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;


public final class CharacterRigEditor extends Screen {
   private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("rose", "dynamic/rig_editor");
   private static final long PREVIEW_DELAY_MS = 250L;
   private final Screen parent;
   private final Path png;
   private JsonObject base;
   private ImageFiles.Pixels pixels;
   private RoseTexture texture;
   private int imageW;
   private int imageH;
   private boolean broken;
   private CharacterRigEditor.Tool tool = CharacterRigEditor.Tool.HEAD;
   private float headX;
   private float headY;
   private float headRadius;
   private float pivotX;
   private float pivotY;
   private float breathTop;
   private float breathBottom;
   private boolean pivotSet;
   private final List<float[]> tails = new ArrayList<>();
   private final List<float[]> eyes = new ArrayList<>();
   private final List<float[]> boxes = new ArrayList<>();
   private float hueMin;
   private float hueMax;
   private float minSaturation = 0.2F;
   private float minValue = 0.2F;
   private boolean hairSet;
   private int hairColor;
   private int[] skinSample;
   private boolean dirty;
   private boolean previewStale = true;
   private long changedAt;
   private CharacterModel previewModel;
   private CharacterRig previewRig;
   private boolean dragging;
   private float dragX;
   private float dragY;
   private float currentX;
   private float currentY;
   private float canvasX;
   private float canvasY;
   private float canvasScale;
   private final List<CharacterRigEditor.Button> buttons = new ArrayList<>();

   public CharacterRigEditor(Screen parent, Path png) {
      super(Component.translatable("rose.rig.title"));
      this.parent = parent;
      this.png = png;
   }

   protected void init() {
      if (this.pixels == null && !this.broken) {
         try {
            this.pixels = ImageFiles.read(this.png, 4096);
            this.imageW = this.pixels.width();
            this.imageH = this.pixels.height();
            this.texture = new RoseTexture("rig editor", ImageFiles.toNative(this.pixels)).filter(true, false);
            this.minecraft.getTextureManager().register(TEXTURE, this.texture);
         } catch (IOException | RuntimeException e) {
            RoseClient.LOGGER.error("[Rose] rig editor cannot read {}", this.png, e);
            this.broken = true;
            return;
         }

         this.base = CharacterModel.readRig(this.png);
         this.readRig(this.base);
      }
   }

   private void readRig(JsonObject rig) {
      JsonObject head = object(rig, "head");
      this.headX = number(head, "x", this.imageW * 0.5F);
      this.headY = number(head, "y", this.imageH * 0.12F);
      this.headRadius = number(head, "radius", this.imageW * 0.18F);
      this.pivotSet = head.has("pivotX");
      this.pivotX = number(head, "pivotX", this.headX);
      this.pivotY = number(head, "pivotY", this.headY + this.headRadius);
      JsonObject breath = object(rig, "breath");
      this.breathTop = number(breath, "top", this.imageH * 0.18F);
      this.breathBottom = number(breath, "bottom", this.imageH * 0.45F);
      this.tails.clear();

      for (JsonElement element : array(rig, "tails")) {
         JsonObject tail = element.getAsJsonObject();
         this.tails
            .add(
               new float[]{
                  number(tail, "rootX", 0.0F),
                  number(tail, "rootY", 0.0F),
                  number(tail, "tipX", 0.0F),
                  number(tail, "tipY", this.imageH),
                  number(tail, "minX", 0.0F),
                  number(tail, "maxX", this.imageW)
               }
            );
      }

      this.eyes.clear();

      for (JsonElement element : array(rig, "eyes")) {
         JsonObject eye = element.getAsJsonObject();
         this.eyes.add(new float[]{number(eye, "x", 0.0F), number(eye, "y", 0.0F), number(eye, "w", 0.0F), number(eye, "h", 0.0F)});
      }

      this.boxes.clear();

      for (JsonElement element : array(rig, "exclude")) {
         JsonObject box = element.getAsJsonObject();
         this.boxes.add(new float[]{number(box, "x0", 0.0F), number(box, "y0", 0.0F), number(box, "x1", 0.0F), number(box, "y1", 0.0F)});
      }

      JsonObject hair = object(rig, "hair");
      this.hairSet = hair.has("hueMin");
      this.hueMin = number(hair, "hueMin", 0.0F);
      this.hueMax = number(hair, "hueMax", 0.0F);
      this.minSaturation = number(hair, "minSaturation", 0.2F);
      this.minValue = number(hair, "minValue", 0.2F);
      if (this.hairSet) {
         this.hairColor = ColorUtil.hsb((this.hueMin + this.hueMax) / 2.0F / 360.0F, Math.max(0.5F, this.minSaturation), Math.max(0.7F, this.minValue), 255);
      }

      this.skinSample = rig.has("skinSample") && rig.get("skinSample").isJsonArray()
         ? new int[]{rig.getAsJsonArray("skinSample").get(0).getAsInt(), rig.getAsJsonArray("skinSample").get(1).getAsInt()}
         : null;
   }

   private JsonObject toJson() {
      JsonObject rig = this.base == null ? new JsonObject() : this.base.deepCopy();
      JsonObject head = new JsonObject();
      head.addProperty("x", round(this.headX));
      head.addProperty("y", round(this.headY));
      head.addProperty("radius", round(this.headRadius));
      head.addProperty("pivotX", round(this.pivotX));
      head.addProperty("pivotY", round(this.pivotY));
      rig.add("head", head);
      JsonObject breath = rig.has("breath") && rig.get("breath").isJsonObject() ? rig.getAsJsonObject("breath") : new JsonObject();
      breath.addProperty("top", round(Math.min(this.breathTop, this.breathBottom)));
      breath.addProperty("bottom", round(Math.max(this.breathTop, this.breathBottom)));
      rig.add("breath", breath);
      JsonArray tailArray = new JsonArray();

      for (float[] tail : this.tails) {
         JsonObject object = new JsonObject();
         object.addProperty("rootX", round(tail[0]));
         object.addProperty("rootY", round(tail[1]));
         object.addProperty("tipX", round(tail[2]));
         object.addProperty("tipY", round(tail[3]));
         object.addProperty("minX", round(tail[4]));
         object.addProperty("maxX", round(tail[5]));
         tailArray.add(object);
      }

      rig.add("tails", tailArray);
      JsonArray eyeArray = new JsonArray();

      for (float[] eye : this.eyes) {
         JsonObject object = new JsonObject();
         object.addProperty("x", round(eye[0]));
         object.addProperty("y", round(eye[1]));
         object.addProperty("w", round(eye[2]));
         object.addProperty("h", round(eye[3]));
         eyeArray.add(object);
      }

      rig.add("eyes", eyeArray);
      JsonArray boxArray = new JsonArray();

      for (float[] box : this.boxes) {
         JsonObject object = new JsonObject();
         object.addProperty("x0", round(Math.min(box[0], box[2])));
         object.addProperty("y0", round(Math.min(box[1], box[3])));
         object.addProperty("x1", round(Math.max(box[0], box[2])));
         object.addProperty("y1", round(Math.max(box[1], box[3])));
         boxArray.add(object);
      }

      rig.add("exclude", boxArray);
      if (this.hairSet) {
         JsonObject hair = new JsonObject();
         hair.addProperty("hueMin", round(this.hueMin));
         hair.addProperty("hueMax", round(this.hueMax));
         hair.addProperty("minSaturation", Math.round(this.minSaturation * 100.0F) / 100.0F);
         hair.addProperty("minValue", Math.round(this.minValue * 100.0F) / 100.0F);
         rig.add("hair", hair);
      }

      if (this.skinSample != null) {
         JsonArray sample = new JsonArray();
         sample.add(this.skinSample[0]);
         sample.add(this.skinSample[1]);
         rig.add("skinSample", sample);
      }

      return rig;
   }

   private void changed() {
      this.dirty = true;
      this.previewStale = true;
      this.changedAt = System.currentTimeMillis();
   }

   private void save() {
      Path file = CharacterModel.rigFile(this.png);

      try {
         Files.writeString(file, new GsonBuilder().setPrettyPrinting().create().toJson(this.toJson()), StandardCharsets.UTF_8);
         this.base = this.toJson();
         this.dirty = false;
         MenuCharacter.reloadCustom();
         Notifications.show(Notifications.Kind.SUCCESS, I18n.get("rose.rig.saved", new Object[0]), file.getFileName().toString());
      } catch (IOException e) {
         RoseClient.LOGGER.error("[Rose] cannot save rig {}", file, e);
         Notifications.show(Notifications.Kind.ERROR, I18n.get("rose.rig.save_failed", new Object[0]), file.getFileName().toString());
      }
   }

   private void reset() {
      this.base = new JsonObject();
      this.readRig(this.base);
      this.changed();
   }

   private void undoLast() {
      List<float[]> list = switch (this.tool) {
         case EYE -> this.eyes;
         case EXCLUDE -> this.boxes;
         default -> this.tails;
      };
      if (!list.isEmpty()) {
         list.removeLast();
         this.changed();
      }
   }

   public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
      super.render(g, mouseX, mouseY, partialTick);
      SdfFont semibold = Fonts.get(FontWeight.SEMIBOLD);
      SdfFont regular = Fonts.get(FontWeight.REGULAR);
      SdfFont icons = Fonts.icons();
      if (semibold != null && regular != null && icons != null) {
         this.buttons.clear();
         if (this.broken) {
            String text = I18n.get("rose.rig.broken", new Object[0]);
            semibold.draw(g, text, this.width / 2.0F - semibold.width(text, 10.0F) / 2.0F, this.height / 2.0F, 10.0F, -1);
         } else {
            Glass glass = Theme.glass();
            int label = glass.label;
            float panelW = Math.min(230.0F, this.width * 0.36F);
            float areaX = 12.0F;
            float areaY = 40.0F;
            float areaW = this.width - panelW - 36.0F;
            float areaH = this.height - 52;
            this.canvasScale = Math.min(areaW / this.imageW, areaH / this.imageH);
            float drawW = this.imageW * this.canvasScale;
            float drawH = this.imageH * this.canvasScale;
            this.canvasX = areaX + (areaW - drawW) / 2.0F;
            this.canvasY = areaY + (areaH - drawH) / 2.0F;
            LiquidGlass.draw(g, areaX, areaY, areaW, areaH, Corners.themed(16.0F), glass, 1.0F);
            this.checker(g, this.canvasX, this.canvasY, drawW, drawH);
            Images.draw(g, this.texture, 0.0F, 0.0F, 1.0F, 1.0F, this.canvasX, this.canvasY, drawW, drawH, Corners.NONE, 1.0F);
            this.drawRig(g, mouseX, mouseY);
            this.toolbar(g, semibold, icons, mouseX, mouseY);
            float px = this.width - panelW - 12.0F;
            float py = 40.0F;
            float ph = this.height - 52;
            LiquidGlass.draw(g, px, py, panelW, ph, Corners.themed(16.0F), glass, 1.0F);
            this.preview(g, px, py, panelW, ph * 0.62F, mouseX, mouseY);
            float textY = py + ph * 0.62F + 6.0F;
            String title = I18n.get("rose.rig.tool." + this.tool.name().toLowerCase(Locale.ROOT), new Object[0]);
            icons.drawIcon(g, this.tool.icon, px + 16.0F, textY + 5.0F, 9.0F, Theme.accent());
            semibold.draw(g, title, px + 26.0F, textY, 8.5F, label);
            textY += 14.0F;

            for (String line : wrap(
               regular, I18n.get("rose.rig.hint." + this.tool.name().toLowerCase(Locale.ROOT), new Object[0]), 7.0F, panelW - 24.0F
            )) {
               regular.draw(g, line, px + 12.0F, textY, 7.0F, ColorUtil.withAlpha(label, 184));
               textY += 10.0F;
            }

            String counts = I18n.get("rose.rig.counts", new Object[]{this.tails.size(), this.eyes.size(), this.boxes.size()});
            regular.draw(g, counts, px + 12.0F, py + ph - 14.0F, 6.6F, ColorUtil.withAlpha(label, 128));
            if (this.previewStale && !this.dragging && System.currentTimeMillis() - this.changedAt > 250L) {
               this.rebuildPreview();
            }
         }
      }
   }

   private void checker(GuiGraphics g, float x, float y, float w, float h) {
      Surfaces.fill(g, x, y, w, h, Corners.NONE, Paint.solid(822083583), 1.0F);
   }

   private void toolbar(GuiGraphics g, SdfFont font, SdfFont icons, int mouseX, int mouseY) {
      float x = 12.0F;
      float y = 10.0F;
      float h = 22.0F;
      Glass glass = Theme.glass();

      for (CharacterRigEditor.Tool candidate : CharacterRigEditor.Tool.values()) {
         String text = I18n.get("rose.rig.tool." + candidate.name().toLowerCase(Locale.ROOT), new Object[0]);
         float w = font.width(text, 7.4F) + 30.0F;
         boolean selected = candidate == this.tool;
         this.pill(g, font, icons, x, y, w, h, candidate.icon, text, selected, mouseX, mouseY);
         this.buttons.add(new CharacterRigEditor.Button("tool:" + candidate.name(), x, y, w, h, () -> this.tool = candidate));
         x += w + 5.0F;
      }

      float right = this.width - 12;
      String close = I18n.get("rose.rig.close", new Object[0]);
      float closeW = font.width(close, 7.4F) + 30.0F;
      this.pill(g, font, icons, right - closeW, y, closeW, h, 57778, close, false, mouseX, mouseY);
      this.buttons.add(new CharacterRigEditor.Button("close", right - closeW, y, closeW, h, this::onClose));
      right -= closeW + 5.0F;
      String save = I18n.get("rose.rig.save", new Object[0]);
      float saveW = font.width(save, 7.4F) + 30.0F;
      this.pill(g, font, icons, right - saveW, y, saveW, h, 57677, save, this.dirty, mouseX, mouseY);
      this.buttons.add(new CharacterRigEditor.Button("save", right - saveW, y, saveW, h, this::save));
      right -= saveW + 5.0F;
      this.pill(g, font, icons, right - h, y, h, h, 58017, "", false, mouseX, mouseY);
      this.buttons.add(new CharacterRigEditor.Button("undo", right - h, y, h, h, this::undoLast));
      right -= h + 5.0F;
      this.pill(g, font, icons, right - h, y, h, h, 57672, "", false, mouseX, mouseY);
      this.buttons.add(new CharacterRigEditor.Button("reset", right - h, y, h, h, this::reset));
   }

   private void pill(
      GuiGraphics g, SdfFont font, SdfFont icons, float x, float y, float w, float h, int icon, String text, boolean accent, int mouseX, int mouseY
   ) {
      boolean hovered = mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
      Glass glass = Theme.glass();
      Corners corners = Corners.round(h / 2.0F);
      LiquidGlass.draw(g, x, y, w, h, corners, glass, 1.0F);
      if (accent) {
         Surfaces.fill(g, x, y, w, h, corners, Paint.solid(ColorUtil.withAlpha(Theme.accent(), 112)), 1.0F);
      }

      if (hovered) {
         Surfaces.fill(g, x, y, w, h, corners, Paint.solid(ColorUtil.withAlpha(glass.label, 28)), 1.0F);
      }

      int color = accent ? -1 : glass.label;
      if (text.isEmpty()) {
         icons.drawIcon(g, icon, x + w / 2.0F, y + h / 2.0F, 10.0F, color);
      } else {
         icons.drawIcon(g, icon, x + 13.0F, y + h / 2.0F, 9.0F, accent ? -1 : Theme.accent());
         font.draw(g, text, x + 22.0F, y + h / 2.0F - font.ascent(7.4F) + font.capHeight(7.4F) / 2.0F, 7.4F, color);
      }
   }

   private void drawRig(GuiGraphics g, int mouseX, int mouseY) {
      int accent = Theme.accent();
      int accent2 = Theme.accent2();
      float top = this.sy(Math.min(this.breathTop, this.breathBottom));
      float bottom = this.sy(Math.max(this.breathTop, this.breathBottom));
      Surfaces.fill(
         g,
         this.canvasX,
         top,
         this.imageW * this.canvasScale,
         bottom - top,
         Corners.NONE,
         Paint.solid(ColorUtil.withAlpha(accent2, this.tool == CharacterRigEditor.Tool.BREATH ? 48 : 20)),
         1.0F
      );
      this.dashed(g, this.canvasX, top, this.imageW * this.canvasScale, ColorUtil.withAlpha(accent2, 192));
      this.dashed(g, this.canvasX, bottom, this.imageW * this.canvasScale, ColorUtil.withAlpha(accent2, 192));

      for (float[] box : this.boxes) {
         this.outlineBox(g, box[0], box[1], box[2], box[3], -1056964609);
      }

      float hx = this.sx(this.headX);
      float hy = this.sy(this.headY);
      float r = this.headRadius * this.canvasScale;
      this.ring(g, hx, hy, r, ColorUtil.withAlpha(accent, this.tool == CharacterRigEditor.Tool.HEAD ? 255 : 176));
      Render2D.circle(g, hx, hy, 2.2F, accent);
      Render2D.circle(g, this.sx(this.pivotX), this.sy(this.pivotY), 3.2F, -1);
      Render2D.circle(g, this.sx(this.pivotX), this.sy(this.pivotY), 2.2F, accent2);

      for (float[] tail : this.tails) {
         this.tailMark(g, tail[0], tail[1], tail[2], tail[3], tail[4], tail[5], accent);
      }

      for (float[] eye : this.eyes) {
         this.outlineBox(g, eye[0] - eye[2] / 2.0F, eye[1] - eye[3] / 2.0F, eye[0] + eye[2] / 2.0F, eye[1] + eye[3] / 2.0F, accent2);
      }

      if (this.dragging) {
         switch (this.tool) {
            case TAIL:
               float[] tail = this.tailFromDrag();
               this.tailMark(g, tail[0], tail[1], tail[2], tail[3], tail[4], tail[5], -1);
               break;
            case EYE:
            case EXCLUDE:
               this.outlineBox(g, this.dragX, this.dragY, this.currentX, this.currentY, -1);
            case HAIR:
         }
      }

      if (this.hairSet) {
         float size = 10.0F;
         float x = this.canvasX + 6.0F;
         float y = this.canvasY + 6.0F;
         Surfaces.fill(g, x, y, size, size, Corners.round(size / 2.0F), Paint.solid(this.hairColor | 0xFF000000), 1.0F);
         Surfaces.border(g, x, y, size, size, Corners.round(size / 2.0F), 1.0F, -1, 1.0F);
      }

      if (this.overCanvas(mouseX, mouseY)) {
         Render2D.rect(g, mouseX - 5, mouseY - 0.5F, 10.0F, 1.0F, 0.0F, -1325400065);
         Render2D.rect(g, mouseX - 0.5F, mouseY - 5, 1.0F, 10.0F, 0.0F, -1325400065);
      }
   }

   private void tailMark(GuiGraphics g, float rootX, float rootY, float tipX, float tipY, float minX, float maxX, int color) {
      Surfaces.fill(
         g,
         this.sx(minX),
         this.sy(Math.min(rootY, tipY)),
         (maxX - minX) * this.canvasScale,
         Math.abs(tipY - rootY) * this.canvasScale,
         Corners.themed(4.0F),
         Paint.solid(ColorUtil.withAlpha(color, 28)),
         1.0F
      );
      this.dots(g, this.sx(rootX), this.sy(rootY), this.sx(tipX), this.sy(tipY), color);
      Render2D.circle(g, this.sx(rootX), this.sy(rootY), 3.0F, color);
      Render2D.circle(g, this.sx(tipX), this.sy(tipY), 2.2F, ColorUtil.withAlpha(color, 192));
   }

   private void ring(GuiGraphics g, float cx, float cy, float r, int color) {
      Surfaces.border(g, cx - r, cy - r, r * 2.0F, r * 2.0F, Corners.round(r), 1.2F, color, 1.0F);
   }

   private void outlineBox(GuiGraphics g, float x0, float y0, float x1, float y1, int color) {
      float ax = this.sx(Math.min(x0, x1));
      float ay = this.sy(Math.min(y0, y1));
      float w = Math.abs(x1 - x0) * this.canvasScale;
      float h = Math.abs(y1 - y0) * this.canvasScale;
      Surfaces.border(g, ax, ay, w, h, Corners.themed(3.0F), 1.0F, color, 1.0F);
   }

   private void dashed(GuiGraphics g, float x, float y, float w, int color) {
      for (float d = 0.0F; d < w; d += 8.0F) {
         Render2D.rect(g, x + d, y - 0.5F, Math.min(4.0F, w - d), 1.0F, 0.0F, color);
      }
   }

   private void dots(GuiGraphics g, float x0, float y0, float x1, float y1, int color) {
      float length = (float)Math.hypot(x1 - x0, y1 - y0);
      int count = Math.max(1, (int)(length / 4.0F));

      for (int i = 0; i <= count; i++) {
         float t = (float)i / count;
         Render2D.circle(g, x0 + (x1 - x0) * t, y0 + (y1 - y0) * t, 0.9F, color);
      }
   }

   private void preview(GuiGraphics g, float x, float y, float w, float h, int mouseX, int mouseY) {
      if (this.previewRig != null) {
         CharacterModel model = this.previewRig.model();
         float scale = Math.min((w - 20.0F) / model.width(), (h - 16.0F) / model.height());
         float drawX = x + (w - model.width() * scale) / 2.0F;
         float drawY = y + h - 8.0F - model.height() * scale;
         this.previewRig.place(drawX, drawY, scale);
         boolean inside = mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
         this.previewRig.update(inside ? mouseX : Float.NaN, inside ? mouseY : Float.NaN);
         this.previewRig.draw(g, 1.0F, 1426063360, 4.0F, 3.0F);
      }
   }

   private void rebuildPreview() {
      this.previewStale = false;
      CharacterModel model = CharacterModel.file(this.png, this.toJson(), "editor/");
      if (model != null) {
         this.previewModel = model;
         this.previewRig = new CharacterRig(model);
      }
   }

   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();

      for (CharacterRigEditor.Button candidate : this.buttons) {
         if (candidate.contains(mouseX, mouseY)) {
            candidate.action.run();
            UiSounds.play(UiSounds.Sound.CLICK);
            return true;
         }
      }

      if (!this.broken && this.overCanvas(mouseX, mouseY)) {
         float ix = this.ix(mouseX);
         float iy = this.iy(mouseY);
         if (button == 1) {
            this.removeNear(ix, iy);
            return true;
         }

         if (button != 0) {
            return false;
         }

         this.dragging = true;
         this.dragX = this.currentX = ix;
         this.dragY = this.currentY = iy;
         switch (this.tool) {
            case HEAD:
               this.headX = ix;
               this.headY = iy;
               if (!this.pivotSet) {
                  this.pivotX = this.headX;
                  this.pivotY = this.headY + this.headRadius;
               }

               this.changed();
               break;
            case NECK:
               this.pivotX = ix;
               this.pivotY = iy;
               this.pivotSet = true;
               this.changed();
               break;
            case BREATH:
               this.breathTop = this.breathBottom = iy;
               this.changed();
            case TAIL:
            case EYE:
            default:
               break;
            case HAIR:
               this.sampleHair(ix, iy);
               this.dragging = false;
         }

         return true;
      } else {
         return super.mouseClicked(event, doubleClick);
      }
   }

   public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();
      if (!this.dragging) {
         return super.mouseDragged(event, dragX, dragY);
      }

      this.currentX = Math.clamp(this.ix(mouseX), 0.0F, this.imageW);
      this.currentY = Math.clamp(this.iy(mouseY), 0.0F, this.imageH);
      switch (this.tool) {
         case HEAD:
            this.headRadius = Math.max(6.0F, (float)Math.hypot(this.currentX - this.headX, this.currentY - this.headY));
            if (!this.pivotSet) {
               this.pivotX = this.headX;
               this.pivotY = this.headY + this.headRadius;
            }

            this.changed();
            break;
         case NECK:
            this.pivotX = this.currentX;
            this.pivotY = this.currentY;
            this.changed();
            break;
         case BREATH:
            this.breathBottom = this.currentY;
            this.changed();
      }

      return true;
   }

   public boolean mouseReleased(MouseButtonEvent event) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();
      if (!this.dragging) {
         return super.mouseReleased(event);
      }

      this.dragging = false;
      switch (this.tool) {
         case TAIL:
            float[] tail = this.tailFromDrag();
            if (Math.hypot(tail[2] - tail[0], tail[3] - tail[1]) > this.imageH * 0.03F) {
               this.tails.add(tail);
               this.changed();
            }
            break;
         case EYE:
            float w = Math.abs(this.currentX - this.dragX);
            float h = Math.abs(this.currentY - this.dragY);
            if (w > 2.0F && h > 1.0F) {
               float cx = (this.dragX + this.currentX) / 2.0F;
               float cy = (this.dragY + this.currentY) / 2.0F;
               this.eyes.add(new float[]{cx, cy, w, h});
               this.skinSample = new int[]{Math.round(cx), Math.round(Math.min(this.imageH - 1, cy + h * 1.2F))};
               this.changed();
            }
            break;
         case HAIR:
         default:
            this.changed();
            break;
         case EXCLUDE:
            if (Math.abs(this.currentX - this.dragX) > 4.0F && Math.abs(this.currentY - this.dragY) > 4.0F) {
               this.boxes.add(new float[]{this.dragX, this.dragY, this.currentX, this.currentY});
               this.changed();
            }
      }

      return true;
   }

   private float[] tailFromDrag() {
      float spread = this.imageW * 0.07F;
      return new float[]{
         this.dragX,
         this.dragY,
         this.currentX,
         this.currentY,
         Math.max(0.0F, Math.min(this.dragX, this.currentX) - spread),
         Math.min(this.imageW, Math.max(this.dragX, this.currentX) + spread)
      };
   }

   private void removeNear(float ix, float iy) {
      float threshold = 14.0F / this.canvasScale;
      int best = -1;
      float bestDistance = threshold;

      List<float[]> list = switch (this.tool) {
         case EYE -> this.eyes;
         case EXCLUDE -> this.boxes;
         default -> this.tails;
      };

      for (int i = 0; i < list.size(); i++) {
         float[] item = list.get(i);

         float distance = switch (this.tool) {
            case EYE -> (float)Math.hypot(ix - item[0], iy - item[1]);
            case EXCLUDE -> ix >= Math.min(item[0], item[2])
                  && ix <= Math.max(item[0], item[2])
                  && iy >= Math.min(item[1], item[3])
                  && iy <= Math.max(item[1], item[3])
               ? 0.0F
               : Float.MAX_VALUE;
            default -> segmentDistance(ix, iy, item[0], item[1], item[2], item[3]);
         };
         if (distance < bestDistance) {
            bestDistance = distance;
            best = i;
         }
      }

      if (best >= 0) {
         list.remove(best);
         this.changed();
         UiSounds.play(UiSounds.Sound.TOGGLE_OFF);
      }
   }

   private static float segmentDistance(float px, float py, float x0, float y0, float x1, float y1) {
      float dx = x1 - x0;
      float dy = y1 - y0;
      float length = dx * dx + dy * dy;
      float t = length == 0.0F ? 0.0F : Math.clamp(((px - x0) * dx + (py - y0) * dy) / length, 0.0F, 1.0F);
      return (float)Math.hypot(px - (x0 + dx * t), py - (y0 + dy * t));
   }

   private void sampleHair(float ix, float iy) {
      int x = Math.clamp(Math.round(ix), 0, this.imageW - 1);
      int y = Math.clamp(Math.round(iy), 0, this.imageH - 1);
      int color = this.pixels.argb()[y * this.imageW + x];
      if (ColorUtil.alpha(color) >= 128) {
         float[] hsb = ColorUtil.toHsb(color);
         float hue = hsb[0] * 360.0F;
         this.hueMin = Math.max(0.0F, hue - 18.0F);
         this.hueMax = Math.min(360.0F, hue + 18.0F);
         this.minSaturation = Math.max(0.08F, hsb[1] * 0.5F);
         this.minValue = Math.max(0.08F, hsb[2] * 0.5F);
         this.hairSet = true;
         this.hairColor = color;
         this.changed();
         UiSounds.play(UiSounds.Sound.TOGGLE_ON);
      }
   }

   private boolean overCanvas(double mouseX, double mouseY) {
      return mouseX >= this.canvasX
         && mouseY >= this.canvasY
         && mouseX < this.canvasX + this.imageW * this.canvasScale
         && mouseY < this.canvasY + this.imageH * this.canvasScale;
   }

   private float sx(float imageX) {
      return this.canvasX + imageX * this.canvasScale;
   }

   private float sy(float imageY) {
      return this.canvasY + imageY * this.canvasScale;
   }

   private float ix(double screenX) {
      return (float)((screenX - this.canvasX) / this.canvasScale);
   }

   private float iy(double screenY) {
      return (float)((screenY - this.canvasY) / this.canvasScale);
   }

   public boolean keyPressed(KeyEvent event) {
      int key = event.key();
      int scanCode = event.scancode();
      int modifiers = event.modifiers();
      if (key >= 49 && key <= 55) {
         this.tool = CharacterRigEditor.Tool.values()[key - 49];
         UiSounds.play(UiSounds.Sound.TAB);
         return true;
      } else if (key == 83 && Minecraft.getInstance().hasControlDown()) {
         this.save();
         return true;
      } else if (key == 90 && Minecraft.getInstance().hasControlDown()) {
         this.undoLast();
         return true;
      } else {
         return super.keyPressed(event);
      }
   }

   void testMark(CharacterRigEditor.Tool which, float x0, float y0, float x1, float y1) {
      this.tool = which;
      this.dragging = true;
      this.dragX = this.currentX = x0;
      this.dragY = this.currentY = y0;
      double screenX0 = this.sx(x0);
      double screenY0 = this.sy(y0);
      double screenX1 = this.sx(x1);
      double screenY1 = this.sy(y1);
      this.mouseClicked(new MouseButtonEvent(screenX0, screenY0, new MouseButtonInfo(0, 0)), false);
      this.mouseDragged(new MouseButtonEvent(screenX1, screenY1, new MouseButtonInfo(0, 0)), screenX1 - screenX0, screenY1 - screenY0);
      this.mouseReleased(new MouseButtonEvent(screenX1, screenY1, new MouseButtonInfo(0, 0)));
   }

   void testSave() {
      this.save();
   }

   public void onClose() {
      this.minecraft.setScreen(this.parent);
   }

   public void removed() {
      if (this.texture != null) {
         this.minecraft.getTextureManager().release(TEXTURE);
      }

      this.texture = null;
      this.previewModel = null;
      this.previewRig = null;
   }

   private static List<String> wrap(SdfFont font, String text, float size, float maxWidth) {
      List<String> lines = new ArrayList<>();
      StringBuilder line = new StringBuilder();

      for (String word : text.split(" ")) {
         String candidate = line.isEmpty() ? word : line + " " + word;
         if (!(font.width(candidate, size) <= maxWidth) && !line.isEmpty()) {
            lines.add(line.toString());
            line.setLength(0);
            line.append(word);
         } else {
            line.setLength(0);
            line.append(candidate);
         }
      }

      if (!line.isEmpty()) {
         lines.add(line.toString());
      }

      return lines;
   }

   private static float round(float value) {
      return Math.round(value * 10.0F) / 10.0F;
   }

   private static JsonObject object(JsonObject json, String key) {
      return json.has(key) && json.get(key).isJsonObject() ? json.getAsJsonObject(key) : new JsonObject();
   }

   private static JsonArray array(JsonObject json, String key) {
      return json.has(key) && json.get(key).isJsonArray() ? json.getAsJsonArray(key) : new JsonArray();
   }

   private static float number(JsonObject json, String key, float fallback) {
      return json.has(key) ? json.get(key).getAsFloat() : fallback;
   }

   private record Button(String id, float x, float y, float w, float h, Runnable action) {
      boolean contains(double px, double py) {
         return px >= this.x && py >= this.y && px < this.x + this.w && py < this.y + this.h;
      }
   }

   public enum Tool {
      HEAD(58472),
      NECK(58181),
      BREATH(57987),
      TAIL(58251),
      EYE(57530),
      HAIR(58087),
      EXCLUDE(58633);

      final int icon;

      Tool(int icon) {
         this.icon = icon;
      }
   }
}
