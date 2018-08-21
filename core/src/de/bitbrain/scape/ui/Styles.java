package de.bitbrain.scape.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.BitmapFontBaker;
import de.bitbrain.scape.Colors;
import de.bitbrain.scape.assets.Assets;

public final class Styles {

   private static final AssetManager m = SharedAssetManager.getInstance();

   public static final Label.LabelStyle LABEL_INGAME_POINTS = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_CAPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_DESCRIPTION = new Label.LabelStyle();
   public static final Label.LabelStyle LABEL_INGAME_INTERACTION = new Label.LabelStyle();
   public static void init() {
      LABEL_INGAME_POINTS.font = BitmapFontBaker.bake(Assets.Fonts.UI_NUMBER, (Gdx.graphics.getWidth() * Gdx.graphics.getHeight() / 2000));
      LABEL_INGAME_POINTS.fontColor = Colors.PRIMARY_RED;

      LABEL_INGAME_CAPTION.font = BitmapFontBaker.bake(Assets.Fonts.UI_NUMBER, (Gdx.graphics.getWidth() * Gdx.graphics.getHeight() / 1800));
      LABEL_INGAME_CAPTION.fontColor = Colors.PRIMARY_BLUE;

      LABEL_INGAME_DESCRIPTION.font = BitmapFontBaker.bake(Assets.Fonts.UI_NUMBER, (Gdx.graphics.getWidth() * Gdx.graphics.getHeight() / 3000));
      LABEL_INGAME_DESCRIPTION.fontColor = Colors.PRIMARY_RED;

      LABEL_INGAME_INTERACTION.font = BitmapFontBaker.bake(Assets.Fonts.UI_NUMBER, (Gdx.graphics.getWidth() * Gdx.graphics.getHeight() / 6000));
      LABEL_INGAME_INTERACTION.fontColor = Colors.PRIMARY_BLUE;
   }
}
