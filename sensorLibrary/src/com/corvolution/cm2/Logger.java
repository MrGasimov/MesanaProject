package com.corvolution.cm2;

public final class Logger
{
	public static String log=null;
	private static Logger instance = null;
	
	public void printLog(String msg)
	{
		Logger.log = msg;
		System.out.println(log);
	}
	
	public static Logger getInstance()
	{
		if(instance==null)
		{
			instance = new Logger();
		}
		return instance;
	}
	
	
}
