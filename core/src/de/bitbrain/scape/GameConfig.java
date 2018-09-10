package de.bitbrain.scape;

public interface GameConfig {

   float PLAYER_START_SPEED = 50f;
   float LEVEL_START_SCROLLING_SPEED = 20f;
   float BOOT_SEQUENCE_DURATION = 5.5f;

   String PLAYER_PREFERENCES_PATH = "scape/player/config";
   String PLAYER_LEVEL_RECORD = "record";
   String PLAYER_CURRENT_LEVEL = "current_level";
   String PLAYER_MAXIMUM_LEVEL = "maximum_level";
}
