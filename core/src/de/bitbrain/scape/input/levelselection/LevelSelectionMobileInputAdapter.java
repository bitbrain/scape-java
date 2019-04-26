package de.bitbrain.scape.input.levelselection;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.scape.level.LevelManager;
import de.bitbrain.scape.screens.LevelSelectionScreen;

public class LevelSelectionMobileInputAdapter extends GestureDetector.GestureAdapter {

   private final LevelManager levelManager;
   private final LevelSelectionScreen screen;
   private final Vector2 tmpVector = new Vector2();

   public LevelSelectionMobileInputAdapter(LevelManager levelManager, LevelSelectionScreen screen) {
      this.levelManager = levelManager;
      this.screen = screen;
   }

   @Override
   public boolean longPress(float x, float y) {
      if (screen.isExiting()) {
         return false;
      }
      screen.enterLevel();
      screen.exit();
      return true;
   }

   @Override
   public boolean fling(float velocityX, float velocityY, int button) {
      if (screen.isExiting()) {
         return false;
      }
      if (screen.shouldAutoEnterLevel()) {
         return false;
      }
      Orientation orientation = getOrientation(velocityX, velocityY);
      if (orientation == null) {
         return false;
      }
      switch (orientation) {
         case RIGHT: case UP:
            levelManager.selectPreviousLevel();
            break;
         case LEFT: case DOWN:
            levelManager.selectNextLevel();
      }
      return true;
   }

   private Orientation getOrientation(float velocityX, float velocityY) {
      tmpVector.set(velocityX, velocityY);
      float angle = tmpVector.angle();
      if (angle <= 45f || angle >= 335) {
         return Orientation.LEFT;
      }
      if (angle >= 45f && angle <= 135) {
         return Orientation.UP;
      }
      if (angle >= 135f && angle <= 225) {
         return Orientation.RIGHT;
      }
      if (angle >= 225 && angle <= 335) {
         return Orientation.DOWN;
      }
      return null;
   }
}
