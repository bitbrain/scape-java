package de.bitbrain.scape.camera;

import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.tmx.TiledMapContext;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.scape.GameConfig;

public class LevelScrollingBounds implements GameWorld.WorldBounds {

   private float levelProgress;
   private TiledMapContext context;
   private final GameCamera gameCamera;
   private final float scrollingSpeed;

   public LevelScrollingBounds(float scrollingSpeed, TiledMapContext context, GameCamera gameCamera) {
      this.scrollingSpeed = scrollingSpeed;
      this.context = context;
      this.gameCamera = gameCamera;
   }

   public void update(float delta) {
      levelProgress += scrollingSpeed * delta;
      float cameraLeft = gameCamera.getInternalCamera().position.x - gameCamera.getScaledCameraWidth() / 2f;
      if (cameraLeft > levelProgress) {
         levelProgress = cameraLeft;
      }
   }

   public void reset() {
      levelProgress = 0f;
   }

   @Override
   public float getWorldOffsetX() {
      return levelProgress;
   }

   @Override
   public float getWorldOffsetY() {
      return 0;
   }

   @Override
   public boolean isInBounds(GameObject object) {
      return true;
   }

   @Override
   public float getWorldWidth() {
      return context.getWorldWidth() - getLevelProgress();
   }

   @Override
   public float getWorldHeight() {
      return context.getWorldHeight();
   }

   private float getLevelProgress() {
      return Math.min(levelProgress, context.getWorldWidth());
   }
}