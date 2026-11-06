package sg.modules.impl.movement;

import com.google.gson.annotations.SerializedName;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import sg.events.impl.PlayerMovementEvent;
import sg.ec.PacketEventBypass;
import sg.events.impl.PlayerTickEvent;
import sg.events.impl.PacketReceiveEvent;
import sg.ec.ClientTimerSpeed;
import sg.events.EventListener;
import sg.events.impl.PacketSendEvent;
import sg.settings.BooleanSetting;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.events.impl.PlayerMovementSyncEvent;
import sg.settings.FloatSetting;
import sg.modules.impl.init.MovementUtils;
import sg.settings.SingleChoiceSetting;

public class Fly extends ClientModule {
   private final SingleChoiceSetting сЭ;
   private final FloatSetting е必;
   private final FloatSetting ещ;
   private final BooleanSetting ЯЩ;
   private final SingleChoiceSetting с必;
   private final SingleChoiceSetting сщ;
   private final BooleanSetting Яу;
   private final BooleanSetting Я2;
   private final FloatSetting еГ;
   private final FloatSetting ел;
   private final FloatSetting еэ;
   private final FloatSetting ею;
   private final FloatSetting еЩ;
   private final FloatSetting еу;
   private final FloatSetting е2;
   private int Ъц;
   @SerializedName("Х")
   private final ConcurrentHashMap<Integer, Fly.PositionSample> Х_at_62f8e93e;
   private int Ъя;
   private boolean А0;
   private boolean Ах;
   private float Ю6;
   private float Ю_;
   private final EventListener<PlayerTickEvent> еЫ;
   private final EventListener<PlayerMovementEvent> еl;
   private final EventListener<PlayerMovementSyncEvent> е8;
   private final EventListener<PacketSendEvent> е5;
   private final EventListener<PacketReceiveEvent> ев;
   private static final String Ц2 = "Fly";
   private static final String Цф = "Полет";
   private static final String Ци = "Mode";
   private static final String Ц9 = "Vanilla";
   private static final String Цо = "Metahvh";
   private static final String ЦO = "Horizontal";
   private static final float Ц7 = 0.1F;
   private static final float ЦР = 5.0F;
   private static final float Ц3 = 0.1F;
   private static final String Цр = "Vertical";
   private static final float Ц6 = 0.5F;
   private static final float Ц_ = 3.0F;
   private static final float ЦШ = 0.1F;
   private static final String Цы = "Y-Jitter";
   private static final String Цй = "Type";
   private static final String ЦЖ = "Bounds";
   private static final String Цм = "Phase";
   private static final String ЦК = "Full";
   private static final String ЦI = "Off";
   private static final String ЦТ = "Semi";
   private static final String ЦО = "Limit";
   private static final String ЦФ = "AntiKick";
   private static final String ЦЙ = "AntiKickOffset";
   private static final float Цк = 0.04F;
   private static final float Цг = 0.008F;
   private static final float Ц0 = 0.001F;
   private static final String Цх = "Speed";
   private static final float ЦУ = 0.5F;
   private static final float ЦЗ = 10.0F;
   private static final float ЦЕ = 0.01F;
   private static final String ЦП = "UpSpeed";
   private static final float Ц西 = 0.06F;
   private static final float Цд = 0.01F;
   private static final float ЦБ = 0.01F;
   private static final String ЦС = "Timer";
   private static final float Цз = 0.1F;
   private static final float Цц = 5.0F;
   private static final float Ця = 0.1F;
   private static final String чс = "IncreaseTicks";
   private static final float чВ = 20.0F;
   private static final String чЯ = "Factor";
   private static final float чХ = 10.0F;
   private static final String че = "Offset";
   private static final float чА = 1337.0F;
   private static final float ч4 = 1337.0F;
   private static final String чп = "Vanilla";
   private static final double чД = 0.1;
   private static final double чЪ = -0.1;
   private static final String чт = "Metahvh";
   private static final String ча = "Metahvh";
   private static final String чЮ = "Metahvh";
   private static final double чЧ = -0.0625;
   private static final String чЛ = "Full";
   private static final double ч诶 = 2.5;
   private static final double ч1 = 1.5;
   private static final String чЬ = "Full";
   private static final double ч衣 = 0.034444444444444444;
   private static final double чш = 0.26;
   private static final double чИ = 20.0;
   private static final double чь = 20.0;
   private static final String чж = "Off";
   private static final String ч弟 = "Semi";
   private static final double чб = 10.0;
   private static final double чЫ = 255.0;
   private static final float чl = -45.0F;
   private static final float ч8 = 45.0F;
   private static final float ч5 = 45.0F;
   private static final float чв = -45.0F;
   private static final float чъ = -1.0F;
   private static final float чМ = 90.0F;
   private static final float чН = 90.0F;
   private static final String чн = "Metahvh";
   private static final String чЦ = "Metahvh";
   private static final long чч = 30000L;
   private static final String чЭ = "Metahvh";
   private static final String ч必 = "Metahvh";
   private static final String чщ = "Metahvh";
   private static final String чГ = "Metahvh";
   private static final String чл = "Metahvh";
   private static final String чэ = "Metahvh";
   private static final String чю = "Metahvh";
   private static final String чЩ = "Metahvh";
   private static final String чу = "Metahvh";
   private static final String ч2 = "Metahvh";
   private static final String чф = "Metahvh";
   private static final String чи = "Vanilla";
   private static final String ч9 = "Vanilla";
   private static final String чо = "Vanilla";

