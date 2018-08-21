package de.bitbrain.scape.event;

import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.scape.screens.IngameScreen;

public class GameOverEventListener implements GameEventListener<GameOverEvent> {

   private final IngameScreen ingameScreen;

   public GameOverEventListener(IngameScreen screen) {
      this.ingameScreen = screen;
   }

   @Override
   public void onEvent(GameOverEvent event) {
      ingameScreen.resetLevel();
   }
}
