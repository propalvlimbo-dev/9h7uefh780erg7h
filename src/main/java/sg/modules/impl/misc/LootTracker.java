package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Generated;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.joml.Matrix4f;
import org.joml.Vector4i;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.hud.HudScaleCoordinates;
import sg.ec.ClientFonts;
import sg.render.pipeline.UiDrawing;
import sg.theme.ThemeColorSlot;
import sg.util.time.Stopwatch;
import sg.util.time.ThemeEditor;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;
import sg.hud.LootTrackerHudElement;
import sg.modules.impl.init.PanelBackgroundRenderer;

public class LootTracker extends ClientModule {
   private static final int сИ = 5;
   private static final float сы = 12.0F;
   private static final float сй = 10.0F;
   private static final float сЖ = 18.0F;
   private static final float см = 8.0F;
   private static final float сК = 34.0F;
   private static final float сI = 12.0F;
   private static final float сТ = 13.0F;
   private static final float сО = 8.0F;
   private static final float сФ = 14.0F;
   private static final int сь = ColorMath.packRgb(255, 40, 200);
   private static final int сж = ColorMath.packRgb(40, 230, 255);
   @SerializedName("7")
   private final FloatSetting field_7;
   private final FloatSetting Р;
   private final EventListener<PlayerTickEvent> ю;
   private final EventListener<HudRenderEvent> Щ;
   private final Map<String, Integer> 诶;
   private final List<LootTracker.LootEntry> ш;
   @SerializedName("1")
   private final Map<String, ItemStack> field_1;
   @SerializedName("_")
   private boolean field__;
   private static final String см_ = "LootTracker";
   private static final String смШ = "Плашка при подборе донатных предметов на ReallyWorld";
   private static final String смы = "Длительность";
   private static final float смй = 3.5F;
   private static final float смЖ = 1.5F;
   private static final float смм = 8.0F;
   private static final float смК = 0.5F;
   private static final String смI = "Размер";
   private static final float смТ = 0.6F;
   private static final float смО = 0.05F;
   private static final float смФ = 1000.0F;
   private static final float смЙ = -1.0F;
   private static final float смк = 14.0F;
   private static final float смг = 0.01F;
   private static final float см0 = 0.01F;
   private static final float смх = 34.0F;
   private static final float смУ = 14.0F;
   private static final float смЗ = 24.0F;
   private static final float смЕ = 8.0F;
   private static final String смП = "Донат";
   private static final float см西 = 34.0F;
   private static final float смд = 14.0F;
   private static final float смБ = 34.0F;
   private static final float смС = 220.0F;
   private static final float смз = 14.0F;
   private static final float смц = 255.0F;
   private static final float смя = 18.0F;
   private static final float сКс = 12.0F;
   private static final float сКВ = 8.0F;
   private static final float сКЯ = 13.0F;
   private static final float сКХ = 0.5F;
   private static final float сКе = 16.0F;
   private static final float сКА = 0.5F;
   private static final String сК4 = "Вы подобрали ";
   private static final float сКп = 12.0F;
   private static final float сКД = 0.5F;
   private static final float сКЪ = 14.0F;
   private static final float сКт = 1.6F;
   private static final float сКа = 22.0F;
   private static final double сКЮ = 255.0;
   private static final double сКЧ = 145.0;
   private static final double сКЛ = 125.0;
   private static final float сК诶 = 13.0F;
   private static final String сК1 = "Вы подобрали ";
   private static final float сКЬ = 50.0F;
   private static final float сК衣 = 10.0F;

   private static String л(ItemStack var0) {
      return var0.getItem().toString() + "|" + StringUtil.stripColor(var0.getHoverName().getString());
   }

