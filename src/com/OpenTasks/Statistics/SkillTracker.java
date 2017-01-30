package com.OpenTasks.Statistics;

import com.OpenTasks.EventBus.Broadcaster.Events.ExperienceGainedEvent;
import com.OpenTasks.EventBus.Broadcaster.Events.LevelUpEvent;
import com.OpenTasks.EventBus.EventHandler;
import com.OpenTasks.EventBus.Events.StartTasksEvent;
import com.OpenTasks.EventBus.Events.StopTasksEvent;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.listeners.events.SkillEvent;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by VTM on 10/7/2016.
 */
public class SkillTracker extends StatisticsTracker {

  /* FIELDS */

  private Skill skill;
  private int levelsGained;
  private int expGained;

  /* METHODS */

  public SkillTracker(Skill skill) {
    this.skill = skill;

    levelsGained = 0;
    expGained = 0;
  }

  @EventHandler
  public void onLevelUp(LevelUpEvent levelUpEvent) {
    if (!isRunning()) return;

    SkillEvent skillEvent = levelUpEvent.event;

    if (skillEvent.getSkill() != skill)
      return;

    levelsGained += skillEvent.getChange();
  }

  @EventHandler
  public void onExperienceGained(ExperienceGainedEvent experienceGainedEvent) {
    if (!isRunning()) return;

    SkillEvent skillEvent = experienceGainedEvent.event;

    if (skillEvent.getSkill() != skill)
      return;

    expGained += skillEvent.getChange();
  }

  @Override
  public void reset() {
    super.reset();

    expGained = 0;
    levelsGained = 0;
  }

  public int getLevelsGained() {
    return levelsGained;
  }

  public int getExperienceGained() {
    return expGained;
  }

  public int getExpPerHour() {
    return (int) ((getExperienceGained() * 3600000D) / getRunningTime());
  }

  public Duration millisecondsToNextLevel() {
    double expToNextLevel = (double) skill.getExperienceToNextLevel();
    double expPerMillisecond = ((double) getExperienceGained()) / ((double) getRunningTime());
    Double msTNL = expToNextLevel / expPerMillisecond;

    // Return 0 if infinity
    if (msTNL.isInfinite())
      return Duration.ZERO;

    return Duration.ofMillis(msTNL.longValue());
  }

  public String timeToNextLevelAsString() {
    return timeToNextLevelAsString("HH:mm:ss");
  }

  public String timeToNextLevelAsString(String pattern) {
    Duration TNL = millisecondsToNextLevel();
    LocalTime t = LocalTime.MIDNIGHT.plus(TNL);
    return DateTimeFormatter.ofPattern(pattern).format(t);
  }

}
