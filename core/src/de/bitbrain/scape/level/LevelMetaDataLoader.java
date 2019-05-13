package de.bitbrain.scape.level;

import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.scape.i18n.Bundle;

public class LevelMetaDataLoader {

   public LevelMetaData loadFromWorldMapProperties(MapProperties worldMapProperties) {
      int level = (Integer)worldMapProperties.get("level");
      String translatedName = Bundle.get((String)worldMapProperties.get("name"));
      String translatedDescription = Bundle.get((String)worldMapProperties.get("description"));
      String path = (String)worldMapProperties.get("path");
      return new LevelMetaData(
            level,
            path,
            translatedName,
            translatedDescription,
            getNumberOfBytesInLevel(path)
      );
   }

   private int getNumberOfBytesInLevel(String path) {
      TiledMap map = SharedAssetManager.getInstance().get(path, TiledMap.class);
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
