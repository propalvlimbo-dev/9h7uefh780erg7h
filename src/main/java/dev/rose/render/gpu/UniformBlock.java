package dev.rose.render.gpu;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joml.Matrix4fc;


public final class UniformBlock {
   public static final String NAME = "RoseParams";
   private static final Pattern BLOCK = Pattern.compile("layout\\s*\\(\\s*std140\\s*\\)\\s*uniform\\s+RoseParams\\s*\\{([^}]*)}");
   private static final Pattern MEMBER = Pattern.compile(
      "^\\s*(float|int|bool|vec2|vec3|vec4|ivec2|ivec3|ivec4|mat4)\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*(?:\\[\\s*(\\d+)\\s*])?\\s*;"
   );
   private final List<UniformBlock.Field> fields;
   private final Map<String, UniformBlock.Field> byName = new HashMap<>();
   private final int size;

   private UniformBlock(List<UniformBlock.Field> fields, int size) {
      this.fields = fields;
      this.size = size;

      for (UniformBlock.Field field : fields) {
         this.byName.put(field.name(), field);
      }
   }

   public static UniformBlock empty() {
      return new UniformBlock(List.of(), 0);
   }

   public static UniformBlock parse(String source) {
      Matcher block = BLOCK.matcher(stripComments(source));
      if (!block.find()) {
         return empty();
      }

      List<UniformBlock.Field> fields = new ArrayList<>();
      int offset = 0;

      for (String line : block.group(1).split(";")) {
         if (!line.isBlank()) {
            Matcher m = MEMBER.matcher(line + ";");
            if (!m.find()) {
               throw new IllegalArgumentException("Не понимаю поле блока параметров: '" + line.trim() + "'");
            }

            String type = m.group(1);
            boolean array = m.group(3) != null;
            int count = array ? Integer.parseInt(m.group(3)) : 1;
            int size = sizeOf(type);
            int align = array ? 16 : alignOf(type);
            int stride = array ? roundUp(size, 16) : size;
            offset = roundUp(offset, align);
            fields.add(new UniformBlock.Field(m.group(2), type, count, offset, stride));
            offset += array ? stride * count : size;
         }
      }

      return new UniformBlock(List.copyOf(fields), roundUp(offset, 16));
   }

   private static String stripComments(String source) {
      return source.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("//[^\\n]*", "");
   }

   private static int sizeOf(String type) {
      return switch (type) {
         case "vec2", "ivec2" -> 8;
         case "vec3", "ivec3" -> 12;
         case "vec4", "ivec4" -> 16;
         case "mat4" -> 64;
         default -> 4;
      };
   }

   private static int alignOf(String type) {
      return switch (type) {
         case "vec2", "ivec2" -> 8;
         case "vec3", "ivec3", "vec4", "ivec4", "mat4" -> 16;
         default -> 4;
      };
   }

   private static int roundUp(int value, int step) {
      return (value + step - 1) / step * step;
   }

   public int size() {
      return this.size;
   }

   public UniformBlock.Field field(String name) {
      return this.byName.get(name);
   }

   public List<UniformBlock.Field> fields() {
      return this.fields;
   }

   public ByteBuffer newValues() {
      return ByteBuffer.allocate(Math.max(this.size, 16)).order(ByteOrder.nativeOrder());
   }

   static void write(ByteBuffer values, UniformBlock.Field field, float... numbers) {
      int per = field.components();

      for (int i = 0; i < numbers.length; i++) {
         int element = i / per;
         int component = i % per;
         if (element >= field.count()) {
            break;
         }

         int at = field.offset() + element * field.stride() + component * 4;
         if (field.isInt()) {
            values.putInt(at, Math.round(numbers[i]));
         } else {
            values.putFloat(at, numbers[i]);
         }
      }
   }

   static void write(ByteBuffer values, UniformBlock.Field field, int... numbers) {
      int per = field.components();

      for (int i = 0; i < numbers.length; i++) {
         int element = i / per;
         int component = i % per;
         if (element >= field.count()) {
            break;
         }

         int at = field.offset() + element * field.stride() + component * 4;
         if (field.isInt()) {
            values.putInt(at, numbers[i]);
         } else {
            values.putFloat(at, numbers[i]);
         }
      }
   }

   static void write(ByteBuffer values, UniformBlock.Field field, Matrix4fc matrix) {
      if (field.type().equals("mat4")) {
         matrix.get(field.offset(), values);
      }
   }

   public record Field(String name, String type, int count, int offset, int stride) {
      boolean isInt() {
         return this.type.equals("int") || this.type.equals("bool") || this.type.startsWith("ivec");
      }

      int components() {
         return switch (this.type) {
            case "vec2", "ivec2" -> 2;
            case "vec3", "ivec3" -> 3;
            case "vec4", "ivec4" -> 4;
            case "mat4" -> 16;
            default -> 1;
         };
      }
   }
}
