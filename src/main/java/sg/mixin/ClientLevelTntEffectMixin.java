package sg.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sg.modules.impl.render.TNTEffect;

@Mixin(ClientLevel.class)
public final class ClientLevelTntEffectMixin {
    @Inject(method = "explode", at = @At("HEAD"))
    private void rose$explosion(Entity source, DamageSource damage, ExplosionDamageCalculator calculator,
                                double x, double y, double z, float power, boolean fire,
                                Level.ExplosionInteraction interaction, ParticleOptions small, ParticleOptions large,
                                WeightedList<ExplosionParticleInfo> particles, Holder<SoundEvent> sound, CallbackInfo ci) {
        TNTEffect.onClientExplosion(new Vec3(x, y, z), power, source instanceof PrimedTnt);
    }
}
