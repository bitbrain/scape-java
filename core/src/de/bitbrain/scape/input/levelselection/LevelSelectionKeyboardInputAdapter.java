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
      switch (keycode) {
         case Input.Keys.W: case Input.Keys.D:
            levelManager.selectNextLevel();
            break;
         case Input.Keys.S: case Input.Keys.A:
            levelManager.selectPreviousLevel();
            break;
         case Input.Keys.ESCAPE:
            Gdx.app.exit();
            screen.exit();
            break;
         case Input.Keys.ENTER:
            screen.enterLevel();
            screen.exit();
            break;
      }
      return false;
   }
}
