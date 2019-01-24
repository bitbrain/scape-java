package de.bitbrain.scape.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import de.bitbrain.scape.movement.PlayerControls;
import de.bitbrain.scape.screens.IngameScreen;

public class IngameKeyboardInputAdapter extends InputAdapter {

   private final PlayerControls controls;
   private final IngameScreen ingameScreen;

   private boolean started = false;

   public IngameKeyboardInputAdapter(PlayerControls controls, IngameScreen ingameScreen) {
      this.controls = controls;
      this.ingameScreen = ingameScreen;
   }

   @Override
   public boolean keyDown(int keycode) {
      if (keycode == Input.Keys.ESCAPE) {
         ingameScreen.exitIngame();
         return true;
      }
      if (keycode == Input.Keys.BACKSPACE) {
         controls.respawn();
         return true;
      }

      if (!started) {
         started = true;
         ingameScreen.startLevel();
         controls.unfreezePlayer();
      }

      controls.jump();
      return true;
   }
}
