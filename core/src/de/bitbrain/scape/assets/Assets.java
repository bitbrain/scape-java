package de.bitbrain.scape.assets;

public interface Assets {

   interface Textures {
      String PLAYER = "player.png";
      String PLAYER_CHARGED = "player-charged.png";
      String BYTE = "byte.png";
      String LOGO = "logo.png";
      String POWERCELL = "powercell.png";
      String UI_BG = "ui-bg.png";
      String MENU_FOCUS_NINEPATCH = "focus.9.png";
      String MENU_FOCUS_DEFAULT_NINEPATCH = "focus-default.9.png";
      String SELECTION_NINEPATCH = "selection.9.png";
      String SELECTION_TARGET = "selection-target.png";
      String LOGO_PLAYER = "logo-player.png";
   }

   interface TiledMaps {
      String WORLD_MAP = "maps/worldmap.tmx";
      String LEVEL_01 = "maps/001_databus.tmx";
      String LEVEL_02 = "maps/002_kernelio.tmx";
      String LEVEL_03 = "maps/003_ram.tmx";
      String LEVEL_04 = "maps/004_threadpool.tmx";
      String LEVEL_05 = "maps/005_cachearray.tmx";
      String LEVEL_06 = "maps/006_northbridge.tmx";
      String LEVEL_07 = "maps/007_l2queue.tmx";
      String LEVEL_08 = "maps/008_riscunit.tmx";
      String LEVEL_09 = "maps/009_subroutine.tmx";
      String LEVEL_10 = "maps/010_securitycontroller.tmx";
   }

   interface Particles {
      String TOUCH_TOP = "particles/touch-top.p";
      String TOUCH_BOTTOM = "particles/touch-bottom.p";
      String TOUCH_RIGHT = "particles/touch-right.p";
      String COLLECT = "particles/collect.p";
      String BYTE = "particles/byte.p";
      String STAGE = "particles/stage.p";
   }

   interface Fonts {
      String UI_NUMBER = "fonts/visitor.ttf";
   }

   interface BitmapFonts {
      String VISITOR_MICRO = "fonts/visitor-micro.fnt";
   }

   interface Sounds {
      String BEEP = "sounds/beep.ogg";
      String COLLECT = "sounds/collect.ogg";
      String JUMP = "sounds/jump.ogg";
      String STEP = "sounds/step.ogg";
      String SELECT = "sounds/select.ogg";
      String SUBMIT = "sounds/submit.ogg";
      String ENTER = "sounds/enter.ogg";
      String STARTUP = "sounds/computer-startup.ogg";
      String LEVEL_COMPLETE = "sounds/level-complete.ogg";
      String LEVEL_SELECT = "sounds/level-select.ogg";
   }

   interface Musics {
      String BITBRAIN = "music/bitbrain-intro.ogg";
      String INTRO = "music/scape-intro.ogg";
      String COMPUTER_NOISE = "music/computer-noise.ogg";

      String BACKGROUND_MAIN_MENU = "music/bgm_00.ogg";
      String BACKGROUND_01 = "music/bgm_01.ogg";
      String BACKGROUND_02 = "music/bgm_02.ogg";
      String BACKGROUND_03 = "music/bgm_03.ogg";
      String BACKGROUND_04 = "music/bgm_04.ogg";
   }
}
