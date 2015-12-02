package com.corvolution.mesana.configurator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public final class Printer
{
	private static Printer instance = null;
	
	public static Printer getInstance(){
		if(instance == null)
		{
			instance = new Printer();
		}		
		return instance;
	}
	
	public List<String> printList;

	public HashMap<String, String> hmap = new HashMap<String, String>();

	public void setTemplate(String labelTemplate)
	{

		String label;
	}

	// adding parameters to map e.g linkId and address
	public void addParameter(String key, String value)
	{
		hmap.put(key, value);
	}

	public void print()
	{
		String command;
		command = PropertyManager.getInstance().getProperty(Constants.LABEL_WRITER_BIN)
			    	+ " /objdata \"Address=" + hmap.get("Address").trim() + "\""
			+ " /objdata \"Link-ID=Link-ID " + hmap.get("LinkId")+ "\""
			+ " \"" + PropertyManager.getInstance().getProperty(Constants.LABEL_TEMPLATE) + "\"";
		
		Process pr;
		try
		{
			pr = Runtime.getRuntime().exec(command);
			int exitCode = pr.waitFor();
		}
		catch (IOException | InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}
}
