package de.bitbrain.scape.input;

import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.scape.movement.PlayerControls;
import de.bitbrain.scape.screens.IngameScreen;

public class IngameMobileInputAdapter extends TouchInputManager implements TouchInputManager.TouchInputListener {

   private final PlayerControls controls;

   private boolean started = false;

   private final IngameScreen ingameScreen;

   public IngameMobileInputAdapter(PlayerControls controls, IngameScreen ingameScreen) {
      this.controls = controls;
      this.ingameScreen = ingameScreen;
      addListener(this);
   }

   @Override
   public boolean touchDown(int screenX, int screenY, int pointer, int button) {
      if (!started) {
         started = true;
         ingameScreen.startLevel();
         controls.unfreezePlayer();
      }
      controls.jump();
      return false;
   }

   @Override
   public void onSwipe(Orientation orientation) {
      if (orientation.getXFactor() < 0) {
         controls.respawn();
      } else {
         ingameScreen.exitIngame();
      }

   }

   @Override
   public void onTouch() {

   }

   @Override
   public void onType(int key) {

   }
}
