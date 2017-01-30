package com.OpenTasks.EventBus.Broadcaster;

import com.OpenTasks.EventBus.Broadcaster.Events.ExperienceGainedEvent;
import com.OpenTasks.EventBus.Broadcaster.Events.LevelUpEvent;
import com.OpenTasks.TaskBot;
import com.runemate.game.api.script.framework.listeners.SkillListener;
import com.runemate.game.api.script.framework.listeners.events.SkillEvent;

/**
 * Created by TheVTM on 22/1/2017.
 */
public class SkillEventBroadcaster implements SkillListener {

  @Override
  public void onLevelUp(SkillEvent skillEvent) {
    TaskBot.GetInstance().getDispatcher().publish(new LevelUpEvent(skillEvent));
  }

  @Override
  public void onExperienceGained(SkillEvent skillEvent) {
    TaskBot.GetInstance().getDispatcher().publish(new ExperienceGainedEvent(skillEvent));
  }
}
