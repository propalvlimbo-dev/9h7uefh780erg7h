package sg.hud;

import java.util.Locale;
import lombok.Generated;
import net.minecraft.client.gui.screens.ChatScreen;
import sg.events.EventListener;
import sg.render.WorldProjection;
import sg.client.RoseClient;
import sg.modules.impl.misc.LootTracker;
import sg.events.impl.HudRenderEvent;
import sg.gui.hud.HudSettingsPanel;
import sg.settings.FloatSetting;

public class LootTrackerHudElement extends HudElement implements InteractiveHudElement {
   private final FloatSetting к;
   private final HudSettingsPanel В;
   private boolean с2;
   private final EventListener<HudRenderEvent> ст = this::К;
   private static final String сян = "LootTracker";
   private static final String сяЦ = "LootTracker";
   private static final String сяч = "Размер";
   private static final String сяЭ = "%.2fx";

   @Override
   public void Б5() {
      this.В.сС();
   }

   @Override
   public float getScaledWidth() {
      return this.getWidth();
   }

   @Override
   public boolean ш(float var1, float var2, int var3) {
      if (!this.иг()) {
         return false;
      } else if (var3 == 1 && this.getScaledWidth() > 0.0F && this.getScaledHeight() > 0.0F && WorldProjection.containsPoint(var1, var2, this.getX(), this.getY(), this.getScaledWidth(), this.getScaledHeight())) {
         this.В.method_2(var1, var2);
         return true;
      } else {
         return this.В.ю(var1, var2, var3);
      }
   }

   public LootTrackerHudElement(FloatSetting var1) {
      super(сян, 0.0F, 0.0F, 0.0F, 0.0F);
      this.к = var1;
      this.В = new HudSettingsPanel(сяЦ, сяч, var1, var0 -> String.format(Locale.US, сяЭ, var0));
   }

   public boolean иы() {
      return this.с2 && (this.x != 0.0F || this.baseY != 0.0F);
   }

   @Generated
   public FloatSetting l() {
      return this.к;
   }

   @Generated
   public HudSettingsPanel и_10d41d08() {
      return this.В;
   }

   private boolean иг() {
      try {
         LootTracker var1 = RoseClient.getInstance().getServices().getModules().getLootTracker();
         return var1 != null && var1.isEnabled();
      } catch (Throwable var2) {
         return false;
      }
   }

   private void К(HudRenderEvent var1) {
      if (!(MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen) || !this.иг()) {
         this.В.сС();
      }
   }

   @Override
   public void иВ() {
      this.В.сУ();
   }

   public void Б(float var1, float var2, float var3, float var4) {
      if (!this.с2 && this.x == 0.0F && this.baseY == 0.0F) {
         this.x = var1;
         this.baseY = var2;
         this.с2 = true;
      }

      if (var3 > 0.0F && var4 > 0.0F) {
         this.setWidth(var3);
         this.setHeight(var4);
      } else if (!(MINECRAFT_CLIENT.gui.screen() instanceof ChatScreen)) {
         this.setWidth(0.0F);
         this.setHeight(0.0F);
      }
   }

   @Override
   public float getScaledHeight() {
      return this.getHeight();
   }
}
