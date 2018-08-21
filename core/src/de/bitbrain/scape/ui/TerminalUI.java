package de.bitbrain.scape.ui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.util.DeltaTimer;

import java.util.List;

public class TerminalUI extends Table {

   private static final float BLINK_INTERVAL = 0.5f;
   private static final float LINE_REVEAL_INTERVAL = 0.03f;
   private static final String CURSOR = "_";

   private final List<String> commands;

   private final Label terminal;

   private final DeltaTimer cursorTimer = new DeltaTimer();
   private final DeltaTimer revealTimer = new DeltaTimer();

   private int cursorPosition = 0;

   private boolean showCursor = false;

   private String existingtext = "";

   public TerminalUI(List<String> commands) {
      this.commands = commands;
      terminal = new Label(CURSOR, Styles.LABEL_INTRO_COMMAND);
      terminal.setWrap(true);
      setFillParent(true);
      left().top().padLeft(30f).padTop(30f).add(terminal);
   }

   @Override
   public void act(float delta) {
      cursorTimer.update(delta);
      revealTimer.update(delta);
      if (cursorTimer.reached(BLINK_INTERVAL)) {
         cursorTimer.reset();
         showCursor = !showCursor;
      }

      String text = "";
      String cursor = showCursor ? CURSOR : "";
      if (!commands.isEmpty() && revealTimer.reached(LINE_REVEAL_INTERVAL)) {
         revealTimer.reset();
         if (cursorPosition < commands.get(0).length()) {
            text = commands.get(0).substring(0, ++cursorPosition);
         } else {
            existingtext += commands.get(0).substring(0, cursorPosition);
            cursorPosition = 0;
            commands.remove(0);
         }
      } else if (!commands.isEmpty()) {
         text = commands.get(0).substring(0, cursorPosition);
      }

      terminal.setText(existingtext + text + cursor);

   }
}
