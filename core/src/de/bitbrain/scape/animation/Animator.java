package de.bitbrain.scape.animation;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.assets.Assets;

import static de.bitbrain.scape.GameConfig.PLAYER_JUMPING_DURATION;
import static de.bitbrain.scape.GameConfig.PLAYER_LANDING_DURATION;

public class Animator {

   public static void animatePowercell(GameContext context, GameObject o) {
      context.getParticleManager().attachEffect(Assets.Particles.BYTE, o, 8f, 8f);
      context.getBehaviorManager().apply(new PointLightBehavior(Colors.PRIMARY_RED, 140f, context.getLightingManager()), o);
      Tween.to(o, GameObjectTween.SCALE, 0.5f)
            .target(1.3f)
            .ease(TweenEquations.easeInQuad)
            .repeatYoyo(Tween.INFINITY, 0)
            .start(context.getTweenManager());
      o.getColor().a = 0.8f;
      Tween.to(o, GameObjectTween.ALPHA, 0.5f)
            .target(1f)
            .ease(TweenEquations.easeInOutQuad)
            .repeatYoyo(Tween.INFINITY, 0)
            .start(context.getTweenManager());
   }

   public static void animateByte(GameContext context, GameObject o) {
      context.getParticleManager().attachEffect(Assets.Particles.BYTE, o, 4f, 4f);
      context.getBehaviorManager().apply(new PointLightBehavior(Colors.PRIMARY_RED, 16f, context.getLightingManager()), o);
      float delay = (float) Math.random() * 2f;
      Tween.to(o, GameObjectTween.SCALE, 0.5f)
            .delay(delay)
            .target(1.3f)
            .ease(TweenEquations.easeInQuad)
            .repeatYoyo(Tween.INFINITY, 0)
            .start(context.getTweenManager());
      o.getColor().a = 0.8f;
      Tween.to(o, GameObjectTween.ALPHA, 0.5f)
            .delay(delay)
            .target(1f)
            .ease(TweenEquations.easeInOutQuad)
            .repeatYoyo(Tween.INFINITY, 0)
            .start(context.getTweenManager());
   }



   public static void animateJumping(final GameObject o) {
      SharedTweenManager.getInstance().killTarget(o, GameObjectTween.SCALE_X);
      SharedTweenManager.getInstance().killTarget(o, GameObjectTween.SCALE_Y);

      float sign = o.getScaleY() < 0 ? -1 : 1;

      // Initial impact
      Tween.to(o, GameObjectTween.SCALE_X, PLAYER_JUMPING_DURATION / 3)
            .target(1.1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(o, GameObjectTween.SCALE_Y, PLAYER_JUMPING_DURATION / 3)
            .target(0.9f * sign)
            .start(SharedTweenManager.getInstance());

      // Bouncing up
      Tween.to(o, GameObjectTween.SCALE_X, PLAYER_JUMPING_DURATION / 3)
            .delay(PLAYER_JUMPING_DURATION / 3)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(o, GameObjectTween.SCALE_Y, PLAYER_JUMPING_DURATION / 3)
            .delay(PLAYER_JUMPING_DURATION / 3)
            .target(1.3f * sign)
            .start(SharedTweenManager.getInstance());

      // Bouncing up
      Tween.to(o, GameObjectTween.SCALE_X, PLAYER_JUMPING_DURATION / 3)
            .delay(PLAYER_JUMPING_DURATION / 3 * 2)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(o, GameObjectTween.SCALE_Y, PLAYER_JUMPING_DURATION / 3)
            .delay(PLAYER_JUMPING_DURATION / 3 * 2)
            .target(1f * sign)
            .start(SharedTweenManager.getInstance());
   }

   public static void animateLanding(GameObject o) {

      SharedTweenManager.getInstance().killTarget(o, GameObjectTween.SCALE_X);
      SharedTweenManager.getInstance().killTarget(o, GameObjectTween.SCALE_Y);

      float sign = o.getScaleY() < 0 ? -1 : 1;

      // Initial impact
      Tween.to(o, GameObjectTween.SCALE_X, PLAYER_LANDING_DURATION / 3)
            .target(1.3f)
            .start(SharedTweenManager.getInstance());
      Tween.to(o, GameObjectTween.SCALE_Y, PLAYER_LANDING_DURATION / 3)
            .target(0.6f * sign)
            .start(SharedTweenManager.getInstance());

      // Bouncing up
      Tween.to(o, GameObjectTween.SCALE_X, PLAYER_LANDING_DURATION / 3)
            .delay(PLAYER_LANDING_DURATION / 3)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(o, GameObjectTween.SCALE_Y, PLAYER_LANDING_DURATION / 3)
            .delay(PLAYER_LANDING_DURATION / 3)
            .target(1.1f * sign)
            .start(SharedTweenManager.getInstance());

      // Bouncing up
      Tween.to(o, GameObjectTween.SCALE_X, PLAYER_LANDING_DURATION / 3)
            .delay(PLAYER_LANDING_DURATION / 3 * 2)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(o, GameObjectTween.SCALE_Y, PLAYER_LANDING_DURATION / 3)
            .delay(PLAYER_LANDING_DURATION / 3 * 2)
            .target(1f * sign)
            .start(SharedTweenManager.getInstance());
   }
}
