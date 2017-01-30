package com.OpenTasks.EventBus.Broadcaster.Events;

import com.runemate.game.api.script.framework.listeners.events.SkillEvent;

/**
 * Created by TheVTM on 22/1/2017.
 */
public class LevelUpEvent {
  public final SkillEvent event;

  public LevelUpEvent(SkillEvent event) {
    this.event = event;
  }
}
