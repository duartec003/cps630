package com.mod.cps630app.obj;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;


public class Order implements Serializable {
	private static final long		serialVersionUID	= 1067788747481699795L;

	private ArrayList<OrderItem>	items;

	public Order() {
		items = new ArrayList<OrderItem>();
	}

	/**
	 * 
	 * @param item
	 *            to add
	 * @return the order for chaining
	 */
	public Order addItem(OrderItem item) {
		items.add(item);
		return this;
	}

	/**
	 * 
	 * @param i
	 *            index of order to add extra to
	 * @param extra
	 *            to add
	 * @return the order for chaining
	 */
	public Order addExtra(int i, OrderItem extra) {
		items.get(i).addExtra(extra);
		return this;
	}

	public BigDecimal getCost() {
		BigDecimal cost = new BigDecimal(0);
		for (OrderItem item : items) {
			cost = cost.add(item.getCost());
		}
		return cost;
	}

	public Order removeItem(int i) {
		items.remove(i);
		return this;
	}

	public Order removeLastItem() {
		items.remove(items.size() - 1);
		return this;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Order[Cost=");
		sb.append(getCost());
		sb.append(", ");
		for (OrderItem item : items) {
			sb.append(item.toString());
			sb.append(", ");
		}
		sb.setLength(sb.length() - 2);
		return sb.toString();
	}

	public int size() {
		return items.size();
	}

	public OrderItem get(int i) {
		return items.get(i);
	}
}
