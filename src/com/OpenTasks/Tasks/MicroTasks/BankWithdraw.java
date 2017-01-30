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
public class BankWithdraw <T> extends Task {

  /* FIELDS */

  private T parameter;
  private int quantity;

  /* METHODS */

  private BankWithdraw(int priority, T parameter, int quantity) {
    super("BankWithdraw", priority);
    this.parameter = parameter;
    this.quantity = quantity;
  }

  public static BankWithdraw<String> fromItemName(int priority, String itemName, int quantity) {
    return new BankWithdraw<>(priority, itemName, quantity);
  }

  public static BankWithdraw<Integer> fromItemID(int priority, Integer itemID, int quantity) {
    return new BankWithdraw<>(priority, itemID, quantity);
  }

  public static BankWithdraw<Predicate<SpriteItem>> fromPredicate(int priority, Predicate<SpriteItem> predicate, int quantity) {
    return new BankWithdraw<>(priority, predicate, quantity);
  }

  @Override
  public boolean validate() {
    try {
      // 1. Bank must be open
      boolean isBankOpen = Bank.isOpen();

      // 2. Inventory is not full
      boolean isInventoryNotFull = !Inventory.isFull();

      // 3. Doesn't have it in inventory
      boolean isItemNotInInventory;

      if (parameter instanceof String) {
        isItemNotInInventory = !Inventory.contains((String) parameter);
      } else if (parameter instanceof Integer) {
        isItemNotInInventory = !Inventory.contains((Integer) parameter);
      } else if (parameter instanceof Predicate<?>) {
        isItemNotInInventory = !Inventory.contains((Predicate<SpriteItem>) parameter);
      } else {
        throw new RuntimeException(String.format("Unknown *parameter* Type: %s", parameter.getClass().getName()));
      }

      Logger.debug("isBankOpen: %b, isInventoryNotFull: %b, isItemNotInInventory: %b", isBankOpen, isInventoryNotFull,
          isItemNotInInventory);

      return isBankOpen
          && isInventoryNotFull
          && isItemNotInInventory;

    } catch (Exception e) {
      Logger.debug("Unable to validate.", e);
      return false;
    }
  }

  @Override
  public void execute() {
    Logger.debug("Withdrawing from bank...");

    if (parameter instanceof String) {
      Bank.withdraw((String) parameter, quantity);
    } else if (parameter instanceof Integer) {
      Bank.withdraw((Integer) parameter, quantity);
    } else if (parameter instanceof Predicate<?>) {
      Bank.withdraw((Predicate<SpriteItem>) parameter, quantity);
    } else {
      throw new RuntimeException(String.format("Unknown *parameter* Type: %s", parameter.getClass().getName()));
    }

    super.execute();
  }
}
