package com.corvolution.mesana.data;
import java.util.Collections;
import java.util.List;
import com.corvolution.mesana.configurator.CustomComparator;
import com.corvolution.mesana.configurator.HighFilter;
import com.corvolution.mesana.configurator.LowFilter;
import com.corvolution.mesana.configurator.MediumFilter;
import com.corvolution.mesana.rest.RestApiConnector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**MeasurementCollection - This class represents object holding measurement services needed for sensor configuration.
 * @author Suleyman Gasimov
 *
 */
public class MeasurementCollection extends RestApiConnector
{

	private List<Measurement> measList;

	TypeToken<List<Measurement>> token = new TypeToken<List<Measurement>>()
	{
	};

	/**getList - returns list of holding measurement objects.
	 * @return List - holds measurement objects
	 */
	public List<Measurement> getList()
	{
		return measList;
	}

	/**
	 * sortListbyDate - This method sorts list which holds measurement objects according to customer comparator.
	 *
	 * @return void
	 */
	public void sortListbyDate()
	{
		Collections.sort(getList(), new CustomComparator());

	}

	/**
	 * highPriorityfilter - This method sorts list which holds measurement objects according to high filter comparator.
	 *
	 * @return void
	 */
	public void highPriorityfilter()
	{
		Collections.sort(measList, new HighFilter());
	}
	
	/**
	 * mediumPriorityfilter - This method sorts list which holds measurement objects according to medium filter comparator.
	 *
	 * @return void
	 */
	public void mediumPriorityfilter()
	{
		Collections.sort(measList, new MediumFilter());
	}
	
	/**
	 * lowPriorityfilter - This method sorts list which holds measurement objects according to low filter comparator.
	 *
	 * @return void
	 */
	public void lowPriorityfilter()
	{
		Collections.sort(measList, new LowFilter());
	}

	/**getMesID(int index) - returns id of the measurement object from the list at the given index.
	 * @param index of the measurement object in the list
	 * @return String id - id of the measurement object at given index
	 */
	public String getMesID(int index)
	{
		String id = measList.get(index).getId();
		return id;
	}

	/**
	 * setList(String url) - This method initializes all measurement objects and stores them in the list.
	 *
	 * @param url the new list
	 * @return void
	 */
	public void setList(String url)
	{
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		measList = gson.fromJson(getMethod(url), token.getType());

	}

}
