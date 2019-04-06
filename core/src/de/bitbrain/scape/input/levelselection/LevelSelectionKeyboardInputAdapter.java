package de.bitbrain.scape.input.levelselection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import de.bitbrain.scape.level.LevelManager;
import de.bitbrain.scape.screens.LevelSelectionScreen;

public class LevelSelectionKeyboardInputAdapter extends InputAdapter {

   private final LevelManager levelManager;
   private final LevelSelectionScreen screen;


   public LevelSelectionKeyboardInputAdapter(LevelManager levelManager, LevelSelectionScreen screen) {
      this.levelManager = levelManager;
      this.screen = screen;
   }

   @Override
   public boolean keyDown(int keycode) {
      if (screen.isExiting()) {
         return false;
      }
      if (screen.shouldAutoEnterLevel()) {
         return false;
      }
      switch (keycode) {
         case Input.Keys.W: case Input.Keys.D:
            levelManager.selectNextLevel();
            return true;
         case Input.Keys.S: case Input.Keys.A:
            levelManager.selectPreviousLevel();
            return true;
         case Input.Keys.ESCAPE:
            Gdx.app.exit();
            screen.exit();
            return true;
         case Input.Keys.ENTER:
            screen.enterLevel();
            screen.exit();
            return true;
      }
      return false;
   }
}
