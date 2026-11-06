package sg.messenger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import sg.network.irc.IrcCustomPackets;
import sg.network.irc.custom.IrcAudience;
import sg.network.irc.custom.IrcPacket;
import sg.network.irc.custom.IrcCustomPacket;
import sg.network.ClientChat;
import sg.network.IrcRoles;
import sg.network.irc.RoseIrc;

public final class MessengerClient {
   private static final MessengerClient В_8dfd5497 = new MessengerClient();
   @SerializedName("В")
   private Channel В_04e5b147;
   @SerializedName("А")
   private boolean А_bbeebd87;
   @SerializedName("4")
   private boolean field_4_bbeebd87;
   @SerializedName("п")
   private boolean п_bbeebd87;
   @SerializedName("В")
   private JsonObject В_e1451646 = new JsonObject();
   @SerializedName("Я")
   private JsonObject Я_e1451646;
   @SerializedName("Х")
   private JsonObject Х_e1451646;
   @SerializedName("Я")
   final Map<String, String> Я_ea411899 = new HashMap<>();
   @SerializedName("В")
   private Runnable В_3382daff;
   @SerializedName("В")
   private long В_6da43b94;
   @SerializedName("Я")
   private long Я_6da43b94;
   @SerializedName("Х")
   private long Х_6da43b94;
   @SerializedName("Я")
   private String Я_388008bb = "";
   @SerializedName("Х")
   private String Х_388008bb;
   @SerializedName("е")
   private String е_388008bb;
   @SerializedName("А")
   private String А_388008bb;
   @SerializedName("4")
   private String field_4_388008bb;
   @SerializedName("е")
   private long е_6da43b94;
   @SerializedName("А")
   private long А_6da43b94;
   @SerializedName("4")
   private long field_4_6da43b94;
   @SerializedName("п")
   private long п_6da43b94;
   private static final String ВЛ8 = "global";
   private static final String ВЛ5 = "nonce";
   private static final String ВЛв = "self";
   private static final String ВЛъ = "self";
   private static final String ВЛМ = "id";
   private static final String ВЛН = "chats";
   private static final String ВЛн = "unread";
   private static final String ВЛЦ = "messages";
   private static final String ВЛч = "hasMore";
   private static final String ВЛЭ = "id";
   private static final String ВЛ必 = "Нет соединения с мессенджером";
   private static final String ВЛщ = "Дождитесь ответа сервера";
   private static final String ВЛГ = "action";
   private static final String ВЛл = "chat";
   private static final String ВЛэ = "chat";
   private static final String ВЛю = "action";
   private static final String ВЛЩ = "chat";
   private static final String ВЛу = "text";
   private static final String ВЛ2 = "id";
   private static final String ВЛф = "reply";
   private static final String ВЛи = "sourceChat";
   private static final String ВЛ9 = "nonce";
   private static final String ВЛо = "nonce";
   private static final String ВЛO = "send";
   private static final String ВЛ7 = "forward";
   private static final String ВЛР = "v";
   private static final String ВЛ3 = "request";
   private static final String ВЛр = "role";
   private static final String ВЛ6 = "chat";
   private static final String ВЛ_ = "chat";
   private static final String ВЛШ = "before";
   private static final String ВЛы = "query";
   private static final String ВЛй = "chat";
   private static final String ВЛЖ = "read";
   private static final int ВЛм = 1000000;
   private static final String ВЛК = "event";
   private static final String ВЛI = "banned";
   private static final String ВЛТ = "until";
   private static final String ВЛО = "notice";
   private static final String ВЛФ = "Доступ к IRC заблокирован";
   private static final String ВЛЙ = "notice";
   private static final String ВЛк = "notice";
   private static final String ВЛг = "request";
   private static final String ВЛ0 = "request";
   private static final String ВЛх = "!";
   private static final String ВЛУ = "nonce";
   private static final String ВЛЗ = "nonce";
   private static final String ВЛЕ = "error";
   private static final String ВЛП = "error";
   private static final String ВЛ西 = "Ошибка сервера";
   private static final long ВЛд = 4000L;
   private static final String ВЛБ = "messages";
   private static final String ВЛС = "self";
   private static final String ВЛз = "Несовместимая версия сервера";
   private static final String ВЛц = "notice";
   private static final String ВЛя = "notice";
   private static final String В诶с = "Некорректный ответ сервера";
   private static final long В诶В = 8000L;
   private static final long В诶Я = 12000L;
   private static final String В诶Х = "Сервер не ответил. Нужна новая сборка Netty-сервера";
   private static final long В诶е = 8000L;
   private static final String В诶А = "action";
   private static final String В诶4 = "profile";
   private static final String В诶п = "skin";
   private static final String В诶Д = "action";
   private static final String В诶Ъ = "sync";
   private static final String В诶т = "Не удалось отправить. Текст сохранён в поле ввода";

