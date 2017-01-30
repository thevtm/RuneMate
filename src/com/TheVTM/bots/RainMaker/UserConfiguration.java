package com.TheVTM.bots.RainMaker;

import com.OpenTasks.Logger;
import com.OpenTasks.TaskBot;
import com.runemate.game.api.hybrid.util.io.ManagedProperties;

/**
 * Created by TheVTM on 30/1/2017.
 */
public class UserConfiguration {

  /* FIELDS */

  public final boolean takeBreaks;

  /* METHODS */

  // Default
  public UserConfiguration() { this(true); }

  public UserConfiguration(boolean takeBreaks) {
    this.takeBreaks = takeBreaks;
  }

  @Override
  public String toString() {
    return "UserConfiguration{" +
        "takeBreaks=" + takeBreaks +
        '}';
  }

  /* STATIC METHODS */

  public static UserConfiguration load() {
    Logger.info("Loading user configuration.");

    // 1.
    ManagedProperties settings = TaskBot.GetInstance().getSettings();

    // 2. Get data from settings
    boolean takeBreaks = Boolean.parseBoolean(settings.getProperty("takeBreaks", "true"));

    // 3. Create UserConfiguration instance
    UserConfiguration config = new UserConfiguration(takeBreaks);

    Logger.info("Loaded user configuration: %s", config.toString());

    return config;
  }

  public static void save(UserConfiguration config) {
    Logger.info("Saving user configuration: %s", config.toString());

    // 1.
    ManagedProperties settings = TaskBot.GetInstance().getSettings();

    // 2. Set data from settings
    settings.setProperty("takeBreaks", String.valueOf(config.takeBreaks));

    Logger.info("User configuration saved.");
  }

}
