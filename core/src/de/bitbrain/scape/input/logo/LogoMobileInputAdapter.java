package de.bitbrain.scape.input.logo;

import com.badlogic.gdx.input.GestureDetector;
import de.bitbrain.scape.screens.LogoScreen;

public class LogoMobileInputAdapter extends GestureDetector.GestureAdapter {

   private final LogoScreen screen;

   public LogoMobileInputAdapter(LogoScreen screen) {
      this.screen = screen;
   }

   @Override
   public boolean touchDown(float x, float y, int pointer, int button) {
      screen.exit();
      return super.touchDown(x, y, pointer, button);
   }
}
