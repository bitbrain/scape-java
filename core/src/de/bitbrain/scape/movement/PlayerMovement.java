package de.bitbrain.scape.movement;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.animation.Animator;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.model.Direction;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class PlayerMovement extends BehaviorAdapter implements Movement<Integer> {

   private static final long INPUT_LAG_BUFFER_IN_MS = 220;

   private Vector2 velocity = new Vector2(GameConfig.PLAYER_START_SPEED, 0f);

   private float increaseRatio = 1f;

   private boolean flipping = false;

   private boolean jumpRequested = false;

   private boolean enabled = false;

   private boolean landing = false;

   private long timestamp = 0;

   private final CollisionDetector collisionDetector;
   private Vector2 horizontalCollision, verticalCollision;
   private boolean lastHorizontalCollision;

   public PlayerMovement(CollisionDetector collisionDetector) {
      this.collisionDetector = collisionDetector;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public void increaseSpeed(float percentage) {
      this.increaseRatio += percentage;
   }

   public void jumpIfUpAgain() {
      jumpRequested = true;
      timestamp = System.currentTimeMillis();
   }

   @Override
   public void update(final GameObject source, float delta) {
      if (!enabled) {
         return;
      }

      Direction direction = ((Direction) source.getAttribute(Direction.class));

      velocity.x = GameConfig.PLAYER_START_SPEED * delta;
      velocity.y = GameConfig.PLAYER_START_SPEED * direction.getMultiplier() * delta;

      velocity.scl(increaseRatio);

      source.setPosition(source.getLeft() + velocity.x, source.getTop() + velocity.y);
      horizontalCollision = collisionDetector.getCollisionInFront(source);
      verticalCollision = Direction.UP.equals(direction) ?
            collisionDetector.getCollisionAbove(source) :
            collisionDetector.getCollisionBelow(source);

      boolean hadVerticalCollision = false;
      boolean lastFlipping = flipping;
      if (hasVerticalCollision()) {
         flipping = false;
         hadVerticalCollision = true;
         source.setPosition(source.getLeft(), verticalCollision.y);
      }
      if (hasHorizontalCollision()) {
         if (!lastHorizontalCollision) {
            SharedAssetManager.getInstance().get(Assets.Sounds.STEP, Sound.class)
                  .play(0.1f, (float) (0.6f + Math.random() * 0.3f), 0f);
         }
         lastHorizontalCollision = true;
               source.setPosition(horizontalCollision.x, source.getTop());
      } else{
         lastHorizontalCollision = false;
         if (!hadVerticalCollision && !flipping) {
            Animator.animateJumping(source);
            flipping = true;
         }
      }

      if (jumpRequested) {
         // We want to apply additional input lag so we can insert commands while being in flipping state
         if (flipping && (System.currentTimeMillis() - timestamp) > INPUT_LAG_BUFFER_IN_MS) {
            jumpRequested = false;
         }
         flip(source);
      }

      if (!flipping && lastFlipping) {
         landing = true;
         Animator.animateLanding(source);
         SharedAssetManager.getInstance().get(Assets.Sounds.STEP, Sound.class)
               .play(0.1f, (float) (0.6f + Math.random() * 0.3f), 0f);
         Tween.call(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
               landing = false;
            }
         })
         .delay(GameConfig.PLAYER_LANDING_DURATION)
         .start(SharedTweenManager.getInstance());
      }
   }

   public boolean isLanding() {
      return landing;
   }

   public boolean hasHorizontalCollisionInFront() {
      return hasHorizontalCollision();
   }

   private void flip(GameObject source) {
      if (flipping || (hasHorizontalCollisionInFront() && !hasVerticalCollision())) {
         return;
      }
      SharedAssetManager.getInstance().get(Assets.Sounds.JUMP, Sound.class)
            .play(0.3f, (float) (1f + Math.random() * 0.3f), 0f);
      if (Direction.DOWN.equals(source.getAttribute(Direction.class))) {
         source.setAttribute(Direction.class, Direction.UP);
         source.setScaleY(min(-source.getScaleY(), source.getScaleY()));
         source.setOrigin(source.getWidth() / 2f, 0f);
         source.setOffset(0f, source.getHeight());
      } else {
         source.setAttribute(Direction.class, Direction.DOWN);
         source.setScaleY(max(-source.getScaleY(), source.getScaleY()));
         source.setOrigin(source.getWidth() / 2f, 0f);
         source.setOffset(0f, 0f);
      }
      flipping = true;
      Animator.animateJumping(source);
      jumpRequested = false;
   }

   @Override
   public void move(Integer influencer) {

   }

   public boolean isJumping() {
      return flipping && !hasHorizontalCollisionInFront();
   }

   @Override
   public boolean isMoving() {
      return !flipping;
   }

   public void reset() {
      flipping = false;
      horizontalCollision = null;
      verticalCollision = null;
      jumpRequested = false;
      enabled = true;
      increaseRatio = 1f;
   }

   public boolean hasVerticalCollision() {
      return verticalCollision != null;
   }

   public boolean hasHorizontalCollision() {
      return horizontalCollision != null;
   }
}
