package de.bitbrain.scape.animation;

import de.bitbrain.braingdx.graphics.animation.AnimationTypeResolver;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.movement.PlayerMovement;

public class PlayerAnimationTypeResolver implements AnimationTypeResolver<GameObject> {

   private final PlayerMovement playerMovement;

   public PlayerAnimationTypeResolver(PlayerMovement movement) {
      this.playerMovement = movement;
   }

   @Override
   public Object getAnimationType(GameObject object) {
      if (playerMovement.hasHorizontalCollision() && playerMovement.hasVerticalCollision()) {
         return AnimationTypes.PLAYER_WALL_CORNERED;
      }
      if (playerMovement.hasHorizontalCollisionInFront()) {
         return AnimationTypes.PLAYER_WALL_CLIMBING;
      }
      if (playerMovement.hasVerticalCollision()) {
         return AnimationTypes.PLAYER_DEFAULT;
      }
      return AnimationTypes.PLAYER_JUMPING;
   }
}
