package com.corvolution.mesana.configurator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents printer manager for setting template, adding parameters as link id, address and executing
 * printing on a label printer.
 * 
 * @author Suleyman Gasimov
 *
 */
public final class Printer
{

	/** The instance of this singleton object. */
	private static Printer instance = null;

	/** The print list. */
	public List<String> printList;

	/** The hmap. */
	public HashMap<String, String> hmap = new HashMap<String, String>();

	/** The path name of the current label template.. */
	private String label;

	/**
	 * Instantiates a new printer.
	 */
	private Printer()
	{
	}

	/**
	 * This method creates instance of this class and returns it.
	 * 
	 * @return Printer - instance of this class
	 */
	public static Printer getInstance()
	{
		if (instance == null)
		{
			instance = new Printer();
		}
		return instance;
	}

	/**
	 * This method sets template path to a class field for sending command to printer.
	 * 
	 * @param labelTemplate
	 *            - path to template used by printer for printing.
	 */
	public void setTemplate(String labelTemplate)
	{
		label = labelTemplate;
	}

	/**
	 * This method adds keys and their values to HashMap for sending commands to printer.
	 * 
	 * @param key
	 *            - for example link id or address
	 * @param value
	 *            - value of a key
	 */
	public void addParameter(String key, String value)
	{
		hmap.put(key, value);
	}

	/**
	 * This method executes commands for printing. Must be called after necessary fields are initialized (template, link
	 * id or address)
	 */
	public void print()
	{
		String command;
		command = PropertyManager.getInstance().getProperty(Constants.LABEL_WRITER_BIN) + " /objdata \"Address="
				+ hmap.get("Address").trim() + "\"" + " /objdata \"Link-ID=Link-ID " + hmap.get("LinkId") + "\"" + " \""
				+ PropertyManager.getInstance().getProperty(Constants.LABEL_TEMPLATE) + "\"";

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
