package com.TheVTM.bots.RainMaker.Tasks;

import com.OpenTasks.Logger;
import com.OpenTasks.Task;
import com.OpenTasks.Tasks.MetaTasks.BotStop;
import com.OpenTasks.Tasks.MicroTasks.*;
import com.TheVTM.bots.RainMaker.Constants;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.Players;

/**
 * Created by VTM on 5/7/2016.
 */
public class BankTask extends Task {

  public BankTask(int priority) {
    super("Bank", priority);
  }

  @Override
  public void configure(Object config) {

    // 0. Turn camera to bank task
    addChild(new CameraTurnConcurrentlyTo(0, () -> Banks.getLoaded().nearest()));

    // 10. Open bank task
    addChild(new BankOpen(10));

    // 20. Deposit all except runes and hard clay
    addChild(BankDepositAllExcept.fromItemsName(20, Constants.WATER_RUNE_NAME, Constants.FIRE_RUNE_NAME,
        Constants.ASTRAL_RUNE_NAME, Constants.CLAY_NAME ));

    // 25. Stop bot if has less than 27 clay in bank
    addChild(new BotStop(25, () -> {

      // 1. Bank is open
      boolean isBankOpen = Bank.isOpen();

      // 2. Inventory not full
      boolean isInventoryNotFull = !Inventory.isFull();

      // 3. Has less than 27 clay in bank
      boolean hasTooFewItemsInBank = Bank.getQuantity(Constants.CLAY_NAME) < 27;

      Logger.debug("isInventoryNotFull: %b, isBankOpen: %b, hasTooFewItemsInBank: %b",
          isInventoryNotFull, isBankOpen, hasTooFewItemsInBank);

      return isBankOpen
          && isInventoryNotFull
          && hasTooFewItemsInBank;
    }));

    // 30. Withdraw hard clay
    addChild(BankWithdrawFillInventory.fromItemName(30, Constants.CLAY_NAME));

    // 40. Close bank
    addChild(new BankClose(40, () -> Inventory.containsOnly(Constants.WATER_RUNE_NAME, Constants.FIRE_RUNE_NAME,
        Constants.ASTRAL_RUNE_NAME, Constants.CLAY_NAME)));


    super.configure(config);
  }

  @Override
  public boolean validate() {
    try {
      // 1. Inventory doesn't contain Clay (Hard)
      boolean hasNoClayInInventory = !Inventory.contains(Constants.CLAY_NAME);

      // 2. Inventory contains Soft Clay
      boolean hasSoftClayInInventory = Inventory.contains(Constants.SOFT_CLAY_NAME);

      // 3. OR bank is open
      boolean isBankOpen = Bank.isOpen();

      // 4. OR is casting Humidify
      boolean isCastingHumidify = Players.getLocal().getAnimationId() == Constants.HUMIDIFY_ANIMATION_ID;

      // 5. OR inventory is not full
      boolean isInventoryFull = !Inventory.isFull();

      Logger.debug("hasNoClayInInventory: %b OR hasSoftClayInInventory: %b OR isBankOpen: %b, isCastingHumidify: %b, isInventoryFull: %b",
          hasNoClayInInventory, hasSoftClayInInventory, isBankOpen, isCastingHumidify, isInventoryFull);

      return hasNoClayInInventory
          || hasSoftClayInInventory
          || isBankOpen
          || isCastingHumidify
          || isInventoryFull;

    } catch (Exception e) {
      Logger.error("Unable to validate.", e);
      return false;
    }
  }

}
