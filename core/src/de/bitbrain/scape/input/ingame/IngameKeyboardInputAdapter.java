package de.bitbrain.scape.input.ingame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import de.bitbrain.scape.movement.PlayerControls;
import de.bitbrain.scape.screens.IngameScreen;

public class IngameKeyboardInputAdapter extends InputAdapter {

   private final PlayerControls controls;
   private final IngameScreen ingameScreen;

   public IngameKeyboardInputAdapter(PlayerControls controls, IngameScreen ingameScreen) {
      this.controls = controls;
      this.ingameScreen = ingameScreen;
   }

   @Override
   public boolean keyDown(int keycode) {
      if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
         ingameScreen.exitIngame();
         return true;
      }

      if (!ingameScreen.isStarted()) {
         ingameScreen.startLevel();
         controls.unfreezePlayer();
         return true;
      }

      if (keycode == Input.Keys.BACKSPACE) {
         controls.respawn();
         return true;
      }

      controls.jump();
      return true;
   }
}
