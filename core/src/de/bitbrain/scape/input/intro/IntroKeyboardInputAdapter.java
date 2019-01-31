package de.bitbrain.scape.input.intro;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import de.bitbrain.scape.screens.IntroScreen;

public class IntroKeyboardInputAdapter extends InputAdapter {

   private final IntroScreen screen;

   public IntroKeyboardInputAdapter(IntroScreen screen) {
      this.screen = screen;
   }

   @Override
   public boolean keyDown(int keycode) {
      if (keycode == Input.Keys.ESCAPE) {
         screen.exit();
         return true;
      }
      screen.proceed();
      return true;
   }
}
