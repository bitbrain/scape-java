package de.bitbrain.scape.ui;

import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.ui.NavigationMenu;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.assets.Assets;

import static de.bitbrain.braingdx.ui.NavigationMenu.NavigationMenuStyle.Alignment.HORIZONTAL;
import static de.bitbrain.braingdx.ui.NavigationMenu.NavigationMenuStyle.Alignment.VERTICAL;
import static de.bitbrain.scape.GameConfig.MENU_BUTTON_PADDING;
import static de.bitbrain.scape.assets.Assets.Textures.MENU_FOCUS_DEFAULT_NINEPATCH;

public final class Styles {

   private static final AssetManager m = SharedAssetManager.getInstance();

   public static final Label.LabelStyle LABEL_LOGO = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_POINTS_ALL = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_POINTS = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_POINTS_ALL_COMPLETE = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_POINTS_COMPLETE = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_CAPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_DESCRIPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_INTERACTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_TOTAL_PROGRESS = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_CAPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_LEVEL_PROGRESS = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_PROGRESS_DESCRIPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_PROGRESS = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_PROGRESS_COMPLETE = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_COMPLETE = new Label.LabelStyle();
   ;
   public static final Label.LabelStyle LABEL_SELECTION_DESCRIPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INTRO_COMMAND = new Label.LabelStyle();
   public static final GlitchLabel.GlitchLabelStyle LABEL_INTRO_BITBRAIN = new GlitchLabel.GlitchLabelStyle();
   public static final Label.LabelStyle LABEL_CREDITS = new Label.LabelStyle();

   public static final TextButton.TextButtonStyle BUTTON_MENU = new TextButton.TextButtonStyle();

   public static final NavigationMenu.NavigationMenuStyle MENU_STYLE = new NavigationMenu.NavigationMenuStyle();

