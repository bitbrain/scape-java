package de.bitbrain.scape.input.logo;

import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.scape.input.TouchInputManager;
import de.bitbrain.scape.movement.PlayerControls;
import de.bitbrain.scape.screens.IngameScreen;
import de.bitbrain.scape.screens.LogoScreen;

public class LogoMobileInputAdapter extends TouchInputManager implements TouchInputManager.TouchInputListener {

   private final LogoScreen screen;

   public LogoMobileInputAdapter(LogoScreen screen) {
      this.screen = screen;
   }

   @Override
   public boolean touchDown(int screenX, int screenY, int pointer, int button) {
      screen.exit();
      return true;
   }

   @Override
   public void onSwipe(Orientation orientation) {
      screen.exit();
   }

   @Override
   public void onTouch() {

   }

   @Override
   public void onType(int key) {

   }
}
