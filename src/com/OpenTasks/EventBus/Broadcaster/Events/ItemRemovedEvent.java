package com.OpenTasks.EventBus.Broadcaster.Events;

import com.runemate.game.api.script.framework.listeners.events.ItemEvent;

/**
 * Created by TheVTM on 22/1/2017.
 */
public class ItemRemovedEvent {
  public final ItemEvent event;

  public ItemRemovedEvent(ItemEvent event) {
    this.event = event;
  }
}
