package com.TheVTM.bots.BonePrayer.Tasks;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by VTM on 4/4/2016.
 */
public class BuryTask extends Task {

  /* LOGGER */
  private static final Logger LOGGER = Logger.getLogger(BuryTask.class.getName());


  @Override
  public boolean validate() {
    try {
//            boolean isInventoryFull = Inventory.isFull();
//            FSM.State state = FSM.currentState();
//
//            // If inventory is full start burying
//            if(state != FSM.State.BURYING && isInventoryFull) {
//                FSM.changeState(FSM.State.BURYING);
//            }
//
//            return Players.getLocal().getAnimationId() == -1 // Player idle
//               && state == FSM.State.BURYING;

      return false;

    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Validation failed, returning false.", e);
      return false;
    }
  }

  @Override
  public void execute() {
    LOGGER.log(Level.FINEST, "Burying bones...");

    // Get bones
    SpriteItemQueryResults bones = Inventory.newQuery().actions("Bury").results();

    // Verify if has bones
//        if(bones.isEmpty()) {
//            FSM.changeState(FSM.State.LOOTING);
//            return;
//        }

    // Pick a random bone
    SpriteItem bone = bones.get(Random.nextInt(bones.size()));
    String name = bone.getDefinition().getName();

    LOGGER.log(Level.INFO, String.format("Burying %s (%d) at slot %d. %d bones in inventory.",
        name, bone.getId(), bone.getIndex(), bones.size()));

    // Bury
    if (bone.interact("Bury")) {
      int nBones = Inventory.getQuantity(name);

      Execution.delayUntil(() -> {
        int n = Inventory.getQuantity(name);
        if (n < nBones) {
          LOGGER.log(Level.FINER, String.format("%s (%d) at slot %d buried.", name, bone.getId(), bone.getIndex()));
          return Inventory.getQuantity(name) < nBones;
        }
        return false;
      }, 1000, 2000);

    }
  }

}
