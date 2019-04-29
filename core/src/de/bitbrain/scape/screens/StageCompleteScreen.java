package de.bitbrain.scape.screens;

import com.badlogic.gdx.input.GestureDetector;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.input.stagecomplete.StageCompleteControllerInputAdapter;
import de.bitbrain.scape.input.stagecomplete.StageCompleteKeyboardInputAdapter;
import de.bitbrain.scape.input.stagecomplete.StageCompleteMobileInputAdapter;
import de.bitbrain.scape.progress.PlayerProgress;

import static de.bitbrain.scape.GameConfig.DEFAULT_BLOOM_CONFIG;

public class StageCompleteScreen extends AbstractScreen<ScapeGame> {

   private final boolean stageCompletedForTheFirstTime;
   private boolean exiting = false;
   private GameContext context;

   public StageCompleteScreen(ScapeGame game, PlayerProgress progress) {
      super(game);
      this.stageCompletedForTheFirstTime = progress.getRecord() == 0 && progress.getPoints() > 0;
      if (stageCompletedForTheFirstTime) {
         progress.increaseMaxLevel();
      }
   }

   public void exit() {
      if (!exiting) {
         context.getScreenTransitions().out(new StageSelectionScreen(getGame(), !stageCompletedForTheFirstTime), 1f);
         exiting = true;
      }
   }

   @Override
   protected void onCreate(GameContext context) {
      this.context = context;
      context.getScreenTransitions().in(0.3f);
      setBackgroundColor(Colors.BACKGROUND_VIOLET);

      setupInput(context);
      setupShaders(context);
   }

   private void setupInput(GameContext context) {
      context.getInputManager().register(new StageCompleteKeyboardInputAdapter(this));
      context.getInputManager().register(new GestureDetector(new StageCompleteMobileInputAdapter(this)));
      context.getInputManager().register(new StageCompleteControllerInputAdapter(this));
   }

   private void setupShaders(GameContext context) {
      AutoReloadPostProcessorEffect<Bloom> bloomEffect = context.getShaderManager().createBloomEffect();
      AutoReloadPostProcessorEffect<Vignette> vignetteEffect = context.getShaderManager().createVignetteEffect();
      bloomEffect.mutate(DEFAULT_BLOOM_CONFIG);
      context.getRenderPipeline().addEffects(RenderPipeIds.UI, vignetteEffect, bloomEffect);
   }
}
