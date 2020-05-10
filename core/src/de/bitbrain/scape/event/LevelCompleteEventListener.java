package de.bitbrain.scape.event;

import com.badlogic.gdx.audio.Music;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.scape.ScapeGame;
import de.bitbrain.scape.googleplay.Achievements;
import de.bitbrain.scape.googleplay.Events;
import de.bitbrain.scape.googleplay.Leaderboards;
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
      final Events playEvent = Events.valueOf("LEVEL_COMPLETED_LEVEL_" + levelMetaData.getLevelNumber());
      screen.getGame().getPlayEventManager().submitEvent(playEvent);
      if (levelMetaData.getBackgroundMusicPath() != null) {
         Asset.get(levelMetaData.getBackgroundMusicPath(), Music.class).stop();
      }
      if (levelMetaData.getNumberOfBytes() == progress.getPoints()) {
         game.getPlayAchievementManager().unlockAchievement(Achievements.FULL_HOUSE);
      } else if (progress.getPoints() == 0) {
         game.getPlayAchievementManager().unlockAchievement(Achievements.ZERO_BYTE_EXCEPTION);
      }
      final Leaderboards leaderboard = Leaderboards.valueOf("QUICKEST_PLAYERS_LEVEL_" + levelMetaData.getLevelNumber());
      game.getGameServiceClient().submitToLeaderboard(leaderboard.getGooglePlayId(), progress.getCurrentTime(), null);
      game.setScreen(new StageCompleteScreen(game, progress));
   }
}
