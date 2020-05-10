package de.bitbrain.scape.level;

import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.i18n.Bundle;

public class LevelMetaDataLoader {

   public LevelMetaData loadFromWorldMapProperties(int number, GameObject object) {
      String translatedName = Bundle.get(object.getAttribute("name", String.class));
      String translatedDescription = Bundle.get(object.getAttribute("description", String.class));
      String path = object.getAttribute("path", String.class);
      TiledMap map = Asset.get(path, TiledMap.class);
      return new LevelMetaData(
            number,
            path,
            translatedName,
            translatedDescription,
            getNumberOfBytesInLevel(map),
            getBaseScrollingSpeed(map),
            getPlayerSpeed(map),
            getPlayerSpeedIncrease(map),
            getBackgroundMusicPath(map));
   }

   private float getBaseScrollingSpeed(TiledMap map) {
      return map.getProperties().get("baseSpeed", GameConfig.LEVEL_START_DEFAULT_SCROLLING_SPEED, Float.class);
   }

   private float getPlayerSpeed(TiledMap map) {
      return map.getProperties().get("playerSpeed", GameConfig.PLAYER_START_DEFAULT_SPEED, Float.class);
   }

   private float getPlayerSpeedIncrease(TiledMap map) {
      return map.getProperties().get("playerSpeedIncrease", GameConfig.PLAYER_SPEED_DEFAULT_INCREASE, Float.class);
   }

   private String getBackgroundMusicPath(TiledMap map) {
      return map.getProperties().get("music", null, String.class);
   }

   private int getNumberOfBytesInLevel(TiledMap map) {
      MapLayers mapLayers = map.getLayers();
      int totalNumberOfBytes = 0;
      for (int i = 0; i < mapLayers.getCount(); ++i) {
         MapLayer mapLayer = mapLayers.get(i);
         // Object layer
         if (!(mapLayer instanceof TiledMapTileLayer)) {
            MapObjects mapObjects = mapLayer.getObjects();
            for (int objectIndex = 0; objectIndex < mapObjects.getCount(); ++objectIndex) {
               MapObject mapObject = mapObjects.get(objectIndex);
               MapProperties objectProperties = mapObject.getProperties();
               if (objectProperties.get("type").equals("BYTE")) {
                  totalNumberOfBytes++;
               }
            }
         }
      }
      return totalNumberOfBytes;
   }
}
