package de.bitbrain.scape.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import de.bitbrain.scape.LevelMetaData;

public class LevelSelectionUI extends VerticalGroup {

   private final LevelMetaData metadata;
   private final Label caption;
   private final Label points;

   public LevelSelectionUI(LevelMetaData metadata) {
      this.metadata = metadata;
      caption = new Label(metadata.getName(), Styles.LABEL_SELECTION_CAPTION);
      points = new Label("record: " + metadata.getProgress().getRecord(), Styles.LABEL_SELECTION_DESCRIPTION);
      addActor(caption);
      addActor(points);
   }
}
