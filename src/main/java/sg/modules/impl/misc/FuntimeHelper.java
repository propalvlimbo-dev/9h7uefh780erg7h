package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import org.joml.Vector2f;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.network.ClientChat;
import sg.network.FunTimeItemMetadata;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.InventoryActions;
import sg.settings.BooleanSetting;
import sg.util.time.ColorMath;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.BoundItemIndicator;
import sg.modules.impl.init.FunTimeItemCatalog;
import sg.events.impl.HudRenderEvent;
import sg.events.impl.InputEvent;
import sg.modules.impl.init.InventoryActionCoordinator;
import sg.settings.KeybindSetting;
import sg.modules.impl.init.TimedLootLocation;

public class FuntimeHelper extends ClientModule {
   private final KeybindSetting Ъ;
   @SerializedName("т")
   private final KeybindSetting т_bf56bd03;
   @SerializedName("а")
   private final KeybindSetting а_bf56bd03;
   private final KeybindSetting Ю;
   private final KeybindSetting Ч;
   private final KeybindSetting Л;
   private final KeybindSetting 诶;
   @SerializedName("1")
   private final KeybindSetting field_1;
   private final KeybindSetting Ь;
   private final KeybindSetting 衣;
   private final KeybindSetting ш;
   private final KeybindSetting И;
   private final KeybindSetting ь;
   private final KeybindSetting ж;
   @SerializedName("в")
   private final BooleanSetting в_3276742a_at_62b87517;
   @SerializedName("ъ")
   private final BooleanSetting ъ_3276742a;
   @SerializedName("В")
   private final Queue<Map<BlockPos, BlockState>> В_d8882623;
   @SerializedName("Я")
   private final Queue<FunTimeItemMetadata.TimedItemLocation> Я_at_62b87517;
   @SerializedName("т")
   private final Map<BlockPos, FuntimeHelper.TimedBlockState> т_ea411899;
   @SerializedName("т")
   private final List<TimedLootLocation> т_2571477d;
   @SerializedName("а")
   private final List<TimedLootLocation> а_2571477d;
   @SerializedName("а")
   private final Map<String, ItemStack> а_ea411899;
   @SerializedName("Х")
   private final Object Х_at_62b87517;
   @SerializedName("В")
   private FuntimeHelper.BoundServerItem В_976e88fe;
   @SerializedName("В")
   private FuntimeHelper.ItemUseStage В_d7e2a688;
   private int Ш;
   private int ы;
   private int й;
   private boolean Г;
   @SerializedName("8")
   private final EventListener<PlayerTickEvent> field_8;
   @SerializedName("5")
   private final EventListener<InputEvent> field_5;
   @SerializedName("в")
   private final EventListener<PacketReceiveEvent> в_f8db0d09_at_62b87517;
   @SerializedName("ъ")
   private final EventListener<HudRenderEvent> ъ_f8db0d09;
   private static final String Вь2 = "Funtime Helper";
   private static final String Вьф = "Помощник для сервера Funtime";
   private static final String Вьи = "Кнопка явной пыли";
   private static final String Вь9 = "Кнопка дезориентация";
   private static final String Вьо = "Кнопка снежка заморозки";
   private static final String ВьO = "Кнопка пласта";
   private static final String Вь7 = "Кнопка трапки";
   private static final String ВьР = "Кнопка огненного смерча";
   private static final String Вь3 = "Кнопка божьей ауры";
   private static final String Вьр = "Кнопка хлопушки";
   private static final String Вь6 = "Кнопка святой воды";
   private static final String Вь_ = "Кнопка зелья гнева";
   private static final String ВьШ = "Кнопка зелья палладина";
   private static final String Вьы = "Кнопка зелья ассасина";
   private static final String Вьй = "Кнопка зелья радиации";
   private static final String ВьЖ = "Кнопка снотворного";
   private static final String Вьм = "Таймер структур";
   private static final String ВьК = "Таймеры пласта и трапки";
   private static final String ВьI = "[★] Хлопушка";
   private static final String ВьТ = "[★] Святая вода";
   private static final String ВьО = "[★] Зелье Гнева";
   private static final String ВьФ = "[★] Зелье Палладина";
   private static final String ВьЙ = "[★] Зелье Ассасина";
   private static final String Вьк = "[★] Зелье Радиации";
   private static final String Вьг = "[★] Снотворное";
   private static final double Вь0 = 15000.0;
   private static final double Вьх = 30000.0;
   private static final double ВьУ = 1000.0;
   private static final String ВьЗ = "%.1fs";
   private static final double ВьЕ = 10.0;
   private static final double ВьП = 10.0;
   private static final float Вь西 = 34.0F;
   private static final float Вьд = 24.0F;
   private static final float ВьБ = 0.5F;
   private static final float ВьС = 0.5F;
   private static final float Вьз = 9.0F;
   private static final double Вьц = 205.0;
   private static final float Вья = 7.0F;
   private static final double Вжс = 140.0;
   private static final float ВжВ = 7.0F;
   private static final double ВжЯ = 38.0;
   private static final float ВжХ = 6.0F;
   private static final float Вже = 4.0F;
   private static final float ВжА = 27.0F;
   private static final float Вж4 = 4.0F;
   private static final double Вжп = 255.0;
   private static final double ВжД = 2.0;
   private static final String ВжЪ = "null";
   private static final String Вжт = "(?:-|#)(\\d+)";
   private static final String Вжа = "Явная Пыль";
   private static final String ВжЮ = "Дезориентация";
   private static final String ВжЧ = "Трапка";
   private static final String ВжЛ = "Снежок заморозки";
   private static final String Вж诶 = "Пласт";
   private static final String Вж1 = "Огненный смерч";
   private static final String ВжЬ = "Божья аура";
   private static final String Вж衣 = "[★] Хлопушка";
   private static final String Вжш = "[★] Святая вода";
   private static final String ВжИ = "[★] Зелье Гнева";
   private static final String Вжь = "[★] Зелье Палладина";
   private static final String Вжж = "[★] Зелье Ассасина";
   private static final String Вж弟 = "[★] Зелье Радиации";
   private static final String Вжб = "[★] Снотворное";
   private static final String ВжЫ = "[★] ";
   private static final long Вжl = 2500L;

