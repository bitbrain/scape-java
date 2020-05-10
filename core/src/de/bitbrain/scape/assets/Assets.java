package de.bitbrain.scape.assets;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.assets.annotations.AssetSource;

public interface Assets {

   @AssetSource(directory = "", assetClass = Texture.class)
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

   @AssetSource(directory = "maps", assetClass = TiledMap.class)
   interface TiledMaps {
      String WORLD_MAP = "worldmap.tmx";
      String LEVEL_01 = "001_databus.tmx";
      String LEVEL_02 = "002_kernelio.tmx";
      String LEVEL_03 = "003_ram.tmx";
      String LEVEL_04 = "004_threadpool.tmx";
      String LEVEL_05 = "005_cachearray.tmx";
      String LEVEL_06 = "006_northbridge.tmx";
      String LEVEL_07 = "007_l2queue.tmx";
      String LEVEL_08 = "008_riscunit.tmx";
      String LEVEL_09 = "009_subroutine.tmx";
      String LEVEL_10 = "010_securitycontroller.tmx";
   }

   @AssetSource(directory = "particles", assetClass = ParticleEffect.class)
   interface Particles {
      String TOUCH_TOP = "touch-top.p";
      String TOUCH_BOTTOM = "touch-bottom.p";
      String TOUCH_RIGHT = "touch-right.p";
      String COLLECT = "collect.p";
      String BYTE = "byte.p";
      String STAGE = "stage.p";
   }

   @AssetSource(directory = "fonts", assetClass = FreeTypeFontGenerator.class)
   interface Fonts {
      String UI_NUMBER = "visitor.ttf";
   }

   @AssetSource(directory = "fonts", assetClass = BitmapFont.class)
   interface BitmapFonts {
      String VISITOR_MICRO = "visitor-micro.fnt";
   }

   @AssetSource(directory = "sounds", assetClass = Sound.class)
   interface Sounds {
      String BEEP = "beep.ogg";
      String COLLECT = "collect.ogg";
      String JUMP = "jump.ogg";
      String STEP = "step.ogg";
      String SELECT = "select.ogg";
      String SUBMIT = "submit.ogg";
      String ENTER = "enter.ogg";
      String STARTUP = "computer-startup.ogg";
      String LEVEL_COMPLETE = "level-complete.ogg";
      String LEVEL_SELECT = "level-select.ogg";
   }

   @AssetSource(directory = "music", assetClass = Music.class)
   interface Musics {
      String BITBRAIN = "bitbrain-intro.ogg";
      String INTRO = "scape-intro.ogg";
      String COMPUTER_NOISE = "computer-noise.ogg";

      String BACKGROUND_MAIN_MENU = "bgm_00.ogg";
      String BACKGROUND_01 = "bgm_01.ogg";
      String BACKGROUND_02 = "bgm_02.ogg";
      String BACKGROUND_03 = "bgm_03.ogg";
      String BACKGROUND_04 = "bgm_04.ogg";
   }
}
