package com.TheVTM.bots.BonePrayer.Events;

import com.TheVTM.bots.BonePrayer.UserConfiguration;

/**
 * Created by VTM on 4/4/2016.
 */
public class ConfigurationEvent {
    public UserConfiguration userConfiguration;

    public ConfigurationEvent(UserConfiguration userConfiguration) {
        this.userConfiguration = userConfiguration;
    }
}
