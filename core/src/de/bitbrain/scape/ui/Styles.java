package de.bitbrain.scape.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.assets.Assets;

import static de.bitbrain.scape.assets.Assets.Textures.MENU_FOCUS_DEFAULT_NINEPATCH;
import static de.bitbrain.scape.assets.Assets.Textures.MENU_FOCUS_NINEPATCH;

public final class Styles {

   private static final AssetManager m = SharedAssetManager.getInstance();

   public static final Label.LabelStyle LABEL_INGAME_POINTS_ALL = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_POINTS = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_POINTS_ALL_COMPLETE = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_POINTS_COMPLETE = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_CAPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_DESCRIPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_INTERACTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_TOTAL_PROGRESS = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_CAPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_PROGRESS_DESCRIPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_PROGRESS = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_PROGRESS_COMPLETE = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_SELECTION_COMPLETE = new Label.LabelStyle();;
   public static final Label.LabelStyle LABEL_SELECTION_DESCRIPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INTRO_COMMAND = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INTRO_BITBRAIN = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_CREDITS = new Label.LabelStyle();

   public static final TextButton.TextButtonStyle BUTTON_MENU = new TextButton.TextButtonStyle();

   public static void init() {
      LABEL_INGAME_POINTS.font = bake(Assets.Fonts.UI_NUMBER, 140);
      LABEL_INGAME_POINTS.fontColor = Colors.PRIMARY_RED;
      LABEL_INGAME_POINTS_ALL.font = bake(Assets.Fonts.UI_NUMBER, 86);
      LABEL_INGAME_POINTS_ALL.fontColor = Colors.PRIMARY_RED;
      LABEL_INGAME_POINTS_COMPLETE.font = bake(Assets.Fonts.UI_NUMBER, 140);
      LABEL_INGAME_POINTS_COMPLETE.fontColor = Colors.PRIMARY_BLUE;
      LABEL_INGAME_POINTS_ALL_COMPLETE.font = bake(Assets.Fonts.UI_NUMBER, 140);
      LABEL_INGAME_POINTS_ALL_COMPLETE.fontColor = Colors.PRIMARY_BLUE;

      LABEL_INGAME_CAPTION.font = bake(Assets.Fonts.UI_NUMBER, 128);
      LABEL_INGAME_CAPTION.fontColor = Colors.PRIMARY_RED;

      LABEL_INGAME_DESCRIPTION.font = bake(Assets.Fonts.UI_NUMBER, 66);
      LABEL_INGAME_DESCRIPTION.fontColor = Colors.PRIMARY_BLUE;

      LABEL_INGAME_INTERACTION.font = bake(Assets.Fonts.UI_NUMBER, 46);
      LABEL_INGAME_INTERACTION.fontColor = Colors.PRIMARY_BLUE;

      LABEL_INTRO_COMMAND.font = bake(Assets.Fonts.UI_NUMBER, 35);
      LABEL_INTRO_COMMAND.fontColor = Colors.PRIMARY_BLUE;

      LABEL_SELECTION_TOTAL_PROGRESS.font = bake(Assets.Fonts.UI_NUMBER, 90);
      LABEL_SELECTION_TOTAL_PROGRESS.fontColor = Colors.PRIMARY_RED;

      LABEL_SELECTION_CAPTION.font = bake(Assets.Fonts.UI_NUMBER, 62);
      LABEL_SELECTION_CAPTION.fontColor = Colors.PRIMARY_RED;

      LABEL_SELECTION_DESCRIPTION.font = bake(Assets.Fonts.UI_NUMBER, 22);
      LABEL_SELECTION_DESCRIPTION.fontColor = Colors.PRIMARY_BLUE;

      LABEL_SELECTION_PROGRESS_DESCRIPTION.font = bake(Assets.Fonts.UI_NUMBER, 20);
      LABEL_SELECTION_PROGRESS_DESCRIPTION.fontColor = Colors.PRIMARY_BLUE;
      LABEL_SELECTION_PROGRESS.font = bake(Assets.Fonts.UI_NUMBER, 30);
      LABEL_SELECTION_PROGRESS.fontColor = Colors.PRIMARY_RED;
      LABEL_SELECTION_PROGRESS_COMPLETE.font = bake(Assets.Fonts.UI_NUMBER, 30);
      LABEL_SELECTION_PROGRESS_COMPLETE.fontColor = Colors.PRIMARY_BLUE;

      LABEL_SELECTION_COMPLETE.font = bake(Assets.Fonts.UI_NUMBER, 24);
      LABEL_SELECTION_COMPLETE.fontColor = Colors.BACKGROUND_VIOLET;

      LABEL_INTRO_BITBRAIN.font = bake(Assets.Fonts.UI_NUMBER, 20);
      LABEL_INTRO_BITBRAIN.fontColor = Colors.BITBRAIN;

      LABEL_CREDITS.font = bake(Assets.Fonts.UI_NUMBER, 20);
      LABEL_CREDITS.fontColor = Colors.PRIMARY_BLUE;

      BUTTON_MENU.font = bake(Assets.Fonts.UI_NUMBER, 36);
      BUTTON_MENU.fontColor = Colors.PRIMARY_BLUE;
      BUTTON_MENU.downFontColor = Colors.PRIMARY_RED;
      BUTTON_MENU.checkedFontColor = Colors.PRIMARY_RED;
      BUTTON_MENU.overFontColor = Colors.PRIMARY_RED;
      Texture ninePatchTexture = SharedAssetManager.getInstance().get(MENU_FOCUS_NINEPATCH, Texture.class);
      BUTTON_MENU.over = new NinePatchDrawable(GraphicsFactory.createNinePatch(ninePatchTexture, 22));
      Texture defaultPatchTexture = SharedAssetManager.getInstance().get(MENU_FOCUS_DEFAULT_NINEPATCH, Texture.class);
      BUTTON_MENU.checked = BUTTON_MENU.over;
      BUTTON_MENU.up = new NinePatchDrawable(GraphicsFactory.createNinePatch(defaultPatchTexture, 22));
   }

   public static BitmapFont bake(String fontPath, int size) {
      FreeTypeFontGenerator generator = SharedAssetManager.getInstance().get(fontPath, FreeTypeFontGenerator.class);
      FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
      param.color = Color.WHITE;
      param.size = size;
      param.mono = true;
      return generator.generateFont(param);
   }
}
