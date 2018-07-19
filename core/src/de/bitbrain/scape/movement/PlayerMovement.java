package de.bitbrain.scape.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.model.Direction;

public class PlayerMovement extends BehaviorAdapter {

   private Vector2 velocity = new Vector2(GameConfig.PLAYER_START_SPEED, 0f);

   private Direction direction;

   private boolean flipping = false;

   private final CollisionDetector collisionDetector;

   public PlayerMovement(CollisionDetector collisionDetector) {
      this.collisionDetector = collisionDetector;
   }

   @Override
   public void update(GameObject source, float delta) {
      if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
         flip(source);
      }

      if (flipping) {
         velocity.y = GameConfig.PLAYER_START_SPEED * this.direction.getMultiplier();
      } else {
         velocity.y = 0;
      }

      // 1. When moving up, get collision on the top
      //    When moving down, get collision on the bottom
      //    Get collision on the right
      // 2. when distance to horizontal collision > distance to vertical collision -> horizontal collision
      // 3. for horizontal collision -> set back right side to horizontal left point
      //    for vertical collision -> - if moving up, set up side to vertical down point
      //                              - if moving down, set down side to vertical up point
      //                              - set flipping to false
      source.setPosition(
            source.getLeft() + velocity.x * delta,
            source.getTop() + velocity.y * delta);

      if (flipping) {
         Vector2 horizontalCollision = collisionDetector.getCollisionInFront(source);
         Vector2 verticalCollision = Direction.UP.equals(this.direction) ?
               collisionDetector.getCollisionAbove(source) :
               collisionDetector.getCollisionBelow(source);
         if (verticalCollision != null) {
            flipping = false;
            source.setAttribute(Direction.class, this.direction);
            velocity.y = 0;
            source.setPosition(verticalCollision.x, verticalCollision.y);
         } else
         if (horizontalCollision != null) {
            source.setPosition(horizontalCollision.x, horizontalCollision.y);
         }
      }
   }

   private void flip(GameObject source) {
      if (flipping) {
         return;
      }
      if (Direction.DOWN.equals(source.getAttribute(Direction.class))) {
         this.direction = Direction.UP;
      } else {
         this.direction = Direction.DOWN;
      }
      flipping = true;

      System.out.println("Flipping " + this.direction);
   }
}
