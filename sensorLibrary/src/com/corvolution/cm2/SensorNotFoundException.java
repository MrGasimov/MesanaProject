package com.corvolution.cm2;

public class SensorNotFoundException extends Exception
{
	private String message;
	/**
	 * 
	 */
	private static final long serialVersionUID = -3666409772339775926L;

	public SensorNotFoundException()
	{

	}
	
	public SensorNotFoundException(String message)
	{
		super(message);
		this.message = message;
	}
	
	public SensorNotFoundException(Throwable cause)
	{
		super(cause);
	}
	
	public SensorNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message,cause,enableSuppression,writableStackTrace);
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}

		
}
