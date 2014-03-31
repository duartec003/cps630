package com.mod.cps630app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	public static final String		LOCATION_DATA			= "com.mod.cps630app.LOCATION";

	/** Because this is easier for now **/
	public static final String[]	QUALIFIED_STORE_LIST	= new String[] {
			"Ted Rogers.7.Tim Hortons", "Ted Rogers.9.Tim Hortons",
			"Kerr Hall.2.Tim Hortons", "Library.1.Tim Hortons",
			"Engineering Building.1.Tim Hortons",
			"Image Arts Building.1.Balzacs", "Eric Palin Hall.1.Expressos" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);
		readLocationsXML();

	}

	private void readLocationsXML() {
		File locationCache = new File(getCacheDir(), "locCache");
		BufferedWriter fos = null;
		try {
			fos = new BufferedWriter(new FileWriter(locationCache));
			fos.write("");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		XmlResourceParser locationsXml = getResources().getXml(R.xml.locations);

		int eventType;
		try {
			eventType = locationsXml.getEventType();
		} catch (XmlPullParserException e) {
			eventType = 1;
			e.printStackTrace();
		}
		try {
			String text = null;
			String[] data = new String[3];
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String tagName = locationsXml.getName();
					if (tagName.equals("Buildings")) {
						if (data[0] != null) {
							fos.append(data[0] + "," + data[1] + "," + data[2]);
							fos.newLine();
							data = new String[3];
						}
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					String tagName = locationsXml.getName();
					if (tagName.equals("Name")) data[0] = text;
					if (tagName.equals("latitude")) data[1] = text;
					if (tagName.equals("longitude")) data[2] = text;
				} else if (eventType == XmlPullParser.TEXT) {
					text = locationsXml.getText();
				}

				eventType = locationsXml.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
