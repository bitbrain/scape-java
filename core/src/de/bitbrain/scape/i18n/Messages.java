package de.bitbrain.scape.i18n;

public enum Messages {
   MENU_LOGO_CREDITS("menu.logo.credits"),
   MENU_MAIN_CONTINUE("menu.main.continue"),
   MENU_MAIN_NEWGAME("menu.main.newgame"),
   MENU_MAIN_EXIT("menu.main.exit"),
   MENU_MAIN_CREDITS("menu.main.credits");

   private final String key;

   Messages(String key) {
      this.key = key;
   }

   public String getKey() {
      return key;
   }
}
