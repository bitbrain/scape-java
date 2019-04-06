package de.bitbrain.scape.input.levelselection;

import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.scape.input.TouchInputManager;
import de.bitbrain.scape.level.LevelManager;
import de.bitbrain.scape.screens.LevelSelectionScreen;

public class LevelSelectionMobileInputAdapter implements TouchInputManager.TouchInputListener {

   private final LevelManager levelManager;
   private final LevelSelectionScreen screen;

   public LevelSelectionMobileInputAdapter(LevelManager levelManager, LevelSelectionScreen screen) {
      this.levelManager = levelManager;
      this.screen = screen;
   }

   @Override
   public void onSwipe(Orientation orientation) {
      if (screen.isExiting()) {
         return;
      }
      if (screen.shouldAutoEnterLevel()) {
         return;
      }
      switch (orientation) {
         case RIGHT: case UP:
            levelManager.selectPreviousLevel();
            break;
         case LEFT: case DOWN:
            levelManager.selectNextLevel();
      }
   }

   @Override
   public void onTouch() {
      if (screen.isExiting()) {
         return;
      }
      screen.enterLevel();
      screen.exit();
   }

   @Override
   public void onType(int key) {

   }
}
