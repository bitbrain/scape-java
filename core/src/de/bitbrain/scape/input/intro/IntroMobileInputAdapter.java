package de.bitbrain.scape.input.intro;

import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.scape.input.TouchInputManager;
import de.bitbrain.scape.screens.IntroScreen;

public class IntroMobileInputAdapter implements TouchInputManager.TouchInputListener {

   private final IntroScreen screen;

   public IntroMobileInputAdapter(IntroScreen screen) {
      this.screen = screen;
   }

   @Override
   public void onSwipe(Orientation orientation) {
      screen.proceed();
   }

   @Override
   public void onTouch() {
      screen.proceed();
   }

   @Override
   public void onType(int key) {

   }
}
