package de.bitbrain.scape.level;

import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.scape.i18n.Bundle;

public class LevelMetaDataLoader {

   public LevelMetaData loadFromWorldMapProperties(int number, GameObject object) {
      String translatedName = Bundle.get(object.getAttribute("name", String.class));
      String translatedDescription = Bundle.get(object.getAttribute("description", String.class));
      String path = object.getAttribute("path", String.class);
      return new LevelMetaData(
            number,
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
