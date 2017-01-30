package com.TheVTM.bots.BonePrayer;

import com.runemate.game.api.hybrid.location.Coordinate;

/**
 * Created by VTM on 4/4/2016.
 */
public class UserConfiguration {

    /* STARTING POSITION */
    public Coordinate startingPosition;

    /* MAX DISTANCE */
    public static final int MAX_DISTANCE_DEFAULT = 10;
    public static final int MAX_DISTANCE_MIN = 5;
    public static final int MAX_DISTANCE_MAX = 50;
    public int maxDistance;

    public UserConfiguration(Coordinate startingPosition, int maxDistance) {
        this.startingPosition = startingPosition;
        this.maxDistance = maxDistance;
    }
}
