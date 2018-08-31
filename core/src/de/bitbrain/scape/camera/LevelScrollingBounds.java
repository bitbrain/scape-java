package de.bitbrain.scape.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.scape.GameConfig;

public class LevelScrollingBounds implements GameWorld.WorldBounds {

   private float levelProgress;
   private TiledMapAPI tiledMapAPI;
   private final GameCamera gameCamera;

   public LevelScrollingBounds(TiledMapAPI api, GameCamera gameCamera) {
      this.tiledMapAPI = api;
      this.gameCamera = gameCamera;
   }

   public void update(float delta) {
      levelProgress += GameConfig.LEVEL_START_SCROLLING_SPEED * delta;
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
   public boolean isInBounds(GameObject object, OrthographicCamera camera) {
      return true;
   }

   @Override
   public float getWorldWidth() {
      return tiledMapAPI.getWorldWidth() - getLevelProgress();
   }

   @Override
   public float getWorldHeight() {
      return tiledMapAPI.getWorldHeight();
   }

   private float getLevelProgress() {
      return Math.min(levelProgress, tiledMapAPI.getWorldWidth());
   }
}