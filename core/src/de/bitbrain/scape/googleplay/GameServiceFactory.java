package de.bitbrain.scape.googleplay;

import de.golfgl.gdxgamesvcs.IGameServiceClient;

public interface GameServiceFactory {
   IGameServiceClient create() throws Exception;
}
