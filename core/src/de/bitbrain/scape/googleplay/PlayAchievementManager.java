package de.bitbrain.scape.googleplay;

import de.golfgl.gdxgamesvcs.IGameServiceClient;

import java.util.HashMap;
import java.util.Map;

public class PlayAchievementManager {

   private final IGameServiceClient client;
   private final Map<Achievements, Integer> counters = new HashMap<Achievements, Integer>();

   public PlayAchievementManager(IGameServiceClient client) {
      this.client = client;
   }

   public void clear(Achievements achievements) {
      counters.remove(achievements);
   }

   public void incrementAchievement(Achievements achievement) {
      client.incrementAchievement(achievement.getGooglePlayId(), 1, 1f);
   }

   public void unlockAchievement(Achievements achievement) {
      client.unlockAchievement(achievement.getGooglePlayId());
   }

   public void incrementAndPublish(Achievements achievementId, int threshold) {
      if (counters.containsKey(achievementId)) {
         counters.put(achievementId, 1);
      } else {
         counters.put(achievementId, counters.get(achievementId) + 1);
      }
      if (counters.get(achievementId) >= threshold) {
         client.unlockAchievement(achievementId.getGooglePlayId());
      }
   }
}
