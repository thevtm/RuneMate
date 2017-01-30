package com.TheVTM.bots.ClayWetanabe;

/**
 * Created by VTM on 8/4/2016.
 */
public class Constants {

    /* ITEMS */
    public static final String CLAY = "Clay";
    public static final String SOFT_CLAY = "Soft clay";


    public static final String[] VESSELS_EMPTY = {"Bucket"};
    public static final String[] VESSELS_WITH_WATER = {"Bucket of water"};

    public static String[] VESSELS() {
        String[] r = new String[VESSELS_EMPTY.length + VESSELS_WITH_WATER.length];
        System.arraycopy(VESSELS_EMPTY, 0, r, 0, VESSELS_EMPTY.length);
        System.arraycopy(VESSELS_WITH_WATER, 0, r, VESSELS_EMPTY.length, VESSELS_WITH_WATER.length);

        return r;
    }

    /* OBJECTS */
    public static final String[] WATER_SOURCES = {"Waterpump", "Fountain"};

    /* FILES */
    public static final String LOG_PROPERTIES_PATH = "/com/TheVTM/bots/ClayWetanabe/log.properties";
    public static final String LOG_DEV_PROPERTIES_PATH = "/com/TheVTM/bots/ClayWetanabe/log.dev.properties";
    public static final String GUI_FXML_PATH = "/com/TheVTM/bots/ClayWetanabe/GUI/GUI.fxml";

}
