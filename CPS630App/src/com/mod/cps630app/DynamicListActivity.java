package com.mod.cps630app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
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
		this.setContentView(R.layout.activity_dynamic_list);

		this.locationClient = new LocationClient(this, this, this);

		this.locationRequest = LocationRequest.create();
		this.locationRequest
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		this.locationRequest.setInterval(UPDATE_INTERVAL);
		this.locationRequest.setFastestInterval(FASTEST_INTERVAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.dynamic_list, menu);
		return true;
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
			errorFragment.show(this.getFragmentManager(),
					connectionResult.getErrorCode() + "");
		}

	}

	@Override
	public void onConnected(Bundle bundle) {
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		this.currentLocation = this.locationClient.getLastLocation();

		if (this.currentLocation != null) {
			this.showClosestStores();
		} else {
			Toast.makeText(this.getApplicationContext(),
					"Can't connect to Google Play Services", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.locationClient.connect();
	}

	@Override
	protected void onStop() {
		this.locationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onLocationChanged(Location location) {
		this.currentLocation = location;
		if (this.currentLocation != null) {
			this.showClosestStores();
		} else {
			Toast.makeText(this.getApplicationContext(),
					"Can't connect to Google Play Services", Toast.LENGTH_SHORT)
					.show();
		}

	}

	private void showClosestStores() {
		String[] fullStoreNames = this.getStoreNames(this.getBuildingNames(this
				.getClosestBuildings()));
		final ListView list = (ListView) this
				.findViewById(R.id.dynamicListView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, fullStoreNames);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String itemValue = (String) list.getItemAtPosition(position);
				Toast.makeText(DynamicListActivity.this.getApplication(),
						itemValue, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(DynamicListActivity.this
						.getApplicationContext(), MenuDisplayActivity.class);
				intent.putExtra(MainActivity.LOCATION_DATA, itemValue);
				DynamicListActivity.this.startActivity(intent);
			}
		});

		// list.addView(new TextView(getParent()));
	}

	private String[] getStoreNames(String[] buildingNames) {
		ArrayList<String> list = new ArrayList<String>();
		for (String s : buildingNames) {
			String distStr = String.format(Locale.ENGLISH, " %.2f m",
					this.locationMap.get(s).distanceTo(this.currentLocation));
			for (String t : MainActivity.QUALIFIED_STORE_LIST) {
				if (t.startsWith(s)) {
					list.add(t + distStr);
				}
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
		if (this.locationMap == null) {
			this.createLocationMap();
		}
		PriorityQueue<Entry<String, Location>> closest = new PriorityQueue<Entry<String, Location>>(
				4, new ClosestLocationComparator(this.currentLocation));

		int i = 0;
		for (Entry<String, Location> e : this.locationMap.entrySet()) {
			if (i < 4) {
				closest.offer(e);
				i++;
			} else {
				Entry<String, Location> f = closest.peek();
				if (f.getValue().distanceTo(this.currentLocation) > e
						.getValue().distanceTo(this.currentLocation)) {
					closest.poll();
					closest.offer(e);
				}
			}
		}
		System.out.println(closest.toString());
		return closest;
	}

	private void createLocationMap() {
		File locationCache = new File(this.getCacheDir(), "locCache");
		if (!locationCache.exists()) throw new IllegalStateException();
		this.locationMap = new HashMap<String, Location>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(locationCache));
			String line = br.readLine();
			while (line != null) {
				String[] arr = line.split(",");
				Location l = new Location("cps630");
				l.setLatitude(Double.parseDouble(arr[1]));
				l.setLongitude(Double.parseDouble(arr[2]));
				this.locationMap.put(arr[0], l);
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
