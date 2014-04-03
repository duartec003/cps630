package com.mod.cps630app;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderItem implements Serializable {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= -2678954447005150558L;
	/**
	 * Name of the item, doesn't have to be the name which will be displayed.
	 */
	private String					name;
	/**
	 * Cost of the item without any extras, big decimal for accuracy
	 */
	private BigDecimal				cost;
	/**
	 * Any extras that can get added to an order. Ex: Cream, Sugar, Espresso
	 * shot
	 */
	private ArrayList<OrderItem>	extras;

	public OrderItem() {
		extras = new ArrayList<OrderItem>();
		cost = new BigDecimal(0);
		name = "";
	}

	public BigDecimal getCost() {
		BigDecimal extraCost = new BigDecimal(0);
		if (extras != null) {
			for (OrderItem extra : extras) {
				if (extra != null) {
					BigDecimal cCost = extra.getCost();
					if (cCost != null) extraCost = extraCost.add(cCost);
				}
			}
		}
		return cost.add(extraCost);
	}

	public void addExtra(OrderItem extra) {
		extras.add(extra);
	}

	public OrderItem name(String name) {
		this.name = name;
		return this;
	}

	public OrderItem cost(BigDecimal cost) {
		this.cost = cost;
		return this;
	}

	public OrderItem extra(OrderItem extra) {
		extras.add(extra);
		return this;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Item[");
		sb.append("Name=");
		sb.append(getName());
		sb.append(", Cost=");
		sb.append(getCost());

		if (!extras.isEmpty()) {
			sb.append(", Extras[");
			for (OrderItem extra : extras) {
				if (extra != null) sb.append(extra.toString());
				sb.append(", ");
			}
			sb.setLength(sb.length() - 2);
			sb.append("]");
		}

		sb.setLength(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
}
