package com.corvolution.mesana.data;
import java.util.Collections;
import java.util.List;

import com.corvolution.mesana.rest.RestApiConnector;
import com.corvolution.mesana.utility.filter.CustomComparator;
import com.corvolution.mesana.utility.filter.HighFilter;
import com.corvolution.mesana.utility.filter.LowFilter;
import com.corvolution.mesana.utility.filter.MediumFilter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**This class represents object holding measurement services needed for sensor configuration.
 * @author Suleyman Gasimov
 *
 */
public class MeasurementCollection extends RestApiConnector
{

	private List<Measurement> measList;

	TypeToken<List<Measurement>> token = new TypeToken<List<Measurement>>()
	{
	};

	/**Returns list of holding measurement objects.
	 * @return List - holds measurement objects
	 */
	public List<Measurement> getList()
	{
		return measList;
	}

	/**
	 * This method sorts list which holds measurement objects according to customer comparator.
	 *
	 */
	public void sortListbyDate()
	{
		Collections.sort(getList(), new CustomComparator());

	}

	/**
	 * This method sorts list which holds measurement objects according to high filter comparator.
	 *
	 */
	public void highPriorityfilter()
	{
		Collections.sort(measList, new HighFilter());
	}
	
	/**
	 * This method sorts list which holds measurement objects according to medium filter comparator.
	 *
	 */
	public void mediumPriorityfilter()
	{
		Collections.sort(measList, new MediumFilter());
	}
	
	/**
	 * This method sorts list which holds measurement objects according to low filter comparator.
	 *
	 */
	public void lowPriorityfilter()
	{
		Collections.sort(measList, new LowFilter());
	}

	/**Returns id of the measurement object from the list at the given index.
	 * @param index of the measurement object in the list
	 * @return String id - id of the measurement object at given index
	 */
	public String getMesID(int index)
	{
		String id = measList.get(index).getId();
		return id;
	}

	/**
	 * This method initializes all measurement objects and stores them in the list.
	 * @param url the new list
	 */
	public void setList(String url)
	{
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		measList = gson.fromJson(getMethod(url), token.getType());

	}

}
