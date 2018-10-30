package de.bitbrain.scape.assets;

import com.badlogic.gdx.graphics.Texture;

public interface Assets {

   interface Textures {
      String PLAYER = "player.png";
      String BACKGROUND = "background.png";
      String LOGO = "logo.png";
      String POWERCELL = "powercell.png";
      String UI_BG = "ui-bg.png";
      String MENU_LOGO = "menu-logo.png";
      String MENU_FOCUS_NINEPATCH = "focus.9.png";
      String MENU_FOCUS_DEFAULT_NINEPATCH = "focus-default.9.png";
   }

   interface TiledMaps {
      String WORLD_MAP = "maps/worldmap.tmx";
      String LEVEL_1 = "maps/001_databus.tmx";
      String LEVEL_2 = "maps/002_kernelio.tmx";
      String LEVEL_0= "maps/000_test.tmx";
   }

   interface Particles {
      String TOUCH_TOP = "particles/touch-top.p";
      String TOUCH_BOTTOM = "particles/touch-bottom.p";
      String COLLECT = "particles/collect.p";
      String BYTE = "particles/byte.p";
   }

   interface Fonts {
      String UI_NUMBER = "fonts/visitor.ttf";
   }

   interface Sounds {
      String BEEP = "sounds/beep.ogg";
   }
}
