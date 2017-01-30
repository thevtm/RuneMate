package com.TheVTM.bots.ClayWetanabe;

import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VTM on 8/4/2016.
 */
public class Location {

    /* FIELDS */
    private final String name;
    private final Area bankArea;
    private final Area waterSourceArea;

    private Location(String name, Area bankPos, Area waterSourcePos) {
        this.name = name;
        this.bankArea = bankPos;
        this.waterSourceArea = waterSourcePos;
    }

    public static List<Location> getLocations() {
        List<Location> locations = new ArrayList<>();

        /* VARROCK EAST */
        locations.add(new Location("Varrock East",
                new Area.Rectangular(new Coordinate(3250, 3422, 0), new Coordinate(3254, 3420, 0)),
                new Area.Rectangular(new Coordinate(3236, 3433, 0), new Coordinate(3241, 3432, 0))));

        /* FALADOR WEST */
        locations.add(new Location("Falador West",
            new Area.Rectangular(new Coordinate(2944, 3372, 0), new Coordinate(2946, 3368, 0)),
            new Area.Rectangular(new Coordinate(2949, 3384, 0), new Coordinate(2952, 3381, 0))));

        //locations.add(new Location("Varrock West", new Coordinate(3208, 3429, 0), new Coordinate(3182, 3438, 0)));

        return locations;
    }

    public String getName() {
        return name;
    }

    public Area getBankArea() {
        return bankArea;
    }

    public Area getWaterSourceArea() {
        return waterSourceArea;
    }
}