   public static boolean Я(ItemStack var0) {
      if (var0 != null && !var0.isEmpty()) {
         Component var1 = var0.get(DataComponents.CUSTOM_NAME);
         String var2 = StringUtil.stripColor(var0.getHoverName().getString()).trim();
         String var3 = StringUtil.stripColor(Component.translatable(var0.getItem().getDescriptionId()).getString()).trim();
         boolean var4 = var1 != null || !var2.isEmpty() && !var2.equalsIgnoreCase(var3);
         if (!var4) {
            return false;
         } else if (var1 != null && У(var1)) {
            return true;
         } else if (У(var0.getHoverName())) {
            return true;
         } else if (var0.has(DataComponents.CUSTOM_DATA)) {
            return true;
         } else if (var0.has(DataComponents.ATTRIBUTE_MODIFIERS)) {
            return true;
         } else {
            ItemLore var5 = var0.get(DataComponents.LORE);
            if (var5 != null && !var5.lines().isEmpty()) {
               return true;
            } else {
               return method_2(var0) ? true : var1 != null;
            }
         }
      } else {
         return false;
      }
   }

   private void ш(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         HashMap<String, Integer> var2 = new HashMap<>();
         this.field_1.clear();
         this.л(var2);
         if (this.field__) {
            for (Entry var4 : var2.entrySet()) {
               int var5 = (Integer)var4.getValue() - this.诶.getOrDefault(var4.getKey(), 0);
               if (var5 > 0) {
                  ItemStack var6 = this.field_1.get(var4.getKey());
                  if (var6 != null) {
                     this.У(var6.copyWithCount(Math.min(var5, var6.getMaxStackSize())));
                  }
               }
            }
         }

         this.诶.clear();
         this.诶.putAll(var2);
         this.field__ = true;
      } else {
         this.мг();
      }
   }

   @Generated
   public FloatSetting я() {
      return this.Р;
   }

   @Override
   public void onDisable() {
      this.诶.clear();
      this.field_1.clear();
      this.ш.clear();
      this.field__ = false;
   }

   private void У(ItemStack var1, String var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      UiDrawing.setGlobalAlpha(смц * var8);
      this.method_3(var3, var4, var5, var6, var7);
      float var9 = смя * var7;
      float var10 = сКс * var7;
      float var11 = сКВ * var7;
      int var12 = Math.max(1, Math.round(сКЯ * var7));
      float var13 = var3 + var10;
      float var14 = var4 + (var6 - var9) * сКХ;
      UiDrawing.drawItem(var1, var13, var14, var9 / сКе, false);
      float var15 = var13 + var9 + var11;
      float var16 = var4 + (var6 - var12) * сКА - 1.0F;
      String var17 = сК4;
      int var18 = ColorMath.withAlpha(ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT), var8);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var17, var15, var16, var12, var18);
      float var19 = var15 + UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var17, var12);
      UiDrawing.drawText(ClientFonts.getMediumFont(), У(var2), var19, var16, var12, -1);
      UiDrawing.resetGlobalAlpha();
   }

   @Generated
   public List<LootTracker.LootEntry> э_2571477d() {
      return this.ш;
   }

   private void У(Map<String, Integer> var1, ItemStack var2) {
      if (var2 != null && !var2.isEmpty() && Я(var2)) {
         String var3 = л(var2);
         var1.merge(var3, var2.getCount(), Integer::sum);
         if (!this.field_1.containsKey(var3)) {
            this.field_1.put(var3, var2.copy());
         }
      }
   }

   @Generated
   public FloatSetting ж() {
      return this.field_7;
   }

   private LootTrackerHudElement У_b9e04de1() {
      try {
         if (RoseClient.getInstance() == null || RoseClient.getInstance().getServices() == null) {
            return null;
         } else {
            return !RoseClient.getInstance().getServices().getHudElements().isEnabled() ? null : RoseClient.getInstance().getServices().getHudElements().getLootTracker();
         }
      } catch (Throwable var2) {
         return null;
      }
   }

   private float У(String var1, float var2) {
      int var3 = Math.max(1, Math.round(сК诶 * var2));
      String var4 = сК1;
      return сКЬ * var2 + UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var4, var3) + UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var1, var3) + сК衣 * var2;
   }

   private Vector4i У_1a8864ae() {
      int var1 = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_BACKGROUND);
      int var2 = ColorMath.getRed(var1);
      int var3 = ColorMath.getGreen(var1);
      int var4 = ColorMath.getBlue(var1);
      int var5 = ColorMath.packArgb(Math.min(255, var2 + 12), Math.min(255, var3 + 12), Math.min(255, var4 + 12), сКЧ);
      int var6 = ColorMath.packArgb(var2, var3, var4, сКЛ);
      return new Vector4i(var5, var5, var6, var6);
   }

   private static boolean У(Component var0) {
      AtomicBoolean var1 = new AtomicBoolean(false);
      var0.visit((var1x, var2) -> {
         TextColor var3 = var1x.getColor();
         if (var3 != null) {
            var1.set(true);
         }

         return Optional.empty();
      }, Style.EMPTY);
      return var1.get();
   }

   private float 诶_f67ab10a() {
      LootTrackerHudElement var1 = this.У_b9e04de1();
      return var1 != null && var1.l() != null ? var1.l().getValue() * HudScaleCoordinates.getHudScale() : this.Р.getValue() * HudScaleCoordinates.getHudScale();
   }

   @Generated
   public Map<String, ItemStack> method_3() {
      return this.field_1;
   }

   public void У(ItemStack var1) {
      String var2 = StringUtil.stripColor(var1.getHoverName().getString()).trim();
      if (!var2.isEmpty()) {
         LootTracker.LootEntry var3 = new LootTracker.LootEntry(var1, var2);
         var3.field_0.setImmediately(0.0F);
         var3.х.setImmediately(1.0F);
         this.ш.add(0, var3);

         while (this.ш.size() > 5) {
            this.ш.remove(this.ш.size() - 1);
         }
      }
   }

   @Generated
   public EventListener<PlayerTickEvent> Ы() {
      return this.ю;
   }

   @Override
   public void onEnable() {
      this.мг();
   }

   @Generated
   public boolean мO() {
      return this.field__;
   }

   public LootTracker() {
      super(см_, смШ, ModuleCategory.MISC);
      this.field_7 = new FloatSetting(смы, смй, смЖ, смм).withStep(смК);
      this.Р = new FloatSetting(смI, 1.0F, смТ, 2.0F).withStep(смО);
      this.ю = this::ш;
      this.Щ = this::Х;
      this.诶 = new HashMap<>();
      this.ш = new ArrayList<>();
      this.field_1 = new HashMap<>();
      this.addSettings(this.field_7, this.Р);
   }

   private void л(Map<String, Integer> var1) {
      int var2 = MINECRAFT_CLIENT.player.getInventory().getContainerSize();

      for (int var3 = 0; var3 < var2; var3++) {
         this.У(var1, MINECRAFT_CLIENT.player.getInventory().getItem(var3));
      }

      this.У(var1, MINECRAFT_CLIENT.player.getOffhandItem());
   }

   @Generated
   public EventListener<HudRenderEvent> Я() {
      return this.Щ;
   }

   private static boolean method_2(ItemStack var0) {
      ItemEnchantments var1 = var0.get(DataComponents.ENCHANTMENTS);
      if (var1 == null || var1.isEmpty()) {
         var1 = var0.get(DataComponents.STORED_ENCHANTMENTS);
      }

      if (var1 != null && !var1.isEmpty()) {
         for (it.unimi.dsi.fastutil.objects.Object2IntMap.Entry var3 : var1.entrySet()) {
            if (var3.getIntValue() > ((Enchantment)((Holder)var3.getKey()).value()).getMaxLevel()) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static Component У(String var0) {
      MutableComponent var1 = Component.empty();
      int var2 = var0.length();

      for (int var3 = 0; var3 < var2; var3++) {
         float var4 = var2 == 1 ? 0.0F : (float)var3 / (var2 - 1);
         int var5 = ColorMath.lerpArgb(сь, сж, var4);
         var1.append(Component.literal(String.valueOf(var0.charAt(var3))).setStyle(Style.EMPTY.withColor(var5)));
      }

      return var1;
   }

   private void Х(HudRenderEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.getWindow() != null) {
         float var2 = this.诶_f67ab10a();
         long var3 = (long)(this.field_7.getValue() * смФ);
         boolean var5 = MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen;
         UiDrawing.setTransform(new Matrix4f());
         float var6 = MINECRAFT_CLIENT.getWindow().getScreenWidth();
         LootTrackerHudElement var7 = this.У_b9e04de1();
         boolean var8 = var7 != null && var7.иы();
         float var9 = var8 ? var7.getX() : смЙ;
         float var10 = var8 ? var7.getY() : смк;
         float var11 = var10;
         float var12 = var11;
         float var13 = 0.0F;
         float var14 = var9;
         int var15 = 0;
         Iterator var16 = this.ш.iterator();

         while (var16.hasNext()) {
            LootTracker.LootEntry var17 = (LootTracker.LootEntry)var16.next();
            boolean var18 = var17.Ю.method_0(var3);
            var17.field_0.updateTarget(var18 ? 0.0F : 1.0F);
            var17.х.updateTarget(var18 ? 1.0F : 0.0F);
            if (var18 && var17.field_0.isFinished() && var17.field_0.getValue() < смг) {
               var16.remove();
            } else {
               float var19 = var17.field_0.getValue();
               if (!(var19 < см0)) {
                  float var20 = this.У(var17.с西, var2);
                  float var21 = смх * var2;
                  float var22 = var8 ? var9 : Math.max(0.0F, var6 - var20 - смУ);
                  float var23 = var22 + var17.х.getValue() * (var20 + смЗ);
                  if (var15 == 0) {
                     var14 = var22;
                  }

                  this.У(var17.Д, var17.с西, var23, var12, var20, var21, var2, var19);
                  var13 = Math.max(var13, var20);
                  var12 += var21 + смЕ * var2;
                  var15++;
               }
            }
         }

         if (var15 == 0 && var5) {
            ItemStack var24 = MINECRAFT_CLIENT.player.getMainHandItem();
            if (var24.isEmpty()) {
               var24 = Items.NETHERITE_SWORD.getDefaultInstance();
            }

            String var25 = StringUtil.stripColor(var24.getHoverName().getString()).trim();
            if (var25.isEmpty()) {
               var25 = смП;
            }

            float var26 = this.У(var25, var2);
            float var27 = см西 * var2;
            float var28 = var8 ? var9 : Math.max(0.0F, var6 - var26 - смд);
            var14 = var28;
            this.У(var24, var25, var28, var11, var26, var27, var2, 1.0F);
            var13 = var26;
            var12 = var11 + var27;
            var15 = 1;
         }

         if (var7 != null) {
            if (var15 > 0) {
               var7.Б(var14, var10, var13, Math.max(смБ * var2, var12 - var11));
            } else {
               var7.Б(var8 ? var9 : Math.max(0.0F, var6 - смС * var2 - смз), var10, 0.0F, 0.0F);
            }
         }
      }
   }

   @Generated
   public Map<String, Integer> л() {
      return this.诶;
   }

    private void method_3(float var1, float var2, float var3, float var4, float var5) {
       float var6 = сКп * var5;
       sg.hud.HudBackground.draw(var1, var2, var3, var4, var6);
   }

   private void мг() {
      this.诶.clear();
      this.field_1.clear();
      this.field__ = false;
   }

   public static final class LootEntry {
      private final ItemStack Д;
      private final String с西;
      private final Stopwatch Ю = new Stopwatch();
      @SerializedName("0")
      private final FloatAnimation field_0;
      private final FloatAnimation х;
      private static final float Я5Ы = 10.0F;
      private static final float Я5l = 10.0F;

      private LootEntry(ItemStack var1, String var2) {
         this.field_0 = new FloatAnimation(0.0F, Я5Ы, Easings.CUBIC_OUT);
         this.х = new FloatAnimation(1.0F, Я5l, Easings.CUBIC_OUT);
         this.Д = var1;
         this.с西 = var2;
      }
   }
}
