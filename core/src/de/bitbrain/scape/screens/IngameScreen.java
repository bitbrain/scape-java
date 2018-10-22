package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.graphics.animation.SpriteSheet;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
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
import de.bitbrain.scape.preferences.PlayerProgress;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.camera.OutOfBoundsManager;
import de.bitbrain.scape.event.*;
import de.bitbrain.scape.camera.LevelScrollingBounds;
import de.bitbrain.scape.graphics.CharacterType;
import de.bitbrain.scape.graphics.PlayerParticleSpawner;
import de.bitbrain.scape.movement.CollisionDetector;
import de.bitbrain.scape.movement.PlayerAdjustment;
import de.bitbrain.scape.movement.PlayerMovement;
import de.bitbrain.scape.ui.IngameLevelDescriptionUI;
import de.bitbrain.scape.ui.PointsLabel;

import static de.bitbrain.scape.graphics.CharacterInitializer.createAnimations;

public class IngameScreen extends AbstractScreen<BrainGdxGame> {

   private final LevelMetaData levelMetaData;

   private PlayerProgress progress;

   private Vector2 resetPosition = new Vector2();
   private GameObject player;
   private LevelScrollingBounds levelScroller;
   private OutOfBoundsManager outOfBoundsManager;
   private GameContext context;

   private boolean anyKeyPressedToStartlevel = false;

   private boolean exiting = false;

   private IngameLevelDescriptionUI descriptionUI;

   private AutoReloadPostProcessorEffect<Zoomer> zoomerEffect;
   private PlayerMovement movement;

   public IngameScreen(BrainGdxGame game, LevelMetaData levelMetaData) {
      super(game);
      this.levelMetaData = levelMetaData;
   }

   @Override
   protected void onCreate(final GameContext context) {
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
   }

   @Override
   public void dispose() {
      super.dispose();
      progress.save();
   }

   @Override
   protected void onUpdate(float delta) {
      if (exiting) {
         return;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         context.getScreenTransitions().out(new LevelSelectionScreen(getGame(), true), 1f);
         zoomerEffect.mutate(GameConfig.EXIT_ZOOMER_CONFIG);
         exiting = true;
         return;
      }
      if (!anyKeyPressedToStartlevel && (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched())) {
         anyKeyPressedToStartlevel = true;
         descriptionUI.hide(2f);
         Tween.to(descriptionUI, ActorTween.ALPHA, 1f).delay(0.5f)
               .target(0f)
               .setCallbackTriggers(TweenCallback.COMPLETE)
               .setCallback(new TweenCallback() {
                  @Override
                  public void onEvent(int type, BaseTween<?> source) {
                     context.getStage().getActors().removeValue(descriptionUI, false);
                  }
               })
               .start(SharedTweenManager.getInstance());
         setupPlayer();
         setupEvents(context);
      }
      if (anyKeyPressedToStartlevel) {
         super.onUpdate(delta);
         levelScroller.update(delta);
         outOfBoundsManager.update();
      }
   }

   public void resetLevel() {
      player.setPosition(resetPosition.x, resetPosition.y);
      levelScroller.reset();
      PlayerAdjustment.adjust(player, context);
   }

   private void setupEvents(GameContext context) {
      context.getEventManager().register(
            new GameOverEventListener(this),
            GameOverEvent.class
      );
      context.getEventManager().register(
            new LevelCompleteEventListener(getGame(), context, zoomerEffect, levelMetaData),
            LevelCompleteEvent.class
      );
      context.getEventManager().register(
            new ByteCollector(context.getGameWorld(), context.getParticleManager(), progress),
            ByteCollectedEvent.class
      );
   }

