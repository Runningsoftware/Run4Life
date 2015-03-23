package com.example.tawfiq.run4life;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tawfiq on 3/22/2015.
 * Run to manage the run with all its locations
 */
public class Run {

    private List<Location> Route;

    public Run() {
        Route = new ArrayList<>();
    }

    public List<Location> getRoute()
    {
        return Route;
    }

}
