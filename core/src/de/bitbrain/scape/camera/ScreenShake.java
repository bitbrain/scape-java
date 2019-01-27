package de.bitbrain.scape.camera;


import aurelienribon.tweenengine.*;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.VectorTween;

import java.util.Random;

public class ScreenShake {

   // Interval in miliseconds between each movement
   public static final float STEP_INTERVAL = 0.05f;

   private static Vector2 shake = new Vector2();

   // our tween manager provided by Universal Tween Engine
   private static TweenManager tweenManager = SharedTweenManager.getInstance();

   // We use a random to select an angle at random
   private static Random random = new Random();

   static {
      // it is important to tell Universal Tween Engine how
      // to translate the camera movement
      Tween.registerAccessor(Vector2.class, new VectorTween());
   }

   public static Vector2 getShake() {
      return shake;
   }

   // strength is the maximum radius
   // duration is the time in miliseconds
   public static void shake(float strength, final float duration) {
      // Calculate the number of steps to take until radius is 0
      final int STEPS = Math.round(duration / STEP_INTERVAL);
      // Radius reduction on each iteration
      final float STRENGTH_STEP = strength / STEPS;
      // Do not forget to kill previous animations!
      tweenManager.killTarget(shake);
      for (int step = 0; step < STEPS; ++step) {
         // Step 1: Let's find a random angle
         double angle = Math.toRadians(random.nextFloat() * 360f);
         float x = (float) Math.floor(strength * Math.cos(angle));
         float y = (float) Math.floor(strength * Math.sin(angle));

         final int finalStep = step;

         // Step 2: ease to the calculated point. Do not forget to set
         // delay!
         Tween.to(shake, VectorTween.POS_X, STEP_INTERVAL).delay(step * STEP_INTERVAL).target(x)
               .setCallback(new TweenCallback() {
                  @Override
                  public void onEvent(int i, BaseTween<?> baseTween) {
                     if (finalStep == STEPS - 1) {
                        shake.set(0f, 0f);
                     }
                  }
               })
               .setCallbackTriggers(TweenCallback.COMPLETE)
               .ease(TweenEquations.easeInOutCubic).start(tweenManager);
         Tween.to(shake, VectorTween.POS_Y, STEP_INTERVAL).delay(step * STEP_INTERVAL).target(y)
               .ease(TweenEquations.easeInOutCubic).start(tweenManager);

         // Step 3: reduce the radius of the screen shake circle
         strength -= STRENGTH_STEP;
      }
   }
}