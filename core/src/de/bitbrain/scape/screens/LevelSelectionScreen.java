package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Zoomer;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.level.LevelManager;
import de.bitbrain.scape.level.LevelMetaData;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.i18n.Bundle;
import de.bitbrain.scape.input.GameInputManager;
import de.bitbrain.scape.preferences.PlayerProgress;
import de.bitbrain.scape.ui.LevelOverviewUI;

import java.util.HashMap;
import java.util.Map;

import static de.bitbrain.scape.GameConfig.DEFAULT_ZOOMER_CONFIG;
import static de.bitbrain.scape.GameConfig.EXIT_ZOOMER_CONFIG;
import static de.bitbrain.scape.GameConfig.INITIAL_ZOOMER_CONFIG;

public class LevelSelectionScreen extends AbstractScreen<BrainGdxGame> {

   private AutoReloadPostProcessorEffect<Zoomer> zoomer;
   private PlayerProgress progress;

   private boolean exiting = false;
   private GameContext context;
   private boolean initialScreen;
   private LevelManager levelManager;

   public LevelSelectionScreen(BrainGdxGame game, boolean initialScreen) {
      super(game);
      this.initialScreen = initialScreen;
   }

   public LevelSelectionScreen(BrainGdxGame game) {
      this(game, false);
   }

   @Override
   protected void onUpdate(float delta) {
      if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         Gdx.app.exit();
      }
   }

   @Override
   protected void onCreate(GameContext context) {
      this.context = context;
      setBackgroundColor(Colors.BACKGROUND_VIOLET);
      context.getTiledMapManager().load(
            SharedAssetManager.getInstance().get(Assets.TiledMaps.WORLD_MAP, TiledMap.class),
            context.getGameCamera().getInternalCamera(),
            TiledMapType.ORTHOGONAL
      );
      this.progress = new PlayerProgress(null);
      levelManager = new LevelManager(context);
      Tween.registerAccessor(VectorGameCamera.class, new GameCameraTween());
      GameCamera camera = context.getGameCamera();
      camera.setStickToWorldBounds(false);
      camera.setTargetTrackingSpeed(0.1f);
      camera.setDefaultZoomFactor(0.2f);
      camera.setZoomScalingFactor(0f);
      setupShaders(context);
      Tween.to(camera, GameCameraTween.DEFAULT_ZOOM_FACTOR, 1f)
            .target(0.15f)
            .start(SharedTweenManager.getInstance());
      if (shouldAutoEnterLevel()) {
         exiting = true;
         Tween.call(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
               levelManager.selectNextLevel();
            }
         }).delay(1.0f)
               .start(context.getTweenManager());
         Tween.call(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
               enterLevel();
            }
         }).delay(2.5f)
               .start(context.getTweenManager());

      }

      GameInputManager inputManager = new GameInputManager();
      inputManager.addListener(new GameInputManager.GameInputListener() {
         @Override
         public void onSwipe(Orientation orientation) {
            if (exiting) {
               return;
            }
            switch (orientation) {
               case RIGHT: case UP:
                  levelManager.selectPreviousLevel();
                  break;
               case LEFT: case DOWN:
                  levelManager.selectNextLevel();
            }
         }

         @Override
         public void onTouch() {
            if (exiting) {
               return;
            }
            enterLevel();
            exiting = true;
         }

         @Override
         public void onType(int key) {
            if (exiting) {
               return;
            }
            switch (key) {
               case Input.Keys.W: case Input.Keys.D:
                  levelManager.selectNextLevel();
                  break;
               case Input.Keys.S: case Input.Keys.A:
                  levelManager.selectPreviousLevel();
                  break;
               case Input.Keys.ESCAPE:
                  Gdx.app.exit();
                  exiting = true;
                  break;
               case Input.Keys.ENTER:
                  enterLevel();
                  exiting = true;
                  break;
            }
         }
      });
      context.getInput().addProcessor(inputManager);
   }

   private void setupShaders(final GameContext context) {
      AutoReloadPostProcessorEffect<Bloom> bloom = context.getShaderManager().createBloomEffect();
      Vignette vignette = new Vignette(Gdx.
            graphics.getWidth(), Gdx.graphics.getHeight(), false);
      bloom.mutate(GameConfig.DEFAULT_BLOOM_CONFIG);
      zoomer = context.getShaderManager().createZoomerEffect();
      if (initialScreen) {
         zoomer.mutate(INITIAL_ZOOMER_CONFIG);
      } else {
         zoomer.mutate(DEFAULT_ZOOMER_CONFIG);
      }
      context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(vignette, zoomer, bloom);
   }

   private void enterLevel() {
      context.getScreenTransitions().out(new IngameScreen(getGame(), levelManager.getCurrentMetaData()), 0.7f);
      Tween.to(context.getGameCamera(), GameCameraTween.DEFAULT_ZOOM_FACTOR, 0.7f)
            .target(0.001f)
            .ease(TweenEquations.easeInExpo)
            .start(SharedTweenManager.getInstance());
      zoomer.mutate(EXIT_ZOOMER_CONFIG);
   }

   private boolean shouldAutoEnterLevel() {
      return !initialScreen || progress.isNewGame();
   }
}
