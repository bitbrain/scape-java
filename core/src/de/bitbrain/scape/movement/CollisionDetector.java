package de.bitbrain.scape.movement;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.world.GameObject;

public class CollisionDetector {

   private final GameContext context;

   public CollisionDetector(GameContext context) {
      this.context = context;
   }

   public Vector2 getCollisionAbove(GameObject object) {
      TiledMapAPI api = context.getTiledMapManager().getAPI();
      boolean collisionBottomLeft = api.isExclusiveCollision(object.getLeft(), object.getTop(), api.layerIndexOf(object), object);
      boolean collisionBottomRight = api.isExclusiveCollision(object.getLeft() + object.getWidth(), object.getTop(), api.layerIndexOf(object), object);
      boolean collisionTopLeft = api.isExclusiveCollision(object.getLeft(), object.getTop() + object.getHeight(), api.layerIndexOf(object), object);
      boolean collisionTopRight = api.isExclusiveCollision(object.getLeft() + object.getWidth(), object.getTop() + object.getHeight(), api.layerIndexOf(object), object);
      if (collisionBottomLeft && collisionTopLeft && !collisionTopRight) {
         return null;
      }
      if (collisionBottomRight && collisionTopRight && !collisionTopLeft) {
         return null;
      }
      if (collisionTopLeft) {
         float correction = (float)Math.floor((object.getTop())/ api.getCellHeight()) * api.getCellHeight();
         return new Vector2(object.getLeft(), correction);
      }
      if (collisionTopRight) {
         float correction = (float)Math.floor((object.getTop())/ api.getCellHeight()) * api.getCellHeight();
         return new Vector2(object.getLeft(), correction);
      }
      return null;
   }

   public Vector2 getCollisionBelow(GameObject object) {
      TiledMapAPI api = context.getTiledMapManager().getAPI();
      boolean collisionBottomLeft = api.isExclusiveCollision(object.getLeft(), object.getTop(), api.layerIndexOf(object), object);
      boolean collisionBottomRight = api.isExclusiveCollision(object.getLeft() + object.getWidth(), object.getTop(), api.layerIndexOf(object), object);
      boolean collisionTopLeft = api.isExclusiveCollision(object.getLeft(), object.getTop() + object.getHeight(), api.layerIndexOf(object), object);
      boolean collisionTopRight = api.isExclusiveCollision(object.getLeft() + object.getWidth(), object.getTop() + object.getHeight(), api.layerIndexOf(object), object);
      if (collisionBottomLeft && collisionTopLeft && !collisionBottomRight) {
         return null;
      }
      if (collisionBottomRight && collisionTopRight && !collisionBottomLeft) {
         return null;
      }
      if (collisionBottomLeft) {
         float correction = (float)Math.floor((object.getTop() + object.getHeight())/ api.getCellHeight()) * api.getCellHeight();
         return new Vector2(object.getLeft(), correction);
      }
      if (collisionBottomRight) {
         float correction = (float)Math.floor((object.getTop() + object.getHeight())/ api.getCellHeight()) * api.getCellHeight();
         return new Vector2(object.getLeft(), correction);
      }
      return null;
   }

   public Vector2 getCollisionInFront(GameObject object) {
      TiledMapAPI api = context.getTiledMapManager().getAPI();
      boolean collisionBottomLeft = api.isExclusiveCollision(object.getLeft(), object.getTop(), api.layerIndexOf(object), object);
      boolean collisionTopLeft = api.isExclusiveCollision(object.getLeft(), object.getTop() + object.getHeight(), api.layerIndexOf(object), object);
      boolean collisionBottomRight = api.isExclusiveCollision(object.getLeft() + object.getWidth(), object.getTop(), api.layerIndexOf(object), object);
      boolean collisionTopRight = api.isExclusiveCollision(object.getLeft() + object.getWidth(), object.getTop() + object.getHeight(), api.layerIndexOf(object), object);
      if (collisionTopRight && collisionTopLeft && !collisionBottomRight) {
         return null;
      }
      if (collisionBottomRight && collisionBottomLeft && !collisionTopRight) {
         return null;
      }

      if (collisionBottomRight || collisionTopRight) {
         float correction = (float)Math.floor((object.getLeft())/ api.getCellWidth()) * api.getCellWidth();
         return new Vector2(correction, object.getTop());
      }
      return null;
   }

}