   public Fly() {
      super(Ц2, Цф, ModuleCategory.MOVEMENT);
      this.сЭ = new SingleChoiceSetting(Ци, new ChoiceOption(Ц9), new ChoiceOption(Цо));
      this.е必 = new FloatSetting(ЦO, 1.0F, Ц7, ЦР).withStep(Ц3).visibleWhen(() -> this.сЭ.isSelected(чо));
      this.ещ = new FloatSetting(Цр, Ц6, 0.0F, Ц_).withStep(ЦШ).visibleWhen(() -> this.сЭ.isSelected(ч9));
      this.ЯЩ = new BooleanSetting(Цы, false).visibleWhen(() -> this.сЭ.isSelected(чи));
      this.с必 = new SingleChoiceSetting(Цй, new ChoiceOption(ЦЖ)).visibleWhen(() -> this.сЭ.isSelected(чф));
      this.сщ = new SingleChoiceSetting(Цм, new ChoiceOption(ЦК), new ChoiceOption(ЦI), new ChoiceOption(ЦТ)).visibleWhen(() -> this.сЭ.isSelected(ч2));
      this.Яу = new BooleanSetting(ЦО, true).visibleWhen(() -> this.сЭ.isSelected(чу));
      this.Я2 = new BooleanSetting(ЦФ, true).visibleWhen(() -> this.сЭ.isSelected(чЩ));
      this.еГ = new FloatSetting(ЦЙ, Цк, Цг, 1.0F).withStep(Ц0).visibleWhen(() -> this.сЭ.isSelected(чю) && this.Я2.getValue());
      this.ел = new FloatSetting(Цх, ЦУ, 0.0F, ЦЗ).withStep(ЦЕ).visibleWhen(() -> this.сЭ.isSelected(чэ));
      this.еэ = new FloatSetting(ЦП, Ц西, Цд, 1.0F).withStep(ЦБ).visibleWhen(() -> this.сЭ.isSelected(чл));
      this.ею = new FloatSetting(ЦС, 1.0F, Цз, Цц).withStep(Ця).visibleWhen(() -> this.сЭ.isSelected(чГ));
      this.еЩ = new FloatSetting(чс, 1.0F, 1.0F, чВ).withStep(1.0F).visibleWhen(() -> this.сЭ.isSelected(чщ));
      this.еу = new FloatSetting(чЯ, 1.0F, 1.0F, чХ).withStep(1.0F).visibleWhen(() -> this.сЭ.isSelected(ч必));
      this.е2 = new FloatSetting(че, чА, 1.0F, ч4).withStep(1.0F).visibleWhen(() -> this.сЭ.isSelected(чЭ));
      this.Ъц = -1;
      this.Х_at_62f8e93e = new ConcurrentHashMap<>();
      this.еЫ = this::弟西;
      this.еl = this::е;
      this.е8 = this::ш;
      this.е5 = this::п;
      this.ев = this::西;
      this.addSettings(this.сЭ, this.е必, this.ещ, this.ЯЩ, this.с必, this.сщ, this.Яу, this.Я2, this.еГ, this.ел, this.еэ, this.ею, this.еЩ, this.еу, this.е2);
   }

   private void е(PlayerMovementEvent var1) {
      if (this.сЭ.isSelected(ча) && MINECRAFT_CLIENT.player != null) {
         MINECRAFT_CLIENT.player.setDeltaMovement(Vec3.ZERO);
         var1.setMovement(Vec3.ZERO);
      }
   }

