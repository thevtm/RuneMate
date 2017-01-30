package com.OpenTasks.Tasks.MicroTasks;

import com.OpenTasks.Logger;
import com.OpenTasks.Task;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.region.GameObjects;

/**
 * Created by VTM on 6/7/2016.
 */
public class BankOpen extends Task {

  public BankOpen(int priority) {
    super("BankOpen", priority);
  }

  @Override
  public boolean validate() {
    // 1. Bank must not be open already
    boolean isBankClosed = !Bank.isOpen();

    // 2. Has a visible bank to interact
    boolean hasVisibleBank = !GameObjects.newQuery().actions("Bank").visible().results().isEmpty();

    Logger.debug("isBankClosed: %b, hasVisibleBank: %b", isBankClosed, hasVisibleBank);

    return isBankClosed
        && hasVisibleBank;
  }

  @Override
  public void execute() {
    Logger.debug("Opening bank...");

    Bank.open();

    super.execute();
  }
}
