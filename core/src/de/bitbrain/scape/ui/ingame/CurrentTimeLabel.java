package de.bitbrain.scape.ui.ingame;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.util.ValueProvider;

public class CurrentTimeLabel extends Label {

   private ValueProvider valueProvider;

   public CurrentTimeLabel(ValueProvider valueProvider, LabelStyle labelStyle) {
      super("", labelStyle);
      this.valueProvider = valueProvider;
   }

   @Override
   public void act(float delta) {
      super.act(delta);
      int minutes = (int) (valueProvider.getValue() /(1000 * 60));
      int seconds = (int) (valueProvider.getValue() / 1000 % 60);
      int millis  = (int) (valueProvider.getValue() % 1000);
      setText( String.format("%02d:%02d.%03d", minutes, seconds, millis));
   }
}
