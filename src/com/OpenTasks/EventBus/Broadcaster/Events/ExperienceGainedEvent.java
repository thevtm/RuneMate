package com.OpenTasks.EventBus.Broadcaster.Events;

import com.runemate.game.api.script.framework.listeners.events.SkillEvent;

/**
 * Created by TheVTM on 22/1/2017.
 */
public class ExperienceGainedEvent {
  public final SkillEvent event;

  public ExperienceGainedEvent(SkillEvent event) {
    this.event = event;
  }
}
