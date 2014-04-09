package com.mod.cps630app;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

import com.mod.cps630app.util.JSONHelper;

public class MenuDisplayActivity extends FragmentActivity implements
		NavListFragment.OnItemSelectedListener {

	public static final String	MENU_DISPLAY_CONTENTS	= "com.mod.cps630.MENU_DISPLAY_CONTENTS";
	public static final String	MENU_LEVEL				= "com.mod.cps630.MENU_LEVEL";
	public static final String	BEV						= "Beverages";
	JSONObject					json;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_display);

		Intent intent = getIntent();
		// Should use message to choose which menu to load.
		String message = intent.getStringExtra(MainActivity.LOCATION_DATA);

		json = JSONHelper.createJSONObject(JSONHelper.getJSONString(
				getResources(), R.raw.tim_hortons));
		String[] topLevel = new String[json.length()];
		Iterator<?> iter = json.keys();
		int i = 0;
		while (iter.hasNext())
			topLevel[i++] = iter.next().toString();

		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) { return; }

			NavListFragment firstFragment = new NavListFragment();

			Bundle b = new Bundle();
			b.putStringArray(MENU_DISPLAY_CONTENTS, topLevel);
			b.putInt(MENU_LEVEL, 0);
			firstFragment.setArguments(b);

			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_display, menu);
		return true;
	}

	@Override
	// This is for fragment to activity to fragment communication
	public void onItemSelected(String itemName, int level) {
		String[] displayList = null;
		if (level == 0) {
			JSONObject jObj = null;
			try {
				jObj = json.getJSONObject(itemName);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			displayList = new String[jObj.length()];
			Iterator<?> iter = jObj.keys();
			int i = 0;
			while (iter.hasNext())
				displayList[i++] = iter.next().toString();

		} else if (level == 1) {
			JSONObject jObj = null;
			try {
				jObj = json.getJSONObject(BEV);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONArray jArr = null;
			try {
				jArr = jObj.getJSONArray(itemName);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			displayList = new String[jArr.length()];
			for (int i = 0; i < displayList.length; i++)
				try {
					displayList[i] = jArr.getString(i).toString();
				} catch (JSONException e) {
					e.printStackTrace();
				}
		} else if (level == 2) {
			Toast.makeText(this, "Added to order", Toast.LENGTH_SHORT).show();
			displayList = new String[] { BEV };
			level = -1;
		}
		NavListFragment frag = new NavListFragment();

		Bundle b = new Bundle();
		b.putStringArray(MENU_DISPLAY_CONTENTS, displayList);
		b.putInt(MENU_LEVEL, level + 1);
		frag.setArguments(b);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, frag).addToBackStack(null)
				.commit();
	}
}
