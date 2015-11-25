package com.corvolution.mesana.configurator;

import java.util.Comparator;

import com.corvolution.mesana.data.Measurement;

public class CustomComparator implements Comparator<Measurement>
{

	@Override
	public int compare(Measurement m1, Measurement m2)
	{

		return m1.getDate().compareTo(m2.getDate());
	}

}
