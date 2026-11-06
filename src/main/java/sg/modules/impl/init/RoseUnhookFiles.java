package sg.modules.impl.init;

import com.google.gson.annotations.SerializedName;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import net.minecraft.client.Minecraft;
import sg.command.KeyMacro;
import sg.ec.UnhookState;
import sg.network.ClientChat;
import sg.settings.BooleanSetting;
import sg.client.ClientServices;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.settings.ChoiceOption;
import sg.settings.KeybindSetting;
import sg.settings.MultiChoiceSetting;
import sg.settings.SingleChoiceSetting;
import sg.settings.ValueSetting;

public final class RoseUnhookFiles {
   private static final String з = "0123456789";
   private static final int Вп = 4;
   private static final Logger В_699e860c = System.getLogger(RoseUnhookFiles.class.getName());
   private final SecureRandom Я = new SecureRandom();
   @SerializedName("В")
   private final UnhookFileManager В_2359e981 = new UnhookFileManager();
   @SerializedName("В")
   private final UnhookDisplayState В_213690c7 = new UnhookDisplayState();
   @SerializedName("В")
   private final Executor В_a2b90f18 = Executors.newSingleThreadExecutor(var0 -> {
      Thread var1 = new Thread(var0, Вдм);
      var1.setDaemon(true);
      return var1;
   });
   private final Set<ClientModule> Ч = Collections.newSetFromMap(new IdentityHashMap<>());
   @SerializedName("5")
   private final Map<ClientModule, Integer> field_5 = new IdentityHashMap<>();
   private final Map<KeybindSetting, Integer> в = new IdentityHashMap<>();
   @SerializedName("ъ")
   private final Map<BooleanSetting, Integer> ъ_ea411899 = new IdentityHashMap<>();
   private final Map<ChoiceOption, Integer> М = new IdentityHashMap<>();
   @SerializedName("ъ")
   private List<KeyMacro> ъ_2571477d = List.of();
   private Map<String, Integer> Н = Map.of();
   private boolean сЮ;
   private float Вч;
   private volatile boolean сЧ;
   private volatile boolean сЛ;
   private volatile String ц;
   private static final long Вдо = 10000L;
   private static final String ВдO = "Failed to prepare unhook files";
   private static final String Вд7 = "Восстановление клиента...";
   private static final long ВдР = 10000L;
   private static final String Вд3 = "Не удалось восстановить файлы. Введи код восстановления ещё раз, чтобы повторить.";
   private static final long Вдр = 10000L;
   private static final String Вд6 = "Unhook отключён. Клиент восстановлен.";
   private static final String Вд_ = "0123456789";
   private static final String ВдШ = "0123456789";
   private static final String Вды = "Failed to restore unhook files";
   private static final String Вдй = "Не удалось подготовить файлы для Unhook. Восстанавливаем клиент.";
   private static final long ВдЖ = 10000L;
   private static final String Вдм = "rose-unhook-files";

   private void х(ClientModule var1) {
      for (ValueSetting var3 : var1.getSettings()) {
         if (var3 instanceof KeybindSetting var9) {
            var9.setValue(-1);
         } else if (var3 instanceof BooleanSetting var8) {
            var8.getKeybind().setKeyCode(-1);
         } else if (var3 instanceof SingleChoiceSetting var7) {
            for (ChoiceOption var11 : var7.getValue()) {
               var11.getKeybind().setKeyCode(-1);
            }
         } else if (var3 instanceof MultiChoiceSetting var4) {
            for (ChoiceOption var6 : var4.getValue()) {
               var6.getKeybind().setKeyCode(-1);
            }
         }
      }
   }

   private synchronized void хб() {
      ClientServices var1 = RoseClient.getInstance().getServices();
      this.field_5.forEach((var0, var1x) -> var0.getKeybind().setKeyCode(var1x));
      this.в.forEach(ValueSetting::setValue);
      this.ъ_ea411899.forEach((var0, var1x) -> var0.getKeybind().setKeyCode(var1x));
      this.М.forEach((var0, var1x) -> var0.getKeybind().setKeyCode(var1x));
      var1.getCommandShortcuts().clear();
      var1.getCommandShortcuts().addAll(this.ъ_2571477d);
      var1.getSoundPadManager().н(this.Н, this.сЮ, this.Вч);
      this.ц = null;

      for (ClientModule var3 : this.Ч) {
         if (!var3.isEnabled()) {
            var3.enable();
         }
      }

      this.хз();
      this.сЛ = false;
      this.сЧ = false;
      UnhookState.ч(false);
      this.В_213690c7.method_4();
      ClientChat.З(Вд6);
   }

