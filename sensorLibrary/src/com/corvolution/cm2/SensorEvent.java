package com.corvolution.cm2;

import java.util.EventObject;

public class SensorEvent extends EventObject
{

	private boolean state;
	private String path;
	private int numOfSensors;

	public SensorEvent(Object source, boolean state, String path, int num)
	{
		super(source);
		this.state = state;
		this.path = path;
		this.numOfSensors = num;

	}

	public boolean getState()
	{
		return state;
	}

	public String getSensorPath()
	{
		return path;
	}

	public int getNumOfConnectedSensors()
	{
		return numOfSensors;
	}
}
