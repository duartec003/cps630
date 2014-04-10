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

public class PaymentFragment extends Fragment {
	OnPaymentListener	mCallback;

	public interface OnPaymentListener {
		public void onPayment(String itemName, int level);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnPaymentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnPaymentListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String[] options = getArguments().getStringArray(
				MenuDisplayActivity.MENU_DISPLAY_CONTENTS);

		View v = inflater.inflate(R.layout.activity_payment, container, false);
		// do stuff with view
		return v;
	}
}