   public static void init() {
      boolean isMobile = Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;

      LABEL_INGAME_POINTS.font = bake(Assets.Fonts.UI_NUMBER, getScaledFontSize(160));
      LABEL_INGAME_POINTS.fontColor = Colors.PRIMARY_RED;
      LABEL_INGAME_POINTS_ALL.font = bake(Assets.Fonts.UI_NUMBER, getScaledFontSize(120));
      LABEL_INGAME_POINTS_ALL.fontColor = Colors.PRIMARY_RED;
      LABEL_INGAME_POINTS_COMPLETE.font = bake(Assets.Fonts.UI_NUMBER, getScaledFontSize(140));
      LABEL_INGAME_POINTS_COMPLETE.fontColor = Colors.PRIMARY_BLUE;
      LABEL_INGAME_POINTS_ALL_COMPLETE.font = bake(Assets.Fonts.UI_NUMBER, getScaledFontSize(120));
      LABEL_INGAME_POINTS_ALL_COMPLETE.fontColor = Colors.PRIMARY_BLUE;

      LABEL_INGAME_CAPTION.font = bake(Assets.Fonts.UI_NUMBER, getScaledFontSize(190));
      LABEL_INGAME_CAPTION.fontColor = Colors.PRIMARY_RED;

      LABEL_INGAME_DESCRIPTION.font = bake(Assets.Fonts.UI_NUMBER, getScaledFontSize(110));
      LABEL_INGAME_DESCRIPTION.fontColor = Colors.PRIMARY_BLUE;

      LABEL_INGAME_INTERACTION.font = bake(Assets.Fonts.UI_NUMBER, getScaledFontSize(60));
      LABEL_INGAME_INTERACTION.fontColor = Colors.PRIMARY_BLUE;

      LABEL_INTRO_COMMAND.font = bake(Assets.Fonts.UI_NUMBER, 35);
      LABEL_INTRO_COMMAND.fontColor = Colors.PRIMARY_BLUE;

      LABEL_SELECTION_TOTAL_PROGRESS.font = bake(Assets.Fonts.UI_NUMBER, 90);
      LABEL_SELECTION_TOTAL_PROGRESS.fontColor = Colors.PRIMARY_RED;

      LABEL_SELECTION_CAPTION.font = SharedAssetManager.getInstance().get(Assets.BitmapFonts.VISITOR_MICRO, BitmapFont.class);
      LABEL_SELECTION_CAPTION.fontColor = Colors.PRIMARY_RED;

      LABEL_SELECTION_DESCRIPTION.font = SharedAssetManager.getInstance().get(Assets.BitmapFonts.VISITOR_MICRO, BitmapFont.class);
      LABEL_SELECTION_DESCRIPTION.fontColor = Colors.PRIMARY_BLUE;

      LABEL_SELECTION_LEVEL_PROGRESS.font = SharedAssetManager.getInstance().get(Assets.BitmapFonts.VISITOR_MICRO, BitmapFont.class);
      LABEL_SELECTION_LEVEL_PROGRESS.fontColor = Colors.PRIMARY_RED;

      LABEL_SELECTION_PROGRESS_DESCRIPTION.font = SharedAssetManager.getInstance().get(Assets.BitmapFonts.VISITOR_MICRO, BitmapFont.class);
      LABEL_SELECTION_PROGRESS_DESCRIPTION.fontColor = Colors.PRIMARY_BLUE;
      LABEL_SELECTION_PROGRESS.font = bake(Assets.Fonts.UI_NUMBER, 30);
      LABEL_SELECTION_PROGRESS.fontColor = Colors.PRIMARY_RED;
      LABEL_SELECTION_PROGRESS_COMPLETE.font = bake(Assets.Fonts.UI_NUMBER, 30);
      LABEL_SELECTION_PROGRESS_COMPLETE.fontColor = Colors.PRIMARY_BLUE;

      LABEL_SELECTION_COMPLETE.font = bake(Assets.Fonts.UI_NUMBER, 24);
      LABEL_SELECTION_COMPLETE.fontColor = Colors.BACKGROUND_VIOLET;

      LABEL_INTRO_BITBRAIN.font = bake(Assets.Fonts.UI_NUMBER, 48);
      LABEL_INTRO_BITBRAIN.fontColor = Colors.BITBRAIN;
      LABEL_INTRO_BITBRAIN.glitchPool = "01";

      LABEL_CREDITS.font = bake(Assets.Fonts.UI_NUMBER, isMobile ? 50 : 20);
      LABEL_CREDITS.fontColor = Colors.PRIMARY_BLUE;

      LABEL_LOGO.font = bake(Assets.Fonts.UI_NUMBER, isMobile ? 260 : 120);
      LABEL_LOGO.fontColor = Colors.PRIMARY_BLUE;

      BUTTON_MENU.font = bake(Assets.Fonts.UI_NUMBER, isMobile ? 48 : 36);
      Texture ninePatchTexture = SharedAssetManager.getInstance().get(MENU_FOCUS_DEFAULT_NINEPATCH, Texture.class);
      BUTTON_MENU.up = new NinePatchDrawable(GraphicsFactory.createNinePatch(ninePatchTexture, 22));

      MENU_STYLE.padding = MENU_BUTTON_PADDING;
      MENU_STYLE.hoverSound = SharedAssetManager.getInstance().get(Assets.Sounds.SELECT, Sound.class);
      MENU_STYLE.enterSound = SharedAssetManager.getInstance().get(Assets.Sounds.SUBMIT, Sound.class);
      MENU_STYLE.alignment = isMobile ? HORIZONTAL : VERTICAL;
      MENU_STYLE.fadeOutColor = Colors.PRIMARY_BLUE;
      MENU_STYLE.fadeInColor = Colors.PRIMARY_RED;
      MENU_STYLE.fadeOutEquation = TweenEquations.easeOutCubic;
      MENU_STYLE.fadeInEquation = TweenEquations.easeInCubic;
   }

   public static BitmapFont bake(String fontPath, int size) {
      FreeTypeFontGenerator generator = SharedAssetManager.getInstance().get(fontPath, FreeTypeFontGenerator.class);
      FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
      param.color = Color.WHITE;
      param.size = size;
      param.mono = true;
      return generator.generateFont(param);
   }

   private static int getScaledFontSize(int fontSize) {
      return (int) (fontSize / (1900f / (float) Gdx.graphics.getWidth()));
   }
}
