package com.TheVTM.bots.BonePrayer;

import java.util.regex.Pattern;

/**
 * Created by VTM on 4/4/2016.
 */
public final class Constants {

    /* FILES */
    static final String LOG_PROPERTIES_PATH = "/com/TheVTM/bots/BonePrayer/log.properties";
    static final String LOG_DEV_PROPERTIES_PATH = "/com/TheVTM/bots/BonePrayer/log.dev.properties";
    public static final String LAYOUT_FXML = "/com/TheVTM/bots/BonePrayer/GUI/layout.fxml";

    /* BONES */
    public static final Pattern BONES_PATTERN = Pattern.compile(".*?[B|b]ones?");
}
