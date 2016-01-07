package com.corvolution.mesana.configurator;
import java.util.Comparator;
import com.corvolution.mesana.data.Measurement;

/**Custom Comparator class which implements Comparator Interface. Instance of this class is used to compare two objects according to date.
 * @author Suleyman Gasimov
 *
 */
public class CustomComparator implements Comparator<Measurement>
{
	
	/**
	 * This method compares objects according to date.
	 *
	 * @param m1 the m1
	 * @param m2 the m2
	 * @return Returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
	 */
	public int compare(Measurement m1, Measurement m2)
	{

		return m1.getDate().compareTo(m2.getDate());
	}

}
