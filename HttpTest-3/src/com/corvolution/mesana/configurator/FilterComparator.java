package com.corvolution.mesana.configurator;
import java.util.Comparator;

public class FilterComparator implements Comparator<Measurement> {
	
	
	@Override
	public int compare(Measurement m1, Measurement m2) {
		// TODO Auto-generated method stub
		return m1.getPriority().compareTo(m2.getPriority());
	}

}
