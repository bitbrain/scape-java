package de.bitbrain.scape.gsv;

import de.golfgl.gdxgamesvcs.IGameServiceClient;

public interface GameServiceFactory {
   IGameServiceClient create() throws Exception;
}
