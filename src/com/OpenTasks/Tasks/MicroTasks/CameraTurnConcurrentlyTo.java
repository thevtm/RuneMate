package com.OpenTasks.Tasks.MicroTasks;

import com.OpenTasks.Logger;
import com.OpenTasks.Task;
import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.local.Camera;

import java.util.concurrent.Callable;

/**
 * Created by VTM on 6/7/2016.
 */
public class CameraTurnConcurrentlyTo extends Task {

  Callable<Interactable> filter;

  public CameraTurnConcurrentlyTo(int priority, Callable<Interactable> filter) {
    super("CameraTurnConcurrentlyTo", priority);

    this.filter = filter;
  }

  @Override
  public boolean validate() {
    try {
      Interactable interactable = filter.call();

      // 1. *interactable* is not null
      boolean interactableIsNotNull = interactable != null;

      // 2. *interactable* is not visible
      boolean interactableIsNotVisible = !interactable.isVisible();

      Logger.debug("interactableIsNotNull: %b, interactableIsNotVisible: %b",
          interactableIsNotNull, interactableIsNotVisible);

      return interactableIsNotNull
          && interactableIsNotVisible;

    } catch (Exception e) {
      Logger.error("Unable to validate.", e);
      return false;
    }
  }

  @Override
  public void execute() {
    try {
      Interactable interactable = filter.call();

      Logger.debug("Turning camera to %s...", interactable.toString());

      // Turn camera
      Camera.concurrentlyTurnTo((Locatable) interactable);

    } catch (Exception e) {
      Logger.error("Unable to execute.", e);
    }

    super.execute();
  }

}
