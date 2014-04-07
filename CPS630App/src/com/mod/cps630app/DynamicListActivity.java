package com.mod.cps630app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.XmlResourceParser;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class DynamicListActivity extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	private static final int			CONNECTION_FAILURE_RESOLUTION_REQUEST	= 9000;
	private static final int			MILLISECONDS_PER_SECOND					= 1000;
	public static final int				UPDATE_INTERVAL_IN_SECONDS				= 5;
	private static final long			UPDATE_INTERVAL							= MILLISECONDS_PER_SECOND
																						* UPDATE_INTERVAL_IN_SECONDS;
	private static final int			FASTEST_INTERVAL_IN_SECONDS				= 1;
	private static final long			FASTEST_INTERVAL						= MILLISECONDS_PER_SECOND
																						* FASTEST_INTERVAL_IN_SECONDS;
	private LocationClient				locationClient;
	private Location					currentLocation;
	private LocationRequest				locationRequest;

	private HashMap<String, Location>	locationMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamic_list);

		locationClient = new LocationClient(this, this, this);

		locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(UPDATE_INTERVAL);
		locationRequest.setFastestInterval(FASTEST_INTERVAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dynamic_list, menu);
		return true;
	}

	// This should be called by whatever should switch to the menu page
	// This isn't called anywhere yets
	public void onSelectLocation(View view) {
		Intent intent = new Intent(this, MenuDisplayActivity.class);
		intent.putExtra(MainActivity.LOCATION_DATA,
				"string representation of stuff");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CONNECTION_FAILURE_RESOLUTION_REQUEST:
				/*
				 * If the result code is Activity.RESULT_OK, try to connect
				 * again
				 */
				switch (resultCode) {
					case Activity.RESULT_OK:
						/*
						 * Try the request again
						 */
						break;
				}
		}

	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Get the error code
			int errorCode = 1/* connectionResult.getErrorCode() */;
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					errorCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				DialogFragment errorFragment = new DialogFragment();
				// Set the dialog in the DialogFragment
				// errorFragment.setShowsDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(getFragmentManager(), "Location Updates");
				return false;
			}
		}
		return false;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (IntentSender.SendIntentException e) {
				e.printStackTrace();
			}
		} else {
			DialogFragment errorFragment = new DialogFragment();
			errorFragment.show(getFragmentManager(),
					connectionResult.getErrorCode() + "");
		}

	}

	@Override
	public void onConnected(Bundle bundle) {
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		currentLocation = locationClient.getLastLocation();

		if (currentLocation != null) showClosestStores();
		else Toast.makeText(getApplicationContext(),
				"Can't connect to Google Play Services", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStart() {
		super.onStart();
		locationClient.connect();
	}

	@Override
	protected void onStop() {
		locationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onLocationChanged(Location location) {
		currentLocation = location;
		if (currentLocation != null) showClosestStores();
		else Toast.makeText(getApplicationContext(),
				"Can't connect to Google Play Services", Toast.LENGTH_SHORT).show();
		
	}

	private void showClosestStores() {
		String[] fullStoreNames = getStoreNames(getBuildingNames(getClosestBuildings()));
		final ListView list = (ListView) findViewById(R.id.dynamicListView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, fullStoreNames);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String itemValue = (String) list.getItemAtPosition(position);
				Toast.makeText(getApplication(), itemValue, Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(getApplicationContext(),
						MenuDisplayActivity.class);
				intent.putExtra(MainActivity.LOCATION_DATA, itemValue);
				startActivity(intent);
			}
		});

		// list.addView(new TextView(getParent()));
	}

	private String[] getStoreNames(String[] buildingNames) {
		ArrayList<String> list = new ArrayList<String>();
		for (String s : buildingNames) {
			String distStr = String.format(Locale.ENGLISH, " %.2f m",
					locationMap.get(s).distanceTo(currentLocation));
			for (String t : MainActivity.QUALIFIED_STORE_LIST) {
				if (t.startsWith(s)) list.add(t + distStr);
			}
		}
		return list.toArray(new String[list.size()]);

	}

	@SuppressWarnings("unchecked")
	private String[] getBuildingNames(
			PriorityQueue<Entry<String, Location>> closestBuildings) {
		String[] arr = new String[4];
		Entry<String, Location>[] cArr = closestBuildings
				.toArray(new Entry[closestBuildings.size()]);
		for (int i = 0; i < 4; i++) {
			arr[i] = cArr[3 - i].getKey();
		}
		return arr;
	}

	private PriorityQueue<Entry<String, Location>> getClosestBuildings() {
		if (locationMap == null) {
			createLocationMap();
		}
		PriorityQueue<Entry<String, Location>> closest = new PriorityQueue<Entry<String, Location>>(
				4, new ClosestLocationComparator(currentLocation));

		int i = 0;
		for (Entry<String, Location> e : locationMap.entrySet()) {
			if (i < 4) {
				closest.offer(e);
				i++;
			} else {
				Entry<String, Location> f = closest.peek();
				if (f.getValue().distanceTo(currentLocation) > e.getValue()
						.distanceTo(currentLocation)) {
					closest.poll();
					closest.offer(e);
				}
			}
		}
		System.out.println(closest.toString());
		return closest;
	}

	private void createLocationMap() {
		File locationCache = new File(getCacheDir(), "locCache");
		if (!locationCache.exists()) throw new IllegalStateException();
		locationMap = new HashMap<String, Location>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(locationCache));
			String line = br.readLine();
			while (line != null) {
				String[] arr = line.split(",");
				Location l = new Location("cps630");
				l.setLatitude(Double.parseDouble(arr[1]));
				l.setLongitude(Double.parseDouble(arr[2]));
				locationMap.put(arr[0], l);
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
