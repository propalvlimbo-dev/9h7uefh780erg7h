package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.impl.combat.Aura;
import sg.events.impl.CameraRotationEvent;
import sg.settings.ChoiceOption;
import sg.settings.MultiChoiceSetting;
import sg.events.impl.PlayerTurnEvent;
import sg.rotation.RotationController;

public class AimingItems extends ClientModule {
   @SerializedName("1")
   private final MultiChoiceSetting field_1;
   private ItemEntity В;
   private float ез;
   private float ец;
   private boolean ВС;
   private long Вl;
   private final EventListener<PlayerTickEvent> ВХ;
   private final EventListener<CameraRotationEvent> Ве;
   private final EventListener<PlayerTurnEvent> ВА;
   private static final String ъЩ = "AimingItems";
   private static final String ъу = "Автоматически прицеливается на ценные предметы";
   private static final String ъ2 = "Элементы";
   private static final String ъф = "Шары";
   private static final String ъи = "Элитра";
   private static final String ъ9 = "Чарки";
   private static final String ъо = "Осколки";
   private static final String ъO = "Ауры";
   private static final String ъ7 = "Шары";
   private static final String ъР = "Осколки";
   private static final String ъ3 = "Осколок";
   private static final String ър = "Элитра";
   private static final String ъ6 = "Чарки";
   private static final String ъ_ = "Ауры";
   private static final float ъШ = 1.0E-9F;
   private static final float ъы = 0.05F;
   private static final float ъй = -7.5F;
   private static final float ъЖ = -90.0F;
   private static final float ъм = 90.0F;
   private static final String ъК = "Аура Охотника";
   private static final String ъI = "Аура Твёрдости Брони";
   private static final String ъТ = "Аура Телепортации";
   private static final String ъО = "Аура Богача";
   private static final String ъФ = "Аура Защиты От Падения";
   private static final double ъЙ = 2.0;
   private static final double ък = 90.0;

   @Override
   public void onDisable() {
      this.ФБ();
   }

   private boolean ч(ItemStack var1, String var2) {
      return var1.is(Items.SUNFLOWER) && var2.contains(ъК)
         || var1.is(Items.CLAY_BALL) && var2.contains(ъI)
         || var1.is(Items.POPPED_CHORUS_FRUIT) && var2.contains(ъТ)
         || var1.is(Items.GOLD_NUGGET) && var2.contains(ъО)
         || var1.is(Items.DYE.white()) && var2.contains(ъФ);
   }

   public void Я(PlayerTickEvent var1) {
      LocalPlayer var2 = MINECRAFT_CLIENT.player;
      ClientLevel var3 = MINECRAFT_CLIENT.level;
      if (var2 != null && var3 != null) {
         this.В = null;

         for (Entity var5 : var3.entitiesForRendering()) {
            if (var5 instanceof ItemEntity var6 && !var6.isRemoved()) {
               ItemStack var7 = var6.getItem();
               String var8 = var7.getHoverName().getString();
               boolean var9 = this.field_1.isSelected(ъ7) && var7.is(Items.PLAYER_HEAD);
               boolean var10 = this.field_1.isSelected(ъР) && var8.contains(ъ3);
               boolean var11 = this.field_1.isSelected(ър) && var7.is(Items.ELYTRA);
               boolean var12 = this.field_1.isSelected(ъ6) && var7.is(Items.ENCHANTED_GOLDEN_APPLE);
               boolean var13 = this.field_1.isSelected(ъ_) && this.ч(var7, var8);
               if (var9 || var10 || var11 || var12 || var13) {
                  this.В = var6;
               }
            }
         }

         if (!this.чВ()) {
            this.ФБ();
         }
      } else {
         this.ФБ();
      }
   }

   private void Ф(PlayerTurnEvent var1) {
      if (this.чВ()) {
         var1.setCancelled(true);
      }
   }

   public AimingItems() {
      super(ъЩ, ъу, ModuleCategory.MISC);
      this.field_1 = new MultiChoiceSetting(
         ъ2,
         new ChoiceOption(ъф, true),
         new ChoiceOption(ъи, true),
         new ChoiceOption(ъ9, true),
         new ChoiceOption(ъо, true),
         new ChoiceOption(ъO, true)
      );
      this.ВХ = this::Я;
      this.Ве = this::Ф;
      this.ВА = this::Ф;
      this.addSettings(this.field_1);
   }

   private UVPair т(LocalPlayer var1, Entity var2, float var3) {
      Vec3 var4 = var1.getEyePosition(var3);
      Vec3 var5 = var2.getPosition(var3);
      double var6 = var5.x;
      double var8 = var5.y + var2.getBbHeight() / ъЙ;
      double var10 = var5.z;
      double var12 = var6 - var4.x;
      double var14 = var8 - var4.y;
      double var16 = var10 - var4.z;
      double var18 = Math.sqrt(var12 * var12 + var16 * var16);
      float var20 = (float)(Math.toDegrees(Math.atan2(var16, var12)) - ък);
      float var21 = (float)(-Math.toDegrees(Math.atan2(var14, var18)));
      return new UVPair(var20, var21);
   }

   private boolean чВ() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !MINECRAFT_CLIENT.isPaused() && this.В != null && !this.В.isRemoved() && this.В.level() == MINECRAFT_CLIENT.level) {
         Aura var1 = RoseClient.getInstance().getServices().getModules().getAura();
         return (var1 == null || var1.Ж_95bedd6e() == null) && !RoseClient.getInstance().getServices().getModules().getFreeCam().isEnabled();
      } else {
         return false;
      }
   }

   private void Ф(CameraRotationEvent var1) {
      if (!this.чВ()) {
         this.ФБ();
      } else {
         LocalPlayer var2 = MINECRAFT_CLIENT.player;
         float var3 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         UVPair var4 = this.т(var2, this.В, var3);
         RotationController var5 = RoseClient.getInstance().getControllers().getRotations();
         long var6 = System.nanoTime();
         if (!this.ВС) {
            this.ез = var5.getEffectiveStrategy() != null ? var5.getCameraRotation().x : var2.getYRot();
            this.ец = var5.getEffectiveStrategy() != null ? var5.getCameraRotation().y : var2.getXRot();
            this.ВС = true;
            this.Вl = var6;
         }

         float var8 = Mth.clamp((float)(var6 - this.Вl) * ъШ, 0.0F, ъы);
         this.Вl = var6;
         float var9 = 1.0F - (float)Math.exp(ъй * var8);
         this.ез = this.ез + Mth.wrapDegrees(var4.u() - this.ез) * var9;
         this.ец = this.ец + (var4.v() - this.ец) * var9;
         this.ец = Mth.clamp(this.ец, ъЖ, ъм);
         var2.setYRot(this.ез);
         var2.setXRot(this.ец);
         var2.yRotO = this.ез;
         var2.xRotO = this.ец;
         var5.getCameraRotation().set(this.ез, this.ец);
         var5.getAppliedRotation().set(this.ез, this.ец);
         var1.setYaw(this.ез);
         var1.setPitch(this.ец);
      }
   }

   private void ФБ() {
      this.В = null;
      this.ВС = false;
      this.Вl = 0L;
   }
}
