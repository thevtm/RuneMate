package com.TheVTM.bots.BonePrayer;

import com.OpenTasks.Statistics.SkillTracker;
import com.TheVTM.bots.BonePrayer.Events.ConfigurationEvent;
import com.TheVTM.bots.BonePrayer.Tasks.BuryTask;
import com.TheVTM.bots.BonePrayer.Tasks.DropJunkTask;
import com.TheVTM.bots.BonePrayer.Tasks.LootTask;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.framework.task.TaskBot;
import simpleeventbus.EventDispatcher;
import simpleeventbus.EventHandler;

/**
 * Created by VTM on 4/4/2016.
 */
public class BonePrayer extends com.OpenTasks.TaskBot {

  /* FIELDS */

  public UserConfiguration userConfiguration;
  public SkillTracker skillTracker;

  /* METHODS */

  public static BonePrayer GetInstance() {
    return (BonePrayer) Environment.getBot();
  }

  @Override
  public void onStart(String... a) {
    // 1.
    loadLogger();

    // 2.
    super.onStart(a);

    // 3.
    setLoopDelay(100, 200);

    // 4. Statistics
    skillTracker = new SkillTracker(Skill.PRAYER);
    getDispatcher().addHandler(skillTracker);

    // Initialize
    UserConfiguration userConfiguration = new UserConfiguration(Players.getLocal().getPosition(), 7);


  }

  public void configure(UserConfiguration config) {
    // 1. Create Tasks


  }

  public void loadLogger() {
    if (Environment.isSDK()) {
      com.OpenTasks.Logger logger = com.OpenTasks.Logger.fromJson(com.TheVTM.bots.RainMaker.Constants.LOGGER_CONFIG_PATH);
      setLogger(logger);

    } else {
      setLogger(new com.OpenTasks.Logger());
    }
  }

}
