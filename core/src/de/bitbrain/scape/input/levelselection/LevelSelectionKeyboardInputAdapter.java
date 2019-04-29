package de.bitbrain.scape.input.levelselection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import de.bitbrain.scape.level.StageManager;
import de.bitbrain.scape.screens.StageSelectionScreen;

public class LevelSelectionKeyboardInputAdapter extends InputAdapter {

   private final StageManager stageManager;
   private final StageSelectionScreen screen;


   public LevelSelectionKeyboardInputAdapter(StageManager stageManager, StageSelectionScreen screen) {
      this.stageManager = stageManager;
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
         case Input.Keys.UP: case Input.Keys.RIGHT:
            stageManager.selectNextLevel();
            return true;
         case Input.Keys.S: case Input.Keys.A:
         case Input.Keys.DOWN: case Input.Keys.LEFT:
            stageManager.selectPreviousLevel();
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
