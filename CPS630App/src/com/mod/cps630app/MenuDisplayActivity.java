package com.mod.cps630app;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mod.cps630app.menu.NavListFragment;
import com.mod.cps630app.menu.OrderDisplayFragment;
import com.mod.cps630app.menu.PaymentFragment;
import com.mod.cps630app.util.JSONHelper;

public class MenuDisplayActivity extends FragmentActivity implements
		NavListFragment.OnItemSelectedListener,
		OrderDisplayFragment.ViewOrderListener,
		PaymentFragment.OnPaymentListener {

	public static final String	MENU_DISPLAY_CONTENTS	= "com.mod.cps630.MENU_DISPLAY_CONTENTS";
	public static final String	MENU_LEVEL				= "com.mod.cps630.MENU_LEVEL";
	public static final String	BEV						= "Beverages";
	private JSONObject			json;
	private Order				order;
	private String				currentHeader;
	private Menu				gMenu;
	private Fragment			currentFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_display);

		// Intent intent = getIntent();
		// Should use message to choose which menu to load.
		// String message = intent.getStringExtra(MainActivity.LOCATION_DATA);

		json = JSONHelper.createJSONObject(JSONHelper.getJSONString(
				getResources(), R.raw.tim_hortons));
		if (order == null) order = new Order();
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
		gMenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.view_order) {
			if (order.size() == 0) {
				Toast.makeText(this, "You haven't added any items yet",
						Toast.LENGTH_SHORT).show();
				return true;
			}
			getActionBar().setTitle("Your Order");
			OrderDisplayFragment frag = new OrderDisplayFragment();

			Bundle b = new Bundle();
			frag.setArguments(b);

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, frag)
					.addToBackStack(null).commit();
			item.setVisible(false);
			gMenu.findItem(R.id.back_to_menu).setVisible(true);
		} else if (item.getItemId() == R.id.back_to_menu) {
			displayTopLevelMenu();
			item.setVisible(false);
			gMenu.findItem(R.id.view_order).setVisible(true);
		}
		return true;
	}

	private void displayTopLevelMenu() {
		NavListFragment frag = new NavListFragment();
		getActionBar().setTitle("Top Level");
		Bundle b = new Bundle();
		b.putStringArray(MENU_DISPLAY_CONTENTS, new String[] { BEV });
		b.putInt(MENU_LEVEL, 0);
		frag.setArguments(b);
		clearBackStack();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, frag).commit();
	}

	@Override
	// This is for fragment to activity to fragment communication
	public void onItemSelected(String itemName, int level) {
		getActionBar().setTitle(itemName);
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
			currentHeader = itemName;

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
			currentHeader = itemName;
		} else if (level == 2) {
			getActionBar().setTitle("Top Level");
			Toast.makeText(this, "Added to order", Toast.LENGTH_SHORT).show();
			order.addItem(new OrderItem().name(currentHeader + ": " + itemName)
					.cost(CostLookUp.costFor(itemName)));
			displayList = new String[] { BEV };
			level = -1;
		}
		NavListFragment frag = new NavListFragment();

		Bundle b = new Bundle();
		b.putStringArray(MENU_DISPLAY_CONTENTS, displayList);
		b.putInt(MENU_LEVEL, level + 1);
		frag.setArguments(b);

		if (level != -1) getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, frag).addToBackStack(null)
				.commit();
		else {
			clearBackStack();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, frag).commit();
		}
	}

	private void clearBackStack() {
		FragmentManager fm = getSupportFragmentManager();
		for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
			fm.popBackStack();
		}
	}

	public Order getOrder() {
		return order;
	}

	@Override
	public void onItemSelectedInOrder(int position) {
		order.removeItem(position);
		OrderDisplayFragment frag = new OrderDisplayFragment();

		Bundle b = new Bundle();
		frag.setArguments(b);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, frag).commit();
	}

	@Override
	public void onCheckout() {
		PaymentFragment frag = new PaymentFragment();
		getActionBar().setTitle("Payment");
		Bundle b = new Bundle();
		frag.setArguments(b);
		clearBackStack();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, frag).commit();
	}

	@Override
	public void onPayment() {
		ConfirmationFragment frag = new ConfirmationFragment();

		getActionBar().setTitle("Confirmation");
		Bundle b = new Bundle();
		frag.setArguments(b);
		clearBackStack();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, frag).commit();
		gMenu.findItem(R.id.back_to_start).setVisible(true);
		gMenu.findItem(R.id.back_to_menu).setVisible(false);
		gMenu.findItem(R.id.view_order).setVisible(false);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		currentFrag = fragment;
	}

	@Override
	public void onBackPressed() {
		if (currentFrag == null) return;
		if (currentFrag instanceof PaymentFragment
				|| currentFrag instanceof OrderDisplayFragment) {
			displayTopLevelMenu();

			gMenu.findItem(R.id.back_to_start).setVisible(false);
			gMenu.findItem(R.id.back_to_menu).setVisible(false);
			gMenu.findItem(R.id.view_order).setVisible(true);

		} else if (currentFrag instanceof ConfirmationFragment) {
			gMenu.findItem(R.id.back_to_menu).setVisible(false);
			gMenu.findItem(R.id.view_order).setVisible(false);
			gMenu.findItem(R.id.back_to_start).setVisible(false);
		}

	}
}
