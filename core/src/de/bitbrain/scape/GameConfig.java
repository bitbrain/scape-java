package de.bitbrain.scape;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Zoomer;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.ZoomerShaderTween;
import de.bitbrain.braingdx.util.Mutator;

public interface GameConfig {

   int SWIPE_TOLERANCE = 2;

   float PLAYER_START_SPEED = 50f;
   float LEVEL_START_SCROLLING_SPEED = 0f;
   float BOOT_SEQUENCE_DURATION = 0.6f;

   String PLAYER_PREFERENCES_PATH = "scape.player.config";
   String PLAYER_LEVEL_RECORD = "record";
   String PLAYER_CURRENT_LEVEL = "current_level";
   String PLAYER_MAXIMUM_LEVEL = "maximum_level";

   float ZOOMER_DEFAULT_ZOOM = 2f;
   float ZOOMER_DEFAULT_BLUR_STRENGTH = 3f;

   Mutator<Bloom> DEFAULT_BLOOM_CONFIG = new Mutator<Bloom>() {
      @Override
      public void mutate(Bloom target) {
         target.setBlurAmount(5f);
         target.setBloomIntesity(1.2f);
         target.setBlurPasses(50);
         target.setThreshold(0.3f);
      }
   };

   Mutator<Zoomer> DEFAULT_ZOOMER_CONFIG = new Mutator<Zoomer>() {
      @Override
      public void mutate(Zoomer target) {
         target.setZoom(ZOOMER_DEFAULT_ZOOM);
         target.setBlurStrength(ZOOMER_DEFAULT_BLUR_STRENGTH);
         Tween.to(target, ZoomerShaderTween.ZOOM_AMOUNT, 2f)
               .target(1f)
               .ease(TweenEquations.easeOutExpo)
               .start(SharedTweenManager.getInstance());
         Tween.to(target, ZoomerShaderTween.BLUR_STRENGTH, 2f)
               .target(0f)
               .ease(TweenEquations.easeOutExpo)
               .start(SharedTweenManager.getInstance());
      }
   };

   Mutator<Zoomer> EXIT_ZOOMER_CONFIG = new Mutator<Zoomer>() {
      @Override
      public void mutate(Zoomer target) {
         Tween.to(target, ZoomerShaderTween.ZOOM_AMOUNT, 1f)
               .target(ZOOMER_DEFAULT_ZOOM)
               .ease(TweenEquations.easeOutExpo)
               .start(SharedTweenManager.getInstance());
         Tween.to(target, ZoomerShaderTween.BLUR_STRENGTH, 1f)
               .target(1f)
               .ease(TweenEquations.easeOutExpo)
               .start(SharedTweenManager.getInstance());
      }
   };
}
