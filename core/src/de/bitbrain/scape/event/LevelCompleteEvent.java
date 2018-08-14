package de.bitbrain.scape.event;

import de.bitbrain.braingdx.event.GameEvent;

public class LevelCompleteEvent implements GameEvent {

   private final String nextTiledMap;

   public LevelCompleteEvent(String nextTiledMap) {
      this.nextTiledMap = nextTiledMap;
   }

   public String getNextTiledMap() {
      return nextTiledMap;
   }
}
