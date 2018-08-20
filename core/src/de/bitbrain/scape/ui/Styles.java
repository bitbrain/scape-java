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

   public static void init() {
      LABEL_INGAME_POINTS.font = BitmapFontBaker.bake(Assets.Fonts.PIXEL_MADNESS, (Gdx.graphics.getWidth() * Gdx.graphics.getHeight() / 2500));
      LABEL_INGAME_POINTS.fontColor = Colors.PRIMARY_RED;
   }
}
