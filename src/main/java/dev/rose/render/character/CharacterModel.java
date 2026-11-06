package dev.rose.render.character;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import dev.rose.RoseClient;
import dev.rose.render.gpu.RoseTexture;
import dev.rose.util.ColorUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;


public final class CharacterModel {
   private static final int FIELD_CELL = 4;
   private static final int HIT_CELL = 3;
   final String name;
   final String credit;
   final int width;
   final int height;
   final RoseTexture texture;
   final float headX;
   final float headY;
   final float headRadius;
   final float pivotX;
   final float pivotY;
   final float breathTop;
   final float breathBottom;
   final float breathAmount;
   final float feetX;
   final float feetY;
   final List<CharacterModel.Tail> tails = new ArrayList<>();
   final List<CharacterModel.Box> exclude = new ArrayList<>();
   final List<CharacterModel.Eye> eyes = new ArrayList<>();
   final float eyeAngle;
   final int skinColor;
   final int lashColor;
   private final float[] hairField;
   private final int fieldWidth;
   private final int fieldHeight;
   private final boolean[] opaque;
   private final int hitWidth;
   private final int hitHeight;

   private CharacterModel(String id, NativeImage image, JsonObject rig) {
      this.width = image.getWidth();
      this.height = image.getHeight();
      this.name = string(rig, "name", id);
      this.credit = string(rig, "credit", "");
      JsonObject head = object(rig, "head");
      this.headX = number(head, "x", this.width * 0.5F);
      this.headY = number(head, "y", this.height * 0.12F);
      this.headRadius = number(head, "radius", this.width * 0.18F);
      this.pivotX = number(head, "pivotX", this.headX);
      this.pivotY = number(head, "pivotY", this.headY + this.headRadius);
      JsonObject breath = object(rig, "breath");
      this.breathTop = number(breath, "top", this.height * 0.18F);
      this.breathBottom = number(breath, "bottom", this.height * 0.45F);
      this.breathAmount = number(breath, "amount", this.height * 0.0018F);
      JsonObject feet = object(rig, "feet");
      this.feetX = number(feet, "x", this.width * 0.5F);
      this.feetY = number(feet, "y", this.height);

      for (JsonElement element : array(rig, "tails")) {
         JsonObject tail = element.getAsJsonObject();
         this.tails
            .add(
               new CharacterModel.Tail(
                  number(tail, "rootX", 0.0F),
                  number(tail, "rootY", 0.0F),
                  number(tail, "tipX", 0.0F),
                  number(tail, "tipY", this.height),
                  number(tail, "minX", 0.0F),
                  number(tail, "maxX", this.width)
               )
            );
      }

      for (JsonElement element : array(rig, "exclude")) {
         JsonObject box = element.getAsJsonObject();
         this.exclude.add(new CharacterModel.Box(number(box, "x0", 0.0F), number(box, "y0", 0.0F), number(box, "x1", 0.0F), number(box, "y1", 0.0F)));
      }

      for (JsonElement element : array(rig, "eyes")) {
         JsonObject eye = element.getAsJsonObject();
         this.eyes.add(new CharacterModel.Eye(number(eye, "x", 0.0F), number(eye, "y", 0.0F), number(eye, "w", 0.0F), number(eye, "h", 0.0F)));
      }

      this.eyeAngle = (float)Math.toRadians(number(rig, "eyeAngle", 0.0F));
      JsonArray skin = rig.has("skinSample") ? rig.getAsJsonArray("skinSample") : null;
      this.skinColor = skin == null ? -661798 : pixel(image, skin.get(0).getAsInt(), skin.get(1).getAsInt()) | 0xFF000000;
      this.lashColor = parseColor(string(rig, "lashColor", "#202020"));
      JsonObject hair = object(rig, "hair");
      this.fieldWidth = this.width / 4 + 1;
      this.fieldHeight = this.height / 4 + 1;
      this.hairField = this.tails.isEmpty()
         ? new float[0]
         : this.hairMap(image, number(hair, "hueMin", 0.0F), number(hair, "hueMax", 0.0F), number(hair, "minSaturation", 0.2F), number(hair, "minValue", 0.2F));
      this.hitWidth = this.width / 3 + 1;
      this.hitHeight = this.height / 3 + 1;
      this.opaque = new boolean[this.hitWidth * this.hitHeight];

      for (int y = 0; y < this.height; y++) {
         for (int x = 0; x < this.width; x++) {
            if (ColorUtil.alpha(pixel(image, x, y)) > 128) {
               this.opaque[y / 3 * this.hitWidth + x / 3] = true;
            }
         }
      }

      this.bleedEdges(image);
      this.texture = new RoseTexture("character " + id, image).filter(true, false);
      Minecraft.getInstance().getTextureManager().register(Identifier.fromNamespaceAndPath("rose", "character/" + id), this.texture);
   }

