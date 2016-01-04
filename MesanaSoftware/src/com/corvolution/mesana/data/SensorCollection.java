package com.corvolution.mesana.data;

import java.util.List;
import com.corvolution.mesana.rest.RestApiConnector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**SensorCollection - This class represents object, holding SensorData instances.
 * @author Suleyman Gasimov
 *
 */
public class SensorCollection extends RestApiConnector
{
	
	/** The sensor list. */
	private List<SensorData> sensorList;
	
	/** The token. */
	TypeToken<List<SensorData>> token = new TypeToken<List<SensorData>>()
	{
	};

	/**getList() - returns list holding SensorData instances.
	 * @return List
	 */
	public List<SensorData> getList()
	{
		return sensorList;
	}

	/**
	 * setList(String sURL) - This method initializes sensorData instances and stores them in a list.
	 *
	 * @param sURL the new list
	 */
	public void setList(String sURL)
	{
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		sensorList = gson.fromJson(getMethod(sURL), token.getType());
	}

}
