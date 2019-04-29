package de.bitbrain.scape.input.stagecomplete;

import com.badlogic.gdx.input.GestureDetector;
import de.bitbrain.scape.screens.LogoScreen;
import de.bitbrain.scape.screens.StageCompleteScreen;

public class StageCompleteMobileInputAdapter extends GestureDetector.GestureAdapter {

   private final StageCompleteScreen screen;

   public StageCompleteMobileInputAdapter(StageCompleteScreen screen) {
      this.screen = screen;
   }

   @Override
   public boolean touchDown(float x, float y, int pointer, int button) {
      screen.exit();
      return super.touchDown(x, y, pointer, button);
   }
}