   private void хз() {
      this.Ч.clear();
      this.field_5.clear();
      this.в.clear();
      this.ъ_ea411899.clear();
      this.М.clear();
      this.ъ_2571477d = List.of();
      this.Н = Map.of();
   }

   public synchronized void хЙ() {
      if (this.сЧ && !this.сЛ) {
         this.сЛ = true;
         ClientChat.method_2(Вд7, ВдР);
         this.В_a2b90f18.execute(() -> {
            try {
               this.В_2359e981.method_9Ф();
            } catch (RuntimeException var2) {
               В_699e860c.log(Level.ERROR, Вды, var2);
               Minecraft.getInstance().execute(this::хO);
               return;
            }

            Minecraft.getInstance().execute(this::хб);
         });
      }
   }

   private void х(ValueSetting<?> var1) {
      if (var1 instanceof KeybindSetting var7) {
         this.в.put(var7, var7.getValue());
      } else if (var1 instanceof BooleanSetting var6) {
         this.ъ_ea411899.put(var6, var6.getKeybind().getKeyCode());
      } else if (var1 instanceof SingleChoiceSetting var5) {
         for (ChoiceOption var9 : var5.getValue()) {
            this.М.put(var9, var9.getKeybind().getKeyCode());
         }
      } else {
         if (var1 instanceof MultiChoiceSetting var2) {
            for (ChoiceOption var4 : var2.getValue()) {
               this.М.put(var4, var4.getKeybind().getKeyCode());
            }
         }
      }
   }

   private void хИ() {
      try {
         this.В_2359e981.method_6(() -> this.сЛ);
      } catch (RuntimeException var2) {
         В_699e860c.log(Level.ERROR, ВдO, var2);
         Minecraft.getInstance().execute(() -> {
            ClientChat.method_2(Вдй, ВдЖ);
            this.хЙ();
         });
      }
   }

   public synchronized String ам() {
      if (this.сЧ) {
         return this.ц;
      }

      ClientServices var1 = RoseClient.getInstance().getServices();
      this.хз();

      for (ClientModule var3 : var1.getModules()) {
         this.field_5.put(var3, var3.getKeybind().getKeyCode());
         if (var3.isEnabled()) {
            this.Ч.add(var3);
         }

         for (ValueSetting var5 : var3.getSettings()) {
            this.х(var5);
         }
      }

      this.ъ_2571477d = new ArrayList<>(var1.getCommandShortcuts());
      SoundPadManager var6 = var1.getSoundPadManager();
      this.Н = new LinkedHashMap<>(var6.Х());
      this.сЮ = var6.ЙЫ();
      this.Вч = var6.б4();
      this.ц = this.аЮ();
      this.сЧ = true;
      UnhookState.ч(true);

      for (ClientModule var9 : this.Ч) {
         if (var9.isEnabled()) {
            var9.disable();
         }
      }

      for (ClientModule var10 : var1.getModules()) {
         var10.getKeybind().setKeyCode(-1);
         this.х(var10);
      }

      var1.getCommandShortcuts().clear();
      var6.н(Map.of(), this.сЮ, this.Вч);
      var6.уП();
      this.В_213690c7.method_2();
      ClientChat.method_2("Unhook включён. Код восстановления: " + this.ц + ". Введи его обычным сообщением в чат, чтобы вернуть клиент.", Вдо);
      this.В_a2b90f18.execute(this::хИ);
      return this.ц;
   }

   private synchronized void хO() {
      this.сЛ = false;
      ClientChat.method_2(Вд3, Вдр);
   }

   public boolean аА() {
      return this.сЧ;
   }

   public String аЬ() {
      return this.ц;
   }

   private String аЮ() {
      StringBuilder var1 = new StringBuilder(4);

      for (int var2 = 0; var2 < 4; var2++) {
         var1.append(Вд_.charAt(this.Я.nextInt(ВдШ.length())));
      }

      return var1.toString();
   }

   public synchronized boolean ь(String var1) {
      if (this.сЧ && var1 != null) {
         String var2 = var1.trim();
         if (this.ц != null && this.ц.equalsIgnoreCase(var2)) {
            this.хЙ();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
