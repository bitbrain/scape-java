package de.bitbrain.scape;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameSettings;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.event.GameEventManagerImpl;
import de.bitbrain.braingdx.graphics.GraphicsSettings;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.graphics.postprocessing.filters.RadialBlur;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.ValueTween;
import de.bitbrain.braingdx.util.ValueProvider;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.i18n.Bundle;
import de.bitbrain.scape.screens.StageSelectionScreen;
import de.bitbrain.scape.screens.LogoScreen;
import de.bitbrain.scape.ui.Styles;
import org.apache.commons.lang.SystemUtils;

public class ScapeGame extends BrainGdxGame {

   private boolean devMode;
   private boolean debugMode;
   private boolean gifMode;

   public ScapeGame(String ... args) {
      for (String arg : args) {
         if ("dev".equalsIgnoreCase(arg)) {
            devMode = true;
         }
         if ("debug".equalsIgnoreCase(arg)) {
            debugMode = true;
         }
         if ("gif".equalsIgnoreCase(arg)) {
            gifMode = true;
         }
      }
   }

   public boolean isDevMode() {
      return devMode;
   }

   public boolean isDebugMode() {
      return debugMode;
   }

   @Override
   protected GameAssetLoader getAssetLoader() {
      return new SmartAssetLoader(Assets.class);
   }

   @Override
   protected AbstractScreen<?, ?> getInitialScreen() {
      Bundle.load();
      Styles.init();
      Tween.registerAccessor(VectorGameCamera.class, new GameCameraTween());
      Tween.registerAccessor(ValueProvider.class, new ValueTween());
      configureSettings();
      if (isDebugMode()) {
         return new StageSelectionScreen(this);
      } else {
         return new LogoScreen(this);
      }
   }

   private void configureSettings() {
      GameSettings settings = new GameSettings(new GameEventManagerImpl());
      GraphicsSettings graphicsSettings = settings.getGraphics();
      if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
         graphicsSettings.setRadialBlurQuality(RadialBlur.Quality.Low);
         Gdx.input.setCatchBackKey(true);
         graphicsSettings.setRenderScale(0.05f);
         graphicsSettings.setParticleMultiplier(0.1f);
         graphicsSettings.save();
      } else if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
         if (gifMode) {
            Gdx.graphics.setWindowedMode(1248, 770);
         } else if (SystemUtils.IS_OS_WINDOWS) {
            Gdx.graphics.setUndecorated(true);
            Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
         } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
         }
         graphicsSettings.setRadialBlurQuality(RadialBlur.Quality.High);
         graphicsSettings.setRenderScale(0.3f);
         graphicsSettings.setParticleMultiplier(0.5f);
         graphicsSettings.save();
      }
   }
}