   private void method__(Item var1, Vec3 var2, double var3) {
      if (this.т_2571477d.stream().noneMatch(var1x -> var1x.т().equals(var2))) {
         this.т_2571477d.add(new TimedLootLocation(var1, var2, this.ЧГ(), this.к(), var3));
      }
   }

   private void method__(Map<BlockPos, BlockState> var1) {
      for (BlockPos var3 : var1.keySet()) {
         Vec3 var4 = Vec3.atCenterOf(var3);
         if (this.method__(var3.above(2), var1)) {
            this.method__(Items.NETHERITE_SCRAP, var4, System.currentTimeMillis() + Вь0);
         } else if (this.诶(var3.above(3), var1)) {
            this.method__(Items.NETHERITE_SCRAP, var4, System.currentTimeMillis() + Вьх);
         }
      }
   }

   public FuntimeHelper() {
      super(Вь2, Вьф, ModuleCategory.MISC);
      this.Ъ = new KeybindSetting(Вьи);
      this.т_bf56bd03 = new KeybindSetting(Вь9);
      this.а_bf56bd03 = new KeybindSetting(Вьо);
      this.Ю = new KeybindSetting(ВьO);
      this.Ч = new KeybindSetting(Вь7);
      this.Л = new KeybindSetting(ВьР);
      this.诶 = new KeybindSetting(Вь3);
      this.field_1 = new KeybindSetting(Вьр);
      this.Ь = new KeybindSetting(Вь6);
      this.衣 = new KeybindSetting(Вь_);
      this.ш = new KeybindSetting(ВьШ);
      this.И = new KeybindSetting(Вьы);
      this.ь = new KeybindSetting(Вьй);
      this.ж = new KeybindSetting(ВьЖ);
      this.в_3276742a_at_62b87517 = new BooleanSetting(Вьм, true);
      this.ъ_3276742a = new BooleanSetting(ВьК, true);
      this.В_d8882623 = new ConcurrentLinkedQueue<>();
      this.Я_at_62b87517 = new ConcurrentLinkedQueue<>();
      this.т_ea411899 = new HashMap<>();
      this.т_2571477d = new ArrayList<>();
      this.а_2571477d = new ArrayList<>();
      this.а_ea411899 = new HashMap<>();
      this.Х_at_62b87517 = new Object();
      this.В_d7e2a688 = FuntimeHelper.ItemUseStage.IDLE;
      this.ы = -1;
      this.й = -1;
      this.field_8 = this::method_6;
      this.field_5 = this::Г;
      this.в_f8db0d09_at_62b87517 = this::诶;
      this.ъ_f8db0d09 = this::Б;
      this.addSettings(
         this.Ъ,
         this.т_bf56bd03,
         this.а_bf56bd03,
         this.Ю,
         this.Ч,
         this.Л,
         this.诶,
         this.field_1,
         this.Ь,
         this.衣,
         this.ш,
         this.И,
         this.ь,
         this.ж,
         this.в_3276742a_at_62b87517,
         this.ъ_3276742a
      );
   }

