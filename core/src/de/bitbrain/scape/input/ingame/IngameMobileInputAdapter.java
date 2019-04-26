package de.bitbrain.scape.input.ingame;

import com.badlogic.gdx.input.GestureDetector;
import de.bitbrain.scape.movement.PlayerControls;
import de.bitbrain.scape.screens.IngameScreen;

public class IngameMobileInputAdapter extends GestureDetector.GestureAdapter {

   private final PlayerControls controls;

   private final IngameScreen ingameScreen;

   public IngameMobileInputAdapter(PlayerControls controls, IngameScreen ingameScreen) {
      this.controls = controls;
      this.ingameScreen = ingameScreen;
   }

   @Override
   public boolean touchDown(float x, float y, int pointer, int button) {
      if (!ingameScreen.isStarted()) {
         ingameScreen.startLevel();
         controls.unfreezePlayer();
         return true;
      }
      controls.jump();
      return false;
   }
}
