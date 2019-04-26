package de.bitbrain.scape.input.intro;

import com.badlogic.gdx.input.GestureDetector;
import de.bitbrain.scape.screens.IntroScreen;

public class IntroMobileInputAdapter extends GestureDetector.GestureAdapter {

   private final IntroScreen screen;

   public IntroMobileInputAdapter(IntroScreen screen) {
      this.screen = screen;
   }

   @Override
   public boolean longPress(float x, float y) {
      screen.exit();
      return true;
   }

   @Override
   public boolean touchDown(float x, float y, int pointer, int button) {
      screen.proceed();
      return true;
   }
}
