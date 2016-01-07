package com.corvolution.mesana.utility.filter;

import java.util.Comparator;

import com.corvolution.mesana.data.Measurement;

/**This class is custom comparator for ordering two objects by priority.Ordering is accomplished as high,medium and low. 
 * @author Suleyman Gasimov
 */
public class HighFilter implements Comparator<Measurement>
{
	
	private final String highOrder = "HIGHMEDIUMLOW";
	
	/**
	 * This method compares two objects by their priority given by themselves. Ordering is done as following. HIGH MEDIUM LOW 
	 *
	 * @param m1 the m1
	 * @param m2 the m2
	 * @return int
	 */
	@Override
	public int compare(Measurement m1, Measurement m2)
	{
		return highOrder.indexOf(m1.getPriority()) - highOrder.indexOf(m2.getPriority());
	}

}
