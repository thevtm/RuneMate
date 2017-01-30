package com.OpenTasks.EventBus;

import com.OpenTasks.EventBus.Broadcaster.Events.ExperienceGainedEvent;
import com.OpenTasks.EventBus.Broadcaster.Events.ItemAddedEvent;
import com.OpenTasks.EventBus.Broadcaster.Events.ItemRemovedEvent;
import com.OpenTasks.EventBus.Broadcaster.Events.LevelUpEvent;
import com.OpenTasks.EventBus.Broadcaster.ItemEventBroadcaster;
import com.OpenTasks.EventBus.Broadcaster.SkillEventBroadcaster;
import com.OpenTasks.Logger;
import com.OpenTasks.TaskBot;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventDispatcher {

  /* CLASSES */

  private class EventHandlerInfo {
    public WeakReference<Object> handler;
    public Method method;
  }

  private class EventTypeHandler {
    private Set<EventHandlerInfo> eventHandlerInfos = new HashSet<>();

    public boolean addHandler(EventHandlerInfo eventHandlerInfo) {
      return eventHandlerInfos.add(eventHandlerInfo);
    }

    public boolean removeHandler(EventHandlerInfo eventHandlerInfo) {
      return eventHandlerInfos.remove(eventHandlerInfo);
    }

    public Set<EventHandlerInfo> getEventHandlerInfos() {
      return eventHandlerInfos;
    }

    public boolean isEmpty() {
      return eventHandlerInfos.isEmpty();
    }
  }

  private class EventBroadcasterManager {
    private class EventBroadcasterReferenceCounter {
      private Class<?> eventBroadcasterType;
      private EventListener eventBroadcaster;
      private int counter = 0;

      public EventBroadcasterReferenceCounter(Class<?> eventBroadcasterType) {
        this.eventBroadcasterType = eventBroadcasterType;
      }

      public EventListener lock() {
        if (counter == 0)
          instantiate();

        counter += 1;

        return eventBroadcaster;
      }

      public void release() {
        counter -= 1;

        if (counter == 0)
          delete();
      }

      private void instantiate() {
        try {
          // 1. Construct event broadcaster
          Constructor<EventListener> constructor = (Constructor<EventListener>) eventBroadcasterType.getConstructor();

          // 2. Create a new instance of the broadcaster
          eventBroadcaster = constructor.newInstance();

          // 3. Add it to the event dispatcher
          TaskBot.GetInstance().getEventDispatcher().addListener(eventBroadcaster);

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      }

      private void delete() {
        eventBroadcaster = null;
        TaskBot.GetInstance().getEventDispatcher().removeListener(eventBroadcaster);
      }
    }

    private HashMap<Class<?>, EventBroadcasterReferenceCounter> broadcasters = new HashMap<>();

    public void lockBroadcaster(Class<?> eventBroadcasterType) {
      // 1. Get or create a EventBroadcasterReferenceCounter for *eventBroadcasterType*
      EventBroadcasterReferenceCounter eventBRC = broadcasters.computeIfAbsent(eventBroadcasterType, EventBroadcasterReferenceCounter::new);

      // 2. Lock it
      eventBRC.lock();
    }

    public void releaseBroadcaster(Class<?> eventBroadcasterType) {
      // 1. Get or create a EventBroadcasterReferenceCounter for *eventBroadcasterType*
      EventBroadcasterReferenceCounter eventBRC = broadcasters.get(eventBroadcasterType);

      // 2. Lock it
      eventBRC.release();
    }
  }

  /* FIELDS */

  private Map<Class<?>, EventTypeHandler> handlers = new HashMap<>();
  private Map<Class<?>, Class<?>> eventToBroadcaster =  new HashMap<>();
  EventBroadcasterManager broadcasterManager = new EventBroadcasterManager();

  // HACK: necessary for RuneMate to import some classes.
  private static final Dummy __dummy__ = new Dummy();


  /* METHODS */

  public EventDispatcher() {
    /* 1. Populate event -> broadcaster Map */
    eventToBroadcaster = new HashMap<>();

    // 1.1 ItemEventBroadcaster
    eventToBroadcaster.put(ItemAddedEvent.class, ItemEventBroadcaster.class);
    eventToBroadcaster.put(ItemRemovedEvent.class, ItemEventBroadcaster.class);

    // 1.2 SkillEventBroadcaster
    eventToBroadcaster.put(ExperienceGainedEvent.class, SkillEventBroadcaster.class);
    eventToBroadcaster.put(LevelUpEvent.class, SkillEventBroadcaster.class);

  }

  public void addHandler(final Object handler) {
    // 1. Iterate over methods of the *handler* object
    for (Method method : handler.getClass().getMethods()) {

      // 2. If method has @EventHandler add it
      if (method.getAnnotation(EventHandler.class) != null) {

        // 3. Make sure the method found has only one parameter
        Class<?>[] paramTypes = method.getParameterTypes();

        if (paramTypes.length == 1) {
          addTypeSpecificHandler(handler, paramTypes[0], method);
        }
      }
    }
  }

  private void addTypeSpecificHandler(final Object handler, final Class<?> type, Method method) {
    // 1. Get the Set for the *type*, if not found add it to the map
    EventTypeHandler eventTypeHandlers = handlers.computeIfAbsent(type,
      k -> {
      // 1.1 Check if this type of event uses a broadcaster
        Class<?> broadcasterType = eventToBroadcaster.get(type);

        if (broadcasterType != null) {
          // 1.2 Lock the broadcaster
          broadcasterManager.lockBroadcaster(broadcasterType);
        }

        // 1.3 Create a new EventTypeHandler
        return new EventTypeHandler();
    });

    // 2. Add it to the *type* Set
    EventHandlerInfo info = new EventHandlerInfo();
    info.handler = new WeakReference<Object>(handler);
    info.method = method;

    eventTypeHandlers.addHandler(info);
  }

  public void publish(Object event) {
    // 1. Get the Set for the *event* type provided
    EventTypeHandler eventTypeHandlers = handlers.get(event.getClass());

    // 1.1 If Set is not found return
    if (eventTypeHandlers == null)
      return;

    /* 2. Call event handlers */

    // 2.1 Create a list for the handlers that have been deleted
    Collection<EventHandlerInfo> invalidHandlers = new LinkedList<EventHandlerInfo>();

    // 2.2 Iterate over HandlersInfo
    for (EventHandlerInfo info : eventTypeHandlers.getEventHandlerInfos()) {

      // 2.3 Try to handle event
      boolean handled = false;
      Object handler = info.handler.get();

      if (handler != null) {
        try {
          info.method.invoke(handler, event);
          handled = true;

        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
          Logger.error("Unable to invoke handler method.", e);
        }
      }

      // 2.4 If not able to handle the event add handler to be removed
      if (!handled)
        invalidHandlers.add(info);
    }

    // 3. Remove invalid handlers
    for (EventHandlerInfo info : invalidHandlers) {
      eventTypeHandlers.removeHandler(info);
    }

    // 4. If all handlers for this type were invalidated remove it from the handlers Map
    if (eventTypeHandlers.isEmpty()) {

      // 4.1 Check if this type of event uses a broadcaster
      Class<?> broadcasterType = eventToBroadcaster.get(event.getClass());

      if (broadcasterType != null) {
        // 4.2 Release the broadcaster
        broadcasterManager.releaseBroadcaster(broadcasterType);
      }

      // 4.3 Remove it
      handlers.remove(event.getClass());
    }
  }

}

