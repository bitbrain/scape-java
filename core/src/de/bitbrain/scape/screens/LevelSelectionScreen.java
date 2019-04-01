package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationRenderer;
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
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.input.TouchInputManager;
import de.bitbrain.scape.input.levelselection.LevelSelectionControllerInputAdapter;
import de.bitbrain.scape.input.levelselection.LevelSelectionKeyboardInputAdapter;
import de.bitbrain.scape.input.levelselection.LevelSelectionMobileInputAdapter;
import de.bitbrain.scape.level.LevelManager;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.Styles;

import static de.bitbrain.scape.GameConfig.*;

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
      camera.setDefaultZoomFactor(0.3f);
      camera.setZoomScalingFactor(0f);
      setupInput(context);
      setupUI(context);
      setupShaders(context);
      Tween.to(camera, GameCameraTween.DEFAULT_ZOOM_FACTOR, 1f)
            .target(0.12f)
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
   }

   private void setupUI(final GameContext context) {
      Table layout = new Table();
      layout.setFillParent(true);

      final Texture playerTexture = SharedAssetManager.getInstance().get(Assets.Textures.PLAYER);
      AnimationSpriteSheet sheet = new AnimationSpriteSheet(playerTexture, 8);
      Image image = new Image(new AnimationDrawable(sheet,
            AnimationConfig.builder()
                  .registerFrames(AnimationDrawable.DEFAULT_FRAME_ID, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.05f)
                        .origin(0, 1)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .build()
      ));

      Label progress = new Label(levelManager.getTotalCollectedBytes() + "/" + levelManager.getTotalBytes(), Styles.LABEL_SELECTION_TOTAL_PROGRESS);
      layout.right().bottom().add(image).width(100f).height(100f)
            .padRight(10f)
            .padBottom(50f);
      layout.add(progress)
            .padRight(80f)
            .padBottom(50f);
      context.getStage().addActor(layout);
   }

   private void setupInput(GameContext context) {
      TouchInputManager touchManager = new TouchInputManager();
      touchManager.addListener(new LevelSelectionMobileInputAdapter(levelManager, this));
      context.getInputManager().register(touchManager);
      context.getInputManager().register(new LevelSelectionKeyboardInputAdapter(levelManager, this));
      context.getInputManager().register(new LevelSelectionControllerInputAdapter(levelManager, this));
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
      context.getRenderPipeline().getPipe(RenderPipeIds.WORLD_UI).addEffects(vignette);
      context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(zoomer, bloom);
   }

   public void enterLevel() {
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
