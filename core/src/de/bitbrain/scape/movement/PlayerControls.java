package de.bitbrain.scape.movement;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.scape.event.GameOverEvent;

public class PlayerControls {

   private final PlayerMovement playerMovement;
   private final GameContext2D context;

   public PlayerControls(PlayerMovement playerMovement, GameContext2D context) {
      this.playerMovement = playerMovement;
      this.context = context;
   }

   public void unfreezePlayer() {
      playerMovement.setEnabled(true);
   }

   public void jump() {
      playerMovement.jumpIfUpAgain();
   }

   public void respawn() {
      context.getEventManager().publish(new GameOverEvent());
   }
}
