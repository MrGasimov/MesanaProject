package com.corvolution.mesana.configurator;
import java.util.Comparator;
import com.corvolution.mesana.data.Measurement;

/**LowFilter - This class is a custom comparator for ordering two objects by priority.Ordering is accomplished as low, medium and high.  
 * @author Suleyman Gasimov
 */
public class LowFilter implements Comparator<Measurement>
{
	
	/** The high order. */
	final String highOrder = "LOWMEDIUMHIGH";
	
	/**
	 * This method compares two objects by their priority given by themselves.Ordering is done as following. LOW MEDIUM HIGH
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
