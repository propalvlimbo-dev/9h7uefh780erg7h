package sg.modules.impl.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import sg.ec.WorldBoxGeometry;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.settings.BooleanSetting;
import sg.util.time.ReallyWorldItemCooldowns;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ColorSetting;
import sg.modules.impl.init.RoseXrayLines;
import sg.modules.impl.misc.LootTracker;

public class Tracers extends ClientModule {
   private final ColorSetting л;
   private final ColorSetting э;
   private final BooleanSetting В1;
   private final BooleanSetting ВЬ;
   private final EventListener<WorldEffectsEvent> ЯЫ;
   private static final String Р7 = "Tracers";
   private static final String РР = "Рисует линии к энтити";
   private static final String Р3 = "Цвет";
   private static final String Рр = "Цвет Друзей";
   private static final String Р6 = "Работать на людей";
   private static final String Р_ = "Работать на дон предметы";
   private static final double РШ = 150.0;
   private static final int Ры = -16777216;
   private static final long Рй = 255L;
   private static final long РЖ = 255L;
   private static final long Рм = 255L;

   private int о(Component var1, int var2) {
      long[] var3 = new long[4];
      var1.visit((var1x, var2x) -> {
         TextColor var3x = var1x.getColor();
         if (var3x != null && !var2x.isEmpty()) {
            int var4x = var3x.getValue();
            int var5x = var2x.codePointCount(0, var2x.length());
            var3[0] += (var4x >> 16 & Рй) * var5x;
            var3[1] += (var4x >> 8 & РЖ) * var5x;
            var3[2] += (var4x & Рм) * var5x;
            var3[3] += var5x;
         }

         return Optional.empty();
      }, Style.EMPTY);
      if (var3[3] == 0L) {
         return var2;
      }

      int var4 = (int)(var3[0] / var3[3]);
      int var5 = (int)(var3[1] / var3[3]);
      int var6 = (int)(var3[2] / var3[3]);
      return Ры | var4 << 16 | var5 << 8 | var6;
   }

   public Tracers() {
      super(Р7, РР, ModuleCategory.RENDER);
      this.л = new ColorSetting(Р3, false, -1);
      this.э = new ColorSetting(Рр, false, -1);
      this.В1 = new BooleanSetting(Р6, true);
      this.ВЬ = new BooleanSetting(Р_, false);
      this.ЯЫ = this::弟;
      this.addSettings(this.л, this.э, this.В1, this.ВЬ);
   }

   private void о(PoseStack var1, VertexConsumer var2, Vec3 var3, Vec3 var4, float var5) {
      for (Entity var7 : MINECRAFT_CLIENT.level.entitiesForRendering()) {
         if (var7 instanceof ItemEntity var8 && var8.isAlive() && !var8.isRemoved()) {
            ItemStack var9 = var8.getItem();
            if (LootTracker.Я(var9)) {
               Vec3 var10 = new Vec3(Mth.lerp(var5, var8.xo, var8.getX()), Mth.lerp(var5, var8.yo, var8.getY()), Mth.lerp(var5, var8.zo, var8.getZ()))
                  .subtract(var4);
               int var11 = this.о(var8.getCustomName() != null ? var8.getCustomName() : var9.getHoverName(), this.л.getValue());
               WorldBoxGeometry.appendLine(var1.last(), var2, (float)var3.x, (float)var3.y, (float)var3.z, (float)var10.x, (float)var10.y, (float)var10.z, var11);
            }
         }
      }
   }

   private void 弟(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         PoseStack var2 = var1.getPoses();
         VertexConsumer var3 = var1.getGeometryBuffer().getBuffer(RoseXrayLines.LINES);
         Vec3 var4 = var1.getCameraPosition();
         float var5 = var1.getTickDelta();
         Camera var6 = MINECRAFT_CLIENT.gameRenderer.mainCamera();
         Vec3 var7 = new Vec3(0.0, 0.0, РШ).xRot((float)(-Math.toRadians(var6.xRot()))).yRot((float)(-Math.toRadians(var6.yRot())));
         if (this.В1.getValue()) {
            for (AbstractClientPlayer var9 : MINECRAFT_CLIENT.level.players()) {
               if (var9 != MINECRAFT_CLIENT.player && var9.isAlive() && !var9.isRemoved()) {
                  double var10 = Mth.lerp(var5, var9.xo, var9.getX());
                  double var12 = Mth.lerp(var5, var9.yo, var9.getY());
                  double var14 = Mth.lerp(var5, var9.zo, var9.getZ());
                  Vec3 var16 = new Vec3(var10, var12, var14).subtract(var4);
                  int var17 = this.л.getValue();
                  if (RoseClient.getInstance().getServices().getFriends().О(var9.getGameProfile().name())) {
                     var17 = this.э.getValue();
                  }

                  WorldBoxGeometry.appendLine(var2.last(), var3, (float)var7.x, (float)var7.y, (float)var7.z, (float)var16.x, (float)var16.y, (float)var16.z, var17);
               }
            }
         }

         if (this.ВЬ.getValue() && ReallyWorldItemCooldowns.Ы().method_3Щ()) {
            this.о(var2, var3, var7, var4, var5);
         }
      }
   }
}
