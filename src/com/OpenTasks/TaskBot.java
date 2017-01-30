package com.OpenTasks;

import com.OpenTasks.EventBus.Dummy;
import com.OpenTasks.EventBus.EventDispatcher;
import com.OpenTasks.EventBus.Events.*;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.LoopingBot;

/**
 * Created by TheVTM on 21/1/2017.
 */
public class TaskBot extends LoopingBot {

  /* FIELDS */

  private EventDispatcher dispatcher;
  private Logger logger;
  private Task mainTask;

  private boolean tasksRunning = false;
  private StopWatch runtimeStopWatch;

  /* METHODS */

  public static TaskBot GetInstance() {
    return (TaskBot) Environment.getBot();
  }

  @Override
  public void onStart(String... strings) {
    // 1. Initialize fields

    // 1.1 Initialize logger
    if (logger == null) {
      if (Environment.isSDK()) {
        logger = new Logger(Logger.Level.All);
      } else {
        logger = new Logger();
      }
    }

    Logger.info("Starting bot.");

    runtimeStopWatch = new StopWatch();
    dispatcher = new EventDispatcher();

    // 2. Dispatch start event
    dispatcher.publish(new StartBotEvent());

    // 2. Call super's method
    super.onStart(strings);
  }

  @Override
  public void onLoop() {
    if (tasksRunning) {
      if (mainTask.validate()) {
        Logger.debug("Executing Main Task.");

        mainTask.execute();
      }
    }
  }

  @Override
  public void onStop() {
    Logger.info("Stopping bot.");

    // 1. Dispatch stop event
    dispatcher.publish(new StopBotEvent());

    // 2. Call super's method
    super.onStop();
  }

  @Override
  public void onResume() {
    try {
      getPlatform().invokeAndWait(() -> {
        Logger.info("Resuming bot.");

        // 1. Restart tasks if they were running
        if (tasksRunning)
          onStartTasks();

        // 2. Dispatch resume event
        dispatcher.publish(new ResumeBotEvent());

      });
  } catch (Exception e) {
    throw new RuntimeException(e);
  }

    // 3. Call super's method
    super.onResume();
  }

  @Override
  public void onPause() {
    try {
      getPlatform().invokeAndWait(() -> {
        Logger.info("Pausing bot.");

        // 1. Restart tasks if they were running
        if (tasksRunning)
          onStopTasks();

        // 2. Dispatch pause event
        dispatcher.publish(new PauseBotEvent());

      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // 3. Call super's method
    super.onPause();
  }

  public void onStartTasks() {
    Logger.info("Starting tasks.");

    // 1. Dispatch start tasks event
    dispatcher.publish(new StartTasksEvent());

    // 2. Start runtime stopwatch
    runtimeStopWatch.start();
  }

  public void onStopTasks() {
    Logger.info("Stopping tasks.");

    // 1. Dispatch stop tasks event
    dispatcher.publish(new StopTasksEvent());

    // 2. Stop runtime stopwatch
    runtimeStopWatch.stop();
  }

  public void startTasks() {
    // 1. If its already running throw exception
    if (tasksRunning) {
      throw new RuntimeException("Tasks are already running.");
    }

    // 2. Run onStartTasks
    onStartTasks();

    // 3. Set tasksRunning true
    tasksRunning = true;
  }

  public void stopTasks() {
    // 1. If its already stopped throw exception
    if (!tasksRunning) {
      throw new RuntimeException("Tasks are already stopped.");
    }

    // 2. Run onStopTasks
    onStopTasks();

    // 3. Set tasksRunning false
    tasksRunning = false;
  }

  public EventDispatcher getDispatcher() {
    return dispatcher;
  }

  public Logger getLogger() {
    return logger;
  }

  public void setLogger(Logger logger) {
    this.logger = logger;
  }

  public Task getMainTask() {
    return mainTask;
  }

  public void setMainTask(Task mainTask) {
    this.mainTask = mainTask;
  }

  public boolean isTasksRunning() {
    return tasksRunning;
  }

  public long getRuntime() {
    return runtimeStopWatch.getRuntime();
  }

  public String getRuntimeAsString() {
    return runtimeStopWatch.getRuntimeAsString();
  }

}