   public static long method_4_6da43b94(JsonObject var0, String var1) {
      return var0.has(var1) ? var0.get(var1).getAsLong() : 0L;
   }

   public String method_1_388008bb() {
      return this.Я_388008bb;
   }

   public String Й() {
      return method_4(method_4_e1451646(this.В_e1451646, ВЛъ), ВЛМ, "");
   }

   public boolean я() {
      return this.field_4_6da43b94 > 0L;
   }

   public static JsonObject method_4_e1451646(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonObject() ? var0.getAsJsonObject(var1) : new JsonObject();
   }

   public boolean п() {
      return this.Ы_bbeebd87() && this.В_e1451646.has(ВЛв);
   }

   public JsonObject method_4_e1451646() {
      return this.В_e1451646;
   }

   public void т() {
      this.method_9();
      this.Я_6da43b94 = 0L;
      RoseIrc var1 = RoseIrc.З_3e6e5516();
      if (var1 != null && !var1.Щ6() && System.currentTimeMillis() >= this.А_6da43b94) {
         var1.жв();
      }
   }

   public void Ч() {
      JsonArray var1 = method_4_fea5d33f(this.В_e1451646, ВЛЦ);
      if (!var1.isEmpty() && method_4_bbeebd87(this.В_e1451646, ВЛч)) {
         this.field_4_6da43b94 = method_4_6da43b94(var1.get(0).getAsJsonObject(), ВЛЭ);
         this.Я_6da43b94 = 0L;
      }
   }

   public static String method_4(JsonObject var0, String var1, String var2) {
      return var0.has(var1) && !var0.get(var1).isJsonNull() ? var0.get(var1).getAsString() : var2;
   }

   public boolean Ы_bbeebd87() {
      return this.field_4_bbeebd87 && this.В_04e5b147 != null && this.В_04e5b147.isActive() && RoseIrc.З_3e6e5516().Щ6();
   }

   public void method_6(String var1) {
      this.е_388008bb = var1;
      this.Я_6da43b94 = 0L;
   }

   public long method_4_6da43b94() {
      return this.Х_6da43b94;
   }

   public int method_4_a83dd0cc() {
      int var1 = 0;

      for (JsonElement var3 : method_4_fea5d33f(this.В_e1451646, ВЛН)) {
         var1 += (int)method_4_6da43b94(var3.getAsJsonObject(), ВЛн);
      }

      return var1;
   }

   private void method_4(JsonObject var1, Runnable var2) {
      var1.addProperty(ВЛР, 1);
      var1.addProperty(ВЛ3, UUID.randomUUID().toString());
      var1.addProperty(ВЛр, IrcRoles.Яп());
      if (!var1.has(ВЛ6)) {
         var1.addProperty(ВЛ_, this.Х_388008bb);
      }

      var1.addProperty(ВЛШ, this.field_4_6da43b94);
      var1.addProperty(ВЛы, this.е_388008bb);
      if (Minecraft.getInstance().gui.screen() instanceof MessengerScreen && this.п_6da43b94 > 0L && this.Х_388008bb.equals(method_4(var1, ВЛй, ""))) {
         var1.addProperty(ВЛЖ, this.п_6da43b94);
      }

      this.Я_e1451646 = var1;
      this.В_3382daff = var2;
      this.В_6da43b94 = System.currentTimeMillis();
      this.Я_388008bb = "";
      Channel var3 = this.В_04e5b147;
      var3.writeAndFlush(new MessengerClient.MessengerPayloadHandler(var1.toString(), null)).addListener(var3x -> {
         if (!var3x.isSuccess()) {
            Minecraft.getInstance().execute(() -> {
               if (this.В_04e5b147 == var3 && this.Я_e1451646 == var1) {
                  this.Я_e1451646 = null;
                  this.В_3382daff = null;
                  this.Я_388008bb = В诶т;
               }
            });
         }
      });
   }

