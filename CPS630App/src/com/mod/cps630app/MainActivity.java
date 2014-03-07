package com.mod.cps630app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_display, menu);
		return true;
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