   private void 西(PacketReceiveEvent var1) {
      if (this.сЭ.isSelected(чЦ) && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (var1.getPacket() instanceof ClientboundPlayerPositionPacket var2) {
            Fly.PositionSample var6 = this.Х_at_62f8e93e.remove(var2.id());
            Vec3 var4 = var2.change().position();
            boolean var5 = MINECRAFT_CLIENT.level.hasChunk((int)MINECRAFT_CLIENT.player.getX() >> 4, (int)MINECRAFT_CLIENT.player.getZ() >> 4);
            if (MINECRAFT_CLIENT.player.isAlive() && var5 && var6 != null && var6.x == var4.x && var6.y == var4.y && var6.z == var4.z) {
               var1.setCancelled(true);
            } else {
               this.Ю6 = MINECRAFT_CLIENT.player.getYRot();
               this.Ю_ = MINECRAFT_CLIENT.player.getXRot();
               this.Ах = true;
               this.Ъц = var2.id();
            }
         }
      }
   }

   private void п(PacketSendEvent var1) {
      if (this.сЭ.isSelected(чн)) {
         if (var1.getPacket() instanceof ServerboundMovePlayerPacket) {
            var1.setCancelled(true);
         }
      }
   }

   @Override
   public void onEnable() {
      ClientTimerSpeed.Оэ(1.0F);
      this.Ъц = -1;
      this.Х_at_62f8e93e.clear();
      this.Ъя = 0;
      this.А0 = false;
      this.Ах = false;
      super.onEnable();
   }

   @Override
   public void onDisable() {
      ClientTimerSpeed.Оэ(1.0F);
      if (MINECRAFT_CLIENT.player != null) {
         MINECRAFT_CLIENT.player.getAbilities().flying = false;
         MINECRAFT_CLIENT.player.noPhysics = false;
         MINECRAFT_CLIENT.player.setDeltaMovement(Vec3.ZERO);
      }

      super.onDisable();
   }

   private Vec3 у(Vec3 var1) {
      return new Vec3(var1.x, MINECRAFT_CLIENT.player.getY() <= чб ? чЫ : 1.0, var1.z);
   }

   private double[] ъ(double var1) {
      float var3 = MINECRAFT_CLIENT.player.input.getMoveVector().y;
      float var4 = MINECRAFT_CLIENT.player.input.getMoveVector().x;
      float var5 = MINECRAFT_CLIENT.player.getYRot();
      if (var3 != 0.0F) {
         if (var4 > 0.0F) {
            var5 += var3 > 0.0F ? чl : ч8;
         } else if (var4 < 0.0F) {
            var5 += var3 > 0.0F ? ч5 : чв;
         }

         var4 = 0.0F;
         if (var3 > 0.0F) {
            var3 = 1.0F;
         } else if (var3 < 0.0F) {
            var3 = чъ;
         }
      }

      double var6 = Math.sin(Math.toRadians(var5 + чМ));
      double var8 = Math.cos(Math.toRadians(var5 + чН));
      return new double[]{var3 * var1 * var8 + var4 * var1 * var6, var3 * var1 * var6 - var4 * var1 * var8};
   }

