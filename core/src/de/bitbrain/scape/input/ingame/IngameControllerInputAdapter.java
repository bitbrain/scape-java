package de.bitbrain.scape.input.ingame;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.mappings.Xbox;
import de.bitbrain.scape.movement.PlayerControls;
import de.bitbrain.scape.screens.IngameScreen;

public class IngameControllerInputAdapter extends ControllerAdapter {

   private final PlayerControls controls;
   private final IngameScreen ingameScreen;

   public IngameControllerInputAdapter(PlayerControls controls, IngameScreen ingameScreen) {
      this.controls = controls;
      this.ingameScreen = ingameScreen;
   }

   @Override
   public boolean buttonDown(Controller controller, int buttonIndex) {
      if (buttonIndex == getEscapeButton(controller)) {
         ingameScreen.exitIngame();
         return true;
      }
      if (buttonIndex == getRespawnButton(controller)) {
         controls.respawn();
         return true;
      }

      if (!ingameScreen.isStarted()) {
         ingameScreen.startLevel();
         controls.unfreezePlayer();
         return true;
      }

      if (buttonIndex == getJumpButton(controller)) {
         controls.jump();
         return true;
      }
      return false;
   }

   private int getEscapeButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.B;
      }
      return -1;
   }

   private int getRespawnButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.START;
      }
      return -1;
   }

   private int getJumpButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.A;
      }
      return -1;
   }
}
