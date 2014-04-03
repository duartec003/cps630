package com.mod.cps630app.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.math.BigDecimal;

import com.mod.cps630app.Order;
import com.mod.cps630app.OrderItem;

public class Sample {
	public void ExampleOrderWriteRead() {
		Order o = new Order();
		o.addItem(new OrderItem().name("Coffee").cost(new BigDecimal("1.25"))
				.extra(new OrderItem().name("Cream")));

		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(new File(
					"getCacheDir()", "test")));
			oos.writeObject(o);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ObjectInputStream ois = null;
		Order oo = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File(
					"getCacheDir()", "test")));
			oo = (Order) ois.readObject();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (oo != null) System.out.println(oo.toString());
	}
}
