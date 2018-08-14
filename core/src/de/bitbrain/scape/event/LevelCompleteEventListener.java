package de.bitbrain.scape.event;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.scape.IngameScreen;

public class LevelCompleteEventListener implements GameEventListener<LevelCompleteEvent> {

   private final BrainGdxGame game;
   private final IngameScreen screen;
   private final GameContext context;

   public LevelCompleteEventListener(BrainGdxGame game, IngameScreen screen, GameContext context) {
      this.game = game;
      this.screen = screen;
      this.context = context;
   }
   @Override
   public void onEvent(LevelCompleteEvent event) {
      context.getScreenTransitions().out(new IngameScreen(game, event.getNextTiledMap()), 0.5f);
   }
}
