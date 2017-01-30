package com.TheVTM.bots;

/**
 * Created by VTM on 6/4/2016.
 */
public class Util {


    public static String formatTime(long millis) {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60));

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}
