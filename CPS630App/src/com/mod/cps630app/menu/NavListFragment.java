package com.mod.cps630app.menu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mod.cps630app.MenuDisplayActivity;
import com.mod.cps630app.R;

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
