package de.bitbrain.scape.movement;

import aurelienribon.tweenengine.Tween;
import de.bitbrain.braingdx.tmx.TiledMapContext;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.model.Direction;

public class PlayerAdjustment {

   public static void adjust(GameObject player, TiledMapContext context) {
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

   private static float getTargetScale(GameObject player, TiledMapContext context) {
      int indexX = (int) (context.getPositionTranslator().toIndexX(player.getLeft()) * context.getCellWidth());

      player.setAttribute(Direction.class, Direction.UP);

      player.setPosition(
            indexX,
            context.getPositionTranslator().toIndexY(
                  player.getTop()) * context.getCellHeight());

      int count = 0;
      for (int indexY = context.getPositionTranslator().toIndexY(player.getTop());
           indexY >= 0; indexY--) {
         count--;
         if (context.isExclusiveCollision(indexX, indexY, context.layerIndexOf(player), player)) {
            break;
         }
      }
      for (int indexY = context.getPositionTranslator().toIndexY(player.getTop());
           indexY < context.getNumberOfRows(); indexY++) {
         count++;
         if (context.isExclusiveCollision(indexX, indexY, context.layerIndexOf(player), player)) {
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
