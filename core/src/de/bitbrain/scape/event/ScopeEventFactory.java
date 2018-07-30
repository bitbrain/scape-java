package de.bitbrain.scape.event;

import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.event.GameEventFactory;
import de.bitbrain.braingdx.world.GameObject;

public class ScopeEventFactory implements GameEventFactory {

   @Override
   public GameEvent create(GameObject eventObject, GameObject producerObject) {
      if (eventObject.getType().equals("game_over_event")) {
         return new GameOverEvent();
      }
      return null;
   }

   @Override
   public Object[] identifiers() {
      return new Object[]{"game_over_event"};
   }
}
