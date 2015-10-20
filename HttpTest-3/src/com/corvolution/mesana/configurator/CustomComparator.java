package com.corvolution.mesana.configurator;
import java.util.Comparator;

public class CustomComparator implements Comparator<Measurement> {

	@Override
	public int compare(Measurement m1, Measurement m2) {
		
		return m2.getDate().compareTo(m1.getDate());
	}

}
