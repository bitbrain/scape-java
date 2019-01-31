package de.bitbrain.scape.input.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Xbox;
import de.bitbrain.scape.screens.LogoScreen;
import de.bitbrain.scape.screens.MainMenuScreen;
import de.bitbrain.scape.ui.ButtonMenu;

public class MainMenuControllerInputAdapter extends ControllerAdapter {

   private final ButtonMenu menu;

   public MainMenuControllerInputAdapter(ButtonMenu menu) {
      this.menu = menu;
   }

   @Override
   public boolean buttonDown(Controller controller, int buttonIndex) {
      if (buttonIndex == getExitButton(controller)) {
         Gdx.app.exit();
         return true;
      }
      if (buttonIndex == getPreviousButton(controller)) {
         menu.checkPrevious();
         return true;
      }
      if (buttonIndex == getNextButton(controller)) {
         menu.checkNext();
         return true;
      }
      if (buttonIndex == getAcceptButton(controller)) {
         menu.clickChecked();
         return true;
      }
      return false;
   }

   @Override
   public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
      if (value == PovDirection.west || value == PovDirection.south) {
         menu.checkPrevious();
         return true;
      }
      if (value == PovDirection.east || value == PovDirection.north) {
         menu.checkNext();
         return true;
      }
      return super.povMoved(controller, povIndex, value);
   }

   private int getPreviousButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.L_BUMPER;
      }
      return -1;
   }

   private int getNextButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.R_BUMPER;
      }
      return -1;
   }

   private int getAcceptButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.A;
      }
      return -1;
   }

   private int getExitButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.B;
      }
      return -1;
   }

}
