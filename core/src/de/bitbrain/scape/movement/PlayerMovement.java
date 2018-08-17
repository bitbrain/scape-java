package de.bitbrain.scape.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.model.Direction;

public class PlayerMovement extends BehaviorAdapter implements Movement<Integer> {

   private Vector2 velocity = new Vector2(GameConfig.PLAYER_START_SPEED, 0f);

   private boolean flipping = false;

   private boolean upAgain = false;

   private final CollisionDetector collisionDetector;

   public PlayerMovement(CollisionDetector collisionDetector) {
      this.collisionDetector = collisionDetector;
   }

   public boolean isFlipping() {
      return flipping;
   }

   @Override
   public void update(GameObject source, float delta) {
      if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
         if (upAgain) {
            flip(source);
         }
         upAgain = false;
      } else {
         upAgain = true;
      }

      Direction direction = ((Direction) source.getAttribute(Direction.class));

      velocity.x = GameConfig.PLAYER_START_SPEED * delta;
      velocity.y = GameConfig.PLAYER_START_SPEED * direction.getMultiplier() * delta;

      source.setPosition(source.getLeft() + velocity.x, source.getTop() + velocity.y);
      Vector2 horizontalCollision = collisionDetector.getCollisionInFront(source);
      Vector2 verticalCollision = Direction.UP.equals(direction) ?
            collisionDetector.getCollisionAbove(source) :
            collisionDetector.getCollisionBelow(source);

      if (verticalCollision != null) {
         flipping = false;
         source.setPosition(source.getLeft(), verticalCollision.y);
      }
      if (horizontalCollision != null) {
         source.setPosition(horizontalCollision.x, source.getTop());
      } else if (verticalCollision == null) {
         flipping = true;
      }
   }

   private void flip(GameObject source) {
      if (flipping) {
         return;
      }
      if (Direction.DOWN.equals(source.getAttribute(Direction.class))) {
         source.setAttribute(Direction.class, Direction.UP);
      } else {
         source.setAttribute(Direction.class, Direction.DOWN);
      }
      flipping = true;
   }

   @Override
   public void move(Integer influencer) {

   }

   @Override
   public boolean isMoving() {
      return !flipping;
   }
}
