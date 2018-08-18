package de.bitbrain.scape.event;

import com.badlogic.gdx.maps.MapProperties;
import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.event.GameEventFactory;
import de.bitbrain.braingdx.world.GameObject;

public class ScopeEventFactory implements GameEventFactory {

   @Override
   public GameEvent create(GameObject eventObject, GameObject producerObject) {
      if (eventObject.getType().equals("game_over_event")) {
         return new GameOverEvent();
      }
      if (eventObject.getType().equals("level_complete_event")) {
         MapProperties properties = (MapProperties)eventObject.getAttribute(MapProperties.class);
         return new LevelCompleteEvent((String)properties.get("next"));
      }
      if (eventObject.getType().equals("BYTE")) {
         return new ByteCollectedEvent(eventObject);
      }
      return null;
   }

   @Override
   public Object[] identifiers() {
      return new Object[]{"game_over_event", "level_complete_event", "BYTE"};
   }
}
