package de.bitbrain.scape.movement;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.model.Direction;

public class PlayerAdjustment {

   public static void adjust(GameObject player, GameContext context) {
      Direction direction = (Direction)player.getAttribute(Direction.class);
      TiledMapAPI api = context.getTiledMapManager().getAPI();

   }
}
