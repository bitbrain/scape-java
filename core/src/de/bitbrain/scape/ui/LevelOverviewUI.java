package de.bitbrain.scape.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.scape.level.LevelMetaData;
import de.bitbrain.scape.assets.Assets;
import de.bitbrain.scape.i18n.Bundle;
import de.bitbrain.scape.i18n.Messages;
import de.bitbrain.scape.progress.PlayerProgress;

public class LevelOverviewUI extends VerticalGroup {
   private final Label caption;
   private final Label points;

   public LevelOverviewUI(LevelMetaData metadata) {
      PlayerProgress progress = new PlayerProgress(metadata);
      caption = new Label(metadata.getName(), Styles.LABEL_SELECTION_CAPTION);
      points = new Label(progress.getRecord() + "/" +  metadata.getNumberOfBytes(), Styles.LABEL_SELECTION_DESCRIPTION);
      addActor(caption);
      addActor(points);
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      Texture background = SharedAssetManager.getInstance().get(Assets.Textures.UI_BG, Texture.class);
      batch.setColor(getColor());
      batch.draw(background, getX() - 40, getY() - 30, 80, 40);
      super.draw(batch, parentAlpha);
   }
}
