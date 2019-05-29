package de.bitbrain.scape.animation;

import de.bitbrain.braingdx.util.Enabler;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.movement.PlayerMovement;

public class PlayerAnimationEnabler implements Enabler<GameObject> {

   private final PlayerMovement movement;

   public PlayerAnimationEnabler(PlayerMovement movement) {
      this.movement = movement;
   }
   @Override
   public boolean isEnabledFor(GameObject target) {
      return !movement.isLanding() && !movement.isJumping();
   }
}
