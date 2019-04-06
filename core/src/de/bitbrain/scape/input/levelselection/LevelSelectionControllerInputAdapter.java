package de.bitbrain.scape.input.levelselection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Xbox;
import de.bitbrain.scape.level.LevelManager;
import de.bitbrain.scape.level.LevelMetaData;
import de.bitbrain.scape.movement.PlayerControls;
import de.bitbrain.scape.screens.IngameScreen;
import de.bitbrain.scape.screens.LevelSelectionScreen;

import java.util.logging.Level;

public class LevelSelectionControllerInputAdapter extends ControllerAdapter {

   private final LevelManager levelManager;
   private final LevelSelectionScreen screen;

   private boolean started = false;

   public LevelSelectionControllerInputAdapter(LevelManager levelManager, LevelSelectionScreen screen) {
      this.levelManager = levelManager;
      this.screen = screen;
   }

   @Override
   public boolean buttonDown(Controller controller, int buttonIndex) {
      if (screen.isExiting()) {
         return false;
      }
      if (buttonIndex == getExitButton(controller)) {
         screen.exit();
         Gdx.app.exit();
         return true;
      }
      if (screen.shouldAutoEnterLevel()) {
         return false;
      }
      if (buttonIndex == getPreviousLevelButton(controller)) {
         levelManager.selectPreviousLevel();
         return true;
      }
      if (buttonIndex == getNextLevelButton(controller)) {
         levelManager.selectNextLevel();
         return true;
      }
      if (buttonIndex == getEnterLevelButton(controller)) {
         screen.enterLevel();
         screen.exit();
         return true;
      }
      return false;
   }

   @Override
   public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
      if (value == PovDirection.west || value == PovDirection.south) {
         levelManager.selectPreviousLevel();
         return true;
      }
      if (value == PovDirection.east || value == PovDirection.north) {
         levelManager.selectNextLevel();
         return true;
      }
      return super.povMoved(controller, povIndex, value);
   }

   private int getExitButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.B;
      }
      return -1;
   }

   private int getNextLevelButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.R_BUMPER;
      }
      return -1;
   }

   private int getPreviousLevelButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.L_BUMPER;
      }
      return -1;
   }

   private int getEnterLevelButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.A;
      }
      return -1;
   }
}
