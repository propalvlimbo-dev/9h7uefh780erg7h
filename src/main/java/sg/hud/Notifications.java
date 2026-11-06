package sg.hud;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Generated;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import sg.hud.HudDisplacementManager;
import sg.network.StatusNotificationToast;
import sg.render.WorldProjection;
import sg.util.time.ColorMath;
import sg.gui.hud.HudSettingsPanel;
import sg.modules.impl.init.NotificationCategory;
import sg.modules.impl.init.NotificationToast;
import sg.settings.MultiChoiceSetting;

public final class Notifications extends HudElement implements InteractiveHudElement {
   private final MultiChoiceSetting н;
   private final ArrayList<NotificationCategory> 诶 = new ArrayList<>();
   @SerializedName("А")
   private final HudSettingsPanel А_at_5e96588b;
   @SerializedName("В")
   private NotificationToast В_29299991;
   private NotificationToast Я;
   @SerializedName("В")
   private NotificationCategory В_791d2e21;
   private long Хф;
   private static final String скХ = "Notifications";
   private static final float ске = -1.0F;
   private static final String скА = "Уведомления";
   private static final String ск4 = "Показывать";
   private static final float скп = 0.5F;
   private static final float скД = 0.5F;
   private static final float скЪ = 42.0F;
   private static final long скт = 2000000000L;
   private static final String ска = "Выберите уведомления через ПКМ";
   private static final String скЮ = "Player";
   private static final String скЧ = "TNT рядом — взорвётся через 2 сек.";
   private static final String скЛ = "H";
   private static final String ск诶 = "Модуль Beautiful включён";
   private static final String ск1 = "H";
   private static final String скЬ = "Инвентарь отсортирован";
   private static final long ск衣 = 2000L;

   public float method_6К() {
      return this.baseY;
   }

   @Override
   public void Б5() {
      this.А_at_5e96588b.сС();
      this.Жэ();
   }

   @Override
   public void иВ() {
      this.А_at_5e96588b.сУ();
   }

   @Override
   public float getY() {
      return this.getBaseY() + HudDisplacementManager.getVerticalDisplacement(this);
   }

   public Notifications(MultiChoiceSetting var1) {
      super(скХ, 0.0F, ске, 0.0F, 0.0F);
      this.н = var1;
      this.А_at_5e96588b = new HudSettingsPanel(скА, ск4, var1);
   }

   private NotificationToast т(NotificationCategory var1) {
      ItemStack var2 = ItemStack.EMPTY;
      String var3 = "";
      int var5 = ColorMath.getAnimatedThemeColor(0);
      String var4;
      if (var1 == null) {
         var4 = ска;
      } else {
         switch (var1) {
            case ARMOR:
               var2 = new ItemStack(ThreadLocalRandom.current().nextBoolean() ? Items.DIAMOND_HELMET : Items.DIAMOND_CHESTPLATE);
               int var7 = ThreadLocalRandom.current().nextBoolean() ? 5 : 10;
               var2.setDamageValue(var2.getMaxDamage() - Math.max(1, var2.getMaxDamage() * var7 / 100));
               var4 = "Скоро сломается: " + var2.getHoverName().getString() + " — осталось " + var7 + "%";
               var5 = ColorMath.packRgb(255, 180, 65);
               break;
            case EFFECTS:
               var2 = new ItemStack(Items.POTION);
               var2.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withEffectAdded(new MobEffectInstance(MobEffects.SPEED, 40)));
               var4 = "Скоро закончится эффект: " + PotionContents.getPotionDescription(MobEffects.SPEED, 0).getString() + " — осталось 2 сек.";
               break;
            case SELF_USE:
            case PLAYER_USE:
               var2 = new ItemStack(ThreadLocalRandom.current().nextBoolean() ? Items.GOLDEN_APPLE : Items.ENCHANTED_GOLDEN_APPLE);
               String var6 = var1 == NotificationCategory.SELF_USE ? MINECRAFT_CLIENT.player.getName().getString() : скЮ;
               var4 = var6 + " съел " + var2.getHoverName().getString();
               break;
            case TNT:
               var2 = new ItemStack(Items.TNT);
               var4 = скЧ;
               var5 = ColorMath.packRgb(255, 70, 70);
               break;
            case MODULES:
               var3 = скЛ;
               var4 = ск诶;
               break;
            default:
               var3 = ск1;
               var4 = скЬ;
         }
      }

      return new StatusNotificationToast(var3, Component.literal(var4), var5, var2, ск衣);
   }

   @Generated
   public NotificationToast т() {
      return this.В_29299991;
   }

   @Override
   public void setX(float var1) {
   }

   @Override
   public float getX() {
      return (MINECRAFT_CLIENT.getWindow().getScreenWidth() - this.getScaledWidth()) * скп;
   }

   @Override
   public float getBaseY() {
      float var1 = this.baseY < 0.0F ? MINECRAFT_CLIENT.getWindow().getScreenHeight() * скД + скЪ : this.baseY;
      return Math.clamp(var1, 0.0F, Math.max(0.0F, MINECRAFT_CLIENT.getWindow().getScreenHeight() - this.getScaledHeight()));
   }

   public void Жэ() {
      this.В_29299991 = null;
      this.Я = null;
      this.В_791d2e21 = null;
      this.Хф = 0L;
   }

   @Override
   public boolean ш(float var1, float var2, int var3) {
      if (!(MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen) || !this.hasRenderableSize()) {
         return false;
      } else if (this.А_at_5e96588b.ю(var1, var2, var3)) {
         return true;
      } else if (var3 == 1 && WorldProjection.containsPoint(var1, var2, this.getX(), this.getY(), this.getScaledWidth(), this.getScaledHeight())) {
         this.А_at_5e96588b.м(this.getX(), this.getY(), this.getScaledWidth());
         return true;
      } else {
         return false;
      }
   }

   @Generated
   public NotificationToast method_6_29299991() {
      return this.Я;
   }

   public void ЖЫ() {
      if (MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen && MINECRAFT_CLIENT.player != null) {
         long var1 = System.nanoTime();
         if (this.В_29299991 == null
            || var1 >= this.Хф
            || this.В_791d2e21 != null && !this.В_791d2e21.ъ(this.н)
            || this.В_791d2e21 == null && !this.н.getValue().stream().noneMatch(var0 -> var0.isSelected())) {
            this.诶.clear();

            for (NotificationCategory var6 : NotificationCategory.values()) {
               if (var6.ъ(this.н)) {
                  this.诶.add(var6);
               }
            }

            if (this.诶.size() > 1) {
               this.诶.remove(this.В_791d2e21);
            }

            NotificationCategory var7 = this.诶.isEmpty() ? null : this.诶.get(ThreadLocalRandom.current().nextInt(this.诶.size()));
            this.Я = this.В_29299991;
            if (this.Я != null) {
               this.Я.ИК();
            }

            this.В_29299991 = this.т(var7);
            this.В_791d2e21 = var7;
            this.Хф = var1 + скт;
         }
      } else {
         this.Жэ();
      }
   }

   @Generated
   public HudSettingsPanel method_9() {
      return this.А_at_5e96588b;
   }
}
