package com.OpenTasks.Tasks.MicroTasks;

import com.OpenTasks.Logger;
import com.OpenTasks.Task;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;

import java.util.function.Predicate;

/**
 * Created by VTM on 7/7/2016.
 */
public class BankDepositAllExcept <T> extends Task {

  private T parameter;

  private BankDepositAllExcept(int priority, T parameter) {
    super("BankDepositAllExcept", priority);
    this.parameter = parameter;
  }

  public static BankDepositAllExcept<String[]> fromItemsName(int priority, String... itemsName) {
    return new BankDepositAllExcept<>(priority, itemsName);
  }

  public static BankDepositAllExcept<Integer[]> fromItemsID(int priority, Integer... itemsID) {
    return new BankDepositAllExcept<>(priority, itemsID);
  }

  public static BankDepositAllExcept<Predicate<SpriteItem>> fromPredicate(int priority, Predicate<SpriteItem> predicate) {
    return new BankDepositAllExcept<>(priority, predicate);
  }

  @Override
  public boolean validate() {
    try {
      // 1. Bank must be open
      boolean isBankOpen = Bank.isOpen();

      // 2. Inventory has any of the items it should deposit
      boolean containsAnyExcept;

      if (parameter instanceof String[]) {
        containsAnyExcept = Inventory.containsAnyExcept((String[]) parameter);
      } else if (parameter instanceof Integer[]) {
        containsAnyExcept = Inventory.containsAnyExcept((int[]) parameter);
      } else if (parameter instanceof Predicate<?>) {
        containsAnyExcept = Inventory.containsAnyExcept((Predicate<SpriteItem>) parameter);
      } else {
        throw new RuntimeException(String.format("Unknown *parameter* Type: %s", parameter.getClass().getName()));
      }

      Logger.debug("isBankOpen: %b, containsAnyExcept: %b", isBankOpen, containsAnyExcept);

      return isBankOpen
          && containsAnyExcept;

    } catch (Exception e) {
      Logger.error("Unable to validate.", e);
      return false;
    }
  }

  @Override
  public void execute() {
    Logger.debug("Depositing all except...");

    if (parameter instanceof String[]) {
      Bank.depositAllExcept((String[]) parameter);
    } else if (parameter instanceof Integer[]) {
      Bank.depositAllExcept((int[]) parameter);
    } else if (parameter instanceof Predicate<?>) {
      Bank.depositAllExcept((Predicate<SpriteItem>) parameter);
    } else {
      throw new RuntimeException(String.format("Unknown *parameter* Type: %s", parameter.getClass().getName()));
    }

    super.execute();
  }
}
