package com.TheVTM.bots.ClayWetanabe.Tasks;

import com.TheVTM.bots.ClayWetanabe.ClayWetanabe;
import com.TheVTM.bots.ClayWetanabe.Common;
import com.TheVTM.bots.ClayWetanabe.Location;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by VTM on 8/4/2016.
 */
public class WalkToBankTask extends Task {

    /* LOGGER */
    private static final Logger LOGGER = Logger.getLogger(WalkToBankTask.class.getName());

    /* METHODS */

    @Override
    public boolean validate() {
        try {
            GameObject bank = GameObjects.newQuery().actions("Bank").results().nearest();

            return !Common.hasRequiredItems()
                && bank != null && bank.distanceTo(Players.getLocal().getPosition()) >= 7;

        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Unable to validate, returning false.", e);
            return false;

        }
    }

    @Override
    public void execute() {
        Location location = ClayWetanabe.GetInstance().getLocation();
        Coordinate destination = location.getBankArea().getRandomCoordinate();
        RegionPath pathToBank = RegionPath.buildTo(destination); // Sets path

        if (pathToBank != null) {
            LOGGER.log(Level.FINE, String.format("Found path to bank at (%d, %d, %d).",
                destination.getX(), destination.getY(), destination.getPlane()));

            // Toggle run
            if(Traversal.getRunEnergy() > 50 && !Traversal.isRunEnabled())
                Traversal.toggleRun();

            // Steps through path
            pathToBank.step(true); // goes to the bank

            // Turn camera to bank if nearby
            LocatableEntityQueryResults<GameObject> bank = GameObjects.newQuery().actions("Bank").results();
            if(!bank.isEmpty() && !bank.nearest().isVisible()) {
                Camera.concurrentlyTurnTo(bank.nearest());
            }
        } else {
            LOGGER.log(Level.WARNING, String.format("Unable to find path to the bank at (%d, %d, %d).",
                destination.getX(), destination.getY(), destination.getPlane()));
        }
    }

}