   public static CharacterModel file(Path png) {
      return file(png, readRig(png), "custom/");
   }

   public static CharacterModel file(Path png, JsonObject rig, String idPrefix) {
      String fileName = png.getFileName().toString();

      try (InputStream in = Files.newInputStream(png)) {
         String id = fileName.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_.-]", "_");
         JsonObject copy = rig == null ? new JsonObject() : rig.deepCopy();
         if (!copy.has("name")) {
            copy.addProperty("name", fileName);
         }

         return new CharacterModel(idPrefix + id, NativeImage.read(NativeImage.Format.RGBA, in), copy);
      } catch (IOException | RuntimeException e) {
         RoseClient.LOGGER.error("[Rose] custom character {} failed to load", png, e);
         return null;
      }
   }

   public static Path rigFile(Path png) {
      String fileName = png.getFileName().toString();
      int dot = fileName.lastIndexOf(46);
      return png.resolveSibling((dot > 0 ? fileName.substring(0, dot) : fileName) + ".json");
   }

   public static JsonObject readRig(Path png) {
      Path rigFile = rigFile(png);
      if (!Files.isRegularFile(rigFile)) {
         return new JsonObject();
      }

      try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(rigFile), StandardCharsets.UTF_8)) {
         JsonElement root = JsonParser.parseReader(reader);
         return root.isJsonObject() ? root.getAsJsonObject() : new JsonObject();
      } catch (IOException | RuntimeException e) {
         RoseClient.LOGGER.warn("[Rose] cannot read rig {}", rigFile, e);
         return new JsonObject();
      }
   }

   public String name() {
      return this.name;
   }

   public String credit() {
      return this.credit;
   }

   public int width() {
      return this.width;
   }

   public int height() {
      return this.height;
   }

   float hair(float x, float y) {
      if (this.hairField.length == 0) {
         return 0.0F;
      }

      float fx = Math.clamp(x / 4.0F, 0.0F, this.fieldWidth - 1.001F);
      float fy = Math.clamp(y / 4.0F, 0.0F, this.fieldHeight - 1.001F);
      int x0 = (int)fx;
      int y0 = (int)fy;
      float tx = fx - x0;
      float ty = fy - y0;
      float top = this.hairField[y0 * this.fieldWidth + x0] * (1.0F - tx) + this.hairField[y0 * this.fieldWidth + x0 + 1] * tx;
      float bottom = this.hairField[(y0 + 1) * this.fieldWidth + x0] * (1.0F - tx) + this.hairField[(y0 + 1) * this.fieldWidth + x0 + 1] * tx;
      return top * (1.0F - ty) + bottom * ty;
   }

   boolean opaqueAt(float x, float y) {
      return !(x < 0.0F) && !(y < 0.0F) && !(x >= this.width) && !(y >= this.height) ? this.opaque[(int)y / 3 * this.hitWidth + (int)x / 3] : false;
   }

   private float[] hairMap(NativeImage image, float hueMin, float hueMax, float minSaturation, float minValue) {
      float[] field = new float[this.fieldWidth * this.fieldHeight];
      int[] counts = new int[field.length];

      for (int y = 0; y < this.height; y++) {
         for (int x = 0; x < this.width; x++) {
            int color = pixel(image, x, y);
            int cell = y / 4 * this.fieldWidth + x / 4;
            counts[cell]++;
            if (ColorUtil.alpha(color) >= 128) {
               float[] hsb = ColorUtil.toHsb(color);
               float hue = hsb[0] * 360.0F;
               if (hue >= hueMin && hue <= hueMax && hsb[1] >= minSaturation && hsb[2] >= minValue) {
                  field[cell]++;
               }
            }
         }
      }

      for (int i = 0; i < field.length; i++) {
         field[i] = counts[i] == 0 ? 0.0F : field[i] / counts[i];
      }

      return this.blur(this.blur(field, 3), 2);
   }

   private float[] blur(float[] source, int radius) {
      float[] rows = new float[source.length];

      for (int y = 0; y < this.fieldHeight; y++) {
         for (int x = 0; x < this.fieldWidth; x++) {
            float sum = 0.0F;
            int n = 0;

            for (int k = -radius; k <= radius; k++) {
               int xx = x + k;
               if (xx >= 0 && xx < this.fieldWidth) {
                  sum += source[y * this.fieldWidth + xx];
                  n++;
               }
            }

            rows[y * this.fieldWidth + x] = sum / n;
         }
      }

      float[] result = new float[source.length];

      for (int x = 0; x < this.fieldWidth; x++) {
         for (int y = 0; y < this.fieldHeight; y++) {
            float sum = 0.0F;
            int n = 0;

            for (int k = -radius; k <= radius; k++) {
               int yy = y + k;
               if (yy >= 0 && yy < this.fieldHeight) {
                  sum += rows[yy * this.fieldWidth + x];
                  n++;
               }
            }

            result[y * this.fieldWidth + x] = Math.min(1.0F, sum / n * 1.6F);
         }
      }

      return result;
   }

   private void bleedEdges(NativeImage image) {
      for (int pass = 0; pass < 3; pass++) {
         int[] updates = new int[this.width * this.height];
         boolean[] changed = new boolean[this.width * this.height];

         for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
               int self = pixel(image, x, y);
               if (ColorUtil.alpha(self) <= 8) {
                  int r = 0;
                  int g = 0;
                  int b = 0;
                  int n = 0;

                  for (int dy = -1; dy <= 1; dy++) {
                     for (int dx = -1; dx <= 1; dx++) {
                        int xx = x + dx;
                        int yy = y + dy;
                        if ((dx != 0 || dy != 0) && xx >= 0 && yy >= 0 && xx < this.width && yy < this.height) {
                           int neighbour = pixel(image, xx, yy);
                           if (ColorUtil.alpha(neighbour) > 8) {
                              r += ColorUtil.red(neighbour);
                              g += ColorUtil.green(neighbour);
                              b += ColorUtil.blue(neighbour);
                              n++;
                           }
                        }
                     }
                  }

                  if (n != 0) {
                     updates[y * this.width + x] = ColorUtil.argb(pass == 2 ? 0 : 9, r / n, g / n, b / n);
                     changed[y * this.width + x] = true;
                  }
               }
            }
         }

         for (int i = 0; i < changed.length; i++) {
            if (changed[i]) {
               setPixel(image, i % this.width, i / this.width, updates[i]);
            }
         }
      }

      for (int y = 0; y < this.height; y++) {
         for (int x = 0; x < this.width; x++) {
            int color = pixel(image, x, y);
            if (ColorUtil.alpha(color) == 9) {
               setPixel(image, x, y, color & 16777215);
            }
         }
      }
   }

   private static int pixel(NativeImage image, int x, int y) {
      return image.getPixel(x, y);
   }

   private static void setPixel(NativeImage image, int x, int y, int argb) {
      image.setPixel(x, y, argb);
   }

   private static int parseColor(String hex) {
      try {
         return 0xFF000000 | Integer.parseInt(hex.replace("#", ""), 16);
      } catch (NumberFormatException e) {
         return -14671840;
      }
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

   private static String string(JsonObject json, String key, String fallback) {
      return json.has(key) ? json.get(key).getAsString() : fallback;
   }

   public record Box(float x0, float y0, float x1, float y1) {
   }

   public record Eye(float x, float y, float w, float h) {
   }

   public record Tail(float rootX, float rootY, float tipX, float tipY, float minX, float maxX) {
   }
}
