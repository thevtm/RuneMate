package com.OpenTasks.EventBus.Events;

/**
 * Created by VTM on 7/4/2016.
 */
public class StartBotEvent {
    public final String[] string;

    public StartBotEvent(String... string) {
        this.string = string;
    }
}
