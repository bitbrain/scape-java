package de.bitbrain.scape.googleplay;

import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class PlayEventManager {

   private final IGameServiceClient client;

   public PlayEventManager(IGameServiceClient client) {
      this.client = client;
   }

   public void submitEvent(Events event) {
      client.submitEvent(event.getGooglePlayId(), 1);
   }
}
