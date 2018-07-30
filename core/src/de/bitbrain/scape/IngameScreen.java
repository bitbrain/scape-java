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
import de.bitbrain.scape.event.GameOverEvent;
import de.bitbrain.scape.event.GameOverEventListener;
import de.bitbrain.scape.event.ScopeEventFactory;
import de.bitbrain.scape.graphics.DirectionSpriteRenderer;
import de.bitbrain.scape.model.Direction;
import de.bitbrain.scape.movement.CollisionDetector;
import de.bitbrain.scape.movement.PlayerAdjustment;
import de.bitbrain.scape.movement.PlayerMovement;

public class IngameScreen extends AbstractScreen<BrainGdxGame> {

   private Vector2 resetPosition = new Vector2();
   private GameObject player;

   public IngameScreen(BrainGdxGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final GameContext context) {
      setBackgroundColor(Color.GRAY);
      context.getRenderManager().register("spawn", new DirectionSpriteRenderer(Assets.Textures.PLAYER));
      context.getTiledMapManager().load(SharedAssetManager.getInstance().get(Assets.TiledMaps.LEVEL_1, TiledMap.class), context.getGameCamera().getInternal(), TiledMapType.ORTHOGONAL);
      context.getEventManager().register(new GameOverEventListener(this), GameOverEvent.class);
      context.getTiledMapManager().getAPI().setEventFactory(new ScopeEventFactory());
      context.getTiledMapManager().getAPI().setDebug(true);
      for (GameObject o : context.getGameWorld()) {
         if ("spawn".equals(o.getType())) {
            o.setDimensions(32f, 32f);
            context.getGameCamera().setStickToWorldBounds(false);
            context.getGameCamera().setBaseZoom(0.3f);
            context.getGameCamera().setTarget(o);
            context.getGameCamera().setSpeed(0.05f);
            CollisionDetector collisionDetector = new CollisionDetector(context);
            context.getBehaviorManager().apply(new PlayerMovement(collisionDetector), o);
            o.setAttribute(Direction.class, Direction.UP);
            PlayerAdjustment.adjust(o, context);
            player = o;
            this.resetPosition.x = player.getLeft();
            this.resetPosition.y = player.getTop();
         }
      }
   }

   public void resetLevel() {
      player.setPosition(resetPosition.x, resetPosition.y);
   }
}
