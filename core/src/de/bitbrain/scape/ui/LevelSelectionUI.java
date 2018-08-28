package de.bitbrain.scape.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import de.bitbrain.scape.LevelMetaData;

public class LevelSelectionUI extends VerticalGroup {

   private final LevelMetaData metadata;
   private final Label caption;

   public LevelSelectionUI(LevelMetaData metadata) {
      this.metadata = metadata;
      caption = new Label(metadata.getName(), Styles.LABEL_INGAME_DESCRIPTION);
      addActor(caption);
   }

   @Override
   public void act(float delta) {
      super.act(delta);
   }
}
