package com.OpenTasks.Tasks.MetaTasks;

import com.OpenTasks.Logger;
import com.OpenTasks.Task;
import com.runemate.game.api.hybrid.Environment;

import java.util.concurrent.Callable;

/**
 * Created by VTM on 10/7/2016.
 */
public class BotStop extends Task {

  private Callable<Boolean> validation;
  private Callable<Void> execute;

  public BotStop(int priority, Callable<Boolean> validation) {
    super("BotStop", priority);
    this.validation = validation;
  }

  public BotStop(int priority, Callable<Boolean> validation, Callable<Void> execute) {
    super("BotStop", priority);
    this.validation = validation;
  }

  @Override
  public boolean validate() {
    try {
      return validation.call();

    } catch (Exception e) {
      Logger.error("Unable to validate.", e);
      return false;
    }
  }

  @Override
  public void execute() {

    Logger.debug("Stopping bot...");

    try {
      if (execute != null) {
        execute.call();
      }
    } catch (Exception e) {
      Logger.error("*execute* has thrown an exception.", e);
    }

    Environment.getBot().stop();
  }

  // TODO: Hide addChild method.
}
