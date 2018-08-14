package de.bitbrain.scape.camera;

import com.badlogic.gdx.math.Rectangle;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.scape.event.GameOverEvent;

public class OutOfBoundsManager {

   private final GameEventManager gameEventManager;
   private final GameWorld.WorldBounds worldBounds;
   private final GameObject gameObject;

   private final Rectangle worldBoundsRect, objectBoundsRect;

   public OutOfBoundsManager(GameEventManager gameEventManager, GameWorld.WorldBounds worldBounds, GameObject gameObject) {
      this.gameEventManager = gameEventManager;
      this.worldBounds = worldBounds;
      this.gameObject = gameObject;
      this.worldBoundsRect = new Rectangle();
      this.objectBoundsRect = new Rectangle();
   }

   public void update() {
      worldBoundsRect.set(
            worldBounds.getWorldOffsetX(),
            worldBounds.getWorldOffsetY(),
            worldBounds.getWorldWidth(),
            worldBounds.getWorldHeight()
      );
      objectBoundsRect.set(
            gameObject.getLeft(),
            gameObject.getTop(),
            gameObject.getWidth(),
            gameObject.getHeight()
      );
      if (isObjectOutOfBounds()) {
         gameEventManager.publish(new GameOverEvent());
      }
   }

   private boolean isObjectOutOfBounds() {
      return !worldBoundsRect.contains(objectBoundsRect) && !worldBoundsRect.overlaps(objectBoundsRect);
   }
}
