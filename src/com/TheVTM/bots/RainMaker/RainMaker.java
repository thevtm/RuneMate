package com.TheVTM.bots.RainMaker;

import com.OpenTasks.Logger;
import com.OpenTasks.Statistics.ItemTracker;
import com.OpenTasks.Statistics.ProfitTracker;
import com.OpenTasks.Statistics.SkillTracker;
import com.OpenTasks.TaskBot;
import com.TheVTM.bots.RainMaker.GUI.RainMakerController;
import com.TheVTM.bots.RainMaker.Tasks.MainTask;
import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.util.Resources;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * Created by VTM on 5/7/2016.
 */
public class RainMaker extends TaskBot implements EmbeddableUI {

  /* FIELDS */

  public SkillTracker skillTracker;
  public ProfitTracker profitTracker;
  public ItemTracker itemTracker;

  private RainMakerController controller;
  private ObjectProperty<Node> botInterfaceProperty;

  private UserConfiguration userConfiguration;

  /* METHODS */

  public static RainMaker GetInstance() {
    return (RainMaker) TaskBot.GetInstance();
  }

  public RainMaker() {
    super();

    // 1. UI
    setEmbeddableUI(this);
  }

  @Override
  public void onStart(String... args) {
    // 1.
    loadLogger();

    // 2.
    super.onStart(args);

    // 3. Define loop delay
    setLoopDelay(100, 200);

    /* 4. Statistics */

    // 4.1 Magic Skill Tracker
    skillTracker = new SkillTracker(Skill.MAGIC);
    getDispatcher().addHandler(skillTracker);

    // 4.2 Profit Tracker
    profitTracker = new ProfitTracker(itemEvent -> {
      String itemName = itemEvent.getItem().getDefinition().getName();
      ItemEvent.Type type = itemEvent.getType();

      // 4.2.1 Clay (hard) removed
      if (itemName.equals(Constants.CLAY_NAME) && type == ItemEvent.Type.REMOVAL) {
        return true;
      }

      // 4.2.2 Soft clay added
      else if (itemName.equals(Constants.SOFT_CLAY_NAME) && type == ItemEvent.Type.ADDITION) {
        return true;
      }

      // 4.2.3 Astral rune removed
      else if (itemName.equals(Constants.ASTRAL_RUNE_NAME) && type == ItemEvent.Type.REMOVAL) {
        return true;
      }

      return false;
    });

    getDispatcher().addHandler(profitTracker);

    // 4.3 Clay Item Tracker
    itemTracker = new ItemTracker(itemEvent -> {
      String itemName = itemEvent.getItem().getDefinition().getName();
      ItemEvent.Type type = itemEvent.getType();

      return itemName.equals(Constants.SOFT_CLAY_NAME) && (type == ItemEvent.Type.ADDITION);
    });

    getDispatcher().addHandler(itemTracker);

    // 5. Load user configs
    userConfiguration = UserConfiguration.load();

    // 6. Configure bot
    configure(userConfiguration);

    // 7. Start tasks
    startTasks();
  }

  @Override
  public ObjectProperty<? extends Node> botInterfaceProperty() {
    if (botInterfaceProperty == null) {
      try {
        FXMLLoader loader = new FXMLLoader();
        controller = new RainMakerController(this);
        loader.setController(controller);
        Node node = loader.load(Resources.getAsStream("com/TheVTM/bots/RainMaker/GUI/layout.fxml"));
        botInterfaceProperty = new SimpleObjectProperty<>(node);

      } catch (Exception e) {
        throw new RuntimeException("Unable to load GUI.", e);
      }
    }

    return botInterfaceProperty;
  }

  public void configure(UserConfiguration config) {
    Logger.info("Configuring... config: %s", config);

    // 1. Create / set main task
    MainTask mainTask = new MainTask();
    mainTask.configure(config);
    setMainTask(mainTask);

    // 2. Save new configuration
    UserConfiguration.save(config);
  }

  public void loadLogger() {
    if (Environment.isSDK()) {
      Logger logger = Logger.fromJson(Constants.LOGGER_CONFIG_PATH);
      setLogger(logger);

    } else {
      setLogger(new Logger());
    }
  }

  public UserConfiguration getUserConfiguration() {
    return userConfiguration;
  }
}