   private void setupWorld(GameContext context) {
      this.context = context;
      levelScroller = new LevelScrollingBounds(context.getTiledMapManager().getAPI(), context.getGameCamera());
      context.getGameWorld().setBounds(levelScroller);
      final Texture playerTexture = SharedAssetManager.getInstance().get(Assets.Textures.PLAYER);
      SpriteSheet sheet = new SpriteSheet(playerTexture, 8, 2);
      final Texture powercellTexture = SharedAssetManager.getInstance().get(Assets.Textures.POWERCELL);
      SpriteSheet powercellSheet = new SpriteSheet(powercellTexture, 8, 1);
      createAnimations(context, sheet, CharacterType.PLAYER, AnimationTypes.FORWARD)
            .origin(0, 0)
            .frames(8)
            .interval(0.05f);
      createAnimations(context, sheet, CharacterType.BYTE, AnimationTypes.FORWARD)
            .origin(0, 1)
            .frames(8)
            .interval(0.05f);
      createAnimations(context, powercellSheet, CharacterType.POWERCELL, AnimationTypes.FORWARD)
            .origin(0, 0)
            .frames(8)
            .interval(0.1f);
      for (GameObject o : context.getGameWorld()) {
         if ("PLAYER".equals(o.getType())) {
            o.setDimensions(8f, 8f);
            float correctX = (float) (Math.floor(o.getLeft() / context.getTiledMapManager().getAPI().getCellWidth()) * context.getTiledMapManager().getAPI().getCellWidth());
            float correctY = (float) (Math.floor(o.getTop() / context.getTiledMapManager().getAPI().getCellHeight()) * context.getTiledMapManager().getAPI().getCellHeight());
            o.setPosition(correctX, correctY);
            context.getGameCamera().setStickToWorldBounds(true);
            context.getGameCamera().setDefaultZoomFactor(0.08f);
            context.getGameCamera().setZoomScalingFactor(0.0000001f);
            context.getGameCamera().setTrackingTarget(o);
            context.getGameCamera().setTargetTrackingSpeed(0.07f);
            player = o;
            this.resetPosition.x = player.getLeft();
            this.resetPosition.y = player.getTop();
         }
         if ("BYTE".equals(o.getType())) {
            o.setDimensions(8f, 8f);
            context.getParticleManager().attachEffect(Assets.Particles.BYTE, o, 4f, 4f);
            float correctX = (float) (Math.floor(o.getLeft() / context.getTiledMapManager().getAPI().getCellWidth()) * context.getTiledMapManager().getAPI().getCellWidth());
            float correctY = (float) (Math.floor(o.getTop() / context.getTiledMapManager().getAPI().getCellHeight()) * context.getTiledMapManager().getAPI().getCellHeight());
            o.setPosition(correctX, correctY);
         }
         if ("POWERCELL".equals(o.getType())) {
            o.setDimensions(16f, 16f);
            context.getParticleManager().attachEffect(Assets.Particles.BYTE, o, 8f, 8f);
            float correctX = (float) (Math.floor(o.getLeft() / context.getTiledMapManager().getAPI().getCellWidth()) * context.getTiledMapManager().getAPI().getCellWidth());
            float correctY = (float) (Math.floor(o.getTop() / context.getTiledMapManager().getAPI().getCellHeight()) * context.getTiledMapManager().getAPI().getCellHeight());
            o.setPosition(correctX, correctY);
         }
      }
      outOfBoundsManager = new OutOfBoundsManager(context.getEventManager(), levelScroller, player);
   }

   private void setupUI(GameContext context) {
      Table layout = new Table();
      layout.setFillParent(true);
      layout.right().bottom().padRight(90).padBottom(50).add(new PointsLabel(progress));
      context.getStage().addActor(layout);

      descriptionUI = new IngameLevelDescriptionUI(levelMetaData.getName(), levelMetaData.getNumber());
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

   private void setupPlayer() {
      CollisionDetector collisionDetector = new CollisionDetector(context);
      movement = new PlayerMovement(collisionDetector);
      context.getBehaviorManager().apply(movement, player);
      player.setAttribute(Movement.class, movement);
      player.setAttribute(Orientation.class, Orientation.RIGHT);
      context.getBehaviorManager().apply(new PlayerParticleSpawner(context.getParticleManager(), movement), player);
      PlayerAdjustment.adjust(player, context);
   }
}