   private boolean method__(BlockPos var1, Map<BlockPos, BlockState> var2) {
      int var3 = 0;

      for (BlockPos var5 : this.method__(var1, 2)) {
         if (Vec3.atCenterOf(var5).distanceTo(Vec3.atCenterOf(var1)) < ВжД) {
            BlockState var6 = (BlockState)var2.get(var5);
            if (var6 != null && !var6.isAir()) {
               var3++;
            }
         } else if (!var5.equals(var1.above(2).north().east())
            && !var5.equals(var1.above(2).north().west())
            && !var5.equals(var1.above(2).south().east())
            && !var5.equals(var1.above(2).south().west())) {
            BlockState var7 = (BlockState)var2.get(var5);
            if (var7 == null || var7.isAir()) {
               var3++;
            }
         }

         if (var3 > 5) {
            return false;
         }
      }

      return true;
   }

   public List<BoundItemIndicator> method_5() {
      return List.of(
         new BoundItemIndicator(Items.SUGAR, this.Ъ.getValue()),
         new BoundItemIndicator(Items.ENDER_EYE, this.т_bf56bd03.getValue()),
         new BoundItemIndicator(Items.SNOWBALL, this.а_bf56bd03.getValue()),
         new BoundItemIndicator(Items.DRIED_KELP, this.Ю.getValue()),
         new BoundItemIndicator(Items.NETHERITE_SCRAP, this.Ч.getValue()),
         new BoundItemIndicator(Items.FIRE_CHARGE, this.Л.getValue()),
         new BoundItemIndicator(Items.PHANTOM_MEMBRANE, this.诶.getValue()),
         new BoundItemIndicator(this.method__(ВьI), this.field_1.getValue()),
         new BoundItemIndicator(this.method__(ВьТ), this.Ь.getValue()),
         new BoundItemIndicator(this.method__(ВьО), this.衣.getValue()),
         new BoundItemIndicator(this.method__(ВьФ), this.ш.getValue()),
         new BoundItemIndicator(this.method__(ВьЙ), this.И.getValue()),
         new BoundItemIndicator(this.method__(Вьк), this.ь.getValue()),
         new BoundItemIndicator(this.method__(Вьг), this.ж.getValue())
      );
   }

   private void Ш(int var1) {
      if (var1 >= 0 && var1 <= 8) {
         MINECRAFT_CLIENT.player.getInventory().selected = var1;
         InventoryActions.衣(var1);
      }
   }

