package com.mod.cps630app.menu;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mod.cps630app.MenuDisplayActivity;
import com.mod.cps630app.Order;
import com.mod.cps630app.OrderItem;
import com.mod.cps630app.R;

public class PaymentFragment extends Fragment {
	private static final BigDecimal	TAX	= new BigDecimal("1.13");
	OnPaymentListener				mCallback;

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
		Order order = ((MenuDisplayActivity) mCallback).getOrder();
		View v = inflater.inflate(R.layout.activity_payment, container, false);
		ListView orderSummary = (ListView) v
				.findViewById(R.id.payment_order_summary);
		TextView orderCost = (TextView) v
				.findViewById(R.id.payment_order_total);

		String[] arr = new String[order.size()];
		for (int i = 0; i < order.size(); i++) {
			OrderItem curr = order.get(i);
			arr[i] = curr.getName() + "\t\t" + curr.getCost();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.confirmation_list_item, arr);
		orderSummary.setAdapter(adapter);
		orderCost.setText(orderCost.getText()
				+ order.getCost().multiply(TAX)
						.setScale(2, RoundingMode.CEILING).toString());

		return v;
	}
}
