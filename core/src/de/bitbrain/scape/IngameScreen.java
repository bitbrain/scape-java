package de.bitbrain.scape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
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
import de.bitbrain.scape.graphics.DirectionSpriteRenderer;
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
      setBackgroundColor(Color.GRAY);
      context.getRenderManager().register(
            "spawn",
            new DirectionSpriteRenderer(Assets.Textures.PLAYER)
      );
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
      for (GameObject o : context.getGameWorld()) {
         if ("spawn".equals(o.getType())) {
            o.setDimensions(32f, 32f);
            context.getGameCamera().setStickToWorldBounds(true);
            context.getGameCamera().setDefaultZoomFactor(0.4f);
            context.getGameCamera().setTrackingTarget(o);
            context.getGameCamera().setTargetTrackingSpeed(0.5f);
            CollisionDetector collisionDetector = new CollisionDetector(context);
            context.getBehaviorManager().apply(new PlayerMovement(collisionDetector), o);
            o.setAttribute(Direction.class, Direction.UP);
            PlayerAdjustment.adjust(o, context);
            player = o;
            this.resetPosition.x = player.getLeft();
            this.resetPosition.y = player.getTop();
         }
      }
      levelScroller = new LevelScrollingBounds(context.getTiledMapManager().getAPI());
      context.getGameWorld().setBounds(levelScroller);
      outOfBoundsManager = new OutOfBoundsManager(context.getEventManager(), levelScroller, player);
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
}
