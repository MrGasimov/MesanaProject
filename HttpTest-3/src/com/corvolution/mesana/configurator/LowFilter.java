package com.corvolution.mesana.configurator;

import java.util.Comparator;

public class LowFilter implements Comparator<Measurement> {
	final String highOrder = "LOWMEDIUMHIGH";

	@Override
	public int compare(Measurement m1, Measurement m2) {
		// TODO Auto-generated method stub
		return highOrder.indexOf(m1.getPriority()) - highOrder.indexOf(m2.getPriority());
	}
}