   private void 弟西(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null) {
         if (this.Ах) {
            MINECRAFT_CLIENT.player.setYRot(this.Ю6);
            MINECRAFT_CLIENT.player.setXRot(this.Ю_);
            this.Ах = false;
         }

         if (this.сЭ.isSelected(чп)) {
            MovementUtils.setHorizontalSpeed(this.е必.getValue().floatValue());
            double var2 = 0.0;
            if (MINECRAFT_CLIENT.options.keyJump.isDown()) {
               var2 = this.ещ.getValue().floatValue();
            }

            if (MINECRAFT_CLIENT.options.keyShift.isDown()) {
               var2 = -this.ещ.getValue();
            }

            if (this.ЯЩ.getValue()) {
               var2 += MINECRAFT_CLIENT.player.tickCount % 2 == 0 ? чД : чЪ;
            }

            Vec3 var4 = MINECRAFT_CLIENT.player.getDeltaMovement();
            MINECRAFT_CLIENT.player.setDeltaMovement(var4.x, var2, var4.z);
         } else {
            if (this.сЭ.isSelected(чт)) {
               this.Х_at_62f8e93e.entrySet().removeIf(var0 -> System.currentTimeMillis() - var0.getValue().time > чч);
            }
         }
      }
   }

   private void ш(PlayerMovementSyncEvent var1) {
      if (this.сЭ.isSelected(чЮ) && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (!var1.isCancelled()) {
            if (this.Ъц != 0) {
               if (this.ею.getValue() != 1.0F) {
                  ClientTimerSpeed.Оэ(this.ею.getValue());
               }

               boolean var2 = MINECRAFT_CLIENT.level.getBlockCollisions(MINECRAFT_CLIENT.player, MINECRAFT_CLIENT.player.getBoundingBox().inflate(чЧ)).iterator().hasNext();
               Vec2 var3 = MINECRAFT_CLIENT.player.input.getMoveVector();
               boolean var4 = var3.y == 0.0F && var3.x == 0.0F;
               double var5 = 0.0;
               if (!MINECRAFT_CLIENT.options.keyJump.isDown() || !var2 && !var4) {
                  if (MINECRAFT_CLIENT.options.keyShift.isDown()) {
                     var5 = -this.еэ.getValue();
                  } else if (this.Я2.getValue() && !var2 && MINECRAFT_CLIENT.player.tickCount % 20 == 0) {
                     var5 = -this.еГ.getValue();
                  }
               } else {
                  var5 = this.еэ.getValue().floatValue();
               }

               if (this.сщ.isSelected(чЛ) && var2 && !var4 && var5 != 0.0) {
                  var5 = MINECRAFT_CLIENT.options.keyJump.isDown() ? var5 / ч诶 : var5 / ч1;
               }

               double var7 = this.сщ.isSelected(чЬ) && var2 ? ч衣 : this.ел.getValue().floatValue() * чш;
               double[] var9 = this.ъ(var7);
               int var10 = 1;
               if (MINECRAFT_CLIENT.player.tickCount % this.еЩ.getValue().intValue() == 0) {
                  var10 = (int)Math.floor(this.еу.getValue().floatValue());
                  this.Ъя++;
                  double var11 = this.еу.getValue() - var10;
                  if (var11 != 0.0) {
                     double var13 = чИ / (var11 * чь);
                     if (this.Ъя > (int)var13) {
                        var10++;
                        this.Ъя = 0;
                     }
                  }
               }

               for (int var18 = 1; var18 <= var10; var18++) {
                  double var12 = var9[0] * var18;
                  double var14 = var5 * var18;
                  double var16 = var9[1] * var18;
                  if (this.Яу.getValue()) {
                     if (MINECRAFT_CLIENT.player.tickCount % 2 == 0) {
                        if (this.А0 && var14 >= 0.0) {
                           this.А0 = false;
                           var14 = -this.еэ.getValue() / 2.0F;
                        }

                        MINECRAFT_CLIENT.player.setDeltaMovement(var12, var14, var16);
                        this.Й(var12, var14, var16, false);
                     } else if (var14 < 0.0) {
                        this.А0 = true;
                     }
                  } else {
                     MINECRAFT_CLIENT.player.setDeltaMovement(var12, var14, var16);
                     this.Й(var12, var14, var16, true);
                  }
               }

               if (!this.сщ.isSelected(чж) && !this.сщ.isSelected(ч弟) && var2) {
                  MINECRAFT_CLIENT.player.noPhysics = true;
               }

               var1.setPosition(MINECRAFT_CLIENT.player.position());
               var1.setYaw(MINECRAFT_CLIENT.player.getYRot());
               var1.setPitch(MINECRAFT_CLIENT.player.getXRot());
               var1.setOnGround(MINECRAFT_CLIENT.player.onGround());
               var1.setCancelled(true);
            }
         }
      }
   }

   private void Й(double var1, double var3, double var5, boolean var7) {
      Vec3 var8 = MINECRAFT_CLIENT.player.position().add(var1, var3, var5);
      Vec3 var9 = this.у(var8);
      boolean var10 = MINECRAFT_CLIENT.player.onGround();
      boolean var11 = MINECRAFT_CLIENT.player.horizontalCollision;
      PacketEventBypass.А(new Pos(var8.x, var8.y, var8.z, var10, var11));
      PacketEventBypass.А(new Pos(var9.x, var9.y, var9.z, var10, var11));
      if (var7) {
         PacketEventBypass.А(new ServerboundAcceptTeleportationPacket(++this.Ъц));
         this.Х_at_62f8e93e.put(this.Ъц, new Fly.PositionSample(var8.x, var8.y, var8.z, System.currentTimeMillis()));
      }
   }

   private record PositionSample(double x, double y, double z, long time) {
   }
}
