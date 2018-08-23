package de.bitbrain.scape;

import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.screens.IntroScreen;
import de.bitbrain.scape.screens.LevelSelectionScreen;
import de.bitbrain.scape.ui.Styles;

public class ScapeGame extends BrainGdxGame {

   @Override
   protected GameAssetLoader getAssetLoader() {
      return new SmartAssetLoader(Assets.class);
   }

   @Override
   protected AbstractScreen<?> getInitialScreen() {
      Styles.init();
      Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
      Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
      return new LevelSelectionScreen(this);
      //return new IntroScreen(this);
   }
}
