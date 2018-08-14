package de.bitbrain.scape.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.scape.GameConfig;

public class LevelScrollingBounds implements GameWorld.WorldBounds {

   private float levelProgress;
   private TiledMapAPI tiledMapAPI;

   public LevelScrollingBounds(TiledMapAPI api) {
      this.tiledMapAPI = api;
   }

   public void update(float delta) {
      levelProgress += GameConfig.LEVEL_START_SCROLLING_SPEED * delta;
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