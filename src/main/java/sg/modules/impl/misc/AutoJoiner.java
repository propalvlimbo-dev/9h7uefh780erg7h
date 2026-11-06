package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.PlayerTeam;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.network.ClientChat;
import sg.util.time.Stopwatch;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;

public class AutoJoiner extends ClientModule {
   private static final String Я9 = "выбор сервера";
   private static final String Яо = "гриф выживание (1.16.5)";
   private static final String ЯO = "гриф выживание (1.21.11)";
   private static final String Я7 = "следующая страница";
   private static final String ЯР = "выбор мира грифа";
   private static final String Я3 = "выбор мира грифа 1/3";
   private static final String Яр = "выбор мира грифа 2/3";
   private static final String Я6 = "выбор мира грифа 3/3";
   private static final Pattern ш = Pattern.compile(AutoJoiner.Хll);
   private static final Pattern И = Pattern.compile(AutoJoiner.Хl8);
   private static final Pattern ь = Pattern.compile(AutoJoiner.Хl5);
   private final SingleChoiceSetting с诶;
   private final FloatSetting Хю;
   private final FloatSetting ХЩ;
   private final FloatSetting Ху;
   private final Stopwatch Э;
   private final Stopwatch 必;
   private boolean еЖ;
   private boolean ем;
   private boolean еК;
   private boolean еI;
   @SerializedName("В")
   private AutoJoiner.ServerKind В_c1c2aab4;
   @SerializedName("В")
   private AutoJoiner.HolyWorldSelection В_f514e7a3;
   @SerializedName("В")
   private AutoJoiner.JoinStage В_ba8dcff0;
   private int Др;
   @SerializedName("В")
   private ContainerScreen В_1fa4a375;
   @SerializedName("5")
   private ClientLevel field_5;
   @SerializedName("в")
   private ClientLevel в_at_fde4c7fc;
   private boolean еТ;
   private boolean еО;
   private ClientLevel ъ;
   private long ХШ;
   private long Хы;
   private volatile int Д6;
   private volatile boolean еФ;
   private final EventListener<PacketReceiveEvent> Хж;
   private final EventListener<PlayerTickEvent> Х弟;
   private static final String ХЫм = "AutoJoiner";
   private static final String ХЫК = "Авто заход на гриф RW";
   private static final String ХЫI = "Режим";
   private static final String ХЫТ = "Reallyworld 1.16.5";
   private static final String ХЫО = "Reallyworld 1.21.11";
   private static final String ХЫФ = "Гриф";
   private static final float ХЫЙ = 96.0F;
   private static final String ХЫк = "Гриф 1.21.11";
   private static final float ХЫг = 24.0F;
   private static final String ХЫ0 = "Скорость";
   private static final float ХЫх = 3.0F;
   private static final float ХЫУ = 20.0F;
   private static final long ХЫЗ = 1000L;
   private static final long ХЫЕ = 400L;
   private static final long ХЫП = 100L;
   private static final long ХЫ西 = 100L;
   private static final String ХЫд = "выбор сервера";
   private static final String ХЫБ = "гриф выживание (1.21.11)";
   private static final String ХЫС = "гриф выживание (1.16.5)";
   private static final String ХЫз = "следующая страница";
   private static final String ХЫц = "hub";
   private static final String ХЫя = "hub";
   private static final String Хlс = "лайт";
   private static final String ХlВ = "light";
   private static final String ХlЯ = "анарх";
   private static final String ХlХ = "anarch";
   private static final String Хlе = "выбор сервера";
   private static final String ХlА = "выбор мира грифа";
   private static final String Хl4 = "гриф выживание (1.16.5)";
   private static final String Хlп = "гриф выживание (1.21.11)";
   private static final String ХlД = "\\n";
   private static final String ХlЪ = " ";
   private static final String Хlт = "не удалось подключиться к серверу newgrief-";
   private static final String Хlа = "не удалось подключиться к серверу grief-";
   private static final String ХlЮ = "большой поток игроков";
   private static final String ХlЧ = "сервер переполнен";
   private static final String ХlЛ = "reallyworld";
   private static final String Хl诶 = "подождите несколько секунд";
   private static final String Хl1 = "подключением";
   private static final long ХlЬ = 3000L;
   private static final long Хl衣 = 100L;
   private static final String Хlш = "AutoJoiner -> Ошибка при заходе на гриф. Ошибка: большой поток.";
   private static final String ХlИ = "AutoJoiner -> Ошибка при заходе на гриф. Ошибка: сервер переполнен.";
   private static final String Хlь = "Reallyworld 1.21.11";
   private static final String Хlж = "выбор мира грифа";
   private static final String Хl弟 = "выбор мира грифа 2/3";
   private static final String Хlб = "выбор мира грифа 3/3";
   private static final String ХlЫ = "выбор мира грифа 1/3";
   private static final String Хll = "(?i)гриф\\s*#\\s*(\\d+)";
   private static final String Хl8 = "(?:#|№)\\s*(\\d+)";
   private static final String Хl5 = "(?<!\\d)1[.,]16(?!\\d)";

