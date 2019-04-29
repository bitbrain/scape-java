package de.bitbrain.scape.input.stagecomplete;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import de.bitbrain.scape.screens.LogoScreen;
import de.bitbrain.scape.screens.StageCompleteScreen;

public class StageCompleteControllerInputAdapter extends ControllerAdapter {

   private final StageCompleteScreen screen;

   public StageCompleteControllerInputAdapter(StageCompleteScreen screen) {
      this.screen = screen;
   }

   @Override
   public boolean buttonDown(Controller controller, int buttonIndex) {
      screen.exit();
      return true;
   }
}
