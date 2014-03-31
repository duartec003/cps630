package com.mod.cps630app;

import java.util.Comparator;
import java.util.Map.Entry;

import android.location.Location;

public class ClosestLocationComparator implements
		Comparator<Entry<String, Location>> {
	private Location	origin;

	public ClosestLocationComparator(Location currentLocation) {
		super();
		origin = new Location(currentLocation);
	}

	@Override
	public int compare(Entry<String, Location> x, Entry<String, Location> y) {
		Float d1 = x.getValue().distanceTo(origin);
		Float d2 = y.getValue().distanceTo(origin);
		return -d1.compareTo(d2);
	}

}
