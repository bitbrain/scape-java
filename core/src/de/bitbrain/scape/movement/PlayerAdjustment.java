package de.bitbrain.scape.movement;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.tmx.IndexCalculator;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.model.Direction;

import static de.bitbrain.braingdx.tmx.IndexCalculator.calculateIndex;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class PlayerAdjustment {

   public static void adjust(GameObject player, GameContext context) {
      TiledMapAPI api = context.getTiledMapManager().getAPI();

      int indexX = (int) (calculateIndex(
                  player.getLeft(), api.getCellWidth()) * api.getCellWidth());

      player.setAttribute(Direction.class, Direction.UP);

      player.setPosition(
            indexX,
            calculateIndex(
            player.getTop(), api.getCellHeight()) * api.getCellHeight());

      for (int indexY = calculateIndex(player.getLeft(), api.getCellWidth());
           indexY >= 0; indexY--) {
         if (api.isExclusiveCollision(indexX, indexY, api.layerIndexOf(player), player)) {
            player.setAttribute(Direction.class, Direction.DOWN);
            player.getScale().y = max(-player.getScale().y, player.getScale().y);
            break;
         }
      }
      for (int indexY = calculateIndex(player.getLeft(), api.getCellWidth());
           indexY < api.getNumberOfRows(); indexY++) {
         if (api.isExclusiveCollision(indexX, indexY, api.layerIndexOf(player), player)) {
            player.setAttribute(Direction.class, Direction.UP);
            player.getScale().y = min(-player.getScale().y, player.getScale().y);
            break;
         }
      }
   }
}
