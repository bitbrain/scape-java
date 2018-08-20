package de.bitbrain.scape.assets;

public interface Assets {

   interface Textures {
      String PLAYER = "player.png";
      String BACKGROUND = "background.png";
   }

   interface TiledMaps {
      String LEVEL_1 = "maps/1_intro.tmx";
      String LEVEL_2 = "maps/2_city.tmx";
   }

   interface Particles {
      String TOUCH_TOP = "particles/touch-top.p";
      String TOUCH_BOTTOM = "particles/touch-bottom.p";
      String COLLECT = "particles/collect.p";
      String BYTE = "particles/byte.p";
   }

   interface Fonts {
      String EIGHT_BIT_WONDER = "fonts/8bitwonder.ttf";
   }
}
