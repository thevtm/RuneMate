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
public class BankWithdrawFillInventory<T> extends Task {

  private T parameter;

  private BankWithdrawFillInventory(int priority, T parameter) {
    super("BankWithdrawFillInventory", priority);
    this.parameter = parameter;
  }

  public static BankWithdrawFillInventory<String> fromItemName(int priority, String itemName) {
    return new BankWithdrawFillInventory<>(priority, itemName);
  }

  public static BankWithdrawFillInventory<Integer> fromItemID(int priority, Integer itemID) {
    return new BankWithdrawFillInventory<>(priority, itemID);
  }

  public static BankWithdrawFillInventory<Predicate<SpriteItem>> fromPredicate(int priority, Predicate<SpriteItem> predicate) {
    return new BankWithdrawFillInventory<>(priority, predicate);
  }

  @Override
  public boolean validate() {
    try {
      // 1. Bank must be open
      boolean isBankOpen = Bank.isOpen();

      // 2. Inventory is not full
      boolean isInventoryNotFull = !Inventory.isFull();


      Logger.debug("isBankOpen: %b, isInventoryNotFull: %b", isBankOpen, isInventoryNotFull);

      return isBankOpen
        && isInventoryNotFull;

    } catch (Exception e) {
      Logger.debug("Unable to validate.", e);
      return false;
    }
  }

  @Override
  public void execute() {
    Logger.debug("Withdrawing from bank...");

    if (parameter instanceof String) {
      Bank.withdraw((String) parameter, 0);
    } else if (parameter instanceof Integer) {
      Bank.withdraw((Integer) parameter, 0);
    } else if (parameter instanceof Predicate<?>) {
      Bank.withdraw((Predicate<SpriteItem>) parameter, 0);
    } else {
      throw new RuntimeException(String.format("Unknown *parameter* Type: %s", parameter.getClass().getName()));
    }

    super.execute();
  }
}
