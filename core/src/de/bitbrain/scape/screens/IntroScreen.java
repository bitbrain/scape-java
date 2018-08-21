package de.bitbrain.scape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.ui.TerminalUI;

import java.util.ArrayList;
import java.util.List;

public class IntroScreen extends AbstractScreen<BrainGdxGame> {

   private GameContext context;

   public IntroScreen(BrainGdxGame game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext context) {
      setBackgroundColor(Colors.BACKGROUND_VIOLET);
      List<String> commands = new ArrayList<String>();
      commands.add("EVIL CORP. SYSTEMS, 1984-2003 ALL RIGHTS RESERVED.\n");
      commands.add("VERSION 1.1\n\n");
      commands.add("> ./scripts/load-scape.sh\n");
      commands.add("Load VIRUS.PK...\n");
      commands.add("Load CORRUPT.PK...\n");
      commands.add("Load TK_019.PK...\n");
      commands.add("Load VOLUME.DAT...\n");
      commands.add("Load EXEC.DAT...\n");
      commands.add("Load SCAPE.EXE...\n");
      commands.add("All sectors loaded!\n");
      commands.add("Preparing corruption of main frame...\n");
      commands.add("20%\n");
      commands.add("40%\n");
      commands.add("60%\n");
      commands.add("80%\n");
      commands.add("100%\n");
      commands.add("COMPLETE!\n");
      commands.add("\n");
      commands.add("> Press any key to continue...\n");
      context.getStage().addActor(new TerminalUI(commands));
      this.context  = context;
      setupShaders(context);
   }

   @Override
   protected void onUpdate(float delta) {
      if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         context.getScreenTransitions().out(new IngameScreen(getGame(), Assets.TiledMaps.LEVEL_1), 1f);
      }
   }

   private void setupShaders(GameContext context) {
      Bloom bloom = new Bloom(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      bloom.setBlurAmount(5f);
      bloom.setBloomIntesity(1.2f);
      bloom.setBlurPasses(50);
      bloom.setThreshold(0.3f);
      context.getRenderPipeline().getPipe(RenderPipeIds.UI).addEffects(bloom);
   }
}
