package com.mod.cps630app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class DynamicListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamic_list);
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

}
