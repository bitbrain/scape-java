package de.bitbrain.scape.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.input.controller.NavigateableControllerInput;
import de.bitbrain.braingdx.input.keyboard.NavigateableKeyboardInput;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.ui.NavigationMenu;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.level.LevelMetaData;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.Styles;

import static de.bitbrain.scape.GameConfig.*;

public class StageCompleteScreen extends AbstractScreen<ScapeGame> {

   private final boolean stageCompletedForTheFirstTime;
   private boolean exiting = false;
   private GameContext context;
   private PlayerProgress progress;
   private NavigationMenu<TextButton> buttonMenu;

   public StageCompleteScreen(ScapeGame game, PlayerProgress progress) {
      super(game);
      this.progress = progress;
      this.stageCompletedForTheFirstTime = progress.getPointRecord() == 0 && progress.getPoints() > 0;
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

      setupUI(context);
      setupInput(context);
      setupShaders(context);
   }

   private void setupUI(final GameContext context) {

      boolean isMobile = Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;

      Table layout = new Table();
      layout.setFillParent(true);

      final LevelMetaData metaData = progress.getMetadata();

      // 1. Level Caption
      Label captionLevel = new Label(metaData.getName(), Styles.LABEL_INGAME_CAPTION);
      layout.center().add(captionLevel).padBottom(20).row();

      // 2. Complete message
      Label completeLabel = new Label("Stage complete!", Styles.LABEL_INGAME_DESCRIPTION);
      layout.center().add(completeLabel).padBottom(50).row();

      // 2. Collected
      Label collectedBytesLabel = new Label( progress.getPoints() + "/" + metaData.getNumberOfBytes() + " bytes", Styles.LABEL_SELECTION_TOTAL_PROGRESS);
      layout.add(collectedBytesLabel).row();

      NavigationMenu.NavigationMenuStyle style = new NavigationMenu.NavigationMenuStyle();
      style.vertical = false;
      buttonMenu = new NavigationMenu<TextButton>(style);
      buttonMenu.add(new TextButton("Retry", Styles.BUTTON_MENU), new ClickListener() {

         @Override
         public void clicked(InputEvent event, float x, float y) {
            context.getScreenTransitions().out(new IngameScreen(getGame(), metaData), 0.5f);
         }
      }).width(isMobile ? MENU_BUTTON_WIDTH_MOBILE : MENU_BUTTON_WIDTH)
        .height(isMobile ? MENU_BUTTON_HEIGHT_MOBILE : MENU_BUTTON_HEIGHT);
      String text = stageCompletedForTheFirstTime ? "Next Stage" : "Stage selection";
      buttonMenu.add(new TextButton(text, Styles.BUTTON_MENU), new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            context.getScreenTransitions().out(new StageSelectionScreen(getGame(), !stageCompletedForTheFirstTime), 0.5f);
         }
      }).width(isMobile ? MENU_BUTTON_WIDTH_MOBILE : MENU_BUTTON_WIDTH)
            .height(isMobile ? MENU_BUTTON_HEIGHT_MOBILE : MENU_BUTTON_HEIGHT);

      layout.add(buttonMenu).row();
      buttonMenu.next();
      if (stageCompletedForTheFirstTime) {
         buttonMenu.next();
      }


      context.getStage().addActor(layout);
   }

   private void setupInput(GameContext context) {
      context.getInputManager().register(new NavigateableControllerInput(buttonMenu));
      context.getInputManager().register(new NavigateableKeyboardInput(buttonMenu));
   }

   private void setupShaders(GameContext context) {
      AutoReloadPostProcessorEffect<Bloom> bloomEffect = context.getShaderManager().createBloomEffect();
      AutoReloadPostProcessorEffect<Vignette> vignetteEffect = context.getShaderManager().createVignetteEffect();
      bloomEffect.mutate(DEFAULT_BLOOM_CONFIG);
      context.getRenderPipeline().addEffects(RenderPipeIds.UI, vignetteEffect, bloomEffect);
   }
}
