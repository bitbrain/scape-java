package de.bitbrain.scape.event;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Zoomer;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.LevelMetaData;
import de.bitbrain.scape.movement.PlayerMovement;
import de.bitbrain.scape.screens.LevelSelectionScreen;

public class LevelCompleteEventListener implements GameEventListener<LevelCompleteEvent> {

   private final BrainGdxGame game;
   private final GameContext context;
   private final LevelMetaData metadata;
   private final AutoReloadPostProcessorEffect<Zoomer> zoomer;

   public LevelCompleteEventListener(BrainGdxGame game, GameContext context, AutoReloadPostProcessorEffect<Zoomer> zoomer, LevelMetaData metadata) {
      this.game = game;
      this.context = context;
      this.metadata = metadata;
      this.zoomer = zoomer;
   }

   @Override
   public void onEvent(final LevelCompleteEvent event) {
      metadata.getProgress().increaseMaxLevel();
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            context.getBehaviorManager().clear();
            zoomer.mutate(new Mutator<Zoomer>() {
               @Override
               public void mutate(Zoomer target) {
                  Camera camera = context.getGameCamera().getInternalCamera();
                  Vector3 worldCoords = new Vector3(event.position().x, event.position().y, 0f);
                  Vector3 screenCoords = camera.project(worldCoords);
                  target.setOrigin(screenCoords.x, screenCoords.y);
               }
            });
            zoomer.mutate(GameConfig.EXIT_ZOOMER_CONFIG_INGAME);
            context.getScreenTransitions().out(new LevelSelectionScreen(game), 1.5f);
            Tween.to(context.getGameCamera(), GameCameraTween.DEFAULT_ZOOM_FACTOR, 2f)
                  .target(0.001f)
                  .ease(TweenEquations.easeInExpo)
                  .start(SharedTweenManager.getInstance());
         }
      });

   }
}
