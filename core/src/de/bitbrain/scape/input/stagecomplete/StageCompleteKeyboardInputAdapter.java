package de.bitbrain.scape.input.stagecomplete;

import com.badlogic.gdx.InputAdapter;
import de.bitbrain.scape.screens.LogoScreen;
import de.bitbrain.scape.screens.StageCompleteScreen;

public class StageCompleteKeyboardInputAdapter extends InputAdapter {

   private final StageCompleteScreen screen;

   public StageCompleteKeyboardInputAdapter(StageCompleteScreen screen) {
      this.screen = screen;
   }

   @Override
   public boolean keyDown(int keycode) {
      screen.exit();
      return true;
   }
}
