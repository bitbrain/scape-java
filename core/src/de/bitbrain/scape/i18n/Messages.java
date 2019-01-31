package de.bitbrain.scape.i18n;

public enum Messages {

   // MAIN MENU

   MENU_LOGO_CREDITS("menu.logo.credits"),
   MENU_MAIN_CONTINUE("menu.main.continue"),
   MENU_MAIN_NEWGAME("menu.main.newgame"),
   MENU_MAIN_EXIT("menu.main.exit"),
   MENU_MAIN_CREDITS("menu.main.credits"),
   MENU_INGAME_LEVEL("menu.ingame.level"),

   // LEVEL SELECTION

   MENU_SELECTION_PROGRESS("menu.selection.progress"),
   MENU_SELECTION_COMPLETE("menu.selection.complete"),

   // INGAME

   MENU_INGAME_PRESS_ANY_KEY("menu.ingame.pressanykey");

   private final String key;

   Messages(String key) {
      this.key = key;
   }

   public String getKey() {
      return key;
   }
}
