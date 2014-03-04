package com.mod.cps630app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	public static final String	LOCATION_DATA	= "com.mod.cps630app.LOCATION";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	public void showStaticLocations(View view) {
		Intent intent = new Intent(this, StaticListActivity.class);
		startActivity(intent);
	}

	public void showDynamicLocations(View view) {
		Intent intent = new Intent(this, DynamicListActivity.class);
		startActivity(intent);
	}
}
