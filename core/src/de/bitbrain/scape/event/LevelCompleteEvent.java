package de.bitbrain.scape.event;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.event.GameEvent;

public class LevelCompleteEvent implements GameEvent {

   private final String nextTiledMap;
   private final Vector2 position;

   public LevelCompleteEvent(String nextTiledMap, Vector2 position) {
      this.nextTiledMap = nextTiledMap;
      this.position = position;
   }

   public String getNextTiledMap() {
      return nextTiledMap;
   }

   public Vector2 position() {
      return position;
   }
}
