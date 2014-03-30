package com.mod.cps630app;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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

	private static final int	CONNECTION_FAILURE_RESOLUTION_REQUEST	= 9000;
	private static final int	MILLISECONDS_PER_SECOND					= 1000;
	public static final int		UPDATE_INTERVAL_IN_SECONDS				= 5;
	private static final long	UPDATE_INTERVAL							= MILLISECONDS_PER_SECOND
																				* UPDATE_INTERVAL_IN_SECONDS;
	private static final int	FASTEST_INTERVAL_IN_SECONDS				= 1;
	private static final long	FASTEST_INTERVAL						= MILLISECONDS_PER_SECOND
																				* FASTEST_INTERVAL_IN_SECONDS;
	private LocationClient		locationClient;
	private Location			currentLocation;
	private LocationRequest		locationRequest;

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
		((TextView) findViewById(R.id.dynamic_list_text_view))
				.append(currentLocation.toString());
		
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
		String msg = "Updated Location: "
				+ Double.toString(location.getLatitude()) + ","
				+ Double.toString(location.getLongitude());
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		((TextView) findViewById(R.id.dynamic_list_text_view)).append(location
				.toString());
	}
}
