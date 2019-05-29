package de.bitbrain.scape.ui;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.StringRandomizerTween;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.util.StringRandomizer;

public class GlitchLabel extends Label {

   private final DeltaTimer glitchTimer = new DeltaTimer();
   private final GlitchLabelStyle glitchLabelStyle;
   private StringRandomizer randomizer;

   public GlitchLabel(CharSequence text, GlitchLabelStyle style) {
      super(text, style);
      this.glitchLabelStyle = style;
      this.randomizer = new StringRandomizer(text.toString(), glitchLabelStyle.glitchPool);
      glitchIn();
   }

   @Override
   public void setText(CharSequence newText) {
      super.setText(newText);
      this.randomizer = new StringRandomizer(newText.toString(), glitchLabelStyle.glitchPool);
   }

   @Override
   public void act(float delta) {
      glitchTimer.update(delta);
      super.act(delta);
      if (glitchTimer.reached(glitchLabelStyle.glitchInterval)) {
         super.setText(randomizer.randomize());
         glitchTimer.reset();
      }
   }

   public void glitchIn() {
      SharedTweenManager.getInstance().killTarget(randomizer);
      randomizer.setFactor(1f);
      Tween.to(randomizer, StringRandomizerTween.FACTOR, glitchLabelStyle.fadeInDuration)
            .target(0f)
            .ease(glitchLabelStyle.fadeInEquation)
            .start(SharedTweenManager.getInstance());
   }

   public void glitchOut() {
      SharedTweenManager.getInstance().killTarget(randomizer);
      Tween.to(randomizer, StringRandomizerTween.FACTOR, glitchLabelStyle.fadeOutDuration)
            .target(1f)
            .ease(glitchLabelStyle.fadeOutEquation)
            .start(SharedTweenManager.getInstance());
   }

   public static class GlitchLabelStyle extends LabelStyle {
      public float glitchInterval = 0.1f;
      public String glitchPool = "01";
      public float fadeInDuration = 1f;
      public float fadeOutDuration = 1f;
      public TweenEquation fadeInEquation = TweenEquations.easeInCubic;
      public TweenEquation fadeOutEquation = TweenEquations.easeOutCubic;
   }
}
