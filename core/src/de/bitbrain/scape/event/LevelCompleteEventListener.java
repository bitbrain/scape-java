package de.bitbrain.scape.event;

import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.scape.LevelMetaData;
import de.bitbrain.scape.screens.LevelSelectionScreen;

public class LevelCompleteEventListener implements GameEventListener<LevelCompleteEvent> {

   private final BrainGdxGame game;
   private final GameContext context;
   private final LevelMetaData metadata;

   public LevelCompleteEventListener(BrainGdxGame game, GameContext context, LevelMetaData metadata) {
      this.game = game;
      this.context = context;
      this.metadata = metadata;
   }

   @Override
   public void onEvent(final LevelCompleteEvent event) {
      metadata.getProgress().increaseMaxLevel();
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            context.getScreenTransitions().out(new LevelSelectionScreen(game), 0.5f);
         }
      });

   }
}
