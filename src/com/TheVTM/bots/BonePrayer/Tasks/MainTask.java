package com.TheVTM.bots.BonePrayer.Tasks;

import com.OpenTasks.Logger;
import com.OpenTasks.Task;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.region.Players;

/**
 * Created by TheVTM on 26/1/2017.
 */
public class MainTask extends Task {

  public MainTask(String name, int priority) {
    super(name, priority);
  }

  @Override
  public boolean validate() {
    try {
      Player player = Players.getLocal();

      Logger.debug("Player is null: %b", player == null);

      return (player != null);

    } catch (RuntimeException e) {
      Logger.error("Unable to validate to get local player.", e);
      return false;
    }
  }
}
