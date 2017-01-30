package com.TheVTM.bots.ClayWetanabe.Tasks;

import com.TheVTM.bots.ClayWetanabe.Common;
import com.TheVTM.bots.ClayWetanabe.Constants;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by VTM on 8/4/2016.
 *
 */
public class HumidifyClayTask extends Task {

    /* LOGGER */
    private static final Logger LOGGER = Logger.getLogger(HumidifyClayTask.class.getName());

    /* METHODS */

    @Override
    public boolean validate() {
        try {
            Player player = Players.getLocal();
            LocatableEntityQueryResults<GameObject> waterSources = GameObjects.newQuery().names(Constants.WATER_SOURCES).results();

            return Common.hasRequiredItems()
                && player.getAnimationId() == -1    // Is idle
                && waterSources.nearest().distanceTo(player.getPosition()) <= 7; // Has a water source nearby

        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Unable to validate, returning false.", e);
            return false;
        }
    }

    @Override
    public void execute() {
        SpriteItem selectedItem = Inventory.getSelectedItem();

        // Select vessel
        if (selectedItem == null) { // Has a vessel with watter
            SpriteItem vessel = Inventory.newQuery().names(Constants.VESSELS()).results().first();

            if (vessel != null) { // Has a vessel
                LOGGER.log(Level.INFO, String.format("Selecting vessel with water \"%s\".", vessel.getDefinition().getName()));

                // Interact with vessel to select
                if(vessel.interact("Use")) {
                    // Wait till item is selected
                    Execution.delayUntil(() -> Inventory.getSelectedItem() != null, 1000, 2000);
                }

            } else {
                LOGGER.log(Level.WARNING, "Unable to find vessel in inventory.");
                return;
            }
        }
        else   // Has an item selected
        {
            String selectedItemName = selectedItem.getDefinition().getName();

            // Fill up vessel
            if (Arrays.asList(Constants.VESSELS_EMPTY).contains(selectedItemName)) { // Empty vessel is selected
                GameObject nearestWaterSource = GameObjects.newQuery().names(Constants.WATER_SOURCES).results().nearest();

                LOGGER.log(Level.INFO, String.format("Filling up \"%s\" in \"%s\".", selectedItemName,
                        nearestWaterSource.getDefinition().getName()));

                // Turn to water source if not visible
                if(!nearestWaterSource.isVisible()) {
                    Camera.turnTo(nearestWaterSource);
                }

                // Use vessel with water source
                if(nearestWaterSource.interact("Use")) {
                    // Wait until the selected vessel is invalid
                    Execution.delayUntil(() -> !selectedItem.isValid(), 1000, 2000);
                }
            }

            // Humidify clay
            if (Arrays.asList(Constants.VESSELS_WITH_WATER).contains(selectedItemName)) { // Vessel with water is selected
                SpriteItem clay = Inventory.newQuery().names(Constants.CLAY).results().first();

                LOGGER.log(Level.FINER, String.format("Vessel \"%s\" with water selected.",
                        selectedItemName));

                if (clay != null) { // Has clay
                    LOGGER.log(Level.INFO, String.format("Humidifying \"%s\" with \"%s\".", clay.getDefinition().getName(),
                            selectedItemName));

                    // Use vessel with water with clay
                    if(clay.interact("Use")) {
                        // Wait until the selected vessel is invalid
                        Execution.delayUntil(() -> !selectedItem.isValid(), 1000, 2000);
                    }

                } else {
                    LOGGER.log(Level.WARNING, "Unable to find clay in inventory.");
                    return;
                }
            }

        }

    }

}
