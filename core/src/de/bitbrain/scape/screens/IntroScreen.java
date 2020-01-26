package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Zoomer;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.screens.ColorTransition;
import de.bitbrain.braingdx.tweens.BloomShaderTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.input.intro.IntroControllerInputAdapter;
import de.bitbrain.scape.input.intro.IntroKeyboardInputAdapter;
import de.bitbrain.scape.input.intro.IntroMobileInputAdapter;
import de.bitbrain.scape.ui.effects.TextGlitchRandomizer;
import de.bitbrain.scape.ui.intro.TerminalUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IntroScreen extends BrainGdxScreen2D<ScapeGame> {

   private GameContext2D context;
   private List<String> commands;

   private boolean bootSequence = false;
   private boolean exiting = false;

   private TextGlitchRandomizer randomizer;
   private TerminalUI ui;
   private AutoReloadPostProcessorEffect<Bloom> bloom;
   private AutoReloadPostProcessorEffect<Zoomer> zoomer;

   private boolean proceedWithBootSequence = false;

   public IntroScreen(ScapeGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext2D context) {
      context.getScreenTransitions().in(1.3f);
      SharedAssetManager.getInstance().get(Assets.Musics.INTRO, Music.class).play();
      context.setBackgroundColor(Colors.BACKGROUND_VIOLET);
      commands = loadIntroCommands();
      this.context  = context;
      ui = new TerminalUI(commands);
      context.getStage().addActor(ui);
      randomizer = new TextGlitchRandomizer(ui);
      setupInput(context);
      if (Gdx.app.getType() != Application.ApplicationType.Android && Gdx.app.getType() != Application.ApplicationType.iOS) {
         setupShaders(context);
      }
   }

   @Override
   public void dispose() {
      super.dispose();
   }

   @Override
   public Viewport getViewport(int width, int height, Camera camera) {
      return new ScreenViewport();
   }

   public void exit() {
      if (!exiting) {
         SharedAssetManager.getInstance().get(Assets.Musics.INTRO, Music.class).stop();
         SharedAssetManager.getInstance().get(Assets.Sounds.STARTUP, Sound.class).stop();
         SharedAssetManager.getInstance().get(Assets.Musics.COMPUTER_NOISE, Music.class).stop();
         exiting = true;
         context.getScreenTransitions().out(new StageSelectionScreen(getGame(), true), 1f);
      }
   }

   public void proceed() {
      if (commands.isEmpty()) {
         proceedWithBootSequence = true;
      }
   }

   @Override
   protected void onUpdate(float delta) {
      if (!bootSequence && commands != null && commands.isEmpty() && proceedWithBootSequence) {
         SharedAssetManager.getInstance().get(Assets.Sounds.ENTER, Sound.class).play();
         SharedAssetManager.getInstance().get(Assets.Musics.COMPUTER_NOISE, Music.class).stop();
         bootSequence = true;
         ui.setPaused(true);
         randomizer.start();
         if (bloom != null) {
            bloom.mutate(new Mutator<Bloom>() {
               @Override
               public void mutate(Bloom target) {
                  Tween.to(target, BloomShaderTween.BLOOM_INTENSITY, GameConfig.BOOT_SEQUENCE_DURATION * 2)
                        .target(20f)
                        .start(SharedTweenManager.getInstance());
                  Tween.to(target, BloomShaderTween.BASE_INTENSITY, GameConfig.BOOT_SEQUENCE_DURATION * 2)
                        .target(0.3f)
                        .start(SharedTweenManager.getInstance());
               }
            });
         }
         context.getScreenTransitions().out(
               new ColorTransition(Colors.PRIMARY_BLUE),
               new StageSelectionScreen(getGame(), true),
               GameConfig.BOOT_SEQUENCE_DURATION
         );
         if (zoomer != null) {
            zoomer.mutate(GameConfig.EXIT_ZOOMER_CONFIG);
         }
         exiting = true;
      }
      if (bootSequence) {
         randomizer.update(delta);
      }
   }

   private void setupInput(GameContext2D context) {
      GestureDetector gestureDetector = new GestureDetector(new IntroMobileInputAdapter(this));
      context.getInputManager().register(gestureDetector);
      context.getInputManager().register(new IntroKeyboardInputAdapter(this));
      context.getInputManager().register(new IntroControllerInputAdapter(this));
   }

   private void setupShaders(GameContext2D context) {
      bloom = context.getShaderManager().createBloomEffect();
      bloom.mutate(GameConfig.DEFAULT_BLOOM_CONFIG);
      zoomer = context.getShaderManager().createZoomerEffect();
      zoomer.mutate(new Mutator<Zoomer>() {
         @Override
         public void mutate(Zoomer target) {
            target.setZoom(1.1f);
            target.setBlurStrength(0f);
         }
      });
      context.getRenderPipeline().addEffects(RenderPipeIds.UI, bloom, zoomer);
   }

   private List<String> loadIntroCommands() {
      List<String> commands = new ArrayList<String>();
      InputStream stream = null;
      try {
         stream = Gdx.files.internal("intro.txt").read();
         BufferedReader r = new BufferedReader(new InputStreamReader(stream));
         String line;
         while ((line = r.readLine()) != null) {
            commands.add(line + "\n");
         }
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      return commands;
   }
}
