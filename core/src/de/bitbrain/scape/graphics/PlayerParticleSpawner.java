package de.bitbrain.scape.graphics;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.model.Direction;
import de.bitbrain.scape.movement.PlayerMovement;

public class PlayerParticleSpawner extends BehaviorAdapter {

   private static final int HORIZONTAL_OFFSET = 3;

   private final ParticleManager particleManager;
   private final PlayerMovement playerMovement;

   private boolean touching = false;
   private boolean firstTouch = false;

   public PlayerParticleSpawner(ParticleManager particleManager, PlayerMovement playerMovement) {
      this.particleManager = particleManager;
      this.playerMovement = playerMovement;
   }

   @Override
   public void update(GameObject source, float delta) {
      if (!touching && playerMovement.hasVerticalCollision()) {
         touching = true;
         firstTouch = true;
      }
      if (touching && !playerMovement.hasVerticalCollision()) {
         touching = false;
      }
      if (firstTouch) {
         Direction direction = (Direction)source.getAttribute(Direction.class);
         String effect = direction == Direction.UP ? Assets.Particles.TOUCH_TOP : Assets.Particles.TOUCH_BOTTOM;
         final float x = HORIZONTAL_OFFSET;
         final float y = direction == Direction.UP ? source.getHeight() : 0f;
         particleManager.attachEffect(effect, source, x, y);
      }
      firstTouch = false;
   }
}
