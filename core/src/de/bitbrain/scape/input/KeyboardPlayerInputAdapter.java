package de.bitbrain.scape.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import de.bitbrain.scape.movement.PlayerControls;

public class KeyboardPlayerInputAdapter extends InputAdapter {

   private final PlayerControls controls;

   public KeyboardPlayerInputAdapter(PlayerControls controls) {
      this.controls = controls;
   }

   @Override
   public boolean keyDown(int keycode) {
      if (keycode == Input.Keys.ESCAPE) {
         return false;
      }
      if (keycode == Input.Keys.BACKSPACE) {
         controls.respawn();
         return true;
      }
      controls.jump();
      return true;
   }
}
