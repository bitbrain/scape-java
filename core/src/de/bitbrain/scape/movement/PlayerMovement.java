package de.bitbrain.scape.movement;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.model.Direction;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class PlayerMovement extends BehaviorAdapter implements Movement<Integer> {

   private Vector2 velocity = new Vector2(GameConfig.PLAYER_START_SPEED, 0f);

   private boolean flipping = false;

   private boolean upAgain = false;

   private final CollisionDetector collisionDetector;
   private Vector2 horizontalCollision, verticalCollision;

   public PlayerMovement(CollisionDetector collisionDetector) {
      this.collisionDetector = collisionDetector;
   }

   public boolean isFlipping() {
      return flipping;
   }

   @Override
   public void update(GameObject source, float delta) {
      if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched()) {
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
      horizontalCollision = collisionDetector.getCollisionInFront(source);
      verticalCollision = Direction.UP.equals(direction) ?
            collisionDetector.getCollisionAbove(source) :
            collisionDetector.getCollisionBelow(source);

      if (hasVerticalCollision()) {
         flipping = false;
         source.setPosition(source.getLeft(), verticalCollision.y);
      }
      if (hasHorizontalCollision()) {
         source.setPosition(horizontalCollision.x, source.getTop());
      } else if (!hasVerticalCollision()) {
         flipping = true;
      }
   }

   private void flip(GameObject source) {
      if (flipping) {
         return;
      }
      if (Direction.DOWN.equals(source.getAttribute(Direction.class))) {
         source.setAttribute(Direction.class, Direction.UP);
         source.setScaleY(min(-source.getScaleY(), source.getScaleY()));
      } else {
         source.setAttribute(Direction.class, Direction.DOWN);
         source.setScaleY(max(-source.getScaleY(), source.getScaleY()));
      }
      flipping = true;
      animate(source);
   }

   @Override
   public void move(Integer influencer) {

   }

   @Override
   public boolean isMoving() {
      return !flipping;
   }

   public boolean hasVerticalCollision() {
      return verticalCollision != null;
   }

   public boolean hasHorizontalCollision() {
      return horizontalCollision != null;
   }

   private void animate(GameObject source) {
      SharedTweenManager.getInstance().killTarget(source);
      float targetScale = 1.3f;
      float time = 0.1f;
      Tween.to(source, GameObjectTween.SCALE_X, time)
            .target(targetScale)
            .repeatYoyo(1, 0f)
            .start(SharedTweenManager.getInstance());
      Tween.to(source, GameObjectTween.SCALE_Y, time)
            .target(source.getScaleY() < 0 ? -targetScale : targetScale)
            .repeatYoyo(1, 0f)
            .start(SharedTweenManager.getInstance());
   }
}
