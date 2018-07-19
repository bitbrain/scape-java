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
      boolean collisionLeft = api.isCollision(object.getLeft(), object.getTop() + object.getHeight(), api.layerIndexOf(object), object);
      boolean collisionRight = api.isCollision(object.getLeft() + object.getWidth(), object.getTop() + object.getHeight(), api.layerIndexOf(object), object);
      if (collisionLeft) {
         System.out.println("aboveLeft at " + (object.getLeft()) + "," + (object.getTop() + object.getHeight()));
         float correction = (float)Math.floor((object.getTop())/ api.getCellWidth()) * api.getCellWidth();
         return new Vector2(object.getLeft(), correction);
      }
      if (collisionRight) {
         System.out.println("aboveRight at " + (object.getLeft() + object.getWidth()) + "," + (object.getTop() + object.getHeight()));
         float correction = (float)Math.floor((object.getTop())/ api.getCellWidth()) * api.getCellWidth();
         return new Vector2(object.getLeft(), correction);
      }
      return null;
   }

   public Vector2 getCollisionBelow(GameObject object) {
      TiledMapAPI api = context.getTiledMapManager().getAPI();
      boolean collisionLeft = api.isCollision(object.getLeft(), object.getTop(), api.layerIndexOf(object), object);
      boolean collisionRight = api.isCollision(object.getLeft() + object.getWidth(), object.getTop(), api.layerIndexOf(object), object);
      if (collisionLeft) {
         System.out.println("belowLeft at " + (object.getLeft()) + "," + (object.getTop()));
         float correction = (float)Math.floor((object.getTop() + object.getHeight())/ api.getCellHeight()) * api.getCellHeight();
         return new Vector2(object.getLeft(), correction);
      }
      if (collisionRight) {
         System.out.println("belowRight at " + (object.getLeft() + object.getWidth()) + "," + (object.getTop()));
         float correction = (float)Math.floor((object.getTop() + object.getHeight())/ api.getCellHeight()) * api.getCellHeight();
         return new Vector2(object.getLeft(), correction);
      }
      return null;
   }

   public Vector2 getCollisionInFront(GameObject object) {
      TiledMapAPI api = context.getTiledMapManager().getAPI();
      boolean collisionTop = api.isCollision(object.getLeft() + object.getWidth(), object.getTop(), api.layerIndexOf(object), object);
      boolean collisionBottom = api.isCollision(object.getLeft() + object.getWidth(), object.getTop(), api.layerIndexOf(object), object);

      if (collisionTop) {
         System.out.println("inFrontTop at " + (object.getLeft() + object.getWidth()) + "," + (object.getTop() + object.getHeight()));
         float correction = (float)Math.floor((object.getLeft() - object.getWidth())/ api.getCellWidth()) * api.getCellWidth();
         return new Vector2(correction, object.getTop());
      }
      if (collisionBottom) {
         System.out.println("inFrontBottom at " + (object.getLeft() + object.getWidth()) + "," + (object.getTop()));
         float correction = (float)Math.floor((object.getLeft() - object.getWidth())/ api.getCellWidth()) * api.getCellWidth();
         return new Vector2(correction, object.getTop());
      }
      return null;
   }

}
