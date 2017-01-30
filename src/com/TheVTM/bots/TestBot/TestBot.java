package com.TheVTM.bots.TestBot;

import com.OpenTasks.Logger;
import com.OpenTasks.TaskBot;

/**
 * Created by TheVTM on 25/1/2017.
 */
public class TestBot extends TaskBot {
  @Override
  public void onStart(String... strings) {
    // 1. Initialize logger from JSON
    Logger logger = Logger.fromJson("com/TheVTM/bots/TestBot/log.config.json");
    setLogger(logger);

    System.out.println(logger);
    Logger.debug(logger.toString());

    super.onStart(strings);
  }

  @Override
  public void onLoop() {
    super.onLoop();
  }
}
