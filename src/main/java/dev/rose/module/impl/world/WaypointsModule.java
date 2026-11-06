package dev.rose.module.impl.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.rose.RoseClient;
import dev.rose.module.Category;
import dev.rose.module.impl.cosmetic.CosmeticModule;
import dev.rose.notify.Notifications;
import dev.rose.render.RoseRenderTypes;
import dev.rose.render.Surfaces;
import dev.rose.render.Theme;
import dev.rose.render.font.FontWeight;
import dev.rose.render.font.Fonts;
import dev.rose.render.font.SdfFont;
import dev.rose.render.gpu.GuiDraw;
import dev.rose.render.style.Corners;
import dev.rose.render.style.Paint;
import dev.rose.render.world.CosmeticContext;
import dev.rose.render.world.CosmeticTextures;
import dev.rose.render.world.Geometry;
import dev.rose.render.world.WorldProjection;
import dev.rose.server.KnownServers;
import dev.rose.setting.ActionSetting;
import dev.rose.setting.BoolSetting;
import dev.rose.setting.KeySetting;
import dev.rose.setting.NumberSetting;
import dev.rose.util.ColorUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;


public final class WaypointsModule extends CosmeticModule {
   private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("rose").resolve("waypoints.json");
   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   private static final int DEATH_COLOR = -47814;
   private final BoolSetting deathPoint = this.add(new BoolSetting("death_point", true));
   private final KeySetting addKey = this.add(new KeySetting("add_key", 66));
   private final KeySetting removeKey = this.add(new KeySetting("remove_key", -1));
   private final ActionSetting clear = this.add(new ActionSetting("clear", 57742, this::clearHere));
   private final BoolSetting beam = this.add(new BoolSetting("beam", true));
   private final BoolSetting labels = this.add(new BoolSetting("labels", true));
   private final NumberSetting maxDistance = this.add(new NumberSetting("max_distance", 0.0, 0.0, 5000.0, 50.0, "rose.unit.blocks"));
   private JsonObject root;
   private String loadedKey;
   private final List<WaypointsModule.Waypoint> current = new ArrayList<>();
   private boolean wasDead;
   private final List<WaypointsModule.Label> labelPool = new ArrayList<>();
   private final List<WaypointsModule.Label> shownLabels = new ArrayList<>();
   private float[] placed = new float[32];
   private int placedCount;
   private final Matrix4f viewProjection = new Matrix4f();
   private final Vector4f clip = new Vector4f();
   private float labelsTime = -1.0F;
   private float labelsDrawn;

   public WaypointsModule() {
      super("waypoints", Category.WORLD, true);
   }

   @Override
   public int icon() {
      return 57617;
   }

   @Override
   public boolean hasKeybind() {
      return false;
   }

   private static String placeKey() {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) {
         return null;
      }

