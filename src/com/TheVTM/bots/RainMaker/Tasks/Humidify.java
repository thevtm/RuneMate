package com.TheVTM.bots.RainMaker.Tasks;

import com.OpenTasks.Logger;
import com.OpenTasks.Tasks.MetaTasks.BotStop;
import com.OpenTasks.Tasks.MicroTasks.MagicActivate;
import com.TheVTM.bots.RainMaker.Constants;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.osrs.local.hud.interfaces.Magic;

/**
 * Created by VTM on 5/7/2016.
 */
public class Humidify extends MagicActivate<Magic.Lunar> {

  public Humidify(int priority) {
    super(priority, Magic.Lunar.HUMIDIFY, Constants.HUMIDIFY_CASTABLE_TEXTURE_ID);
  }

  @Override
  public void configure(Object config) {

    // 0. Stop bot if its is not able to cast the spell
    addChild(new BotStop(0, () -> {

      // 1. Is not able to cast spell
      boolean isNotAbleToCastMagic = Magic.Lunar.HUMIDIFY.getComponent().getSpriteId() != Constants.HUMIDIFY_CASTABLE_TEXTURE_ID;

      Logger.debug("isNotAbleToCastMagic: %b",
          isNotAbleToCastMagic);

      return isNotAbleToCastMagic;
    }));

    // 10. Activate humidify
    addChild(MagicActivate.fromLunar(10, Magic.Lunar.HUMIDIFY, Constants.HUMIDIFY_CASTABLE_TEXTURE_ID));

    super.configure(config);
  }

  @Override
  public boolean validate() {
    try {
      // 1. Inventory must be full
      boolean isInventoryFull = Inventory.isFull();

      // 2. Has only Runes and Clay (Hard) in inventory
      boolean hasOnlyClayAndRunesInInventory = Inventory.containsOnly(Constants.CLAY_NAME, Constants.WATER_RUNE_NAME,
          Constants.FIRE_RUNE_NAME, Constants.ASTRAL_RUNE_NAME);

      // 3. Bank is closed
      boolean isBankClosed = !Bank.isOpen();

      // 4. Is idle
      boolean isIdle = Players.getLocal().getAnimationId() ==  -1;

      // 5. Super is valid
      boolean isSuperTrue = super.validate();

      Logger.debug("hasOnlyClayAndRunesInInventory: %b, BankClosed: %b, isIdle: %b, isSuperTrue: %b.",
          hasOnlyClayAndRunesInInventory, isBankClosed, isIdle, isSuperTrue);

      return isInventoryFull
          && hasOnlyClayAndRunesInInventory
          && isBankClosed
          && isIdle;

    } catch (RuntimeException e) {
      Logger.error("Failed to validate.", e);
      return false;
    }
  }

}
