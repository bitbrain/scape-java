package de.bitbrain.scape.input.intro;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.mappings.Xbox;
import de.bitbrain.scape.screens.IntroScreen;

public class IntroControllerInputAdapter extends ControllerAdapter {

   private final IntroScreen screen;

   public IntroControllerInputAdapter(IntroScreen screen) {
      this.screen = screen;
   }

   @Override
   public boolean buttonDown(Controller controller, int buttonIndex) {
      if (buttonIndex == getSkipButton(controller)) {
         screen.exit();
      }
      screen.proceed();
      return true;
   }

   private int getSkipButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.B;
      }
      return -1;
   }
}
