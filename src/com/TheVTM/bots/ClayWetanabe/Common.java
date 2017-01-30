package com.TheVTM.bots.ClayWetanabe;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;

/**
 * Created by VTM on 8/4/2016.
 */
public class Common {

    public static boolean hasRequiredItems() {
        boolean hasHardClay = Inventory.contains(Constants.CLAY);
        boolean hasVessel = Inventory.containsAnyOf(Constants.VESSELS());

        return hasHardClay && hasVessel;
    }
}
