package de.bitbrain.scape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.graphics.animation.SpriteSheet;
import de.bitbrain.braingdx.graphics.particles.ParticleManagerRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipe;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.camera.OutOfBoundsManager;
import de.bitbrain.scape.event.LevelCompleteEventListener;
import de.bitbrain.scape.camera.LevelScrollingBounds;
import de.bitbrain.scape.event.GameOverEvent;
import de.bitbrain.scape.event.GameOverEventListener;
import de.bitbrain.scape.event.LevelCompleteEvent;
import de.bitbrain.scape.event.ScopeEventFactory;
import de.bitbrain.scape.graphics.CharacterInitializer;
import de.bitbrain.scape.graphics.CharacterType;
import de.bitbrain.scape.graphics.PlayerParticleSpawner;
import de.bitbrain.scape.model.Direction;
import de.bitbrain.scape.movement.CollisionDetector;
import de.bitbrain.scape.movement.PlayerAdjustment;
import de.bitbrain.scape.movement.PlayerMovement;

public class IngameScreen extends AbstractScreen<BrainGdxGame> {

   private final String tiledMapPath;

   private Vector2 resetPosition = new Vector2();
   private GameObject player;
   private LevelScrollingBounds levelScroller;
   private OutOfBoundsManager outOfBoundsManager;

   public IngameScreen(BrainGdxGame game, String tiledMapPath) {
      super(game);
      this.tiledMapPath = tiledMapPath;
   }

   @Override
   protected void onCreate(final GameContext context) {
      setBackgroundColor(Color.valueOf("140a1b"));
      context.getTiledMapManager().load(
            SharedAssetManager.getInstance().get(tiledMapPath, TiledMap.class),
            context.getGameCamera().getInternalCamera(),
            TiledMapType.ORTHOGONAL
      );
      context.getEventManager().register(
            new GameOverEventListener(this),
            GameOverEvent.class
      );
      context.getEventManager().register(
            new LevelCompleteEventListener(getGame(), context),
            LevelCompleteEvent.class
      );
      context.getTiledMapManager().getAPI().setEventFactory(new ScopeEventFactory());
      context.getTiledMapManager().getAPI().setDebug(false);

      final Texture playerTexture = SharedAssetManager.getInstance().get(Assets.Textures.PLAYER);
      SpriteSheet sheet = new SpriteSheet(playerTexture, 8, 1);
      CharacterInitializer.createAnimations(context, sheet, CharacterType.PLAYER);
      for (GameObject o : context.getGameWorld()) {
         if ("PLAYER".equals(o.getType())) {
            o.setDimensions(8f, 8f);
            context.getGameCamera().setStickToWorldBounds(true);
            context.getGameCamera().setDefaultZoomFactor(0.15f);
            context.getGameCamera().setTrackingTarget(o);
            context.getGameCamera().setTargetTrackingSpeed(0.05f);
            CollisionDetector collisionDetector = new CollisionDetector(context);
            PlayerMovement movement = new PlayerMovement(collisionDetector);
            context.getBehaviorManager().apply(movement, o);
            o.setAttribute(Movement.class, movement);
            o.setAttribute(Direction.class, Direction.UP);
            o.setAttribute(Orientation.class, Orientation.RIGHT);
            context.getBehaviorManager().apply(new PlayerParticleSpawner(context.getParticleManager(), movement), o);
            PlayerAdjustment.adjust(o, context);
            player = o;
            this.resetPosition.x = player.getLeft();
            this.resetPosition.y = player.getTop();
         }
      }
      levelScroller = new LevelScrollingBounds(context.getTiledMapManager().getAPI());
      context.getGameWorld().setBounds(levelScroller);
      outOfBoundsManager = new OutOfBoundsManager(context.getEventManager(), levelScroller, player);

      setupShaders(context);
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      levelScroller.update(delta);
      outOfBoundsManager.update();
   }

   public void resetLevel() {
      player.setPosition(resetPosition.x, resetPosition.y);
      levelScroller.reset();
   }

   private void setupShaders(GameContext context) {
      Bloom bloom = new Bloom(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      Vignette vignette = new Vignette(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, false);
      vignette.setIntensity(0.6f);
      bloom.setBlurAmount(5f);
      bloom.setBloomIntesity(0.8f);
      bloom.setBlurPasses(50);
      bloom.setThreshold(0.3f);
      context.getRenderPipeline().getPipe(RenderPipeIds.WORLD).addEffects(vignette, bloom);
   }
}
