package sg.render;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class CosmeticModel {
   @SerializedName("4З")
   public int textureWidth = 64;
   @SerializedName("4Е")
   public int textureHeight = 64;
   public @com.google.gson.annotations.SerializedName("Хю")  boolean absolutePivotTransforms;
   public final @com.google.gson.annotations.SerializedName("сЩ")  List<CosmeticModel.ModelBone> rootBones = new ArrayList<>();
   private @com.google.gson.annotations.SerializedName("В")  CosmeticModel.ModelBounds cachedBounds;
   private static final String RELATIVE_GEOMETRY_PRESENT_KEY = "minecraft:geometry";
   private static final String RELATIVE_GEOMETRY_ARRAY_KEY = "minecraft:geometry";
   private static final String RELATIVE_BONES_PRESENT_KEY = "bones";
   private static final String RELATIVE_BONES_ARRAY_KEY = "bones";
   private static final String RELATIVE_BONE_NAME_PRESENT_KEY = "name";
   private static final String RELATIVE_BONE_NAME_VALUE_KEY = "name";
   private static final String RELATIVE_DEFAULT_BONE_NAME = "bone";
   private static final String RELATIVE_BONE_PIVOT_KEY = "pivot";
   private static final String RELATIVE_BONE_ROTATION_KEY = "rotation";
   private static final String RELATIVE_BONE_MIRROR_PRESENT_KEY = "mirror";
   private static final String RELATIVE_BONE_MIRROR_VALUE_KEY = "mirror";
   private static final String RELATIVE_CUBES_PRESENT_KEY = "cubes";
   private static final String RELATIVE_CUBES_ARRAY_KEY = "cubes";
   private static final String RELATIVE_CUBE_ROTATION_PRESENT_KEY = "rotation";
   private static final String RELATIVE_CUBE_PIVOT_PRESENT_KEY = "pivot";
   private static final String RELATIVE_CUBE_PIVOT_VALUE_KEY = "pivot";
   private static final String RELATIVE_CUBE_ROTATION_VALUE_KEY = "rotation";
   private static final String RELATIVE_PARENT_PRESENT_KEY = "parent";
   private static final String RELATIVE_PARENT_VALUE_KEY = "parent";
   private static final String ABSOLUTE_GEOMETRY_PRESENT_KEY = "minecraft:geometry";
   private static final String ABSOLUTE_GEOMETRY_ARRAY_KEY = "minecraft:geometry";
   private static final String ABSOLUTE_BONES_PRESENT_KEY = "bones";
   private static final String ABSOLUTE_BONES_ARRAY_KEY = "bones";
   private static final String ABSOLUTE_BONE_NAME_PRESENT_KEY = "name";
   private static final String ABSOLUTE_BONE_NAME_VALUE_KEY = "name";
   private static final String ABSOLUTE_DEFAULT_BONE_NAME = "bone";
   private static final String ABSOLUTE_BONE_PIVOT_KEY = "pivot";
   private static final String ABSOLUTE_BONE_ROTATION_KEY = "rotation";
   private static final String BONE_TINT_PRESENT_KEY = "rose_tint";
   private static final String BONE_TINT_VALUE_KEY = "rose_tint";
   private static final String TINT_HEX_PREFIX = "#";
   private static final String ABSOLUTE_BONE_MIRROR_PRESENT_KEY = "mirror";
   private static final String ABSOLUTE_BONE_MIRROR_VALUE_KEY = "mirror";
   private static final String ABSOLUTE_CUBES_PRESENT_KEY = "cubes";
   private static final String ABSOLUTE_CUBES_ARRAY_KEY = "cubes";
   private static final String ABSOLUTE_PARENT_PRESENT_KEY = "parent";
   private static final String ABSOLUTE_PARENT_VALUE_KEY = "parent";
   private static final String DESCRIPTION_PRESENT_KEY = "description";
   private static final String DESCRIPTION_OBJECT_KEY = "description";
   private static final String TEXTURE_WIDTH_PRESENT_KEY = "texture_width";
   private static final String TEXTURE_WIDTH_VALUE_KEY = "texture_width";
   private static final String TEXTURE_HEIGHT_PRESENT_KEY = "texture_height";
   private static final String TEXTURE_HEIGHT_VALUE_KEY = "texture_height";
   private static final String RELATIVE_CUBE_ORIGIN_KEY = "origin";
   private static final String RELATIVE_CUBE_SIZE_KEY = "size";
   private static final String ABSOLUTE_CUBE_ORIGIN_KEY = "origin";
   private static final String ABSOLUTE_CUBE_SIZE_KEY = "size";
   private static final String ABSOLUTE_CUBE_PIVOT_KEY = "pivot";
   private static final String ABSOLUTE_CUBE_ROTATION_KEY = "rotation";
   private static final String INFLATE_PRESENT_KEY = "inflate";
   private static final String INFLATE_VALUE_KEY = "inflate";
   private static final String CUBE_MIRROR_PRESENT_KEY = "mirror";
   private static final String CUBE_MIRROR_VALUE_KEY = "mirror";
   private static final String CUBE_UV_PRESENT_KEY = "uv";
   private static final String CUBE_UV_VALUE_KEY = "uv";
   private static final String FACE_UV_PRESENT_KEY = "uv";
   private static final String FACE_UV_ARRAY_KEY = "uv";
   private static final String EAST_FACE_KEY = "east";
   private static final String WEST_FACE_KEY = "west";
   private static final String UP_FACE_KEY = "up";
   private static final String DOWN_FACE_KEY = "down";
   private static final String FACE_UV_SIZE_PRESENT_KEY = "uv_size";
   private static final String FACE_UV_SIZE_ARRAY_KEY = "uv_size";
   private static final float DEFAULT_MIN_X = -0.5F;
   private static final float DEFAULT_MIN_Y = -0.5F;
   private static final float DEFAULT_MIN_Z = -0.5F;
   private static final float DEFAULT_MAX_X = 0.5F;
   private static final float DEFAULT_MAX_Y = 0.5F;
   private static final float DEFAULT_MAX_Z = 0.5F;
   private static final float RELATIVE_BONE_PIVOT_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float RELATIVE_BONE_PIVOT_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float RELATIVE_BONE_PIVOT_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float RELATIVE_CUBE_MIN_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float RELATIVE_CUBE_MIN_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float RELATIVE_CUBE_MIN_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float RELATIVE_CUBE_MAX_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float RELATIVE_CUBE_MAX_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float RELATIVE_CUBE_MAX_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_BONE_PIVOT_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_BONE_PIVOT_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_BONE_PIVOT_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_BONE_UNPIVOT_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_BONE_UNPIVOT_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_BONE_UNPIVOT_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_PIVOT_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_PIVOT_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_PIVOT_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_UNPIVOT_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_UNPIVOT_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_UNPIVOT_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_MIN_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_MIN_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_MIN_Z_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_MAX_X_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_MAX_Y_MODEL_UNITS_PER_BLOCK = 16.0F;
   private static final float ABSOLUTE_CUBE_MAX_Z_MODEL_UNITS_PER_BLOCK = 16.0F;

   private static CosmeticModel.ModelCube parseAbsolutePivotCube(JsonObject json) {
      CosmeticModel.ModelCube cube = new CosmeticModel.ModelCube();
      float[] origin = readVector3(json, ABSOLUTE_CUBE_ORIGIN_KEY);
      float[] size = readVector3(json, ABSOLUTE_CUBE_SIZE_KEY);
      cube.originX = -origin[0] - size[0];
      cube.originY = origin[1];
      cube.originZ = origin[2];
      cube.width = size[0];
      cube.height = size[1];
      cube.depth = size[2];
      float[] pivot = readVector3(json, ABSOLUTE_CUBE_PIVOT_KEY);
      cube.pivotX = -pivot[0];
      cube.pivotY = pivot[1];
      cube.pivotZ = pivot[2];
      float[] rotation = readVector3(json, ABSOLUTE_CUBE_ROTATION_KEY);
      cube.xRotationDegrees = -rotation[0];
      cube.yRotationDegrees = -rotation[1];
      cube.zRotationDegrees = rotation[2];
      parseCubeAppearance(cube, json);
      return cube;
   }

   private static void parseCubeAppearance(CosmeticModel.ModelCube cube, JsonObject json) {
      float inflate = json.has(INFLATE_PRESENT_KEY) ? json.get(INFLATE_VALUE_KEY).getAsFloat() : 0.0F;
      cube.inflateX = inflate;
      cube.inflateY = inflate;
      cube.inflateZ = inflate;
      cube.mirrorUv = json.has(CUBE_MIRROR_PRESENT_KEY) && json.get(CUBE_MIRROR_VALUE_KEY).getAsBoolean();
      if (json.has(CUBE_UV_PRESENT_KEY)) {
         JsonElement uvElement = json.get(CUBE_UV_VALUE_KEY);
         if (uvElement.isJsonArray()) {
            JsonArray boxUv = uvElement.getAsJsonArray();
            cube.textureU = boxUv.get(0).getAsFloat();
            cube.textureV = boxUv.get(1).getAsFloat();
         } else if (uvElement.isJsonObject()) {
            JsonObject faceUvs = uvElement.getAsJsonObject();

            for (Entry faceEntry : faceUvs.entrySet()) {
               if (((JsonElement)faceEntry.getValue()).isJsonObject()) {
                  JsonObject faceJson = ((JsonElement)faceEntry.getValue()).getAsJsonObject();
                  if (faceJson.has(FACE_UV_PRESENT_KEY)) {
                     JsonArray uvOrigin = faceJson.getAsJsonArray(FACE_UV_ARRAY_KEY);

                     float uvWidth = switch ((String)faceEntry.getKey()) {
                        case EAST_FACE_KEY, WEST_FACE_KEY -> cube.depth;
                        default -> cube.width;
                     };

                     float uvHeight = switch ((String)faceEntry.getKey()) {
                        case UP_FACE_KEY, DOWN_FACE_KEY -> cube.depth;
                        default -> cube.height;
                     };
                     if (faceJson.has(FACE_UV_SIZE_PRESENT_KEY)) {
                        JsonArray uvSize = faceJson.getAsJsonArray(FACE_UV_SIZE_ARRAY_KEY);
                        uvWidth = uvSize.get(0).getAsFloat();
                        uvHeight = uvSize.get(1).getAsFloat();
                     }

                     cube.faceUvs.put((String)faceEntry.getKey(), new CosmeticModel.UvRect(uvOrigin.get(0).getAsFloat(), uvOrigin.get(1).getAsFloat(), uvWidth, uvHeight));
                  }
               }
            }
         }
      }
   }

   private static CosmeticModel.ModelCube parseRelativePivotCube(JsonObject json, float[] pivot) {
      CosmeticModel.ModelCube cube = new CosmeticModel.ModelCube();
      float[] origin = readVector3(json, RELATIVE_CUBE_ORIGIN_KEY);
      cube.originX = origin[0] - pivot[0];
      cube.originY = origin[1] - pivot[1];
      cube.originZ = origin[2] - pivot[2];
      float[] size = readVector3(json, RELATIVE_CUBE_SIZE_KEY);
      cube.width = size[0];
      cube.height = size[1];
      cube.depth = size[2];
      parseCubeAppearance(cube, json);
      return cube;
   }

   private static float[] readVector3(JsonObject json, String key) {
      if (json.has(key) && json.get(key).isJsonArray()) {
         JsonArray components = json.getAsJsonArray(key);
         return new float[]{
            components.size() > 0 ? components.get(0).getAsFloat() : 0.0F,
            components.size() > 1 ? components.get(1).getAsFloat() : 0.0F,
            components.size() > 2 ? components.get(2).getAsFloat() : 0.0F
         };
      } else {
         return new float[]{0.0F, 0.0F, 0.0F};
      }
   }

   private static void includeTransformedBox(Matrix4f transform, float[] bounds, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
      for (int xCorner = 0; xCorner < 2; xCorner++) {
         for (int yCorner = 0; yCorner < 2; yCorner++) {
            for (int zCorner = 0; zCorner < 2; zCorner++) {
               Vector3f transformedCorner = transform.transformPosition(new Vector3f(xCorner == 0 ? minX : maxX, yCorner == 0 ? minY : maxY, zCorner == 0 ? minZ : maxZ));
               bounds[0] = Math.min(bounds[0], transformedCorner.x);
               bounds[1] = Math.min(bounds[1], transformedCorner.y);
               bounds[2] = Math.min(bounds[2], transformedCorner.z);
               bounds[3] = Math.max(bounds[3], transformedCorner.x);
               bounds[4] = Math.max(bounds[4], transformedCorner.y);
               bounds[5] = Math.max(bounds[5], transformedCorner.z);
            }
         }
      }
   }

   private static void includeAbsolutePivotBoneBounds(CosmeticModel.ModelBone bone, Matrix4f parentTransform, float[] bounds) {
      Matrix4f boneTransform = new Matrix4f(parentTransform)
         .translate(bone.pivotX / ABSOLUTE_BONE_PIVOT_X_MODEL_UNITS_PER_BLOCK, bone.pivotY / ABSOLUTE_BONE_PIVOT_Y_MODEL_UNITS_PER_BLOCK, bone.pivotZ / ABSOLUTE_BONE_PIVOT_Z_MODEL_UNITS_PER_BLOCK)
         .rotateZ((float)Math.toRadians(bone.zRotationDegrees))
         .rotateY((float)Math.toRadians(bone.yRotationDegrees))
         .rotateX((float)Math.toRadians(bone.xRotationDegrees))
         .translate(-bone.pivotX / ABSOLUTE_BONE_UNPIVOT_X_MODEL_UNITS_PER_BLOCK, -bone.pivotY / ABSOLUTE_BONE_UNPIVOT_Y_MODEL_UNITS_PER_BLOCK, -bone.pivotZ / ABSOLUTE_BONE_UNPIVOT_Z_MODEL_UNITS_PER_BLOCK);

      for (CosmeticModel.ModelCube cube : bone.cubes) {
         Matrix4f cubeTransform = new Matrix4f(boneTransform)
            .translate(cube.pivotX / ABSOLUTE_CUBE_PIVOT_X_MODEL_UNITS_PER_BLOCK, cube.pivotY / ABSOLUTE_CUBE_PIVOT_Y_MODEL_UNITS_PER_BLOCK, cube.pivotZ / ABSOLUTE_CUBE_PIVOT_Z_MODEL_UNITS_PER_BLOCK)
            .rotateZ((float)Math.toRadians(cube.zRotationDegrees))
            .rotateY((float)Math.toRadians(cube.yRotationDegrees))
            .rotateX((float)Math.toRadians(cube.xRotationDegrees))
            .translate(-cube.pivotX / ABSOLUTE_CUBE_UNPIVOT_X_MODEL_UNITS_PER_BLOCK, -cube.pivotY / ABSOLUTE_CUBE_UNPIVOT_Y_MODEL_UNITS_PER_BLOCK, -cube.pivotZ / ABSOLUTE_CUBE_UNPIVOT_Z_MODEL_UNITS_PER_BLOCK);
         includeAbsolutePivotCubeBounds(cube, cubeTransform, bounds);
      }

      for (CosmeticModel.ModelBone child : bone.children) {
         includeAbsolutePivotBoneBounds(child, boneTransform, bounds);
      }
   }

   private static void includeAbsolutePivotCubeBounds(CosmeticModel.ModelCube cube, Matrix4f transform, float[] bounds) {
      float minX = (cube.originX - cube.inflateX) / ABSOLUTE_CUBE_MIN_X_MODEL_UNITS_PER_BLOCK;
      float minY = (cube.originY - cube.inflateY) / ABSOLUTE_CUBE_MIN_Y_MODEL_UNITS_PER_BLOCK;
      float minZ = (cube.originZ - cube.inflateZ) / ABSOLUTE_CUBE_MIN_Z_MODEL_UNITS_PER_BLOCK;
      float maxX = (cube.originX + cube.width + cube.inflateX) / ABSOLUTE_CUBE_MAX_X_MODEL_UNITS_PER_BLOCK;
      float maxY = (cube.originY + cube.height + cube.inflateY) / ABSOLUTE_CUBE_MAX_Y_MODEL_UNITS_PER_BLOCK;
      float maxZ = (cube.originZ + cube.depth + cube.inflateZ) / ABSOLUTE_CUBE_MAX_Z_MODEL_UNITS_PER_BLOCK;
      includeTransformedBox(transform, bounds, minX, minY, minZ, maxX, maxY, maxZ);
   }

   public CosmeticModel.ModelBounds getBounds() {
      if (this.cachedBounds != null) {
         return this.cachedBounds;
      }

      float[] bounds = new float[]{
         Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY
      };
      Matrix4f identityTransform = new Matrix4f();

      for (CosmeticModel.ModelBone rootBone : this.rootBones) {
         if (this.absolutePivotTransforms) {
            includeAbsolutePivotBoneBounds(rootBone, identityTransform, bounds);
         } else {
            includeRelativePivotBoneBounds(rootBone, identityTransform, bounds);
         }
      }

      if (!Float.isFinite(bounds[0])) {
         this.cachedBounds = new CosmeticModel.ModelBounds(DEFAULT_MIN_X, DEFAULT_MIN_Y, DEFAULT_MIN_Z, DEFAULT_MAX_X, DEFAULT_MAX_Y, DEFAULT_MAX_Z);
      } else {
         this.cachedBounds = new CosmeticModel.ModelBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
      }

      return this.cachedBounds;
   }

   private static void parseTextureDimensions(CosmeticModel model, JsonObject geometry) {
      if (geometry.has(DESCRIPTION_PRESENT_KEY)) {
         JsonObject description = geometry.getAsJsonObject(DESCRIPTION_OBJECT_KEY);
         model.textureWidth = description.has(TEXTURE_WIDTH_PRESENT_KEY) ? description.get(TEXTURE_WIDTH_VALUE_KEY).getAsInt() : 64;
         model.textureHeight = description.has(TEXTURE_HEIGHT_PRESENT_KEY) ? description.get(TEXTURE_HEIGHT_VALUE_KEY).getAsInt() : 64;
      }
   }

   private static void includeRelativePivotBoneBounds(CosmeticModel.ModelBone bone, Matrix4f parentTransform, float[] bounds) {
      Matrix4f boneTransform = new Matrix4f(parentTransform)
         .translate(bone.pivotX / RELATIVE_BONE_PIVOT_X_MODEL_UNITS_PER_BLOCK, -bone.pivotY / RELATIVE_BONE_PIVOT_Y_MODEL_UNITS_PER_BLOCK, bone.pivotZ / RELATIVE_BONE_PIVOT_Z_MODEL_UNITS_PER_BLOCK)
         .rotateZ((float)Math.toRadians(bone.zRotationDegrees))
         .rotateY((float)Math.toRadians(bone.yRotationDegrees))
         .rotateX((float)Math.toRadians(bone.xRotationDegrees));

      for (CosmeticModel.ModelCube cube : bone.cubes) {
         includeRelativePivotCubeBounds(cube, boneTransform, bounds);
      }

      for (CosmeticModel.ModelBone child : bone.children) {
         includeRelativePivotBoneBounds(child, boneTransform, bounds);
      }
   }

   public static CosmeticModel loadRelativePivotGeometry(InputStream stream) {
      JsonObject rootJson = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
      CosmeticModel model = new CosmeticModel();
      if (!rootJson.has(RELATIVE_GEOMETRY_PRESENT_KEY)) {
         return model;
      }

      JsonArray geometries = rootJson.getAsJsonArray(RELATIVE_GEOMETRY_ARRAY_KEY);
      if (geometries.size() == 0) {
         return model;
      }

      JsonObject geometry = geometries.get(0).getAsJsonObject();
      parseTextureDimensions(model, geometry);
      if (!geometry.has(RELATIVE_BONES_PRESENT_KEY)) {
         return model;
      }

      LinkedHashMap<String, CosmeticModel.ModelBone> bonesByName = new LinkedHashMap<>();
      LinkedHashMap<String, float[]> pivotsByName = new LinkedHashMap<>();
      LinkedHashMap<String, String> parentsByName = new LinkedHashMap<>();

      for (JsonElement boneElement : geometry.getAsJsonArray(RELATIVE_BONES_ARRAY_KEY)) {
         JsonObject boneJson = boneElement.getAsJsonObject();
         CosmeticModel.ModelBone bone = new CosmeticModel.ModelBone();
         bone.name = boneJson.has(RELATIVE_BONE_NAME_PRESENT_KEY) ? boneJson.get(RELATIVE_BONE_NAME_VALUE_KEY).getAsString() : RELATIVE_DEFAULT_BONE_NAME;
         float[] pivot = readVector3(boneJson, RELATIVE_BONE_PIVOT_KEY);
         pivotsByName.put(bone.name, pivot);
         float[] rotation = readVector3(boneJson, RELATIVE_BONE_ROTATION_KEY);
         bone.xRotationDegrees = rotation[0];
         bone.yRotationDegrees = rotation[1];
         bone.zRotationDegrees = rotation[2];
         bone.mirrorUv = boneJson.has(RELATIVE_BONE_MIRROR_PRESENT_KEY) && boneJson.get(RELATIVE_BONE_MIRROR_VALUE_KEY).getAsBoolean();
         if (boneJson.has(RELATIVE_CUBES_PRESENT_KEY)) {
            for (JsonElement cubeElement : boneJson.getAsJsonArray(RELATIVE_CUBES_ARRAY_KEY)) {
               JsonObject cubeJson = cubeElement.getAsJsonObject();
               if (!cubeJson.has(RELATIVE_CUBE_ROTATION_PRESENT_KEY) && !cubeJson.has(RELATIVE_CUBE_PIVOT_PRESENT_KEY)) {
                  bone.cubes.add(parseRelativePivotCube(cubeJson, pivot));
               } else {
                  float[] cubePivot = readVector3(cubeJson, RELATIVE_CUBE_PIVOT_VALUE_KEY);
                  float[] cubeRotation = readVector3(cubeJson, RELATIVE_CUBE_ROTATION_VALUE_KEY);
                  CosmeticModel.ModelBone cubeBone = new CosmeticModel.ModelBone();
                  cubeBone.name = bone.name + "_cube_" + bone.children.size();
                  cubeBone.pivotX = cubePivot[0] - pivot[0];
                  cubeBone.pivotY = cubePivot[1] - pivot[1];
                  cubeBone.pivotZ = cubePivot[2] - pivot[2];
                  cubeBone.xRotationDegrees = cubeRotation[0];
                  cubeBone.yRotationDegrees = cubeRotation[1];
                  cubeBone.zRotationDegrees = cubeRotation[2];
                  cubeBone.cubes.add(parseRelativePivotCube(cubeJson, cubePivot));
                  bone.children.add(cubeBone);
               }
            }
         }

         if (boneJson.has(RELATIVE_PARENT_PRESENT_KEY)) {
            parentsByName.put(bone.name, boneJson.get(RELATIVE_PARENT_VALUE_KEY).getAsString());
         }

         bonesByName.put(bone.name, bone);
      }

      for (Entry<String, CosmeticModel.ModelBone> boneEntry : bonesByName.entrySet()) {
         CosmeticModel.ModelBone bone = (CosmeticModel.ModelBone)boneEntry.getValue();
         String parentName = (String)parentsByName.get(boneEntry.getKey());
         float[] pivot = pivotsByName.getOrDefault(boneEntry.getKey(), new float[]{0.0F, 0.0F, 0.0F});
         if (parentName == null) {
            bone.pivotX = pivot[0];
            bone.pivotY = pivot[1];
            bone.pivotZ = pivot[2];
            model.rootBones.add(bone);
         } else {
            float[] parentPivot = pivotsByName.getOrDefault(parentName, new float[]{0.0F, 0.0F, 0.0F});
            bone.pivotX = pivot[0] - parentPivot[0];
            bone.pivotY = pivot[1] - parentPivot[1];
            bone.pivotZ = pivot[2] - parentPivot[2];
            CosmeticModel.ModelBone parentBone = (CosmeticModel.ModelBone)bonesByName.get(parentName);
            if (parentBone == null) {
               model.rootBones.add(bone);
            } else {
               parentBone.children.add(bone);
            }
         }
      }

      return model;
   }

   private static void includeRelativePivotCubeBounds(CosmeticModel.ModelCube cube, Matrix4f transform, float[] bounds) {
      float minX = (cube.originX - cube.inflateX) / RELATIVE_CUBE_MIN_X_MODEL_UNITS_PER_BLOCK;
      float minY = (-cube.originY - cube.height - cube.inflateY) / RELATIVE_CUBE_MIN_Y_MODEL_UNITS_PER_BLOCK;
      float minZ = (cube.originZ - cube.inflateZ) / RELATIVE_CUBE_MIN_Z_MODEL_UNITS_PER_BLOCK;
      float maxX = (cube.originX + cube.width + cube.inflateX) / RELATIVE_CUBE_MAX_X_MODEL_UNITS_PER_BLOCK;
      float maxY = (-cube.originY + cube.inflateY) / RELATIVE_CUBE_MAX_Y_MODEL_UNITS_PER_BLOCK;
      float maxZ = (cube.originZ + cube.depth + cube.inflateZ) / RELATIVE_CUBE_MAX_Z_MODEL_UNITS_PER_BLOCK;
      includeTransformedBox(transform, bounds, minX, minY, minZ, maxX, maxY, maxZ);
   }

   public static CosmeticModel loadAbsolutePivotGeometry(InputStream stream) {
      JsonObject rootJson = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
      CosmeticModel model = new CosmeticModel();
      model.absolutePivotTransforms = true;
      if (!rootJson.has(ABSOLUTE_GEOMETRY_PRESENT_KEY)) {
         return model;
      }

      JsonArray geometries = rootJson.getAsJsonArray(ABSOLUTE_GEOMETRY_ARRAY_KEY);
      if (geometries.size() == 0) {
         return model;
      }

      JsonObject geometry = geometries.get(0).getAsJsonObject();
      parseTextureDimensions(model, geometry);
      if (!geometry.has(ABSOLUTE_BONES_PRESENT_KEY)) {
         return model;
      }

      LinkedHashMap<String, CosmeticModel.ModelBone> bonesByName = new LinkedHashMap<>();
      LinkedHashMap<String, String> parentsByName = new LinkedHashMap<>();

      for (JsonElement boneElement : geometry.getAsJsonArray(ABSOLUTE_BONES_ARRAY_KEY)) {
         JsonObject boneJson = boneElement.getAsJsonObject();
         CosmeticModel.ModelBone bone = new CosmeticModel.ModelBone();
         bone.name = boneJson.has(ABSOLUTE_BONE_NAME_PRESENT_KEY) ? boneJson.get(ABSOLUTE_BONE_NAME_VALUE_KEY).getAsString() : ABSOLUTE_DEFAULT_BONE_NAME;
         float[] pivot = readVector3(boneJson, ABSOLUTE_BONE_PIVOT_KEY);
         bone.pivotX = -pivot[0];
         bone.pivotY = pivot[1];
         bone.pivotZ = pivot[2];
         float[] rotation = readVector3(boneJson, ABSOLUTE_BONE_ROTATION_KEY);
         bone.xRotationDegrees = -rotation[0];
         bone.yRotationDegrees = -rotation[1];
         bone.zRotationDegrees = rotation[2];
         if (boneJson.has(BONE_TINT_PRESENT_KEY)) {
            bone.tintColorRgb = (int)Long.parseLong(boneJson.get(BONE_TINT_VALUE_KEY).getAsString().replace(TINT_HEX_PREFIX, ""), 16);
         }

         bone.mirrorUv = boneJson.has(ABSOLUTE_BONE_MIRROR_PRESENT_KEY) && boneJson.get(ABSOLUTE_BONE_MIRROR_VALUE_KEY).getAsBoolean();
         if (boneJson.has(ABSOLUTE_CUBES_PRESENT_KEY)) {
            for (JsonElement cubeElement : boneJson.getAsJsonArray(ABSOLUTE_CUBES_ARRAY_KEY)) {
               JsonObject cubeJson = cubeElement.getAsJsonObject();
               bone.cubes.add(parseAbsolutePivotCube(cubeJson));
            }
         }

         if (boneJson.has(ABSOLUTE_PARENT_PRESENT_KEY)) {
            parentsByName.put(bone.name, boneJson.get(ABSOLUTE_PARENT_VALUE_KEY).getAsString());
         }

         bonesByName.put(bone.name, bone);
      }

      for (Entry<String, CosmeticModel.ModelBone> boneEntry : bonesByName.entrySet()) {
         String boneName = (String)boneEntry.getKey();
         CosmeticModel.ModelBone bone = (CosmeticModel.ModelBone)boneEntry.getValue();
         String parentName = (String)parentsByName.get(boneName);
         if (parentName == null) {
            model.rootBones.add(bone);
         } else {
            CosmeticModel.ModelBone parentBone = (CosmeticModel.ModelBone)bonesByName.get(parentName);
            if (parentBone == null) {
               model.rootBones.add(bone);
            } else {
               parentBone.children.add(bone);
            }
         }
      }

      return model;
   }

   public record UvRect(float u, float v, float width, float height) {
   }

   public record ModelBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
      public float centerZ() {
         return (this.minZ + this.maxZ) * 0.5F;
      }

      public float centerY() {
         return (this.minY + this.maxY) * 0.5F;
      }

      public float largestSize() {
         return Math.max(this.maxX - this.minX, Math.max(this.maxY - this.minY, this.maxZ - this.minZ));
      }

      public float centerX() {
         return (this.minX + this.maxX) * 0.5F;
      }
   }

   public static final class ModelBone {
      @SerializedName("4ж")
      public int tintColorRgb;
      public @com.google.gson.annotations.SerializedName("Вр")  String name;
      public @com.google.gson.annotations.SerializedName("пН")  float pivotX;
      public @com.google.gson.annotations.SerializedName("пн")  float pivotY;
      public @com.google.gson.annotations.SerializedName("пЦ")  float pivotZ;
      public @com.google.gson.annotations.SerializedName("пч")  float xRotationDegrees;
      public @com.google.gson.annotations.SerializedName("пЭ")  float yRotationDegrees;
      public @com.google.gson.annotations.SerializedName("п必")  float zRotationDegrees;
      public @com.google.gson.annotations.SerializedName("Ха")  boolean mirrorUv;
      public final @com.google.gson.annotations.SerializedName("с必")  List<CosmeticModel.ModelCube> cubes;
      public final @com.google.gson.annotations.SerializedName("сщ")  List<CosmeticModel.ModelBone> children;
      private static final int DEFAULT_TINT_RGB = 16777215;

      public ModelBone() {
         this.tintColorRgb = DEFAULT_TINT_RGB;
         this.name = "";
         this.cubes = new ArrayList<>();
         this.children = new ArrayList<>();
      }
   }

   public static final class ModelCube {
      public @com.google.gson.annotations.SerializedName("пС")  float originX;
      public @com.google.gson.annotations.SerializedName("пз")  float originY;
      public @com.google.gson.annotations.SerializedName("пц")  float originZ;
      public @com.google.gson.annotations.SerializedName("пя")  float width;
      public @com.google.gson.annotations.SerializedName("Дс")  float height;
      public @com.google.gson.annotations.SerializedName("ДВ")  float depth;
      public @com.google.gson.annotations.SerializedName("ДЯ")  float textureU;
      public @com.google.gson.annotations.SerializedName("ДХ")  float textureV;
      public @com.google.gson.annotations.SerializedName("Де")  float pivotX;
      public @com.google.gson.annotations.SerializedName("ДА")  float pivotY;
      public @com.google.gson.annotations.SerializedName("Д4")  float pivotZ;
      public @com.google.gson.annotations.SerializedName("Дп")  float xRotationDegrees;
      public @com.google.gson.annotations.SerializedName("ДД")  float yRotationDegrees;
      public @com.google.gson.annotations.SerializedName("ДЪ")  float zRotationDegrees;
      public @com.google.gson.annotations.SerializedName("Дт")  float inflateX;
      public @com.google.gson.annotations.SerializedName("Да")  float inflateY;
      public @com.google.gson.annotations.SerializedName("ДЮ")  float inflateZ;
      public @com.google.gson.annotations.SerializedName("ХР")  boolean mirrorUv;
      public final @com.google.gson.annotations.SerializedName("сф")  Map<String, CosmeticModel.UvRect> faceUvs = new LinkedHashMap<>();
   }
}
