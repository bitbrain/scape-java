package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Zoomer;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.ui.AnimationDrawable;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.input.levelselection.LevelSelectionControllerInputAdapter;
import de.bitbrain.scape.input.levelselection.LevelSelectionKeyboardInputAdapter;
import de.bitbrain.scape.input.levelselection.LevelSelectionMobileInputAdapter;
import de.bitbrain.scape.level.StageManager;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.Styles;

import static de.bitbrain.scape.GameConfig.*;

public class StageSelectionScreen extends AbstractScreen<ScapeGame> {

   private AutoReloadPostProcessorEffect<Zoomer> zoomer;
   private PlayerProgress progress;

   private boolean exiting = false;
   private GameContext context;
   private boolean manualNavigationMode;
   private StageManager stageManager;

   public StageSelectionScreen(ScapeGame game, boolean manualNavigationMode) {
      super(game);
      this.manualNavigationMode = manualNavigationMode;
   }

   public StageSelectionScreen(ScapeGame game) {
      this(game, false);
   }

   @Override
   protected void onUpdate(float delta) {
      if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         Gdx.app.exit();
      }
   }

   @Override
   public void dispose() {
      super.dispose();
      Controllers.clearListeners();
   }

   public boolean isExiting() {
      return exiting;
   }

   public void exit() {
      exiting = true;
   }

   @Override
   protected void onCreate(GameContext context) {
      this.context = context;
      context.getLightingManager().setAmbientLight(Colors.BACKGROUND_VIOLET);
      setBackgroundColor(Colors.BACKGROUND_VIOLET);
      context.getTiledMapManager().load(
            SharedAssetManager.getInstance().get(Assets.TiledMaps.WORLD_MAP, TiledMap.class),
            context.getGameCamera().getInternalCamera(),
            TiledMapType.ORTHOGONAL
      );
      this.progress = new PlayerProgress(null);
      stageManager = new StageManager(context);

      Tween.registerAccessor(VectorGameCamera.class, new GameCameraTween());
      GameCamera camera = context.getGameCamera();
      camera.setStickToWorldBounds(false);

      camera.setTargetTrackingSpeed(0.1f);
      camera.setDefaultZoomFactor(0.3f);
      camera.setZoomScalingFactor(0f);
      camera.setDistanceStoppingThreshold(40f);
      setupInput(context);
      setupUI(context);
      if (Gdx.app.getType() != Application.ApplicationType.Android && Gdx.app.getType() != Application.ApplicationType.iOS) {
         setupShaders(context);
      }
      Tween.to(camera, GameCameraTween.DEFAULT_ZOOM_FACTOR, 1f)
            .target(0.09f)
            .start(SharedTweenManager.getInstance());
      if (shouldAutoEnterLevel()) {
         exiting = true;
         Tween.call(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
               stageManager.selectNextLevel();
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
   }

   private void setupUI(final GameContext context) {
      Table layout = new Table();
      layout.setFillParent(true);

      final Texture playerTexture = SharedAssetManager.getInstance().get(Assets.Textures.BYTE);
      AnimationSpriteSheet sheet = new AnimationSpriteSheet(playerTexture, 8);
      Image image = new Image(new AnimationDrawable(sheet,
            AnimationConfig.builder()
                  .registerFrames(AnimationDrawable.DEFAULT_FRAME_ID, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.05f)
                        .origin(0, 0)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .build()
      ));

      Label progress = new Label(stageManager.getTotalCollectedBytes() + "/" + stageManager.getTotalBytes(), Styles.LABEL_SELECTION_TOTAL_PROGRESS);
      layout.right().bottom().add(image).width(100f).height(100f)
            .padRight(10f)
            .padBottom(50f);
      layout.add(progress)
            .padRight(80f)
            .padBottom(50f);
      context.getStage().addActor(layout);
   }

   private void setupInput(GameContext context) {
      GestureDetector detector = new GestureDetector(new LevelSelectionMobileInputAdapter(stageManager, this));
      detector.setLongPressSeconds(0.1f);
      context.getInputManager().register(detector);
      context.getInputManager().register(new LevelSelectionKeyboardInputAdapter(stageManager, this));
      context.getInputManager().register(new LevelSelectionControllerInputAdapter(stageManager, this));
   }

   private void setupShaders(final GameContext context) {
      AutoReloadPostProcessorEffect<Bloom> bloom = context.getShaderManager().createBloomEffect();
      Vignette vignette = new Vignette(Gdx.
            graphics.getWidth(), Gdx.graphics.getHeight(), false);
      bloom.mutate(GameConfig.DEFAULT_BLOOM_CONFIG);
      zoomer = context.getShaderManager().createZoomerEffect();
      if (manualNavigationMode) {
         zoomer.mutate(INITIAL_ZOOMER_CONFIG);
      } else {
         zoomer.mutate(DEFAULT_ZOOMER_CONFIG);
      }
      context.getRenderPipeline().addEffects(RenderPipeIds.WORLD_UI, vignette);
      context.getRenderPipeline().addEffects(RenderPipeIds.UI, zoomer, bloom);
   }

   public void enterLevel() {
      context.getLightingManager().setAmbientLight(Color.WHITE, 0.4f, TweenEquations.easeOutCubic);
      context.getWorldStage().clear();
      context.getStage().clear();
      context.getScreenTransitions().out(new IngameScreen(getGame(), stageManager.getCurrentMetaData()), 0.5f);
      Tween.to(context.getGameCamera(), GameCameraTween.DEFAULT_ZOOM_FACTOR, 0.5f)
            .target(0.005f)
            .ease(TweenEquations.easeInExpo)
            .start(SharedTweenManager.getInstance());
      if (zoomer != null) {
         zoomer.mutate(EXIT_ZOOMER_CONFIG);
      }
   }

   public boolean shouldAutoEnterLevel() {
      return (!manualNavigationMode || progress.isNewGame()) && !getGame().isDebugMode();
   }
}
