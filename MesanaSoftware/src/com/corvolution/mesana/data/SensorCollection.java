package com.corvolution.mesana.data;

import java.util.List;
import com.corvolution.mesana.rest.RestApiConnector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SensorCollection extends RestApiConnector
{

	
	private List<SensorData> sensorList;
	TypeToken<List<SensorData>> token = new TypeToken<List<SensorData>>()
	{
	};

	public List<SensorData> getList()
	{
		return sensorList;
	}

	public void setList(String sURL)
	{
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		sensorList = gson.fromJson(getMethod(sURL), token.getType());
	}

}
