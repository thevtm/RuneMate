package com.OpenTasks.EventBus;

import com.OpenTasks.EventBus.Broadcaster.ItemEventBroadcaster;
import com.OpenTasks.EventBus.Broadcaster.SkillEventBroadcaster;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import com.runemate.game.api.script.framework.listeners.events.SkillEvent;

import java.lang.annotation.Annotation;

/**
 * Created by VTM on 14/4/2016.
 * Required for the RuneMate thingy to import some of the EnventBus classes.
 */
public class Dummy {

  /* Force EventHandler import */
  public EventHandlerDummy eventHandlerDummy;
  public class EventHandlerDummy implements EventHandler {
    @Override
    public Class<? extends Annotation> annotationType() {
      return null;
    }
  }

  /* Force ItemEventBroadcaster import */
  public ItemEventBroadcasterDummy itemEventBroadcasterDummy;
  public class ItemEventBroadcasterDummy extends ItemEventBroadcaster {
    @Override
    public void onItemRemoved(ItemEvent itemEvent) { }

    @Override
    public void onItemAdded(ItemEvent itemEvent) { }
  }

  /* Force SkillEventBroadcaster import */
  public SkillEventBroadcasterDummy skillEventBroadcasterDummy;
  public class SkillEventBroadcasterDummy extends SkillEventBroadcaster {
    @Override
    public void onLevelUp(SkillEvent skillEvent) { }

    @Override
    public void onExperienceGained(SkillEvent skillEvent) { }
  }


  public Dummy() {
    this.eventHandlerDummy = new EventHandlerDummy();
    this.itemEventBroadcasterDummy = new ItemEventBroadcasterDummy();
    this.skillEventBroadcasterDummy = new SkillEventBroadcasterDummy();
  }
}
