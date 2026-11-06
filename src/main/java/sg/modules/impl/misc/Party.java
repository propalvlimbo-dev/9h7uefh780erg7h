package sg.modules.impl.misc;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.handler.PartyClient;
import sg.network.ClientChat;
import sg.render.pipeline.PartyMarker;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;
import sg.events.impl.InputEvent;
import sg.settings.KeybindSetting;
import sg.settings.TextSetting;

public final class Party extends ClientModule {
   private final KeybindSetting о;
   private final KeybindSetting O;
   @SerializedName("А")
   private final TextSetting А_at_670f2154;
   private final FloatSetting Х5;
   private final PartyMarker В;
   private final EventListener<InputEvent> Хе;
   private final EventListener<WorldEffectsEvent> ХА;
   private final EventListener<HudRenderEvent> Х4;
   private static final String В诶Э = "Party";
   private static final String В诶必 = "Общие светящиеся метки для участников IRC-группы";
   private static final String В诶щ = "Поставить метку";
   private static final String В诶Г = "Убрать свою метку";
   private static final String В诶л = "Название метки";
   private static final String В诶э = "Метка";
   private static final String В诶ю = "Время метки (сек)";
   private static final float В诶Щ = 60.0F;
   private static final float В诶у = 15.0F;
   private static final float В诶2 = 180.0F;
   private static final float В诶ф = 5.0F;
   private static final String В诶и = "Метка не отправлена: вы не состоите в .party.";
   private static final double В诶9 = 256.0;
   private static final double В诶о = 256.0;
   private static final double В诶O = 65536.0;
   private static final double В诶7 = 0.035;
   private static final double В诶Р = 0.035;
   private static final double В诶3 = 0.035;
   private static final double В诶р = 0.035;
   private static final long В诶6 = 32L;
   private static final String В诶_ = "Метка";
   private static final float В诶Ш = 1000.0F;
   private static final String В诶ы = "Метка не отправлена: данные игрока или группы ещё не готовы.";

   public void Дю() {
      this.В.эЛ();
   }

   public Party() {
      super(В诶Э, В诶必, ModuleCategory.MISC);
      this.о = new KeybindSetting(В诶щ);
      this.O = new KeybindSetting(В诶Г);
      this.А_at_670f2154 = new TextSetting(В诶л, В诶э);
      this.Х5 = new FloatSetting(В诶ю, В诶Щ, В诶у, В诶2).withStep(В诶ф);
      this.В = new PartyMarker();
      this.Хе = this::ъ;
      this.ХА = var1 -> this.В.Ф(var1, ThemeEditor.getColorArgb(ThemeColorSlot.MAIN));
      this.Х4 = var1 -> this.В.Ф(ThemeEditor.getColorArgb(ThemeColorSlot.MAIN), var1.getGraphics());
      this.addSettings(this.о, this.O, this.А_at_670f2154, this.Х5);
   }

   private void ъ(InputEvent var1) {
      if (MINECRAFT_CLIENT.gui.screen() == null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && var1.getAction() == 1) {
         if (this.O.getValue() >= 0 && var1.getInputCode() == this.O.getValue()) {
            PartyClient.Х_4d6328fc().ХВ();
         } else if (this.о.getValue() >= 0 && var1.getInputCode() == this.о.getValue()) {
            PartyClient var2 = PartyClient.Х_4d6328fc();
            if (!var2.ХХ()) {
               ClientChat.З(В诶и);
            } else {
               long var3 = var2.ф();
               if (var3 > 0L) {
                  ClientChat.З("Метка не отправлена: подождите " + var3 + " мс. перед следующей меткой.");
               } else {
                  HitResult var5 = MINECRAFT_CLIENT.player.pick(В诶9, 1.0F, false);
                  Vec3 var6 = MINECRAFT_CLIENT.player.getEyePosition();
                  Vec3 var7 = MINECRAFT_CLIENT.player.getViewVector(1.0F).scale(В诶о);
                  double var8 = var5.getType() == Type.BLOCK ? var6.distanceToSqr(var5.getLocation()) : В诶O;
                  EntityHitResult var10 = ProjectileUtil.getEntityHitResult(
                     MINECRAFT_CLIENT.player,
                     var6,
                     var6.add(var7),
                     MINECRAFT_CLIENT.player.getBoundingBox().expandTowards(var7).inflate(1.0),
                     var0 -> var0 instanceof LivingEntity var1x && var1x.isAlive() && !var0.isSpectator(),
                     var8
                  );
                  Vec3 var11 = MINECRAFT_CLIENT.player.position().add(0.0, В诶7, 0.0);
                  if (var5 instanceof BlockHitResult var12 && var5.getType() == Type.BLOCK) {
                     var11 = var12.getLocation()
                        .add(var12.getDirection().getStepX() * В诶Р, var12.getDirection().getStepY() * В诶3, var12.getDirection().getStepZ() * В诶р);
                  }

                  StringBuilder var17 = new StringBuilder();
                  this.А_at_670f2154.getValue().strip().codePoints().filter(var0 -> !Character.isISOControl(var0) && var0 != 167).limit(В诶6).forEach(var1x -> {
                     if (var17.length() + Character.charCount(var1x) <= 32) {
                        var17.appendCodePoint(var1x);
                     }
                  });
                  if (var17.isEmpty()) {
                     var17.append(В诶_);
                  }

                  int var13 = Math.round(this.Х5.getValue() * В诶Ш);
                  boolean var14 = var10 != null && var10.getEntity() instanceof LivingEntity var15
                     ? var2.Х(var17.toString(), var15, var13)
                     : var2.Х(var17.toString(), var11.x, var11.y, var11.z, var13);
                  if (!var14) {
                     ClientChat.З(В诶ы);
                  }
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      this.В.эЛ();
   }
}