      String place = mc.getSingleplayerServer() != null ? "world:" + mc.getSingleplayerServer().getWorldData().getLevelName() : "server:" + KnownServers.currentHost();
      return place + "|" + mc.level.dimension().identifier();
   }

   private void ensureLoaded() {
      String key = placeKey();
      if (key != null && !key.equals(this.loadedKey)) {
         if (this.root == null) {
            this.root = new JsonObject();
            if (Files.exists(FILE)) {
               try {
                  JsonElement parsed = JsonParser.parseString(Files.readString(FILE, StandardCharsets.UTF_8));
                  if (parsed.isJsonObject()) {
                     this.root = parsed.getAsJsonObject();
                  }
               } catch (IOException | RuntimeException e) {
                  RoseClient.LOGGER.warn("[Rose] cannot read waypoints", e);
               }
            }
         }

         this.current.clear();
         this.loadedKey = key;
         if (this.root.has(key) && this.root.get(key).isJsonArray()) {
            for (JsonElement element : this.root.getAsJsonArray(key)) {
               if (element.isJsonObject()) {
                  JsonObject object = element.getAsJsonObject();

                  try {
                     WaypointsModule.Waypoint waypoint = new WaypointsModule.Waypoint();
                     waypoint.name = object.get("name").getAsString();
                     waypoint.x = object.get("x").getAsDouble();
                     waypoint.y = object.get("y").getAsDouble();
                     waypoint.z = object.get("z").getAsDouble();
                     waypoint.death = object.has("death") && object.get("death").getAsBoolean();
                     this.current.add(waypoint);
                  } catch (RuntimeException var7) {
                  }
               }
            }
         }
      }
   }

   private void store() {
      if (this.loadedKey != null && this.root != null) {
         JsonArray array = new JsonArray();

         for (WaypointsModule.Waypoint waypoint : this.current) {
            JsonObject object = new JsonObject();
            object.addProperty("name", waypoint.name);
            object.addProperty("x", waypoint.x);
            object.addProperty("y", waypoint.y);
            object.addProperty("z", waypoint.z);
            if (waypoint.death) {
               object.addProperty("death", true);
            }

            array.add(object);
         }

         this.root.add(this.loadedKey, array);

         try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(this.root), StandardCharsets.UTF_8);
         } catch (IOException e) {
            RoseClient.LOGGER.warn("[Rose] cannot save waypoints", e);
         }
      }
   }

   @Override
   public void onTick() {
      LocalPlayer player = Minecraft.getInstance().player;
      if (player != null) {
         this.ensureLoaded();
         boolean dead = player.isDeadOrDying();
         if (dead && !this.wasDead && this.deathPoint.isOn()) {
            this.current.removeIf(waypoint -> waypoint.death);
            this.add(I18n.get("rose.waypoints.death", new Object[0]), player.getX(), player.getY(), player.getZ(), true);
         }

         this.wasDead = dead;
         if (!dead && this.current.removeIf(waypoint -> waypoint.death && player.distanceToSqr(waypoint.x, waypoint.y, waypoint.z) < 9.0)) {
            this.store();
         }
      }
   }

   @Override
   public void onRawKey(int key) {
      Minecraft mc = Minecraft.getInstance();
      if (key != -1 && mc.player != null && mc.screen == null) {
         this.ensureLoaded();
         if (key == this.addKey.get()) {
            int number = 1;

            for (WaypointsModule.Waypoint waypoint : this.current) {
               if (!waypoint.death) {
                  number++;
               }
            }

            String name = I18n.get("rose.waypoints.name", new Object[]{number});
            this.add(name, Math.floor(mc.player.getX()) + 0.5, mc.player.getY(), Math.floor(mc.player.getZ()) + 0.5, false);
            Notifications.show(
               Notifications.Kind.SUCCESS,
               I18n.get("rose.waypoints.added", new Object[]{name}),
               I18n.get("rose.waypoints.added.hint", new Object[0])
            );
         } else if (key == this.removeKey.get()) {
            WaypointsModule.Waypoint target = this.lookedAt(mc);
            if (target != null) {
               this.current.remove(target);
               this.store();
               Notifications.show(Notifications.Kind.NEUTRAL, I18n.get("rose.waypoints.removed", new Object[]{target.name}), "");
            }
         }
      }
   }

   public void mark(String name, double x, double y, double z, boolean death) {
      this.ensureLoaded();
      this.add(name, x, y, z, death);
   }

   public void clearAll() {
      this.clearHere();
   }

   private void add(String name, double x, double y, double z, boolean death) {
      WaypointsModule.Waypoint waypoint = new WaypointsModule.Waypoint();
      waypoint.name = name;
      waypoint.x = x;
      waypoint.y = y;
      waypoint.z = z;
      waypoint.death = death;
      this.current.add(waypoint);
      this.store();
   }

   private WaypointsModule.Waypoint lookedAt(Minecraft mc) {
      WaypointsModule.Waypoint best = null;
      double bestDot = 0.995;

      for (WaypointsModule.Waypoint waypoint : this.current) {
         Vec3 to = new Vec3(waypoint.x, waypoint.y + 1.0, waypoint.z).subtract(mc.player.getEyePosition());
         if (to.lengthSqr() < 9.0) {
            return waypoint;
         }

         double dot = to.normalize().dot(mc.player.getLookAngle());
         if (dot > bestDot) {
            bestDot = dot;
            best = waypoint;
         }
      }

      return best;
   }

   private void clearHere() {
      this.ensureLoaded();
      this.current.clear();
      this.store();
   }

   public boolean active() {
      return !this.current.isEmpty();
   }

   public void render(CosmeticContext ctx) {
      if (!this.current.isEmpty()) {
         Minecraft mc = Minecraft.getInstance();
         float time = ctx.time();
         float limit = this.maxDistance.floatValue();
         PoseStack pose = ctx.pose();
         List<float[]> visible = new ArrayList<>();

         for (int i = 0; i < this.current.size(); i++) {
            WaypointsModule.Waypoint waypoint = this.current.get(i);
            Vector3f local = ctx.local(waypoint.x, waypoint.y, waypoint.z);
            float distance = local.length();
            if (!(limit > 0.0F) || !(distance > limit)) {
               float shown = Math.min(distance, 48.0F);
               if (distance > 0.001F) {
                  local.mul(shown / distance);
               }

               visible.add(new float[]{local.x, local.y, local.z, distance, i, shown / Math.max(distance, 0.001F)});
            }
         }

         if (!visible.isEmpty()) {
            if (this.beam.isOn()) {
               VertexConsumer glow = ctx.buffers().getBuffer(RoseRenderTypes.GLOW_THROUGH);

               for (float[] entry : visible) {
                  WaypointsModule.Waypoint waypoint = this.current.get((int)entry[4]);
                  float scale = entry[5];
                  float near = Math.clamp((entry[3] - 2.0F) / 4.0F, 0.0F, 1.0F);
                  int base = waypoint.death ? -47814 : this.rgb(entry[4] * 0.17F, time);
                  float alpha = (0.45F + 0.15F * Mth.sin(time * 2.2F + entry[4])) * near * this.opacity();
                  int bright = ColorUtil.withAlpha(base, Math.round(255.0F * alpha));
                  int clear = base & 16777215;
                  Vector3f bottom = new Vector3f(entry[0], entry[1] - 1.5F * scale, entry[2]);
                  Vector3f middle = new Vector3f(entry[0], entry[1] + 6.0F * scale, entry[2]);
                  Vector3f top = new Vector3f(entry[0], entry[1] + 90.0F * scale, entry[2]);
                  float width = Math.max(0.12F, 0.22F * scale * Math.max(1.0F, entry[3] / 24.0F));
                  Geometry.beam(glow, pose.last(), new Vector3f(), bottom, middle, width, width, bright, bright);
                  Geometry.beam(glow, pose.last(), new Vector3f(), middle, top, width, width * 0.6F, bright, clear);
               }

               ctx.buffers().endBatch(RoseRenderTypes.GLOW_THROUGH);
            }

            VertexConsumer dots = ctx.buffers().getBuffer(RoseRenderTypes.glowSpriteThrough(CosmeticTextures.ring()));

            for (float[] entry : visible) {
               WaypointsModule.Waypoint waypoint = this.current.get((int)entry[4]);
               float size = 0.4F * Math.max(entry[5], 0.35F) * Math.max(1.0F, entry[3] / 14.0F) * entry[5] + 0.12F;
               int base = waypoint.death ? -47814 : this.rgb(entry[4] * 0.17F, time);
               Geometry.sprite(
                  dots,
                  pose.last(),
                  new Vector3f(entry[0], entry[1] + 1.2F * entry[5], entry[2]),
                  ctx.right(),
                  ctx.up(),
                  size,
                  size,
                  time * 0.8F,
                  ColorUtil.withAlpha(base, Math.round(230.0F * this.opacity()))
               );
            }

            ctx.buffers().endBatch();
            this.shownLabels.clear();
            this.labelsTime = time;
            if (this.labels.isOn()) {
               WorldProjection.viewProjection(this.viewProjection);
               float guiWidth = mc.getWindow().getGuiScaledWidth();
               float guiHeight = mc.getWindow().getGuiScaledHeight();

               for (float[] entry : visible) {
                  if (!(entry[3] < 2.0F)) {
                     this.clip.set(entry[0], entry[1] + 1.2F * entry[5], entry[2], 1.0F).mul(this.viewProjection);
                     if (!(this.clip.w <= 0.05F)) {
                        WaypointsModule.Waypoint waypoint = this.current.get((int)entry[4]);

                        while (this.labelPool.size() <= this.shownLabels.size()) {
                           this.labelPool.add(new WaypointsModule.Label());
                        }

                        WaypointsModule.Label label = this.labelPool.get(this.shownLabels.size());
                        label.waypoint = waypoint;
                        label.x = (this.clip.x / this.clip.w * 0.5F + 0.5F) * guiWidth;
                        label.y = (0.5F - this.clip.y / this.clip.w * 0.5F) * guiHeight;
                        label.distance = entry[3];
                        label.color = (waypoint.death ? -47814 : this.rgb(entry[4] * 0.17F, time)) | 0xFF000000;
                        this.shownLabels.add(label);
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void onHud(GuiGraphics g, DeltaTracker deltaTracker) {
      float now = Theme.time();
      if (!this.shownLabels.isEmpty() && !(now - this.labelsTime > 0.25F)) {
         SdfFont names = Fonts.get(FontWeight.MEDIUM);
         SdfFont numbers = Fonts.get(FontWeight.REGULAR);
         SdfFont icons = Fonts.icons();
         if (names != null && numbers != null && icons != null) {
            Minecraft mc = Minecraft.getInstance();
            float guiWidth = mc.getWindow().getGuiScaledWidth();
            float guiHeight = mc.getWindow().getGuiScaledHeight();
            float rate = 1.0F - (float)Math.exp(-Math.min(0.1F, now - this.labelsDrawn) * 12.0F);
            this.labelsDrawn = now;

            for (WaypointsModule.Label label : this.shownLabels) {
               float fromCrosshair = (float)Math.hypot(label.x - guiWidth / 2.0F, label.y - guiHeight / 2.0F);
               float wanted = 1.0F - Mth.clamp((fromCrosshair - 36.0F) / 60.0F, 0.0F, 1.0F);
               label.waypoint.focus = label.waypoint.focus + (wanted * wanted * (3.0F - 2.0F * wanted) - label.waypoint.focus) * rate;
               label.order = fromCrosshair;
            }

            this.shownLabels.sort(Comparator.comparingDouble(labelx -> labelx.order));
            this.placedCount = 0;
            String unit = I18n.get("rose.unit.blocks", new Object[0]);
            float height = 13.0F;
            float pad = 5.5F;
            float iconSize = 7.2F;
            float nameSize = 7.4F;
            float numberSize = 6.8F;

            for (WaypointsModule.Label label : this.shownLabels) {
               float focus = label.waypoint.focus;
               int rounded = Math.round(label.distance);
               if (rounded != label.waypoint.distanceShown) {
                  label.waypoint.distanceShown = rounded;
                  label.waypoint.distanceText = (rounded < 1000 ? String.valueOf(rounded) : rounded / 1000 + "." + rounded % 1000 / 100 + "k") + unit;
               }

               String number = label.waypoint.distanceText;
               float numberWidth = numbers.width(number, numberSize);
               float nameWidth = names.width(label.waypoint.name, nameSize);
               float compact = pad + iconSize + 3.5F + numberWidth + pad;
               float width = compact + (nameWidth + 5.0F) * focus;
               float x = Mth.clamp(label.x - width / 2.0F, 4.0F, Math.max(4.0F, guiWidth - width - 4.0F));
               float y = Mth.clamp(label.y - height - 7.0F, 4.0F, Math.max(4.0F, guiHeight - height - 4.0F));

               for (int tries = 0; tries < 8 && this.overlaps(x, y, width, height); tries++) {
                  y -= height + 2.0F;
               }

               if (!(y < 4.0F)) {
                  if (this.placedCount + 4 > this.placed.length) {
                     this.placed = Arrays.copyOf(this.placed, this.placed.length * 2);
                  }

                  this.placed[this.placedCount++] = x;
                  this.placed[this.placedCount++] = y;
                  this.placed[this.placedCount++] = width;
                  this.placed[this.placedCount++] = height;
                  float alpha = this.opacity() * Mth.clamp((label.distance - 2.0F) / 3.0F, 0.0F, 1.0F);
                  Corners pill = Corners.round(height / 2.0F);
                  Surfaces.fill(g, x, y, width, height, pill, Paint.solid(-1207169774), alpha);
                  Surfaces.border(g, x, y, width, height, pill, 0.6F, ColorUtil.withAlpha(label.color, Math.round(112.0F + 112.0F * focus)), alpha);
                  float middle = y + height / 2.0F;
                  float cursor = x + pad;
                  icons.drawIcon(
                     g,
                     label.waypoint.death ? 57889 : 57617,
                     cursor + iconSize / 2.0F,
                     middle,
                     iconSize,
                     ColorUtil.withAlpha(label.color, Math.round(255.0F * alpha))
                  );
                  cursor += iconSize + 3.5F;
                  if (focus > 0.02F) {
                     boolean cut = focus < 0.98F;
                     if (cut) {
                        GuiDraw.scissor(g, (int)cursor, (int)y, (int)(x + width - pad - numberWidth), (int)(y + height));
                     }

                     names.draw(
                        g,
                        label.waypoint.name,
                        cursor,
                        middle - names.lineHeight(nameSize) / 2.0F,
                        nameSize,
                        ColorUtil.withAlpha(-723464, Math.round(255.0F * alpha * focus))
                     );
                     if (cut) {
                        g.disableScissor();
                     }
                  }

                  numbers.draw(
                     g,
                     number,
                     x + width - pad - numberWidth,
                     middle - numbers.lineHeight(numberSize) / 2.0F,
                     numberSize,
                     ColorUtil.withAlpha(ColorUtil.lerp(-1644564, -5657416, focus), Math.round(255.0F * alpha))
                  );
               }
            }
         }
      }
   }

   private boolean overlaps(float x, float y, float width, float height) {
      for (int i = 0; i < this.placedCount; i += 4) {
         if (x < this.placed[i] + this.placed[i + 2] + 2.0F
            && x + width + 2.0F > this.placed[i]
            && y < this.placed[i + 1] + this.placed[i + 3]
            && y + height > this.placed[i + 1]) {
            return true;
         }
      }

      return false;
   }

   private static final class Label {
      WaypointsModule.Waypoint waypoint;
      float x;
      float y;
      float distance;
      float order;
      int color;
   }

   private static final class Waypoint {
      String name;
      double x;
      double y;
      double z;
      boolean death;
      float focus;
      String distanceText;
      int distanceShown = -1;
   }
}
