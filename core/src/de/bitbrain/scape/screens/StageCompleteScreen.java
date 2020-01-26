package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.input.controller.NavigateableControllerInput;
import de.bitbrain.braingdx.input.keyboard.NavigateableKeyboardInput;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.ValueTween;
import de.bitbrain.braingdx.ui.NavigationMenu;
import de.bitbrain.braingdx.util.ValueProvider;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.level.LevelMetaData;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.Styles;
import de.bitbrain.scape.ui.ingame.CurrentTimeLabel;

import static de.bitbrain.scape.GameConfig.*;

public class StageCompleteScreen extends BrainGdxScreen2D<ScapeGame> {

   private final boolean stageCompletedForTheFirstTime;
   private final boolean newPointRecord;
   private final boolean newTimeRecord;
   private boolean exiting = false;
   private GameContext2D context;
   private PlayerProgress progress;
   private NavigationMenu<TextButton> buttonMenu;

   public StageCompleteScreen(ScapeGame game, PlayerProgress progress) {
      super(game);
      this.progress = progress;
      this.stageCompletedForTheFirstTime = progress.getTimeRecord() == 0;
      if (stageCompletedForTheFirstTime) {
         progress.increaseMaxLevel();
         newPointRecord = false;
         newTimeRecord = false;
      } else {
         newPointRecord = progress.getPoints() > progress.getPointRecord();
         newTimeRecord = progress.getCurrentTime() < progress.getTimeRecord();
      }
      progress.save();
   }

   public void exit() {
      if (!exiting) {
         context.getScreenTransitions().out(new StageSelectionScreen(getGame(), !stageCompletedForTheFirstTime), 1f);
         exiting = true;
      }
   }

   @Override
   protected void onCreate(GameContext2D context) {
      this.context = context;
      context.getScreenTransitions().in(0.3f);
      context.setBackgroundColor(Colors.BACKGROUND_VIOLET);

      setupUI(context);
      setupInput(context);
      if (Gdx.app.getType() != Application.ApplicationType.Android && Gdx.app.getType() != Application.ApplicationType.iOS) {
         setupShaders(context);
      }
   }

   private void setupUI(final GameContext2D context) {

      boolean isMobile = Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;

      Table layout = new Table();
      layout.setFillParent(true);

      final LevelMetaData metaData = progress.getMetadata();

      // 1. Complete message
      Label completeLabel = new Label("Stage complete:", Styles.LABEL_SELECTION_PROGRESS_DESCRIPTION);
      layout.center().add(completeLabel).padBottom(10).row();
      // 2. Level Caption
      Label captionLevel = new Label(metaData.getName(), Styles.LABEL_INGAME_CAPTION);
      layout.center().add(captionLevel).padBottom(50).row();

      // 3. Collected
      final Label byteDescription = new Label("bytes", Styles.LABEL_INGAME_DESCRIPTION);
      layout.add(byteDescription).row();
      Label collectedBytesLabel = new Label( progress.getPoints() + " out of " + metaData.getNumberOfBytes() + "", Styles.LABEL_SELECTION_TOTAL_PROGRESS);
      layout.add(collectedBytesLabel).row();

      float blinkDuration = 1;
      final Label pointRecordLabel = new Label("new record!", Styles.LABEL_SELECTION_PROGRESS_DESCRIPTION);
      if (newPointRecord) {
         Tween.to(pointRecordLabel, ActorTween.ALPHA, blinkDuration / 3)
               .setCallback(new TweenCallback() {
                  @Override
                  public void onEvent(int type, BaseTween<?> source) {
                     Tween.to(pointRecordLabel, ActorTween.ALPHA, 0.5f)
                           .target(0.5f)
                           .repeatYoyo(Tween.INFINITY, 0f)
                           .start(SharedTweenManager.getInstance());
                  }
               })
               .setCallbackTriggers(TweenCallback.COMPLETE)
               .target(1f)
               .start(SharedTweenManager.getInstance());
      } else {
         pointRecordLabel.getColor().a = 0f;
      }
      layout.add(pointRecordLabel).padBottom(60).row();

      // 4. Current time
      final Label timeDescription = new Label("time", Styles.LABEL_INGAME_DESCRIPTION);
      layout.add(timeDescription).row();
      ValueProvider valueProvider = new ValueProvider();
      Label currentTime = new CurrentTimeLabel(valueProvider, Styles.LABEL_SELECTION_TOTAL_PROGRESS);
      layout.add(currentTime).row();
      // Animate time
      Tween.to(valueProvider, ValueTween.VALUE, 2f)
            .delay(1f)
            .target(progress.getCurrentTime())
            .start(SharedTweenManager.getInstance());

      final Label timeRecordLabel = new Label("new record!", Styles.LABEL_SELECTION_PROGRESS_DESCRIPTION);
      layout.add(timeRecordLabel).padBottom(60).row();
      if (newTimeRecord) {
         Tween.to(timeRecordLabel, ActorTween.ALPHA, blinkDuration / 3)
               .setCallback(new TweenCallback() {
                  @Override
                  public void onEvent(int type, BaseTween<?> source) {
                     Tween.to(timeRecordLabel, ActorTween.ALPHA, 0.5f)
                           .target(0.5f)
                           .repeatYoyo(Tween.INFINITY, 0f)
                           .start(SharedTweenManager.getInstance());
                  }
               })
               .setCallbackTriggers(TweenCallback.COMPLETE)
               .target(1f)
               .start(SharedTweenManager.getInstance());
      } else {
         timeRecordLabel.getColor().a = 0f;
      }

      NavigationMenu.NavigationMenuStyle style = new NavigationMenu.NavigationMenuStyle();
      style.vertical = false;
      style.padding = 100;
      style.hoverSound = SharedAssetManager.getInstance().get(Assets.Sounds.SELECT, Sound.class);
      style.enterSound = SharedAssetManager.getInstance().get(Assets.Sounds.SUBMIT, Sound.class);
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

      layout.add(buttonMenu).padTop(40).row();
      buttonMenu.next();
      if (stageCompletedForTheFirstTime) {
         buttonMenu.next();
      }


      context.getStage().addActor(layout);
   }

   private void setupInput(GameContext2D context) {
      context.getInputManager().register(new NavigateableControllerInput(buttonMenu));
      context.getInputManager().register(new NavigateableKeyboardInput(buttonMenu));
   }

   private void setupShaders(GameContext2D context) {
      AutoReloadPostProcessorEffect<Bloom> bloomEffect = context.getShaderManager().createBloomEffect();
      AutoReloadPostProcessorEffect<Vignette> vignetteEffect = context.getShaderManager().createVignetteEffect();
      bloomEffect.mutate(DEFAULT_BLOOM_CONFIG);
      context.getRenderPipeline().addEffects(RenderPipeIds.UI, vignetteEffect, bloomEffect);
   }
}
