package de.bitbrain.scape.event;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Zoomer;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.ZoomerShaderTween;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.screens.IngameScreen;
import de.bitbrain.scape.screens.StageCompleteScreen;
import de.bitbrain.scape.screens.StageSelectionScreen;

import java.util.List;

import static de.bitbrain.braingdx.world.GameObjectUtils.distanceBetween;

public class LevelCompleteEventListener implements GameEventListener<LevelCompleteEvent> {

   private final ScapeGame game;
   private final GameContext context;
   private final PlayerProgress progress;
   private final List<GameObject> powerCells;
   private final GameObject player;
   private final AutoReloadPostProcessorEffect<Zoomer> zoomerEffect;
   private final IngameScreen screen;

   public LevelCompleteEventListener(
         ScapeGame game,
         IngameScreen screen,
         GameContext context,
         PlayerProgress progress,
         List<GameObject> powerCells,
         GameObject player,
         AutoReloadPostProcessorEffect<Zoomer> zoomerEffect) {
      this.game = game;
      this.screen = screen;
      this.context = context;
      this.progress = progress;
      this.powerCells = powerCells;
      this.player = player;
      this.zoomerEffect = zoomerEffect;
   }

   @Override
   public void onEvent(final LevelCompleteEvent event) {
      if (screen.isGameOver()) {
         return;
      }
      if (screen.isGameComplete()) {
         return;
      }
      screen.setGameComplete(true);
      if (screen.getStartTime() != 0) {
         this.progress.setCurrentTime(System.currentTimeMillis() - screen.getStartTime());
      }
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            context.getBehaviorManager().clear();
            context.getStage().clear();
            context.getWorldStage().clear();
            context.getScreenTransitions().out(new StageCompleteScreen(game, progress), 0.5f);
            for (GameObject powercell : powerCells) {
               SharedTweenManager.getInstance().killTarget(powercell);
               Tween.to(powercell, GameObjectTween.SCALE, 1f)
                     .target(8f)
                     .start(SharedTweenManager.getInstance());
               Tween.to(powercell, GameObjectTween.ALPHA, 1f)
                     .target(0f)
                     .start(SharedTweenManager.getInstance());
            }
            SharedTweenManager.getInstance().killTarget(player);
            player.setOrigin(player.getWidth() / 2f, player.getHeight() / 2f);
            Tween.to(player, GameObjectTween.SCALE, 0.8f)
                  .target(0f)
                  .ease(TweenEquations.easeOutQuad)
                  .start(context.getTweenManager());
            Tween.to(player, GameObjectTween.ALPHA, 0.8f)
                  .target(0f)
                  .ease(TweenEquations.easeOutQuad)
                  .start(context.getTweenManager());
            context.getGameCamera().setTrackingTarget(null);
            context.getGameCamera().focusCentered(player);
            context.getGameCamera().setStickToWorldBounds(false);
            if (zoomerEffect != null) {
               zoomerEffect.mutate(new Mutator<Zoomer>() {
                  @Override
                  public void mutate(Zoomer target) {
                     Tween.to(target, ZoomerShaderTween.ZOOM_AMOUNT, 2f)
                           .target(3f)
                           .ease(TweenEquations.easeInOutCubic)
                           .start(SharedTweenManager.getInstance());
                     Tween.to(target, ZoomerShaderTween.BLUR_STRENGTH, 2f)
                           .target(4f)
                           .ease(TweenEquations.easeInOutCubic)
                           .start(SharedTweenManager.getInstance());
                     GameObject closestPowerCell = null;
                     for (GameObject powercell : powerCells) {
                        if (closestPowerCell == null || distanceBetween(powercell, player) < distanceBetween(closestPowerCell, player)) {
                           closestPowerCell = powercell;
                        }
                     }
                     if (closestPowerCell != null) {
                        Vector3 worldCoords = new Vector3(
                              closestPowerCell.getLeft() + closestPowerCell.getWidth() / 2f,
                              closestPowerCell.getTop() + closestPowerCell.getHeight() / 2f,
                              0f);
                        Vector3 screenCoords = context.getGameCamera().getInternalCamera().project(worldCoords);
                        target.setOrigin(screenCoords.x, screenCoords.y);
                     }
                  }
               });
            }
            Tween.to(context.getGameCamera(), GameCameraTween.DEFAULT_ZOOM_FACTOR, 2f)
                  .target(1.5f)
                  .ease(TweenEquations.easeInExpo)
                  .start(SharedTweenManager.getInstance());
         }
      });

   }
}
