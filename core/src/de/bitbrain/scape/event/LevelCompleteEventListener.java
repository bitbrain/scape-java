package de.bitbrain.scape.event;

import com.badlogic.gdx.audio.Music;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.googleplay.Achievements;
import de.bitbrain.scape.level.LevelMetaData;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.screens.IngameScreen;
import de.bitbrain.scape.screens.StageCompleteScreen;


public class LevelCompleteEventListener implements GameEventListener<LevelCompleteEvent> {

   private final ScapeGame game;
   private final PlayerProgress progress;
   private final IngameScreen screen;
   private final LevelMetaData levelMetaData;

   public LevelCompleteEventListener(
         ScapeGame game,
         IngameScreen screen,
         PlayerProgress progress,
         LevelMetaData levelMetaData) {
      this.game = game;
      this.screen = screen;
      this.progress = progress;
      this.levelMetaData = levelMetaData;
   }

   @Override
   public void onEvent(final LevelCompleteEvent event) {
      if (screen.isGameOver()) {
         return;
      }
      if (screen.isGameComplete()) {
         return;
      }
      screen.setGameComplete(true);
      if (screen.getStartTime() != 0) {
         this.progress.setCurrentTime(System.currentTimeMillis() - screen.getStartTime());
      }
      if (levelMetaData.getBackgroundMusicPath() != null) {
         SharedAssetManager.getInstance().get(levelMetaData.getBackgroundMusicPath(), Music.class).stop();
      }
      if (levelMetaData.getNumberOfBytes() == progress.getPointRecord()) {
         game.getGameServiceClient().unlockAchievement(Achievements.FULL_HOUSE);
      }
      game.setScreen(new StageCompleteScreen(game, progress));
   }
}
