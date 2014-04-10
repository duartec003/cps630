package com.mod.cps630app.menufragments;

import com.mod.cps630app.MenuDisplayActivity;
import com.mod.cps630app.R;
import com.mod.cps630app.R.id;
import com.mod.cps630app.R.layout;
import com.mod.cps630app.obj.OrderItem;
import com.mod.cps630app.sample.Order;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ConfirmationFragment extends Fragment {

	private MenuDisplayActivity	mCallback;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (MenuDisplayActivity) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnPaymentListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_confirmation, container,
				false);
		Order order = mCallback.getOrder();
		ListView orderSummary = (ListView) v
				.findViewById(R.id.confirmation_order_contents);
		String[] arr = new String[order.size()];
		for (int i = 0; i < order.size(); i++) {
			OrderItem curr = order.get(i);
			arr[i] = curr.getName();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.confirmation_list_item, arr);
		orderSummary.setAdapter(adapter);
		order = null;
		return v;
	}
}
