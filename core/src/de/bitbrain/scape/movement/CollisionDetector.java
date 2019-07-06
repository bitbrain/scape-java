package de.bitbrain.scape.movement;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.tmx.TiledMapContext;
import de.bitbrain.braingdx.world.GameObject;

public class CollisionDetector {

   private final TiledMapContext context;

   public CollisionDetector(TiledMapContext context) {
      this.context = context;
   }

   public Vector2 getCollisionAbove(GameObject object) {
      boolean collisionBottomLeft = context.isExclusiveCollision(object.getLeft(), object.getTop(), context.layerIndexOf(object), object);
      boolean collisionBottomRight = context.isExclusiveCollision(object.getLeft() + object.getWidth(), object.getTop(), context.layerIndexOf(object), object);
      boolean collisionTopLeft = context.isExclusiveCollision(object.getLeft(), object.getTop() + object.getHeight(), context.layerIndexOf(object), object);
      boolean collisionTopRight = context.isExclusiveCollision(object.getLeft() + object.getWidth() - object.getWidth() / 2f, object.getTop() + object.getHeight(), context.layerIndexOf(object), object);
      if (collisionBottomLeft && collisionTopLeft && !collisionTopRight) {
         return null;
      }
      if (collisionBottomRight && collisionTopRight && !collisionTopLeft) {
         return null;
      }
      if (collisionTopLeft || collisionTopRight) {
         float correction = (float)Math.floor((object.getTop())/ context.getCellHeight()) * context.getCellHeight();
         return new Vector2(object.getLeft(), correction);
      }
      return null;
   }

   public Vector2 getCollisionBelow(GameObject object) {
      boolean collisionBottomLeft = context.isExclusiveCollision(object.getLeft(), object.getTop(), context.layerIndexOf(object), object);
      boolean collisionBottomRight = context.isExclusiveCollision(object.getLeft() + object.getWidth() - object.getWidth() / 2f, object.getTop(), context.layerIndexOf(object), object);
      boolean collisionTopLeft = context.isExclusiveCollision(object.getLeft(), object.getTop() + object.getHeight(), context.layerIndexOf(object), object);
      boolean collisionTopRight = context.isExclusiveCollision(object.getLeft() + object.getWidth(), object.getTop() + object.getHeight(), context.layerIndexOf(object), object);
      if (collisionBottomLeft && collisionTopLeft && !collisionBottomRight) {
         return null;
      }
      if (collisionBottomRight && collisionTopRight && !collisionBottomLeft) {
         return null;
      }
      if (collisionBottomLeft || collisionBottomRight) {
         float correction = (float)Math.floor((object.getTop() + object.getHeight())/ context.getCellHeight()) * context.getCellHeight();
         return new Vector2(object.getLeft(), correction);
      }
      return null;
   }

   public Vector2 getCollisionInFront(GameObject object) {
      boolean collisionBottomLeft = context.isExclusiveCollision(object.getLeft(), object.getTop(), context.layerIndexOf(object), object);
      boolean collisionTopLeft = context.isExclusiveCollision(object.getLeft(), object.getTop() + object.getHeight(), context.layerIndexOf(object), object);
      boolean collisionBottomRight = context.isExclusiveCollision(object.getLeft() + object.getWidth(), object.getTop(), context.layerIndexOf(object), object);
      boolean collisionTopRight = context.isExclusiveCollision(object.getLeft() + object.getWidth(), object.getTop() + object.getHeight(), context.layerIndexOf(object), object);
      if (collisionTopRight && collisionTopLeft && !collisionBottomRight) {
         return null;
      }
      if (collisionBottomRight && collisionBottomLeft && !collisionTopRight) {
         return null;
      }

      if (collisionBottomRight || collisionTopRight) {
         float correction = (float)Math.floor((object.getLeft())/ context.getCellWidth()) * context.getCellWidth();
         if (correction < object.getLastPosition().x && (getCollisionBelow(object) == null || getCollisionAbove(object) == null)) {
            return null;
         }
         return new Vector2(correction, object.getTop());
      }
      return null;
   }

}
