package com.corvolution.mesana.configurator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Printer{
	
	/**
	 * @uml.property  name="printList"
	 */
	public List<String> printList ;
	/**
	 * @uml.property  name="hmap"
	 */
	public HashMap<Integer, String> hmap = new HashMap<Integer, String>();
	
	public void printLabel(){
		
	}
	
	public void setTemplate(){
		ConfigApp conf =  new ConfigApp();
		conf.getProperty("LABEL_TEMPLATE");
	}
	
	public void addParameter(){
		
	}

	public void print(){
		
	}
}


