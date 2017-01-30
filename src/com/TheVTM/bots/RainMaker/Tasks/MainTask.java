package com.TheVTM.bots.RainMaker.Tasks;

import com.OpenTasks.Logger;
import com.OpenTasks.Task;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.region.Players;

/**
 * Created by VTM on 5/7/2016.
 */
public class MainTask extends Task {

  public MainTask() {
    super("MainTask", 0);
  }

  @Override
  public void configure(Object config) {
    // TODO: Add antiban

    // 0. Bank Task
    addChild(new BankTask(0));

    // 10. Humidify task
    addChild(new Humidify(10));

    super.configure(config);
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
