package de.bitbrain.scape.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.postprocessing.AutoReloadPostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.input.controller.NavigateableControllerInput;
import de.bitbrain.braingdx.input.keyboard.NavigateableKeyboardInput;
import de.bitbrain.braingdx.screen.AbstractBrainGdxScreen2D;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.ui.NavigationMenu;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.googleplay.Leaderboards;
import de.bitbrain.scape.level.LevelMetaData;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.Styles;
import de.golfgl.gdxgamesvcs.GameServiceException;

import static de.bitbrain.braingdx.ui.NavigationMenu.NavigationMenuStyle.Alignment.HORIZONTAL;
import static de.bitbrain.scape.GameConfig.DEFAULT_BLOOM_CONFIG;
import static de.bitbrain.scape.i18n.Bundle.get;
import static de.bitbrain.scape.i18n.Messages.*;
import static de.bitbrain.scape.ui.Styles.MENU_STYLE;
import static de.bitbrain.scape.ui.UiFactory.addMenuButton;
import static de.bitbrain.scape.ui.UiFactory.createAnimatedLogo;
import static java.lang.String.format;

public class StageCompleteScreen extends AbstractBrainGdxScreen2D<ScapeGame, GameContext2D> {

   private final boolean stageCompletedForTheFirstTime;
   private final boolean newPointRecord;
   private final boolean newTimeRecord;
   private boolean exiting = false;
   private GameContext2D context;
   private PlayerProgress progress;
   private NavigationMenu<Button> buttonMenu;

   public StageCompleteScreen(ScapeGame game, PlayerProgress progress) {
      super(game);
      this.progress = progress;
      this.stageCompletedForTheFirstTime = progress.getTimeRecord() == 0;
      if (stageCompletedForTheFirstTime) {
         progress.setMaxLevel(progress.getMetadata().getLevelNumber() + 1);
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
      Table layout = new Table();
      layout.setFillParent(true);

      final LevelMetaData metaData = progress.getMetadata();

      // 1. Complete message
      Label completeLabel = new Label(get(MENU_STAGE_COMPLETE), Styles.LABEL_SELECTION_PROGRESS_DESCRIPTION);
      completeLabel.setFontScale(3f);
      layout.center().add(completeLabel).padBottom(10).row();
      // 2. Level Caption
      Actor captionLevel = createAnimatedLogo(metaData.getName(), Styles.LABEL_INGAME_CAPTION, context.getTweenManager());
      layout.center().add(captionLevel).padBottom(50).row();

      // 3. Collected
      final Label byteDescription = new Label(get(MENU_STAGE_COMPLETE_BYTES), Styles.LABEL_INGAME_DESCRIPTION);
      layout.add(byteDescription).row();
      Actor collectedBytesLabel = createAnimatedLogo(progress.getPoints() + " out of " + metaData.getNumberOfBytes(), Styles.LABEL_SELECTION_TOTAL_PROGRESS, context.getTweenManager());
      layout.add(collectedBytesLabel).row();

      float blinkDuration = 1;
      final Label pointRecordLabel = new Label(get(MENU_STAGE_COMPLETE_RECORD), Styles.LABEL_SELECTION_PROGRESS_DESCRIPTION);
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
      final Label timeDescription = new Label(get(MENU_STAGE_COMPLETE_TIME), Styles.LABEL_INGAME_DESCRIPTION);
      layout.add(timeDescription).row();
      int minutes = (int) (progress.getCurrentTime() / (1000 * 60));
      int seconds = (int) (progress.getCurrentTime() / 1000 % 60);
      int millis = (int) (progress.getCurrentTime() % 1000);
      Actor currentTime = createAnimatedLogo(format("%02d:%02d.%03d", minutes, seconds, millis), Styles.LABEL_SELECTION_TOTAL_PROGRESS, context.getTweenManager());
      layout.add(currentTime).row();

      final Label timeRecordLabel = new Label(get(MENU_STAGE_COMPLETE_RECORD), Styles.LABEL_SELECTION_PROGRESS_DESCRIPTION);
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

      NavigationMenu.NavigationMenuStyle style = new NavigationMenu.NavigationMenuStyle(MENU_STYLE);
      style.alignment = HORIZONTAL;
      style.padding = 100;
      buttonMenu = new NavigationMenu<Button>(style);
      addMenuButton(MENU_STAGE_RETRY, buttonMenu, new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            context.getScreenTransitions().out(new IngameScreen(getGame(), metaData), 0.5f);
         }
      });
      addMenuButton(MENU_MAIN_PLAY_LEADERBOARDS, buttonMenu, new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            try {
               final String leaderboardId = Leaderboards.valueOf("QUICKEST_PLAYERS_LEVEL_" + progress.getMetadata().getLevelNumber()).getGooglePlayId();
               getGame().getGameServiceClient().showLeaderboards(leaderboardId);
            } catch (GameServiceException e) {
               e.printStackTrace();
               Gdx.app.error("GooglePlay", "Unable to show leaderboard");
            }
         }
      });
      addMenuButton(stageCompletedForTheFirstTime ? MENU_STAGE_NEXT : MENU_STAGE_SELECTION, buttonMenu, new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            context.getScreenTransitions().out(new StageSelectionScreen(getGame(), !stageCompletedForTheFirstTime), 0.5f);
         }
      });

      layout.add(buttonMenu).padTop(40).row();
      buttonMenu.next();
      if (stageCompletedForTheFirstTime) {
         buttonMenu.next();
      }

      context.getWorldStage().addActor(layout);
      context.getGameCamera().setStickToWorldBounds(false);
      context.getGameCamera().setZoom(1300, GameCamera.ZoomMode.TO_HEIGHT);
      context.getGameCamera().getInternalCamera().update();

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
