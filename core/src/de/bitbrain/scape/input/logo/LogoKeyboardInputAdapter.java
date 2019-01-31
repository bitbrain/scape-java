package de.bitbrain.scape.input.logo;

import com.badlogic.gdx.InputAdapter;
import de.bitbrain.scape.screens.LogoScreen;

public class LogoKeyboardInputAdapter extends InputAdapter {

   private final LogoScreen screen;

   public LogoKeyboardInputAdapter(LogoScreen screen) {
      this.screen = screen;
   }

   @Override
   public boolean keyDown(int keycode) {
      screen.exit();
      return true;
   }
}
