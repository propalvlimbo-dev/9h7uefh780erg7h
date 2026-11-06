package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerTickEvent;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;
import sg.ec.BaritoneIntegration;
import sg.events.impl.WorldEffectsEvent;
import sg.network.ClientChat;
import sg.settings.BooleanSetting;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.init.AutoBrewerSchematic;
import sg.modules.impl.init.RoseXrayLines;
import sg.events.impl.InputEvent;
import sg.settings.KeybindSetting;

public class AutoBrewerBuilder extends ClientModule {
   public static volatile boolean с4 = false;
   public static volatile boolean сп = false;
   private static final int сц = -16718337;
   private static final int ся = -26624;
   private static final int Вс = -2080517;
   @SerializedName("7")
   private final BooleanSetting field_7;
   private final KeybindSetting М;
   @SerializedName("к")
   private final EventListener<PlayerTickEvent> к_at_52ce3431;
   @SerializedName("г")
   private final EventListener<InputEvent> г_at_52ce3431;
   @SerializedName("0")
   private final EventListener<WorldEffectsEvent> field_0_at_52ce3431;
   @SerializedName("В")
   private AutoBrewerBuilder.BuildStage В_3cf9aa23;
   @SerializedName("В")
   private AutoBrewerSchematic В_50e6a30b;
   @SerializedName("В")
   private BlockPos В_10b53497;
   private int ВВ;
   private boolean сД;
   private String С;
   private boolean сЪ;
   private boolean ст;
   private static final String ЯЖ4 = "AutoBrewerBuilder";
   private static final String ЯЖп = "Каркас автозельеварки через Baritone (сундуки/воронки/стойки — руками)";
   private static final String ЯЖД = "Заменять траву/снег";
   private static final String ЯЖЪ = "Клавиша отключения";
   private static final String ЯЖт = "Выключено биндом.";
   private static final int ЯЖа = -16718337;
   private static final int ЯЖЮ = -2080517;
   private static final String ЯЖЧ = "Нет игрока или мира";
   private static final String ЯЖЛ = "Нужен Baritone в mods";
   private static final String ЯЖ诶 = "allowBreak";
   private static final String ЯЖ1 = "allowPlace";
   private static final String ЯЖЬ = "allowInventory";
   private static final String ЯЖ衣 = "buildInLayers";
   private static final String ЯЖш = "layerOrder";
   private static final String ЯЖИ = "distanceTrim";
   private static final String ЯЖь = "rightClickContainerOnArrival";
   private static final String ЯЖж = "AutoBrewer";
   private static final String ЯЖ弟 = "Baritone: булыжник / редстоун / повторители / рычаг / факела.";
   private static final String ЯЖб = "Сундуки(голубой) / воронки(оранж) / стойки(розовый) — ставь сам.";
   private static final String ЯЖЫ = "AutoBrewer";
   private static final String ЯЖl = "?";
   private static final String ЯЖ8 = "Каркас готов. Сундуки / воронки / стойки — по цвету, руками.";
   private static final String ЯЖ5 = ", ";

