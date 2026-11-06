package sg.render;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public final class EmoteAnimationClip {
   private final Map<String, EmoteAnimationClip.BoneTracks> сЫ;
   private final float АТ;
   private final float АО;
   private final float АФ;
   private final boolean Яб;
   private static final float Эы = 0.05F;
   private static final String Эй = "rose";
   private static final String ЭЖ = "animations";
   private static final String Эм = "_bends";
   private static final String ЭК = "_overrides";
   private static final String ЭI = "animation_length";
   private static final String ЭТ = "animation_length";
   private static final String ЭО = "loop_start";
   private static final String ЭФ = "loop_start";
   private static final String ЭЙ = "loop_end";
   private static final String Эк = "loop_end";
   private static final String Эг = "loop_end";
   private static final String Э0 = "loop_end";
   private static final String Эх = "easing_before";
   private static final String ЭУ = "easing_before";
   private static final String ЭЗ = "easing_before";
   private static final String ЭЕ = "easing_before";
   private static final String ЭП = "bones";
   private static final String Э西 = "bones";
   private static final String Эд = "bones";
   private static final String ЭБ = "rotation";
   private static final String ЭС = "rotation";
   private static final String Эз = "rotation";
   private static final String Эц = "position";
   private static final String Эя = "position";
   private static final String 必с = "position";
   private static final String 必В = "bend_direction";
   private static final String 必Я = "bendDirection";
   private static final String 必Х = "bend";
   private static final float 必е = 0.05F;
   private static final float 必А = 0.05F;
   private static final String 必4 = "rose";
   private static final String 必п = "animations";
   private static final float 必Д = 1.0E-4F;
   private static final String 必Ъ = "linear";
   private static final String 必т = "vector";
   private static final String 必а = "easing";
   private static final String 必Ю = "easing";
   private static final String 必Ч = "linear";
   private static final String 必Л = "vector";
   private static final String 必诶 = "easing";
   private static final String 必1 = "linear";
   private static final String 必Ь = "vector";
   private static final String 必衣 = "easing";
   private static final String 必ш = "easing";
   private static final String 必И = "linear";
   private static final String 必ь = "value";
   private static final String 必ж = "easing";
   private static final String 必弟 = "easing";
   private static final String 必б = "linear";
   private static final String 必Ы = "value";
   private static final String 必l = "easing";
   private static final String 必8 = "linear";
   private static final String 必5 = "value";
   private static final String 必в = "easing";
   private static final String 必ъ = "easing";

   private EmoteAnimationClip(Map<String, EmoteAnimationClip.BoneTracks> var1, float var2, float var3, float var4, boolean var5) {
      this.сЫ = var1;
      this.АТ = Math.max(Эы, var2);
      this.АО = Math.clamp(var3, 0.0F, this.АТ);
      this.АФ = Math.max(this.АТ, var4);
      this.Яб = var5;
   }

   public static EmoteAnimationClip З(EmoteDefinition var0, String var1) {
      Identifier var2 = Identifier.fromNamespaceAndPath(Эй, "player_animation/" + var0.file() + ".json");

      try {
         BufferedReader var3 = Minecraft.getInstance().getResourceManager().getResource(var2).orElseThrow().openAsReader();

         try (var3) {
            JsonObject var5 = JsonParser.parseReader(var3).getAsJsonObject();
            JsonObject var6 = var5.getAsJsonObject(ЭЖ).getAsJsonObject(var1);
            JsonObject var7 = З(var0, var1, Эм);
            JsonObject var8 = З(var0, var1, ЭК);
            HashMap var9 = new HashMap();
            float var10 = var6.has(ЭI) ? var6.get(ЭТ).getAsFloat() : 0.0F;
            float var11 = var6.has(ЭО) ? var6.get(ЭФ).getAsFloat() : 0.0F;
            float var12 = var6.has(ЭЙ) ? var6.get(Эк).getAsFloat() : (var7 != null && var7.has(Эг) ? var7.get(Э0).getAsFloat() : var10);
            boolean var13 = var6.has(Эх) ? var6.get(ЭУ).getAsBoolean() : var7 == null || !var7.has(ЭЗ) || var7.get(ЭЕ).getAsBoolean();
            JsonObject var14 = var6.getAsJsonObject(ЭП);
            JsonObject var15 = var7 == null ? null : var7.getAsJsonObject(Э西);
            JsonObject var16 = var8 == null ? null : var8.getAsJsonObject(Эд);

            for (Entry var18 : var14.entrySet()) {
               JsonObject var19 = ((JsonElement)var18.getValue()).getAsJsonObject();
               JsonObject var20 = var15 != null && var15.has((String)var18.getKey()) ? var15.getAsJsonObject((String)var18.getKey()) : var19;
               JsonObject var21 = var16 != null && var16.has((String)var18.getKey()) ? var16.getAsJsonObject((String)var18.getKey()) : null;
               EmoteAnimationClip.VectorTrack var22 = З_c0631fce(var21 != null && var21.has(ЭБ) ? var21.get(ЭС) : var19.get(Эз));
               EmoteAnimationClip.VectorTrack var23 = З_c0631fce(var21 != null && var21.has(Эц) ? var21.get(Эя) : var19.get(必с));
               EmoteAnimationClip.ScalarTrack var24 = З_5dfd5aaa(З(var20, 必В, 必Я));
               EmoteAnimationClip.ScalarTrack var25 = З_5dfd5aaa(var20.get(必Х));
               var9.put((String)var18.getKey(), new EmoteAnimationClip.BoneTracks(var22, var23, var24, var25));
               var10 = Math.max(var10, Math.max(var22.end(), var23.end()));
            }

            return new EmoteAnimationClip(var9, var10, var11, var12, var13);
         }
      } catch (Exception var28) {
         return new EmoteAnimationClip(Map.of(), 必е, 0.0F, 必А, true);
      }
   }

   private static EmoteAnimationClip.VectorTrack З_c0631fce(JsonElement var0) {
      if (var0 != null && !var0.isJsonNull()) {
         if (var0.isJsonArray()) {
            return new EmoteAnimationClip.VectorTrack(List.of(new EmoteAnimationClip.VectorKeyframe(0.0F, З_f5cce25e(var0), 必Ъ)));
         }

         JsonObject var1 = var0.getAsJsonObject();
         if (var1.has(必т)) {
            String var9 = var1.has(必а) ? var1.get(必Ю).getAsString() : 必Ч;
            return new EmoteAnimationClip.VectorTrack(List.of(new EmoteAnimationClip.VectorKeyframe(0.0F, З_f5cce25e(var1.get(必Л)), var9)));
         }

         ArrayList var2 = new ArrayList();

         for (Entry var4 : var1.entrySet()) {
            if (!((String)var4.getKey()).equals(必诶)) {
               JsonElement var5 = (JsonElement)var4.getValue();
               String var7 = 必1;
               Vector3f var6;
               if (var5.isJsonObject()) {
                  JsonObject var8 = var5.getAsJsonObject();
                  var6 = З_f5cce25e(var8.get(必Ь));
                  if (var8.has(必衣)) {
                     var7 = var8.get(必ш).getAsString();
                  }
               } else {
                  var6 = З_f5cce25e(var5);
               }

               var2.add(new EmoteAnimationClip.VectorKeyframe(Float.parseFloat((String)var4.getKey()), var6, var7));
            }
         }

         var2.sort(Comparator.comparingDouble(EmoteAnimationClip.VectorKeyframe::time));
         return new EmoteAnimationClip.VectorTrack(var2);
      } else {
         return EmoteAnimationClip.VectorTrack.EMPTY;
      }
   }

   private static EmoteAnimationClip.ScalarTrack З_5dfd5aaa(JsonElement var0) {
      if (var0 != null && !var0.isJsonNull()) {
         if (var0.isJsonPrimitive()) {
            return new EmoteAnimationClip.ScalarTrack(List.of(new EmoteAnimationClip.ScalarKeyframe(0.0F, var0.getAsFloat(), 必И)));
         }

         JsonObject var1 = var0.getAsJsonObject();
         if (var1.has(必ь)) {
            String var9 = var1.has(必ж) ? var1.get(必弟).getAsString() : 必б;
            return new EmoteAnimationClip.ScalarTrack(List.of(new EmoteAnimationClip.ScalarKeyframe(0.0F, var1.get(必Ы).getAsFloat(), var9)));
         }

         ArrayList var2 = new ArrayList();

         for (Entry var4 : var1.entrySet()) {
            if (!((String)var4.getKey()).equals(必l)) {
               JsonElement var5 = (JsonElement)var4.getValue();
               String var7 = 必8;
               float var6;
               if (var5.isJsonObject()) {
                  JsonObject var8 = var5.getAsJsonObject();
                  var6 = var8.get(必5).getAsFloat();
                  if (var8.has(必в)) {
                     var7 = var8.get(必ъ).getAsString();
                  }
               } else {
                  var6 = var5.getAsFloat();
               }

               var2.add(new EmoteAnimationClip.ScalarKeyframe(Float.parseFloat((String)var4.getKey()), var6, var7));
            }
         }

         var2.sort(Comparator.comparingDouble(EmoteAnimationClip.ScalarKeyframe::time));
         return new EmoteAnimationClip.ScalarTrack(var2);
      } else {
         return EmoteAnimationClip.ScalarTrack.EMPTY;
      }
   }

   public float ЗO() {
      return this.АТ;
   }

   public EmoteAnimationClip.BonePoseSample З(String var1, float var2) {
      EmoteAnimationClip.BoneTracks var3 = this.сЫ.get(var1);
      return var3 == null
         ? EmoteAnimationClip.BonePoseSample.ZERO
         : new EmoteAnimationClip.BonePoseSample(
            var3.rotation.sample(var2, this.АО, this.АФ, this.Яб),
            var3.position.sample(var2, this.АО, this.АФ, this.Яб),
            var3.bendDirection.sample(var2, this.АО, this.АФ, this.Яб),
            var3.bend.sample(var2, this.АО, this.АФ, this.Яб),
            !var3.rotation.frames().isEmpty(),
            !var3.position.frames().isEmpty(),
            !var3.bend.frames().isEmpty()
         );
   }

   private static JsonElement З(JsonObject var0, String var1, String var2) {
      return var0.has(var1) ? var0.get(var1) : var0.get(var2);
   }

   private static JsonObject З(EmoteDefinition var0, String var1, String var2) {
      Identifier var3 = Identifier.fromNamespaceAndPath(必4, "player_animation/" + var0.file() + var2 + ".json");

      try {
         BufferedReader var4 = Minecraft.getInstance().getResourceManager().getResource(var3).orElseThrow().openAsReader();

         try (var4) {
            JsonObject var6 = JsonParser.parseReader(var4).getAsJsonObject();
            return var6.getAsJsonObject(必п).getAsJsonObject(var1);
         }
      } catch (Exception var10) {
         return null;
      }
   }

   private static Vector3f З_f5cce25e(JsonElement var0) {
      if (var0 != null && var0.isJsonArray()) {
         JsonArray var1 = var0.getAsJsonArray();
         return new Vector3f(var1.get(0).getAsFloat(), var1.get(1).getAsFloat(), var1.get(2).getAsFloat());
      } else {
         return new Vector3f();
      }
   }

   public float З(float var1, boolean var2) {
      if (!var2) {
         return Math.min(var1, this.АТ);
      }

      if (var1 < this.АФ) {
         return var1;
      }

      float var3 = this.АФ - this.АО;
      return var3 <= 必Д ? this.АТ : this.АО + (var1 - this.АО) % var3;
   }

   private record BoneTracks(
      EmoteAnimationClip.VectorTrack rotation, EmoteAnimationClip.VectorTrack position, EmoteAnimationClip.ScalarTrack bendDirection, EmoteAnimationClip.ScalarTrack bend
   ) {
   }

   private record ScalarKeyframe(float time, float value, String easing) {
   }

   private record VectorTrack(List<EmoteAnimationClip.VectorKeyframe> frames) {
      private static final EmoteAnimationClip.VectorTrack EMPTY = new EmoteAnimationClip.VectorTrack(List.of());

      private static float ease(String var0, float var1) {
         return switch (var0) {
            case "easeInQuad" -> var1 * var1;
            case "easeOutQuad" -> 1.0F - (1.0F - var1) * (1.0F - var1);
            case "easeInOutQuad" -> var1 < 0.5F ? 2.0F * var1 * var1 : 1.0F - (float)Math.pow(-2.0F * var1 + 2.0F, 2.0) * 0.5F;
            case "easeInCubic" -> var1 * var1 * var1;
            case "easeOutCubic" -> 1.0F - (float)Math.pow(1.0F - var1, 3.0);
            case "easeInOutCubic" -> var1 < 0.5F ? 4.0F * var1 * var1 * var1 : 1.0F - (float)Math.pow(-2.0F * var1 + 2.0F, 3.0) * 0.5F;
            case "easeInSine" -> 1.0F - (float)Math.cos(var1 * Math.PI * 0.5);
            case "easeInOutSine" -> -((float)Math.cos(Math.PI * var1) - 1.0F) * 0.5F;
            case "step" -> 0.0F;
            default -> var1;
         };
      }

      Vector3f sample(float var1, float var2, float var3, boolean var4) {
         if (this.frames.isEmpty()) {
            return new Vector3f();
         }

         if (this.frames.size() != 1 && !(var1 <= this.frames.getFirst().time)) {
            for (int var5 = 1; var5 < this.frames.size(); var5++) {
               EmoteAnimationClip.VectorKeyframe var6 = this.frames.get(var5);
               if (var1 <= var6.time) {
                  EmoteAnimationClip.VectorKeyframe var7 = this.frames.get(var5 - 1);
                  float var8 = (var1 - var7.time) / Math.max(1.0E-4F, var6.time - var7.time);
                  var8 = ease(var4 ? var6.easing : var7.easing, Math.clamp(var8, 0.0F, 1.0F));
                  return new Vector3f(var7.value).lerp(var6.value, var8);
               }
            }

            if (var3 > this.frames.getLast().time) {
               EmoteAnimationClip.VectorKeyframe var9 = this.frames.stream().filter(var1x -> var1x.time >= var2).findFirst().orElse(null);
               if (var9 != null) {
                  EmoteAnimationClip.VectorKeyframe var10 = this.frames.getLast();
                  float var11 = var9.time + var3 - var2;
                  float var13 = (var1 - var10.time) / Math.max(1.0E-4F, var11 - var10.time);
                  var13 = ease(var4 ? var9.easing : var10.easing, Math.clamp(var13, 0.0F, 1.0F));
                  return new Vector3f(var10.value).lerp(var9.value, var13);
               }
            }

            return new Vector3f(this.frames.getLast().value);
         } else {
            return new Vector3f(this.frames.getFirst().value);
         }
      }

      float end() {
         return this.frames.isEmpty() ? 0.0F : this.frames.getLast().time;
      }
   }

   private record ScalarTrack(List<EmoteAnimationClip.ScalarKeyframe> frames) {
      private static final EmoteAnimationClip.ScalarTrack EMPTY = new EmoteAnimationClip.ScalarTrack(List.of());

      float sample(float var1, float var2, float var3, boolean var4) {
         if (this.frames.isEmpty()) {
            return 0.0F;
         }

         if (this.frames.size() != 1 && !(var1 <= this.frames.getFirst().time)) {
            for (int var5 = 1; var5 < this.frames.size(); var5++) {
               EmoteAnimationClip.ScalarKeyframe var6 = this.frames.get(var5);
               if (var1 <= var6.time) {
                  EmoteAnimationClip.ScalarKeyframe var7 = this.frames.get(var5 - 1);
                  float var8 = (var1 - var7.time) / Math.max(1.0E-4F, var6.time - var7.time);
                  var8 = EmoteAnimationClip.VectorTrack.ease(var4 ? var6.easing : var7.easing, Math.clamp(var8, 0.0F, 1.0F));
                  return Mth.lerp(var8, var7.value, var6.value);
               }
            }

            if (var3 > this.frames.getLast().time) {
               EmoteAnimationClip.ScalarKeyframe var9 = this.frames.stream().filter(var1x -> var1x.time >= var2).findFirst().orElse(null);
               if (var9 != null) {
                  EmoteAnimationClip.ScalarKeyframe var10 = this.frames.getLast();
                  float var11 = var9.time + var3 - var2;
                  float var13 = (var1 - var10.time) / Math.max(1.0E-4F, var11 - var10.time);
                  var13 = EmoteAnimationClip.VectorTrack.ease(var4 ? var9.easing : var10.easing, Math.clamp(var13, 0.0F, 1.0F));
                  return Mth.lerp(var13, var10.value, var9.value);
               }
            }

            return this.frames.getLast().value;
         } else {
            return this.frames.getFirst().value;
         }
      }
   }

   private record VectorKeyframe(float time, Vector3f value, String easing) {
   }

   public record BonePoseSample(
      Vector3f rotation, Vector3f position, float bendDirection, float bend, boolean hasRotation, boolean hasPosition, boolean hasBend
   ) {
      private static final EmoteAnimationClip.BonePoseSample ZERO = new EmoteAnimationClip.BonePoseSample(new Vector3f(), new Vector3f(), 0.0F, 0.0F, false, false, false);
   }
}
