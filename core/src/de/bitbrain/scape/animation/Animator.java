package de.bitbrain.scape.animation;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.assets.Assets;

public class Animator {

   public static void animateByte(GameContext context, GameObject o) {
      context.getParticleManager().attachEffect(Assets.Particles.BYTE, o, 4f, 4f);
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

   public static void animatePlayerBounce(final GameObject o) {
      SharedTweenManager.getInstance().killTarget(o, GameObjectTween.SCALE_Y);
      Tween.to(o, GameObjectTween.SCALE_Y, 0.08f)
            .target(o.getScaleY() > 0f ? 0.6f : -0.6f)
            .ease(TweenEquations.easeOutCubic)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> t) {
                  Tween.to(o, GameObjectTween.SCALE_Y, 0.1f)
                        .target(o.getScaleY() > 0f ? 1f : -1f)
                        .ease(TweenEquations.easeInCubic)
                        .start(SharedTweenManager.getInstance());
               }
            })
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .start(SharedTweenManager.getInstance());
   }
}
