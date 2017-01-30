package com.TheVTM.bots.RainMaker.GUI;

import com.OpenTasks.Statistics.ItemTracker;
import com.OpenTasks.Statistics.ProfitTracker;
import com.OpenTasks.Statistics.SkillTracker;
import com.TheVTM.bots.RainMaker.RainMaker;
import com.TheVTM.bots.RainMaker.UserConfiguration;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.script.framework.core.LoopingThread;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

/**
 * Created by VTM on 11/7/2016.
 */
public class RainMakerController implements Initializable {

  /* FXML */

  @FXML
  private Label timeRunning;

  @FXML
  private Label currentLevel;

  @FXML
  private Label levelsGained;

  @FXML
  private Label experienceGained;

  @FXML
  private Label experiencePerHour;

  @FXML
  private Label timeToNextLevel;

  @FXML
  private Label profit;

  @FXML
  private Label profitPerHour;

  @FXML
  private Label itemPerHour;

  @FXML
  private CheckBox chbTakeBreaks;

  /* FIELDS */

  private RainMaker rainMaker;

  /* METHODS */

  public RainMakerController(RainMaker rainMaker) {
    this.rainMaker = rainMaker;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    UserConfiguration initialUserConfiguration = rainMaker.getUserConfiguration();

    // 1. Apply user config to UI
    chbTakeBreaks.setSelected(initialUserConfiguration.takeBreaks);

    // 2. On toggle take breaks checkbox reconfigure bot
    chbTakeBreaks.setOnAction(this::configure);

    // 3. Create update thread
    createStatisticsLoopingThread();
  }

  public void configure(ActionEvent event) {
    // 1. Get config from UI
    boolean takeBreaks = chbTakeBreaks.isSelected();

    // 2. Create config object
    UserConfiguration config = new UserConfiguration(takeBreaks);

    // 3. Pass the configuration to the bot
    rainMaker.getPlatform().invokeLater(() -> rainMaker.configure(config));
  }

  public void createStatisticsLoopingThread() {
//    Logger.debug("Creating looping thread...");

    rainMaker.getPlatform().invokeLater(
      () -> new LoopingThread(() -> {
        NumberFormat integerFormatter = NumberFormat.getIntegerInstance();

        /* Cache info from the bot (thread) */
        String botRuntimeAsString = rainMaker.getRuntimeAsString();

        // Magic
        SkillTracker magicST = rainMaker.skillTracker;

        String botCurrentLevel = Integer.toString(Skill.MAGIC.getBaseLevel());
        String botLevelsGained = Integer.toString(magicST.getLevelsGained());
        String botExperienceGained = integerFormatter.format(magicST.getExperienceGained());
        String botExperiencePerHour = integerFormatter.format(magicST.getExpPerHour());
        String botTimeToNextLevel = magicST.timeToNextLevelAsString("HH:mm:ss");

        // Profit
        ProfitTracker profitTracker = rainMaker.profitTracker;
        String botProfit = integerFormatter.format(profitTracker.getTotalProfit());
        String botProfitPerHour = integerFormatter.format(profitTracker.getProfitPerHour());

        // Item tracker
        ItemTracker itemTracker = rainMaker.itemTracker;
        String botItemPerHour = integerFormatter.format(itemTracker.getPerHour());

        Platform.runLater(() -> {
          timeRunning.setText(botRuntimeAsString);

          // Magic
          currentLevel.setText(botCurrentLevel);
          levelsGained.setText(botLevelsGained);
          experienceGained.setText(botExperienceGained);
          experiencePerHour.setText(botExperiencePerHour);
          timeToNextLevel.setText(botTimeToNextLevel);

          // Profit
//          profit.setText(botProfit);
          profitPerHour.setText(botProfitPerHour);

          // Item tracker
          itemPerHour.setText(botItemPerHour);
        });

      }, 500).start()
    );
  }

}
