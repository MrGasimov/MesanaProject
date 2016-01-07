package com.corvolution.mesana.configurator;
import java.util.Comparator;

import com.corvolution.mesana.data.Measurement;

/**MediumFilter - This class is a custom comparator for ordering two objects by priority.Ordering is accomplished as medium, high and low.  
 * @author Suleyman Gasimov
 */
public class MediumFilter implements Comparator<Measurement>
{
	
	private final String highOrder = "MEDIUMHIGHLOW";
	
	/**
	 * This method compares two objects by their priority given by themselves.Ordering is done as following. MEDIUM HIGH LOW 
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
