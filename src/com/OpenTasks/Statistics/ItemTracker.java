package com.OpenTasks.Statistics;

import com.OpenTasks.EventBus.Broadcaster.Events.ItemAddedEvent;
import com.OpenTasks.EventBus.Broadcaster.Events.ItemRemovedEvent;
import com.OpenTasks.EventBus.EventHandler;
import com.OpenTasks.EventBus.Events.StartTasksEvent;
import com.OpenTasks.EventBus.Events.StopTasksEvent;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;

import java.util.function.Predicate;

/**
 * Created by VTM on 12/7/2016.
 */
public class ItemTracker extends StatisticsTracker {

  /* FIELDS */

  private Predicate<ItemEvent> predicate;
  private int total;

  /* METHODS */

  public ItemTracker(Predicate<ItemEvent> predicate) {
    super();

    this.predicate = predicate;
    total = 0;
  }

  @EventHandler
  public void onItemAdded(ItemAddedEvent itemAddedEvent) {
    if (!isRunning()) return;

    ItemEvent itemEvent = itemAddedEvent.event;

    if (!predicate.test(itemEvent)) return;

    total += itemEvent.getQuantityChange();
  }

  @EventHandler
  public void onItemRemoved(ItemRemovedEvent itemRemovedEvent) {
    if (!isRunning()) return;

    ItemEvent itemEvent = itemRemovedEvent.event;

    if (!predicate.test(itemEvent)) return;

    total -= itemEvent.getQuantityChange();
  }

  @Override
  public void reset() {
    super.reset();
    total = 0;
  }

  public int getTotal() {
    return total;
  }

  public int getPerHour() {
    return (int) ((getTotal() * 3600000D) / getRunningTime());
  }

}
