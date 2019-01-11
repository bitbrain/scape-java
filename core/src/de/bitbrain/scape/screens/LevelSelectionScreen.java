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
import de.bitbrain.scape.LevelMetaData;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.i18n.Bundle;
import de.bitbrain.scape.i18n.Messages;
import de.bitbrain.scape.input.GameInputManager;
import de.bitbrain.scape.preferences.PlayerProgress;
import de.bitbrain.scape.ui.LevelSelectionUI;

import java.util.HashMap;
import java.util.Map;

import static de.bitbrain.scape.GameConfig.DEFAULT_ZOOMER_CONFIG;
import static de.bitbrain.scape.GameConfig.EXIT_ZOOMER_CONFIG;
import static de.bitbrain.scape.GameConfig.INITIAL_ZOOMER_CONFIG;

public class LevelSelectionScreen extends AbstractScreen<BrainGdxGame> {

   private AutoReloadPostProcessorEffect<Zoomer> zoomer;
   private PlayerProgress progress;
   private Preferences prefs;

   private class Level {

      private final GameObject worldObject;
      private final LevelMetaData metadata;
      private final Actor uiObject;

      public Level(GameObject worldObject, LevelMetaData metadata, Actor uiObject) {
         this.worldObject = worldObject;
         this.metadata = metadata;
         this.uiObject = uiObject;
      }

      public GameObject getWorldObject() {
         return worldObject;
      }

      public LevelMetaData getMetadata() {
         return metadata;
      }

      public Actor getUiObject() {
         return uiObject;
      }
   }

   private final Map<Integer, Level> levelMapping = new HashMap<Integer, Level>();
   private Integer currentlySelectedLevel = 0;
   private GameObject selector;

   private boolean exiting = false;
   private GameContext context;
   private boolean initialScreen;

   public LevelSelectionScreen(BrainGdxGame game, boolean initialScreen) {
      super(game);
      this.initialScreen = initialScreen;
   }

   public LevelSelectionScreen(BrainGdxGame game) {
      this(game, false);
   }

   @Override
   protected void onCreate(GameContext context) {
      setBackgroundColor(Colors.BACKGROUND_VIOLET);
      context.getTiledMapManager().load(
            SharedAssetManager.getInstance().get(Assets.TiledMaps.WORLD_MAP, TiledMap.class),
            context.getGameCamera().getInternalCamera(),
            TiledMapType.ORTHOGONAL
      );
      this.progress = new PlayerProgress(null);
      populateLevelMapping(context);
      selector = context.getGameWorld().addObject();
      prefs = Gdx.app.getPreferences(GameConfig.PLAYER_PREFERENCES_PATH);
      currentlySelectedLevel = prefs.getInteger(GameConfig.PLAYER_CURRENT_LEVEL, 1) - 1;
      selectNextLevel();
      GameObject currentlySelected = getLevel(currentlySelectedLevel).getWorldObject();
      selector.setPosition(currentlySelected.getLeft(), currentlySelected.getTop());
      GameCamera camera = context.getGameCamera();
      Tween.registerAccessor(VectorGameCamera.class, new GameCameraTween());
      camera.setStickToWorldBounds(false);
      camera.setTargetTrackingSpeed(0.1f);
      camera.setDefaultZoomFactor(0.2f);
      camera.setZoomScalingFactor(0f);
      camera.setTrackingTarget(selector, true);
      setupShaders(context);
      Tween.to(camera, GameCameraTween.DEFAULT_ZOOM_FACTOR, 1f)
            .target(0.15f)
            .start(SharedTweenManager.getInstance());
      if (shouldAutoEnterLevel()) {
         exiting = true;
         Tween.call(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
               selectNextLevel();
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
                  selectPreviousLevel();
                  break;
               case LEFT: case DOWN:
                  selectNextLevel();
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
                  selectNextLevel();
                  break;
               case Input.Keys.S: case Input.Keys.A:
                  selectPreviousLevel();
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

   private void populateLevelMapping(GameContext context) {
      this.context = context;
      levelMapping.clear();
      for (GameObject o : context.getGameWorld()) {
         if ("LEVEL".equals(o.getType())) {
            int level = Integer.valueOf((String)((MapProperties)o.getAttribute(MapProperties.class)).get("level"));
            String translatedName = Bundle.get((String)((MapProperties)o.getAttribute(MapProperties.class)).get("name"));
            String translatedDescription = Bundle.get((String)((MapProperties)o.getAttribute(MapProperties.class)).get("description"));
            LevelMetaData metadata = new LevelMetaData(
                  level,
                  (String)((MapProperties)o.getAttribute(MapProperties.class)).get("path"),
                  translatedName,
                  translatedDescription
            );
            if (metadata.getProgress().getMaximumLevel() < level) {
               break;
            }
            LevelSelectionUI levelUI = new LevelSelectionUI(metadata);
            levelUI.setPosition(o.getLeft(), o.getTop());
            levelUI.setScale(0.2f);
            levelUI.getColor().a = 0f;
            context.getWorldStage().addActor(levelUI);

            levelMapping.put(level, new Level(
                  o, metadata, levelUI
            ));
         }
      }
   }

   private Level getLevel(int level) {
      return levelMapping.get(level);
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

   private void selectPreviousLevel() {
      Level previouslySelected = getLevel(currentlySelectedLevel);
      if (previouslySelected != null) {
         context.getTweenManager().killTarget(previouslySelected.uiObject);
      }
      currentlySelectedLevel--;
      if (getLevel(currentlySelectedLevel) == null) {
         currentlySelectedLevel = levelMapping.size();
      }
      Level currentlySelected = getLevel(currentlySelectedLevel);
      prefs.putInteger(GameConfig.PLAYER_CURRENT_LEVEL, currentlySelected.getMetadata().getNumber());
      prefs.flush();
      selector.setPosition(currentlySelected.getWorldObject().getLeft(), currentlySelected.getWorldObject().getTop());
      if (previouslySelected != null) {
         Tween.to(previouslySelected.getUiObject(), ActorTween.ALPHA, 0.5f)
               .target(0f)
               .start(context.getTweenManager());
      }
      Tween.to(currentlySelected.getUiObject(), ActorTween.ALPHA, 0.5f)
            .target(1f)
            .start(context.getTweenManager());
   }

   private void selectNextLevel() {
      Level previouslySelected = getLevel(currentlySelectedLevel);
      if (previouslySelected != null) {
         context.getTweenManager().killTarget(previouslySelected.uiObject);
      }
      currentlySelectedLevel++;
      if (levelMapping.get(currentlySelectedLevel) == null) {
         currentlySelectedLevel = 1;
      }
      Level currentlySelected = getLevel(currentlySelectedLevel);
      prefs.putInteger(GameConfig.PLAYER_CURRENT_LEVEL, currentlySelected.getMetadata().getNumber());
      prefs.flush();
      selector.setPosition(currentlySelected.getWorldObject().getLeft(), currentlySelected.getWorldObject().getTop());
      if (previouslySelected != null) {
         Tween.to(previouslySelected.getUiObject(), ActorTween.ALPHA, 0.5f)
               .target(0f)
               .start(context.getTweenManager());
      }
      Tween.to(currentlySelected.getUiObject(), ActorTween.ALPHA, 0.5f)
            .target(1f)
            .start(context.getTweenManager());
   }

   private LevelMetaData getCurrentMetaData() {
      return levelMapping.get(currentlySelectedLevel).getMetadata();
   }

   private void enterLevel() {
      context.getScreenTransitions().out(new IngameScreen(getGame(), getCurrentMetaData()), 0.7f);
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
