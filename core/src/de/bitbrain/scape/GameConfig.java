package de.bitbrain.scape;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Zoomer;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.ZoomerShaderTween;
import de.bitbrain.braingdx.util.Mutator;

public interface GameConfig {

   float PLAYER_START_SPEED = 45f;
   float LEVEL_START_SCROLLING_SPEED = 25f;
   float BOOT_SEQUENCE_DURATION = 3.6f;
   float PLAYER_SPEED_INCREASE_PER_BYTE_PERCENTAGE = 0.05f;

   float PLAYER_LANDING_DURATION = 0.25f;
   float PLAYER_JUMPING_DURATION = 0.3f;

   String PLAYER_PREFERENCES_PATH = "scape.player.config";
   String PLAYER_LEVEL_POINT_RECORD = "point_record";
   String PLAYER_LEVEL_TIME_RECORD = "time_record";
   String PLAYER_CURRENT_LEVEL = "current_level";
   String PLAYER_MAXIMUM_LEVEL = "maximum_level";
   String PLAYER_TOTAL_PROGRESS = "total_progress";

   float ZOOMER_DEFAULT_ZOOM = 2f;
   float ZOOMER_DEFAULT_BLUR_STRENGTH = 3f;

   Mutator<Bloom> DEFAULT_BLOOM_CONFIG = new Mutator<Bloom>() {
      @Override
      public void mutate(Bloom target) {
         target.setBlurAmount(10f);
         target.setBloomIntesity(1.5f);
         target.setBlurPasses(Gdx.app.getType() == Application.ApplicationType.Desktop ? 10 : 8);
         target.setThreshold(0.3f);
      }
   };

   Mutator<Zoomer> DEFAULT_ZOOMER_CONFIG = new Mutator<Zoomer>() {
      @Override
      public void mutate(Zoomer target) {
         target.setZoom(ZOOMER_DEFAULT_ZOOM);
         target.setBlurStrength(ZOOMER_DEFAULT_BLUR_STRENGTH);
         Tween.to(target, ZoomerShaderTween.ZOOM_AMOUNT, 1f)
               .target(1f)
               .ease(TweenEquations.easeOutExpo)
               .start(SharedTweenManager.getInstance());
         Tween.to(target, ZoomerShaderTween.BLUR_STRENGTH, 1f)
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

   Mutator<Zoomer> EXIT_ZOOMER_CONFIG_INGAME = new Mutator<Zoomer>() {
      @Override
      public void mutate(Zoomer target) {
         Tween.to(target, ZoomerShaderTween.ZOOM_AMOUNT, 2f)
               .target(ZOOMER_DEFAULT_ZOOM)
               .ease(TweenEquations.easeOutExpo)
               .start(SharedTweenManager.getInstance());
         Tween.to(target, ZoomerShaderTween.BLUR_STRENGTH, 2f)
               .target(0.5f)
               .ease(TweenEquations.easeOutExpo)
               .start(SharedTweenManager.getInstance());
      }
   };


   Mutator<Zoomer> INITIAL_ZOOMER_CONFIG = new Mutator<Zoomer>() {
      @Override
      public void mutate(Zoomer target) {
         target.setBlurStrength(ZOOMER_DEFAULT_BLUR_STRENGTH);
         Tween.to(target, ZoomerShaderTween.ZOOM_AMOUNT, 1f)
               .target(1f)
               .ease(TweenEquations.easeOutExpo)
               .start(SharedTweenManager.getInstance());
         Tween.to(target, ZoomerShaderTween.BLUR_STRENGTH, 1f)
               .target(0.0f)
               .ease(TweenEquations.easeOutExpo)
               .start(SharedTweenManager.getInstance());
      }
   };

   float MENU_BUTTON_WIDTH = 300f;
   float MENU_BUTTON_HEIGHT = 80f;
   float MENU_BUTTON_PADDING = 30f;
}
