package com.OpenTasks.Tasks.MicroTasks;

import com.OpenTasks.Logger;
import com.OpenTasks.Task;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;

import java.util.concurrent.Callable;

/**
 * Created by VTM on 8/7/2016.
 */
public class BankClose extends Task {

  private Callable<Boolean> validateFn;

  public BankClose(int priority, Callable<Boolean> validateFn) {
    super("BankClose", priority);
    this.validateFn = validateFn;
  }

  @Override
  public boolean validate() {
    try {
      // 1. Bank is open
      boolean isBankOpen = Bank.isOpen();

      // 2. *validateFn* is true
      boolean isValidateFnTrue = validateFn.call();

      Logger.debug("isBankOpen: %b, isValidateFnTrue: %b", isBankOpen, isValidateFnTrue);

      return isBankOpen
          && isValidateFnTrue;

    } catch (Exception e) {
      Logger.error("Unable to validate.", e);
      return false;
    }
  }

  @Override
  public void execute() {
    Logger.debug("Closing bank...");

    Bank.close();

    super.execute();
  }
}