   private void method_9(AutoJoiner.JoinFailure var1) {
      long var2 = var1 == AutoJoiner.JoinFailure.COOLDOWN ? ХlЬ : this.Ху.getValue().intValue() * Хl衣;
      this.Хы = Math.max(this.Хы, System.currentTimeMillis() + var2);
      this.Э.ВД();
      this.必.ВД();
      if (var1 == AutoJoiner.JoinFailure.BUSY) {
         ClientChat.К(Хlш);
      } else if (var1 == AutoJoiner.JoinFailure.FULL) {
         ClientChat.К(ХlИ);
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.ЗЩ();
      this.Э.ВД();
      this.必.ВД();
      this.еЖ = false;
      this.еI = false;
      this.Др = -1;
      if (!this.ем) {
         this.еК = true;
         if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
            this.ЗЧ();
         }

         if (this.еК) {
            this.OЮ();
         }
      }
   }

   private void method_9(ChestMenu var1) {
      if (this.В_f514e7a3 != null && this.В_ba8dcff0 != AutoJoiner.JoinStage.IDLE && this.В_ba8dcff0 != AutoJoiner.JoinStage.WAITING_FOR_HUB) {
         if (var1.containerId != this.Др) {
            Slot var2 = this.method_9(var1, true);
            if (var2 == null) {
               var2 = this.method_9(var1, false);
            }

            if (var2 != null) {
               if (this.В_ba8dcff0 == AutoJoiner.JoinStage.SELECT_SERVER) {
                  this.ЗФ();
               }

               this.Ж西(var2.index);
               this.Др = var1.containerId;
               this.Э.ВД();

               this.В_ba8dcff0 = switch (this.В_ba8dcff0) {
                  case SELECT_LIGHT -> AutoJoiner.JoinStage.SELECT_CATEGORY;
                  case SELECT_CATEGORY -> AutoJoiner.JoinStage.SELECT_SERVER;
                  case SELECT_SERVER -> AutoJoiner.JoinStage.COMPLETE;
                  default -> this.В_ba8dcff0;
               };
            }
         }
      }
   }

   private static String method_0(String var0) {
      String var1 = StringUtil.stripColor(var0);
      if (var1 == null) {
         var1 = var0;
      }

      return var1.toLowerCase(Locale.ROOT).replace('ё', 'е');
   }

   private static String А(ItemStack var0) {
      StringBuilder var1 = new StringBuilder(var0.getHoverName().getString());
      ItemLore var2 = var0.getComponents().get(DataComponents.LORE);
      if (var2 != null) {
         for (Component var4 : var2.lines()) {
            var1.append(' ').append(var4.getString());
         }
      }

      return method_0(var1.toString());
   }

