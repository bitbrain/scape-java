package de.bitbrain.scape.animation;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.tweens.GameObjectTween;
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
}
