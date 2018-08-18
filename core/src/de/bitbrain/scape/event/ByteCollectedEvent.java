package de.bitbrain.scape.event;

import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.world.GameObject;

public class ByteCollectedEvent implements GameEvent {

   private final GameObject byteObject;

   public ByteCollectedEvent(GameObject byteObject) {
      this.byteObject = byteObject;
   }

   public GameObject getByteObject() {
      return byteObject;
   }
}