   private void нЬ() {
      if (!BaritoneIntegration.ЮЧ()) {
         this.Ш(ЯЖЛ);
      } else {
         AutoBrewerSchematic.SchematicKind var1 = AutoBrewerSchematic.SchematicKind.OBYCHNAYA_PITEVYE;
         Direction var2 = MINECRAFT_CLIENT.player.getDirection();

         try {
            this.В_50e6a30b = AutoBrewerSchematic.з(var1, MINECRAFT_CLIENT.player.blockPosition(), var2);
            this.В_10b53497 = this.В_50e6a30b.з_10b53497();
         } catch (Throwable var5) {
            var5.printStackTrace();
            this.Ш("Схема: " + var5.getMessage());
            return;
         }

         String var3 = this.OТ();
         if (var3 != null) {
            this.Ш(var3);
         } else {
            var3 = this.O西();
            if (var3 != null) {
               this.Ш(var3);
            } else {
               try {
                  BaritoneIntegration.г1();
                  BaritoneIntegration.Ъ(ЯЖ诶, true);
                  BaritoneIntegration.Ъ(ЯЖ1, true);
                  BaritoneIntegration.Ъ(ЯЖЬ, true);
                  BaritoneIntegration.Ъ(ЯЖ衣, true);
                  BaritoneIntegration.Ъ(ЯЖш, false);
                  BaritoneIntegration.Ъ(ЯЖИ, false);
                  BaritoneIntegration.Ъ(ЯЖь, false);
                  BaritoneIntegration.г2();
                  var3 = BaritoneIntegration.Ъ(ЯЖж, this.В_50e6a30b, this.В_10b53497);
                  if (var3 != null) {
                     this.Ш(var3);
                     return;
                  }

                  BaritoneIntegration.г0();
               } catch (Throwable var6) {
                  var6.printStackTrace();
                  this.Ш("Baritone: " + var6.getMessage());
                  return;
               }

               this.сД = true;
               this.ВВ = 0;
               this.ст = false;
               this.В_3cf9aa23 = AutoBrewerBuilder.BuildStage.BUILD_STRUCTURE;
               this.Р("Каркас: " + var1.Ъ + " " + var2.name() + " (" + this.В_50e6a30b.西() + "x" + this.В_50e6a30b.l() + "x" + this.В_50e6a30b.必() + ")");
               this.Р(ЯЖ弟);
               this.Р(ЯЖб);
            }
         }
      }
   }

   private boolean н(BlockState var1, BlockState var2) {
      if (AutoBrewerSchematic.ы(var2)) {
         return AutoBrewerSchematic.ы(var1);
      } else if (var1.getBlock() != var2.getBlock()) {
         return false;
      } else {
         return !var2.is(Blocks.REDSTONE_WIRE) && !var2.is(Blocks.REPEATER) && !var2.is(Blocks.LEVER) ? var1.equals(var2) : true;
      }
   }

   private boolean н(BlockPos var1, Block var2) {
      return MINECRAFT_CLIENT.level.getBlockState(var1).is(var2);
   }

   @Override
   public void onDisable() {
      с4 = false;
      сп = false;
      if (this.сД) {
         try {
            BaritoneIntegration.гШ();
            BaritoneIntegration.г2();
         } catch (Throwable var2) {
         }
      }

      this.нп();
      super.onDisable();
   }

   private void ж(Item var1) {
      try {
         for (int var2 = 0; var2 < 9; var2++) {
            if (MINECRAFT_CLIENT.player.getInventory().getItem(var2).is(var1)) {
               return;
            }
         }

         for (int var6 = 9; var6 < 36; var6++) {
            if (MINECRAFT_CLIENT.player.getInventory().getItem(var6).is(var1)) {
               int var3 = 0;

               for (int var4 = 0; var4 < 9; var4++) {
                  if (MINECRAFT_CLIENT.player.getInventory().getItem(var4).isEmpty()) {
                     var3 = var4;
                     break;
                  }
               }

               MINECRAFT_CLIENT.gameMode.handleContainerInput(MINECRAFT_CLIENT.player.inventoryMenu.containerId, var6, var3, ContainerInput.SWAP, MINECRAFT_CLIENT.player);
               return;
            }
         }
      } catch (Throwable var5) {
      }
   }

   public AutoBrewerBuilder() {
      super(ЯЖ4, ЯЖп, ModuleCategory.MISC);
      this.field_7 = new BooleanSetting(ЯЖД, true);
      this.М = new KeybindSetting(ЯЖЪ, -1);
      this.к_at_52ce3431 = this::Л;
      this.г_at_52ce3431 = this::й;
      this.field_0_at_52ce3431 = this::l;
      this.В_3cf9aa23 = AutoBrewerBuilder.BuildStage.IDLE;
      this.addSettings(this.field_7, this.М);
   }

