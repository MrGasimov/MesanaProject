package com.corvolution.cm2.connection;

import java.util.EventObject;

public class ConnectionEvent extends EventObject
{

	private static final long serialVersionUID = 1L;
	private boolean connectionState;
	private String path;
	private int nConnectedSensors;

	public ConnectionEvent(Object source, boolean state, String path, int num)
	{
		super(source);
		this.connectionState = state;
		this.path = path;
		this.nConnectedSensors = num;

	}

	public boolean getState()
	{
		return connectionState;
	}

	public String getSensorPath()
	{
		return path;
	}

	public int getNumOfConnectedSensors()
	{
		return nConnectedSensors;
	}
}
