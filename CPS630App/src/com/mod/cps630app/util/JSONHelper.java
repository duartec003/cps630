package com.mod.cps630app.util;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;

public class JSONHelper {
	public static JSONObject createJSONObject(String jsonString) {
		try {
			return new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		throw new NullPointerException("JSON object not created");
	}

	public static String getJSONString(Resources res, int resId) {
		try {
			InputStream in_s = res.openRawResource(resId);

			byte[] b = new byte[in_s.available()];
			in_s.read(b);
			return new String(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new NullPointerException("JSON string not created");
	}
}
