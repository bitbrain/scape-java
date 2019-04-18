package de.bitbrain.scape.ui.levelselection;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.i18n.Bundle;
import de.bitbrain.scape.i18n.Messages;
import de.bitbrain.scape.level.LevelMetaData;
import de.bitbrain.scape.progress.PlayerProgress;
import de.bitbrain.scape.ui.Styles;

public class LevelOverviewUI extends VerticalGroup {

   public LevelOverviewUI(LevelMetaData metadata) {
      PlayerProgress progress = new PlayerProgress(metadata);
      addActor(new Label(metadata.getName(), Styles.LABEL_SELECTION_CAPTION));
      addActor(new Label(" ", Styles.LABEL_SELECTION_DESCRIPTION));
      Label description = new Label(Bundle.get(Messages.MENU_INGAME_LEVEL) + " " + metadata.getLevelNumber(), Styles.LABEL_SELECTION_DESCRIPTION);
      addActor(description);
      addActor(new Label(" ", Styles.LABEL_SELECTION_DESCRIPTION));
      addActor(new Label(" ", Styles.LABEL_SELECTION_DESCRIPTION));
      addActor(new LevelProgressUI(metadata, progress));
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      Texture background = SharedAssetManager.getInstance().get(Assets.Textures.UI_BG, Texture.class);
      batch.setColor(getColor());
      batch.draw(background, getX() - 40, getY() - 40, 80, 50);
      super.draw(batch, parentAlpha);
   }
}
