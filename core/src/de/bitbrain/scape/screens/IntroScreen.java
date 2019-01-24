package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Zoomer;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.screens.ColorTransition;
import de.bitbrain.braingdx.tweens.BloomShaderTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.GameConfig;
import de.bitbrain.scape.ui.intro.TerminalUI;
import de.bitbrain.scape.ui.effects.TextGlitchRandomizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IntroScreen extends AbstractScreen<BrainGdxGame> {

   private GameContext context;
   private List<String> commands;

   private boolean bootSequence = false;
   private boolean exiting = false;

   private TextGlitchRandomizer randomizer;
   private TerminalUI ui;
   private AutoReloadPostProcessorEffect<Bloom> bloom;
   private AutoReloadPostProcessorEffect<Zoomer> zoomer;

   public IntroScreen(BrainGdxGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext context) {
      setBackgroundColor(Colors.BACKGROUND_VIOLET);
      commands = loadIntroCommands();
      this.context  = context;
      ui = new TerminalUI(commands);
      context.getStage().addActor(ui);
      randomizer = new TextGlitchRandomizer(ui);
      setupShaders(context);
   }

   @Override
   protected Viewport getViewport(int width, int height, Camera camera) {
      return new ExtendViewport(width, height, camera);
   }

   @Override
   protected void onUpdate(float delta) {
      if (!exiting && Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         exiting = true;
         context.getScreenTransitions().out(new LevelSelectionScreen(getGame(), true), 1f);
      } else if (!bootSequence && commands != null && commands.isEmpty()
            && (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched())) {
         bootSequence = true;
         ui.setPaused(true);
         randomizer.start();
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
         context.getScreenTransitions().out(
               new ColorTransition(Colors.PRIMARY_BLUE),
               new LevelSelectionScreen(getGame(), true),
               GameConfig.BOOT_SEQUENCE_DURATION
         );
         zoomer.mutate(GameConfig.EXIT_ZOOMER_CONFIG);
         exiting = true;
      } else if (!exiting && Gdx.input.isTouched()) {
         exiting = true;
         context.getScreenTransitions().out(new LevelSelectionScreen(getGame(), true), 1f);
      }
      if (bootSequence) {
         randomizer.update(delta);
      }
   }

   private void setupShaders(GameContext context) {
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
      context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(bloom, zoomer);
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
