package de.bitbrain.scape;

import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.ui.Styles;

public class ScapeGame extends BrainGdxGame {

   @Override
   protected GameAssetLoader getAssetLoader() {
      return new SmartAssetLoader(Assets.class);
   }

   @Override
   protected AbstractScreen<?> getInitialScreen() {
      Styles.init();
      //Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
      //Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
      Gdx.graphics.setWindowedMode(1280, 1020);
      return new IngameScreen(this, Assets.TiledMaps.LEVEL_1);
   }
}
