package com.mod.cps630app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MenuDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_display);

		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.LOCATION_DATA);

		TextView t = (TextView) findViewById(R.id.menuDisplayTextView);
		t.setText(message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_display, menu);
		return true;
	}

}
