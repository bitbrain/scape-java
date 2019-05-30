package de.bitbrain.scape.animation;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.Enabler;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.movement.PlayerMovement;

public class PlayerAnimationEnabler implements Enabler<GameObject> {

   private final PlayerMovement movement;

   private boolean wasJumping = false;
   private boolean initialJump = false;

   public PlayerAnimationEnabler(PlayerMovement movement) {
      this.movement = movement;
   }
   @Override
   public boolean isEnabledFor(GameObject target) {
      boolean jumping = movement.isJumping();
      if (!wasJumping && jumping) {
         wasJumping = true;
         initialJump = true;
         Tween.call(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
               initialJump = false;
            }
         })
         .delay(GameConfig.PLAYER_JUMPING_DURATION)
         .start(SharedTweenManager.getInstance());
      }
      if (!jumping) {
         wasJumping = false;
      }
      return !movement.isLanding() && !initialJump;
   }
}
