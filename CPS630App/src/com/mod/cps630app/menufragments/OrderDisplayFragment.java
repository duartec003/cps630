package com.mod.cps630app.menufragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mod.cps630app.MenuDisplayActivity;
import com.mod.cps630app.R;
import com.mod.cps630app.sample.Order;

public class OrderDisplayFragment extends Fragment {
	ViewOrderListener	mCallback;

	public interface ViewOrderListener {
		public void onItemSelectedInOrder(int position);

		public void onCheckout();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (ViewOrderListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ViewOrderCallBackListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Order order = ((MenuDisplayActivity) getActivity()).getOrder();
		String[] orderList = new String[order.size()];
		for (int i = 0; i < order.size(); i++) {
			orderList[i] = order.get(i).getName();
		}
		View v = inflater.inflate(R.layout.order_view, container, false);
		ListView listView = (ListView) v
				.findViewById(R.id.order_view_order_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.menu_list_item, orderList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCallback.onItemSelectedInOrder(position);
			}
		});

		ImageButton b = (ImageButton) v.findViewById(R.id.check_out_button);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCallback.onCheckout();
			}
		});
		return v;
	}
}
