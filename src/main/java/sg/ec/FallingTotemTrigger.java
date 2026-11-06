package sg.ec;

public class FallingTotemTrigger extends TotemTrigger {
   private static final float яЫ = 5.0F;
   private static final String яl = "Падение";

   @Override
   public boolean ац() {
      boolean var1 = MINECRAFT_CLIENT.player.fallDistance >= this.В.е_989fed9a().getValue().floatValue() || MINECRAFT_CLIENT.player.getDeltaMovement().y <= -(this.В.е_989fed9a().getValue() / яЫ);
      return this.В.Ы().isSelected(яl) && var1;
   }
}