   public boolean Е(AutoJoiner.HolyWorldSelection var1) {
      if (var1 != null && var1.number() >= 1) {
         this.В_c1c2aab4 = AutoJoiner.ServerKind.HOLY_WORLD;
         this.В_f514e7a3 = var1;
         this.В_ba8dcff0 = AutoJoiner.JoinStage.WAITING_FOR_HUB;
         this.ЗЩ();
         this.Др = -1;
         this.ем = true;
         this.еК = true;
         this.еI = false;
         this.еЖ = false;
         this.Э.ВД();
         this.必.ВД();
         if (!this.isEnabled()) {
            this.enable();
         }

         return true;
      } else {
         return false;
      }
   }

   public AutoJoiner() {
      super(ХЫм, ХЫК, ModuleCategory.MISC);
      this.с诶 = new SingleChoiceSetting(ХЫI, new ChoiceOption(ХЫТ), new ChoiceOption(ХЫО));
      this.Хю = new FloatSetting(ХЫФ, 1.0F, 1.0F, ХЫЙ).withStep(1.0F).visibleWhen(() -> !this.OЩ());
      this.ХЩ = new FloatSetting(ХЫк, 1.0F, 1.0F, ХЫг).withStep(1.0F).visibleWhen(this::OЩ);
      this.Ху = new FloatSetting(ХЫ0, ХЫх, 1.0F, ХЫУ).withStep(1.0F);
      this.Э = new Stopwatch();
      this.必 = new Stopwatch();
      this.В_c1c2aab4 = AutoJoiner.ServerKind.REALLY_WORLD;
      this.В_ba8dcff0 = AutoJoiner.JoinStage.IDLE;
      this.Др = -1;
      this.Хж = this::н;
      this.Х弟 = this::ЖН;
      this.addSettings(this.с诶, this.Хю, this.ХЩ, this.Ху);
   }

   private void Ж西(int var1) {
      this.ЖЯ(var1);
      AbstractContainerMenu var2 = MINECRAFT_CLIENT.player.containerMenu;
      if (!var2.getCarried().isEmpty()) {
         this.ЖЯ(var1);
      }
   }

   private void ЖК_de5a6f78(String var1) {
      int var2 = this.ЖЗ().getValue().intValue();
      String var3 = this.衣(var1);
      if (this.OЩ()) {
         this.еЖ = var3.contains(Хlж);
      } else {
         if (var2 >= 33 && var2 <= 64) {
            this.еЖ = var3.contains(Хl弟);
         } else if (var2 >= 65) {
            this.еЖ = var3.contains(Хlб);
         } else {
            this.еЖ = var3.contains(ХlЫ);
         }
      }
   }

   public boolean ы(int var1) {
      if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.player.connection == null) {
         return false;
      }

      if (!this.method_7(var1)) {
         return false;
      }

