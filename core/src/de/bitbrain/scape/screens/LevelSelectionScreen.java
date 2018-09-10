package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.postprocessing.effects.Zoomer;
import de.bitbrain.braingdx.postprocessing.filters.RadialBlur;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.ZoomerShaderTween;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.LevelMetaData;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.ui.LevelSelectionUI;

import java.util.HashMap;
import java.util.Map;

public class LevelSelectionScreen extends AbstractScreen<BrainGdxGame> {

   private Zoomer zoomer;

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
      populateLevelMapping(context);
      selector = context.getGameWorld().addObject();
      selectNextLevel();
      GameObject currentlySelected = getLevel(currentlySelectedLevel).getWorldObject();
      selector.setPosition(currentlySelected.getLeft(), currentlySelected.getTop());
      GameCamera camera = context.getGameCamera();
      Tween.registerAccessor(VectorGameCamera.class, new GameCameraTween());
      camera.setStickToWorldBounds(false);
      camera.setTargetTrackingSpeed(0.1f);
      camera.setDefaultZoomFactor(0.0001f);
      camera.setZoomScalingFactor(0f);
      camera.setTrackingTarget(selector, true);
      setupShaders(context);
      Tween.to(camera, GameCameraTween.DEFAULT_ZOOM_FACTOR, 1f)
            .target(0.07f)
            .start(SharedTweenManager.getInstance());
      if (!initialScreen) {
         exiting = true;
         Tween.call(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
               selectNextLevel();
            }
         }).delay(1.5f)
               .start(context.getTweenManager());
         Tween.call(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
               enterLevel();
            }
         }).delay(3.5f)
               .start(context.getTweenManager());

      }
   }

   @Override
   protected void onUpdate(float delta) {
      if (exiting) {
         return;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         Gdx.app.exit();
         exiting = true;
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isTouched()) {
         enterLevel();
         exiting = true;
      } else if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
         selectNextLevel();
      }
   }

   private void populateLevelMapping(GameContext context) {
      this.context = context;
      levelMapping.clear();
      for (GameObject o : context.getGameWorld()) {
         if ("LEVEL".equals(o.getType())) {
            int level = Integer.valueOf((String)((MapProperties)o.getAttribute(MapProperties.class)).get("level"));
            LevelMetaData metadata = new LevelMetaData(
                  level,
                  (String)((MapProperties)o.getAttribute(MapProperties.class)).get("path"),
                  (String)((MapProperties)o.getAttribute(MapProperties.class)).get("name"),
                  (String)((MapProperties)o.getAttribute(MapProperties.class)).get("description")
            );
            LevelSelectionUI levelUI = new LevelSelectionUI(metadata);
            levelUI.setPosition(o.getLeft(), o.getTop());
            levelUI.setScale(0.15f);
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
   private void setupShaders(GameContext context) {
      Bloom bloom = new Bloom(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      Vignette vignette = new Vignette(Gdx.
            graphics.getWidth(), Gdx.graphics.getHeight(), false);
      bloom.setBlurAmount(5f);
      bloom.setBloomIntesity(1.2f);
      bloom.setBlurPasses(50);
      bloom.setThreshold(0.3f);
      zoomer = new Zoomer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), RadialBlur.Quality.High);
      zoomer.setOrigin(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
      zoomer.setZoom(1.5f);
      zoomer.setBlurStrength(10f);
      context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(vignette, zoomer, bloom);

      Tween.to(zoomer, ZoomerShaderTween.ZOOM_AMOUNT, 2f)
            .target(1.0f)
            .ease(TweenEquations.easeOutExpo)
            .start(SharedTweenManager.getInstance());
      Tween.to(zoomer, ZoomerShaderTween.BLUR_STRENGTH, 2f)
            .target(0f)
            .ease(TweenEquations.easeOutExpo)
            .start(SharedTweenManager.getInstance());
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
      context.getScreenTransitions().out(new IngameScreen(getGame(), getCurrentMetaData()), 1f);
      Tween.to(context.getGameCamera(), GameCameraTween.DEFAULT_ZOOM_FACTOR, 1f)
            .target(0.0001f)
            .ease(TweenEquations.easeInExpo)
            .start(SharedTweenManager.getInstance());
      Tween.to(zoomer, ZoomerShaderTween.ZOOM_AMOUNT, 1f)
            .target(1.1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(zoomer, ZoomerShaderTween.BLUR_STRENGTH, 1f)
            .target(5f)
            .ease(TweenEquations.easeInExpo)
            .start(SharedTweenManager.getInstance());
   }
}
