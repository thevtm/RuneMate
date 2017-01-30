package com.TheVTM.bots.BonePrayer.Tasks;

import com.TheVTM.bots.BonePrayer.BonePrayer;
import com.TheVTM.bots.BonePrayer.Constants;
import com.TheVTM.bots.BonePrayer.UserConfiguration;
import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by VTM on 4/4/2016.
 */
public class LootTask extends Task {

    /* FIELDS */
    Area.Circular area;

    /* LOGGER */
    private static final Logger LOGGER = Logger.getLogger(LootTask.class.getName());


    public LootTask(Task... a) {
        super(a);

        /* Configuration */
        UserConfiguration config = BonePrayer.GetInstance().userConfiguration;
        area = new Area.Circular(config.startingPosition, config.maxDistance);
    }

    @Override
    public boolean validate() {
        try {
            Player player = Players.getLocal();

            return player.getAnimationId() == -1 // Player Idle
                    && !Inventory.isFull();
//                    && FSM.currentState() == FSM.State.LOOTING;

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Validation failed, returning false.", e);
            return false;
        }
    }

    @Override
    public void execute() {
        LOGGER.log(Level.FINEST, "Looking for bones...");

        try {
            LocatableEntityQueryResults<GroundItem> bones = GroundItems.newQuery()
                    .names(Constants.BONES_PATTERN).within(area).reachable().results();
            LOGGER.log(Level.FINER, String.format("Found %d bones.", bones.size()));

            if (!bones.isEmpty()) { // Found some bones
                GroundItem bone = bones.nearest(); // Find nearest bone
                String boneName = bone.getDefinition().getName();
                int bonesInInventory = Inventory.getQuantity(boneName); // Get number of bones in inventory

                LOGGER.log(Level.INFO, String.format("Picking up [%08X] %s (%d) at %s. %d bones in inventory.",
                        bone.hashCode(), boneName, bone.getId(), bone.getPosition().toString(), bonesInInventory));

                // TODO: Might be necessary to walk to the item.

                // Turn camera if cant see bone
                if (!bone.isVisible()) {
                    Camera.turnTo(bone);
                }

                // Pick up bone
                if (bone.interact("Take", boneName)) {
                    Execution.delayUntil(() -> {
                        if (Inventory.getQuantity(boneName) > bonesInInventory) {
                            LOGGER.log(Level.FINER, "New bone in inventory.");
                            return true;
                        }
                        return false;
                    }, 1000, 2000);
                }
            } else {
                LOGGER.log(Level.FINE, "No bones found.");
            }
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Unable to pick up item", e);
        }
    }
}
