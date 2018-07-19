package de.bitbrain.scape.movement;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.tmx.IndexCalculator;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.model.Direction;

public class PlayerAdjustment {

   public static void adjust(GameObject player, GameContext context) {
      TiledMapAPI api = context.getTiledMapManager().getAPI();

      player.setPosition(
            IndexCalculator.calculateIndex(
            player.getLeft(), api.getCellWidth()) * api.getCellWidth(),
            IndexCalculator.calculateIndex(
            player.getTop(), api.getCellHeight()) * api.getCellHeight());
   }
}