      this.еО = true;
      this.ъ = MINECRAFT_CLIENT.level;
      MINECRAFT_CLIENT.player.connection.sendCommand(ХЫц);
      return true;
   }

   private void ЗЩ() {
      this.Д6++;
      this.В_1fa4a375 = null;
      this.field_5 = null;
      this.в_at_fde4c7fc = null;
      this.еО = false;
      this.ъ = null;
      this.ХШ = 0L;
      this.еТ = false;
      this.Хы = 0L;
      this.еФ = this.isEnabled() && this.В_c1c2aab4 == AutoJoiner.ServerKind.REALLY_WORLD;
   }

   private static boolean М(String var0, int var1) {
      Matcher var2 = И.matcher(var0);

      while (var2.find()) {
         try {
            if (Integer.parseInt(var2.group(1)) == var1) {
               return true;
            }
         } catch (NumberFormatException var4) {
            return false;
         }
      }

      return false;
   }

   private static boolean ЖO(String var0) {
      return ь.matcher(var0).find();
   }

   public boolean М(AbstractContainerScreen<?> var1) {
      return this.isEnabled()
         && MINECRAFT_CLIENT.player != null
         && MINECRAFT_CLIENT.level != null
         && MINECRAFT_CLIENT.player.isAlive()
         && !MINECRAFT_CLIENT.player.isRemoved()
         && !this.еТ
         && !this.еО
         && (!this.еI || this.в_at_fde4c7fc == MINECRAFT_CLIENT.level)
         && MINECRAFT_CLIENT.player.containerMenu == var1.getMenu()
         && this.Е(var1);
   }

   public boolean method_7(int var1) {
      FloatSetting var2 = this.ЖЗ();
      if (!(var1 < var2.getMinimum()) && !(var1 > var2.getMaximum())) {
         var2.setValue((float)var1);
         this.В_c1c2aab4 = AutoJoiner.ServerKind.REALLY_WORLD;
         this.В_f514e7a3 = null;
         this.В_ba8dcff0 = AutoJoiner.JoinStage.IDLE;
         this.ЗЩ();
         this.Др = -1;
         this.ем = true;
         this.еК = true;
         this.еI = false;
         this.еЖ = false;
         this.Э.ВД();
         this.必.ВД();
         if (!this.isEnabled()) {
            this.enable();
         }

         return true;
      } else {
         return false;
      }
   }

   private void ЗФ() {
      if (!this.еI) {
         this.в_at_fde4c7fc = MINECRAFT_CLIENT.level;
         this.ХШ = System.currentTimeMillis();
      }

      this.еI = true;
   }

   private String 衣(String var1) {
      return method_0(var1);
   }

   private int 弟(Item var1) {
      if (MINECRAFT_CLIENT.player == null) {
         return -1;
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (MINECRAFT_CLIENT.player.getInventory().getItem(var2).is(var1)) {
            return var2;
         }
      }

      return -1;
   }

   private static boolean Жп(String var0) {
      return var0.contains(Хlс) || var0.contains(ХlВ);
   }

   private static AutoJoiner.HolyWorldSelection method_9(net.minecraft.world.scores.Scoreboard var0, Objective var1) {
      if (var1 == null) {
         return null;
      }

      AutoJoiner.HolyWorldSelection var2 = method_9_f514e7a3(var1.getDisplayName().getString());
      if (var2 != null) {
         return var2;
      }

      for (PlayerScoreEntry var4 : var0.listPlayerScores(var1)) {
         var2 = method_9_f514e7a3(var4.ownerName().getString());
         if (var2 != null) {
            return var2;
         }

         MutableComponent var5 = PlayerTeam.formatNameForTeam(var0.getPlayersTeam(var4.owner()), var4.ownerName());
         var2 = method_9_f514e7a3(var5.getString());
         if (var2 != null) {
            return var2;
         }

         var2 = method_9_f514e7a3(var4.owner());
         if (var2 != null) {
            return var2;
         }
      }

      return null;
   }

   private static AutoJoiner.HolyWorldSelection method_9_f514e7a3(String var0) {
      String var1 = method_0(var0);
      if (!Жп(var1)) {
         return null;
      }

      AutoJoiner.HolyWorldCategory var2 = AutoJoiner.HolyWorldCategory.method_3(var1);
      if (var2 == null) {
         return null;
      }

      Matcher var3 = И.matcher(var1);
      if (!var3.find()) {
         return null;
      }

      try {
         return new AutoJoiner.HolyWorldSelection(var2, Integer.parseInt(var3.group(1)));
      } catch (NumberFormatException var5) {
         return null;
      }
   }

   public static int method_9С() {
      if (MINECRAFT_CLIENT.level == null) {
         return -1;
      }

      for (Objective var1 : MINECRAFT_CLIENT.level.getScoreboard().getObjectives()) {
         String var2 = var1.getDisplayName().getString();
         String var3 = StringUtil.stripColor(var2);
         if (var3 == null) {
            var3 = var2;
         }

         Matcher var4 = ш.matcher(var3);
         if (var4.find()) {
            try {
               return Integer.parseInt(var4.group(1));
            } catch (NumberFormatException var6) {
               return -1;
            }
         }
      }

      return -1;
   }

   private boolean Е(AbstractContainerScreen<?> var1) {
      if (!(var1 instanceof ContainerScreen)) {
         return false;
      }

      if (this.В_c1c2aab4 == AutoJoiner.ServerKind.HOLY_WORLD) {
         return this.ем && this.В_ba8dcff0 != AutoJoiner.JoinStage.IDLE && this.В_ba8dcff0 != AutoJoiner.JoinStage.WAITING_FOR_HUB;
      }

      String var2 = this.衣(var1.getTitle().getString());
      return var2.contains(Хlе) || var2.contains(ХlА) || var2.contains(Хl4) || var2.contains(Хlп);
   }

   private boolean OЩ() {
      return this.с诶.isSelected(Хlь);
   }

   public static AutoJoiner.HolyWorldSelection method_9() {
      if (MINECRAFT_CLIENT.level == null) {
         return null;
      }

      net.minecraft.world.scores.Scoreboard var0 = MINECRAFT_CLIENT.level.getScoreboard();
      Objective var1 = var0.getDisplayObjective(DisplaySlot.SIDEBAR);
      AutoJoiner.HolyWorldSelection var2 = method_9(var0, var1);
      if (var2 != null) {
         return var2;
      }

      for (Objective var4 : var0.getObjectives()) {
         if (var4 != var1) {
            var2 = method_9(var0, var4);
            if (var2 != null) {
               return var2;
            }
         }
      }

      return null;
   }

   private boolean OФ() {
      return this.В_c1c2aab4 != AutoJoiner.ServerKind.HOLY_WORLD
         ? method_9С() == this.ЖЗ().getValue().intValue()
         : this.В_f514e7a3 != null && this.В_f514e7a3.equals(method_9());
   }

   private FloatSetting ЖЗ() {
      return this.OЩ() ? this.ХЩ : this.Хю;
   }

   private static boolean ЖК_bbeebd87(String var0) {
      return var0.contains(ХlЯ) || var0.contains(ХlХ);
   }

   private Slot method_9(ChestMenu var1, boolean var2) {
      for (Slot var4 : var1.slots) {
         if (var4.container != MINECRAFT_CLIENT.player.getInventory() && var4.hasItem()) {
            String var5 = var2 ? method_0(var4.getItem().getHoverName().getString()) : А(var4.getItem());

            boolean var6 = switch (this.В_ba8dcff0) {
               case SELECT_LIGHT -> Жп(var5) && ЖК_bbeebd87(var5) && ЖO(var5);
               case SELECT_CATEGORY -> this.В_f514e7a3.category().ГЖ(var5);
               case SELECT_SERVER -> this.В_f514e7a3.category().ГЖ(var5) && М(var5, this.В_f514e7a3.number());
               default -> false;
            };
            if (var6) {
               return var4;
            }
         }
      }

      return null;
   }

   private boolean OЮ() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null) {
         int var1 = this.弟(Items.COMPASS);
         if (var1 == -1) {
            return false;
         }

         MINECRAFT_CLIENT.player.getInventory().setSelectedSlot(var1);
         MINECRAFT_CLIENT.player.connection.send(new ServerboundSetCarriedItemPacket(var1));
         MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
         return true;
      } else {
         return false;
      }
   }

   private void ЗЧ() {
      if (MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen var1 && var1.getMenu() == MINECRAFT_CLIENT.player.containerMenu && this.Е(var1)) {
         this.В_1fa4a375 = var1;
         this.field_5 = MINECRAFT_CLIENT.level;
         this.еК = false;
      } else if (this.В_1fa4a375 != null && this.field_5 == MINECRAFT_CLIENT.level && MINECRAFT_CLIENT.player.containerMenu == this.В_1fa4a375.getMenu()) {
         MINECRAFT_CLIENT.gui.setScreen(this.В_1fa4a375);
      } else {
         this.В_1fa4a375 = null;
         this.field_5 = null;
         if (!this.еК) {
            this.еК = true;
            this.必.ВД();
         }
      }
   }

   @Override
   public void onDisable() {
      this.еФ = false;
      this.Д6++;
      this.В_1fa4a375 = null;
      this.field_5 = null;
      this.в_at_fde4c7fc = null;
      this.еО = false;
      this.ъ = null;
      this.еТ = false;
      this.ХШ = 0L;
      this.Хы = 0L;
      this.Э.ВД();
      this.必.ВД();
      this.еЖ = false;
      this.ем = false;
      this.еК = false;
      this.еI = false;
      this.В_c1c2aab4 = AutoJoiner.ServerKind.REALLY_WORLD;
      this.В_f514e7a3 = null;
      this.В_ba8dcff0 = AutoJoiner.JoinStage.IDLE;
      this.Др = -1;
      super.onDisable();
   }

   public void н(PacketReceiveEvent var1) {
      int var2 = this.Д6;
      if (this.еФ && var1.getPacket() instanceof ClientboundSystemChatPacket var3 && !var3.overlay()) {
         AutoJoiner.JoinFailure var5 = method_9_de79a5c3(var3.content().getString());
         if (var5 != null) {
            var1.setCancelled(true);
            MINECRAFT_CLIENT.execute(() -> {
               if (this.isEnabled() && this.Д6 == var2) {
                  this.method_9(var5);
               }
            });
         }
      } else {
         if (var1.getPacket() instanceof ClientboundLoginPacket || var1.getPacket() instanceof ClientboundRespawnPacket) {
            MINECRAFT_CLIENT.execute(() -> {
               if (this.isEnabled() && this.Д6 == var2) {
                  this.еТ = this.еI;
                  this.В_1fa4a375 = null;
                  this.field_5 = null;
                  if (!this.еI) {
                     this.еО = false;
                     this.еК = true;
                     this.еЖ = false;
                     if (this.В_c1c2aab4 == AutoJoiner.ServerKind.HOLY_WORLD) {
                        this.В_ba8dcff0 = AutoJoiner.JoinStage.WAITING_FOR_HUB;
                     }

                     this.必.ВД();
                  }
               }
            });
         }
      }
   }

   private void ЖЯ(int var1) {
      if (MINECRAFT_CLIENT.gameMode != null && MINECRAFT_CLIENT.player != null) {
         MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.containerMenu.containerId, var1, 0, ContainerInput.PICKUP, MINECRAFT_CLIENT.player);
      }
   }

   private static AutoJoiner.JoinFailure method_9_de79a5c3(String var0) {
      String var1 = method_0(var0).replace(ХlД, ХlЪ).strip();
      boolean var2 = var1.startsWith(Хlт) || var1.startsWith(Хlа);
      if (var2 && var1.contains(ХlЮ)) {
         return AutoJoiner.JoinFailure.BUSY;
      } else if (var2 && var1.contains(ХlЧ)) {
         return AutoJoiner.JoinFailure.FULL;
      } else {
         return var1.startsWith(ХlЛ) && var1.contains(Хl诶) && var1.contains(Хl1) ? AutoJoiner.JoinFailure.COOLDOWN : null;
      }
   }

   public void ЖН(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.gameMode != null) {
         if (!this.еI
            || !this.еТ
               && this.в_at_fde4c7fc == MINECRAFT_CLIENT.level
               && (this.В_c1c2aab4 != AutoJoiner.ServerKind.HOLY_WORLD || System.currentTimeMillis() - this.ХШ < ХЫЗ || !this.OФ())) {
            if (MINECRAFT_CLIENT.player.isAlive() && !MINECRAFT_CLIENT.player.isRemoved()) {
               if (this.еО) {
                  if (MINECRAFT_CLIENT.level == this.ъ) {
                     return;
                  }

                  this.еО = false;
                  this.必.ВД();
               }

               this.ЗЧ();
               if (System.currentTimeMillis() >= this.Хы) {
                  if (this.еК) {
                     long var13 = Math.max(ХЫЕ, this.Ху.getValue().intValue() * ХЫП);
                     if (this.必.method_0(var13) && this.OЮ()) {
                        this.Др = -1;
                        if (this.В_c1c2aab4 == AutoJoiner.ServerKind.HOLY_WORLD) {
                           this.В_ba8dcff0 = AutoJoiner.JoinStage.SELECT_LIGHT;
                        }

                        this.必.ВД();
                        this.Э.ВД();
                     }
                  } else if (MINECRAFT_CLIENT.gui.screen() instanceof ContainerScreen) {
                     if (MINECRAFT_CLIENT.player.containerMenu instanceof ChestMenu var2) {
                        if (this.Э.method_0(this.Ху.getValue().intValue() * ХЫ西)) {
                           if (this.В_c1c2aab4 == AutoJoiner.ServerKind.HOLY_WORLD) {
                              this.method_9(var2);
                           } else {
                              String var14 = this.衣(MINECRAFT_CLIENT.gui.screen().getTitle().getString());
                              this.ЖК_de5a6f78(var14);
                              boolean var4 = var14.contains(ХЫд);
                              boolean var5 = this.OЩ();
                              Item var6 = var5 ? Items.TNT : Items.CRAFTING_TABLE;
                              String var7 = var5 ? ХЫБ : ХЫС;
                              String var8 = String.valueOf(this.ЖЗ().getValue().intValue());

                              for (Slot var10 : var2.slots) {
                                 if (var10.container != MINECRAFT_CLIENT.player.getInventory() && var10.hasItem()) {
                                    if (var4 && var10.getItem().is(var6)) {
                                       this.ЖЯ(var10.index);
                                       this.Э.ВД();
                                       return;
                                    }

                                    String var11 = this.衣(var10.getItem().getHoverName().getString());
                                    if (var11.contains(var7)) {
                                       this.ЖЯ(var10.index);
                                       this.Э.ВД();
                                       return;
                                    }

                                    if (!var5 && !this.еЖ && var11.contains(ХЫз)) {
                                       this.ЖЯ(var10.index);
                                       this.Э.ВД();
                                       return;
                                    }

                                    Matcher var12 = ш.matcher(var11);
                                    if (this.еЖ && var12.find() && var12.group(1).equals(var8)) {
                                       this.ЗФ();
                                       this.Ж西(var10.index);
                                       this.Э.ВД();
                                       return;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         } else {
            this.disable();
         }
      }
   }

   public boolean method_9(AutoJoiner.HolyWorldSelection var1) {
      if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.player.connection == null) {
         return false;
      }

      if (!this.Е(var1)) {
         return false;
      }

      this.еО = true;
      this.ъ = MINECRAFT_CLIENT.level;
      MINECRAFT_CLIENT.player.connection.sendCommand(ХЫя);
      return true;
   }

   public enum HolyWorldCategory {
      SOLO("соло", "solo"),
      DUO("дуо", "duo"),
      TRIO("трио", "trio"),
      CLAN("клан", "clan");

      private final String[] 诶;

      HolyWorldCategory(String... var3) {
         this.诶 = var3;
      }

      private boolean ГЖ(String var1) {
         for (String var5 : this.诶) {
            if (var1.contains(var5)) {
               return true;
            }
         }

         return false;
      }

      private static AutoJoiner.HolyWorldCategory method_3(String var0) {
         for (AutoJoiner.HolyWorldCategory var4 : values()) {
            if (var4.ГЖ(var0)) {
               return var4;
            }
         }

         return null;
      }
   }

   public record HolyWorldSelection(AutoJoiner.HolyWorldCategory category, int number) {
   }

   private enum ServerKind {
      REALLY_WORLD,
      HOLY_WORLD;
   }

   private enum JoinStage {
      IDLE,
      WAITING_FOR_HUB,
      SELECT_LIGHT,
      SELECT_CATEGORY,
      SELECT_SERVER,
      COMPLETE;
   }

   private enum JoinFailure {
      BUSY,
      FULL,
      COOLDOWN;
   }
}
