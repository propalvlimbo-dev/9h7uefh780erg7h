package sg.modules.impl.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Camera;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerTickEvent;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.settings.BooleanSetting;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.modules.impl.init.RoseXrayLines;
import sg.settings.FloatSetting;
import sg.settings.MultiChoiceSetting;
import sg.modules.impl.init.NotificationManager;

public class GroundLoot extends ClientModule {
   private static final long еж = 8000L;
   private final @com.google.gson.annotations.SerializedName("Э")  MultiChoiceSetting trackedItemsSetting;
   private final @com.google.gson.annotations.SerializedName("еБ")  FloatSetting radiusSetting;
   private final @com.google.gson.annotations.SerializedName("Ям")  BooleanSetting notificationsSetting;
   private final @com.google.gson.annotations.SerializedName("В8")  Map<Integer, Long> lastNotificationByEntityId;
   private final @com.google.gson.annotations.SerializedName("ею")  EventListener<PlayerTickEvent> playerTickListener;
   private final @com.google.gson.annotations.SerializedName("еЩ")  EventListener<WorldEffectsEvent> worldEffectsListener;
   private static final String ъЮ = "GroundLoot";
   private static final String ъЧ = "Подсветка и тост, если рядом на земле донат, осколки, элитры, тотем или голова";
   private static final String ъЛ = "Предметы";
   private static final String ъ诶 = "Донат";
   private static final String ъ1 = "Осколки";
   private static final String ъЬ = "Элитры";
   private static final String ъ衣 = "Тотем";
   private static final String ъш = "Голова";
   private static final String ъИ = "Радиус";
   private static final float ъь = 24.0F;
   private static final float ъж = 8.0F;
   private static final float ъ弟 = 48.0F;
   private static final String ъб = "Уведомление";
   private static final long ъЫ = 8000L;
   private static final double ъl = 150.0;
   private static final double ъ8 = 70.0;
   private static final double ъ5 = 255.0;
   private static final double ъв = 0.18;
   private static final double ъъ = 220.0;
   private static final String ъМ = "Донат";
   private static final String ъН = "донат";
   private static final String ън = "Осколки";
   private static final String ъЦ = "осколок";
   private static final String ъч = "Тотем";
   private static final String ъЭ = "тотем";
   private static final String ъ必 = "Элитры";
   private static final String ъщ = "элитры";
   private static final String ъГ = "Голова";
   private static final String ъл = "голова";
   private static final String ъэ = "лут";
   private static final String ъю = "осколок";

   private void сп(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         PoseStack var2 = var1.getPoses();
         Vec3 var3 = var1.getCameraPosition();
         float var4 = var1.getTickDelta();
         Camera var5 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         Vec3 var6 = new Vec3(0.0, 0.0, ъl).xRot((float)(-Math.toRadians(var5.xRot()))).yRot((float)(-Math.toRadians(var5.yRot())));
         VertexConsumer var7 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
         VertexConsumer var8 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.QUADS);

