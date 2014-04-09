package com.mod.cps630app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NavListFragment extends Fragment {
	OnItemSelectedListener	mCallback;

	public interface OnItemSelectedListener {
		public void onItemSelected(String itemName, int level);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnItemSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnItemSelectedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String[] options = getArguments().getStringArray(
				MenuDisplayActivity.MENU_DISPLAY_CONTENTS);

		View v = inflater.inflate(R.layout.nav_list, container, false);
		ListView listView = (ListView) v.findViewById(R.id.nav_list_list_view);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.menu_list_item, options);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCallback.onItemSelected(
						((TextView) view).getText().toString(), getArguments()
								.getInt(MenuDisplayActivity.MENU_LEVEL));
			}
		});
		return v;
	}
}
