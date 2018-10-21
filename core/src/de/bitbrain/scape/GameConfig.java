package de.bitbrain.scape;

import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.util.Mutator;

public interface GameConfig {

   int SWIPE_TOLERANCE = 2;

   float PLAYER_START_SPEED = 50f;
   float LEVEL_START_SCROLLING_SPEED = 0f;
   float BOOT_SEQUENCE_DURATION = 0.6f;

   String PLAYER_PREFERENCES_PATH = "scape.player.config";
   String PLAYER_LEVEL_RECORD = "record";
   String PLAYER_CURRENT_LEVEL = "current_level";
   String PLAYER_MAXIMUM_LEVEL = "maximum_level";

   Mutator<Bloom> DEFAULT_BLOOM_CONFIG = new Mutator<Bloom>() {
      @Override
      public void mutate(Bloom target) {
         target.setBlurAmount(5f);
         target.setBloomIntesity(1.2f);
         target.setBlurPasses(50);
         target.setThreshold(0.3f);
      }
   };
}
