package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationRenderer;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Zoomer;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.animation.AnimationTypes;
import de.bitbrain.scape.animation.Animator;
import de.bitbrain.scape.animation.PlayerAnimationTypeResolver;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.camera.LevelScrollingBounds;
import de.bitbrain.scape.camera.OutOfBoundsManager;
import de.bitbrain.scape.event.*;
import de.bitbrain.scape.graphics.CharacterType;
import de.bitbrain.scape.graphics.PlayerParticleSpawner;
import de.bitbrain.scape.input.ingame.IngameControllerInputAdapter;
import de.bitbrain.scape.input.ingame.IngameKeyboardInputAdapter;
import de.bitbrain.scape.input.ingame.IngameMobileInputAdapter;
import de.bitbrain.scape.level.LevelMetaData;
import de.bitbrain.scape.movement.*;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.ingame.PointsLabel;
import de.bitbrain.scape.ui.ingame.IngameLevelDescriptionUI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.bitbrain.scape.GameConfig.EXIT_ZOOMER_CONFIG;
import static de.bitbrain.scape.GameConfig.EXIT_ZOOMER_CONFIG_INGAME;
import static de.bitbrain.scape.animation.Animator.animatePowercell;

public class IngameScreen extends AbstractScreen<BrainGdxGame> {

   private final LevelMetaData levelMetaData;

   private PlayerProgress progress;

   private Vector2 resetPosition = new Vector2();
   private GameObject player;
   private LevelScrollingBounds levelScroller;
   private OutOfBoundsManager outOfBoundsManager;
   private GameContext context;

   private List<GameObject> powerCells = new ArrayList<GameObject>();

   private boolean anyKeyPressedToStartlevel = false;

   private boolean exiting = false;
   private boolean gameOver = false;
   private boolean gameComplete = false;

   private IngameLevelDescriptionUI descriptionUI;

   private AutoReloadPostProcessorEffect<Zoomer> zoomerEffect;
   private PlayerMovement movement;

   private Set<GameObject> bytes = new HashSet<GameObject>();

   public IngameScreen(BrainGdxGame game, LevelMetaData levelMetaData) {
      super(game);
      this.levelMetaData = levelMetaData;
   }

   @Override
   protected void onCreate(final GameContext context) {
      context.getLightingManager().setAmbientLight(Colors.BACKGROUND_VIOLET);
      progress = new PlayerProgress(levelMetaData);
      setBackgroundColor(Colors.BACKGROUND_VIOLET);
      context.getTiledMapManager().getAPI().setEventFactory(new ScopeEventFactory());
      context.getTiledMapManager().getAPI().setDebug(false);

      context.getTiledMapManager().load(
            SharedAssetManager.getInstance().get(levelMetaData.getPath(), TiledMap.class),
            context.getGameCamera().getInternalCamera(),
            TiledMapType.ORTHOGONAL
      );

      setupWorld(context);
      setupUI(context);
      setupShaders(context);
      setupPlayer(context);
      setupRendering(context);
   }

   @Override
   public void dispose() {
      super.dispose();
      if (gameComplete) {
         progress.save();
      }
      Controllers.clearListeners();
   }

   public GameObject getPlayer() {
      return player;
   }

   public void exitIngame() {
      context.getLightingManager().setAmbientLight(Color.WHITE, 0.3f, TweenEquations.easeOutCubic);
      context.getStage().clear();
      context.getBehaviorManager().clear();
      context.getScreenTransitions().out(new LevelSelectionScreen(getGame(), true), 0.5f);
      exiting = true;
      zoomerEffect.mutate(EXIT_ZOOMER_CONFIG_INGAME);
   }

   public void setGameOver(boolean gameOver) {
      this.gameOver = gameOver;
   }

   public void setGameComplete(boolean gameComplete) {
      this.gameComplete = gameComplete;
   }

   public boolean isGameOver() {
      return gameOver;
   }

   public Set<GameObject> getAllLoadedBytes() {
      return bytes;
   }

   public void resetUI() {
      if (!gameComplete) {
         progress.setPoints(0);
         player.setPosition(resetPosition.x, resetPosition.y);
         levelScroller.reset();
         movement.reset();
         descriptionUI.show(1f);
         PlayerAdjustment.adjust(player, context);
         movement.setEnabled(false);
         anyKeyPressedToStartlevel = false;
         Tween.to(descriptionUI, ActorTween.ALPHA, 0.6f)
               .target(1f)
               .start(SharedTweenManager.getInstance());
      }
   }

