package de.bitbrain.scape.event;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.event.GameEventFactory;
import de.bitbrain.braingdx.world.GameObject;

public class ScopeEventFactory implements GameEventFactory {

   @Override
   public GameEvent create(GameObject eventObject, GameObject producerObject) {
      if (eventObject.getType().equals("game_over_event")) {
         return new GameOverEvent();
      }
      if (eventObject.getType().equals("POWERCELL")) {
         MapProperties properties = (MapProperties)eventObject.getAttribute(MapProperties.class);
         Vector2 position = new Vector2(
               eventObject.getLeft() + eventObject.getWidth() / 2f,
               eventObject.getTop() + eventObject.getHeight() / 2f
         );
         return new LevelCompleteEvent((String)properties.get("next"), position);
      }
      if (eventObject.getType().equals("BYTE")) {
         return new ByteCollectedEvent(eventObject);
      }
      return null;
   }

   @Override
   public Object[] identifiers() {
      return new Object[]{"game_over_event", "POWERCELL", "BYTE"};
   }
}
