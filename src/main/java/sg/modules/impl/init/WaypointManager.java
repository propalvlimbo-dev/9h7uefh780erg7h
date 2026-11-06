package sg.modules.impl.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import sg.ec.UnhookState;
import sg.ec.MinecraftAccess;
import sg.events.EventListener;
import sg.ec.Waypoint;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.events.impl.HudRenderEvent;

public final class WaypointManager implements MinecraftAccess {
   private static final WaypointManager В = new WaypointManager();
   private final List<Waypoint> с3 = new ArrayList<>();
   private final Gson Я = new GsonBuilder().setPrettyPrinting().create();
   private final Path п;
   private final EventListener<HudRenderEvent> Як;
   private boolean Хг;
   private static final String Я必Ш = "rose";
   private static final String Я必ы = "waypoints.json";
   private static final String Я必й = "WAY";
   private static final float Я必Ж = 16.0F;
   private static final float Я必м = 12.0F;
   private static final float Я必К = 12.0F;
   private static final float Я必I = 16.0F;
   private static final float Я必Т = 32.0F;
   private static final float Я必О = 0.5F;
   private static final float Я必Ф = 0.5F;
   private static final float Я必Й = 10.0F;
   private static final float Я必к = 16.0F;
   private static final float Я必г = 11.0F;
   private static final float Я必0 = 6.0F;
   private static final float Я必х = 7.0F;
   private static final float Я必У = 9.0F;
   private static final double Я必З = 255.0;
   private static final float Я必Е = 12.0F;
   private static final float Я必П = 11.0F;
   private static final float Я必西 = 0.5F;
   private static final String Я必д = "name";
   private static final String Я必Б = "x";
   private static final String Я必С = "y";
   private static final String Я必з = "z";
   private static final String Я必ц = "name";
   private static final String Я必я = "x";
   private static final String Ящс = "y";
   private static final String ЯщВ = "z";

   public boolean method__(String var1, BlockPos var2) {
      if (this.method_7(var1).isPresent()) {
         return false;
      }

      this.с3.add(new Waypoint(var1, var2));
      this.р9();
      return true;
   }

   public void ру() {
      if (!this.Хг) {
         this.Хг = true;
         this.рс();
         RoseClient.getInstance().getEventBus().register(this);
      }
   }

   private void ш(HudRenderEvent var1) {
      if (!UnhookState.ые() && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !this.с3.isEmpty()) {
         for (Waypoint var3 : this.с3) {
            Vec3 var4 = Vec3.atCenterOf(var3.pos());
            Vector2f var5 = WorldProjection.projectToScreen(var4);
            if (var5 != null) {
               double var6 = MINECRAFT_CLIENT.player.position().distanceTo(var4);
               String var8 = Я必й;
               String var9 = var3.name();
               String var10 = Math.round(var6) + "м";
               float var11 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var8, 10);
               float var12 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var9, 13);
               float var13 = UiDrawing.getTextWidth(ClientFonts.getRegularFont(), var10, 11);
               float var14 = Я必Ж + var11 + Я必м + var12 + Я必К + var13 + Я必I;
               float var15 = Я必Т;
               float var16 = var5.x - var14 * Я必О;
               float var17 = var5.y - var15 * Я必Ф;
               UiDrawing.drawWindowBackground(var16, var17, var14, var15, Я必Й);
               float var18 = var16 + Я必к;
               UiDrawing.drawText(ClientFonts.getMediumFont(), var8, var18, var17 + Я必г, 11, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
               float var19 = var18 + var11 + Я必0;
               float var20 = var19 + Я必х;
               UiDrawing.drawText(ClientFonts.getMediumFont(), var9, var20, var17 + Я必У, 13, ColorMath.packArgb(235, 239, 247, Я必З));
               UiDrawing.drawText(ClientFonts.getRegularFont(), var10, var20 + var12 + Я必Е, var17 + Я必П, 11, ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), Я必西));
            }
         }
      }
   }

   public WaypointManager() {
      this.п = FabricLoader.getInstance().getConfigDir().resolve(Я必Ш).resolve(Я必ы);
      this.Як = this::ш;
   }

   public boolean З_bbeebd87(String var1) {
      boolean var2 = this.с3.removeIf(var1x -> var1x.name().equalsIgnoreCase(var1));
      if (var2) {
         this.р9();
      }

      return var2;
   }

   public void рс() {
      this.с3.clear();
      if (Files.exists(this.п)) {
         try {
            for (JsonElement var3 : JsonParser.parseString(Files.readString(this.п, StandardCharsets.UTF_8)).getAsJsonArray()) {
               JsonObject var4 = var3.getAsJsonObject();
               this.с3
                  .add(
                     new Waypoint(
                        var4.get(Я必ц).getAsString(), new BlockPos(var4.get(Я必я).getAsInt(), var4.get(Ящс).getAsInt(), var4.get(ЯщВ).getAsInt())
                     )
                  );
            }
         } catch (Exception var5) {
            this.с3.clear();
         }
      }
   }

   public List<Waypoint> О() {
      return List.copyOf(this.с3);
   }

   public Optional<Waypoint> method_7(String var1) {
      return this.с3.stream().filter(var1x -> var1x.name().equalsIgnoreCase(var1)).findFirst();
   }

   public int _Г() {
      int var1 = this.с3.size();
      this.с3.clear();
      this.р9();
      return var1;
   }

   public static WaypointManager method__() {
      return В;
   }

   public void р9() {
      try {
         Files.createDirectories(this.п.getParent());
         JsonArray var1 = new JsonArray();

         for (Waypoint var3 : this.с3) {
            JsonObject var4 = new JsonObject();
            var4.addProperty(Я必д, var3.name());
            var4.addProperty(Я必Б, var3.pos().getX());
            var4.addProperty(Я必С, var3.pos().getY());
            var4.addProperty(Я必з, var3.pos().getZ());
            var1.add(var4);
         }

         Files.writeString(this.п, this.Я.toJson(var1), StandardCharsets.UTF_8);
      } catch (Exception var5) {
      }
   }
}