   private int к() {
      if (MINECRAFT_CLIENT.level == null) {
         return -1;
      }

      Objective var1 = MINECRAFT_CLIENT.level.getScoreboard().getDisplayObjective(DisplaySlot.SIDEBAR);
      if (var1 == null) {
         return -1;
      }

      Matcher var2 = Pattern.compile(Вжт).matcher(var1.getDisplayName().getString());
      return var2.find() ? Integer.parseInt(var2.group(1)) : -1;
   }

   private List<BlockPos> method__(BlockPos var1, int var2) {
      ArrayList var3 = new ArrayList();

      for (int var4 = var1.getX() - var2; var4 <= var1.getX() + var2; var4++) {
         for (int var5 = var1.getZ() - var2; var5 <= var1.getZ() + var2; var5++) {
            for (int var6 = var1.getY() - var2; var6 < var1.getY() + var2; var6++) {
               var3.add(new BlockPos(var4, var6, var5));
            }
         }
      }

      return var3;
   }

   private void method_6(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         HashMap<BlockPos, BlockState> var2 = new HashMap<>();

         Map<BlockPos, BlockState> var3;
         while ((var3 = this.В_d8882623.poll()) != null) {
            var2.putAll(var3);
         }

         long var4 = System.currentTimeMillis();
         this.т_ea411899.entrySet().removeIf(var2x -> var4 - var2x.getValue().time() > Вжl);
         var2.forEach((var3x, var4x) -> this.т_ea411899.put(var3x, new FuntimeHelper.TimedBlockState(var4x, var4)));
         if (!var2.isEmpty() && this.т_ea411899.size() > 35 && this.т_ea411899.size() < 1600) {
            HashMap var6 = new HashMap();
            this.т_ea411899.forEach((var1x, var2x) -> var6.put(var1x, var2x.state()));
            this.method__(var6);
         }

         FunTimeItemMetadata.TimedItemLocation var7;
         while ((var7 = this.Я_at_62b87517.poll()) != null) {
            this.method__(var7);
         }

         this.а_2571477d.removeIf(var0 -> var0.К() <= System.currentTimeMillis());
         this.т_2571477d.removeIf(var0 -> var0.К() <= System.currentTimeMillis());
         this.Чя();
      }
   }

   private ItemStack method__(String var1) {
      return this.а_ea411899
         .computeIfAbsent(
            var1,
            var0 -> FunTimeItemCatalog.с0
               .stream()
               .filter(var1x -> var1x.name().equals(var0) && var1x.item() == Items.SPLASH_POTION)
               .findFirst()
               .map(FunTimeItemCatalog.CatalogEntry::stack)
               .orElseGet(Items.SPLASH_POTION::getDefaultInstance)
         );
   }

   private boolean 诶(BlockPos var1, Map<BlockPos, BlockState> var2) {
      int var3 = 0;

      for (BlockPos var5 : this.method__(var1, 3)) {
         if (Math.abs(var5.getX() - var1.getX()) <= 2 && Math.abs(var5.getY() - var1.getY()) <= 2 && Math.abs(var5.getZ() - var1.getZ()) <= 2) {
            BlockState var7 = (BlockState)var2.get(var5);
            if (var7 != null && !var7.isAir()) {
               var3++;
            }
         } else if (!var5.equals(var1.above(3))) {
            BlockState var6 = (BlockState)var2.get(var5);
            if (var6 == null || var6.isAir()) {
               var3++;
            }
         }

         if (var3 > 5) {
            return false;
         }
      }

      return true;
   }

   private void method___de5a6f78(TimedLootLocation var1) {
      String var2 = this.ЧГ();
      int var3 = this.к();
      if (var1.ю必().equals(var2) && (var1.Ц5() < 0 || var3 < 0 || var1.Ц5() == var3)) {
         Vector2f var4 = WorldProjection.projectToScreen(var1.т());
         if (var4 != null) {
            double var5 = Math.max(0.0, (var1.К() - System.currentTimeMillis()) / ВьУ);
            String var7 = String.format(Locale.US, ВьЗ, Math.round(var5 * ВьЕ) / ВьП);
            float var8 = Вь西 + UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var7, 12);
            float var9 = Вьд;
            float var10 = var4.x - var8 * ВьБ;
            float var11 = var4.y - var9 * ВьС;
            UiDrawing.drawShadow(var10, var11, var8, var9, Вьз, ColorMath.packArgb(0, 0, 0, Вьц));
            UiDrawing.drawRoundedRect(var10, var11, var8, var9, Вья, ColorMath.packArgb(9, 14, 22, Вжс));
            UiDrawing.drawOutline(var10, var11, var8, var9, 1.0F, ВжВ, ColorMath.packArgb(93, 184, 255, ВжЯ));
            UiDrawing.drawItem(var1.ы().getDefaultInstance(), var10 + ВжХ, var11 + Вже, 1.0F, false);
            UiDrawing.drawText(ClientFonts.getMediumFont(), var7, var10 + ВжА, var11 + Вж4, 12, ColorMath.packArgb(232, 246, 255, Вжп));
         }
      }
   }

   private void method___de5a6f78(FuntimeHelper.BoundServerItem var1) {
      if (MINECRAFT_CLIENT.player != null && this.В_976e88fe == null && this.В_d7e2a688 == FuntimeHelper.ItemUseStage.IDLE) {
         int var2 = this.method___a83dd0cc(var1);
         if (var2 == -1) {
            ClientChat.З(var1.name() + " не найдена");
         } else {
            this.В_976e88fe = var1;
            this.й = var2;
            this.ы = MINECRAFT_CLIENT.player.getInventory().selected;
            this.В_d7e2a688 = FuntimeHelper.ItemUseStage.ACQUIRE;
         }
      }
   }

   private void ЧЭ() {
      if (MINECRAFT_CLIENT.gui.screen() == null) {
         KeyMapping[] var1 = new KeyMapping[]{
            MINECRAFT_CLIENT.options.keyUp, MINECRAFT_CLIENT.options.keyDown, MINECRAFT_CLIENT.options.keyLeft, MINECRAFT_CLIENT.options.keyRight, MINECRAFT_CLIENT.options.keyJump, MINECRAFT_CLIENT.options.keySprint
         };

         for (KeyMapping var5 : var1) {
            var5.setDown(InputConstants.isKeyDown(MINECRAFT_CLIENT.getWindow(), var5.key.getValue()));
         }
      }
   }

   private void Г(InputEvent var1) {
      boolean var2 = var1.getAction() == 1;
      if (MINECRAFT_CLIENT.gui.screen() == null) {
         if (var2 && var1.getInputCode() == this.Ъ.getValue()) {
            this.method__(Items.SUGAR, Вжа);
         }

         if (var2 && var1.getInputCode() == this.т_bf56bd03.getValue()) {
            this.method__(Items.ENDER_EYE, ВжЮ);
         }

         if (var2 && var1.getInputCode() == this.Ч.getValue()) {
            this.method__(Items.NETHERITE_SCRAP, ВжЧ);
         }

         if (var2 && var1.getInputCode() == this.а_bf56bd03.getValue()) {
            this.method__(Items.SNOWBALL, ВжЛ);
         }

         if (var2 && var1.getInputCode() == this.Ю.getValue()) {
            this.method__(Items.DRIED_KELP, Вж诶);
         }

         if (var2 && var1.getInputCode() == this.Л.getValue()) {
            this.method__(Items.FIRE_CHARGE, Вж1);
         }

         if (var2 && var1.getInputCode() == this.诶.getValue()) {
            this.method__(Items.PHANTOM_MEMBRANE, ВжЬ);
         }

         if (var2 && var1.getInputCode() == this.field_1.getValue()) {
            this.Ь(Вж衣);
         }

         if (var2 && var1.getInputCode() == this.Ь.getValue()) {
            this.Ь(Вжш);
         }

         if (var2 && var1.getInputCode() == this.衣.getValue()) {
            this.Ь(ВжИ);
         }

         if (var2 && var1.getInputCode() == this.ш.getValue()) {
            this.Ь(Вжь);
         }

         if (var2 && var1.getInputCode() == this.И.getValue()) {
            this.Ь(Вжж);
         }

         if (var2 && var1.getInputCode() == this.ь.getValue()) {
            this.Ь(Вж弟);
         }

         if (var2 && var1.getInputCode() == this.ж.getValue()) {
            this.Ь(Вжб);
         }
      }
   }

   private void Б(HudRenderEvent var1) {
      if (var1.getGraphics() != null && MINECRAFT_CLIENT.level != null) {
         if (this.в_3276742a_at_62b87517.getValue()) {
            for (TimedLootLocation var3 : this.т_2571477d) {
               this.method___de5a6f78(var3);
            }
         }

         if (this.ъ_3276742a.getValue()) {
            for (TimedLootLocation var5 : this.а_2571477d) {
               this.method___de5a6f78(var5);
            }
         }
      }
   }

   private void Ь(String var1) {
      FunTimeItemCatalog.CatalogEntry var2 = FunTimeItemCatalog.с0.stream().filter(var1x -> var1x.name().equals(var1)).findFirst().orElse(null);
      this.method___de5a6f78(new FuntimeHelper.BoundServerItem(Items.SPLASH_POTION, var2, var1.replace(ВжЫ, "")));
   }

   @Override
   public void onDisable() {
      this.т_2571477d.clear();
      this.а_2571477d.clear();
      this.В_d8882623.clear();
      this.Я_at_62b87517.clear();
      this.т_ea411899.clear();
      this.Чх();
      this.ЧЦ();
   }

   private String ЧГ() {
      return MINECRAFT_CLIENT.level == null ? ВжЪ : MINECRAFT_CLIENT.level.dimension().identifier().getPath();
   }

   private void Чх() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null && this.ы >= 0) {
         if (this.Г && this.й >= 9) {
            InventoryActions.method_7(this.ы, this.й);
         }

         this.Ш(this.ы);
      }
   }

   private void Чя() {
      if (this.В_d7e2a688 != FuntimeHelper.ItemUseStage.IDLE && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.gameMode != null) {
         if (MINECRAFT_CLIENT.gui.screen() == null) {
            if (this.В_d7e2a688 == FuntimeHelper.ItemUseStage.ACQUIRE) {
               if (!InventoryActionCoordinator.и(this.Х_at_62b87517)) {
                  return;
               }

               this.О(true);
               this.В_d7e2a688 = FuntimeHelper.ItemUseStage.WAIT_MOVEMENT;
               this.Ш = this.method_0();
            }

            this.Чд();
            if (this.Ш > 0) {
               this.Ш--;
            } else {
               switch (this.В_d7e2a688) {
                  case WAIT_MOVEMENT:
                     this.й = this.method___a83dd0cc(this.В_976e88fe);
                     if (this.й == -1) {
                        ClientChat.З(this.В_976e88fe.name() + " не найдена");
                        this.ЧЦ();
                        return;
                     }

                     if (this.й < 9) {
                        this.Ш(this.й);
                     } else {
                        InventoryActions.method_7(this.ы, this.й);
                        this.Г = true;
                     }

                     this.В_d7e2a688 = FuntimeHelper.ItemUseStage.USE;
                     this.Ш = this.method_0();
                     break;
                  case USE:
                     MINECRAFT_CLIENT.gameMode.useItem(MINECRAFT_CLIENT.player, InteractionHand.MAIN_HAND);
                     this.В_d7e2a688 = FuntimeHelper.ItemUseStage.RESTORE;
                     this.Ш = this.method_0();
                     break;
                  case RESTORE:
                     if (this.Г) {
                        InventoryActions.method_7(this.ы, this.й);
                     }

                     this.Ш(this.ы);
                     this.В_d7e2a688 = FuntimeHelper.ItemUseStage.FINISH;
                     this.Ш = this.method_0();
                     break;
                  case FINISH:
                     this.ЧЦ();
               }
            }
         }
      }
   }

   private int method_0() {
      return RoseClient.getInstance().getServices().getModules().getGuiMove().И_989fed9a().getValue().intValue();
   }

   private void method__(FunTimeItemMetadata.TimedItemLocation var1) {
      this.а_2571477d.removeIf(var1x -> var1x.т().distanceToSqr(var1.position()) < 1.0);
      this.а_2571477d.add(new TimedLootLocation(var1.item(), var1.position(), this.ЧГ(), this.к(), System.currentTimeMillis() + var1.durationMs()));
   }

   private void method__(Item var1, String var2) {
      this.method___de5a6f78(new FuntimeHelper.BoundServerItem(var1, null, var2));
   }

   private int method___a83dd0cc(FuntimeHelper.BoundServerItem var1) {
      if (MINECRAFT_CLIENT.player != null && var1 != null) {
         NonNullList var2 = MINECRAFT_CLIENT.player.getInventory().getNonEquipmentItems();

         for (int var3 = var2.size() - 1; var3 >= 0; var3--) {
            ItemStack var4 = (ItemStack)var2.get(var3);
            if (!var4.isEmpty() && var4.is(var1.item()) && (var1.catalogEntry() == null || var1.catalogEntry().matches(var4))) {
               return var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private void ЧЦ() {
      this.О(false);
      InventoryActionCoordinator.к(this.Х_at_62b87517);
      this.ЧЭ();
      this.В_976e88fe = null;
      this.В_d7e2a688 = FuntimeHelper.ItemUseStage.IDLE;
      this.Ш = 0;
      this.ы = -1;
      this.й = -1;
      this.Г = false;
   }

   private void 诶(PacketReceiveEvent var1) {
      if (this.ъ_3276742a.getValue() && var1.getPacket() instanceof ClientboundSoundPacket var2) {
         this.Я_at_62b87517.addAll(FunTimeItemMetadata.Ю(var2));
      }

      if (this.в_3276742a_at_62b87517.getValue()) {
         HashMap var6 = new HashMap();
         if (var1.getPacket() instanceof ClientboundSectionBlocksUpdatePacket var4) {
            var4.runUpdates((var1x, var2x) -> var6.put(var1x.immutable(), var2x));
         } else {
            if (!(var1.getPacket() instanceof ClientboundBlockUpdatePacket var7)) {
               return;
            }

            var6.put(var7.getPos().immutable(), var7.getBlockState());
         }

         if (!var6.isEmpty()) {
            this.В_d8882623.add(var6);
         }
      }
   }

   private void Чд() {
      KeyMapping[] var1 = new KeyMapping[]{MINECRAFT_CLIENT.options.keyUp, MINECRAFT_CLIENT.options.keyDown, MINECRAFT_CLIENT.options.keyLeft, MINECRAFT_CLIENT.options.keyRight, MINECRAFT_CLIENT.options.keyJump, MINECRAFT_CLIENT.options.keySprint};

      for (KeyMapping var5 : var1) {
         var5.setDown(false);
      }
   }

   public List<TimedLootLocation> Ъ_2571477d() {
      return this.т_2571477d;
   }

   private void О(boolean var1) {
      if (var1) {
         RoseClient.getInstance().getServices().getModules().getGuiMove().Ь(this.Х_at_62b87517);
      } else {
         RoseClient.getInstance().getServices().getModules().getGuiMove().method_5(this.Х_at_62b87517);
      }
   }

   private record TimedBlockState(BlockState state, long time) {
   }

   private record BoundServerItem(Item item, FunTimeItemCatalog.CatalogEntry catalogEntry, String name) {
   }

   private enum ItemUseStage {
      IDLE,
      ACQUIRE,
      WAIT_MOVEMENT,
      USE,
      RESTORE,
      FINISH;
   }
}
