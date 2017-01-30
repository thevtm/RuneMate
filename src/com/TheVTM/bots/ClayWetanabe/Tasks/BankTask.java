package com.TheVTM.bots.ClayWetanabe.Tasks;

import com.TheVTM.bots.ClayWetanabe.ClayWetanabe;
import com.TheVTM.bots.ClayWetanabe.Common;
import com.TheVTM.bots.ClayWetanabe.Constants;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by VTM on 8/4/2016.
 *
 */
public class BankTask extends Task {

    /* LOGGER */
    private static final Logger LOGGER = Logger.getLogger(BankTask.class.getName());

    @Override
    public boolean validate() {
        try {
            Player player = Players.getLocal();
            LocatableEntityQueryResults<GameObject> banks = GameObjects.newQuery().actions("Bank").results();

            return !Common.hasRequiredItems()
                && banks != null && !banks.isEmpty() && banks.nearest().distanceTo(player) <= 7;

        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Unable to validate, returning false.", e);
            return false;
        }
    }

    @Override
    public void execute() {

        // Open bank
        if(!Bank.isOpen()) {
            LOGGER.log(Level.INFO, "Opening bank.");

            if(Bank.open()) {
                Execution.delayUntil(Bank::isOpen, 1000, 2000);
            }

            return;
        }

        // Deposit everything but vessels
        if(Inventory.contains(Constants.SOFT_CLAY)) {
            LOGGER.log(Level.INFO, "Depositing everything except vessels in the bank.");

            if(Bank.depositAllExcept(Constants.VESSELS())) {
                Execution.delayUntil(() -> Inventory.containsOnly(Constants.VESSELS()));
            }

            return;
        }

        // Withdraw a vessel
        if(!Inventory.containsAnyOf(Constants.VESSELS())) { // No vessel in inventory
            SpriteItem vesselInBank = Bank.newQuery().names(Constants.VESSELS()).results().first();

            if(vesselInBank != null) { // Has vessel in bank
                LOGGER.log(Level.INFO, String.format("Withdrawing one vessel \"%s\" from the bank.",
                    vesselInBank.getDefinition().getName()));

                if(Bank.withdraw(vesselInBank, 1)) {
                    Execution.delayUntil(() -> Inventory.contains(vesselInBank.getDefinition().getName()),
                            1000, 2000);
                }

            } else {    // No vessel in bank
                LOGGER.log(Level.SEVERE, "Unable to find any valid vessel in the bank. Stopping bot.");
                // TODO: Logout
                ClayWetanabe.GetInstance().stop();
            }

            return;
        }

        // Withdraw clay from bank
        if(Bank.containsAnyOf(Constants.CLAY)) { // Has clay in bank
            SpriteItem clay = Bank.newQuery().names(Constants.CLAY).results().first();

            LOGGER.log(Level.INFO, "Withdrawing clay from bank.");

            if(clay.interact("Withdraw-All")) {
                Execution.delayUntil(() -> Inventory.contains(Constants.CLAY), 1000, 2000);
            }

            return;

        } else { // No clay found in bank
            LOGGER.log(Level.INFO, "Not found any clay in inventory. Stopping bot.");
            ClayWetanabe.GetInstance().stop();

            return;
        }
    }

}
