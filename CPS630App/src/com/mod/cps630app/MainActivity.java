package com.mod.cps630app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mod.cps630app.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainActivity extends Activity {
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
