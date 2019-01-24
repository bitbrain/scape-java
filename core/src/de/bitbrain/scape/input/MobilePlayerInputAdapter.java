package de.bitbrain.scape.input;

import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.scape.movement.PlayerControls;

public class MobilePlayerInputAdapter extends TouchInputManager implements TouchInputManager.TouchInputListener {

   private final PlayerControls controls;

   public MobilePlayerInputAdapter(PlayerControls controls) {
      this.controls = controls;
      addListener(this);
   }

   @Override
   public boolean touchDown(int screenX, int screenY, int pointer, int button) {
      controls.jump();
      return false;
   }

   @Override
   public void onSwipe(Orientation orientation) {
      controls.respawn();
   }

   @Override
   public void onTouch() {

   }

   @Override
   public void onType(int key) {

   }
}