   private void й(InputEvent var1) {
      int var2 = this.М.getValue();
      if (var2 != -1 && var2 == var1.getInputCode() && var1.getAction() == 1) {
         if (this.isEnabled()) {
            this.Р(ЯЖт);
            this.Ш(null);
         }
      }
   }

   private void нп() {
      с4 = false;
      сп = false;
      this.В_3cf9aa23 = AutoBrewerBuilder.BuildStage.IDLE;
      this.В_50e6a30b = null;
      this.В_10b53497 = null;
      this.ВВ = 0;
      this.сД = false;
      this.С = null;
      this.сЪ = false;
      this.ст = false;
   }

   private void Ш(String var1) {
      с4 = false;
      сп = false;
      this.С = var1;
      this.В_3cf9aa23 = AutoBrewerBuilder.BuildStage.STOP_PENDING;
   }

   private int Й(Item var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         ItemStack var4 = MINECRAFT_CLIENT.player.getInventory().getItem(var3);
         if (var4.is(var1)) {
            var2 += var4.getCount();
         }
      }

      ItemStack var5 = MINECRAFT_CLIENT.player.getOffhandItem();
      if (var5.is(var1)) {
         var2 += var5.getCount();
      }

      return var2;
   }

   private void l(WorldEffectsEvent var1) {
      if (this.isEnabled() && this.В_50e6a30b != null && MINECRAFT_CLIENT.level != null) {
         try {
            VertexConsumer var2 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
            Vec3 var3 = var1.getCameraPosition();

            for (BlockPos var5 : this.В_50e6a30b.У()) {
               if (!this.н(var5, Blocks.CHEST)) {
                  this.н(var1, var2, var3, var5, ЯЖа);
               }
            }

            for (BlockPos var9 : this.В_50e6a30b.и()) {
               if (!this.н(var9, Blocks.HOPPER)) {
                  this.н(var1, var2, var3, var9, -26624);
               }
            }

            for (BlockPos var10 : this.В_50e6a30b.с()) {
               if (!this.н(var10, Blocks.BREWING_STAND)) {
                  this.н(var1, var2, var3, var10, ЯЖЮ);
               }
            }
         } catch (Throwable var6) {
         }
      }
   }

   private void Л(PlayerTickEvent var1) {
      if (!this.сЪ && this.isEnabled()) {
         try {
            if (MINECRAFT_CLIENT.player == null || MINECRAFT_CLIENT.level == null) {
               this.Ш(ЯЖЧ);
               return;
            }

            switch (this.В_3cf9aa23) {
               case START_PENDING:
                  this.нЬ();
                  break;
               case BUILD_STRUCTURE:
                  this.нд();
                  break;
               case STOP_PENDING:
                  this.нЕ();
            }
         } catch (Throwable var3) {
            var3.printStackTrace();
            this.Ш("Ошибка: " + var3.getClass().getSimpleName() + ": " + var3.getMessage());
         }
      }
   }

   private void нЕ() {
      if (!this.сЪ) {
         this.сЪ = true;
         с4 = false;
         сп = false;

         try {
            if (this.сД) {
               BaritoneIntegration.гШ();
               BaritoneIntegration.г2();
            }
         } catch (Throwable var2) {
         }

         if (this.С != null && !this.С.isEmpty()) {
            this.Р("AutoBrewerBuilder: " + this.С);
         }

         if (this.isEnabled()) {
            this.disable();
         }
      }
   }

   private List<BlockPos> н(int var1) {
      ArrayList var2 = new ArrayList();

      for (BlockPos var4 : this.В_50e6a30b.Я()) {
         BlockState var5 = this.В_50e6a30b.з(var4);
         if (var5 != null && !AutoBrewerSchematic.Б(var5) && !this.н(MINECRAFT_CLIENT.level.getBlockState(var4), var5)) {
            var2.add(var4.immutable());
            if (var2.size() >= var1) {
               break;
            }
         }
      }

      return var2;
   }

   private void н(WorldEffectsEvent var1, VertexConsumer var2, Vec3 var3, BlockPos var4, int var5) {
      WorldBoxGeometry.appendBox(
         var1.getPoses(), var2, var3, var4.getX(), var4.getY(), var4.getZ(), var4.getX() + 1, var4.getY() + 1, var4.getZ() + 1, var5, false
      );
   }

   private void нд() {
      boolean var1 = BaritoneIntegration.ЮИ();
      if (var1) {
         if (!this.ст) {
            this.ж(Items.COBBLESTONE);
            this.ж(Items.REPEATER);
            this.ж(Items.REDSTONE);
            this.ж(Items.LEVER);
            this.ж(Items.REDSTONE_TORCH);
         }

         BaritoneIntegration.г0();
      }

      this.ст = var1;
      List var2 = this.н(20);
      boolean var3 = BaritoneIntegration.Ю8() && !var1;
      if (!var2.isEmpty()) {
         if (var3) {
            this.ВВ = 0;
         } else {
            this.ВВ++;
            if (this.ВВ == 60 || this.ВВ == 140) {
               try {
                  this.ж(Items.COBBLESTONE);
                  this.ж(Items.REPEATER);
                  this.ж(Items.REDSTONE);
                  this.ж(Items.REDSTONE_TORCH);
                  BaritoneIntegration.г0();
                  BaritoneIntegration.Ъ(ЯЖЫ, this.В_50e6a30b, this.В_10b53497);
               } catch (Throwable var6) {
               }
            }

            if (this.ВВ > 500) {
               BlockPos var4 = (BlockPos)var2.get(0);
               BlockState var5 = this.В_50e6a30b.з(var4);
               this.Ш("Baritone застрял на " + (var5 == null ? ЯЖl : var5.getBlock().getName().getString()) + " @ " + var4.toShortString());
            }
         }
      } else {
         BaritoneIntegration.г2();
         BaritoneIntegration.гШ();
         this.Р(ЯЖ8);
         this.Ш(null);
      }
   }

   private String O西() {
      ArrayList var1 = new ArrayList();

      for (Entry var3 : this.В_50e6a30b.Б().entrySet()) {
         Item var4 = (Item)var3.getKey();
         if (var4 != Items.CHEST && var4 != Items.HOPPER && var4 != Items.BREWING_STAND) {
            int var5 = this.Й(var4);
            if (var4 == Items.REDSTONE_TORCH) {
               for (BlockPos var7 : this.В_50e6a30b.method_6()) {
                  if (AutoBrewerSchematic.ы(MINECRAFT_CLIENT.level.getBlockState(var7))) {
                     var5++;
                  }
               }
            }

            if (var5 < (Integer)var3.getValue()) {
               var1.add(var4.getDefaultInstance().getItemName().getString() + " x" + ((Integer)var3.getValue() - var5));
            }
         }
      }

      return var1.isEmpty() ? null : "Не хватает: " + String.join(ЯЖ5, var1);
   }

   private String OТ() {
      for (BlockPos var2 : this.В_50e6a30b.Я()) {
         BlockState var3 = MINECRAFT_CLIENT.level.getBlockState(var2);
         if (!var3.isAir() && (!this.field_7.getValue() || !var3.canBeReplaced())) {
            BlockState var4 = this.В_50e6a30b.з(var2);
            if ((var4 == null || var3.getBlock() != var4.getBlock()) && (var4 == null || !AutoBrewerSchematic.Б(var4))) {
               return "Место занято: " + var3.getBlock().getName().getString() + " @ " + var2.toShortString();
            }
         }
      }

      return null;
   }

   private void Р(String var1) {
      try {
         if (MINECRAFT_CLIENT.player != null) {
            ClientChat.З(var1);
         }
      } catch (Throwable var3) {
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.нп();
      this.В_3cf9aa23 = AutoBrewerBuilder.BuildStage.START_PENDING;
   }

   private enum BuildStage {
      IDLE,
      START_PENDING,
      BUILD_STRUCTURE,
      STOP_PENDING;
   }
}