   public void startLevel() {
      if (!anyKeyPressedToStartlevel) {
         anyKeyPressedToStartlevel = true;
         descriptionUI.hide(2f);
         Tween.to(descriptionUI, ActorTween.ALPHA, 1f).delay(0.5f)
               .target(0f)
               .start(SharedTweenManager.getInstance());
         setupEvents(context);
      }
   }

   @Override
   protected void onUpdate(float delta) {
      if (exiting || gameOver) {
         return;
      }
      if (anyKeyPressedToStartlevel) {
         super.onUpdate(delta);
         levelScroller.update(delta);
         outOfBoundsManager.update();
      }
   }

   @Override
   protected Viewport getViewport(int width, int height, Camera camera) {
      return new ExtendViewport(width, height, camera);
   }

   private void setupEvents(GameContext context) {
      context.getEventManager().register(
            new GameOverEventListener(this, context),
            GameOverEvent.class
      );
      context.getEventManager().register(
            new LevelCompleteEventListener(getGame(), this, context, progress, powerCells, player, zoomerEffect),
            LevelCompleteEvent.class
      );
      context.getEventManager().register(
            new ByteCollector(context.getGameWorld(), context.getParticleManager(), progress),
            ByteCollectedEvent.class
      );
   }

   private void setupRendering(GameContext context) {
      final Texture playerTexture = SharedAssetManager.getInstance().get(Assets.Textures.PLAYER);
      AnimationSpriteSheet sheet = new AnimationSpriteSheet(playerTexture, 8);
      final Texture byteTexture = SharedAssetManager.getInstance().get(Assets.Textures.BYTE);
      AnimationSpriteSheet byteSheet = new AnimationSpriteSheet(byteTexture, 8);
      final Texture powercellTexture = SharedAssetManager.getInstance().get(Assets.Textures.POWERCELL);
      AnimationSpriteSheet powercellSheet = new AnimationSpriteSheet(powercellTexture, 16);

      context.getRenderManager().register(CharacterType.PLAYER.name(), new AnimationRenderer(sheet,
            AnimationConfig.builder()
                  .registerFrames(AnimationTypes.PLAYER_DEFAULT, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.1f)
                        .origin(0, 0)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP_REVERSED)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.PLAYER_WALL_CLIMBING, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.1f)
                        .origin(0, 1)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP_REVERSED)
                        .frames(8)
                        .build())
                  .registerFrames(AnimationTypes.PLAYER_WALL_CORNERED, AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.1f)
                        .origin(0, 2)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP_REVERSED)
                        .frames(8)
                        .build())
                  .build(), new PlayerAnimationTypeResolver(movement)
      ));
      context.getRenderManager().register(CharacterType.BYTE.name(), new AnimationRenderer(byteSheet,
            AnimationConfig.builder()
                  .registerFrames(CharacterType.BYTE.name(), AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.05f)
                        .origin(0, 0)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP)
                        .frames(8)
                        .build())
                  .build()
      ));
      context.getRenderManager().register(CharacterType.POWERCELL.name(), new AnimationRenderer(powercellSheet,
            AnimationConfig.builder()
                  .registerFrames(CharacterType.POWERCELL.name(), AnimationFrames.builder()
                        .resetIndex(0)
                        .duration(0.05f)
                        .origin(0, 0)
                        .direction(AnimationFrames.Direction.HORIZONTAL)
                        .playMode(Animation.PlayMode.LOOP_PINGPONG)
                        .frames(8)
                        .build())
                  .build()
      ));
   }

   private void setupWorld(GameContext context) {
      this.context = context;
      levelScroller = new LevelScrollingBounds(context.getTiledMapManager().getAPI(), context.getGameCamera());
      context.getGameWorld().setBounds(levelScroller);

      for (GameObject o : context.getGameWorld()) {
         if (CharacterType.PLAYER.name().equals(o.getType())) {
            o.setDimensions(8f, 8f);
            float correctX = (float) (Math.floor(o.getLeft() / context.getTiledMapManager().getAPI().getCellWidth()) * context.getTiledMapManager().getAPI().getCellWidth());
            float correctY = (float) (Math.floor(o.getTop() / context.getTiledMapManager().getAPI().getCellHeight()) * context.getTiledMapManager().getAPI().getCellHeight());
            o.setPosition(correctX, correctY);
            context.getGameCamera().setStickToWorldBounds(true);
            context.getGameCamera().setDefaultZoomFactor(getCameraZoom());
            context.getGameCamera().setZoomScalingFactor(0.0000001f);
            context.getGameCamera().setTrackingTarget(o);
            context.getGameCamera().setTargetTrackingSpeed(0.15f, 0.05f);
            player = o;
            o.setOrigin(o.getWidth() / 2f, o.getHeight() / 2f);
            player.getColor().a = 0f;
            Tween.to(player, GameObjectTween.ALPHA, 0.3f)
                  .target(1f)
                  .start(SharedTweenManager.getInstance());
            this.resetPosition.x = player.getLeft();
            this.resetPosition.y = player.getTop();
         }
         if (CharacterType.BYTE.name().equals(o.getType())) {
            o.setDimensions(8f, 8f);
            context.getParticleManager().attachEffect(Assets.Particles.BYTE, o, 4f, 4f);
            float correctX = (float) (Math.floor(o.getLeft() / context.getTiledMapManager().getAPI().getCellWidth()) * context.getTiledMapManager().getAPI().getCellWidth());
            float correctY = (float) (Math.floor(o.getTop() / context.getTiledMapManager().getAPI().getCellHeight()) * context.getTiledMapManager().getAPI().getCellHeight());
            o.setPosition(correctX, correctY);
            o.setOrigin(4f, 4f);
            // Create a copy to not interfere with object pooling!
            bytes.add(o.copy());
            Animator.animateByte(context, o);
         }
         if (CharacterType.POWERCELL.name().equals(o.getType())) {
            o.setDimensions(16f, 16f);
            float correctX = (float) (Math.floor(o.getLeft() / context.getTiledMapManager().getAPI().getCellWidth()) * context.getTiledMapManager().getAPI().getCellWidth());
            float correctY = (float) (Math.floor(o.getTop() / context.getTiledMapManager().getAPI().getCellHeight()) * context.getTiledMapManager().getAPI().getCellHeight());
            o.setPosition(correctX, correctY);
            o.setOrigin(8f, 8f);
            context.getBehaviorManager().apply(new PowerCellMovement(), o);
            animatePowercell(context, o);
            powerCells.add(o);
         }
      }
      outOfBoundsManager = new OutOfBoundsManager(context.getEventManager(), levelScroller, player);
   }

   private void setupUI(GameContext context) {
      Table layout = new Table();
      layout.setFillParent(true);
      layout.right().bottom().padRight(90).padBottom(50).add(new PointsLabel(progress, levelMetaData));
      context.getStage().addActor(layout);

      descriptionUI = new IngameLevelDescriptionUI(levelMetaData.getName(), levelMetaData.getLevelNumber());
      context.getStage().addActor(descriptionUI);
      descriptionUI.show(2f);
   }

   private void setupShaders(GameContext context) {
      AutoReloadPostProcessorEffect<Bloom> bloomEffect = context.getShaderManager().createBloomEffect();
      bloomEffect.mutate(GameConfig.DEFAULT_BLOOM_CONFIG);
      zoomerEffect = context.getShaderManager().createZoomerEffect();
      zoomerEffect.mutate(GameConfig.DEFAULT_ZOOMER_CONFIG);
      context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(zoomerEffect, bloomEffect);
   }

   private float getCameraZoom() {
      if (Gdx.graphics.getWidth() > 3000 || Gdx.graphics.getHeight() > 2000) {
         return 350000f * (1f/(Gdx.graphics.getWidth() * Gdx.graphics.getHeight()));
      }
      if (Gdx.graphics.getWidth() < 1300) {
         return 5400f * (20f/(Gdx.graphics.getWidth() * Gdx.graphics.getHeight()));
      }
      return 7800f * (20f/(Gdx.graphics.getWidth() * Gdx.graphics.getHeight()));
   }

   private void setupPlayer(GameContext context) {
      CollisionDetector collisionDetector = new CollisionDetector(context);
      movement = new PlayerMovement(collisionDetector);
      context.getBehaviorManager().apply(movement, player);
      player.setAttribute(Movement.class, movement);
      player.setAttribute(Orientation.class, Orientation.RIGHT);
      player.setOrigin(player.getWidth() / 2f, player.getHeight() / 2f);
      context.getBehaviorManager().apply(new PlayerParticleSpawner(context.getParticleManager(), movement), player);
      PlayerAdjustment.adjust(player, context);
      PlayerControls controls = new PlayerControls(movement, context);
      setupInput(context, controls);
      context.getBehaviorManager().apply(new PointLightBehavior(Color.WHITE, 140f, context.getLightingManager()), player);
   }

   private void setupInput(GameContext context, PlayerControls playerControls) {
      context.getInputManager().register(new IngameKeyboardInputAdapter(playerControls, this));
      context.getInputManager().register(new IngameMobileInputAdapter(playerControls, this));
      context.getInputManager().register(new IngameControllerInputAdapter(playerControls, this));
   }

   public boolean isStarted() {
      return anyKeyPressedToStartlevel;
   }
}
