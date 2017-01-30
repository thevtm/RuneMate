package com.OpenTasks.Statistics;

import com.OpenTasks.EventBus.EventHandler;
import com.OpenTasks.EventBus.Events.StartTasksEvent;
import com.OpenTasks.EventBus.Events.StopTasksEvent;
import com.runemate.game.api.hybrid.util.StopWatch;

/**
 * Created by TheVTM on 25/1/2017.
 */
public abstract class StatisticsTracker {

  /* FIELDS */

  protected StopWatch stopWatch = new StopWatch();

  /* METHODS */

  public void start() {
    stopWatch.start();
  }

  public void stop() {
    stopWatch.stop();
  }

  public void reset() {
    stopWatch.reset();
  }

  public boolean isRunning() { return stopWatch.isRunning(); }

  public long getRunningTime() {
    return stopWatch.getRuntime();
  }

  @EventHandler
  public void StartTasksEventHandler(StartTasksEvent event) {
    start();
  }

  @EventHandler
  public void StopTasksEventHandler(StopTasksEvent event) { stop(); }
}