         for (ItemEntity var10 : MINECRAFT_CLIENT.level.getEntitiesOfClass(ItemEntity.class, MINECRAFT_CLIENT.player.getBoundingBox().inflate(this.radiusSetting.getValue().floatValue()), this::method_8)) {
            int var11 = У(var10.getItem());
            int var12 = ColorMath.packArgb(var11 >> 16 & 0xFF, var11 >> 8 & 0xFF, var11 & 0xFF, ъ8);
            int var13 = ColorMath.packArgb(var11 >> 16 & 0xFF, var11 >> 8 & 0xFF, var11 & 0xFF, ъ5);
            AABB var14 = var10.getBoundingBox().inflate(ъв);
            WorldBoxGeometry.appendBox(var2, var8, var3, var14.minX, var14.minY, var14.minZ, var14.maxX, var14.maxY, var14.maxZ, var12, true);
            WorldBoxGeometry.appendBoxOutline(var2, var7, var3, var14.minX, var14.minY, var14.minZ, var14.maxX, var14.maxY, var14.maxZ, var13);
            Vec3 var15 = new Vec3(Mth.lerp(var4, var10.xo, var10.getX()), Mth.lerp(var4, var10.yo, var10.getY()), Mth.lerp(var4, var10.zo, var10.getZ()))
               .subtract(var3);
            WorldBoxGeometry.appendLine(
               var2.last(),
               var7,
               (float)var6.x,
               (float)var6.y,
               (float)var6.z,
               (float)var15.x,
               (float)var15.y,
               (float)var15.z,
               ColorMath.packArgb(var11 >> 16 & 0xFF, var11 >> 8 & 0xFF, var11 & 0xFF, ъъ)
            );
         }
      }
   }

   private boolean method_8(Entity var1) {
      if (!(var1 instanceof ItemEntity var2 && var2.isAlive() && !var2.isRemoved())) {
         return false;
      } else {
         return MINECRAFT_CLIENT.player.distanceToSqr(var2) > this.radiusSetting.getValue() * this.radiusSetting.getValue() ? false : this.ц(var2.getItem()) != null;
      }
   }

   private static int У(ItemStack var0) {
      int var1 = у(var0);
      if (var1 != 0) {
         return var1;
      } else {
         Item var2 = var0.getItem();
         if (св(var0)) {
            return ColorMath.packRgb(255, 230, 90);
         } else if (сы(var0)) {
            return ColorMath.packRgb(255, 210, 70);
         } else if (var2 == Items.ELYTRA) {
            return ColorMath.packRgb(90, 210, 255);
         } else {
            return ы(var2) ? ColorMath.packRgb(255, 170, 210) : ColorMath.packRgb(255, 40, 200);
         }
      }
   }

   private static boolean ы(Item var0) {
      return var0 == Items.PLAYER_HEAD
         || var0 == Items.ZOMBIE_HEAD
         || var0 == Items.CREEPER_HEAD
         || var0 == Items.SKELETON_SKULL
         || var0 == Items.WITHER_SKELETON_SKULL
         || var0 == Items.DRAGON_HEAD
         || var0 == Items.PIGLIN_HEAD;
   }

   private static int у(ItemStack var0) {
      if (var0 != null && !var0.isEmpty()) {
         AtomicInteger var1 = new AtomicInteger(0);
         var0.getHoverName().visit((var1x, var2) -> {
            if (var1.get() == 0 && var2 != null && !var2.isBlank()) {
               TextColor var3 = var1x.getColor();
               if (var3 == null) {
                  return Optional.empty();
               }

               var1.set(var3.getValue());
               return Optional.of(Boolean.TRUE);
            } else {
               return Optional.empty();
            }
         }, Style.EMPTY);
         return var1.get();
      } else {
         return 0;
      }
   }

   private void с4(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         long var2 = System.currentTimeMillis();
         double var4 = this.radiusSetting.getValue().floatValue();

         for (ItemEntity var7 : MINECRAFT_CLIENT.level.getEntitiesOfClass(ItemEntity.class, MINECRAFT_CLIENT.player.getBoundingBox().inflate(var4), this::method_8)) {
            if (this.notificationsSetting.getValue()) {
               Long var8 = this.lastNotificationByEntityId.get(var7.getId());
               if (var8 == null || var2 - var8 >= ъЫ) {
                  this.lastNotificationByEntityId.put(var7.getId(), var2);
                  ItemStack var9 = var7.getItem();
                  String var10 = this.Ж(var9);
                  NotificationManager.р("", Component.literal("Рядом на земле: " + var10), "", -1, var9.copy());
               }
            }
         }

         Iterator var11 = this.lastNotificationByEntityId.entrySet().iterator();

         while (var11.hasNext()) {
            int var12 = (Integer)((Entry)var11.next()).getKey();
            if (MINECRAFT_CLIENT.level.getEntity(var12) == null) {
               var11.remove();
            }
         }
      } else {
         this.lastNotificationByEntityId.clear();
      }
   }

   private String Ж(ItemStack var1) {
      String var2 = this.ц(var1);
      if (var2 == null) {
         return ъэ;
      }

      String var3 = StringUtil.stripColor(var1.getHoverName().getString()).trim();
      return var3.isEmpty() ? var2 : var3;
   }

   private String ц(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         Item var2 = var1.getItem();
         if (this.trackedItemsSetting.isSelected(ъМ) && LootTracker.Я(var1)) {
            return ъН;
         } else if (this.trackedItemsSetting.isSelected(ън) && св(var1)) {
            return ъЦ;
         } else if (this.trackedItemsSetting.isSelected(ъч) && сы(var1)) {
            return ъЭ;
         } else if (this.trackedItemsSetting.isSelected(ъ必) && var2 == Items.ELYTRA) {
            return ъщ;
         } else {
            return this.trackedItemsSetting.isSelected(ъГ) && ы(var2) ? ъл : null;
         }
      } else {
         return null;
      }
   }

   private static boolean св(ItemStack var0) {
      String var1 = StringUtil.stripColor(var0.getHoverName().getString()).trim();
      String var2 = var1.toLowerCase(Locale.ROOT).replace('ё', 'е');
      if (!var2.contains(ъю)) {
         return false;
      }

      String var3 = StringUtil.stripColor(Component.translatable(var0.getItem().getDescriptionId()).getString()).trim();
      return !var1.equalsIgnoreCase(var3);
   }

   private static boolean сы(ItemStack var0) {
      if (var0 != null && !var0.isEmpty() && var0.is(Items.TOTEM_OF_UNDYING)) {
         String var1 = StringUtil.stripColor(var0.getHoverName().getString()).trim();
         String var2 = StringUtil.stripColor(Component.translatable(Items.TOTEM_OF_UNDYING.getDescriptionId()).getString()).trim();
         return !var1.equalsIgnoreCase(var2);
      } else {
         return false;
      }
   }

   @Override
   public void onDisable() {
      this.lastNotificationByEntityId.clear();
   }

   public GroundLoot() {
      super(ъЮ, ъЧ, ModuleCategory.MISC);
      this.trackedItemsSetting = new MultiChoiceSetting(
         ъЛ,
         new ChoiceOption(ъ诶, true),
         new ChoiceOption(ъ1, true),
         new ChoiceOption(ъЬ, true),
         new ChoiceOption(ъ衣, true),
         new ChoiceOption(ъш, true)
      );
      this.radiusSetting = new FloatSetting(ъИ, ъь, ъж, ъ弟).withStep(1.0F);
      this.notificationsSetting = new BooleanSetting(ъб, true);
      this.lastNotificationByEntityId = new HashMap<>();
      this.playerTickListener = this::с4;
      this.worldEffectsListener = this::сп;
      this.addSettings(this.trackedItemsSetting, this.radiusSetting, this.notificationsSetting);
   }
}
