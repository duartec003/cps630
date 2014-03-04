package com.mod.cps630app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class StaticListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_static_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.static_list, menu);
		return true;
	}

	// This should be called by whatever should switch to the menu page
	// This isn't called anywhere yet
	public void onSelectLocation(View view) {
		Intent intent = new Intent(this, MenuDisplayActivity.class);
		intent.putExtra(MainActivity.LOCATION_DATA,
				"string representation of stuff");
	}

}
