package sg.util.crypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class EncryptedAssetContainer {
   private static final byte[] field_4 = new byte[]{72, 67, 85, 49};
   private static final byte В = 1;
   private static final int тЪ = 12;
   private static final int тт = 128;
   private static final int та = field_4.length + 1 + 12;
   private static final int тЮ = та + 16;
   private static final byte[] п = EncryptedAssetContainer.Я25.getBytes(StandardCharsets.UTF_8);
   private static final byte[] Д = EncryptedAssetContainer.Я2в.getBytes(StandardCharsets.UTF_8);
   private static final byte[] Ъ = EncryptedAssetContainer.Я2ъ.getBytes(StandardCharsets.UTF_8);
   private static final byte[] т = Base64.getDecoder().decode(EncryptedAssetContainer.Я2М);
   private static final String Хю = "scIej/F4xKYzCaLOJ8foi2JBh/56mLrb4WQwA45hBFI=";
   private static final String ХЩ = "cthckz58zUCvwpdZQNxDoOQXuwL2VEQy8c7wUakb4l8=";
   private static final String Ху = "+fnl0yJfvVY2VwTkHbslVEV+IU/y29RsQKALXWAf7gw=";
   private static final String Х2 = "R5l4XDIL4QfF67Zk";
   private static final String Хф = "lgL2fFflxSMBXvpzcX9Z6MJSTBwyzb2EUG9CXOB6rv3W0vkwi+sqNVTj";
   private static final SecureRandom Х = new SecureRandom();
   private static final String Хи = "asset";
   private static final String ЯуБ = "asset";
   private static final String ЯуС = "plaintext";
   private static final String Яуз = "Data is already encrypted by HasherUtils";
   private static final String Яуц = "AES/GCM/NoPadding";
   private static final String Яуя = "AES";
   private static final String Я2с = "asset";
   private static final String Я2В = "container";
   private static final String Я2Я = "Data is not a HasherUtils container";
   private static final String Я2Х = "Encrypted container is truncated";
   private static final String Я2е = "AES/GCM/NoPadding";
   private static final String Я2А = "AES";
   private static final String Я24 = "input";
   private static final String Я2п = "input";
   private static final String Я2Д = "input";
   private static final String Я2Ъ = "input";
   private static final String Я2т = "input";
   private static final String Я2а = "HmacSHA256";
   private static final String Я2Ю = "HmacSHA256";
   private static final String Я2Ч = "HmacSHA256";
   private static final String Я2Л = "HmacSHA256";
   private static final String Я2诶 = "R5l4XDIL4QfF67Zk";
   private static final String Я21 = "lgL2fFflxSMBXvpzcX9Z6MJSTBwyzb2EUG9CXOB6rv3W0vkwi+sqNVTj";
   private static final String Я2Ь = "AES/GCM/NoPadding";
   private static final String Я2衣 = "AES";
   private static final String Я2ш = "scIej/F4xKYzCaLOJ8foi2JBh/56mLrb4WQwA45hBFI=";
   private static final String Я2И = "cthckz58zUCvwpdZQNxDoOQXuwL2VEQy8c7wUakb4l8=";
   private static final String Я2ь = "+fnl0yJfvVY2VwTkHbslVEV+IU/y29RsQKALXWAf7gw=";
   private static final String Я2ж = "SHA-256";
   private static final String Я2弟 = "resourceContext";
   private static final String Я2б = "/";
   private static final String Я2Ы = "resourceContext must not be empty";
   private static final String Я2l = "output";
   private static final String Я28 = ".tmp";
   private static final String Я25 = "HasherUtils.MasterKey.v1";
   private static final String Я2в = "HasherUtils.Asset.v1";
   private static final String Я2ъ = "HasherUtils.AssetKey.v1";
   private static final String Я2М = "FrNjQZNRRPCHq8h+Kll0MgZNisPKPrfmYzzjEoL0s7I=";

   public static byte[] б(InputStream var0, String var1) throws IOException, GeneralSecurityException {
      Objects.requireNonNull(var0, Я2п);
      return б(var0.readAllBytes(), var1);
   }

   public static byte[] I(byte[] var0) throws GeneralSecurityException {
      return л(var0, ЯуБ);
   }

   private static String method_4Е(String var0) {
      Objects.requireNonNull(var0, Я2弟);
      String var1 = var0.trim().replace('\\', '/');

      while (var1.startsWith(Я2б)) {
         var1 = var1.substring(1);
      }

      if (var1.isEmpty()) {
         throw new IllegalArgumentException(Я2Ы);
      } else {
         return var1;
      }
   }

   public static byte[] б(byte[] var0, String var1) throws GeneralSecurityException {
      Objects.requireNonNull(var0, Я2В);
      String var2 = method_4Е(var1);
      if (!Ж(var0)) {
         throw new IllegalArgumentException(Я2Я);
      }

      if (var0.length < тЮ) {
         throw new IllegalArgumentException(Я2Х);
      }

      ByteBuffer var3 = ByteBuffer.wrap(var0);
      var3.position(field_4.length);
      byte var4 = var3.get();
      if (var4 != 1) {
         throw new IllegalArgumentException("Unsupported format version: " + var4);
      }

      byte[] var5 = new byte[12];
      var3.get(var5);
      byte[] var6 = new byte[var3.remaining()];
      var3.get(var6);
      byte[] var7 = л(var2);

      try {
         Cipher var8 = Cipher.getInstance(Я2е);
         var8.init(2, new SecretKeySpec(var7, Я2А), new GCMParameterSpec(128, var5));
         var8.updateAAD(б(var2));
         return var8.doFinal(var6);
      } finally {
         Arrays.fill(var7, (byte)0);
         Arrays.fill(var6, (byte)0);
      }
   }

   private static byte[] л(String var0) throws GeneralSecurityException {
      byte[] var1 = method_8();
      byte[] var2 = null;

      try {
         Mac var3 = Mac.getInstance(Я2а);
         var3.init(new SecretKeySpec(т, Я2Ю));
         var2 = var3.doFinal(var1);
         Mac var4 = Mac.getInstance(Я2Ч);
         var4.init(new SecretKeySpec(var2, Я2Л));
         var4.update(Ъ);
         var4.update((byte)0);
         var4.update(var0.getBytes(StandardCharsets.UTF_8));
         var4.update((byte)1);
         return var4.doFinal();
      } finally {
         Arrays.fill(var1, (byte)0);
         if (var2 != null) {
            Arrays.fill(var2, (byte)0);
         }
      }
   }

   private EncryptedAssetContainer() {
   }

   public static void л(Path var0, Path var1, String var2) throws IOException, GeneralSecurityException {
      Objects.requireNonNull(var0, Я2Ъ);
      byte[] var3 = Files.readAllBytes(var0);

      try {
         Ж(var1, л(var3, var2));
      } finally {
         Arrays.fill(var3, (byte)0);
      }
   }

   public static byte[] ш(byte[] var0) throws GeneralSecurityException {
      return б(var0, Я2с);
   }

   private static byte[] б(String var0) {
      byte[] var1 = var0.getBytes(StandardCharsets.UTF_8);
      ByteBuffer var2 = ByteBuffer.allocate(Д.length + 1 + var1.length);
      var2.put(Д);
      var2.put((byte)0);
      var2.put(var1);
      return var2.array();
   }

   private static byte[] Й() throws GeneralSecurityException {
      byte[] var0 = Base64.getDecoder().decode(Я2ш);
      byte[] var1 = Base64.getDecoder().decode(Я2И);
      byte[] var2 = Base64.getDecoder().decode(Я2ь);
      byte[] var3 = new byte[32];

      try {
         for (int var4 = 0; var4 < var3.length; var4++) {
            int var5 = Byte.toUnsignedInt(var0[var4]) ^ Byte.toUnsignedInt(var1[var4 * 7 + 3 & 31]);
            var5 = ь(var5, var4 & 7);
            var5 ^= Byte.toUnsignedInt(var2[var4 * 11 + 5 & 31]);
            var5 ^= var4 * 29 + 93 & 0xFF;
            var3[var4] = (byte)var5;
         }

         return MessageDigest.getInstance(Я2ж).digest(var3);
      } finally {
         Arrays.fill(var0, (byte)0);
         Arrays.fill(var1, (byte)0);
         Arrays.fill(var2, (byte)0);
         Arrays.fill(var3, (byte)0);
      }
   }

   public static boolean Ж(byte[] var0) {
      if (var0 != null && var0.length >= field_4.length + 1) {
         for (int var1 = 0; var1 < field_4.length; var1++) {
            if (var0[var1] != field_4[var1]) {
               return false;
            }
         }

         return var0[field_4.length] == 1;
      } else {
         return false;
      }
   }

   private static void Ж(Path var0, byte[] var1) throws IOException {
      Objects.requireNonNull(var0, Я2l);
      Path var2 = var0.toAbsolutePath().normalize();
      Path var3 = var2.getParent();
      if (var3 == null) {
         throw new IOException("Output path has no parent: " + var0);
      }

      Files.createDirectories(var3);
      Path var4 = Files.createTempFile(var3, var2.getFileName().toString(), Я28);

      try {
         Files.write(var4, var1);

         try {
            Files.move(var4, var2, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException var9) {
            Files.move(var4, var2, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var4);
      }
   }

   private static int ь(int var0, int var1) {
      var0 &= 255;
      var1 &= 7;
      return var1 == 0 ? var0 : (var0 << var1 | var0 >>> 8 - var1) & 0xFF;
   }

   public static InputStream л_5c929562(InputStream var0, String var1) throws GeneralSecurityException, IOException {
      Objects.requireNonNull(var0, Я2Д);
      byte[] var2 = var0.readAllBytes();
      byte[] var3 = Ж(var2) ? б(var2, var1) : var2;
      return new ByteArrayInputStream(var3);
   }

   public static void б(Path var0, Path var1, String var2) throws IOException, GeneralSecurityException {
      Objects.requireNonNull(var0, Я2т);
      byte[] var3 = Files.readAllBytes(var0);
      byte[] var4 = null;

      try {
         var4 = б(var3, var2);
         Ж(var1, var4);
      } finally {
         Arrays.fill(var3, (byte)0);
         if (var4 != null) {
            Arrays.fill(var4, (byte)0);
         }
      }
   }

   private static byte[] method_8() throws GeneralSecurityException {
      byte[] var0 = Й();
      byte[] var1 = Base64.getDecoder().decode(Я2诶);
      byte[] var2 = Base64.getDecoder().decode(Я21);

      try {
         Cipher var3 = Cipher.getInstance(Я2Ь);
         var3.init(2, new SecretKeySpec(var0, Я2衣), new GCMParameterSpec(128, var1));
         var3.updateAAD(п);
         return var3.doFinal(var2);
      } finally {
         Arrays.fill(var0, (byte)0);
         Arrays.fill(var1, (byte)0);
         Arrays.fill(var2, (byte)0);
      }
   }

   public static byte[] л_1be31765(InputStream var0, String var1) throws IOException, GeneralSecurityException {
      Objects.requireNonNull(var0, Я24);
      return л(var0.readAllBytes(), var1);
   }

   public static byte[] л(byte[] var0, String var1) throws GeneralSecurityException {
      Objects.requireNonNull(var0, ЯуС);
      String var2 = method_4Е(var1);
      if (Ж(var0)) {
         throw new IllegalArgumentException(Яуз);
      }

      byte[] var3 = new byte[12];
      Х.nextBytes(var3);
      byte[] var4 = л(var2);

      try {
         Cipher var5 = Cipher.getInstance(Яуц);
         var5.init(1, new SecretKeySpec(var4, Яуя), new GCMParameterSpec(128, var3));
         var5.updateAAD(б(var2));
         byte[] var6 = var5.doFinal(var0);
         ByteBuffer var7 = ByteBuffer.allocate(та + var6.length);
         var7.put(field_4);
         var7.put((byte)1);
         var7.put(var3);
         var7.put(var6);
         return var7.array();
      } finally {
         Arrays.fill(var4, (byte)0);
      }
   }
}
