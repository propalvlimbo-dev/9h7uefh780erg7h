package sg.modules.impl.init;

import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

public record CrystalAttackCandidate(EndCrystal crystal, float enemyDamage, float selfDamage) {
}
