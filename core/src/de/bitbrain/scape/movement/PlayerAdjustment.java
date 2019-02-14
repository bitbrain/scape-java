package de.bitbrain.scape.movement;

import aurelienribon.tweenengine.Tween;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.model.Direction;

import static de.bitbrain.braingdx.tmx.IndexCalculator.calculateIndex;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class PlayerAdjustment {

   public static void adjust(GameObject player, GameContext context) {
      float targetScaleY = getTargetScale(player, context);
      SharedTweenManager.getInstance().killTarget(player, GameObjectTween.SCALE);
      SharedTweenManager.getInstance().killTarget(player, GameObjectTween.SCALE_X);
      SharedTweenManager.getInstance().killTarget(player, GameObjectTween.SCALE_Y);
      player.setScale(0f);
      Tween.to(player, GameObjectTween.SCALE_Y, 0.3f)
            .target(targetScaleY)
            .start(SharedTweenManager.getInstance());
      Tween.to(player, GameObjectTween.SCALE_X, 0.3f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
   }

   private static float getTargetScale(GameObject player, GameContext context) {
      TiledMapAPI api = context.getTiledMapManager().getAPI();

      int indexX = (int) (calculateIndex(
            player.getLeft(), api.getCellWidth()) * api.getCellWidth());

      player.setAttribute(Direction.class, Direction.UP);

      player.setPosition(
            indexX,
            calculateIndex(
                  player.getTop(), api.getCellHeight()) * api.getCellHeight());

      int count = 0;
      for (int indexY = calculateIndex(player.getTop(), api.getCellWidth());
           indexY >= 0; indexY--) {
         count--;
         if (api.isExclusiveCollision(indexX, indexY, api.layerIndexOf(player), player)) {
            break;
         }
      }
      for (int indexY = calculateIndex(player.getTop(), api.getCellWidth());
           indexY < api.getNumberOfRows(); indexY++) {
         count++;
         if (api.isExclusiveCollision(indexX, indexY, api.layerIndexOf(player), player)) {
            break;
         }
      }
      if (count > 0) {
         player.setAttribute(Direction.class, Direction.DOWN);
         player.setOrigin(player.getWidth() / 2f, 0f);
         player.setOffset(0f, 0f);
         return 1;
      } else {
         player.setAttribute(Direction.class, Direction.UP);
         player.setOrigin(player.getWidth() / 2f, 0f);
         player.setOffset(0f, player.getHeight());
         return -1;
      }
   }
}
