package com.mod.cps630app;

import java.math.BigDecimal;
import java.util.HashMap;

public class CostLookUp {
	public static HashMap<String, BigDecimal>	costLookupTable;

	public static BigDecimal costFor(String itemName) {
		return new BigDecimal("1.30");
	}
}
