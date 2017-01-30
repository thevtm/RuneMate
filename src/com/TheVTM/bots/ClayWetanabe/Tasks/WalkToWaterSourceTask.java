package com.TheVTM.bots.ClayWetanabe.Tasks;

import com.TheVTM.bots.ClayWetanabe.ClayWetanabe;
import com.TheVTM.bots.ClayWetanabe.Common;
import com.TheVTM.bots.ClayWetanabe.Constants;
import com.TheVTM.bots.ClayWetanabe.Location;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by VTM on 8/4/2016.
 */
public class WalkToWaterSourceTask extends Task {

    /* LOGGER */
    private static final Logger LOGGER = Logger.getLogger(WalkToWaterSourceTask.class.getName());

    /* METHODS */

    @Override
    public boolean validate() {
        try {
            GameObject waterSource = GameObjects.newQuery().names(Constants.WATER_SOURCES).results().nearest();

            return Common.hasRequiredItems()
                && waterSource != null && waterSource.distanceTo(Players.getLocal().getPosition()) >= 7;

        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Unable to validate, returning false.", e);
            return false;

        }
    }

    @Override
    public void execute() {
        Location location = ClayWetanabe.GetInstance().getLocation();
        Coordinate destination = location.getWaterSourceArea().getRandomCoordinate();
        RegionPath pathToWaterSource = RegionPath.buildTo(destination); // Sets path

        if (pathToWaterSource != null) {
            LOGGER.log(Level.FINE, String.format("Found path to water source at (%d, %d, %d).",
                    destination.getX(), destination.getY(), destination.getPlane()));

            // Toggle run
            if(Traversal.getRunEnergy() > 50 && !Traversal.isRunEnabled())
                Traversal.toggleRun();

            // Steps through path
            pathToWaterSource.step(true); // goes to the bank

            // Turn camera to bank if nearby
            GameObject waterSource = GameObjects.newQuery().names(Constants.WATER_SOURCES).results().nearest();
            if(waterSource != null && waterSource.isVisible()) {
                Camera.concurrentlyTurnTo(waterSource);
            }
        } else {
            LOGGER.log(Level.WARNING, String.format("Unable to find path to the bank at (%d, %d, %d).",
                    destination.getX(), destination.getY(), destination.getPlane()));
        }
    }
}
