package de.bitbrain.scape.i18n;

public enum Messages {

   // MAIN MENU

   MENU_LOGO_CREDITS("menu.logo.credits"),
   MENU_MAIN_CONTINUE("menu.main.continue"),
   MENU_MAIN_NEWGAME("menu.main.newgame"),
   MENU_MAIN_EXIT("menu.main.exit"),
   MENU_MAIN_CREDITS("menu.main.credits"),
   MENU_INGAME_LEVEL("menu.ingame.level"),
   MENU_STAGE_RETRY("menu.stage.retry"),
   MENU_STAGE_SELECTION("menu.stage.selection"),
   MENU_STAGE_NEXT("menu.stage.next"),

   // LEVEL SELECTION

   MENU_SELECTION_PROGRESS("menu.selection.progress"),
   MENU_SELECTION_COMPLETE("menu.selection.complete"),

   // INGAME

   MENU_INGAME_PRESS_ANY_KEY("menu.ingame.pressanykey"),

   // LEVEL COMPLETE
   MENU_STAGE_COMPLETE("menu.stage.complete"),
   MENU_STAGE_COMPLETE_BYTES("menu.stage.complete.bytes"),
   MENU_STAGE_COMPLETE_RECORD("menu.stage.complete.record"),
   MENU_STAGE_COMPLETE_TIME("menu.stage.complete.time");


   private final String key;

   Messages(String key) {
      this.key = key;
   }

   public String getKey() {
      return key;
   }
}
