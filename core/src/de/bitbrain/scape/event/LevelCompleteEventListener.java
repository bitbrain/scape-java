package de.bitbrain.scape.event;

import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.scape.screens.IngameScreen;
import de.bitbrain.scape.screens.LevelSelectionScreen;

public class LevelCompleteEventListener implements GameEventListener<LevelCompleteEvent> {

   private final BrainGdxGame game;
   private final GameContext context;

   public LevelCompleteEventListener(BrainGdxGame game, GameContext context) {
      this.game = game;
      this.context = context;
   }

   @Override
   public void onEvent(final LevelCompleteEvent event) {
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            context.getScreenTransitions().out(new LevelSelectionScreen(game), 0.5f);
         }
      });

   }
}
