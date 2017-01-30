package com.OpenTasks.EventBus.Broadcaster;

import com.OpenTasks.EventBus.Broadcaster.Events.ItemAddedEvent;
import com.OpenTasks.EventBus.Broadcaster.Events.ItemRemovedEvent;
import com.OpenTasks.TaskBot;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;

/**
 * Created by TheVTM on 22/1/2017.
 */
public class ItemEventBroadcaster implements InventoryListener {

  @Override
  public void onItemRemoved(ItemEvent itemEvent) {
    TaskBot.GetInstance().getDispatcher().publish(new ItemRemovedEvent(itemEvent));
  }

  @Override
  public void onItemAdded(ItemEvent itemEvent) {
    TaskBot.GetInstance().getDispatcher().publish(new ItemAddedEvent(itemEvent));
  }
}