   public static boolean method_4_bbeebd87(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).getAsBoolean();
   }

   public void method_4_de5a6f78(String var1) {
      this.Я_388008bb = var1;
   }

   public MessengerClient() {
      this.Х_388008bb = ВЛ8;
      this.е_388008bb = "";
      this.А_388008bb = "";
      this.field_4_388008bb = "";
   }

   public static MessengerClient method_4_8dfd5497() {
      return В_8dfd5497;
   }

   public boolean method_4_bbeebd87(String var1) {
      return this.Я_e1451646 != null && var1.equals(method_4(this.Я_e1451646, ВЛ5, ""));
   }

   public void 诶(String var1) {
      this.Х_388008bb = var1;
      this.field_4_6da43b94 = this.п_6da43b94 = 0L;
      this.Я_6da43b94 = 0L;
      this.Я_388008bb = "";
   }

   private void 必(String var1) {
      this.А_388008bb = var1;
      this.е_6da43b94 = System.currentTimeMillis() + В诶В;
   }

   private void method_4(Channel var1, String var2) {
      if (this.В_04e5b147 == var1 && var2 != null && var2.length() <= ВЛм) {
         try {
            JsonObject var3 = JsonParser.parseString(var2).getAsJsonObject();
            String var4 = method_4(var3, ВЛК, "");
            if (var4.equals(ВЛI)) {
               this.А_6da43b94 = method_4_6da43b94(var3, ВЛТ);
               this.field_4_388008bb = method_4(var3, ВЛО, ВЛФ);
               this.Я_e1451646 = null;
               this.В_3382daff = null;
               this.Я_388008bb = this.field_4_388008bb;
               this.В_e1451646 = new JsonObject();
               this.Х_6da43b94++;
               ClientChat.З(this.field_4_388008bb);
               return;
            }

            if (var4.equals(ВЛЙ)) {
               this.必(method_4(var3, ВЛк, ""));
               ClientChat.З(this.А_388008bb);
               return;
            }

            if (this.Я_e1451646 == null || !method_4(this.Я_e1451646, ВЛг, "").equals(method_4(var3, ВЛ0, ВЛх))) {
               return;
            }

            Runnable var5 = this.В_3382daff;
            if (this.Х_e1451646 != null && Objects.equals(this.Я_e1451646.get(ВЛУ), this.Х_e1451646.get(ВЛЗ))) {
               this.Х_e1451646 = null;
            }

            this.Я_e1451646 = null;
            this.В_3382daff = null;
            if (var3.has(ВЛЕ)) {
               this.Я_388008bb = method_4(var3, ВЛП, ВЛ西);
               this.Я_6da43b94 = System.currentTimeMillis() + ВЛд;
               return;
            }

            if (!var3.has(ВЛБ) || !var3.has(ВЛС)) {
               this.Я_388008bb = ВЛз;
               return;
            }

            this.В_e1451646 = var3;
            this.Х_6da43b94++;
            this.Я_388008bb = "";
            if (var3.has(ВЛц)) {
               this.必(method_4(var3, ВЛя, ""));
            }

            if (var5 != null) {
               var5.run();
            }
         } catch (RuntimeException var6) {
            this.Я_e1451646 = null;
            this.В_3382daff = null;
            this.Я_388008bb = В诶с;
         }
      }
   }

   public boolean method_4(String var1, JsonObject var2, Runnable var3) {
      if (System.currentTimeMillis() < this.А_6da43b94) {
         this.Я_388008bb = this.field_4_388008bb;
         return false;
      }

      if (!this.Ы_bbeebd87()) {
         this.Я_388008bb = ВЛ必;
         return false;
      }

      if (this.Я_e1451646 != null) {
         this.Я_388008bb = ВЛщ;
         return false;
      }

      var2.addProperty(ВЛГ, var1);
      if (!var2.has(ВЛл)) {
         var2.addProperty(ВЛэ, this.Х_388008bb);
      }

      boolean var4 = this.Х_e1451646 != null;

      for (String var6 : List.of(ВЛю, ВЛЩ, ВЛу, ВЛ2, ВЛф, ВЛи)) {
         if (var4 && !Objects.equals(var2.get(var6), this.Х_e1451646.get(var6))) {
            var4 = false;
         }
      }

      var2.addProperty(ВЛ9, var4 ? method_4(this.Х_e1451646, ВЛо, UUID.randomUUID().toString()) : UUID.randomUUID().toString());
      if (var1.equals(ВЛO) || var1.equals(ВЛ7)) {
         this.Х_e1451646 = var2.deepCopy();
         this.field_4_6da43b94 = 0L;
      }

      this.method_4(var2, var3);
      return true;
   }

   private void method_4(Minecraft var1) {
      RoseIrc var2 = RoseIrc.З_3e6e5516();
      Channel var3 = var2 == null ? null : var2.Й();
      if (var3 != null && !var3.isActive()) {
         var3 = null;
      }

      if (var3 != this.В_04e5b147) {
         this.В_04e5b147 = var3;
         this.field_4_bbeebd87 = this.п_bbeebd87 = false;
         this.Я_e1451646 = null;
         this.В_3382daff = null;
         this.В_e1451646 = new JsonObject();
         this.Х_6da43b94++;
         this.п_6da43b94 = this.field_4_6da43b94 = 0L;
         if (var3 != null) {
            Channel var4 = var3;
            var4.eventLoop().execute(() -> {
               if (var2.Й() == var4) {
                  IrcCustomPackets var4x = var2.З_677a0540().М_4c863fd0();
                  var4x.removeIf(var0 -> var0 instanceof MessengerClient.MessengerPayloadHandler);
                  var4x.add(new MessengerClient.MessengerPayloadHandler("", var3xx -> var1.execute(() -> this.method_4(var4, var3xx))));
                  var1.execute(() -> {
                     if (this.В_04e5b147 == var4) {
                        this.field_4_bbeebd87 = true;
                        this.Я_6da43b94 = 0L;
                     }
                  });
               }
            });
         }
      }

      long var7 = System.currentTimeMillis();
      if (var7 < this.А_6da43b94) {
         this.Я_388008bb = this.field_4_388008bb;
      } else {
         if (this.А_6da43b94 > 0L) {
            this.А_6da43b94 = 0L;
            this.field_4_388008bb = "";
            this.Я_388008bb = "";
            this.Я_6da43b94 = 0L;
         }

         if (this.Я_e1451646 != null && var7 - this.В_6da43b94 > В诶Я) {
            this.Я_e1451646 = null;
            this.В_3382daff = null;
            this.Я_388008bb = В诶Х;
            this.Я_6da43b94 = var7 + В诶е;
         }

         if (this.Ы_bbeebd87() && this.Я_e1451646 == null && var7 >= this.Я_6da43b94) {
            this.Я_6da43b94 = var7 + (var1.gui.screen() instanceof MessengerScreen ? 2000 : 10000);
            JsonObject var6 = new JsonObject();
            if (!this.п_bbeebd87) {
               this.п_bbeebd87 = true;
               var6.addProperty(В诶А, В诶4);
               var6.addProperty(В诶п, var1.getUser().getProfileId().toString());
            } else {
               var6.addProperty(В诶Д, В诶Ъ);
            }

            this.method_4(var6, null);
         }
      }
   }

   public static JsonArray method_4_fea5d33f(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonArray() ? var0.getAsJsonArray(var1) : new JsonArray();
   }

   public String Ы_388008bb() {
      return System.currentTimeMillis() < this.е_6da43b94 ? this.А_388008bb : "";
   }

   public boolean method_1_bbeebd87() {
      return this.Я_e1451646 != null;
   }

   public void Ф() {
      this.field_4_6da43b94 = 0L;
      this.Я_6da43b94 = 0L;
   }

   public void method_9() {
      if (!this.А_bbeebd87) {
         this.А_bbeebd87 = true;
         ClientTickEvents.END_CLIENT_TICK.register(this::method_4);
      }
   }

   public void method_4(long var1) {
      if (this.field_4_6da43b94 == 0L && var1 > this.п_6da43b94) {
         this.п_6da43b94 = var1;
         this.Я_6da43b94 = 0L;
      }
   }

   public String 必() {
      return this.Х_388008bb;
   }

   private static final class MessengerPayloadHandler extends IrcCustomPacket {
      private final Consumer<String> 诶;
      private static final String ВЛ衣 = "rose_messenger_v1";

      @Override
      public void Жу() {
         if (this.诶 != null) {
            this.诶.accept(this.method_0().method_1п());
         }
      }

      MessengerPayloadHandler(String var1, Consumer<String> var2) {
         super(new IrcPacket(ВЛ衣, IrcAudience.USER_ONLY, var1));
         this.诶 = var2;
      }
   }
}
