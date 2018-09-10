package de.bitbrain.scape.ui;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.scape.assets.Assets;

import java.util.List;

public class TerminalUI extends Table {

   private static final float BLINK_INTERVAL = 0.5f;
   private static final float LINE_REVEAL_INTERVAL = 0.03f;
   private static final String CURSOR = "_";

   private final List<String> commands;

   private final Label terminal;

   private final DeltaTimer cursorTimer = new DeltaTimer();
   private final DeltaTimer revealTimer = new DeltaTimer();
   private final DeltaTimer lineTimer = new DeltaTimer();

   private int cursorPosition = 0;

   private boolean showCursor = false;

   private boolean nextLineTriggered = false;

   private String existingtext = "";

   private float randomLineInterval = 0.4f;

   private boolean paused = false;

   public TerminalUI(List<String> commands) {
      this.commands = commands;
      terminal = new Label(CURSOR, Styles.LABEL_INTRO_COMMAND);
      terminal.setWrap(true);
      setFillParent(true);
      left().top().padLeft(30f).padTop(30f).add(terminal);
   }

   public String getText() {
      return existingtext;
   }

   public void setText(String text) {
      this.existingtext = text;
   }

   public void setPaused(boolean paused) {
      this.paused = paused;
   }

   @Override
   public void act(float delta) {
      if (paused) {
         terminal.setText(existingtext);
         return;
      }
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
         if (!nextLineTriggered && cursorPosition < commands.get(0).length()) {
            text = commands.get(0).substring(0, ++cursorPosition);
            if (!text.equals("\n")) {
               SharedAssetManager.getInstance().get(Assets.Sounds.BEEP, Sound.class).play(0.05f);
            }
         } else if (lineTimer.reached(randomLineInterval)) {
            cursorPosition = 0;
            commands.remove(0);
            lineTimer.reset();
            randomLineInterval = (float) (Math.random() * 0.2f);
            nextLineTriggered = false;
         } else {
            if (!nextLineTriggered) {
               existingtext += commands.get(0).substring(0, cursorPosition);
               cursorPosition = 0;
               nextLineTriggered = true;
            }
            lineTimer.update(delta);
         }
      } else if (!commands.isEmpty()) {
         text = commands.get(0).substring(0, cursorPosition);
      }
      terminal.setText(existingtext + text + cursor);
   }
}
