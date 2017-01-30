package com.TheVTM.bots.ClayWetanabe;

import com.TheVTM.bots.ClayWetanabe.Tasks.BankTask;
import com.TheVTM.bots.ClayWetanabe.Tasks.HumidifyClayTask;
import com.TheVTM.bots.ClayWetanabe.Tasks.WalkToBankTask;
import com.TheVTM.bots.ClayWetanabe.Tasks.WalkToWaterSourceTask;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.framework.task.TaskBot;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by VTM on 8/4/2016.
 */
public class ClayWetanabe extends TaskBot {

    /* FIELDS */
    private Location location;

    /* LOGGER */
    private static final Logger LOGGER = Logger.getLogger(ClayWetanabe.class.getName());

    /* METHODS */

    public static ClayWetanabe GetInstance() {
        return (ClayWetanabe) Environment.getBot();
    }

    @Override
    public void onStart(String... a) {
        super.onStart(a);

        /* INITIALIZATION */

        // Log configs
        configureLog();

        // Pick the Location
        location = Location.getLocations().get(0);

        /* TASKS */
        add(new HumidifyClayTask(),
            new WalkToBankTask(),
            new WalkToWaterSourceTask(),
            new BankTask());

        // Set loop delay
        setLoopDelay(400, 600);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void configureLog() {
        try {
            if(Environment.isSDK()) {
                LogManager.getLogManager().readConfiguration(Resources.getAsStream(Constants.LOG_DEV_PROPERTIES_PATH));
                LOGGER.log(Level.CONFIG, String.format("Using log config \"%s\".", Constants.LOG_DEV_PROPERTIES_PATH));
            } else {
                LogManager.getLogManager().readConfiguration(Resources.getAsStream(Constants.LOG_PROPERTIES_PATH));
                LOGGER.log(Level.CONFIG, String.format("Using log config \"%s\".", Constants.LOG_PROPERTIES_PATH));
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to load log properties.");
            e.printStackTrace();
        }
    }

    public Location getLocation() {
        return location;
    }

}
